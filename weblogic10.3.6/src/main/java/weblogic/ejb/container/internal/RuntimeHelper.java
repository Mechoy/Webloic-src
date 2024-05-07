package weblogic.ejb.container.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.SecurityRole;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.NoSuchRoleException;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.logging.Loggable;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityApplicationInfo;
import weblogic.security.service.SecurityApplicationInfoImpl;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.spi.ApplicationInfo.ComponentType;

public final class RuntimeHelper {
   private static final DebugLogger debugLogger;
   private SecurityHelper helper;
   private Map principal2Subject;
   private DeploymentInfo deploymentInfo;
   private int roleMappingBehavior;
   private SecurityApplicationInfo securityAppInfo;
   private ApplicationContextInternal appContext;

   public RuntimeHelper(DeploymentInfo var1, ApplicationContextInternal var2) throws WLDeploymentException {
      this.deploymentInfo = var1;
      this.appContext = var2;
      this.securityAppInfo = new SecurityApplicationInfoImpl(this.appContext.getAppDeploymentMBean(), ComponentType.EJB, this.deploymentInfo.getEJBComponentName());
      this.helper = new SecurityHelper(this.deploymentInfo.getSecurityRealmName(), this.deploymentInfo.getJACCPolicyConfig(), this.deploymentInfo.getJACCPolicyContextId(), this.deploymentInfo.getJACCCodeSource(), this.deploymentInfo.getJACCRoleMapper());
      this.helper.setupApplicationInfo(this.appContext, this.deploymentInfo, this.securityAppInfo);
      this.roleMappingBehavior = SecurityServiceManager.getRoleMappingBehavior(this.deploymentInfo.getSecurityRealmName(), this.securityAppInfo);
   }

   public boolean fullyDelegateSecurityCheck(String var1) {
      return this.helper.fullyDelegateSecurityCheck(var1);
   }

   public void setRunAsSubject(MethodDescriptor var1, String var2) throws PrincipalNotFoundException {
      if (var2 != null) {
         if (this.principal2Subject == null) {
            this.principal2Subject = new HashMap();
         }

         AuthenticatedSubject var3 = (AuthenticatedSubject)this.principal2Subject.get(var2);
         if (var3 == null) {
            var3 = this.helper.getSubjectForPrincipal(var2);
            this.principal2Subject.put(var2, var3);
         }

         var1.setRunAsSubject(var3);
      }

   }

   public void checkRunAsPrivileges(BeanInfo var1) throws WLDeploymentException {
      AuthenticatedSubject var2 = this.appContext.getDeploymentInitiator();
      boolean var3 = SubjectUtils.isUserAnAdministrator(var2);
      if (!var3 && (!this.appContext.isStaticDeploymentOperation() || !SubjectUtils.isUserAnonymous(var2))) {
         this.checkRunAsPrivilege(var1.getRunAsPrincipalName(), "run", var1);
         this.checkRunAsPrivilege(var1.getCreateAsPrincipalName(), "create", var1);
         this.checkRunAsPrivilege(var1.getRemoveAsPrincipalName(), "remove", var1);
         this.checkRunAsPrivilege(var1.getPassivateAsPrincipalName(), "passivate", var1);
      }

   }

   private void checkRunAsPrivilege(String var1, String var2, BeanInfo var3) throws WLDeploymentException {
      if (var1 != null) {
         Loggable var5;
         try {
            AuthenticatedSubject var4 = this.helper.getSubjectForPrincipal(var1);
            if (var4 != null && SubjectUtils.isUserAnAdministrator(var4)) {
               var5 = EJBLogger.logAttemptToBumpUpPrivilegesWithRunAsLoggable(var3.getDisplayName(), var2);
               throw new WLDeploymentException(var5.getMessage());
            }
         } catch (PrincipalNotFoundException var6) {
            var5 = EJBLogger.logRunAsPrincipalNotFoundLoggable(var3.getDisplayName(), var2, var1);
            throw new WLDeploymentException(var5.getMessage());
         }
      }

   }

   public void registerRoleRefs(String var1, Map var2) throws WLDeploymentException {
      this.helper.registerRoleRefs(var1, var2);
   }

   public boolean processUncheckedExcludedMethod(MethodDescriptor var1) throws WLDeploymentException {
      return this.helper.processUncheckedExcludedMethod(var1);
   }

   public void deployRoles(DeploymentInfo var1, SecurityRoleMapping var2) throws Exception {
      this.helper.deployRoles(var1, var2, this.appContext, this.roleMappingBehavior);
   }

   public void unDeployRoles(DeploymentInfo var1, SecurityRoleMapping var2) {
      this.helper.unDeployRoles(var1, var2);
   }

   public void activate() {
      this.helper.activate();
   }

   public void deactivate() {
      this.helper.deactivate();
   }

   public boolean isUserPrincipal(String var1) {
      if (var1 == null) {
         return false;
      } else {
         try {
            return this.helper.getSubjectForPrincipal(var1) != null;
         } catch (PrincipalNotFoundException var3) {
            return false;
         }
      }
   }

   public static void registerSupplementalPolicyObject(String[] var0, String var1) {
      SecurityHelper.registerSupplementalPolicyObject(var0, var1);
   }

   public static void removeSupplementalPolicyObject(String[] var0) {
      SecurityHelper.removeSupplementalPolicyObject(var0);
   }

   public static String getDefaultRealmName() {
      return SecurityHelper.getDefaultRealmName();
   }

   public static String getCurrent() {
      return SecurityHelper.getCurrentPrincipal().getName();
   }

   public void deployAllPolicies() throws Exception {
      this.helper.deployAllPolicies();
   }

   public String getRunAsPrincipalFromRoleMapping(String var1, String var2, SecurityRoleMapping var3) throws WLDeploymentException {
      if (debugLogger.isDebugEnabled()) {
         debug("attempting to get the run-as principal for run-as role " + var2 + " from a security-role assignment for the role.");
      }

      String var4 = null;
      switch (this.roleMappingBehavior) {
         case 0:
            Collection var5 = null;

            try {
               var5 = var3.getSecurityRolePrincipalNames(var2);
            } catch (NoSuchRoleException var11) {
               throw new AssertionError("Expected role in mapping");
            }

            for(Iterator var12 = var5.iterator(); var12.hasNext(); var4 = null) {
               var4 = (String)var12.next();
               if (this.isUserPrincipal(var4)) {
                  EJBLogger.logRunAsPrincipalChosenFromSecurityRoleAssignment(var1, var2, var4);
                  break;
               }
            }

            if (var4 == null) {
               EJBComplianceTextFormatter var13 = new EJBComplianceTextFormatter();
               throw new WLDeploymentException(var13.COULD_NOT_DETERMINE_RUN_AS_PRINCIPAL_FROM_ROLE_ASSIGNMENT(var1, var2));
            } else {
               return var4;
            }
         case 1:
         case 2:
            Collection var6 = null;

            try {
               var6 = var3.getSecurityRolePrincipalNames(var2);
            } catch (NoSuchRoleException var10) {
               throw new AssertionError("Expected role in mapping");
            }

            if (!var6.isEmpty()) {
               var4 = (String)var6.iterator().next();
               EJBLogger.logRunAsPrincipalChosenFromSecurityRoleAssignment(var1, var2, var4);
               return var4;
            } else {
               String[] var7 = null;
               SecurityRole var8 = this.appContext.getSecurityRole(var2);
               if (var8 != null) {
                  var7 = var8.getPrincipalNames();
               }

               if (var7 != null && var7.length > 0) {
                  var4 = var7[0];
                  EJBLogger.logRunAsPrincipalChosenFromSecurityRoleAssignment(var1, var2, var4);
                  return var4;
               } else {
                  if (this.roleMappingBehavior == 1) {
                     EJBComplianceTextFormatter var9 = new EJBComplianceTextFormatter();
                     throw new WLDeploymentException(var9.COULD_NOT_DETERMINE_RUN_AS_PRINCIPAL_FROM_ROLE_ASSIGNMENT(var1, var2));
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debug("setting run-as principal equal to the role name for run-as role " + var2);
                  }

                  return var2;
               }
            }
         default:
            throw new AssertionError("Unexpected role mapping behavior: " + this.roleMappingBehavior);
      }
   }

   public static Object getCurrentNew() {
      return SecurityHelper.getCurrentSubject();
   }

   private static void debug(String var0) {
      debugLogger.debug("[RuntimeHelper] " + var0);
   }

   public static int getCurrentState(Object var0) {
      int var1 = 0;
      if (var0 != null) {
         var1 = ((WLEnterpriseBean)var0).__WL_getMethodState();
      } else {
         Object var2 = AllowedMethodsHelper.getMethodInvocationState();
         if (var2 != null) {
            var1 = (Integer)var2;
         }
      }

      return var1;
   }

   static {
      debugLogger = EJBDebugService.securityLogger;
   }
}
