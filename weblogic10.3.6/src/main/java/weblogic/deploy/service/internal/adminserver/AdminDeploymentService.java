package weblogic.deploy.service.internal.adminserver;

import java.io.File;
import java.io.Serializable;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.ChangeDescriptorFactory;
import weblogic.deploy.service.DeploymentProvider;
import weblogic.deploy.service.DeploymentProviderManager;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.DeploymentRequestFactory;
import weblogic.deploy.service.DeploymentServiceCallbackHandler;
import weblogic.deploy.service.DeploymentServiceOperations;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.InvalidCreateChangeDescriptorException;
import weblogic.deploy.service.RegistrationExistsException;
import weblogic.deploy.service.RequiresTaskMediatedStartException;
import weblogic.deploy.service.StatusListener;
import weblogic.deploy.service.StatusListenerManager;
import weblogic.deploy.service.Version;
import weblogic.deploy.service.internal.DeploymentRequestTaskRuntimeMBeanImpl;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.deploy.service.internal.InvalidStateException;
import weblogic.deploy.service.internal.ServiceRequest;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class AdminDeploymentService extends AbstractServerService implements DeploymentRequestFactory, DeploymentServiceOperations, ChangeDescriptorFactory, DeploymentProviderManager, StatusListenerManager {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private AdminRequestManager requestManager;
   private AdminDeploymentsManager deploymentsManager;
   private final StatusDeliverer statusDeliverer = StatusDeliverer.getInstance();
   private static AdminDeploymentService singleton;
   private final HashSet providers = new HashSet();
   private String localServerName;

   private AdminDeploymentService() {
   }

   public static AdminDeploymentService getDeploymentService() {
      if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
         Class var0 = AdminDeploymentService.class;
         synchronized(AdminDeploymentService.class) {
            if (singleton == null) {
               Class var1 = AdminDeploymentService.class;
               synchronized(AdminDeploymentService.class) {
                  singleton = new AdminDeploymentService();
               }
            }
         }
      }

      return singleton;
   }

   private static final void debug(String var0) {
      Debug.serviceDebug(var0);
   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   private String getLocalServerName() {
      if (this.localServerName == null) {
         this.localServerName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      }

      return this.localServerName;
   }

   private void sendCancelFailedTo(DeploymentRequest var1, Exception var2) {
      AdminDeploymentException var3 = new AdminDeploymentException();
      String var4 = DeploymentServiceLogger.indeterminate();
      FailureDescription var5 = new FailureDescription(this.localServerName, var2, var4);
      var3.addFailureDescription(var5);
      DeploymentRequestTaskRuntimeMBeanImpl var6 = (DeploymentRequestTaskRuntimeMBeanImpl)var1.getTaskRuntime();
      if (var6 == null) {
         if (isDebugEnabled()) {
            debug("Cancel attempt failed since underlying request " + var1 + " has completed");
         }

      } else {
         var6.addFailedTarget(this.localServerName, var2);
         this.requestManager.deliverDeployCancelFailedCallback((AdminRequestImpl)var1, var3);
         var6.setState(7);
      }
   }

   public final DeploymentRequest createDeploymentRequest() throws ManagementException {
      Class var2 = AdminDeploymentService.class;
      AdminRequestImpl var1;
      synchronized(AdminDeploymentService.class) {
         var1 = new AdminRequestImpl();
         var1.setId(System.currentTimeMillis());
      }

      DeploymentRequestTaskRuntimeMBeanImpl var5 = new DeploymentRequestTaskRuntimeMBeanImpl("Deploy request with id '" + var1.getId() + "'", var1);
      var1.setTaskRuntime(var5);
      return var1;
   }

   public final void register(Version var1, DeploymentServiceCallbackHandler var2) throws RegistrationExistsException {
      if (isDebugEnabled()) {
         debug("DeploymentServiceCallbackHandler for '" + var2.getHandlerIdentity() + "' registering with version '" + var1.toString() + "'");
      }

      this.deploymentsManager = AdminDeploymentsManager.getInstance();
      this.deploymentsManager.registerCallbackHandler(var1, var2);
   }

   public final DeploymentRequestTaskRuntimeMBean deploy(DeploymentRequest var1) throws RequiresTaskMediatedStartException {
      if (isDebugEnabled()) {
         debug("'deploy'-ing id " + var1.getId());
      }

      if (var1.isStartControlEnabled()) {
         throw new RequiresTaskMediatedStartException(DeploymentServiceLogger.logStartControlLoggable().getMessage());
      } else {
         return this.startDeploy(var1);
      }
   }

   public final DeploymentRequestTaskRuntimeMBean startDeploy(final DeploymentRequest var1) {
      final DeploymentRequestTaskRuntimeMBeanImpl var2 = (DeploymentRequestTaskRuntimeMBeanImpl)var1.getTaskRuntime();
      if (var1 instanceof AdminRequestImpl) {
         if (isDebugEnabled()) {
            debug("starting 'deploy' of id '" + var1.getId() + "'");
         }

         this.requestManager.addRequest(new ServiceRequest() {
            public void run() {
               var2.setState(1);
               ((AdminRequestImpl)var1).run();
            }

            public String toString() {
               return var1.toString();
            }
         });
      } else {
         var2.setState(3);
         String var3 = DeploymentServiceLogger.incompatibleModification();
         Exception var4 = new Exception(var3);
         var2.addFailedTarget(this.getLocalServerName(), var4);
      }

      return var2;
   }

   public final void cancel(DeploymentRequest var1) {
      long var2 = var1.getId();
      if (isDebugEnabled()) {
         debug("'cancel' called on id '" + var2 + "'");
      }

      AdminRequestImpl var4 = this.requestManager.getRequest(var2);
      if (var4 == null) {
         String var9 = DeploymentServiceLogger.noRequestToCancel(var2);
         if (isDebugEnabled()) {
            debug(var9);
         }

         this.requestManager.addPendingCancel(var2);
         this.sendCancelFailedTo(var1, new Exception(var9));
      } else {
         DeploymentRequestTaskRuntimeMBeanImpl var5 = (DeploymentRequestTaskRuntimeMBeanImpl)var1.getTaskRuntime();
         var5.setState(5);

         try {
            var4.cancel();
         } catch (InvalidStateException var8) {
            if (isDebugEnabled()) {
               debug("attempt to 'cancel' id '" + var2 + "' failed due to '" + var8.getMessage() + "'");
            }

            AdminRequestStatus var7 = var4.getStatus();
            if (var7 != null) {
               var7.signalCancelFailed(false);
            }
         }

      }
   }

   public final void unregister(final String var1) {
      final String var2 = "DeploymentServiceCallbackHandler for '" + var1 + "' unregistering ";
      if (isDebugEnabled()) {
         debug(var2);
      }

      this.requestManager.addRequest(new ServiceRequest() {
         public void run() {
            AdminDeploymentService.this.deploymentsManager.unregisterCallbackHandler(var1);
            AdminDeploymentService.this.requestManager.scheduleNextRequest();
         }

         public String toString() {
            return var2;
         }
      });
   }

   public final ChangeDescriptor createChangeDescriptor(String var1, String var2, String var3, Serializable var4) throws InvalidCreateChangeDescriptorException {
      return this.createChangeDescriptor(var1, var2, var3, var4, (String)null);
   }

   public final ChangeDescriptor createChangeDescriptor(String var1, String var2, String var3, Serializable var4, String var5) throws InvalidCreateChangeDescriptorException {
      String var6 = File.separator;
      String var7 = DomainDir.getRootDir() + var6 + var3;
      File var8 = new File(var7);
      if (!var8.exists()) {
         throw new InvalidCreateChangeDescriptorException(DeploymentServiceLogger.logNoFileLoggable(var7).getMessage());
      } else if (!var1.equals("add") && !var1.equals("delete") && !var1.equals("update")) {
         String var10 = DeploymentServiceLogger.unsupportedOperation(var1);
         throw new InvalidCreateChangeDescriptorException(var10);
      } else {
         ChangeDescriptorImpl var9 = new ChangeDescriptorImpl(var1, var2, var3, var4, var5);
         return var9;
      }
   }

   public final ChangeDescriptor createChangeDescriptor(Serializable var1, Serializable var2) {
      return new ChangeDescriptorImpl(var1, var2);
   }

   public final void registerDeploymentProvider(DeploymentProvider var1) {
      synchronized(this.providers) {
         this.providers.add(var1);
      }
   }

   public final Set getRegisteredDeploymentProviders() {
      synchronized(this.providers) {
         return (Set)this.providers.clone();
      }
   }

   public void registerStatusListener(String var1, StatusListener var2) {
      this.statusDeliverer.registerStatusListener(var1, var2);
   }

   public void unregisterStatusListener(String var1) {
      this.statusDeliverer.unregisterStatusListener(var1);
   }

   public final void start() throws ServiceFailureException {
      this.requestManager = AdminRequestManager.getInstance();
   }
}
