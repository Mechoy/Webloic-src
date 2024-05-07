package weblogic.corba.rmic;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import org.omg.CORBA.portable.IDLEntity;

public class IDLMangler implements IDLKeywords {
   private static String SEQ = "seq";
   public static final String GET = "get";
   public static final String SET = "set";
   public static final String IS = "is";
   public static final String GETTER = "_get_";
   public static final String SETTER = "_set_";
   public static final String DOT = ".";
   public static final String DOUBLEUNDERSCORE = "__";
   public static final String UNDERSCORE = "_";
   public static String BOXED_IDL = ".org.omg.boxedIDL.";
   public static String BOXED_RMI = ".org.omg.boxedRMI";
   private static final Object[][] typeMapping;
   private static final String[] ILLEGAL_CHARS;

   public static boolean isIDLEntity(Class var0) {
      return !IDLEntity.class.equals(var0) && IDLEntity.class.isAssignableFrom(var0);
   }

   public static String normalizeClassToIDLName(Class var0) {
      for(int var1 = 0; var1 < typeMapping.length; ++var1) {
         if (var0.equals(typeMapping[var1][0])) {
            return (String)typeMapping[var1][1];
         }
      }

      String var6 = var0.getName();
      if (var0.getComponentType() != null) {
         return getSequenceTypeName(var0);
      } else if (isIDLEntity(var0)) {
         return BOXED_IDL + normalizeJavaName(var6);
      } else {
         StringBuffer var2 = new StringBuffer(".");
         int var3 = var6.lastIndexOf(".");
         if (var3 >= 0) {
            ++var3;
            var2.append(var6.substring(0, var3));
         } else {
            var3 = 0;
         }

         int var4;
         StringBuffer var5;
         for(var5 = new StringBuffer(); (var4 = var6.indexOf(36, var3)) >= 0; var3 = var4 + 1) {
            var5.append(var6.substring(var3, var4)).append("__");
         }

         var5.append(var6.substring(var3));
         var2.append(normalizeJavaName(var5.toString()));
         return var2.toString();
      }
   }

   public static String normalizeJavaName(String var0) {
      String var1 = var0;
      if (var0.startsWith("_")) {
         var1 = "J" + var0;
      } else {
         for(int var2 = 0; var2 < KEYWORDS.length; ++var2) {
            if (var0.equalsIgnoreCase(KEYWORDS[var2])) {
               var1 = "_" + var1;
            }
         }
      }

      return convertIllegalCharacters(var1);
   }

   public static String accessorToAttribute(String var0) {
      byte var1 = 3;
      if (var0.startsWith("is")) {
         var1 = 2;
      }

      String var2 = var0.substring(var1);
      if (var2.length() == 1 || var2.length() >= 2 && (!Character.isUpperCase(var2.charAt(0)) || !Character.isUpperCase(var2.charAt(1)))) {
         var2 = Character.toLowerCase(var2.charAt(0)) + var2.substring(1);
      }

      return normalizeJavaName(var2);
   }

   public static String convertOverloadedName(String var0, Class[] var1) {
      StringBuffer var2 = new StringBuffer(normalizeJavaName(var0));

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append("__");

         String var4;
         int var5;
         for(var4 = normalizeClassToIDLName(var1[var3]); (var5 = var4.lastIndexOf(".")) >= 0 && var4.length() > var5 + 1 && var4.charAt(var5 + 1) == '_'; var4 = var4.substring(0, var5) + var4.substring(var5 + 1)) {
         }

         while(var4.startsWith("_") || var4.startsWith(".")) {
            var4 = var4.substring(1);
         }

         var4 = var4.replace('.', '_');
         var4 = var4.replace(' ', '_');
         var2.append(var4);
      }

      if (var1.length == 0) {
         var2.append("__");
      }

      return var2.toString();
   }

   public static String convertOverloadedName(Method var0) {
      return convertOverloadedName(var0.getName(), var0.getParameterTypes());
   }

   public static int isOverloaded(Class var0, Method var1) {
      if (var0 != null && !var0.equals(Object.class)) {
         Method[] var2 = var0.getDeclaredMethods();
         Class[] var3 = var1.getParameterTypes();

         Class[] var6;
         int var7;
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (!var1.getName().equals(var2[var4].getName())) {
               if (var1.getName().equalsIgnoreCase(var2[var4].getName())) {
                  return 2;
               }
            } else {
               boolean var5 = true;
               var6 = var2[var4].getParameterTypes();
               if (var6.length != var3.length) {
                  var5 = false;
               } else {
                  for(var7 = 0; var7 < var6.length; ++var7) {
                     if (var6[var7] != var3[var7]) {
                        var5 = false;
                     }
                  }
               }

               if (!var5) {
                  return 1;
               }
            }
         }

         if (var1.getName().equalsIgnoreCase(var0.getName()) && !var1.getName().equals(var0.getName())) {
            return 2;
         } else {
            Class var8 = var0.getSuperclass();
            int var9 = isOverloaded(var8, var1);
            if (var9 > 0) {
               return var9;
            } else {
               var6 = var0.getInterfaces();

               for(var7 = 0; var7 < var6.length; ++var7) {
                  var9 = isOverloaded(var6[var7], var1);
                  if (var9 > 0) {
                     return var9;
                  }
               }

               return 0;
            }
         }
      } else {
         return 0;
      }
   }

   public static boolean methodThrowsRemoteException(Method var0) {
      Class[] var1 = var0.getExceptionTypes();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].isAssignableFrom(RemoteException.class)) {
            return true;
         }
      }

      return false;
   }

   public static boolean methodThrowsCheckedException(Method var0) {
      Class[] var1 = var0.getExceptionTypes();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (Exception.class.isAssignableFrom(var1[var2]) && !RemoteException.class.isAssignableFrom(var1[var2]) && !RuntimeException.class.isAssignableFrom(var1[var2])) {
            return true;
         }
      }

      return false;
   }

   public static final String getMangledMethodName(Method var0) {
      return getMangledMethodName(var0, (Class)null);
   }

   public static final String getMangledMethodName(Method var0, Class var1) {
      Class var2 = var0.getDeclaringClass();
      Method[] var3 = var2.getDeclaredMethods();
      String var4 = var0.getName();
      if (var1 == null) {
         var1 = var2;
      }

      if (!org.omg.CORBA.Object.class.isAssignableFrom(var1) && !org.omg.CORBA.Object.class.isAssignableFrom(var0.getDeclaringClass())) {
         int var5 = isOverloaded(var2, var0);
         if (var5 == 1) {
            var4 = convertOverloadedName(var0);
         } else if (var5 == 2) {
            var4 = convertCaseSensitiveName(var0);
         } else {
            var4 = normalizeJavaName(var4);
         }

         if (!methodThrowsCheckedException(var0)) {
            if (isIsser(var0)) {
               var4 = "_get_" + unescape(accessorToAttribute(var4));
            } else if (isGetter(var0)) {
               var4 = "_get_" + unescape(accessorToAttribute(var4));
            } else if (isSetter(var0)) {
               var4 = "_set_" + unescape(accessorToAttribute(var4));
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   private static final String unescape(String var0) {
      while(var0.startsWith("_")) {
         var0 = var0.substring(1, var0.length());
      }

      return var0;
   }

   public static String stripPackage(String var0) {
      String var1 = var0;
      int var2 = var0.lastIndexOf(".");
      if (var2 >= 0) {
         var1 = var0.substring(var2 + 1);
      }

      return var1;
   }

   public static boolean isIsser(Method var0) {
      String var1 = var0.getName();
      return var1.startsWith("is") && var0.getReturnType() == Boolean.TYPE && var0.getParameterTypes().length == 0 && !methodThrowsCheckedException(var0);
   }

   public static boolean isGetter(Method var0) {
      String var1 = var0.getName();
      if (var1.startsWith("get") && var1.length() > "get".length() && var0.getReturnType() != Void.TYPE && var0.getParameterTypes().length == 0 && !methodThrowsCheckedException(var0)) {
         StringBuffer var2 = new StringBuffer("is");
         var2.append(var1.substring("get".length()));

         try {
            Method var3 = var0.getDeclaringClass().getDeclaredMethod(var2.toString(), Void.TYPE);
            if (var3.getReturnType() != var0.getReturnType()) {
               return true;
            }
         } catch (NoSuchMethodException var4) {
            return true;
         }
      }

      return false;
   }

   public static boolean isSetter(Method var0) {
      String var1 = var0.getName();
      Class[] var2 = var0.getParameterTypes();
      if (var1.startsWith("set") && var0.getReturnType() == Void.TYPE && var2.length == 1 && !methodThrowsCheckedException(var0)) {
         String var3 = "is" + var1.substring("set".length());

         Method var4;
         try {
            var4 = var0.getDeclaringClass().getDeclaredMethod(var3, (Class[])null);
            if (var4.getReturnType() == var2[0]) {
               return true;
            }
         } catch (NoSuchMethodException var6) {
         }

         try {
            var3 = "get" + var1.substring("set".length());
            var4 = var0.getDeclaringClass().getDeclaredMethod(var3, (Class[])null);
            if (var4.getReturnType() == var2[0]) {
               return true;
            }
         } catch (NoSuchMethodException var5) {
         }

         return false;
      } else {
         return false;
      }
   }

   private static String convertCaseSensitiveName(Method var0) {
      String var1 = var0.getName();
      StringBuffer var2 = new StringBuffer(normalizeJavaName(var1));
      boolean var3 = true;

      for(int var4 = 0; var4 < var2.length(); ++var4) {
         if (Character.isUpperCase(var2.charAt(var4))) {
            var2.append("_");
            var2.append(var4);
            var3 = false;
         }
      }

      if (var3) {
         var2.append("_");
      }

      return var2.toString();
   }

   private static String getSequenceTypeName(Class var0) {
      StringBuffer var1 = new StringBuffer(BOXED_RMI);

      Class var2;
      for(var2 = var0.getComponentType(); var2.getComponentType() != null; var2 = var2.getComponentType()) {
      }

      String var3 = normalizeClassToIDLName(var2);
      String var4 = var0.toString();
      int var5 = var4.lastIndexOf(91) - var4.indexOf(91) + 1;
      int var6 = var3.lastIndexOf(".");
      if (var6 >= 0) {
         var1.append(var3.substring(0, var6 + 1));
      } else {
         var1.append(".");
      }

      var1.append(SEQ).append(Integer.toString(var5)).append("_").append(var3.substring(var6 + 1));
      return var1.toString().replace(' ', '_');
   }

   public static String convertIllegalCharacters(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         boolean var4 = false;

         for(int var5 = 0; var5 < ILLEGAL_CHARS.length; var5 += 2) {
            if (var3 != '\\') {
               char var7 = ILLEGAL_CHARS[var5].charAt(0);
               if (var3 == var7) {
                  var1.append(ILLEGAL_CHARS[var5 + 1]);
                  var4 = true;
                  break;
               }
            } else {
               if ('u' == var0.charAt(var2 + 1)) {
                  var1.append("U");

                  for(int var6 = var2 + 2; var6 < var2 + 5; ++var6) {
                     var1.append(Character.toUpperCase(var0.charAt(var6)));
                  }
               }

               var2 += 4;
               var4 = true;
            }
         }

         if (!var4) {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   private static void p(String var0) {
      System.out.println("***<IDLMangler> " + var0);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length >= 1 && var0.length <= 2) {
         Class var1 = Class.forName(var0[0]);
         Method[] var2 = var1.getDeclaredMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var0.length == 1 || var2[var3].getName().equals(var0[1])) {
               System.out.println(getMangledMethodName(var2[var3]));
            }
         }
      } else {
         System.out.println("weblogic.corba.rmic.IDLMangler <class> <method>");
      }

   }

   static {
      typeMapping = new Object[][]{{null, ""}, {Void.TYPE, "void"}, {Boolean.TYPE, "boolean"}, {Character.TYPE, "wchar"}, {Byte.TYPE, "octet"}, {Short.TYPE, "short"}, {Integer.TYPE, "long"}, {Long.TYPE, "long long"}, {Float.TYPE, "float"}, {Double.TYPE, "double"}, {String.class, ".CORBA.WStringValue"}, {Object.class, ".java.lang._Object"}, {Class.class, ".javax.rmi.CORBA.ClassDesc"}, {Serializable.class, ".java.io.Serializable"}, {Externalizable.class, ".java.io.Externalizable"}, {org.omg.CORBA.Object.class, "Object"}};
      ILLEGAL_CHARS = new String[]{"$", "U0024"};
   }

   public interface Overloaded {
      int FALSE = 0;
      int TRUE = 1;
      int IGNORING_CASE = 2;
   }
}
