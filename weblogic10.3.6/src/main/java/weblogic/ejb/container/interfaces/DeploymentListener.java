package weblogic.ejb.container.interfaces;

public interface DeploymentListener {
   void onBeforeDeploy();

   void onAfterDeploy();
}
