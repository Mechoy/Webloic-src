package weblogic.transaction.internal;

import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import weblogic.management.MBeanCreationException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JTATransactionStatisticsRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.transaction.Transaction;

public abstract class JTATransactionStatisticsImpl extends JTAStatisticsImpl implements Constants, JTATransactionStatisticsRuntimeMBean {
   private static final long serialVersionUID = 7547754151703772920L;
   protected long transactionCommittedTotalCount;
   protected long transactionNoResourcesCommittedTotalCount;
   protected long transactionOneResourceOnePhaseCommittedTotalCount;
   protected long transactionReadOnlyOnePhaseCommittedTotalCount;
   protected long transactionTwoPhaseCommittedTotalCount;
   protected long transactionLLRCommittedTotalCount;
   protected long transactionRolledBackTimeoutTotalCount;
   protected long transactionRolledBackResourceTotalCount;
   protected long transactionRolledBackAppTotalCount;
   protected long transactionRolledBackSystemTotalCount;
   protected long transactionAbandonedTotalCount;
   protected long transactionHeuristicsTotalCount;
   protected long millisecondsActiveTotalCount;

   public JTATransactionStatisticsImpl(String var1, RuntimeMBean var2) throws MBeanCreationException, ManagementException {
      super(var1, var2);
   }

   void tallyRollbackReason(Throwable var1) {
      if (var1 == null) {
         ++this.transactionRolledBackAppTotalCount;
      } else if (var1 instanceof XAException) {
         ++this.transactionRolledBackResourceTotalCount;
      } else if (var1 instanceof TimedOutException) {
         ++this.transactionRolledBackTimeoutTotalCount;
      } else if (var1 instanceof SystemException) {
         ++this.transactionRolledBackSystemTotalCount;
      } else if (var1 instanceof AppSetRollbackOnlyException) {
         ++this.transactionRolledBackAppTotalCount;
      } else {
         ++this.transactionRolledBackAppTotalCount;
      }

   }

   void tallyCompletion(Transaction var1) {
      TransactionImpl var2 = (TransactionImpl)var1;
      if (var2.getHeuristicErrorMessage() != null) {
         ++this.transactionHeuristicsTotalCount;
      }

      try {
         if (var2.isAbandoned()) {
            ++this.transactionAbandonedTotalCount;
            return;
         }

         switch (var1.getStatus()) {
            case 3:
               ++this.transactionCommittedTotalCount;
               this.tallySecondsActive(var2);
               this.tallyCommitType(var2);
               break;
            case 4:
            case 9:
               Throwable var3 = var2.getRollbackReason();
               this.tallyRollbackReason(var3);
         }
      } catch (SystemException var4) {
      }

   }

   public long getTransactionTotalCount() {
      return this.getTransactionCommittedTotalCount() + this.getTransactionRolledBackTotalCount() + this.getTransactionAbandonedTotalCount();
   }

   public long getTransactionCommittedTotalCount() {
      return this.transactionCommittedTotalCount;
   }

   public long getTransactionNoResourcesCommittedTotalCount() {
      return this.transactionNoResourcesCommittedTotalCount;
   }

   public long getTransactionOneResourceOnePhaseCommittedTotalCount() {
      return this.transactionOneResourceOnePhaseCommittedTotalCount;
   }

   public long getTransactionReadOnlyOnePhaseCommittedTotalCount() {
      return this.transactionReadOnlyOnePhaseCommittedTotalCount;
   }

   public long getTransactionTwoPhaseCommittedTotalCount() {
      return this.transactionTwoPhaseCommittedTotalCount;
   }

   public long getTransactionLLRCommittedTotalCount() {
      return this.transactionLLRCommittedTotalCount;
   }

   public long getTransactionRolledBackTotalCount() {
      return this.getTransactionRolledBackAppTotalCount() + this.getTransactionRolledBackResourceTotalCount() + this.getTransactionRolledBackTimeoutTotalCount() + this.getTransactionRolledBackSystemTotalCount();
   }

   public long getTransactionRolledBackTimeoutTotalCount() {
      return this.transactionRolledBackTimeoutTotalCount;
   }

   public long getTransactionRolledBackResourceTotalCount() {
      return this.transactionRolledBackResourceTotalCount;
   }

   public long getTransactionRolledBackAppTotalCount() {
      return this.transactionRolledBackAppTotalCount;
   }

   public long getTransactionRolledBackSystemTotalCount() {
      return this.transactionRolledBackSystemTotalCount;
   }

   public long getTransactionAbandonedTotalCount() {
      return this.transactionAbandonedTotalCount;
   }

   public long getTransactionHeuristicsTotalCount() {
      return this.transactionHeuristicsTotalCount;
   }

   public long getSecondsActiveTotalCount() {
      return this.millisecondsActiveTotalCount / 1000L;
   }

   protected void tallySecondsActive(Transaction var1) {
      try {
         if (var1.getStatus() != 3) {
            return;
         }
      } catch (SystemException var3) {
         return;
      }

      this.millisecondsActiveTotalCount += var1.getMillisSinceBegin();
   }

   protected void tallyCommitType(TransactionImpl var1) {
      if (var1.isNoResourceInTx) {
         ++this.transactionNoResourcesCommittedTotalCount;
      } else if (var1.onePhase) {
         if (var1.isOnlyOneResourceInTx) {
            ++this.transactionOneResourceOnePhaseCommittedTotalCount;
         } else {
            ++this.transactionReadOnlyOnePhaseCommittedTotalCount;
         }
      } else if (var1.isLLR) {
         ++this.transactionLLRCommittedTotalCount;
      } else {
         ++this.transactionTwoPhaseCommittedTotalCount;
      }

   }

   public int getAverageCommitTimeSeconds() {
      return this.transactionCommittedTotalCount == 0L ? 0 : (int)(this.millisecondsActiveTotalCount / this.transactionCommittedTotalCount);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(64);
      var1.append("{");
      var1.append(super.toString());
      var1.append(",");
      var1.append("transactionRolledBackTimeoutTotalCount=").append(this.getTransactionRolledBackTimeoutTotalCount());
      var1.append(", ");
      var1.append("transactionRolledBackResourceTotalCount=").append(this.getTransactionRolledBackResourceTotalCount());
      var1.append(", ");
      var1.append("transactionRolledBackAppTotalCount=").append(this.getTransactionRolledBackAppTotalCount());
      var1.append(", ");
      var1.append("transactionRolledBackSystemTotalCount=").append(this.getTransactionRolledBackSystemTotalCount());
      var1.append(", ");
      var1.append("transactionAbandonedTotalCount=").append(this.getTransactionAbandonedTotalCount());
      var1.append(", ");
      var1.append("millisecondsActiveTotalCount=").append(this.millisecondsActiveTotalCount);
      var1.append(", ");
      var1.append("averageCommitTimeSeconds=").append(this.getAverageCommitTimeSeconds());
      var1.append("}");
      return var1.toString();
   }
}
