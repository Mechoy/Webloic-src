package weblogic.nodemanager.mbean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import weblogic.coherence.descriptor.wl.CoherenceClusterParamsBean;
import weblogic.coherence.descriptor.wl.CoherenceClusterWellKnownAddressBean;
import weblogic.coherence.descriptor.wl.CoherenceClusterWellKnownAddressesBean;
import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.CoherenceClusterSystemResourceMBean;
import weblogic.management.configuration.CoherenceServerMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ManagedExternalServerMBean;
import weblogic.management.configuration.ManagedExternalServerStartMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.NodeManagerMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.ServerStartMBean;
import weblogic.management.configuration.UnixMachineMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.nodemanager.NMException;
import weblogic.nodemanager.NodeManagerLogger;
import weblogic.nodemanager.ScriptExecutionFailureException;
import weblogic.nodemanager.client.NMClient;
import weblogic.nodemanager.client.ShellClient;
import weblogic.nodemanager.client.VMMClient;
import weblogic.nodemanager.common.CoherenceStartupConfig;
import weblogic.nodemanager.common.ConfigException;
import weblogic.nodemanager.common.ServerType;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.security.SecurityLogger;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.ServerResource;
import weblogic.security.utils.SSLSetup;

public class NodeManagerRuntime {
   private String type = "ssl";
   private String host = "localhost";
   private int port = 5556;
   private String cmd = "VERSION";
   private String nmHome = ".";
   private boolean debug = false;
   private static final String TEMP_FILE_NAME = "nodemanager";
   private static final String TEMP_FILE_EXT = ".tmp";
   private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private MachineMBean myMachine;

   private NodeManagerRuntime() {
   }

   private NodeManagerRuntime(String var1) {
      this.host = var1;
   }

   private NodeManagerRuntime(String var1, int var2, String var3) {
      this.host = var1;
      this.port = var2;
      this.type = var3;
   }

   private NodeManagerRuntime(MachineMBean var1) {
      this.myMachine = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupMachine(var1.getName());
      if (this.myMachine != null) {
         NodeManagerMBean var2 = this.myMachine.getNodeManager();
         if (var2 != null) {
            this.type = var2.getNMType();

            assert this.type != null;

            String var3 = var2.getListenAddress();
            if (var3 != null && var3.trim().length() > 0) {
               this.host = var3;
            }

            int var4 = var2.getListenPort();
            if (var4 > 0) {
               this.port = var4;
            }

            this.cmd = var2.getShellCommand();
            this.nmHome = var2.getNodeManagerHome();
            this.debug = var2.isDebugEnabled();
            this.debug("NodeManagerRuntime created");
         }
      }

   }

   public static NodeManagerRuntime getInstance(String var0, int var1, String var2) {
      return new NodeManagerRuntime(var0, var1, var2);
   }

   public static NodeManagerRuntime getInstance(MachineMBean var0) {
      assert Kernel.isServer();

      return var0 != null ? new NodeManagerRuntime(var0) : new NodeManagerRuntime();
   }

   public static NodeManagerRuntime getInstance(ServerMBean var0) {
      assert Kernel.isServer();

      MachineMBean var1 = var0.getMachine();
      if (var1 != null) {
         return new NodeManagerRuntime(var1);
      } else {
         String var2 = var0.getListenAddress();
         return var2 != null && var2.trim().length() > 0 ? new NodeManagerRuntime(var2.trim()) : new NodeManagerRuntime();
      }
   }

   public static NodeManagerRuntime getInstance(ManagedExternalServerMBean var0) {
      assert Kernel.isServer();

      MachineMBean var1 = var0.getMachine();
      return var1 != null ? new NodeManagerRuntime(var1) : new NodeManagerRuntime();
   }

   public static void checkStartPrivileges(String var0, AuthenticatedSubject var1) throws SecurityException {
      AuthorizationManager var2 = SecurityServiceManager.getAuthorizationManager(kernelId, "weblogicDEFAULT");
      ServerResource var3 = new ServerResource((String)null, var0, "boot");
      if (!var2.isAccessAllowed(var1, var3, (ContextHandler)null)) {
         Loggable var4 = SecurityLogger.logUserNotPermittedToBootLoggable(SubjectUtils.getUsername(var1));
         throw new SecurityException(var4.getMessageText());
      }
   }

   public NodeManagerTask start(ServerMBean var1) throws IOException {
      this.debug(var1.getName(), "Preparing for server startup");
      NMClient var3 = this.getNMClient(var1);
      StartupProperties var4 = NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1);
      Properties var5 = var4.getStartupProperties();
      var5.putAll(var4.getBootProperties());
      if (var3 instanceof VMMClient) {
         ((VMMClient)var3).setVirtualMachineName(var1.getVirtualMachineName());
      }

      String var2 = var1.getName();
      StartRequest var6 = new StartRequest(var2, var3, var5);
      Kernel.execute(var6);
      this.debug(var2, "Server start request task created");
      return var6;
   }

   public NodeManagerTask start(ManagedExternalServerMBean var1) throws IOException {
      this.debug(var1.getName(), "Preparing for server startup");
      NMClient var2 = this.getNMClient(var1);
      StartupProperties var3 = NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1);
      Properties var4 = var3.getStartupProperties();
      var4.putAll(var3.getBootProperties());
      StartRequest var5 = new StartRequest(var1.getName(), var2, var4);
      Kernel.execute(var5);
      this.debug(var1.getName(), "Server start request task created");
      return var5;
   }

   public void kill(ServerMBean var1) throws IOException {
      try {
         NMClient var2 = this.getNMClient(var1);

         try {
            var2.kill();
         } finally {
            var2.done();
         }
      } catch (NMException var9) {
         NodeManagerLogger.logSvrCmdFailedReason("kill", var1.getName(), var9.getMessage());
         throw var9;
      } catch (IOException var10) {
         this.debug(var1.getName(), "Error connecting to NodeManager: " + var10, var10);
         throw var10;
      }

      this.debug(var1.getName(), "Command 'kill' succeeded");
   }

   public void kill(ManagedExternalServerMBean var1) throws IOException {
      try {
         NMClient var2 = this.getNMClient(var1);

         try {
            var2.kill();
         } finally {
            var2.done();
         }
      } catch (NMException var9) {
         NodeManagerLogger.logSvrCmdFailedReason("kill", var1.getName(), var9.getMessage());
         throw var9;
      } catch (IOException var10) {
         this.debug(var1.getName(), "Error connecting to NodeManager: " + var10, var10);
         throw var10;
      }

      this.debug(var1.getName(), "Command 'kill' succeeded");
   }

   public String getStates(boolean var1, int var2) throws IOException {
      String var3;
      try {
         NMClient var4 = this.getNMClient();

         try {
            var3 = var4.getStates(var2);
         } finally {
            var4.done();
         }

         if (var1) {
            StringBuffer var5 = new StringBuffer(var3);
            if (var5.length() > 0 && var5.charAt(var5.length() - 1) != ' ') {
               var5.append(' ');
            }

            ServerMBean[] var6 = ManagementService.getRuntimeAccess(kernelId).getDomain().getServers();
            String var7 = " " + var3;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               ServerMBean var9 = var6[var8];
               ServerStartMBean var10 = var9.getServerStart();
               String var11 = " " + var9.getName() + "=";
               if (var7.indexOf(var11) < 0 && var10 != null && var9.getMachine() != null && this.myMachine != null && var10.getRootDirectory() != null && var9.getMachine().getName().equals(this.myMachine.getName())) {
                  String var12 = this.getState(var9, var2);
                  var5.append(var9.getName());
                  var5.append('=');
                  var5.append(var12);
                  var5.append(' ');
               }
            }

            var3 = var5.toString();
         }
      } catch (NMException var17) {
         NodeManagerLogger.logDebugMsgWithException("getStates", var17);
         throw var17;
      } catch (IOException var18) {
         this.debug("", "Error connecting to NodeManager: " + var18, var18);
         throw var18;
      }

      this.debug("", "Command 'getStates' returned '" + var3 + "'");
      return var3;
   }

   public String getState(ServerMBean var1, int var2) throws IOException {
      String var3;
      try {
         NMClient var4 = this.getNMClient(var1);

         try {
            var3 = var4.getState(var2);
         } finally {
            var4.done();
         }
      } catch (NMException var11) {
         this.debug(var1.getName(), "Error executing STAT on NM " + var11, var11);
         throw var11;
      } catch (IOException var12) {
         this.debug(var1.getName(), "Error connecting to NodeManager: " + var12, var12);
         throw var12;
      }

      this.debug(var1.getName(), "Command 'getState' returned '" + var3 + "'");
      return var3;
   }

   public String getState(ServerMBean var1) throws IOException {
      return this.getState(var1, var1.getNMSocketCreateTimeoutInMillis());
   }

   public String getState(ManagedExternalServerMBean var1, int var2) throws IOException {
      String var3;
      try {
         NMClient var4 = this.getNMClient(var1);

         try {
            var3 = var4.getState(var2);
         } finally {
            var4.done();
         }
      } catch (NMException var11) {
         this.debug(var1.getName(), "Error executing STAT on NM " + var11, var11);
         throw var11;
      } catch (IOException var12) {
         this.debug(var1.getName(), "Error connecting to NodeManager: " + var12, var12);
         throw var12;
      }

      this.debug(var1.getName(), "Command 'getState' returned '" + var3 + "'");
      return var3;
   }

   public String getState(ManagedExternalServerMBean var1) throws IOException {
      return this.getState(var1, var1.getNMSocketCreateTimeoutInMillis());
   }

   public void getLog(ServerMBean var1, Writer var2) throws IOException {
      try {
         NMClient var3 = this.getNMClient(var1);

         try {
            var3.getLog(var2);
         } finally {
            var3.done();
         }
      } catch (NMException var10) {
         NodeManagerLogger.logSvrCmdFailedReason("getLog", var1.getName(), var10.getMessage());
         throw var10;
      } catch (IOException var11) {
         this.debug(var1.getName(), "Error connecting to NodeManager: " + var11, var11);
         throw var11;
      }

      this.debug("Command 'getLog' succeeded");
   }

   public Reader getLog(ServerMBean var1) throws IOException {
      File var2 = null;

      try {
         var2 = this.createTempFile();
      } catch (IOException var12) {
         NodeManagerLogger.logErrorFileCreate("getLog", var1.getName(), "nodemanager.tmp", TEMP_DIR);
         throw var12;
      }

      TempFileReader var3;
      try {
         FileWriter var4 = new FileWriter(var2);

         try {
            this.getLog(var1, var4);
         } finally {
            var4.close();
         }

         var3 = new TempFileReader(var2);
      } catch (IOException var11) {
         this.debug(var1.getName(), "Error connecting to NodeManager: " + var11, var11);
         throw var11;
      }

      this.debug(var1.getName(), "Command 'getLog' succeeded (tmp file is '" + var2 + "'");
      return var3;
   }

   public void getNMLog(Writer var1) throws IOException {
      NMClient var2 = this.getNMClient();

      try {
         try {
            var2.getNMLog(var1);
         } finally {
            var2.done();
         }
      } catch (NMException var9) {
         NodeManagerLogger.logNMCmdFailedReason("getNMLog", var9.getMessage());
         throw var9;
      } catch (IOException var10) {
         this.debug("Error connecting to NodeManager: " + var10, (Throwable)var10);
         throw var10;
      }

      this.debug("Command 'getNMLog' succeeded");
   }

   public Reader getNMLog() throws IOException {
      File var1 = null;

      try {
         var1 = this.createTempFile();
      } catch (IOException var11) {
         NodeManagerLogger.logErrorNMCmdFailedFileCreate("getNMLog", "nodemanager.tmp", TEMP_DIR);
         throw var11;
      }

      TempFileReader var2;
      try {
         FileWriter var3 = new FileWriter(var1);

         try {
            this.getNMLog(var3);
         } finally {
            var3.close();
         }

         var2 = new TempFileReader(var1);
      } catch (IOException var10) {
         this.debug("Error connecting to NodeManager: " + var10, (Throwable)var10);
         throw var10;
      }

      this.debug("Command 'getNMLog' succeeded (tmp file is '" + var1 + "'");
      return var2;
   }

   private File createTempFile() throws IOException {
      File var1 = TEMP_DIR != null ? new File(TEMP_DIR) : null;
      return File.createTempFile("nodemanager", ".tmp", var1);
   }

   public void runScript(File var1, long var2) throws IOException, ScriptExecutionFailureException {
      try {
         NMClient var4 = this.getNMClient();

         try {
            var4.execScript(var1.getPath(), var2);
         } finally {
            var4.done();
         }
      } catch (NMException var11) {
         NodeManagerLogger.logNMCmdFailedReason("runScript", var11.getMessage());
         throw var11;
      } catch (IOException var12) {
         this.debug("Error connecting to NodeManager: " + var12, (Throwable)var12);
         throw var12;
      }

      this.debug("Command 'runScript' succeeded");
   }

   public void updateServerProps(ServerMBean var1) throws IOException {
      StartupProperties var2 = NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1);
      Properties var3 = var2.getStartupProperties();
      var3.putAll(var2.getBootProperties());

      try {
         NMClient var4 = this.getNMClient(var1);

         try {
            var4.updateServerProps(var3);
         } finally {
            var4.done();
         }
      } catch (NMException var11) {
         NodeManagerLogger.logNMCmdFailedReason("updateServerProps", var11.getMessage());
         throw var11;
      } catch (IOException var12) {
         this.debug("Error connecting to NodeManager: " + var12, (Throwable)var12);
         throw var12;
      }

      this.debug("Command 'updateServerProps' succeeded");
   }

   public String getVersion() throws IOException {
      String var1;
      try {
         NMClient var2 = this.getNMClient();

         try {
            var1 = var2.getVersion();
         } finally {
            var2.done();
         }
      } catch (NMException var9) {
         NodeManagerLogger.logNMCmdFailedReason("getVersion", var9.getMessage());
         throw var9;
      } catch (IOException var10) {
         this.debug("Error connecting to NodeManager: " + var10, (Throwable)var10);
         throw var10;
      }

      this.debug("Command 'getVersion' returned '" + var1 + "'");
      return var1;
   }

   private NMClient getNMClient() throws IOException {
      this.debug("NM type is " + this.type);
      NMClient var1 = NMClient.getInstance(this.type);
      var1.setVerbose(this.debug);
      var1.setHost(this.host);
      var1.setPort(this.port);
      if (this.type.equalsIgnoreCase("ssh") || this.type.equalsIgnoreCase("rsh")) {
         if (this.nmHome != null) {
            var1.setNMDir(this.nmHome);
         }

         if (this.cmd != null) {
            ((ShellClient)var1).setShellCommand(this.cmd);
            this.debug("Client shell command is '" + this.cmd + '"');
         }
      }

      var1.setDomainName(ManagementService.getRuntimeAccess(kernelId).getDomainName());

      try {
         if (var1 instanceof VMMClient) {
            if (this.myMachine != null) {
               NodeManagerMBean var2 = this.myMachine.getNodeManager();
               if (var2 != null) {
                  var1.setNMUser(var2.getUserName());
                  var1.setNMPass(var2.getPassword());
                  ((VMMClient)var1).setVmmAdapterName(var2.getAdapterName());
                  ((VMMClient)var1).setVmmAdapterVersion(var2.getAdapterVersion());
               }
            }
         } else {
            DomainMBean var7 = ManagementService.getRuntimeAccess(kernelId).getDomain();
            SecurityConfigurationMBean var3 = var7.getSecurityConfiguration();

            assert var3 != null;

            String var4 = var3.getNodeManagerUsername();
            String var5 = var3.getNodeManagerPassword();
            if (var4 != null && var4.length() > 0 && var5 != null && var5.length() > 0) {
               var1.setNMUser(var4);
               var1.setNMPass(var5);
               this.debug("Node manager username and password specified");
            }
         }

         return var1;
      } catch (Throwable var6) {
         throw new IOException(var6.getLocalizedMessage());
      }
   }

   private NMClient getNMClient(ServerMBean var1) throws IOException {
      NMClient var3 = this.getNMClient();
      ServerStartMBean var4 = var1.getServerStart();
      String var5 = var4.getRootDirectory();
      if (var5 != null) {
         var3.setDomainDir(var5);
         this.debug(var1.getName(), "Root directory for server is '" + var5 + "'");
      }

      String var2 = var1.getName();
      var3.setServerName(var2);
      var3.setServerType(ServerType.WebLogic);

      try {
         if (var3 instanceof VMMClient) {
            ((VMMClient)var3).setVirtualMachineName(var1.getVirtualMachineName());
            MachineMBean var6 = var1.getMachine();
            if (var6 != null) {
               NodeManagerMBean var7 = var6.getNodeManager();
               if (var7 != null) {
                  var3.setNMUser(var7.getUserName());
                  var3.setNMPass(var7.getPassword());
                  ((VMMClient)var3).setVmmAdapterName(var7.getAdapterName());
                  ((VMMClient)var3).setVmmAdapterVersion(var7.getAdapterVersion());
               }
            }
         }

         return var3;
      } catch (Throwable var8) {
         throw new IOException(var8.getLocalizedMessage());
      }
   }

   private NMClient getNMClient(ManagedExternalServerMBean var1) throws IOException {
      NMClient var2 = this.getNMClient();
      ManagedExternalServerStartMBean var3 = var1.getManagedExternalServerStart();
      String var4 = var3.getRootDirectory();
      if (var4 != null) {
         var2.setDomainDir(var4);
         this.debug(var1.getName(), "Root directory for server is '" + var4 + "'");
      }

      var2.setServerName(var1.getName());
      var2.setServerType(ServerType.Coherence);
      return var2;
   }

   public Properties getBootProperties(ServerMBean var1) {
      return NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1).getBootProperties();
   }

   public Properties getBootProperties(ManagedExternalServerMBean var1) {
      return NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1).getBootProperties();
   }

   public Properties getStartupProperties(ServerMBean var1) {
      return NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1).getStartupProperties();
   }

   public Properties getStartupProperties(ManagedExternalServerMBean var1) {
      return NodeManagerRuntime.StartupPropertiesFactory.getStartupProperties(this, var1).getStartupProperties();
   }

   private void debug(String var1) {
      if (this.debug) {
         NodeManagerLogger.logDebugMsg(this.host + ":" + this.port + "> <" + var1);
      }

   }

   private void debug(String var1, Throwable var2) {
      if (this.debug) {
         NodeManagerLogger.logDebugMsgWithException(this.host + ":" + this.port + "> <" + var1, var2);
      }

   }

   private void debug(String var1, String var2) {
      if (this.debug) {
         NodeManagerLogger.logDebugMsg(this.host + ":" + this.port + "> <" + var1 + "> <" + var2);
      }

   }

   private void debug(String var1, String var2, Throwable var3) {
      if (this.debug) {
         NodeManagerLogger.logDebugMsgWithException(this.host + ":" + this.port + "> <" + var1 + "> <" + var2, var3);
      }

   }

   private abstract static class ManagedExternalServerStartupProperties extends AbstractStartupProperties {
      private final ManagedExternalServerMBean smb;

      public ManagedExternalServerStartupProperties(NodeManagerRuntime var1, ManagedExternalServerMBean var2) {
         super(var1);
         this.smb = var2;
      }

      public Properties getStartupProperties() {
         ManagedExternalServerStartMBean var1 = this.smb.getManagedExternalServerStart();

         assert var1 != null;

         StartupConfig var2 = this.createStartupConfig();
         String var3;
         if ((var3 = this.trim(var1.getBeaHome())) != null) {
            var2.setBeaHome(var3);
         }

         if ((var3 = this.trim(var1.getJavaVendor())) != null) {
            var2.setJavaVendor(var3);
         }

         if ((var3 = this.trim(var1.getJavaHome())) != null) {
            var2.setJavaHome(var3);
         }

         if ((var3 = this.trim(var1.getClassPath())) != null) {
            var2.setClassPath(var3);
         }

         if ((var3 = this.trim(var1.getArguments())) != null) {
            var2.setArguments(var3);
         }

         MachineMBean var4 = this.smb.getMachine();
         if (var4 != null && var4 instanceof UnixMachineMBean) {
            UnixMachineMBean var5 = (UnixMachineMBean)var4;
            if (var5.isPostBindUIDEnabled() && (var3 = this.trim(var5.getPostBindUID())) != null) {
               var2.setUid(var3);
            }

            if (var5.isPostBindGIDEnabled() && (var3 = this.trim(var5.getPostBindGID())) != null) {
               var2.setGid(var3);
            }
         }

         DomainMBean var7 = ManagementService.getRuntimeAccess(NodeManagerRuntime.kernelId).getDomain();
         if (!this.smb.getName().equals(var7.getAdminServerName())) {
            var2.setAdminURL(PropertyService.getAdminHttpUrl());
            this.debug("StartupProperties: AdminURL = " + var2.getAdminURL());
         } else {
            this.debug("StartupProperties: AdminURL is not set for Admin server");
         }

         var2.setAutoRestart(this.smb.getAutoRestart());
         var2.setRestartMax(this.smb.getRestartMax());
         var2.setRestartInterval(this.smb.getRestartIntervalSeconds());
         int var6 = this.smb.getRestartDelaySeconds();
         this.debug("StartupProperties: Configured restart delay = " + var6);
         var2.setRestartDelaySeconds(var6);
         return this.getStartupProperties(var2, this.smb.getName());
      }

      public Properties getBootProperties() {
         StartupConfig var1 = this.createStartupConfig();
         ManagedExternalServerStartMBean var2 = this.smb.getManagedExternalServerStart();

         assert var2 != null;

         Properties var3 = var1.getBootProperties();
         if (this.isDebug()) {
            Iterator var4 = var3.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               this.debug(this.smb.getName(), "Server boot property '" + var5.getKey() + "' is '" + var5.getValue() + "'");
            }
         }

         return var3;
      }
   }

   private static class CoherenceServerStartupProperties extends ManagedExternalServerStartupProperties {
      private final CoherenceServerMBean smb;

      public CoherenceServerStartupProperties(NodeManagerRuntime var1, CoherenceServerMBean var2) {
         super(var1, var2);
         this.smb = var2;
      }

      protected StartupConfig createStartupConfig(Properties var1) throws ConfigException {
         return new CoherenceStartupConfig(var1);
      }

      protected StartupConfig createStartupConfig() {
         return new CoherenceStartupConfig();
      }

      protected Properties getStartupProperties(StartupConfig var1, String var2) {
         assert var1 instanceof CoherenceStartupConfig;

         CoherenceStartupConfig var3 = (CoherenceStartupConfig)var1;
         CoherenceClusterSystemResourceMBean var4 = this.smb.getCoherenceClusterSystemResource();
         boolean var5 = false;
         String var6;
         if (var4 != null) {
            if (var4.isUsingCustomClusterConfigurationFile()) {
               var6 = (new File(var4.getCustomClusterConfigurationFileName())).getName();
               var3.setCustomClusterConfigurationFileName(var6);
               var3.setClusterName(var4.getName());
               var5 = true;
            } else {
               WeblogicCoherenceBean var7 = var4.getCoherenceClusterResource();
               if (var7 != null) {
                  this.getStartupProperties(var3, var7.getCoherenceClusterParams());
               }
            }
         }

         if (!var5) {
            if (this.smb.isSet("UnicastListenAddress")) {
               var6 = this.smb.getUnicastListenAddress();
               if (var6 != null) {
                  var3.setUnicastListenAddress(var6);
               }
            }

            if (this.smb.isSet("UnicastListenPort")) {
               int var8 = this.smb.getUnicastListenPort();
               if (var8 != 0) {
                  var3.setUnicastListenPort(var8);
               }
            }

            if (this.smb.isSet("UnicastPortAutoAdjust")) {
               var3.setUnicastPortAutoAdjust(this.smb.isUnicastPortAutoAdjust());
            }
         }

         return super.getStartupProperties(var1, var2);
      }

      private void getStartupProperties(CoherenceStartupConfig var1, CoherenceClusterParamsBean var2) {
         if (var2 != null) {
            CoherenceClusterWellKnownAddressesBean var3 = var2.getCoherenceClusterWellKnownAddresses();
            if (var3 != null) {
               CoherenceClusterWellKnownAddressBean[] var4 = var3.getCoherenceClusterWellKnownAddresses();
               if (var4 != null) {
                  CoherenceClusterWellKnownAddressBean[] var5 = var4;
                  int var6 = var4.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     CoherenceClusterWellKnownAddressBean var8 = var5[var7];
                     var1.addWellKnownAddress(var8.getName(), var8.getListenAddress(), var8.getListenPort());
                     var8.getName();
                  }
               }
            }

            var1.setMulticastListenAddress(var2.getMulticastListenAddress());
            var1.setMulticastListenPort(var2.getMulticastListenPort());
            var1.setTimeToLive(var2.getTimeToLive());
            var1.setUnicastListenAddress(var2.getUnicastListenAddress());
            var1.setUnicastListenPort(var2.getUnicastListenPort());
            var1.setUnicastPortAutoAdjust(var2.isUnicastPortAutoAdjust());
         }

      }
   }

   private static class ServerStartupProperties extends AbstractStartupProperties {
      private final ServerMBean smb;

      public ServerStartupProperties(NodeManagerRuntime var1, ServerMBean var2) {
         super(var1);
         this.smb = var2;
      }

      public Properties getStartupProperties() {
         ServerStartMBean var1 = this.smb.getServerStart();

         assert var1 != null;

         StartupConfig var2 = this.createStartupConfig();
         String var3;
         if ((var3 = this.trim(var1.getBeaHome())) != null) {
            var2.setBeaHome(var3);
         }

         if ((var3 = this.trim(var1.getJavaVendor())) != null) {
            var2.setJavaVendor(var3);
         }

         if ((var3 = this.trim(var1.getJavaHome())) != null) {
            var2.setJavaHome(var3);
         }

         if ((var3 = this.trim(var1.getClassPath())) != null) {
            var2.setClassPath(var3);
         }

         if ((var3 = this.trim(var1.getSecurityPolicyFile())) != null) {
            var2.setSecurityPolicyFile(var3);
         }

         if ((var3 = this.trim(var1.getArguments())) != null) {
            var2.setArguments(var3);
         }

         if ((var3 = this.trim(this.getSSLArguments(this.smb))) != null) {
            var2.setSSLArguments(var3);
         }

         MachineMBean var4 = this.smb.getMachine();
         if (var4 != null && var4 instanceof UnixMachineMBean) {
            UnixMachineMBean var5 = (UnixMachineMBean)var4;
            if (var5.isPostBindUIDEnabled() && (var3 = this.trim(var5.getPostBindUID())) != null) {
               var2.setUid(var3);
            }

            if (var5.isPostBindGIDEnabled() && (var3 = this.trim(var5.getPostBindGID())) != null) {
               var2.setGid(var3);
            }
         }

         String var13 = ManagementService.getRuntimeAccess(NodeManagerRuntime.kernelId).getDomain().getAdminServerName();
         if (!this.smb.getName().equals(var13)) {
            var2.setAdminURL(PropertyService.getAdminHttpUrl());
            this.debug("StartupProperties: AdminURL = " + var2.getAdminURL());
         } else {
            this.debug("StartupProperties: AdminURL is not set for Admin server");
         }

         var2.setAutoRestart(this.smb.getAutoRestart());
         var2.setAutoKillIfFailed(this.smb.getAutoKillIfFailed());
         var2.setRestartMax(this.smb.getRestartMax());
         var2.setRestartInterval(this.smb.getRestartIntervalSeconds());
         int var6 = this.smb.getRestartDelaySeconds();
         this.debug("StartupProperties: Configured restart delay = " + var6);
         ClusterMBean var7;
         int var10;
         if (this.smb.isAutoMigrationEnabled() && (var7 = this.smb.getCluster()) != null) {
            int var8 = var7.getHealthCheckIntervalMillis() / 1000;
            int var9 = var7.getHealthCheckPeriodsUntilFencing();
            var10 = var8 * var9;
            this.debug("StartupProperties: Health Check Interval seconds = " + var8);
            this.debug("StartupProperties: Health Check Period Before Fencing = " + var9);
            if (var10 > 0 && var6 == 0) {
               this.debug("StartupProperties:  Resetting restart delay to " + var10);
               var6 = var10;
            }
         }

         this.debug("StartupProperties:  Restart delay seconds " + var6);
         var2.setRestartDelaySeconds(var6);
         if (this.smb.isAutoMigrationEnabled()) {
            ArrayList var14 = new ArrayList();
            this.prepareIPForList(this.smb.getListenAddress(), var14);
            NetworkAccessPointMBean[] var15 = this.smb.getNetworkAccessPoints();
            var10 = var15.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               NetworkAccessPointMBean var12 = var15[var11];
               this.prepareIPForList(var12.getListenAddress(), var14);
            }

            if (var14.isEmpty()) {
               NodeManagerLogger.logNoIPFoundForMigratableServer();
            }

            var2.setServerIPList(var14);
         }

         return this.getStartupProperties(var2, this.smb.getName());
      }

      private void prepareIPForList(String var1, List<String> var2) {
         InetAddress var3 = null;

         try {
            var3 = InetAddress.getByName(var1);
            var1 = var3.getHostAddress();
         } catch (UnknownHostException var5) {
            NodeManagerLogger.logUnknownMigratableListenAddress(var1, var5.getMessage());
         }

         if (!var2.contains(var1)) {
            var2.add(var1);
         }

      }

      public Properties getBootProperties() {
         StartupConfig var1 = this.createStartupConfig();
         ServerStartMBean var2 = this.smb.getServerStart();

         assert var2 != null;

         String var4 = var2.getUsername();
         String var5 = var2.getPassword();
         if (var4 != null && var4.length() != 0 && var5 != null && var5.length() != 0) {
            ClearOrEncryptedService var6 = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());
            String var3;
            if ((var3 = this.trim(var2.getUsername())) != null) {
               var1.setUsername(var6.encrypt(var3));
            }

            if ((var3 = var2.getPassword()) != null) {
               var1.setPassword(var6.encrypt(var3));
            }
         } else {
            ManagementService.getPropertyService(NodeManagerRuntime.kernelId).establishServerBootIdentity(var1);
         }

         SSLMBean var10 = this.smb.getSSL();
         if ("KeyStores".equals(var10.getIdentityAndTrustLocations())) {
            var1.setKeyStoreProperties(SSLSetup.getSSLTrustProperties(this.smb));
         }

         Properties var7 = var1.getBootProperties();
         if (this.isDebug()) {
            Iterator var8 = var7.entrySet().iterator();

            while(var8.hasNext()) {
               Map.Entry var9 = (Map.Entry)var8.next();
               this.debug(this.smb.getName(), "Server boot property '" + var9.getKey() + "' is '" + var9.getValue() + "'");
            }
         }

         return var7;
      }

      private String getSSLArguments(ServerMBean var1) {
         SSLMBean var2 = var1.getSSL();
         if (var2 == null) {
            return null;
         } else {
            StringBuffer var3 = new StringBuffer();
            var3.append("-Dweblogic.security.SSL.ignoreHostnameVerification=");
            var3.append(var2.isHostnameVerificationIgnored());
            String var4;
            if ((var4 = var2.getHostnameVerifier()) != null) {
               var3.append(" -Dweblogic.security.SSL.hostnameVerifier=");
               var3.append(var4);
            }

            var3.append(" -Dweblogic.ReverseDNSAllowed=");
            var3.append(var1.isReverseDNSAllowed());
            return var3.toString();
         }
      }

      protected StartupConfig createStartupConfig(Properties var1) throws ConfigException {
         return new StartupConfig(var1);
      }

      protected StartupConfig createStartupConfig() {
         return new StartupConfig();
      }
   }

   private abstract static class AbstractStartupProperties implements StartupProperties {
      protected final NodeManagerRuntime nmr;

      AbstractStartupProperties(NodeManagerRuntime var1) {
         this.nmr = var1;
      }

      protected abstract StartupConfig createStartupConfig(Properties var1) throws ConfigException;

      protected abstract StartupConfig createStartupConfig();

      protected Properties getStartupProperties(StartupConfig var1, String var2) {
         Properties var3 = var1.getStartupProperties();
         if (this.isDebug()) {
            Iterator var4 = var3.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               Map.Entry var6 = (Map.Entry)var5;
               this.debug(var2, "Server start property '" + var6.getKey() + "' is '" + var6.getValue() + "'");
            }
         }

         return var3;
      }

      protected void debug(String var1) {
         this.nmr.debug(var1);
      }

      protected void debug(String var1, String var2) {
         this.nmr.debug(var1, var2);
      }

      protected String trim(String var1) {
         if (var1 != null) {
            var1 = var1.trim();
            if (var1.length() > 0) {
               return var1;
            }
         }

         return null;
      }

      protected boolean isDebug() {
         return this.nmr.debug;
      }
   }

   private static class StartupPropertiesFactory {
      static StartupProperties getStartupProperties(NodeManagerRuntime var0, ServerMBean var1) {
         return new ServerStartupProperties(var0, var1);
      }

      static StartupProperties getStartupProperties(NodeManagerRuntime var0, ManagedExternalServerMBean var1) {
         if (var1 instanceof CoherenceServerMBean) {
            return new CoherenceServerStartupProperties(var0, (CoherenceServerMBean)var1);
         } else {
            throw new IllegalStateException("Only Coherence ManagedExternalServerMBean supported");
         }
      }
   }

   interface StartupProperties {
      Properties getStartupProperties();

      Properties getBootProperties();
   }
}
