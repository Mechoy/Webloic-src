package weblogic.ejb.container.cmp.rdbms.codegen;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.persistence.spi.CMPCodeGenerator;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.utils.Getopt2;

public abstract class BaseCodeGenerator extends CMPCodeGenerator {
   protected static final DebugLogger debugLogger;
   public static String EOL;

   public String varPrefix() {
      return "__WL_";
   }

   protected BaseCodeGenerator(Getopt2 var1) {
      super(var1);
   }

   protected String lookupConvert(Class var1) {
      if (var1.equals(Boolean.class)) {
         return ".booleanValue()";
      } else if (var1.equals(Integer.class)) {
         return ".intValue()";
      } else if (var1.equals(Float.class)) {
         return ".floatValue()";
      } else if (var1.equals(Double.class)) {
         return ".doubleValue()";
      } else if (var1.equals(Long.class)) {
         return ".longValue()";
      } else if (var1.equals(Short.class)) {
         return ".shortValue()";
      } else if (var1.equals(Character.class)) {
         return ".charValue()";
      } else {
         return var1.equals(Byte.class) ? ".byteValue()" : "";
      }
   }

   protected String perhapsConvert(Class var1, Class var2, String var3) {
      String var4 = null;
      if (var1.equals(var2)) {
         var4 = var3;
      } else if (var1.isPrimitive()) {
         assert ClassUtils.getObjectClass(var1).equals(var2);

         var4 = var3 + this.lookupConvert(var2);
      } else {
         assert var2.isPrimitive();

         assert var1.equals(ClassUtils.getObjectClass(var2));

         var4 = "new " + this.javaCodeForType(ClassUtils.getObjectClass(var2)) + "(" + var3 + ")";
      }

      return var4;
   }

   protected String perhapsConvertPrimitive(Class var1, String var2) {
      String var3 = null;
      if (var1.isPrimitive()) {
         var3 = "new " + this.javaCodeForType(ClassUtils.getObjectClass(var1)) + "(" + var2 + ")";
      } else {
         var3 = var2;
      }

      return var3;
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
      EOL = "\n";
   }
}
