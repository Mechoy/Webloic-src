package weblogic.servlet.security.internal;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.management.DeploymentException;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.RequestDispatcherImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.session.SessionInternal;

public final class ServletSecurityManager {
   private static final AuthenticatedSubject KERNEL_ID;
   private final WebAppSecurity webAppSecurity;
   private final WebAppServletContext context;
   private final boolean jaccEnabled = SecurityServiceManager.isJACCEnabled();
   private SecurityModule delegateModule;
   static final long serialVersionUID = -6466139857456605732L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.servlet.security.internal.ServletSecurityManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public ServletSecurityManager(WebAppServletContext var1) throws DeploymentException {
      this.context = var1;
      if (this.jaccEnabled && !var1.isInternalApp() && this.context.getDocroot() != null) {
         this.webAppSecurity = new WebAppSecurityJacc(var1);
      } else {
         this.webAppSecurity = new WebAppSecurityWLS(var1);
      }

      this.delegateModule = SecurityModule.createModule(this.context, this.webAppSecurity);
   }

   public PrincipalAuthenticator getPrincipalAuthenticator() {
      String var1 = this.context.getSecurityRealmName();
      return (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var1, ServiceType.AUTHENTICATION);
   }

   public boolean checkAccess(HttpServletRequest var1, HttpServletResponse var2, boolean var3) throws IOException, ServletException {
      boolean var16;
      boolean var10000 = var16 = _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var17 = null;
      DiagnosticActionState[] var18 = null;
      Object var15 = null;
      if (var10000) {
         Object[] var11 = null;
         if (_WLDF$INST_FLD_Servlet_Check_Access_Around_Medium.isArgumentsCaptureNeeded()) {
            var11 = new Object[]{this, var1, var2, InstrumentationSupport.convertToObject(var3)};
         }

         DynamicJoinPoint var28 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var11, (Object)null);
         DelegatingMonitor var10001 = _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium;
         DiagnosticAction[] var10002 = var17 = var10001.getActions();
         InstrumentationSupport.preProcess(var28, var10001, var10002, var18 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var21 = false;

      Object var12;
      label323: {
         label324: {
            try {
               var21 = true;
               RequestDispatcherImpl var4 = this.invokePreAuthFilters(var1, var2);
               boolean var5 = true;

               boolean var8;
               label312: {
                  boolean var27;
                  label311: {
                     try {
                        ResourceConstraint var6 = this.webAppSecurity.getConstraint(var1);
                        if (!this.webAppSecurity.isFullSecurityDelegationRequired() && var6 != null && var6.isForbidden()) {
                           if (this.webAppSecurity.isFormAuth()) {
                              String var26 = WebAppSecurity.getRelativeURI(var1);
                              if (var26.equals(this.webAppSecurity.getLoginPage()) || var26.equals(this.webAppSecurity.getErrorPage())) {
                                 var5 = true;
                                 var8 = var5;
                                 break label312;
                              }
                           }

                           var2.sendError(403);
                           var5 = false;
                           var27 = var5;
                           break label311;
                        }

                        SessionInternal var7 = this.delegateModule.getUserSession(var1, false);
                        var5 = this.delegateModule.checkAccess(var1, var2, var7, var6, var3);
                        if (var7 != null && var5) {
                           var7 = this.delegateModule.getUserSession(var1, false);
                           this.context.getServer().getSessionLogin().register(var7.getInternalId(), this.context.getContextPath());
                        }
                     } finally {
                        if (var4 != null) {
                           this.invokePostAuthFilters(var1, var2, var4, var5);
                        }

                        var1.removeAttribute("weblogic.auth.result");
                     }

                     var10000 = var5;
                     var21 = false;
                     break label324;
                  }

                  var10000 = var27;
                  var21 = false;
                  break label323;
               }

               var10000 = var8;
               var21 = false;
            } finally {
               if (var21) {
                  var12 = InstrumentationSupport.convertToObject(false);
                  if (var16) {
                     InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var12), _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium, var17, var18);
                  }

               }
            }

            var12 = InstrumentationSupport.convertToObject(var10000);
            if (var16) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var12), _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium, var17, var18);
            }

            return var10000;
         }

         var12 = InstrumentationSupport.convertToObject(var10000);
         if (var16) {
            InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var12), _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium, var17, var18);
         }

         return var10000;
      }

      var12 = InstrumentationSupport.convertToObject(var10000);
      if (var16) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var12), _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium, var17, var18);
      }

      return var10000;
   }

   public WebAppSecurity getWebAppSecurity() {
      return this.webAppSecurity;
   }

   public void setLoginConfig(LoginConfigBean var1) {
      this.webAppSecurity.setLoginConfig(var1);
      this.delegateModule = SecurityModule.createModule(this.context, this.webAppSecurity);
   }

   public void setAuthRealmName(String var1) {
      this.delegateModule.setAuthRealmBanner(var1);
   }

   private RequestDispatcherImpl invokePreAuthFilters(HttpServletRequest var1, HttpServletResponse var2) {
      RequestDispatcherImpl var3 = this.webAppSecurity.getAuthFilterRD();
      if (var3 == null) {
         return null;
      } else {
         var1.setAttribute("weblogic.auth.result", new Integer(-1));
         AuthFilterAction var4 = new AuthFilterAction(var1, var2, var3);
         Throwable var5 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, SubjectUtils.getAnonymousSubject(), var4);
         if (var5 != null) {
            HTTPLogger.logAuthFilterInvocationFailed(this.webAppSecurity.getAuthFilter(), "pre-auth", var1.getRequestURI(), var5);
         }

         var1.removeAttribute("weblogic.auth.result");
         return var3;
      }
   }

   private void invokePostAuthFilters(HttpServletRequest var1, HttpServletResponse var2, RequestDispatcherImpl var3, boolean var4) throws IOException {
      AuthenticatedSubject var5 = null;
      if (var1.getAttribute("weblogic.auth.result") == null) {
         if (var4) {
            var5 = SecurityModule.getCurrentUser(this.context.getServer(), var1);
            var1.setAttribute("weblogic.auth.result", new Integer(0));
         } else {
            var1.setAttribute("weblogic.auth.result", new Integer(1));
         }
      }

      if (var5 == null) {
         var5 = SubjectUtils.getAnonymousSubject();
      }

      AuthFilterAction var6 = new AuthFilterAction(var1, var2, var3);
      Throwable var7 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, var5, var6);
      if (var7 != null) {
         HTTPLogger.logAuthFilterInvocationFailed(this.webAppSecurity.getAuthFilter(), "post-auth", var1.getRequestURI(), var7);
      }

      Integer var8 = (Integer)var1.getAttribute("weblogic.auth.result");
      if (var8 != null && var4 && var8 == 1) {
         var4 = false;
         this.delegateModule.sendError(var1, var2);
      }

   }

   static {
      _WLDF$INST_FLD_Servlet_Check_Access_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Check_Access_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ServletSecurityManager.java", "weblogic.servlet.security.internal.ServletSecurityManager", "checkAccess", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Z)Z", 57, InstrumentationSupport.makeMap(new String[]{"Servlet_Check_Access_Around_Medium"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, InstrumentationSupport.createValueHandlingInfo("ret", (String)null, false, true), new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("req", "weblogic.diagnostics.instrumentation.gathering.ServletRequestRenderer", false, true), null, null})}), (boolean)0);
      KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static class AuthFilterAction implements PrivilegedAction {
      private HttpServletRequest request;
      private HttpServletResponse response;
      private RequestDispatcherImpl dispatcher;

      AuthFilterAction(HttpServletRequest var1, HttpServletResponse var2, RequestDispatcherImpl var3) {
         this.request = var1;
         this.response = var2;
         this.dispatcher = var3;
      }

      public Object run() {
         try {
            this.dispatcher.include(this.request, this.response);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }
}
