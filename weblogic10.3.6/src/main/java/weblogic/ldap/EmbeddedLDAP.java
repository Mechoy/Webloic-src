package weblogic.ldap;

import com.octetstring.vde.ExternalExecutor;
import com.octetstring.vde.LDAPServer;
import com.octetstring.vde.WorkQueueItem;
import com.octetstring.vde.acl.ACLChecker;
import com.octetstring.vde.backend.BackendHandler;
import com.octetstring.vde.backend.standard.BackendStandard;
import com.octetstring.vde.replication.Consumer;
import com.octetstring.vde.replication.Replication;
import com.octetstring.vde.schema.InitSchema;
import com.octetstring.vde.syntax.DirectoryString;
import com.octetstring.vde.util.EncryptionHelper;
import com.octetstring.vde.util.ExternalLogger;
import com.octetstring.vde.util.LDIF;
import com.octetstring.vde.util.Logger;
import com.octetstring.vde.util.ServerConfig;
import com.octetstring.vde.util.TimedActivityThread;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.TimeZone;
import javax.mail.internet.MimeUtility;
import weblogic.logging.LogOutputStream;
import weblogic.logging.Loggable;
import weblogic.management.DomainDir;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.EmbeddedLDAPMBean;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.security.RDBMSSecurityStoreMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.management.servlet.FileDistributionServlet;
import weblogic.protocol.AdminServerIdentity;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.ServerIdentityManager;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.providers.authentication.DefaultAuthenticatorMBean;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.net.InetAddressHelper;
import weblogic.work.WorkManagerFactory;

public final class EmbeddedLDAP extends AbstractServerService implements ExternalLogger, ExternalExecutor {
   private static final String EMBEDDED_LDAP_DIR_NAME = "ldap";
   public static final String ROOT_USER_NAME = "cn=Admin";
   private static final String VDE_BACKUP_DIR = "backup";
   private static final String VDE_CONF_DIR = "conf";
   private static final String VDE_LOG_DIR = "log";
   private static final String VDE_DATA_DIR = "ldapfiles";
   private static final String VDE_REPLICADATA_DIR = "replicadata";
   private static final String VDE_PROP_NAME = "vde.prop";
   private static final String VDE_MAPPING_NAME = "mapping.cfg";
   private static final String VDE_REPLICAS_NAME = "replicas.prop";
   private static final String VDE_INVALID_REPLICA_NAME = "replica.invalid";
   private static final String DOMAIN_SCHEMA_NAME = "dc=";
   private static final String VDE_SCHEMA_FILENAME = "schema.core.xml";
   private static final String VDE_BACKENDTYPES_FILENAME = "adaptertypes.prop";
   private static final String VDE_BACKEND_FILENAME = "adapters.prop";
   private static final String VDE_ACL_FILENAME = "acls.prop";
   private static final String VDE_DATA_DIR_LOCK = "EmbeddedLDAP.lok";
   public static final String VDE_PROPS_REPLICA = "replica.";
   public static final String EMBEDDED_LDAP = "EmbeddedLDAP";
   private static final int timerInterval = 60000;
   private static EmbeddedLDAP singleton = null;
   private static final LogOutputStream log = new LogOutputStream("EmbeddedLDAP");
   private static boolean masterLDAPUseSSL = false;
   private static String masterLDAPHost = null;
   private static int masterLDAPPort = 0;
   private static String masterLDAPURL;
   private static boolean isDBMSOnly = false;
   private int state = 1;
   private LDAPServer ldapServer;
   private Logger logger;
   private ServerMBean serverMBean;
   private EmbeddedLDAPMBean embeddedLDAPMBean;
   private ServerDebugMBean debugMBean;
   private int numReplicas = 0;
   private Properties replicaProps = null;
   private Replication replication;
   private EmbeddedLDAPTimedActivity timedActivity = null;
   private boolean invalidReplica = false;
   private boolean debugEnabled = false;
   private boolean masterFirst = false;
   private int timeout = 0;
   private BackendHandler handler;
   private BackendStandard backend = null;
   private static File lockFile = null;
   private FileLock lockFileLock = null;
   private String configDirPath = null;
   private Timer pollerTimer = null;
   private static final AuthenticatedSubject KERNELID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static String[] confFiles = new String[]{"schema.core.xml", "adaptertypes.prop", "adapters.prop", "acls.prop"};

   public EmbeddedLDAP() {
      singleton = this;
   }

   public static EmbeddedLDAP getEmbeddedLDAP() {
      return singleton;
   }

   private static boolean isDBMSStoreOnly(RuntimeAccess var0) {
      AuthenticationProviderMBean[] var1 = var0.getDomain().getSecurityConfiguration().getDefaultRealm().getAuthenticationProviders();
      RDBMSSecurityStoreMBean var2 = var0.getDomain().getSecurityConfiguration().getDefaultRealm().getRDBMSSecurityStore();
      if (var2 == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3] instanceof DefaultAuthenticatorMBean) {
               return false;
            }
         }

         return true;
      }
   }

   public void start() throws ServiceFailureException {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNELID);
      this.serverMBean = var1.getServer();
      isDBMSOnly = isDBMSStoreOnly(var1);
      this.debugMBean = this.serverMBean.getServerDebug();
      this.debugEnabled = this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAP();
      if (this.debugEnabled) {
         log.debug("Initialize service for EmbeddedLDAP");
      }

      this.embeddedLDAPMBean = var1.getDomain().getEmbeddedLDAP();
      if (this.embeddedLDAPMBean != null) {
         String var2 = this.embeddedLDAPMBean.getCredential();
         if (var2 != null && var2.length() != 0) {
            String var3 = getEmbeddedLDAPDir();
            boolean var4 = this.validateVDEDirectories(var3);
            String var5 = null;
            if (!var1.isAdminServer() && var1.isAdminServerAvailable()) {
               boolean var6 = var4 || this.invalidReplica || this.embeddedLDAPMBean.isRefreshReplicaAtStartup();
               var5 = this.getInitialReplicaFromAdminServer(var6, !var4);
            }

            this.validateVDEConfigFiles(var3);
            this.ensureExclusiveAccess(var3);
            this.initServerConfig(var3);
            this.logger = Logger.getInstance();
            this.logger.setExternalLogger(this);
            this.ldapServer = new LDAPServer();
            MuxableSocketLDAP.initialize(this.ldapServer);
            MuxableSocketLDAPS.initialize(this.ldapServer);
            this.logger.log(5, this.ldapServer, "VDE Engine Starting");
            (new InitSchema()).init();
            ACLChecker.getInstance().initialize();
            Properties var27 = new Properties();
            String var7 = "dc=" + var1.getDomain().getName();
            var27.setProperty("backend.0.root", var7);
            var27.setProperty("backend.0.config.backup-hour", Integer.toString(this.embeddedLDAPMBean.getBackupHour()));
            var27.setProperty("backend.0.config.backup-minute", Integer.toString(this.embeddedLDAPMBean.getBackupMinute()));
            var27.setProperty("backend.0.config.backup-max", Integer.toString(this.embeddedLDAPMBean.getBackupCopies()));
            if (this.debugEnabled) {
               debugLogProperties("VDE Backend properties: ", var27);
            }

            if (this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAPWriteOverrideProps()) {
               debugWriteProperties("./lib/adaptertypes.prop", var27);
            }

            this.handler = BackendHandler.getInstance(var27);

            try {
               this.backend = (BackendStandard)this.handler.getBackend(new DirectoryString(var7));
            } catch (Exception var26) {
               if (!var1.isAdminServer()) {
                  this.setReplicaInvalid();
                  EmbeddedLDAPLogger.logErrorInitializingLDAPReplica(var26);
               } else {
                  EmbeddedLDAPLogger.logErrorInitializingLDAPMaster(var3 + File.separator + "backup", var26);
               }

               throw new ServiceFailureException("Error initializing Embedded LDAP Server", var26);
            }

            try {
               this.timedActivity = new EmbeddedLDAPTimedActivity(this.backend);
               this.pollerTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this.timedActivity, 60000L);
            } catch (Exception var25) {
               EmbeddedLDAPLogger.logCouldNotScheduleTrigger(var25.toString());
            }

            if (var5 != null) {
               this.loadInitialReplicaFile(var5);
            }

            URI var8 = this.getMasterEmbeddedLDAPURI();
            if (var8 != null) {
               masterLDAPUseSSL = var8.getScheme().equalsIgnoreCase("ldaps");
               masterLDAPHost = var8.getHost();
               masterLDAPPort = var8.getPort();
               masterLDAPURL = var8.toString();
            }

            this.masterFirst = this.embeddedLDAPMBean.isMasterFirst();
            this.timeout = this.embeddedLDAPMBean.getTimeout();
            this.updateReplicaProperties();
            if (!var1.isAdminServer() || this.numReplicas > 0) {
               this.initReplication();
            }

            if (this.debugEnabled) {
               log.debug("Start service for EmbeddedLDAP");
            }

            this.ldapServer.start();
            this.state = 2;
            if (isDBMSOnly && var4 && var1.isAdminServer()) {
               String var9 = var7.substring(3);
               String var10 = "dn: " + var7 + "\n" + "objectclass: top" + "\n" + "objectclass: domain" + "\n" + "dc: " + var9 + "\n\n";
               ByteArrayInputStream var11 = null;

               try {
                  var11 = new ByteArrayInputStream(var10.getBytes());
                  (new LDIF()).importLDIF((String)null, var11, true);
               } catch (Exception var23) {
                  throw new ServiceFailureException("Error loading the min LDAP content forEmbeddedLDAP", var23);
               } finally {
                  try {
                     var11.close();
                  } catch (Exception var22) {
                  }

               }

               if (this.debugEnabled) {
                  log.debug("Populated minimum domain content for EmbeddedLDAP");
               }
            }

         } else {
            throw new ServiceFailureException(EmbeddedLDAPLogger.getCredUnavailable());
         }
      }
   }

   public void stop() {
      LDAPExecuteRequest.waitForRequestsToComplete();
      this.shutdown();
   }

   public void halt() {
      this.shutdown();
   }

   private void shutdown() {
      this.state = 0;
      LDAPExecuteRequest.waitForRequestsToComplete();
      this.debugEnabled = this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAP();
      if (this.debugEnabled) {
         log.debug("Shutdown server for EmbeddedLDAP");
      }

      try {
         if (this.replication != null) {
            this.replication.shutdown();
         }

         if (this.backend != null) {
            this.backend.shutdown();
         }

         try {
            if (this.lockFileLock != null) {
               this.lockFileLock.release();
               this.lockFileLock.channel().close();
            }
         } catch (IOException var2) {
            if (this.debugEnabled) {
               log.debug("Exception releasing lock " + var2);
            }
         }

         lockFile.delete();
         FileUtils.unregisterLockFile(lockFile);
         lockFile = null;
         this.lockFileLock = null;
      } catch (Exception var3) {
         if (this.debugEnabled) {
            log.debug("Exception shutting down " + var3);
         }
      }

   }

   public boolean isRunning() {
      return this.state == 2;
   }

   public synchronized String initReplicaForNewServer(String var1, String var2) {
      RuntimeAccess var3 = ManagementService.getRuntimeAccess(KERNELID);
      if (!var3.isAdminServer()) {
         return null;
      } else {
         this.debugEnabled = this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAP();
         if (this.debugEnabled) {
            log.debug("init Replica for new server " + var1 + ", url " + var2);
         }

         URI var4;
         try {
            var4 = new URI(var2);
         } catch (URISyntaxException var11) {
            throw new EmbeddedLDAPException("Invalid replica url: " + var2);
         }

         ServerMBean var5 = var3.getDomain().lookupServer(var1);
         if (var5 == null) {
            throw new EmbeddedLDAPException("Could not find server for Initial Replica: " + var1);
         } else {
            int var6 = this.getReplicaIndex(var1);
            if (var6 < 0) {
               var6 = this.numReplicas++;
            }

            boolean var7 = var4.getScheme().equalsIgnoreCase("ldaps");
            String var8 = "replica." + var6;
            this.replicaProps.setProperty(var8 + ".name", var1);
            this.replicaProps.setProperty(var8 + ".consumerid", var1);
            this.replicaProps.setProperty(var8 + ".base", "dc=" + var3.getDomainName());
            this.replicaProps.setProperty(var8 + ".masterid", var3.getAdminServerName());
            this.replicaProps.setProperty(var8 + ".masterurl", masterLDAPURL + "/");
            this.replicaProps.setProperty(var8 + ".hostname", var4.getHost());
            this.replicaProps.setProperty(var8 + ".port", Integer.toString(var4.getPort()));
            this.replicaProps.setProperty(var8 + ".secure", var7 ? "1" : "0");
            this.replicaProps.setProperty(var8 + ".binddn", "cn=Admin");
            this.replicaProps.setProperty("replica.num", Integer.toString(this.numReplicas));
            this.writeReplicaProps();
            if (this.replication == null) {
               this.initReplication();
            } else {
               Consumer var9 = this.replication.getReplicaByName(var1);
               if (var9 != null) {
                  var9.setHostname(var4.getHost());
                  var9.setPort(var4.getPort());
                  var9.setSecure(var7);
               } else {
                  Hashtable var10 = new Hashtable();
                  var10.put("name", var1);
                  var10.put("consumerid", var1);
                  var10.put("base", "dc=" + var3.getDomainName());
                  var10.put("masterid", var3.getAdminServerName());
                  var10.put("masterurl", masterLDAPURL + "/");
                  var10.put("hostname", var4.getHost());
                  var10.put("port", Integer.toString(var4.getPort()));
                  var10.put("secure", var7 ? "1" : "0");
                  var10.put("binddn", "cn=Admin");
                  var10.put("bindpw", this.embeddedLDAPMBean.getCredential());
                  if (this.debugEnabled) {
                     log.debug("Adding replica for " + var1);
                  }

                  this.replication.addReplica(var10);
               }
            }

            if (this.debugEnabled) {
               log.debug("Initializing VDE Replica for " + var1);
            }

            String var12 = getEmbeddedLDAPDir() + File.separator + var1 + ".ldif";
            this.replication.setupAgreement(var1, var12);
            return var12;
         }
      }
   }

   public synchronized boolean isValidReplica(String var1, String var2) {
      this.debugEnabled = this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAP();
      if (this.debugEnabled) {
         log.debug("validate replica status for server " + var1 + " url " + var2);
      }

      if (this.replication != null && this.replication.getReplicaByName(var1) != null) {
         int var3 = this.getReplicaIndex(var1);
         if (var3 < 0) {
            return false;
         } else {
            String var4 = "replica." + var3;
            String var5 = this.replicaProps.getProperty(var4 + ".hostname");
            String var6 = this.replicaProps.getProperty(var4 + ".port");

            try {
               URI var7 = new URI(var2);
               return var7.getHost().equals(var5) && Integer.toString(var7.getPort()).equals(var6);
            } catch (URISyntaxException var8) {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private int getReplicaIndex(String var1) {
      for(int var2 = 0; var2 < this.numReplicas; ++var2) {
         if (var1.equals(this.replicaProps.getProperty("replica." + var2 + ".name"))) {
            return var2;
         }
      }

      return -1;
   }

   public EmbeddedLDAPMBean getEmbeddedLDAPMBean() {
      return this.embeddedLDAPMBean;
   }

   public static boolean importLDIF(String var0, String var1, String var2, String var3) {
      System.setProperty("vde.home", var0);
      ServerConfig var4 = ServerConfig.getInstance();

      try {
         var4.init();
      } catch (IOException var7) {
         return false;
      }

      var4.setProperty("vde.server.name", "myserver");
      var4.setProperty("vde.hostname", "localhost");
      var4.setProperty("vde.server.port", "7003");
      var4.setProperty("vde.rootuser", "cn=Admin");
      var4.setProperty("vde.rootpw", "manager");
      var4.setProperty("vde.logconsole", "0");
      var4.setProperty("vde.tls", "0");
      var4.setProperty("vde.changelog", "0");
      var4.setProperty("vde.debug", "0");
      String var5 = BootStrap.getWebLogicHome() + "/lib/";
      var4.setProperty("vde.schema.std", var5 + "schema.core.xml");
      var4.setProperty("vde.backendtypes", var5 + "adaptertypes.prop");
      var4.setProperty("vde.server.backends", var5 + "adapters.prop");
      var4.setProperty("vde.aclfile", var5 + "acls.prop");
      (new InitSchema()).init();
      ACLChecker.getInstance().initialize();
      Properties var6 = new Properties();
      var6.setProperty("backend.0.root", "dc=" + var2);
      var6.setProperty("backend.0.config.backup-hour", "23");
      var6.setProperty("backend.0.config.backup-minute", "59");
      var6.setProperty("backend.0.config.backup-max", "7");
      BackendHandler.getInstance(var6);
      return (new LDIF()).importLDIF(var3, (InputStream)null, true);
   }

   public void log(int var1, String var2, String var3) {
      switch (var1) {
         case 0:
            log.error(var3);
            break;
         case 1:
         case 2:
         case 4:
         case 5:
         case 6:
         case 8:
         case 10:
         default:
            log.info(var3);
            break;
         case 3:
            log.warning(var3);
            break;
         case 7:
         case 9:
         case 11:
            log.debug(var3);
      }

   }

   public void printStackTrace(Throwable var1) {
      log.critical(StackTraceUtils.throwable2StackTrace(var1));
   }

   public void printStackTraceLog(Throwable var1) {
      log.critical(StackTraceUtils.throwable2StackTrace(var1));
   }

   public void printStackTraceConsole(Throwable var1) {
      log.critical(StackTraceUtils.throwable2StackTrace(var1));
   }

   public void execute(WorkQueueItem var1) {
      WorkManagerFactory.getInstance().getSystem().schedule(new LDAPExecuteRequest(var1));
   }

   private static String getEmbeddedLDAPDir() {
      return DomainDir.getLDAPDataDirForServer(ManagementService.getRuntimeAccess(KERNELID).getServerName());
   }

   public static String getEmbeddedLDAPDataDir() {
      return getEmbeddedLDAPDir();
   }

   public static String getEmbeddedLDAPHost() {
      return masterLDAPHost;
   }

   private URI getMasterEmbeddedLDAPURI() {
      String var1 = null;
      if (ManagementService.getRuntimeAccess(KERNELID).isAdminServerAvailable()) {
         var1 = findLdapURL(AdminServerIdentity.getBootstrapIdentity());
      } else {
         String var2 = ManagementService.getPropertyService(KERNELID).getAdminHost();
         var2 = InetAddressHelper.convertIfIPV6URL(var2);
         var1 = URLManager.normalizeToLDAPProtocol(var2);
      }

      if (var1 == null) {
         EmbeddedLDAPLogger.logCouldNotGetAdminListenAddress();
      } else {
         try {
            return new URI(var1);
         } catch (URISyntaxException var3) {
            EmbeddedLDAPLogger.logInvalidAdminListenAddress(var1);
         }
      }

      return null;
   }

   public static int getEmbeddedLDAPPort() {
      return masterLDAPPort;
   }

   public static String getEmbeddedLDAPDomain() {
      return ManagementService.getRuntimeAccess(KERNELID).getDomainName();
   }

   /** @deprecated */
   public static String getEmbeddedLDAPCredential() {
      SecurityServiceManager.checkKernelPermission();
      EmbeddedLDAPMBean var0 = getEmbeddedLDAP().getEmbeddedLDAPMBean();
      return var0.getCredential();
   }

   public static String getEmbeddedLDAPCredential(AuthenticatedSubject var0) {
      SecurityServiceManager.checkKernelIdentity(var0);
      EmbeddedLDAPMBean var1 = getEmbeddedLDAP().getEmbeddedLDAPMBean();
      return var1.getCredential();
   }

   public static boolean getEmbeddedLDAPUseSSL() {
      return masterLDAPUseSSL;
   }

   public void setReplicaInvalid() {
      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(getEmbeddedLDAPDir() + File.separator + "ldapfiles" + File.separator + "replica.invalid", false));
         var1.println("# Replica set invalid");
         var1.close();
      } catch (IOException var2) {
         log.debug("Got I/O error writing invalid replica file", var2);
      }

   }

   public void setPasswords2WayEncrypted() {
      this.backend.setPasswordExternalEncryptionHelper(new EncryptionHelperImpl(new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService())));
   }

   public boolean isDebugEnabled() {
      return this.debugEnabled;
   }

   public boolean isMasterFirst() {
      return this.masterFirst;
   }

   public int getTimeout() {
      return this.timeout;
   }

   public void registerChangeListener(String var1, EmbeddedLDAPChangeListener var2) {
      if (this.handler == null) {
         throw new IllegalStateException("EmbeddedLDAP has not been initialized yet");
      } else {
         EntryChangesListenerImpl var3 = new EntryChangesListenerImpl(var1, var2);
         this.handler.registerEntryChangesListener(var3);
      }
   }

   public static String getDateFormat(long var0) {
      Calendar var2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      var2.setTime(new Date(var0));
      int var3 = var2.get(1);
      int var4 = var2.get(2);
      int var5 = var2.get(5);
      int var6 = var2.get(11);
      int var7 = var2.get(12);
      String var8 = (var4 < 10 ? "0" : "") + var4;
      String var9 = (var5 < 10 ? "0" : "") + var5;
      String var10 = (var6 < 10 ? "0" : "") + var6;
      String var11 = (var7 < 10 ? "0" : "") + var7;
      return var3 + var8 + var9 + var10 + var11 + "Z";
   }

   private void validateVDEConfigFiles(String var1) {
      String var2 = BootStrap.getWebLogicHome();
      if (var2 == null) {
         throw new EmbeddedLDAPException("weblogic.home must be set");
      } else {
         File var3 = new File(var2, "lib");
         this.configDirPath = var3.getAbsolutePath() + File.separator;
         if (this.debugEnabled) {
            log.debug("Using configuration directory of " + this.configDirPath);
         }

         boolean var4 = true;

         for(int var5 = 0; var5 < confFiles.length; ++var5) {
            File var6 = new File(var3, confFiles[var5]);
            if (!var6.exists()) {
               var4 = false;

               String var7;
               try {
                  var7 = var6.getCanonicalPath();
               } catch (IOException var9) {
                  if (this.debugEnabled) {
                     log.debug("Error checking file " + confFiles[var5], var9);
                  }

                  var7 = var6.getAbsolutePath();
               }

               EmbeddedLDAPLogger.logConfigFileNotFound(var7);
            }
         }

         if (!var4) {
            throw new EmbeddedLDAPException("Could not find configuration files - see log file for more information");
         }
      }
   }

   private void ensureExclusiveAccess(String var1) throws ServiceFailureException {
      if (!Boolean.getBoolean("weblogic.ldap.skipExclusiveAccessCheck")) {
         String var2 = var1 + File.separator + "ldapfiles";

         try {
            lockFile = new File(var2, "EmbeddedLDAP.lok");
            FileOutputStream var3 = new FileOutputStream(lockFile);
            FileChannel var4 = var3.getChannel();
            this.lockFileLock = var4.tryLock();
            if (this.lockFileLock == null) {
               byte var5 = 4;
               byte var6 = 10;

               for(int var7 = 0; var7 < var5; ++var7) {
                  EmbeddedLDAPLogger.logEmbeddedLDAPServerRunningRetry(var2, Integer.toString(var6));

                  try {
                     Thread.sleep((long)(var6 * 1000));
                  } catch (InterruptedException var14) {
                  }

                  this.lockFileLock = var4.tryLock();
                  if (this.lockFileLock != null) {
                     break;
                  }
               }

               if (this.lockFileLock == null) {
                  Loggable var17 = EmbeddedLDAPLogger.logEmbeddedLDAPServerAlreadyRunningLoggable(var2);
                  var17.log();
                  throw new ServiceFailureException(var17.getMessageText());
               }
            }
         } catch (IOException var15) {
            EmbeddedLDAPLogger.logErrorGettingExclusiveAccess(var2, var15);
         } finally {
            if (this.lockFileLock == null) {
               lockFile = null;
            } else {
               FileUtils.registerLockFile(lockFile);
            }

         }

      }
   }

   private boolean validateVDEDirectories(String var1) throws ServiceFailureException {
      boolean var2 = false;
      File var3 = new File(var1, "ldapfiles");
      if (!var3.exists()) {
         var3.mkdirs();
         var2 = true;
      }

      File var4 = new File(var1, "replicadata");
      if (!var4.exists()) {
         var4.mkdirs();
      }

      File var5 = new File(var1, "log");
      if (!var5.exists()) {
         var5.mkdirs();
      }

      File var6 = new File(var1, "backup");
      if (!var6.exists()) {
         var6.mkdirs();
      }

      File var7 = new File(var1, "conf");
      if (!var7.exists()) {
         var7.mkdirs();
      }

      if (this.debugEnabled) {
         log.debug("Creating directories and initial files");
      }

      try {
         File var8 = new File(var7, "vde.prop");
         if (!var8.exists()) {
            PrintWriter var9 = new PrintWriter(new FileWriter(var8));
            var9.println("vde.server.threads=5");
            var9.println("vde.schemacheck=1");
            var9.println("vde.aclcheck=1");
            var9.println("vde.logfile=log/EmbeddedLDAP.log");
            var9.println("vde.logrotate.hour=0");
            var9.println("vde.logrotate.minute=10");
            var9.println("vde.logrotate.maxlogs=7");
            var9.println("vde.accesslogfile=log/EmbeddedLDAPAccess.log");
            var9.println("vde.logconsole=0");
            var9.println("vde.changelog.suffix=cn=changelog");
            var9.println("vde.changelog.file=ldapfiles/changelog");
            var9.println("vde.replicas=conf/replicas.prop");
            var9.println("vde.tls.keystore=notused");
            var9.println("vde.tls.pass=notused");
            var9.println("vde.quota.max.connections=1800");
            var9.println("vde.quota.max.opspercon=0");
            int var10 = 100;

            try {
               String var11 = System.getProperty("weblogic.security.providers.authentication.LDAPDelegatePoolSize");
               if (var11 != null && var11.length() > 0) {
                  var10 = new Integer(var11);
               }
            } catch (Exception var13) {
            }

            var9.println("vde.quota.max.conpersubject=" + var10);
            var9.println("vde.quota.max.conperip=0");
            var9.println("vde.quota.period=30000");
            var9.println("vde.quota.exemptips=");
            var9.println("vde.quota.exemptusers=");
            var9.println("vde.quota.check=1");
            var9.close();
         }

         File var15 = new File(var7, "mapping.cfg");
         if (!var15.exists()) {
            var15.createNewFile();
         }

         this.replicaProps = new Properties();
         File var16 = new File(var7, "replicas.prop");
         if (!var16.exists()) {
            PrintWriter var17 = new PrintWriter(new FileWriter(var16));
            var17.println("replica.num=0");
            var17.close();
            this.replicaProps.setProperty("replica.num", Integer.toString(this.numReplicas));
         } else if (ManagementService.getRuntimeAccess(KERNELID).isAdminServer()) {
            FileInputStream var18 = new FileInputStream(var16);
            this.replicaProps.load(var18);
            var18.close();
            String var12 = this.replicaProps.getProperty("replica.num");
            this.numReplicas = Integer.parseInt(var12);
         }

         File var19 = new File(var3, "replica.invalid");
         if (var19.exists()) {
            this.invalidReplica = true;
         }

         return var2;
      } catch (IOException var14) {
         throw new ServiceFailureException("Error creating configuration files", var14);
      }
   }

   private ServerConfig initServerConfig(String var1) throws ServiceFailureException {
      System.setProperty("vde.home", var1);
      if (this.debugEnabled) {
         log.debug("Setting vde.home to " + var1);
      }

      ServerConfig var2 = ServerConfig.getInstance();

      try {
         var2.init();
      } catch (IOException var7) {
         throw new ServiceFailureException("Error initializing VDE ", var7);
      }

      var2.setProperty("vde.server.name", this.serverMBean.getName());

      String var3;
      try {
         var3 = findLocalLdapURL();
         if (var3 == null) {
            throw new ServiceFailureException("Null VDE URL");
         }

         URI var4 = new URI(var3);
         var2.setProperty("vde.server.listenaddr", var4.getHost());
         var2.setProperty("vde.server.port", Integer.toString(var4.getPort()));
      } catch (URISyntaxException var8) {
         throw new ServiceFailureException(var8);
      }

      var2.setProperty("vde.rootuser", "cn=Admin");
      var3 = this.embeddedLDAPMBean.getCredential();
      var2.setProperty("vde.rootpw", var3);
      var2.setProperty("vde.allow.anonymous", this.embeddedLDAPMBean.isAnonymousBindAllowed() ? "true" : "false");
      boolean var9 = this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAPLogToConsole();
      var2.setProperty("vde.logconsole", var9 ? "1" : "0");
      int var5 = this.debugMBean != null ? this.debugMBean.getDebugEmbeddedLDAPLogLevel() : 0;
      var2.setProperty("vde.debug", Integer.toString(var5));
      var2.setProperty("vde.tls", "0");
      boolean var6 = ManagementService.getRuntimeAccess(KERNELID).isAdminServer();
      var2.setProperty("vde.changelog", var6 ? "1" : "0");
      var2.setProperty("vde.schema.std", this.configDirPath + "schema.core.xml");
      var2.setProperty("vde.backendtypes", this.configDirPath + "adaptertypes.prop");
      var2.setProperty("vde.server.backends", this.configDirPath + "adapters.prop");
      var2.setProperty("vde.aclfile", this.configDirPath + "acls.prop");
      if (this.debugEnabled) {
         debugLogProperties("VDE configuration properties", var2);
      }

      if (this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAPWriteOverrideProps()) {
         debugWriteProperties(getEmbeddedLDAPDir() + File.separator + "conf" + File.separator + "vde.prop", var2);
      }

      return var2;
   }

   private void updateReplicaProperties() {
      if (this.numReplicas != 0) {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNELID);
         String var2 = var1.getAdminServerName();
         String var3 = masterLDAPURL + "/";
         String var4 = "dc=" + var1.getDomainName();
         int var5 = 0;

         int var6;
         String var7;
         for(var6 = 0; var6 < this.numReplicas; ++var6) {
            var7 = "replica." + var6;
            String var8 = this.replicaProps.getProperty(var7 + ".name");
            if (var1.getDomain().lookupServer(var8) != null) {
               String var9 = var7;
               if (var6 != var5) {
                  var9 = "replica." + var5;
                  this.replicaProps.setProperty(var9 + ".name", var8);
                  this.replicaProps.setProperty(var9 + ".consumerid", this.replicaProps.getProperty(var7 + ".consumerid"));
                  this.replicaProps.setProperty(var9 + ".hostname", this.replicaProps.getProperty(var7 + ".hostname"));
                  this.replicaProps.setProperty(var9 + ".port", this.replicaProps.getProperty(var7 + ".port"));
                  this.replicaProps.setProperty(var9 + ".secure", this.replicaProps.getProperty(var7 + ".secure"));
               }

               this.replicaProps.setProperty(var9 + ".masterid", var2);
               this.replicaProps.setProperty(var9 + ".masterurl", var3);
               this.replicaProps.setProperty(var9 + ".base", var4);
               this.replicaProps.setProperty(var9 + ".binddn", "cn=Admin");
               ++var5;
            }
         }

         for(var6 = var5; var6 < this.numReplicas; ++var6) {
            var7 = "replica." + var6;
            this.replicaProps.remove(var7 + ".name");
            this.replicaProps.remove(var7 + ".consumerid");
            this.replicaProps.remove(var7 + ".hostname");
            this.replicaProps.remove(var7 + ".port");
            this.replicaProps.remove(var7 + ".secure");
            this.replicaProps.remove(var7 + ".masterid");
            this.replicaProps.remove(var7 + ".masterurl");
            this.replicaProps.remove(var7 + ".base");
            this.replicaProps.remove(var7 + ".binddn");
         }

         this.numReplicas = var5;
         this.replicaProps.setProperty("replica.num", Integer.toString(this.numReplicas));
         this.writeReplicaProps();
      }
   }

   private void initReplication() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNELID);
      Properties var2 = new Properties();
      String var3 = this.embeddedLDAPMBean.getCredential();
      if (var1.isAdminServer()) {
         for(int var4 = 0; var4 < this.numReplicas; ++var4) {
            var2.setProperty("replica." + var4 + ".bindpw", var3);
         }
      } else {
         String var8 = "replica.0";
         var2.setProperty(var8 + ".name", this.serverMBean.getName());
         var2.setProperty(var8 + ".consumerid", this.serverMBean.getName());
         var2.setProperty(var8 + ".base", "dc=" + var1.getDomainName());
         var2.setProperty(var8 + ".masterid", var1.getAdminServerName());
         var2.setProperty(var8 + ".masterurl", masterLDAPURL + "/");

         try {
            URI var5 = this.findLdapURI(this.serverMBean);
            var2.setProperty(var8 + ".hostname", var5.getHost());
            var2.setProperty(var8 + ".port", Integer.toString(var5.getPort()));
            boolean var6 = var5.getScheme().equalsIgnoreCase("ldaps");
            var2.setProperty(var8 + ".secure", var6 ? "1" : "0");
         } catch (URISyntaxException var7) {
            throw new IllegalStateException("No master embedded LDAP server.");
         }

         var2.setProperty(var8 + ".binddn", "cn=Admin");
         var2.setProperty(var8 + ".bindpw", var3);
         var2.setProperty("replica.num", "1");
      }

      if (this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAP()) {
         debugLogProperties("VDE Replica properties: ", this.replicaProps);
         debugLogProperties("VDE Override Replica properties: ", var2);
      }

      if (this.debugMBean != null && this.debugMBean.getDebugEmbeddedLDAPWriteOverrideProps()) {
         debugWriteProperties(getEmbeddedLDAPDir() + File.separator + "conf" + File.separator + "replicas.prop", var2);
      }

      this.replication = new Replication();
      this.replication.init(var2, KERNELID.getSubject());
   }

   private String getInitialReplicaFromAdminServer(boolean var1, boolean var2) {
      URL var3 = null;
      InputStream var4 = null;
      FileOutputStream var5 = null;

      try {
         var3 = FileDistributionServlet.getURL();
      } catch (MalformedURLException var23) {
         this.setReplicaInvalid();
         throw new EmbeddedLDAPException("Unable to build initial replica url", var23);
      }

      String var6 = findLocalLdapURL();
      if (var6 == null) {
         this.setReplicaInvalid();
         throw new EmbeddedLDAPException("Unable to get local addressing information");
      } else {
         HttpURLConnection var7 = null;

         try {
            var7 = URLManager.createAdminHttpConnection(var3);
            ConnectionSigner.signConnection(var7, KERNELID);
            var7.setRequestProperty("wl_request_type", "wl_init_replica_request");
            var7.setRequestProperty("init-replica_server-name", mimeEncode(ManagementService.getRuntimeAccess(KERNELID).getServerName()));
            var7.setRequestProperty("init-replica_server-url", var6);
            if (!var1) {
               var7.setRequestProperty("init-replica-validate", String.valueOf(var1));
            }

            var7.setRequestProperty("Connection", "Close");
            String var8 = null;
            var4 = var7.getInputStream();
            byte[] var9 = new byte[4096];
            int var10 = var4.read(var9, 0, 4096);
            String var11;
            if (var10 == -1) {
               if (!var1) {
                  var11 = null;
                  return var11;
               } else if (!isDBMSOnly) {
                  throw new EmbeddedLDAPException("Empty initial replica");
               } else {
                  if (this.debugEnabled) {
                     this.logger.log(7, this.ldapServer, "Received empty replica file for EmbeddedLDAP");
                  }

                  var11 = null;
                  return var11;
               }
            } else {
               var8 = getEmbeddedLDAPDir() + File.separator + ManagementService.getRuntimeAccess(KERNELID).getServerName() + ".ldif";
               var5 = new FileOutputStream(var8);

               do {
                  var5.write(var9, 0, var10);
               } while((var10 = var4.read(var9, 0, 4096)) != -1);

               if (var2) {
                  this.cleanupDataDirectory(false);
               }

               var11 = var8;
               return var11;
            }
         } catch (IOException var24) {
            this.setReplicaInvalid();
            throw new EmbeddedLDAPException("Unable to open initial replica url: " + var3, var24);
         } finally {
            try {
               if (var4 != null) {
                  var4.close();
               }

               if (var5 != null) {
                  var5.close();
               }

               if (var7 != null) {
                  var7.disconnect();
               }
            } catch (Exception var22) {
            }

         }
      }
   }

   private void loadInitialReplicaFile(String var1) {
      boolean var2 = false;

      try {
         if (this.debugEnabled) {
            log.debug("Loading initial replica file " + var1);
         }

         boolean var3 = (new LDIF()).importLDIF(var1, (InputStream)null, true);
         if (!var3) {
            EmbeddedLDAPLogger.logReloadInitReplicaFile();
            boolean var4 = (new LDIF()).importLDIF(var1, (InputStream)null, true);
            if (!var4) {
               throw new EmbeddedLDAPException(EmbeddedLDAPLogger.getErrLoadInitReplicaFile());
            }

            EmbeddedLDAPLogger.logSuccessReloadInitReplicaFile();
         }

         var2 = true;
      } finally {
         if (!var2) {
            this.setReplicaInvalid();
            this.cleanupDataDirectory(true);
         }

      }

   }

   private void cleanupDataDirectory(boolean var1) {
      File var2 = new File(getEmbeddedLDAPDir(), "ldapfiles");
      if (var2.exists()) {
         File[] var3 = var2.listFiles();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            File var5 = var3[var4];
            if (!var5.delete()) {
               var5.deleteOnExit();
               EmbeddedLDAPLogger.logCouldNotDeleteOnCleanup(var5.getAbsolutePath(), var2.getAbsolutePath());
            }
         }

         if (var1 && !var2.delete()) {
            var2.deleteOnExit();
            EmbeddedLDAPLogger.logCouldNotDeleteOnCleanup(var2.getAbsolutePath(), var2.getAbsolutePath());
         }

      }
   }

   private URI findLdapURI(ServerMBean var1) throws URISyntaxException {
      if (var1.getName().equals(this.serverMBean.getName())) {
         return new URI(findLocalLdapURL());
      } else {
         ServerIdentity var2 = ServerIdentityManager.findServerIdentity(getEmbeddedLDAPDomain(), var1.getName());
         if (var2 == null) {
            throw new URISyntaxException("<null>", "Unknown host");
         } else {
            String var3 = findLdapURL(var2);
            if (var3 == null) {
               throw new URISyntaxException("<null>", "Null url");
            } else {
               return new URI(var3);
            }
         }
      }
   }

   private static String findLocalLdapURL() {
      return findLdapURL(LocalServerIdentity.getIdentity());
   }

   public static String findLdapURL(ServerIdentity var0) {
      ServerChannel var1 = ServerChannelManager.findServerChannel(var0, ProtocolHandlerAdmin.PROTOCOL_ADMIN);
      String var2 = null;
      if (var1 == null || !var1.supportsTLS()) {
         var2 = URLManager.findURL(var0, ProtocolHandlerLDAP.PROTOCOL_LDAP);
      }

      if (var2 == null) {
         var2 = URLManager.findURL(var0, ProtocolHandlerLDAPS.PROTOCOL_LDAPS);
      }

      return var2;
   }

   private void writeReplicaProps() {
      File var1 = new File(getEmbeddedLDAPDir() + File.separator + "conf" + File.separator + "replicas.prop");

      try {
         this.replicaProps.save(new FileOutputStream(var1), "Generated property file");
      } catch (Exception var3) {
         EmbeddedLDAPLogger.logErrorWritingReplicasFile(var1.getAbsolutePath(), var3.toString());
      }

   }

   private static void debugLogProperties(String var0, Properties var1) {
      log.debug("Logging properties for " + var0);
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         log.debug("Property " + var3 + "=" + var1.getProperty(var3));
      }

   }

   private static void debugWriteProperties(String var0, Properties var1) {
      try {
         PrintWriter var2 = new PrintWriter(new FileWriter(var0, true));
         var2.println("# Adding properties set at runtime");
         Iterator var3 = var1.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.println(var4 + "=" + var1.getProperty(var4));
         }

         var2.close();
      } catch (IOException var5) {
         log.debug("Got I/O error writing override props", var5);
      }

   }

   private static String mimeEncode(String var0) {
      String var1 = null;

      try {
         var1 = MimeUtility.encodeText(var0, "UTF-8", (String)null);
      } catch (UnsupportedEncodingException var3) {
         var1 = var0;
      }

      return var1;
   }

   private static final class EncryptionHelperImpl implements EncryptionHelper {
      private ClearOrEncryptedService encrypter;

      public EncryptionHelperImpl(ClearOrEncryptedService var1) {
         this.encrypter = var1;
      }

      public String encrypt(String var1) {
         return this.encrypter.encrypt(var1);
      }

      public String decrypt(String var1) {
         return this.encrypter.decrypt(var1);
      }

      public boolean isEncrypted(String var1) {
         return this.encrypter.isEncrypted(var1);
      }
   }

   private static class EmbeddedLDAPTimedActivity implements TimerListener {
      BackendStandard backend;

      EmbeddedLDAPTimedActivity(BackendStandard var1) {
         this.backend = var1;
      }

      public void timerExpired(Timer var1) {
         TimedActivityThread.getInstance().runOnDemand();
         Logger.getInstance().flush();
         if (this.backend != null) {
            this.backend.cleanupPools();
         }

         TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(this, 60000L);
      }
   }
}
