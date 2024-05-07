package weblogic.transaction.internal;

import weblogic.management.MBeanCreationException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JTAStatisticsRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public abstract class JTAStatisticsImpl extends RuntimeMBeanDelegate implements JTAStatisticsRuntimeMBean {
   public JTAStatisticsImpl(String var1, RuntimeMBean var2) throws MBeanCreationException, ManagementException {
      super(var1, var2);
   }

   public abstract long getTransactionTotalCount();

   public abstract long getTransactionCommittedTotalCount();

   public abstract long getTransactionRolledBackTotalCount();

   public abstract long getTransactionHeuristicsTotalCount();

   public String toString() {
      StringBuffer var1 = new StringBuffer(64);
      var1.append("{");
      var1.append("transactionTotalCount=").append(this.getTransactionTotalCount());
      var1.append(", ");
      var1.append("transactionCommittedTotalCount=").append(this.getTransactionCommittedTotalCount());
      var1.append(", ");
      var1.append("transactionRolledBackTotalCount=").append(this.getTransactionRolledBackTotalCount());
      var1.append(", ");
      var1.append("transactionHeuristicsTotalCount=").append(this.getTransactionHeuristicsTotalCount());
      var1.append("}");
      return var1.toString();
   }
}
