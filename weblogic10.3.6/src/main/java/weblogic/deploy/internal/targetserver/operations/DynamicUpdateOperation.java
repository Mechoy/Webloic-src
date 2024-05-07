package weblogic.deploy.internal.targetserver.operations;

import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.application.ModuleListener;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.targetserver.AppDeployment;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.datamanagement.DataUpdateRequestInfo;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.DeployerRuntimeTextTextFormatter;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DynamicUpdateOperation extends AbstractOperation {
   private String[] files = new String[0];
   private final Map subModuleTargets;
   private DeploymentPlanBean dpb = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public DynamicUpdateOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      if (this.deploymentData == null || !this.deploymentData.hasFiles() && !this.deploymentData.hasSubModuleTargets()) {
         throw new AssertionError();
      } else {
         if (this.deploymentData.hasFiles()) {
            this.files = this.deploymentData.getFiles();
         } else {
            this.files = null;
         }

         this.subModuleTargets = this.deploymentData.getAllSubModuleTargets();
         this.appcontainer = this.getApplication().findDeployment();
         this.operation = var4 == null ? 9 : var4.getDeploymentOperation();
      }
   }

   protected void compatibilityProcessor() throws DeploymentException {
      MBeanConverter.reconcile81MBeans(this.deploymentData, (AppDeploymentMBean)this.mbean);
   }

   public AbstractOperation refine() throws DeploymentException {
      if (this.isAppContainerActive(this.appcontainer)) {
         return this;
      } else {
         this.internalDeploymentData.setDeploymentOperation(6);
         return new RedeployOperation(this.requestId, this.taskId, this.internalDeploymentData, this.mbean, this.proposedDomain, this.initiator, this.requiresRestart);
      }
   }

   public void doPrepare() throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("DynamicUpdateOperation: prepare called.");
      }

      try {
         this.commitDataUpdate();
         this.setupPrepare();
         this.ensureAppContainerSet();
         if (this.isASystemResourceRequiringRestart()) {
            if (this.isDebugEnabled()) {
               this.debug("System Resource '" + this.mbean.getName() + "' requires a " + "restart for the changes to take effect - prepare returning " + "without further validation");
            }

            if (this.getState() != null) {
               this.getState().setCurrentState(ModuleListener.STATE_UPDATE_PENDING.toString());
            }

         } else {
            if (!this.subModuleTargets.isEmpty()) {
               this.convertSubModuleTargetsToFiles();
            }

            this.initializeDeploymentPlan();
            if (this.isDebugEnabled()) {
               this.debug("Files: " + Arrays.asList(this.files));
            }

            this.deploymentContext.setUpdatedResourceURIs(this.files);
            this.appcontainer.prepareUpdate(this.deploymentContext);
         }
      } catch (Throwable var3) {
         this.silentCancelOnPrepareFailure();
         DeploymentException var2 = DeployHelper.convertThrowable(var3);
         this.complete(2, var2);
         throw var2;
      }
   }

   private void initializeDeploymentPlan() throws DeploymentException {
      if (this.isAppDeployment() && this.operation == 10 && this.mbean != null) {
         this.dpb = ((AppDeployment)this.getApplication()).parsePlan();
         ((AppDeploymentMBean)this.mbean).setDeploymentPlanDescriptor(this.dpb);
      }

   }

   private void convertSubModuleTargetsToFiles() {
      ArrayList var1 = new ArrayList();
      if (this.files != null) {
         var1.addAll(Arrays.asList((Object[])this.files));
      }

      Iterator var2 = this.subModuleTargets.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Map var4 = (Map)this.subModuleTargets.get(var3);
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var3.equals("_the_standalone_module")) {
               var1.add(var6);
            } else {
               var1.add(var3 + '/' + var6);
            }
         }
      }

      this.files = (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   private final boolean isASystemResourceRequiringRestart() {
      if (this.mbean instanceof SystemResourceMBean) {
         ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         if (var1.isRestartRequired() || var1.isRestartPendingForSystemResource(this.mbean.getName())) {
            return true;
         }
      }

      return false;
   }

   protected void doCommit() throws IOException, DeploymentException {
      if (this.isASystemResourceRequiringRestart()) {
         if (this.isDebugEnabled()) {
            this.debug("System Resource '" + this.mbean.getName() + "' requires a " + "restart for the changes to take effect - commit returning " + "without proceeding further");
         }

         if (this.getState() != null) {
            this.getState().setCurrentState(ModuleListener.STATE_ACTIVE.toString());
         }
      } else {
         this.deploymentContext.setUpdatedResourceURIs(this.files);
         if (this.isDebugEnabled()) {
            this.debug("DynamicUpdateOperation: Invoking activateUpdate() on Container.");
         }

         this.appcontainer.activateUpdate(this.deploymentContext);
      }

      this.complete(3, (Exception)null);
   }

   protected final void doCancel() {
      if (this.appcontainer != null) {
         this.cancelDataUpdate();
         int var1 = this.getState(this.appcontainer);
         if (var1 == 4) {
            this.deploymentContext.setUpdatedResourceURIs(this.files);
            if (this.isDebugEnabled()) {
               this.debug("DynamicUpdateOperation: Invoking rollbackUpdate() on Container.");
            }

            this.appcontainer.rollbackUpdate(this.deploymentContext);
            if (this.isDebugEnabled()) {
               this.debug("DynamicUpdateOperation: rollbackUpdate() on Container finished.");
            }
         }

      }
   }

   protected void initDataUpdate() throws DeploymentException {
      try {
         final ArrayList var1 = new ArrayList();
         if (this.files != null) {
            var1.addAll(Arrays.asList(this.files));
         }

         if (this.isDebugEnabled()) {
            this.debug("DynamicUpdateOperation.initDataUpdate: delta-files : " + var1);
         }

         final boolean var2 = this.deploymentData.isPlanUpdate();
         if (var2) {
            if (!(this.getApplication() instanceof AppDeployment)) {
               throw new DeploymentException("PlanUpdate cannot be applied for SystemResources");
            }

            AppDeployment var3 = (AppDeployment)this.getApplication();
            var3.updateDescriptorsPathInfo();
            String var4 = var3.getRelativePlanPath();
            if (var4 == null || var4.length() == 0) {
               throw new DeploymentException("Application " + var3.getName() + " does not contain plan path to update");
            }

            if (this.isDebugEnabled()) {
               this.debug("DynamicUpdateOperation: deltaFiles is : " + var1 + " : " + var1.getClass().getName());
            }

            var1.add(var4);
         }

         if (!var1.isEmpty()) {
            final boolean var6 = this.deploymentData != null && this.deploymentData.getDelete();
            this.getApplication().initDataUpdate(new DataUpdateRequestInfo() {
               public List getDeltaFiles() {
                  return var1;
               }

               public long getRequestId() {
                  return DynamicUpdateOperation.this.requestId;
               }

               public boolean isStatic() {
                  return false;
               }

               public boolean isDelete() {
                  return var6;
               }

               public boolean isPlanUpdate() {
                  return var2;
               }
            });
         }
      } catch (IOException var5) {
         var5.printStackTrace();
         throw new DeploymentException("Error occured while initiating data update", var5);
      }
   }

   protected void ensureAppContainerSet() throws DeploymentException {
      super.ensureAppContainerSet();
      if (this.appcontainer == null) {
         Loggable var1 = DeployerRuntimeLogger.logNullAppLoggable(this.mbean.getName(), DeployerRuntimeTextTextFormatter.getInstance().messageRedeploy());
         var1.log();
         DeploymentException var2 = new DeploymentException(var1.getMessage());
         this.complete(2, var2);
         throw var2;
      }
   }
}
