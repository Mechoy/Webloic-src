package weblogic.servlet.security.internal;

import java.io.IOException;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyConfigurationFactory;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.security.jacc.WebResourcePermission;
import javax.security.jacc.WebRoleRefPermission;
import javax.security.jacc.WebUserDataPermission;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.j2ee.descriptor.AuthConstraintBean;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.UserDataConstraintBean;
import weblogic.j2ee.descriptor.WebResourceCollectionBean;
import weblogic.management.DeploymentException;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.CommonPolicyContextHandler;
import weblogic.security.jacc.DelegatingPolicyContextHandler;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.jacc.RoleMapperFactory;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.collections.SoftHashMap;

final class WebAppSecurityJacc extends WebAppSecurity {
   private static final boolean DEBUG = false;
   private static final char DELIMITER = '_';
   private static final boolean CACHE = true;
   private final RoleMapper roleMapper;
   private final PolicyConfiguration policyConfig;
   private final CodeSource codeSource;
   private final ProtectionDomain protectionDomain;
   private final String contextId;
   private HashMap patterns = new HashMap();
   private SoftHashMap pdCache = new SoftHashMap();
   private SoftHashMap udPermCache = new SoftHashMap();
   private SoftHashMap rrPermCache = new SoftHashMap();
   private SoftHashMap resPermCache = new SoftHashMap();

   WebAppSecurityJacc(WebAppServletContext var1) throws DeploymentException {
      super(var1);

      PolicyConfigurationFactory var2;
      try {
         var2 = PolicyConfigurationFactory.getPolicyConfigurationFactory();
      } catch (ClassNotFoundException var10) {
         throw new DeploymentException(var10);
      } catch (PolicyContextException var11) {
         throw new DeploymentException(var11);
      }

      String var3 = var1.getContextPath();
      var3 = var3.replace('/', '_');
      String var4 = ApplicationVersionUtils.replaceDelimiter(var1.getApplicationId(), '_');
      this.contextId = (new StringBuffer(101)).append(var1.getServer().getName()).append('_').append(var4).append('_').append(var3).toString();

      try {
         this.policyConfig = var2.getPolicyConfiguration(this.contextId, true);
      } catch (PolicyContextException var9) {
         throw new DeploymentException(var9);
      }

      RoleMapperFactory var5;
      try {
         var5 = RoleMapperFactory.getRoleMapperFactory();
      } catch (ClassNotFoundException var7) {
         throw new DeploymentException(var7);
      } catch (PolicyContextException var8) {
         throw new DeploymentException(var8);
      }

      ApplicationContextInternal var6 = this.context.getApplicationContext();
      this.roleMapper = var5.getRoleMapper(var6.getApplicationId(), this.contextId, false);
      this.codeSource = this.initializeCodeSource();
      this.protectionDomain = new ProtectionDomain(this.codeSource, (PermissionCollection)null);
      var6.addJACCPolicyConfiguration(this.policyConfig);
   }

   protected boolean isJaccEnabled() {
      return true;
   }

   protected boolean isFullSecurityDelegationRequired() {
      return true;
   }

   protected void deployRoles() throws DeploymentException {
      if (!this.roles.isEmpty()) {
         Iterator var1 = this.roles.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            String[] var3 = (String[])((String[])this.roleMapping.get(var2));
            if (!this.isExternallyDefined(var3)) {
               WebRoleRefPermission var4 = new WebRoleRefPermission("", var2);

               try {
                  this.policyConfig.addToRole(var2, var4);
               } catch (PolicyContextException var6) {
                  throw new DeploymentException(var6);
               }
            }
         }

         if (this.roleMapping != null && !this.roleMapping.isEmpty()) {
            this.roleMapper.addAppRolesToPrincipalMap(this.roleMapping);
         }

      }
   }

   protected void deployPolicies(SecurityConstraintBean[] var1) throws DeploymentException {
      this.collectPatterns(var1);
      this.computeQualifiedPatterns();
      this.deployRegisteredPolicies(var1);
      this.deployUncheckedPoliciesForException();
   }

   private void deployUncheckedPoliciesForException() throws DeploymentException {
      Iterator var1 = this.patterns.values().iterator();

      while(var1.hasNext()) {
         PatternHelper var2 = (PatternHelper)var1.next();
         String[] var3;
         String var4;
         if (var2.getMethodExceptionForWRP() != null) {
            var3 = new String[var2.getMethodExceptionForWRP().size()];
            var2.getMethodExceptionForWRP().toArray(var3);
            var4 = this.methodsToAction(var3);
            if (var4 != null) {
               var4 = "!" + var4;
            }

            WebResourcePermission var5 = new WebResourcePermission(var2.getQualifiedPattern(), var4);

            try {
               this.policyConfig.addToUncheckedPolicy(var5);
            } catch (PolicyContextException var8) {
               throw new DeploymentException(var8);
            }
         }

         if (var2.getMethodExceptionForUDC() != null) {
            var3 = new String[var2.getMethodExceptionForUDC().size()];
            var2.getMethodExceptionForUDC().toArray(var3);
            var4 = this.methodsToAction(var3);
            if (var4 != null) {
               var4 = "!" + var4;
            }

            WebUserDataPermission var9 = new WebUserDataPermission(var2.getQualifiedPattern(), var4);

            try {
               this.policyConfig.addToUncheckedPolicy(var9);
            } catch (PolicyContextException var7) {
               throw new DeploymentException(var7);
            }
         }
      }

   }

   private void deployRegisteredPolicies(SecurityConstraintBean[] var1) throws DeploymentException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            SecurityConstraintBean var3 = var1[var2];
            WebResourceCollectionBean[] var4 = var3.getWebResourceCollections();
            if (var4 != null) {
               this.registerWebResourceCollections(var1[var2], var4);
            }
         }

      }
   }

   private void registerWebResourceCollections(SecurityConstraintBean var1, WebResourceCollectionBean[] var2) throws DeploymentException {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.registerSecurityPattern(var2[var3], var1);
      }

   }

   private void registerSecurityPattern(WebResourceCollectionBean var1, SecurityConstraintBean var2) throws DeploymentException {
      String[] var3 = var1.getHttpMethods();
      String[] var4 = var1.getUrlPatterns();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         String var6 = fixupURLPattern(var4[var5]);
         PatternHelper var7 = (PatternHelper)this.patterns.get(var6);
         this.registerAuthConstraint(var7, var3, var2);
         UserDataConstraintBean var8 = var2.getUserDataConstraint();
         if (var8 != null) {
            this.registerUserDataConstraints(var8, var7, var3);
         }
      }

   }

   private void registerAuthConstraint(PatternHelper var1, String[] var2, SecurityConstraintBean var3) throws DeploymentException {
      String[] var4 = null;
      AuthConstraintBean var5 = var3.getAuthConstraint();
      if (var5 != null) {
         var4 = var5.getRoleNames();
         var1.addExceptionMethodsForWRP(var2);
         if (var4 != null && var4.length >= 1) {
            this.deployRoleBasedPolicies(var4, var1, var2);
         } else {
            this.deployExcludedPolicy(var1, var2);
            var1.addExceptionMethodsForUDC(var2);
         }

      }
   }

   private void registerUserDataConstraints(UserDataConstraintBean var1, PatternHelper var2, String[] var3) throws DeploymentException {
      String var4 = var1.getTransportGuarantee();
      if (var4.equals("CONFIDENTIAL") || var4.equals("INTEGRAL")) {
         var2.addExceptionMethodsForUDC(var3);
         String var5 = this.methodsToAction(var3) + ':' + var4;
         WebUserDataPermission var6 = new WebUserDataPermission(var2.getQualifiedPattern(), var5);

         try {
            this.policyConfig.addToUncheckedPolicy(var6);
         } catch (PolicyContextException var8) {
            throw new DeploymentException(var8);
         }
      }

   }

   private void deployRoleBasedPolicies(String[] var1, PatternHelper var2, String[] var3) throws DeploymentException {
      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         if (var1[var4].equals("*")) {
            var1 = new String[this.roles.size()];
            this.roles.toArray(var1);
            break;
         }
      }

      for(var4 = 0; var4 < var1.length; ++var4) {
         WebResourcePermission var5 = new WebResourcePermission(var2.getQualifiedPattern(), this.methodsToAction(var3));

         try {
            this.policyConfig.addToRole(var1[var4], var5);
         } catch (PolicyContextException var7) {
            throw new DeploymentException(var7);
         }
      }

   }

   private void deployExcludedPolicy(PatternHelper var1, String[] var2) throws DeploymentException {
      WebResourcePermission var3 = new WebResourcePermission(var1.getQualifiedPattern(), this.methodsToAction(var2));
      WebUserDataPermission var4 = new WebUserDataPermission(var1.getQualifiedPattern(), this.methodsToAction(var2));

      try {
         this.policyConfig.addToExcludedPolicy(var3);
         this.policyConfig.addToExcludedPolicy(var4);
      } catch (PolicyContextException var6) {
         throw new DeploymentException(var6);
      }
   }

   private String methodsToAction(String[] var1) {
      if (var1 != null && var1.length != 0) {
         StringBuffer var2 = new StringBuffer(101);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var3 != 0) {
               var2.append(',');
            }

            var2.append(var1[var3]);
         }

         return var2.toString();
      } else {
         return null;
      }
   }

   private void collectPatterns(SecurityConstraintBean[] var1) {
      this.patterns.put("/", new PatternHelper("/"));
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            WebResourceCollectionBean[] var3 = var1[var2].getWebResourceCollections();
            if (var3 != null && var3.length >= 1) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  String[] var5 = var3[var4].getUrlPatterns();
                  if (var5 != null) {
                     for(int var6 = 0; var6 < var5.length; ++var6) {
                        String var7 = fixupURLPattern(var5[var6]);
                        this.patterns.put(var7, new PatternHelper(var7));
                     }
                  }
               }
            }
         }

      }
   }

   private void computeQualifiedPatterns() {
      if (!this.patterns.isEmpty()) {
         Iterator var1 = this.patterns.values().iterator();

         while(var1.hasNext()) {
            PatternHelper var2 = (PatternHelper)var1.next();
            var2.computeQualifiedPatterns(this.patterns);
         }

      }
   }

   protected void deployRoleLink(ServletStubImpl var1, String var2, String var3) throws DeploymentException {
      WebRoleRefPermission var4 = new WebRoleRefPermission(var1.getServletName(), var2);

      try {
         this.policyConfig.addToRole(var3, var4);
      } catch (PolicyContextException var6) {
         throw new DeploymentException(var6);
      }
   }

   public void registerRoleRefs(ServletStubImpl var1) throws DeploymentException {
      if (!this.roles.isEmpty()) {
         HashSet var2 = this.getRoleRefsFromServletBean(var1.getServletName());
         Iterator var3 = this.roles.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (!var2.contains(var4)) {
               WebRoleRefPermission var5 = new WebRoleRefPermission(var1.getServletName(), var4);

               try {
                  this.policyConfig.addToRole(var4, var5);
               } catch (PolicyContextException var7) {
                  throw new DeploymentException(var7);
               }
            }
         }

      }
   }

   private HashSet getRoleRefsFromServletBean(String var1) {
      HashSet var2 = new HashSet();
      ServletBean var3 = this.getContext().getWebAppModule().getWebAppBean().lookupServlet(var1);
      if (var3 != null) {
         SecurityRoleRefBean[] var4 = var3.getSecurityRoleRefs();
         if (var4 != null && var4.length > 0) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               var2.add(var4[var5].getRoleName());
            }
         }
      }

      return var2;
   }

   public void start() {
   }

   private CodeSource initializeCodeSource() throws DeploymentException {
      URL var1;
      try {
         URI var2 = new URI("file:///" + this.context.getDocroot().replace('\\', '/'));
         var1 = new URL(var2.toString());
      } catch (URISyntaxException var3) {
         throw new DeploymentException(var3);
      } catch (MalformedURLException var4) {
         throw new DeploymentException(var4);
      }

      return new CodeSource(var1, (Certificate[])null);
   }

   public void initContextHandler(ServletRequestImpl var1) {
      WebAppContextHandlerData var2 = new WebAppContextHandlerData(var1);
      PolicyContext.setHandlerData(var2);
   }

   public void resetContextHandler() {
      PolicyContext.setHandlerData((Object)null);
   }

   boolean checkTransport(ResourceConstraint var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      String var4 = getRelativeURI(var2);
      if (var4.length() == 1 && var4.charAt(0) == '/') {
         var4 = "";
      }

      String var5 = var2.getMethod();
      if (var2.isSecure()) {
         var5 = var5 + ":CONFIDENTIAL";
      }

      boolean var6 = false;

      try {
         var6 = this.checkTransport(var4, var5);
         if (var6) {
            return true;
         } else {
            if (var2.isSecure()) {
               var3.sendError(403);
            } else {
               var5 = var5 + ":CONFIDENTIAL";
               var6 = this.checkTransport(var4, var5);
               if (!var6) {
                  var3.sendError(403);
               } else {
                  String var7 = this.getSecuredURL(var2, var3, var2.getRequestURI());
                  if (var7 != null) {
                     var3.sendRedirect(var7);
                  } else {
                     var3.sendError(403);
                  }
               }
            }

            return var6;
         }
      } catch (SecurityException var8) {
         HTTPLogger.logSecurityException("user data constraints check", var2.getRequestURI(), this.context.getLogContext(), var8);
         return false;
      }
   }

   public boolean isSSLRequired(String var1, String var2) {
      if (var1.length() == 1 && var1.charAt(0) == '/') {
         var1 = "";
      }

      String var3 = var2;

      try {
         boolean var4 = this.checkTransport(var1, var3);
         if (var4) {
            return false;
         } else {
            var3 = var3 + ":CONFIDENTIAL";
            return this.checkTransport(var1, var3);
         }
      } catch (SecurityException var5) {
         HTTPLogger.logSecurityException("user data constraints check", var1, this.context.getLogContext(), var5);
         return true;
      }
   }

   private boolean checkTransport(String var1, String var2) {
      WebUserDataPermission var3 = this.getWebUserDataPermission(var1, var2);
      return this.implies(var3, this.protectionDomain);
   }

   public boolean isSubjectInRole(AuthenticatedSubject var1, String var2, WebAppContextHandler var3, ServletStubImpl var4) {
      ProtectionDomain var5 = this.getProtectionDomainForSubject(var1);
      String var6;
      if (var4.isDynamicallyGenerated()) {
         var6 = "";
      } else {
         var6 = var4.getServletName();
      }

      WebRoleRefPermission var7 = this.getWebRoleRefPermission(var6, var2);
      boolean var8 = false;

      try {
         var8 = this.implies(var7, var5);
         return var8;
      } catch (SecurityException var10) {
         HTTPLogger.logSecurityException("isUserInRole check", var1.getName(), this.context.getLogContext(), var10);
         return false;
      }
   }

   public boolean hasPermission(HttpServletRequest var1, HttpServletResponse var2, AuthenticatedSubject var3, ResourceConstraint var4) {
      if (this.context.isAdminMode() && this.context.isInternalApp() && ConnectionSigner.isConnectionSigned(var1)) {
         return true;
      } else if (this.context.isAdminMode()) {
         return this.checkAdminMode(var3);
      } else {
         ProtectionDomain var5 = this.getProtectionDomainForSubject(var3);
         WebResourcePermission var6 = this.getWebResourcePermission(var1);
         boolean var7 = false;

         try {
            var7 = this.implies(var6, var5);
         } catch (SecurityException var9) {
            HTTPLogger.logSecurityException("permission check", var1.getRequestURI(), this.context.getLogContext(), var9);
            var7 = false;
         }

         return var7;
      }
   }

   private WebUserDataPermission getWebUserDataPermission(String var1, String var2) {
      PermKey var3 = new PermKey(var1, var2);
      WebUserDataPermission var4 = (WebUserDataPermission)this.udPermCache.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         var4 = new WebUserDataPermission(var1, var2);
         this.udPermCache.put(var3, var4);
         return var4;
      }
   }

   private WebRoleRefPermission getWebRoleRefPermission(String var1, String var2) {
      PermKey var3 = new PermKey(var1, var2);
      WebRoleRefPermission var4 = (WebRoleRefPermission)this.rrPermCache.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         var4 = new WebRoleRefPermission(var1, var2);
         this.rrPermCache.put(var3, var4);
         return var4;
      }
   }

   private WebResourcePermission getWebResourcePermission(HttpServletRequest var1) {
      String var2 = var1.getServletPath();
      if (var2.length() == 1 && var2.charAt(0) == '/') {
         var2 = "";
      }

      String var3 = var1.getMethod();
      PermKey var4 = new PermKey(var2, var3);
      WebResourcePermission var5 = (WebResourcePermission)this.resPermCache.get(var4);
      if (var5 != null) {
         return var5;
      } else {
         var5 = new WebResourcePermission(var2, var3);
         this.resPermCache.put(var4, var5);
         return var5;
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

      ProtectionDomain var3 = (ProtectionDomain)this.pdCache.get(var1);
      if (var3 != null) {
         return var3;
      } else {
         var3 = new ProtectionDomain(this.codeSource, (PermissionCollection)null, (ClassLoader)null, var2);
         this.pdCache.put(var1, var3);
         return var3;
      }
   }

   private boolean implies(Permission var1, ProtectionDomain var2) {
      String var3 = PolicyContext.getContextID();
      this.setPolicyContext(this.contextId);

      boolean var4;
      try {
         var4 = Policy.getPolicy().implies(var2, var1);
      } finally {
         this.setPolicyContext(var3);
      }

      return var4;
   }

   private void setPolicyContext(final String var1) {
      String var2 = PolicyContext.getContextID();
      if (var2 != var1 && (var2 == null || var1 == null || !var2.equals(var1))) {
         try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  PolicyContext.setContextID(var1);
                  return null;
               }
            });
         } catch (PrivilegedActionException var5) {
            Object var4 = var5;
            if (var5.getCause() != null) {
               var4 = var5.getCause();
            }

            throw new SecurityException(((Throwable)var4).getMessage());
         }
      }

   }

   public ResourceConstraint getConstraint(HttpServletRequest var1) {
      return null;
   }

   public void unregister() {
      super.unregister();

      try {
         this.policyConfig.delete();
      } catch (PolicyContextException var2) {
         HTTPLogger.logFailedToUndeploySecurityPolicy(this.policyConfig.toString(), var2);
      }

   }

   static {
      CommonPolicyContextHandler var0 = new CommonPolicyContextHandler();
      String[] var1 = WebAppContextHandlerData.getKeys();
      DelegatingPolicyContextHandler var2 = new DelegatingPolicyContextHandler(var1);

      try {
         PolicyContext.registerHandler("javax.security.auth.Subject.container", var0, true);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            PolicyContext.registerHandler(var1[var3], var2, true);
         }
      } catch (PolicyContextException var4) {
         HTTPLogger.logFailedToRegisterPolicyContextHandlers(var4);
      }

   }

   private class PermKey {
      private String key1;
      private String key2;

      private PermKey(String var2, String var3) {
         this.key1 = var2;
         this.key2 = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof PermKey)) {
            return false;
         } else {
            PermKey var2 = (PermKey)var1;
            if (this.key2 != null) {
               if (!this.key2.equals(var2.key2)) {
                  return false;
               }
            } else if (var2.key2 != null) {
               return false;
            }

            if (this.key1 != null) {
               if (!this.key1.equals(var2.key1)) {
                  return false;
               }
            } else if (var2.key1 != null) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         int var1 = this.key1 != null ? this.key1.hashCode() : 0;
         var1 = 29 * var1 + (this.key2 != null ? this.key2.hashCode() : 0);
         return var1;
      }

      // $FF: synthetic method
      PermKey(String var2, String var3, Object var4) {
         this(var2, var3);
      }
   }

   private static class PatternHelper {
      private final String pattern;
      private final boolean prefixPattern;
      private final boolean exactPattern;
      private final boolean extensionPattern;
      private final boolean defaultPattern;
      private final String prefix;
      private HashSet methodExceptionForWRP = new HashSet();
      private HashSet methodExceptionForUDC = new HashSet();
      private String qualifiedPattern;
      private final String extension;

      PatternHelper(String var1) {
         this.pattern = var1;
         this.qualifiedPattern = this.pattern;
         if (var1 == null) {
            throw new IllegalArgumentException("pattern is null");
         } else {
            if (var1.length() == 1 && var1.charAt(0) == '/') {
               this.defaultPattern = true;
               this.exactPattern = false;
               this.extensionPattern = false;
               this.prefixPattern = false;
               this.prefix = "";
               this.extension = null;
            } else if (var1.startsWith("*.")) {
               this.exactPattern = false;
               this.extensionPattern = true;
               this.prefixPattern = false;
               this.defaultPattern = false;
               this.prefix = null;
               this.extension = this.pattern.substring(1);
            } else if (var1.length() == 2 && var1.equals("/*")) {
               this.exactPattern = false;
               this.extensionPattern = false;
               this.prefixPattern = true;
               this.defaultPattern = true;
               this.prefix = "";
               this.extension = null;
            } else if (var1.endsWith("/*")) {
               this.exactPattern = false;
               this.extensionPattern = false;
               this.prefixPattern = true;
               this.defaultPattern = false;
               this.prefix = this.pattern.substring(0, this.pattern.length() - 1);
               this.extension = null;
            } else {
               this.exactPattern = true;
               this.extensionPattern = false;
               this.prefixPattern = false;
               this.defaultPattern = false;
               this.prefix = var1;
               this.extension = null;
            }

         }
      }

      String getPattern() {
         return this.pattern;
      }

      boolean isPrefixPattern() {
         return this.prefixPattern;
      }

      boolean isExactPattern() {
         return this.exactPattern;
      }

      boolean isExtensionPattern() {
         return this.extensionPattern;
      }

      boolean isDefaultPattern() {
         return this.defaultPattern;
      }

      String getPrefix() {
         return this.prefix;
      }

      HashSet getMethodExceptionForWRP() {
         return this.methodExceptionForWRP;
      }

      HashSet getMethodExceptionForUDC() {
         return this.methodExceptionForUDC;
      }

      String getQualifiedPattern() {
         return this.qualifiedPattern;
      }

      private void computeQualifiedPatterns(HashMap var1) {
         if (!this.isExactPattern()) {
            Iterator var2 = var1.values().iterator();

            while(true) {
               PatternHelper var3;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (PatternHelper)var2.next();
               } while(var3.getPattern().equals(this.pattern));

               boolean var4 = false;
               if (this.isDefaultPattern()) {
                  var4 = !var3.getPattern().equals("/") && !var3.getPattern().equals("/*");
               } else if (!this.isPrefixPattern()) {
                  if (var3.isPrefixPattern()) {
                     var4 = true;
                  } else if (var3.isExactPattern()) {
                     var4 = var3.getPattern().endsWith(this.extension);
                  }
               } else if (var3.isPrefixPattern()) {
                  var4 = var3.getPrefix().startsWith(this.prefix);
               } else if (var3.isExactPattern()) {
                  var4 = var3.getPattern().startsWith(this.prefix) || this.prefix.equals(var3.getPattern() + "/");
               }

               if (var4) {
                  this.qualifiedPattern = this.qualifiedPattern + ":" + var3.getPattern();
               }
            }
         }
      }

      private void addExceptionMethodsForWRP(String[] var1) {
         if (var1 != null && var1.length != 0) {
            if (this.methodExceptionForWRP == null) {
               this.methodExceptionForWRP = new HashSet();
            }

            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.methodExceptionForWRP.add(var1[var2]);
            }

         } else {
            this.methodExceptionForWRP = null;
         }
      }

      private void addExceptionMethodsForUDC(String[] var1) {
         if (var1 != null && var1.length != 0) {
            if (this.methodExceptionForUDC == null) {
               this.methodExceptionForUDC = new HashSet();
            }

            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.methodExceptionForUDC.add(var1[var2]);
            }

         } else {
            this.methodExceptionForUDC = null;
         }
      }
   }
}
