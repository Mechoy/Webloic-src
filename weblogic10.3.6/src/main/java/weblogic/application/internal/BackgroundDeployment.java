package weblogic.application.internal;

import weblogic.application.ApplicationContext;
import weblogic.application.Deployment;
import weblogic.deploy.container.DeploymentContext;
import weblogic.management.DeploymentException;

public class BackgroundDeployment implements Deployment {
   private final Deployment delegate;
   private boolean completedDeployment;
   private boolean startedDeployment;

   BackgroundDeployment(Deployment var1) {
      this.delegate = var1;
   }

   Deployment getDelegate() {
      return this.delegate;
   }

   void setCompletedDeployment(boolean var1) {
      this.completedDeployment = var1;
   }

   boolean getCompletedDeployment() {
      return this.completedDeployment;
   }

   void setStartedDeployment(boolean var1) {
      this.startedDeployment = var1;
   }

   boolean getStartedDeployment() {
      return this.startedDeployment;
   }

   public void prepare(DeploymentContext var1) {
      this.completedDeployment = false;
      BackgroundDeploymentService.addBackgroundDeployment(this, var1);
   }

   public void activate(DeploymentContext var1) {
   }

   public void adminToProduction(DeploymentContext var1) {
   }

   public void gracefulProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.gracefulProductionToAdmin(var1);
      } else {
         Deployment.AdminModeCallback var2 = null;
         if (var1 != null) {
            var2 = var1.getAdminModeCallback();
         }

         if (var2 != null) {
            var2.completed();
         }
      }

   }

   public void forceProductionToAdmin(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.forceProductionToAdmin(var1);
      }

   }

   public void deactivate(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.deactivate(var1);
      }

   }

   public void unprepare(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.unprepare(var1);
      }

   }

   public void remove(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.remove(var1);
      }

   }

   public void prepareUpdate(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.prepareUpdate(var1);
      }

   }

   public void activateUpdate(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.activateUpdate(var1);
      }

   }

   public void rollbackUpdate(DeploymentContext var1) {
      if (this.completedDeployment) {
         this.delegate.rollbackUpdate(var1);
      }

   }

   public void start(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.start(var1);
      }

   }

   public void stop(DeploymentContext var1) throws DeploymentException {
      if (this.completedDeployment) {
         this.delegate.stop(var1);
      }

   }

   public ApplicationContext getApplicationContext() {
      return this.delegate.getApplicationContext();
   }
}
