package weblogic.application;

import weblogic.application.utils.ApplicationVersionUtils;

public class ApplicationVersionLifecycleEvent {
   private String ownAppId;
   private String appId;

   public ApplicationVersionLifecycleEvent(String var1, String var2) {
      this.ownAppId = var1;
      this.appId = var2;
   }

   public String getApplicationId() {
      return this.appId;
   }

   public String getApplicationName() {
      return ApplicationVersionUtils.getApplicationName(this.appId);
   }

   public String getVersionId() {
      return ApplicationVersionUtils.getVersionId(this.appId);
   }

   public String getArchiveVersion() {
      return ApplicationVersionUtils.getArchiveVersion(this.getVersionId());
   }

   public String getPlanVersion() {
      return ApplicationVersionUtils.getPlanVersion(this.getPlanVersion());
   }

   public String getLibSpecVersion() {
      return ApplicationVersionUtils.getLibSpecVersion(this.getArchiveVersion());
   }

   public String getLibImplVersion() {
      return ApplicationVersionUtils.getLibImplVersion(this.getArchiveVersion());
   }

   public boolean isAdminMode() {
      return ApplicationVersionUtils.getAdminModeAppCtxParam(ApplicationAccess.getApplicationAccess().getCurrentApplicationContext());
   }

   public boolean isActiveVersion() {
      return ApplicationVersionUtils.isActiveVersion(this.appId);
   }

   public boolean isOwnVersion() {
      return this.appId != null && this.ownAppId.equals(this.appId);
   }

   public boolean isOwnVersionActive() {
      return ApplicationVersionUtils.isActiveVersion(this.ownAppId);
   }

   public String toString() {
      return "ApplicationVersionLifecycleEvent(" + ApplicationVersionUtils.getDisplayName(this.appId) + ",isAdmin=" + this.isAdminMode() + ",isActive=" + this.isActiveVersion() + ",isOwnVersion=" + this.isOwnVersion() + ",isOwnActive=" + this.isOwnVersionActive() + ")";
   }
}
