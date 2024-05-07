package weblogic.application.internal;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.Deployment;
import weblogic.application.Module;
import weblogic.application.ModuleLocationInfo;
import weblogic.application.ModuleManager;
import weblogic.application.ModuleWrapper;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.StateChange;
import weblogic.application.utils.StateChangeException;
import weblogic.application.utils.StateMachineDriver;
import weblogic.deploy.container.DeploymentContext;
import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.j2ee.J2EELogger;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StringUtils;

abstract class BaseDeployment implements Deployment {
   private final StateMachineDriver driver = new StateMachineDriver();
   String asString = null;
   protected final ApplicationContextImpl appCtx;
   private static final StateChange prepareStateChange = new StateChange() {
      public String toString() {
         return "prepare";
      }

      public void next(Object var1) throws Exception {
         ((Flow)var1).prepare();
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).unprepare();
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringUndeploymentError(var1.getCause());
      }
   };
   private static final StateChange activateStateChange = new StateChange() {
      public String toString() {
         return "activate";
      }

      public void next(Object var1) throws Exception {
         ((Flow)var1).activate();
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).deactivate();
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringUndeploymentError(var1.getCause());
      }
   };
   private static final StateChange removeStateChange = new StateChange() {
      public String toString() {
         return "remove";
      }

      public void next(Object var1) throws Exception {
         throw new AssertionError("someone is transitioning up to remove!");
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).remove();
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringUndeploymentError(var1.getCause());
      }
   };
   private static final StateChange adminStateChange = new StateChange() {
      public String toString() {
         return "admin";
      }

      public void next(Object var1) throws Exception {
         ((Flow)var1).adminToProduction();
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).forceProductionToAdmin(new AdminModeCompletionBarrier(EarUtils.noopAdminModeCallback));
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringAdminModeErrro(var1.getCause());
      }
   };

   public BaseDeployment(AppDeploymentMBean var1, File var2) throws DeploymentException {
      this.initString(var1);
      this.appCtx = new ApplicationContextImpl(var1, var2);
   }

   public BaseDeployment(SystemResourceMBean var1, File var2) throws DeploymentException {
      this.initString(var1);
      this.appCtx = new ApplicationContextImpl(var1, var2);
   }

   private void initString(BasicDeploymentMBean var1) {
      this.asString = "[" + this.getClass().getName() + "] name: " + var1.getName() + " path: " + var1.getSourcePath();
   }

   public String toString() {
      return this.asString;
   }

   protected void throwAppException(Throwable var1) throws DeploymentException {
      if (var1 instanceof DeploymentException) {
         throw (DeploymentException)var1;
      } else {
         if (var1 instanceof ErrorCollectionException) {
            ErrorCollectionException var2 = (ErrorCollectionException)var1;
            if (var2.size() == 1) {
               this.throwAppException((Throwable)var2.getErrors().next());
            }
         }

         throw new DeploymentException(var1);
      }
   }

   protected abstract Flow[] getFlow();

   private ClassLoader pushLoader() {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      var1.setContextClassLoader(this.appCtx.getAppClassLoader());
      return var2;
   }

   private void popLoader(ClassLoader var1) {
      Thread.currentThread().setContextClassLoader(var1);
   }

   private void propagateDeploymentContext(DeploymentContext var1) {
      if (var1 != null) {
         this.appCtx.setProposedDomain(var1.getProposedDomain());
         this.appCtx.setDeploymentInitiator(var1.getInitiator());
         this.appCtx.setRequiresRestart(var1.requiresRestart());
         this.appCtx.setDeploymentOperation(var1.getDeploymentOperation());
         this.appCtx.setStaticDeploymentOperation(var1.isStaticDeploymentOperation());
         if (var1.isStaticDeploymentOperation()) {
            this.appCtx.setStoppedModules(var1.getStoppedModules());
         }

         ApplicationVersionUtils.setAdminModeAppCtxParam(this.appCtx, var1.isAdminModeTransition());
         ApplicationVersionUtils.setIgnoreSessionsAppCtxParam(this.appCtx, var1.isIgnoreSessionsEnabled());
         ApplicationVersionUtils.setRMIGracePeriodAppCtxParam(this.appCtx, var1.getRMIGracePeriodSecs());
      }
   }

   private void resetDeploymentContext() {
      this.appCtx.setProposedDomain((DomainMBean)null);
      this.appCtx.setDeploymentInitiator((AuthenticatedSubject)null);
      this.appCtx.setDeploymentOperation(-1);
      ApplicationVersionUtils.unsetAdminModeAppCtxParam(this.appCtx);
      ApplicationVersionUtils.unsetIgnoreSessionsAppCtxParam(this.appCtx);
      ApplicationVersionUtils.unsetRMIGracePeriodAppCtxParam(this.appCtx);
   }

   private String[] getUpdatedResourceURIs(DeploymentContext var1) {
      String[] var2 = new String[0];
      if (var1 != null && var1.getUpdatedResourceURIs() != null) {
         var2 = EarUtils.toModuleIds(this.appCtx, var1.getUpdatedResourceURIs());
      }

      return var2;
   }

   public void prepare(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("prepare " + this.appCtx.getApplicationId());
      }

      ClassLoader var2 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.nextState(prepareStateChange, this.getFlow());
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var2);
      }

   }

   public void activate(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("activate " + this.appCtx.getApplicationId());
      }

      ClassLoader var2 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.nextState(activateStateChange, this.getFlow());
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var2);
      }

   }

   public void deactivate(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("deactivate " + this.appCtx.getApplicationId());
      }

      ClassLoader var2 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.previousState(activateStateChange, this.getFlow());
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var2);
      }

   }

   public void unprepare(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("unprepare " + this.appCtx.getApplicationId());
      }

      ClassLoader var2 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.previousState(prepareStateChange, this.getFlow());
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      } finally {
         this.appCtx.clear();
         this.resetDeploymentContext();
         this.popLoader(var2);
      }

   }

   public void remove(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("remove " + this.appCtx.getApplicationId());
      }

      ClassLoader var2 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.previousState(removeStateChange, this.getFlow());
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var2);
      }

   }

   private void validateRedeploy(DeploymentContext var1) throws DeploymentException {
      String[] var2 = this.getUpdatedResourceURIs(var1);
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("validateRedeploy " + this.appCtx.getApplicationId() + " uris --> " + StringUtils.join(var2, ","));
      }

      if (var2.length != 0) {
         ClassLoader var3 = this.pushLoader();

         try {
            this.driver.nextState(new ValidateRedeployStateChange(var1), this.getFlow());
         } catch (StateChangeException var9) {
            this.throwAppException(var9.getCause());
         } finally {
            this.popLoader(var3);
            this.appCtx.setAdditionalModuleUris(Collections.EMPTY_MAP);
            this.appCtx.setProposedPartialRedeployDDs((AppDDHolder)null);
         }

      }
   }

   public void start(DeploymentContext var1) throws DeploymentException {
      String[] var2 = this.getUpdatedResourceURIs(var1);
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("start " + this.appCtx.getApplicationId() + " uris --> " + StringUtils.join(var2, ","));
      }

      if (!this.appCtx.isRedeployOperation()) {
         ModuleManager var3 = this.appCtx.getModuleManager();
         if (var3.validateModuleIds(var2)) {
            throw new NonFatalDeploymentException(J2EELogger.logModulesAlreadyRunningErrorLoggable(StringUtils.join(var3.getValidModuleIds(var2), ",")).getMessage());
         }
      }

      Flow[] var11 = this.getFlow();
      ClassLoader var4 = this.pushLoader();
      if (this.appCtx.getPartialRedeployURIs() != null) {
         var2 = this.appCtx.getPartialRedeployURIs();
      }

      this.appCtx.setPartialRedeployURIs(var2);

      try {
         this.propagateDeploymentContext(var1);
         Object var5 = null;

         for(int var6 = 0; var6 < var11.length; ++var6) {
            var11[var6].start(var2);
         }

         if (var5 != null) {
            this.throwAppException((Throwable)var5);
         }
      } finally {
         if (this.appCtx.getStartingModules() != null && this.appCtx.getStartingModules().length > 0) {
            this.appCtx.setStartingModules(new Module[0]);
         }

         this.appCtx.setPartialRedeployURIs((String[])null);
         this.resetDeploymentContext();
         this.popLoader(var4);
      }

   }

   public void stop(DeploymentContext var1) throws DeploymentException {
      this.setModuleURItoModuleIdMap();
      String[] var2 = this.getUpdatedResourceURIs(var1);
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("stop " + this.appCtx.getApplicationId() + " uris --> " + StringUtils.join(var2, ","));
      }

      this.propagateDeploymentContext(var1);
      this.validateRedeploy(var1);
      Flow[] var3 = this.getFlow();
      ClassLoader var4 = this.pushLoader();

      try {
         ErrorCollectionException var5 = null;
         if (this.appCtx.getPartialRedeployURIs() != null) {
            var2 = this.appCtx.getPartialRedeployURIs();
         }

         for(int var6 = var3.length - 1; var6 >= 0; --var6) {
            try {
               var3[var6].stop(var2);
            } catch (Throwable var12) {
               if (var5 == null) {
                  var5 = new ErrorCollectionException();
               }

               var5.addError(var12);
            }
         }

         if (var5 != null) {
            this.throwAppException(var5);
         }
      } finally {
         if (this.appCtx.isStopOperation()) {
            if (this.appCtx.getStoppingModules() != null && this.appCtx.getStoppingModules().length > 0) {
               this.appCtx.setStoppingModules(new Module[0]);
            }

            this.appCtx.setPartialRedeployURIs((String[])null);
         }

         this.resetDeploymentContext();
         this.popLoader(var4);
      }

   }

   public void prepareUpdate(DeploymentContext var1) throws DeploymentException {
      String[] var2 = this.getUpdatedResourceURIs(var1);
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("prepareUpdate " + this.appCtx.getApplicationId() + " uris --> " + StringUtils.join(var2, ","));
      }

      ClassLoader var3 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.nextState(new PrepareUpdateStateChange(var2), this.getFlow());
      } catch (StateChangeException var9) {
         this.throwAppException(var9.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var3);
      }

   }

   public void activateUpdate(DeploymentContext var1) throws DeploymentException {
      String[] var2 = this.getUpdatedResourceURIs(var1);
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("activateUpdate " + this.appCtx.getApplicationId() + " uris --> " + StringUtils.join(var2, ","));
      }

      ClassLoader var3 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.nextState(new ActivateUpdateStateChange(var2), this.getFlow());
      } catch (StateChangeException var9) {
         this.throwAppException(var9.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var3);
      }

      this.updateAppDeploymentBean(var1);
   }

   private void updateAppDeploymentBean(DeploymentContext var1) {
      if (var1 != null && var1.getProposedDomain() != null) {
         AppDeploymentMBean var2 = var1.getProposedDomain().lookupAppDeployment(this.appCtx.getApplicationId());
         this.appCtx.setUpdatedAppDeploymentMBean(var2);
      }

   }

   public void rollbackUpdate(DeploymentContext var1) {
      String[] var2 = this.getUpdatedResourceURIs(var1);
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("rollbackUpdate " + this.appCtx.getApplicationId() + " uris --> " + StringUtils.join(var2, ","));
      }

      ClassLoader var3 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.previousState(new PrepareUpdateStateChange(var2), this.getFlow());
      } catch (StateChangeException var9) {
         J2EELogger.logIgnoringRollbackUpdateError(this.appCtx.getApplicationId(), var9.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var3);
      }

   }

   public void adminToProduction(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("adminToProduction " + this.appCtx.getApplicationId());
      }

      ClassLoader var2 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.nextState(adminStateChange, this.getFlow());
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var2);
      }

   }

   public void forceProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("forceProductionToAdmin " + this.appCtx.getApplicationId());
      }

      Deployment.AdminModeCallback var2 = null;
      if (var1 != null) {
         var2 = var1.getAdminModeCallback();
      }

      ClassLoader var3 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.previousState(new ForceProdToAdminStateChange(var2), this.getFlow());
      } catch (StateChangeException var9) {
         this.throwAppException(var9.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var3);
      }

   }

   public void gracefulProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      if (EarUtils.isDebugOn()) {
         EarUtils.debug("gracefulProductionToAdmin " + this.appCtx.getApplicationId());
      }

      Deployment.AdminModeCallback var2 = null;
      if (var1 != null) {
         var2 = var1.getAdminModeCallback();
      }

      ClassLoader var3 = this.pushLoader();

      try {
         this.propagateDeploymentContext(var1);
         this.driver.previousState(new GracefulProdToAdminStateChange(var2), this.getFlow());
      } catch (StateChangeException var9) {
         this.throwAppException(var9.getCause());
      } finally {
         this.resetDeploymentContext();
         this.popLoader(var3);
      }

   }

   private void setModuleURItoModuleIdMap() {
      HashMap var1 = new HashMap();
      Module[] var2 = this.appCtx.getApplicationModules();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Module var5 = var2[var4];
         if (var5 instanceof ModuleWrapper) {
            var5 = ((ModuleWrapper)var5).unwrap();
         }

         String var6 = var5.getId();
         if (var5 instanceof ModuleLocationInfo) {
            var1.put(((ModuleLocationInfo)var5).getModuleURI(), var6);
         }

         var1.put(var6, var6);
      }

      this.appCtx.setModuleURItoIdMap(var1);
   }

   public ApplicationContext getApplicationContext() {
      return this.appCtx;
   }

   private static final class ValidateRedeployStateChange implements StateChange {
      private final DeploymentContext deplCtx;

      ValidateRedeployStateChange(DeploymentContext var1) {
         this.deplCtx = var1;
      }

      public String toString() {
         return "validateRedeploy";
      }

      public void next(Object var1) throws DeploymentException {
         ((Flow)var1).validateRedeploy(this.deplCtx);
      }

      public void previous(Object var1) {
      }

      public void logRollbackError(StateChangeException var1) {
      }
   }

   private static final class ForceProdToAdminStateChange extends AdminCallbackStateChange implements StateChange {
      ForceProdToAdminStateChange(Deployment.AdminModeCallback var1) {
         super(var1);
      }

      public String toString() {
         return "forceProdToAdmin";
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).forceProductionToAdmin(this.barrier);
      }
   }

   private static final class GracefulProdToAdminStateChange extends AdminCallbackStateChange implements StateChange {
      public String toString() {
         return "gracefulProdToAdmin";
      }

      GracefulProdToAdminStateChange(Deployment.AdminModeCallback var1) {
         super(var1);
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).gracefulProductionToAdmin(this.barrier);
      }
   }

   private abstract static class AdminCallbackStateChange implements StateChange {
      protected final AdminModeCompletionBarrier barrier;

      AdminCallbackStateChange(Deployment.AdminModeCallback var1) {
         this.barrier = new AdminModeCompletionBarrier(var1);
      }

      public String toString() {
         return "adminCallback";
      }

      public void next(Object var1) throws Exception {
         throw new AssertionError("should not be called");
      }

      public abstract void previous(Object var1) throws Exception;

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringAdminModeErrro(var1.getCause());
      }
   }

   private static final class ActivateUpdateStateChange implements StateChange {
      private String[] updateURIs;

      ActivateUpdateStateChange(String[] var1) {
         this.updateURIs = var1;
      }

      public String toString() {
         return "activateUpdate";
      }

      public void next(Object var1) throws Exception {
         ((Flow)var1).activateUpdate(this.updateURIs);
      }

      public void previous(Object var1) throws Exception {
      }

      public void logRollbackError(StateChangeException var1) {
      }
   }

   private static final class PrepareUpdateStateChange implements StateChange {
      private String[] updateURIs;

      PrepareUpdateStateChange(String[] var1) {
         this.updateURIs = var1;
      }

      public String toString() {
         return "prepareUpdate";
      }

      public void next(Object var1) throws Exception {
         ((Flow)var1).prepareUpdate(this.updateURIs);
      }

      public void previous(Object var1) throws Exception {
         ((Flow)var1).rollbackUpdate(this.updateURIs);
      }

      public void logRollbackError(StateChangeException var1) {
         J2EELogger.logIgnoringUndeploymentError(var1.getCause());
      }
   }
}
