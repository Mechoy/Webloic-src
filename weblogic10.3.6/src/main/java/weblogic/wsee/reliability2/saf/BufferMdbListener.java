package weblogic.wsee.reliability2.saf;

import com.sun.xml.ws.addressing.WsaPropertyBag;
import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.WSEndpoint;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service.Mode;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.jaxws.client.async.AsyncTransportProvider;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;
import weblogic.wsee.util.Verbose;

public class BufferMdbListener implements MessageListener {
   private static final Logger LOGGER = Logger.getLogger(BufferMdbListener.class.getName());
   private WSEndpoint _endpoint;
   private TubelineSpliceFactory.DispatchFactory _dispatchFactory;
   private boolean _forResponse;

   public BufferMdbListener(WSEndpoint var1, TubelineSpliceFactory.DispatchFactory var2, boolean var3) {
      this._endpoint = var1;
      this._dispatchFactory = var2;
      this._forResponse = var3;
   }

   public void onMessage(Message var1) {
      try {
         if (!(var1 instanceof ObjectMessage)) {
            throw new WsrmException("Wrong message type, only allow object message");
         } else {
            Serializable var2 = ((ObjectMessage)var1).getObject();
            if (!(var2 instanceof PersistentMessage)) {
               throw new WsrmException("Wrong object message received, expected type: weblogic.wsee.jaxws.persistence.PersistentMessage");
            } else {
               final String var3 = var1.getStringProperty("ASYNC_URI");
               final PersistentMessage var4 = (PersistentMessage)var2;

               try {
                  int var5 = var1.getIntProperty("JMSXDeliveryCount");
                  var4.getContext().getPropertyMap().put("weblogic.wsee.buffer.BufferedMessageJmsDeliveryCount", var5);
               } catch (Exception var7) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     Verbose.logException(var7);
                  } else {
                     var7.printStackTrace();
                  }
               }

               AuthenticatedSubject var10 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               PrivilegedExceptionAction var6 = new PrivilegedExceptionAction<NullObject>() {
                  public NullObject run() throws Exception {
                     BufferMdbListener.this.deliver(var3, var4);
                     return null;
                  }
               };
               PersistentMessageFactory.getInstance().runActionInContext(var4.getContext(), var10, var6);
            }
         }
      } catch (RuntimeException var8) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, var8.toString(), var8);
         }

         throw var8;
      } catch (Throwable var9) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, var9.toString(), var9);
         }

         throw new RuntimeException(var9.toString(), var9);
      }
   }

   public void deliver(String var1, PersistentMessage var2) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Dispatching buffered message on endpoint with target URI: " + var1);
      }

      Packet var3 = WsrmTubeUtils.createPacketFromPersistentMessage(var2, this._endpoint.getBinding().getAddressingVersion(), this._endpoint.getBinding().getSOAPVersion());
      WsrmPropertyBag var4 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var3);
      DestinationMessageInfo var5 = var4.getDestMessageInfoFromRequest();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Beginning to process buffered message with msg id " + var5.getMessageId() + " seq " + var5.getSequenceId() + " and message number " + var5.getMessageNum());
      }

      DestinationSequence var6 = DestinationSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var5.getSequenceId(), false);
      var5 = (DestinationMessageInfo)var6.getRequestByMessageId(var5.getMessageId());
      var5.setInProcess();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Writing IN PROCESS destination message info for msg " + var5.getMessageId() + " seq " + var5.getSequenceId() + " and message number " + var5.getMessageNum());
      }

      DestinationSequenceManager.getInstance().updateSequence(var6);
      if (LOGGER.isLoggable(Level.FINE)) {
         String var7 = AsyncTransportProvider.dumpPersistentContextContextProps(var3.persistentContext);
         LOGGER.fine("Dispatching buffered " + (this._forResponse ? "*response* " : " ") + "message with action " + var3.soapAction + " msg number " + var5.getMessageNum() + " msg id " + var5.getMessageId() + " seq id " + var5.getSequenceId() + " persistentContext: " + var7);
      }

      var3.addSatellite(new WsaPropertyBag(var6.getAddressingVersion(), var6.getSoapVersion(), var3));
      WSEndpointReference var9;
      Dispatch var11;
      if (this._forResponse) {
         AddressingVersion var8 = this._endpoint.getBinding().getAddressingVersion();
         if (var8 == null) {
            var8 = AddressingVersion.W3C;
         }

         var9 = var8.anonymousEpr;
         var11 = this._dispatchFactory.createResponseDispatch(var9, Packet.class, Mode.MESSAGE);
      } else {
         var11 = this._dispatchFactory.createDispatch(Packet.class, Mode.MESSAGE);
      }

      var3.endpointAddress = new EndpointAddress(var6.getHostEpr().getAddress());
      var3.endpoint = this._endpoint;
      var11.getRequestContext().put("weblogic.wsee.jaxws.async.PersistentContext", var3.persistentContext);
      if (LOGGER.isLoggable(Level.FINE)) {
         String var12 = AsyncTransportProvider.dumpPersistentContextContextProps((Map)var11.getRequestContext().get("weblogic.wsee.jaxws.async.PersistentContext"));
         LOGGER.fine("Set RequestContext's persistent context for " + (this._forResponse ? "*response* " : " ") + "msg id " + var5.getMessageId() + " seq id " + var5.getSequenceId() + " persistentContext: " + var12);
      }

      Packet var13 = (Packet)var11.invoke(var3);
      if (!this._forResponse && var13 != null && var13.getMessage() != null) {
         var9 = var3.getMessage().getHeaders().getReplyTo(var6.getAddressingVersion(), var6.getSoapVersion());
         if (var9 != null && !var9.isAnonymous()) {
            if (LOGGER.isLoggable(Level.FINE)) {
               String var10 = (String)var13.get("com.sun.xml.ws.api.addressing.messageId");
               LOGGER.fine("Dispatching response to buffered " + (this._forResponse ? "*response* " : " ") + "msg id " + var5.getMessageId() + " response msg id " + var10);
            }

            Dispatch var14 = this._dispatchFactory.createResponseDispatch(var9, Packet.class, Mode.PAYLOAD);
            var14.invokeOneWay(var13);
         }
      }

   }

   public class NullObject {
   }
}
