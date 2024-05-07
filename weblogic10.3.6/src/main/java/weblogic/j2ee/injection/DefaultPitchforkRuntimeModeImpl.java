package weblogic.j2ee.injection;

public class DefaultPitchforkRuntimeModeImpl extends AbstractPitchforkRuntimeModeImpl {
   private final boolean usesSpringExtensionModel;

   public DefaultPitchforkRuntimeModeImpl(String var1) {
      this.componentFactoryClassName = this.normalizeComponentFactoryClassName(var1);
      this.usesSpringExtensionModel = "com.oracle.pitchfork.interfaces.SpringComponentFactory".equalsIgnoreCase(var1) || "org.springframework.jee.interfaces.SpringComponentFactory".equalsIgnoreCase(var1);
   }

   public boolean isUsesSpringExtensionModel() {
      return this.usesSpringExtensionModel;
   }
}
