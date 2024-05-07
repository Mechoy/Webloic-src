package weblogic.connector.transaction.outbound;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.connector.common.Debug;
import weblogic.connector.outbound.ConnectionHandler;

final class TxCompletionNotification implements Synchronization {
   private ConnectionHandler connHandler;
   private boolean deregistered;

   private TxCompletionNotification(ConnectionHandler var1) {
      this.connHandler = var1;
      this.deregistered = false;
      this.debug("Registered object for transaction completion notification");
   }

   public static TxCompletionNotification register(Transaction var0, ConnectionHandler var1) throws SystemException, RollbackException {
      TxCompletionNotification var2 = new TxCompletionNotification(var1);
      var0.registerSynchronization(var2);
      return var2;
   }

   public void beforeCompletion() {
   }

   public void afterCompletion(int var1) {
      this.debug("Received afterCompletion notification, deregistered = " + this.deregistered);
      if (!this.deregistered) {
         ((TxConnectionHandler)((TxConnectionHandler)this.connHandler)).notifyConnPoolOfTransCompletion();
      }

   }

   public void deregister() {
      this.deregistered = true;
   }

   private void debug(String var1) {
      if (Debug.isXAoutEnabled()) {
         Debug.xaOut(this.connHandler.getPool(), "TransCompletionNotification ( " + this.toString() + " ) - " + var1);
      }

   }
}
