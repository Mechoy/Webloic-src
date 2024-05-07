package weblogic.management.provider;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import weblogic.management.DomainDir;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.SecurityHelper;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.URLManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.ServerAuthenticate;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.servlet.internal.ProtocolHandlerHTTP;
import weblogic.utils.net.InetAddressHelper;

public class PropertyService extends AbstractServerService {
   private static PropertyService singleton;
   public static final String ADMIN_ANONYMOUSADMINLOOKUPENABLED_PROP = "weblogic.management.anonymousAdminLookupEnabled";
   public static final String ADMIN_CLEAR_TEXT_CREDENTIAL_ACCESS_ENABLED = "weblogic.management.clearTextCredentialAccessEnabled";
   public static final String ADMIN_IACACHETTL_PROP = "weblogic.security.identityAssertionTTL";
   public static final String ADMIN_SSLENFORCECONSTRAINT_PROP = "weblogic.security.SSL.enforceConstraints";
   public static final String ADMIN_SSLTRUSTCA_PROP = "weblogic.security.SSL.trustedCAKeyStore";
   public static final String ADMIN_SSL_MINIMUM_PROTOCOL_VERSION_PROP = "weblogic.security.SSL.minimumProtocolVersion";
   public static final String ADMIN_SSLVERSION_PROP = "weblogic.security.SSL.protocolVersion";
   public static final String SECURITY_FW_DELEGATE_CLASS_NAME_PROP = "weblogic.security.SecurityServiceManagerDelegate";
   public static final String SECURITY_FW_SUBJECT_MANAGER_CLASS_NAME_PROP = "weblogic.security.SubjectManager";
   public static final String ADMIN_HOST_PROP = "weblogic.management.server";
   public static final String ADMIN_USERNAME_PROP = "weblogic.management.username";
   public static final String ADMIN_PASSWORD_PROP = "weblogic.management.password";
   public static final String LEGAL_BYPASS_ON_PARSING_PROP = "weblogic.mbeanLegalClause.ByPass";
   public static final String MBEAN_AUDITING_ENABLED_PROP = "weblogic.AdministrationMBeanAuditingEnabled";
   public static final String OLD_ADMIN_HOST_PROP = "weblogic.admin.host";
   public static final String ADMIN_PKPASSWORD_PROP = "weblogic.management.pkpassword";
   public static final String ADMIN_HIERARCHY_GROUP_PROP = "weblogic.security.hierarchyGroupMemberships";
   public static final String LDAP_DELEGATE_POOL_SIZE_PROP = "weblogic.security.providers.authentication.LDAPDelegatePoolSize";
   public static final String ADMIN_AUDITLOG_DIR = "weblogic.security.audit.auditLogDir";
   public static final String JMX_REMOTE_REQUEST_TIMEOUT = "weblogic.management.jmx.remote.requestTimeout";
   public static final String CONVERT_SECURITY_EXTENSION_SCHEMA = "weblogic.management.convertSecurityExtensionSchema";
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static String adminHost = null;
   private String serverName = "myserver";
   boolean serverNameIsSet = false;
   private static boolean isChannelServiceReady = false;
   private String userName = null;
   private String password = null;
   private String pkpassword = null;
   private URL adminURL;
   private String adminBinaryURL = null;
   private boolean isAdminServer = false;
   private boolean securityInitialized = false;

   private void checkSecurityInitialized() {
      if (!this.securityInitialized) {
         if (!this.serverNameIsSet) {
            throw new AssertionError("Security required before it is initialized");
         }

         this.initializeSecurityProperties(false);
      }

   }

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }

   public void start() throws ServiceFailureException {
      this.initializeServerName();
      this.initializeAdminHost();
      if (!this.serverNameIsSet && !this.isAdminServer()) {
         this.serverName = "myserver";
         this.serverNameIsSet = true;
      }

   }

   static PropertyService getPropertyService(AuthenticatedSubject var0) {
      SecurityHelper.assertIfNotKernel(var0);
      return singleton;
   }

   public PropertyService() {
      if (singleton != null) {
         throw new AssertionError("PropertyService already initialized");
      } else {
         singleton = this;
      }
   }

   public final String getTimestamp1() {
      this.checkSecurityInitialized();
      return this.userName;
   }

   public final String getTimestamp2() {
      this.checkSecurityInitialized();
      return this.password;
   }

   public final String getTimestamp3() {
      this.checkSecurityInitialized();
      return this.pkpassword;
   }

   public void updateTimestamp5(String var1) {
      this.password = var1;
   }

   public void updateTimestamp6(String var1) {
      this.userName = var1;
   }

   public void updateTimestamp3() {
      this.pkpassword = null;
   }

   public void initializeSecurityProperties(boolean var1) {
      if (!this.securityInitialized) {
         String[] var2 = null;
         if (var1) {
            var2 = new String[]{"domainCreation"};
         }

         ServerAuthenticate.main(var2);
         this.userName = System.getProperty("weblogic.management.username", "guest");
         this.password = System.getProperty("weblogic.management.password", "guest");
         this.pkpassword = System.getProperty("weblogic.management.pkpassword");
         Properties var3 = System.getProperties();
         var3.remove("weblogic.management.username");
         var3.remove("weblogic.management.password");
         var3.remove("weblogic.management.pkpassword");
         this.securityInitialized = true;
      }
   }

   public void establishServerBootIdentity(StartupConfig var1) {
      this.checkSecurityInitialized();
      ClearOrEncryptedService var2 = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());
      if (this.userName != null && this.userName.length() != 0) {
         var1.setUsername(var2.encrypt(this.userName));
      }

      if (this.password != null && this.password.length() != 0) {
         var1.setPassword(var2.encrypt(this.password));
      }

   }

   public synchronized void setAdminHost(String var1) throws MalformedURLException {
      int var2 = var1.indexOf("://");
      if (var2 != -1) {
         adminHost = var1.substring(0, var2).toLowerCase(Locale.US) + var1.substring(var2);
      } else {
         adminHost = var1;
      }

      this.adminBinaryURL = null;
      this.adminURL = new URL(getAdminHttpUrl());
      this.isAdminServer = adminHost == null;
   }

   public synchronized String getAdminHost() {
      return adminHost;
   }

   public synchronized void setChannelServiceReady() {
      isChannelServiceReady = true;
      this.notifyAll();
   }

   public synchronized void waitForChannelServiceReady() {
      if (!isChannelServiceReady) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }

      }
   }

   public static final synchronized String getAdminHttpUrl() {
      String var0 = null;

      try {
         if (isChannelServiceReady) {
            String var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getAdminServerName();
            Protocol var2 = null;
            Protocol var3 = null;
            Protocol var4 = null;
            Iterator var5 = ProtocolManager.iterator();

            while(var5.hasNext()) {
               Protocol var6 = (Protocol)var5.next();
               switch (var6.toByte()) {
                  case 1:
                     var3 = var6;
                     break;
                  case 3:
                     var4 = var6;
                     break;
                  case 6:
                     var2 = var6;
               }
            }

            String var10 = null;
            String var7 = null;
            String var8 = null;
            if (var2 != null) {
               var10 = URLManager.findURL(var1, var2);
            }

            if (var3 != null) {
               var7 = URLManager.findURL(var1, var3);
            }

            if (var4 != null) {
               var8 = URLManager.findURL(var1, var4);
            }

            var0 = URLManager.findAdministrationURL(var1);
            if (var10 != null) {
               var0 = var10;
            } else if (var7 != null) {
               var0 = var7;
            } else if (var8 != null) {
               var0 = var8;
            }
         }
      } catch (UnknownHostException var9) {
      }

      if (var0 == null) {
         var0 = getPropertyService(KERNEL_ID).getAdminHost();
      }

      if (var0 == null && getPropertyService(KERNEL_ID).isAdminServer()) {
         var0 = ChannelHelper.getURL(ProtocolHandlerAdmin.PROTOCOL_ADMIN);
         if (var0 == null) {
            var0 = ChannelHelper.getURL(ProtocolHandlerHTTP.PROTOCOL_HTTP);
         }

         if (var0 == null) {
            throw new AssertionError("Can not extract host name of the adminstration server from JVMID");
         }
      }

      if (var0 == null) {
         throw new AssertionError("Could not determine admin url");
      } else {
         var0 = InetAddressHelper.convertIfIPV6URL(var0);
         return URLManager.normalizeToHttpProtocol(var0);
      }
   }

   public static final synchronized String[] getAllAdminHttpUrls() {
      String var0 = getAdminHttpUrl();

      try {
         URL var1 = new URL(var0);
         String var2 = var1.getProtocol();
         String var3 = var1.getHost();
         int var4 = var1.getPort();
         InetAddress[] var5 = InetAddress.getAllByName(var3);
         if (var5 != null && var5.length != 0) {
            String[] var6 = new String[var5.length];

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var6[var7] = (new URL(var2, var5[var7].getHostAddress(), var4, "")).toString();
            }

            return var6;
         } else {
            return stringToArray(var0);
         }
      } catch (MalformedURLException var8) {
         return stringToArray(var0);
      } catch (java.net.UnknownHostException var9) {
         return stringToArray(var0);
      }
   }

   private static String[] stringToArray(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String[] var1 = new String[]{var0};
         return var1;
      }
   }

   public String getAdminBinaryURL() {
      if (this.adminBinaryURL != null) {
         return this.adminBinaryURL;
      } else {
         this.adminBinaryURL = adminHost;
         if (this.adminBinaryURL == null) {
            return null;
         } else {
            this.adminBinaryURL = URLManager.normalizeToAdminProtocol(this.adminBinaryURL);
            return this.adminBinaryURL;
         }
      }
   }

   private String initializeAdminHost() {
      if (adminHost != null) {
         return adminHost;
      } else {
         String var1 = System.getProperty("weblogic.management.server");
         if (var1 == null) {
            var1 = System.getProperty("weblogic.admin.host");
         }

         if (var1 != null) {
            try {
               this.setAdminHost(var1);
            } catch (MalformedURLException var3) {
            }
         } else {
            this.isAdminServer = true;
         }

         return adminHost;
      }
   }

   private void initializeServerName() {
      String var1 = BootStrap.getServerName();
      if (var1 != null) {
         this.serverName = var1;
         this.serverNameIsSet = true;
      } else {
         String var2 = BootStrap.getConfigFileName();
         File var3 = new File(DomainDir.getConfigDir(), BootStrap.getDefaultConfigFileName());
         File var4 = new File(DomainDir.getRootDir(), var2);
         if (!var4.exists() && !var3.exists()) {
            this.serverName = "myserver";
            this.serverNameIsSet = true;
         }
      }

   }

   public URL getAdminURL() {
      return this.adminURL;
   }

   public final String getServerName() {
      if (!this.serverNameIsSet) {
         throw new AssertionError("Server has not yet been established.");
      } else {
         return this.serverName;
      }
   }

   public boolean isAdminServer() {
      return this.isAdminServer;
   }

   public void doPostParseInitialization(DomainMBean var1) {
      if (!this.serverNameIsSet && this.isAdminServer()) {
         ServerMBean[] var2 = var1.getServers();
         if (var2 != null && var2.length == 1) {
            this.serverName = var2[0].getName();
            this.serverNameIsSet = true;
         }

         if (!this.serverNameIsSet) {
            String var3 = var1.getAdminServerName();
            if (var3 != null && var3.length() >= 0) {
               this.serverName = var3;
               this.serverNameIsSet = true;
            }
         }
      }

      if (!this.serverNameIsSet) {
         this.serverName = "myserver";
         this.serverNameIsSet = true;
      }

   }

   public boolean serverNameIsSet() {
      return this.serverNameIsSet;
   }

   public static void main(String[] var0) throws Exception {
      PropertyService var1 = new PropertyService();
      var1.setAdminHost(var0[0]);
      String[] var2 = getAllAdminHttpUrls();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         System.out.println(var2[var3]);
      }

   }
}
