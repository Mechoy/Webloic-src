package weblogic.transaction.internal;

import javax.transaction.xa.XAException;
import weblogic.health.HealthState;
import weblogic.management.MBeanCreationException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.TransactionResourceRuntimeMBean;

public final class TransactionResourceRuntimeImpl extends JTAStatisticsImpl implements TransactionResourceRuntimeMBean, TransactionResourceRuntime {
   private static final long serialVersionUID = -2736453126861335594L;
   private JTARuntimeImpl jtaRuntime;
   private String resourceName;
   protected long transactionCommittedTotalCount;
   protected long transactionRolledBackTotalCount;
   protected long transactionHeuristicCommitTotalCount;
   protected long transactionHeuristicRollbackTotalCount;
   protected long transactionHeuristicMixedTotalCount;
   protected long transactionHeuristicHazardTotalCount;
   private boolean healthy;
   private HealthState healthState;

   public TransactionResourceRuntimeImpl(String var1, JTARuntimeImpl var2) throws MBeanCreationException, ManagementException {
      super(var1, var2);
      var2.addTransactionResourceRuntimeMBean(this);
      this.jtaRuntime = var2;
      this.resourceName = var1;
      this.healthy = true;
      this.healthState = new HealthState(0);
   }

   public String getResourceName() {
      return this.resourceName;
   }

   public HealthState getHealthState() {
      return this.healthState;
   }

   public void tallyCompletion(ServerResourceInfo var1, XAException var2) {
      if (var2 != null) {
         switch (var2.errorCode) {
            case 5:
               this.tallyHeuristicMixedCompletion();
               break;
            case 6:
               this.tallyHeuristicRollbackCompletion();
               break;
            case 7:
               this.tallyHeuristicCommitCompletion();
               break;
            case 8:
               this.tallyHeuristicHazardCompletion();
         }
      }

      if (var1.isCommitted()) {
         ++this.transactionCommittedTotalCount;
      } else if (var1.isRolledBack()) {
         ++this.transactionRolledBackTotalCount;
      }

   }

   public void setHealthy(boolean var1) {
      if (this.healthy != var1) {
         HealthState var2 = this.healthState;
         String[] var3 = new String[1];
         this.healthy = var1;
         HealthEvent var4 = null;
         if (!var1) {
            var3[0] = "Resource " + this.resourceName + " declared unhealthy";
            this.healthState = new HealthState(3, var3);
            var4 = new HealthEvent(5, this.resourceName, var3[0]);
         } else {
            var3[0] = "Resource " + this.resourceName + " declared healthy";
            this.healthState = new HealthState(0, var3);
            var4 = new HealthEvent(6, this.resourceName, var3[0]);
         }

         this.jtaRuntime.healthEvent(var4);
      }
   }

   public void tallyHeuristicCommitCompletion() {
      ++this.transactionHeuristicCommitTotalCount;
   }

   public void tallyHeuristicRollbackCompletion() {
      ++this.transactionHeuristicRollbackTotalCount;
   }

   public void tallyHeuristicMixedCompletion() {
      ++this.transactionHeuristicMixedTotalCount;
   }

   public void tallyHeuristicHazardCompletion() {
      ++this.transactionHeuristicHazardTotalCount;
   }

   public long getTransactionTotalCount() {
      return this.getTransactionCommittedTotalCount() + this.getTransactionRolledBackTotalCount();
   }

   public long getTransactionCommittedTotalCount() {
      return this.transactionCommittedTotalCount;
   }

   public long getTransactionRolledBackTotalCount() {
      return this.transactionRolledBackTotalCount;
   }

   public long getTransactionHeuristicsTotalCount() {
      return this.getTransactionHeuristicCommitTotalCount() + this.getTransactionHeuristicRollbackTotalCount() + this.getTransactionHeuristicMixedTotalCount() + this.getTransactionHeuristicHazardTotalCount();
   }

   public long getTransactionHeuristicCommitTotalCount() {
      return this.transactionHeuristicCommitTotalCount;
   }

   public long getTransactionHeuristicRollbackTotalCount() {
      return this.transactionHeuristicRollbackTotalCount;
   }

   public long getTransactionHeuristicMixedTotalCount() {
      return this.transactionHeuristicMixedTotalCount;
   }

   public long getTransactionHeuristicHazardTotalCount() {
      return this.transactionHeuristicHazardTotalCount;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(128);
      var1.append("{");
      var1.append("resourceName=").append(this.resourceName);
      var1.append(",");
      var1.append("transactionHeuristicCommitTotalCount=").append(this.getTransactionHeuristicCommitTotalCount());
      var1.append(",");
      var1.append("transactionHeuristicRollbackTotalCount=").append(this.getTransactionHeuristicRollbackTotalCount());
      var1.append(",");
      var1.append("transactionHeuristicMixedTotalCount=").append(this.getTransactionHeuristicMixedTotalCount());
      var1.append(",");
      var1.append("transactionHeuristicHazardTotalCount=").append(this.getTransactionHeuristicHazardTotalCount());
      var1.append(",");
      var1.append(super.toString());
      var1.append("}");
      return var1.toString();
   }
}
