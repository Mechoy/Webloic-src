package weblogic.management.deploy.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import weblogic.application.FatalModuleException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.DeploymentType;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.deploy.internal.targetserver.BasicDeployment;
import weblogic.deploy.internal.targetserver.OrderedDeployments;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLifecycleException;
import weblogic.server.ServiceFailureException;

public class ConfiguredDeployments {
   private static ConfiguredDeployments singleton;
   private static final String PSEUDO_DEPLOYMENT_HANDLER = "PseudoDeploymentHandler";
   private static final String PSEUDO_RESOURCE_DEPENDENT_DEPLOYMENT_HANDLER = "PseudoResourceDependentDeploymentHandler";
   private static final String PSEUDO_STARTUP_CLASS = "PseudoStartupClass";
   private static final String TUNNELING_WEBAPP = "bea_wls_internal";
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final AppRuntimeStateManager appRTStateMgr = AppRuntimeStateManager.getManager();
   private static final DomainMBean domain;

   private ConfiguredDeployments() {
      singleton = this;
   }

   static ConfiguredDeployments getConfigureDeploymentsHandler() {
      if (singleton == null) {
         new ConfiguredDeployments();
      }

      return singleton;
   }

   void deployPreStandbyInternalApps() throws ServiceFailureException {
      try {
         AppDeploymentMBean var1 = domain.lookupInternalAppDeployment("bea_wls_internal");
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Deploy preStandby internal app bea_wls_internal, mbean=" + var1);
         }

         if (var1 != null) {
            BasicDeployment var2 = OrderedDeployments.getOrCreateBasicDeployment(var1);
            var2.prepare();
            var2.activateFromServerLifecycle();
            var2.adminToProductionFromServerLifecycle();
         }
      } catch (Exception var3) {
         throw new ServiceFailureException("Cannot deploy internal app bea_wls_internal", var3);
      }
   }

   void undeployPreStandbyInternalApps() throws ServiceFailureException {
      try {
         AppDeploymentMBean var1 = domain.lookupInternalAppDeployment("bea_wls_internal");
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("undeploy preStandby internal app bea_wls_internal, mbean=" + var1);
         }

         if (var1 == null) {
            return;
         }

         BasicDeployment var2 = OrderedDeployments.getOrCreateBasicDeployment(var1);
         var2.productionToAdminFromServerLifecycle(false);
         var2.deactivateFromServerLifecycle();
         var2.unprepare();
      } catch (Exception var3) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Cannot deploy internal app bea_wls_internal", var3);
         }
      }

   }

   void deploy() throws DeploymentException, ServiceFailureException {
      SlaveDeployerLogger.logSlaveResumeStart();
      this.init();
      this.prepare();
      this.activate();
   }

   private void init() throws DeploymentException, ServiceFailureException {
      BasicDeploymentMBean[] var1 = domain.getBasicDeployments();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         BasicDeploymentMBean var3 = var1[var2];
         if (this.isRetiredApp(var3)) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Skipping retired app: " + var3);
            }
         } else if (TargetHelper.isTargetedLocaly(var3)) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("+++ " + var3.getName() + " is locally targeted");
            }

            OrderedDeployments.getOrCreateBasicDeployment(var3);
         } else if (TargetHelper.isPinnedToServerInCluster(var3) && !(var3 instanceof SystemResourceMBean)) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("+++ " + var3.getName() + " is pinned to other servers in cluster");
            }

            OrderedDeployments.getOrCreateBasicDeployment(var3);
         }
      }

      OrderedDeployments.addDeployment(DeploymentType.PSEUDO_DEPLOYMENT_HANDLER_MBEAN, "PseudoDeploymentHandler");
      OrderedDeployments.addDeployment(DeploymentType.PSEUDO_RESOURCE_DEPENDENT_DEP_HANDLER_MBEAN, "PseudoResourceDependentDeploymentHandler");
      OrderedDeployments.addDeployment(DeploymentType.PSEUDO_STARTUP_CLASS_MBEAN, "PseudoStartupClass");
   }

   private void prepare() throws DeploymentException, ServiceFailureException {
      this.transitionApps(AppTransition.PREPARE);
   }

   private void activate() throws DeploymentException, ServiceFailureException {
      this.transitionApps(AppTransition.ACTIVATE);
   }

   void adminToProduction() throws DeploymentException, ServiceFailureException {
      this.transitionApps(AppTransition.ADMIN_TO_PRODUCTION);
   }

   void productionToAdmin(boolean var1) throws DeploymentException, ServiceFailureException {
      if (var1) {
         this.transitionApps(AppTransition.GRACEFUL_PRODUCTION_TO_ADMIN);
      } else {
         this.transitionApps(AppTransition.FORCE_PRODUCTION_TO_ADMIN);
      }

   }

   void undeploy() throws DeploymentException, ServiceFailureException {
      this.deactivate();
      this.unprepare();
   }

   private void deactivate() throws DeploymentException, ServiceFailureException {
      this.transitionApps(AppTransition.DEACTIVATE);
   }

   private void unprepare() throws DeploymentException, ServiceFailureException {
      this.transitionApps(AppTransition.UNPREPARE);
   }

   private void transitionApps(AppTransition var1) throws DeploymentException, ServiceFailureException {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.getAppsIterator(var1);

      while(true) {
         while(true) {
            Object var5;
            DeploymentAdapter var6;
            do {
               do {
                  if (!var4.hasNext()) {
                     if (!var3.isEmpty()) {
                        this.transitionApps(var1, var3, var2);
                        var3.clear();
                     }

                     this.handleFailedApps(var2, var1);
                     return;
                  }

                  var5 = var4.next();
               } while(this.isPreStandbyApp(var5));

               var6 = this.getDeploymentAdapter(var5);
            } while(var6 == null);

            if (!var3.isEmpty() && !this.isSameApp(var5, var3.get(0))) {
               this.transitionApps(var1, var3, var2);
               var3.clear();
            }

            if (this.isAppVersion(var5)) {
               boolean var7 = this.isActiveAppVersion(var5);
               if (var1.isStartup() && var7 || !var1.isStartup() && !var7) {
                  if (!this.isAdminMode(var5)) {
                     var3.add(0, var5);
                  } else {
                     var3.add(var5);
                  }
                  continue;
               }
            }

            try {
               var1.transitionApp(var6, var5);
            } catch (Throwable var8) {
               this.handleAppException(var1, var5, var8, var2);
            }
         }
      }
   }

   private void transitionApps(AppTransition var1, ArrayList var2, ArrayList var3) throws DeploymentException, ServiceFailureException {
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         DeploymentAdapter var6 = this.getDeploymentAdapter(var5);
         if (var6 != null) {
            try {
               var1.transitionApp(var6, var5);
            } catch (Throwable var8) {
               this.handleAppException(var1, var5, var8, var3);
            }
         }
      }

   }

   private void handleAppException(AppTransition var1, Object var2, Throwable var3, ArrayList var4) throws DeploymentException, ServiceFailureException {
      if (var3 instanceof ServiceFailureException) {
         throw (ServiceFailureException)var3;
      } else {
         if (var3 instanceof FatalModuleException) {
            try {
               ManagementService.getRuntimeAccess(kernelId).getServerRuntime().abortStartupAfterAdminState();
            } catch (ServerLifecycleException var6) {
               throw (FatalModuleException)var3;
            }
         }

         if (!(var2 instanceof BasicDeployment)) {
            SlaveDeployerLogger.logAppStartupFailed(var3);
         }

         if (var1.isStartup()) {
            var4.add(var2);
         }

      }
   }

   private Iterator getAppsIterator(AppTransition var1) {
      return var1.isStartup() ? OrderedDeployments.getDeployments().iterator() : new Iterator() {
         private Collection deps = OrderedDeployments.getDeployments();
         private ListIterator listIter;

         {
            this.listIter = (new ArrayList(this.deps)).listIterator(this.deps.size());
         }

         public boolean hasNext() {
            return this.listIter.hasPrevious();
         }

         public Object next() {
            return this.listIter.previous();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   private void handleFailedApps(List var1, AppTransition var2) throws DeploymentException {
      if (var1.size() != 0) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            DeploymentAdapter var5 = this.getDeploymentAdapter(var4);
            if (var5 != null) {
               try {
                  var5.remove(var4, false);
               } catch (Throwable var7) {
                  Debug.deploymentDebug("Error in removing deployment", var7);
               }
            }
         }

         ServerMBean var9 = ManagementService.getRuntimeAccess(kernelId).getServer();
         ClusterMBean var10 = var9.getCluster();
         if (var10 != null) {
            if (var2 != AppTransition.ACTIVATE) {
               if (!Boolean.getBoolean("weblogic.deployment.IgnorePrepareStateFailures")) {
                  try {
                     ManagementService.getRuntimeAccess(kernelId).getServerRuntime().abortStartupAfterAdminState();
                     SlaveDeployerLogger.logStartupFailedTransitionToAdmin(var9.getName(), var10.getName());
                  } catch (ServerLifecycleException var8) {
                     if (var2 == AppTransition.PREPARE) {
                        this.failForErrorsInCluster(var9.getName(), var10.getName());
                     }
                  }

               }
            }
         }
      }
   }

   private void failForErrorsInCluster(String var1, String var2) throws DeploymentException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         SlaveDeployerLogger.logFailedDeployClusterAS(var1, var2);
      } else {
         Loggable var3 = SlaveDeployerLogger.logStartupFailedLoggable(var1, var2);
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }
   }

   private DeploymentAdapter getDeploymentAdapter(Object var1) {
      if (var1 instanceof BasicDeployment) {
         return DeploymentAdapter.BASIC_DEP_ADAPTER;
      } else if (var1 == "PseudoDeploymentHandler") {
         return DeploymentAdapter.DEPLOYMENT_HANDLERS_ADAPTER;
      } else if (var1 == "PseudoResourceDependentDeploymentHandler") {
         return DeploymentAdapter.RESOURCE_DEPENDENT_DEPLOYMENT_HANDLERS_ADAPTER;
      } else {
         return var1 == "PseudoStartupClass" ? DeploymentAdapter.STARTUP_CLASSES_ADAPTER : null;
      }
   }

   private boolean isPreStandbyApp(Object var1) {
      return !(var1 instanceof AppDeployment) ? false : "bea_wls_internal".equals(((AppDeployment)var1).getName());
   }

   private boolean isAppVersion(Object var1) {
      if (!(var1 instanceof AppDeployment)) {
         return false;
      } else {
         AppDeploymentMBean var2 = (AppDeploymentMBean)((AppDeployment)var1).getDeploymentMBean();
         return var2.getVersionIdentifier() != null;
      }
   }

   private boolean isActiveAppVersion(Object var1) {
      return var1 instanceof AppDeployment && appRTStateMgr.isActiveVersion(((AppDeployment)var1).getDeploymentMBean().getName());
   }

   private boolean isAdminMode(Object var1) {
      return var1 instanceof AppDeployment && appRTStateMgr.isAdminMode(((AppDeployment)var1).getDeploymentMBean().getName());
   }

   private boolean isSameApp(Object var1, Object var2) {
      if (var1 instanceof AppDeployment && var2 instanceof AppDeployment) {
         AppDeploymentMBean var3 = (AppDeploymentMBean)((AppDeployment)var1).getDeploymentMBean();
         AppDeploymentMBean var4 = (AppDeploymentMBean)((AppDeployment)var2).getDeploymentMBean();
         return var3.getApplicationName().equals(var4.getApplicationName());
      } else {
         return false;
      }
   }

   private boolean isRetiredApp(BasicDeploymentMBean var1) {
      return var1 instanceof AppDeploymentMBean && appRTStateMgr.isRetiredVersion((AppDeploymentMBean)var1);
   }

   static {
      domain = ManagementService.getRuntimeAccess(kernelId).getDomain();
   }
}
