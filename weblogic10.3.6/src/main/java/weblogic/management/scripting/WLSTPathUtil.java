package weblogic.management.scripting;

import java.io.IOException;
import java.util.ArrayList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.utils.StringUtils;

public class WLSTPathUtil {
   private static final String JMS_SR = "JMSSystemResources";
   private static final String JDBC_SR = "JDBCSystemResources";
   private static final String WLDF_SR = "WLDFSystemResources";
   private static final String REALM_MBEAN = "weblogic.management.security.RealmMBean";
   private static final String SECURITY_STORE_MBEAN = "weblogic.management.security.RDBMSSecurityStoreMBean";
   private static final String ULM_MBEAN = "weblogic.management.security.authentication.UserLockoutManagerMBean";
   private static final String PROVIDER_MBEAN = "weblogic.management.security.ProviderMBean";

   public static String lookupPath(MBeanServerConnection var0, String var1, ObjectName var2) throws Exception {
      ObjectName var3 = var2;
      String var4 = var2.getKeyProperty("Name");
      ObjectName var5 = null;
      String var6 = null;

      String var10;
      String var12;
      String var17;
      String var19;
      try {
         var5 = (ObjectName)var0.getAttribute(var3, "Parent");
      } catch (AttributeNotFoundException var15) {
         String var8 = var2.getKeyProperty("Parent");
         if (var8 != null) {
            if (var8.indexOf("/") == -1) {
               return null;
            }

            var17 = var8.substring(var8.indexOf("/"), var8.length());
            int var18 = var17.indexOf("[");
            var19 = var17.substring(1, var18);
            var12 = var17.substring(var18 + 1, var17.length() - 1);
            if (var19.equals("JMSSystemResources") || var19.equals("JDBCSystemResources") || var19.equals("WLDFSystemResources")) {
               return getSystemResourcePath(var2.getKeyProperty("Path"), var19, var12);
            }
         } else {
            ModelMBeanInfo var9 = (ModelMBeanInfo)var0.getMBeanInfo(var2);
            var10 = var9.getClassName();
            Class var11 = DescriptorClassLoader.loadClass(var10);
            if (isSecurityMBean(var11)) {
               var12 = "SecurityConfiguration/" + var1;
               if (isRealm(var11)) {
                  var12 = var12 + "/Realms/" + var2.getKeyProperty("Name");
                  return var12;
               }

               ObjectName var13;
               if (isProvider(var11)) {
                  var13 = findRealmON(var2, var0);
                  String var14 = getAttributeNameFromRealm(var13, var2, var0);
                  if (var14 != null) {
                     var12 = lookupPath(var0, var1, var13);
                     var12 = var12 + "/" + var14 + "/" + (String)var0.getAttribute(var2, "Name");
                     return var12;
                  }
               } else if (isUserLockOut(var11)) {
                  var13 = findRealmON(var2, var0);
                  var12 = lookupPath(var0, var1, var13);
                  var12 = var12 + "/UserLockoutManager/UserLockoutManager";
                  return var12;
               }
            }
         }
      }

      while(var4 == null && var5 != null) {
         var4 = var5.getKeyProperty("Name");
         var5 = (ObjectName)var0.getAttribute(var3, "Parent");
      }

      String var7 = getRightType(var3.getKeyProperty("Type"), (ObjectName)var0.getAttribute(var3, "Parent"), var0);
      ObjectName var16 = (ObjectName)var0.getAttribute(var3, "Parent");
      if (var16 != null) {
         var7 = getTheRightAttributeName(var0, var16, var7);
      }

      var17 = var7 + "/" + var4;
      var10 = "";

      while(var16 != null && !var0.isInstanceOf(var16, "weblogic.management.configuration.DomainMBean") && !var0.isInstanceOf(var16, "weblogic.management.runtime.DomainRuntimeMBean") && !var0.isInstanceOf(var16, "weblogic.management.runtime.ServerRuntimeMBean")) {
         var19 = getRightType(var16.getKeyProperty("Type"), (ObjectName)var0.getAttribute(var16, "Parent"), var0);
         if (var0.getAttribute(var16, "Parent") != null) {
            var19 = getTheRightAttributeName(var0, (ObjectName)var0.getAttribute(var16, "Parent"), var19);
         }

         if (var19.equals("Application") || var19.equals("ComponentRuntimes")) {
            var6 = var3.getKeyProperty("Name");
         }

         if (var10.length() == 0) {
            var10 = var19 + "/" + var16.getKeyProperty("Name") + var10;
         } else {
            var10 = var19 + "/" + var16.getKeyProperty("Name") + "/" + var10;
         }

         if (var19.equals("Application")) {
            var6 = var16.getKeyProperty("Name");
         }

         if (var0.getAttribute(var16, "Parent") != null) {
            var16 = (ObjectName)var0.getAttribute(var16, "Parent");
         } else {
            var16 = var3;
         }
      }

      var19 = "";
      if (var10.length() != 0) {
         var19 = var10 + "/" + var17;
      } else {
         var19 = var17;
      }

      var12 = hackThePath(var19, var6);
      return var12;
   }

   private static String getTheRightAttributeName(MBeanServerConnection var0, ObjectName var1, String var2) throws Exception {
      ModelMBeanInfo var3 = (ModelMBeanInfo)var0.getMBeanInfo(var1);
      MBeanAttributeInfo[] var4 = var3.getAttributes();
      String var5 = "";
      if (var2.endsWith("Runtime")) {
         var5 = "weblogic.management.runtime." + var2 + "MBean";
      } else {
         var5 = "weblogic.management.configuration." + var2 + "MBean";
      }

      for(int var6 = 0; var6 < var4.length; ++var6) {
         Descriptor var7 = ((ModelMBeanAttributeInfo)var4[var6]).getDescriptor();
         String var8 = (String)((ModelMBeanAttributeInfo)var4[var6]).getDescriptor().getFieldValue("interfaceClassName");
         if (var8 != null) {
            if (var8.startsWith("[L")) {
               var8 = var8.substring(2, var8.length() - 1);
            }

            if (var5.equals(var8)) {
               return var4[var6].getName();
            }

            if (var8.endsWith("MBean") && Class.forName(var8).isAssignableFrom(Class.forName(var5)) && !var4[var6].getName().equals("Parent") && !var4[var6].getName().equals("Targets")) {
               return var4[var6].getName();
            }
         }
      }

      return var2;
   }

   private static String getSystemResourcePath(String var0, String var1, String var2) {
      int var3 = 0;
      int var4 = 0;
      StringBuffer var5 = new StringBuffer();

      ArrayList var6;
      String var8;
      for(var6 = new ArrayList(); var3 < var0.length(); var3 = var4 + 1) {
         int var7 = var0.indexOf("[", var3);
         if (var7 != -1) {
            var4 = var0.indexOf("]", var7);
         }

         if (var7 == -1 || var4 == -1) {
            var5.append(var0.substring(var3));
            break;
         }

         var8 = var0.substring(var7 + 1, var4);
         var6.add(var8);
         var5.append(var0.substring(var3, var7));
         var5.append("[]");
      }

      StringBuffer var12 = new StringBuffer(var1);
      var12.append("/");
      var12.append(var2);
      var8 = var2;
      String[] var9 = StringUtils.splitCompletely(var5.toString(), "/");

      for(int var10 = 0; var10 < var9.length; ++var10) {
         var12.append("/");
         int var11 = var9[var10].indexOf("[]");
         if (var11 != -1) {
            var8 = (String)var6.remove(0);
            var12.append(var9[var10].substring(0, var11));
         } else {
            var12.append(var9[var10]);
         }

         var12.append("/");
         var12.append(var8);
      }

      return var12.toString();
   }

   private static String getAttributeNameFromRealm(ObjectName var0, ObjectName var1, MBeanServerConnection var2) throws Exception {
      MBeanAttributeInfo[] var3 = var2.getMBeanInfo(var0).getAttributes();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         MBeanAttributeInfo var5 = var3[var4];
         if (var5.getType().equals("javax.management.ObjectName")) {
            ObjectName var8 = (ObjectName)var2.getAttribute(var0, var5.getName());
            if (var8 != null && var8.toString().equals(var1.toString())) {
               return var5.getName();
            }
         } else if (var5.getType().equals("[Ljavax.management.ObjectName;")) {
            ObjectName[] var6 = (ObjectName[])((ObjectName[])var2.getAttribute(var0, var5.getName()));
            if (var6 != null && var6.length > 0) {
               for(int var7 = 0; var7 < var6.length; ++var7) {
                  if (var6[var7].toString().equals(var1.toString())) {
                     return var5.getName();
                  }
               }
            }
         }
      }

      return null;
   }

   private static ObjectName findRealmON(ObjectName var0, MBeanServerConnection var1) throws MBeanException, AttributeNotFoundException, ReflectionException, InstanceNotFoundException, IOException {
      return (ObjectName)var1.getAttribute(var0, "Realm");
   }

   private static String hackThePath(String var0, String var1) {
      String var2;
      if (!var0.startsWith("/Application/") && !var0.startsWith("Application/")) {
         if (var0.indexOf("/EJBTransactionRuntime/") != -1) {
            var2 = StringUtils.replaceGlobal(var0, "/EJBTransactionRuntime/", "/EJBRuntimes/" + var1 + "/TransactionRuntime/");
            return var2;
         } else if (var0.indexOf("/EJBCacheRuntime/") != -1) {
            var2 = StringUtils.replaceGlobal(var0, "/EJBCacheRuntime/", "/EJBRuntimes/" + var1 + "/CacheRuntime/");
            return var2;
         } else if (var0.indexOf("/EJBLockingRuntime/") != -1) {
            var2 = StringUtils.replaceGlobal(var0, "/EJBLockingRuntime/", "/EJBRuntimes/" + var1 + "/LockingRuntime/");
            return var2;
         } else if (var0.indexOf("/EJBPoolRuntime/") != -1) {
            var2 = StringUtils.replaceGlobal(var0, "/EJBPoolRuntime/", "/EJBRuntimes/" + var1 + "/PoolRuntime/");
            return var2;
         } else {
            return var0;
         }
      } else {
         var2 = var0.replaceFirst("Application/" + var1, "AppDeployments/" + var1 + "/AppMBean/" + var1);
         return var2;
      }
   }

   private static String getRightType(String var0, ObjectName var1, MBeanServerConnection var2) {
      if (var1 == null) {
         return var0 + "s";
      } else {
         try {
            MBeanAttributeInfo[] var3 = var2.getMBeanInfo(var1).getAttributes();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               MBeanAttributeInfo var5 = var3[var4];
               String var6 = var5.getName();
               String var7 = var5.getType();
               if (var5.getName().startsWith(var0)) {
                  if (var5.getType().indexOf("MBean") != -1) {
                     return var5.getName();
                  }
               } else {
                  String var9 = var5.getType();
                  String var8;
                  Class var10;
                  if (var9.startsWith("[L") && var9.indexOf("MBean") != -1) {
                     var9 = var9.substring(2, var9.length() - 1);
                     var10 = Class.forName(var9);
                     if (var0.endsWith("Runtime")) {
                        var8 = "weblogic.management.runtime." + var0 + "MBean";
                     } else {
                        var8 = "weblogic.management.configuration." + var0 + "MBean";
                     }

                     if (var10.isAssignableFrom(Class.forName(var8))) {
                        return var5.getName();
                     }
                  } else if (var5.getType().indexOf("MBean") != -1 && !var5.getType().equals("weblogic.management.WebLogicMBean")) {
                     var10 = Class.forName(var9);
                     if (var0.endsWith("Runtime")) {
                        var8 = "weblogic.management.runtime." + var0 + "MBean";
                     } else {
                        var8 = "weblogic.management.configuration." + var0 + "MBean";
                     }

                     if (var10.isAssignableFrom(Class.forName(var8))) {
                        return var5.getName();
                     }
                  }
               }
            }

            return var0;
         } catch (Throwable var11) {
            return var0;
         }
      }
   }

   private static boolean isSecurityMBean(Class var0) throws ClassNotFoundException {
      return Class.forName("weblogic.management.security.RealmMBean").isAssignableFrom(var0) || Class.forName("weblogic.management.security.ProviderMBean").isAssignableFrom(var0) || Class.forName("weblogic.management.security.authentication.UserLockoutManagerMBean").isAssignableFrom(var0) || Class.forName("weblogic.management.security.RDBMSSecurityStoreMBean").isAssignableFrom(var0);
   }

   private static boolean isRealm(Class var0) throws ClassNotFoundException {
      return Class.forName("weblogic.management.security.RealmMBean").isAssignableFrom(var0);
   }

   private static boolean isProvider(Class var0) throws ClassNotFoundException {
      return Class.forName("weblogic.management.security.ProviderMBean").isAssignableFrom(var0);
   }

   private static boolean isUserLockOut(Class var0) throws ClassNotFoundException {
      return Class.forName("weblogic.management.security.authentication.UserLockoutManagerMBean").isAssignableFrom(var0);
   }
}
