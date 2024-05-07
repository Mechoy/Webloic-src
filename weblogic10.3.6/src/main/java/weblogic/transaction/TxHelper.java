package weblogic.transaction;

import javax.naming.Context;
import javax.transaction.UserTransaction;
import javax.transaction.xa.Xid;
import weblogic.kernel.Kernel;
import weblogic.transaction.internal.TransactionManagerImpl;

public class TxHelper extends ClientTxHelper {
   /** @deprecated */
   public static Transaction getTransaction() {
      return (Transaction)TransactionHelper.getTransactionHelper().getTransaction();
   }

   /** @deprecated */
   public static UserTransaction getUserTransaction() {
      return TransactionHelper.getTransactionHelper().getUserTransaction();
   }

   /** @deprecated */
   public static ClientTransactionManager getClientTransactionManager() {
      return TransactionHelper.getTransactionHelper().getTransactionManager();
   }

   public static String getTransactionId() {
      if (TransactionManagerImpl.isInitialized()) {
         Transaction var0 = getTransaction();
         if (var0 != null) {
            Xid var1 = var0.getXID();
            if (var1 != null) {
               return var1.toString();
            }
         }
      }

      return null;
   }

   public static InterposedTransactionManager getServerInterposedTransactionManager() {
      return Kernel.isServer() ? TransactionManagerImpl.getTransactionManager() : null;
   }

   public static InterposedTransactionManager getClientInterposedTransactionManager(Context var0, String var1) {
      try {
         return (InterposedTransactionManager)var0.lookup("weblogic.transaction.coordinators." + var1);
      } catch (Exception var3) {
         return null;
      }
   }

   public static String status2String(int var0) {
      switch (var0) {
         case 0:
            return "Active";
         case 1:
            return "Marked Rollback";
         case 2:
            return "Prepared";
         case 3:
            return "Committed";
         case 4:
            return "Rolledback";
         case 5:
            return "Unknown";
         case 6:
            return "No Transaction";
         case 7:
            return "Preparing";
         case 8:
            return "Committing";
         case 9:
            return "Rolling Back";
         default:
            return "Unknown";
      }
   }

   public static Xid createXid(int var0, byte[] var1, byte[] var2) {
      return XIDFactory.createXID(var0, var1, var2);
   }

   public static Xid createXid(byte[] var0, byte[] var1) {
      return XIDFactory.createXID(var0, var1);
   }

   public static String xidToString(Xid var0, boolean var1) {
      return XIDFactory.xidToString(var0, var1);
   }
}
