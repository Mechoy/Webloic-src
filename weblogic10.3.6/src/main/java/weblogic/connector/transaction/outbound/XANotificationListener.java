package weblogic.connector.transaction.outbound;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import weblogic.connector.common.Debug;
import weblogic.connector.outbound.ConnectionHandler;
import weblogic.kernel.AuditableThreadLocal;
import weblogic.kernel.AuditableThreadLocalFactory;
import weblogic.kernel.ThreadLocalInitialValue;
import weblogic.transaction.BeginNotificationListener;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.TransactionHelper;

public final class XANotificationListener implements BeginNotificationListener {
   private static XANotificationListener xaNotifListenerSingleton = new XANotificationListener();
   private boolean jtaRegistered = false;
   private int registedCount = 0;
   private AuditableThreadLocal xaThreadLocal = AuditableThreadLocalFactory.createThreadLocal(new XANotificationThreadLocal());

   static final XANotificationListener getInstance() {
      return xaNotifListenerSingleton;
   }

   public void beginNotification(Object var1) throws NotSupportedException, SystemException {
      if (!this.getRegistedHandlers().isEmpty()) {
         String var2 = Debug.getExceptionXAStartInLocalTxIllegal();
         throw new NotSupportedException(var2);
      }
   }

   void registerNotification(ConnectionHandler var1) {
      synchronized(xaNotifListenerSingleton) {
         if (!this.jtaRegistered) {
            ((ServerTransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).registerBeginNotificationListener(xaNotifListenerSingleton, (Object)null);
            this.jtaRegistered = true;
         }

         ++this.registedCount;
      }

      this.getRegistedHandlers().add(var1);
   }

   void deregisterNotification(ConnectionHandler var1) {
      List var2 = this.getRegistedHandlers();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         if (var2.get(var3) == var1) {
            var2.remove(var3);
            synchronized(xaNotifListenerSingleton) {
               --this.registedCount;
               this.unregisterFromJTAIfNeeded();
               break;
            }
         }
      }

   }

   private void unregisterFromJTAIfNeeded() {
      if (this.jtaRegistered && this.registedCount <= 0) {
         ((ServerTransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).unregisterBeginNotificationListener(xaNotifListenerSingleton);
         this.jtaRegistered = false;
         this.registedCount = 0;
      }

   }

   private List<ConnectionHandler> getRegistedHandlers() {
      return (List)this.xaThreadLocal.get();
   }

   private class XANotificationThreadLocal extends ThreadLocalInitialValue {
      private XANotificationThreadLocal() {
      }

      protected final Object initialValue() {
         return new ArrayList();
      }

      protected final Object resetValue(Object var1) {
         ArrayList var2 = (ArrayList)var1;
         if (!var2.isEmpty()) {
            synchronized(XANotificationListener.xaNotifListenerSingleton) {
               XANotificationListener.this.registedCount = var2.size();
               XANotificationListener.this.unregisterFromJTAIfNeeded();
            }

            var2.clear();
         }

         return var2;
      }

      // $FF: synthetic method
      XANotificationThreadLocal(Object var2) {
         this();
      }
   }
}
