package weblogic.servlet.internal;

public final class OnDemandContext {
   private final String contextPath;
   private final OnDemandListener listener;
   private final String appName;
   private final boolean displayRefresh;
   private int progressIndicator = 3;

   OnDemandContext(String var1, OnDemandListener var2, String var3, boolean var4) {
      this.contextPath = var1;
      this.listener = var2;
      this.appName = var3;
      this.displayRefresh = var4;
   }

   String getContextPath() {
      return this.contextPath;
   }

   OnDemandListener getListener() {
      return this.listener;
   }

   String getAppName() {
      return this.appName;
   }

   boolean isDisplayRefresh() {
      return this.displayRefresh;
   }

   int updateProgressIndicator() {
      return this.progressIndicator++;
   }
}
