package weblogic.deploy.compatibility;

import java.security.AccessController;
import weblogic.application.ModuleListener;
import weblogic.deploy.internal.targetserver.state.AppTransition;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.deploy.internal.targetserver.state.ModuleTransition;
import weblogic.deploy.internal.targetserver.state.TargetModuleState;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class NotificationBroadcaster {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void sendNotificationsFromManagedServer(DeploymentState var0, DeploymentTaskRuntimeMBean var1, DebugLogger var2) {
      if (null != var0) {
         ApplicationMBean var3 = var1.getDeploymentObject();
         if (var3 == null) {
            if (var2.isDebugEnabled()) {
               var2.debug("AppMBean from Task is NULL. No notifs sent.");
            }

         } else {
            Object[] var4 = var0.getTransitions();

            for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
               sendJMXNotification(var1, var3, var4[var5]);
            }

         }
      }
   }

   private static void sendJMXNotification(DeploymentTaskRuntimeMBean var0, ApplicationMBean var1, Object var2) {
      if (var2 instanceof ModuleTransition && var0.getNotificationLevel() >= 2) {
         ModuleTransition var3 = (ModuleTransition)var2;
         TargetModuleState var4 = var3.getModule();
         var1.sendModuleNotification(var4.getServerName(), var4.getModuleId(), var3.getName(), var3.getCurrentState(), var3.getNewState(), var0.getId(), var3.getGenerationTime());
      }

      if (var2 instanceof AppTransition && var0.getNotificationLevel() >= 1) {
         AppTransition var5 = (AppTransition)var2;
         var1.sendAppLevelNotification(var5.getServerName(), var5.getXition(), (String)null);
      }

   }

   public static void sendAppNotification(String var0, String var1, String var2) {
      RuntimeAccess var3 = ManagementService.getRuntimeAccess(kernelId);
      ApplicationMBean var4 = var3.getDomain().lookupApplication(var1);
      if (var4 != null) {
         var4.sendAppLevelNotification(ManagementService.getRuntimeAccess(kernelId).getServerName(), var0, var2);
      }

   }

   public static boolean isRelevantToWLS81(String var0, String var1) {
      boolean var2 = var0.equals(ModuleListener.STATE_NEW.toString()) && var1.equals(ModuleListener.STATE_PREPARED.toString());
      boolean var3 = var0.equals(ModuleListener.STATE_PREPARED.toString()) && var1.equals(ModuleListener.STATE_NEW.toString());
      return !var2 && !var3;
   }
}
