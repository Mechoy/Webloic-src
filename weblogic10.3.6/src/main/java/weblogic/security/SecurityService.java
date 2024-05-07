package weblogic.security;

import com.bea.common.security.jdkutils.X509CertificateFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import weblogic.common.T3User;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.CachingRealm;
import weblogic.security.acl.CertAuthentication;
import weblogic.security.acl.ListableRealm;
import weblogic.security.acl.Realm;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.ClusterRealm;
import weblogic.security.acl.internal.FileRealm;
import weblogic.security.audit.Audit;
import weblogic.security.audit.AuditProvider;
import weblogic.security.internal.SecurityConfigurationValidator;
import weblogic.security.net.ConnectionFilter;
import weblogic.security.net.ConnectionFilterRulesListener;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityServiceRuntimeException;
import weblogic.security.shared.LoggerWrapper;
import weblogic.security.utils.CertPathTrustManagerUtils;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.NestedRuntimeException;

public class SecurityService extends AbstractServerService implements PropertyChangeListener {
   private BasicRealm oldSecRealm = null;
   private AuditProvider oldAudit = null;
   private static SecurityService singleton = null;
   private SecurityMBean oldMbean = null;
   private SecurityConfigurationMBean newMbean = null;
   private RuntimeMBean runtime;
   private SecurityServiceManager securityServiceManager = null;
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityService");
   private static final String DEFAULT_REALM = "weblogic.security.acl.internal.FileRealm";
   private static final String WLREALMNAME = "weblogic";
   private static boolean enableConnectionFilter = false;
   private static boolean enableConnectionLogger = false;
   private static boolean enableCompatibilityFilters = false;
   private static Object filterObject;
   private static String filterClass;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private SecurityConfigurationValidator securityConfigurationValidator = null;

   public SecurityService() {
      if (singleton != null) {
         throw new InternalError(SecurityLogger.getSecurityAlreadyConfigured());
      } else {
         singleton = this;
         this.securityConfigurationValidator = SecurityConfigurationValidator.getInstance();
      }
   }

   public static SecurityService getSecurityService() {
      return singleton;
   }

   public void start() throws ServiceFailureException {
      try {
         X509CertificateFactory.register();
         this.initializeMBean();
         this.initializeConnectionFilter();
         SecurityServiceManager var1 = new SecurityServiceManager(kernelId);
         var1.initialize(kernelId);
         this.securityServiceManager = var1;
         this.initializeRuntimeMBeans();
         this.securityConfigurationValidator.start();
      } catch (SecurityServiceRuntimeException var2) {
         throw new ServiceFailureException(var2);
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         SecurityLogger.logStackTrace(var4);
         throw new ServiceFailureException(var4);
      }
   }

   private T3User getAdminUser() {
      String var1 = ManagementService.getPropertyService(kernelId).getTimestamp1();
      String var2 = ManagementService.getPropertyService(kernelId).getTimestamp2();
      return new T3User(var1, var2);
   }

   public void stop() throws ServiceFailureException {
      this.securityConfigurationValidator.stop();
      CertPathTrustManagerUtils.stop();
   }

   public void halt() throws ServiceFailureException {
      this.securityConfigurationValidator.halt();
      CertPathTrustManagerUtils.halt();
   }

   private void initializeMBean() {
      this.newMbean = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurityConfiguration();
      if (this.newMbean.getSalt() == null) {
         throw new ConfigurationError(SecurityLogger.getSaltNotSet());
      } else {
         this.newMbean.addPropertyChangeListener(this);
         this.oldMbean = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity();
      }
   }

   public static final boolean getConnectionFilterEnabled() {
      return enableConnectionFilter;
   }

   public static final void setConnectionFilter(ConnectionFilter var0) {
      filterObject = var0;
   }

   public static final ConnectionFilter getConnectionFilter() {
      return (ConnectionFilter)filterObject;
   }

   public static final boolean getConnectionLoggerEnabled() {
      return enableConnectionLogger;
   }

   public static final boolean getCompatibilityConnectionFiltersEnabled() {
      return enableCompatibilityFilters;
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if (var2.equalsIgnoreCase("ConnectionFilterRules") && getConnectionFilterEnabled()) {
         this.setConnectionFilterRules();
      }

      if (var2.equalsIgnoreCase("ConnectionLoggerEnabled")) {
         this.setConnectionLoggerEnabled();
      }

      if (var2.equalsIgnoreCase("CompatibilityConnectionFiltersEnabled")) {
         this.setCompatibilityConnectionFiltersEnabled();
      }

   }

   private void setConnectionLoggerEnabled() {
      enableConnectionLogger = this.newMbean.getConnectionLoggerEnabled();
   }

   private void setCompatibilityConnectionFiltersEnabled() {
      enableCompatibilityFilters = this.newMbean.getCompatibilityConnectionFiltersEnabled();
   }

   private synchronized void setConnectionFilterRules() {
      String[] var1 = this.newMbean.getConnectionFilterRules();

      try {
         Class var2 = Class.forName(filterClass);
         if (ConnectionFilterRulesListener.class.isAssignableFrom(var2)) {
            try {
               String var3 = "setRules";
               Class[] var9 = new Class[]{String[].class};
               Method var5 = var2.getMethod(var3, var9);
               Object[] var6 = new Object[]{var1};
               var5.invoke(filterObject, var6);
            } catch (InvocationTargetException var7) {
               Throwable var4 = var7.getTargetException();
               if (var4.toString().startsWith("java.text.ParseException")) {
                  SecurityLogger.logBootFilterCritical(var4.getMessage());
               }

               throw var7;
            }
         }

      } catch (Throwable var8) {
         SecurityLogger.logStackTrace(var8);
         throw new NestedRuntimeException(SecurityLogger.getProblemWithConnFilterRules(), var8);
      }
   }

   private void initializeConnectionFilter() {
      filterClass = this.newMbean.getConnectionFilter();
      if (filterClass != null) {
         try {
            filterObject = Class.forName(filterClass).newInstance();
            enableConnectionFilter = true;
            this.setConnectionFilterRules();
         } catch (Exception var2) {
            SecurityLogger.logStackTrace(var2);
            throw new NestedRuntimeException(SecurityLogger.getProblemWithConnFilter(), var2);
         }
      }

      this.setConnectionLoggerEnabled();
      this.setCompatibilityConnectionFiltersEnabled();
   }

   public synchronized void initializeAuditing() {
      if (this.oldAudit == null) {
         String var1 = this.oldMbean.getAuditProviderClassName();
         if (var1 != null) {
            try {
               AuditProvider var2 = (AuditProvider)Class.forName(var1).newInstance();
               Audit.setProvider(var2);
               this.oldAudit = var2;
            } catch (Exception var4) {
               SecurityLogger.logStackTrace(var4);
               String var3 = var4.getMessage();
               log.severe("*** Security audit provider not set correctly [" + var4.getClass().getName() + (var3 != null && var3.length() != 0 ? ": " + var3 : "") + "]");
               throw new SecurityException(SecurityLogger.getMustSetAuditProviderClassName());
            }
         }

      }
   }

   public void initializeClusterRealm(String var1) {
      ClusterRealm.THE_ONE = (ClusterRealm)Realm.getRealm("wl_realm", var1, ClusterRealm.class.getName());
   }

   public synchronized void initializeRealm() {
      if (this.oldSecRealm == null) {
         T3User var1 = this.getAdminUser();
         FileRealm var2 = (FileRealm)Realm.getRealm("weblogic", var1, "weblogic.security.acl.internal.FileRealm");
         Object var3 = var2;
         if (this.oldMbean.getRealm().getCachingRealm() != null) {
            String var4 = this.oldMbean.getRealm().getCachingRealm().getBasicRealm().getRealmClassName();
            if (var4 == null || var4.length() == 0) {
               String var7 = this.oldMbean.getRealm().getCachingRealm().getBasicRealm().getName();
               throw new SecurityException(SecurityLogger.getMustSetRealmClassName(var7));
            }

            BasicRealm var5 = Realm.getRealm("custom", var1, var4);
            CachingRealm var6 = new CachingRealm((ListableRealm)var5, var2, var1);
            var6.masqueradeAs("weblogic");
            weblogic.security.acl.Security.init(var6);
            var3 = var6;
         } else {
            weblogic.security.acl.Security.init(var2);
         }

         var2.loadMembers();
         var2.addRuntimeACLs();
         this.oldSecRealm = (BasicRealm)var3;
      }
   }

   public void initializeCertAuthentication() {
      CertAuthentication.setup();
   }

   private void initializeRuntimeMBeans() {
      try {
         new SecurityRuntime(this.newMbean, this.oldMbean);
         new SingleSignOnServicesRuntime();
      } catch (ManagementException var2) {
         SecurityLogger.logErrorCreatingSecurityRuntime(var2);
      }

   }
}
