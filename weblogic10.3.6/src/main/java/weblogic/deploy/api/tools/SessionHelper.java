package weblogic.deploy.api.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import weblogic.deploy.api.internal.Closable;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.internal.utils.LibrarySpec;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.model.WebLogicJ2eeApplicationObject;
import weblogic.deploy.api.model.internal.WebLogicDeployableObjectFactoryImpl;
import weblogic.deploy.api.model.sca.ScaApplicationObject;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDConfigBeanRoot;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.deploy.api.spi.factories.WebLogicDeploymentFactory;
import weblogic.deploy.api.spi.factories.internal.DeploymentFactoryImpl;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.jms.module.DefaultingHelper;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.utils.FileUtils;

public class SessionHelper {
   private static final Class DEF_FACTORY_CLASS = DeploymentFactoryImpl.class;
   private boolean debug = Debug.isDebug("config");
   protected WebLogicDeploymentManager dm;
   protected WebLogicDeployableObject dObject = null;
   protected WebLogicDeploymentConfiguration dConfig = null;
   private ModuleType moduleType = null;
   private boolean explicitApplication = false;
   private boolean explicitPlan = false;
   private boolean explicitRoot = false;
   private File application = null;
   private String lightWeightAppName = null;
   private File plan = null;
   private File root = null;
   private File plandir = null;
   private boolean fullInit = true;
   private static final String APP_DIR = "app";
   private static final String PLAN_DIR = "plan";
   private boolean updatePlanVersion = false;
   private DeploymentPlanBean planBean = null;
   private List libs = null;

   private boolean isExplicitApplication() {
      return this.explicitApplication;
   }

   private void setExplicitApplication(boolean var1) {
      this.explicitApplication = var1;
   }

   private boolean isExplicitPlan() {
      return this.explicitPlan;
   }

   private void setExplicitPlan(boolean var1) {
      this.explicitPlan = var1;
   }

   private boolean isExplicitRoot() {
      return this.explicitRoot;
   }

   private void setExplicitRoot(boolean var1) {
      this.explicitRoot = var1;
   }

   public void close() {
      this.close(this.dConfig);
      this.close(this.dObject);
      this.dConfig = null;
      this.dObject = null;
   }

   private void close(Closable var1) {
      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (Throwable var3) {
      }

   }

   public File getApplication() {
      return this.application;
   }

   public void setApplication(File var1) {
      this.setApplication(var1, true);
      this.setImplicitRoot(this.getApplication(), "app");
      this.setImplicitPlan();
   }

   protected void setLightWeightAppName(String var1) {
      this.lightWeightAppName = var1;
   }

   private void setImplicitRoot(File var1, String var2) {
      if (!this.isExplicitRoot()) {
         if (var1 != null) {
            if (var2.equals(var1.getAbsoluteFile().getParentFile().getName())) {
               this.setApplicationRoot(var1.getAbsoluteFile().getParentFile().getParentFile(), false);
            }

         }
      }
   }

   private void setImplicitPlan() {
      if (!this.isExplicitPlan()) {
         File var1 = this.getApplicationRoot();
         if (var1 != null) {
            File var2 = new File(var1, "plan");
            if (var2.exists() && var2.isDirectory()) {
               String[] var3 = var2.list();
               String var4 = this.getOnlyXMLFile(var3);
               if (var4 != null) {
                  this.setPlan(new File(var2, var4), false);
               }
            }

         }
      }
   }

   private String getOnlyXMLFile(String[] var1) {
      String var2 = null;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         if (var4.endsWith(".xml")) {
            if (var2 != null) {
               return null;
            }

            var2 = var4;
         }
      }

      return var2;
   }

   private void setImplicitApplication() {
      if (!this.isExplicitApplication()) {
         File var1 = this.getApplicationRoot();
         if (var1 != null) {
            File var2 = new File(var1, "app");
            if (var2.exists() && var2.isDirectory()) {
               String[] var3 = var2.list();
               if (var3.length == 1) {
                  this.setApplication(new File(var2, var3[0]), false);
               }
            }

         }
      }
   }

   private void setApplication(File var1, boolean var2) {
      this.application = var1;
      this.setExplicitApplication(var2);
   }

   public File getPlan() {
      return this.plan;
   }

   public void setPlan(File var1) {
      this.setPlan(var1, true);
      this.setImplicitRoot(var1, "plan");
   }

   private void setPlan(File var1, boolean var2) {
      if (var1 != null && var1.exists() && var1.isDirectory()) {
         throw new IllegalArgumentException(SPIDeployerLogger.planIsDir(var1.getPath()));
      } else {
         this.plan = var1;
         this.setExplicitPlan(var2);
      }
   }

   public File getPlandir() {
      return this.plandir;
   }

   public void setPlandir(File var1) {
      this.plandir = var1;
   }

   public void setPlanBean(DeploymentPlanBean var1) {
      this.planBean = var1;
   }

   public File getApplicationRoot() {
      return this.root;
   }

   public void setApplicationRoot(File var1) {
      this.setApplicationRoot(var1, true);
      this.setImplicitApplication();
      this.setImplicitPlan();
   }

   private void setApplicationRoot(File var1, boolean var2) {
      if (var1 != null && var1.exists() && !var1.isDirectory()) {
         throw new IllegalArgumentException(SPIDeployerLogger.rootIsFile(var1.getPath()));
      } else {
         this.root = var1;
         this.setExplicitRoot(var2);
      }
   }

   protected String[] getChangedDescriptors() {
      if (this.planBean == null) {
         return new String[0];
      } else {
         ArrayList var1 = new ArrayList();
         ModuleOverrideBean[] var2 = this.planBean.getModuleOverrides();
         if (var2 == null) {
            return new String[0];
         } else {
            String var3 = null;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (this.planBean.rootModule(var2[var4].getModuleName())) {
                  var3 = var2[var4].getModuleName();
                  break;
               }
            }

            for(int var8 = 0; var8 < var2.length; ++var8) {
               boolean var5 = ModuleType.EAR.toString().equals(var2[var8].getModuleType());
               boolean var10 = var2[var8].getModuleName().equals(var3);
               ModuleDescriptorBean[] var6 = var2[var8].getModuleDescriptors();
               if (var6 != null) {
                  for(int var9 = 0; var9 < var6.length; ++var9) {
                     String var7 = var6[var9].getUri();
                     if (!var10) {
                        var7 = var2[var8].getModuleName() + "/" + var7;
                     }

                     if (var6[var9].isChanged()) {
                        var1.add(var7);
                     }
                  }
               }
            }

            return (String[])((String[])var1.toArray(new String[var1.size()]));
         }
      }
   }

   public void setDebug() {
      this.debug = true;
   }

   public boolean isFullInit() {
      return this.fullInit;
   }

   public void setFullInit(boolean var1) {
      this.fullInit = var1;
   }

   public boolean isUpdatePlanVersion() {
      return this.updatePlanVersion;
   }

   public void setUpdatePlanVersion(boolean var1) {
      this.updatePlanVersion = var1;
      if (var1 && this.getConfiguration() != null) {
         this.bumpVersion();
      }

   }

   protected SessionHelper(WebLogicDeploymentManager var1) {
      this.dm = var1;
   }

   public static SessionHelper getInstance(WebLogicDeploymentManager var0) {
      return new SessionHelper(var0);
   }

   public static WebLogicDeploymentManager getDisconnectedDeploymentManager() throws DeploymentManagerCreationException {
      WebLogicDeploymentFactory var0 = (WebLogicDeploymentFactory)registerDefaultFactory();
      return (WebLogicDeploymentManager)var0.getDisconnectedDeploymentManager("deployer:WebLogic");
   }

   public static WebLogicDeploymentManager getDeploymentManager(String var0, String var1, String var2, String var3, String var4) throws DeploymentManagerCreationException {
      WebLogicDeploymentFactory var5 = (WebLogicDeploymentFactory)registerDefaultFactory();
      return (WebLogicDeploymentManager)var5.getDeploymentManager(var5.createUri(var0, "deployer:WebLogic", var1, var2), var3, var4);
   }

   public static WebLogicDeploymentManager getDeploymentManager(String var0, String var1, String var2, String var3) throws DeploymentManagerCreationException {
      return getDeploymentManager("t3", var0, var1, var2, var3);
   }

   public static WebLogicDeploymentManager getRemoteDeploymentManager(String var0, String var1, String var2, String var3) throws DeploymentManagerCreationException {
      return getRemoteDeploymentManager("t3", var0, var1, var2, var3);
   }

   public static WebLogicDeploymentManager getRemoteDeploymentManager(String var0, String var1, String var2, String var3, String var4) throws DeploymentManagerCreationException {
      WebLogicDeploymentFactory var5 = (WebLogicDeploymentFactory)registerDefaultFactory();
      return (WebLogicDeploymentManager)var5.getDeploymentManager(var5.createUri(var0, "remote:deployer:WebLogic", var1, var2), var3, var4);
   }

   public static WebLogicDeploymentManager getDeploymentManager(String var0, String var1) throws DeploymentManagerCreationException {
      WebLogicDeploymentFactory var2 = (WebLogicDeploymentFactory)registerDefaultFactory();
      return (WebLogicDeploymentManager)var2.getDeploymentManager(var2.createUri("authenticated:deployer:WebLogic", var0, var1), (String)null, (String)null);
   }

   public WebLogicDeployableObject getDeployableObject() {
      return this.dObject;
   }

   public WebLogicDeploymentConfiguration getConfiguration() {
      return this.dConfig;
   }

   public void initializeConfiguration() throws ConfigurationException, IOException, InvalidModuleException {
      this.initializeConfiguration((AppDeploymentMBean)null);
   }

   public void initializeConfiguration(AppDeploymentMBean var1) throws ConfigurationException, IOException, InvalidModuleException {
      if (this.dObject != null) {
         throw new AssertionError(SPIDeployerLogger.getReinitializeError());
      } else {
         this.normalizeProperties(var1);
         this.initialize();
      }
   }

   private void normalizeProperties() throws IOException {
      this.normalizeProperties((AppDeploymentMBean)null);
   }

   private void normalizeProperties(AppDeploymentMBean var1) throws IOException {
      if (this.getApplication() == null) {
         throw new IllegalArgumentException(SPIDeployerLogger.noAppProvided());
      } else {
         String var3 = this.getApplication().getName();
         if (var1 != null) {
            var3 = var1.getName();
         }

         InstallDir var2 = new InstallDir(var3, this.getApplicationRoot());
         var2.setArchive(this.getApplication());
         var2.setPlan(this.getPlan());
         if (this.getPlandir() != null) {
            var2.setConfigDir(this.getPlandir());
         }

         this.setApplication(var2.getArchive(), true);
         this.setPlan(var2.getPlan(), true);
         this.setApplicationRoot(var2.getInstallDir(), true);
      }
   }

   /** @deprecated */
   public void initializeConfiguration(File var1, File var2, ModuleType var3) throws ConfigurationException, IOException, InvalidModuleException {
      this.initializeConfiguration(var1, var2, (File)null);
   }

   /** @deprecated */
   public void initializeConfiguration(File var1, File var2) throws ConfigurationException, IOException, InvalidModuleException {
      this.initializeConfiguration(var1, var2, (File)null);
   }

   /** @deprecated */
   public void initializeConfiguration(File var1, File var2, File var3) throws ConfigurationException, IOException, InvalidModuleException {
      if (this.debug) {
         Debug.say("In deprecated method");
      }

      this.setApplication(var1, true);
      this.setPlan(var2, true);
      this.setApplicationRoot(var3, true);
      this.initializeConfiguration();
   }

   /** @deprecated */
   public void initializeConfiguration(File var1, File var2, File var3, ModuleType var4) throws ConfigurationException, IOException, InvalidModuleException {
      this.initializeConfiguration(var1, var2, var3);
   }

   public void inspect() throws IOException, InvalidModuleException, ConfigurationException {
      if (this.dObject != null) {
         throw new AssertionError(SPIDeployerLogger.getReinitializeError());
      } else {
         this.normalizeProperties();
         WebLogicDeployableObjectFactoryImpl var1 = new WebLogicDeployableObjectFactoryImpl();
         if (this.lightWeightAppName != null) {
            var1.setLightWeightAppName(this.lightWeightAppName);
         }

         this.dObject = var1.createLazyDeployableObject(this.getApplication(), this.getApplicationRoot(), this.getPlan(), this.getPlandir(), this.getLibraries());
         this.moduleType = this.dObject.getType();
         if (this.debug) {
            Debug.say("derived module type: " + this.moduleType.toString());
         }

         this.dConfig = (WebLogicDeploymentConfiguration)this.dm.createConfiguration(this.dObject);
         if (this.dObject instanceof WebLogicJ2eeApplicationObject) {
            this.dConfig.getDConfigBeanRoot(this.dObject.getDDBeanRoot());
         }

         this.restorefromPlan();
      }
   }

   public LibrarySpec registerLibrary(File var1, String var2, String var3, String var4) throws IllegalArgumentException {
      if (this.libs == null) {
         this.enableLibraryMerge();
      }

      LibrarySpec var5 = new LibrarySpec(var2, var3, var4, var1);
      this.libs.add(var5);
      return var5;
   }

   public ModuleInfo getModuleInfo() throws IOException, ConfigurationException {
      return ModuleInfo.createModuleInfo(this.getDeployableObject(), this.getConfiguration(), (String)null);
   }

   protected void initialize() throws ConfigurationException, IOException, InvalidModuleException {
      if (this.debug) {
         Debug.say("Initializing configuration using");
         Debug.say("   app: " + this.getApplication().getPath());
         if (this.getPlan() != null) {
            Debug.say("   plan: " + this.getPlan().getPath());
         }

         if (this.getApplicationRoot() != null) {
            Debug.say("   root: " + this.getApplicationRoot().getPath());
         }
      }

      WebLogicDeployableObjectFactoryImpl var1 = new WebLogicDeployableObjectFactoryImpl();
      if (this.lightWeightAppName != null) {
         var1.setLightWeightAppName(this.lightWeightAppName);
      }

      this.dObject = var1.createDeployableObject(this.getApplication(), this.getApplicationRoot(), this.getPlan(), this.getPlandir(), this.getLibraries());
      this.moduleType = this.dObject.getType();
      if (this.debug) {
         Debug.say("derived module type: " + this.moduleType.toString());
      }

      this.dConfig = (WebLogicDeploymentConfiguration)this.dm.createConfiguration(this.dObject);
      this.initializeWithConfig();
      if (this.updatePlanVersion) {
         this.bumpVersion();
      }

   }

   public LibrarySpec[] getLibraries() {
      return this.libs == null ? null : (LibrarySpec[])((LibrarySpec[])this.libs.toArray(new LibrarySpec[0]));
   }

   public void enableLibraryMerge() {
      if (this.libs == null) {
         this.libs = new ArrayList();
      }

   }

   private void initializeScaWithConfig() throws ConfigurationException {
      ScaApplicationObject var1 = (ScaApplicationObject)this.dObject;
      DeployableObject[] var2 = var1.getDeployableObjects();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            ModuleType var4 = var2[var3].getType();
            if (var4 == ModuleType.WAR || var4 == ModuleType.EJB) {
               DDBeanRoot var5 = var2[var3].getDDBeanRoot();
               DConfigBeanRoot var6 = this.dConfig.getDConfigBeanRoot(var5);
               if (this.fullInit) {
                  ConfigHelper.beanWalker(var5, var6);
               }
            }
         }

      }
   }

   protected void initializeWithConfig() throws ConfigurationException {
      if (this.debug) {
         Debug.say("initializing dconfig");
      }

      if (this.moduleType == WebLogicModuleType.SCA_COMPOSITE) {
         this.initializeScaWithConfig();
      } else {
         DDBeanRoot var1 = this.dObject.getDDBeanRoot();
         this.restorefromPlan();
         DConfigBeanRoot var2 = this.dConfig.getDConfigBeanRoot(var1);
         if (this.fullInit) {
            ConfigHelper.beanWalker(var1, var2);
         }

         if (var2 != null && this.moduleType == ModuleType.EAR) {
            DeployableObject[] var5 = ((WebLogicJ2eeApplicationObject)this.dObject).getDeployableObjects();
            if (var5 != null) {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  DeployableObject var4 = var5[var6];
                  String var7 = ((WebLogicDeployableObject)var4).getUri();
                  if (this.debug) {
                     Debug.say("Collecting beans for embedded module at, " + var7);
                  }

                  try {
                     if (DescriptorSupportManager.getForModuleType(var4.getDDBeanRoot().getType()).length > 0) {
                        DConfigBeanRoot var3 = this.dConfig.getDConfigBeanRoot(var4.getDDBeanRoot());
                        if (this.fullInit) {
                           ConfigHelper.beanWalker(var4.getDDBeanRoot(), var3);
                        }
                     }
                  } catch (ConfigurationException var9) {
                     SPIDeployerLogger.logNoDCB(var7, var9.toString());
                     throw var9;
                  }
               }
            }
         }

      }
   }

   private void restorefromPlan() throws ConfigurationException {
      if (this.planBean != null) {
         this.dConfig.restore(this.planBean);
      } else if (this.getPlan() != null) {
         try {
            FileInputStream var1 = new FileInputStream(this.getPlan());

            try {
               this.dConfig.restore(var1);
            } finally {
               var1.close();
            }
         } catch (IOException var7) {
            SPIDeployerLogger.logNoPlan(this.getPlan().getPath());
            this.setPlan((File)null);
         }
      }

   }

   public void savePlan() throws IllegalStateException, ConfigurationException, FileNotFoundException {
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else {
         if (this.debug) {
            Debug.say("Saving plan to " + this.getPlan());
         }

         this.getPlan().getAbsoluteFile().getParentFile().mkdirs();
         this.savePlan(new FileOutputStream(this.getPlan()));
      }
   }

   public void saveApplicationRoot() throws IOException, ConfigurationException, IllegalStateException {
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else {
         File var1 = this.getApplication().getCanonicalFile();
         InstallDir var2 = new InstallDir(this.getApplicationRoot());
         var2.getAppDir().mkdirs();
         var2.setArchive(new File(var2.getAppDir(), this.getApplication().getName()));
         var2.getConfigDir().mkdirs();
         String var3 = this.getPlan() == null ? "plan.xml" : this.getPlan().getName();
         var2.setPlan(new File(var2.getConfigDir(), var3));
         this.setApplication(var2.getArchive(), true);
         this.setPlan(var2.getPlan(), true);
         this.getConfiguration().getPlan().setConfigRoot(var2.getConfigDir().getCanonicalPath());
         if (!var1.equals(this.getApplication().getCanonicalFile())) {
            if (this.debug) {
               Debug.say("Copying app to " + this.getApplication());
            }

            FileUtils.copy(var1, this.getApplication());
         }

         this.savePlan();
      }
   }

   private static DeploymentFactory getRegisteredDefaultFactory() {
      DeploymentFactory[] var0 = DeploymentFactoryManager.getInstance().getDeploymentFactories();
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (DEF_FACTORY_CLASS.isInstance(var0[var1])) {
               return var0[var1];
            }
         }
      }

      return null;
   }

   private static DeploymentFactory registerDefaultFactory() throws IllegalArgumentException {
      DeploymentFactory var0 = getRegisteredDefaultFactory();
      if (var0 != null) {
         return var0;
      } else {
         try {
            if (DeploymentFactory.class.isAssignableFrom(DEF_FACTORY_CLASS)) {
               DeploymentFactory var1 = (DeploymentFactory)DEF_FACTORY_CLASS.newInstance();
               DeploymentFactoryManager.getInstance().registerDeploymentFactory(var1);
               return var1;
            } else {
               throw new IllegalArgumentException(SPIDeployerLogger.invalidFactory(DEF_FACTORY_CLASS.getName()));
            }
         } catch (InstantiationException var2) {
            throw new IllegalArgumentException(SPIDeployerLogger.invalidFactory(DEF_FACTORY_CLASS.getName()));
         } catch (IllegalAccessException var3) {
            throw new IllegalArgumentException(SPIDeployerLogger.invalidFactory(DEF_FACTORY_CLASS.getName()));
         }
      }
   }

   public File[] getPlanDirectories(String var1) throws IOException {
      ConfigHelper.checkParam("appName", var1);
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else {
         HashSet var2 = new HashSet();
         if (this.getPlan() != null) {
            this.addFile(var2, this.getPlan().getParentFile().getCanonicalFile(), false);
         }

         if (this.getConfiguration().getPlan().getConfigRoot() != null) {
            this.addFile(var2, (new File(this.getConfiguration().getPlan().getConfigRoot())).getCanonicalFile(), false);
         }

         File var3 = new File(DomainDir.getDeploymentsDir(), var1);
         if (var3.exists()) {
            this.addFile(var2, (new InstallDir(var1, var3)).getConfigDir().getCanonicalFile(), false);
         }

         return (File[])((File[])var2.toArray(new File[0]));
      }
   }

   public File savePlan(File var1, String var2) throws IOException, ConfigurationException {
      ConfigHelper.checkParam("dir", var1);
      ConfigHelper.checkParam("planName", var2);
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else {
         if (!var1.exists()) {
            var1.mkdirs();
         }

         File var3 = var1.getCanonicalFile();
         if (!var3.isDirectory()) {
            throw new IllegalArgumentException(SPIDeployerLogger.notDir(var3.getPath()));
         } else {
            return this.saveBackToSource(var1, var2);
         }
      }
   }

   private File saveBackToSource(File var1, String var2) throws FileNotFoundException, ConfigurationException {
      File var3 = new File(var1, var2);
      FileOutputStream var4 = new FileOutputStream(var3);
      this.savePlan(var4);
      return var3;
   }

   private void savePlan(OutputStream var1) throws ConfigurationException {
      this.getConfiguration().save(var1);
   }

   protected void bumpVersion() {
      DeploymentPlanBean var1 = this.getConfiguration().getPlan();
      if (var1.getVersion() == null) {
         var1.setVersion("1.0");
      } else {
         try {
            Float var2 = new Float(var1.getVersion());
            Float var3 = new Float(1.0);
            var1.setVersion((new Float(var2 + var3)).toString());
         } catch (NumberFormatException var4) {
         }
      }

   }

   public String getNewPlanName() {
      String var1 = "plan.xml";
      if (this.getPlan() != null) {
         var1 = this.getPlan().getName();
      }

      if (this.getConfiguration() != null) {
         String var2 = this.getConfiguration().getPlan().getVersion();
         if (var2 != null) {
            int var3 = var1.indexOf(".xml");
            if (var3 == -1) {
               var1 = var1 + "_" + var2;
            } else {
               var1 = var1.substring(0, var3) + "_" + var2 + ".xml";
            }
         }
      }

      return var1;
   }

   public File[] findPlans() {
      HashSet var1 = new HashSet();
      if (this.getPlan() != null) {
         this.addFile(var1, this.getPlan(), true);
      }

      File var2;
      if (this.getConfiguration() != null) {
         var2 = new File(this.getConfiguration().getPlan().getConfigRoot());
         this.addPlansFromDirectory(var2, var1);
      }

      if (this.getApplicationRoot() != null) {
         var2 = new File(this.getApplicationRoot().getPath() + File.separator + "plan");
         this.addPlansFromDirectory(var2, var1);
      }

      return (File[])((File[])var1.toArray(new File[0]));
   }

   private void addPlansFromDirectory(File var1, Set var2) {
      if (var1.exists() && var1.isDirectory()) {
         File[] var3 = var1.listFiles(new FileFilter() {
            public boolean accept(File var1) {
               return var1.isFile() && var1.getName().endsWith(".xml");
            }
         });

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.addFile(var2, var3[var4], true);
         }
      }

   }

   private void addFile(Set var1, File var2, boolean var3) {
      try {
         if (!var3 || var2.exists()) {
            var1.add(var2.getCanonicalFile());
         }
      } catch (IOException var5) {
      }

   }

   public TargetModuleID[] getDefaultJMSTargetModuleIDs(DomainMBean var1, TargetMBean[] var2, String var3, String var4) throws ConfigurationException {
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else {
         ArrayList var5 = new ArrayList();
         if (var4 == null) {
            return null;
         } else if (var2 != null && var2.length > 0) {
            AppDeploymentMBean var6 = AppDeploymentHelper.lookupAppOrLib(var3, var1);
            if (var6 != null && this.checkAppTargetting(var6, var4)) {
               return null;
            } else {
               JMSBean var7 = null;

               try {
                  var7 = this.getJMSDescriptor(var4);
               } catch (FileNotFoundException var16) {
               }

               if (var7 == null) {
                  return null;
               } else {
                  HashMap var8 = DefaultingHelper.getJMSDefaultTargets(var7, var1, var2);
                  Iterator var9 = var8.keySet().iterator();

                  while(var9.hasNext()) {
                     String var11 = (String)var9.next();
                     TargetMBean[] var12 = (TargetMBean[])((TargetMBean[])var8.get(var11));

                     for(int var13 = 0; var13 < var12.length; ++var13) {
                        TargetMBean var14 = var12[var13];
                        if (var14 != null) {
                           TargetModuleID var10 = this.createParentTmids(var3, var4, var14);
                           if (var10.getChildTargetModuleID() != null) {
                              var10 = var10.getChildTargetModuleID()[0];
                           }

                           WebLogicTargetModuleID var15 = this.dm.createTargetModuleID((TargetModuleID)var10, (String)var11, (ModuleType)WebLogicModuleType.SUBMODULE);
                           var5.add(var15);
                        }
                     }
                  }

                  return (TargetModuleID[])((TargetModuleID[])var5.toArray(new TargetModuleID[0]));
               }
            }
         } else {
            return null;
         }
      }
   }

   private boolean checkAppTargetting(AppDeploymentMBean var1, String var2) {
      SubDeploymentMBean[] var3;
      if (this.getDeployableObject().getType().getValue() == ModuleType.EAR.getValue()) {
         var3 = var1.getSubDeployments();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            SubDeploymentMBean var5 = var3[var4];
            if (var5.getName().equals(var2)) {
               return this.checkIfTargeted(var5);
            }
         }
      } else {
         var3 = var1.getSubDeployments();
         if (var3 != null && var3.length != 0) {
            return true;
         }
      }

      return false;
   }

   private boolean checkIfTargeted(SubDeploymentMBean var1) {
      SubDeploymentMBean[] var2 = var1.getSubDeployments();
      return var2 != null && var2.length != 0;
   }

   private TargetModuleID createParentTmids(String var1, String var2, TargetMBean var3) {
      WebLogicTargetModuleID var4;
      if (this.getDeployableObject().getType().getValue() == ModuleType.EAR.getValue()) {
         var4 = this.dm.createTargetModuleID(var1, ModuleType.EAR, this.getTarget(var3));
         var4 = this.dm.createTargetModuleID((TargetModuleID)var4, (String)var2, (ModuleType)WebLogicModuleType.JMS);
      } else {
         var4 = this.dm.createTargetModuleID((String)var2, (ModuleType)WebLogicModuleType.JMS, (Target)this.getTarget(var3));
      }

      return var4;
   }

   public Target getTarget(TargetMBean var1) {
      return this.dm.getTarget(var1.getName());
   }

   public JMSBean getJMSDescriptor(String var1) throws ConfigurationException, FileNotFoundException {
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else {
         WebLogicDConfigBeanRoot var2;
         if (this.getDeployableObject().getType().getValue() == WebLogicModuleType.JMS.getValue() && this.getApplication().getName().equals(var1)) {
            var2 = (WebLogicDConfigBeanRoot)this.getConfiguration().getDConfigBeanRoot(this.getDeployableObject().getDDBeanRoot());
            return (JMSBean)var2.getDescriptorBean();
         } else {
            if (this.getDeployableObject() instanceof WebLogicJ2eeApplicationObject) {
               var2 = null;

               try {
                  var2 = (WebLogicDConfigBeanRoot)this.getConfiguration().getDConfigBeanRoot(this.getDeployableObject().getDDBeanRoot());
                  WeblogicApplicationBean var3 = (WeblogicApplicationBean)var2.getDescriptorBean();
                  if (var3.getModules() == null) {
                     return null;
                  }

                  Iterator var4 = Arrays.asList((Object[])var3.getModules()).iterator();
                  String var5 = null;

                  while(var4.hasNext()) {
                     WeblogicModuleBean var6 = (WeblogicModuleBean)var4.next();
                     if (var6.getName().equals(var1)) {
                        var5 = var6.getPath();
                        break;
                     }
                  }

                  if (var5 != null) {
                     var2 = (WebLogicDConfigBeanRoot)var2.getDConfigBean(this.getDeployableObject().getDDBeanRoot(var5));
                     return (JMSBean)var2.getDescriptorBean();
                  }
               } catch (DDBeanCreateException var7) {
                  throw new ConfigurationException(var7.toString());
               }
            }

            return null;
         }
      }
   }

   public String[] getDescriptorUris(ModuleType var1) {
      if (this.getConfiguration() == null) {
         throw new IllegalStateException(SPIDeployerLogger.mustInit());
      } else if (this.getDeployableObject().getType().getValue() == var1.getValue()) {
         return new String[]{this.getApplication().getName()};
      } else if (this.getDeployableObject().getType().getValue() == ModuleType.EAR.getValue()) {
         ArrayList var2 = new ArrayList();
         WebLogicJ2eeApplicationObject var3 = (WebLogicJ2eeApplicationObject)this.getDeployableObject();
         DDBeanRoot[] var4 = var3.getDDBeanRoots();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            DDBeanRoot var6 = var4[var5];
            if (var6.getType().getValue() == var1.getValue()) {
               var2.add(var6.getFilename());
            }
         }

         return (String[])((String[])var2.toArray(new String[0]));
      } else {
         return new String[0];
      }
   }
}
