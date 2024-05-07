package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.common.ServerType;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.nodemanager.util.ConcurrentFile;
import weblogic.nodemanager.util.ProcessControl;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

abstract class AbstractServerManager implements ServerManagerI {
   protected final DomainManager domainMgr;
   protected final String serverName;
   private final ServerDir serverDir;
   private final String[] logParams;
   protected ServerMonitorI monitor;
   private boolean recoveryNeeded;
   private StartupConfig conf = null;
   protected boolean domainDirShared;
   private static final Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   protected static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public AbstractServerManager(DomainManager var1, String var2, ServerType var3) throws ConfigException, IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("DomainManager null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("Server name null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("ServerType null");
      } else {
         this.domainMgr = var1;
         this.serverName = var2;
         this.serverDir = var1.getDomainDir().getServerDir(this.serverName, var3);
         this.logParams = new String[]{var1.getDomainName(), this.serverName};
         this.loadStartupConfig();
         this.initialize();
      }
   }

   protected abstract ServerMonitorI createServerMonitor(StartupConfig var1);

   protected abstract boolean canPingServer(ServerDir var1);

   protected abstract boolean isCrashRecoveryNeeded(StartupConfig var1) throws IOException;

   protected abstract List<WLSProcess.ExecuteCallbackHook> getStopCallbacks(StartupConfig var1) throws IOException;

   protected abstract List<WLSProcess.ExecuteCallbackHook> getStartCallbacks(StartupConfig var1) throws IOException;

   protected abstract StartupConfig createStartupConfig(Properties var1) throws ConfigException;

   private void initialize() throws ConfigException, IOException {
      NMServerConfig var1 = this.domainMgr.getNMServer().getConfig();
      ProcessControl var2 = var1.getProcessControl();
      ConcurrentFile var3 = this.serverDir.getLockFile();
      boolean var4 = var3.exists();
      this.log(Level.FINEST, " Lock file " + var3 + " exists:" + var4);
      if (var2 != null && var4) {
         String var5 = var3.readLine();
         this.log(Level.FINEST, " Checking if process from lock file is running :" + var5);
         if (var2.isProcessAlive(var5) && this.canPingServer(this.serverDir)) {
            this.log(Level.INFO, nmText.msgMonitoringServer(var5));
            this.log(Level.INFO, " Initializing ServerMonitor for " + this + " : with config : " + this.conf);
            this.setServerOutAndErrFiles();
            this.monitor = this.createServerMonitor(this.conf);
            this.monitor.setPreStartHooks(this.getStartCallbacks(this.conf));
            this.monitor.setPostStopHooks(this.getStopCallbacks(this.conf));
            this.monitor.start(var5);
         } else if (var1.isCrashRecoveryEnabled()) {
            this.setServerOutAndErrFiles();
            this.recoveryNeeded = this.isCrashRecoveryNeeded(this.conf);
         } else {
            var3.delete();
         }
      }

   }

   Thread createAndStartMonitor(WLSProcess var1) throws IOException {
      synchronized(this) {
         if (this.monitor != null && !this.monitor.isFinished()) {
            throw new IOException(nmText.getServerAlreadyRunning());
         } else {
            this.monitor = this.createServerMonitor(this.conf);
            return this.monitor.startMonitor(var1);
         }
      }
   }

   public void recoverServer() throws ConfigException, IOException {
      if (this.recoveryNeeded) {
         this.log(Level.INFO, nmText.getRecoveringServerProcess());
         this.loadStartupConfig();
         this.startServer();
         this.recoveryNeeded = false;
      } else {
         if (this.domainDirShared) {
            return;
         }

         if (this.monitor != null) {
            return;
         }

         ServerMonitorI var1 = this.createServerMonitor(this.conf);
         if (var1.isCleanupAfterCrashNeeded()) {
            var1.cleanup(this.getStopCallbacks(this.conf));
         }
      }

   }

   public void start(Properties var1) throws IllegalStateException, InterruptedException, ConfigException, IOException {
      synchronized(this) {
         if (this.monitor != null && !this.monitor.isFinished()) {
            throw new IllegalStateException(nmText.getServerAlreadyRunning());
         }

         this.makeDir(this.serverDir.getLogsDir());
         this.makeDir(this.serverDir.getSecurityDir());
         this.makeDir(this.serverDir.getNMDataDir());
         this.makeDir(this.serverDir.getTmpDir());
         synchronized(this.domainMgr) {
            this.makeDir(this.serverDir.getDomainBakDir());
            this.makeDir(this.serverDir.getConfigPrevDir());
         }

         if (var1 != null) {
            this.conf = this.saveStartupConfig(var1);
         }

         if (this.conf == null) {
            this.loadStartupConfig();
         }

         this.setServerOutAndErrFiles();
         this.startServer();
      }

      synchronized(this.monitor) {
         while(!this.monitor.isStarted() && !this.monitor.isFinished()) {
            this.monitor.wait();
         }

         if (!this.monitor.isStarted() || this.monitor.isStartupAborted()) {
            throw new IOException(nmText.getServerFailedToStart());
         }
      }
   }

   protected void startServer() throws IOException {
      LogFileRotationUtil.rotateServerFiles(this, this.conf);
      this.monitor = this.createServerMonitor(this.conf);
      this.monitor.setPreStartHooks(this.getStartCallbacks(this.conf));
      this.monitor.setPostStopHooks(this.getStopCallbacks(this.conf));
      this.monitor.start();
   }

   private void makeDir(File var1) throws IOException {
      Class var2 = ServerManager.class;
      synchronized(ServerManager.class) {
         if (!var1.isDirectory()) {
            this.log(Level.INFO, nmText.getCreatingDirectory(var1.getPath()));
            if (!var1.mkdirs()) {
               throw new IOException(nmText.getErrorCreatingDirectory(var1.getPath()));
            }
         }

      }
   }

   protected StartupConfig loadStartupConfig() throws ConfigException, IOException {
      Properties var1 = new Properties();
      File var2 = this.serverDir.getStartupConfigFile();
      if (var2.exists()) {
         FileInputStream var3 = new FileInputStream(var2);

         try {
            var1.load(var3);
         } finally {
            var3.close();
         }

         this.log(Level.INFO, nmText.getStartupPropertiesLoaded(var2.getPath()));
      }

      this.conf = this.createStartupConfig(var1);
      return this.conf;
   }

   public StartupConfig saveStartupConfig(Properties var1) throws ConfigException, IOException {
      this.saveBootIdentity(var1);
      if (this.domainMgr.getNMServer().getConfig().isDomainsDirRemoteSharingEnabled()) {
         var1.put("NMHostName", this.getNMHostName());
      }

      this.conf = this.createStartupConfig(var1);
      File var2 = this.serverDir.getStartupConfigFile();
      FileOutputStream var3 = new FileOutputStream(var2);

      try {
         var1.store(var3, "Server startup properties");
      } finally {
         var3.close();
      }

      this.log(Level.INFO, nmText.getStartupPropertiesSaved(var2.getPath()));
      return this.conf;
   }

   private void saveBootIdentity(Properties var1) throws IOException {
      String var2 = (String)var1.remove("username");
      if (var2 == null) {
         var2 = (String)var1.remove("Username");
      }

      String var3 = (String)var1.remove("password");
      if (var3 == null) {
         var3 = (String)var1.remove("Password");
      }

      ClearOrEncryptedService var4 = this.domainMgr.getEncryptor();
      Properties var5 = new Properties();
      if (var2 != null && var3 != null) {
         var5.setProperty("username", var4.encrypt(var2));
         var5.setProperty("password", var4.encrypt(var3));
         String var6 = (String)var1.remove("TrustKeyStore");
         if (var6 != null) {
            var5.setProperty("TrustKeyStore", var6);
         }

         var6 = (String)var1.remove("CustomTrustKeyStoreFileName");
         if (var6 != null) {
            var5.setProperty("CustomTrustKeyStoreFileName", var6);
         }

         var6 = (String)var1.remove("CustomTrustKeyStoreType");
         if (var6 != null) {
            var5.setProperty("CustomTrustKeyStoreType", var6);
         }

         var6 = (String)var1.remove("CustomTrustKeyStorePassPhrase");
         if (var6 != null) {
            var5.setProperty("CustomTrustKeyStorePassPhrase", var6);
         }

         var6 = (String)var1.remove("JavaStandardTrustKeyStorePassPhrase");
         if (var6 != null) {
            var5.setProperty("JavaStandardTrustKeyStorePassPhrase", var6);
         }

         File var7 = this.serverDir.getNMBootIdentityFile();
         FileOutputStream var8 = new FileOutputStream(var7);

         try {
            var5.store(var8, (String)null);
         } finally {
            var8.close();
         }

         this.log(Level.INFO, nmText.getBootIdentitySaved(var7.getPath()));
      }

   }

   public synchronized String getState() throws IOException {
      return this.monitor != null ? this.monitor.getStateInfo().getState() : null;
   }

   public synchronized void kill() throws IOException, InterruptedException {
      if (this.monitor != null && !this.monitor.isFinished()) {
         this.monitor.kill();
      } else {
         throw new IllegalStateException(nmText.getServerNotRunning());
      }
   }

   void resetCredentials(String var1, String var2) throws IOException, ConfigException {
      if (this.conf == null) {
         this.loadStartupConfig();
      }

      Properties var3 = this.conf.getBootProperties();
      var3.setProperty("username", var1);
      var3.setProperty("password", var2);
      this.saveBootIdentity(var3);
   }

   public DomainManager getDomainManager() {
      return this.domainMgr;
   }

   public String getServerName() {
      return this.serverName;
   }

   public ServerDir getServerDir() {
      return this.serverDir;
   }

   public void log(Level var1, String var2, Throwable var3) {
      if (nmLog.isLoggable(var1)) {
         LogRecord var4 = new LogRecord(var1, var2);
         var4.setParameters(this.logParams);
         if (var3 != null) {
            var4.setThrown(var3);
         }

         nmLog.log(var4);
      }
   }

   protected void log(Level var1, String var2) {
      this.log(var1, var2, (Throwable)null);
   }

   protected String getNMHostName() {
      String var1 = this.domainMgr.getNMServer().getConfig().getListenAddress();

      try {
         if (var1 != null && !var1.equals("localhost") && !var1.equals("127.0.0.1")) {
            var1 = InetAddress.getByName(var1).getCanonicalHostName();
         } else {
            var1 = InetAddress.getLocalHost().getCanonicalHostName();
         }
      } catch (Exception var3) {
      }

      return var1;
   }

   private void setServerOutAndErrFiles() {
      String var1 = this.conf.getServerOutFile();
      if (var1 != null) {
         this.serverDir.setOutFile(var1);
      }

      String var2 = this.conf.getServerErrFile();
      if (var2 != null) {
         this.serverDir.setErrFile(var2);
      }

   }
}
