package weblogic.jms.adapter51;

import javax.resource.ResourceException;
import javax.resource.spi.EISSystemException;
import weblogic.jms.JMSLogger;
import weblogic.jms.bridge.AdapterConnection;

public class LocalTransactionImpl {
   private JMSManagedConnection mc;

   public LocalTransactionImpl(JMSManagedConnection var1) {
      this.mc = var1;
   }

   public void begin() throws ResourceException {
      try {
         AdapterConnection var1 = this.mc.getJMSBaseConnection();
         ((JMSBaseConnection)var1).createTransactedSession();
         this.mc.sendEvent(2, (Exception)null);
      } catch (Exception var3) {
         JMSLogger.logStackTrace(var3);
         EISSystemException var2 = new EISSystemException("Failed to begin a local transaction");
         var2.setLinkedException(var3);
         throw var2;
      }
   }

   public void commit() throws ResourceException {
      AdapterConnection var1 = null;

      try {
         var1 = this.mc.getJMSBaseConnection();
         ((JMSBaseConnection)var1).commit();
         this.mc.sendEvent(3, (Exception)null);
      } catch (Exception var4) {
         EISSystemException var3 = new EISSystemException("Failed to commit a local transaction");
         var3.setLinkedException(var4);
         throw var3;
      }
   }

   public void rollback() throws ResourceException {
      AdapterConnection var1 = null;

      try {
         var1 = this.mc.getJMSBaseConnection();
         ((JMSBaseConnection)var1).rollback();
         this.mc.sendEvent(4, (Exception)null);
      } catch (Exception var4) {
         EISSystemException var3 = new EISSystemException("Failed to roll back a local transaction");
         var3.setLinkedException(var4);
         throw var3;
      }
   }
}
