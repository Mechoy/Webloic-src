package weblogic.deploy.service.internal;

import java.io.PrintWriter;
import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.adminserver.DeploymentManager;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.DeploymentRequestSubTask;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DeploymentRequestTaskRuntimeMBeanImpl extends DomainRuntimeMBeanDelegate implements DeploymentRequestTaskRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final DeploymentRequest deploymentRequest;
   private static final String TASK_NAME_PREFIX = "DREQ-";
   private final String description;
   private final long taskId;
   private int state;
   private HashMap failedTargetsMap = new HashMap();
   private Map deploymentSubTasks = new HashMap();
   private Set serversToBeRestarted;
   private final long beginTime = System.currentTimeMillis();
   private Exception lastException;
   private long endTime;
   private boolean failedWhilePreparingToStart = false;
   private boolean allSubTasksCompleted = false;

   public DeploymentRequestTaskRuntimeMBeanImpl(String var1, DeploymentRequest var2) throws ManagementException {
      super("DREQ-" + var2.getId(), false);
      this.description = var1;
      this.deploymentRequest = var2;
      this.taskId = var2.getId();
      this.state = 0;
   }

   private static final void debug(String var0) {
      Debug.serviceDebug(var0);
   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   public final String getDescription() {
      return this.description;
   }

   public final String getStatus() {
      switch (this.state) {
         case 0:
            return "STATE_INITIALIZING";
         case 1:
            return "STATE_INPROGRESS";
         case 2:
            return "STATE_SUCCESS";
         case 3:
            return "STATE_FAILED";
         case 4:
            return "STATE_CANCEL_SCHEDULED";
         case 5:
            return "STATE_CANCEL_INPROGRESS";
         case 6:
            return "STATE_CANCEL_COMPLETED";
         case 7:
            return "STATE_CANCEL_FAILED";
         case 8:
            return "STATE_COMMIT_FAILED";
         default:
            return "STATE_UNKNOWN";
      }
   }

   public final boolean isRunning() {
      if (this.getDeploymentRequestSubTasks().size() <= 0) {
         return true;
      } else {
         Iterator var1 = this.deploymentSubTasks.entrySet().iterator();

         DeploymentRequestSubTask var3;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            Map.Entry var2 = (Map.Entry)var1.next();
            var3 = (DeploymentRequestSubTask)var2.getValue();
         } while(!var3.isRunning());

         return true;
      }
   }

   public final boolean isComplete() {
      if (this.getDeploymentRequestSubTasks().size() <= 0) {
         return true;
      } else {
         Iterator var1 = this.deploymentSubTasks.entrySet().iterator();

         DeploymentRequestSubTask var3;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            Map.Entry var2 = (Map.Entry)var1.next();
            var3 = (DeploymentRequestSubTask)var2.getValue();
         } while(var3.isComplete());

         return false;
      }
   }

   public final long getBeginTime() {
      return this.beginTime;
   }

   public final long getEndTime() {
      return this.endTime;
   }

   public final void setEndTime() {
      this.endTime = System.currentTimeMillis();
   }

   public final TaskRuntimeMBean[] getSubTasks() {
      return (TaskRuntimeMBean[])((TaskRuntimeMBean[])this.getDeploymentRequestSubTasks().keySet().toArray());
   }

   public final TaskRuntimeMBean getParentTask() {
      return this;
   }

   public final void cancel() throws Exception {
      if (this.deploymentSubTasks != null && this.deploymentSubTasks.size() > 0) {
         boolean var1 = this.getState() == 0;
         this.setState(4);
         this.prepareSubTasksForCancel();
         if (var1) {
            DeploymentManager.getInstance(kernelId).deploymentRequestCancelledBeforeStart(this.deploymentRequest);
         } else {
            DeploymentService.getDeploymentService().cancel(this.deploymentRequest);
         }

      } else {
         throw new ManagementException(DeploymentServiceLogger.logNoTaskToCancelLoggable().getMessage());
      }
   }

   public final void printLog(PrintWriter var1) {
   }

   public final synchronized Exception getError() {
      if (this.lastException != null) {
         return this.lastException;
      } else if (this.getDeploymentRequestSubTasks().size() <= 0) {
         return this.lastException;
      } else {
         Iterator var1 = this.deploymentSubTasks.entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry var2 = (Map.Entry)var1.next();
            DeploymentRequestSubTask var3 = (DeploymentRequestSubTask)var2.getValue();
            if (var3.getError() != null) {
               this.lastException = var3.getError();
               break;
            }
         }

         return this.lastException;
      }
   }

   public final boolean isSystemTask() {
      return false;
   }

   public final void setSystemTask(boolean var1) {
   }

   public final long getTaskId() {
      return this.taskId;
   }

   public final int getState() {
      return this.state;
   }

   public final void setState(int var1) {
      this.state = var1;
   }

   public final synchronized Map getFailedTargets() {
      if (this.failedTargetsMap.size() <= 0 && this.getDeploymentRequestSubTasks().size() > 0) {
         Iterator var1 = this.getDeploymentRequestSubTasks().entrySet().iterator();

         while(true) {
            Map var4;
            do {
               do {
                  if (!var1.hasNext()) {
                     if (isDebugEnabled()) {
                        debug("getFailedTargets returning: " + this.failedTargetsMap);
                     }

                     return this.failedTargetsMap;
                  }

                  Map.Entry var2 = (Map.Entry)var1.next();
                  DeploymentRequestSubTask var3 = (DeploymentRequestSubTask)var2.getValue();
                  var4 = var3.getFailedTargets();
               } while(var4 == null);
            } while(var4.size() <= 0);

            Iterator var5 = var4.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry var6 = (Map.Entry)var5.next();
               String var7 = (String)var6.getKey();
               if (!this.failedTargetsMap.containsKey(var7)) {
                  Exception var8 = (Exception)var6.getValue();
                  if (isDebugEnabled()) {
                     debug("getFailedTargets adding target '" + var7 + "' and failure: " + var8 + " to failed targets map");
                  }

                  this.failedTargetsMap.put(var7, var8);
               }
            }
         }
      } else {
         if (isDebugEnabled()) {
            debug("getFailedTargets returning: " + this.failedTargetsMap);
         }

         return (HashMap)this.failedTargetsMap.clone();
      }
   }

   public final synchronized void addFailedTarget(String var1, Exception var2) {
      this.failedTargetsMap.put(var1, var2);
      if (isDebugEnabled()) {
         debug("addFailedTargets adding target '" + var1 + "' and failure: " + var2 + " to failed targets map");
      }

      this.lastException = var2;
   }

   private final Set getRestartSet() {
      if (this.serversToBeRestarted == null) {
         this.serversToBeRestarted = new HashSet();
      }

      return this.serversToBeRestarted;
   }

   public final synchronized String[] getServersToBeRestarted() {
      String[] var1 = new String[0];
      return (String[])((String[])this.getRestartSet().toArray(var1));
   }

   public final synchronized void addServerToRestartSet(String var1) {
      this.getRestartSet().add(var1);
   }

   public final DeploymentRequest getDeploymentRequest() {
      return this.deploymentRequest;
   }

   private final Map getDeploymentRequestSubTasks() {
      return this.deploymentSubTasks;
   }

   public final void addDeploymentRequestSubTask(DeploymentRequestSubTask var1, String var2) {
      this.getDeploymentRequestSubTasks().put(var2, var1);
      var1.setMyParent(this);
   }

   public final void start() throws ManagementException {
      this.prepareSubTasksForStart();
      this.startTaskIfNecessary();
   }

   public final void unregisterIfNoSubTasks() {
      if (this.getDeploymentRequestSubTasks().isEmpty()) {
         try {
            if (isDebugEnabled()) {
               debug("Unregistering DeploymentRequestTaskRuntimeMBean : " + this);
            }

            this.unregister();
         } catch (Throwable var2) {
            var2.printStackTrace();
         }
      }

   }

   private final void prepareSubTasksForStart() throws ManagementException {
      try {
         Iterator var1 = this.getDeploymentRequestSubTasks().entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry var2 = (Map.Entry)var1.next();
            DeploymentRequestSubTask var3 = (DeploymentRequestSubTask)var2.getValue();
            var3.prepareToStart();
         }

      } catch (ManagementException var5) {
         this.failedWhilePreparingToStart = true;

         try {
            this.cancel();
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         throw var5;
      }
   }

   private void startTaskIfNecessary() {
      if (this.getDeploymentRequestSubTasks().size() > 0) {
         this.allSubTasksCompleted = true;
      }

      this.lastException = null;
      this.checkForPreStartErrors();
      if (this.lastException != null) {
         this.setState(3);
      } else if (this.allSubTasksCompleted) {
         if (isDebugEnabled()) {
            debug("All sub tasks complete for task '" + this.taskId + "'" + " - transitioning task to STATE_SUCCESS");
         }

         this.setState(2);
      } else {
         this.setState(1);
         DeploymentService.getDeploymentService().startDeploy(this.deploymentRequest);
      }

   }

   private final void checkForPreStartErrors() {
      Iterator var1 = this.deploymentSubTasks.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         DeploymentRequestSubTask var3 = (DeploymentRequestSubTask)var2.getValue();
         if (var3.isRunning()) {
            this.allSubTasksCompleted = false;
         } else {
            this.lastException = var3.getError();
            if (this.lastException != null) {
               String var4 = ManagementService.getRuntimeAccess(kernelId).getServerName();
               this.addFailedTarget(var4, this.lastException);
               return;
            }
         }
      }

   }

   private final void prepareSubTasksForCancel() throws Exception {
      Iterator var1 = this.deploymentSubTasks.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         DeploymentRequestSubTask var3 = (DeploymentRequestSubTask)var2.getValue();
         boolean var4 = this.failedWhilePreparingToStart ? !var3.isComplete() : true;
         if (var4) {
            var3.prepareToCancel();
         }
      }

   }
}
