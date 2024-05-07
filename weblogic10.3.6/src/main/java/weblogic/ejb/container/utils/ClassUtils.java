package weblogic.ejb.container.utils;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.utils.AssertionError;

public final class ClassUtils {
   private static byte[] byteArray = new byte[1];

   public static List getFinderMethodList(Class var0) {
      Method[] var1 = var0.getMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getName().startsWith("find")) {
            var2.add(var1[var3]);
         }
      }

      return var2;
   }

   public static Iterator getFinderMethods(Class var0) {
      return getFinderMethodList(var0).iterator();
   }

   public static String classToJavaSourceType(Class var0) {
      int var1;
      for(var1 = 0; var0.isArray(); var0 = var0.getComponentType()) {
         ++var1;
      }

      StringBuffer var2 = new StringBuffer(var0.getName());

      for(Class var3 = var0.getDeclaringClass(); var3 != null; var3 = var3.getDeclaringClass()) {
         var2.setCharAt(var3.getName().length(), '.');
      }

      for(int var4 = 0; var4 < var1; ++var4) {
         var2.append("[]");
      }

      return var2.toString();
   }

   public static String[] classesToJavaSourceTypes(Class[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = classToJavaSourceType(var0[var2]);
      }

      return var1;
   }

   public static Class nameToClass(String var0, ClassLoader var1) throws ClassNotFoundException {
      if (var0.equals("int")) {
         return Integer.TYPE;
      } else if (var0.equals("float")) {
         return Float.TYPE;
      } else if (var0.equals("double")) {
         return Double.TYPE;
      } else if (var0.equals("char")) {
         return Character.TYPE;
      } else if (var0.equals("boolean")) {
         return Boolean.TYPE;
      } else if (var0.equals("byte")) {
         return Byte.TYPE;
      } else if (var0.equals("long")) {
         return Long.TYPE;
      } else if (var0.equals("short")) {
         return Short.TYPE;
      } else {
         return var1 != null ? var1.loadClass(var0) : Class.forName(var0);
      }
   }

   public static boolean isPrimitiveOrImmutable(Class var0) {
      if (isObjectPrimitive(var0)) {
         return true;
      } else if (var0.isPrimitive() && !var0.equals(Void.TYPE)) {
         return true;
      } else if (var0.equals(String.class)) {
         return true;
      } else if (var0.equals(BigInteger.class)) {
         return true;
      } else {
         return var0.equals(BigDecimal.class);
      }
   }

   public static Class getPrimitiveClass(Class var0) {
      if (var0.isPrimitive()) {
         return var0;
      } else if (!isObjectPrimitive(var0)) {
         return null;
      } else if (var0.equals(Integer.class)) {
         return Integer.TYPE;
      } else if (var0.equals(Long.class)) {
         return Long.TYPE;
      } else if (var0.equals(Float.class)) {
         return Float.TYPE;
      } else if (var0.equals(Double.class)) {
         return Double.TYPE;
      } else if (var0.equals(Byte.class)) {
         return Byte.TYPE;
      } else if (var0.equals(Short.class)) {
         return Short.TYPE;
      } else if (var0.equals(Boolean.class)) {
         return Boolean.TYPE;
      } else {
         return var0.equals(Character.class) ? Character.TYPE : null;
      }
   }

   public static boolean isObjectPrimitive(Class var0) {
      if (var0.equals(Integer.class)) {
         return true;
      } else if (var0.equals(Long.class)) {
         return true;
      } else if (var0.equals(Float.class)) {
         return true;
      } else if (var0.equals(Double.class)) {
         return true;
      } else if (var0.equals(Byte.class)) {
         return true;
      } else if (var0.equals(Short.class)) {
         return true;
      } else if (var0.equals(Boolean.class)) {
         return true;
      } else {
         return var0.equals(Character.class);
      }
   }

   public static String getDefaultValue(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "false";
         } else if (var0 == Byte.TYPE) {
            return "(byte)0";
         } else if (var0 == Character.TYPE) {
            return "'\\u0000'";
         } else if (var0 == Double.TYPE) {
            return "0.0d";
         } else if (var0 == Float.TYPE) {
            return "0.0f";
         } else if (var0 == Integer.TYPE) {
            return "0";
         } else if (var0 == Long.TYPE) {
            return "0L";
         } else {
            return var0 == Short.TYPE ? "(short)0" : "";
         }
      } else {
         return "null";
      }
   }

   public static String makeLegalName(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = var0.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var0.charAt(var3);
         if (Character.isLetterOrDigit(var4)) {
            if (var3 == 0 && Character.isDigit(var4)) {
               var1.append('_');
            }

            var1.append(var4);
         } else {
            var1.append("_");
         }
      }

      return var1.toString();
   }

   public static String getSQLTypeForClass(Class var0) throws EJBCException {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "java.sql.Types.BINARY";
         }

         if (var0 == Byte.TYPE) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Character.TYPE) {
            return "java.sql.Types.CHAR";
         }

         if (var0 == Double.TYPE) {
            return "java.sql.Types.DOUBLE";
         }

         if (var0 == Float.TYPE) {
            return "java.sql.Types.FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Short.TYPE) {
            return "java.sql.Types.INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "java.sql.Types.VARCHAR";
         }

         if (var0 == BigDecimal.class) {
            return "java.sql.Types.NUMERIC";
         }

         if (var0 == Boolean.class) {
            return "java.sql.Types.BINARY";
         }

         if (var0 == Byte.class) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Character.class) {
            return "java.sql.Types.CHAR";
         }

         if (var0 == Double.class) {
            return "java.sql.Types.DOUBLE";
         }

         if (var0 == Float.class) {
            return "java.sql.Types.FLOAT";
         }

         if (var0 == Integer.class) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Long.class) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Short.class) {
            return "java.sql.Types.INTEGER";
         }

         if (var0 == Date.class) {
            return "java.sql.Types.DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "java.sql.Types.DATE";
         }

         if (var0 == Time.class) {
            return "java.sql.Types.TIME";
         }

         if (var0 == Timestamp.class) {
            return "java.sql.Types.TIMESTAMP";
         }

         if (var0 == byteArray.getClass()) {
            return "java.sql.Types.VARBINARY";
         }

         if (Serializable.class.isAssignableFrom(var0)) {
            return "java.sql.Types.VARBINARY";
         }
      }

      throw new EJBCException("CMP20 Could not handle a SQL type in TypeUtils.getSQLTypeForClass:  type = " + var0);
   }

   public static boolean isValidSQLType(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return true;
         }

         if (var0 == Byte.TYPE) {
            return true;
         }

         if (var0 == Character.TYPE) {
            return true;
         }

         if (var0 == Double.TYPE) {
            return true;
         }

         if (var0 == Float.TYPE) {
            return true;
         }

         if (var0 == Integer.TYPE) {
            return true;
         }

         if (var0 == Long.TYPE) {
            return true;
         }

         if (var0 == Short.TYPE) {
            return true;
         }
      } else {
         if (var0 == String.class) {
            return true;
         }

         if (var0 == Boolean.class) {
            return true;
         }

         if (var0 == Byte.class) {
            return true;
         }

         if (var0 == Character.class) {
            return true;
         }

         if (var0 == Double.class) {
            return true;
         }

         if (var0 == Float.class) {
            return true;
         }

         if (var0 == Integer.class) {
            return true;
         }

         if (var0 == Long.class) {
            return true;
         }

         if (var0 == Short.class) {
            return true;
         }

         if (var0 == Date.class) {
            return true;
         }

         if (var0 == java.sql.Date.class) {
            return true;
         }

         if (var0 == Time.class) {
            return true;
         }

         if (var0 == Timestamp.class) {
            return true;
         }

         if (var0 == BigDecimal.class) {
            return true;
         }

         if (var0 == byteArray.getClass()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isByteArray(Class var0) {
      return var0.isArray() && var0.getComponentType() == Byte.TYPE;
   }

   public static String setClassName(CMPBeanDescriptor var0, String var1) {
      return MethodUtils.tail(var0.getGeneratedBeanClassName()) + "_" + var1 + "_Set";
   }

   public static String iteratorClassName(CMPBeanDescriptor var0, String var1) {
      return MethodUtils.tail(var0.getGeneratedBeanClassName()) + "_" + var1 + "_Iterator";
   }

   public static Class getObjectClass(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return Boolean.class;
         } else if (var0 == Byte.TYPE) {
            return Byte.class;
         } else if (var0 == Character.TYPE) {
            return Character.class;
         } else if (var0 == Double.TYPE) {
            return Double.class;
         } else if (var0 == Float.TYPE) {
            return Float.class;
         } else if (var0 == Integer.TYPE) {
            return Integer.class;
         } else if (var0 == Long.TYPE) {
            return Long.class;
         } else if (var0 == Short.TYPE) {
            return Short.class;
         } else {
            throw new AssertionError("Missing primitive in ClassUtils.getObjectClass");
         }
      } else {
         return var0;
      }
   }

   public static Class getPrimitiveClass(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.equals("boolean")) {
         return Boolean.TYPE;
      } else if (var0.equals("byte")) {
         return Byte.TYPE;
      } else if (var0.equals("char")) {
         return Character.TYPE;
      } else if (var0.equals("double")) {
         return Double.TYPE;
      } else if (var0.equals("float")) {
         return Float.TYPE;
      } else if (var0.equals("int")) {
         return Integer.TYPE;
      } else if (var0.equals("long")) {
         return Long.TYPE;
      } else {
         return var0.equals("short") ? Short.TYPE : null;
      }
   }

   public static Method getDeclaredMethod(Class var0, String var1, Class[] var2) throws NoSuchMethodException {
      Class var3 = var0;

      while(true) {
         try {
            return var3.getDeclaredMethod(var1, var2);
         } catch (NoSuchMethodException var5) {
            if ((var3 = var3.getSuperclass()) == null) {
               throw new NoSuchMethodException("Method " + var1 + " not found in the " + var0);
            }
         }
      }
   }

   public static String fileNameToClass(String var0) {
      String var1 = "";
      var1 = var0.replace(File.separator.charAt(0), '.');
      var1 = var1.substring(0, var0.indexOf(".class"));
      return var1;
   }

   public static String getCanonicalName(Class var0) {
      if (var0.getDeclaringClass() != null) {
         String var1 = getCanonicalName(var0.getDeclaringClass());
         return var1 + "." + var0.getName().substring(var1.length() + 1);
      } else {
         return var0.getName();
      }
   }

   public static Method getMethodForNameAndParams(String var0, String[] var1, List var2) {
      if (var2 == null) {
         return null;
      } else {
         Iterator var3 = var2.iterator();

         Method var4;
         boolean var7;
         do {
            String var5;
            Class[] var6;
            do {
               do {
                  if (!var3.hasNext()) {
                     return null;
                  }

                  var4 = (Method)var3.next();
                  var5 = var4.getName();
                  var6 = var4.getParameterTypes();
               } while(!var5.equals(var0));
            } while(var1.length != var6.length);

            var7 = true;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               if (!var6[var8].getName().equals(var1[var8])) {
                  var7 = false;
                  break;
               }
            }
         } while(!var7);

         return var4;
      }
   }

   public static List getMethodNamesForNameAndParams(String var0, Class[] var1, Method[] var2) {
      ArrayList var3 = new ArrayList();
      if (var2 == null) {
         return var3;
      } else {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            Method var5 = var2[var4];
            String var6 = var5.getName();
            Class[] var7 = var5.getParameterTypes();
            if (var6.startsWith(var0) && var1.length == var7.length) {
               boolean var8 = true;

               for(int var9 = 0; var9 < var7.length; ++var9) {
                  if (!var7[var9].getName().equals(var1[var9].getName())) {
                     var8 = false;
                     break;
                  }
               }

               if (var8) {
                  var3.add(var5.getName());
               }
            }
         }

         return var3;
      }
   }
}
