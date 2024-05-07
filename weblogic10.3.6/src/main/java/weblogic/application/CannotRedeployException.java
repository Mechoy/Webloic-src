package weblogic.application;

import weblogic.deploy.container.NonFatalDeploymentException;

public class CannotRedeployException extends NonFatalDeploymentException {
   static final long serialVersionUID = 2960878996294897858L;

   public CannotRedeployException(String var1) {
      super(var1);
   }
}
