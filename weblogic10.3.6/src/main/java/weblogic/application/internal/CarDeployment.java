package weblogic.application.internal;

import java.io.File;
import weblogic.application.ApplicationContext;
import weblogic.application.Deployment;
import weblogic.application.RedeployInfo;
import weblogic.deploy.container.DeploymentContext;
import weblogic.management.configuration.AppDeploymentMBean;

final class CarDeployment implements Deployment {
   private final ApplicationContext appCtx;

   CarDeployment(AppDeploymentMBean var1, File var2) {
      this.appCtx = new ApplicationContextImpl(var1, var2);
   }

   public void prepare(DeploymentContext var1) {
   }

   public void activate(DeploymentContext var1) {
   }

   public void deactivate(DeploymentContext var1) {
   }

   public void unprepare(DeploymentContext var1) {
   }

   public void remove(DeploymentContext var1) {
   }

   public void prepareUpdate(DeploymentContext var1) {
   }

   public void activateUpdate(DeploymentContext var1) {
   }

   public void rollbackUpdate(DeploymentContext var1) {
   }

   public void adminToProduction(DeploymentContext var1) {
   }

   public void gracefulProductionToAdmin(DeploymentContext var1) {
   }

   public void forceProductionToAdmin(DeploymentContext var1) {
   }

   public void stop(DeploymentContext var1) {
   }

   public void start(DeploymentContext var1) {
   }

   public RedeployInfo validateRedeploy(DeploymentContext var1) {
      return new RedeployInfoImpl();
   }

   public boolean deregisterCallback(int var1) {
      return false;
   }

   public ApplicationContext getApplicationContext() {
      return this.appCtx;
   }
}
