package weblogic.transaction.internal;

import java.beans.PropertyChangeListener;
import weblogic.management.configuration.JTAMBean;

class JTAConfigImpl implements JTAConfig {
   private JTAMBean jtaMBean;

   JTAConfigImpl(JTAMBean var1) {
      this.jtaMBean = var1;
   }

   public int getTimeoutSeconds() {
      return this.jtaMBean.getTimeoutSeconds();
   }

   public int getAbandonTimeoutSeconds() {
      return this.jtaMBean.getAbandonTimeoutSeconds();
   }

   public int getCompletionTimeoutSeconds() {
      return this.jtaMBean.getCompletionTimeoutSeconds();
   }

   public boolean isTwoPhaseEnabled() {
      return this.jtaMBean.isTwoPhaseEnabled();
   }

   public boolean getForgetHeuristics() {
      return this.jtaMBean.getForgetHeuristics();
   }

   public int getBeforeCompletionIterationLimit() {
      return this.jtaMBean.getBeforeCompletionIterationLimit();
   }

   public int getMaxTransactions() {
      return this.jtaMBean.getMaxTransactions();
   }

   public int getMaxUniqueNameStatistics() {
      return this.jtaMBean.getMaxUniqueNameStatistics();
   }

   public int getMaxResourceRequestsOnServer() {
      return this.jtaMBean.getMaxResourceRequestsOnServer();
   }

   public long getMaxXACallMillis() {
      return this.jtaMBean.getMaxXACallMillis();
   }

   public long getMaxResourceUnavailableMillis() {
      return this.jtaMBean.getMaxResourceUnavailableMillis();
   }

   public int getMigrationCheckpointIntervalSeconds() {
      return this.jtaMBean.getMigrationCheckpointIntervalSeconds();
   }

   public long getMaxTransactionsHealthIntervalMillis() {
      return this.jtaMBean.getMaxTransactionsHealthIntervalMillis();
   }

   public int getPurgeResourceFromCheckpointIntervalSeconds() {
      return this.jtaMBean.getPurgeResourceFromCheckpointIntervalSeconds();
   }

   public int getCheckpointIntervalSeconds() {
      return this.jtaMBean.getCheckpointIntervalSeconds();
   }

   public long getSerializeEnlistmentsGCIntervalMillis() {
      return this.jtaMBean.getSerializeEnlistmentsGCIntervalMillis();
   }

   public boolean isParallelXAEnabled() {
      return this.jtaMBean.getParallelXAEnabled();
   }

   public String getParallelXADispatchPolicy() {
      return this.jtaMBean.getParallelXADispatchPolicy();
   }

   public int getUnregisterResourceGracePeriod() {
      return this.jtaMBean.getUnregisterResourceGracePeriod();
   }

   public String getSecurityInteropMode() {
      return this.jtaMBean.getSecurityInteropMode();
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.jtaMBean.addPropertyChangeListener(var1);
   }

   public TransactionLogJDBCStoreConfig getTransactionLogJDBCStoreConfig() {
      return new TransactionLogJDBCStoreConfigImpl();
   }
}
