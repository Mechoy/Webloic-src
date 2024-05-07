package weblogic.work;

import java.security.AccessController;
import weblogic.application.ApplicationAccess;
import weblogic.management.runtime.ExecuteThread;
import weblogic.management.runtime.JTATransaction;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;

public final class ExecuteThreadRuntime implements ExecuteThread {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final long serialVersionUID = -5035431251569572684L;
   private final String name;
   private final String currentWork;
   private final String lastRequest;
   private final int servicedRequests;
   private final JTATransaction transaction;
   private final String user;
   private final long startTime;
   private final transient weblogic.work.ExecuteThread executeThread;
   private String wmName;
   private String applicationName;
   private String moduleName;
   private String applicationVersion;
   private final boolean standby;
   private final boolean hogger;
   private final boolean stuck;

   public ExecuteThreadRuntime(weblogic.work.ExecuteThread var1) {
      this.executeThread = var1;
      WorkAdapter var2 = var1.getCurrentWork();
      this.currentWork = var2 != null ? var2.toString() : null;
      this.lastRequest = null;
      this.servicedRequests = var1.getExecuteCount();
      TransactionManager var3 = TxHelper.getTransactionManager();
      if (var3 != null && var3 instanceof ServerTransactionManager) {
         this.transaction = ((ServerTransactionManager)var3).getJTATransactionForThread(var1);
      } else {
         this.transaction = null;
      }

      String var4 = null;

      try {
         AuthenticatedSubject var5 = SecurityManager.getCurrentSubject(kernelID, var1);
         if (var5 != null) {
            var4 = SubjectUtils.getUsername(var5);
         }
      } catch (Exception var9) {
      }

      this.user = var4;
      this.name = var1.getName();
      this.standby = var1.isStandby();
      this.startTime = var1.getTimeStamp();
      this.hogger = var1.isHog();
      this.stuck = var1.isStuck();

      try {
         SelfTuningWorkManagerImpl var10 = var1.getWorkManager();
         this.wmName = var10.getName();
         ClassLoader var6 = var1.getContextClassLoader();
         ApplicationAccess var7 = ApplicationAccess.getApplicationAccess();
         this.applicationName = var7.getApplicationName(var6);
         this.moduleName = var7.getModuleName(var6);
         this.applicationVersion = var7.getApplicationVersion(var6);
      } catch (Exception var8) {
         this.wmName = null;
         this.applicationName = null;
         this.moduleName = null;
         this.applicationVersion = null;
      }

   }

   public String getName() {
      return this.name;
   }

   public String getCurrentRequest() {
      return this.currentWork;
   }

   public String getLastRequest() {
      return this.lastRequest;
   }

   public int getServicedRequestTotalCount() {
      return this.servicedRequests;
   }

   public JTATransaction getTransaction() {
      return this.transaction;
   }

   public String getUser() {
      return this.user;
   }

   public boolean isIdle() {
      return this.currentWork == null;
   }

   public boolean isStandby() {
      return this.standby;
   }

   public long getCurrentRequestStartTime() {
      return this.startTime;
   }

   public Thread getExecuteThread() {
      return this.executeThread;
   }

   public String getWorkManagerName() {
      return this.wmName;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getApplicationVersion() {
      return this.applicationVersion;
   }

   public boolean isHogger() {
      return this.executeThread != null ? this.executeThread.isHog() : this.hogger;
   }

   public boolean isStuck() {
      return this.executeThread != null ? this.executeThread.isStuck() : this.stuck;
   }
}
