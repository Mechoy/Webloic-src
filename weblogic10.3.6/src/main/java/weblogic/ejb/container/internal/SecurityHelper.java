package weblogic.ejb.container.internal;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.login.LoginException;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;
import javax.security.jacc.PolicyConfiguration;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.SecurityRole;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.NoSuchRoleException;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.logging.Loggable;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.EJBResource;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityApplicationInfo;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SupplementalPolicyObject;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.AssertionError;

public final class SecurityHelper {
   private static final DebugLogger debugLogger;
   static final int SYSTEM_REALM = 0;
   static final int APP_REALM = 1;
   private static final AuthenticatedSubject subject;
   private boolean jaccEnabled = false;
   private SecurityHelperWLS wlsHelper;
   private SecurityHelperJACC jaccHelper;
   private String sysRealmName;
   private String appRealmName;
   private PrincipalAuthenticator appPrincipalAuth;
   private PrincipalAuthenticator sysPrincipalAuth;
   private Map<String, List<MethodDescriptor>> checkedMethodDescriptors = new HashMap();
   private Map<String, List<MethodDescriptor>> uncheckedMethodDescriptors = new HashMap();
   private Map<String, List<MethodDescriptor>> excludedMethodDescriptors = new HashMap();

   public SecurityHelper(String var1, PolicyConfiguration var2, String var3, String var4, RoleMapper var5) throws WLDeploymentException {
      this.appRealmName = var1;
      if (var3 != null) {
         this.jaccHelper = new SecurityHelperJACC(var2, var3, var4, var5);
         this.jaccEnabled = true;
      } else {
         this.sysRealmName = getSysRealmName();
         this.wlsHelper = new SecurityHelperWLS(var1, this.sysRealmName);
         this.jaccEnabled = false;
      }

   }

   void setupApplicationInfo(ApplicationContextInternal var1, DeploymentInfo var2, SecurityApplicationInfo var3) {
      if (this.jaccEnabled) {
         this.jaccHelper.setupApplicationInfo(var1, var2);
      } else {
         this.wlsHelper.setupApplicationInfo(var1, var2, var3);
      }

   }

   protected AuthenticatedSubject getSubject() {
      return subject;
   }

   public AuthenticatedSubject getSubjectForPrincipal(String var1) throws PrincipalNotFoundException {
      return this.getSubjectForPrincipal(var1, 1);
   }

   AuthenticatedSubject getSubjectForPrincipal(String var1, int var2) throws PrincipalNotFoundException {
      PrincipalAuthenticator var3 = this.obtainPA(var2);

      try {
         AuthenticatedSubject var4 = var3.impersonateIdentity(var1, (ContextHandler)null);
         if (debugLogger.isDebugEnabled()) {
            debug(" getSubjectForPrincipal: for Principal: '" + var1 + "', Subject is: '" + var4.toString() + "'");
         }

         return var4;
      } catch (LoginException var5) {
         throw new PrincipalNotFoundException(var5.getMessage());
      }
   }

   void deployRoles(DeploymentInfo var1, SecurityRoleMapping var2, ApplicationContextInternal var3, int var4) throws Exception {
      this.deployRoles(var1, var2, var3, var4, 1);
   }

   void deployRoles(DeploymentInfo var1, SecurityRoleMapping var2, ApplicationContextInternal var3, int var4, int var5) throws Exception {
      if (this.jaccEnabled) {
         this.jaccHelper.deployRoles(var1, var2);
      } else {
         Map var6 = this.getDeployableSecurityRoleMapping(var2, var3, var4);
         this.wlsHelper.deployRoles(var1, var6, var5);
      }

   }

   void unDeployRoles(DeploymentInfo var1, SecurityRoleMapping var2) {
      this.unDeployRoles(var1, var2, 1);
   }

   void unDeployRoles(DeploymentInfo var1, SecurityRoleMapping var2, int var3) {
      if (this.jaccEnabled) {
         this.jaccHelper.unDeployRoles();
      } else {
         this.wlsHelper.unDeployRoles(var1, var2, var3);
      }

   }

   void registerRoleRefs(String var1, Map var2) throws WLDeploymentException {
      if (this.jaccEnabled) {
         this.jaccHelper.registerRoleRefs(var1, var2);
      }

   }

   void deployAllPolicies() throws Exception {
      this.deployAllPolicies(1);
   }

   void deployAllPolicies(int var1) throws Exception {
      if (!this.jaccEnabled) {
         this.wlsHelper.beginPolicyRegistration();
      }

      Set var2 = this.getEjbNames();

      List var5;
      List var6;
      List var7;
      for(Iterator var3 = var2.iterator(); var3.hasNext(); this.deployPolicies(var5, var6, var7, var1)) {
         String var4 = (String)var3.next();
         if (debugLogger.isDebugEnabled()) {
            debug("registering policies for EJB: " + var4);
         }

         var5 = this.getCheckedMethodDescriptors(var4);
         if (var5 != null && debugLogger.isDebugEnabled()) {
            debug("registering policies for all " + var5.size() + " checked methods");
         }

         var6 = this.getUncheckedMethodDescriptors(var4);
         if (var6 != null && debugLogger.isDebugEnabled()) {
            debug("registering policies for all " + var6.size() + " unchecked methods");
         }

         var7 = this.getExcludedMethodDescriptors(var4);
         if (var7 != null && debugLogger.isDebugEnabled()) {
            debug("registering policies for all " + var7.size() + " excluded methods");
         }
      }

      if (!this.jaccEnabled) {
         this.wlsHelper.endPolicyRegistration();
      }

   }

   private void deployPolicies(List<MethodDescriptor> var1, List<MethodDescriptor> var2, List<MethodDescriptor> var3, int var4) throws Exception {
      if (this.jaccEnabled) {
         this.jaccHelper.deployPolicies(var1, var2, var3, this);
      } else {
         this.wlsHelper.deployPolicies(var1, var2, var3, this, var4);
      }

   }

   void unDeployAllPolicies() {
      if (!this.jaccEnabled) {
         this.wlsHelper.unDeployAllPolicies();
      }

   }

   public boolean processUncheckedExcludedMethod(MethodDescriptor var1) throws WLDeploymentException {
      MethodInfo var2 = var1.getMethodInfo();
      if (var2.getUnchecked()) {
         this.addUncheckedMethod(var1);
         createEJBResource(var1);
         if (this.jaccEnabled) {
            this.jaccHelper.processUncheckedExcludedMethod(var1);
         }

         return true;
      } else if (var2.getIsExcluded()) {
         this.addExcludedMethod(var1);
         createEJBResource(var1);
         if (this.jaccEnabled) {
            this.jaccHelper.processUncheckedExcludedMethod(var1);
         }

         return true;
      } else {
         this.addCheckedMethod(var1);
         return false;
      }
   }

   void activate() {
      if (this.jaccEnabled) {
         this.jaccHelper.activate();
      }

   }

   void deactivate() {
      if (this.jaccEnabled) {
         this.jaccHelper.deactivate();
      } else {
         this.wlsHelper.unDeployAllPolicies();
      }

   }

   boolean fullyDelegateSecurityCheck() {
      return this.fullyDelegateSecurityCheck(this.appRealmName);
   }

   boolean fullyDelegateSecurityCheck(String var1) {
      return this.jaccEnabled ? true : this.wlsHelper.fullyDelegateSecurityCheck(var1);
   }

   public void setContext(ContextHandler var1) {
      if (this.jaccEnabled) {
         this.jaccHelper.setContext(var1);
      }

   }

   public void resetContext() {
      if (this.jaccEnabled) {
         this.jaccHelper.resetContext();
      }

   }

   boolean isAccessAllowed(EJBResource var1, EJBMethodPermission var2, ContextHandler var3) {
      return this.isAccessAllowed(var1, var2, var3, 1);
   }

   boolean isAccessAllowed(EJBResource var1, EJBMethodPermission var2, ContextHandler var3, int var4) {
      return this.jaccEnabled ? this.jaccHelper.isAccessAllowed(var2, var3) : this.wlsHelper.isAccessAllowed(var1, var3, var4);
   }

   boolean isCallerInRole(String var1, EJBResource var2, String var3, String var4) {
      return this.isCallerInRole(var2, var1, var3, var4, 1);
   }

   boolean isCallerInRole(EJBResource var1, String var2, String var3, String var4, int var5) {
      AuthenticatedSubject var6 = getCallerSubject();
      if (var6 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug(" isCallerInRole: Caller subject is null. isCallerInRole returns false");
         }

         return false;
      } else {
         return this.jaccEnabled ? this.jaccHelper.isCallerInRole(var2, var6, var3) : this.wlsHelper.isCallerInRole(var1, var6, var4, var5);
      }
   }

   private void addCheckedMethod(MethodDescriptor var1) {
      Object var2 = (List)this.checkedMethodDescriptors.get(var1.getEjbName());
      if (var2 == null) {
         var2 = new ArrayList();
         this.checkedMethodDescriptors.put(var1.getEjbName(), var2);
      }

      ((List)var2).add(var1);
   }

   private void addUncheckedMethod(MethodDescriptor var1) {
      Object var2 = (List)this.uncheckedMethodDescriptors.get(var1.getEjbName());
      if (var2 == null) {
         var2 = new ArrayList();
         this.uncheckedMethodDescriptors.put(var1.getEjbName(), var2);
      }

      ((List)var2).add(var1);
   }

   private void addExcludedMethod(MethodDescriptor var1) {
      Object var2 = (List)this.excludedMethodDescriptors.get(var1.getEjbName());
      if (var2 == null) {
         var2 = new ArrayList();
         this.excludedMethodDescriptors.put(var1.getEjbName(), var2);
      }

      ((List)var2).add(var1);
   }

   private Set<String> getEjbNames() {
      HashSet var1 = new HashSet();
      var1.addAll(this.checkedMethodDescriptors.keySet());
      var1.addAll(this.uncheckedMethodDescriptors.keySet());
      var1.addAll(this.excludedMethodDescriptors.keySet());
      return var1;
   }

   private List<MethodDescriptor> getCheckedMethodDescriptors(String var1) {
      return (List)this.checkedMethodDescriptors.get(var1);
   }

   private List<MethodDescriptor> getUncheckedMethodDescriptors(String var1) {
      return (List)this.uncheckedMethodDescriptors.get(var1);
   }

   private List<MethodDescriptor> getExcludedMethodDescriptors(String var1) {
      return (List)this.excludedMethodDescriptors.get(var1);
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

   public static AuthenticatedSubject getAnonymousUser() {
      return SubjectUtils.getAnonymousSubject();
   }

   static Principal getAnonymousUserPrincipal() {
      return WLSPrincipals.getAnonymousUserPrincipal();
   }

   static Principal getPrincipalFromSubject(AuthenticatedSubject var0) {
      if (var0 == null) {
         return getAnonymousUserPrincipal();
      } else {
         Principal var1 = SubjectUtils.getUserPrincipal(var0);
         return var1 != null ? var1 : getAnonymousUserPrincipal();
      }
   }

   static Principal getCurrentPrincipal() {
      AuthenticatedSubject var0 = getCurrentSubject();
      return getPrincipalFromSubject(var0);
   }

   static AuthenticatedSubject getCurrentSubject() {
      return SecurityServiceManager.getCurrentSubject(subject);
   }

   public static void pushRunAsSubject(AuthenticatedSubject var0) {
      if (debugLogger.isDebugEnabled()) {
         debug("pushRunAsSubject to push: '" + var0.toString() + "', currentSubject is: '" + getCurrentSubject() + "' ");
      }

      SecurityServiceManager.pushSubject(subject, var0);
   }

   public static void popRunAsSubject() {
      if (debugLogger.isDebugEnabled()) {
         debug("\n popRunAsSubject,  subject before pop is: '" + getCurrentSubject() + "'");
      }

      SecurityServiceManager.popSubject(subject);
      if (debugLogger.isDebugEnabled()) {
         debug("\n popRunAsSubject,  subject after  pop is: '" + getCurrentSubject() + "'");
      }

   }

   static boolean pushSpecificRunAsMaybe(AuthenticatedSubject var0, AuthenticatedSubject var1) {
      if (var0 != null) {
         pushRunAsSubject(var0);
         return true;
      } else if (var1 != null) {
         pushRunAsSubject(var1);
         return true;
      } else if (SecurityServiceManager.isKernelIdentity(getCurrentSubject())) {
         pushRunAsSubject(getAnonymousUser());
         return true;
      } else {
         return false;
      }
   }

   static Principal getCallerPrincipal() throws PrincipalNotFoundException {
      AuthenticatedSubject var0 = getCallerSubject();
      Principal var1 = getPrincipalFromSubject(var0);
      if (var1 == null) {
         String var2 = "getCallerPrincipal";
         Loggable var3 = EJBLogger.logmissingCallerPrincipalLoggable(var2);
         throw new PrincipalNotFoundException(var3.getMessage());
      } else {
         return var1;
      }
   }

   static AuthenticatedSubject getCallerSubject() {
      AuthenticatedSubject var0 = CallerSubjectStack.getCurrentSubject();
      return var0;
   }

   public static void pushCallerPrincipal() {
      AuthenticatedSubject var0 = getCurrentSubject();
      if (debugLogger.isDebugEnabled()) {
         debug("\n pushCallerPrincipal to push Subject: '" + var0 + "' " + " from which we get principal '" + getPrincipalFromSubject(var0) + "'");
      }

      CallerSubjectStack.pushSubject(var0);
   }

   public static void popCallerPrincipal() throws PrincipalNotFoundException {
      if (debugLogger.isDebugEnabled()) {
         debug("\n popCallerPrincipal, CallerSubject before pop is: '" + getCallerSubject() + "'");
      }

      AuthenticatedSubject var0 = CallerSubjectStack.popSubject();
      if (var0 == null) {
         String var1 = "popCallerPrincipal";
         Loggable var2 = EJBLogger.logmissingCallerPrincipalLoggable(var1);
         throw new PrincipalNotFoundException(var2.getMessage());
      }
   }

   static String getDefaultRealmName() {
      return "weblogicDEFAULT";
   }

   static String getSysRealmName() {
      String var0 = SecurityServiceManager.getDefaultRealmName();
      if (var0 == null) {
         throw new RuntimeException(" Could not get System Realm Name. ");
      } else {
         return var0;
      }
   }

   static void registerSupplementalPolicyObject(String[] var0, String var1) {
      SupplementalPolicyObject.setPoliciesFromGrantStatement(subject, var0, var1, "EJB");
   }

   static void removeSupplementalPolicyObject(String[] var0) {
      SupplementalPolicyObject.removePolicies(subject, var0);
   }

   static EJBResource createEJBResource(DeploymentInfo var0) {
      return new EJBResource(var0.getApplicationName(), var0.getEJBComponentName(), (String)null, (String)null, (String)null, (String[])null);
   }

   static EJBResource createEJBResource(String var0, String var1, String var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("\n\n ++++++++++  creating EJBResource: appName: '" + var0 + "' moduleName: '" + var1 + "' ejbName: '" + var2 + "' methodName: 'null" + "' interfaceType: 'null" + "' methodParams:     'null");
      }

      return new EJBResource(var0, var1, var2, (String)null, (String)null, (String[])null);
   }

   static EJBResource createEJBResource(MethodDescriptor var0) {
      String var1 = var0.getApplicationName();
      String var2 = var0.getEjbComponentName();
      String var3 = var0.getEjbName();
      MethodInfo var4 = var0.getMethodInfo();
      String[] var5 = getCanonicalMethodParamNames(var0.getMethod());
      if (debugLogger.isDebugEnabled()) {
         StringBuilder var6 = new StringBuilder();
         if (var5.length > 0) {
            for(int var7 = 0; var7 < var5.length; ++var7) {
               var6.append(var5[var7]).append(", ");
            }
         } else {
            var6.append(" NONE ");
         }

         debug("\n\n ++++++++++  creating EJBResource: appName: '" + var1 + "' moduleName: '" + var2 + "' ejbName: '" + var3 + "' methodName: '" + var4.getMethodName() + "' interfaceType: '" + var4.getMethodInterfaceType() + "' methodParams:     '" + var6.toString() + "'");
      }

      return new EJBResource(var1, var2, var3, var4.getMethodName(), var4.getMethodInterfaceType(), var5);
   }

   private static String[] getCanonicalMethodParamNames(Method var0) {
      Class[] var1 = var0.getParameterTypes();
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3].getCanonicalName();
      }

      return var2;
   }

   static EJBMethodPermission createEJBMethodPermission(MethodDescriptor var0) {
      String var1 = var0.getEjbName();
      MethodInfo var2 = var0.getMethodInfo();
      String[] var3 = getCanonicalMethodParamNames(var0.getMethod());
      if (debugLogger.isDebugEnabled()) {
         StringBuilder var4 = new StringBuilder();
         if (var3.length > 0) {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               var4.append(var3[var5]).append(", ");
            }
         } else {
            var4.append("");
         }

         debug("\n\n ++++++++++  creating EJBMethodPermission: ejbName: '" + var1 + "' methodName: '" + var2.getMethodName() + "' interfaceType: '" + var2.getMethodInterfaceType() + "' methodParams: '" + var4.toString() + "'");
      }

      short var7 = var2.getMethodDescriptorMethodType();
      if (var7 == 1) {
         return new EJBMethodPermission(var1, "");
      } else {
         StringBuilder var8 = new StringBuilder(var2.getMethodName());
         var8.append(",");
         var8.append(var2.getMethodInterfaceType());
         if (var7 == 2) {
            return new EJBMethodPermission(var1, var8.toString());
         } else {
            for(int var6 = 0; var6 < var3.length; ++var6) {
               if (var6 == 0) {
                  var8.append(",");
               }

               var8.append(var3[var6]);
            }

            return new EJBMethodPermission(var1, var8.toString());
         }
      }
   }

   static EJBRoleRefPermission createEJBRoleRefPermission(String var0, String var1) {
      return new EJBRoleRefPermission(var0, var1);
   }

   private static AuthenticatedSubject obtainSubject() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return var0;
   }

   public Map getDeployableSecurityRoleMapping(SecurityRoleMapping var1, ApplicationContextInternal var2, int var3) throws WLDeploymentException {
      try {
         switch (var3) {
            case 0:
               if (debugLogger.isDebugEnabled()) {
                  debug("deployable role map calculated for Compatibility mode");
               }

               return this.getCompatibilitySecurityRoleMapping(var1);
            case 1:
               if (debugLogger.isDebugEnabled()) {
                  debug("deployable role map calculated for Application mode");
               }

               return this.getApplicationSecurityRoleMapping(var1, var2, var3);
            case 2:
               if (debugLogger.isDebugEnabled()) {
                  debug("deployable role map calculated for Externally Defined mode");
               }

               return this.getApplicationSecurityRoleMapping(var1, var2, var3);
            default:
               throw new AssertionError("Unexpected role mapping behavior: " + var3);
         }
      } catch (NoSuchRoleException var5) {
         throw new AssertionError("Unexpected exception: ", var5);
      }
   }

   private Map getCompatibilitySecurityRoleMapping(SecurityRoleMapping var1) throws NoSuchRoleException, WLDeploymentException {
      HashMap var2 = new HashMap();
      Collection var3 = var1.getSecurityRoleNames();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (var1.isExternallyDefinedRole(var5)) {
            if (debugLogger.isDebugEnabled()) {
               debug("role '" + var5 + "' is externally defined; skipping deployment");
            }
         } else {
            if (!var1.isRoleMappedToPrincipals(var5)) {
               EJBComplianceTextFormatter var8 = new EJBComplianceTextFormatter();
               throw new WLDeploymentException(var8.ROLE_NOT_MAPPED_TO_PRINCIPALS(var5));
            }

            Collection var6 = var1.getSecurityRolePrincipalNames(var5);
            String[] var7 = (String[])((String[])var6.toArray(new String[0]));
            var2.put(var5, var7);
         }
      }

      return var2;
   }

   private Map getApplicationSecurityRoleMapping(SecurityRoleMapping var1, ApplicationContextInternal var2, int var3) throws NoSuchRoleException {
      HashMap var4 = new HashMap();
      Collection var5 = var1.getSecurityRoleNames();
      Iterator var6 = var5.iterator();

      while(true) {
         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            if (var1.isExternallyDefinedRole(var7)) {
               if (debugLogger.isDebugEnabled()) {
                  debug("role '" + var7 + "' is externally defined at module level;" + " skipping deployment");
               }
            } else {
               SecurityRole var8 = var2.getSecurityRole(var7);
               if (!var1.isRoleMappedToPrincipals(var7)) {
                  if (var8 != null && var8.isExternallyDefined()) {
                     if (debugLogger.isDebugEnabled()) {
                        debug("role '" + var7 + "' is externally defined at app level " + "and no module principals defined; skipping deployment");
                     }
                  } else if (var8 != null && var8.getPrincipalNames() != null && var8.getPrincipalNames().length != 0) {
                     var4.put(var7, var8.getPrincipalNames());
                  } else if (var3 == 1) {
                     var4.put(var7, new String[0]);
                  } else if (debugLogger.isDebugEnabled()) {
                     debug("role '" + var7 + "' has no principals defined at app level " + "or module level; skipping deployment");
                  }
               } else {
                  Collection var9;
                  String[] var10;
                  if (var8 != null && var8.isExternallyDefined()) {
                     var9 = var1.getSecurityRolePrincipalNames(var7);
                     var10 = (String[])((String[])var9.toArray(new String[0]));
                     var4.put(var7, var10);
                  }

                  if (var8 != null && var8.getPrincipalNames() != null && var8.getPrincipalNames().length != 0) {
                     HashSet var11 = new HashSet();
                     var11.addAll(var1.getSecurityRolePrincipalNames(var7));
                     var11.addAll(Arrays.asList(var8.getPrincipalNames()));
                     var10 = (String[])((String[])var11.toArray(new String[0]));
                     var4.put(var7, var10);
                  } else {
                     var9 = var1.getSecurityRolePrincipalNames(var7);
                     var10 = (String[])((String[])var9.toArray(new String[0]));
                     var4.put(var7, var10);
                  }
               }
            }
         }

         return var4;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[SecurityHelper] " + var0);
   }

   static {
      debugLogger = EJBDebugService.securityLogger;
      subject = obtainSubject();
   }
}
