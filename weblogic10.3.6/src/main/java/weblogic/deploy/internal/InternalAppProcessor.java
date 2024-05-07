package weblogic.deploy.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.management.InvalidAttributeValueException;
import weblogic.Home;
import weblogic.application.MBeanFactory;
import weblogic.deploy.common.Debug;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.VariableAssignmentBean;
import weblogic.j2ee.descriptor.wl.VariableBean;
import weblogic.j2ee.descriptor.wl.VariableDefinitionBean;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AdminConsoleMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.DeploymentManagerLogger;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.UpdateException;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SAMLServerConfig;
import weblogic.security.internal.saml2.SAML2ServerConfig;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public class InternalAppProcessor implements ConfigurationProcessor {
   private static final String WAR = ".war";
   private static final String JAR = ".jar";
   private static final String EAR = ".ear";
   private static final boolean ADMIN_ONLY = true;
   private static final boolean ALL_SERVERS = false;
   private static final boolean CRITICAL = true;
   private static final boolean NON_CRITICAL = false;
   private static final boolean BACKGROUND = true;
   private static final boolean IN_STARTUP = false;
   private static final boolean DISPLAY_REFRESH = true;
   private static final boolean NO_REFRESH = false;
   private static final boolean APP = false;
   private static final boolean LIBRARY = true;
   public static final String LIB;
   private static final String WEBLOGIC_JAR_PATH;
   private static boolean done;
   private String serverName;
   private static final String COOKIE_NAME = "CookieName";
   private static final String SESSION_TIMEOUT = "SessionTimeout";
   private static final String WSAT_APP = "wls-wsat";
   private static final boolean m_isWSATDeployed;
   private static final AuthenticatedSubject kernelId;
   private boolean isAdminServer;
   private ArrayList internalApps;
   private File stageDir;

   public InternalAppProcessor() {
      this.isAdminServer = ManagementService.getRuntimeAccess(kernelId).isAdminServer();
      this.internalApps = new ArrayList();
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      this.internalApps.addAll(Arrays.asList(new InternalApp("bea_wls_management_internal2", ".war", true, true, false, false, new String[]{"bea_wls_management_internal2"}, false), new InternalApp("bea_wls_diagnostics", ".war", false, false, false, true, new String[]{"bea_wls_diagnostics"}, false)));
      if (var1.getDeploymentConfiguration().isRemoteDeployerEJBEnabled() && var2.isServiceAvailable("EJB")) {
         this.internalApps.add(new InternalApp("bea_wls_remote_deployer", ".jar", true, false));
      }

      this.internalApps.add(new InternalApp("uddi", ".war", false, false, false, true, new String[]{"uddi"}, false));
      this.internalApps.add(new InternalApp("uddiexplorer", ".war", false, false, false, true, new String[]{"uddiexplorer"}, true));
      InternalApp var4;
      if (var1.isConsoleEnabled()) {
         String var3 = var1.getConsoleContextPath();
         var4 = new InternalApp("consoleapp", "", true, false, false, false, new String[]{var3, "console-help"}, true);
         this.initConsoleDeploymentPlan(var4, var1);
         this.internalApps.add(var4);
         if (!var1.isProductionModeEnabled()) {
            this.internalApps.add(new InternalApp("wlstestclient", ".ear", true, false, false, true, new String[]{"wls_utc"}, true));
         }
      }

      if (var1.getRestfulManagementServices().isEnabled()) {
         this.internalApps.add(new InternalApp("wls-management-services", ".war", true, false, true));
      }

      if (var1.getJMX().isManagementEJBEnabled() && var2.isServiceAvailable("EJB")) {
         this.internalApps.add(new InternalApp("mejb", ".jar", true, false, true));
      }

      this.internalApps.add(new InternalApp("bea_wls_deployment_internal", ".war", false, true, false, false, new String[]{"bea_wls_deployment_internal"}, false));
      ServerMBean var6 = ManagementService.getRuntimeAccess(kernelId).getServer();
      if (var6.getCluster() != null) {
         this.internalApps.add(new InternalApp("bea_wls_cluster_internal", ".war", false, true));
      }

      if (!var6.isDefaultInternalServletsDisabled()) {
         this.internalApps.add(new InternalApp("bea_wls_internal", ".war", false, true));
      }

      if (System.getProperty("weblogic.wsee.skip.async.response") == null) {
         this.internalApps.add(new InternalApp("bea_wls9_async_response", ".war", false, false, false, var6.getCluster() == null && !var1.isProductionModeEnabled()));
      }

      if (var2.isServiceAvailable("EJB")) {
         this.internalApps.add(new InternalApp("bea_wls_async_response", ".jar", false, false, true, true));
      }

      if (var1.isGuardianEnabled()) {
         this.internalApps.add(new InternalApp("bea-guardian-agent", ".war", false, false, true));
      }

      if (m_isWSATDeployed) {
         var4 = new InternalApp("wls-wsat", ".war", false, false, false, true, new String[]{"wls-wsat"}, false);
         DeploymentPlanBean var5 = WSATAppDeployHelper.buildWSATAppDeploymentPlan("wls-wsat", var1.getJTA());
         if (var5 != null) {
            var4.setDeploymentPlanBean(var5);
         }

         this.internalApps.add(var4);
      }

      if (!var1.isProductionModeEnabled()) {
         this.internalApps.add(new InternalApp("wls-cat", ".war", false, false, false, true, new String[]{"wls-cat"}, true));
      }

   }

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      if (!done) {
         this.initStagingDir();
         this.createStagingDirForInternalApps();
         if (SAMLServerConfig.isApplicationConfigured("samlits_ba")) {
            this.internalApps.add(new InternalApp("samlits_ba", ".war", false, false));
         }

         if (SAMLServerConfig.isApplicationConfigured("samlits_cc")) {
            this.internalApps.add(new InternalApp("samlits_cc", ".war", false, false));
         }

         if (SAMLServerConfig.isApplicationConfigured("samlacs")) {
            this.internalApps.add(new InternalApp("samlacs", ".war", false, false));
         }

         if (SAMLServerConfig.isApplicationConfigured("samlars")) {
            this.internalApps.add(new InternalApp("samlars", ".war", false, false));
         }

         if (SAML2ServerConfig.isApplicationConfigured("saml2")) {
            InternalApp var2 = new InternalApp("saml2", ".war", false, false);
            var2.setClustered(true);
            this.internalApps.add(var2);
         }

         Iterator var6 = this.internalApps.iterator();

         while(var6.hasNext()) {
            InternalApp var3 = (InternalApp)var6.next();

            try {
               if (!var3.adminOnly || this.isAdminServer) {
                  this.stageFilesAndCreateBeansForInternalApp(var3, 1, var1);
               }
            } catch (Exception var5) {
               this.handleErr(var5, var3.name, var3.critical);
            }
         }

         done = true;
      }
   }

   private void handleErr(Exception var1, String var2, boolean var3) throws UpdateException {
      if (var3) {
         String var4 = DeploymentManagerLogger.logCriticalInternalAppNotDeployed(var2, var1.getMessage());
         throw new UpdateException(var4, var1);
      } else {
         DeploymentManagerLogger.logNonCriticalInternalAppNotDeployed(var2, var1.getMessage());
      }
   }

   private void createStagingDirForInternalApps() {
      if (this.stageDir.exists()) {
         if (!this.stageDir.isDirectory()) {
            this.stageDir.delete();
            this.stageDir.mkdirs();
         }
      } else {
         this.stageDir.mkdirs();
      }

   }

   private void stageFilesAndCreateBeansForInternalApp(InternalApp var1, int var2, DomainMBean var3) throws ManagementException {
      String var4 = var1.name;
      boolean var5 = false;
      DeploymentException var6 = null;

      for(int var7 = 0; var7 <= var2 && !var5; ++var7) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Deploying internal app: " + var1.location + File.separator + var4);
         }

         try {
            if (!var1.adminOnly) {
               this.stageFilesForInternalApp(var1);
            }

            this.createBeansForInternalApp(var1, var3);
            var5 = true;
         } catch (SecurityException var9) {
            var6 = new DeploymentException(var9.getMessage(), var9);
         } catch (ManagementException var10) {
            var6 = new DeploymentException(var10.getMessage(), var10);
         } catch (ApplicationException var11) {
            var6 = new DeploymentException(var11.getMessage(), var11);
         } catch (InvalidAttributeValueException var12) {
            var6 = new DeploymentException(var12.getMessage(), var12);
         }

         if (var7 > 0 && !var5) {
            SlaveDeployerLogger.logRetryingInternalAppDeploymentLoggable(var4);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Retrying deployment of internal app " + var4 + ", the initial attempt failed with this exception " + var6.toString());
            }
         }
      }

      if (!var5) {
         if (var1.critical) {
            DeploymentManagerLogger.logDeployFailedForInternalApp(var4, var6);
            throw var6;
         }

         DeploymentManagerLogger.logNonCriticalInternalAppNotDeployed(var4, var6.getMessage());
         if (Debug.isDeploymentDebugEnabled()) {
            var6.printStackTrace();
         }
      }

   }

   private void stageFilesForInternalApp(InternalApp var1) throws DeploymentException {
      String var2 = var1.name + var1.suffix;
      String var3 = var1.location;
      boolean var4 = false;
      DeploymentException var5 = new DeploymentException(var2);
      File var6 = new File(this.stageDir, var2);
      File var8 = new File(var3);
      if (var8.exists() && var8.isDirectory()) {
         File var7 = new File(var8, var2);
         if (var7.exists()) {
            if (var6.lastModified() > var7.lastModified()) {
               var4 = true;
            } else {
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("Staging File: " + var3 + var2 + ". copying: '" + var2 + "' to: '" + var6 + "'");
               }

               try {
                  FileUtils.copy(var7, var6);
                  var4 = true;
               } catch (IOException var23) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Exception while staging file: " + var3 + var2 + ". copying: '" + var2 + "' to: '" + var6 + "' " + var23.getMessage());
                  }

                  var5 = new DeploymentException(var23);
               }
            }
         } else {
            String var9 = DeployerRuntimeLogger.noAppFilesExist(var7.toString());
            var5 = new DeploymentException(var9);
         }
      }

      if (!var4) {
         var8 = new File(WEBLOGIC_JAR_PATH);
         if (var8.exists() && var6.lastModified() > var8.lastModified()) {
            var4 = true;
         } else {
            InputStream var24 = InternalAppProcessor.class.getResourceAsStream("/" + var2);
            if (var24 != null) {
               try {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Staging globalWarFile. copying: '" + var2 + "' to: '" + var6 + "'");
                  }

                  FileUtils.writeToFile(var24, var6);
                  var4 = true;
               } catch (IOException var21) {
                  if (Debug.isDeploymentDebugEnabled()) {
                     Debug.deploymentDebug("Exception while staging file: /" + var2 + " Unable to get resource from classloader" + var21.getMessage());
                  }

                  var5 = new DeploymentException(var21);
               } finally {
                  try {
                     var24.close();
                  } catch (IOException var20) {
                  }

               }
            }
         }
      }

      if (!var4) {
         throw var5;
      }
   }

   public void createBeansForInternalApp(InternalApp var1, DomainMBean var2) throws ApplicationException, InvalidAttributeValueException, ManagementException {
      String var3 = var1.name;
      File var5;
      if (var1.adminOnly) {
         var5 = new File(new File(var1.location), var3 + var1.suffix);
      } else {
         var5 = new File(this.stageDir, var3 + var1.suffix);
      }

      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Creating ApplicationMBean for internal app: " + var5);
      }

      if (!var5.exists()) {
         throw new ApplicationException(DeployerRuntimeLogger.noAppFilesExist(var3));
      } else {
         ApplicationMBean var4 = MBeanFactory.getMBeanFactory().initializeMBeans(var2, var5, var3, (String)null, (String)null, (AppDeploymentMBean)null);
         var4.setPersistenceEnabled(false);
         var4.setLoadOrder(0);
         var4.setStagingMode("nostage");
         var4.setInternalApp(true);
         ComponentMBean[] var6 = var4.getComponents();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var6[var7].setPersistenceEnabled(false);
            if (var1.isClustered()) {
               var6[var7].setTargets(this.getTargetsForClusteredInternalApp(var2));
            } else {
               var6[var7].setTargets(this.getTargetsForInternalApp(var2));
            }
         }

         this.createAppDeploymentMBeanForInternalApp(var4, var5.getPath(), var2, var1);
      }
   }

   public void createAppDeploymentMBeanForInternalApp(ApplicationMBean var1, String var2, DomainMBean var3, InternalApp var4) throws InvalidAttributeValueException, ManagementException {
      Object var5;
      if (var4.isLib) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Creating LibraryMBean for internal app: " + var2);
         }

         var5 = var3.createInternalLibrary(var1.getName(), var2);
      } else {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Creating AppDeploymentMBean for internal app: " + var2);
         }

         var5 = var3.createInternalAppDeployment(var1.getName(), var2);
      }

      if (var4.isBackground) {
         ((AppDeploymentMBean)var5).setBackgroundDeployment(true);
      }

      ((AppDeploymentMBean)var5).setPersistenceEnabled(false);
      ((AppDeploymentMBean)var5).setDeploymentOrder(0);
      if (var4.isClustered()) {
         ((AppDeploymentMBean)var5).setTargets(this.getTargetsForClusteredInternalApp(var3));
      } else {
         ((AppDeploymentMBean)var5).setTargets(this.getTargetsForInternalApp(var3));
      }

      ((AppDeploymentMBean)var5).setStagingMode("nostage");
      ((AppDeploymentMBean)var5).setSecurityDDModel("DDOnly");
      ((AppDeploymentMBean)var5).setInternalApp(true);
      if (var4.onDemandContextPaths != null && var4.onDemandContextPaths.length > 0 && var3.isInternalAppsDeployOnDemandEnabled()) {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Marking internal application for on-demand deployment: " + var4.name + " with refresh page boolean: " + var4.onDemandDisplayRefresh);
         }

         ((AppDeploymentMBean)var5).setOnDemandContextPaths(var4.onDemandContextPaths);
         ((AppDeploymentMBean)var5).setOnDemandDisplayRefresh(var4.onDemandDisplayRefresh);
         ((AppDeploymentMBean)var5).setBackgroundDeployment(true);
      }

      ((AppDeploymentMBean)var5).setDeploymentPlanDescriptor(var4.getDeploymentPlanBean());
   }

   private TargetMBean[] getTargetsForInternalApp(DomainMBean var1) {
      ServerMBean var2 = var1.lookupServer(this.serverName);
      return new TargetMBean[]{var2};
   }

   private TargetMBean[] getTargetsForClusteredInternalApp(DomainMBean var1) {
      ServerMBean var2 = var1.lookupServer(this.serverName);
      ClusterMBean var3 = var2.getCluster();
      return var3 != null ? new TargetMBean[]{var3} : new TargetMBean[]{var2};
   }

   private void initStagingDir() {
      this.serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      String var1 = DomainDir.getTempDirForServer(this.serverName) + File.separatorChar + ".internal";

      try {
         this.stageDir = (new File(var1)).getCanonicalFile();
      } catch (IOException var3) {
         throw new AssertionError(var3);
      }
   }

   private void initConsoleDeploymentPlan(InternalApp var1, DomainMBean var2) {
      AdminConsoleMBean var3 = var2.getAdminConsole();
      if (var3 != null) {
         boolean var4 = var3.isSet("CookieName");
         boolean var5 = var3.isSet("SessionTimeout");
         if (var4 || var5) {
            EditableDescriptorManager var6 = new EditableDescriptorManager();
            DeploymentPlanBean var7 = (DeploymentPlanBean)var6.createDescriptorRoot(DeploymentPlanBean.class, "UTF-8").getRootBean();
            var7.setApplicationName("consoleapp");
            VariableDefinitionBean var8 = var7.getVariableDefinition();
            VariableBean var9;
            if (var4) {
               var9 = var8.createVariable();
               var9.setName("CookieName");
               var9.setValue(var3.getCookieName());
            }

            if (var5) {
               var9 = var8.createVariable();
               var9.setName("SessionTimeout");
               var9.setValue("" + var3.getSessionTimeout());
            }

            ModuleOverrideBean var12 = var7.createModuleOverride();
            var12.setModuleName("webapp");
            var12.setModuleType("war");
            ModuleDescriptorBean var10 = var12.createModuleDescriptor();
            var10.setRootElement("weblogic-web-app");
            var10.setUri("WEB-INF/weblogic.xml");
            VariableAssignmentBean var11;
            if (var4) {
               var11 = var10.createVariableAssignment();
               var11.setName("CookieName");
               var11.setXpath("/weblogic-web-app/session-descriptor/cookie-name");
            }

            if (var5) {
               var11 = var10.createVariableAssignment();
               var11.setName("SessionTimeout");
               var11.setXpath("/weblogic-web-app/session-descriptor/timeout-secs");
            }

            var1.setDeploymentPlanBean(var7);
         }
      }
   }

   static {
      LIB = Home.getPath() + File.separator + "lib";
      WEBLOGIC_JAR_PATH = LIB + File.separator + "weblogic.jar";
      done = false;
      m_isWSATDeployed = new Boolean(System.getProperty("weblogic.wsee.wstx.wsat.deployed", "true"));
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private final class InternalApp {
      private final String name;
      private final String suffix;
      private final boolean adminOnly;
      private final boolean critical;
      private final String location;
      private final boolean isLib;
      private boolean isBackground;
      private String[] onDemandContextPaths;
      private boolean onDemandDisplayRefresh;
      private DeploymentPlanBean deploymentPlanBean;
      private boolean clustered;

      private InternalApp(String var2, String var3, boolean var4, boolean var5) {
         this(var2, var3, var4, var5, false, false);
      }

      private InternalApp(String var2, String var3, boolean var4, boolean var5, boolean var6) {
         this(var2, var3, var4, var5, false, var6);
      }

      private InternalApp(String var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7) {
         this(var2, var3, var4, var5, var6, var7, (String[])null, false);
      }

      private InternalApp(String var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7, String[] var8, boolean var9) {
         this.name = var2;
         this.suffix = var3;
         this.adminOnly = var4;
         this.critical = var5;
         this.location = InternalAppProcessor.LIB;
         this.isLib = var6;
         this.isBackground = var7;
         this.onDemandContextPaths = var8;
         this.onDemandDisplayRefresh = var9;
      }

      private DeploymentPlanBean getDeploymentPlanBean() {
         return this.deploymentPlanBean;
      }

      private void setDeploymentPlanBean(DeploymentPlanBean var1) {
         this.deploymentPlanBean = var1;
      }

      private boolean isClustered() {
         return this.clustered;
      }

      private void setClustered(boolean var1) {
         this.clustered = var1;
      }

      // $FF: synthetic method
      InternalApp(String var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7, String[] var8, boolean var9, Object var10) {
         this(var2, var3, var4, var5, var6, var7, var8, var9);
      }

      // $FF: synthetic method
      InternalApp(String var2, String var3, boolean var4, boolean var5, Object var6) {
         this(var2, var3, var4, var5);
      }

      // $FF: synthetic method
      InternalApp(String var2, String var3, boolean var4, boolean var5, boolean var6, Object var7) {
         this(var2, var3, var4, var5, var6);
      }

      // $FF: synthetic method
      InternalApp(String var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7, Object var8) {
         this(var2, var3, var4, var5, var6, var7);
      }
   }
}
