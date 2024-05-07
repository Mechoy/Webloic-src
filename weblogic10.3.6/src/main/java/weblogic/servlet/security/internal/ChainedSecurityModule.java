package weblogic.servlet.security.internal;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionInternal;

final class ChainedSecurityModule extends SecurityModule {
   private final SecurityModule[] modules;

   public ChainedSecurityModule(WebAppServletContext var1, WebAppSecurity var2, String[] var3) {
      super(var1, var2);
      this.modules = this.getSecurityModules(var3);
   }

   private SecurityModule[] getSecurityModules(String[] var1) {
      if (var1 != null && var1.length != 0) {
         SecurityModule[] var2 = new SecurityModule[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = SecurityModule.createModule(this.getServletContext(), this.webAppSecurity, var3 != var1.length - 1, var1[var3]);
            if (DEBUG_SEC.isDebugEnabled()) {
               DEBUG_SEC.debug(this.getServletContext() + " ChainedSecuirtyModule adding " + var2[var3]);
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   boolean checkAccess(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, boolean var5) throws IOException, ServletException {
      SecurityModule var6 = this.modules[this.modules.length - 1];
      if (!this.webAppSecurity.checkTransport(var4, var1, var2)) {
         return var6 instanceof FormSecurityModule ? var6.checkAccess(var1, var2, var3, var4, var5) : false;
      } else {
         AuthenticatedSubject var7 = getCurrentUser(this.getHttpServer(), var1, var3);

         for(int var8 = 0; var8 < this.modules.length; ++var8) {
            if (DEBUG_SEC.isDebugEnabled()) {
               DEBUG_SEC.debug(this.getServletContext() + " ChainedSecuirtyModule checking access with " + this.modules[var8]);
            }

            if (this.modules.length - 1 == var8) {
               if (var6 instanceof FormSecurityModule) {
                  return var6.checkAccess(var1, var2, var3, var4, var5);
               }

               return var6.checkUserPerm(var1, var2, var3, var4, var7, var5);
            }

            if (!var1.getRequestURI().endsWith("/j_security_check") || this.modules[var8] instanceof FormSecurityModule) {
               if (this.modules[var8].checkUserPerm(var1, var2, var3, var4, var7, false)) {
                  return true;
               }

               var3 = this.getUserSession(var1, false);
            }
         }

         return false;
      }
   }

   boolean checkUserPerm(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, AuthenticatedSubject var5, boolean var6) {
      return false;
   }

   void setAuthRealmBanner(String var1) {
      super.setAuthRealmBanner(var1);
      if (this.modules != null) {
         SecurityModule[] var2 = this.modules;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SecurityModule var5 = var2[var4];
            var5.setAuthRealmBanner(var1);
         }

      }
   }
}
