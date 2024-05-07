package weblogic.wsee.security;

public final class AuthorizationContext {
   private final String applicationName;
   private final String contextPath;
   private final String securityRealm;

   public AuthorizationContext(String var1, String var2, String var3) {
      this.applicationName = var1;
      this.contextPath = var2;
      this.securityRealm = var3;
   }

   public final String getApplicationName() {
      return this.applicationName;
   }

   public final String getContextPath() {
      return this.contextPath;
   }

   public final String getSecurityRealm() {
      return this.securityRealm;
   }
}
