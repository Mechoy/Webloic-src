package weblogic.management.mbeanservers.compatibility.internal;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class TypesHelper {
   private static Map autoNameMap = new HashMap();

   public static synchronized String assignAutoName(String var0) {
      Integer var1 = (Integer)autoNameMap.get(var0);
      if (var1 == null) {
         var1 = new Integer(0);
      } else {
         var1 = new Integer(var1 + 1);
      }

      autoNameMap.put(var0, var1);
      return var0 + "-" + var1;
   }

   public static Class findClass(String var0) throws ClassNotFoundException {
      if (var0.equals("long")) {
         return Long.TYPE;
      } else if (var0.equals("double")) {
         return Double.TYPE;
      } else if (var0.equals("float")) {
         return Float.TYPE;
      } else if (var0.equals("int")) {
         return Integer.TYPE;
      } else if (var0.equals("char")) {
         return Character.TYPE;
      } else if (var0.equals("short")) {
         return Short.TYPE;
      } else if (var0.equals("byte")) {
         return Byte.TYPE;
      } else if (var0.equals("boolean")) {
         return Boolean.TYPE;
      } else if (var0.equals("void")) {
         return Void.TYPE;
      } else if (var0.endsWith("[]")) {
         Class var1 = findClass(var0.substring(0, var0.length() - 2));
         return Array.newInstance(var1, 0).getClass();
      } else {
         return Class.forName(var0);
      }
   }

   public static Class wrapClass(Class var0) {
      if (var0 == Long.TYPE) {
         return Long.class;
      } else if (var0 == Double.TYPE) {
         return Double.class;
      } else if (var0 == Float.TYPE) {
         return Float.class;
      } else if (var0 == Integer.TYPE) {
         return Integer.class;
      } else if (var0 == Character.TYPE) {
         return Character.class;
      } else if (var0 == Short.TYPE) {
         return Short.class;
      } else if (var0 == Byte.TYPE) {
         return Byte.class;
      } else {
         return var0 == Boolean.TYPE ? Boolean.class : var0;
      }
   }
}
