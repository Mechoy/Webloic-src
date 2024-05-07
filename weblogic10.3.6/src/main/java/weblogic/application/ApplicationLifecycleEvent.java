package weblogic.application;

public class ApplicationLifecycleEvent {
   private final DeploymentOperationType deploymentOperation;
   private final boolean isStatic;
   private ApplicationContext context;

   /** @deprecated */
   public ApplicationLifecycleEvent(ApplicationContext var1) {
      this.context = var1;
      this.isStatic = false;
      this.deploymentOperation = null;
   }

   public ApplicationLifecycleEvent(ApplicationContext var1, DeploymentOperationType var2, boolean var3) {
      this.context = var1;
      this.deploymentOperation = var2;
      this.isStatic = var3;
   }

   public ApplicationContext getApplicationContext() {
      return this.context;
   }

   public String toString() {
      return "ApplicationLifecycleEvent{" + this.context + "}";
   }

   public DeploymentOperationType getDeploymentOperation() {
      return this.deploymentOperation;
   }

   public boolean isStaticOperation() {
      return this.isStatic;
   }
}
