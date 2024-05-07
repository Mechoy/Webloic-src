package weblogic.ejb.container.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.application.ApplicationContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.NoSuchRoleException;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.spi.BusinessObject;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.AuthorizationManagerDeployHandle;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.DeployHandleCreationException;
import weblogic.security.service.EJBResource;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.ResourceCreationException;
import weblogic.security.service.ResourceRemovalException;
import weblogic.security.service.RoleCreationException;
import weblogic.security.service.RoleManager;
import weblogic.security.service.RoleManagerDeployHandle;
import weblogic.security.service.RoleRemovalException;
import weblogic.security.service.SecurityApplicationInfo;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.StringUtils;

final class SecurityHelperWLS {
   private static final DebugLogger debugLogger;
   static final int SYSTEM_REALM = 0;
   static final int APP_REALM = 1;
   private static final AuthenticatedSubject subject;
   private SecurityApplicationInfo securityAppInfo;
   private PrincipalAuthenticator appPrincipalAuth;
   private RoleManager appRoleManager;
   private AuthorizationManager appAuthManager;
   private PrincipalAuthenticator sysPrincipalAuth;
   private RoleManager sysRoleManager;
   private AuthorizationManager sysAuthManager;
   private RoleManagerDeployHandle roleMgrHandle = null;
   private AuthorizationManagerDeployHandle authMgrHandle = null;
   private boolean fullDelegation;
   private boolean customRoles;
   private ApplicationContext appContext;
   private String sysRealmName;
   private String appRealmName;
   private EJBResource ejbRoleResource;

   SecurityHelperWLS(String var1, String var2) {
      this.appRealmName = var1;
      this.sysRealmName = var2;
   }

   void setupApplicationInfo(ApplicationContext var1, DeploymentInfo var2, SecurityApplicationInfo var3) {
      assert var1 != null : "Passed in ApplicationContext is unexpectedly null !";

      this.appContext = var1;
      this.securityAppInfo = var3;
      String var4 = var3.getSecurityDDModel();
      this.customRoles = var4.equals("CustomRoles") || var4.equals("CustomRolesAndPolicies");
      this.fullDelegation = SecurityServiceManager.isFullAuthorizationDelegationRequired(this.appRealmName != null ? this.appRealmName : this.sysRealmName, var3);
      if (debugLogger.isDebugEnabled()) {
         debug(" using 9.x+ WLS Security APIs.   Full Delegation = '" + this.fullDelegation + "'");
      }

   }

   void deployRoles(DeploymentInfo var1, Map var2, int var3) throws WLDeploymentException, NoSuchRoleException {
      String var4 = var1.getApplicationName();
      String var5 = var1.getEJBComponentName();
      if (debugLogger.isDebugEnabled()) {
         debug("deployRoles(...), appName: '" + var4 + "', ejbComponentName: '" + var5 + "'  there are: '" + var2.size() + "' roles in this jar.");
      }

      RoleManager var6 = this.obtainRM(var3);

      try {
         this.roleMgrHandle = var6.startDeployRoles(this.securityAppInfo);
      } catch (DeployHandleCreationException var13) {
         throw new WLDeploymentException("", var13);
      }

      if (!var2.isEmpty()) {
         this.ejbRoleResource = SecurityHelper.createEJBResource(var1);
         Iterator var7 = var2.keySet().iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            String[] var9 = (String[])((String[])var2.get(var8));
            if (debugLogger.isDebugEnabled()) {
               debug("deploying role: " + var8 + " with principals: " + StringUtils.join(var9, ", "));
            }

            try {
               var6.deployRole(this.roleMgrHandle, this.ejbRoleResource, var8, var9);
            } catch (RoleCreationException var12) {
               throw new NoSuchRoleException("registerEjbRolesAndUsers: Exception while attempting to deploy Security Role: " + var12.toString());
            }
         }

         try {
            var6.endDeployRoles(this.roleMgrHandle);
         } catch (RoleCreationException var11) {
            throw new WLDeploymentException("", var11);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("done with role deployment for appName: '" + var4 + "', ejbComponentName: '" + var5 + "'");
         }

      }
   }

   void unDeployRoles(DeploymentInfo var1, SecurityRoleMapping var2) {
      this.unDeployRoles(var1, var2, 1);
   }

   void unDeployRoles(DeploymentInfo var1, SecurityRoleMapping var2, int var3) {
      String var4 = var1.getApplicationName();
      String var5 = var1.getEJBComponentName();
      EJBResource var6 = SecurityHelper.createEJBResource(var1);
      RoleManager var7 = this.obtainRM(var3);
      if (this.roleMgrHandle != null) {
         try {
            var7.undeployAllRoles(this.roleMgrHandle);
         } catch (RoleRemovalException var9) {
            EJBLogger.logFailedToUndeploySecurityRole(var4 + " - " + var5, var9);
         }

      }
   }

   void beginPolicyRegistration() throws WLDeploymentException {
      this.beginPolicyRegistration(1);
   }

   void beginPolicyRegistration(int var1) throws WLDeploymentException {
      AuthorizationManager var2 = this.obtainAM(var1);

      try {
         this.authMgrHandle = var2.startDeployPolicies(this.securityAppInfo);
      } catch (DeployHandleCreationException var4) {
         throw new WLDeploymentException("", var4);
      }
   }

   void endPolicyRegistration() throws WLDeploymentException {
      this.endPolicyRegistration(1);
   }

   void endPolicyRegistration(int var1) throws WLDeploymentException {
      AuthorizationManager var2 = this.obtainAM(var1);

      try {
         var2.endDeployPolicies(this.authMgrHandle);
      } catch (ResourceCreationException var4) {
         throw new WLDeploymentException("", var4);
      }
   }

   void deployPolicies(List<MethodDescriptor> var1, List<MethodDescriptor> var2, List<MethodDescriptor> var3, SecurityHelper var4, int var5) throws PrincipalNotFoundException {
      AuthorizationManager var6 = this.obtainAM(var5);
      if (var1 != null) {
         boolean var7 = this.deployOptimizedPolicy(var1, var4, var6);
         if (!var7) {
            Iterator var8 = var1.iterator();

            while(var8.hasNext()) {
               MethodDescriptor var9 = (MethodDescriptor)var8.next();
               this.deployPolicy(var9, var4, var6);
            }
         }
      }

      Iterator var10;
      MethodDescriptor var11;
      if (var2 != null) {
         var10 = var2.iterator();

         while(var10.hasNext()) {
            var11 = (MethodDescriptor)var10.next();
            this.deployPolicy(var11, var4, var6);
         }
      }

      if (var3 != null) {
         var10 = var3.iterator();

         while(var10.hasNext()) {
            var11 = (MethodDescriptor)var10.next();
            this.deployPolicy(var11, var4, var6);
         }
      }

   }

   private boolean deployOptimizedPolicy(List<MethodDescriptor> var1, SecurityHelper var2, AuthorizationManager var3) throws PrincipalNotFoundException {
      if (var1 == null) {
         return false;
      } else if (var1.isEmpty()) {
         return false;
      } else {
         ArrayList var4 = new ArrayList();
         MethodDescriptor var5 = null;
         Iterator var6 = var1.iterator();

         while(var6.hasNext()) {
            MethodDescriptor var7 = (MethodDescriptor)var6.next();
            if (var5 == null) {
               var5 = var7;
            } else {
               if (!var5.getEjbName().equals(var7.getEjbName())) {
                  return false;
               }

               Set var8 = var5.getMethodInfo().getSecurityRoleNames();
               Set var9 = var7.getMethodInfo().getSecurityRoleNames();
               if (!var8.equals(var9)) {
                  if (var5.getMethod().getDeclaringClass() == BusinessObject.class) {
                     var4.add(var5);
                     var5 = var7;
                  } else {
                     if (var7.getMethod().getDeclaringClass() != BusinessObject.class) {
                        return false;
                     }

                     var4.add(var7);
                  }
               }
            }
         }

         String var13 = var5.getApplicationName();
         String var14 = var5.getEjbComponentName();
         String var15 = var5.getEjbName();
         EJBResource var16 = SecurityHelper.createEJBResource(var13, var14, var15);
         Iterator var10 = var1.iterator();

         while(var10.hasNext()) {
            MethodDescriptor var11 = (MethodDescriptor)var10.next();
            var11.setSecurityHelper(var2);
            var11.setEJBResource(SecurityHelper.createEJBResource(var11));
         }

         if (debugLogger.isDebugEnabled()) {
            debug("deployPolicy:  register optimized EJB Role restrictions for appName: '" + var13 + "', ejbComponentName: '" + var14 + "', ejbName: '" + var15);
         }

         MethodInfo var17 = var5.getMethodInfo();
         this.deployPolicy(var16, var17.getSecurityRoleNames(), var17.needsSecurityCheck(), var17.getUnchecked(), var17.getIsExcluded(), var3);
         Iterator var18 = var4.iterator();

         while(var18.hasNext()) {
            MethodDescriptor var12 = (MethodDescriptor)var18.next();
            this.deployPolicy(var12, var2, var3);
         }

         return true;
      }
   }

   private boolean deployPolicy(MethodDescriptor var1, SecurityHelper var2, AuthorizationManager var3) throws PrincipalNotFoundException {
      var1.setSecurityHelper(var2);
      EJBResource var4 = SecurityHelper.createEJBResource(var1);
      var1.setEJBResource(var4);
      MethodInfo var5 = var1.getMethodInfo();
      if (debugLogger.isDebugEnabled()) {
         debug("deployPolicy:  register EJB Role restrictions for appName: '" + var1.getApplicationName() + "', ejbComponentName: '" + var1.getEjbComponentName() + "', ejbName: '" + var1.getEjbName() + "', methodName: '" + var5.getMethodName() + "', methodInterface: '" + var5.getMethodInterfaceType());
      }

      return this.deployPolicy(var4, var5.getSecurityRoleNames(), var5.needsSecurityCheck(), var5.getUnchecked(), var5.getIsExcluded(), var3);
   }

   boolean deployPolicy(EJBResource var1, Set<String> var2, boolean var3, boolean var4, boolean var5, AuthorizationManager var6) throws PrincipalNotFoundException {
      try {
         if (!var3) {
            boolean var22 = false;
            return var22;
         }

         if (debugLogger.isDebugEnabled()) {
            Iterator var7 = var2.iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               debug("  next roleName is: '" + var8 + "'");
            }
         }

         String[] var21 = (String[])((String[])var2.toArray(new String[var2.size()]));

         try {
            if (var2.isEmpty()) {
               if (debugLogger.isDebugEnabled()) {
                  debug(" count of restrictable roles in policy = " + var2.size() + ", so skipping authManager.deployPolicy. ");
               }

               boolean var23 = true;
               return var23;
            }

            var6.deployPolicy(this.authMgrHandle, var1, var21);
         } catch (ResourceCreationException var19) {
            throw new PrincipalNotFoundException("Exception while attempting to deploy Security Policy:  " + var19.toString());
         }
      } finally {
         try {
            if (var4) {
               if (debugLogger.isDebugEnabled()) {
                  debug(" deploying unchecked policy");
               }

               var6.deployUncheckedPolicy(this.authMgrHandle, var1);
            } else if (var5) {
               if (debugLogger.isDebugEnabled()) {
                  debug(" deploying excluded policy ");
               }

               var6.deployExcludedPolicy(this.authMgrHandle, var1);
            }
         } catch (ResourceCreationException var18) {
            throw new PrincipalNotFoundException("Exception while attempting to deploy Unchecked or Excluded Security Policy:  " + var18.toString());
         }

      }

      if (debugLogger.isDebugEnabled()) {
         debug("authManager.deployPolicy: registered  EJB Role restrictions with Policy Manager");
      }

      return true;
   }

   void unDeployAllPolicies() {
      this.unDeployAllPolicies(1);
   }

   void unDeployAllPolicies(int var1) {
      if (this.authMgrHandle != null) {
         AuthorizationManager var2 = this.obtainAM(var1);

         try {
            var2.undeployAllPolicies(this.authMgrHandle);
         } catch (ResourceRemovalException var4) {
            EJBLogger.logFailedToUndeploySecurityPolicy("All EJBs in Application", var4);
         }

      }
   }

   boolean isAccessAllowed(EJBResource var1, ContextHandler var2) {
      return this.isAccessAllowed(var1, var2, 1);
   }

   boolean isAccessAllowed(EJBResource var1, ContextHandler var2, int var3) {
      AuthorizationManager var4 = this.obtainAM(var3);
      AuthenticatedSubject var5 = SecurityHelper.getCurrentSubject();
      if (debugLogger.isDebugEnabled()) {
         debug(" isAccessAllowed:  checking Method Permission for ejb: '" + var1 + "' with Subject: " + var5);
      }

      return var4.isAccessAllowed(var5, var1, var2);
   }

   boolean isCallerInRole(EJBResource var1, AuthenticatedSubject var2, String var3) {
      return this.isCallerInRole(var1, var2, var3, 1);
   }

   boolean isCallerInRole(EJBResource var1, AuthenticatedSubject var2, String var3, int var4) {
      RoleManager var5 = this.obtainRM(var4);
      Map var6 = var5.getRoles(var2, var1, (ContextHandler)null);
      if (var6 != null && var6.size() >= 1) {
         if (debugLogger.isDebugEnabled()) {
            StringBuilder var7 = new StringBuilder();
            Iterator var8 = var6.keySet().iterator();

            while(var8.hasNext()) {
               var7.append((String)var8.next()).append(", ");
            }

            debug(" isCallerInRole:  check securityRoles for resource; '" + var1 + "',\n subject: '" + var2 + ", candidate role name '" + var3 + "'roles mapped to this subject are: '" + var7.toString() + "'" + "'  isCallerInRole returns " + SecurityServiceManager.isUserInRole(var2, var3, var6));
         }

         return SecurityServiceManager.isUserInRole(var2, var3, var6);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug(" isCallerInRole:  securityRoles for resource; '" + var1 + "',\n Caller subject: '" + var2 + ", role name '" + var3 + "' there are no roles mapped to this subject." + "'  isCallerInRole returns false");
         }

         return false;
      }
   }

   boolean fullyDelegateSecurityCheck(String var1) {
      return this.fullDelegation;
   }

   private PrincipalAuthenticator obtainPA(int var1) {
      switch (var1) {
         case 0:
            if (this.sysPrincipalAuth != null) {
               return this.sysPrincipalAuth;
            }

            this.sysPrincipalAuth = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(subject, this.sysRealmName, ServiceType.AUTHENTICATION);
            return this.sysPrincipalAuth;
         case 1:
            if (this.appPrincipalAuth != null) {
               return this.appPrincipalAuth;
            }

            this.appPrincipalAuth = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(subject, this.appRealmName, ServiceType.AUTHENTICATION);
            return this.appPrincipalAuth;
         default:
            throw new AssertionError("  weblogic.ejb.internal.SecurityHelper.obtainPA, unknown realm type: " + var1);
      }
   }

   private RoleManager obtainRM(int var1) {
      switch (var1) {
         case 0:
            if (this.sysRoleManager != null) {
               return this.sysRoleManager;
            }

            this.sysRoleManager = (RoleManager)SecurityServiceManager.getSecurityService(subject, this.sysRealmName, ServiceType.ROLE);
            return this.sysRoleManager;
         case 1:
            if (this.appRoleManager != null) {
               return this.appRoleManager;
            }

            this.appRoleManager = (RoleManager)SecurityServiceManager.getSecurityService(subject, this.appRealmName, ServiceType.ROLE);
            return this.appRoleManager;
         default:
            throw new AssertionError("  weblogic.ejb.internal.SecurityHelper.obtainRM, unknown realm type: " + var1);
      }
   }

   private AuthorizationManager obtainAM(int var1) {
      switch (var1) {
         case 0:
            if (this.sysAuthManager != null) {
               return this.sysAuthManager;
            }

            this.sysAuthManager = (AuthorizationManager)SecurityServiceManager.getSecurityService(subject, this.sysRealmName, ServiceType.AUTHORIZE);
            return this.sysAuthManager;
         case 1:
            if (this.appAuthManager != null) {
               return this.appAuthManager;
            }

            this.appAuthManager = (AuthorizationManager)SecurityServiceManager.getSecurityService(subject, this.appRealmName, ServiceType.AUTHORIZE);
            return this.appAuthManager;
         default:
            throw new AssertionError("  weblogic.ejb.internal.SecurityHelper.obtainAM, unknown realm type: " + var1);
      }
   }

   private static AuthenticatedSubject obtainSubject() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return var0;
   }

   private static void debug(String var0) {
      debugLogger.debug("[SecurityHelperWLS] " + var0);
   }

   static {
      debugLogger = EJBDebugService.securityLogger;
      subject = obtainSubject();
   }
}
