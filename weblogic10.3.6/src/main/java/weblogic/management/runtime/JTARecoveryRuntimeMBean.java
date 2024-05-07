package weblogic.management.runtime;

public interface JTARecoveryRuntimeMBean extends RuntimeMBean {
   boolean isActive();

   int getInitialRecoveredTransactionTotalCount();

   int getRecoveredTransactionCompletionPercent();
}
