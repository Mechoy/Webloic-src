package weblogic.nodemanager.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

public class StartupConfig extends Config {
   private String javaVendor;
   private String javaHome;
   private String beaHome;
   private String classPath;
   private String securityPolicyFile;
   private String arguments;
   private String sslArguments;
   private String adminURL;
   private String username;
   private String password;
   private boolean autoRestart = true;
   private boolean autoKillIfFailed = false;
   private int restartMax = 2;
   private int restartDelaySeconds = 0;
   private int restartInterval = 0;
   private String serverIPs;
   private transient List<String> serverIPList;
   private String serverUid;
   private String serverGid;
   private String trustKeyStore;
   private String customTrustKeyStoreFileName;
   private String customTrustKeyStoreType;
   private String customTrustKeyStorePassPhrase;
   private String javaStandardTrustKeyStorePassPhrase;
   private String serverOutFile = null;
   private String serverErrFile = null;
   private String transientScriptEnv;
   private String nmHostName;
   public static final String JAVA_VENDOR_PROP = "JavaVendor";
   public static final String JAVA_HOME_PROP = "JavaHome";
   public static final String ARGUMENTS_PROP = "Arguments";
   public static final String SSL_ARGUMENTS_PROP = "SSLArguments";
   public static final String SECURITY_POLICY_FILE_PROP = "SecurityPolicyFile";
   public static final String CLASS_PATH_PROP = "ClassPath";
   public static final String BEA_HOME_PROP = "BeaHome";
   public static final String ADMIN_URL_PROP = "AdminURL";
   public static final String AUTO_RESTART_PROP = "AutoRestart";
   public static final String AUTO_KILL_IF_FAILED_PROP = "AutoKillIfFailed";
   public static final String RESTART_MAX_PROP = "RestartMax";
   public static final String RESTART_INTERVAL_PROP = "RestartInterval";
   public static final String RESTART_DELAY_SECONDS_PROP = "RestartDelaySeconds";
   public static final String USERNAME_PROP = "username";
   public static final String PASSWORD_PROP = "password";
   public static final String SERVER_IP_PROP = "ServerIP";
   public static final String SERVER_UID_PROP = "ServerUID";
   public static final String SERVER_GID_PROP = "ServerGID";
   public static final String TRANSIENT_SCRIPT_ENV = "TransientScriptEnv";
   public static final String NM_HOSTNAME_PROP = "NMHostName";
   public static final String SERVER_CMDLINE_STDOUT_PROPERTY = "-Dweblogic.Stdout=";
   public static final String SERVER_CMDLINE_STDERR_PROPERTY = "-Dweblogic.Stderr=";

   public StartupConfig(Properties var1) throws ConfigException {
      super(var1);
      this.loadProperties();
      this.processArguments();
   }

   public StartupConfig() {
   }

   private void loadProperties() throws ConfigException {
      this.javaVendor = this.getProperty("JavaVendor", this.javaVendor);
      this.javaHome = this.getProperty("JavaHome", this.javaHome);
      this.arguments = this.getProperty("Arguments");
      this.sslArguments = this.getProperty("SSLArguments");
      this.securityPolicyFile = this.getProperty("SecurityPolicyFile", this.securityPolicyFile);
      this.classPath = this.getProperty("ClassPath", this.classPath);
      this.beaHome = this.getProperty("BeaHome", this.beaHome);
      this.adminURL = this.getProperty("AdminURL");
      this.username = this.getProperty("username");
      if (this.username == null) {
         this.username = this.getProperty("Username");
      }

      this.password = this.getProperty("password");
      if (this.password == null) {
         this.password = this.getProperty("Password");
      }

      this.autoRestart = this.getBooleanProperty("AutoRestart", this.autoRestart);
      this.autoKillIfFailed = this.getBooleanProperty("AutoKillIfFailed", this.autoKillIfFailed);
      this.setServerIPs(this.getProperty("ServerIP"));
      this.restartMax = this.getIntProperty("RestartMax", this.restartMax);
      this.restartInterval = this.getIntProperty("RestartInterval", this.restartInterval);
      this.restartDelaySeconds = this.getIntProperty("RestartDelaySeconds", this.restartDelaySeconds);
      this.serverUid = this.getProperty("ServerUID");
      this.serverGid = this.getProperty("ServerGID");
      this.nmHostName = this.getProperty("NMHostName");
      this.trustKeyStore = this.getProperty("TrustKeyStore");
      this.customTrustKeyStoreFileName = this.getProperty("CustomTrustKeyStoreFileName");
      this.customTrustKeyStoreType = this.getProperty("CustomTrustKeyStoreType");
      this.customTrustKeyStorePassPhrase = this.getProperty("CustomTrustKeyStorePassPhrase");
      this.javaStandardTrustKeyStorePassPhrase = this.getProperty("JavaStandardTrustKeyStorePassPhrase");
      this.transientScriptEnv = this.getProperty("TransientScriptEnv");
      if (this.transientScriptEnv != null) {
         this.props.remove("TransientScriptEnv");
      }

   }

   public String getJavaVendor() {
      return this.javaVendor;
   }

   public void setJavaVendor(String var1) {
      this.javaVendor = var1;
   }

   public String getJavaHome() {
      return this.javaHome;
   }

   public void setJavaHome(String var1) {
      this.javaHome = var1;
   }

   public String getBeaHome() {
      return this.beaHome;
   }

   public void setBeaHome(String var1) {
      this.beaHome = var1;
   }

   public String getClassPath() {
      return this.classPath;
   }

   public void setClassPath(String var1) {
      this.classPath = var1;
   }

   public String getSecurityPolicyFile() {
      return this.securityPolicyFile;
   }

   public void setSecurityPolicyFile(String var1) {
      this.securityPolicyFile = var1;
   }

   public String getArguments() {
      return this.arguments;
   }

   public void setArguments(String var1) {
      this.arguments = var1;
   }

   public String getSSLArguments() {
      return this.sslArguments;
   }

   public void setSSLArguments(String var1) {
      this.sslArguments = var1;
   }

   public String getAdminURL() {
      return this.adminURL;
   }

   public void setAdminURL(String var1) {
      this.adminURL = var1;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public boolean isAutoRestart() {
      return this.autoRestart;
   }

   public void setAutoRestart(boolean var1) {
      this.autoRestart = var1;
   }

   public boolean isAutoKillIfFailed() {
      return this.autoKillIfFailed;
   }

   public void setAutoKillIfFailed(boolean var1) {
      this.autoKillIfFailed = var1;
   }

   public int getRestartMax() {
      return this.restartMax;
   }

   public void setRestartMax(int var1) {
      this.restartMax = var1;
   }

   public int getRestartInterval() {
      return this.restartInterval;
   }

   public void setRestartInterval(int var1) {
      this.restartInterval = var1;
   }

   public int getRestartDelaySeconds() {
      return this.restartDelaySeconds;
   }

   public void setRestartDelaySeconds(int var1) {
      this.restartDelaySeconds = var1;
   }

   public List<String> getServerIPList() {
      if ((this.serverIPList == null || this.serverIPList.isEmpty()) && this.serverIPs != null) {
         this.serverIPList = this.getIPListFromString(this.serverIPs);
      }

      return this.serverIPList;
   }

   public void setServerIPList(List<String> var1) {
      this.serverIPList = var1;
      this.setServerIPs(this.getServerIPsFromList(this.serverIPList));
   }

   private void setServerIPs(String var1) {
      this.serverIPs = var1;
   }

   private List<String> getIPListFromString(String var1) {
      ArrayList var2 = new ArrayList();
      String[] var3 = var1.split(",");
      String[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var2.add(var7);
      }

      return var2;
   }

   private String getServerIPsFromList(List<String> var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         try {
            InetAddress var5 = InetAddress.getByName(var4);
            var4 = var5.getHostAddress();
         } catch (UnknownHostException var6) {
            var6.printStackTrace();
         }

         var2.append(var4);
         if (var3.hasNext()) {
            var2.append(",");
         }
      }

      return var2.toString();
   }

   public String getUid() {
      return this.serverUid;
   }

   public void setUid(String var1) {
      this.serverUid = var1;
   }

   public String getGid() {
      return this.serverGid;
   }

   public void setGid(String var1) {
      this.serverGid = var1;
   }

   public String getTrustKeyStore() {
      return this.trustKeyStore;
   }

   public void setTrustKeyStore(String var1) {
      this.trustKeyStore = var1;
   }

   public String getCustomTrustKeyStoreFileName() {
      return this.customTrustKeyStoreFileName;
   }

   public void setCustomTrustKeyStoreFileName(String var1) {
      this.customTrustKeyStoreFileName = var1;
   }

   public String getCustomTrustKeyStoreType() {
      return this.customTrustKeyStoreType;
   }

   public void setCustomTrustKeyStoreType(String var1) {
      this.customTrustKeyStoreType = var1;
   }

   public String getCustomTrustKeyStorePassPhrase() {
      return this.customTrustKeyStorePassPhrase;
   }

   public void setCustomTrustKeyStorePassPhrase(String var1) {
      this.customTrustKeyStorePassPhrase = var1;
   }

   public String getJavaStandardTrustKeyStorePassPhrase() {
      return this.javaStandardTrustKeyStorePassPhrase;
   }

   public void setJavaStandardTrustKeyStorePassPhrase(String var1) {
      this.javaStandardTrustKeyStorePassPhrase = var1;
   }

   public Properties getBootProperties() {
      Properties var1 = new Properties();
      if (this.username != null) {
         var1.setProperty("username", this.username);
      }

      if (this.password != null) {
         var1.setProperty("password", this.password);
      }

      if (this.trustKeyStore != null) {
         var1.setProperty("TrustKeyStore", this.trustKeyStore);
      }

      if (this.customTrustKeyStoreFileName != null) {
         var1.setProperty("CustomTrustKeyStoreFileName", this.customTrustKeyStoreFileName);
      }

      if (this.customTrustKeyStoreType != null) {
         var1.setProperty("CustomTrustKeyStoreType", this.customTrustKeyStoreType);
      }

      if (this.customTrustKeyStorePassPhrase != null) {
         var1.setProperty("CustomTrustKeyStorePassPhrase", this.customTrustKeyStorePassPhrase);
      }

      if (this.javaStandardTrustKeyStorePassPhrase != null) {
         var1.setProperty("JavaStandardTrustKeyStorePassPhrase", this.javaStandardTrustKeyStorePassPhrase);
      }

      return var1;
   }

   public Properties getStartupProperties() {
      Properties var1 = new Properties();
      if (this.javaVendor != null) {
         var1.setProperty("JavaVendor", this.javaVendor);
      }

      if (this.javaHome != null) {
         var1.setProperty("JavaHome", this.javaHome);
      }

      if (this.beaHome != null) {
         var1.setProperty("BeaHome", this.beaHome);
      }

      if (this.classPath != null) {
         var1.setProperty("ClassPath", this.classPath);
      }

      if (this.securityPolicyFile != null) {
         var1.setProperty("SecurityPolicyFile", this.securityPolicyFile);
      }

      if (this.arguments != null) {
         var1.setProperty("Arguments", this.arguments);
      }

      if (this.sslArguments != null) {
         var1.setProperty("SSLArguments", this.sslArguments);
      }

      if (this.adminURL != null) {
         var1.setProperty("AdminURL", this.adminURL);
      }

      if (this.serverIPs != null) {
         var1.setProperty("ServerIP", this.serverIPs);
      }

      if (this.nmHostName != null) {
         var1.setProperty("NMHostName", this.nmHostName);
      }

      var1.setProperty("AutoRestart", Boolean.toString(this.autoRestart));
      var1.setProperty("AutoKillIfFailed", Boolean.toString(this.autoKillIfFailed));
      var1.setProperty("RestartMax", Integer.toString(this.restartMax));
      var1.setProperty("RestartInterval", Integer.toString(this.restartInterval));
      var1.setProperty("RestartDelaySeconds", Integer.toString(this.restartDelaySeconds));
      if (this.serverUid != null) {
         var1.setProperty("ServerUID", this.serverUid);
      }

      if (this.serverGid != null) {
         var1.setProperty("ServerGID", this.serverGid);
      }

      return var1;
   }

   public Properties getProperties() {
      Properties var1 = this.getBootProperties();
      var1.putAll(this.getStartupProperties());
      return var1;
   }

   public void setKeyStoreProperties(Properties var1) {
      this.trustKeyStore = var1.getProperty("TrustKeyStore");
      this.customTrustKeyStoreFileName = var1.getProperty("CustomTrustKeyStoreFileName");
      this.customTrustKeyStoreType = var1.getProperty("CustomTrustKeyStoreType");
      ClearOrEncryptedService var2 = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());
      String var3;
      if ((var3 = var1.getProperty("CustomTrustKeyStorePassPhrase")) != null) {
         this.customTrustKeyStorePassPhrase = var2.encrypt(var3);
      }

      if ((var3 = var1.getProperty("JavaStandardTrustKeyStorePassPhrase")) != null) {
         this.javaStandardTrustKeyStorePassPhrase = var2.encrypt(var3);
      }

   }

   private void processArguments() {
      if (this.arguments != null) {
         List var1 = Arrays.asList((Object[])this.arguments.trim().split("\\s"));

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            String var3 = ((String)var1.get(var2)).trim();
            if (var3.length() > 0) {
               String var4;
               if (var3.startsWith("-Dweblogic.Stdout=") && this.serverOutFile == null) {
                  var4 = var3.substring(var3.indexOf("=") + 1);
                  if (var4.startsWith("\"") && var4.endsWith("\"")) {
                     this.serverOutFile = var4.substring(var4.indexOf("\"") + 1, var4.lastIndexOf("\""));
                  } else {
                     this.serverOutFile = var4;
                  }
               } else if (var3.startsWith("-Dweblogic.Stderr=") && this.serverErrFile == null) {
                  var4 = var3.substring(var3.indexOf("=") + 1);
                  if (var4.startsWith("\"") && var4.endsWith("\"")) {
                     this.serverErrFile = var4.substring(var4.indexOf("\"") + 1, var4.lastIndexOf("\""));
                  } else {
                     this.serverErrFile = var4;
                  }
               }
            }
         }
      }

   }

   public String getNMHostName() {
      return this.nmHostName;
   }

   public void setNMHostName(String var1) {
      this.nmHostName = var1;
   }

   public String getServerOutFile() {
      return this.serverOutFile;
   }

   public String getServerErrFile() {
      return this.serverErrFile;
   }

   public String getTransientScriptEnv() {
      return this.transientScriptEnv;
   }
}
