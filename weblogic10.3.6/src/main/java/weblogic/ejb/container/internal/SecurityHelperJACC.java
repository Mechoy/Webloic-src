package weblogic.ejb.container.internal;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import weblogic.application.ApplicationContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.NoSuchRoleException;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.CommonPolicyContextHandler;
import weblogic.security.jacc.DelegatingPolicyContextHandler;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.service.ContextHandler;
import weblogic.utils.AssertionError;
import weblogic.utils.StringUtils;

final class SecurityHelperJACC {
   private static final String DONT_REGISTER_UNCOVERED_METHODS = "weblogic.ejb.container.internal.SecurityHelperJACC.dont_register_uncovered_methods";
   private static final boolean dont_register_uncovered_methods = System.getProperty("weblogic.ejb.container.internal.SecurityHelperJACC.dont_register_uncovered_methods") != null;
   private static final DebugLogger debugLogger;
   private String jaccPolicyContextId;
   private String jaccCodeSourceLocation;
   private CodeSource jaccCodeSource;
   private PolicyConfiguration jaccPolicyConfig;
   private RoleMapper jaccRoleMapper;

   SecurityHelperJACC(PolicyConfiguration var1, String var2, String var3, RoleMapper var4) throws WLDeploymentException {
      this.jaccPolicyConfig = var1;
      this.jaccPolicyContextId = var2;
      this.jaccCodeSourceLocation = var3;
      this.jaccRoleMapper = var4;

      try {
         URI var5 = new URI("file:///" + var3.replace('\\', '/'));
         URL var6 = new URL(var5.toString());
         this.jaccCodeSource = new CodeSource(var6, (Certificate[])null);
      } catch (MalformedURLException var7) {
         throw new WLDeploymentException(var7.getMessage(), var7);
      } catch (URISyntaxException var8) {
         throw new WLDeploymentException(var8.getMessage(), var8);
      }
   }

   void deployRoles(DeploymentInfo var1, SecurityRoleMapping var2) {
      String var3 = var1.getApplicationName();
      String var4 = var1.getEJBComponentName();
      Collection var5 = var2.getSecurityRoleNames();
      if (debugLogger.isDebugEnabled()) {
         debug("deployRoles(...), appName: '" + var3 + "', ejbComponentName: '" + var4 + "'  there are: '" + var5.size() + "' roles in this jar.");
      }

      if (!var5.isEmpty()) {
         HashMap var6 = new HashMap();
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();

            try {
               if (var2.isExternallyDefinedRole(var8)) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("skipping deployment of role: " + var8 + " because it's externally defined");
                  }
               } else if (!var2.isRoleMappedToPrincipals(var8)) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("skipping deployment of role: " + var8 + " because it's not mapped to any principals");
                  }
               } else {
                  Collection var9 = var2.getSecurityRolePrincipalNames(var8);
                  String[] var10 = (String[])((String[])var9.toArray(new String[0]));
                  if (debugLogger.isDebugEnabled()) {
                     debug("deploying role: " + var8 + " with principals: " + StringUtils.join(var10, ", "));
                  }

                  var6.put(var8, var10);
               }
            } catch (NoSuchRoleException var11) {
               throw new AssertionError("Unexpected exception: ", var11);
            }
         }

         if (!var6.isEmpty()) {
            this.jaccRoleMapper.addAppRolesToPrincipalMap(var6);
            if (debugLogger.isDebugEnabled()) {
               debug("Role mapping to add to the RoleMapper for appName: '" + var3 + "', ejbComponentName: '" + var4 + "'");
            }
         } else if (debugLogger.isDebugEnabled()) {
            debug("No Role mapping to add to the RoleMapper for appName: '" + var3 + "', ejbComponentName: '" + var4 + "'");
         }

      }
   }

   void setupApplicationInfo(ApplicationContext var1, DeploymentInfo var2) {
      var1.addJACCPolicyConfiguration(this.jaccPolicyConfig);
   }

   void unDeployRoles() {
   }

   void deployPolicies(List<MethodDescriptor> var1, List<MethodDescriptor> var2, List<MethodDescriptor> var3, SecurityHelper var4) throws Exception {
      Iterator var5;
      MethodDescriptor var6;
      if (var1 != null) {
         var5 = var1.iterator();

         while(var5.hasNext()) {
            var6 = (MethodDescriptor)var5.next();
            this.deployPolicy(var6, var4);
         }
      }

      if (var2 != null) {
         var5 = var2.iterator();

         while(var5.hasNext()) {
            var6 = (MethodDescriptor)var5.next();
            this.deployPolicy(var6, var4);
         }
      }

      if (var3 != null) {
         var5 = var3.iterator();

         while(var5.hasNext()) {
            var6 = (MethodDescriptor)var5.next();
            this.deployPolicy(var6, var4);
         }
      }

   }

   boolean deployPolicy(MethodDescriptor var1, SecurityHelper var2) throws Exception {
      MethodInfo var3 = var1.getMethodInfo();
      Set var4 = var3.getSecurityRoleNames();
      EJBMethodPermission var5 = SecurityHelper.createEJBMethodPermission(var1);
      var1.setSecurityHelper(var2);
      var1.setEJBMethodPermission(var5);
      if (!var3.needsSecurityCheck()) {
         return false;
      } else {
         String var7;
         if (var4.isEmpty()) {
            if (debugLogger.isDebugEnabled()) {
               debug(" no policy for " + var5);
            }

            if (!dont_register_uncovered_methods && !var3.getIsExcluded()) {
               if (debugLogger.isDebugEnabled()) {
                  debug("  deploying uncovered method as 'unchecked': '" + var5 + "'");
               }

               this.jaccPolicyConfig.addToUncheckedPolicy(var5);
            }
         } else {
            for(Iterator var6 = var4.iterator(); var6.hasNext(); this.jaccPolicyConfig.addToRole(var7, var5)) {
               var7 = (String)var6.next();
               if (debugLogger.isDebugEnabled()) {
                  debug("  next roleName is: '" + var7 + "'");
               }

               if (debugLogger.isDebugEnabled()) {
                  debug("registerRolesWithMethod, jaccPolicyConfig.addToRole " + var7 + ", " + var5);
               }
            }
         }

         if (var3.getUnchecked()) {
            this.jaccPolicyConfig.addToUncheckedPolicy(var5);
         }

         if (var3.getIsExcluded()) {
            this.jaccPolicyConfig.addToExcludedPolicy(var5);
         }

         return true;
      }
   }

   void processUncheckedExcludedMethod(MethodDescriptor var1) throws WLDeploymentException {
      MethodInfo var2 = var1.getMethodInfo();
      EJBMethodPermission var3;
      if (var2.getUnchecked()) {
         var3 = SecurityHelper.createEJBMethodPermission(var1);

         try {
            this.jaccPolicyConfig.addToUncheckedPolicy(var3);
         } catch (PolicyContextException var5) {
            throw new WLDeploymentException(var5.getMessage(), var5);
         }
      } else if (var2.getIsExcluded()) {
         var3 = SecurityHelper.createEJBMethodPermission(var1);

         try {
            this.jaccPolicyConfig.addToExcludedPolicy(var3);
         } catch (PolicyContextException var6) {
            throw new WLDeploymentException(var6.getMessage(), var6);
         }
      }
   }

   void registerRoleRefs(String var1, Map var2) throws WLDeploymentException {
      Set var3 = var2.keySet();
      if (var3.size() > 0) {
         try {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               SecurityRoleReference var6 = (SecurityRoleReference)var2.get(var5);
               String var7 = var6.getReferencedRole();
               EJBRoleRefPermission var8 = SecurityHelper.createEJBRoleRefPermission(var1, var5);
               this.jaccPolicyConfig.addToRole(var7, var8);
            }

         } catch (PolicyContextException var9) {
            throw new WLDeploymentException(var9.getMessage(), var9);
         }
      }
   }

   void activate() {
   }

   void deactivate() {
      try {
         this.jaccPolicyConfig.delete();
      } catch (PolicyContextException var2) {
      }

   }

   public void setContext(ContextHandler var1) {
      PolicyContext.setHandlerData(var1);
   }

   public void resetContext() {
      PolicyContext.setHandlerData((Object)null);
   }

   boolean isAccessAllowed(EJBMethodPermission var1, ContextHandler var2) {
      AuthenticatedSubject var3 = SecurityHelper.getCurrentSubject();
      Principal[] var4;
      if (var3 != null) {
         var4 = new Principal[var3.getPrincipals().size()];
         var3.getPrincipals().toArray(var4);
      } else {
         var4 = new Principal[0];
      }

      ProtectionDomain var5 = new ProtectionDomain(this.jaccCodeSource, (PermissionCollection)null, (ClassLoader)null, var4);
      boolean var6 = false;

      try {
         var6 = this.implies(var1, var5);
      } catch (SecurityException var8) {
         var8.printStackTrace();
         var6 = false;
      }

      return var6;
   }

   boolean isCallerInRole(String var1, AuthenticatedSubject var2, String var3) {
      ProtectionDomain var4 = this.getProtectionDomainForSubject(var2);
      EJBRoleRefPermission var5 = new EJBRoleRefPermission(var1, var3);
      boolean var6 = false;

      try {
         var6 = this.implies(var5, var4);
         return var6;
      } catch (SecurityException var8) {
         var8.printStackTrace();
         return false;
      }
   }

   private boolean implies(Permission var1, ProtectionDomain var2) {
      String var3 = PolicyContext.getContextID();

      try {
         this.setPolicyContext(this.jaccPolicyContextId);
         if (debugLogger.isDebugEnabled()) {
            debug("about to call Policy.getPolicy().implies on ProtectionDomain: " + var2 + ", permission: " + var1);
         }

         boolean var4 = Policy.getPolicy().implies(var2, var1);
         return var4;
      } catch (Throwable var14) {
         var14.printStackTrace();
      } finally {
         try {
            this.setPolicyContext(var3);
         } catch (Throwable var13) {
         }

      }

      return false;
   }

   private void setPolicyContext(final String var1) throws Throwable {
      String var2 = PolicyContext.getContextID();
      if (var2 != var1 && (var2 == null || var1 == null || !var2.equals(var1))) {
         try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  PolicyContext.setContextID(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var4) {
            throw var4.getCause();
         }
      } else if (debugLogger.isDebugEnabled()) {
         debug("#### setPolicyContext(): Policy Context ID was the same: " + var2);
      }

   }

   private ProtectionDomain getProtectionDomainForSubject(AuthenticatedSubject var1) {
      Principal[] var2;
      if (var1 != null) {
         var2 = new Principal[var1.getPrincipals().size()];
         var1.getPrincipals().toArray(var2);
      } else {
         var2 = new Principal[0];
      }

      return new ProtectionDomain(this.jaccCodeSource, (PermissionCollection)null, (ClassLoader)null, var2);
   }

   private static void debug(String var0) {
      debugLogger.debug("[SecurityHelperJACC] " + var0);
   }

   static {
      debugLogger = EJBDebugService.securityLogger;
      CommonPolicyContextHandler var0 = new CommonPolicyContextHandler();
      String[] var1 = EJBContextHandler.getKeys();
      DelegatingPolicyContextHandler var2 = new DelegatingPolicyContextHandler(var1);

      try {
         PolicyContext.registerHandler("javax.security.auth.Subject.container", var0, true);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            PolicyContext.registerHandler(var1[var3], var2, true);
         }
      } catch (PolicyContextException var4) {
         EJBLogger.logFailedToRegisterPolicyContextHandlers(var4);
      }

   }
}
