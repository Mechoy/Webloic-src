package weblogic.wsee.buffer2.internal.common;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.CreateException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.xml.rpc.JAXRPCException;
import weblogic.wsee.buffer.NoRetryException;
import weblogic.wsee.buffer2.api.common.BufferingDispatch;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.buffer2.utils.BufferingConstants;
import weblogic.wsee.jws.RetryException;
import weblogic.wsee.jws.container.Duration;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.ws.WsException;

public abstract class BufferingMDB implements MessageListener {
   protected static final Logger LOGGER = Logger.getLogger(BufferingMDB.class.getName());

   public void ejbRemove() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("BufferingMDB removed");
      }

   }

   public void ejbCreate() throws CreateException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("BufferingMDB created");
      }

   }

   public void onMessage(Message var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("onMessage");
      }

      boolean var2 = true;
      long var3 = 0L;
      String var5 = null;

      try {
         try {
            var5 = var1.getStringProperty(BufferingConstants.TARGET_URI);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("BufferingMDB.onMessage: Deliver message to " + var5);
            }

            boolean var6 = true;
            String var17 = var1.getStringProperty(BufferingConstants.DIRECTION);
            if (var17.equals(BufferingConstants.MsgDirection.REQUEST.toString())) {
               var6 = false;
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("BufferingMDB.onMessage: isResponse='" + var6 + "'");
            }

            if (var6) {
               WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.CLIENT_IN_MSG_UPON_DEBUFFER);
            } else {
               WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.SERV_IN_MSG_UPON_DEBUFFER);
            }

            BufferingFeature var18 = BufferingFeature.getBufferingFeature(var5);
            BufferingDispatch var19 = BufferingFeature.getBufferDispatch(var5);
            var19.onMessage(var1, var6, var18);
            return;
         } catch (NoRetryException var14) {
            assert var14.getCause() != null;

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, var14.toString(), var14);
            }

            var2 = false;
            if (var14.getCause() instanceof JAXRPCException) {
               throw (JAXRPCException)var14.getCause();
            }

            throw new JAXRPCException(var14.getCause());
         } catch (Exception var15) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, var15.toString(), var15);
            }
         }

         if (var15 instanceof JAXRPCException) {
            Throwable var7 = ((JAXRPCException)var15).getLinkedCause();
            if (var7 instanceof WsException && var7.getCause() instanceof RetryException) {
               if (!this.retrySupported()) {
                  throw new RuntimeException("Got RetryException when JMS retries is not supported. " + var15.getMessage());
               }

               RetryException var8 = (RetryException)var7.getCause();
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Reset the retry delay from user-defined RetryException:" + var8.getRetryDelay());
               }

               Duration var9 = new Duration(var8.getRetryDelay());
               var3 = var9.convertToSeconds(new Date());
               return;
            }
         }

         if (var15 instanceof RuntimeException) {
            throw (RuntimeException)var15;
         } else {
            throw new RuntimeException(var15);
         }
      } finally {
         if (this.retrySupported() && var2) {
            this.setRetryDelay(var1, var5, var3);
         }

      }
   }

   protected abstract void setRetryDelay(Message var1, String var2, long var3);

   public abstract void setSession(Session var1);

   protected abstract boolean retrySupported();
}
