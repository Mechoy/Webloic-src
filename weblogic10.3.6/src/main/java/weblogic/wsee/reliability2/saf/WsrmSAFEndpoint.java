package weblogic.wsee.reliability2.saf;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFEndpoint;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.utils.BufferingConstants;

public final class WsrmSAFEndpoint implements SAFEndpoint {
   private static final Logger LOGGER = Logger.getLogger(WsrmSAFEndpoint.class.getName());
   private static boolean USE_NEW_BUFFERING_FEATURE = true;
   private String _targetURI;

   public WsrmSAFEndpoint(String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Created WsrmSAFEndpoint for targetURI: " + var1);
      }

      this._targetURI = var1;
   }

   public void deliver(SAFConversationInfo var1, SAFRequest var2) throws SAFException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("[WsrmSAFEndpoint.deliver()] sequence id " + var1.getConversationName() + ": " + var2.getSequenceNumber() + " for targetURI: " + this._targetURI);
      }

      try {
         Serializable var3 = SequenceSAFMap.getPayloadFromSAFRequest(var2);
         int var4 = 3;
         long var5 = 10L;
         if (USE_NEW_BUFFERING_FEATURE) {
            BufferingConstants.MsgDirection var7 = BufferingConstants.MsgDirection.REQUEST;
            BufferingFeature var8 = BufferingFeature.getBufferingFeature(this._targetURI);
            if (var8 != null) {
               var4 = var8.getDeployedRetryCount();
               String var9 = var8.getDeployedRetryDelay();
               if (var9 != null && var9.length() > 0) {
                  try {
                     var5 = Long.parseLong(var9);
                  } catch (Exception var13) {
                     var5 = var5;
                  }
               }

               var7 = var8.getDirection();
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("WsrmSAFEndpoint buffering message with request ID " + var2.getMessageId() + " to targetURI = " + this._targetURI + " direction " + var7 + " seq " + var1.getConversationName());
            }

            BufferingFeature.getBufferingManager().bufferMessage(this._targetURI, var3, var7, var4, var5, var1.getConversationName(), var2.getSequenceNumber());
         } else {
            BufferManager.instance().bufferMessageUOO(this._targetURI, var3, var4, var5, var1.getConversationName(), var2.getSequenceNumber());
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("WsrmSAFEndpoint done buffering msg id " + var2.getMessageId() + " requestSeqNum " + var2.getSequenceNumber() + " on destination sequence " + var1.getConversationName() + ". Will now proceed to ack this message back to the sender.");
         }

      } catch (Throwable var14) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, var14.toString(), var14);
         }

         throw new SAFException(var14.toString(), var14);
      }
   }

   public String getTargetQueue() {
      String var1;
      if (USE_NEW_BUFFERING_FEATURE) {
         try {
            var1 = BufferingFeature.getQueueJndiName(this._targetURI);
         } catch (BufferingException var3) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.log(Level.WARNING, "Couldn't find buffering request queue JNDI name: " + var3.toString(), var3);
            }

            WseeRmLogger.logUnexpectedException(var3.toString(), var3);
            var1 = null;
         }
      } else {
         var1 = BufferManager.instance().getTargetQueue(this._targetURI).getQueueName();
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.log(Level.FINE, "WsrmSAFEndpoint.getTargetQueue returning targetQueueName for targetURI='" + this._targetURI + "', queueName is '" + var1 + "'");
      }

      return var1;
   }

   public boolean isAvailable() {
      return true;
   }
}
