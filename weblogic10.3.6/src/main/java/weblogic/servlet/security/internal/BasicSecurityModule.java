package weblogic.servlet.security.internal;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.utils.StringUtils;
import weblogic.utils.encoders.BASE64Decoder;

class BasicSecurityModule extends SecurityModule {
   public BasicSecurityModule(WebAppServletContext var1, WebAppSecurity var2, boolean var3) {
      super(var1, var2, var3);
   }

   boolean checkUserPerm(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, AuthenticatedSubject var5, boolean var6) throws IOException, ServletException {
      boolean var7 = this.webAppSecurity.hasPermission(var1, var2, var5, var4);
      if (var7) {
         if (!this.checkAuthCookie(this.getHttpServer(), var1, var3)) {
            if (DEBUG_SEC.isDebugEnabled()) {
               DEBUG_SEC.debug("AuthCookie not found - permission denied for " + var1);
            }

            this.handleFailure(var1, var2, false);
            return false;
         }

         if (ServletRequestImpl.getOriginalRequest(var1).getServletStub().isProxyServlet()) {
            return true;
         }

         if (!this.enforceValidBasicAuthCredentials() || var5 != null && !SubjectUtils.isUserAnonymous(var5)) {
            return true;
         }
      }

      boolean var8 = this.webAppSecurity.isFullSecurityDelegationRequired() && var4 != null && var4.isForbidden();
      String[] var9 = splitAuthHeader(var1);
      if (var9 == null) {
         if (var7) {
            return true;
         } else {
            if (var8 || var5 != null && !this.isReloginEnabled()) {
               this.sendForbiddenResponse(var1, var2);
            } else {
               this.handleFailure(var1, var2, var6);
            }

            return false;
         }
      } else {
         AuthenticatedSubject var10 = checkAuthenticate(var9[0], var9[1], var1, var2, this.getServletContext(), false);
         if (var10 == null) {
            if (var8 || var5 != null && !this.isReloginEnabled()) {
               this.sendForbiddenResponse(var1, var2);
            } else {
               this.handleFailure(var1, var2, var6);
            }

            return false;
         } else if (this.webAppSecurity.hasPermission(var1, var2, var10, var4)) {
            if (!this.checkAuthCookie(this.getHttpServer(), var1, var3)) {
               if (DEBUG_SEC.isDebugEnabled()) {
                  DEBUG_SEC.debug("AuthCookie not found - permission denied for " + var1);
               }

               this.handleFailure(var1, var2, false);
               return false;
            } else {
               if (DEBUG_SEC.isDebugEnabled()) {
                  DEBUG_SEC.debug(this.webAppSecurity.getContextLog() + ": user: " + getUsername(var10) + " has permissions to access " + var1);
               }

               this.login(var1, var10, var3);
               return true;
            }
         } else {
            if (!var8 && this.isReloginEnabled()) {
               this.handleFailure(var1, var2, var6);
            } else {
               this.sendForbiddenResponse(var1, var2);
            }

            return false;
         }
      }
   }

   protected boolean enforceValidBasicAuthCredentials() {
      WebAppSecurity var10000 = this.webAppSecurity;
      return WebAppSecurity.getEnforceValidBasicAuthCredentials();
   }

   private void handleFailure(HttpServletRequest var1, HttpServletResponse var2, boolean var3) throws IOException, ServletException {
      if (var3 && this.webAppSecurity.hasAuthFilters()) {
         this.invokeAuthFilterChain(var1, var2);
      } else {
         this.sendError(var1, var2);
      }
   }

   private static final String[] splitAuthHeader(HttpServletRequest var0) throws IOException {
      String var1 = ServletRequestImpl.getOriginalRequest(var0).getRequestHeaders().getAuthorization();
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = StringUtils.split(var1, ' ');
         if (!var2[0].equals("Basic")) {
            return null;
         } else {
            BASE64Decoder var3 = new BASE64Decoder();
            byte[] var4 = var3.decodeBuffer(var2[1]);
            String[] var5 = StringUtils.split(new String(var4), ':');
            if (var5 != null && var5.length >= 2) {
               String[] var6 = new String[]{var5[0], var5[1]};
               return var6;
            } else {
               return null;
            }
         }
      }
   }
}
