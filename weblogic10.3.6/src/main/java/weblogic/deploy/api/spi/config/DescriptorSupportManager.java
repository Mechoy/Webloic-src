package weblogic.deploy.api.spi.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.beangen.XMLHelper;
import weblogic.j2ee.descriptor.wl.ConfigurationSupportBean;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;

public class DescriptorSupportManager {
   private static List ddSupport = new ArrayList();
   private static final boolean debug = Debug.isDebug("config");
   public static final String WEB_ROOT = "web-app";
   public static final String WLS_WEB_ROOT = "weblogic-web-app";
   public static final String WEB_NAMESPACE = "http://java.sun.com/xml/ns/j2ee";
   public static final String WLS_WEB_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String WEB_URI = "WEB-INF/web.xml";
   public static final String WLS_WEB_URI = "WEB-INF/weblogic.xml";
   public static final String EAR_ROOT = "application";
   public static final String WLS_EAR_ROOT = "weblogic-application";
   public static final String EAR_NAMESPACE = "http://java.sun.com/xml/ns/j2ee";
   public static final String WLS_EAR_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String EAR_URI = "META-INF/application.xml";
   public static final String WLS_EAR_URI = "META-INF/weblogic-application.xml";
   public static final String EJB_ROOT = "ejb-jar";
   public static final String WLS_EJB_ROOT = "weblogic-ejb-jar";
   public static final String EJB_NAMESPACE = "http://java.sun.com/xml/ns/j2ee";
   public static final String WLS_EJB_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String EJB_URI = "META-INF/ejb-jar.xml";
   public static final String WLS_EJB_URI = "META-INF/weblogic-ejb-jar.xml";
   public static final String RAR_ROOT = "connector";
   public static final String WLS_RAR_ROOT = "weblogic-connector";
   public static final String RAR_NAMESPACE = "http://java.sun.com/xml/ns/j2ee";
   public static final String WLS_RAR_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String RAR_URI = "META-INF/ra.xml";
   public static final String WLS_RAR_URI = "META-INF/weblogic-ra.xml";
   public static final String CAR_ROOT = "application-client";
   public static final String WLS_CAR_ROOT = "weblogic-application-client";
   public static final String CAR_NAMESPACE = "http://java.sun.com/xml/ns/j2ee";
   public static final String WLS_CAR_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String CAR_URI = "META-INF/application-client.xml";
   public static final String WLS_CAR_URI = "META-INF/weblogic-application-client.xml";
   public static final String WLS_CMP_ROOT = "weblogic-rdbms-jar";
   public static final String WLS_CMP_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String WLS_CMP_URI = "META-INF/weblogic-cmp-rdbms-jar.xml";
   public static final String WLS_CMP11_ROOT = "weblogic-rdbms-jar";
   public static final String WLS_CMP11_NAMESPACE = "http://www.bea.com/ns/weblogic/60";
   public static final String WLS_CMP11_URI = "META-INF/weblogic-cmp-rdbms-jar.xml";
   public static final String WLS_CMP11_CLASS = "weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanImpl";
   public static final String WLS_CMP11_DCONFIGCLASS = "weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanDConfig";
   public static final String WLS_CMP11_CONFIGCLASS = "weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanImpl";
   public static final String WLS_JMS_ROOT = "weblogic-jms";
   public static final String WLS_JMS_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String WLS_JDBC_ROOT = "jdbc-data-source";
   public static final String WLS_JDBC_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String WLS_INTERCEPT_ROOT = "weblogic-interception";
   public static final String WLS_INTERCEPT_NAMESPACE = "http://www.bea.com/ns/weblogic/90";
   public static final String WSEE_ROOT_81 = "web-services";
   public static final String WSEE_ROOT = "webservices";
   public static final String WLS_WSEE_ROOT = "weblogic-webservices";
   public static final String WSEE_WEB_URI_81 = "WEB-INF/web-services.xml";
   public static final String WSEE_EJB_URI_81 = "META-INF/web-services.xml";
   public static final String WSEE_WEB_URI = "WEB-INF/webservices.xml";
   public static final String WSEE_EJB_URI = "META-INF/webservices.xml";
   public static final String WLS_WSEE_WEB_URI = "WEB-INF/weblogic-webservices.xml";
   public static final String WLS_WSEE_EJB_URI = "META-INF/weblogic-webservices.xml";
   public static final String WLS_WS_POLICY_WEB_URI = "WEB-INF/weblogic-webservices-policy.xml";
   public static final String WLS_WS_POLICY_EJB_URI = "META-INF/weblogic-webservices-policy.xml";
   public static final String PERSISTENCE_ROOT = "persistence";
   public static final String WLS_PERSISTENCE_ROOT = "persistence-configuration";
   public static final String PERSISTENCE_URI = "META-INF/persistence.xml";
   public static final String WLS_PERSISTENCE_URI = "META-INF/persistence-configuration.xml";
   public static final String PERSISTENCE_NAMESPACE = "http://java.sun.com/xml/ns/persistence";
   public static final String WLS_PERSISTENCE_NAMESPACE = "http://bea.com/ns/weblogic/950/persistence";
   public static final String PERSISTENCE_BEAN_CLASS = "weblogic.j2ee.descriptor.PersistenceBean";
   public static final String WLS_PERSISTENCE_BEAN_CLASS = "kodo.jdbc.conf.descriptor.PersistenceConfigurationBean";
   public static final String WLS_PERSISTENCE_DCONFIG_CLASS = "kodo.jdbc.conf.descriptor.PersistenceConfigurationBeanDConfig";
   public static final String WLS_WLDF_ROOT = "wldf-resource";
   public static final String WLS_WLDF_NAMESPACE = "java:weblogic.diagnostics.descriptor";
   public static final String WLS_WLDF_URI = "META-INF/weblogic-diagnostics.xml";
   public static final String WLS_WLDF_BEAN_CLASS = "weblogic.diagnostics.descriptor.WLDFResourceBeanImpl";
   public static final String WLS_WLDF_DCONFIG_CLASS = "weblogic.diagnostics.descriptor.WLDFResourceBeanDConfig";
   private static Map forceWrites = new HashMap();
   public static final String WLS_EAR_EXT_URI = "META-INF/weblogic-extension.xml";
   public static final String WLS_WEB_EXT_URI = "WEB-INF/weblogic-extension.xml";
   public static final DescriptorSupport EJB_DESC_SUPPORT;

   public static void flush() {
      Iterator var0 = ddSupport.iterator();

      while(var0.hasNext()) {
         DescriptorSupport var1 = (DescriptorSupport)var0.next();
         if (var1.isFlush()) {
            if (debug) {
               Debug.say("removing DS: " + var1.toString());
            }

            var0.remove();
         }
      }

   }

   public static void add(ModuleType var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
      add(new DescriptorSupport(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, false));
   }

   public static void add(ModuleType var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      add(var0, var1, var2, var3, var4, var5, var6, createImplClassName(var7, var1), createImplClassName(var8, var2), createDConfigClassName(var8, var2));
   }

   public static void add(DescriptorSupport var0) {
      if (!ddSupport.contains(var0)) {
         var0.setFlush(true);
         ddSupport.add(var0);
      }

   }

   private static String createImplClassName(String var0, String var1) {
      return var0.concat("." + XMLHelper.toPropName(var1) + "BeanImpl");
   }

   private static String createDConfigClassName(String var0, String var1) {
      return var0.concat("." + XMLHelper.toPropName(var1) + "BeanDConfig");
   }

   public static DescriptorSupport getForTag(String var0) {
      String var1 = removeNamespace(var0);
      Iterator var3 = ddSupport.iterator();

      DescriptorSupport var2;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var2 = (DescriptorSupport)var3.next();
      } while(!var2.isPrimary() || !var2.getBaseTag().equals(var1));

      return copy(var2);
   }

   public static DescriptorSupport[] getForSecondaryTag(String var0) {
      String var1 = removeNamespace(var0);
      ArrayList var2 = new ArrayList();
      Iterator var4 = ddSupport.iterator();

      while(var4.hasNext()) {
         DescriptorSupport var3 = (DescriptorSupport)var4.next();
         if (!var3.isPrimary() && var3.getBaseTag().equals(var1)) {
            var2.add(copy(var3));
         }
      }

      return (DescriptorSupport[])((DescriptorSupport[])var2.toArray(new DescriptorSupport[0]));
   }

   public static DescriptorSupport[] getForBaseURI(String var0) {
      ArrayList var1 = new ArrayList();
      Iterator var3 = ddSupport.iterator();

      while(var3.hasNext()) {
         DescriptorSupport var2 = (DescriptorSupport)var3.next();
         if (var2.getBaseURI().equals(var0)) {
            var1.add(copy(var2));
         }
      }

      return (DescriptorSupport[])((DescriptorSupport[])var1.toArray(new DescriptorSupport[0]));
   }

   private static String removeNamespace(String var0) {
      int var1 = var0.indexOf(58);
      if (var1 != -1) {
         var0 = var0.substring(var1 + 1);
      }

      return var0;
   }

   public static DescriptorSupport[] getForModuleType(ModuleType var0) throws IllegalArgumentException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = ddSupport.iterator();

      while(var3.hasNext()) {
         DescriptorSupport var1 = (DescriptorSupport)var3.next();
         if (var1.getModuleType().equals(var0)) {
            var2.add(copy(var1));
         }
      }

      return (DescriptorSupport[])((DescriptorSupport[])var2.toArray(new DescriptorSupport[0]));
   }

   public static DescriptorSupport getForDConfigClass(String var0) throws IllegalArgumentException {
      Iterator var2 = ddSupport.iterator();

      DescriptorSupport var1;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var1 = (DescriptorSupport)var2.next();
      } while(!var1.getDConfigClassName().equals(var0));

      return copy(var1);
   }

   public static DescriptorSupport getForConfigClass(String var0) throws IllegalArgumentException {
      Iterator var2 = ddSupport.iterator();

      DescriptorSupport var1;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var1 = (DescriptorSupport)var2.next();
      } while(!var1.getConfigClassName().equals(var0));

      return copy(var1);
   }

   public static DescriptorSupport getForStandardClass(String var0) throws IllegalArgumentException {
      Iterator var2 = ddSupport.iterator();

      DescriptorSupport var1;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var1 = (DescriptorSupport)var2.next();
      } while(!var1.getStandardClassName().equals(var0));

      return copy(var1);
   }

   public static DescriptorSupport getForClass(String var0) throws IllegalArgumentException {
      Iterator var2 = ddSupport.iterator();

      DescriptorSupport var1;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var1 = (DescriptorSupport)var2.next();
         if (var1.getStandardClassName().equals(var0)) {
            return copy(var1);
         }
      } while(!var1.getDConfigClassName().equals(var0));

      return copy(var1);
   }

   public static DescriptorSupport[] getForModuleType(String var0) throws IllegalArgumentException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = ddSupport.iterator();

      while(var3.hasNext()) {
         DescriptorSupport var1 = (DescriptorSupport)var3.next();
         if (var1.getModuleType().toString().equals(var0)) {
            var2.add(copy(var1));
         }
      }

      if (var2.isEmpty()) {
         throw new IllegalArgumentException(SPIDeployerLogger.unsupportedModuleType(var0.toString()));
      } else {
         return (DescriptorSupport[])((DescriptorSupport[])var2.toArray(new DescriptorSupport[0]));
      }
   }

   private static DescriptorSupport copy(DescriptorSupport var0) {
      return new DescriptorSupport(var0.getModuleType(), var0.getBaseTag(), var0.getConfigTag(), var0.getBaseNameSpace(), var0.getConfigNameSpace(), var0.getBaseURI(), var0.getConfigURI(), var0.getStandardClassName(), var0.getConfigClassName(), var0.getDConfigClassName(), var0.isPrimary());
   }

   private static void initDescriptorSupport() {
      try {
         ddSupport.add(new DescriptorSupport(ModuleType.WAR, "web-app", "weblogic-web-app", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "WEB-INF/web.xml", "WEB-INF/weblogic.xml", "weblogic.j2ee.descriptor.WebAppBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebAppBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebAppBeanDConfig", true));
         ddSupport.add(new DescriptorSupport(ModuleType.EAR, "application", "weblogic-application", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/application.xml", "META-INF/weblogic-application.xml", "weblogic.j2ee.descriptor.ApplicationBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicApplicationBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicApplicationBeanDConfig", true));
         ddSupport.add(EJB_DESC_SUPPORT);
         ddSupport.add(new DescriptorSupport(ModuleType.RAR, "connector", "weblogic-connector", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/ra.xml", "META-INF/weblogic-ra.xml", "weblogic.j2ee.descriptor.ConnectorBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicConnectorBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicConnectorBeanDConfig", true));
         ddSupport.add(new DescriptorSupport(ModuleType.CAR, "application-client", "weblogic-application-client", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/application-client.xml", "META-INF/weblogic-application-client.xml", "weblogic.j2ee.descriptor.ApplicationClientBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicApplicationClientBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicApplicationClientBeanDConfig", true));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.JMS, "weblogic-jms", "weblogic-jms", "http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/90", ".", ".", "weblogic.j2ee.descriptor.wl.JMSBeanImpl", "weblogic.j2ee.descriptor.wl.JMSBeanImpl", "weblogic.j2ee.descriptor.wl.JMSBeanDConfig", true));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.JDBC, "jdbc-data-source", "jdbc-data-source", "http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/90", ".", ".", "weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanImpl", "weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanImpl", "weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig", true));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.INTERCEPT, "weblogic-interception", "weblogic-interception", "http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/90", ".", ".", "weblogic.j2ee.descriptor.wl.InterceptionBeanImpl", "weblogic.j2ee.descriptor.wl.InterceptionBeanImpl", "weblogic.j2ee.descriptor.wl.InterceptionBeanDConfig", true));
         ddSupport.add(new DescriptorSupport(ModuleType.EJB, "weblogic-rdbms-jar", "weblogic-rdbms-jar", "http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/90", "META-INF/weblogic-cmp-rdbms-jar.xml", "META-INF/weblogic-cmp-rdbms-jar.xml", "weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig", false));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.WLDF, "wldf-resource", "wldf-resource", "java:weblogic.diagnostics.descriptor", "java:weblogic.diagnostics.descriptor", "META-INF/weblogic-diagnostics.xml", "META-INF/weblogic-diagnostics.xml", "weblogic.diagnostics.descriptor.WLDFResourceBeanImpl", "weblogic.diagnostics.descriptor.WLDFResourceBeanImpl", "weblogic.diagnostics.descriptor.WLDFResourceBeanDConfig", false));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.WSEE, "webservices", "weblogic-webservices", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/webservices.xml", "META-INF/weblogic-webservices.xml", "weblogic.j2ee.descriptor.WebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanDConfig", false));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.WSEE, "webservices", "weblogic-webservices", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "WEB-INF/webservices.xml", "WEB-INF/weblogic-webservices.xml", "weblogic.j2ee.descriptor.WebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanDConfig", false));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.WSEE, "web-services", "weblogic-webservices", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/web-services.xml", "META-INF/weblogic-webservices.xml", "weblogic.j2ee.descriptor.WebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanDConfig", false));
         ddSupport.add(new DescriptorSupport(WebLogicModuleType.WSEE, "web-services", "weblogic-webservices", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "WEB-INF/web-services.xml", "WEB-INF/weblogic-webservices.xml", "weblogic.j2ee.descriptor.WebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicWebservicesBeanDConfig", false));
         DescriptorSupport var0 = new DescriptorSupport(WebLogicModuleType.WSEE, "webservice-policy-ref", "webservice-policy-ref", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/weblogic-webservices-policy.xml", "META-INF/weblogic-webservices-policy.xml", "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanImpl", "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanImpl", "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanDConfig", false);
         ddSupport.add(var0);
         forceWrites.put("META-INF/weblogic-webservices-policy.xml", var0);
         var0 = new DescriptorSupport(WebLogicModuleType.WSEE, "webservice-policy-ref", "webservice-policy-ref", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "WEB-INF/weblogic-webservices-policy.xml", "WEB-INF/weblogic-webservices-policy.xml", "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanImpl", "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanImpl", "weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanDConfig", false);
         ddSupport.add(var0);
         forceWrites.put("WEB-INF/weblogic-webservices-policy.xml", var0);
      } catch (Exception var2) {
         var2.printStackTrace();
         throw new AssertionError(var2.toString());
      }
   }

   public static DescriptorSupport getForceWriteDS(String var0) {
      return (DescriptorSupport)forceWrites.get(var0);
   }

   public static void registerWebLogicExtensions(WeblogicExtensionBean var0, String var1) {
      if (var0 != null) {
         CustomModuleBean[] var2 = var0.getCustomModules();
         if (var2 != null) {
            try {
               for(int var3 = 0; var3 < var2.length; ++var3) {
                  CustomModuleBean var4 = var2[var3];
                  ConfigurationSupportBean var5 = var4.getConfigurationSupport();
                  if (var5 != null) {
                     String var6 = var5.getBaseRootElement();
                     if (var6 == null && debug) {
                        Debug.say(SPIDeployerLogger.getMissingExt(var1, "base-root-element", var4.getUri(), var4.getProviderName()));
                     }

                     String var7 = var5.getConfigRootElement();
                     if (var7 == null) {
                        var7 = var6;
                     }

                     String var8 = var5.getBaseNamespace();
                     if (var8 == null && debug) {
                        Debug.say(SPIDeployerLogger.getMissingExt(var1, "base-namespace", var4.getUri(), var4.getProviderName()));
                     }

                     String var9 = var5.getConfigNamespace();
                     if (var9 == null) {
                        var9 = var8;
                     }

                     String var10 = var5.getBaseUri();
                     if (var10 == null && debug) {
                        Debug.say(SPIDeployerLogger.getMissingExt(var1, "base-uri", var4.getUri(), var4.getProviderName()));
                     }

                     String var11 = var5.getConfigUri();
                     if (var11 == null) {
                        var11 = var10;
                     }

                     String var12 = var5.getBasePackageName();
                     if (var12 == null && debug) {
                        Debug.say(SPIDeployerLogger.getMissingExt(var1, "base-package-name", var4.getUri(), var4.getProviderName()));
                     }

                     String var13 = var5.getConfigPackageName();
                     if (var13 == null) {
                        var13 = var12;
                     }

                     SPIDeployerLogger.logAddDS(var10, var11);
                     add(WebLogicModuleType.CONFIG, var6, var7, var8, var9, var10, var11, var12, var13);
                  }
               }
            } catch (IllegalArgumentException var14) {
               if (debug) {
                  Debug.say(var14.toString());
               }
            }

         }
      }
   }

   static {
      EJB_DESC_SUPPORT = new DescriptorSupport(ModuleType.EJB, "ejb-jar", "weblogic-ejb-jar", "http://java.sun.com/xml/ns/j2ee", "http://www.bea.com/ns/weblogic/90", "META-INF/ejb-jar.xml", "META-INF/weblogic-ejb-jar.xml", "weblogic.j2ee.descriptor.EjbJarBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicEjbJarBeanImpl", "weblogic.j2ee.descriptor.wl.WeblogicEjbJarBeanDConfig", true);
      initDescriptorSupport();
   }
}
