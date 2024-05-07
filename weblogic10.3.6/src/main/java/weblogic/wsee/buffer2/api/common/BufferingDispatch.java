package weblogic.wsee.buffer2.api.common;

import com.sun.xml.ws.addressing.WsaPropertyBag;
import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service.Mode;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.jaxws.client.async.AsyncTransportProvider;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.persistence.PersistentObject;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.UnknownDestinationSequenceException;

public abstract class BufferingDispatch {
   protected static final Logger LOGGER = Logger.getLogger(BufferingDispatch.class.getName());
   private WSEndpoint _endpoint;
   private TubelineSpliceFactory.DispatchFactory _dispatchFactory;

   public void setWSEndpoint(WSEndpoint var1) {
      this._endpoint = var1;
   }

   public void setDispatchFactory(TubelineSpliceFactory.DispatchFactory var1) {
      this._dispatchFactory = var1;
   }

   public void onMessage(Message var1, boolean var2, BufferingFeature var3) {
      try {
         if (!(var1 instanceof ObjectMessage)) {
            throw new BufferingException("Wrong message type, only allow object message");
         } else {
            Serializable var4 = ((ObjectMessage)var1).getObject();
            if (!(var4 instanceof PersistentMessage) && !(var4 instanceof PersistentObject)) {
               throw new BufferingException("Wrong object message received, expected type: weblogic.wsee.jaxws.persistence.PersistentMessage or weblogic.wsee.jaxws.persistence.PersistentObject: " + (var4 != null ? var4.getClass().getName() : null));
            } else {
               PersistentContext var9;
               if (var4 instanceof PersistentMessage) {
                  var9 = ((PersistentMessage)var4).getContext();
               } else {
                  var9 = ((PersistentObject)var4).getContext();
               }

               try {
                  int var6 = var1.getIntProperty("JMSXDeliveryCount");
                  var9.getPropertyMap().put("weblogic.wsee.buffer.BufferedMessageJmsDeliveryCount", var6);
               } catch (Exception var7) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.log(Level.FINE, var7.toString(), var7);
                  } else {
                     var7.printStackTrace();
                  }
               }

               this.executeDeliverPlatform(var4, var2, var3);
            }
         }
      } catch (Exception var8) {
         String var5 = "Error processing message off buffer queue for endpoint " + this._endpoint.getEndpointId() + ": " + var8.toString();
         if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, var5, var8);
         }

         WseeCoreLogger.logUnexpectedException(var5, var8);
         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         } else {
            throw new RuntimeException(var8.toString(), var8);
         }
      }
   }

   protected void deliver(PersistentMessage var1, boolean var2, BufferingFeature var3) {
      try {
         Packet var4 = new Packet();
         PersistentMessageFactory.getInstance().setMessageIntoPacket(var1, var4);
         PreDispatch var5 = this.getPreDispatch(var3.getBufferingFeatureUser(), var4);
         boolean var6 = var5.execute(var4, var2);
         AddressingVersion var7 = var5.getAddressingVersion();
         SOAPVersion var8 = var5.getSOAPVersion();
         if (var1 != null && var4.getMessage() != null) {
            String var9 = var4.getMessage().getHeaders().getMessageID(var7, var8);
            if (!var6) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** deliver(): bypassing the processing for message " + var9 + " after PreDispatch told us to.");
               }

            } else {
               Dispatch var10;
               if (var2) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("** deliver(): process Response with msgId " + var9);
                  }

                  AddressingVersion var11 = this._endpoint.getBinding().getAddressingVersion();
                  if (var11 == null) {
                     var11 = var5.getAddressingVersion();
                  }

                  WSEndpointReference var12 = var11.anonymousEpr;
                  var10 = this._dispatchFactory.createResponseDispatch(var12, Packet.class, Mode.MESSAGE);
               } else {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("** deliver(): process Request with msgId " + var9);
                  }

                  var10 = this._dispatchFactory.createDispatch(Packet.class, Mode.MESSAGE);
               }

               var4.endpoint = this._endpoint;
               var10.getRequestContext().put("weblogic.wsee.jaxws.async.PersistentContext", var4.persistentContext);
               if (LOGGER.isLoggable(Level.FINE)) {
                  String var14 = AsyncTransportProvider.dumpPersistentContextContextProps((Map)var10.getRequestContext().get("weblogic.wsee.jaxws.async.PersistentContext"));
                  LOGGER.fine("** deliver(): Set RequestContext's persistent context for msgID=" + var9 + " " + (var2 ? "*response* " : " ") + " persistentContext: " + var14);
               }

               Packet var15 = (Packet)var10.invoke(var4);
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** deliver(): completed dispatch.invoke for msgId=" + var9 + ".  ");
                  LOGGER.fine(var15 == null ? "** deliver(): response is NULL" : (var15.getMessage() == null ? "** deliver(): response.message is NULL" : "** deliver(): response.message is NOT NULL"));
               }

               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("** deliver(): deliver() complete for msgId=" + var9 + ".  returning ");
               }

            }
         } else {
            throw new IllegalArgumentException("Null message for preDispatch: " + var5);
         }
      } catch (Exception var13) {
         if (var13 instanceof RuntimeException) {
            throw (RuntimeException)var13;
         } else {
            throw new RuntimeException(var13);
         }
      }
   }

   public abstract String getMDBClassName();

   protected abstract void executeDeliverPlatform(Object var1, boolean var2, BufferingFeature var3);

   private PreDispatch getPreDispatch(BufferingFeature.BufferingFeatureUsers var1, Packet var2) throws Exception {
      return (PreDispatch)(var1.equals(BufferingFeature.BufferingFeatureUsers.WSRM) ? new PreDispatch_WSRM(var2, this._endpoint) : new PreDispatch_NONE());
   }

   protected static class NullObject {
   }

   static class PreDispatch_WSRM extends PreDispatch {
      private static final Logger LOGGER = Logger.getLogger(PreDispatch_WSRM.class.getName());
      private Packet _packet;
      private WSEndpoint _endpoint;
      private WsrmPropertyBag _rmProps;
      private DestinationMessageInfo _msgInfo;

      protected PreDispatch_WSRM(Packet var1, WSEndpoint var2) throws Exception {
         this._packet = var1;
         this._endpoint = var2;
         this._rmProps = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(this._packet);
         this._msgInfo = this._rmProps.getDestMessageInfoFromRequest();
         if (this._msgInfo == null) {
            String var3 = "Null MessageInfo in BufferingDispatch!!. Inbound msgId: " + this._rmProps.getInboundMessageId() + " Inbound endpointAddress: " + this._rmProps.getInboundEndpointAddress();
            if (LOGGER.isLoggable(Level.INFO)) {
               LOGGER.info(var3);
            }

            throw new IllegalStateException(var3);
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** BufferingDispatch$PreDispatch_WSRM.execute: Beginning to process buffered message with msg id " + this._msgInfo.getMessageId() + " seq " + this._msgInfo.getSequenceId() + " and message number " + this._msgInfo.getMessageNum());
            }

            this._rmProps.restorePersistentPropsIntoJaxWsRi(this.getAddressingVersion(), this.getSOAPVersion());
         }
      }

      boolean execute(Packet var1, boolean var2) throws Exception {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** BufferingDispatch_WSRM is processing message id " + this._msgInfo.getMessageId() + " from seq " + this._msgInfo.getSequenceId() + " and msg num " + this._msgInfo.getMessageNum());
         }

         DestinationSequence var3 = DestinationSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), this._msgInfo.getSequenceId(), true);
         DestinationMessageInfo var4 = var3 != null ? (DestinationMessageInfo)var3.getRequest(this._msgInfo.getMessageNum()) : null;
         boolean var5 = true;
         String var6 = "IN PROCESS";
         if (this._msgInfo.isEmptyLastMessage()) {
            var5 = false;
            var6 = "LastMessage NO_RESPONSE";
            this._msgInfo.setNoResponse();
            if (var4 != null) {
               var4.setNoResponse();
            }
         } else {
            this._msgInfo.setInProcess();
            if (var4 != null) {
               var4.setInProcess();
            }
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** BufferingDispatch$PreDispatch_WSRM.execute: Writing " + var6 + " destination message info for msg " + this._msgInfo.getMessageId() + " seq " + this._msgInfo.getSequenceId() + " and message number " + this._msgInfo.getMessageNum());
         }

         if (var3 != null) {
            try {
               DestinationSequenceManager.getInstance().updateSequence(var3);
            } catch (UnknownDestinationSequenceException var8) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Destination sequence " + var3.getId() + " was terminated before we could register NoResponse/InProcess on incoming request msg " + this._msgInfo.getMessageId() + " msg num " + this._msgInfo.getMessageNum());
               }
            }
         }

         var1.addSatellite(new WsaPropertyBag(this.getAddressingVersion(), this.getSOAPVersion(), var1));
         var1.endpointAddress = new EndpointAddress(this._rmProps.getInboundEndpointAddress());
         return var5;
      }

      AddressingVersion getAddressingVersion() {
         AddressingVersion var1 = this._endpoint.getBinding().getAddressingVersion();
         if (var1 == null) {
            var1 = AddressingVersion.W3C;
         }

         return var1;
      }

      SOAPVersion getSOAPVersion() {
         return this._endpoint.getBinding().getSOAPVersion();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString());
         var1.append(" msgId=").append(this._msgInfo.getMessageId());
         var1.append(" msgInfo=").append(this._msgInfo);
         return var1.toString();
      }
   }

   static class PreDispatch_NONE extends PreDispatch {
      boolean execute(Packet var1, boolean var2) throws Exception {
         return true;
      }

      AddressingVersion getAddressingVersion() {
         return null;
      }

      SOAPVersion getSOAPVersion() {
         return null;
      }
   }

   abstract static class PreDispatch {
      abstract boolean execute(Packet var1, boolean var2) throws Exception;

      abstract AddressingVersion getAddressingVersion();

      abstract SOAPVersion getSOAPVersion();
   }
}
