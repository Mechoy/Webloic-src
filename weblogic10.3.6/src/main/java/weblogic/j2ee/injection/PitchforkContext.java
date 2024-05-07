package weblogic.j2ee.injection;

import com.oracle.pitchfork.interfaces.PitchforkRuntimeMode;
import com.oracle.pitchfork.interfaces.PitchforkUtils;
import weblogic.utils.AssertionError;

public class PitchforkContext {
   private static final String SPRING_COMPONENT_FACTORY_CLASS_NAME = "com.oracle.pitchfork.interfaces.SpringComponentFactory";
   private static final String OLD_SPRING_COMPONENT_FACTORY_CLASS_NAME = "org.springframework.jee.interfaces.SpringComponentFactory";
   private PitchforkUtils pUtils;
   private boolean isWLSComponentFactoryClassName;
   private boolean isSpringComponentFactoryClassName;
   private String componentFactoryClassName;
   private final PitchforkRuntimeMode pitchforkRuntimeMode;

   public PitchforkContext(String var1) {
      this.componentFactoryClassName = var1;
      this.isWLSComponentFactoryClassName = var1 == null;
      this.isSpringComponentFactoryClassName = "com.oracle.pitchfork.interfaces.SpringComponentFactory".equalsIgnoreCase(var1) || "org.springframework.jee.interfaces.SpringComponentFactory".equalsIgnoreCase(var1);
      this.pitchforkRuntimeMode = PitchforkRuntimeModeFactory.createPitchforkRuntimeMode(var1);
   }

   public boolean isWLSComponentFactoryClassName() {
      return this.isWLSComponentFactoryClassName;
   }

   public boolean isSpringComponentFactoryClassName() {
      return this.isSpringComponentFactoryClassName;
   }

   public String getComponentFactoryClassName() {
      return this.componentFactoryClassName;
   }

   public static String getComponentFactoryClassName(String var0) {
      return var0 != null && !"com.oracle.pitchfork.interfaces.SpringComponentFactory".equalsIgnoreCase(var0) && !"org.springframework.jee.interfaces.SpringComponentFactory".equalsIgnoreCase(var0) ? var0 : null;
   }

   public PitchforkUtils getPitchforkUtils() {
      if (this.pUtils != null) {
         return this.pUtils;
      } else {
         Class var1 = this.getTargetClass("com.oracle.pitchfork.spi.PitchforkUtilsImpl");
         if (var1 != null) {
            try {
               this.pUtils = (PitchforkUtils)var1.newInstance();
               return this.pUtils;
            } catch (Throwable var3) {
               throw new AssertionError("Couldn't create instance for class " + var1.getName(), var3);
            }
         } else {
            return null;
         }
      }
   }

   private Class getTargetClass(String var1) {
      try {
         if (var1 != null) {
            Class var2 = Class.forName(var1);
            return var2;
         } else {
            return null;
         }
      } catch (Throwable var4) {
         throw new AssertionError("The following exception is thrown during loading class " + var1 + ": ", var4);
      }
   }

   public PitchforkRuntimeMode getPitchforkRuntimeMode() {
      return this.pitchforkRuntimeMode;
   }
}
