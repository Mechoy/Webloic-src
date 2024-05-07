package weblogic.j2ee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.application.ApplicationFileManager;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.logging.Loggable;
import weblogic.management.ApplicationException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.utils.jars.VirtualJarFile;

public class J2EEUtils {
   private static final boolean debug = false;
   private static final String sep;
   public static final String APP_DD_NAME = "application.xml";
   public static final String APP_DD_PATH;
   public static final String APP_DD_URI = "META-INF/application.xml";
   public static final String WLAPP_DD_NAME = "weblogic-application.xml";
   public static final String WLAPP_DD_URI = "META-INF/weblogic-application.xml";
   public static final String WLAPP_DD_PATH;
   public static final String WEB_DD_NAME = "web.xml";
   public static final String WEB_DD_URI = "WEB-INF/web.xml";
   public static final String WEB_DD_PATH;
   public static final String WLWEB_DD_NAME = "weblogic.xml";
   public static final String WLWEB_DD_URI = "WEB-INF/weblogic.xml";
   public static final String WEBSERVICE_DD_NAME = "web-services.xml";
   public static final String WEBSERVICE_DD_URI = "WEB-INF/web-services.xml";
   public static final String WEBSERVICE_DD_PATH;
   public static final String EJB_DD_NAME = "ejb-jar.xml";
   public static final String EJB_DD_URI = "META-INF/ejb-jar.xml";
   public static final String EJB_DD_PATH;
   public static final String PER_DD_NAME = "persistence.xml";
   public static final String PER_DD_URI = "META-INF/persistence.xml";
   public static final String PER_DD_PATH;
   public static final String PERCONF_DD_NAME = "persistence-configuration.xml";
   public static final String PERCONF_DD_URI = "META-INF/persistence-configuration.xml";
   public static final String PERCONF_DD_PATH;
   public static final String WLEJB_DD_NAME = "weblogic-ejb-jar.xml";
   public static final String WLEJB_DD_URI = "META-INF/weblogic-ejb-jar.xml";
   public static final String WLEJBCMP_DD_NAME = "weblogic-rdbms-jar.xml";
   public static final String RAR_DD_NAME = "ra.xml";
   public static final String RAR_DD_URI = "META-INF/ra.xml";
   public static final String RAR_DD_PATH;
   public static final String WLRAR_DD_NAME = "weblogic-ra.xml";
   public static final String WLRAR_DD_URI = "META-INF/weblogic-ra.xml";
   public static final String WLRAR_DD_PATH;
   public static final String CAR_DD_NAME = "application-client.xml";
   public static final String CAR_DD_URI = "META-INF/application-client.xml";
   public static final String CAR_DD_PATH;
   public static final String WLCAR_DD_NAME = "weblogic-application-client.xml";
   public static final String WLCAR_DD_URI = "META-INF/weblogic-application-client.xml";
   public static final String WLCAR_DD_PATH;
   public static final String APP_INF = "APP-INF";
   public static final String APP_INF_LIB;
   public static final String APP_INF_CLASS;
   public static final String APPLICATION_POSTFIX = ".ear";
   public static final String EJBJAR_POSTFIX = ".jar";
   public static final String WEBAPP_POSTFIX = ".war";
   public static final String CONNECTOR_POSTFIX = ".rar";
   public static final String CLIENT_POSTFIX = ".jar";
   public static final String JMS_POSTFIX = "-jms.xml";
   public static final String JDBC_POSTFIX = "-jdbc.xml";
   public static final int EAR = 0;
   public static final int COMPONENT = 1;
   public static final int EXPLODED_EAR = 2;
   public static final int EXPLODED_COMPONENT = 3;
   public static final int UNKNOWN = 4;
   public static final int SINGLE_FILE_COMPONENT = 5;
   public static final int NOT_INITIALIZED = 6;
   private static String EJB_MODULE_TYPE;
   private static String WEB_MODULE_TYPE;
   private static String CONNECTOR_MODULE_TYPE;
   private static String WEBSERVICE_MODULE_TYPE;
   private static String JDBCPOOL_MODULE_TYPE;
   private static String JMS_MODULE_TYPE;
   private static String APPCLIENT_MODULE_TYPE;

   public static final String getArchivePostfix(String var0) {
      return isValidArchiveName(var0) ? var0.substring(var0.length() - 3, var0.length()) : null;
   }

   public static final String getWLSModulePostfix(String var0) {
      return isValidWLSModuleName(var0) ? var0.substring(var0.length() - 3, var0.length()) : null;
   }

   public static final String getArchiveName(String var0) {
      return isValidArchiveName(var0) ? var0.substring(0, var0.length() - 4) : var0;
   }

   public static final String getWLSModuleName(String var0) {
      return isValidWLSModuleName(var0) ? var0.substring(0, var0.length() - 4) : var0;
   }

   public static final boolean isValidArchiveName(String var0) {
      String var1 = var0.toLowerCase();
      return var1.endsWith(".ear") || isValidArchiveModuleName(var0);
   }

   public static final boolean isValidArchiveModuleName(String var0) {
      String var1 = var0.toLowerCase();
      return var1.endsWith(".jar") || var1.endsWith(".war") || var1.endsWith(".jar") || var1.endsWith(".rar");
   }

   public static final boolean isValidWLSModuleName(String var0) {
      String var1 = var0.toLowerCase();
      return var1.endsWith("-jms.xml") || var1.endsWith("-jdbc.xml");
   }

   public static int getDeploymentCategory(ApplicationMBean var0) throws IOException {
      byte var1 = 4;
      if (var0 != null) {
         String var2 = var0.getPath();
         if (var2 != null) {
            File var3 = new File(var2);
            if (!var3.exists()) {
               throw new FileNotFoundException("No such path: " + var3);
            }

            if (var3.isDirectory()) {
               ApplicationFileManager var4 = ApplicationFileManager.newInstance(var3.getCanonicalPath());
               VirtualJarFile var5 = var4.getVirtualJarFile();
               if (var5.getEntry(APP_DD_PATH) != null) {
                  var1 = 2;
               } else {
                  ComponentMBean[] var6 = var0.getComponents();
                  if (var6 != null && var6.length > 0) {
                     if (isValidArchiveName(var6[0].getURI())) {
                        var1 = 1;
                     } else {
                        File var7 = new File(var0.getPath() + File.separatorChar + var6[0].getURI());
                        if (var7.isDirectory()) {
                           var1 = 3;
                        } else {
                           var1 = 5;
                        }
                     }
                  }
               }
            } else if (var3.toString().endsWith(".ear")) {
               var1 = 0;
            }
         }
      }

      return var1;
   }

   public static void addAppInfToClasspath(StringBuffer var0, File var1) {
      File var2 = new File(var1, APP_INF_CLASS);
      if (var2.exists() && var2.isDirectory()) {
         var0.append(var2.getPath());
         var0.append(File.pathSeparator);
      }

      File var3 = new File(var1, APP_INF_LIB);
      if (var3.exists() && var3.isDirectory()) {
         File[] var4 = var3.listFiles();
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (!var4[var5].isDirectory()) {
                  var0.append(var4[var5].getPath());
                  var0.append(File.pathSeparator);
               }
            }
         }
      }

   }

   public static String getAppScopedLinkPath(String var0, String var1, Context var2) throws NamingException {
      if (var0.indexOf("#") > 0) {
         if (var0.startsWith("../")) {
            var0 = makePathAbsolute(var0, var1);
         }

         return var0;
      } else {
         NamingEnumeration var3 = var2.listBindings("");

         String var5;
         do {
            if (!var3.hasMoreElements()) {
               return findByName(var2, var0);
            }

            Binding var4 = (Binding)var3.nextElement();
            var5 = var4.getName();
         } while(!var5.equals(var1 + "#" + var0));

         return var5;
      }
   }

   public static String makePathAbsolute(String var0, String var1) {
      var1 = normalizeJarName(var1);
      int var2 = 0;
      int var3 = var1.length();

      while(var0.regionMatches(var2, "../", 0, 3)) {
         var2 += 3;
         if (var3 >= 0) {
            var3 = var1.lastIndexOf(47, var3 - 1);
         }
      }

      return var1.substring(0, var3 + 1) + var0.substring(var2);
   }

   public static String normalizeJarName(String var0) {
      return escapeChars(var0, new char[]{'\'', '"', '.'});
   }

   public static String normalizeJNDIName(String var0) {
      return escapeChars(var0, new char[]{'/', '.'});
   }

   public static String escapeChars(String var0, char[] var1) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var0.length(); ++var3) {
            char var4 = var0.charAt(var3);
            if (var4 == '\\' && var3 < var0.length() - 1) {
               StringBuffer var10000 = var2.append(var4);
               ++var3;
               var10000.append(var0.charAt(var3));
            } else {
               for(int var5 = 0; var5 < var1.length; ++var5) {
                  if (var4 == var1[var5]) {
                     var2.append('\\');
                     break;
                  }
               }

               var2.append(var4);
            }
         }

         return var2.toString();
      }
   }

   private static String findByName(Context var0, String var1) throws NamingException {
      NamingEnumeration var2 = var0.listBindings("");

      String var4;
      do {
         if (!var2.hasMoreElements()) {
            return null;
         }

         Binding var3 = (Binding)var2.nextElement();
         var4 = var3.getName();
      } while(!var4.endsWith("#" + var1));

      return var4;
   }

   public static WebLogicMBean createComponentMBean(String var0, String var1, String var2, ApplicationMBean var3) throws ApplicationException {
      String var4 = ApplicationVersionUtils.getApplicationId(var0, var1);
      Object var5 = null;
      if (var2.equals(EJB_MODULE_TYPE)) {
         var5 = var3.createEJBComponent(var4);
      } else if (var2.equals(WEB_MODULE_TYPE)) {
         var5 = var3.createWebAppComponent(var4);
      } else if (var2.equals(CONNECTOR_MODULE_TYPE)) {
         var5 = var3.createConnectorComponent(var4);
      } else if (var2.equals(WEBSERVICE_MODULE_TYPE)) {
         var5 = var3.createWebServiceComponent(var4);
      } else if (var2.equals(JDBCPOOL_MODULE_TYPE)) {
         var5 = var3.createJDBCPoolComponent(var4);
      } else {
         if (!var2.equals(JMS_MODULE_TYPE)) {
            throw new AssertionError("Invalid Module type specified  :  " + var2);
         }

         var5 = var3.createDummyComponent(var4);
      }

      if (var5 == null) {
         Loggable var6 = J2EELogger.logMBeanCreationFailureLoggable(var0, var2, "Could not create MBean", (Throwable)null);
         throw new ApplicationException(var6.getMessage());
      } else {
         return (WebLogicMBean)var5;
      }
   }

   static {
      sep = File.separator;
      APP_DD_PATH = sep + "META-INF" + sep + "application.xml";
      WLAPP_DD_PATH = sep + "META-INF" + sep + "weblogic-application.xml";
      WEB_DD_PATH = sep + "WEB-INF" + sep + "web.xml";
      WEBSERVICE_DD_PATH = "WEB-INF" + File.separator + "web-services.xml";
      EJB_DD_PATH = sep + "META-INF" + sep + "ejb-jar.xml";
      PER_DD_PATH = sep + "META-INF" + sep + "persistence.xml";
      PERCONF_DD_PATH = sep + "META-INF" + sep + "persistence-configuration.xml";
      RAR_DD_PATH = sep + "META-INF" + sep + "ra.xml";
      WLRAR_DD_PATH = sep + "META-INF" + sep + "weblogic-ra.xml";
      CAR_DD_PATH = sep + "META-INF" + sep + "application-client.xml";
      WLCAR_DD_PATH = sep + "META-INF" + sep + "weblogic-application-client.xml";
      APP_INF_LIB = "APP-INF" + sep + "lib";
      APP_INF_CLASS = "APP-INF" + sep + "classes";
      EJB_MODULE_TYPE = "EJBComponent";
      WEB_MODULE_TYPE = "WebAppComponent";
      CONNECTOR_MODULE_TYPE = "ConnectorComponent";
      WEBSERVICE_MODULE_TYPE = "WebServiceComponent";
      JDBCPOOL_MODULE_TYPE = "JDBCPoolComponent";
      JMS_MODULE_TYPE = "JMSComponent";
      APPCLIENT_MODULE_TYPE = "AppClientComponent";
   }
}
