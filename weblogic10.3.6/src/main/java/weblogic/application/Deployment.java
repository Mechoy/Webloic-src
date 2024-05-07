package weblogic.application;

import weblogic.deploy.container.DeploymentContext;
import weblogic.management.DeploymentException;

public interface Deployment {
   void prepare(DeploymentContext var1) throws DeploymentException;

   void activate(DeploymentContext var1) throws DeploymentException;

   void deactivate(DeploymentContext var1) throws DeploymentException;

   void unprepare(DeploymentContext var1) throws DeploymentException;

   void remove(DeploymentContext var1) throws DeploymentException;

   void prepareUpdate(DeploymentContext var1) throws DeploymentException;

   void activateUpdate(DeploymentContext var1) throws DeploymentException;

   void rollbackUpdate(DeploymentContext var1);

   void adminToProduction(DeploymentContext var1) throws DeploymentException;

   void gracefulProductionToAdmin(DeploymentContext var1) throws DeploymentException;

   void forceProductionToAdmin(DeploymentContext var1) throws DeploymentException;

   void start(DeploymentContext var1) throws DeploymentException;

   void stop(DeploymentContext var1) throws DeploymentException;

   ApplicationContext getApplicationContext();

   public interface AdminModeCallback {
      void completed();

      void waitForCompletion(long var1);
   }
}
