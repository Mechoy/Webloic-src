package weblogic.application.internal.flow;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.ExceptionUtils;
import weblogic.deploy.container.DeploymentContext;
import weblogic.management.DeploymentException;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public abstract class BaseFlow implements Flow {
   private static final boolean VERBOSE = false;
   protected final FlowContext appCtx;

   protected BaseFlow(ApplicationContextInternal var1) {
      this.appCtx = (FlowContext)var1;
   }

   public String toString() {
      return this.getClass().getName() + "(" + this.appCtx.toString() + ")";
   }

   protected void throwAppException(Throwable var1) throws DeploymentException {
      ExceptionUtils.throwDeploymentException(var1);
   }

   protected static void log(String var0) {
      Debug.say(var0);
   }

   protected void log(String var1, Throwable var2) {
      Debug.say(var1 + StackTraceUtils.throwable2StackTrace(var2));
   }

   public void remove() throws DeploymentException {
   }

   public void prepare() throws DeploymentException {
   }

   public void activate() throws DeploymentException {
   }

   public void deactivate() throws DeploymentException {
   }

   public void unprepare() throws DeploymentException {
   }

   public void start(String[] var1) throws DeploymentException {
   }

   public void stop(String[] var1) throws DeploymentException {
   }

   public void prepareUpdate(String[] var1) throws DeploymentException {
   }

   public void activateUpdate(String[] var1) throws DeploymentException {
   }

   public void rollbackUpdate(String[] var1) throws DeploymentException {
   }

   public void adminToProduction() throws DeploymentException {
   }

   public void forceProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
   }

   public void validateRedeploy(DeploymentContext var1) throws DeploymentException {
   }
}
