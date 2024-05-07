package weblogic.iiop;

import java.io.Externalizable;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.omg.CORBA.portable.IDLEntity;
import weblogic.corba.rmic.IDLAttribute;
import weblogic.corba.rmic.IDLField;
import weblogic.corba.rmic.IDLKeywords;
import weblogic.corba.rmic.IDLMangler;
import weblogic.corba.rmic.IDLOptions;
import weblogic.corba.utils.ValueHandlerImpl;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.Debug;
import weblogic.utils.compiler.CodeGenerationException;

public final class IDLUtils implements IDLKeywords {
   private static String SEQ = "seq";
   public static final String GET = "get";
   public static final String SET = "set";
   public static final String IS = "is";
   public static final String DOT = ".";
   public static final char DOT_C = '.';
   public static String RAISES = " raises (";
   public static final String DOUBLEUNDERSCORE = "__";
   public static final String UNDERSCORE = "_";
   public static final String EXCEPTION = "Exception";
   public static final String EX = "Ex";
   private static Hashtable m_treatedClasses = null;
   public static final String BOXED_IDL_COLON_COLON = "::org::omg::boxedIDL::";

   public static String javaTypeToPath(Class var0) {
      return javaTypeToPath((String)null, var0);
   }

   public static String getTypeID(Class var0) {
      return ValueHandlerImpl.getRepositoryID(var0);
   }

   public static String getPragmaID(Class var0) {
      String var1 = "";
      String var2 = getTypeID(var0);
      String var3 = stripPackage(getIDLType(var0, "."));
      var1 = "#pragma ID " + var3 + " \"" + var2 + "\"\n";
      return var1;
   }

   public static Class[] getInheritedInterfaces(Class var0) {
      Hashtable var1 = new Hashtable();
      getInheritedInterfaces(var0, var1);
      Class[] var2 = new Class[var1.size()];
      Enumeration var3 = var1.elements();

      for(int var4 = 0; var3.hasMoreElements(); var2[var4++] = (Class)var3.nextElement()) {
      }

      return var2;
   }

   static void getInheritedInterfaces(Class var0, Hashtable var1) {
      Class var2 = var0.getSuperclass();
      Class[] var3 = null;
      if (var2 != null && !var2.isInterface()) {
         Class[] var4 = var0.getInterfaces();
         var3 = new Class[var4.length + 1];
         System.arraycopy(var4, 0, var3, 0, var4.length);
         var3[var4.length] = var2;
      } else {
         var3 = var0.getInterfaces();
      }

      Vector var7 = new Vector();

      Class var6;
      for(int var5 = 0; var5 < var3.length; ++var5) {
         var6 = (Class)var1.get(var3[var5]);
         if (null == var6) {
            var1.put(var3[var5], var3[var5]);
            var7.addElement(var3[var5]);
         }
      }

      Enumeration var8 = var7.elements();

      while(var8.hasMoreElements()) {
         var6 = (Class)var8.nextElement();
         getInheritedInterfaces(var6, var1);
      }

   }

   public static Class[] getInheritedRemoteInterfaces(Class var0) {
      Class[] var1 = getInheritedInterfaces(var0);
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (Utilities.isARemote(var1[var3])) {
            var2.addElement(var1[var3]);
         }
      }

      Class[] var6 = new Class[var2.size()];
      int var4 = 0;

      for(Enumeration var5 = var2.elements(); var5.hasMoreElements(); var6[var4++] = (Class)var5.nextElement()) {
      }

      return var6;
   }

   static boolean isCORBAObject(Class var0) {
      return "org.omg.CORBA.Object".equals(var0.getName());
   }

   static String convertLeadingUnderscore(String var0) {
      String var1 = var0;
      if (var0.startsWith("_")) {
         var1 = "J" + var0;
      }

      return var1;
   }

   public static String javaTypeToPath(String var0, Class var1) {
      String var2 = new String();
      if (null != var0) {
         var2 = var2 + var0;
      }

      String var3 = (new Character(File.separatorChar)).toString();
      if (isCORBAObject(var1)) {
         return "org" + var3 + "omg" + var3 + "CORBA" + var3 + "_Object.idl";
      } else {
         String var4 = getIDLType(var1, var3);
         var2 = var2 + var4 + ".idl";
         if (var2.startsWith(var3)) {
            var2 = var2.substring(1);
         }

         return var2;
      }
   }

   public static final String exceptionToEx(String var0) {
      String var1 = var0;
      if (var0.endsWith("Exception")) {
         var1 = var0.substring(0, var0.length() - "Exception".length());
      }

      return var1 + "Ex";
   }

   public static String getIDLType(Class var0, String var1, String var2) {
      String var3 = IDLMangler.normalizeClassToIDLName(var0);
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;
      if (IDLOptions.getVisibroker() && var3.equals(".javax.rmi.CORBA.ClassDesc")) {
         return var2 + "javax" + var1 + "rmi" + var1 + "CORBA_" + var1 + "ClassDesc";
      } else {
         if (var3.charAt(0) == '.') {
            var4.append(var2);
            ++var5;
         }

         if (var1.equals(".")) {
            return var4.append(var3.substring(var5).replace(' ', '_')).toString();
         } else {
            for(; var5 < var3.length(); ++var5) {
               switch (var3.charAt(var5)) {
                  case ' ':
                     if (var1.equals("_")) {
                        var4.append("_");
                     } else {
                        var4.append(" ");
                     }
                     break;
                  case '.':
                     var4.append(var1);
                     break;
                  default:
                     var4.append(var3.charAt(var5));
               }
            }

            return var4.toString();
         }
      }
   }

   public static String getIDLType(Class var0, String var1) {
      return getIDLType(var0, var1, "");
   }

   public static String getIDLType(Class var0) {
      return getIDLType(var0, "::", "::");
   }

   public static boolean isException(Class var0) {
      return Exception.class.isAssignableFrom(var0) && !RemoteException.class.equals(var0);
   }

   public static String stripPackage(String var0) {
      return stripPackage(var0, ".");
   }

   public static String stripPackage(String var0, String var1) {
      String var2 = var0;
      int var3 = var0.lastIndexOf(var1);
      if (-1 != var3) {
         var2 = var0.substring(var3 + var1.length());
      }

      return var2;
   }

   public static Class[] getClasses(Class var0, boolean var1, boolean var2) {
      Hashtable var3 = new Hashtable();
      getAllTypes(var0, var3, var1, var2);
      Class[] var4 = new Class[var3.size()];
      Enumeration var5 = var3.elements();

      for(int var6 = 0; var5.hasMoreElements(); var4[var6++] = (Class)var5.nextElement()) {
      }

      return var4;
   }

   static void getAllTypes(Class var0, Hashtable var1, boolean var2, boolean var3) {
      m_treatedClasses = new Hashtable();
      getAllTypesInternal(var0, var1, var2, var3);
   }

   public static boolean isValidField(Field var0) {
      return !attributeMustBeIgnored(var0.getName()) && !Modifier.isStatic(var0.getModifiers()) && !Modifier.isTransient(var0.getModifiers());
   }

   public static boolean isValid(Method var0) {
      boolean var1 = true;
      int var2 = var0.getModifiers();
      if (Modifier.isNative(var2) || !Modifier.isPublic(var2)) {
         var1 = false;
      }

      return var1;
   }

   public static boolean isValid(Class var0) {
      boolean var1 = true;
      if (String.class.equals(var0) || RemoteException.class.equals(var0) || var0.isPrimitive() || IDLEntity.class.equals(var0) || Void.TYPE.equals(var0) || Externalizable.class.equals(var0)) {
         var1 = false;
      }

      return var1;
   }

   static void addType(Class var0, Hashtable var1, Class var2, boolean var3) {
      if (!var0.equals(var2) && isValid(var0)) {
         var1.put(var0, var0);
         if (var3) {
            for(var0 = var0.getSuperclass(); null != var0; var0 = var0.getSuperclass()) {
               if (!var0.equals(var2) && isValid(var0)) {
                  var1.put(var0, var0);
               }
            }
         }
      }

   }

   static void getAllTypesInternal(Class var0, Hashtable var1, boolean var2, boolean var3) {
      Class var4 = var0.getComponentType();
      if (null != var4) {
         addType(var4, var1, var0, var2);
      } else {
         Class var6 = var0.getSuperclass();
         if (null != var6 && !Object.class.equals(var6)) {
            addType(var0.getSuperclass(), var1, var0, var2);
         }

         Class[] var7 = var0.getInterfaces();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            addType(var7[var8], var1, var0, var2);
         }

         Hashtable var18 = new Hashtable();
         new Vector();
         getAllFieldTypes(var0, var18);
         Enumeration var10 = var18.elements();

         while(var10.hasMoreElements()) {
            Class var11 = (Class)var10.nextElement();
            addType(var11, var1, var0, var2);
         }

         if (var3) {
            Method[] var19 = var0.getDeclaredMethods();

            for(int var12 = 0; var12 < var19.length; ++var12) {
               Method var13 = var19[var12];
               if (isValid(var13)) {
                  Class var14 = var13.getReturnType();
                  if (null != var14) {
                     addType(var14, var1, var0, var2);
                  }

                  Class[] var15 = var13.getParameterTypes();

                  for(int var16 = 0; var16 < var15.length; ++var16) {
                     addType(var15[var16], var1, var0, var2);
                  }

                  Class[] var22 = var13.getExceptionTypes();

                  for(int var17 = 0; var17 < var22.length; ++var17) {
                     addType(var22[var17], var1, var0, var2);
                  }
               }
            }
         }

         if (var2) {
            Enumeration var20 = var1.elements();

            while(var20.hasMoreElements()) {
               Class var21 = (Class)var20.nextElement();
               if (!isException(var21) && null == m_treatedClasses.get(var21.getName())) {
                  m_treatedClasses.put(var21.getName(), var21);
                  getAllTypesInternal(var21, var1, var2, var3);
               }
            }
         }

      }
   }

   public static void getAllFieldTypes(Class var0, Hashtable var1) {
      Field[] var2 = var0.getDeclaredFields();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (isValidField(var2[var3])) {
            addType(var2[var3].getType(), var1, var0, false);
         }
      }

   }

   public static Class getNonConformantType(Class var0) {
      Class var1 = null;
      if (isValid(var0) && !isARemote(var0) && !isConcreteValueType(var0) && !isAbstractInterface(var0)) {
         var1 = var0;
      }

      return var1;
   }

   public static boolean isAbstractInterface(Class var0) {
      boolean var1 = false;
      if (isValid(var0) && !isARemote(var0) && !isRemote(var0) && !Serializable.class.equals(var0)) {
         Method[] var2 = var0.getMethods();
         int var3 = 0;

         while(true) {
            if (var3 >= var2.length) {
               var1 = true;
               break;
            }

            if (!IDLMangler.methodThrowsRemoteException(var2[var3])) {
               break;
            }

            ++var3;
         }
      }

      return var1;
   }

   public static boolean isValueType(Class var0) {
      Debug.assertion(null != var0);
      if (!var0.isPrimitive() && !Serializable.class.equals(var0) && !Externalizable.class.equals(var0) && !RemoteException.class.equals(var0) && !IDLEntity.class.equals(var0) && !Class.class.equals(var0) && !Void.TYPE.equals(var0) && !isRemote(var0) && !isARemote(var0)) {
         if (var0.isInterface() && !isAbstractInterface(var0)) {
            return true;
         } else if (var0.getComponentType() != null) {
            return false;
         } else {
            return Serializable.class.isAssignableFrom(var0);
         }
      } else {
         return false;
      }
   }

   public static boolean isConcreteValueType(Class var0) {
      return isValueType(var0) && !var0.isInterface();
   }

   public static boolean isRemote(Class var0) {
      return Remote.class.equals(var0);
   }

   public static boolean isIDLInterface(Class var0) {
      return org.omg.CORBA.Object.class.isAssignableFrom(var0) && IDLEntity.class.isAssignableFrom(var0);
   }

   public static boolean isARemote(Class var0) {
      boolean var1 = true;
      if (isRemote(var0)) {
         var1 = false;
      } else {
         var1 = Remote.class.isAssignableFrom(var0);
      }

      return var1;
   }

   public static Class getRemoteInterface(Class var0) {
      Class var1 = null;
      Class[] var2 = var0.getInterfaces();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (isARemote(var2[var3])) {
            var1 = var2[var3];
            break;
         }
      }

      return var1;
   }

   public static boolean isACheckedException(Class var0) {
      return !RemoteException.class.isAssignableFrom(var0) && !RuntimeException.class.isAssignableFrom(var0) && !Error.class.isAssignableFrom(var0);
   }

   public static String openModule(Class var0) {
      StringBuffer var1 = new StringBuffer();
      String var2 = getIDLType(var0, ".");

      for(int var3 = var2.indexOf("."); var3 != -1; var3 = var2.indexOf(".")) {
         String var4 = var2.substring(0, var3);
         var1.append("module " + var4 + " {\n");
         var2 = var2.substring(var3 + 1);
      }

      return var1.toString();
   }

   public static String closeModule(Class var0) {
      StringBuffer var1 = new StringBuffer();
      String var2 = getIDLType(var0, ".").substring(1);

      for(int var3 = var2.indexOf("."); var3 != -1; var3 = var2.indexOf(".")) {
         var1.append("};\n");
         var2 = var2.substring(var3 + 1);
      }

      return var1.toString();
   }

   public static String generateGuard(Class var0, String var1) {
      String var2 = getIDLType(var0, "_");
      return generateGuard(var2, var1);
   }

   public static String generateGuard(String var0, String var1) {
      String var2 = var1 + " __" + var0 + "__\n";
      return var2;
   }

   static String removeChars(String var0, char var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 != var1) {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   public static String generateInclude(String var0, Class var1) {
      StringBuffer var2 = new StringBuffer();
      String var3 = javaTypeToPath((String)null, var1);
      var2.append("#include \"" + var3 + "\"\n");
      return var2.toString();
   }

   public static void getAttributesFromMethods(Class var0, Hashtable var1) throws CodeGenerationException {
      Hashtable var2 = new Hashtable();
      findIDLAttributes(var0, var2);
      Enumeration var3 = var2.elements();

      while(var3.hasMoreElements()) {
         IDLField var4 = (IDLField)var3.nextElement();
         var1.put(var4.getMangledName(), var4);
      }

   }

   public static void findIDLAttributes(Class var0, Hashtable var1) {
      Method[] var2 = var0.getDeclaredMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Method var4 = var2[var3];
         if (isValid(var4)) {
            boolean var5 = IDLMangler.isGetter(var4) || IDLMangler.isIsser(var4);
            boolean var6 = IDLMangler.isSetter(var4);
            if (var5 || var6) {
               Class var7 = null;
               if (var5) {
                  var7 = var4.getReturnType();
               } else {
                  var7 = var4.getParameterTypes()[0];
               }

               int var8 = var6 ? 2 : 1;
               String var9 = IDLMangler.accessorToAttribute(var4.getName());
               IDLAttribute var10 = (IDLAttribute)var1.get(var9);
               if (null == var10) {
                  var10 = new IDLAttribute(var9, var8, (Object)null, var7);
                  var1.put(var9, var10);
               } else if (var6) {
                  var10.setModifier(2);
               }
            }
         }
      }

   }

   public static boolean isASetterFor(Field var0, Method var1) {
      return isAnAccessorFor(var0, var1, "set");
   }

   public static boolean isAGetterFor(Field var0, Method var1) {
      return isAnAccessorFor(var0, var1, "get") && isAnAccessorFor(var0, var1, "is");
   }

   public static boolean isAnAccessorFor(Field var0, Method var1, String var2) {
      boolean var3 = false;
      String var4 = var1.getName();
      int var5 = var1.getModifiers();
      if (!Modifier.isNative(var5) && (!var2.equals("set") || IDLMangler.isSetter(var1)) && (!var2.equals("get") || IDLMangler.isGetter(var1)) && (!var2.equals("is") || IDLMangler.isIsser(var1)) && (var0 == null || IDLMangler.accessorToAttribute(var4).equals(IDLMangler.normalizeJavaName(var0.getName())))) {
         if ("set".equals(var2)) {
            if (null == var0 || var1.getParameterTypes()[0].equals(var0.getType())) {
               var3 = true;
            }
         } else if (null == var0 || var1.getReturnType().equals(var0.getType())) {
            var3 = true;
         }

         return var3;
      } else {
         return false;
      }
   }

   public static void getAttributesFromFields(Class var0, Hashtable var1) throws CodeGenerationException {
      Field[] var2 = var0.getDeclaredFields();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Field var4 = var2[var3];
         if (isValidField(var4)) {
            IDLField var5 = new IDLField(var0, var4);

            while(var1.get(var5.getMangledName()) != null) {
               var5.setMangledName(var5.getMangledName() + "_");
            }

            var1.put(var5.getMangledName(), var5);
         }
      }

   }

   public static boolean attributeMustBeIgnored(String var0) {
      return "serialVersionUID".equals(var0);
   }

   public static String mangleAttributeName(Class var0, Field var1) {
      return IDLMangler.convertIllegalCharacters(var1.getName());
   }

   public static boolean isThrown(Class var0, Class var1) {
      Debug.assertion(Exception.class.isAssignableFrom(var1));
      Method[] var2 = var0.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Method var4 = var2[var3];
         if (isValid(var4)) {
            Class[] var5 = var4.getExceptionTypes();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6].equals(var1)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean isInheritable(Class var0) {
      boolean var1 = true;
      if (var0.equals(Object.class) || isRemote(var0) || Serializable.class.equals(var0)) {
         var1 = false;
      }

      return var1;
   }

   public static boolean isMethodForAnAttribute(Class var0, Method var1) {
      return IDLMangler.isIsser(var1) || IDLMangler.isGetter(var1) || IDLMangler.isSetter(var1);
   }

   public static String mangleExceptionName(String var0) {
      int var1 = var0.lastIndexOf("Exception");
      if (var1 != -1) {
         var0 = var0.substring(0, var1) + "Ex";
      } else {
         var0 = var0 + "Ex";
      }

      return var0;
   }

   public static boolean mustSkipClass(Class var0) {
      return mustSkipClass(var0, isValueType(var0));
   }

   public static boolean mustSkipClass(Class var0, String var1) {
      boolean var2 = -1 != var1.indexOf("valuetype");
      return mustSkipClass(var0, var2);
   }

   public static boolean mustSkipClass(Class var0, boolean var1) {
      boolean var2 = false;
      boolean var3 = isValid(var0) && null != var0.getComponentType();
      var2 = var1 || var3;
      return var2;
   }

   public static String getMangledMethodName(Method var0, Method[] var1) throws CodeGenerationException {
      return IDLMangler.getMangledMethodName(var0);
   }

   private static void p(String var0) {
      System.out.println("***<IDLUtils> " + var0);
   }
}
