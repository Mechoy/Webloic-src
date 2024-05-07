package weblogic.deploy.internal.adminserver.operations;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.ApplicationFileManager;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DeploymentConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeployerRuntimeTextTextFormatter;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.ApplicationRuntimeState;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.RetirementManager;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.FileUtils;

public class OperationHelper {
   private static final AppRuntimeStateManager appRTStateMgr = AppRuntimeStateManager.getManager();
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static DeployerRuntimeTextTextFormatter textformatter = new DeployerRuntimeTextTextFormatter();

   static boolean isLibrary(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null && var0.getDeploymentOptions().isLibrary();
   }

   static String getAppName(String var0, DeploymentData var1, String var2) throws ManagementException {
      return isLibrary(var1) ? getAndValidateLibraryName(var0, var1, var2) : ensureAppName(var0);
   }

   private static String getAndValidateLibraryName(String var0, DeploymentData var1, String var2) throws ManagementException {
      String var3 = ensureAppName(var0);
      String var4 = getLibNameFromSource(var0, var2);
      if (var3 != null && !var3.equals("")) {
         if (var4 != null && !var4.equals("")) {
            if (!var4.equals(var3)) {
               Loggable var5 = DeployerRuntimeLogger.logLibNameMismatchLoggable(var3, var4, var2);
               var5.log();
               throw new ManagementException(var5.getMessage());
            } else {
               if (isDebugEnabled()) {
                  debug("lib name = <" + var3 + ">");
               }

               return var3;
            }
         } else {
            return var3;
         }
      } else {
         return var4 != null && !var4.equals("") ? var4 : getDefaultLibNameFromSource(var2, var1);
      }
   }

   private static String getLibNameFromSource(String var0, String var1) throws ManagementException {
      validateSource(var0, var1);
      return ApplicationVersionUtils.getLibName(var1);
   }

   private static String getDefaultLibNameFromSource(String var0, DeploymentData var1) throws ManagementException {
      try {
         String var2 = (new File(var0)).getCanonicalFile().getName();
         int var5 = var2.indexOf(46);
         if (var5 != -1) {
            var2 = var2.substring(0, var5);
         }

         var1.setIsNameFromSource(true);
         return var2;
      } catch (IOException var4) {
         Loggable var3 = DeployerRuntimeLogger.logNoLibNameLoggable(var0);
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   private static String getArchiveVersionIdFromData(DeploymentData var0, String var1) {
      String var2 = null;
      if (var0 != null && var0.getDeploymentOptions() != null) {
         String var3 = var0.getDeploymentOptions().getLibSpecVersion();
         String var4 = var0.getDeploymentOptions().getLibImplVersion();
         if (!isLibrary(var0) || var3 == null && var4 == null) {
            var2 = var0.getDeploymentOptions().getArchiveVersion();
         } else {
            var2 = ApplicationVersionUtils.getLibVersionId(var3, var4);
         }
      }

      return var2 == null ? ApplicationVersionUtils.getVersionId(var1) : var2;
   }

   static String getVersionIdFromData(DeploymentData var0, String var1) throws ManagementException {
      return getCompositeVersionId(var1, getArchiveVersionIdFromData(var0, var1), var0);
   }

   private static String getArchiveVersionIdFromSource(String var0, DeploymentData var1, String var2) throws ManagementException {
      validateSource(var2, var0);
      return isLibrary(var1) ? ApplicationVersionUtils.getLibVersionId(var0) : ApplicationVersionUtils.getManifestVersion(var0);
   }

   private static String getCompositeVersionId(String var0, String var1, DeploymentData var2) throws ManagementException {
      String var3 = null;
      var3 = ApplicationVersionUtils.getPlanVersion(var1);
      if (var3 != null) {
         return var1;
      } else {
         if (var2 != null && var2.getDeploymentOptions() != null) {
            var3 = var2.getDeploymentOptions().getPlanVersion();
         }

         if (var1 == null && var3 != null) {
            Loggable var4 = DeployerRuntimeLogger.logPlanVersionNotAllowedLoggable(var0, var3);
            var4.log();
            throw new ManagementException(var4.getMessage());
         } else {
            return ApplicationVersionUtils.getVersionId(var1, var3);
         }
      }
   }

   static String getAndValidateVersionIdWithSrc(DeploymentData var0, String var1, String var2, String var3) throws ManagementException {
      String var4 = getArchiveVersionIdFromData(var0, var1);
      String var5 = null;
      if (var2 != null) {
         String var6 = getArchiveVersionIdFromSource(var2, var0, var3);
         if (var4 == null) {
            var5 = getCompositeVersionId(var3, var6, var0);
         } else {
            String var7 = ApplicationVersionUtils.getLibSpecVersion(var4);
            String var8 = ApplicationVersionUtils.getLibSpecVersion(var6);
            String var9 = ApplicationVersionUtils.getLibImplVersion(var4);
            String var10 = ApplicationVersionUtils.getLibImplVersion(var6);
            if (var8 != null && var7 != null && !var7.equals(var8) || var10 != null && var9 != null && !var9.equals(var10)) {
               Loggable var12 = logAppVersionMismatchLoggable(var3, var0, var4, var6);
               var12.log();
               throw new ManagementException(var12.getMessage());
            }

            String var11;
            if (var9 == null && var10 == null) {
               var11 = var8 == null ? var7 : var8;
            } else {
               var11 = ApplicationVersionUtils.getLibVersionId(var8 == null ? var7 : var8, var10 == null ? var9 : var10);
            }

            var5 = getCompositeVersionId(var3, var11, var0);
         }
      }

      if (var5 != null) {
         Debug.deploymentLogger.debug("new app version = <" + var5 + ">");
      }

      return var5;
   }

   private static Loggable logAppVersionMismatchLoggable(String var0, DeploymentData var1, String var2, String var3) {
      if (isLibrary(var1)) {
         String var4 = ApplicationVersionUtils.getLibSpecVersion(var2);
         var4 = var4 == null ? "" : var4;
         String var5 = ApplicationVersionUtils.getLibImplVersion(var2);
         var5 = var5 == null ? "" : var5;
         String var6 = ApplicationVersionUtils.getLibSpecVersion(var3);
         var6 = var6 == null ? "" : var6;
         String var7 = ApplicationVersionUtils.getLibImplVersion(var3);
         var7 = var7 == null ? "" : var7;
         return DeployerRuntimeLogger.logLibVersionMismatchLoggable(var0, var4, var5, var6, var7);
      } else {
         return DeployerRuntimeLogger.logAppVersionMismatchLoggable(var0, var2, var3);
      }
   }

   static boolean undeployAllVersions(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null && var0.getDeploymentOptions().isUndeployAllVersions();
   }

   static AppDeploymentMBean getActiveVersionIfNeeded(DomainMBean var0, String var1, AppDeploymentMBean var2, String var3, DeploymentData var4, String var5) throws ManagementException {
      AppDeploymentMBean var6 = var2;
      if (var1 == null && var2.getVersionIdentifier() != null) {
         var6 = ApplicationVersionUtils.getActiveAppDeployment(var0, var3, isAdminMode(var4));
         if (undeployAllVersions(var4)) {
            if (var6 != null) {
               DeployerRuntimeLogger.logRemoveAllActiveAppVersion(var5, "allversions", var3, var6.getVersionIdentifier());
            }
         } else {
            if (var6 == null) {
               Loggable var7 = DeployerRuntimeLogger.logNoActiveAppLoggable(var3, var5);
               var7.log();
               throw new ManagementException(var7.getMessage());
            }

            DeployerRuntimeLogger.logActiveAppVersionWarning(var3, var6.getVersionIdentifier(), var5);
         }
      }

      return var6;
   }

   static AppDeploymentMBean getActiveVersion(DomainMBean var0, AppDeploymentMBean var1, DeploymentData var2) throws ManagementException {
      return var1 != null && var1.getVersionIdentifier() == null ? ApplicationVersionUtils.getActiveAppDeployment(var0, var1.getApplicationName(), isAdminMode(var2)) : null;
   }

   private static void validateSource(String var0, String var1) throws ManagementException {
      if (var1 != null && !var1.equals("")) {
         if (!(new File(var1)).exists()) {
            Loggable var2 = DeployerRuntimeLogger.logInvalidSourceLoggable(var1, var0, "No application files exist");
            var2.log();
            throw new ManagementException(var2.getMessage());
         }
      }
   }

   static void validateVersionForDeprecatedOp(DeploymentData var0, String var1, String var2, int var3) throws ManagementException {
      String var4 = getArchiveVersionIdFromData(var0, var1);
      if (var4 != null) {
         Loggable var5 = DeployerRuntimeLogger.logVersionNotAllowedForDeprecatedOpLoggable(ApplicationVersionUtils.getDisplayName(ensureAppName(var1), var4), var2, getTaskString(var3));
         var5.log();
         throw new ManagementException(var5.getMessage());
      }
   }

   static void validateMaxAppVersions(DomainMBean var0, String var1, String var2) throws ManagementException {
      if (var2 != null && !var2.equals("")) {
         AppDeploymentMBean[] var3 = ApplicationVersionUtils.getAppDeployments(var0, var1, true);
         DeploymentConfigurationMBean var4 = var0.getDeploymentConfiguration();
         if (var4 == null) {
            Loggable var9 = DeployerRuntimeLogger.logFailedToObtainConfigLoggable();
            var9.log();
            throw new ManagementException(var9.getMessage());
         } else {
            int var5 = var4.getMaxAppVersions();
            if (var3 != null && var3.length >= var5) {
               StringBuffer var6 = new StringBuffer();

               for(int var7 = 0; var7 < var3.length; ++var7) {
                  String var8 = var3[var7].getVersionIdentifier();
                  if (var2.equals(var8)) {
                     return;
                  }

                  var6.append(var8);
                  if (var7 != var3.length - 1) {
                     var6.append(", ");
                  }
               }

               Loggable var10 = DeployerRuntimeLogger.logMaxAppVersionsExceededLoggable(ApplicationVersionUtils.getDisplayName(var1, var2), var1, var5, var6.toString());
               var10.log();
               throw new ManagementException(var10.getMessage());
            }
         }
      }
   }

   static void validateVersionIdFormat(String var0, String var1) throws ManagementException {
      if (var1 != null && !var1.equals("")) {
         Loggable var2;
         if (var1.length() > 215) {
            var2 = DeployerRuntimeLogger.logVersionIdLengthExceededLoggable(var0, ApplicationVersionUtils.getDisplayVersionId(var1), 215);
            var2.log();
            throw new ManagementException(var2.getMessage());
         } else if (!ApplicationVersionUtils.isVersionIdValid(var1)) {
            var2 = DeployerRuntimeLogger.logInvalidVersionIdLoggable(var0, ApplicationVersionUtils.getDisplayVersionId(var1));
            var2.log();
            throw new ManagementException(var2.getMessage());
         }
      }
   }

   static void validateSourceVersion(AppDeploymentMBean var0, DeploymentData var1, String var2) throws ManagementException {
      String var3 = ApplicationVersionUtils.getArchiveVersion(var0);
      String var4 = getArchiveVersionIdFromSource(var0.getAbsoluteSourcePath(), var1, var2);
      if (var3 != null && !var3.equals(var4)) {
         Loggable var5 = DeployerRuntimeLogger.logAppVersionMismatch2Loggable(var0.getApplicationName(), var3, var4);
         var5.log();
         throw new ManagementException(var5.getMessage());
      }
   }

   static void validateNonVersionWithVersion(String var0, AppDeploymentMBean var1, String var2, String var3) throws ManagementException {
      if (var0 == null && var1.getVersionIdentifier() != null) {
         Loggable var4 = DeployerRuntimeLogger.logInvalidAppVersion2Loggable(var2, var1.getVersionIdentifier(), var3);
         var4.log();
         throw new ManagementException(var4.getMessage());
      }
   }

   static void validateVersionWithNonVersion(DomainMBean var0, String var1, String var2, String var3) throws ManagementException {
      if (var1 != null) {
         AppDeploymentMBean var4 = ApplicationVersionUtils.getAppDeployment(var0, var2, (String)null);
         if (var4 != null && var4.getVersionIdentifier() == null) {
            Loggable var5 = DeployerRuntimeLogger.logInvalidAppVersionLoggable(var2, var1, var3);
            var5.log();
            throw new ManagementException(var5.getMessage());
         }
      }

   }

   static void validateRetireTimeout(DomainMBean var0, String var1, String var2, DeploymentData var3) throws ManagementException {
      if (!var3.getDeploymentOptions().isRetireGracefully() && var3.getDeploymentOptions().getRetireTime() > -1) {
         AppDeploymentMBean var4 = ApplicationVersionUtils.getAppDeployment(var0, var1, (String)null);
         if (var4 == null) {
            throw new ManagementException(DeployerRuntimeLogger.invalidRetireTimeout(var1, var2, var3.getDeploymentOptions().getRetireTime()));
         }
      }

   }

   static void validateVersionStagingAndPath(DomainMBean var0, String var1, String var2, String var3) throws ManagementException {
      if (var2 != null && var3 != null) {
         AppDeploymentMBean[] var4 = ApplicationVersionUtils.getAppDeployments(var0, var2);
         if (var4 != null) {
            try {
               String var5 = (new File(var1)).getCanonicalPath();

               for(int var9 = 0; var9 < var4.length; ++var9) {
                  if ("nostage".equals(var4[var9].getStagingMode()) && isSameCanonicalPaths(var5, var4[var9])) {
                     Loggable var7 = DeployerRuntimeLogger.logInvalidNewSource2Loggable(var2, var3, var5, var4[var9].getVersionIdentifier());
                     var7.log();
                     throw new ManagementException(var7.getMessage());
                  }
               }

            } catch (IOException var8) {
               Loggable var6 = DeployerRuntimeLogger.logInvalidSourceLoggable(var1, ApplicationVersionUtils.getDisplayName(var2, var3), var8.getMessage());
               var6.log();
               throw new ManagementException(var6.getMessage(), var8);
            }
         }
      }
   }

   static void validateVersionTargets(DomainMBean var0, String var1, DeploymentData var2, String var3) throws ManagementException {
      if (var3 != null && var2 != null && var2.hasTargets()) {
         AppDeploymentMBean var4 = ApplicationVersionUtils.getAppDeployment(var0, var1, (String)null);
         if (var4 != null && var4.getVersionIdentifier() != null) {
            Set var5 = TargetHelper.getAllTargetedServers(var4);

            Set var6;
            try {
               var6 = var2.getAllTargetedServers(var2.getAllLogicalTargets(), var0);
            } catch (Throwable var8) {
               if (isDebugEnabled()) {
                  debug("Cannot obtain targeted servers for " + var1 + " from " + var2, var8);
               }

               return;
            }

            if (!var5.containsAll(var6) || !var6.containsAll(var5)) {
               Loggable var7 = DeployerRuntimeLogger.logInvalidTargetsForAppVersionLoggable(var1, var3, var5.toString(), var6.toString());
               var7.log();
               throw new ManagementException(var7.getMessage());
            }
         }

      }
   }

   static void validateTargets(DomainMBean var0, DeploymentData var1, AppDeploymentMBean var2, String var3) throws ManagementException {
      if (var1.hasTargets()) {
         checkForClusterTargetSubset(var0, var1, var2, var3);
         checkForTargetListSubset(var2, var1);
      }
   }

   static void checkForTargetListSubset(AppDeploymentMBean var0, DeploymentData var1) {
      Map var2 = createTargetMap(var0);
      if (var1.getGlobalTargets().length != 0) {
         Set var3 = (Set)var2.get(var0.getName());
         Set var4 = createTargetSet(var1.getGlobalTargets());
         verifyTargets(var0.getApplicationIdentifier(), var3, var4);
      }

      Set var5;
      Set var6;
      String var8;
      if (var1.hasModuleTargets()) {
         for(Iterator var7 = var1.getAllModuleTargets().keySet().iterator(); var7.hasNext(); verifyTargets(var8, var6, var5)) {
            var8 = (String)var7.next();
            var5 = createTargetSet((String[])((String[])var1.getAllModuleTargets().get(var8)));
            var6 = (Set)var2.get(var0.getName());
            if (var2.keySet().contains(var8)) {
               var6 = (Set)var2.get(var8);
            }
         }
      }

   }

   static void checkForClusterTargetSubset(DomainMBean var0, DeploymentData var1, AppDeploymentMBean var2, String var3) throws ManagementException {
      checkForClusterTargetSubset(var0, var1, var2, var3, false);
   }

   static void checkForClusterTargetSubset(DomainMBean var0, DeploymentData var1, AppDeploymentMBean var2, String var3, boolean var4) throws ManagementException {
      if (isAppHasClusterTargets(var2)) {
         String[] var5 = var1.getGlobalTargets();
         List var6 = getAppClusterTargets(var2);
         ArrayList var7 = new ArrayList();

         for(int var8 = 0; var8 < var5.length; ++var8) {
            var7.add(var5[var8]);
         }

         checkTargetsPartOfCluster(var0, var2.getApplicationIdentifier(), var6, var7, var3, var4);
      }

   }

   private static List getAppClusterTargets(AppDeploymentMBean var0) {
      ArrayList var1 = new ArrayList();
      TargetMBean[] var2 = var0.getTargets();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] instanceof ClusterMBean) {
               var1.add(var2[var3]);
            }
         }
      }

      return var1;
   }

   private static void checkTargetsPartOfCluster(DomainMBean var0, String var1, List var2, List var3, String var4, boolean var5) throws ManagementException {
      Iterator var6 = var3.iterator();

      while(true) {
         String var7;
         ServerMBean var8;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var7 = (String)var6.next();
            var8 = var0.lookupServer(var7);
         } while(var8 == null);

         Iterator var9 = var2.iterator();

         while(var9.hasNext()) {
            ClusterMBean var10 = (ClusterMBean)var9.next();
            Set var11 = var10.getServerNames();
            if (var11.contains(var7) && !var3.contains(var10.getName())) {
               if (!var5) {
                  Loggable var12 = DeployerRuntimeLogger.logInvalidIndividualTargetLoggable(var1, var10.getName(), var7, var4);
                  throw new ManagementException(var12.getMessage());
               }

               DeployerRuntimeLogger.logPartialClusterTarget(var1, var10.getName(), var7, var4);
            }
         }
      }
   }

   private static boolean isAppHasClusterTargets(AppDeploymentMBean var0) {
      TargetMBean[] var1 = var0.getTargets();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] instanceof ClusterMBean) {
               return true;
            }
         }
      }

      return false;
   }

   static Map createTargetMap(AppDeploymentMBean var0) {
      HashMap var1 = new HashMap();
      var1.put(var0.getName(), createTargetSet(var0.getTargets()));
      SubDeploymentMBean[] var2 = var0.getSubDeployments();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.put(var2[var3].getName(), createTargetSet(var2[var3].getTargets()));
         }
      }

      return var1;
   }

   static void verifyTargets(String var0, Set var1, Set var2) {
      if (!var2.containsAll(var1) && containsAny(var2, var1)) {
         Loggable var3 = DeployerRuntimeLogger.logInvalidTargetSubsetLoggable(var0, var1.toString(), var2.toString());
         var3.log();
      }

   }

   private static boolean containsAny(Set var0, Set var1) {
      Iterator var2 = var1.iterator();

      Object var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = var2.next();
      } while(!var0.contains(var3));

      return true;
   }

   static Set createTargetSet(TargetMBean[] var0) {
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2].getName());
      }

      return var1;
   }

   static Set createTargetSet(String[] var0) {
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2]);
      }

      return var1;
   }

   static boolean isOnlyAdminServerTarget(DomainMBean var0, DeploymentData var1) throws ManagementException {
      if (null == var1) {
         return true;
      } else if (!var1.hasTargets()) {
         return true;
      } else {
         try {
            HashSet var2 = new HashSet();
            var2.add(var0.getAdminServerName());
            Set var3 = var1.getAllTargetedServers(var1.getAllLogicalTargets(), var0);
            if (!var2.equals(var3)) {
               return false;
            }
         } catch (InvalidTargetException var4) {
         }

         return true;
      }
   }

   static void validateAutoDeployTarget(DomainMBean var0, String var1, DeploymentData var2) throws ManagementException {
      AppDeploymentMBean var3 = ApplicationVersionUtils.getAppDeployment(var0, var1, (String)null);
      if (null != var3 && var3.isAutoDeployedApp() && !isOnlyAdminServerTarget(var0, var2)) {
         Loggable var4 = DeployerRuntimeLogger.logNoReTargetOnAutoDeployedAppLoggable(var1);
         var4.log();
         throw new ManagementException(var4.getMessage());
      }
   }

   static void validateSplitDirTarget(DomainMBean var0, String var1, DeploymentData var2) throws ManagementException {
      AppDeploymentMBean var3 = ApplicationVersionUtils.getAppDeployment(var0, var1, (String)null);
      if (null != var3) {
         try {
            ApplicationFileManager var4 = ApplicationFileManager.newInstance(var3.getAbsoluteSourcePath());
            if (var4.isSplitDirectory() && !isOnlyAdminServerTarget(var0, var2)) {
               Loggable var5 = DeployerRuntimeLogger.logNoReTargetOnSplitDirAppLoggable(var1);
               var5.log();
               throw new ManagementException(var5.getMessage());
            }

            isOnlyAdminServerTarget(var0, var2);
         } catch (IOException var6) {
         }
      }

   }

   static void validateDeployWhileRetire(String var0, String var1, AppDeploymentMBean var2) throws ManagementException {
      if (!RetirementManager.cancelIfNeeded(var0, var1)) {
         Loggable var3 = DeployerRuntimeLogger.logActivateWhileRetireInProgressLoggable(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var2));
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   static void validateUndeployWhileRetire(String var0, String var1, AppDeploymentMBean var2, DeploymentData var3, String var4) throws ManagementException {
      if (!isRetirementTask(var4)) {
         if (isGracefulProdToAdmin(var3)) {
            if (RetirementManager.isRetirementInProgress(var0, var1)) {
               Loggable var5 = DeployerRuntimeLogger.logGracefulUndeployWhileRetireInProgressLoggable(ApplicationVersionUtils.getDisplayName(var0, var1));
               var5.log();
               throw new ManagementException(var5.getMessage());
            }
         } else {
            RetirementManager.cancelIfNeeded(var0, var1);
         }

      }
   }

   static void validateModuleType(String var0, String var1, AppDeploymentMBean var2) throws ManagementException {
      if (var1 != null && !DeployHelper.isModuleType(var2, ModuleType.EAR) && !DeployHelper.isModuleType(var2, ModuleType.WAR) && !(var2 instanceof LibraryMBean)) {
         String var3 = var2.getModuleType();
         Loggable var4 = DeployerRuntimeLogger.logModuleTypeNotSupportedLoggable(ApplicationVersionUtils.getDisplayName(var0, var1), var3 == null ? null : var3.toUpperCase(Locale.US), "EAR, WAR (Webapps, including stateless Webservices)");
         var4.log();
         throw new ManagementException(var4.getMessage());
      }
   }

   static void validatePath(String var0, AppDeploymentMBean var1) throws ManagementException {
      if (var0 != null) {
         Loggable var3;
         try {
            String var2 = (new File(var0)).getCanonicalPath();
            if (!isSameCanonicalPaths(var2, var1)) {
               var3 = DeployerRuntimeLogger.logInvalidNewSourceLoggable(var2, ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var1), var1.getAbsoluteSourcePath());
               var3.log();
               throw new ManagementException(var3.getMessage());
            }
         } catch (IOException var4) {
            if (!"nostage".equals(var1.getStagingMode())) {
               var3 = DeployerRuntimeLogger.logInvalidSourceLoggable(var0, ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var1), var4.getMessage());
               var3.log();
               throw new ManagementException(var3.getMessage(), var4);
            }
         }

      }
   }

   static boolean isGracefulProdToAdmin(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null ? var0.getDeploymentOptions().isGracefulProductionToAdmin() : false;
   }

   static boolean isSameCanonicalPaths(String var0, AppDeploymentMBean var1) throws IOException {
      String var2 = var1.getAbsoluteSourcePath();
      if (var2 == null && var0 != null) {
         return false;
      } else if (var2 == null && var0 == null) {
         return true;
      } else {
         String var3 = (new File(var2)).getCanonicalPath();
         if (isDebugEnabled()) {
            debug("Validating new path, " + var0 + ", against configured path, " + var3);
         }

         return var0.equals(var3);
      }
   }

   static void assertModeIsStage(AppDeploymentMBean var0, String var1) throws ManagementException {
      String var2 = var0.getStagingMode();
      if (var2 != null && !var2.equals("stage")) {
         Loggable var3 = DeployerRuntimeLogger.logErrorStagingModeLoggable(var1, var2);
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   static void assertAppIsNonNull(AppDeploymentMBean var0, String var1, String var2, String var3) throws ManagementException {
      if (var0 == null) {
         Loggable var4;
         if (ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isRestartRequired()) {
            var4 = DeployerRuntimeLogger.logNullAppNonDynamicLoggable(ApplicationVersionUtils.getDisplayName(var1, var2), var3);
         } else {
            var4 = DeployerRuntimeLogger.logNullAppLoggable(ApplicationVersionUtils.getDisplayName(var1, var2), var3);
         }

         var4.log();
         throw new ManagementException(var4.getMessage());
      }
   }

   static void assertAppIsActive(AppDeploymentMBean var0, String var1, String var2, String var3) throws ManagementException {
      if (var0 != null) {
         String var4 = appRTStateMgr.getCurrentState(var0);
         if (var4 != null && (var4.equals("STATE_RETIRED") || var4.equals("STATE_UPDATE_PENDING"))) {
            Loggable var5 = DeployerRuntimeLogger.logNonActiveAppLoggable(ApplicationVersionUtils.getDisplayName(var1, var2), var4, var3);
            var5.log();
            throw new ManagementException(var5.getMessage());
         }
      }
   }

   static void assertAppIsNotRetired(AppDeploymentMBean var0, String var1, String var2, String var3) throws ManagementException {
      if (var0 != null) {
         String var4 = appRTStateMgr.getCurrentState(var0);
         if (var4 != null && var4.equals("STATE_RETIRED")) {
            Loggable var5 = DeployerRuntimeLogger.logInvalidAppStateLoggable(ApplicationVersionUtils.getDisplayName(var1, var2), var4, var3);
            var5.log();
            throw new ManagementException(var5.getMessage());
         }
      }
   }

   static void assertNameIsNonNull(String var0, String var1) throws ManagementException {
      if (var0 == null) {
         Loggable var2 = DeployerRuntimeLogger.logNullAppLoggable("null", var1);
         var2.log();
         throw new ManagementException(var2.getMessage());
      }
   }

   static void assertSourceIsNonNull(String var0, String var1, String var2) throws ManagementException {
      if (var0 == null) {
         Loggable var3 = DeployerRuntimeLogger.logNoSourceLoggable(ApplicationVersionUtils.getDisplayName(var1, var2));
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   static void assertInfoIsNonNull(DeploymentData var0, String var1, String var2) throws ManagementException {
      if (var0 == null || !var0.hasFiles()) {
         Loggable var3 = DeployerRuntimeLogger.logNullInfoLoggable(ApplicationVersionUtils.getDisplayName(var1, var2));
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   static void assertNoChangeInStagingMode(String var0, AppDeploymentMBean var1) throws ManagementException {
      if (var0 != null && var1.getStagingMode() != null && !var0.equals(var1.getStagingMode())) {
         Loggable var2 = DeployerRuntimeLogger.logMisMatchStagingModeLoggable(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var1), var1.getStagingMode(), var0);
         var2.log();
         throw new ManagementException(var2.getMessage());
      }
   }

   static void assertTargetIsNonNull(DeploymentData var0) throws ManagementException {
      if (var0 == null || !var0.hasTargets()) {
         String var1 = DeployerRuntimeLogger.operationRequiresTarget();
         throw new ManagementException(var1);
      }
   }

   static void assertPlanIsNonNull(DeploymentData var0) throws ManagementException {
      if (var0 == null || var0.getDeploymentPlan() == null) {
         String var1 = DeployerRuntimeLogger.operationRequiresPlan();
         throw new ManagementException(var1);
      }
   }

   static boolean isAdminMode(DeploymentData var0) {
      return var0 != null && var0.getDeploymentOptions() != null ? var0.getDeploymentOptions().isTestMode() : false;
   }

   public static void setAdminMode(AppDeploymentMBean var0, DeploymentData var1, String var2) throws ManagementException {
      if (var0 == null) {
         if (isDebugEnabled()) {
            debug("OperationHelper.setAdminMode(): Deployment is null");
         }

      } else {
         if (isDebugEnabled()) {
            debug("OperationHelper.setAdminMode(): For application = " + var0.getName() + ", defaultState = " + var2);
         }

         boolean var3 = isAdminMode(var1);
         if (isDebugEnabled()) {
            debug("OperationHelper.setAdminMode(): AdminMode from deployData: " + var3);
         }

         String var4 = var3 ? "STATE_ADMIN" : var2;
         setState(var0, var1, var4);
      }
   }

   public static void setState(AppDeploymentMBean var0, DeploymentData var1, String var2) throws ManagementException {
      if (var0 != null) {
         resetUnconfiguredTargets(var0);
         if (isDebugEnabled()) {
            debug("OperationHelper.setState(): Setting intended state for app : " + var0.getName() + " to : " + var2);
         }

         appRTStateMgr.setState(var0.getName(), (String[])((String[])var1.getAllLogicalTargets().toArray(new String[0])), var2);
         var1.setIntendedState(var2);
      }
   }

   private static void resetUnconfiguredTargets(AppDeploymentMBean var0) throws ManagementException {
      ApplicationRuntimeState var1 = appRTStateMgr.get(var0.getName());
      if (var1 != null) {
         ArrayList var2 = new ArrayList();
         Map var3 = var1.getAppTargetState();
         if (var3 != null) {
            Set var4 = DeployHelper.getAllTargetNames((BasicDeploymentMBean)var0);
            Iterator var6;
            String var7;
            if (isDebugEnabled()) {
               String var5 = "";

               for(var6 = var4.iterator(); var6.hasNext(); var5 = var5 + var7 + " ") {
                  var7 = (String)var6.next();
               }

               debug("Targets assoc with " + var0.getName() + ": " + var5);
            }

            Set var8 = var3.keySet();
            var6 = var8.iterator();

            while(var6.hasNext()) {
               var7 = (String)var6.next();
               if (!var4.contains(var7)) {
                  var2.add(var7);
                  if (isDebugEnabled()) {
                     debug("Resetting intended state for " + var0.getName() + " on target " + var7);
                  }
               }
            }
         }

         if (!var2.isEmpty()) {
            appRTStateMgr.resetState(var0.getName(), (String[])((String[])var2.toArray(new String[0])));
         }
      }

   }

   static boolean hasFiles(DeploymentData var0) {
      return var0.getFiles() != null && var0.getFiles().length > 0;
   }

   static String ensureAppName(String var0) {
      return ApplicationVersionUtils.getApplicationName(var0);
   }

   static void validateRedeploySource(String var0, String var1, AppDeploymentMBean var2) throws ManagementException {
      Loggable var3;
      if (var1 == null) {
         var3 = DeployerRuntimeLogger.logRedeployWithSrcNotAllowedForNonVersionLoggable(var0);
         var3.log();
         throw new ManagementException(var3.getMessage());
      } else if (var2 != null && var1.equals(var2.getVersionIdentifier())) {
         var3 = DeployerRuntimeLogger.logRedeployWithSrcNotAllowedForSameVersionLoggable(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var2));
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   public static void logTaskFailed(String var0, int var1) {
      logTaskFailed(var0, getTaskString(var1));
   }

   public static void logTaskFailed(String var0, int var1, Throwable var2) {
      logTaskFailed(var0, getTaskString(var1), var2);
   }

   public static String getTaskString(int var0) {
      switch (var0) {
         case 1:
            return textformatter.messageActivate();
         case 2:
         default:
            throw new AssertionError();
         case 3:
            return textformatter.messageDeactivate();
         case 4:
            return textformatter.messageRemove();
         case 5:
            return textformatter.messageUnprepare();
         case 6:
            return textformatter.messageDistribute();
         case 7:
            return textformatter.messageStart();
         case 8:
            return textformatter.messageStop();
         case 9:
            return textformatter.messageRedeploy();
         case 10:
            return textformatter.messageUpdate();
         case 11:
            return textformatter.messageDeploy();
         case 12:
            return textformatter.messageUndeploy();
         case 13:
            return textformatter.messageRetire();
      }
   }

   private static boolean isRetirementTask(String var0) {
      return RetirementManager.isRetireTaskId(var0);
   }

   private static void logTaskFailed(String var0, String var1, Throwable var2) {
      if (var2 == null) {
         if (var0 != null) {
            DeployerRuntimeLogger.logTaskFailed(ApplicationVersionUtils.getDisplayName(var0), var1);
         } else {
            DeployerRuntimeLogger.logTaskFailedNoApp(var1);
         }
      } else if (var0 != null) {
         DeployerRuntimeLogger.logTaskFailedWithError(ApplicationVersionUtils.getDisplayName(var0), var1, var2.getMessage());
      } else {
         DeployerRuntimeLogger.logTaskFailedNoAppWithError(var1, var2.getMessage());
      }

   }

   private static void logTaskFailed(String var0, String var1) {
      logTaskFailed(var0, var1, (Throwable)null);
   }

   public static void assertNoMixedTargeting(DeploymentData var0) throws ManagementException {
      if (var0 != null && !var0.hasFiles() && !var0.isTargetsFromConfig() && var0.hasSubModuleTargets() && var0.hasModuleTargets()) {
         Thread.dumpStack();
         throw new ManagementException(DeployerRuntimeLogger.mixedTargetError());
      }
   }

   public static void assertNoChangedAltDDs(DeploymentData var0) throws ManagementException {
      if (var0 != null) {
         String var1 = var0.getAltDescriptorPath();
         String var2 = var0.getAltWLSDescriptorPath();
         if (var1 != null && !var1.equals("")) {
            throw new ManagementException(DeployerRuntimeLogger.invalidAltDDDuringRedeploy());
         }

         if (var2 != null && !var2.equals("")) {
            throw new ManagementException(DeployerRuntimeLogger.invalidAltDDDuringRedeploy());
         }
      }

   }

   public static String normalizePaths(String var0, DeploymentData var1) {
      var0 = normalize(var0);
      var1.setRootDirectory(normalize(var1.getRootDirectory()));
      var1.setDeploymentPlan(normalize(var1.getDeploymentPlan()));
      var1.setConfigDirectory(normalize(var1.getConfigDirectory()));
      return var0;
   }

   private static String normalize(String var0) {
      return FileUtils.normalize(var0);
   }

   private static void debug(String var0, Throwable var1) {
      Debug.deploymentDebug(var0, var1);
   }

   private static void debug(String var0) {
      Debug.deploymentDebug(var0);
   }

   private static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }
}
