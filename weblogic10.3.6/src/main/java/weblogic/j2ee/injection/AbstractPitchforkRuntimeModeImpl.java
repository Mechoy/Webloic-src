package weblogic.j2ee.injection;

import com.oracle.pitchfork.interfaces.PitchforkRuntimeMode;

public abstract class AbstractPitchforkRuntimeModeImpl implements PitchforkRuntimeMode {
   protected static final String SPRING_COMPONENT_FACTORY_CLASS_NAME = "com.oracle.pitchfork.interfaces.SpringComponentFactory";
   protected static final String OLD_SPRING_COMPONENT_FACTORY_CLASS_NAME = "org.springframework.jee.interfaces.SpringComponentFactory";
   protected String componentFactoryClassName;

   protected AbstractPitchforkRuntimeModeImpl() {
   }

   protected String normalizeComponentFactoryClassName(String var1) {
      return var1 != null && !"com.oracle.pitchfork.interfaces.SpringComponentFactory".equalsIgnoreCase(var1) && !"org.springframework.jee.interfaces.SpringComponentFactory".equalsIgnoreCase(var1) ? var1 : null;
   }

   public String getComponentFactoryClassName() {
      return this.componentFactoryClassName;
   }
}
