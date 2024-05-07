package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.Config;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.util.Platform;
import weblogic.nodemanager.util.ProcessControl;
import weblogic.nodemanager.util.ProcessControlFactory;

public class NMServerConfig extends Config {
   private String nmHome = System.getProperty("user.dir");
   private String weblogicHome = System.getProperty("user.dir");
   private String listenAddress;
   private int listenPort = 5556;
   private int listenBacklog = 50;
   private boolean secureListener = true;
   private boolean nativeVersionEnabled = true;
   private boolean crashRecoveryEnabled = false;
   private String javaHome = System.getProperty("java.home");
   private String logFile;
   private int logLimit = 0;
   private int logCount = 1;
   private boolean logAppend = true;
   private boolean logToStderr = false;
   private Level logLevel;
   private Formatter logFormatter;
   private boolean authenticationEnabled;
   private boolean domainsFileEnabled;
   private File domainsFile;
   private long domainsFileModTime;
   private Map domainsMap;
   private ProcessControl processControl;
   private boolean startScriptEnabled;
   private boolean stopScriptEnabled;
   private String startScriptName;
   private String stopScriptName;
   private boolean quitEnabled;
   private int stateCheckInterval;
   private final List<NetworkInfo> networkInfoList;
   private String ifConfigDir;
   private long execScriptTimeout;
   private boolean domainRegistrationEnabled;
   private boolean domainsDirRemoteSharingEnabled;
   private boolean useMACBroadcast;
   public static final String PROPERTIES_VERSION_PROP = "PropertiesVersion";
   public static final String NM_HOME_PROP = "NodeManagerHome";
   public static final String WEBLOGIC_HOME_PROP = "WeblogicHome";
   public static final String JAVA_HOME_PROP = "JavaHome";
   public static final String LISTEN_ADDRESS_PROP = "ListenAddress";
   public static final String LISTEN_PORT_PROP = "ListenPort";
   public static final String LISTEN_BACKLOG_PROP = "ListenBacklog";
   public static final String LISTENER_TYPE_PROP = "ListenerType";
   public static final String SECURE_LISTENER_PROP = "SecureListener";
   public static final String NATIVE_VERSION_ENABLED_PROP = "NativeVersionEnabled";
   public static final String CRASH_RECOVERY_ENABLED_PROP = "CrashRecoveryEnabled";
   public static final String LOG_LIMIT_PROP = "LogLimit";
   public static final String LOG_COUNT_PROP = "LogCount";
   public static final String LOG_FILE_PROP = "LogFile";
   public static final String LOG_APPEND_PROP = "LogAppend";
   public static final String LOG_TO_STDERR_PROP = "LogToStderr";
   public static final String LOG_LEVEL_PROP = "LogLevel";
   public static final String LOG_FORMATTER_PROP = "LogFormatter";
   public static final String AUTHENTICATION_ENABLED_PROP = "AuthenticationEnabled";
   public static final String KEY_PASSWORD_PROP = "keyPassword";
   public static final String DOMAINS_FILE_PROP = "DomainsFile";
   public static final String DOMAINS_FILE_ENABLED_PROP = "DomainsFileEnabled";
   public static final String DOMAIN_DIR_PROP = "DomainDir.";
   public static final String START_SCRIPT_ENABLED_PROP = "StartScriptEnabled";
   public static final String STOP_SCRIPT_ENABLED_PROP = "StopScriptEnabled";
   public static final String START_SCRIPT_NAME_PROP = "StartScriptName";
   public static final String STOP_SCRIPT_NAME_PROP = "StopScriptName";
   public static final String QUIT_ENABLED_PROP = "QuitEnabled";
   public static final String PROPERTIES_FILE_PROP = "PropertiesFile";
   public static final String STATE_CHECK_INTERVAL_PROP = "StateCheckInterval";
   public static final String IF_INTERFACE_NAME = "Interface";
   public static final String IF_NET_MASK_NAME = "NetMask";
   public static final String IF_CONFIG_DIR_PROP = "IfConfigDir";
   public static final String SCRIPT_TIMEOUT_PROP = "ScriptTimeout";
   public static final String USE_MAC_BROADCST_PROP = "UseMACBroadcast";
   public static final String DOMAIN_REGISTRATION_ENABLED_PROP = "DomainRegistrationEnabled";
   public static final String DOMAINDIRS_DIR_REMOTE_SHARING_ENABLED_PROP = "DomainsDirRemoteSharingEnabled";
   private static final String[] KNOWNPROPS = new String[]{"PropertiesVersion", "AuthenticationEnabled", "LogFile", "LogLimit", "LogCount", "LogAppend", "LogToStderr", "LogLevel", "LogFormatter", "ListenBacklog", "CrashRecoveryEnabled", "SecureListener", "CipherSuite", "StartScriptEnabled", "StartScriptName", "StopScriptEnabled", "StopScriptName", "QuitEnabled", "RestartInterval", "RestartMax", "DomainsFile", "DomainsFileEnabled", "StateCheckInterval", "CustomIdentityAlias", "CustomIdentity", "JavaHome", "KeyStores", "ListenAddress", "ListenPort", "NativeVersionEnabled", "NodeManagerHome", "WeblogicHome", "keyFile", "keyPassword", "certificateFile", "NetMask", "Interface", "DomainsDirRemoteSharingEnabled", "CustomTrustKeyStorePassPhrase", "CustomIdentityKeyStoreType", "JavaStandardTrustKeyStorePassPhrase", "DomainRegistrationEnabled", "CustomIdentityKey", "StoreFileName", "KeyStorePassPhrase", "KeyStoreType", "PrivateKeyPassPhrase", "JavaStandardTrustKey", "StorePassPhrase", "CustomTrustKeyStoreFileName", "CustomTrustKeyPassPhrase"};
   public static final int LISTEN_PORT = 5556;
   public static final int PLAIN_LISTEN_PORT = 5556;
   public static final int LISTEN_BACKLOG = 50;
   public static final String LOG_FILE_NAME = "nodemanager.log";
   public static final String DOMAINS_FILE_NAME = "nodemanager.domains";
   private static final Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public NMServerConfig(Properties var1) throws IOException, ConfigException {
      super(var1);
      this.logLevel = Level.INFO;
      this.logFormatter = new LogFormatter();
      this.authenticationEnabled = true;
      this.domainsFileEnabled = true;
      this.startScriptEnabled = false;
      this.stopScriptEnabled = false;
      this.startScriptName = Platform.isWindows() ? "startWebLogic.cmd" : "startWebLogic.sh";
      this.stopScriptName = null;
      this.quitEnabled = false;
      this.stateCheckInterval = 500;
      this.networkInfoList = new ArrayList();
      this.domainRegistrationEnabled = false;
      this.domainsDirRemoteSharingEnabled = false;
      this.useMACBroadcast = false;
      checkUpgrade(var1, false);
      this.nmHome = this.getProperty("NodeManagerHome", this.nmHome);
      this.javaHome = this.getProperty("JavaHome", this.javaHome);
      this.listenBacklog = this.getIntProperty("ListenBacklog", this.listenBacklog);
      this.listenAddress = this.getProperty("ListenAddress");
      this.secureListener = this.getBooleanProperty("SecureListener", this.secureListener);
      if (this.secureListener) {
         this.listenPort = this.getIntProperty("ListenPort", 5556);
      } else {
         this.listenPort = this.getIntProperty("ListenPort", 5556);
      }

      this.nativeVersionEnabled = this.getBooleanProperty("NativeVersionEnabled", this.nativeVersionEnabled);
      this.crashRecoveryEnabled = this.getBooleanProperty("CrashRecoveryEnabled", this.crashRecoveryEnabled);
      this.authenticationEnabled = this.getBooleanProperty("AuthenticationEnabled", this.authenticationEnabled);
      this.logFile = var1.getProperty("LogFile");
      if (this.logFile == null) {
         this.logFile = (new File(this.nmHome, "nodemanager.log")).getPath();
      }

      this.logLevel = this.getLevelProperty("LogLevel", this.logLevel);
      String var2;
      if ((var2 = var1.getProperty("LogFormatter")) != null) {
         this.logFormatter = this.loadFormatter(var2);
      }

      this.logLimit = this.getIntProperty("LogLimit", this.logLimit);
      this.logCount = this.getIntProperty("LogCount", this.logCount);
      this.logAppend = this.getBooleanProperty("LogAppend", this.logAppend);
      this.logToStderr = this.getBooleanProperty("LogToStderr", this.logToStderr);
      this.domainsFileEnabled = this.getBooleanProperty("DomainsFileEnabled", this.domainsFileEnabled);
      if ((var2 = this.getProperty("DomainsFile")) != null) {
         this.domainsFile = new File(var2);
      } else {
         this.domainsFile = new File(this.nmHome, "nodemanager.domains");
      }

      this.initLogger(nmLog);
      this.initDomainsMap();
      if (this.nativeVersionEnabled) {
         this.initProcessControl();
      }

      this.startScriptEnabled = this.getBooleanProperty("StartScriptEnabled", this.startScriptEnabled);
      this.stopScriptEnabled = this.getBooleanProperty("StopScriptEnabled", this.stopScriptEnabled);
      this.startScriptName = this.getProperty("StartScriptName", this.startScriptName);
      this.stopScriptName = this.getProperty("StopScriptName", this.stopScriptName);
      this.quitEnabled = this.getBooleanProperty("QuitEnabled", this.quitEnabled);
      this.ifConfigDir = this.getProperty("IfConfigDir");
      this.execScriptTimeout = this.getLongProperty("ScriptTimeout", 60000L);
      this.useMACBroadcast = this.getBooleanProperty("UseMACBroadcast", this.useMACBroadcast);
      this.domainRegistrationEnabled = this.getBooleanProperty("DomainRegistrationEnabled", this.domainRegistrationEnabled);
      this.domainsDirRemoteSharingEnabled = this.getBooleanProperty("DomainsDirRemoteSharingEnabled", this.domainsDirRemoteSharingEnabled);
   }

   private long getLongProperty(String var1, long var2) throws ConfigException {
      String var4 = this.getProperty(var1);
      if (var4 != null) {
         try {
            return Long.parseLong(var4);
         } catch (IllegalArgumentException var6) {
            throw new ConfigException(nmText.getInvalidScriptTimeout(var4, var1));
         }
      } else {
         return var2;
      }
   }

   private Level getLevelProperty(String var1, Level var2) throws ConfigException {
      String var3 = this.getProperty("LogLevel");
      if (var3 != null) {
         try {
            return Level.parse(var3);
         } catch (IllegalArgumentException var5) {
            throw new ConfigException(nmText.getInvalidLogLevel(var3, var1));
         }
      } else {
         return var2;
      }
   }

   private Formatter loadFormatter(String var1) throws ConfigException {
      try {
         Object var2 = Class.forName(var1).newInstance();
         return (Formatter)var2;
      } catch (Throwable var3) {
         throw new ConfigException(nmText.getLogFormatterError(var1), var3);
      }
   }

   private void initProcessControl() throws ConfigException {
      try {
         this.processControl = ProcessControlFactory.getProcessControl();
      } catch (UnsatisfiedLinkError var2) {
         throw new ConfigException(nmText.getNativeLibraryLoadError(), var2);
      }

      if (this.processControl == null) {
         throw new ConfigException(nmText.getNativeLibraryNA());
      }
   }

   public static boolean checkUpgrade(Properties var0, boolean var1) throws ConfigException {
      boolean var2 = false;
      String var3;
      if ((var3 = var0.getProperty("ListenerType")) != null) {
         var0.remove("ListenerType");
         if (var1) {
            Upgrader.log(Level.INFO, nmText.getRemovingProp("ListenerType"));
         }

         var2 = true;
         if (!var0.contains("SecureListener")) {
            if (var3.equalsIgnoreCase("plainSocket")) {
               if (var1) {
                  Upgrader.log(Level.INFO, nmText.getAddingProp("SecureListener", "false"));
               }

               var0.setProperty("SecureListener", "false");
            } else {
               if (!var3.equalsIgnoreCase("secureSocket")) {
                  throw new ConfigException(nmText.getInvalidPropValue("ListenerType", var3));
               }

               if (var1) {
                  Upgrader.log(Level.INFO, nmText.getAddingProp("SecureListener", "true"));
               }

               var0.setProperty("SecureListener", "true");
            }
         }
      }

      var3 = var0.getProperty("PropertiesVersion");
      if (!"10.3".equals(var3)) {
         var0.setProperty("PropertiesVersion", "10.3");
         if (var1) {
            Upgrader.log(Level.INFO, nmText.getSettingVersion("10.3"));
         }

         var2 = true;
      }

      return var2;
   }

   public Map getDomainsMap() throws ConfigException {
      if (this.domainsFileEnabled) {
         long var1 = this.domainsFile.lastModified();
         if (var1 != this.domainsFileModTime) {
            NMProperties var3 = new NMProperties();
            nmLog.info(nmText.getReloadingDomainsFile(this.domainsFile.toString()));

            try {
               var3.load(this.domainsFile);
               this.domainsFileModTime = var1;
            } catch (IllegalArgumentException var5) {
               nmLog.warning(nmText.getInvalidDomainsFile(this.domainsFile.toString()));
            } catch (FileNotFoundException var6) {
               nmLog.warning(nmText.getDomainsFileNotFound(this.domainsFile.toString()));
            } catch (IOException var7) {
               nmLog.log(Level.WARNING, nmText.getErrorReadingDomainsFile(this.domainsFile.toString()), var7);
            }

            this.loadDomainsProps(this.props, var3);
            this.domainsMap = var3;
            this.printDomainsMap(new PrintWriter(new OutputStreamWriter(System.err), true));
         }
      }

      return this.domainsMap;
   }

   private void initDomainsMap() throws ConfigException {
      NMProperties var1 = new NMProperties();
      nmLog.info(nmText.getLoadingDomainsFile(this.domainsFile.toString()));

      String var3;
      try {
         long var2 = this.domainsFile.lastModified();
         var1.load(this.domainsFile);
         this.domainsFileModTime = var2;
      } catch (IllegalArgumentException var4) {
         var3 = nmText.getInvalidDomainsFile(this.domainsFile.toString());
         nmLog.severe(var3);
         throw new ConfigException(var3);
      } catch (FileNotFoundException var5) {
         nmLog.warning(nmText.getDomainsFileNotFound(this.domainsFile.toString()));
      } catch (IOException var6) {
         var3 = nmText.getErrorReadingDomainsFile(this.domainsFile.toString());
         nmLog.log(Level.SEVERE, var3, var6);
         throw new ConfigException(var3, var6);
      }

      this.loadDomainsProps(this.props, var1);
      this.domainsMap = var1;
   }

   private void loadDomainsProps(Properties var1, Map var2) {
      Iterator var3 = var1.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.startsWith("DomainDir.")) {
            String var5 = var1.getProperty(var4);
            var2.put(var4.substring("DomainDir.".length()), var5);
         }
      }

   }

   public String getNMHome() {
      return this.nmHome;
   }

   public String getWeblogicHome() {
      return this.weblogicHome;
   }

   public int getListenPort() {
      return this.listenPort;
   }

   public int getListenBacklog() {
      return this.listenBacklog;
   }

   public String getListenAddress() {
      return this.listenAddress;
   }

   public boolean isSecureListener() {
      return this.secureListener;
   }

   public boolean isNativeVersionEnabled() {
      return this.nativeVersionEnabled;
   }

   public boolean isCrashRecoveryEnabled() {
      return this.crashRecoveryEnabled;
   }

   public boolean isAuthenticationEnabled() {
      return this.authenticationEnabled;
   }

   public boolean isStartScriptEnabled() {
      return this.startScriptEnabled;
   }

   public boolean isStopScriptEnabled() {
      return this.stopScriptEnabled;
   }

   public String getStartScriptName() {
      return this.startScriptName;
   }

   public String getStopScriptName() {
      return this.stopScriptName;
   }

   public String getLogFile() {
      return this.logFile;
   }

   public Formatter getLogFormatter() {
      return this.logFormatter;
   }

   public ProcessControl getProcessControl() {
      return this.processControl;
   }

   public boolean getQuitEnabled() {
      return this.quitEnabled;
   }

   public int getStateCheckInterval() {
      return this.stateCheckInterval;
   }

   public NetworkInfo getNetworkInfoFor(String var1) throws IOException {
      InetAddress var2 = InetAddress.getByName(var1);
      Iterator var3 = this.getNetworkInfoList().iterator();

      NetworkInfo var4;
      do {
         if (!var3.hasNext()) {
            throw new InvalidPropertiesFormatException("Missing an appropriate entry for " + var1);
         }

         var4 = (NetworkInfo)var3.next();
      } while(!var4.isNetworkInfoFor(var2));

      return var4;
   }

   private List<NetworkInfo> getNetworkInfoList() throws IOException {
      if (this.networkInfoList.isEmpty()) {
         this.initNetworkInfoList();
      }

      return this.networkInfoList;
   }

   private void initNetworkInfoList() throws IOException {
      List var1 = Arrays.asList(KNOWNPROPS);
      Iterator var2 = this.props.keySet().iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         if (!var1.contains(var3)) {
            try {
               this.networkInfoList.add(NetworkInfo.convertConfEntry(var3, this.props.getProperty(var3)));
            } catch (IOException var5) {
            }
         }
      }

      String var6 = this.getProperty("Interface");
      var3 = this.getProperty("NetMask");
      if (var6 != null) {
         if (var3 == null && Platform.isWindows()) {
            throw new InvalidPropertiesFormatException(nmText.missingNetMaskProp(var6));
         }

         this.networkInfoList.add(new NetworkInfo(var6, var3));
      }

      if (this.networkInfoList.isEmpty()) {
         throw new InvalidPropertiesFormatException(nmText.missingSrvrMigProp());
      }
   }

   public NMProperties getConfigProperties() {
      NMProperties var1 = new NMProperties();
      var1.setProperty("NodeManagerHome", this.nmHome);
      var1.setProperty("ListenAddress", this.listenAddress != null ? this.listenAddress : "");
      var1.setProperty("ListenPort", String.valueOf(this.listenPort));
      var1.setProperty("ListenBacklog", String.valueOf(this.listenBacklog));
      var1.setProperty("SecureListener", String.valueOf(this.secureListener));
      var1.setProperty("AuthenticationEnabled", String.valueOf(this.authenticationEnabled));
      var1.setProperty("NativeVersionEnabled", String.valueOf(this.nativeVersionEnabled));
      var1.setProperty("CrashRecoveryEnabled", String.valueOf(this.crashRecoveryEnabled));
      var1.setProperty("JavaHome", this.javaHome);
      var1.setProperty("StartScriptEnabled", String.valueOf(this.startScriptEnabled));
      var1.setProperty("StopScriptEnabled", String.valueOf(this.stopScriptEnabled));
      var1.setProperty("StartScriptName", this.startScriptName);
      if (this.stopScriptName != null) {
         var1.setProperty("StopScriptName", this.stopScriptName);
      }

      var1.setProperty("LogFile", this.logFile);
      var1.setProperty("LogLevel", String.valueOf(this.logLevel));
      var1.setProperty("LogLimit", String.valueOf(this.logLimit));
      var1.setProperty("LogCount", String.valueOf(this.logCount));
      var1.setProperty("LogAppend", String.valueOf(this.logAppend));
      var1.setProperty("LogToStderr", String.valueOf(this.logToStderr));
      var1.setProperty("LogFormatter", this.logFormatter.getClass().getName());
      var1.setProperty("DomainsFile", String.valueOf(this.domainsFile));
      var1.setProperty("DomainsFileEnabled", String.valueOf(this.domainsFileEnabled));
      var1.setProperty("QuitEnabled", String.valueOf(this.quitEnabled));
      var1.setProperty("StateCheckInterval", String.valueOf(this.stateCheckInterval));

      try {
         Iterator var2 = this.getNetworkInfoList().iterator();

         while(var2.hasNext()) {
            NetworkInfo var3 = (NetworkInfo)var2.next();
            var1.setProperty(var3.getInterfaceName(), var3.getPropertyValueString());
         }
      } catch (IOException var4) {
      }

      var1.setProperty("DomainRegistrationEnabled", String.valueOf(this.domainRegistrationEnabled));
      var1.setProperty("DomainsDirRemoteSharingEnabled", String.valueOf(this.domainsDirRemoteSharingEnabled));
      return var1;
   }

   public void print(PrintStream var1) {
      this.print(new PrintWriter(new OutputStreamWriter(var1), true));
   }

   public void print(PrintWriter var1) {
      var1.println("Configuration settings:");
      var1.println();
      var1.println("NodeManagerHome=" + this.nmHome);
      var1.println("ListenAddress=" + (this.listenAddress != null ? this.listenAddress : ""));
      var1.println("ListenPort=" + this.listenPort);
      var1.println("ListenBacklog=" + this.listenBacklog);
      var1.println("SecureListener=" + this.secureListener);
      var1.println("AuthenticationEnabled=" + this.authenticationEnabled);
      var1.println("NativeVersionEnabled=" + this.nativeVersionEnabled);
      var1.println("CrashRecoveryEnabled=" + this.crashRecoveryEnabled);
      var1.println("JavaHome=" + this.javaHome);
      var1.println("StartScriptEnabled=" + this.startScriptEnabled);
      var1.println("StopScriptEnabled=" + this.stopScriptEnabled);
      var1.println("StartScriptName=" + this.startScriptName);
      var1.println("StopScriptName=" + (this.stopScriptName == null ? "" : this.stopScriptName));
      var1.println("LogFile=" + this.logFile);
      var1.println("LogLevel=" + this.logLevel);
      var1.println("LogLimit=" + this.logLimit);
      var1.println("LogCount=" + this.logCount);
      var1.println("LogAppend=" + this.logAppend);
      var1.println("LogToStderr=" + this.logToStderr);
      var1.println("LogFormatter=" + this.logFormatter.getClass().getName());
      var1.println("DomainsFile=" + this.domainsFile);
      var1.println("DomainsFileEnabled=" + this.domainsFileEnabled);
      var1.println("StateCheckInterval=" + this.stateCheckInterval);
      if (this.quitEnabled) {
         var1.println("QuitEnabled=" + this.quitEnabled);
      }

      try {
         Iterator var2 = this.getNetworkInfoList().iterator();

         while(var2.hasNext()) {
            NetworkInfo var3 = (NetworkInfo)var2.next();
            var1.println(var3);
         }
      } catch (IOException var4) {
      }

      var1.println("UseMACBroadcast=" + this.useMACBroadcast);
      var1.println("DomainRegistrationEnabled=" + this.domainRegistrationEnabled);
      var1.println("DomainsDirRemoteSharingEnabled=" + this.domainsDirRemoteSharingEnabled);
      var1.println();
      this.printDomainsMap(var1);
   }

   public void printDomainsMap(PrintWriter var1) {
      var1.println("Domain name mappings:");
      var1.println();
      Iterator var2 = this.domainsMap.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.println(var3.getKey() + " -> " + var3.getValue());
      }

      var1.println();
   }

   public void initLogger(Logger var1) throws IOException {
      var1.setUseParentHandlers(true);
      var1.setLevel(this.logLevel);
      FileHandler var2 = new FileHandler(this.logFile, this.logLimit, this.logCount, this.logAppend);
      var2.setFormatter(this.logFormatter);
      var2.setLevel(this.logLevel);
      var1.addHandler(var2);
      if (this.logToStderr) {
         ConsoleHandler var3 = new ConsoleHandler();
         var3.setFormatter(this.logFormatter);
         var3.setLevel(this.logLevel);
         var1.addHandler(var3);
      }

   }

   public String getIfConfigDir() {
      return this.ifConfigDir;
   }

   public long getExecScriptTimeout() {
      return this.execScriptTimeout;
   }

   public boolean useMACBroadcast() {
      return this.useMACBroadcast;
   }

   public boolean isDomainRegistrationEnabled() {
      return this.domainRegistrationEnabled;
   }

   public boolean isDomainsDirRemoteSharingEnabled() {
      return this.domainsDirRemoteSharingEnabled;
   }
}
