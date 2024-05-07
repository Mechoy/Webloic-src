package weblogic.j2ee.dd.xml.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidatorHelper {
   public static Class getClass(String var0, ClassLoader var1) throws ClassNotFoundException {
      return Class.forName(var0, false, var1);
   }

   public static Class getClass(String var0) throws ClassNotFoundException {
      return getClass(var0, Thread.currentThread().getContextClassLoader());
   }

   public static List<Method> getMethods(Class var0, String var1) {
      ArrayList var2 = new ArrayList();
      Method[] var3 = var0.getDeclaredMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         if (var6.getName().equals(var1)) {
            var2.add(var6);
         }
      }

      return var2;
   }

   public static Field getField(Class var0, String var1) {
      try {
         return var0.getDeclaredField(var1);
      } catch (NoSuchFieldException var3) {
         return null;
      }
   }

   public static String getSetterName(String var0) {
      char var1 = var0.charAt(0);
      return "set" + ("" + var1).toUpperCase() + var0.substring(1);
   }

   public static String getFieldName(String var0) {
      char var1 = var0.charAt(3);
      return ("" + var1).toLowerCase() + var0.substring(4);
   }
}
