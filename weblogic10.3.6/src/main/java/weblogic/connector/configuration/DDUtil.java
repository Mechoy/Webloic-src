package weblogic.connector.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.connector.common.Debug;
import weblogic.connector.exception.RAConfigurationException;
import weblogic.connector.exception.WLRAConfigurationException;
import weblogic.connector.external.AuthMechInfo;
import weblogic.connector.external.ConfigPropInfo;
import weblogic.connector.external.ConnectorUtils;
import weblogic.connector.external.LoggingInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.PoolInfo;
import weblogic.connector.external.RAInfo;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.AdminObjectBean;
import weblogic.j2ee.descriptor.ConnectionDefinitionBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.OutboundResourceAdapterBean;
import weblogic.j2ee.descriptor.ResourceAdapterBean;
import weblogic.j2ee.descriptor.wl.AdminObjectGroupBean;
import weblogic.j2ee.descriptor.wl.AdminObjectInstanceBean;
import weblogic.j2ee.descriptor.wl.AdminObjectsBean;
import weblogic.j2ee.descriptor.wl.ConnectionDefinitionPropertiesBean;
import weblogic.j2ee.descriptor.wl.ConnectionInstanceBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.PoolParamsBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorExtensionBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class DDUtil {
   private static final String FILE_URL_PREFIX = "file:/";
   private static final String CLASS_NAME = "weblogic.connector.configuration.DDUtil";

   public static RAInfo getRAInfo(VirtualJarFile var0, File var1, String var2, AppDeploymentMBean var3, DeploymentPlanBean var4) throws RAConfigurationException, WLRAConfigurationException {
      RAInfo var12;
      try {
         Debug.enter("weblogic.connector.configuration.DDUtil", "getRAInfo()");
         Debug.parsing("Validating the RAR and the alternate descriptor");
         DDValidator.validateRARAndAltDD(var0, var1);
         Debug.println("weblogic.connector.configuration.DDUtil", ".getRAInfo() Get the config directory");
         File var6 = null;
         String var5;
         if (var3 == null) {
            var5 = var2;
         } else {
            var5 = var3.getApplicationIdentifier();
         }

         if (var3 != null && var3.getPlanDir() != null) {
            var6 = new File(var3.getLocalPlanDir());
         }

         Debug.parsing("Constructing the ConnectorDescriptor");
         ClassLoader var7 = Thread.currentThread().getContextClassLoader();
         ConnectorBean var8 = null;
         WeblogicConnectorBean var9 = null;

         try {
            Thread.currentThread().setContextClassLoader(DDUtil.class.getClassLoader());
            ConnectorDescriptor var10 = new ConnectorDescriptor(var1, var0, var6, var4, getNameForMerging(var0));
            Debug.println("weblogic.connector.configuration.DDUtil", ".getRAInfo() Get the connector bean");
            var8 = var10.getConnectorBean();
            Debug.println("weblogic.connector.configuration.DDUtil", ".getRAInfo() Get the weblogic connector bean");
            var9 = var10.getWeblogicConnectorBean();
         } finally {
            Thread.currentThread().setContextClassLoader(var7);
         }

         if (var8 == null) {
            String var22;
            if (!(var9 instanceof WeblogicConnectorExtensionBean)) {
               var22 = Debug.getExceptionNeedsRAXML();
               throw new WLRAConfigurationException(var22);
            }

            if (!DDValidator.isLinkRef((WeblogicConnectorExtensionBean)var9)) {
               var22 = Debug.getExceptionMustBeLinkRef();
               throw new WLRAConfigurationException(var22);
            }
         }

         if (var9 == null) {
            var9 = createDefaultWLConnBean(var8, getDefaultBaseJndiName(var5, var2));
         }

         Debug.println("weblogic.connector.configuration.DDUtil", ".getRAInfo()Get the url");
         URL var21 = getRAURL(var0.getName());
         RAInfo var11 = ConnectorUtils.raInfo.createRAInfo(var8, var9, var21, var2);
         var12 = var11;
      } finally {
         Debug.exit("weblogic.connector.configuration.DDUtil", "getRAInfo()");
      }

      return var12;
   }

   public static void validateRAInfo(RAInfo var0) throws RAConfigurationException {
      Debug.enter("weblogic.connector.configuration.DDUtil", "validateRAInfo(...)");

      try {
         boolean var1 = false;
         String var2 = "";
         List var3 = var0.getOutboundInfos();
         if (var3 != null && var3.size() > 0) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               OutboundInfo var5 = (OutboundInfo)var4.next();
               if (var5 != null && var5.getInitialCapacity() > var5.getMaxCapacity()) {
                  var1 = true;
                  var2 = var2 + Debug.getExceptionMaxCapacityLessThanInitialCapacity(var5.getJndiName()) + "\n";
               }
            }
         }

         if (var1) {
            throw new RAConfigurationException(var2);
         }
      } finally {
         Debug.exit("weblogic.connector.configuration.DDUtil", "validateRAInfo(...)");
      }

   }

   private static URL getRAURL(String var0) throws RAConfigurationException {
      URL var2;
      try {
         Debug.enter("weblogic.connector.configuration.DDUtil", "getRAURL()");
         String var1 = (new String("file:/")).concat(var0);
         var2 = new URL(var1);
      } catch (MalformedURLException var7) {
         throw new RAConfigurationException(var7);
      } finally {
         Debug.exit("weblogic.connector.configuration.DDUtil", "getRAURL()");
      }

      return var2;
   }

   private static WeblogicConnectorBean createDefaultWLConnBean(ConnectorBean var0, String var1) {
      Debug.parsing("Resource Adapter being deployed does not have weblogic-ra.xml -- a default is being created.");
      WeblogicConnectorBean var2 = (WeblogicConnectorBean)(new DescriptorManager()).createDescriptorRoot(WeblogicConnectorBean.class).getRootBean();
      var2.setNativeLibdir("/temp/nativelibs/");
      Debug.parsing("Setting adapter jndi name to '" + getDefaultJndiNameForRABean(var1) + "'");
      var2.setJNDIName(getDefaultJndiNameForRABean(var1));
      setDefaultOutboundRAs(var0, var2, var1);
      setDefaultAdminObjects(var0, var2, var1);
      return var2;
   }

   private static void setDefaultOutboundRAs(ConnectorBean var0, WeblogicConnectorBean var1, String var2) {
      ResourceAdapterBean var3 = var0.getResourceAdapter();
      OutboundResourceAdapterBean var4 = var3.getOutboundResourceAdapter();
      if (var4 == null) {
         if (var1.getOutboundResourceAdapter() != null) {
            var1.destroyOutboundResourceAdapter(var1.getOutboundResourceAdapter());
         }
      } else {
         ConnectionDefinitionBean[] var5 = var4.getConnectionDefinitions();
         int var6 = var5.length;
         if (var6 > 0) {
            weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean var7 = var1.createOutboundResourceAdapter();
            setDefaultMaxCapacity(var7);

            for(int var8 = 0; var8 < var5.length; ++var8) {
               weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var9 = var7.createConnectionDefinitionGroup();
               String var10 = var5[var8].getConnectionFactoryInterface();
               if (Debug.isParsingEnabled()) {
                  Debug.parsing("Setting ConnectionFactoryInterface of conn defn group[" + var8 + "] to '" + var10 + "'");
               }

               var9.setConnectionFactoryInterface(var10);
               ConnectionInstanceBean var11 = var9.createConnectionInstance();
               String var12 = getDefaultJndiName(var2, var10.replace('.', '_'));
               if (Debug.isParsingEnabled()) {
                  Debug.parsing("Setting JNDI name of conn instance[" + var8 + "] to '" + var12 + "'");
               }

               var11.setJNDIName(var12);
            }
         }
      }

   }

   private static void setDefaultAdminObjects(ConnectorBean var0, WeblogicConnectorBean var1, String var2) {
      ResourceAdapterBean var3 = var0.getResourceAdapter();
      AdminObjectBean[] var4 = var3.getAdminObjects();
      if (var4.length > 0) {
         AdminObjectsBean var5 = var1.createAdminObjects();

         for(int var6 = 0; var6 < var4.length; ++var6) {
            String var7 = var4[var6].getAdminObjectInterface();
            AdminObjectGroupBean var8 = var5.createAdminObjectGroup();
            if (Debug.isParsingEnabled()) {
               Debug.parsing("Setting AdminObjectInterface of admin obj group[" + var6 + "] to '" + var7 + "'");
            }

            var8.setAdminObjectInterface(var7);
            AdminObjectInstanceBean var9 = var8.createAdminObjectInstance();
            String var10 = getDefaultJndiName(var2, var7.replace('.', '_'));
            if (Debug.isParsingEnabled()) {
               Debug.parsing("Setting JNDI name of Admin Obj instance[" + var6 + "] to '" + var10 + "'");
            }

            var9.setJNDIName(var10);
         }
      }

   }

   private static void setDefaultMaxCapacity(weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean var0) {
      ConnectionDefinitionPropertiesBean var1 = var0.createDefaultConnectionProperties();
      PoolParamsBean var2 = var1.createPoolParams();
      var2.setMaxCapacity(Integer.MAX_VALUE);
   }

   private static String getNameForMerging(VirtualJarFile var0) {
      String var1 = var0.getName();
      String var2 = var0.getName();
      String var3 = File.separator;
      if (var3.equals("\\")) {
         var3 = "\\\\";
      }

      String[] var4 = var2.split(var3);
      if (var4 != null) {
         var1 = var4[var4.length - 1];
      }

      return var1;
   }

   private static String getAppName(String var0) {
      String var1 = null;
      var0 = ApplicationVersionUtils.replaceDelimiter(var0, '_');
      if (var0 != null && var0.trim().length() != 0) {
         if (!var0.endsWith(".rar") && !var0.endsWith(".ear")) {
            var1 = var0;
         } else {
            var1 = var0.substring(0, var0.length() - 4);
         }
      } else {
         Debug.throwAssertionError("AppId is null or empty : " + var0);
      }

      return var1;
   }

   public static String getModuleName(String var0) {
      if (var0 != null && var0.trim().length() != 0) {
         if (var0.endsWith(".rar")) {
            var0 = var0.substring(0, var0.length() - 4);
         }
      } else {
         Debug.throwAssertionError("Module name is null or empty : " + var0);
      }

      return var0;
   }

   private static String getDefaultBaseJndiName(String var0, String var1) {
      String var2 = null;
      String var3 = getAppName(var0);
      String var4 = getModuleName(var1);
      if (var3.equals(var4)) {
         var2 = "eis/" + var3;
      } else {
         var2 = "eis/" + var3 + "_" + var4;
      }

      return var2;
   }

   private static String getDefaultJndiNameForRABean(String var0) {
      return var0 + "_RABean";
   }

   private static String getDefaultJndiName(String var0, String var1) {
      return var0 + "_" + var1;
   }

   public static void main(String[] var0) {
      boolean var1 = false;
      if (var0.length >= 1) {
         if (var0[0].equalsIgnoreCase("testLinkRefs") && var0.length == 3) {
            var1 = true;
            testLinkRefs(var0[1], var0[2]);
         } else if (var0[0].equalsIgnoreCase("testNoWLRaXML") && var0.length == 2) {
            var1 = true;
            testNoWLraXML(var0[1]);
         } else if (var0[0].equalsIgnoreCase("testConsoleUtility") && var0.length == 4) {
            var1 = true;
            testConsoleUtility(var0[1], var0[2], var0[3]);
         } else if (var0[0].equalsIgnoreCase("testDefaultBaseJndiName") && var0.length == 4) {
            var1 = true;
            System.out.println("RABean JNDI Name : " + getDefaultJndiNameForRABean(getDefaultBaseJndiName(var0[1], var0[2])));
            System.out.println("Other JNDI Name : " + getDefaultJndiName(getDefaultBaseJndiName(var0[1], var0[2]), var0[3]));
         }
      }

      if (!var1) {
         printMainUsage();
      }

   }

   private static void testConsoleUtility(String var0, String var1, String var2) {
      VirtualJarFile var3 = null;
      File var4 = null;

      try {
         var4 = new File(var0);
         var3 = VirtualJarFactory.createVirtualJar(var4);
         RAInfo var5 = getRAInfo(var3, (File)null, "connector", (AppDeploymentMBean)null, (DeploymentPlanBean)null);
         System.out.println("raInfo : " + var5);
         Hashtable var6 = var5.getAdminObjectGroupProperties(var1);
         Enumeration var7 = var6.elements();
         System.out.println("--------------\nAdmin Group Config Properties\n--------------");
         System.out.println("HT Size : " + var6.size());

         while(var7.hasMoreElements()) {
            ConfigPropInfo var8 = (ConfigPropInfo)var7.nextElement();
            System.out.println("--------------");
            System.out.println("Description : " + var8.getDescription());
            System.out.println("Name : " + var8.getName());
            System.out.println("Type : " + var8.getType());
            System.out.println("Value : " + var8.getValue());
            System.out.println("--------------");
         }

         var6 = var5.getConnectionGroupConfigProperties(var2);
         Iterator var16 = var6.values().iterator();
         System.out.println("--------------Connection Group Config Properties\n--------------");
         System.out.println("HT Size : " + var6.size());

         while(var16.hasNext()) {
            ConfigPropInfo var9 = (ConfigPropInfo)var16.next();
            System.out.println("--------------");
            System.out.println("Description : " + var9.getDescription());
            System.out.println("Name : " + var9.getName());
            System.out.println("Type : " + var9.getType());
            System.out.println("Value : " + var9.getValue());
            System.out.println("--------------");
         }

         String var17 = var5.getConnectionGroupTransactionSupport(var2);
         System.out.println("TransactionSupport = " + var17);
         AuthMechInfo[] var10 = var5.getConnectionGroupAuthenticationMechanisms(var2);
         System.out.println("--------------Connection Group Authentication Mechanisms\n--------------");

         for(int var11 = 0; var11 < var10.length; ++var11) {
            System.out.println("--------------");
            System.out.println("Description : " + var10[var11].getDescription());
            System.out.println("Type : " + var10[var11].getType());
            System.out.println("Interface : " + var10[var11].getCredentialInterface());
            System.out.println("--------------");
         }

         String var18 = var5.getConnectionGroupResAuth(var2);
         System.out.println("ResAuth = " + var18);
         boolean var12 = var5.isConnectionGroupReauthenticationSupport(var2);
         System.out.println("Reauthentication Support = " + var12);
         LoggingInfo var13 = var5.getConnectionGroupLoggingProperties(var2);
         System.out.println("--------------Connection Group Logging Properties\n--------------");
         System.out.println("--------------");
         System.out.println("logFilename:           " + var13.getLogFilename());
         System.out.println("loggingEnabled:        " + var13.isLoggingEnabled());
         System.out.println("rotationType:          " + var13.getRotationType());
         System.out.println("rotationTime:          " + var13.getRotationTime());
         System.out.println("numberOfFilesLimited:  " + var13.isNumberOfFilesLimited());
         System.out.println("fileCount:             " + var13.getFileCount());
         System.out.println("fileSizeLimit:         " + var13.getFileSizeLimit());
         System.out.println("fileTimeSpan:          " + var13.getFileTimeSpan());
         System.out.println("rotateLogOnStartup:    " + var13.isRotateLogOnStartup());
         System.out.println("logFileRotationDir:    " + var13.getLogFileRotationDir());
         System.out.println("--------------");
         PoolInfo var14 = var5.getConnectionGroupPoolProperties(var2);
         System.out.println("--------------Connection Group Pool Properties\n--------------");
         System.out.println("--------------");
         System.out.println("initialCapacity:                 " + var14.getInitialCapacity());
         System.out.println("maxCapacity:                     " + var14.getMaxCapacity());
         System.out.println("capacityIncrement:               " + var14.getCapacityIncrement());
         System.out.println("shrinkingEnabled:                " + var14.isShrinkingEnabled());
         System.out.println("shrinkFrequencySeconds:          " + var14.getShrinkFrequencySeconds());
         System.out.println("highestNumWaiters:               " + var14.getHighestNumWaiters());
         System.out.println("highestNumUnavailable:           " + var14.getHighestNumUnavailable());
         System.out.println("connectionCreationRetryFrequencySeconds:  " + var14.getConnectionCreationRetryFrequencySeconds());
         System.out.println("connectionReserveTimeoutSeconds: " + var14.getConnectionReserveTimeoutSeconds());
         System.out.println("testFrequencySeconds:            " + var14.getTestFrequencySeconds());
         System.out.println("testConnectionsOnCreate:         " + var14.isTestConnectionsOnCreate());
         System.out.println("testConnectionsOnRelease:        " + var14.isTestConnectionsOnRelease());
         System.out.println("testConnectionsOnReserve:        " + var14.isTestConnectionsOnReserve());
         System.out.println("profileHarvestFrequencySeconds:  " + var14.getProfileHarvestFrequencySeconds());
         System.out.println("ignoreInUseConnectionsEnabled:   " + var14.isIgnoreInUseConnectionsEnabled());
         System.out.println("matchConnectionsSupported:       " + var14.isMatchConnectionsSupported());
         System.out.println("--------------");
      } catch (Exception var15) {
         System.out.println("Caught exception : " + var15);
         var15.printStackTrace();
      }

   }

   private static void printMainUsage() {
      System.out.println("Improper call.  Usage --->\n   java weblogic.connector.configuration.DDUtil <nameOfTest> <args> \n\n   where: nameOfTest & args = \n          testLinkRefs baseJarPath linkRefJarPath -- prints out effective properties for link-ref deployment \n          testNoWLraXML rarPath                   -- prints out generated default weblogic-ra.xml for rar without one\n          testConsoleUtility rarPath Interface    -- tests the console utility \n");
   }

   public static void testLinkRefs(String var0, String var1) {
      VirtualJarFile var2 = null;
      VirtualJarFile var3 = null;

      try {
         File var4 = new File(var0);
         var2 = VirtualJarFactory.createVirtualJar(var4);
         File var5 = new File(var1);
         var3 = VirtualJarFactory.createVirtualJar(var5);
         RAInfo var6 = getRAInfo(var2, (File)null, "baseConnector", (AppDeploymentMBean)null, (DeploymentPlanBean)null);
         System.out.println("baseRAInfo : " + var6);
         RAInfo var7 = getRAInfo(var3, (File)null, "linkrefConnector", (AppDeploymentMBean)null, (DeploymentPlanBean)null);
         var7.setBaseRA(var6);
         List var8 = var7.getOutboundInfos();
         OutboundInfo var9 = (OutboundInfo)var8.get(0);
         Hashtable var10 = var9.getMCFProps();
         ConfigPropInfo var11 = null;
         if (var10 != null) {
            Iterator var12 = var10.values().iterator();

            while(var12.hasNext()) {
               var11 = (ConfigPropInfo)var12.next();
               System.out.println("---------------------");
               System.out.println("Name : " + var11.getName());
               System.out.println("Type : " + var11.getType());
               System.out.println("Value : " + var11.getValue());
               System.out.println("---------------------");
            }
         }

         System.out.println("linkrefRAInfo : " + var6);
      } catch (Exception var13) {
         System.out.println("Caught exception : " + var13);
         var13.printStackTrace();
      }

   }

   public static void testNoWLraXML(String var0) {
      VirtualJarFile var1 = null;

      try {
         File var2 = new File(var0);
         var1 = VirtualJarFactory.createVirtualJar(var2);
         RAInfo var3 = getRAInfo(var1, (File)null, getModuleName(var2.getName()), (AppDeploymentMBean)null, (DeploymentPlanBean)null);
         System.out.println("baseRAInfo : " + var3);
         WeblogicConnectorBean var4 = var3.getWeblogicConnectorBean();
         System.out.println("WeblogicConnectorBean :");
         (new DescriptorManager()).writeDescriptorAsXML(((DescriptorBean)var4).getDescriptor(), System.out);
      } catch (Exception var5) {
         System.out.println("Caught exception : " + var5);
         var5.printStackTrace();
      }

   }
}
