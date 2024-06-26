package weblogic.jms.safclient.transaction.jta;

import javax.transaction.UserTransaction;
import weblogic.transaction.ClientTransactionManager;
import weblogic.transaction.TransactionHelper;

public class SimpleTransactionHelper extends TransactionHelper {
   private SimpleTransactionManager tm;

   public SimpleTransactionHelper(SimpleTransactionManager var1) {
      this.tm = var1;
   }

   public UserTransaction getUserTransaction() {
      return this.tm;
   }

   public ClientTransactionManager getTransactionManager() {
      return this.tm;
   }
}
