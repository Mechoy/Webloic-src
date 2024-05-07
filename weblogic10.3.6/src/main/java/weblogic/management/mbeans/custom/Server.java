package weblogic.management.mbeans.custom;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import weblogic.logging.LoggingConfigurationProcessor;
import weblogic.logging.MessageLogger;
import weblogic.logging.Severities;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.CoherenceClusterSystemResourceMBean;
import weblogic.management.configuration.DomainLogFilterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.protocol.ProtocolManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;

public final class Server extends Kernel {
   private static final DebugCategory DEBUG_DEPLOYER = Debug.getCategory("weblogic.deployer");
   private static final long serialVersionUID = -2191431493098472191L;
   private static final long READ_DELAY = 250L;
   private static final long READ_MAX_DELAY_COUNT = 120L;
   private static final boolean DEBUG = false;
   private String activeDir = null;
   private transient ClusterMBean cluster;
   private String stageDir;
   private DomainLogFilterMBean domainLogFilter;
   private boolean enabledForDomainLog = true;
   private String startupMode;
   private transient CoherenceClusterSystemResourceMBean coherenceClusterSysResMBean;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String uploadDir;
   private int threadPoolSize = 5;
   private int adminPort;
   private boolean messageIdPrefixEnabled;

   private ServerMBean getServer() {
      return (ServerMBean)this.getMbean();
   }

   public Server(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public static String getNameOfDefaultMigratableTargetFor(ServerMBean var0) {
      return var0.getName() + " (migratable)";
   }

   public Reader getLogs(String var1) {
      String var2 = this.getServer().getName();
      if (var2 != null && !var2.equals("")) {
         NodeManagerRuntime var7 = NodeManagerRuntime.getInstance(this.getServer());

         try {
            return var7.getLog(this.getServer());
         } catch (IOException var6) {
            String var5 = "Could not get logs for server '" + var2 + "' via Node Manager - reason: " + var6.getMessage();
            return this.printError(var5);
         }
      } else {
         String var3 = "Could not get logs for server '" + var2 + "' via Node Manager - reason: 'Server name is not set'";
         return this.printError(var3);
      }
   }

   public String getStagingDirectoryName() {
      if (this.stageDir == null) {
         this.stageDir = this.getDefaultStagingDirName();
         if (DEBUG_DEPLOYER.isEnabled()) {
            Debug.say(this.getMbean().getName() + "\nusing " + this.stageDir + " as staging directory, my name is : " + this.getMbean().getName());
         }
      }

      return this.stageDir;
   }

   public void setStagingDirectoryName(String var1) {
      this.stageDir = var1;
   }

   public String getUploadDirectoryName() {
      String var1 = this.getMbean().getName();
      if (this.uploadDir == null && var1 != null) {
         this.uploadDir = DomainDir.getPathRelativeServerDirNonCanonical(var1, "upload");
      }

      return this.uploadDir;
   }

   public void setUploadDirectoryName(String var1) {
      this.uploadDir = var1;
   }

   public String[] getSupportedProtocols() {
      return ProtocolManager.getProtocols();
   }

   public boolean isEnabledForDomainLog() {
      if (this.isDelegateModeEnabled()) {
         return Severities.severityStringToNum(this.getServer().getLog().getDomainLogBroadcastSeverity()) > 0;
      } else {
         return this.enabledForDomainLog;
      }
   }

   public void setEnabledForDomainLog(boolean var1) {
      this.enabledForDomainLog = var1;
      if (this.isDelegateModeEnabled()) {
         ServerMBean var2 = this.getServer();
         LoggingConfigurationProcessor.upgradeDomainLogFilterEnabled(var2);
      }

   }

   public boolean getEnabledForDomainLog() {
      String var1 = this.getServer().getLog().getDomainLogBroadcastSeverity();
      int var2 = Severities.severityStringToNum(var1);
      return var2 > 0;
   }

   public DomainLogFilterMBean getDomainLogFilter() {
      if (!this.isDelegateModeEnabled()) {
         return this.domainLogFilter;
      } else {
         LogFilterMBean var1 = this.getServer().getLog().getDomainLogBroadcastFilter();
         if (var1 != null) {
            DomainMBean var2 = (DomainMBean)this.getMbean().getParent();
            DomainLogFilterMBean var3 = var2.lookupDomainLogFilter(var1.getName());
            return var3;
         } else {
            return null;
         }
      }
   }

   public void setDomainLogFilter(DomainLogFilterMBean var1) {
      this.domainLogFilter = var1;
      if (this.isDelegateModeEnabled()) {
         ServerMBean var2 = this.getServer();
         DomainMBean var3 = (DomainMBean)var2.getParent();
         LoggingConfigurationProcessor.applyDomainLogFilterToLogMBean(var3, var2.getLog(), this.domainLogFilter);
      }

   }

   public void setThreadPoolSize(int var1) {
      this.threadPoolSize = var1;
   }

   public int getThreadPoolSize() {
      return this.threadPoolSize;
   }

   public void setActiveDirectoryName(String var1) {
      this.activeDir = var1;
   }

   public String getActiveDirectoryName() {
      if (this.activeDir == null) {
         this.activeDir = this.getStagingDirectoryName();
      }

      return this.activeDir;
   }

   public void setCluster(ClusterMBean var1) {
      ClusterMBean var2 = this.cluster;
      this.cluster = var1;
      if (var1 != null || var2 != null) {
         if (var1 != null && var2 != null) {
            if (var1.getName().equals(var2.getName())) {
               return;
            }

            this.deleteDefaultMigratableTarget();
            this.createDefaultMigratableTarget();
         } else if (var1 == null && var2 != null) {
            this.deleteDefaultMigratableTarget();
            this.getMbean().unSet("Cluster");
         } else if (var1 != null && var2 == null) {
            this.createDefaultMigratableTarget();
         }

      }
   }

   public ClusterMBean getCluster() {
      return this.cluster;
   }

   public void setAdministrationPort(int var1) {
      this.adminPort = var1;
   }

   public int getAdministrationPort() {
      return this.adminPort;
   }

   public void setMessageIdPrefixEnabled(boolean var1) {
      MessageLogger.setUsePrefix(var1);
      this.messageIdPrefixEnabled = var1;
   }

   public boolean getMessageIdPrefixEnabled() {
      return this.messageIdPrefixEnabled;
   }

   public String synchronousStart() {
      return this.getStringFromReader(this.start());
   }

   private Reader start() {
      String var1 = this.getServer().getName();
      if (var1 != null && !var1.equals("")) {
         try {
            NodeManagerRuntime.checkStartPrivileges(var1, SecurityServiceManager.getCurrentSubject(kernelId));
         } catch (SecurityException var6) {
            return this.printError(var6.getMessage());
         }

         NodeManagerRuntime var7 = NodeManagerRuntime.getInstance(this.getServer());

         try {
            var7.start(this.getServer());
            return new StringReader("Server '" + var1 + "' started");
         } catch (IOException var5) {
            String var4 = "Could not start server '" + var1 + "' via Node Manager - reason: " + var5.getMessage();
            return this.printError(var4);
         }
      } else {
         String var2 = "Could not start server '" + var1 + "' via Node Manager - reason: 'Server name is not set'";
         return this.printError(var2);
      }
   }

   public String synchronousKill() {
      return this.getStringFromReader(this.kill());
   }

   private Reader kill() {
      String var1 = this.getServer().getName();
      if (var1 != null && !var1.equals("")) {
         NodeManagerRuntime var6 = NodeManagerRuntime.getInstance(this.getServer());

         try {
            var6.kill(this.getServer());
            return new StringReader("Server killed");
         } catch (IOException var5) {
            String var4 = "Could not kill server '" + var1 + "' via Node Manager - reason: " + var5.getMessage();
            return this.printError(var4);
         }
      } else {
         String var2 = "Could not start server '" + var1 + "' via Node Manager - reason: 'Server name is not set'";
         return this.printError(var2);
      }
   }

   public ServerLifeCycleRuntimeMBean lookupServerLifeCycleRuntime() {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         DomainAccess var1 = ManagementService.getDomainAccess(kernelId);
         return var1.lookupServerLifecycleRuntime(this.getMbean().getName());
      } else {
         return null;
      }
   }

   private String getStringFromReader(Reader var1) {
      StringBuffer var2 = new StringBuffer();

      try {
         BufferedReader var3 = null;
         var3 = new BufferedReader(var1);
         String var4 = null;
         boolean var5 = true;
         int var6 = 0;

         while(var5) {
            while((var4 = var3.readLine()) != null) {
               var6 = 0;
               var2.append(var4);
               var2.append("\n");
            }

            ++var6;
            Thread.sleep(250L);
            if ((long)var6 == 120L) {
               var5 = false;
            }
         }

         var3.close();
      } catch (Exception var7) {
         var2.append(StackTraceUtils.throwable2StackTrace(var7));
      }

      return var2.toString();
   }

   private Reader printError(String var1) {
      ManagementLogger.logNodeManagerError(var1);
      return new StringReader("Error: " + var1);
   }

   private void createDefaultMigratableTarget() {
      ServerMBean var1 = (ServerMBean)this.getMbean();
      DomainMBean var2 = (DomainMBean)var1.getParent();
      MigratableTargetConfigProcessor.createDefaultMigratableTargets(var2, var1);
   }

   private void deleteDefaultMigratableTarget() {
      this.getServer().destroyJTAMigratableTarget();
      this.getMbean().unSet("JTAMigratableTarget");
      MigratableTargetConfigProcessor.destroyDefaultMigratableTarget(this.getServer());
   }

   public Set getServerNames() {
      HashSet var1 = new HashSet(1);
      var1.add(this.getMbean().getName());
      return var1;
   }

   public String getRootDirectory() {
      return DomainDir.getRootDir();
   }

   public String getDefaultStagingDirName() {
      String var1 = this.getMbean().getName();
      return var1 == null ? null : DomainDir.getPathRelativeServerDir(var1, "stage");
   }

   public String get81StyleDefaultStagingDirName() {
      String var1 = this.getMbean().getName();
      return var1 == null ? null : DomainDir.getPathRelativeRootDir(var1 + File.separator + "stage");
   }

   public void setStartupMode(String var1) {
      this.startupMode = var1;
   }

   public String getStartupMode() {
      String var1 = System.getProperty("weblogic.management.startupMode");
      return var1 != null ? var1 : this.startupMode;
   }

   public void setCoherenceClusterSystemResource(CoherenceClusterSystemResourceMBean var1) {
      this.coherenceClusterSysResMBean = var1;
   }

   public CoherenceClusterSystemResourceMBean getCoherenceClusterSystemResource() {
      return this.coherenceClusterSysResMBean;
   }
}
