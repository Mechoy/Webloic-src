package weblogic.transaction.internal;

import weblogic.management.ManagementException;
import weblogic.management.runtime.JTARuntimeMBean;
import weblogic.management.runtime.TransactionNameRuntimeMBean;

public final class TransactionNameRuntimeImpl extends JTATransactionStatisticsImpl implements TransactionNameRuntimeMBean {
   private String transactionName;

   public TransactionNameRuntimeImpl(String var1, long var2, JTARuntimeMBean var4) throws ManagementException {
      super("JTANamed_" + var2, var4);
      var4.addTransactionNameRuntimeMBean(this);
      this.transactionName = var1;
   }

   public String getTransactionName() {
      return this.transactionName;
   }

   public String toString() {
      return new String("{transactionName=" + this.transactionName + ", " + super.toString() + "}");
   }
}
