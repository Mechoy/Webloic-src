package weblogic.kernel;

import java.security.AccessController;
import javax.management.MalformedObjectNameException;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicObjectName;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ExecuteQueueRuntimeMBean;
import weblogic.management.runtime.JTATransaction;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.AssertionError;

public final class ExecuteQueueRuntime extends RuntimeMBeanDelegate implements ExecuteQueueRuntimeMBean {
   private static final long serialVersionUID = 8609236957899063575L;
   private long lastDepartureSnapshot;
   private long lastDepartureSnapshotTime;
   private long lastWaitTime;
   private final ExecuteThreadManager queue;
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static void addExecuteQueueRuntime(ExecuteThreadManager var0) {
      try {
         new ExecuteQueueRuntime(var0.getName(), var0);
      } catch (ManagementException var2) {
         T3SrvrLogger.logErrorCreatingExecuteQueueRuntime(var0.getName(), var2);
      }

   }

   public ExecuteQueueRuntime() throws ManagementException {
      throw new AssertionError("constructor for JMX compliance only");
   }

   private ExecuteQueueRuntime(String var1, ExecuteThreadManager var2) throws ManagementException {
      super(var1);
      if ("weblogic.kernel.Default".equalsIgnoreCase(var1) || "default".equalsIgnoreCase(var1)) {
         ManagementService.getRuntimeAccess(kernelID).getServerRuntime().setDefaultExecuteQueueRuntime(this);
      }

      ManagementService.getRuntimeAccess(kernelID).getServerRuntime().addExecuteQueueRuntime(this);
      this.queue = var2;
      this.lastDepartureSnapshot = 0L;
      this.lastDepartureSnapshotTime = this.lastWaitTime = System.currentTimeMillis();
   }

   public weblogic.management.runtime.ExecuteThread[] getExecuteThreads() {
      ExecuteThread[] var1 = this.queue.getExecuteThreads();
      int var2 = var1.length;
      weblogic.management.runtime.ExecuteThread[] var3 = new weblogic.management.runtime.ExecuteThread[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = new ExecuteThreadRuntime(var1[var4], this);
      }

      return var3;
   }

   public int getExecuteThreadCurrentIdleCount() {
      return this.queue.getIdleThreadCount();
   }

   public int getPendingRequestCurrentCount() {
      return this.queue.getExecuteQueueDepth();
   }

   public long getPendingRequestOldestTime() {
      long var1 = (long)this.getServicedRequestTotalCount();
      long var3 = var1 - this.lastDepartureSnapshot;
      long var5 = System.currentTimeMillis();
      long var7 = var5 - this.lastDepartureSnapshotTime;
      long var9 = var5;
      if (var3 != 0L) {
         long var11 = var7 / var3;
         var9 = var5 - var11 * (long)this.getPendingRequestCurrentCount();
      } else if (this.getPendingRequestCurrentCount() != 0) {
         var9 = this.lastWaitTime;
      }

      this.lastDepartureSnapshot = var1;
      this.lastDepartureSnapshotTime = var5;
      this.lastWaitTime = var9;
      return var9;
   }

   public int getServicedRequestTotalCount() {
      return this.queue.getExecuteQueueDepartures();
   }

   public weblogic.management.runtime.ExecuteThread[] getStuckExecuteThreads() {
      weblogic.management.runtime.ExecuteThread[] var1 = null;
      long var2 = getConfiguredStuckThreadMaxTime(ManagementService.getRuntimeAccess(kernelID).getServer());
      ExecuteThread[] var4 = this.queue.getStuckExecuteThreads(var2);
      if (var4 != null) {
         int var5 = var4.length;
         var1 = new weblogic.management.runtime.ExecuteThread[var5];

         for(int var6 = 0; var6 < var5; ++var6) {
            var1[var6] = new ExecuteThreadRuntime(var4[var6], this);
         }
      }

      return var1;
   }

   private static final long getConfiguredStuckThreadMaxTime(ServerMBean var0) {
      return var0.getOverloadProtection().getServerFailureTrigger() != null ? (long)var0.getOverloadProtection().getServerFailureTrigger().getMaxStuckThreadTime() * 1000L : (long)var0.getStuckThreadMaxTime() * 1000L;
   }

   public int getExecuteThreadTotalCount() {
      return this.queue.getExecuteThreadCount();
   }

   private static final class ExecuteThreadRuntime implements weblogic.management.runtime.ExecuteThread {
      private static final long serialVersionUID = -716311114026904319L;
      private final String name;
      private WebLogicObjectName queueName;
      private final String currentRequest;
      private final String lastRequest;
      private final int servicedRequests;
      private final JTATransaction transaction;
      private final String user;
      private final long startTime;
      private final boolean isStuck;
      private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      private final transient Thread executeThread;

      private ExecuteThreadRuntime(ExecuteThread var1, ExecuteQueueRuntimeMBean var2) {
         this.executeThread = var1;
         ExecuteRequest var3 = var1.getCurrentRequest();
         this.currentRequest = var3 != null ? var3.toString() : null;
         this.lastRequest = null;
         this.servicedRequests = var1.getExecuteCount();
         TransactionManager var4 = TxHelper.getTransactionManager();
         if (var4 != null && var4 instanceof ServerTransactionManager) {
            this.transaction = ((ServerTransactionManager)var4).getJTATransactionForThread(var1);
         } else {
            this.transaction = null;
         }

         String var5 = null;

         try {
            AuthenticatedSubject var6 = SecurityManager.getCurrentSubject(kernelID, var1);
            if (var6 != null) {
               var5 = SubjectUtils.getUsername(var6);
            }
         } catch (Exception var8) {
         }

         this.user = var5;
         this.name = var1.getName();

         try {
            RuntimeAccess var9 = ManagementService.getRuntimeAccess(kernelID);
            this.queueName = new WebLogicObjectName(var9.getServerName(), "ExecuteQueueRuntime", var9.getDomainName(), var9.getServerName());
         } catch (MalformedObjectNameException var7) {
         }

         this.startTime = var1.getTimeStamp();
         this.isStuck = var1.getPrintStuckThreadMessage();
      }

      public String getName() {
         return this.name;
      }

      public WebLogicObjectName getExecuteQueueRuntimeName() {
         return this.queueName;
      }

      public String getWorkManagerName() {
         return null;
      }

      public String getApplicationName() {
         return null;
      }

      public String getModuleName() {
         return null;
      }

      public String getApplicationVersion() {
         return null;
      }

      public String getCurrentRequest() {
         return this.currentRequest;
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
         return this.currentRequest == null;
      }

      public boolean isStuck() {
         return this.isStuck;
      }

      public boolean isHogger() {
         return this.isStuck;
      }

      public boolean isStandby() {
         return false;
      }

      public long getCurrentRequestStartTime() {
         return this.startTime;
      }

      public Thread getExecuteThread() {
         return this.executeThread;
      }

      // $FF: synthetic method
      ExecuteThreadRuntime(ExecuteThread var1, ExecuteQueueRuntimeMBean var2, Object var3) {
         this(var1, var2);
      }
   }
}
