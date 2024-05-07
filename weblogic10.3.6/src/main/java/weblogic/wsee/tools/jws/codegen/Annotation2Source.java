package weblogic.wsee.tools.jws.codegen;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.internal.elements.ClassImpl;

public class Annotation2Source {
   private Annotation2Source() {
   }

   public static String getAnnotationsString(JAnnotation[] var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(getAnnotationString(var0[var2]));
      }

      return var1.toString();
   }

   public static String getAnnotationString(JAnnotation var0) {
      StringBuffer var1 = new StringBuffer();
      String var2 = var0.getQualifiedName();
      if (var2.startsWith("weblogic.") || var2.startsWith("com.bea.") || var2.startsWith("javax.jws.")) {
         var1.append("@" + var2 + "(");
         JAnnotationValue[] var3 = var0.getValues();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1.append(var3[var4].getName());
            var1.append("=");
            var1.append(getValueString(var3[var4]));
            if (var4 < var3.length - 1) {
               var1.append(", ");
            }
         }

         var1.append(")\n");
      }

      return var1.toString();
   }

   private static String getValueString(JAnnotationValue var0) {
      JClass var1 = var0.getType();
      StringBuffer var2 = new StringBuffer();
      if (var1.isArrayType()) {
         getArrayValueString(var2, var0);
      } else if (isEnumType(var0.getType())) {
         var2.append(getSourceTypeName(var1) + "." + var0.asString());
      } else {
         if (var1.isAnnotationType()) {
            throw new Error("NYI - use annotation as annotation value");
         }

         if (String.class.getName().equals(var1.getQualifiedName())) {
            var2.append("\"" + var0.asString() + "\"");
         } else {
            var2.append(var0.asString());
         }
      }

      return var2.toString();
   }

   private static String getSourceTypeName(JClass var0) {
      return var0.getContainingPackage().getQualifiedName() + "." + var0.getSimpleName().replace("$", ".");
   }

   private static void getArrayValueString(StringBuffer var0, JAnnotationValue var1) {
      JClass var2 = var1.getType().getArrayComponentType();
      var0.append("{");
      int var4;
      if (var2.isAnnotationType()) {
         JAnnotation[] var3 = var1.asAnnotationArray();
         if (var3 != null) {
            for(var4 = 0; var4 < var3.length; ++var4) {
               JAnnotation var5 = var3[var4];
               var0.append(getAnnotationString(var5));
               if (var4 < var3.length - 1) {
                  var0.append(", ");
               }
            }
         }
      } else {
         if (isEnumType(var2)) {
            throw new Error("NYI, enum array is not suppported");
         }

         if (!String.class.getName().equals(var2.getQualifiedName())) {
            throw new AssertionError("Annotation array of " + var2.getQualifiedName() + " is not supported.");
         }

         String[] var6 = var1.asStringArray();

         for(var4 = 0; var4 < var6.length; ++var4) {
            var0.append("\"" + var6[var4] + "\"");
            if (var4 < var6.length - 1) {
               var0.append(", ");
            }
         }
      }

      var0.append("}");
   }

   private static boolean isEnumType(JClass var0) {
      return !(var0 instanceof ClassImpl) ? false : ((ClassImpl)var0).isEnumType();
   }
}
