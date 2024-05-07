package weblogic.deploy.internal.targetserver.state;

import java.security.AccessController;
import java.util.Date;
import weblogic.application.ModuleListener;
import weblogic.application.ModuleListenerCtx;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.compatibility.NotificationBroadcaster;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ModuleTransitionTracker implements ModuleListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String serverName;
   private final AppDeploymentMBean mbean;
   private final String taskID;
   private final DeploymentState state;

   ModuleTransitionTracker(AppDeploymentMBean var1, String var2, DeploymentState var3) {
      this.mbean = var1;
      this.state = var3;
      if (var1.getAppMBean() == null && this.isDebugEnabled()) {
         this.debug("Listener: appmbean is NULL for:" + this);
      }

      this.taskID = var2;
   }

   public void beginTransition(ModuleListenerCtx var1, ModuleListener.State var2, ModuleListener.State var3) {
      if (var1.getApplicationId().equals(this.mbean.getApplicationIdentifier())) {
         if (this.isDebugEnabled()) {
            this.debug("Listener: begin " + var1 + " " + var2 + ">" + var3);
         }

         long var4 = (new Date()).getTime();
         TargetModuleState var6 = this.state.addModuleTransition(var1, var2.toString(), var3.toString(), "begin", var4);
         if (var6 != null) {
            var6.setCurrentState(var2.toString());
         }

         RuntimeAccess var7 = ManagementService.getRuntimeAccess(kernelId);
         ApplicationMBean var8 = var7.getDomain().lookupApplication(this.mbean.getApplicationIdentifier());
         if (var8 != null && NotificationBroadcaster.isRelevantToWLS81(var2.toString(), var3.toString())) {
            var8.sendModuleNotification(serverName, var1.getModuleUri(), "begin", var2.toString(), var3.toString(), this.taskID, var4);
         }

         DeployerRuntimeLogger.logStartTransition(this.getAppDisplayName(), this.getModuleDisplayName(var1.getModuleUri()), var2.toString(), var3.toString(), serverName);
      }
   }

   public void endTransition(ModuleListenerCtx var1, ModuleListener.State var2, ModuleListener.State var3) {
      if (var1.getApplicationId().equals(this.mbean.getApplicationIdentifier())) {
         if (this.isDebugEnabled()) {
            this.debug("Listener: end " + var1 + " " + var2 + ">" + var3);
         }

         long var4 = (new Date()).getTime();
         TargetModuleState var6 = this.state.addModuleTransition(var1, var2.toString(), var3.toString(), "end", var4);
         if (var6 != null) {
            var6.setCurrentState(var3.toString());
         } else if (this.isDebugEnabled()) {
            this.debug("Listener: module not locally targeted");
         }

         RuntimeAccess var7 = ManagementService.getRuntimeAccess(kernelId);
         ApplicationMBean var8 = var7.getDomain().lookupApplication(this.mbean.getApplicationIdentifier());
         if (var8 != null && NotificationBroadcaster.isRelevantToWLS81(var2.toString(), var3.toString())) {
            var8.sendModuleNotification(serverName, var1.getModuleUri(), "end", var2.toString(), var3.toString(), this.taskID, var4);
         }

         DeployerRuntimeLogger.logSuccessfulTransition(this.getAppDisplayName(), this.getModuleDisplayName(var1.getModuleUri()), var2.toString(), var3.toString(), serverName);
      }
   }

   public void failedTransition(ModuleListenerCtx var1, ModuleListener.State var2, ModuleListener.State var3) {
      if (var1.getApplicationId().equals(this.mbean.getApplicationIdentifier())) {
         if (this.isDebugEnabled()) {
            this.debug("Listener: fail " + var1 + " " + var2 + ">" + var3);
         }

         long var4 = (new Date()).getTime();
         TargetModuleState var6 = this.state.addModuleTransition(var1, var2.toString(), var3.toString(), "failed", var4);
         if (var6 != null) {
            var6.setCurrentState(var3.toString());
         }

         RuntimeAccess var7 = ManagementService.getRuntimeAccess(kernelId);
         ApplicationMBean var8 = var7.getDomain().lookupApplication(this.mbean.getApplicationIdentifier());
         if (var8 != null && NotificationBroadcaster.isRelevantToWLS81(var2.toString(), var3.toString())) {
            var8.sendModuleNotification(serverName, var1.getModuleUri(), "failed", var2.toString(), var3.toString(), this.taskID, var4);
         }

         DeployerRuntimeLogger.logFailedTransition(this.getAppDisplayName(), this.getModuleDisplayName(var1.getModuleUri()), var2.toString(), var3.toString(), serverName);
      }
   }

   private String getAppDisplayName() {
      return ApplicationVersionUtils.getDisplayName(this.mbean.getName());
   }

   private String getModuleDisplayName(String var1) {
      return ApplicationVersionUtils.getApplicationName(var1);
   }

   public String toString() {
      return "ModuleListener[appName= " + this.mbean.getName() + "]";
   }

   private void debug(String var1) {
      Debug.deploymentDebug(var1);
   }

   private boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   static {
      serverName = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
   }
}
