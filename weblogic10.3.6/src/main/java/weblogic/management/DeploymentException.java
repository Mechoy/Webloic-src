package weblogic.management;

public class DeploymentException extends ManagementException {
   static final long serialVersionUID = 4025521300339146724L;

   public DeploymentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public DeploymentException(Throwable var1) {
      this("", var1);
   }

   public DeploymentException(String var1) {
      super(var1);
   }
}
