package weblogic.management.deploy.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import javax.management.InvalidAttributeValueException;
import weblogic.Home;
import weblogic.application.utils.EarUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.tools.Inspector;
import weblogic.deploy.api.tools.ModuleInfo;
import weblogic.deploy.common.Debug;
import weblogic.management.DistributedManagementException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.configuration.JDBCPoolComponentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.http.HttpParsing;

public class ApplicationMigrationProcessor implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      ApplicationMBean[] var2 = var1.getApplications();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ApplicationMBean var4 = var2[var3];
         this.migrateStagingDirectoryIfNeeded(var4, var1);
         this.migrateAppIfLoadedFromOldAppsDir(var4, var1);
         this.createAppDeploymentForApplication(var1, var4);
         var1.destroyApplication(var4);
      }

      File var8 = getOldAppPollerDir(var1);
      printFileList(var8);
      String[] var9 = var8.list();
      if (var9 != null && var9.length > 0) {
         File var5 = new File(DomainDir.getAppPollerDir());

         try {
            FileUtils.copy(var8, var5);
         } catch (IOException var7) {
            throw new UpdateException(var7);
         }
      }

      FileUtils.remove(var8);
      this.migrateDefaultWebApps(var1);
   }

   private void migrateStagingDirectoryIfNeeded(ApplicationMBean var1, DomainMBean var2) throws UpdateException {
      if (isDebugEnabled()) {
         debugSay(" +++ Migrating Staging directory ...");
      }

      if (var1.getStagingMode() == "external_stage") {
         List var3 = this.getRealTargets(var1, var2);
         if (isDebugEnabled()) {
            debugSay(" +++ Collected Targets : " + var3);
         }

         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            TargetMBean var5 = (TargetMBean)var4.next();
            this.migrateStagingDirectoryIfNeededForTarget(var1, var5);
         }

         if (isDebugEnabled()) {
            debugSay(" +++ Migration of Staging directory completed");
         }

      }
   }

   private void migrateStagingDirectoryIfNeededForTarget(ApplicationMBean var1, TargetMBean var2) throws UpdateException {
      if (var2 instanceof ServerMBean) {
         ServerMBean var3 = (ServerMBean)var2;
         String var4 = var3.get81StyleDefaultStagingDirName();
         if (var4 == null) {
            return;
         }

         String var5 = var4 + File.separatorChar + var1.getName();
         if (isDebugEnabled()) {
            debugSay(" +++ oldDefaultStagingDirName : " + var5);
         }

         File var6 = new File(var5);
         if (!var6.exists()) {
            return;
         }

         String var7 = var3.getDefaultStagingDirName();
         if (var7 == null) {
            return;
         }

         String var8 = var7 + File.separatorChar + var1.getName();
         if (isDebugEnabled()) {
            debugSay(" +++ newDefaultStagingDir : " + var8);
         }

         String var9 = var3.getStagingDirectoryName();
         if (var9 == null) {
            return;
         }

         String var10 = var9 + File.separatorChar + var1.getName();
         if (isDebugEnabled()) {
            debugSay(" +++ appStagingDir : " + var10);
         }

         if (!var8.equals(var10)) {
            return;
         }

         try {
            FileUtils.copyPreserveTimestamps(var6, new File(var8));
            if (isDebugEnabled()) {
               debugSay(" +++ copied oldDefaultStagingDir(" + var6 + ") to newDefaultStagingDir(" + var8 + ")");
            }

            FileUtils.remove(var6);
            if (isDebugEnabled()) {
               debugSay(" +++ deleted oldDefaultStagingDir(" + var6 + ")");
            }
         } catch (IOException var12) {
            throw new UpdateException(var12);
         }
      }

   }

   private List getRealTargets(ApplicationMBean var1, DomainMBean var2) {
      Object var3 = new ArrayList();
      ComponentMBean[] var4 = var1.getComponents();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            List var6 = this.getRealTargets(var4[var5], var2);
            if (((List)var3).isEmpty()) {
               var3 = var6;
            } else {
               for(Iterator var7 = var6.iterator(); var7.hasNext(); var3 = this.addTargetToListIfNeeded((TargetMBean)var7.next(), (List)var3)) {
               }
            }
         }
      }

      return (List)var3;
   }

   private List getRealTargets(ComponentMBean var1, DomainMBean var2) {
      Object var3 = new ArrayList();
      int var5;
      if (var1 instanceof WebAppComponentMBean) {
         VirtualHostMBean[] var4 = ((WebAppComponentMBean)var1).getVirtualHosts();
         if (var4 != null) {
            for(var5 = 0; var5 < var4.length; ++var5) {
               Set var6 = var4[var5].getServerNames();

               ServerMBean var8;
               for(Iterator var7 = var6.iterator(); var7.hasNext(); var3 = this.addTargetToListIfNeeded(var8, (List)var3)) {
                  var8 = var2.lookupServer((String)var7.next());
               }
            }
         }
      }

      TargetMBean[] var9 = var1.getTargets();
      if (var9 != null) {
         if (((List)var3).isEmpty()) {
            List var10 = Arrays.asList(var9);
            ((List)var3).addAll(var10);
            if (isDebugEnabled()) {
               debugSay(" +++ Added targets : " + var10);
            }
         } else {
            for(var5 = 0; var5 < var9.length; ++var5) {
               var3 = this.addTargetToListIfNeeded(var9[var5], (List)var3);
            }
         }
      }

      return (List)var3;
   }

   private List addTargetToListIfNeeded(TargetMBean var1, List var2) {
      if (var1 != null) {
         if (var1 instanceof ClusterMBean) {
            ClusterMBean var3 = (ClusterMBean)var1;
            ServerMBean[] var4 = var3.getServers();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               var2 = this.addTargetToListIfNeeded(var4[var5], var2);
            }

            return var2;
         }

         if (!var2.contains(var1)) {
            var2.add(var1);
            if (isDebugEnabled()) {
               debugSay(" +++ Added target : " + var1.getName());
            }
         }
      }

      return var2;
   }

   private void migrateDefaultWebApps(DomainMBean var1) {
      ServerMBean[] var2 = var1.getServers();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.migrateDefaultWebApps(var2[var3].getWebServer());
         }
      }

      VirtualHostMBean[] var5 = var1.getVirtualHosts();
      if (var5 != null) {
         for(int var4 = 0; var4 < var5.length; ++var4) {
            this.migrateDefaultWebApps((WebServerMBean)var5[var4]);
         }
      }

   }

   private void migrateDefaultWebApps(WebServerMBean var1) {
      if (!var1.isSet("DefaultWebAppContextRoot") && var1.isSet("DefaultWebApp") && var1.getDefaultWebApp() != null) {
         String var2 = this.findContextRoot(var1.getDefaultWebApp());
         if (var2 != null) {
            var1.setDefaultWebAppContextRoot(var2);
         }

      }
   }

   private String findContextRoot(WebAppComponentMBean var1) {
      return HttpParsing.ensureStartingSlash(var1.getName());
   }

   private AppDeploymentMBean createAppDeploymentForApplication(DomainMBean var1, ApplicationMBean var2) throws UpdateException {
      if (isDebugEnabled()) {
         debugSay("creating AppDeploymentMBean for " + var2);
      }

      boolean var4 = false;
      int var5 = var2.getInternalType();
      if (var5 == 4) {
         if ((new File(var2.getPath())).exists()) {
            DeployerRuntimeLogger.logUnknownAppType(var2.getName(), var2.getPath());
            return null;
         }

         if (!var2.getPath().endsWith(".ear") && var2.getComponents().length <= 1) {
            var5 = 1;
         } else {
            var5 = 0;
         }

         var4 = true;
      }

      AppDeploymentMBean var3 = var1.lookupAppDeployment(var2.getName());
      if (var3 != null) {
         String var6 = DeployerRuntimeLogger.appAlreadyExists(var2.getName());
         throw new UpdateException(var6);
      } else {
         var3 = this.createAppDeploymentMBean(var5, var2, var1);
         var2.setDelegationEnabled(false);

         try {
            this.setAppDeploymentTargets(var2, var3, var4);
            var3.setDeploymentOrder(var2.getLoadOrder());
            if (var2.getStagingMode() != null) {
               var3.setStagingMode(var2.getStagingMode());
            }

            var3.setInternalApp(false);
            var3.setSecurityDDModel("Advanced");
         } catch (Throwable var7) {
            DeployerRuntimeLogger.logApplicationUpgradeProblem(var2.getName(), var7);
            var1.destroyAppDeployment(var3);
         }

         var2.setDelegationEnabled(true);
         return var3;
      }
   }

   private AppDeploymentMBean createAppDeploymentMBean(int var1, ApplicationMBean var2, DomainMBean var3) {
      String var6 = null;
      String var5;
      if (var1 != 2 && var1 != 0) {
         ComponentMBean[] var7 = var2.getComponents();
         if (var7 != null && var7.length == 1) {
            String var8 = var2.getComponents()[0].getURI();
            if (!var8.equals("jms-xa-adp.rar") && !var8.equals("jms-local-adp.rar") && !var8.equals("jms-notran-adp.rar") && !var8.equals("jms-notran-adp51.rar")) {
               var5 = var2.getPath() + File.separatorChar + var2.getComponents()[0].getURI();
            } else {
               Home.getHome();
               String var9 = Home.getPath();
               var5 = var9 + File.separatorChar + "lib" + File.separatorChar + var2.getComponents()[0].getURI();
            }

            var6 = this.getTypeFromComp(var2.getComponents()[0]);
         } else {
            DeployerRuntimeLogger.getOldActivateLoggable(var2.getName()).log();
            var5 = var2.getPath();
         }
      } else {
         var5 = var2.getPath();
         var6 = ModuleType.EAR.toString();
      }

      AppDeploymentMBean var4 = var3.createAppDeployment(var2.getName(), var5);
      var4.setModuleType(var6);
      return var4;
   }

   private void setAppDeploymentTargets(ApplicationMBean var1, AppDeploymentMBean var2, boolean var3) throws DistributedManagementException, InvalidAttributeValueException, UpdateException {
      ComponentMBean[] var4 = var1.getComponents();
      if (var4 != null) {
         if (var4.length == 1) {
            var2.setTargets(var4[0].getTargets());
            String var5 = var4[0].getName();
            if (!var5.equals(var2.getName())) {
               var2.setCompatibilityName(var5);
               var2.setModuleType(this.getTypeFromComp(var4[0]));
            }
         } else {
            this.setAppDeploymentTargetsForEAR(var1, var2, var3);
         }

      }
   }

   private void setAppDeploymentTargetsForEAR(ApplicationMBean var1, AppDeploymentMBean var2, boolean var3) throws InvalidAttributeValueException, DistributedManagementException, UpdateException {
      HashMap var4 = new HashMap();
      Inspector var5 = null;
      ModuleInfo[] var6 = null;
      if (!var3) {
         try {
            var5 = new Inspector(new File(var1.getPath()));
            var6 = var5.getModuleInfo().getSubModules();
         } catch (Throwable var14) {
            if (var5 != null) {
               var5.close();
            }

            throw new UpdateException(var14.getMessage());
         }
      }

      boolean var7 = var3 || this.hasAllModules(var1, var6);
      ComponentMBean[] var8 = var1.getComponents();

      for(int var9 = 0; var8 != null && var9 < var8.length; ++var9) {
         TargetMBean[] var10 = this.getTargetsAndVirtualHosts(var8[var9]);

         for(int var11 = 0; var11 < var10.length; ++var11) {
            String var12 = this.getNameForSubDeployment(var8[var9], var6, var3);
            SubDeploymentMBean var13 = var2.lookupSubDeployment(var12);
            if (var13 == null) {
               var13 = var2.createSubDeployment(var12);
            }

            var13.setCompatibilityName(var8[var9].getName());
            var13.setModuleType(this.getTypeFromComp(var8[var9]));
            if (var7 && this.isGlobalTarget(var8, var10[var11], var4)) {
               this.addTarget(var2, var10[var11]);
            } else {
               this.addTarget(var13, var10[var11]);
            }
         }
      }

      if (var5 != null) {
         var5.close();
      }

   }

   private void addTarget(TargetInfoMBean var1, TargetMBean var2) throws DistributedManagementException, InvalidAttributeValueException {
      if (var1.getTargets() != null && var1.getTargets().length != 0) {
         TargetMBean[] var3 = var1.getTargets();

         for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
            if (var3[var4].getName().equals(var2.getName())) {
               return;
            }
         }

         var1.addTarget(var2);
      } else {
         var1.addTarget(var2);
      }

   }

   private boolean hasAllModules(ApplicationMBean var1, ModuleInfo[] var2) {
      return var2.length == var1.getComponents().length;
   }

   private String getNameForSubDeployment(ComponentMBean var1, ModuleInfo[] var2, boolean var3) {
      if (var1 instanceof WebAppComponentMBean) {
         String var4 = this.getContextRoot(var1, var2, var3);
         if (var4 != null & !"".equals(var4) & !"/".equals(var4)) {
            return var4;
         }
      }

      return var1.getURI() == null ? var1.getName() : var1.getURI();
   }

   private String getContextRoot(ComponentMBean var1, ModuleInfo[] var2, boolean var3) {
      if (var3) {
         return null;
      } else {
         String var4 = null;
         if (var2.length == 0) {
            throw new AssertionError("EAR has no modules.");
         } else {
            for(int var5 = 0; var2 != null && var5 < var2.length; ++var5) {
               if (var2[var5].getName().equals(var1.getURI())) {
                  String[] var6 = var2[var5].getContextRoots();
                  if (var6 != null && var6.length > 0) {
                     if (var6.length == 1) {
                        var4 = EarUtils.fixAppContextRoot(var6[0]);
                     } else {
                        var4 = var1.getName();
                     }
                  } else {
                     var4 = null;
                  }
               }
            }

            return var4;
         }
      }
   }

   private String getTypeFromComp(ComponentMBean var1) {
      if (var1 instanceof WebAppComponentMBean) {
         return ModuleType.WAR.toString();
      } else if (var1 instanceof EJBComponentMBean) {
         return ModuleType.EJB.toString();
      } else if (var1 instanceof ConnectorComponentMBean) {
         return ModuleType.RAR.toString();
      } else {
         return var1 instanceof JDBCPoolComponentMBean ? WebLogicModuleType.JDBC.toString() : "unknown";
      }
   }

   private TargetMBean[] getTargetsAndVirtualHosts(ComponentMBean var1) {
      ArrayList var2 = new ArrayList();
      TargetMBean[] var3 = var1.getTargets();
      if (var3 != null) {
         var2.addAll(Arrays.asList(var3));
      }

      if (var1 instanceof WebAppComponentMBean) {
         VirtualHostMBean[] var4 = ((WebAppComponentMBean)var1).getVirtualHosts();
         if (var4 != null) {
            var2.addAll(Arrays.asList(var4));
         }
      }

      return (TargetMBean[])((TargetMBean[])var2.toArray(new TargetMBean[var2.size()]));
   }

   private boolean isGlobalTarget(ComponentMBean[] var1, TargetMBean var2, Map var3) {
      for(int var4 = 0; var4 < var1.length; ++var4) {
         Set var5 = this.getOrCreateTargetList(var1[var4], var3);
         if (!var5.contains(var2.getName())) {
            return false;
         }
      }

      return true;
   }

   private Set getOrCreateTargetList(ComponentMBean var1, Map var2) {
      Object var3 = (Set)var2.get(var1.getName() + var1.getType());
      if (var3 == null) {
         var3 = new HashSet();
         TargetMBean[] var4 = this.getTargetsAndVirtualHosts(var1);

         for(int var5 = 0; var5 < var4.length; ++var5) {
            ((Set)var3).add(var4[var5].getName());
         }

         var2.put(var1.getName() + var1.getType(), var3);
      }

      return (Set)var3;
   }

   private boolean ensureNewAppPollerDirExists(File var1) throws UpdateException {
      boolean var2 = true;
      if (!var1.exists()) {
         var2 = var1.mkdir();
      }

      if (!var2) {
         String var3 = ApplicationPollerLogger.logCouldnotCreateAutodeployDirLoggable(var1.toString()).getMessage();
         throw new UpdateException(var3);
      } else {
         return var2;
      }
   }

   private void migrateAppIfLoadedFromOldAppsDir(ApplicationMBean var1, DomainMBean var2) throws UpdateException {
      String var3 = var1.getPath();
      File var4 = new File(var3);
      File var5 = new File(DomainDir.getAppPollerDir());
      String var6 = var5.getAbsolutePath();
      boolean var7 = this.ensureNewAppPollerDirExists(var5);
      if (var7) {
         File var8 = getOldAppPollerDir(var2);
         String var9 = var8.getAbsolutePath();
         if (var8.exists()) {
            try {
               if (!var4.isAbsolute()) {
                  var4 = new File(DomainDir.getRootDir(), var3);
               }

               var4 = var4.getCanonicalFile();
               var3 = var4.getCanonicalPath();
               var8 = var8.getCanonicalFile();
               var9 = var8.getCanonicalPath();
            } catch (IOException var17) {
            }

            String var11;
            try {
               var5 = var5.getCanonicalFile();
               var6 = var5.getCanonicalPath();
               if (isLoadedFromOldAppsDir(var4, var2)) {
                  String var10 = var6;
                  if (!var3.equals(var9)) {
                     var10 = StringUtils.replaceGlobal(var3, var9, var6);
                  }

                  if (!var3.equals(var9)) {
                     File var22 = new File(var10);
                     FileUtils.copy(var4, var22);
                     if (isDebugEnabled()) {
                        debugSay(" +++ copied " + var4 + " to " + var22);
                     }

                     boolean var23 = FileUtils.remove(var4);
                     if (isDebugEnabled()) {
                        debugSay(" +++ Removed " + var4 + " :: " + var23);
                     }
                  } else {
                     ComponentMBean[] var21 = var1.getComponents();

                     for(int var12 = 0; var12 < var21.length; ++var12) {
                        String var13 = var3 + File.separatorChar + var21[var12].getURI();
                        File var14 = new File(var13);
                        File var15 = new File(var5, var21[var12].getURI());
                        FileUtils.copy(var14, var15);
                        if (isDebugEnabled()) {
                           debugSay(" +++ copied component " + var14 + " to " + var15);
                        }

                        boolean var16 = FileUtils.remove(var14);
                        if (isDebugEnabled()) {
                           debugSay(" +++ Removed " + var14 + " :: " + var16);
                        }
                     }
                  }

                  if (isDebugEnabled()) {
                     debugSay(" +++ Application migrated to new autodeploy directory: " + var10);
                  }

                  var1.setPath(var10);
                  ApplicationPollerLogger.logApplicationMigrated(var1.getName(), var10);
               }

            } catch (IOException var18) {
               var11 = ApplicationPollerLogger.logIOExceptionLoggable(var18).getMessage();
               throw new UpdateException(var11);
            } catch (ManagementException var19) {
               var11 = ApplicationPollerLogger.logExceptionWhileMigratingLoggable(var19).getMessage();
               throw new UpdateException(var11);
            } catch (InvalidAttributeValueException var20) {
               var11 = ApplicationPollerLogger.logExceptionWhileMigratingLoggable(var20).getMessage();
               throw new UpdateException(var11);
            }
         }
      }
   }

   public static boolean isLoadedFromOldAppsDir(File var0, DomainMBean var1) {
      File var2 = getOldAppPollerDir(var1);
      String var3 = var2.getName();

      try {
         var2 = var2.getCanonicalFile();
         var3 = var2.getCanonicalPath();
      } catch (IOException var7) {
         ApplicationPollerLogger.logIOException(var7);
      }

      String var4 = var0.getAbsolutePath();

      try {
         var0 = var0.getCanonicalFile();
         var4 = var0.getCanonicalPath();
      } catch (IOException var6) {
         ApplicationPollerLogger.logIOException(var6);
      }

      return var4.indexOf(var3) > -1;
   }

   private static void debugSay(String var0) {
      Debug.deploymentDebug(var0);
   }

   private static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   private static void printFileList(File var0) {
      if (isDebugEnabled()) {
         String[] var1 = var0.list();
         StringBuffer var2 = new StringBuffer();
         var2.append("ls ").append(var0.toString()).append(" - ");
         if (var1 == null) {
            var2.append(" nothing to list since file doesn't exist");
         } else {
            var2.append("" + var1.length).append(" : ");

            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var3 != 0) {
                  var2.append(", ");
               }

               var2.append(var1[var3]);
            }
         }

         Debug.deploymentDebug(var2.toString());
      }

   }

   private static File getOldAppPollerDir(DomainMBean var0) {
      File var1 = null;
      if (var0.getConfigurationVersion().startsWith("6")) {
         String var2 = DomainDir.getRootDir() + File.separator + "config" + File.separator + var0.getName() + File.separator + "applications";
         var1 = new File(var2);
      } else {
         var1 = new File(DomainDir.getOldAppPollerDir());
      }

      return var1;
   }
}
