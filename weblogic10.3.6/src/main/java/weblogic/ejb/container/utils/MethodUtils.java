package weblogic.ejb.container.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.finders.EjbqlFinder;
import weblogic.ejb.container.cmp.rdbms.finders.Finder;
import weblogic.ejb.container.cmp.rdbms.finders.ParamNode;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.persistence.spi.CMPBeanDescriptor;
import weblogic.utils.Debug;
import weblogic.utils.PlatformConstants;

public class MethodUtils implements PlatformConstants {
   private static final int MAX_LINE_CHARS = 80;
   private static final int THRESHOLD = 7;

   public static String convertToFinderName(String var0) {
      if (var0.startsWith("ejbSelect")) {
         return var0;
      } else {
         String var1 = var0.substring(0, 1);
         String var2 = var0.substring(1);
         return "ejb" + var1.toUpperCase(Locale.ENGLISH) + var2;
      }
   }

   public static String convertToDDFinderName(String var0) {
      return var0.startsWith("ejbSelect") ? var0 : decapitalize(var0.substring(3));
   }

   public static String tail(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var0.substring(var1 + 1);
   }

   public static String getParameterName(int var0) {
      return getParameterName(var0, "param");
   }

   public static String getParameterName(int var0, String var1) {
      return var1 + var0;
   }

   public static String capitalize(String var0) {
      assert var0.length() > 0;

      return var0.substring(0, 1).toUpperCase(Locale.ENGLISH) + var0.substring(1);
   }

   public static String decapitalize(String var0) {
      Debug.assertion(var0.length() > 0);
      return var0.substring(0, 1).toLowerCase(Locale.ENGLISH) + var0.substring(1);
   }

   public static boolean potentialBridgeCandidate(Method var0, Method var1) {
      if (!var0.getName().equals(var1.getName())) {
         return false;
      } else if (!var0.getReturnType().isAssignableFrom(var1.getReturnType())) {
         return false;
      } else {
         Class[] var2 = var0.getParameterTypes();
         Class[] var3 = var1.getParameterTypes();
         if (var2.length != var3.length) {
            return false;
         } else {
            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (!var2[var4].isAssignableFrom(var3[var4])) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static String getFinderMethodDeclaration(Method var0, Class var1, boolean var2) {
      StringBuffer var3 = new StringBuffer();
      int var4 = var0.getModifiers();
      if (var2 && Modifier.isAbstract(var4)) {
         var4 ^= 1024;
      }

      var3.append(Modifier.toString(var4)).append(" ");
      String var5 = var3.toString();
      int var6 = var5.indexOf("strict ");
      if (var6 != -1) {
         var3.insert(var6 + "strict".length(), "fp");
      }

      if (!var0.getReturnType().getName().equals("java.util.Enumeration") && !var0.getReturnType().getName().equals("java.util.Collection")) {
         var3.append("java.lang.Object");
      } else {
         var3.append("java.util.Collection");
      }

      if (var3.length() > 73) {
         var3.append("" + EOL + "    ");
      }

      var3.append(" ");
      var3.append(convertToFinderName(var0.getName()));
      if (var3.length() > 73) {
         var3.append("" + EOL + "    ");
      }

      var3.append("(");
      var3.append(getParameterList(var0));
      var3.append(")");
      int var8 = 0;
      Class[] var9 = var0.getExceptionTypes();

      for(int var7 = 0; var7 < var9.length; ++var7) {
         if (!var9[var7].getName().equals("java.rmi.RemoteException")) {
            if (var8 == 0) {
               var3.append(" throws ");
            } else {
               var3.append(", ");
            }

            ++var8;
            var3.append(ClassUtils.classToJavaSourceType(var9[var7]));
            if (var3.length() > 73) {
               var3.append("" + EOL + "    ");
            }
         }
      }

      return var3.toString();
   }

   public static String[] classesToJavaSourceTypes(Class[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = ClassUtils.classToJavaSourceType(var0[var2]);
      }

      return var1;
   }

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

   public static List getFinderMethodList(Class var0, Class var1) {
      HashSet var2 = new HashSet();
      ArrayList var3 = new ArrayList();
      Method[] var4;
      int var5;
      if (var0 != null) {
         var4 = var0.getMethods();

         for(var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getName().startsWith("find")) {
               var3.add(var4[var5]);
               var2.add(DDUtils.getMethodSignature(var4[var5]));
            }
         }
      }

      if (var1 != null) {
         var4 = var1.getMethods();

         for(var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getName().startsWith("find")) {
               var3.add(var4[var5]);
            }
         }
      }

      return var3;
   }

   public static List getSelectMethodList(Class var0) {
      Method[] var1 = var0.getMethods();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getName().startsWith("ejbSelect")) {
            var2.add(var1[var3]);
         }
      }

      return var2;
   }

   public static Iterator getFinderMethods(Class var0) {
      return getFinderMethodList(var0).iterator();
   }

   public static String getFinderMethodDeclaration(Finder var0, Class var1) {
      String var2 = convertToFinderName(var0.getName());
      List var3 = var0.getExternalMethodParmList();
      String var4 = getFinderReturnClass(var0, var1);
      return getFinderMethodDeclaration(var0, var4, var2, var3);
   }

   public static String ejbSelectMDName(Finder var0) {
      Class[] var1;
      if (var0 instanceof EjbqlFinder && ((EjbqlFinder)var0).isGeneratedRelationFinder()) {
         var1 = new Class[0];
      } else {
         var1 = var0.getParameterClassTypes();
      }

      String var2 = null;
      if (var0 instanceof EjbqlFinder && !((EjbqlFinder)var0).isGeneratedRelationFinder()) {
         var2 = tail(var0.getRDBMSBean().getCMPBeanDescriptor().getGeneratedBeanClassName());
      }

      return convertToEjbSelectInternalName(var0.getName(), var1, var2);
   }

   public static String getEjbSelectInternalMethodDeclaration(Finder var0, Class var1) {
      String var2 = ejbSelectMDName(var0);
      List var3 = var0.getExternalMethodAndInEntityParmList();
      String var4 = getFinderReturnClass(var0, var1);
      return getFinderMethodDeclaration(var0, var4, var2, var3);
   }

   public static String getFinderMethodDeclaration(Finder var0, String var1, String var2, List var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append(var0.getModifierString());
      var4.append(var1);
      if (var4.length() > 73) {
         var4.append("" + EOL + "    ");
      }

      var4.append(" ");
      var4.append(var2);
      if (var4.length() > 73) {
         var4.append("" + EOL + "    ");
      }

      var4.append("(");
      if (var0.isKeyFinder() && ((EjbqlFinder)var0).getKeyBean().getCMPBeanDescriptor().hasComplexPrimaryKey()) {
         RDBMSBean var8 = ((EjbqlFinder)var0).getKeyBean();
         CMPBeanDescriptor var10 = var8.getCMPBeanDescriptor();
         var4.append(var10.getPrimaryKeyClass().getName() + " param0");
      } else {
         StringBuffer var5 = new StringBuffer();
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            ParamNode var7 = (ParamNode)var6.next();
            var5.append(ClassUtils.classToJavaSourceType(var7.getParamClass()));
            var5.append(" ").append(var7.getParamName());
            var5.append(", ");
         }

         if (var5.length() > 2) {
            var5.setLength(var5.length() - 2);
         }

         var4.append(var5.toString());
      }

      if (var4.length() > 73) {
         var4.append("" + EOL + "    ");
      }

      var4.append(")");
      int var9 = 0;
      Class[] var11 = var0.getExceptionClassTypes();

      for(int var12 = 0; var12 < var11.length; ++var12) {
         if (!var11[var12].getName().equals("java.rmi.RemoteException")) {
            if (var9 == 0) {
               var4.append(" throws ");
            } else {
               var4.append(", ");
            }

            ++var9;
            var4.append(ClassUtils.classToJavaSourceType(var11[var12]));
            if (var4.length() > 73) {
               var4.append("" + EOL + "    ");
            }
         }
      }

      return var4.toString();
   }

   public static String getFinderReturnClass(Finder var0, Class var1) {
      Class var2 = var0.getReturnClassType();
      boolean var3 = Enumeration.class.isAssignableFrom(var2);
      var3 |= Collection.class.isAssignableFrom(var2);
      if (var3) {
         return var2.getName();
      } else {
         return var0.finderLoadsBean() && var0.getQueryType() != 3 && var0.getQueryType() != 5 ? Object.class.getName() : ClassUtils.classToJavaSourceType(var1);
      }
   }

   public static String signature2identifier(String var0, Class[] var1) {
      if (var0 == null) {
         return "";
      } else {
         if (var1 == null) {
            var1 = new Class[0];
         }

         String var2 = DDUtils.getMethodSignature(var0, var1);
         var2 = var2.replace('.', '_');
         var2 = var2.replace(',', '_');
         var2 = var2.replace('[', 'A');
         var2 = var2.replace('(', '_');
         var2 = var2.replace(')', '_');
         return var2;
      }
   }

   public static String convertToEjbSelectInternalName(String var0, Class[] var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      String var4 = signature2identifier(var0, var1);
      var3.append("__WL_").append(var4);
      if (var2 != null) {
         var3.append(var2);
      }

      return var3.toString();
   }

   public static String convertToEjbSelectInternalName(String var0, Class[] var1) {
      return convertToEjbSelectInternalName(var0, var1, (String)null);
   }

   public static String varPrefix() {
      return "__WL_";
   }

   public static String getMethodName(String var0) {
      return "get" + capitalize(var0);
   }

   public static String setMethodName(String var0) {
      return "set" + capitalize(var0);
   }

   public static String doSetMethodName(String var0) {
      return varPrefix() + "doSet" + capitalize(var0);
   }

   public static String setRestMethodName(String var0) {
      return varPrefix() + "setRest" + capitalize(var0);
   }

   public static String checkIsRemovedMethodName(String var0) {
      return varPrefix() + "checkIsRemoved" + capitalize(var0);
   }

   public static String setCmrIsLoadedMethodName(String var0) {
      return varPrefix() + "set_" + var0 + "_isLoaded_";
   }

   public static String setNullMethodName(String var0) {
      return varPrefix() + "setNull" + capitalize(var0);
   }

   public static String postSetMethodName(String var0) {
      return varPrefix() + "postSet" + capitalize(var0);
   }

   public static String getResultSetMethodPostfix(Class var0) {
      return getMethodPostfix(var0);
   }

   public static String getPreparedStatementMethodPostfix(Class var0) {
      return getMethodPostfix(var0);
   }

   public static String getWSOPreInvokeMethodName(Method var0) {
      return "__WL_" + var0.getName() + "_WS_preInvoke";
   }

   public static String getWSOBusinessMethodName(Method var0) {
      return "__WL_" + var0.getName() + "_WS";
   }

   public static String getWSOPostInvokeMethodName() {
      return "__WL__WS_postInvoke";
   }

   public static String getWSOPreInvokeMethodDeclaration(Method var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("public void ");
      var1.append(getWSOPreInvokeMethodName(var0));
      var1.append("(");
      var1.append("AuthenticatedSubject altRunAs, ");
      var1.append("ContextHandler wsCtx");
      String var2 = getParameterList(var0, "arg");
      if (var2.length() > 0) {
         var1.append(", ");
         var1.append(var2);
      }

      var1.append(")");
      return var1.toString();
   }

   public static String getWSOBusinessMethodDeclaration(Method var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("public ");
      Class var2 = var0.getReturnType();
      if (var2 != null) {
         var1.append(ClassUtils.classToJavaSourceType(var2));
      } else {
         var1.append(" void");
      }

      var1.append(" ");
      var1.append(getWSOBusinessMethodName(var0));
      var1.append("(");
      var1.append(getParameterList(var0, "arg"));
      var1.append(")");
      return var1.toString();
   }

   public static String getWSOPostInvokeMethodDeclaration() {
      StringBuffer var0 = new StringBuffer();
      var0.append("public void ");
      var0.append(getWSOPostInvokeMethodName());
      var0.append("(");
      var0.append(")");
      return var0.toString();
   }

   public static String getParameterList(Method var0) {
      return getParameterList(var0, "param");
   }

   public static String getParameterList(Method var0, String var1) {
      StringBuffer var2 = new StringBuffer();
      Class[] var3 = var0.getParameterTypes();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.append(ClassUtils.classToJavaSourceType(var3[var4]));
         var2.append(" " + getParameterName(var4, var1));
         if (var4 < var3.length - 1) {
            var2.append(", ");
         }

         if (var2.length() > 73) {
            var2.append("" + EOL + "    ");
         }
      }

      return var2.toString();
   }

   public static String getMethodPostfix(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "Boolean";
         } else if (var0 == Byte.TYPE) {
            return "Byte";
         } else if (var0 == Character.TYPE) {
            return "String";
         } else if (var0 == Double.TYPE) {
            return "Double";
         } else if (var0 == Float.TYPE) {
            return "Float";
         } else if (var0 == Integer.TYPE) {
            return "Int";
         } else if (var0 == Long.TYPE) {
            return "Long";
         } else if (var0 == Short.TYPE) {
            return "Short";
         } else {
            throw new AssertionError("Didn't handle a potential SQL type case in TypeUtils.getMethodPostfix: type = " + var0);
         }
      } else if (var0 == String.class) {
         return "String";
      } else if (var0 == BigDecimal.class) {
         return "BigDecimal";
      } else if (var0 == Boolean.class) {
         return "Boolean";
      } else if (var0 == Byte.class) {
         return "Byte";
      } else if (var0 == Character.class) {
         return "String";
      } else if (var0 == Double.class) {
         return "Double";
      } else if (var0 == Float.class) {
         return "Float";
      } else if (var0 == Integer.class) {
         return "Int";
      } else if (var0 == Long.class) {
         return "Long";
      } else if (var0 == Short.class) {
         return "Short";
      } else if (var0 == Date.class) {
         return "Timestamp";
      } else if (var0 == java.sql.Date.class) {
         return "Date";
      } else if (var0 == Time.class) {
         return "Time";
      } else if (var0 == Timestamp.class) {
         return "Timestamp";
      } else {
         return var0.isArray() && var0.getComponentType() == Byte.TYPE ? "Bytes" : "Bytes";
      }
   }

   public static String convertToPrimitive(Class var0, String var1) {
      if (var0 == Boolean.class) {
         return var1 + ".booleanValue()";
      } else if (var0 == Byte.class) {
         return var1 + ".byteValue()";
      } else if (var0 == Character.class) {
         return var1 + ".charValue()";
      } else if (var0 == Double.class) {
         return var1 + ".doubleValue()";
      } else if (var0 == Float.class) {
         return var1 + ".floatValue()";
      } else if (var0 == Integer.class) {
         return var1 + ".intValue()";
      } else if (var0 == Long.class) {
         return var1 + ".longValue()";
      } else {
         return var0 == Short.class ? var1 + ".shortValue()" : var1;
      }
   }

   public static int dbmsType2int(String var0) {
      if (var0 == null) {
         return 0;
      } else {
         Integer var1 = (Integer)DDConstants.DBTYPE_MAP.get(var0.toUpperCase(Locale.ENGLISH));
         return var1 != null ? var1 : 0;
      }
   }

   public static String getDefaultDBMSColType(Class var0, int var1) throws Exception {
      switch (var1) {
         case 0:
         default:
            return getDefaultDBMSColType(var0);
         case 1:
            return getDefaultDBMSColType_DB_ORACLE(var0);
         case 2:
         case 5:
         case 7:
            return getDefaultDBMSColType_DB_SQLSERVER(var0);
         case 3:
            return getDefaultDBMSColType_DB_INFORMIX(var0);
         case 4:
            return getDefaultDBMSColType_DB_DB2(var0);
         case 6:
            return getDefaultDBMSColType_DB_POINTBASE(var0);
         case 8:
            return getDefaultDBMSColType_DB_MYSQL(var0);
         case 9:
            return getDefaultDBMDColType_DB_DERBY(var0);
      }
   }

   public static String getDefaultDBMSColType_DB_POINTBASE(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "BOOLEAN";
         }

         if (var0 == Byte.TYPE) {
            return "SMALLINT";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL";
         }

         if (var0 == Short.TYPE) {
            return "SMALLINT";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(38,19)";
         }

         if (var0 == Boolean.class) {
            return "BOOLEAN";
         }

         if (var0 == Byte.class) {
            return "SMALLINT";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL";
         }

         if (var0 == Short.class) {
            return "SMALLINT";
         }

         if (var0 == Date.class) {
            return "DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "DATE";
         }

         if (var0 == Time.class) {
            return "TIME";
         }

         if (var0 == Timestamp.class) {
            return "TIMESTAMP";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "BLOB";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "BLOB";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMSColType_DB_MYSQL(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "SMALLINT";
         }

         if (var0 == Byte.TYPE) {
            return "SMALLINT";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL";
         }

         if (var0 == Short.TYPE) {
            return "SMALLINT";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(38,19)";
         }

         if (var0 == Boolean.class) {
            return "TINYINT";
         }

         if (var0 == Byte.class) {
            return "SMALLINT";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL(32,0)";
         }

         if (var0 == Short.class) {
            return "SMALLINT";
         }

         if (var0 == Date.class) {
            return "DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "DATE";
         }

         if (var0 == Time.class) {
            return "TIME";
         }

         if (var0 == Timestamp.class) {
            return "TIMESTAMP";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "BLOB";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "BLOB";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMSColType_DB_ORACLE(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "INTEGER";
         }

         if (var0 == Byte.TYPE) {
            return "INTEGER";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "INTEGER";
         }

         if (var0 == Short.TYPE) {
            return "INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(38,19)";
         }

         if (var0 == Boolean.class) {
            return "INTEGER";
         }

         if (var0 == Byte.class) {
            return "INTEGER";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "INTEGER";
         }

         if (var0 == Short.class) {
            return "INTEGER";
         }

         if (var0 == Date.class) {
            return "DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "DATE";
         }

         if (var0 == Time.class) {
            return "DATE";
         }

         if (var0 == Timestamp.class) {
            return "DATE";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "RAW(1000)";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "RAW(1000)";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMSColType_DB_SQLSERVER(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "BIT";
         }

         if (var0 == Byte.TYPE) {
            return "TINYINT";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL(38,0)";
         }

         if (var0 == Short.TYPE) {
            return "INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(38,19)";
         }

         if (var0 == Boolean.class) {
            return "INTEGER";
         }

         if (var0 == Byte.class) {
            return "TINYINT";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL(38,0)";
         }

         if (var0 == Short.class) {
            return "SMALLINT";
         }

         if (var0 == Date.class) {
            return "DATETIME";
         }

         if (var0 == java.sql.Date.class) {
            return "DATETIME";
         }

         if (var0 == Time.class) {
            return "DATETIME";
         }

         if (var0 == Timestamp.class) {
            return "DATETIME";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "IMAGE";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "IMAGE";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMSColType_DB_INFORMIX(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "SMALLINT";
         }

         if (var0 == Byte.TYPE) {
            return "SMALLINT";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL(32)";
         }

         if (var0 == Short.TYPE) {
            return "INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(32,16)";
         }

         if (var0 == Boolean.class) {
            return "SMALLINT";
         }

         if (var0 == Byte.class) {
            return "SMALLINT";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL(32)";
         }

         if (var0 == Short.class) {
            return "INTEGER";
         }

         if (var0 == Date.class) {
            return "DATETIME YEAR TO DAY";
         }

         if (var0 == java.sql.Date.class) {
            return "DATETIME YEAR TO DAY";
         }

         if (var0 == Time.class) {
            return "DATETIME HOUR TO FRACTION";
         }

         if (var0 == Timestamp.class) {
            return "DATETIME YEAR TO FRACTION";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "BYTE";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "BYTE";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMDColType_DB_DERBY(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "INTEGER";
         }

         if (var0 == Byte.TYPE) {
            return "INTEGER";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL(31)";
         }

         if (var0 == Short.TYPE) {
            return "INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(31,16)";
         }

         if (var0 == Boolean.class) {
            return "INTEGER";
         }

         if (var0 == Byte.class) {
            return "INTEGER";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL(31)";
         }

         if (var0 == Short.class) {
            return "INTEGER";
         }

         if (var0 == Date.class) {
            return "DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "DATE";
         }

         if (var0 == Time.class) {
            return "DATE";
         }

         if (var0 == Timestamp.class) {
            return "DATETIME";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "BLOB";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "BLOB";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMSColType_DB_DB2(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "INTEGER";
         }

         if (var0 == Byte.TYPE) {
            return "INTEGER";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL(31)";
         }

         if (var0 == Short.TYPE) {
            return "INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(31,16)";
         }

         if (var0 == Boolean.class) {
            return "INTEGER";
         }

         if (var0 == Byte.class) {
            return "INTEGER";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL(31)";
         }

         if (var0 == Short.class) {
            return "INTEGER";
         }

         if (var0 == Date.class) {
            return "DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "DATE";
         }

         if (var0 == Time.class) {
            return "DATE";
         }

         if (var0 == Timestamp.class) {
            return "DATETIME";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "RAW(1000)";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "RAW(1000)";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String getDefaultDBMSColType(Class var0) throws Exception {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return "INTEGER";
         }

         if (var0 == Byte.TYPE) {
            return "INTEGER";
         }

         if (var0 == Character.TYPE) {
            return "CHAR(1)";
         }

         if (var0 == Double.TYPE) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.TYPE) {
            return "FLOAT";
         }

         if (var0 == Integer.TYPE) {
            return "INTEGER";
         }

         if (var0 == Long.TYPE) {
            return "DECIMAL(32)";
         }

         if (var0 == Short.TYPE) {
            return "INTEGER";
         }
      } else {
         if (var0 == String.class) {
            return "VARCHAR(150)";
         }

         if (var0 == BigDecimal.class) {
            return "DECIMAL(32,16)";
         }

         if (var0 == Boolean.class) {
            return "INTEGER";
         }

         if (var0 == Byte.class) {
            return "INTEGER";
         }

         if (var0 == Character.class) {
            return "CHAR(1)";
         }

         if (var0 == Double.class) {
            return "DOUBLE PRECISION";
         }

         if (var0 == Float.class) {
            return "FLOAT";
         }

         if (var0 == Integer.class) {
            return "INTEGER";
         }

         if (var0 == Long.class) {
            return "DECIMAL(32)";
         }

         if (var0 == Short.class) {
            return "INTEGER";
         }

         if (var0 == Date.class) {
            return "DATE";
         }

         if (var0 == java.sql.Date.class) {
            return "DATE";
         }

         if (var0 == Time.class) {
            return "DATE";
         }

         if (var0 == Timestamp.class) {
            return "DATETIME";
         }

         if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "RAW(1000)";
         }

         if (!ClassUtils.isValidSQLType(var0) && Serializable.class.isAssignableFrom(var0)) {
            return "RAW(1000)";
         }
      }

      throw new Exception("Could not determine default DBMS Column type from Java type: '" + var0.toString() + "'");
   }

   public static String decodeArrayTypes(Class var0) {
      return var0.getName().indexOf("[L") != -1 ? decodeObjectTypeArrayMaybe(var0) : decodePrimitiveTypeArrayMaybe(var0);
   }

   public static String decodeObjectTypeArrayMaybe(Class var0) {
      StringBuffer var1 = new StringBuffer("[]");
      int var2 = var0.getName().lastIndexOf(91);

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = var1.append("[]");
      }

      Class var4;
      for(var4 = var0; var4.isArray(); var4 = var4.getComponentType()) {
      }

      return var4.getName() + var1;
   }

   public static String decodePrimitiveTypeArrayMaybe(Class var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = var0.getName().length() - 1;
      if (var2 > 0) {
         for(int var3 = 0; var3 < var2; ++var3) {
            var1 = var1.append("[]");
         }
      }

      if (var0.getName().endsWith("[Z")) {
         return "boolean" + var1;
      } else if (var0.getName().endsWith("[C")) {
         return "char" + var1;
      } else if (var0.getName().endsWith("[B")) {
         return "byte" + var1;
      } else if (var0.getName().endsWith("[S")) {
         return "short" + var1;
      } else if (var0.getName().endsWith("[I")) {
         return "int" + var1;
      } else if (var0.getName().endsWith("[J")) {
         return "long" + var1;
      } else if (var0.getName().endsWith("[F")) {
         return "float" + var1;
      } else {
         return var0.getName().endsWith("[D") ? "double" + var1 : var0.getName();
      }
   }

   public static String decodePrimitiveTypeArrayMaybe(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         StringBuffer var1 = new StringBuffer();
         int var2 = var0.length() - 1;
         if (var2 > 0) {
            for(int var3 = 0; var3 < var2; ++var3) {
               var1 = var1.append("[]");
            }
         }

         if (var0.endsWith("[Z")) {
            return "boolean" + var1;
         } else if (var0.endsWith("[C")) {
            return "char" + var1;
         } else if (var0.endsWith("[B")) {
            return "byte" + var1;
         } else if (var0.endsWith("[S")) {
            return "short" + var1;
         } else if (var0.endsWith("[I")) {
            return "int" + var1;
         } else if (var0.endsWith("[J")) {
            return "long" + var1;
         } else if (var0.endsWith("[F")) {
            return "float" + var1;
         } else {
            return var0.endsWith("[D") ? "double" + var1 : var0;
         }
      }
   }
}
