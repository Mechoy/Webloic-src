package weblogic.servlet.security.internal;

import java.io.IOException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.application.SecurityRole;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.WebResourceCollectionBean;
import weblogic.management.DeploymentException;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.AuthorizationManagerDeployHandle;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.DeployHandleCreationException;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.ResourceBase;
import weblogic.security.service.ResourceCreationException;
import weblogic.security.service.ResourceRemovalException;
import weblogic.security.service.RoleCreationException;
import weblogic.security.service.RoleManager;
import weblogic.security.service.RoleManagerDeployHandle;
import weblogic.security.service.RoleRemovalException;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.URLResource;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WebAppConfigManager;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.utils.StandardURLMapping;
import weblogic.servlet.utils.URLMappingFactory;

final class WebAppSecurityWLS extends WebAppSecurity {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final AuthorizationManager authManager;
   private final RoleManager roleManager;
   private final boolean fullDelegation;
   private final boolean customRoles;
   private final RoleManagerDeployHandle roleMgrHandle;
   private final AuthorizationManagerDeployHandle authMgrHandle;
   private HashMap constraintsMap;

   public WebAppSecurityWLS(WebAppServletContext var1) throws DeploymentException {
      super(var1);
      this.authManager = (AuthorizationManager)SecurityServiceManager.getSecurityService(KERNEL_ID, var1.getSecurityRealmName(), ServiceType.AUTHORIZE);
      this.roleManager = (RoleManager)SecurityServiceManager.getSecurityService(KERNEL_ID, var1.getSecurityRealmName(), ServiceType.ROLE);

      try {
         this.authMgrHandle = this.authManager.startDeployPolicies(this.secureAppInfo);
         this.roleMgrHandle = this.roleManager.startDeployRoles(this.secureAppInfo);
      } catch (DeployHandleCreationException var3) {
         throw new DeploymentException(var3);
      }

      String var2 = this.secureAppInfo.getSecurityDDModel();
      this.customRoles = var2.equals("CustomRoles") || var2.equals("CustomRolesAndPolicies");
      this.fullDelegation = SecurityServiceManager.isFullAuthorizationDelegationRequired(var1.getSecurityRealmName(), this.secureAppInfo);
   }

   protected boolean isFullSecurityDelegationRequired() {
      return this.fullDelegation;
   }

   protected boolean isJaccEnabled() {
      return false;
   }

   protected AuthorizationManager getAuthManager() {
      return this.authManager;
   }

   protected AuthorizationManagerDeployHandle getAuthMgrHandle() {
      return this.authMgrHandle;
   }

   protected Set getRoles() {
      return this.roles;
   }

   private void deployRole(ResourceBase var1, String var2, String[] var3) throws DeploymentException {
      try {
         this.roleManager.deployRole(this.roleMgrHandle, var1, var2, var3);
      } catch (RoleCreationException var5) {
         HTTPLogger.logCouldNotDeployRole(var2, this.context.getURI(), ApplicationVersionUtils.getDisplayName(this.context.getApplicationId()), var5);
         throw new DeploymentException(var5);
      }
   }

   protected void deployRoles() throws DeploymentException {
      if (!this.roles.isEmpty()) {
         URLResource var1 = new URLResource(this.context.getApplicationId(), this.context.getContextPath(), getEnforceStrictURLPattern() ? "/" : "/*", (String)null, (String)null);
         StringBuffer var2 = null;
         int var3 = 0;
         Iterator var4 = this.roles.iterator();

         while(true) {
            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               String[] var6 = (String[])((String[])this.roleMapping.get(var5));
               boolean var7 = var6 != null && var6.length > 0;
               if (this.isCompatibilitySecMode()) {
                  if (var7) {
                     if (!this.isExternallyDefined(var6)) {
                        this.deployRole(var1, var5, var6);
                     }
                  } else if (!this.customRoles) {
                     this.deployRole(var1, var5, new String[]{var5});
                     if (var3 < 1) {
                        var2 = new StringBuffer();
                        var2.append(var5);
                     } else {
                        var2.append(", " + var5);
                     }

                     ++var3;
                  }
               } else if (!this.isExternallyDefined(var6)) {
                  SecurityRole var8 = this.context.getApplicationContext().getSecurityRole(var5);
                  String[] var9 = null;
                  if (var8 != null) {
                     var9 = var8.getPrincipalNames();
                  }

                  boolean var10 = var9 != null && var9.length > 0;
                  if (!var7 && !var10) {
                     if (this.isApplicationSecMode()) {
                        this.deployRole(var1, var5, new String[0]);
                     }
                  } else if (var8 != null && var8.isExternallyDefined()) {
                     if (var7) {
                        this.deployRole(var1, var5, var6);
                     }
                  } else {
                     if (var10) {
                        if (!var7) {
                           var6 = var9;
                        } else {
                           String[] var11 = new String[var9.length + var6.length];
                           System.arraycopy(var6, 0, var11, 0, var6.length);
                           System.arraycopy(var9, 0, var11, var6.length, var9.length);
                           var6 = var11;
                        }
                     }

                     if (var6.length > 0) {
                        this.deployRole(var1, var5, var6);
                     }
                  }
               }
            }

            if (var2 != null) {
               HTTPLogger.logCreatingImplicitMapForRoles(this.context.getLogContext(), var3 == 1 ? "role" : "roles", var3 == 1 ? "has" : "have", var2.toString());
            }

            return;
         }
      }
   }

   private void mergePolicy(ResourceConstraint var1) throws DeploymentException {
      if (URLMappingFactory.isInvalidUrlPattern(this.context.getUrlMatchMap(), var1.getResourceId())) {
         throw new DeploymentException("The url-pattern, '" + var1.getResourceId() + "' is not valid");
      } else {
         String var2 = var1.getHttpMethod();
         if (var2 == null) {
            var2 = "";
         }

         StandardURLMapping var3 = (StandardURLMapping)this.constraintsMap.get(var2);
         if (var3 == null) {
            var3 = URLMappingFactory.createCompatibleURLMapping(this.context.getUrlMatchMap(), this.context.getServletClassLoader(), WebAppConfigManager.isCaseInsensitive(), getEnforceStrictURLPattern());
            this.constraintsMap.put(var2, var3);
            var3.put(var1.getResourceId(), var1);
         } else {
            ResourceConstraint var4 = (ResourceConstraint)var3.removePattern(var1.getResourceId());
            if (var4 != null) {
               int var5 = var1.getTransportGuarantee();
               int var6 = var4.getTransportGuarantee();
               if (var5 != var6) {
                  var1.setTransportGuarantee(var5 > var6 ? var5 : var6);
               }

               if (var4.getRoles() != null && var1.getRoles() != null) {
                  if (var4.getRoles().length != 0 && var1.getRoles().length != 0) {
                     var1.addRoles(var4.getRoles());
                  } else {
                     var1 = var4.getRoles().length == 0 ? var4 : var1;
                  }
               } else {
                  var1 = var4.getRoles() == null ? var4 : var1;
               }
            }

            var3.put(var1.getResourceId(), var1);
         }
      }
   }

   private void mergePatterns(String[] var1, String var2, SecurityConstraintBean var3) throws DeploymentException {
      for(int var4 = 0; var1 != null && var4 < var1.length; ++var4) {
         String var5 = fixupURLPattern(var1[var4]);
         if (this.getContext().isInternalApp() && var5.equals("/") && !getEnforceStrictURLPattern()) {
            var5 = "/*";
         }

         this.mergePolicy(new ResourceConstraint(var5, var2, var3));
      }

   }

   protected void mergePolicies(SecurityConstraintBean[] var1) throws DeploymentException {
      if (var1 != null && var1.length >= 1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            WebResourceCollectionBean[] var3 = var1[var2].getWebResourceCollections();

            for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
               String[] var5 = var3[var4].getHttpMethods();
               if (var5 == null || var5.length == 0) {
                  this.mergePatterns(var3[var4].getUrlPatterns(), (String)null, var1[var2]);
               }

               for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
                  this.mergePatterns(var3[var4].getUrlPatterns(), var5[var6], var1[var2]);
               }
            }
         }

      }
   }

   protected void deployPolicies(SecurityConstraintBean[] var1) throws DeploymentException {
      if (var1 != null && var1.length >= 1) {
         if (this.constraintsMap == null) {
            this.constraintsMap = new HashMap();
         }

         this.mergePolicies(var1);
         Iterator var2 = this.constraintsMap.values().iterator();

         while(var2.hasNext()) {
            StandardURLMapping var3 = (StandardURLMapping)var2.next();
            Object[] var4 = var3.values();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               ((ResourceConstraint)var4[var5]).deploy(this);
            }
         }

      }
   }

   public void unregister() {
      super.unregister();

      try {
         this.authManager.undeployAllPolicies(this.authMgrHandle);
      } catch (ResourceRemovalException var3) {
         HTTPLogger.logFailedToUndeploySecurityPolicy(this.context.getLogContext(), var3);
      }

      try {
         this.roleManager.undeployAllRoles(this.roleMgrHandle);
      } catch (RoleRemovalException var2) {
         HTTPLogger.logFailedToUndeploySecurityRole(this.context.getLogContext(), var2);
      }

   }

   public ResourceConstraint getConstraint(HttpServletRequest var1) {
      return this.getConstraint(getRelativeURI(var1), var1.getMethod());
   }

   private ResourceConstraint getConstraint(String var1, String var2) {
      if (this.constraintsMap == null) {
         return null;
      } else {
         StandardURLMapping var3 = null;
         if (var2 != null) {
            var3 = (StandardURLMapping)this.constraintsMap.get(var2);
            if (var3 != null) {
               ResourceConstraint var4 = (ResourceConstraint)var3.get(var1);
               if (var4 != null) {
                  return var4;
               }
            }

            return this.getConstraint(var1, (String)null);
         } else {
            var3 = (StandardURLMapping)this.constraintsMap.get("");
            return var3 == null ? null : (ResourceConstraint)var3.get(var1);
         }
      }
   }

   public boolean hasPermission(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3, ResourceConstraint var4) {
      if (this.context.isAdminMode() && this.context.isInternalApp() && ConnectionSigner.isConnectionSigned(var1)) {
         return true;
      } else if (this.context.isAdminMode()) {
         return this.checkAdminMode(var3);
      } else {
         if (!this.fullDelegation) {
            if (var4 == null || var4.isUnrestricted()) {
               return true;
            }

            if (var4.isForbidden()) {
               return false;
            }

            if (var4.isLoginRequired()) {
               return var3 != null;
            }

            if (var3 == null) {
               return false;
            }
         } else if (var4 != null && var4.isLoginRequired() && var3 == null) {
            return false;
         }

         if (var3 == null) {
            var3 = SubjectUtils.getAnonymousSubject();
         }

         ContextHandler var5 = getContextHandler(var1, var2);
         URLResource var6 = new URLResource(this.context.getApplicationId(), this.context.getContextPath(), getRelativeURI(var1), var1.getMethod(), (String)null);
         return this.authManager.isAccessAllowed(var3, var6, var5);
      }
   }

   public boolean isSubjectInRole(AuthenticatedSubject var1, String var2, WebAppContextHandler var3, ServletStubImpl var4) {
      String var5 = var4.getSecurityHelper().getRoleLink(var2);
      if (var5 != null) {
         var2 = var5;
      }

      URLResource var6 = null;
      var6 = new URLResource(this.context.getApplicationId(), this.context.getContextPath(), getEnforceStrictURLPattern() ? "/" : "/*", (String)null, (String)null);
      if (var1 == null) {
         var1 = SubjectUtils.getAnonymousSubject();
      }

      Map var7 = this.roleManager.getRoles(var1, var6, var3);
      return var7 == null ? false : SecurityServiceManager.isUserInRole(var1, var2, var7);
   }

   public void registerRoleRefs(ServletStubImpl var1) {
   }

   public void start() throws DeploymentException {
      try {
         this.authManager.endDeployPolicies(this.authMgrHandle);
      } catch (ResourceCreationException var3) {
         throw new DeploymentException(var3);
      }

      try {
         this.roleManager.endDeployRoles(this.roleMgrHandle);
      } catch (RoleCreationException var2) {
         throw new DeploymentException(var2);
      }
   }

   public void initContextHandler(ServletRequestImpl var1) {
   }

   public void resetContextHandler() {
   }

   final boolean checkTransport(ResourceConstraint var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      if (var1 == null) {
         return true;
      } else if (var1.getTransportGuarantee() != 0 && !var2.isSecure()) {
         String var4 = this.getSecuredURL(var2, var3, var2.getRequestURI());
         if (var4 != null) {
            var3.sendRedirect(var3.encodeURL(var4));
         } else {
            var3.sendError(403);
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean isSSLRequired(String var1, String var2) {
      ResourceConstraint var3 = this.getConstraint(var1, var2);
      return var3 != null && var3.getTransportGuarantee() != 0;
   }

   protected void deployRoleLink(ServletStubImpl var1, String var2, String var3) {
      var1.getSecurityHelper().addRoleLink(var2, var3);
   }
}
