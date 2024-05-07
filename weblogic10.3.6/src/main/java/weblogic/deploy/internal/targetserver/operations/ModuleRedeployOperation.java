package weblogic.deploy.internal.targetserver.operations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.datamanagement.ModuleRedeployDataUpdateRequestInfo;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class ModuleRedeployOperation extends ActivateOperation {
   private final String[] moduleIds;

   public ModuleRedeployOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, String[] var7, AuthenticatedSubject var8, boolean var9) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var8, var9);
      this.moduleIds = var7;
      this.appcontainer = this.getApplication().findDeployment();
      this.operation = 9;
   }

   protected void compatibilityProcessor() throws DeploymentException {
      MBeanConverter.reconcile81MBeans(this.deploymentData, (AppDeploymentMBean)this.mbean);
   }

   protected void doPrepare() throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("ModuleRedeployOperation: prepare called.");
      }

      this.ensureAppContainerSet();
      if (this.appcontainer == null) {
         super.doPrepare();
      } else {
         if (!this.isAppContainerActive(this.appcontainer)) {
            Loggable var1 = SlaveDeployerLogger.logInvalidStateForRedeployLoggable(this.getApplication().getName());
            var1.log();
            DeploymentException var2 = new DeploymentException(var1.getMessage());
            this.complete(2, var2);
            throw var2;
         }

         this.moduleLevelRedeploy();
      }

   }

   protected final void doCancel() {
      if (this.appcontainer != null) {
         if (this.isDebugEnabled()) {
            this.debug("ModuleRedeployOperation: Invoking undeploy on Container.");
         }

         this.silentProductionToAdmin(this.appcontainer);
         this.silentDeactivate(this.appcontainer);
         this.silentUnprepare(this.appcontainer);
         this.silentRemove(this.appcontainer);
         if (this.isDebugEnabled()) {
            this.debug("ModuleRedeployOperation: undeploy on Container finished.");
         }
      }

      this.getApplication().remove(false);
   }

   protected void doCommit() throws DeploymentException, IOException {
      this.appcontainer = this.getApplication().findDeployment();
      if (this.appcontainer != null && this.getState(this.appcontainer) == 1) {
         super.doCommit();
      } else {
         this.complete(3, (Exception)null);
      }

   }

   public void initDataUpdate() throws DeploymentException {
      try {
         ArrayList var1 = new ArrayList();
         var1.addAll(Arrays.asList(this.moduleIds));
         this.getApplication().initDataUpdate(new ModuleRedeployDataUpdateRequestInfo(var1, this.requestId));
      } catch (Throwable var2) {
         var2.printStackTrace();
         throw DeployHelper.convertThrowable(var2);
      }
   }

   private void moduleLevelRedeploy() throws DeploymentException {
      try {
         this.deploymentContext.setUpdatedResourceURIs(this.moduleIds);
         this.appcontainer.stop(this.deploymentContext);
         this.commitDataUpdate();
         this.setupPrepare();
         this.appcontainer.start(this.deploymentContext);
      } catch (Throwable var3) {
         if (!(var3 instanceof NonFatalDeploymentException)) {
            this.silentCancelOnPrepareFailure();
         }

         DeploymentException var2 = DeployHelper.convertThrowable(var3);
         this.complete(2, var2);
         throw var2;
      }
   }
}
