package weblogic.servlet.security;

import java.io.IOException;
import java.security.AccessController;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AppContextHandler;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.services.AppContext;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.RSID;
import weblogic.servlet.internal.session.SessionContext;
import weblogic.servlet.internal.session.SessionData;
import weblogic.servlet.internal.session.SessionInternal;
import weblogic.servlet.security.internal.CertSecurityModule;
import weblogic.servlet.security.internal.SecurityModule;
import weblogic.servlet.security.internal.WebAppContextHandler;
import weblogic.servlet.security.internal.WebAppSecurity;

public final class ServletAuthentication {
   private static final AuthenticatedSubject KERNELID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugLogger DEBUG_IA = DebugLogger.getDebugLogger("DebugWebAppIdentityAssertion");
   private static final DebugLogger DEBUG_SEC = DebugLogger.getDebugLogger("DebugWebAppSecurity");
   private String usernameField;
   private String passwordField;
   public static final int AUTHENTICATED = 0;
   public static final int FAILED_AUTHENTICATION = 1;
   public static final int NEEDS_CREDENTIALS = 2;

   public ServletAuthentication(String var1, String var2) {
      this.usernameField = var1;
      this.passwordField = var2;
   }

   public static void done(HttpServletRequest var0) {
      logout(var0);
   }

   public static boolean logout(HttpServletRequest var0) {
      ServletRequestImpl var1 = ServletRequestImpl.getOriginalRequest(var0);
      WebAppServletContext var2 = var1.getContext();
      HttpServer var3 = var2.getServer();
      SessionInternal var4 = getSessionInternal(var0, var1);
      if (var4 == null) {
         return false;
      } else {
         var4.removeInternalAttribute("weblogic.authuser");
         var4.removeInternalAttribute(SecurityModule.getWLSAuthCookieName(var4));
         String var5 = var4.getIdWithServerInfo();
         WebAppServletContext[] var6 = var3.getServletContextManager().getAllContexts();
         if (var6 != null) {
            ClassLoader var7 = Thread.currentThread().getContextClassLoader();

            try {
               for(int var8 = 0; var8 < var6.length; ++var8) {
                  WebAppServletContext var9 = var6[var8];
                  if (var9 != null && var9 != var2) {
                     Thread.currentThread().setContextClassLoader(var9.getServletClassLoader());
                     SessionContext var10 = var9.getSessionContext();
                     SessionData var11 = var10.getSessionInternal(var5, var1, var1.getResponse());
                     if (var11 != null) {
                        var11.removeInternalAttribute("weblogic.authuser");
                        var11.removeInternalAttribute(SecurityModule.getWLSAuthCookieName((SessionInternal)var11));
                     }
                  }
               }
            } finally {
               Thread.currentThread().setContextClassLoader(var7);
            }
         }

         var3.getSessionLogin().unregister(var4.getInternalId());
         AuthenticatedSubject var16 = SecurityServiceManager.getCurrentSubject(KERNELID);
         if (var16 != null && !SubjectUtils.isUserAnonymous(var16)) {
            SecurityServiceManager.popSubject(KERNELID);
            AuthenticatedSubject var17 = SubjectUtils.getAnonymousSubject();
            SecurityServiceManager.pushSubject(KERNELID, var17);
         }

         return true;
      }
   }

   public static boolean logout(HttpSession var0) {
      if (var0 == null) {
         return false;
      } else {
         SessionInternal var1 = var0 instanceof SessionInternal ? (SessionInternal)var0 : null;
         if (var1 == null) {
            return false;
         } else {
            SessionContext var2 = var1.getContext();
            if (var2 == null) {
               return false;
            } else {
               WebAppServletContext var3 = var2.getServletContext();
               if (var3 == null) {
                  return false;
               } else {
                  HttpServer var4 = var3.getServer();
                  if (var4 == null) {
                     return false;
                  } else {
                     var1.removeInternalAttribute("weblogic.authuser");
                     var1.removeInternalAttribute(SecurityModule.getWLSAuthCookieName(var1));
                     String var5 = var1.getIdWithServerInfo();
                     WebAppServletContext[] var6 = var4.getServletContextManager().getAllContexts();
                     if (var6 != null) {
                        ClassLoader var7 = Thread.currentThread().getContextClassLoader();

                        try {
                           for(int var8 = 0; var8 < var6.length; ++var8) {
                              WebAppServletContext var9 = var6[var8];
                              if (var9 != null && var9 != var3) {
                                 Thread.currentThread().setContextClassLoader(var9.getServletClassLoader());
                                 SessionContext var10 = var9.getSessionContext();
                                 SessionData var11 = var10.getSessionInternal(var5, (ServletRequestImpl)null, (ServletResponseImpl)null);
                                 if (var11 != null) {
                                    var11.removeInternalAttribute("weblogic.authuser");
                                    var11.removeInternalAttribute(SecurityModule.getWLSAuthCookieName((SessionInternal)var11));
                                 }
                              }
                           }
                        } finally {
                           Thread.currentThread().setContextClassLoader(var7);
                        }
                     }

                     var4.getSessionLogin().unregister(var1.getInternalId());
                     AuthenticatedSubject var16 = SecurityServiceManager.getCurrentSubject(KERNELID);
                     if (var16 != null && !SubjectUtils.isUserAnonymous(var16)) {
                        SecurityServiceManager.popSubject(KERNELID);
                        AuthenticatedSubject var17 = SubjectUtils.getAnonymousSubject();
                        SecurityServiceManager.pushSubject(KERNELID, var17);
                     }

                     return true;
                  }
               }
            }
         }
      }
   }

   public static boolean invalidateAll(HttpServletRequest var0) {
      ServletRequestImpl var1 = ServletRequestImpl.getOriginalRequest(var0);
      WebAppServletContext var2 = var1.getContext();
      HttpServer var3 = var2.getServer();
      SessionInternal var4 = getSessionInternal(var0, var1);
      if (var4 == null) {
         return false;
      } else {
         String var5 = var4.getIdWithServerInfo();
         var4.invalidate();
         WebAppServletContext[] var6 = var3.getServletContextManager().getAllContexts();
         if (var6 != null) {
            ClassLoader var7 = Thread.currentThread().getContextClassLoader();

            try {
               for(int var8 = 0; var8 < var6.length; ++var8) {
                  WebAppServletContext var9 = var6[var8];
                  if (var9 != null) {
                     Thread.currentThread().setContextClassLoader(var9.getServletClassLoader());
                     SessionContext var10 = var9.getSessionContext();
                     SessionData var11 = var10.getSessionInternal(var5, var1, var1.getResponse());
                     if (var11 != null) {
                        var9.invalidateSession(var11);
                     }
                  }
               }
            } finally {
               Thread.currentThread().setContextClassLoader(var7);
            }
         }

         var1.getSessionHelper().killOldSession();
         var3.getSessionLogin().unregister(var4.getInternalId());
         AuthenticatedSubject var16 = SecurityServiceManager.getCurrentSubject(KERNELID);
         if (var16 != null && !SubjectUtils.isUserAnonymous(var16)) {
            SecurityServiceManager.popSubject(KERNELID);
            AuthenticatedSubject var17 = SubjectUtils.getAnonymousSubject();
            SecurityServiceManager.pushSubject(KERNELID, var17);
         }

         return true;
      }
   }

   public static void killCookie(HttpServletRequest var0) {
      ServletRequestImpl var1 = ServletRequestImpl.getOriginalRequest(var0);
      var1.getSessionHelper().killOldSession();
      AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(KERNELID);
      if (var2 != null && !SubjectUtils.isUserAnonymous(var2)) {
         SecurityServiceManager.popSubject(KERNELID);
         AuthenticatedSubject var3 = SubjectUtils.getAnonymousSubject();
         SecurityServiceManager.pushSubject(KERNELID, var3);
      }

   }

   public static int strong(HttpServletRequest var0, HttpServletResponse var1) throws ServletException, IOException {
      ServletRequestImpl var2 = ServletRequestImpl.getOriginalRequest(var0);
      String var3 = var2.getContext().getSecurityRealmName();
      return strong(var0, var1, var3);
   }

   public static int strong(HttpServletRequest var0, HttpServletResponse var1, String var2) throws ServletException, IOException {
      try {
         return assertIdentity(var0, var1, var2);
      } catch (LoginException var4) {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug("Login failed for request: " + var0.toString(), var4);
         }

         return 1;
      }
   }

   public static int assertIdentity(HttpServletRequest var0, HttpServletResponse var1, String var2) throws ServletException, IOException, LoginException {
      return assertIdentity(var0, var1, var2, (AppContext)null);
   }

   public static int assertIdentity(HttpServletRequest var0, HttpServletResponse var1, String var2, AppContext var3) throws ServletException, IOException, LoginException {
      ServletRequestImpl var4 = ServletRequestImpl.getOriginalRequest(var0);
      if (var4 == null) {
         throw new IllegalArgumentException("The request wrapper doesn't allow access to original request");
      } else {
         PrincipalAuthenticator var5 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNELID, var2, ServiceType.AUTHENTICATION);
         AuthenticatedSubject var6 = null;

         try {
            CertSecurityModule.Token var7 = CertSecurityModule.findToken(var0, var4.getConnection(), var4.getContext(), var5);
            if (var7 == null) {
               return 2;
            }

            if (DEBUG_IA.isDebugEnabled()) {
               DEBUG_IA.debug("assertIdentity with tokem.type: " + var7.type + " token.value: " + var7.value);
            }

            Object var8 = null;
            if (var3 == null) {
               var8 = WebAppSecurity.getContextHandler(var0, var1);
            } else {
               var8 = AppContextHandler.getInstance(var3);
            }

            var6 = var5.assertIdentity(var7.type, var7.value, (ContextHandler)var8);
         } catch (SecurityException var9) {
            if (DEBUG_IA.isDebugEnabled()) {
               DEBUG_IA.debug("Indentity assertion failed", var9);
            }

            HTTPLogger.logCertAuthenticationError(var0.getRequestURI(), var9);
         }

         if (var6 != null && !SubjectUtils.isUserAnonymous(var6)) {
            SessionInternal var10 = getSessionInternal(var0, var4);
            var10.setInternalAttribute("weblogic.authuser", var6);
            SecurityServiceManager.pushSubject(KERNELID, var6);
            SecurityModule.setupAuthCookie(var4.getContext().getServer(), var0, var10, var10.getInternalId());
            return 0;
         } else {
            return 1;
         }
      }
   }

   public int weak(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter(this.usernameField);
      String var4 = var1.getParameter(this.passwordField);
      return weak(var3, var4, var1);
   }

   /** @deprecated */
   public static int weak(String var0, String var1, HttpServletRequest var2) {
      return weak(var0, var1, var2, (HttpServletResponse)null);
   }

   public static int weak(String var0, String var1, HttpServletRequest var2, HttpServletResponse var3) {
      try {
         return login(var0, var1, var2, var3);
      } catch (LoginException var5) {
         return 1;
      }
   }

   public static int login(String var0, String var1, HttpServletRequest var2, HttpServletResponse var3) throws LoginException {
      ServletRequestImpl var4 = ServletRequestImpl.getOriginalRequest(var2);
      if (var3 == null) {
         var3 = var4.getResponse();
      }

      WebAppServletContext var5 = var4.getContext();
      AuthenticatedSubject var6 = SecurityModule.checkAuthenticate(var0, var1, var2, (HttpServletResponse)var3, var5);
      if (var6 == null) {
         return 1;
      } else {
         SessionInternal var7 = getSessionInternal(var2, var4);
         var5.getServer().getSessionLogin().setUser(var7.getInternalId(), var6);
         var7.setInternalAttribute("weblogic.authuser", var6);
         SecurityServiceManager.pushSubject(KERNELID, var6);
         SecurityModule.setupAuthCookie(var4.getContext().getServer(), var2, var7, var7.getInternalId());
         return 0;
      }
   }

   /** @deprecated */
   public static int weak(String var0, String var1, HttpSession var2) {
      if (var2 != null && var2 instanceof SessionInternal) {
         SessionInternal var3 = (SessionInternal)var2;
         WebAppServletContext var4 = var3.getContext().getServletContext();
         HttpServer var5 = var4.getServer();
         String var6 = var4.getSecurityRealmName();
         AuthenticatedSubject var7 = null;
         PrincipalAuthenticator var8 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNELID, var6, ServiceType.AUTHENTICATION);
         SimpleCallbackHandler var9 = new SimpleCallbackHandler(var0, var1);

         try {
            var7 = var8.authenticate(var9);
         } catch (LoginException var11) {
            if (DEBUG_SEC.isDebugEnabled()) {
               DEBUG_SEC.debug("Login failed", var11);
            }

            return 1;
         }

         if (var7 == null) {
            return 1;
         } else {
            var5.getSessionLogin().setUser(var3.getInternalId(), var7);
            var3.setInternalAttribute("weblogic.authuser", var7);
            SecurityServiceManager.pushSubject(KERNELID, var7);
            return 0;
         }
      } else {
         return 1;
      }
   }

   /** @deprecated */
   public static int authObject(String var0, Object var1, HttpServletRequest var2) {
      HttpSession var3 = var2.getSession(true);
      return var3 == null ? 1 : authObject(var0, var1, var3, var2);
   }

   /** @deprecated */
   public static int authObject(String var0, Object var1, HttpSession var2, HttpServletRequest var3) {
      ServletRequestImpl var4 = ServletRequestImpl.getOriginalRequest(var3);
      WebAppServletContext var5 = var4.getContext();
      AuthenticatedSubject var6 = SecurityModule.checkAuthenticate(var0, var1, var3, var4.getResponse(), var5, false);
      if (var6 == null) {
         return 1;
      } else {
         SessionInternal var7;
         if (var2 != null && var2 instanceof SessionInternal) {
            var7 = (SessionInternal)var2;
         } else {
            var7 = getSessionInternal(var3, var4);
         }

         var5.getServer().getSessionLogin().setUser(var7.getInternalId(), var6);
         var7.setInternalAttribute("weblogic.authuser", var6);
         SecurityServiceManager.pushSubject(KERNELID, var6);
         return 0;
      }
   }

   public static int authenticate(CallbackHandler var0, HttpServletRequest var1) {
      try {
         return login(var0, var1);
      } catch (LoginException var3) {
         if (DEBUG_SEC.isDebugEnabled()) {
            DEBUG_SEC.debug("Login failed for request: " + var1.toString(), var3);
         }

         return 1;
      }
   }

   public static int login(CallbackHandler var0, HttpServletRequest var1) throws LoginException {
      ServletRequestImpl var2 = ServletRequestImpl.getOriginalRequest(var1);
      String var3 = var2.getContext().getSecurityRealmName();
      PrincipalAuthenticator var4 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNELID, var3, ServiceType.AUTHENTICATION);
      AuthenticatedSubject var5 = null;
      var5 = var4.authenticate(var0, new WebAppContextHandler(var1, var2.getResponse()));
      if (var5 == null) {
         return 1;
      } else {
         SessionInternal var6 = getSessionInternal(var1, var2);
         HttpServer var7 = var2.getContext().getServer();
         if (var6 != null) {
            if (var7 != null) {
               var7.getSessionLogin().setUser(var6.getInternalId(), var5);
            }

            var6.setInternalAttribute("weblogic.authuser", var5);
         } else {
            String var8 = var1.getRequestedSessionId();
            if (var8 != null && var5 != null && !SubjectUtils.isUserAnonymous(var5) && !SecurityServiceManager.isKernelIdentity(var5)) {
               var8 = RSID.getID(var8);
               var7.getSessionLogin().setUser(var8, var5);
            }
         }

         SecurityServiceManager.pushSubject(KERNELID, var5);
         SecurityModule.setupAuthCookie(var7, var1, var6, var6.getInternalId());
         return 0;
      }
   }

   public static void generateNewSessionID(HttpServletRequest var0) {
      ServletRequestImpl var1 = ServletRequestImpl.getOriginalRequest(var0);
      var1.getSessionHelper().updateSessionId();
      SessionInternal var2 = getSessionInternal(var0, var1);
      SecurityModule.setupAuthCookie(var1.getContext().getServer(), var0, var2, var2.getInternalId());
   }

   public static Cookie getSessionCookie(HttpServletRequest var0, HttpServletResponse var1) {
      ServletRequestImpl var2 = ServletRequestImpl.getOriginalRequest(var0);
      ServletResponseImpl var3 = var2.getResponse();
      return var3.getCookie(var2.getContext().getSessionContext().getConfigMgr().getCookieName());
   }

   public static void runAs(Subject var0, HttpServletRequest var1) {
      runAs(AuthenticatedSubject.getFromSubject(var0), var1);
   }

   public static void runAs(AuthenticatedSubject var0, HttpServletRequest var1) {
      ServletRequestImpl var2 = ServletRequestImpl.getOriginalRequest(var1);
      WebAppServletContext var3 = var2.getContext();
      HttpServer var4 = var3.getServer();
      SessionInternal var5 = getSessionInternal(var1, var2);
      var5.setInternalAttribute("weblogic.authuser", var0);
      if (var4 != null) {
         var4.getSessionLogin().setUser(var5.getInternalId(), var0);
      }

      SecurityServiceManager.pushSubject(KERNELID, var0);
      SecurityModule.setupAuthCookie(var4, var1, var5, var5.getInternalId());
   }

   public static String getTargetURLForFormAuthentication(HttpSession var0) {
      return ((WebAppServletContext)var0.getServletContext()).getConfigManager().isServletAuthFromURL() ? (String)((SessionInternal)var0).getInternalAttribute("weblogic.formauth.targeturl") : (String)((SessionInternal)var0).getInternalAttribute("weblogic.formauth.targeturi");
   }

   public static String getTargetURIForFormAuthentication(HttpSession var0) {
      return (String)((SessionInternal)var0).getInternalAttribute("weblogic.formauth.targeturi");
   }

   private static SessionInternal getSessionInternal(HttpServletRequest var0, ServletRequestImpl var1) {
      HttpSession var2 = var0.getSession(true);
      SessionInternal var3 = var2 instanceof SessionInternal ? (SessionInternal)var2 : (SessionInternal)var1.getSession(false);
      if (var3 == null) {
         throw new AssertionError("Internal type of session is not available.");
      } else {
         return var3;
      }
   }
}
