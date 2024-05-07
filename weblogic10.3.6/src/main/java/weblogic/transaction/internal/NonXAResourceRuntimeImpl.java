package weblogic.transaction.internal;

import java.security.AccessController;
import weblogic.management.MBeanCreationException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.NonXAResourceRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.nonxa.NonXAException;

public final class NonXAResourceRuntimeImpl extends JTAStatisticsImpl implements NonXAResourceRuntimeMBean, NonXAResourceRuntime {
   private String nonXAResourceName;
   long transactionCommittedTotalCount;
   long transactionRolledBackTotalCount;
   long transactionHeuristicsTotalCount;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public NonXAResourceRuntimeImpl(String var1, JTARuntimeImpl var2) throws MBeanCreationException, ManagementException {
      super(var1, var2);
      var2.addNonXAResourceRuntimeMBean(this);
      this.nonXAResourceName = var1;
   }

   public String getNonXAResourceName() {
      return this.nonXAResourceName;
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
      return 0L;
   }

   public void tallyCompletion(ServerResourceInfo var1, NonXAException var2) {
      if (var1.isCommitted()) {
         ++this.transactionCommittedTotalCount;
      } else if (var1.isRolledBack()) {
         ++this.transactionRolledBackTotalCount;
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(128);
      var1.append("NonXAResourceRuntimeMBean {");
      var1.append("nonXAResourceName=").append(this.nonXAResourceName);
      var1.append(",");
      var1.append(super.toString());
      var1.append("}");
      return var1.toString();
   }
}
