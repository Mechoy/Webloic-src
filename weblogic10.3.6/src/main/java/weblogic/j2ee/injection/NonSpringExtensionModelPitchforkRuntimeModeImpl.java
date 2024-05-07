package weblogic.j2ee.injection;

public class NonSpringExtensionModelPitchforkRuntimeModeImpl extends AbstractPitchforkRuntimeModeImpl {
   public NonSpringExtensionModelPitchforkRuntimeModeImpl(String var1) {
      this.componentFactoryClassName = this.normalizeComponentFactoryClassName(var1);
   }

   public boolean isUsesSpringExtensionModel() {
      return false;
   }
}
