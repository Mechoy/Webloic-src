package weblogic.deploy.api.spi.deploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.shared.DConfigBeanVersionType;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DeploymentConfiguration;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DConfigBeanVersionUnsupportedException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.JMXDeployerHelper;
import weblogic.deploy.api.internal.utils.LocaleManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.shared.WebLogicTargetType;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.api.spi.config.DeploymentConfigurationImpl;
import weblogic.deploy.api.spi.deploy.internal.ActivateOperation;
import weblogic.deploy.api.spi.deploy.internal.DeactivateOperation;
import weblogic.deploy.api.spi.deploy.internal.DeployOperation;
import weblogic.deploy.api.spi.deploy.internal.DistributeOperation;
import weblogic.deploy.api.spi.deploy.internal.DistributeStreamsOperation;
import weblogic.deploy.api.spi.deploy.internal.RedeployDeltaOperation;
import weblogic.deploy.api.spi.deploy.internal.RedeployOperation;
import weblogic.deploy.api.spi.deploy.internal.RedeployStreamsOperation;
import weblogic.deploy.api.spi.deploy.internal.RemoveOperation;
import weblogic.deploy.api.spi.deploy.internal.StartOperation;
import weblogic.deploy.api.spi.deploy.internal.StopOperation;
import weblogic.deploy.api.spi.deploy.internal.UndeployOperation;
import weblogic.deploy.api.spi.deploy.internal.UnprepareOperation;
import weblogic.deploy.api.spi.deploy.internal.UpdateOperation;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;

public class WebLogicDeploymentManagerImpl implements WebLogicDeploymentManager {
   private static final List DCONFIG_VERSIONS;
   private DConfigBeanVersionType dconfigVersion;
   private static final boolean debug = Debug.isDebug("deploy");
   private String domain;
   private ServerConnection serverConnection;
   private boolean local;
   private boolean auth;
   private String taskId;
   public static final String PREFIX = "app";
   public static final String SUFFIX = ".zip";
   private URI saveUri;
   private String saveUser;
   private String saveClass;
   private String savePword;
   private Thread hook;
   private boolean releasing;

   public WebLogicDeploymentManagerImpl(String var1) throws DeploymentManagerCreationException {
      this.domain = "weblogic";
      this.taskId = null;
      this.hook = null;
      this.releasing = false;
      String var2 = SPIDeployerLogger.noURI();
      if (var1 == null) {
         throw new DeploymentManagerCreationException(var2);
      } else {
         this.setCharacteristics(var1);
         this.dconfigVersion = (DConfigBeanVersionType)DCONFIG_VERSIONS.get(DCONFIG_VERSIONS.size() - 1);
         if (debug) {
            Debug.say("Constructing DeploymentManager for J2EE version " + this.dconfigVersion.toString() + " deployments");
         }

      }
   }

   public WebLogicDeploymentManagerImpl(String var1, String var2, URI var3, String var4, String var5) throws DeploymentManagerCreationException {
      this(var2);

      DeploymentManagerCreationException var7;
      try {
         this.saveConnectionArgs(var3, var4, var1, var5);
         this.getNewConnection();
      } catch (ServerConnectionException var8) {
         this.release();
         var7 = new DeploymentManagerCreationException(var8.getMessage());
         var7.initCause(var8);
         throw var7;
      } catch (Throwable var9) {
         this.serverConnection = null;
         if (debug) {
            if (var9.getCause() != null) {
               var9.getCause().printStackTrace();
            } else {
               var9.printStackTrace();
            }
         }

         var7 = new DeploymentManagerCreationException(SPIDeployerLogger.noClass(var1, var9.toString()));
         var7.initCause(var9);
         throw var7;
      }
   }

   private void saveConnectionArgs(URI var1, String var2, String var3, String var4) {
      this.saveUri = var1;
      this.saveUser = var2;
      this.saveClass = var3;
      this.savePword = var4;
   }

   private void getNewConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      if (debug) {
         Debug.say("Connecting to admin server at " + this.saveUri.getHost() + ":" + this.saveUri.getPort() + ", as user " + this.saveUser);
      }

      this.serverConnection = (ServerConnection)Class.forName(this.saveClass).newInstance();
      this.serverConnection.init(this.saveUri, this.saveUser, this.savePword, this);
      this.hook = new Thread() {
         public void run() {
            if (WebLogicDeploymentManagerImpl.debug) {
               Debug.say("Releasing connection due to user signal");
            }

            WebLogicDeploymentManagerImpl.this.shutdown();
         }
      };
      Runtime.getRuntime().addShutdownHook(this.hook);
   }

   private void shutdown() {
      try {
         this._release(true);
      } catch (Throwable var2) {
      }

   }

   private void removeHook() {
      try {
         if (this.hook != null) {
            Runtime.getRuntime().removeShutdownHook(this.hook);
         }

         this.hook = null;
      } catch (Throwable var2) {
      }

   }

   public Target getTarget(String var1) throws IllegalStateException {
      return this.serverConnection.getTarget(var1);
   }

   public final WebLogicTargetModuleID createTargetModuleID(String var1, ModuleType var2, Target var3) {
      ConfigHelper.checkParam("type", var2);
      return this.createTargetModuleID(var1, var2.getValue(), var3);
   }

   private final WebLogicTargetModuleID createTargetModuleID(String var1, int var2, Target var3) {
      return new TargetModuleIDImpl(var1, var3, (TargetModuleID)null, var2, this);
   }

   public final WebLogicTargetModuleID createTargetModuleID(TargetModuleID var1, String var2, ModuleType var3) {
      ConfigHelper.checkParam("base", var1);
      ConfigHelper.checkParam("module", var2);
      ConfigHelper.checkParam("type", var3);

      WebLogicTargetModuleID var5;
      for(var5 = (WebLogicTargetModuleID)var1; var5.getParentTargetModuleID() != null; var5 = (WebLogicTargetModuleID)var5.getParentTargetModuleID()) {
      }

      boolean var6 = var5 == var1;
      var5 = this.createTargetModuleID(var5.getModuleID(), ((TargetModuleIDImpl)var5).getValue(), var5.getTarget());
      var5.setTargeted(false);
      Object var4;
      if (!var6) {
         var4 = new TargetModuleIDImpl(var1.getModuleID(), var1.getTarget(), var5, ((TargetModuleIDImpl)var1).getValue(), this);
         ((WebLogicTargetModuleID)var4).setTargeted(false);
      } else {
         var4 = var5;
      }

      new TargetModuleIDImpl(var2, ((WebLogicTargetModuleID)var4).getTarget(), (TargetModuleID)var4, var3, this);
      return var5;
   }

   public boolean isLocal() {
      return this.local;
   }

   public String getDomain() {
      return this.domain;
   }

   public void setDomain(String var1) {
      this.domain = var1;
   }

   public boolean isConnected() {
      try {
         this.testConnection();
      } catch (IllegalStateException var2) {
         return false;
      }

      return this.serverConnection != null && !this.releasing;
   }

   public void enableFileUploads() throws IllegalStateException {
      this.checkConnection();
      if (this.local) {
         this.serverConnection.setRemote();
         this.local = false;
      }

   }

   private void checkConnection() throws IllegalStateException {
      if (this.serverConnection == null) {
         throw new IllegalStateException(SPIDeployerLogger.notConnected());
      } else {
         this.testConnection();
      }
   }

   private void testConnection() throws IllegalStateException {
      if (this.serverConnection != null && !this.releasing) {
         try {
            this.serverConnection.test();
         } catch (Throwable var6) {
            this.release();

            try {
               if (debug) {
                  Debug.say("Attempting to recover lost connection!!!");
               }

               this.getNewConnection();
            } catch (IllegalStateException var4) {
               this.release();
               throw var4;
            } catch (Throwable var5) {
               this.release();
               IllegalStateException var3 = new IllegalStateException(SPIDeployerLogger.connectionError());
               var3.initCause(var5);
               throw var3;
            }
         }

      }
   }

   public String getTaskId() {
      String var1 = this.taskId;
      this.taskId = null;
      return var1;
   }

   public boolean isAuthenticated() {
      return this.auth;
   }

   public void setTaskId(String var1) throws IllegalStateException {
      ConfigHelper.checkParam("id", var1);
      this.checkConnection();
      this.taskId = var1;
   }

   public TargetModuleID[] filter(TargetModuleID[] var1, String var2, String var3, String var4) {
      ConfigHelper.checkParam("modules", var1);
      ConfigHelper.checkParam("appName", var2);
      if (var1 == null) {
         return null;
      } else {
         HashSet var5 = new HashSet();

         TargetModuleID var6;
         for(int var8 = 0; var8 < var1.length; ++var8) {
            var6 = var1[var8];
            TargetModuleID var7 = var6.getParentTargetModuleID();
            if (var7 == null) {
               if (var3 == null && var6.getModuleID().equals(var2)) {
                  var5.add(var6);
               }
            } else if (var6.getModuleID().equals(var3) && var2.equals(var7.getModuleID())) {
               var5.add(var6);
            }
         }

         if (var4 != null) {
            Iterator var9 = var5.iterator();

            while(var9.hasNext()) {
               var6 = (TargetModuleID)var9.next();
               if (!var4.equals(((WebLogicTargetModuleID)var6).getVersion())) {
                  var9.remove();
               }
            }
         }

         return (TargetModuleID[])((TargetModuleID[])var5.toArray(new TargetModuleID[0]));
      }
   }

   public TargetModuleID[] getModules(ConfigurationMBean var1) throws IllegalStateException, IllegalArgumentException {
      ConfigHelper.checkParam("mbean", var1);
      this.checkConnection();

      try {
         return (TargetModuleID[])((TargetModuleID[])this.serverConnection.getModules(var1).toArray(new TargetModuleID[0]));
      } catch (Exception var3) {
         throw new IllegalArgumentException(var3.getMessage());
      }
   }

   public JMXDeployerHelper getHelper() {
      return this.serverConnection.getHelper();
   }

   public ServerConnection getServerConnection() {
      return this.serverConnection;
   }

   public ProgressObject redeploy(TargetModuleID[] var1, File var2, String[] var3, DeploymentOptions var4) throws IllegalStateException {
      return (new RedeployDeltaOperation(this, var1, var2, var3, false, var4)).run();
   }

   public ProgressObject undeploy(TargetModuleID[] var1, File var2, String[] var3, DeploymentOptions var4) throws IllegalStateException {
      return (new RedeployDeltaOperation(this, var1, var2, var3, true, var4)).run();
   }

   public ProgressObject deploy(Target[] var1, File var2, File var3, DeploymentOptions var4) throws TargetException, IllegalStateException {
      return (new DeployOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject deploy(TargetModuleID[] var1, File var2, File var3, DeploymentOptions var4) throws TargetException, IllegalStateException {
      return (new DeployOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject update(TargetModuleID[] var1, File var2, DeploymentOptions var3) throws IllegalStateException {
      return (new UpdateOperation(this, var1, var2, var3)).run();
   }

   public ProgressObject update(TargetModuleID[] var1, File var2, String[] var3, DeploymentOptions var4) throws IllegalStateException {
      return (new UpdateOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject start(TargetModuleID[] var1, DeploymentOptions var2) throws IllegalStateException {
      return (new StartOperation(this, var1, var2)).run();
   }

   public ProgressObject stop(TargetModuleID[] var1, DeploymentOptions var2) throws IllegalStateException {
      return (new StopOperation(this, var1, var2)).run();
   }

   public ProgressObject unprepare(TargetModuleID[] var1, DeploymentOptions var2) {
      return (new UnprepareOperation(this, var1, var2)).run();
   }

   public ProgressObject deactivate(TargetModuleID[] var1, DeploymentOptions var2) {
      return (new DeactivateOperation(this, var1, var2)).run();
   }

   public ProgressObject remove(TargetModuleID[] var1, DeploymentOptions var2) {
      return (new RemoveOperation(this, var1, var2)).run();
   }

   public ProgressObject activate(TargetModuleID[] var1, File var2, File var3, DeploymentOptions var4) throws TargetException, IllegalStateException {
      return (new ActivateOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject undeploy(TargetModuleID[] var1, DeploymentOptions var2) throws IllegalStateException {
      return (new UndeployOperation(this, var1, var2)).run();
   }

   public ProgressObject distribute(Target[] var1, File var2, File var3, DeploymentOptions var4) throws IllegalStateException {
      return (new DistributeOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject distribute(TargetModuleID[] var1, File var2, File var3, DeploymentOptions var4) throws IllegalStateException, TargetException {
      return (new DistributeOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject distribute(Target[] var1, InputStream var2, InputStream var3, DeploymentOptions var4) throws IllegalStateException {
      return (new DistributeStreamsOperation(this, var1, (ModuleType)null, var2, var3, var4)).run();
   }

   private ProgressObject distribute(Target[] var1, ModuleType var2, InputStream var3, InputStream var4, DeploymentOptions var5) throws IllegalStateException {
      return (new DistributeStreamsOperation(this, var1, var2, var3, var4, var5)).run();
   }

   public ProgressObject redeploy(TargetModuleID[] var1, File var2, File var3, DeploymentOptions var4) throws UnsupportedOperationException, IllegalStateException {
      return (new RedeployOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject redeploy(TargetModuleID[] var1, InputStream var2, InputStream var3, DeploymentOptions var4) throws UnsupportedOperationException, IllegalStateException {
      return (new RedeployStreamsOperation(this, var1, var2, var3, var4)).run();
   }

   public ProgressObject distribute(Target[] var1, File var2, File var3) throws IllegalStateException {
      return this.distribute(var1, var2, var3, new DeploymentOptions());
   }

   public ProgressObject distribute(Target[] var1, InputStream var2, InputStream var3) throws IllegalStateException {
      return this.distribute(var1, var2, var3, new DeploymentOptions());
   }

   public ProgressObject distribute(Target[] var1, ModuleType var2, InputStream var3, InputStream var4) throws IllegalStateException {
      ConfigHelper.checkParam("moduleType", var2);
      return this.distribute(var1, var2, var3, var4, new DeploymentOptions());
   }

   public ProgressObject start(TargetModuleID[] var1) throws IllegalStateException {
      return this.start(var1, new DeploymentOptions());
   }

   public ProgressObject stop(TargetModuleID[] var1) throws IllegalStateException {
      return this.stop(var1, new DeploymentOptions());
   }

   public ProgressObject undeploy(TargetModuleID[] var1) throws IllegalStateException {
      return this.undeploy(var1, new DeploymentOptions());
   }

   public boolean isRedeploySupported() {
      return false;
   }

   public ProgressObject redeploy(TargetModuleID[] var1, File var2, File var3) throws UnsupportedOperationException, IllegalStateException {
      return this.redeploy(var1, var2, var3, new DeploymentOptions());
   }

   public ProgressObject redeploy(TargetModuleID[] var1, InputStream var2, InputStream var3) throws UnsupportedOperationException, IllegalStateException {
      return this.redeploy(var1, var2, var3, new DeploymentOptions());
   }

   public Target[] getTargets() throws IllegalStateException {
      this.checkConnection();
      Target[] var1 = (Target[])((Target[])this.serverConnection.getTargets().toArray(new Target[0]));
      if (var1.length == 0) {
         var1 = null;
      }

      if (debug) {
         if (var1 == null) {
            Debug.say("Return no targets");
         } else {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               Target var3 = var1[var2];
               Debug.say("Return target " + var3.getName());
            }
         }
      }

      return var1;
   }

   public TargetModuleID[] getRunningModules(ModuleType var1, Target[] var2) throws TargetException, IllegalStateException {
      if (debug) {
         Debug.say("getting all running modules of type " + var1);
      }

      return this.getModules(var1, var2, true);
   }

   public TargetModuleID[] getNonRunningModules(ModuleType var1, Target[] var2) throws TargetException, IllegalStateException {
      if (debug) {
         Debug.say("getting all nonrunning modules of type " + var1);
      }

      return this.getModules(var1, var2, false);
   }

   public TargetModuleID[] getModules(ModuleType var1, Target[] var2, boolean var3) throws TargetException, IllegalStateException {
      ConfigHelper.checkParam("moduleType", var1);
      ConfigHelper.checkParam("targetList", var2);
      this.checkConnection();
      TargetModuleID[] var4 = this.getAvailableModules(var1, var2);
      if (var4 != null) {
         HashSet var5 = new HashSet(Arrays.asList((Object[])var4));
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            TargetModuleID var7 = (TargetModuleID)var6.next();
            if (var3 != this.serverConnection.isRunning(var7)) {
               var6.remove();
            }
         }

         if (var5.isEmpty()) {
            return null;
         } else {
            this.dumpTmidSet(var5, var3);
            return (TargetModuleID[])((TargetModuleID[])var5.toArray(new TargetModuleID[0]));
         }
      } else {
         return null;
      }
   }

   private void dumpTmidSet(Set var1, boolean var2) {
      if (debug) {
         Debug.say(var2 ? "Running Modules" : "Nonrunning Modules");
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Debug.say("   " + var3.next());
         }
      }

   }

   public TargetModuleID[] getAvailableModules(ModuleType var1, Target[] var2) throws TargetException, IllegalStateException {
      ConfigHelper.checkParam("moduleType", var1);
      ConfigHelper.checkParam("targetList", var2);
      if (debug) {
         Debug.say("getting all available modules of type " + var1);
      }

      this.checkConnection();
      this.serverConnection.validateTargets(var2);
      List var3 = this.serverConnection.getModulesForTargets(var1, var2);
      return var3.size() == 0 ? null : (TargetModuleID[])((TargetModuleID[])var3.toArray(new TargetModuleID[0]));
   }

   private Set getTargetNames(Target[] var1) {
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3].getName());
      }

      return var2;
   }

   public DeploymentConfiguration createConfiguration(DeployableObject var1) throws InvalidModuleException {
      ConfigHelper.checkParam("DeployableObject", var1);

      try {
         return new DeploymentConfigurationImpl(var1);
      } catch (IOException var4) {
         InvalidModuleException var3 = new InvalidModuleException(SPIDeployerLogger.createError(var1.toString()));
         var3.initCause(var4);
         throw var3;
      }
   }

   public void release() {
      this._release(false);
   }

   private synchronized void _release(boolean var1) {
      if (!this.releasing) {
         if (this.serverConnection != null) {
            this.releasing = true;
            this.serverConnection.close(var1);
         }

         this.releasing = false;
         this.serverConnection = null;
         this.removeHook();
      }
   }

   public Locale getDefaultLocale() {
      return LocaleManager.getDefaultLocale();
   }

   public Locale getCurrentLocale() {
      return LocaleManager.getCurrentLocale();
   }

   public void setLocale(Locale var1) throws UnsupportedOperationException {
      ConfigHelper.checkParam("Locale", var1);
      LocaleManager.setLocale(var1);
      if (this.serverConnection != null) {
         try {
            this.serverConnection.setLocale(var1);
         } catch (IOException var3) {
            throw new IllegalArgumentException(var3);
         }
      }

      if (debug) {
         Debug.say("Changed locale to : " + var1.toString());
      }

   }

   public Locale[] getSupportedLocales() {
      return LocaleManager.getSupportedLocales();
   }

   public boolean isLocaleSupported(Locale var1) {
      ConfigHelper.checkParam("Locale", var1);
      return LocaleManager.isLocaleSupported(var1);
   }

   public DConfigBeanVersionType getDConfigBeanVersion() {
      return this.dconfigVersion;
   }

   public boolean isDConfigBeanVersionSupported(DConfigBeanVersionType var1) {
      ConfigHelper.checkParam("DConfigBeanVersionType", var1);
      return DCONFIG_VERSIONS.contains(var1);
   }

   public void setDConfigBeanVersion(DConfigBeanVersionType var1) throws DConfigBeanVersionUnsupportedException {
      ConfigHelper.checkParam("DConfigBeanVersionType", var1);
      if (this.isDConfigBeanVersionSupported(var1)) {
         if (!this.dconfigVersion.equals(var1)) {
            this.dconfigVersion = var1;
         }

      } else {
         throw new DConfigBeanVersionUnsupportedException(SPIDeployerLogger.unsupportedVersion(var1.toString()));
      }
   }

   private void setCharacteristics(String var1) throws DeploymentManagerCreationException {
      if (var1.equals("authenticated:deployer:WebLogic")) {
         this.local = true;
         this.auth = true;
      } else if (var1.equals("deployer:WebLogic")) {
         this.local = true;
         this.auth = false;
      } else {
         if (!var1.equals("remote:deployer:WebLogic")) {
            throw new DeploymentManagerCreationException(SPIDeployerLogger.getInvalidURI(var1.toString()));
         }

         this.local = false;
         this.auth = false;
      }

   }

   public TargetModuleID[] getAvailableModules(ModuleType var1, Target[] var2, String var3) {
      ConfigHelper.checkParam("targetList", var2);
      ConfigHelper.checkParam("applicationName", var3);
      if (this.serverConnection == null) {
         return null;
      } else {
         ArrayList var4 = new ArrayList();
         AppRuntimeStateRuntimeMBean var5 = this.serverConnection.getAppRuntimeStateRuntimeMBean();
         DomainMBean var6 = this.serverConnection.getDomainMBean();
         if (var5 != null && var6 != null) {
            Object var7 = var6.lookupAppDeployment(var3);
            if (var7 == null) {
               var7 = var6.lookupLibrary(var3);
            }

            if (var7 == null) {
               return null;
            } else {
               String var8 = ((AppDeploymentMBean)var7).getModuleType();
               ModuleType var9 = WebLogicModuleType.getTypeFromString(var8);
               if (var1 == null || var9.getValue() == var1.getValue()) {
                  TargetMBean[] var10 = ((AppDeploymentMBean)var7).getTargets();
                  if (var10 != null && var10.length > 0) {
                     for(int var11 = 0; var11 < var10.length; ++var11) {
                        boolean var12 = false;
                        if (var2.length > 0) {
                           for(int var13 = 0; var13 < var2.length; ++var13) {
                              if (var10[var11].getName().equals(var2[var13].getName())) {
                                 var12 = true;
                              }
                           }
                        }

                        if (var12) {
                           String var21 = var10[var11].getName();
                           TargetImpl var14 = new TargetImpl(var21, this.getTypeForTarget(var10[var11]), this);
                           WebLogicTargetModuleID var15 = this.createTargetModuleID((String)var3, (ModuleType)var9, (Target)var14);
                           var4.add(var15);
                           String[] var16 = var5.getModuleIds(var3);
                           if (var16 != null && var16.length > 0) {
                              for(int var17 = 0; var17 < var16.length; ++var17) {
                                 String var18 = var5.getModuleType(var3, var16[var17]);
                                 ModuleType var19 = WebLogicModuleType.getTypeFromString(var18);
                                 new TargetModuleIDImpl(var16[var17], var14, var15, var19.getValue(), this);
                              }
                           }
                        }
                     }
                  }
               }

               return (TargetModuleID[])((TargetModuleID[])var4.toArray(new TargetModuleID[0]));
            }
         } else {
            return null;
         }
      }
   }

   private WebLogicTargetType getTypeForTarget(TargetMBean var1) {
      if (var1 instanceof ServerMBean) {
         return WebLogicTargetType.SERVER;
      } else if (var1 instanceof ClusterMBean) {
         return WebLogicTargetType.CLUSTER;
      } else if (var1 instanceof VirtualHostMBean) {
         return WebLogicTargetType.VIRTUALHOST;
      } else if (var1 instanceof JMSServerMBean) {
         return WebLogicTargetType.JMSSERVER;
      } else {
         return var1 instanceof SAFAgentMBean ? WebLogicTargetType.SAFAGENT : null;
      }
   }

   static {
      DCONFIG_VERSIONS = Arrays.asList((Object[])(new DConfigBeanVersionType[]{DConfigBeanVersionType.V1_4}));
   }
}
