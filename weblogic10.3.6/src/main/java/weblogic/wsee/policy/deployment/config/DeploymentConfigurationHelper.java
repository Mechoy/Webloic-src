package weblogic.wsee.policy.deployment.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.J2eeApplicationObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.DeploymentConfiguration;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.WebLogicJ2eeApplicationObject;
import weblogic.deploy.api.spi.WebLogicDConfigBean;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.config.DeploymentConfigurationImpl;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBeanDConfig;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.wsee.util.StringUtil;

public class DeploymentConfigurationHelper {
   public static final String TUNABLE = "TUNABLE";
   public static final String BINDABLE = "BINDABLE";
   private static MBeanServerConnection domainEditMBeanServerConnection = null;
   private static String OBJECT_NAME_KEY = "com.bea:Name=";
   private static String OBJECT_TYPE_APP = ",Type=AppDeployment";
   private String _userId;
   private String _password;
   private String _host;
   private int _port;
   private String _modulePath;
   private boolean _newPlan;
   private ModuleType _moduleType;
   private String[] _moduleUris;
   private File _planFile;
   private File _configAreaPath;
   private String _appName;
   private SessionHelper _sessionHelper;
   private static SessionHelper tmpSessionHelper = null;
   private static WebLogicDeploymentManager tmpDeploymentManager = null;
   private WebLogicDeploymentManager _deploymentManager;
   private DeploymentConfiguration _deploymentConfiguration;
   private Map _annotationDefs;
   private Map _enumDefs;
   private Map _annotationOverridesByUri;
   private Map _annotationDefsByUri;
   private Map _enumDefsByUri;

   public String toString() {
      return super.toString() + "\n   PlanPath=" + this._modulePath;
   }

   public DeploymentConfigurationHelper(String var1, boolean var2, String var3, int var4, String var5, String var6) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException, ClassNotFoundException, DDBeanCreateException {
      this._moduleUris = new String[0];
      this._sessionHelper = null;
      this._annotationDefs = new HashMap();
      this._enumDefs = new HashMap();
      this._annotationOverridesByUri = new HashMap();
      this._annotationDefsByUri = new HashMap();
      this._enumDefsByUri = new HashMap();

      assert var1 != null;

      this._userId = var5;
      this._password = var6;
      this._host = var3;
      this._port = var4;
      this._modulePath = var1;
      this._configAreaPath = getDefaultConfigDir(var1);
      this._appName = this._configAreaPath.getName();
      this._planFile = getDefaultPlanFile(this._configAreaPath);
      this._newPlan = var2;
      long var7 = System.currentTimeMillis();
      this.initDeploymentConfiguration(var2);
      long var9 = System.currentTimeMillis();
      long var11 = System.currentTimeMillis();
   }

   public DeploymentConfigurationHelper(String var1, boolean var2) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException, ClassNotFoundException, DDBeanCreateException {
      this(var1, var2, (String)null, 0, (String)null, (String)null);
   }

   public DeploymentConfigurationHelper(String var1, String var2, String var3, boolean var4) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException, ClassNotFoundException, DDBeanCreateException {
      this._moduleUris = new String[0];
      this._sessionHelper = null;
      this._annotationDefs = new HashMap();
      this._enumDefs = new HashMap();
      this._annotationOverridesByUri = new HashMap();
      this._annotationDefsByUri = new HashMap();
      this._enumDefsByUri = new HashMap();

      assert var1 != null;

      assert var2 != null;

      this._userId = null;
      this._password = null;
      this._host = null;
      this._port = 0;
      this._modulePath = var2;
      this._configAreaPath = new File(var1);
      this._appName = this._configAreaPath.getName();
      this._newPlan = var4;
      if (var3 != null) {
         this._planFile = new File(var3);
      } else {
         this._planFile = getDefaultPlanFile(this._configAreaPath);
      }

      long var5 = System.currentTimeMillis();
      this.initDeploymentConfiguration(var4);
      long var7 = System.currentTimeMillis();
      long var9 = System.currentTimeMillis();
   }

   public DeploymentConfigurationHelper(String var1, String var2, int var3) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException, ClassNotFoundException, DDBeanCreateException {
      this._moduleUris = new String[0];
      this._sessionHelper = null;
      this._annotationDefs = new HashMap();
      this._enumDefs = new HashMap();
      this._annotationOverridesByUri = new HashMap();
      this._annotationDefsByUri = new HashMap();
      this._enumDefsByUri = new HashMap();

      assert var1 != null;

      this._userId = null;
      this._password = null;
      this._host = var2;
      this._port = var3;
      this._modulePath = var1;
      this._configAreaPath = new File("c:\\temp");
      this._planFile = new File("c:\\temp\\temp.dat");
      long var4 = System.currentTimeMillis();
      this.initDeploymentConfiguration(true);
      long var6 = System.currentTimeMillis();
   }

   public DeploymentConfigurationHelper(String var1, String var2, String var3, boolean var4, String var5, int var6) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException, ClassNotFoundException, DDBeanCreateException {
      this._moduleUris = new String[0];
      this._sessionHelper = null;
      this._annotationDefs = new HashMap();
      this._enumDefs = new HashMap();
      this._annotationOverridesByUri = new HashMap();
      this._annotationDefsByUri = new HashMap();
      this._enumDefsByUri = new HashMap();

      assert var1 != null;

      assert var2 != null;

      this._userId = null;
      this._password = null;
      this._host = var5;
      this._port = var6;
      this._modulePath = var2;
      this._configAreaPath = new File(var1);
      this._appName = this._configAreaPath.getName();
      if (var3 != null) {
         this._planFile = new File(var3);
      } else {
         this._planFile = getDefaultPlanFile(this._configAreaPath);
      }

      this._newPlan = var4;
      long var7 = System.currentTimeMillis();
      this.initDeploymentConfiguration(var4);
      long var9 = System.currentTimeMillis();
      long var11 = System.currentTimeMillis();
   }

   public String getSourcePath() {
      return this._modulePath;
   }

   public boolean isNewPlan() {
      return this._newPlan;
   }

   public String getPlanPath() {
      return this._planFile.getPath();
   }

   public void setPlanPath(String var1) {
      if (var1 != null) {
         this._planFile = new File(var1);
      }

   }

   private static File getDefaultConfigDir(String var0) {
      File var1 = new File(var0);
      File var2 = null;
      if (var1.isDirectory()) {
         var2 = var1.getParentFile();
      } else {
         var2 = var1.getParentFile().getParentFile();
      }

      return var2;
   }

   public WebservicePolicyRefBean getWebservicePolicies(String var1) throws FileNotFoundException, DDBeanCreateException, ConfigurationException {
      WebservicePolicyRefBean var2 = null;
      DeployableObject var3 = this.getDeployment(var1);
      DConfigBeanRoot var4 = this._deploymentConfiguration.getDConfigBeanRoot(var3.getDDBeanRoot());
      DDBeanRoot var5 = getBeanRoot(var3, "WEB-INF/weblogic-webservices-policy.xml", "META-INF/weblogic-webservices-policy.xml");
      WebservicePolicyRefBeanDConfig var6 = (WebservicePolicyRefBeanDConfig)var4.getDConfigBean(var5);
      if (var6 != null) {
         var2 = (WebservicePolicyRefBean)var6.getDescriptorBean();
      }

      return var2;
   }

   public ServiceReferenceDescriptionBean getServiceRefPolicies(String var1, String var2, String var3) throws FileNotFoundException, DDBeanCreateException, ConfigurationException {
      DeployableObject var4 = this.getDeployment(var1);
      if (var4.getType() == ModuleType.WAR) {
         WeblogicWebAppBean var11 = (WeblogicWebAppBean)this.getDescriptorBean(var1);
         ServiceReferenceDescriptionBean[] var12 = var11.getServiceReferenceDescriptions();
         if (var12 == null) {
            return this.createServiceRefBean(var3, var11);
         } else {
            ServiceReferenceDescriptionBean var14 = this.getRefBean(var12, var3);
            return var14 == null ? this.createServiceRefBean(var3, var11) : var14;
         }
      } else {
         WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)this.getDescriptorBean(var1);
         WeblogicEnterpriseBeanBean[] var6 = var5.getWeblogicEnterpriseBeans();
         WeblogicEnterpriseBeanBean var13;
         if (var6 == null) {
            var13 = this.createEJBBean(var2, var5);
            return this.getServiceRefPolicies(var13, var3);
         } else {
            WeblogicEnterpriseBeanBean[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               WeblogicEnterpriseBeanBean var10 = var7[var9];
               if (var10.getEjbName().equals(var2)) {
                  return this.getServiceRefPolicies(var10, var3);
               }
            }

            var13 = this.createEJBBean(var2, var5);
            return this.getServiceRefPolicies(var13, var3);
         }
      }
   }

   private WeblogicEnterpriseBeanBean createEJBBean(String var1, WeblogicEjbJarBean var2) {
      WeblogicEnterpriseBeanBean var3 = var2.createWeblogicEnterpriseBean();
      var3.setEjbName(var1);
      return var3;
   }

   private ServiceReferenceDescriptionBean getServiceRefPolicies(WeblogicEnterpriseBeanBean var1, String var2) {
      ServiceReferenceDescriptionBean[] var3 = var1.getServiceReferenceDescriptions();
      if (var3 == null) {
         return this.createServiceRefBean(var2, var1);
      } else {
         ServiceReferenceDescriptionBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ServiceReferenceDescriptionBean var7 = var4[var6];
            if (var7.getServiceRefName().equals(var2)) {
               return var7;
            }
         }

         return this.createServiceRefBean(var2, var1);
      }
   }

   private ServiceReferenceDescriptionBean createServiceRefBean(String var1, WeblogicWebAppBean var2) {
      ServiceReferenceDescriptionBean var3 = var2.createServiceReferenceDescription();
      var3.setServiceRefName(var1);
      return var3;
   }

   private ServiceReferenceDescriptionBean createServiceRefBean(String var1, WeblogicEnterpriseBeanBean var2) {
      ServiceReferenceDescriptionBean var3 = var2.createServiceReferenceDescription();
      var3.setServiceRefName(var1);
      return var3;
   }

   private ServiceReferenceDescriptionBean getRefBean(ServiceReferenceDescriptionBean[] var1, String var2) {
      if (var1 != null) {
         ServiceReferenceDescriptionBean[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ServiceReferenceDescriptionBean var6 = var3[var5];
            if (var6.getServiceRefName().equals(var2)) {
               return var6;
            }
         }
      }

      return null;
   }

   public DescriptorBean getDescriptorBean(String var1) throws ConfigurationException {
      WebLogicDeployableObject var2 = this.getDeployableObject(var1);
      if (var2 != null) {
         WebLogicDConfigBean var3 = (WebLogicDConfigBean)this.getDeploymentConfiguration().getDConfigBeanRoot(var2.getDDBeanRoot());
         return var3.getDescriptorBean();
      } else {
         return null;
      }
   }

   private DeployableObject getDeployment(String var1) {
      Object var2 = null;
      if (this._moduleType == ModuleType.EAR) {
         J2eeApplicationObject var3 = (J2eeApplicationObject)this._deploymentConfiguration.getDeployableObject();
         var2 = var3.getDeployableObject(var1);
      } else if (this._moduleType == ModuleType.WAR || this._moduleType == ModuleType.EJB) {
         var2 = this.getDeployableObject(var1);
      }

      return (DeployableObject)var2;
   }

   private static DDBeanRoot getBeanRoot(DeployableObject var0, String var1, String var2) throws FileNotFoundException, DDBeanCreateException {
      if (var0 == null) {
         return null;
      } else {
         DDBeanRoot var3 = null;
         if (var0.getType() == ModuleType.WAR) {
            var3 = var0.getDDBeanRoot(var1);
         } else {
            var3 = var0.getDDBeanRoot(var2);
         }

         return var3;
      }
   }

   public WebLogicDeploymentConfiguration getDeploymentConfiguration() {
      if (this._deploymentConfiguration == null) {
         throw new IllegalArgumentException("No deployment configuration available");
      } else {
         return (WebLogicDeploymentConfiguration)this._deploymentConfiguration;
      }
   }

   private static File getDefaultPlanFile(File var0) {
      String var1 = var0.getAbsolutePath() + "/plan/Plan.xml";
      return new File(var1);
   }

   private void initDeploymentConfiguration(final boolean var1) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException {
      String var2 = "N/A";

      try {
         runDeploymentAction(new DeploymentAction() {
            public Object execute() throws Exception {
               DeploymentConfigurationHelper.this._deploymentManager = DeploymentConfigurationHelper.this.getDeploymentManager();
               DeploymentConfigurationHelper.this._sessionHelper = SessionHelper.getInstance(DeploymentConfigurationHelper.this._deploymentManager);
               File var1x = null;
               if (!var1) {
                  var1x = DeploymentConfigurationHelper.this._planFile;
               }

               DeploymentConfigurationHelper.this._sessionHelper.enableLibraryMerge();
               DeploymentConfigurationHelper.this._sessionHelper.initializeConfiguration(new File(DeploymentConfigurationHelper.this._modulePath), var1x, DeploymentConfigurationHelper.this._configAreaPath);
               return null;
            }
         });
      } catch (Exception var4) {
         throw new RuntimeException(var2, var4);
      }

      this._deploymentConfiguration = this._sessionHelper.getConfiguration();
      this._moduleType = this._deploymentConfiguration.getDeployableObject().getType();
      if (var1) {
         ((DeploymentConfigurationImpl)this._deploymentConfiguration).getPlan().setApplicationName(this._appName);
      } else {
         this._appName = ((DeploymentConfigurationImpl)this._deploymentConfiguration).getPlan().getApplicationName();
      }

      if (this._moduleType == ModuleType.EAR) {
         this._moduleUris = ((J2eeApplicationObject)this._deploymentConfiguration.getDeployableObject()).getModuleUris();
      }

   }

   public WebLogicDeployableObject getDeployableObject(String var1) {
      DeployableObject var2 = this.getDeploymentConfiguration().getDeployableObject();
      if (var2 != null) {
         if (var2 instanceof WebLogicJ2eeApplicationObject && var1 != null) {
            WebLogicJ2eeApplicationObject var3 = (WebLogicJ2eeApplicationObject)var2;
            return (WebLogicDeployableObject)var3.getDeployableObject(var1);
         } else {
            return (WebLogicDeployableObject)var2;
         }
      } else {
         return null;
      }
   }

   public WebLogicDeploymentManager getDeploymentManager() throws DeploymentManagerCreationException {
      return this._userId != null ? SessionHelper.getDeploymentManager(this._host, Integer.toString(this._port), this._userId, this._password) : SessionHelper.getDeploymentManager(this._host, this._port == 0 ? null : Integer.toString(this._port));
   }

   public static Object runDeploymentAction(DeploymentAction var0) throws Exception {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();

      Object var3;
      try {
         GenericClassLoader var2 = AugmentableClassLoaderManager.getAugmentableSystemClassLoader();
         Thread.currentThread().setContextClassLoader(var2);
         var3 = var0.execute();
      } finally {
         Thread.currentThread().setContextClassLoader(var1);
      }

      return var3;
   }

   static DeploymentConfigurationHelper createDeploymentConfigurationHelper(AppDeploymentMBean var0) throws ManagementException {
      DeploymentConfigurationHelper var1 = null;
      String var2 = var0.getAbsoluteSourcePath();
      String var3 = var0.getAbsoluteInstallDir();
      String var4 = var0.getAbsolutePlanPath();
      File var5 = new File(var2);
      if (StringUtil.isEmpty(var3)) {
         var3 = var5.getParent();
         if (var3 == null) {
            String var6 = var5.getAbsolutePath();
            if (var6 != null) {
               File var7 = new File(var6);
               var3 = var7.getParent();
            }
         }
      }

      try {
         if (StringUtil.isEmpty(var4)) {
            SessionHelper var15 = getSessionHelper();
            var15.setApplication(var5);
            String var16 = var15.getNewPlanName();
            File[] var8 = var15.findPlans();
            if (var8 != null && var8.length > 0) {
               var4 = var8[0].getAbsolutePath() + File.pathSeparator + var16;
            }

            releaseSessionHelper();
            var1 = new DeploymentConfigurationHelper(var3, var2, var4, true, (String)null, 0);
         } else {
            var1 = new DeploymentConfigurationHelper(var3, var2, var4, false, (String)null, 0);
         }
      } catch (DeploymentManagerCreationException var9) {
      } catch (ConfigurationException var10) {
      } catch (IOException var11) {
      } catch (InvalidModuleException var12) {
      } catch (ClassNotFoundException var13) {
      } catch (DDBeanCreateException var14) {
      }

      return var1;
   }

   public static SessionHelper getSessionHelper() throws ManagementException {
      releaseSessionHelper();
      tmpSessionHelper = SessionHelper.getInstance(getTmpDeploymentManager());
      return tmpSessionHelper;
   }

   public static void releaseSessionHelper() {
      if (tmpSessionHelper != null) {
         tmpSessionHelper.close();
         tmpSessionHelper = null;
      }

   }

   public static WebLogicDeploymentManager getTmpDeploymentManager() throws ManagementException {
      if (tmpDeploymentManager == null) {
         try {
            DomainMBean var0 = MBeanUtils.getDomainMBean();
            tmpDeploymentManager = SessionHelper.getDeploymentManager((String)null, (String)null);
         } catch (DeploymentManagerCreationException var1) {
            throw new ManagementException(var1);
         } catch (IOException var2) {
            throw new ManagementException(var2);
         }
      }

      return tmpDeploymentManager;
   }

   private static MBeanServerConnection lookupMBeanServerConnection(String var0) throws IOException {
      short var1 = 7001;
      String var2 = "localhost";
      String var3 = "wlx";
      JMXServiceURL var4 = new JMXServiceURL(var3, var2, var1, "/jndi/" + var0);
      Hashtable var5 = new Hashtable();
      var5.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      JMXConnector var6 = JMXConnectorFactory.connect(var4, var5);
      return var6.getMBeanServerConnection();
   }

   public static MBeanServerConnection getDomainEditMBeanServerConnection() throws IOException {
      if (domainEditMBeanServerConnection == null) {
         domainEditMBeanServerConnection = lookupMBeanServerConnection("weblogic.management.mbeanservers.edit");
      }

      return domainEditMBeanServerConnection;
   }

   private static Object getMBean(ObjectName var0) throws ManagementException {
      try {
         return MBeanServerInvocationHandler.newProxyInstance(getDomainEditMBeanServerConnection(), var0);
      } catch (IOException var2) {
         throw new ManagementException(var2);
      } catch (Throwable var3) {
         throw new ManagementException(var3);
      }
   }

   public static AppDeploymentMBean getAppDeploymentMBean(String var0) throws ManagementException {
      try {
         ObjectName var1 = new ObjectName(OBJECT_NAME_KEY + var0 + OBJECT_TYPE_APP);
         Object var2 = getMBean(var1);
         return (AppDeploymentMBean)var2;
      } catch (Throwable var3) {
         throw new ManagementException(var3);
      }
   }

   public void save() throws IOException, ConfigurationException {
      FileOutputStream var1 = null;

      try {
         var1 = new FileOutputStream(this._planFile, false);
         this._deploymentConfiguration.save(var1);
         this._newPlan = false;
      } catch (IOException var10) {
         throw var10;
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var9) {
         }

      }

   }

   public void release() {
      this._modulePath = null;
      this._planFile = null;
      this._moduleUris = null;
      this._deploymentConfiguration = null;
      this._configAreaPath = null;
      if (this._sessionHelper != null) {
         this._sessionHelper.close();
         this._sessionHelper = null;
      }

      if (this._deploymentManager != null) {
         this._deploymentManager.release();
         this._deploymentManager = null;
      }

      if (this._annotationDefs != null) {
         this._annotationDefs.clear();
         this._annotationDefs = null;
      }

      if (this._annotationDefsByUri != null) {
         this._annotationDefsByUri.clear();
         this._annotationDefsByUri = null;
      }

      if (this._annotationOverridesByUri != null) {
         this._annotationOverridesByUri.clear();
         this._annotationOverridesByUri = null;
      }

      if (this._enumDefs != null) {
         this._enumDefs.clear();
         this._enumDefs = null;
      }

      if (this._enumDefsByUri != null) {
         this._enumDefsByUri.clear();
         this._enumDefsByUri = null;
      }

   }

   public interface DeploymentAction {
      Object execute() throws Exception;
   }
}
