package weblogic.application;

public abstract class ModuleWrapper implements Module {
   public abstract Module getDelegate();

   public Module unwrap() {
      Module var1;
      for(var1 = this.getDelegate(); var1 instanceof ModuleWrapper; var1 = ((ModuleWrapper)var1).getDelegate()) {
      }

      return var1;
   }
}
