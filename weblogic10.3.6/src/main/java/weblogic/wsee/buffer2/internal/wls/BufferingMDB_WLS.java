package weblogic.wsee.buffer2.internal.wls;

import java.util.logging.Level;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import weblogic.ejb.spi.JmsMessageDrivenBean;
import weblogic.jms.extensions.WLSession;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.buffer2.internal.common.BufferingMDB;

public final class BufferingMDB_WLS extends BufferingMDB implements JmsMessageDrivenBean {
   private volatile WLSession session = null;

   protected boolean retrySupported() {
      return true;
   }

   public void setSession(Session var1) {
      this.session = (WLSession)var1;
   }

   protected void setRetryDelay(Message var1, String var2, long var3) {
      try {
         long var5 = BufferManager.instance().getRetryDelay(var2, var1.getJMSMessageID());
         if (var3 > 0L) {
            if (var3 != var5) {
               var5 = var3;
               BufferManager.instance().putRetryDelay(var2, var1.getJMSMessageID(), var3);
            }
         } else if (var1.propertyExists("BEARetryDelay")) {
            var5 = var1.getLongProperty("BEARetryDelay");
            BufferManager.instance().putRetryDelay(var2, var1.getJMSMessageID(), var5);
         }

         if (var5 > 0L) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Setting retry delay to " + var5);
               LOGGER.fine("Old retryDelay=" + this.session.getRedeliveryDelay());
            }

            this.session.setRedeliveryDelay(var5 * 1000L);
         }
      } catch (JMSException var7) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Could not set retry delay");
            LOGGER.log(Level.FINE, var7.toString(), var7);
         }
      }

   }
}
