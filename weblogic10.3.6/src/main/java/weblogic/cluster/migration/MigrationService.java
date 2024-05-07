package weblogic.cluster.migration;

import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.cluster.ClusterTextTextFormatter;
import weblogic.cluster.migration.management.MigratableServiceCoordinatorRuntime;
import weblogic.cluster.migration.management.MigratableServiceUpdateBeanListener;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.SecurityHelper;
import weblogic.management.provider.DomainAccessSettable;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class MigrationService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ClusterTextTextFormatter fmt = new ClusterTextTextFormatter();
   private static MigratableServiceCoordinatorRuntimeMBean coordinator;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static MigratableServiceCoordinatorRuntimeMBean getMigrationCoordinator() {
      SecurityHelper.assertIfNotKernel();
      return coordinator;
   }

   private void initialize() throws ServiceFailureException {
      (new MigrationManager()).initialize();
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         try {
            coordinator = new MigratableServiceCoordinatorRuntime();
            ((DomainAccessSettable)ManagementService.getDomainAccess(kernelId)).setServerMigrationCoordinator(coordinator);
         } catch (ManagementException var6) {
            throw new ServiceFailureException(var6);
         }
      }

      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ClusterMBean[] var2 = var1.getClusters();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.validateMTConfigs(var2[var3]);
      }

      MigratableTargetMBean[] var7 = var1.getMigratableTargets();
      if (var7 != null) {
         for(int var4 = 0; var4 < var7.length; ++var4) {
            MigratableTargetMBean var5 = var7[var4];
            var5.addBeanUpdateListener(new MigratableServiceUpdateBeanListener());
         }

         var1.addBeanUpdateListener(new MigratableConfiguredListener());
      }
   }

   public void start() throws ServiceFailureException {
      this.initialize();

      try {
         MigrationManager.singleton().start();
      } catch (MigrationException var2) {
         throw new ServiceFailureException("Failed to activate targets", var2);
      }
   }

   public void stop() throws ServiceFailureException {
      try {
         MigrationManager.singleton().stop();
      } catch (MigrationException var2) {
         throw new ServiceFailureException("Failed to deactivate targets", var2);
      }
   }

   public void halt() throws ServiceFailureException {
      try {
         MigrationManager.singleton().halt();
      } catch (MigrationException var2) {
         throw new ServiceFailureException("Failed to deactivate targets", var2);
      }
   }

   private void validateMTConfigs(ClusterMBean var1) throws ServiceFailureException {
      MigratableTargetMBean[] var2 = var1.getMigratableTargets();
      boolean var3 = false;
      HashSet var4 = new HashSet();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         if (!"manual".equals(var2[var5].getMigrationPolicy())) {
            var3 = true;
            ServerMBean[] var6 = var2[var5].getConstrainedCandidateServers();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var4.add(var6[var7]);
            }
         }
      }

      if (var3) {
         String var9 = var1.getMigrationBasis();
         if ("database".equals(var9) && var1.getDataSourceForAutomaticMigration() == null) {
            throw new ServiceFailureException(fmt.getCannotEnableAutoMigrationWithoutLeasing2());
         } else {
            if ("consensus".equals(var9)) {
               Iterator var10 = var4.iterator();

               while(var10.hasNext()) {
                  ServerMBean var11 = (ServerMBean)var10.next();
                  MachineMBean var8 = var11.getMachine();
                  if (var8 == null) {
                     throw new ServiceFailureException(fmt.getNodemanagerRequiredOnCandidateServers(var11.getName()));
                  }

                  if (var8.getNodeManager() == null) {
                     throw new ServiceFailureException(fmt.getNodemanagerRequiredOnCandidateServers(var11.getName()));
                  }
               }
            }

         }
      }
   }

   class MigratableConfiguredListener implements BeanUpdateListener {
      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      }

      public void activateUpdate(BeanUpdateEvent var1) {
         BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            BeanUpdateEvent.PropertyUpdate var4 = var2[var3];
            if (var4.getUpdateType() == 2 && var4.getPropertyName().equals("MigratableTargets")) {
               MigratableTargetMBean var5 = (MigratableTargetMBean)var4.getAddedObject();
               var5.addBeanUpdateListener(new MigratableServiceUpdateBeanListener());
            }
         }

      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
      }
   }
}
