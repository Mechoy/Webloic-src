package weblogic.jms;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import javax.jms.Destination;
import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.deployment.jms.JMSSessionPool;
import weblogic.deployment.jms.JMSSessionPoolManager;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.jms.backend.BEConnection;
import weblogic.jms.backend.BEManager;
import weblogic.jms.backend.BEUOOObjectHandler;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.backend.udd.UDDEntity;
import weblogic.jms.client.JMSSession;
import weblogic.jms.common.CDSRouter;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSTargetsListener;
import weblogic.jms.common.LeaderManager;
import weblogic.jms.common.SecurityChecker;
import weblogic.jms.common.ServerCrossDomainSecurityUtil;
import weblogic.jms.common.SingularAggregatableManager;
import weblogic.jms.common.TimedSecurityParticipant;
import weblogic.jms.deployer.BEAdminHandler;
import weblogic.jms.deployer.BEDeployer;
import weblogic.jms.deployer.FEDeployer;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dotnet.proxy.ProxyManager;
import weblogic.jms.frontend.FEClientIDSingularAggregatable;
import weblogic.jms.frontend.FEConnection;
import weblogic.jms.frontend.FEConnectionFactory;
import weblogic.jms.frontend.FEManager;
import weblogic.jms.frontend.FrontEnd;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.TargetingHelper;
import weblogic.jms.module.observers.JMSObserver;
import weblogic.jms.multicast.JMSTDMSocket;
import weblogic.jms.multicast.JMSTDMSocketIPM;
import weblogic.jms.multicast.JMSTMSocket;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.JMSConnectionRuntimeMBean;
import weblogic.management.runtime.JMSPooledConnectionRuntimeMBean;
import weblogic.management.runtime.JMSRuntimeMBean;
import weblogic.management.runtime.JMSServerRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.utils.GenericBeanListener;
import weblogic.management.utils.GenericManagedService;
import weblogic.management.utils.GenericServiceManager;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.path.internal.PathObjectHandler;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.JMSResource;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public final class JMSService extends RuntimeMBeanDelegate implements JMSRuntimeMBean {
   static final long serialVersionUID = -992732582587191590L;
   public static final int STATE_INITIALIZING = 0;
   public static final int STATE_SUSPENDED = 1;
   public static final int STATE_SUSPENDING = 2;
   public static final int STATE_STARTED = 4;
   public static final int STATE_SHUTTING_DOWN = 8;
   public static final int STATE_CLOSED = 16;
   public static final int STATE_DELETING = 32;
   public static final int STATE_DELETED = 64;
   public static final int STATE_BOOTING = 128;
   public static final int STATE_PAUSING_PRODUCTION = 256;
   public static final int STATE_PAUSED_PRODUCTION = 512;
   public static final int STATE_RESUMING_PRODUCTION = 1024;
   public static final int STATE_PAUSING_INSERTION = 2048;
   public static final int STATE_PAUSED_INSERTION = 4096;
   public static final int STATE_RESUMING_INSERTION = 8192;
   public static final int STATE_PAUSING_CONSUMPTION = 16384;
   public static final int STATE_PAUSED_CONSUMPTION = 32768;
   public static final int STATE_RESUMING_CONSUMPTION = 65536;
   public static final int STATE_ADVERTISED_IN_CLUSTER_JNDI = 131072;
   public static final int STATE_ADVERTISED_IN_LOCAL_JNDI = 262144;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String[] stateName = new String[]{"initializing", "suspended", "suspending", "started", "shutting_down", "closed", "deleting", "deleted", "booting", "pausing_production", "paused_production", "resuming_production", "pausing_insertion", "paused_insertion", "resuming_insertion", "pausing_consumption", "paused_consumption", "resuming_consumption", "advertised_in_cluster_jndi", "advertised_in_local_jndi", "unknown"};
   public static final String MESSAGE_LOG_NON_DURABLE_PROP = "weblogic.jms.message.logging.logNonDurableSubscriber";
   public static final String MESSAGE_LOG_DESTINATIONS_ALL_PROP = "weblogic.jms.message.logging.destinations.all";
   public static final String SECURITY_CHECK_INTERVAL_PROP = "weblogic.jms.securityCheckInterval";
   public static final String MULTICAST_SEND_DELAY_PROP = "weblogic.jms.extensions.multicast.sendDelay";
   public static final String BACKEND_JNDI = "weblogic.jms.backend";
   private int state = 0;
   private Object shutdownLock;
   private boolean initialized;
   private volatile boolean startedFirstTime;
   private JMSDispatcher dispatcher;
   private FrontEnd frontEnd;
   private ProxyManager proxyManager;
   private static Context ctx;
   private static Context nonReplicatedCtx;
   private ServerMBean serverMBean;
   private DomainMBean domainMBean;
   private String mbeanName;
   JMSDebug jmsDebug = new JMSDebug();
   private JMSTDMSocket dgmSock;
   private JMSTMSocket mSock;
   private int multicastDelay;
   private static JMSService jmsService;
   private InvocableMonitor invocableMonitor;
   private GenericManagedService beAdminManager;
   private BEDeployer beDeployer;
   private FEDeployer feDeployer;
   private boolean messageLogNonDurableSubscriber;
   private boolean messageLogAll;
   private static final long SECURITY_CHECK_INTERVAL = 60000L;
   private TimerManager securityTimerManager;
   private SecurityChecker securityChecker;
   private Timer securityTimer;
   private long securityCheckInterval = 60000L;
   private static final HashMap<String, Class<Boolean>> wlsServerSignatures = new HashMap();
   private GenericBeanListener wlsServerListener;
   private JMSDiagnosticImageSource diagnosticImageSource;
   private HashSet<JMSTargetsListener> jmsServerListeners = new HashSet();
   private HashMap<String, DescriptorAndListener> jmsServerBeanListeners = new HashMap();
   private boolean use81StyleExecuteQueues;
   private boolean migrationInProgress = false;

   public String getServerName() {
      return this.serverMBean == null ? null : this.serverMBean.getName();
   }

   public String getClusterName() {
      return this.serverMBean == null ? null : (this.serverMBean.getCluster() == null ? null : this.serverMBean.getCluster().getName());
   }

   public String getDomainName() {
      return this.domainMBean == null ? null : this.domainMBean.getName();
   }

   public boolean isMigrationInProgress() {
      return this.migrationInProgress;
   }

   public void setMigrationInProgress(boolean var1) {
      this.migrationInProgress = var1;
   }

   public JMSService() throws ManagementException {
      super(ManagementService.getRuntimeAccess(kernelId).getServerName() + ".jms");
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setJMSRuntime(this);
      this.mbeanName = ManagementService.getRuntimeAccess(kernelId).getServerName() + ".jms";
      this.beDeployer = new BEDeployer(this);
      this.feDeployer = new FEDeployer(this);
      this.shutdownLock = this.beDeployer.getShutdownLock();
      this.feDeployer.setShutdownLock(this.shutdownLock);
      JMSSecurityHelper.getSecurityHelper();
      new PathObjectHandler();
      PathObjectHandler.setObjectHandler((byte)1, new BEUOOObjectHandler());
      this.invocableMonitor = new InvocableMonitor((InvocableMonitor)null);
      InvocableManagerDelegate.delegate.addManager(1, new FEManager(this.invocableMonitor));
      InvocableManagerDelegate.delegate.addManager(2, new BEManager(this.invocableMonitor));
      InvocableManagerDelegate.delegate.addManager(21, LeaderManager.getLeaderManager());
      InvocableManagerDelegate.delegate.addManager(23, CDSRouter.getSingleton());
      String var1 = System.getProperty("weblogic.jms.message.logging.logNonDurableSubscriber");
      this.messageLogNonDurableSubscriber = var1 == null ? false : var1.equalsIgnoreCase("true");
      var1 = System.getProperty("weblogic.jms.message.logging.destinations.all");
      this.messageLogAll = var1 == null ? false : var1.equalsIgnoreCase("true");
      var1 = System.getProperty("weblogic.jms.securityCheckInterval");
      if (var1 != null) {
         try {
            this.securityCheckInterval = (long)Integer.parseInt(var1);
            System.out.println("INFO: Using a JMS security check interval of " + this.securityCheckInterval);
         } catch (NumberFormatException var3) {
            System.out.println("WARNING: Unable to set securityCheckInterval to " + var1 + " : " + var3);
            this.securityCheckInterval = 60000L;
         }
      }

      if (this.securityCheckInterval > 0L) {
         this.securityTimerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.security.checkers", "weblogic.kernel.Default");
         this.securityChecker = new SecurityChecker(this);
      }

      this.diagnosticImageSource = new JMSDiagnosticImageSource();
   }

   public BEDeployer getBEDeployer() {
      return this.beDeployer;
   }

   public FEDeployer getFEDeployer() {
      return this.feDeployer;
   }

   public static final String getStateName(int var0) {
      if (var0 == 0) {
         return stateName[0];
      } else {
         StringBuffer var1 = new StringBuffer();
         boolean var2 = false;

         for(int var3 = 1; var3 < stateName.length - 1; ++var3) {
            if ((var0 & 1 << var3 - 1) != 0) {
               if (var2) {
                  var1.append(", ");
               } else {
                  var2 = true;
               }

               var1.append(stateName[var3]);
            }
         }

         if (!var2) {
            return stateName[stateName.length - 1];
         } else {
            return var1.toString();
         }
      }
   }

   public static synchronized JMSService getService() throws ManagementException {
      if (jmsService == null) {
         jmsService = new JMSService();
      }

      return jmsService;
   }

   public static JMSService getJMSService() {
      return jmsService;
   }

   private void initializeJMSServerListeners() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      JMSServerMBean[] var2 = var1.getJMSServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JMSServerMBean var4 = var2[var3];
         JMSServerBeanListener var5 = new JMSServerBeanListener(var2[var3]);
         var4.addBeanUpdateListener(var5);
         synchronized(this.jmsServerBeanListeners) {
            this.jmsServerBeanListeners.put(var2[var3].getName(), new DescriptorAndListener(var4, var5));
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Listening for changes to JMSServer " + var2[var3].getName());
         }
      }

   }

   private void removeJMSServerListeners() {
      synchronized(this.jmsServerBeanListeners) {
         Iterator var2 = this.jmsServerBeanListeners.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            DescriptorAndListener var4 = (DescriptorAndListener)this.jmsServerBeanListeners.get(var3);
            DescriptorBean var5 = var4.getDescriptorBean();
            JMSServerBeanListener var6 = var4.getListener();
            var5.removeBeanUpdateListener(var6);
            var6.close();
         }

      }
   }

   private void initializeObservers() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      var1.addAccessCallbackClass(JMSObserver.class.getName());
   }

   void start() throws JMSException, ServiceFailureException {
      if (!this.startedFirstTime) {
         if (!this.initialize()) {
            return;
         }

         this.startedFirstTime = true;
      }

      synchronized(this.shutdownLock) {
         this.state = 4;
      }

      HealthMonitorService.register(this.mbeanName, this, false);
      this.initializeObservers();
      this.initializeJMSServerListeners();
      this.feDeployer.initialize(this.frontEnd);
      if (this.serverMBean.isJMSDefaultConnectionFactoriesEnabled()) {
         this.ensureInitialized();
      }

      if (this.frontEnd != null) {
         this.frontEnd.resume();
      }

      if (this.proxyManager != null) {
         this.proxyManager.resume();
      }

      this.beAdminManager.start();
      JMSLogger.logJMSActive();
   }

   public boolean initialize() throws JMSException {
      try {
         Environment var1 = new Environment();
         var1.setCreateIntermediateContexts(true);
         var1.setReplicateBindings(true);
         ctx = var1.getInitialContext();
         var1 = new Environment();
         var1.setCreateIntermediateContexts(true);
         var1.setReplicateBindings(false);
         nonReplicatedCtx = var1.getInitialContext();
      } catch (NamingException var8) {
         JMSLogger.logErrorInitialCtx(var8);
         throw new weblogic.jms.common.JMSException(var8);
      }

      synchronized(this.shutdownLock) {
         if (this.isShutdown()) {
            this.state = 0;
         }
      }

      this.serverMBean = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.domainMBean = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.initializeDispatcher();
      if (this.frontEnd == null) {
         this.frontEnd = new FrontEnd(this);
      }

      if (this.proxyManager == null) {
         try {
            Class var2 = Class.forName("weblogic.jms.dotnet.proxy.internal.ProxyManagerImpl");
            Method var3 = var2.getMethod("getProxyManager");
            this.proxyManager = (ProxyManager)var3.invoke((Object)null);
         } catch (ClassNotFoundException var4) {
            throw new AssertionError(var4);
         } catch (NoSuchMethodException var5) {
            throw new AssertionError(var5);
         } catch (IllegalAccessException var6) {
            throw new AssertionError(var6);
         } catch (InvocationTargetException var7) {
            throw new AssertionError(var7);
         }
      }

      this.beAdminManager = GenericServiceManager.getManager().register(JMSServerMBean.class, BEAdminHandler.class, true);
      JMSLogger.logJMSInitialized();
      return true;
   }

   public final synchronized void ensureInitialized() throws JMSException {
      if (!this.initialized) {
         this.multicastDelay = 0;

         try {
            String var1 = System.getProperty("weblogic.jms.extensions.multicast.sendDelay");
            if (var1 != null) {
               this.multicastDelay = Integer.parseInt(var1);
            }
         } catch (SecurityException var3) {
         }

         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("JMS is initialized");
         }

         this.initialized = true;
         this.wlsServerListener = new GenericBeanListener(this.serverMBean, this, wlsServerSignatures);
      }
   }

   public void openMulticastSendSocket() throws JMSException {
      if (this.mSock == null) {
         try {
            this.dgmSock = new JMSTDMSocketIPM();
            this.mSock = new JMSTMSocket((JMSSession)null, this.dgmSock, this.multicastDelay, 0);
         } catch (IOException var2) {
            if (this.mSock != null) {
               this.mSock.close();
            }

            if (this.dgmSock != null) {
               this.dgmSock.close();
            }

            this.mSock = null;
            this.dgmSock = null;
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("can not open multicast socket " + var2.toString());
            }

            JMSLogger.logErrorMulticastOpen(var2);
            throw new JMSException(var2.toString());
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Multicast socket is opened");
         }

      }
   }

   void stop(boolean var1) throws ServiceFailureException {
      try {
         synchronized(this.shutdownLock) {
            if ((this.state & 17) != 0) {
               return;
            }

            this.state = 8;
            this.removeJMSServerListeners();
            if (this.wlsServerListener != null) {
               this.wlsServerListener.close();
            }

            BackEnd[] var2 = this.beDeployer.getBackEnds();

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var2[var4].markShuttingDown();
            }

            if (this.frontEnd != null) {
               this.frontEnd.markShuttingDown();
            }

            if (var1) {
               this.invocableMonitor.forceInvocablesCompletion();
            }
         }

         if (this.frontEnd != null) {
            this.frontEnd.prepareForSuspend(var1);
         }

         if (!var1) {
            this.invocableMonitor.waitForInvocablesCompletion();
         }

         if (this.beAdminManager != null) {
            this.beAdminManager.stop();
         }

         if (this.frontEnd != null) {
            this.frontEnd.shutdown();
         }

         if (this.proxyManager != null) {
            this.proxyManager.shutdown(var1);
         }

         if (this.feDeployer != null) {
            this.feDeployer.shutdown();
         }

         HealthMonitorService.unregister(this.mbeanName);

         try {
            PrivilegedActionUtilities.unregister(this, kernelId);
         } catch (ManagementException var17) {
         }
      } finally {
         synchronized(this.shutdownLock) {
            this.state = 16;
         }

         JMSLogger.logJMSShutdown();
      }

   }

   public HealthState getHealthState() {
      int var1 = 0;
      ArrayList var2 = new ArrayList(6);
      BackEnd[] var5 = this.getBEDeployer().getBackEnds();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         HealthState var4 = var5[var6].getHealthState();
         var1 = Math.max(var4.getState(), var1);
         String[] var3 = var4.getReasonCode();

         for(int var7 = 0; var7 < var3.length; ++var7) {
            var2.add(var3[var7]);
         }
      }

      return new HealthState(var1, (String[])var2.toArray(new String[var2.size()]));
   }

   public boolean isActive() {
      synchronized(this.shutdownLock) {
         return (this.state & 4) != 0;
      }
   }

   public boolean isShutdown() {
      synchronized(this.shutdownLock) {
         return (this.state & 24) != 0;
      }
   }

   public void checkShutdown() throws JMSException {
      synchronized(this.shutdownLock) {
         if (this.isShutdown()) {
            throw new weblogic.jms.common.JMSException("JMS server shutdown: " + this.state);
         }
      }
   }

   public void checkShutdownOrSuspended(String var1) throws JMSException {
      synchronized(this.shutdownLock) {
         if ((this.state & 27) != 0) {
            throw new JMSException("Failed to " + var1 + " because JMS server shutdown or suspended");
         }
      }
   }

   public FEConnectionFactory getDefaultConnectionFactory(String var1) {
      return this.getFEDeployer().getDefaultConnectionFactory(var1);
   }

   public static Context getContext() {
      return ctx;
   }

   public static Context getContext(boolean var0) {
      return var0 ? ctx : nonReplicatedCtx;
   }

   public synchronized JMSServerId getNextServerId() {
      return new JMSServerId(JMSID.create(), this.dispatcher.getId());
   }

   public JMSID getNextId() {
      return JMSID.create();
   }

   public JMSMessageId getNextMessageId() {
      return JMSMessageId.create();
   }

   public JMSConnectionRuntimeMBean[] getConnections() {
      return FEManager.getConnections();
   }

   public long getConnectionsCurrentCount() {
      return FEManager.getConnectionsCurrentCount();
   }

   public long getConnectionsHighCount() {
      return FEManager.getConnectionsHighCount();
   }

   public long getConnectionsTotalCount() {
      return FEManager.getConnectionsTotalCount();
   }

   public JMSServerRuntimeMBean[] getJMSServers() {
      BackEnd[] var1 = null;
      HashMap var2 = new HashMap();
      synchronized(this.shutdownLock) {
         var1 = this.beDeployer.getBackEnds();
         if (var1 != null && var1.length > 0) {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               if (var1[var4].getConfigType().equals("JMSServer")) {
                  var2.put(var1[var4].getName(), var1[var4]);
               }
            }
         }

         BackEnd[] var7 = new BackEnd[var2.size()];
         return (JMSServerRuntimeMBean[])var2.values().toArray(var7);
      }
   }

   public long getJMSServersCurrentCount() {
      synchronized(this.shutdownLock) {
         return (long)this.getBEDeployer().getBackEndsMap().size();
      }
   }

   public long getJMSServersHighCount() {
      synchronized(this.shutdownLock) {
         return this.getBEDeployer().getBackEndsHighCount();
      }
   }

   public long getJMSServersTotalCount() {
      synchronized(this.shutdownLock) {
         return this.getBEDeployer().getBackEndsTotalCount();
      }
   }

   public JMSPooledConnectionRuntimeMBean[] getJMSPooledConnections() {
      HashMap var1 = new HashMap();
      JMSSessionPoolManager var2 = JMSSessionPoolManager.getSessionPoolManager();
      HashMap var3 = var2.getSessionPools();
      Iterator var4 = var3.values().iterator();

      while(var4.hasNext()) {
         JMSSessionPool var5 = (JMSSessionPool)var4.next();
         JMSPooledConnectionRuntimeMBean var6 = (JMSPooledConnectionRuntimeMBean)var5.getJMSSessionPoolRuntime();
         if (var6 != null) {
            var1.put(var5.getName(), var6);
         }
      }

      JMSPooledConnectionRuntimeMBean[] var7 = new JMSPooledConnectionRuntimeMBean[var1.size()];
      return (JMSPooledConnectionRuntimeMBean[])var1.values().toArray(var7);
   }

   public void setJMSDefaultConnectionFactoriesEnabled(boolean var1) {
      try {
         if (var1) {
            this.getFEDeployer().deployDefaultConnectionFactories();
         } else {
            this.getFEDeployer().undeployDefaultConnectionFactories();
         }
      } catch (JMSException var3) {
         JMSLogger.logErrorDeployingDefaultFactories(var3.toString());
      }

   }

   public String getMbeanName() {
      return this.mbeanName;
   }

   public JMSTMSocket getMulticastSocket() {
      if (this.mSock == null) {
         try {
            this.openMulticastSendSocket();
         } catch (JMSException var2) {
         }
      }

      return this.mSock;
   }

   private void initializeDispatcher() {
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      String var2 = var1.getServerName();
      this.use81StyleExecuteQueues = ManagementService.getRuntimeAccess(kernelId).getServer().getUse81StyleExecuteQueues();
      JMSDispatcherManager.initialize(var2, LocalServerIdentity.getIdentity().getPersistentIdentity().toString(), this.use81StyleExecuteQueues);
      this.dispatcher = JMSDispatcherManager.getLocalDispatcher();
      String var3 = "weblogic.messaging.dispatcher.S:" + this.dispatcher.getId();

      try {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("Binding dispatcher to '" + this.name + "'");
         }

         PrivilegedActionUtilities.rebindAsSU(getContext(), var3, JMSDispatcherManager.getLocalDispatcherWrapper(), kernelId);
      } catch (NamingException var5) {
         throw new AssertionError(var5);
      }
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public void reserveClientID(String var1) throws JMSException {
      SingularAggregatableManager var2 = SingularAggregatableManager.findOrCreate();
      String var3 = "weblogic.jms.connection.clientid." + var1;
      FEClientIDSingularAggregatable var4 = new FEClientIDSingularAggregatable(var1, this.getNextId());
      String var5;
      if ((var5 = var2.singularBind(var3, var4)) != null) {
         throw new InvalidClientIDException("Client id, " + var1 + ", is in use.  The reason for rejection is \"" + var5 + "\"");
      }
   }

   public static void releaseClientID(String var0) throws JMSException {
      SingularAggregatableManager var1 = SingularAggregatableManager.findOrCreate();
      String var2 = "weblogic.jms.connection.clientid." + var0;

      try {
         var1.singularUnbind(var2);
      } catch (JMSException var4) {
         throw new weblogic.jms.common.JMSException("Unable to unbind client id " + var0 + " from JNDI", var4);
      }
   }

   public static String getDestinationName(Destination var0) {
      return var0 instanceof DistributedDestinationImpl ? ((DistributedDestinationImpl)var0).getInstanceName() : ((DestinationImpl)var0).getName();
   }

   public boolean shouldMessageLogNonDurableSubscriber() {
      return this.messageLogNonDurableSubscriber;
   }

   public boolean shouldMessageLogAll() {
      return this.messageLogAll;
   }

   public FrontEnd getFrontEnd() {
      return this.frontEnd;
   }

   public void registerSecurityParticipant(JMSResource var1, TimedSecurityParticipant var2) {
      if (this.securityCheckInterval > 0L) {
         this.securityChecker.registerWithChecker(var1, var2);
      }
   }

   public boolean isSecurityCheckerStop() {
      return this.securityCheckInterval == 0L;
   }

   public void fireUpSecurityChecks() {
      if (this.securityCheckInterval > 0L) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("Firing up periodic security checks");
         }

         ClassLoader var1 = Thread.currentThread().getContextClassLoader();
         Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
         this.securityTimer = this.securityTimerManager.schedule(this.securityChecker, 1000L, this.securityCheckInterval);
         Thread.currentThread().setContextClassLoader(var1);
      }
   }

   public void stopSecurityChecks() {
      if (this.securityTimer != null) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("Stoping periodic security checks");
         }

         this.securityTimer.cancel();
         this.securityTimer = null;
      }

   }

   void postDeploymentStart() {
      this.beDeployer.postDeploymentsStart();
   }

   void postDeploymentStop() {
      this.beDeployer.postDeploymentsStop();
   }

   void postDeploymentHalt() {
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("JMS");
      var2.writeAttribute("id", this.frontEnd.getFrontEndId().toString());
      var2.writeAttribute("serverName", this.mbeanName);
      var2.writeAttribute("state", getStateName(this.state));
      var2.writeAttribute("connectionsCurrentCount", String.valueOf(this.getConnectionsCurrentCount()));
      var2.writeAttribute("connectionsHighCount", String.valueOf(this.getConnectionsHighCount()));
      var2.writeAttribute("connectionsTotalCount", String.valueOf(this.getConnectionsTotalCount()));
      var2.writeAttribute("jmsServersCurrentCount", String.valueOf(this.getJMSServersCurrentCount()));
      var2.writeAttribute("jmsServersHighCount", String.valueOf(this.getJMSServersHighCount()));
      var2.writeAttribute("jmsServersTotalCount", String.valueOf(this.getJMSServersTotalCount()));
      JMSDiagnosticImageSource.dumpHealthStateElement(var2, this.getHealthState());
      var2.writeStartElement("Connections");
      FEConnection[] var3 = FEManager.getFEConnections();
      if (var3 != null && var3.length > 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4].dump(var1, var2);
         }
      }

      var2.writeEndElement();
      var2.writeStartElement("JMSServers");
      HashMap var9;
      synchronized(this.shutdownLock) {
         HashMap var6 = this.getBEDeployer().getBackEndsMap();
         var9 = (HashMap)var6.clone();
      }

      Iterator var5 = var9.values().iterator();

      while(var5.hasNext()) {
         BackEnd var10 = (BackEnd)var5.next();
         var10.dump(var1, var2);
      }

      var2.writeStartElement("Connections");
      BEConnection[] var11 = BEManager.getBEConnections();
      if (var11 != null && var11.length > 0) {
         for(int var7 = 0; var7 < var11.length; ++var7) {
            var11[var7].dump(var1, var2);
         }
      }

      var2.writeEndElement();
      var2.writeEndElement();
      var2.writeEndElement();
   }

   public boolean getUse81StyleExecuteQueues() {
      return this.use81StyleExecuteQueues;
   }

   public void addJMSServerListener(JMSTargetsListener var1) {
      synchronized(this.jmsServerListeners) {
         this.jmsServerListeners.add(var1);
      }
   }

   public void removeJMSServerListener(JMSTargetsListener var1) {
      synchronized(this.jmsServerListeners) {
         this.jmsServerListeners.remove(var1);
      }
   }

   private void fireListenersPrepare(DomainMBean var1, JMSServerMBean var2, int var3) throws BeanUpdateRejectedException {
      boolean var4 = false;
      LinkedList var5 = new LinkedList();
      synchronized(this.jmsServerListeners) {
         try {
            Iterator var7 = this.jmsServerListeners.iterator();

            while(var7.hasNext()) {
               JMSTargetsListener var8 = (JMSTargetsListener)var7.next();
               var8.prepareUpdate(var1, var2, var3, this.migrationInProgress);
               var5.addLast(var8);
            }

            var4 = true;
         } finally {
            if (!var4) {
               Iterator var11 = var5.iterator();

               while(var11.hasNext()) {
                  JMSTargetsListener var12 = (JMSTargetsListener)var11.next();
                  var12.rollbackUpdate();
               }
            }

         }
      }
   }

   private void fireListenersActivateOrRollback(boolean var1) {
      synchronized(this.jmsServerListeners) {
         Iterator var3 = this.jmsServerListeners.iterator();

         while(var3.hasNext()) {
            JMSTargetsListener var4 = (JMSTargetsListener)var3.next();
            if (var1) {
               var4.activateUpdate();
            } else {
               var4.rollbackUpdate();
            }
         }

      }
   }

   public void startAddJMSServers(JMSServerMBean var1) throws BeanUpdateRejectedException {
      DomainMBean var2;
      try {
         var2 = JMSBeanHelper.getDomain(var1);
      } catch (IllegalArgumentException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      this.fireListenersPrepare(var2, var1, 1);
   }

   public void finishAddJMSServers(JMSServerMBean var1, boolean var2) {
      this.fireListenersActivateOrRollback(var2);
      JMSServerBeanListener var3 = new JMSServerBeanListener(var1);
      var1.addBeanUpdateListener(var3);
      synchronized(this.jmsServerBeanListeners) {
         this.jmsServerBeanListeners.put(var1.getName(), new DescriptorAndListener(var1, var3));
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Listening for changes to JMSServer " + var1.getName());
      }

   }

   public void startRemoveJMSServers(JMSServerMBean var1) throws BeanUpdateRejectedException {
      DomainMBean var2 = null;

      try {
         var2 = JMSBeanHelper.getDomain(var1);
      } catch (IllegalArgumentException var4) {
         throw new BeanUpdateRejectedException(var4.getMessage(), var4);
      }

      this.fireListenersPrepare(var2, var1, 0);
   }

   public void finishRemoveJMSServers(JMSServerMBean var1, boolean var2) {
      this.fireListenersActivateOrRollback(var2);
      synchronized(this.jmsServerBeanListeners) {
         DescriptorAndListener var4 = (DescriptorAndListener)this.jmsServerBeanListeners.remove(var1.getName());
         if (var4 == null) {
            return;
         }

         DescriptorBean var5 = var4.getDescriptorBean();
         JMSServerBeanListener var6 = var4.getListener();
         var5.removeBeanUpdateListener(var6);
         var6.close();
      }

      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Not listening for changes to removed JMSServer " + var1.getName());
      }

   }

   static {
      wlsServerSignatures.put("JMSDefaultConnectionFactoriesEnabled", Boolean.TYPE);
      CrossDomainSecurityManager.setCrossDomainSecurityUtil(new ServerCrossDomainSecurityUtil());
   }

   private static class DescriptorAndListener {
      private DescriptorBean db;
      private JMSServerBeanListener listener;

      private DescriptorAndListener(DescriptorBean var1, JMSServerBeanListener var2) {
         this.db = var1;
         this.listener = var2;
      }

      private DescriptorBean getDescriptorBean() {
         return this.db;
      }

      private JMSServerBeanListener getListener() {
         return this.listener;
      }

      // $FF: synthetic method
      DescriptorAndListener(DescriptorBean var1, JMSServerBeanListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private class JMSServerBeanListener implements BeanUpdateListener {
      private JMSServerMBean jmsServer;
      private JMSServerMBean proposedJMSServer;
      private MigratableTargetMBean migratableTarget;
      int numFound;
      boolean jmsServerChanged;

      private JMSServerBeanListener(JMSServerMBean var2) {
         this.jmsServer = var2;
         TargetMBean[] var3 = this.jmsServer.getTargets();
         if (var3.length >= 1) {
            TargetMBean var4 = var3[0];
            if (var4 instanceof MigratableTargetMBean) {
               this.migratableTarget = (MigratableTargetMBean)var4;
               this.migratableTarget.addBeanUpdateListener(this);
            }

         }
      }

      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         if (this.migratableTarget == null) {
            this.jmsServerChanged = true;
         } else {
            DescriptorBean var2 = var1.getProposedBean();
            if (var2 instanceof JMSServerMBean) {
               this.jmsServerChanged = true;
            } else {
               this.jmsServerChanged = false;
            }
         }

         boolean var9 = false;
         BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            BeanUpdateEvent.PropertyUpdate var5 = var3[var4];
            if (this.jmsServerChanged && var5.getPropertyName().equals("Targets")) {
               var9 = true;
               break;
            }

            if (!this.jmsServerChanged && var5.getPropertyName().equals("UserPreferredServer")) {
               var9 = true;
               break;
            }
         }

         if (var9) {
            ++this.numFound;
            this.proposedJMSServer = this.jmsServerChanged ? (JMSServerMBean)var1.getProposedBean() : this.jmsServer;

            DomainMBean var10;
            try {
               var10 = this.jmsServerChanged ? JMSBeanHelper.getDomain(this.proposedJMSServer) : JMSBeanHelper.getDomain((WebLogicMBean)var1.getProposedBean());
            } catch (IllegalArgumentException var8) {
               throw new BeanUpdateRejectedException(var8.getMessage(), var8);
            }

            synchronized(this) {
               if (UDDEntity.getLocalJMSServers().get(this.proposedJMSServer.getName()) == null && !this.isLocallyTargeted(this.proposedJMSServer)) {
                  JMSService.this.fireListenersPrepare(var10, this.jmsServer, 2);
               }

            }
         }
      }

      public void activateUpdate(BeanUpdateEvent var1) {
         if (this.numFound > 0) {
            --this.numFound;
            synchronized(this) {
               if (UDDEntity.getLocalJMSServers().get(this.proposedJMSServer.getName()) == null && !this.isLocallyTargeted(this.proposedJMSServer)) {
                  JMSService.this.fireListenersActivateOrRollback(true);
               }
            }

            if (this.jmsServerChanged) {
               MigratableTargetMBean var2 = this.migratableTarget;
               if (var2 != null) {
                  var2.removeBeanUpdateListener(this);
               }

               this.migratableTarget = null;
               TargetMBean[] var3 = this.jmsServer.getTargets();
               if (var3.length >= 1) {
                  TargetMBean var4 = var3[0];
                  if (var4 instanceof MigratableTargetMBean) {
                     this.migratableTarget = (MigratableTargetMBean)var4;
                     this.migratableTarget.addBeanUpdateListener(this);
                  }

               }
            }
         }
      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
         if (this.numFound > 0) {
            --this.numFound;
            synchronized(this) {
               if (UDDEntity.getLocalJMSServers().get(this.proposedJMSServer.getName()) == null && !this.isLocallyTargeted(this.proposedJMSServer)) {
                  JMSService.this.fireListenersActivateOrRollback(false);
               }

            }
         }
      }

      private void close() {
         if (this.migratableTarget != null) {
            this.migratableTarget.removeBeanUpdateListener(this);
         }
      }

      private boolean isLocallyTargeted(JMSServerMBean var1) {
         ServerMBean var2 = ManagementService.getRuntimeAccess(JMSService.kernelId).getServer();
         return var2 == null ? false : TargetingHelper.isLocallyTargeted(var1, var2.getCluster() == null ? null : var2.getCluster().getName(), var2.getName());
      }

      // $FF: synthetic method
      JMSServerBeanListener(JMSServerMBean var2, Object var3) {
         this(var2);
      }
   }
}
