package weblogic.servlet.security.internal;

import weblogic.servlet.internal.WebAppServletContext;

final class Basic2SecurityModule extends BasicSecurityModule {
   static final String BASIC_PLAIN_AUTH = "BASIC_PLAIN";
   static final String BASIC_ENFORCE_AUTH = "BASIC_ENFORCE";
   private final boolean enforceCredentials;

   public Basic2SecurityModule(WebAppServletContext var1, WebAppSecurity var2, boolean var3, String var4) {
      super(var1, var2, var3);
      this.enforceCredentials = var4.equals("BASIC_ENFORCE");
   }

   protected boolean enforceValidBasicAuthCredentials() {
      return this.enforceCredentials;
   }
}
