package weblogic.wsee.reliability2.saf;

import com.sun.xml.ws.api.message.Packet;
import java.io.Externalizable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLHandshakeException;
import weblogic.messaging.saf.SAFConversationHandle;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFErrorAwareTransport;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.SAFResult;
import weblogic.messaging.saf.SAFTransportException;
import weblogic.messaging.saf.common.SAFConversationHandleImpl;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmPermanentTransportException;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;
import weblogic.wsee.reliability2.sequence.DestinationOfferSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.util.AccessException;

public final class WsrmSAFTransport implements SAFErrorAwareTransport {
   private static final Logger LOGGER = Logger.getLogger(WsrmSAFTransport.class.getName());
   private int type = 3;
   private static Set<String> _deliveredMsgIds = new HashSet();

   public void sendResult(SAFResult var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Received SAFResult successful=" + var1.isSuccessful() + " duplicate=" + var1.isDuplicate() + " error=" + var1.getSAFException() + " sequence=" + var1.getConversationInfo().getConversationName());
      }

      if (var1.isSuccessful() && !var1.isDuplicate()) {
         DestinationSequence var2 = null;

         try {
            var2 = DestinationSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var1.getConversationInfo().getConversationName(), false);
            List var3 = var1.getSequenceNumbers();

            for(int var4 = 0; var4 < var3.size(); var4 += 2) {
               long var5 = (Long)var3.get(var4);
               long var7;
               if (var4 + 1 < var3.size()) {
                  var7 = (Long)var3.get(var4 + 1);
               } else {
                  var7 = var5;
               }

               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("SAFResult indicated message range lower=" + var5 + " upper=" + var7 + ". We're now marking all the indicated requests as 'accepted'.");
               }

               for(long var9 = var5; var9 <= var7; ++var9) {
                  DestinationMessageInfo var11 = (DestinationMessageInfo)var2.getRequest(var9);
                  DestinationSequenceManager.getInstance().markRequestAccepted(var2, var11);
               }
            }

            if (var2 instanceof DestinationOfferSequence) {
               WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_IN_MSG_AFTER_BUFFERING);
            } else {
               WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_IN_MSG_AFTER_BUFFERING);
            }
         } catch (Exception var20) {
            WseeRmLogger.logUnexpectedException(var20.toString(), var20);
         } finally {
            try {
               if (var2 != null) {
                  DestinationSequenceManager.getInstance().updateSequence(var2);
               }
            } catch (Exception var19) {
               WseeRmLogger.logUnexpectedException(var19.toString(), var19);
            }

         }
      }

   }

   public int getType() {
      return this.type;
   }

   public boolean isGapsAllowed() {
      return false;
   }

   public Externalizable send(SAFConversationInfo var1, SAFRequest var2) throws SAFTransportException {
      return null;
   }

   private void updatePacket(SourceSequence var1, WsrmPropertyBag var2, Packet var3) throws WsrmException {
      if (var2.getOutboundMsgNeedsDestSeqId()) {
         SequenceHeader var4 = (SequenceHeader)WsrmHeaderFactory.getInstance().getHeaderFromPacket(SequenceHeader.class, var3);
         var4.setSequenceId(var1.getDestinationId());
         var2.setOutboundMsgNeedsDestSeqId(false);
         var3.getMessage().getHeaders().addOrReplace(var4);
      }

   }

   public SAFConversationHandle createConversation(SAFConversationInfo var1) throws SAFTransportException {
      return null;
   }

   private SAFConversationHandle finishOfferSequenceCreation(SourceOfferSequence var1, SAFConversationInfo var2) {
      var1.setDestinationId(var1.getId());
      SAFConversationHandleImpl var3 = new SAFConversationHandleImpl(var1.getId(), var1.getDestinationId(), var2.getTimeToLive(), var2.getMaximumIdleTime(), (SAFConversationInfo)null, (String)null, var2.getContext());
      return var3;
   }

   public void terminateConversation(SAFConversationInfo var1) throws SAFTransportException {
      assert var1 != null;

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Sending SAF-generated terminateConversation on conversation: " + var1.getConversationName());
      }

      try {
         SourceSequenceManager.getInstance().terminateSequence(var1.getConversationName());
      } catch (Exception var3) {
         throw new SAFTransportException(var3.toString(), var3);
      }
   }

   public Externalizable createSecurityToken(SAFConversationInfo var1) throws SAFTransportException {
      return null;
   }

   public boolean isPermanentError(Throwable var1) {
      boolean var2 = false;

      for(Throwable var3 = var1; var3 != null && !var2; var3 = var3.getCause()) {
         var2 = var3 instanceof WsrmPermanentTransportException || var3 instanceof AccessException || var3 instanceof SSLHandshakeException || var3 instanceof SequenceFaultException || var3 instanceof IllegalStateException || var3 instanceof IllegalArgumentException;
      }

      return var2;
   }
}
