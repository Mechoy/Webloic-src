package weblogic.servlet.security.internal;

import java.io.IOException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.application.SecurityRole;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;
import weblogic.j2ee.descriptor.wl.RunAsRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.ServletDescriptorBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityApplicationInfo;
import weblogic.security.service.SecurityApplicationInfoImpl;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.spi.ApplicationInfo.ComponentType;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ProtocolHandlerHTTPS;
import weblogic.servlet.internal.RequestDispatcherImpl;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WebAppModule;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.http.HttpParsing;

public abstract class WebAppSecurity {
   protected static final String NONE = "NONE";
   protected static final String INTEGRAL = "INTEGRAL";
   protected static final String CONFIDENTIAL = "CONFIDENTIAL";
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected final WebAppServletContext context;
   protected final HashSet roles = new HashSet();
   protected final HashMap roleMapping = new HashMap();
   protected final HashMap runAsMapping = new HashMap();
   private final Filter[] authFilters;
   private final boolean authFiltersPresent;
   protected SecurityApplicationInfo secureAppInfo;
   private String loginPage = null;
   private String errorPage = null;
   private String authMethod = null;
   private boolean formAuth = false;
   private String authFilter;
   private RequestDispatcherImpl authFilterRD;
   private String realmName;
   private int roleMappingMode;
   private ExternalRoleChecker externalRoleChecker;

   WebAppSecurity(WebAppServletContext var1) throws DeploymentException {
      this.context = var1;
      PrincipalAuthenticator var2 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, this.context.getSecurityRealmName(), ServiceType.AUTHENTICATION);

      try {
         this.authFilters = var2.getServletAuthenticationFilters(var1);
      } catch (ServletException var4) {
         throw new DeploymentException(var4);
      }

      this.authFiltersPresent = this.authFilters != null && this.authFilters.length > 0;
      AppDeploymentMBean var3 = null;
      if (this.context.getApplicationContext() != null) {
         var3 = this.context.getApplicationContext().getAppDeploymentMBean();
      }

      this.secureAppInfo = new SecurityApplicationInfoImpl(var3, ComponentType.WEBAPP, var1.getContextPath());
      this.realmName = this.context.getApplicationContext().getApplicationSecurityRealmName();
      this.roleMappingMode = SecurityServiceManager.getRoleMappingBehavior(this.realmName, this.secureAppInfo);
      this.externalRoleChecker = new ExternalRoleCheckerManager(this.context);
   }

   public void unregister() {
      PrincipalAuthenticator var1 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, this.context.getSecurityRealmName(), ServiceType.AUTHENTICATION);
      var1.destroyServletAuthenticationFilters(this.authFilters);
   }

   protected abstract boolean isFullSecurityDelegationRequired();

   protected abstract boolean isJaccEnabled();

   protected abstract void deployRoles() throws DeploymentException;

   protected abstract void deployPolicies(SecurityConstraintBean[] var1) throws DeploymentException;

   protected abstract void deployRoleLink(ServletStubImpl var1, String var2, String var3) throws DeploymentException;

   public abstract ResourceConstraint getConstraint(HttpServletRequest var1);

   public abstract boolean hasPermission(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3, ResourceConstraint var4);

   public abstract boolean isSubjectInRole(AuthenticatedSubject var1, String var2, WebAppContextHandler var3, ServletStubImpl var4);

   public abstract void registerRoleRefs(ServletStubImpl var1) throws DeploymentException;

   public abstract void start() throws DeploymentException;

   abstract boolean checkTransport(ResourceConstraint var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException;

   public abstract boolean isSSLRequired(String var1, String var2);

   public abstract void initContextHandler(ServletRequestImpl var1);

   public abstract void resetContextHandler();

   final String getContextLog() {
      return this.context.getLogContext();
   }

   final String getContextName() {
      return this.context.getName() == null ? "Default WebApplication" : this.context.getName();
   }

   boolean hasAuthFilters() {
      return this.authFiltersPresent;
   }

   public FilterChain getAuthFilterChain() {
      return new AuthFilterChain(this.authFilters, this.context);
   }

   public final void registerSecurityConstraints(SecurityConstraintBean[] var1) throws DeploymentException {
      this.deployPolicies(var1);
      this.deployRoles();
   }

   public final void registerSecurityRoles(WebAppModule var1) throws DeploymentException {
      if (var1 != null && var1.getWebAppBean() != null) {
         SecurityRoleBean[] var2 = var1.getWebAppBean().getSecurityRoles();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               this.roles.add(var2[var3].getRoleName());
            }
         }

         if (var1.getWlWebAppBean() != null) {
            this.setRoleMapping(var1.getWlWebAppBean().getSecurityRoleAssignments());
            RunAsRoleAssignmentBean[] var7 = var1.getWlWebAppBean().getRunAsRoleAssignments();
            if (var7 != null) {
               for(int var4 = 0; var4 < var7.length; ++var4) {
                  RunAsRoleAssignmentBean var5 = var7[var4];
                  if (this.roles.contains(var5.getRoleName())) {
                     this.runAsMapping.put(var5.getRoleName(), var5.getRunAsPrincipalName());
                  } else if (!this.externalRoleChecker.isExternalRole(var5.getRoleName())) {
                     Loggable var6 = HTTPLogger.logUndefinedSecurityRoleLoggable(var5.getRoleName(), "run-as-role-assignment");
                     var6.log();
                     throw new DeploymentException(var6.getMessage());
                  }
               }
            }
         }

      }
   }

   private final void setRoleMapping(SecurityRoleAssignmentBean[] var1) throws DeploymentException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getRoleName();
            if (this.roles.contains(var3)) {
               if (var1[var2].getExternallyDefined() != null) {
                  String[] var4 = new String[]{null};
                  this.roleMapping.put(var3, var4);
               } else if (var1[var2].getPrincipalNames() != null && var1[var2].getPrincipalNames().length > 0) {
                  this.roleMapping.put(var3, var1[var2].getPrincipalNames());
               }
            } else if (!this.externalRoleChecker.isExternalRole(var3)) {
               Loggable var5 = HTTPLogger.logBadSecurityRoleInSRALoggable(var3);
               var5.log();
               throw new DeploymentException(var5.getMessage());
            }
         }

      }
   }

   public final String getRunAsIdentity(String var1) {
      return (String)this.runAsMapping.get(var1);
   }

   public final String getFirstPrincipal(String var1) {
      String[] var2 = (String[])((String[])this.roleMapping.get(var1));
      return var2 != null && var2.length >= 1 ? var2[0] : null;
   }

   final void setLoginConfig(LoginConfigBean var1) {
      if (var1.getFormLoginConfig() != null) {
         if (var1.getFormLoginConfig().getFormLoginPage() != null) {
            this.loginPage = HttpParsing.ensureStartingSlash(var1.getFormLoginConfig().getFormLoginPage());
         }

         if (var1.getFormLoginConfig().getFormErrorPage() != null) {
            this.errorPage = HttpParsing.ensureStartingSlash(var1.getFormLoginConfig().getFormErrorPage());
         }
      }

      this.authMethod = this.initAuthMethod(var1.getAuthMethod());
      this.formAuth = this.authMethod.toUpperCase(Locale.ENGLISH).contains("FORM");
   }

   private String initAuthMethod(String var1) {
      if (var1 == null) {
         return "BASIC";
      } else if (var1.equalsIgnoreCase("BASIC")) {
         return "BASIC";
      } else if (var1.equalsIgnoreCase("FORM")) {
         return "FORM";
      } else if (var1.equalsIgnoreCase("CLIENT-CERT")) {
         return "CLIENT_CERT";
      } else if (var1.equalsIgnoreCase("DIGEST")) {
         return "DIGEST";
      } else {
         return var1.toUpperCase(Locale.ENGLISH).contains("CLIENT-CERT") ? var1.toUpperCase(Locale.ENGLISH).replaceAll("CLIENT-CERT", "CLIENT_CERT") : var1;
      }
   }

   public final String getLoginPage() {
      return this.loginPage;
   }

   public final String getErrorPage() {
      return this.errorPage;
   }

   public final String getAuthMethod() {
      return this.authMethod;
   }

   final WebAppServletContext getContext() {
      return this.context;
   }

   final RequestDispatcherImpl getAuthFilterRD() {
      return this.authFilterRD;
   }

   public final String getAuthFilter() {
      return this.authFilter;
   }

   public final boolean isFormAuth() {
      return this.formAuth;
   }

   public final void setAuthFilter(String var1) {
      this.authFilter = var1;
      ServletStubImpl var2 = new ServletStubImpl(var1, var1, this.context, (Map)null);
      this.authFilterRD = new RequestDispatcherImpl(var2, this.context, -1);
      this.authFilterRD.disableFilters();
   }

   public final void registerSecurityRoleRef(ServletStubImpl var1, SecurityRoleRefBean[] var2) throws DeploymentException {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            SecurityRoleRefBean var4 = var2[var3];
            String var5 = var4.getRoleName();
            String var6 = var4.getRoleLink();
            if (var5 != null && var6 != null) {
               this.deployRoleLink(var1, var5, var6);
            }
         }

      }
   }

   protected final String getSecuredURL(HttpServletRequest var1, HttpServletResponse var2, String var3) {
      String var4 = var1.getServerName();
      int var5 = this.context.getServer().getFrontendHTTPSPort();
      if (var5 == 0) {
         ServerChannel var6 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerHTTPS.PROTOCOL_HTTPS);
         if (var6 == null) {
            return null;
         }

         var5 = var6.getPublicPort();
      }

      ServletResponseImpl var10 = ServletResponseImpl.getOriginalResponse(var2);
      String var7 = var10.processProxyPathHeaders(var3);
      String var8 = var1.getQueryString();
      StringBuffer var9 = new StringBuffer();
      if (var5 == 443) {
         var9.append("https://").append(var4).append(var7);
         if (var8 != null && var8.length() > 1) {
            var9.append("?").append(var8);
         }
      } else {
         var9.append("https://").append(var4).append(":");
         var9.append(var5).append(var7);
         if (var8 != null && var8.length() > 1) {
            var9.append("?").append(var8);
         }
      }

      return var9.toString();
   }

   public static final String getRelativeURI(HttpServletRequest var0) {
      String var1 = (String)var0.getAttribute("webflow_resource");
      if (var1 != null) {
         return var1;
      } else if (var0 instanceof ServletRequestImpl) {
         return ((ServletRequestImpl)var0).getRelativeUri();
      } else {
         var1 = ServletRequestImpl.getResolvedURI(var0);
         String var2 = ServletRequestImpl.getResolvedContextPath(var0);
         return var2 != null && var2.length() > 0 && var1.startsWith(var2) ? var1.substring(var2.length()) : var1;
      }
   }

   public static final ContextHandler getContextHandler(HttpServletRequest var0, HttpServletResponse var1) {
      WebAppContextHandler var2;
      if (var0 instanceof ServletRequestImpl) {
         ServletRequestImpl var3 = (ServletRequestImpl)var0;
         var2 = var3.getSecurityContextHandler();
      } else {
         var2 = new WebAppContextHandler(var0, var1);
      }

      return var2;
   }

   public static String fixupURLPattern(String var0) {
      if (isDefaultUrlPattern(var0)) {
         return "/";
      } else {
         return !var0.startsWith("*.") ? HttpParsing.ensureStartingSlash(var0) : var0;
      }
   }

   private static boolean isDefaultUrlPattern(String var0) {
      if (var0.length() > 2) {
         return false;
      } else if (getEnforceStrictURLPattern()) {
         return var0.equals("/");
      } else {
         return var0.equals("*") || var0.equals("/");
      }
   }

   protected boolean isExternallyDefined(String[] var1) {
      return var1 != null && var1.length == 1 && (var1[0] == null || var1[0].length() == 0);
   }

   public boolean isCompatibilitySecMode() {
      return this.roleMappingMode == 0;
   }

   public boolean isApplicationSecMode() {
      return this.roleMappingMode == 1;
   }

   public boolean isExternallyDefinedSecMode() {
      return this.roleMappingMode == 2;
   }

   public String getRunAsPrincipalName(ServletDescriptorBean var1, String var2) throws DeploymentException {
      String var3;
      if (var1 != null && (var3 = var1.getRunAsPrincipalName()) != null) {
         return var3;
      } else if ((var3 = this.getRunAsIdentity(var2)) != null) {
         return var3;
      } else if ((var3 = this.getFirstPrincipal(var2)) != null) {
         HTTPLogger.logImplicitMappingForRunAsRole(this.context.getLogContext(), "run-as", var2, "web.xml", var3);
         return var3;
      } else {
         if (!this.isCompatibilitySecMode()) {
            SecurityRole var4 = this.context.getApplicationContext().getSecurityRole(var2);
            if (var4 != null) {
               String[] var5 = var4.getPrincipalNames();
               if (!var4.isExternallyDefined() && var5 != null && var5.length > 0) {
                  return var5[0];
               }
            }

            if (this.isApplicationSecMode()) {
               throw new DeploymentException("Cannot resolve role-Name " + var2);
            }
         }

         HTTPLogger.logImplicitMappingForRunAsRoleToSelf(this.context.getLogContext(), "run-as", var2, "web.xml");
         return var2;
      }
   }

   protected boolean checkAdminMode(AuthenticatedSubject var1) {
      return var1 == null ? false : SubjectUtils.isUserInAdminRoles(var1, new String[]{"Admin", "AppTester"});
   }

   public static final boolean getEnforceStrictURLPattern() {
      return SecurityServiceManager.getEnforceStrictURLPattern();
   }

   public static final boolean getEnforceValidBasicAuthCredentials() {
      return SecurityServiceManager.getEnforceValidBasicAuthCredentials();
   }
}
