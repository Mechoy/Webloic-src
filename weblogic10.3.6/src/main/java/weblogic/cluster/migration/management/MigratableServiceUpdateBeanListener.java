package weblogic.cluster.migration.management;

import java.security.AccessController;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.cluster.singleton.SingletonServicesDebugLogger;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class MigratableServiceUpdateBeanListener implements BeanUpdateListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      if (DEBUG) {
         this.p("Rolling back update: " + var1);
      }

      MigratableTargetMBean var2 = (MigratableTargetMBean)var1.getProposedBean();
      MigratableTargetMBean var3 = this.getMigratableTarget(var2.getName());
      boolean var4 = this.isConfigUpdatedForUserPreferredServer(var2, var3);
      if (var4) {
         MigrationManager var5 = MigrationManager.singleton();

         try {
            try {
               var5.deactivateTarget(var2.getName(), (String)null);
            } catch (MigrationException var10) {
               ClusterLogger.logFailedToDeactivateMigratableServicesDuringRollback(var10);
            }

         } finally {
            ;
         }
      }
   }

   private MigratableTargetMBean getMigratableTarget(String var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      return var2.lookupMigratableTarget(var1);
   }

   private boolean isConfigUpdatedForUserPreferredServer(MigratableTargetMBean var1, MigratableTargetMBean var2) {
      boolean var3 = false;
      ServerMBean var4 = var1.getUserPreferredServer();
      if (var4 == null) {
         return var3;
      } else {
         ServerMBean var5 = var2.getUserPreferredServer();
         if (DEBUG) {
            this.p("Intended User Preferred Server - " + var4 + " Existing User Preferred Server - " + var5);
         }

         ServerMBean var6 = ManagementService.getRuntimeAccess(kernelId).getServer();
         if (var5 != null) {
            if (!var4.getName().equals(var5.getName()) && var4.getName().equals(var6.getName())) {
               var3 = true;
            }
         } else if (var4.getName().equals(var6.getName())) {
            var3 = true;
         }

         return var3;
      }
   }

   private void p(Object var1) {
      SingletonServicesDebugLogger.debug("MigratableServiceUpdateBeanListener: " + var1.toString());
   }
}
