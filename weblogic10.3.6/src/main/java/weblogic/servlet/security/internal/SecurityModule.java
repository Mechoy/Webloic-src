package weblogic.servlet.security.internal;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.security.auth.login.LoginException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.WebAppContainerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.Salt;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ErrorMessages;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppConfigManager;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.HTTPSessionLogger;
import weblogic.servlet.internal.session.RSID;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.internal.session.SessionData;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.StringUtils;
import weblogic.utils.encoders.BASE64Encoder;

public abstract class SecurityModule {
   public static final String SESSION_AUTH_USER = "weblogic.authuser";
   public static final String SESSION_FORM_URL = "weblogic.formauth.targeturl";
   public static final String SESSION_FORM_URI = "weblogic.formauth.targeturi";
   public static final int AUTHENTICATED = 0;
   public static final int FAILED_AUTHENTICATION = 1;
   public static final int NEEDS_CREDENTIALS = 2;
   static final String SESSION_FORM_METHOD = "weblogic.formauth.method";
   static final String SESSION_FORM_QUERY = "weblogic.formauth.queryparams";
   static final String SESSION_FORM_BYTEARRAY = "weblogic.formauth.bytearray";
   static final String SESSION_FORM_REQHEADNAMES = "weblogic.formauth.reqheadernames";
   static final String SESSION_FORM_REQHEADVALUES = "weblogic.formauth.reqheadervalues";
   static final String SESSION_POST_COOKIE = "weblogic.formauth.postcookie";
   static final String SESSION_FORM_IMMEDIATE = "weblogic.formauth.immediate";
   public static final String REQUEST_AUTH_RESULT = "weblogic.auth.result";
   public static final int REQUEST_PRE_AUTH = -1;
   protected static int AUTH_COOKIE_ID_LENGTH = 20;
   public static final String ASSERTION_AUTH = "ASSERTION";
   public static final String REALM_AUTH = "REALM";
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected final WebAppServletContext servletContext;
   protected final WebAppSecurity webAppSecurity;
   protected static final DebugLogger DEBUG_SEC = DebugLogger.getDebugLogger("DebugWebAppSecurity");
   protected String authRealmBanner;
   protected boolean delegateControl;
   private static final WebAppContainerMBean webAppContainer;
   private static boolean changeSessionIdOnAuthentication;

   public SecurityModule(WebAppServletContext var1, WebAppSecurity var2) {
      this.authRealmBanner = null;
      this.delegateControl = false;
      this.servletContext = var1;
      this.webAppSecurity = var2;
   }

   public SecurityModule(WebAppServletContext var1, WebAppSecurity var2, boolean var3) {
      this(var1, var2);
      this.delegateControl = var3;
   }

   HttpServer getHttpServer() {
      return this.servletContext.getServer();
   }

   WebAppServletContext getServletContext() {
      return this.servletContext;
   }

   boolean isReloginEnabled() {
      return this.servletContext.getConfigManager().isReloginEnabled();
   }

   boolean checkAccess(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, boolean var5) throws IOException, ServletException {
      if (!this.webAppSecurity.checkTransport(var4, var1, var2)) {
         return false;
      } else {
         AuthenticatedSubject var6 = getCurrentUser(this.getHttpServer(), var1, var3);
         return this.checkUserPerm(var1, var2, var3, var4, var6, var5);
      }
   }

   abstract boolean checkUserPerm(HttpServletRequest var1, HttpServletResponse var2, SessionInternal var3, ResourceConstraint var4, AuthenticatedSubject var5, boolean var6) throws IOException, ServletException;

   public static final AuthenticatedSubject getCurrentUser(HttpServer var0, HttpServletRequest var1) {
      SessionInternal var2 = (SessionInternal)var1.getSession(false);
      return getCurrentUser(var0, var1, var2);
   }

   public static String getWLSAuthCookieName(SessionInternal var0) {
      return var0 == null ? "_WL_AUTHCOOKIE_JSESSIONID" : var0.getContext().getConfigMgr().getWLSAuthCookieName();
   }

   private static String getWLSAuthCookieName(WebAppServletContext var0) {
      return var0.getSessionContext().getConfigMgr().getWLSAuthCookieName();
   }

   public static final AuthenticatedSubject getCurrentUser(HttpServer var0, HttpServletRequest var1, SessionInternal var2) {
      AuthenticatedSubject var3 = null;

      String var5;
      try {
         String var4;
         if (var2 != null) {
            var4 = var2.getInternalId();
            var3 = var0.getSessionLogin().getUser(var4);
            if (var3 != null) {
               var2.setInternalAttribute("weblogic.authuser", var3);
            } else {
               var3 = (AuthenticatedSubject)var2.getInternalAttribute("weblogic.authuser");
               if (var3 != null) {
                  var0.getSessionLogin().setUser(var4, var3);
               }
            }

            var5 = getWLSAuthCookieName(var2);
            String var6 = (String)var2.getInternalAttribute(var5);
            if (var6 == null) {
               var6 = var0.getSessionLogin().getCookieId(var4);
               if (var6 != null) {
                  var2.setInternalAttribute(var5, var6);
               }
            } else {
               var0.getSessionLogin().addCookieId(var4, var6);
            }
         } else {
            var4 = var1.getRequestedSessionId();
            if (var4 != null) {
               var4 = RSID.getID(var4);
               var3 = var0.getSessionLogin().getUser(var4);
            }
         }
      } catch (IllegalStateException var7) {
         var5 = var2 == null ? "null" : var2.getInternalId();
         HTTPSessionLogger.logSessionExpired(var5, var7);
      }

      return var3;
   }

   public static final AuthenticatedSubject checkAuthenticate(String var0, Object var1, HttpServletRequest var2, HttpServletResponse var3, WebAppServletContext var4, boolean var5) {
      try {
         return checkAuthenticate(var0, var1, var2, var3, var4);
      } catch (LoginException var9) {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug("Login failed for request: " + var2.toString(), var9);
         }

         if (var5) {
            var2.setAttribute("javax.servlet.error.exception_type", var9.getClass());
            var2.setAttribute("javax.servlet.error.exception", var9);
            var2.setAttribute("javax.servlet.error.message", var9.getMessage());
            SessionInternal var7 = (SessionInternal)var2.getSession(false);
            if (var7 != null) {
               String var8 = (String)var7.getInternalAttribute("weblogic.formauth.targeturi");
               var2.setAttribute("javax.servlet.error.request_uri", var8 == null ? var2.getRequestURI() : var8);
            }

            var2.setAttribute("javax.servlet.jsp.jspException", var9);
            var2.setAttribute("javax.servlet.error.status_code", new Integer(403));
         }

         return null;
      }
   }

   public static AuthenticatedSubject checkAuthenticate(String var0, Object var1, HttpServletRequest var2, HttpServletResponse var3, WebAppServletContext var4) throws LoginException {
      HttpServer var5 = var4.getServer();
      String var6 = var4.getSecurityRealmName();
      String var7 = var4.getLogContext();
      SessionInternal var8 = (SessionInternal)var2.getSession(false);
      final AuthenticatedSubject var9 = null;
      var9 = getCurrentUser(var5, var2, var8);
      if (var9 != null) {
         if (var0 == null || var0.equals(SubjectUtils.getUsername(var9))) {
            return var9;
         }

         logout(var5, var8);
      }

      if (var0 == null) {
         return null;
      } else {
         ServletCallbackHandler var10 = new ServletCallbackHandler(var0, var1, var2, var3);
         PrincipalAuthenticator var11 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var6, ServiceType.AUTHENTICATION);
         var9 = var11.authenticate(var10, new WebAppContextHandler(var2, var3));
         if (var9 != null) {
            final PasswordCredential var12 = new PasswordCredential(var0, (String)var1);
            AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  var9.getPrivateCredentials(SecurityModule.KERNEL_ID).add(var12);
                  return null;
               }
            });
         }

         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug(var7 + " authenticated user: " + getUsername(var9));
         }

         return var9;
      }
   }

   protected static final String getUsername(AuthenticatedSubject var0) {
      return var0 == null ? "anonymous" : SubjectUtils.getUsername(var0);
   }

   void setAuthRealmBanner(String var1) {
      this.authRealmBanner = "Basic realm=\"" + var1 + "\"";
   }

   public static void setAuthCookieIDLength(int var0) {
      AUTH_COOKIE_ID_LENGTH = var0;
   }

   static final void logout(HttpServer var0, SessionInternal var1) {
      if (var1 != null) {
         var0.getSessionLogin().unregister(var1.getInternalId());
         var1.removeInternalAttribute("weblogic.authuser");
      }
   }

   final void login(HttpServletRequest var1, AuthenticatedSubject var2, SessionInternal var3) {
      if (var2 != null && !SubjectUtils.isUserAnonymous(var2) && !SecurityServiceManager.isKernelIdentity(var2)) {
         HttpServer var4 = this.getHttpServer();
         if (var3 == null) {
            var3 = this.getUserSession(var1, true);
         } else if (!((HttpSession)var3).isNew() && changeSessionIdOnAuthentication) {
            var3 = this.generateNewSession(var1);
         }

         var3.setInternalAttribute("weblogic.authuser", var2);
         String var5 = var3.getInternalId();
         var4.getSessionLogin().setUser(var5, var2);
         setupAuthCookie(var4, var1, var3, var5, true);
      }
   }

   private final SessionInternal generateNewSession(HttpServletRequest var1) {
      ServletRequestImpl var2 = ServletRequestImpl.getOriginalRequest(var1);
      var2.getSessionHelper().updateSessionId();
      return this.getUserSession(var1, true);
   }

   public static final void setupAuthCookie(HttpServer var0, HttpServletRequest var1, SessionInternal var2, String var3) {
      setupAuthCookie(var0, var1, var2, var3, false);
   }

   private static final void setupAuthCookie(HttpServer var0, HttpServletRequest var1, SessionInternal var2, String var3, boolean var4) {
      if (var0.isAuthCookieEnabled()) {
         if (var1.isSecure()) {
            String var5 = var0.getSessionLogin().getCookieId(var3);
            String var6 = getWLSAuthCookieName(var2);
            if (!var4 && var5 != null) {
               var2.setInternalAttribute(var6, var5);
            } else {
               if (var5 == null) {
                  var5 = (String)var2.getInternalAttribute(var6);
                  if (!var4 && var5 != null) {
                     var0.getSessionLogin().addCookieId(var3, var5);
                     return;
                  }
               }

               boolean var7 = var5 != null;
               ServletResponseImpl var8 = ServletRequestImpl.getOriginalRequest(var1).getResponse();
               var5 = generateNewId();
               var2.setInternalAttribute(var6, var5);
               if (var4 && var7) {
                  ServletRequestImpl var9 = ServletRequestImpl.getOriginalRequest(var1);
                  WebAppServletContext[] var10 = var0.getServletContextManager().getAllContexts();

                  for(int var11 = 0; var11 < var10.length; ++var11) {
                     WebAppServletContext var12 = var10[var11];
                     if (!var12.equals(var2.getContext().getServletContext()) && getWLSAuthCookieName(var12).equals(var6)) {
                        SessionContext var13 = var12.getSessionContext();
                        SessionData var14 = var13.getSessionInternalForAuthentication(var3, var9, var8);
                        if (var14 != null) {
                           if (var14.getInternalAttribute(var6) != null) {
                              var14.removeInternalAttribute(var6);
                           }

                           synchronized(var14) {
                              var13.sync(var14);
                           }
                        }
                     }
                  }
               }

               Cookie var18 = new Cookie(var6, var5);
               var18.setSecure(true);
               var18.setMaxAge(-1);
               var18.setPath(var2.getContext().getConfigMgr().getCookiePath());
               String var19 = var2.getContext().getConfigMgr().getCookieDomain();
               if (var19 != null) {
                  var18.setDomain(var19);
               }

               var8.addCookieInternal(var18);
               var0.getSessionLogin().addCookieId(var3, var5);
            }
         }
      }
   }

   private static String generateNewId() {
      BASE64Encoder var0 = new BASE64Encoder();
      String var1 = new String(var0.encodeBuffer(Salt.getRandomBytes(AUTH_COOKIE_ID_LENGTH)));
      var1 = var1.substring(0, AUTH_COOKIE_ID_LENGTH);
      var1 = var1.replace('/', '.');
      var1 = var1.replace('+', '-');
      var1 = var1.replace('=', '_');
      return var1;
   }

   boolean checkAuthCookie(HttpServer var1, HttpServletRequest var2, SessionInternal var3) {
      if (var1.isAuthCookieEnabled() && var2.isSecure()) {
         if (!this.getServletContext().getSessionContext().getConfigMgr().isSessionCookiesEnabled()) {
            return true;
         } else if (this.webAppSecurity.getConstraint(var2) == null) {
            return true;
         } else {
            String var4 = null;
            String var5 = getWLSAuthCookieName(var3);
            if (var3 != null) {
               var4 = var1.getSessionLogin().getCookieId(var3.getInternalId());
               if (var4 != null) {
                  var3.setInternalAttribute(var5, var4);
               } else {
                  var4 = (String)var3.getInternalAttribute(var5);
               }
            }

            if (var4 == null) {
               return true;
            } else {
               Cookie[] var6 = var2.getCookies();
               Cookie var8;
               if (var6 != null) {
                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     if (var6[var7].getName().equals(var5)) {
                        var8 = var6[var7];
                        if (var8.getValue().equals(var4)) {
                           return true;
                        }
                     }
                  }
               }

               ServletResponseImpl var9 = ServletRequestImpl.getOriginalRequest(var2).getResponse();
               var8 = var9.getCookie(var5);
               return var8 != null && var8.getValue().equals(var4);
            }
         }
      } else {
         return true;
      }
   }

   void sendForbiddenResponse(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      if (!this.delegateControl) {
         var2.sendError(403, ErrorMessages.getErrorPage(403));
      }
   }

   void sendUnauthorizedResponse(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      if (!this.delegateControl) {
         var2.sendError(401, ErrorMessages.getErrorPage(401));
      }
   }

   void sendError(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      if (!this.delegateControl) {
         ServletResponseImpl var3 = ServletRequestImpl.getOriginalRequest(var1).getResponse();
         var3.setHeaderInternal("WWW-Authenticate", this.authRealmBanner);
         this.sendUnauthorizedResponse(var1, var2);
      }
   }

   boolean isForbidden(ResourceConstraint var1) {
      return this.webAppSecurity.isFullSecurityDelegationRequired() && var1 != null && var1.isForbidden();
   }

   final SessionInternal getUserSession(HttpServletRequest var1, boolean var2) {
      AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (SecurityServiceManager.isKernelIdentity(var3)) {
         SessionRetrievalAction var4 = new SessionRetrievalAction(var1, var2);
         Throwable var5 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, SubjectUtils.getAnonymousSubject(), var4);
         if (var5 != null) {
            if (var5 instanceof NestedRuntimeException) {
               throw (NestedRuntimeException)var5;
            } else {
               HTTPSessionLogger.logUnexpectedError(this.getServletContext().getLogContext(), var5);
               throw new NestedRuntimeException("Failed to retrieve session: " + var5.getMessage(), var5);
            }
         } else {
            return var4.getUserSession();
         }
      } else {
         return (SessionInternal)var1.getSession(var2);
      }
   }

   protected void invokeAuthFilterChain(HttpServletRequest var1, HttpServletResponse var2) throws ServletException {
      if (this.webAppSecurity.hasAuthFilters()) {
         FilterChain var3 = this.webAppSecurity.getAuthFilterChain();
         ServletAuthenticationFilterAction var4 = new ServletAuthenticationFilterAction(var1, var2, var3);
         Throwable var5 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, var4);
         if (var5 != null) {
            throw new ServletException(var5);
         }
      }
   }

   private static String[] validateAuthMethods(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("NULL auth-method list");
      } else {
         String[] var1 = StringUtils.splitCompletely(var0, ", ");

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!var1[var2].equals("BASIC") && !var1[var2].equals("FORM") && !var1[var2].equals("CLIENT_CERT") && !var1[var2].equals("DIGEST") && !var1[var2].equals("ASSERTION") && !var1[var2].equals("BASIC_ENFORCE") && !var1[var2].equals("BASIC_PLAIN")) {
               throw new IllegalArgumentException("Invalid auth-method list - " + var0);
            }

            if ((var1[var2].equals("BASIC") || var1[var2].equals("BASIC_ENFORCE") || var1[var2].equals("BASIC_PLAIN") || var1[var2].equals("FORM")) && var2 != var1.length - 1) {
               throw new IllegalArgumentException("Invalid auth-method list - '" + var1[var2] + " ' has to be at the end in '" + var0 + "'");
            }
         }

         return var1;
      }
   }

   static SecurityModule createModule(WebAppServletContext var0, WebAppSecurity var1) {
      String var2 = null;
      if (var1 != null) {
         var2 = var1.getAuthMethod();
      }

      if (var2 == null || var2.length() < 1) {
         var2 = "BASIC";
      }

      return createModule(var0, var1, false, var2);
   }

   static SecurityModule createModule(WebAppServletContext var0, WebAppSecurity var1, boolean var2, String var3) {
      Object var4;
      if (var3.equals("BASIC")) {
         var4 = new BasicSecurityModule(var0, var1, var2);
      } else if (var3.equals("FORM")) {
         var4 = new FormSecurityModule(var0, var1);
      } else if (var3.equals("CLIENT_CERT")) {
         var4 = new CertSecurityModule(var0, var1, var2, false);
      } else if (var3.equals("DIGEST")) {
         HTTPLogger.logDigestAuthNotSupported(var0.getLogContext());
         var4 = new BasicSecurityModule(var0, var1, var2);
      } else if (var3.equals("ASSERTION")) {
         var4 = new CertSecurityModule(var0, var1, var2, true);
      } else if (!var3.equals("BASIC_ENFORCE") && !var3.equals("BASIC_PLAIN")) {
         if (var3.equals("REALM")) {
            var0.getConfigManager();
            var4 = new ChainedSecurityModule(var0, var1, validateAuthMethods(WebAppConfigManager.getRealmAuthMethods()));
         } else {
            var4 = new ChainedSecurityModule(var0, var1, validateAuthMethods(var3));
         }
      } else {
         var4 = new Basic2SecurityModule(var0, var1, var2, var3);
      }

      ((SecurityModule)var4).setAuthRealmBanner(var0.getConfigManager().getAuthRealmName());
      if (DEBUG_SEC.isDebugEnabled()) {
         DEBUG_SEC.debug(var0 + " creating " + var4);
      }

      return (SecurityModule)var4;
   }

   static {
      webAppContainer = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().getWebAppContainer();
      changeSessionIdOnAuthentication = webAppContainer.isChangeSessionIDOnAuthentication();
      String var0 = System.getProperty("changeSessionIdOnAuthentication");
      if (var0 != null) {
         changeSessionIdOnAuthentication = new Boolean(var0);
      }

   }

   private static class ServletAuthenticationFilterAction implements PrivilegedAction {
      private final HttpServletRequest request;
      private final HttpServletResponse response;
      private final FilterChain chain;

      ServletAuthenticationFilterAction(HttpServletRequest var1, HttpServletResponse var2, FilterChain var3) {
         this.request = var1;
         this.response = var2;
         this.chain = var3;
      }

      public Object run() {
         try {
            this.chain.doFilter(this.request, this.response);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }

   private static class SessionRetrievalAction implements PrivilegedAction {
      private final HttpServletRequest request;
      private final boolean flag;
      private SessionInternal session = null;

      SessionRetrievalAction(HttpServletRequest var1, boolean var2) {
         this.request = var1;
         this.flag = var2;
      }

      public SessionInternal getUserSession() {
         return this.session;
      }

      public Object run() {
         try {
            this.session = (SessionInternal)this.request.getSession(this.flag);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }
}
