package weblogic.application;

import weblogic.management.DeploymentException;

public class ModuleException extends DeploymentException {
   static final long serialVersionUID = 354496553045662647L;

   public ModuleException() {
      super("");
   }

   public ModuleException(String var1) {
      super(var1);
   }

   public ModuleException(Throwable var1) {
      super(var1);
   }

   public ModuleException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
