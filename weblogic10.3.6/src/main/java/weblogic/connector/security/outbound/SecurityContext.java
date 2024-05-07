package weblogic.connector.security.outbound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.SecurityException;
import javax.resource.spi.security.GenericCredential;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.Debug;
import weblogic.connector.extensions.Unshareable;
import weblogic.connector.external.AuthMechInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.RAInfo;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.EISResource;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public final class SecurityContext {
   public static final String SHARED_APPNAME = "WEBLOGIC_SHAREDAPP";
   private static final String ipAnonymousConnectionsName = "weblogic_ra_anonymous";
   private static final String ipInitialConnectionsName = "weblogic_ra_initial";
   private static final String ipDefaultConnectionsName = "weblogic_ra_default";
   private AuthenticatedSubject currentSubject = null;
   private AuthorizationManager am = null;
   private ConnectionRequestInfo clientInfo;
   private boolean isContainerManaged;
   private boolean shareable;
   private boolean alwaysUnshareable;
   private String poolName;
   private Subject rpSubject;
   private OutboundInfo outboundInfo;
   private EISResource globalEISRes;
   private EISResource poolEISRes;
   private static Context initialContext;

   public SecurityContext(OutboundInfo var1, String var2, String var3, String var4, ManagedConnectionFactory var5, ConnectionRequestInfo var6, boolean var7, AuthenticatedSubject var8) throws SecurityException {
      this.initialize(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private void initialize(OutboundInfo var1, String var2, String var3, String var4, ManagedConnectionFactory var5, ConnectionRequestInfo var6, boolean var7, AuthenticatedSubject var8) throws SecurityException {
      this.am = (AuthorizationManager)SecurityServiceManager.getSecurityService(var8, SecurityServiceManager.getDefaultRealmName(), ServiceType.AUTHORIZE);
      this.currentSubject = SecurityServiceManager.getCurrentSubject(var8);
      this.outboundInfo = var1;
      this.poolName = var4;
      this.clientInfo = var6;
      this.isContainerManaged = true;
      if (isUnshareableMCF(var5.getClass())) {
         this.alwaysUnshareable = true;
         this.setShareable(false);
         if (Debug.isSecurityCtxEnabled()) {
            this.debug("The MCF has @Unshareable annotation on it, so it doesn't support share.");
         }
      } else {
         this.setShareable(true);
      }

      this.rpSubject = null;
      this.globalEISRes = getGlobalEISResource(var2, var3, var1.getRAInfo());
      this.poolEISRes = getPoolEISResource(var2, var3, var1);
      if (Debug.isSecurityCtxEnabled()) {
         Debug.securityCtx("For pool '" + var4 + "' initializing SecurityContext with AppName = " + ApplicationVersionUtils.getDisplayName(this.globalEISRes.getApplicationName()) + ", ModuleName = " + this.globalEISRes.getModuleName() + ", EIS Type = " + this.globalEISRes.getType() + ", DestinationId = " + this.poolEISRes.getDestinationId() + ", Global ResourceId = " + this.globalEISRes.toString() + ", Pool ResourceId = " + this.poolEISRes.toString());
      }

      this.initSubject(var5, var7, var8);
   }

   public static boolean isUnshareableMCF(Class var0) {
      boolean var1 = var0.isAnnotationPresent(Unshareable.class);
      if (var1 && Debug.isSecurityCtxEnabled()) {
         Debug.securityCtx("Find Unshareable annotation on MCF class: " + var0);
      }

      return var1;
   }

   private void initSubject(ManagedConnectionFactory var1, boolean var2, AuthenticatedSubject var3) throws SecurityException {
      Vector var4 = this.getCredentials(var2, var3);
      if (var4 != null && var4.size() > 0) {
         this.rpSubject = new Subject();
         Object var5 = null;

         for(int var6 = 0; var6 < var4.size(); ++var6) {
            var5 = var4.get(var6);
            final ResourcePrincipal var8;
            if (var5 instanceof PasswordCredential) {
               final PasswordCredential var7 = (PasswordCredential)var5;
               var7.setManagedConnectionFactory(var1);
               var8 = new ResourcePrincipal(var7.getUserName(), new String(var7.getPassword()));
               if (Debug.isSecurityCtxEnabled()) {
                  this.debug("Adding resource principal Username: " + var7.getUserName());
               }

               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     SecurityContext.this.rpSubject.getPrincipals().add(var8);
                     SecurityContext.this.rpSubject.getPrivateCredentials().add(var7);
                     return null;
                  }
               });
            } else if (var5 instanceof GenericCredential) {
               final GenericCredential var11 = (GenericCredential)var5;
               var8 = new ResourcePrincipal(var11.getName(), "");
               if (Debug.isSecurityCtxEnabled()) {
                  this.debug("Adding resource principal Username: " + var11.getName());
               }

               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     SecurityContext.this.rpSubject.getPrincipals().add(var8);
                     SecurityContext.this.rpSubject.getPrivateCredentials().add(var11);
                     return null;
                  }
               });
            } else if (var5 instanceof GSSCredential) {
               final GSSCredential var12 = (GSSCredential)var5;

               String var13;
               try {
                  var13 = var12.getName().toString();
               } catch (GSSException var10) {
                  throw new SecurityException(var10);
               }

               final ResourcePrincipal var9 = new ResourcePrincipal(var13, "");
               if (Debug.isSecurityCtxEnabled()) {
                  this.debug("Adding resource principal Username: " + var13);
               }

               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     SecurityContext.this.rpSubject.getPrincipals().add(var9);
                     SecurityContext.this.rpSubject.getPrivateCredentials().add(var12);
                     return null;
                  }
               });
            } else if (Debug.isSecurityCtxEnabled()) {
               this.debug("An unsupported credential type was encountered and will be ignored:  " + var5.getClass().getName());
            }
         }

         this.setSubjectReadOnly(this.rpSubject);
      } else if (this.isContainerManaged && Debug.isSecurityCtxEnabled()) {
         Debug.logNoResourcePrincipalFound();
      }

   }

   private void logUsingAppManagedSecurity() {
      if (Debug.isSecurityCtxEnabled()) {
         Debug.securityCtx(" For pool '" + this.poolName + " ': establishing Security Context for Application Managed client");
         if (this.clientInfo == null) {
            Debug.logNoConnectionRequestInfo();
         }
      }

   }

   private void checkResourceReference() throws NamingException {
      Object var1 = null;
      String var2 = "java:/comp/env/wls-connector-resref";
      Context var3 = getInitialContext();

      try {
         var1 = var3.lookup(var2);
      } catch (NameNotFoundException var5) {
         if (Debug.isSecurityCtxEnabled()) {
            Debug.securityCtx("For pool '" + this.poolName + "' SecurityContext.checkResourceReference() couldn't find " + var2 + " for calling component");
         }

         return;
      }

      if (var1 == null && Debug.isSecurityCtxEnabled()) {
         Debug.securityCtx("For pool '" + this.poolName + "' SecurityContext.checkResourceReference() returned null entry for " + var2 + " of calling component");
      } else if (var1 != null) {
         this.processResourceReference((Context)var1);
      }

   }

   private boolean processResourceReference(Context var1) throws NamingException {
      boolean var2 = false;
      NamingEnumeration var3 = var1.list("");

      while(var3.hasMore() && !var2) {
         Object var4 = var3.next();
         NameClassPair var5 = (NameClassPair)var4;
         if (var5.getClassName().endsWith("NamingNode")) {
            var2 = this.processResourceReference((Context)var1.lookup(var5.getName()));
            if (var2) {
               break;
            }
         } else if (var5.getName().endsWith("JNDI")) {
            String var6 = var5.getName();
            if (Debug.isSecurityCtxEnabled()) {
               this.debug("Found JNDI entry \"" + var6 + "\" in wls-connector-resref context -- looking it up...");
            }

            Object var7 = var1.lookup(var6);
            String var8 = var7.toString();
            if (Debug.isSecurityCtxEnabled()) {
               this.debug("Lookup of \"" + var6 + "\" yields: \"" + var8 + "\", comparing with \"" + this.outboundInfo.getJndiName() + "\"");
            }

            if (var8 != null && var8.equalsIgnoreCase(this.outboundInfo.getJndiName())) {
               if (Debug.isSecurityCtxEnabled()) {
                  this.debug("Found matching entry with jndiName: " + var8);
               }

               String var9 = this.lookupResAttr(var6, var1, "Auth");
               if (var9 != null) {
                  this.isContainerManaged = !var9.equalsIgnoreCase("Application");
                  if (Debug.isSecurityCtxEnabled()) {
                     Debug.logRequestedSecurityType(var8, var9);
                  }
               }

               String var10 = this.lookupResAttr(var6, var1, "SharingScope");
               String var12;
               String var13;
               if (var10 != null) {
                  boolean var11 = var10.equalsIgnoreCase("Shareable");
                  if (var11 && this.alwaysUnshareable) {
                     var12 = var1.getNameInNamespace() + "/" + var6;
                     var13 = getResRefName(var12) == null ? "UNKNOWN" : getResRefName(var12);
                     String var14 = getCallerName(var12) == null ? "UNKNOWN" : getCallerName(var12);
                     ConnectorLogger.logShareableRefToUnshareableMCF(var8, var13, var14);
                     var11 = false;
                     var10 = "Unshareable";
                  }

                  this.setShareable(var11);
                  if (Debug.isSecurityCtxEnabled()) {
                     Debug.logRequestedSharingScope(var8, var10);
                  }
               } else if (this.alwaysUnshareable) {
                  String var15 = var1.getNameInNamespace() + "/" + var6;
                  var12 = getResRefName(var15) == null ? "UNKNOWN" : getResRefName(var15);
                  var13 = getCallerName(var15) == null ? "UNKNOWN" : getCallerName(var15);
                  ConnectorLogger.logUnknownShareableRefToUnshareableMCF(var8, var12, var13);
                  this.setShareable(false);
               } else {
                  this.setShareable(true);
               }

               var2 = true;
               break;
            }

            if (Debug.isSecurityCtxEnabled()) {
               this.debug("Skipping non-matching JNDIName");
            }
         } else if (Debug.isSecurityCtxEnabled()) {
            this.debug("Skipping non-JNDI Entry in context");
         }
      }

      return var2;
   }

   public static String getCallerName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1;
         int var2;
         if (var0.indexOf("/webapp/") >= 0) {
            var1 = var0.substring(var0.indexOf("/webapp/") + 8);
            var2 = var1.indexOf("/");
            return var2 > 0 ? var1.substring(0, var2) : null;
         } else if (var0.indexOf("/ejb/") >= 0) {
            var1 = var0.substring(var0.indexOf("/ejb/") + 5);
            var2 = var1.indexOf("/");
            return var2 > 0 ? var1.substring(0, var2) : null;
         } else {
            return null;
         }
      }
   }

   public static String getResRefName(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.indexOf("/wls-connector-resref/") >= 0) {
         String var1 = var0.substring(var0.indexOf("/wls-connector-resref/") + "/wls-connector-resref/".length());
         return var1.endsWith("JNDI") ? var1.substring(0, var1.lastIndexOf("JNDI")) : var1;
      } else {
         return null;
      }
   }

   private String lookupResAttr(String var1, Context var2, String var3) {
      Object var4 = null;
      String var5 = null;
      String var6 = var1.substring(0, var1.length() - 4) + var3;
      if (Debug.isSecurityCtxEnabled()) {
         this.debug("Now looking up: \"" + var6 + "\" ...");
      }

      try {
         var4 = var2.lookup(var6);
      } catch (NamingException var8) {
      }

      if (var4 != null) {
         var5 = var4.toString();
      }

      return var5;
   }

   private void debug(String var1) {
      Debug.securityCtx("For pool '" + this.poolName + "' " + var1);
   }

   private void setShareable(boolean var1) {
      if (Debug.isSecurityCtxEnabled()) {
         Debug.println(this, ".setShareable() setting shareable to " + var1);
      }

      this.shareable = var1;
   }

   private void setSubjectReadOnly(final Subject var1) {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            try {
               var1.setReadOnly();
            } catch (java.lang.SecurityException var2) {
               SecurityContext.this.debug("WARNING:  Failed to modify Subject to be read-only:  " + var2);
            }

            return null;
         }
      });
   }

   public static EISResource getGlobalEISResource(String var0, String var1, RAInfo var2) {
      String var3 = "";
      if (var0 == null || var0.length() == 0) {
         var0 = "WEBLOGIC_SHAREDAPP";
      }

      if (var2 != null && var2.getEisType() != null) {
         var3 = var2.getEisType();
      }

      return new EISResource(var0, var1, var3);
   }

   public static EISResource getPoolEISResource(String var0, String var1, OutboundInfo var2) {
      String var3 = "";
      String var4 = "";
      if (var0 == null || var0.length() == 0) {
         var0 = "WEBLOGIC_SHAREDAPP";
      }

      if (var2 != null && var2.getEisType() != null) {
         var3 = var2.getEisType();
      }

      if (var2 != null && var2.getKey() != null) {
         var4 = var2.getKey();
      }

      return new EISResource(var0, var1, var3, var4);
   }

   public boolean isAccessAllowed() {
      boolean var1 = this.am.isAccessAllowed(this.currentSubject, this.globalEISRes, (ContextHandler)null);
      if (!var1) {
         Debug.logAccessDeniedWarning(this.poolName, ApplicationVersionUtils.getDisplayName(this.globalEISRes.getApplicationName()), this.globalEISRes.getModuleName(), this.globalEISRes.getEISName());
      }

      return var1;
   }

   public boolean isContainerManaged() {
      return this.isContainerManaged;
   }

   public boolean isEmptyContext() {
      return this.rpSubject == null && this.clientInfo == null;
   }

   public ConnectionRequestInfo getClientInfo() {
      return this.clientInfo;
   }

   public Subject getSubject() {
      return this.rpSubject;
   }

   public boolean isShareable() {
      if (Debug.isSecurityCtxEnabled()) {
         Debug.println(this, ".isShareable() = " + this.shareable);
      }

      return this.shareable;
   }

   private Vector getInitialCredentials(AuthenticatedSubject var1) {
      if (Debug.isSecurityCtxEnabled()) {
         this.debug("Looking up credentials for initial connections");
      }

      Vector var2 = this.getCredentials("weblogic_ra_initial", var1);
      if (var2 != null && var2.size() != 0) {
         if (Debug.isSecurityCtxEnabled()) {
            this.debug("Using provided credentials for initial connections.");
         }
      } else {
         if (Debug.isSecurityCtxEnabled()) {
            this.debug("No credentials explicitly provided for initial connections.  Will attempt to find default credentials.");
         }

         var2 = this.getDefaultCredentials(var1);
      }

      return var2;
   }

   private Vector getAnonymousCredentials(AuthenticatedSubject var1) {
      if (Debug.isSecurityCtxEnabled()) {
         this.debug("No authenticated user, so looking up anonymous credentials");
      }

      Vector var2 = this.getCredentials("weblogic_ra_anonymous", var1);
      if (var2 != null && var2.size() != 0) {
         if (Debug.isSecurityCtxEnabled()) {
            this.debug("Using provided credentials for anonymous users");
         }
      } else if (Debug.isSecurityCtxEnabled()) {
         this.debug("No credentials provided for anonymous users.  Will try to find default credentials.");
      }

      return var2;
   }

   private Vector getDefaultCredentials(AuthenticatedSubject var1) {
      if (Debug.isSecurityCtxEnabled()) {
         this.debug("Looking up default credentials");
      }

      Vector var2 = this.getCredentials("weblogic_ra_default", var1);
      if (Debug.isSecurityCtxEnabled()) {
         if (var2 != null && var2.size() != 0) {
            this.debug("Using provided default credentials");
         } else {
            this.debug("No default credentials are provided");
         }
      }

      return var2;
   }

   private Vector getNonInitialCredentials(AuthenticatedSubject var1) {
      Vector var2 = null;

      try {
         this.checkResourceReference();
      } catch (NamingException var4) {
         Debug.logContextProcessingError(var4);
      }

      if (this.outboundInfo.getResAuth() != null) {
         this.isContainerManaged = this.outboundInfo.getResAuth().equalsIgnoreCase("Container");
      }

      if (this.isContainerManaged) {
         if (this.currentSubject != null && this.currentSubject.getPrincipals() != null && this.currentSubject.getPrincipals().size() != 0) {
            var2 = this.getCredentials(this.currentSubject, var1);
         } else {
            var2 = this.getAnonymousCredentials(var1);
         }

         if (var2 == null || var2.size() == 0) {
            var2 = this.getDefaultCredentials(var1);
         }
      } else {
         var2 = null;
         this.logUsingAppManagedSecurity();
      }

      return var2;
   }

   private Vector getCredentials(boolean var1, AuthenticatedSubject var2) {
      Vector var3;
      if (var1) {
         var3 = this.getInitialCredentials(var2);
      } else {
         var3 = this.getNonInitialCredentials(var2);
      }

      return var3;
   }

   private Vector getCredentials(String var1, AuthenticatedSubject var2) {
      return this.getTheCredentials(var1, var2);
   }

   private Vector getCredentials(AuthenticatedSubject var1, AuthenticatedSubject var2) {
      return this.getTheCredentials(var1, var2);
   }

   private Vector getTheCredentials(Object var1, AuthenticatedSubject var2) {
      Vector var3 = null;
      if (var1 == null) {
         return null;
      } else if (this.getCredentialTypes() == null) {
         if (Debug.isSecurityCtxEnabled()) {
            this.debug("No credential types have been specified. Therefore no credentials can be attempted to be found.");
         }

         return null;
      } else {
         CredentialManager var4 = (CredentialManager)SecurityServiceManager.getSecurityService(var2, SecurityServiceManager.getDefaultRealmName(), ServiceType.CREDENTIALMANAGER);
         if (var4 != null) {
            if (Debug.isSecurityCtxEnabled()) {
               this.debug("Looking up credentials for initiating principal:  " + this.getUserName(var1));
            }

            if (var1 instanceof String) {
               var3 = var4.getCredentials(var2, (String)var1, this.poolEISRes, this.getCredentialTypes());
               if (var3 == null || var3.size() == 0) {
                  if (Debug.isSecurityCtxEnabled()) {
                     this.debug("Matching credentials not found for the pool, checking global mappings");
                  }

                  var3 = var4.getCredentials(var2, (String)var1, this.globalEISRes, this.getCredentialTypes());
               }
            } else if (var1 instanceof AuthenticatedSubject) {
               var3 = var4.getCredentials(var2, (AuthenticatedSubject)var1, this.poolEISRes, this.getCredentialTypes());
               if (var3 == null || var3.size() == 0) {
                  if (Debug.isSecurityCtxEnabled()) {
                     this.debug("Matching credentials not found for the pool, checking global mappings");
                  }

                  var3 = var4.getCredentials(var2, (AuthenticatedSubject)var1, this.globalEISRes, this.getCredentialTypes());
               }
            }

            if (Debug.isSecurityCtxEnabled()) {
               if (var3 != null && var3.size() != 0) {
                  this.debug("Using provided credentials for initiating principal:  " + this.getUserName(var1));
               } else {
                  this.debug("No credentials explicitly provided for initiating principal: " + this.getUserName(var1) + ".  Will attempt to find default.");
               }
            }
         } else if (Debug.isSecurityCtxEnabled()) {
            this.debug("No Credential Manager configured.  Server will not be able to provide any credentials.");
         }

         return var3;
      }
   }

   private String getUserName(Object var1) {
      if (var1 instanceof String) {
         return (String)var1;
      } else {
         return var1 instanceof AuthenticatedSubject ? SubjectUtils.getUsername((AuthenticatedSubject)var1) : var1.toString();
      }
   }

   private String[] getCredentialTypes() {
      List var1 = this.outboundInfo.getAuthenticationMechanisms();
      if (var1 != null && var1.size() != 0) {
         String[] var2 = new String[var1.size()];
         Iterator var3 = var1.iterator();

         for(int var5 = 0; var3.hasNext(); ++var5) {
            AuthMechInfo var4 = (AuthMechInfo)var3.next();
            var2[var5] = var4.getType();
            if (var2[var5].equalsIgnoreCase("BasicPassword")) {
               var2[var5] = "weblogic.UserPassword";
            }
         }

         return var2;
      } else {
         if (Debug.isSecurityCtxEnabled()) {
            this.debug("No authentication mechanisms were specified. Therefore no credential types can be attempted to be found.");
         }

         return null;
      }
   }

   private static Context getInitialContext() throws NamingException {
      if (initialContext == null) {
         initialContext = new InitialContext();
      }

      return initialContext;
   }
}
