package weblogic.diagnostics.snmp.server;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.MBeanServerSubAgentX;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPLocalAgentTrapSender;
import weblogic.diagnostics.snmp.agent.SNMPProxyManager;
import weblogic.diagnostics.snmp.agent.SNMPTrapDestination;
import weblogic.diagnostics.snmp.agent.SNMPTrapSender;
import weblogic.diagnostics.snmp.agent.SNMPTrapUtil;
import weblogic.diagnostics.snmp.agent.SNMPV3Agent;
import weblogic.diagnostics.snmp.agent.SNMPV3AgentToolkit;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;
import weblogic.diagnostics.snmp.mib.SNMPExtensionProvider;
import weblogic.diagnostics.snmp.mib.SNMPExtensionProviderHelper;
import weblogic.diagnostics.snmp.mib.WLSMibMetadata;
import weblogic.diagnostics.snmp.mib.WLSMibMetadataException;
import weblogic.diagnostics.snmp.muxer.ProtocolHandlerSNMP;
import weblogic.jndi.Environment;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.SNMPAgentDeploymentMBean;
import weblogic.management.configuration.SNMPAgentMBean;
import weblogic.management.configuration.SNMPAttributeChangeMBean;
import weblogic.management.configuration.SNMPCounterMonitorMBean;
import weblogic.management.configuration.SNMPGaugeMonitorMBean;
import weblogic.management.configuration.SNMPLogFilterMBean;
import weblogic.management.configuration.SNMPProxyMBean;
import weblogic.management.configuration.SNMPStringMonitorMBean;
import weblogic.management.configuration.SNMPTrapDestinationMBean;
import weblogic.management.configuration.SNMPValidator;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class SNMPAgentDeploymentHandler implements DeploymentHandler, BeanUpdateListener {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String SNMP_DATA_FILE = "snmp/snmp.dat";
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final String DEFAULT_MASTER_AGENTX_HOST = "localhost";
   private static final int DEFAULT_MASTER_AGENTX_PORT = 705;
   static final String CUSTOM_MBEANS_SUB_AGENT_ID = "1.2.3.4.5.6";
   private static final String CUSTOM_MBEANS_MODULE_OID = "1.3.6.1.4.1.140.625.50";
   private static final String PROTOCOL = "wlx";
   private static final String JNDI = "/jndi/";
   private static final String RUNTIME_URI = "weblogic.management.mbeanservers.runtime";
   private static final String DOMAIN_RUNTIME_URI = "weblogic.management.mbeanservers.domainruntime";
   static final int AGENT_STOPPED = 0;
   static final int AGENT_STOPPING = 1;
   static final int AGENT_STARTING = 2;
   static final int AGENT_RUNNING = 3;
   private static final SNMPAgentDeploymentHandler SINGLETON = new SNMPAgentDeploymentHandler();
   private boolean snmpServiceStarted = false;
   private SNMPAgentMBean snmpAgentConfig;
   private SNMPAgentDeploymentMBean targettedAgentConfig;
   private SNMPAgentMBean domainAgentConfig;
   private SNMPV3Agent snmpAgent;
   private List<WLSMibMetadata> wlsMibMetadataList = new ArrayList();
   private MBeanServerConnection mbeanServerConnection;
   private MBeanRegHandler regHandler = null;
   private boolean adminServer;
   private String serverName;
   private String domainName;
   private int serverCount;
   private String listenAddress;
   private int tcpListenPort;
   private List jmxMonitorLifecycleList = new LinkedList();
   private MBeanServerSubAgentX customMBeansSubAgent;
   private int agentState = 0;
   private SNMPRuntimeStats snmpRuntimeStats = new SNMPRuntimeStats();
   private List<SNMPExtensionProvider> snmpExtensionProviders = new ArrayList();

   public static SNMPAgentDeploymentHandler getInstance() {
      return SINGLETON;
   }

   private SNMPAgentDeploymentHandler() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(KERNEL_ID);
      this.adminServer = var1.isAdminServer();
      this.serverName = var1.getServerName();
      this.domainName = var1.getDomainName();
      ServerMBean var2 = var1.getServer();
      this.listenAddress = var2.getListenAddress();
      this.tcpListenPort = var2.getListenPort();
      this.serverCount = var1.getDomain().getServers().length;
   }

   public boolean isAgentRunning() {
      return this.agentState == 3;
   }

   public boolean isAgentStarting() {
      return this.agentState == 2;
   }

   public boolean isAgentStopping() {
      return this.agentState == 1;
   }

   public boolean isAgentStopped() {
      return this.agentState == 0;
   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof SNMPAgentDeploymentMBean) {
         SNMPAgentDeploymentMBean var3 = (SNMPAgentDeploymentMBean)var1;
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Preparing SNMPAgentDeploymentMBean " + var3.getName());
         }
      }

   }

   private String getSNMPDataFileName() {
      return DomainDir.getPathRelativeServersDataDir(this.serverName, "snmp/snmp.dat");
   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof SNMPAgentDeploymentMBean) {
         SNMPAgentDeploymentMBean var3 = (SNMPAgentDeploymentMBean)var1;
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Activating SNMPAgentDeploymentMBean " + var3.getName());
         }

         try {
            this.stopSNMPAgent();
            this.setTargettedAgentConfig(var3);
            this.activateSNMPAgent();
            this.registerBeanUpdateListener(var3);
         } catch (Throwable var7) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Caught exception during startup, shutting down agent", var7);
            }

            SNMPLogger.logAgentInitFailed();

            try {
               this.stopSNMPAgent();
            } catch (Throwable var6) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Exception occurred shutting down agent", var6);
               }
            }

            throw new DeploymentException(var7);
         }
      }

   }

   public synchronized void activateSNMPAgent() throws Exception {
      try {
         this.initializeSNMPAgentConfig();
         if (this.snmpServiceStarted) {
            this.startSNMPAgent();
         }
      } catch (Exception var2) {
         SNMPLogger.logSNMPServiceFailure(var2);
      }

   }

   private void initializeJMXMonitorLifecycleList() {
      this.jmxMonitorLifecycleList = new LinkedList();
      ServerStateLifecycle var1 = new ServerStateLifecycle(this.adminServer, this.serverName, this.snmpAgent, this.mbeanServerConnection);
      this.jmxMonitorLifecycleList.add(var1);
      this.jmxMonitorLifecycleList.add(new MBeanAttributeChangeLifecycle(this.adminServer, this.serverName, this.snmpAgent, this.mbeanServerConnection));
      this.jmxMonitorLifecycleList.add(new GaugeMonitorLifecycle(this.adminServer, this.serverName, this.snmpAgent, this.mbeanServerConnection));
      this.jmxMonitorLifecycleList.add(new CounterMonitorLifecycle(this.adminServer, this.serverName, this.snmpAgent, this.mbeanServerConnection));
      this.jmxMonitorLifecycleList.add(new StringMonitorLifecycle(this.adminServer, this.serverName, this.snmpAgent, this.mbeanServerConnection));
      LogFilterLifecycle var2 = new LogFilterLifecycle(this.adminServer, this.serverName, this.snmpAgent, this.mbeanServerConnection);
      this.jmxMonitorLifecycleList.add(var2);
      var1.setLogFilterLifecycle(var2);
   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      try {
         if (var1 instanceof SNMPAgentDeploymentMBean) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Deactivating SNMPAgentDeploymentMBean " + var1.getName());
            }

            SNMPAgentDeploymentMBean var3 = (SNMPAgentDeploymentMBean)var1;
            this.stopSNMPAgent();
            this.setTargettedAgentConfig((SNMPAgentDeploymentMBean)null);
            this.deregisterBeanUpdateListener(var3);
         }

      } catch (Exception var4) {
         throw new UndeploymentException(var4);
      }
   }

   private synchronized void stopSNMPAgent() throws Exception {
      if (this.snmpAgent != null && this.snmpServiceStarted) {
         this.setAgentState(1);
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Stopping SNMP Agent...");
         }

         if (this.regHandler != null) {
            this.regHandler.deregister();
            this.regHandler = null;
         }

         this.deregisterMonitorListeners();
         ProtocolHandlerSNMP.getSNMPProtocolHandler().setAgent((SNMPV3Agent)null);
         if (this.customMBeansSubAgent != null) {
            this.customMBeansSubAgent.deleteAllSNMPTableRows();
            this.customMBeansSubAgent.shutdown();
         }

         this.snmpAgent.getSNMPAgentToolkit().stopSNMPAgent();
         this.snmpRuntimeStats.setRunning(false);
         this.snmpRuntimeStats.setSNMPAgentName((String)null);
         this.snmpRuntimeStats.setSNMPAgentToolkit((SNMPV3AgentToolkit)null);
         this.snmpAgentConfig = null;
         this.snmpAgent = null;
         this.customMBeansSubAgent = null;
         this.jmxMonitorLifecycleList = null;
         this.wlsMibMetadataList.clear();
         if (!this.adminServer) {
            SNMPTrapUtil.getInstance().setSNMPTrapSender(new SNMPAdminServerTrapSender());
         } else {
            SNMPTrapUtil.getInstance().setSNMPTrapSender((SNMPTrapSender)null);
         }

         this.setAgentState(0);
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("SNMP Agent stopped");
         }

         SNMPLogger.logAgentShutdownComplete();
      }

   }

   private void deregisterBeanUpdateListener(SNMPAgentMBean var1) {
      var1.removeBeanUpdateListener(this);
      SNMPAttributeChangeMBean[] var2 = var1.getSNMPAttributeChanges();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3].removeBeanUpdateListener(this);
         }
      }

      SNMPCounterMonitorMBean[] var10 = var1.getSNMPCounterMonitors();
      if (var10 != null) {
         for(int var4 = 0; var4 < var10.length; ++var4) {
            var10[var4].removeBeanUpdateListener(this);
         }
      }

      SNMPGaugeMonitorMBean[] var11 = var1.getSNMPGaugeMonitors();
      if (var11 != null) {
         for(int var5 = 0; var5 < var11.length; ++var5) {
            var11[var5].removeBeanUpdateListener(this);
         }
      }

      SNMPStringMonitorMBean[] var12 = var1.getSNMPStringMonitors();
      if (var12 != null) {
         for(int var6 = 0; var6 < var12.length; ++var6) {
            var12[var6].removeBeanUpdateListener(this);
         }
      }

      SNMPLogFilterMBean[] var13 = var1.getSNMPLogFilters();
      if (var13 != null) {
         for(int var7 = 0; var7 < var13.length; ++var7) {
            var13[var7].removeBeanUpdateListener(this);
         }
      }

      SNMPProxyMBean[] var14 = var1.getSNMPProxies();
      if (var14 != null) {
         for(int var8 = 0; var8 < var14.length; ++var8) {
            var14[var8].removeBeanUpdateListener(this);
         }
      }

      SNMPTrapDestinationMBean[] var15 = var1.getSNMPTrapDestinations();
      if (var15 != null) {
         for(int var9 = 0; var9 < var15.length; ++var9) {
            var15[var9].removeBeanUpdateListener(this);
         }
      }

   }

   private void deregisterMonitorListeners() {
      Iterator var1 = this.jmxMonitorLifecycleList.iterator();

      while(var1.hasNext()) {
         JMXMonitorLifecycle var2 = (JMXMonitorLifecycle)var1.next();
         var2.deregisterMonitorListeners();
      }

   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
      if (var1 instanceof SNMPAgentDeploymentMBean) {
         SNMPAgentDeploymentMBean var3 = (SNMPAgentDeploymentMBean)var1;
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Unpreparing SNMPAgentDeploymentMBean " + var3.getName());
         }
      }

   }

   boolean isSNMPServiceStarted() {
      return this.snmpServiceStarted;
   }

   synchronized void setSNMPServiceStarted(boolean var1) {
      this.snmpServiceStarted = var1;
   }

   SNMPAgentMBean getSNMPAgentConfig() {
      return this.snmpAgentConfig;
   }

   private synchronized void initializeSNMPAgentConfig() {
      if (this.targettedAgentConfig != null && this.targettedAgentConfig.isEnabled()) {
         this.snmpAgentConfig = this.targettedAgentConfig;
      } else {
         this.snmpAgentConfig = this.domainAgentConfig;
      }

      if (DEBUG.isDebugEnabled()) {
         String var1 = this.snmpAgentConfig == null ? "" : this.snmpAgentConfig.getName();
         DEBUG.debug("Using snmp agent " + var1);
      }

   }

   private synchronized void startSNMPAgent() throws Exception {
      if (this.snmpAgentConfig != null) {
         if (this.snmpAgentConfig.isEnabled() && this.getAgentState() != 3) {
            this.setAgentState(2);
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Starting SNMPAgent");
            }

            SNMPLogger.logAgentInitializing();
            this.snmpAgent = new SNMPV3Agent();
            this.snmpAgent.setMaxPortRetryCount(this.serverCount);
            this.snmpAgent.setEngineId(this.snmpAgentConfig.getSNMPEngineId());
            this.snmpAgent.setCommunity(this.snmpAgentConfig.getCommunityPrefix());
            this.snmpAgent.setTcpListenAddress(this.listenAddress);
            this.snmpAgent.setTcpListenPort(this.tcpListenPort);
            this.snmpAgent.setUdpListenAddress(this.listenAddress);
            this.snmpAgent.setUdpListenPort(this.snmpAgentConfig.getSNMPPort());
            this.snmpAgent.setSNMPTrapVersion(this.snmpAgentConfig.getSNMPTrapVersion());
            this.snmpAgent.setAutomaticTrapsEnabled(this.snmpAgentConfig.isSendAutomaticTrapsEnabled());
            this.snmpAgent.setInformEnabled(this.snmpAgentConfig.isInformEnabled());
            this.snmpAgent.setInformRetryCount(this.snmpAgentConfig.getMaxInformRetryCount());
            this.snmpAgent.setInformTimeout(this.snmpAgentConfig.getInformRetryInterval());
            this.snmpAgent.setCommunityBasedAccessEnabled(this.snmpAgentConfig.isCommunityBasedAccessEnabled());
            this.snmpAgent.setSecurityLevel(SNMPValidator.getSecurityLevel(this.snmpAgentConfig));
            this.snmpAgent.setAuthProtocol(this.getAuthProtocol());
            this.snmpAgent.setPrivProtocol(this.getPrivProtocol());
            this.snmpAgent.setLocalizedKeyCacheInvalidationInterval(this.snmpAgentConfig.getLocalizedKeyCacheInvalidationInterval());
            this.snmpAgent.setSNMPDataFileName(this.getSNMPDataFileName());
            SNMPTrapDestinationMBean[] var1 = this.snmpAgentConfig.getSNMPTrapDestinations();
            this.configureTrapDestinations(var1);
            this.wlsMibMetadataList.add(WLSMibMetadata.loadResource());

            try {
               this.discoverSNMPAgentExtensionProviders();
            } catch (Exception var5) {
               SNMPLogger.logSNMPExtensionProviderError(var5);
            }

            this.snmpAgent.initialize();

            try {
               SNMPProxyMBean[] var2 = this.snmpAgentConfig.getSNMPProxies();
               this.configureSNMPProxies(var2);
            } catch (Throwable var4) {
               SNMPLogger.logUnableToProxy(var4);
            }

            SNMPTrapUtil.getInstance().setSNMPTrapSender(new SNMPLocalAgentTrapSender(this.snmpAgent));
            this.initializeMBeanServerConnection();

            try {
               String var6 = "localhost";
               if (this.listenAddress != null && this.listenAddress.length() > 0) {
                  var6 = this.listenAddress;
               }

               this.snmpAgent.initializeMasterAgentX(var6, this.snmpAgentConfig.getMasterAgentXPort());
               if (this.snmpAgentConfig.isSNMPAccessForUserMBeansEnabled()) {
                  this.customMBeansSubAgent = (MBeanServerSubAgentX)this.snmpAgent.createSubAgentX("1.2.3.4.5.6", "1.3.6.1.4.1.140.625.50");
                  this.customMBeansSubAgent.createMIBModule("CUSTOM-MBEANS-MIB", "customMBeansMib", "MIB for custom MBeans registered in WLS RuntimeMBeanServer", "BEA Systems Inc.", "dev2dev@bea.com");
               }
            } catch (SNMPAgentToolkitException var3) {
               SNMPLogger.logSNMPAgentXInitializationFailure(var3);
            }

            this.initializeJMXMonitorLifecycleList();
            this.initializerJMXMonitorLifecycles(this.snmpAgentConfig);
            this.initializeSNMPAgentRuntime();
            this.regHandler = new MBeanRegHandler(this.domainName, this.adminServer, this.serverName, this.snmpAgentConfig, this.mbeanServerConnection, this.snmpAgent, this.customMBeansSubAgent, this.wlsMibMetadataList, this.jmxMonitorLifecycleList);
            this.regHandler.initializeMBeanServerRegistration();
            ProtocolHandlerSNMP.getSNMPProtocolHandler().setAgent(this.snmpAgent);
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Started SNMPAgent");
            }

            this.setAgentState(3);
            SNMPLogger.logAgentInitComplete();
         }
      }
   }

   private void discoverSNMPAgentExtensionProviders() throws WLSMibMetadataException {
      HashSet var1 = new HashSet();
      HashSet var2 = new HashSet();
      var1.add(this.snmpAgent.getMibBasePath());
      var2.add(this.snmpAgent.getMibModules());
      this.snmpExtensionProviders = SNMPExtensionProviderHelper.discoverSNMPAgentExtensionProviders();
      Iterator var3 = this.snmpExtensionProviders.iterator();

      String var5;
      String var6;
      while(var3.hasNext()) {
         SNMPExtensionProvider var4 = (SNMPExtensionProvider)var3.next();
         var5 = var4.getBasePath();
         var6 = var4.getMibModules();
         if (var5 != null) {
            var1.add(var5);
         }

         if (var6 != null) {
            var2.add(var6);
         }

         WLSMibMetadata var7 = var4.getMibMetaData();
         if (var7 != null) {
            this.wlsMibMetadataList.add(var7);
         }
      }

      HashSet var10 = new HashSet();
      StringBuilder var11 = new StringBuilder();
      Iterator var12 = var1.iterator();

      while(var12.hasNext()) {
         var6 = (String)var12.next();
         if (!var10.contains(var6)) {
            var11.append(var6);
            var11.append(";");
            var10.add(var6);
         }
      }

      var5 = var11.toString();
      DebugLogger var13 = SNMPExtensionProviderHelper.DEBUG_LOGGER;
      if (var13.isDebugEnabled()) {
         var13.debug("MIB base paths = " + var5);
      }

      this.snmpAgent.setMibBasePath(var5);
      var10.clear();
      StringBuilder var14 = new StringBuilder();
      Iterator var8 = var2.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         if (!var10.contains(var9)) {
            var14.append(var9);
            var14.append(":");
            var10.add(var9);
         }
      }

      String var15 = var14.toString();
      if (var13.isDebugEnabled()) {
         var13.debug("MIB module names = " + var15);
      }

      this.snmpAgent.setMibModules(var15);
   }

   private void configureSNMPProxies(SNMPProxyMBean[] var1) throws SNMPAgentToolkitException {
      if (var1 != null) {
         SNMPProxyManager var2 = this.snmpAgent.getSNMPAgentToolkit().getSNMPProxyManager();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            SNMPProxyMBean var4 = var1[var3];
            String var5 = var4.getName();
            int var6 = var4.getPort();
            String var7 = "";
            String var8 = var4.getOidRoot();
            String var9 = var4.getCommunity();
            if (var9 == null || var9.length() == 0 || var9.equals("na")) {
               var9 = "public";
            }

            String var10 = var4.getSecurityName();
            int var11 = this.getSecurityLevelValue(var4.getSecurityLevel());
            long var12 = var4.getTimeout();
            var2.addProxyAgent(var5, var7, var6, var8, var9, var10, var11, var12);
         }

      }
   }

   private int getSecurityLevelValue(String var1) {
      if (var1 != null) {
         if (var1.equals("noAuthNoPriv")) {
            return 0;
         }

         if (var1.equals("authNoPriv")) {
            return 1;
         }

         if (var1.equals("authPriv")) {
            return 3;
         }
      }

      return 0;
   }

   private void registerBeanUpdateListener(SNMPAgentMBean var1) {
      var1.addBeanUpdateListener(this);
      SNMPAttributeChangeMBean[] var2 = var1.getSNMPAttributeChanges();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3].addBeanUpdateListener(this);
         }
      }

      SNMPCounterMonitorMBean[] var10 = var1.getSNMPCounterMonitors();
      if (var10 != null) {
         for(int var4 = 0; var4 < var10.length; ++var4) {
            var10[var4].addBeanUpdateListener(this);
         }
      }

      SNMPGaugeMonitorMBean[] var11 = var1.getSNMPGaugeMonitors();
      if (var11 != null) {
         for(int var5 = 0; var5 < var11.length; ++var5) {
            var11[var5].addBeanUpdateListener(this);
         }
      }

      SNMPStringMonitorMBean[] var12 = var1.getSNMPStringMonitors();
      if (var12 != null) {
         for(int var6 = 0; var6 < var12.length; ++var6) {
            var12[var6].addBeanUpdateListener(this);
         }
      }

      SNMPLogFilterMBean[] var13 = var1.getSNMPLogFilters();
      if (var13 != null) {
         for(int var7 = 0; var7 < var13.length; ++var7) {
            var13[var7].addBeanUpdateListener(this);
         }
      }

      SNMPProxyMBean[] var14 = var1.getSNMPProxies();
      if (var14 != null) {
         for(int var8 = 0; var8 < var14.length; ++var8) {
            var14[var8].addBeanUpdateListener(this);
         }
      }

      SNMPTrapDestinationMBean[] var15 = var1.getSNMPTrapDestinations();
      if (var15 != null) {
         for(int var9 = 0; var9 < var15.length; ++var9) {
            var15[var9].addBeanUpdateListener(this);
         }
      }

   }

   private void initializeSNMPAgentRuntime() throws ManagementException {
      SNMPV3AgentToolkit var1 = (SNMPV3AgentToolkit)this.snmpAgent.getSNMPAgentToolkit();
      if (this.snmpRuntimeStats == null) {
         this.snmpRuntimeStats = new SNMPRuntimeStats(var1);
      } else {
         this.snmpRuntimeStats.setSNMPAgentToolkit(var1);
      }

      this.snmpRuntimeStats.setRunning(true);
      this.snmpRuntimeStats.setSNMPAgentName(this.snmpAgentConfig == null ? null : this.snmpAgentConfig.getName());
      Iterator var2 = this.jmxMonitorLifecycleList.iterator();

      while(var2.hasNext()) {
         JMXMonitorLifecycle var3 = (JMXMonitorLifecycle)var2.next();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Got JMXMonitorLifecycle " + var3.getClass().getName());
         }

         JMXMonitorListener var5;
         for(Iterator var4 = var3.getJMXMonitorListeners(); var4.hasNext(); var5.setSNMPRuntimeStats(this.snmpRuntimeStats)) {
            var5 = (JMXMonitorListener)var4.next();
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Setting SNMP stats " + var5.getClass().getName());
            }
         }
      }

      this.ensureSNMPAgentRuntimeInitialized();
   }

   synchronized void ensureSNMPAgentRuntimeInitialized() throws ManagementException {
      SNMPAgentRuntime var1 = null;
      ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getServerRuntime();
      if (var2.getSNMPAgentRuntime() == null) {
         var1 = new SNMPAgentRuntime(var2, this.snmpRuntimeStats);
         var2.setSNMPAgentRuntime(var1);
      }

      if (this.adminServer) {
         DomainRuntimeMBean var3 = ManagementService.getDomainAccess(KERNEL_ID).getDomainRuntime();
         if (var3.getSNMPAgentRuntime() == null) {
            var1 = new SNMPAgentRuntime(var3, this.snmpRuntimeStats);
            var3.setSNMPAgentRuntime(var1);
         }
      }

   }

   private void initializerJMXMonitorLifecycles(SNMPAgentMBean var1) throws Exception {
      Iterator var2 = this.jmxMonitorLifecycleList.iterator();

      while(var2.hasNext()) {
         JMXMonitorLifecycle var3 = (JMXMonitorLifecycle)var2.next();
         var3.initializeMonitorListenerList(var1);
      }

   }

   private int getAuthProtocol() {
      if (this.snmpAgentConfig == null) {
         return 0;
      } else {
         String var1 = this.snmpAgentConfig.getAuthenticationProtocol();
         if (var1.equals("MD5")) {
            return 0;
         } else {
            return var1.equals("SHA") ? 1 : 0;
         }
      }
   }

   private int getPrivProtocol() {
      if (this.snmpAgentConfig == null) {
         return 2;
      } else {
         String var1 = this.snmpAgentConfig.getPrivacyProtocol();
         if (var1.equals("DES")) {
            return 2;
         } else {
            return var1.equals("AES_128") ? 3 : 2;
         }
      }
   }

   private void configureTrapDestinations(SNMPTrapDestinationMBean[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            SNMPTrapDestinationMBean var3 = var1[var2];
            SNMPTrapDestination var4 = this.snmpAgent.createSNMPTrapDestination(var3.getName());
            var4.setCommunity(var3.getCommunity());
            var4.setHost(var3.getHost());
            var4.setPort(var3.getPort());
            var4.setSecurityName(var3.getSecurityName());
            var4.setSecurityLevel(this.getSecurityLevelValue(var3.getSecurityLevel()));
         }

      }
   }

   private void initializeMBeanServerConnection() throws Exception {
      String var1 = this.adminServer ? "weblogic.management.mbeanservers.domainruntime" : "weblogic.management.mbeanservers.runtime";
      this.mbeanServerConnection = getConnection(var1);
   }

   private static MBeanServerConnection getConnection(String var0) throws Exception {
      JMXServiceURL var1 = new JMXServiceURL("wlx", (String)null, 0, "/jndi/" + var0);
      Hashtable var2 = new Hashtable();
      var2.put("jmx.remote.protocol.provider.pkgs", "weblogic.management.remote");
      var2.put("weblogic.context", getContext());
      JMXConnector var3 = JMXConnectorFactory.connect(var1, var2);
      return var3.getMBeanServerConnection();
   }

   private static Context getContext() throws Exception {
      Environment var0 = new Environment();
      return var0.getInitialContext();
   }

   SNMPV3Agent getSNMPAgent() {
      return this.snmpAgent;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Activating update");
      }

      synchronized(this) {
         try {
            this.stopSNMPAgent();
            this.activateSNMPAgent();
         } catch (Exception var5) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Error updating SNMPAgent", var5);
            }
         }

      }
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   SNMPAgentMBean getDomainAgentConfig() {
      return this.domainAgentConfig;
   }

   void setDomainAgentConfig(SNMPAgentMBean var1) {
      this.domainAgentConfig = var1;
      this.registerBeanUpdateListener(var1);
   }

   SNMPAgentMBean getTargettedAgentConfig() {
      return this.targettedAgentConfig;
   }

   void setTargettedAgentConfig(SNMPAgentDeploymentMBean var1) {
      this.targettedAgentConfig = var1;
   }

   synchronized int getAgentState() {
      return this.agentState;
   }

   private synchronized void setAgentState(int var1) {
      this.agentState = var1;
   }
}
