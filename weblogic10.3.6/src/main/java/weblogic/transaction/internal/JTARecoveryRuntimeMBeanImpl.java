package weblogic.transaction.internal;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JTARecoveryRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class JTARecoveryRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JTARecoveryRuntimeMBean, JTARecoveryRuntime {
   private TransactionRecoveryService trs;
   private int initialTotalCount = -1;
   private int finalCompletionCount = -1;
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public JTARecoveryRuntimeMBeanImpl(TransactionRecoveryService var1) throws ManagementException {
      super(var1.getServerName(), ManagementService.getRuntimeAccess(kernelID).getServerRuntime().getJTARuntime(), true, "RecoveryRuntimeMBeans");
      this.trs = var1;
   }

   public void reset(int var1) {
      this.initialTotalCount = var1;
      this.finalCompletionCount = -1;
   }

   public void setFinalTransactionCompletionCount(int var1) {
      this.finalCompletionCount = var1;
   }

   public boolean isActive() {
      return this.trs.isActive();
   }

   public int getInitialRecoveredTransactionTotalCount() {
      return this.initialTotalCount;
   }

   public int getRecoveredTransactionCompletionPercent() {
      int var1 = this.finalCompletionCount;
      if (var1 == -1) {
         var1 = getTM().getRecoveredTransactionCompletionCount(this.trs.getServerName());
      }

      if (var1 == -1) {
         var1 = this.finalCompletionCount;
      }

      if (var1 == -1) {
         return -1;
      } else {
         double var2 = (double)var1 / (double)this.initialTotalCount;
         return (int)(var2 * 100.0);
      }
   }

   private static ServerTransactionManagerImpl getTM() {
      return (ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager();
   }
}
