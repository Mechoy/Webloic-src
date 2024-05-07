package weblogic.application;

public class FatalModuleException extends ModuleException {
   static final long serialVersionUID = 1815268793210759069L;

   public FatalModuleException(String var1) {
      super(var1);
   }

   public FatalModuleException(Throwable var1) {
      super(var1);
   }

   public FatalModuleException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
