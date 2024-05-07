package weblogic.ejb.container.monitoring;

import java.util.concurrent.atomic.AtomicLong;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.EJBTransactionRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class EJBTransactionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements EJBTransactionRuntimeMBean {
   private static final long serialVersionUID = 2146981779811962481L;
   private AtomicLong txCommitted = new AtomicLong(0L);
   private AtomicLong txAborted = new AtomicLong(0L);
   private AtomicLong txTimedOut = new AtomicLong(0L);

   public EJBTransactionRuntimeMBeanImpl(String var1, EJBRuntimeMBean var2) throws ManagementException {
      super(var1, var2, true, "TransactionRuntime");
   }

   public long getTransactionsCommittedTotalCount() {
      return this.txCommitted.get();
   }

   public void incrementTransactionsCommitted() {
      this.txCommitted.incrementAndGet();
   }

   public long getTransactionsRolledBackTotalCount() {
      return this.txAborted.get();
   }

   public void incrementTransactionsRolledBack() {
      this.txAborted.incrementAndGet();
   }

   public long getTransactionsTimedOutTotalCount() {
      return this.txTimedOut.get();
   }

   public void incrementTransactionsTimedOut() {
      this.txTimedOut.incrementAndGet();
   }
}
