package weblogic.deploy.container;

import weblogic.management.DeploymentException;

public class NonFatalDeploymentException extends DeploymentException {
   private static final long serialVersionUID = 321088334582734288L;

   public NonFatalDeploymentException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public NonFatalDeploymentException(Throwable var1) {
      super(var1);
   }

   public NonFatalDeploymentException(String var1) {
      super(var1);
   }
}
