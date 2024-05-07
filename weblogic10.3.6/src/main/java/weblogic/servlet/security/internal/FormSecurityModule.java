package weblogic.servlet.security.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.security.AccessController;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ForwardAction;
import weblogic.servlet.internal.ServletInputStreamImpl;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.utils.http.QueryParams;

final class FormSecurityModule extends SecurityModule {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private AuthenticatedSubject currentUser = null;

   public FormSecurityModule(WebAppServletContext var1, WebAppSecurity var2) {
      super(var1, var2);
   }

   boolean checkAccess(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, boolean var5) throws IOException, ServletException {
      String var6 = var1.getRequestURI();
      if (var3 != null && var6.endsWith("/j_security_check")) {
         this.currentUser = getCurrentUser(this.getHttpServer(), var1, var3);
         SecurityModule.logout(this.getHttpServer(), var3);
      }

      this.recoverSavedPostData(var1, var3);
      String var7 = WebAppSecurity.getRelativeURI(var1);
      if (var4 == null && !this.webAppSecurity.isFullSecurityDelegationRequired() && !var6.endsWith("/j_security_check") && !this.servletContext.isAdminMode()) {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug(this.webAppSecurity.getContextLog() + ": user: " + var1.getUserPrincipal() + " has permissions to access " + var1);
         }

         return true;
      } else if (!this.webAppSecurity.checkTransport(var4, var1, var2)) {
         return false;
      } else if (!var7.equals(this.webAppSecurity.getLoginPage()) && !var7.equals(this.webAppSecurity.getErrorPage())) {
         if (!this.webAppSecurity.isFullSecurityDelegationRequired() && var4 != null && var4.isUnrestricted() && !var6.endsWith("/j_security_check") && !this.servletContext.isAdminMode()) {
            if (DEBUG_SEC.isDebugEnabled()) {
               DEBUG_SEC.debug(this.webAppSecurity.getContextLog() + ": " + var1.getUserPrincipal() + " has permissions to access " + var1);
            }

            return true;
         } else {
            AuthenticatedSubject var8 = getCurrentUser(this.getHttpServer(), var1, var3);
            if (!this.checkUserPerm(var1, var2, var3, var4, var8, var5)) {
               return false;
            } else {
               if (var8 != null && var3 != null) {
                  var3 = this.getUserSession(var1, false);
                  var3.removeInternalAttribute("weblogic.formauth.immediate");
               }

               return true;
            }
         }
      } else {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug(this.webAppSecurity.getContextLog() + ": user: " + var1.getUserPrincipal() + " has permissions to access " + var1);
         }

         return true;
      }
   }

   private void recoverSavedPostData(HttpServletRequest var1, SessionInternal var2) {
      Object var3 = null;
      if (var2 != null) {
         try {
            var3 = var2.getInternalAttribute("weblogic.formauth.postcookie");
         } catch (IllegalStateException var14) {
            var3 = null;
         }

         if (var3 == null) {
            String var15 = (String)var2.getInternalAttribute("weblogic.formauth.method");
            if (var15 != null && var15.equals("GET")) {
               var2.removeInternalAttribute("weblogic.formauth.method");
            }

         } else {
            var2.removeInternalAttribute("weblogic.formauth.method");
            var2.removeInternalAttribute("weblogic.formauth.postcookie");
            ServletRequestImpl var4 = ServletRequestImpl.getOriginalRequest(var1);
            String var5 = var4.getMethod();
            var4.setMethod("POST");
            QueryParams var6 = (QueryParams)var2.getInternalAttribute("weblogic.formauth.queryparams");
            if (var6 != null) {
               var2.removeInternalAttribute("weblogic.formauth.queryparams");
               var4.getRequestParameters().setQueryParams(var6);
            }

            byte[] var7 = (byte[])((byte[])var2.getInternalAttribute("weblogic.formauth.bytearray"));
            if (var7 != null) {
               var2.removeInternalAttribute("weblogic.formauth.bytearray");
               var4.setInputStream((ServletInputStream)(new ServletInputStreamImpl(new ByteArrayInputStream(var7))));
               if (var5.equals("POST")) {
                  var4.getResponse().disableKeepAlive();
               }
            }

            ArrayList var8 = (ArrayList)var2.getInternalAttribute("weblogic.formauth.reqheadernames");
            if (var8 != null) {
               ArrayList var9 = (ArrayList)var2.getInternalAttribute("weblogic.formauth.reqheadervalues");
               byte[] var10 = var4.getRequestHeaders().getHeaderAsBytes("Cookie");
               int var11;
               if (var10 != null) {
                  var11 = var8.size();
                  boolean var12 = false;

                  for(int var13 = 0; var13 < var11; ++var13) {
                     if ("Cookie".startsWith((String)var8.get(var13))) {
                        var9.set(var13, var10);
                        var12 = true;
                        break;
                     }
                  }

                  if (!var12) {
                     var8.add("Cookie");
                     var9.add(var10);
                  }
               }

               var2.removeInternalAttribute("weblogic.formauth.reqheadernames");
               var2.removeInternalAttribute("weblogic.formauth.reqheadervalues");
               if (var7 == null || var7.length == 0) {
                  var11 = var8.size();

                  for(int var16 = 0; var16 < var11; ++var16) {
                     String var17 = (String)var8.get(var16);
                     if ("Content-Length".equalsIgnoreCase(var17)) {
                        var9.set(var16, String.valueOf(0).getBytes());
                        break;
                     }
                  }
               }

               var4.getRequestHeaders().reset();
               var4.getRequestHeaders().setHeaders(var8, var9);
            }

         }
      }
   }

   boolean checkUserPerm(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, AuthenticatedSubject var5, boolean var6) throws IOException, ServletException {
      if (var1.getRequestURI().endsWith("j_security_check")) {
         return this.processJSecurityCheck(var1, var2, var3);
      } else if (var5 != null) {
         return this.processLoggedInUser(var1, var2, var5);
      } else if (this.webAppSecurity.isFullSecurityDelegationRequired() && this.webAppSecurity.hasPermission(var1, var2, (AuthenticatedSubject)null, var4)) {
         return true;
      } else if (var6 && this.webAppSecurity.hasAuthFilters()) {
         this.invokeAuthFilterChain(var1, var2);
         return false;
      } else {
         if (this.isForbidden(var4)) {
            this.sendForbiddenResponse(var1, var2);
         } else {
            this.sendLoginPage(var1, var2);
         }

         return false;
      }
   }

   private boolean processJSecurityCheck(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3) throws IOException {
      String var5 = var1.getParameter("j_character_encoding");
      if (var5 != null) {
         try {
            if (Charset.isSupported(var5)) {
               var1.setCharacterEncoding(var5);
            }
         } catch (IllegalCharsetNameException var12) {
         }
      }

      String var6 = var1.getParameter("j_username");
      String var7 = var1.getParameter("j_password");
      if (var6 != null && var7 != null) {
         AuthenticatedSubject var4 = checkAuthenticate(var6, var7, var1, var2, this.getServletContext(), true);
         if (var4 == null) {
            this.sendError(var1, var2);
            return false;
         } else {
            var1.setAttribute("weblogic.auth.result", new Integer(0));
            String var8 = null;
            if (var3 != null) {
               if (this.getServletContext().getConfigManager().isRetainOriginalURL()) {
                  var8 = (String)var3.getInternalAttribute("weblogic.formauth.targeturl");
               } else {
                  var8 = (String)var3.getInternalAttribute("weblogic.formauth.targeturi");
               }
            }

            if (this.currentUser != null && !SubjectUtils.isUserAnonymous(this.currentUser) && !SubjectUtils.getUsername(this.currentUser).equals(var6) && this.getServletContext().getSessionContext().getConfigMgr().isInvalidateOnRelogin()) {
               if (var3 != null && var3.isValid()) {
                  var3.invalidate();
               }

               var3 = null;
            }

            this.login(var1, var4, var3);
            String var9;
            if (var8 == null) {
               var9 = var1.getRequestURI();
               int var10 = var1.getContextPath().length();
               String var11;
               if (var10 > 0) {
                  var11 = var9.substring(0, var10);
               } else {
                  var11 = "/";
               }

               var2.sendRedirect(var2.encodeRedirectURL(var11));
               return false;
            } else {
               var3 = this.getUserSession(var1, false);
               var9 = (String)var3.getInternalAttribute("weblogic.formauth.method");
               if (var9 != null && "POST".equals(var9)) {
                  var3.setInternalAttribute("weblogic.formauth.postcookie", "true");
               }

               var3.setInternalAttribute("weblogic.formauth.immediate", "true");
               var2.sendRedirect(var2.encodeRedirectURL(var8));
               return false;
            }
         }
      } else {
         this.sendError(var1, var2);
         return false;
      }
   }

   private boolean processLoggedInUser(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3) throws IOException {
      SessionInternal var4 = this.getUserSession(var1, false);
      ResourceConstraint var5 = this.webAppSecurity.getConstraint(var1);
      if (!this.webAppSecurity.hasPermission(var1, var2, var3, var5)) {
         if (var4 != null && var4.getInternalAttribute("weblogic.formauth.immediate") != null) {
            var4.removeInternalAttribute("weblogic.formauth.immediate");
            this.sendError(var1, var2);
            return false;
         } else {
            if (this.isReloginEnabled() && !this.isForbidden(var5)) {
               this.sendLoginPage(var1, var2);
            } else {
               this.sendForbiddenResponse(var1, var2);
            }

            return false;
         }
      } else {
         if (var4 != null) {
            var4.removeInternalAttribute("weblogic.formauth.targeturi");
            var4.removeInternalAttribute("weblogic.formauth.targeturl");
         }

         if (!this.checkAuthCookie(this.getHttpServer(), var1, var4)) {
            if (DEBUG_SEC.isDebugEnabled()) {
               DEBUG_SEC.debug("AuthCookie not found - permission denied for " + var1);
            }

            this.sendLoginPage(var1, var2);
            return false;
         } else {
            return true;
         }
      }
   }

   void sendError(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      var2.setStatus(403);
      if (var2.isCommitted()) {
         var2.sendRedirect(var2.encodeURL(getContextURL(var1) + this.webAppSecurity.getErrorPage()));
      } else {
         RequestDispatcher var3 = var1.getRequestDispatcher(this.webAppSecurity.getErrorPage());
         AuthenticatedSubject var4 = SecurityModule.getCurrentUser(this.getServletContext().getServer(), var1);
         if (var4 == null) {
            var4 = SubjectUtils.getAnonymousSubject();
         }

         ForwardAction var5 = new ForwardAction(var3, var1, var2);
         Throwable var6 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, var4, var5);
         if (var6 != null) {
            if (var6 instanceof IOException) {
               throw (IOException)var6;
            }

            HTTPLogger.logSendError(this.getServletContext().getLogContext(), var6);
         }
      }

   }

   private static final String getContextURL(HttpServletRequest var0) {
      int var1 = var0.getServerPort();
      StringBuffer var2 = new StringBuffer(128);
      if (var1 != 80 && var1 != 443) {
         var2.append(var0.getScheme()).append("://");
         var2.append(var0.getServerName()).append(':');
         var2.append(var0.getServerPort());
         var2.append(ServletRequestImpl.getResolvedContextPath(var0));
      } else {
         var2.append(var0.getScheme()).append("://");
         var2.append(var0.getServerName());
         var2.append(ServletRequestImpl.getResolvedContextPath(var0));
      }

      return var2.toString();
   }

   private final void sendLoginPage(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      this.stuffSession(var1, var2);
      var2.sendRedirect(var2.encodeURL(ServletRequestImpl.getResolvedContextPath(var1) + this.webAppSecurity.getLoginPage()));
   }

   private void stuffSession(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      SessionInternal var3 = this.getUserSession(var1, true);
      ServletRequestImpl var4 = ServletRequestImpl.getOriginalRequest(var1);
      ServletResponseImpl var5 = var4.getResponse();
      String var6 = var1.getRequestURI();
      String var7 = var5.getURLForRedirect(var1.getRequestURI());
      String var8 = var1.getQueryString();
      if (var8 != null) {
         var7 = var7 + "?" + var8;
         var6 = var6 + "?" + var8;
      }

      var3.setInternalAttribute("weblogic.formauth.targeturi", var6);
      var3.setInternalAttribute("weblogic.formauth.targeturl", var7);
      var3.setInternalAttribute("weblogic.formauth.method", var1.getMethod());
      if (var4.getInputHelper().getRequestParser().isMethodPost()) {
         ServletInputStream var9 = var1.getInputStream();
         byte[] var10 = new byte[4096];
         boolean var11 = false;
         ByteArrayOutputStream var12 = new ByteArrayOutputStream();

         int var14;
         while((var14 = var9.read(var10, 0, var10.length)) != -1) {
            var12.write(var10, 0, var14);
         }

         byte[] var13 = var12.toByteArray();
         if (var13.length > 0) {
            var3.setInternalAttribute("weblogic.formauth.bytearray", var13);
         } else {
            var3.removeInternalAttribute("weblogic.formauth.bytearray");
         }

         var3.setInternalAttribute("weblogic.formauth.reqheadernames", var4.getRequestHeaders().getHeaderNamesAsArrayList().clone());
         var3.setInternalAttribute("weblogic.formauth.reqheadervalues", var4.getRequestHeaders().getHeaderValuesAsArrayList().clone());
      }
   }
}
