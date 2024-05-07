package weblogic.servlet.internal;

import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.login.LoginException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.jsp.JspStub;
import weblogic.servlet.security.internal.WebAppSecurity;

public final class StubSecurityHelper {
   private final ServletStubImpl stub;
   private PrincipalAuthenticator pa = null;
   private AuthenticatedSubject initAs = null;
   private AuthenticatedSubject destroyAs = null;
   private AuthenticatedSubject runAs = null;
   private String runAsIdentity = null;
   private ConcurrentHashMap securityRoleMap;

   public StubSecurityHelper(ServletStubImpl var1) {
      this.stub = var1;
   }

   public final void addRoleLink(String var1, String var2) {
      if (this.securityRoleMap == null) {
         this.securityRoleMap = new ConcurrentHashMap();
      }

      this.securityRoleMap.put(var1, var2);
   }

   public final String getRoleLink(String var1) {
      return this.securityRoleMap == null ? null : (String)this.securityRoleMap.get(var1);
   }

   final Iterator getRoleNames() {
      return this.securityRoleMap == null ? null : this.securityRoleMap.keySet().iterator();
   }

   public Servlet createServlet(Class var1) throws ServletException {
      ServletInitAction var2 = new ServletInitAction(this.stub, var1);
      Throwable var3 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, this.getInitAsSubject(), var2);
      if (var3 instanceof ServletException) {
         throw (ServletException)var3;
      } else if (var3 instanceof Throwable) {
         throw new ServletException(var3);
      } else {
         return var2.getServlet();
      }
   }

   private AuthenticatedSubject getInitAsSubject() {
      if (this.initAs != null) {
         return this.initAs;
      } else {
         return this.runAs != null ? this.runAs : SubjectUtils.getAnonymousSubject();
      }
   }

   public void destroyServlet(Servlet var1) {
      ServletDestroyAction var2 = new ServletDestroyAction(var1, this.stub.getContext());
      Throwable var3 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, this.getDestroyAsSubject(), var2);
      if (var3 != null) {
         HTTPLogger.logServletFailedOnDestroy(this.stub.getContext().getLogContext(), this.stub.getServletName(), var3);
      }

   }

   private AuthenticatedSubject getDestroyAsSubject() {
      if (this.destroyAs != null) {
         return this.destroyAs;
      } else {
         return this.runAs != null ? this.runAs : SubjectUtils.getAnonymousSubject();
      }
   }

   private PrincipalAuthenticator getPrincipalAuthenticator() {
      if (this.pa != null) {
         return this.pa;
      } else {
         this.pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(WebAppConfigManager.KERNEL_ID, this.stub.getContext().getSecurityRealmName(), ServiceType.AUTHENTICATION);
         return this.pa;
      }
   }

   public Throwable invokeServlet(ServletRequest var1, HttpServletRequest var2, ServletRequestImpl var3, ServletResponse var4, HttpServletResponse var5, Servlet var6) throws ServletException {
      ServletServiceAction var7 = new ServletServiceAction(var1, var3, var4, var6, this.stub);
      if (this.runAsIdentity != null) {
         AuthenticatedSubject var8;
         try {
            var8 = this.getPrincipalAuthenticator().impersonateIdentity(this.runAsIdentity, WebAppSecurity.getContextHandler(var2, var5));
         } catch (LoginException var11) {
            Loggable var10 = HTTPLogger.logRunAsUserCouldNotBeResolvedLoggable(this.runAsIdentity, this.stub.getServletName(), this.stub.getContext().getContextPath(), var11);
            var10.log();
            throw new ServletException(var11);
         }

         return (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var8, var7);
      } else {
         return (Throwable)var7.run();
      }
   }

   final String getRunAsIdentity() {
      return this.runAsIdentity;
   }

   final void setRunAsIdentity(String var1) throws DeploymentException {
      try {
         this.runAsIdentity = var1;
         this.runAs = this.getPrincipalAuthenticator().impersonateIdentity(var1);
      } catch (LoginException var4) {
         Loggable var3 = HTTPLogger.logRunAsUserCouldNotBeResolvedLoggable(var1, this.stub.getServletName(), this.stub.getContext().getContextPath(), var4);
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }

      this.checkDeployUserPrivileges(this.runAs, "run-as");
   }

   final void setInitAsIdentity(String var1) throws DeploymentException {
      try {
         this.initAs = this.getPrincipalAuthenticator().impersonateIdentity(var1);
      } catch (LoginException var4) {
         Loggable var3 = HTTPLogger.logRunAsUserCouldNotBeResolvedLoggable(var1, this.stub.getServletName(), this.stub.getContext().getContextPath(), var4);
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }

      this.checkDeployUserPrivileges(this.initAs, "init-as");
   }

   final void setDestroyAsIdentity(String var1) throws DeploymentException {
      try {
         this.destroyAs = this.getPrincipalAuthenticator().impersonateIdentity(var1);
      } catch (LoginException var4) {
         Loggable var3 = HTTPLogger.logRunAsUserCouldNotBeResolvedLoggable(var1, this.stub.getServletName(), this.stub.getContext().getContextPath(), var4);
         var3.log();
         throw new DeploymentException(var3.getMessage());
      }

      this.checkDeployUserPrivileges(this.destroyAs, "destroy-as");
   }

   private void checkDeployUserPrivileges(AuthenticatedSubject var1, String var2) throws DeploymentException {
      if (SubjectUtils.isUserAnAdministrator(var1)) {
         ApplicationContextInternal var3 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
         AuthenticatedSubject var4 = var3.getDeploymentInitiator();
         if (var4 != null && (!var3.isStaticDeploymentOperation() || !SubjectUtils.isUserAnonymous(var4)) && !SubjectUtils.isUserAnAdministrator(var4)) {
            throw new DeploymentException("The " + var2 + " user : " + var1 + " has higher privileges than the deployment " + "user : " + var4 + ". Hence this deployment user " + "cannot perform the current deployment action. Try the " + "deployment action with admin privileged user.");
         }
      }

   }

   private static final class ServletDestroyAction implements PrivilegedAction {
      final Servlet servlet;
      final WebAppServletContext context;

      ServletDestroyAction(Servlet var1, WebAppServletContext var2) {
         this.servlet = var1;
         this.context = var2;
      }

      public Object run() {
         try {
            this.servlet.destroy();
         } catch (Throwable var3) {
            return var3;
         }

         try {
            this.context.getComponentCreator().notifyPreDestroy(this.servlet);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }

   private static final class ServletInitAction implements PrivilegedAction {
      private final ServletStubImpl stub;
      private final Class clazz;
      private Servlet servlet = null;

      public ServletInitAction(ServletStubImpl var1, Class var2) {
         this.stub = var1;
         this.clazz = var2;
      }

      public Servlet getServlet() {
         return this.servlet;
      }

      public Object run() {
         try {
            if (this.stub instanceof JspStub) {
               this.servlet = (Servlet)this.clazz.newInstance();
            } else {
               this.servlet = this.stub.getContext().getComponentCreator().createServletInstance(this.stub.getClassName());
            }
         } catch (NoSuchMethodError var3) {
            HTTPLogger.logInstantiateError(this.stub.getContext().getLogContext(), this.stub.getServletName(), var3);
            return new ServletException("Servlet class: '" + this.stub.getClassName() + "' doesn't have a default constructor");
         } catch (InstantiationException var4) {
            HTTPLogger.logInstantiateError(this.stub.getContext().getLogContext(), this.stub.getServletName(), var4);
            return new ServletException("Servlet class: '" + this.stub.getClassName() + "' couldn't be instantiated");
         } catch (IllegalAccessException var5) {
            HTTPLogger.logIllegalAccessOnInstantiate(this.stub.getContext().getLogContext(), this.stub.getServletName(), var5);
            return new ServletException("Servlet class: '" + this.stub.getClassName() + "' couldn't be instantiated");
         } catch (ClassCastException var6) {
            HTTPLogger.logCastingError(this.stub.getContext().getLogContext(), this.stub.getServletName(), var6);
            return new ServletException("Servlet class: '" + this.stub.getClassName() + "' does not implement javax.servlet.Servlet");
         } catch (Throwable var7) {
            return var7;
         }

         try {
            this.servlet.init(this.stub);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }

   private static final class ServletServiceAction implements PrivilegedAction {
      private final ServletRequest req;
      private final ServletRequestImpl reqi;
      private final ServletResponse rsp;
      private final Servlet servlet;
      private final ServletStubImpl stub;

      ServletServiceAction(ServletRequest var1, ServletRequestImpl var2, ServletResponse var3, Servlet var4, ServletStubImpl var5) {
         this.req = var1;
         this.reqi = var2;
         this.rsp = var3;
         this.servlet = var4;
         this.stub = var5;
      }

      public Object run() {
         try {
            if (this.stub == this.reqi.getServletStub() && this.stub.isFutureResponseServlet()) {
               this.reqi.enableFutureResponse();
            }

            this.servlet.service(this.req, this.rsp);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }
}
