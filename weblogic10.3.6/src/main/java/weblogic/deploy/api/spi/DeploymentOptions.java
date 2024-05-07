package weblogic.deploy.api.spi;

import java.io.Serializable;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.management.security.DeploymentModel;

public final class DeploymentOptions implements Serializable {
   private static final long serialVersionUID = -8124655104926643928L;
   private boolean useNonexclusiveLock = true;
   private boolean operationInitiatedByAutoDeployPoller = false;
   private boolean testOption = false;
   private int retireTimeOption = -1;
   private boolean retireGracefulPolicy = true;
   private boolean gracefulProductionToAdmin = false;
   private boolean gracefulIgnoreSessions = false;
   private int gracefulRMIGracePeriodSecs = -1;
   private boolean undeployAllVersions = false;
   private String stageOption;
   private String name;
   private boolean nameFromLibrary;
   private long forceUndeployTimeoutSecs;
   private boolean disableModuleLevelStartStop;
   private long timeout;
   private int deploymentOrder;
   private String deploymentPrincipalName;
   private boolean nameFromSource;
   private boolean defaultSubmoduleTargets;
   private String altDD;
   private String altWlsDD;
   private boolean noVersion;
   private boolean remote;
   private boolean useExpiredLock;
   public static final String STAGE_DEFAULT = null;
   public static final String STAGE = "stage";
   public static final String NOSTAGE = "nostage";
   public static final String EXTERNAL_STAGE = "external_stage";
   public static final boolean FULL_ACCESS = false;
   public static final boolean ADMIN_ACCESS = true;
   private String securityModel;
   private boolean isSecurityValidationEnabled;
   private String archiveVersion;
   private String planVersion;
   private boolean isLibrary;
   private String libSpecVersion;
   private String libImplVersion;
   public static final int CLUSTER_DEPLOYMENT_TIMEOUT = 3600000;
   private int clusterTimeout;

   public DeploymentOptions() {
      this.stageOption = STAGE_DEFAULT;
      this.name = null;
      this.nameFromLibrary = false;
      this.forceUndeployTimeoutSecs = 0L;
      this.disableModuleLevelStartStop = false;
      this.timeout = 0L;
      this.deploymentOrder = 100;
      this.deploymentPrincipalName = null;
      this.nameFromSource = false;
      this.defaultSubmoduleTargets = true;
      this.altDD = null;
      this.altWlsDD = null;
      this.noVersion = false;
      this.remote = false;
      this.useExpiredLock = false;
      this.securityModel = null;
      this.isSecurityValidationEnabled = false;
      this.archiveVersion = null;
      this.planVersion = null;
      this.isLibrary = false;
      this.libSpecVersion = null;
      this.libImplVersion = null;
      this.clusterTimeout = 3600000;
   }

   public boolean usesNonExclusiveLock() {
      return this.useNonexclusiveLock;
   }

   public boolean isOperationInitiatedByAutoDeployPoller() {
      return this.operationInitiatedByAutoDeployPoller;
   }

   public void setOperationInitiatedByAutoDeployPoller(boolean var1) {
      this.operationInitiatedByAutoDeployPoller = var1;
   }

   public void setUseNonexclusiveLock(boolean var1) {
      this.useNonexclusiveLock = var1;
   }

   public String getAltDD() {
      return this.altDD;
   }

   public void setAltDD(String var1) {
      this.altDD = var1;
   }

   public String getAltWlsDD() {
      return this.altWlsDD;
   }

   public void setAltWlsDD(String var1) {
      this.altWlsDD = var1;
   }

   public String getSecurityModel() {
      return this.securityModel;
   }

   public void setSecurityModel(String var1) throws IllegalArgumentException {
      if (DeploymentModel.isValidModel(var1)) {
         this.securityModel = var1;
      } else {
         throw new IllegalArgumentException(SPIDeployerLogger.invalidSecurityModel(var1, "DDOnly|CustomRoles|CustomRolesAndPolicies|Advanced"));
      }
   }

   public boolean isSecurityValidationEnabled() {
      return this.isSecurityValidationEnabled;
   }

   public void setSecurityValidationEnabled(boolean var1) {
      this.isSecurityValidationEnabled = var1;
   }

   public String getArchiveVersion() {
      return this.archiveVersion;
   }

   public void setArchiveVersion(String var1) {
      this.archiveVersion = var1;
   }

   public String getPlanVersion() {
      return this.planVersion;
   }

   public void setPlanVersion(String var1) {
      this.planVersion = var1;
   }

   public String getVersionIdentifier() {
      return ApplicationVersionUtils.getVersionId(this.archiveVersion, this.planVersion);
   }

   public void setVersionIdentifier(String var1) {
      this.archiveVersion = ApplicationVersionUtils.getArchiveVersion(var1);
      this.planVersion = ApplicationVersionUtils.getPlanVersion(var1);
   }

   public boolean isLibrary() {
      return this.isLibrary;
   }

   public void setLibrary(boolean var1) {
      this.isLibrary = var1;
   }

   public String getLibSpecVersion() {
      return this.libSpecVersion;
   }

   public void setLibSpecVersion(String var1) {
      this.libSpecVersion = var1;
   }

   public String getLibImplVersion() {
      return this.libImplVersion;
   }

   public void setLibImplVersion(String var1) {
      this.libImplVersion = var1;
   }

   public boolean isNameFromLibrary() {
      return this.nameFromLibrary;
   }

   public void setNameFromLibrary(boolean var1) {
      this.nameFromLibrary = var1;
   }

   public boolean isNameFromSource() {
      return this.nameFromSource;
   }

   public void setNameFromSource(boolean var1) {
      this.nameFromSource = var1;
   }

   /** @deprecated */
   public boolean isTestMode() {
      return this.isAdminMode();
   }

   /** @deprecated */
   public void setTestMode(boolean var1) {
      this.setAdminMode(var1);
   }

   public boolean isAdminMode() {
      return this.testOption;
   }

   public void setAdminMode(boolean var1) {
      this.testOption = var1;
   }

   public int getRetireTime() {
      return this.retireTimeOption;
   }

   public void setRetireTime(int var1) {
      this.retireTimeOption = var1;
      if (this.retireTimeOption != -1) {
         this.setRetireGracefully(false);
      }

   }

   public boolean isRetireGracefully() {
      return this.retireGracefulPolicy;
   }

   public void setRetireGracefully(boolean var1) {
      this.retireGracefulPolicy = var1;
   }

   public boolean isGracefulProductionToAdmin() {
      return this.gracefulProductionToAdmin;
   }

   public void setGracefulProductionToAdmin(boolean var1) {
      this.gracefulProductionToAdmin = var1;
   }

   public boolean isGracefulIgnoreSessions() {
      return this.gracefulIgnoreSessions;
   }

   public void setGracefulIgnoreSessions(boolean var1) {
      this.gracefulIgnoreSessions = var1;
   }

   public int getRMIGracePeriodSecs() {
      return this.gracefulRMIGracePeriodSecs;
   }

   public void setRMIGracePeriodSecs(int var1) {
      this.gracefulRMIGracePeriodSecs = var1;
   }

   public boolean isUndeployAllVersions() {
      return this.undeployAllVersions;
   }

   public void setUndeployAllVersions(boolean var1) {
      this.undeployAllVersions = var1;
   }

   public String getStageMode() {
      return this.stageOption;
   }

   public void setStageMode(String var1) {
      this.stageOption = var1;
   }

   public int getClusterDeploymentTimeout() {
      return this.clusterTimeout;
   }

   public void setClusterDeploymentTimeout(int var1) {
      this.clusterTimeout = var1;
   }

   public long getForceUndeployTimeout() {
      return this.forceUndeployTimeoutSecs;
   }

   public void setForceUndeployTimeout(long var1) {
      this.forceUndeployTimeoutSecs = var1;
   }

   public boolean usesExpiredLock() {
      return this.useExpiredLock;
   }

   public void setUseExpiredLock(boolean var1) {
      this.useExpiredLock = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("{").append("isRetireGracefully=").append(this.isRetireGracefully()).append(",").append("isGracefulProductionToAdmin=").append(this.isGracefulProductionToAdmin()).append(",").append("isGracefulIgnoreSessions=").append(this.isGracefulIgnoreSessions()).append(",").append("rmiGracePeriod=").append(this.getRMIGracePeriodSecs()).append(",").append("retireTimeoutSecs=").append(this.getRetireTime()).append(",").append("undeployAllVersions=").append(this.isUndeployAllVersions()).append(",").append("archiveVersion=").append(this.getArchiveVersion()).append(",").append("planVersion=").append(this.getPlanVersion()).append(",").append("isLibrary=").append(this.isLibrary()).append(",").append("libSpecVersion=").append(this.getLibSpecVersion()).append(",").append("libImplVersion=").append(this.getLibImplVersion()).append(",").append("stageMode=").append(this.getStageMode()).append(",").append("clusterTimeout=").append(this.getClusterDeploymentTimeout()).append(",").append("altDD=").append(this.getAltDD()).append(",").append("altWlsDD=").append(this.getAltWlsDD()).append(",").append("name=").append(this.getName()).append(",").append("securityModel=").append(this.getSecurityModel()).append(",").append("securityValidationEnabled=").append(this.isSecurityValidationEnabled()).append(",").append("versionIdentifier=").append(this.getVersionIdentifier()).append(",").append("isTestMode=").append(this.isTestMode()).append(",").append("forceUndeployTimeout=").append(this.getForceUndeployTimeout()).append(",").append("defaultSubmoduleTargets=").append(this.isDefaultSubmoduleTargets()).append(",").append("timeout=").append(this.getTimeout()).append(",").append("deploymentPrincipalName=").append(this.getDeploymentPrincipalName()).append(",").append("useExpiredLock=").append(this.usesExpiredLock()).append("}");
      return var1.toString();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean isDefaultSubmoduleTargets() {
      return this.defaultSubmoduleTargets;
   }

   public void setDefaultSubmoduleTargets(boolean var1) {
      this.defaultSubmoduleTargets = var1;
   }

   public boolean isNoVersion() {
      return this.noVersion;
   }

   public void setNoVersion(boolean var1) {
      this.noVersion = var1;
   }

   public long getTimeout() {
      return this.timeout;
   }

   public void setTimeout(long var1) {
      this.timeout = var1;
   }

   public int getDeploymentOrder() {
      return this.deploymentOrder;
   }

   public void setDeploymentOrder(int var1) {
      this.deploymentOrder = var1;
   }

   public boolean isRemote() {
      return this.remote;
   }

   public void setRemote(boolean var1) {
      this.remote = var1;
   }

   public boolean isDisableModuleLevelStartStop() {
      return this.disableModuleLevelStartStop;
   }

   public void setDisableModuleLevelStartStop(boolean var1) {
      this.disableModuleLevelStartStop = var1;
   }

   public void setDeploymentPrincipalName(String var1) {
      this.deploymentPrincipalName = var1;
   }

   public String getDeploymentPrincipalName() {
      return this.deploymentPrincipalName;
   }
}
