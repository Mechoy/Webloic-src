package weblogic.t3.srvr;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.management.ObjectName;
import weblogic.Home;
import weblogic.version;
import weblogic.cluster.singleton.MigratableServerService;
import weblogic.descriptor.DescriptorBean;
import weblogic.health.HealthFeedback;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.logging.LogBroadcaster;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.management.provider.RegistrationHandler;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.provider.Service;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.AsyncReplicationRuntimeMBean;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.management.runtime.ConnectorServiceRuntimeMBean;
import weblogic.management.runtime.EntityCacheCumulativeRuntimeMBean;
import weblogic.management.runtime.EntityCacheCurrentStateRuntimeMBean;
import weblogic.management.runtime.ExecuteQueueRuntimeMBean;
import weblogic.management.runtime.JDBCServiceRuntimeMBean;
import weblogic.management.runtime.JMSRuntimeMBean;
import weblogic.management.runtime.JTARuntimeMBean;
import weblogic.management.runtime.JVMRuntimeMBean;
import weblogic.management.runtime.JoltConnectionServiceRuntimeMBean;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.management.runtime.LogBroadcasterRuntimeMBean;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.management.runtime.MANAsyncReplicationRuntimeMBean;
import weblogic.management.runtime.MANReplicationRuntimeMBean;
import weblogic.management.runtime.MailSessionRuntimeMBean;
import weblogic.management.runtime.MaxThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.MessagingBridgeRuntimeMBean;
import weblogic.management.runtime.MinThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.PathServiceRuntimeMBean;
import weblogic.management.runtime.PersistentStoreRuntimeMBean;
import weblogic.management.runtime.RequestClassRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SAFRuntimeMBean;
import weblogic.management.runtime.SNMPAgentRuntimeMBean;
import weblogic.management.runtime.ServerChannelRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.ServerSecurityRuntimeMBean;
import weblogic.management.runtime.ServerStates;
import weblogic.management.runtime.SingleSignOnServicesRuntimeMBean;
import weblogic.management.runtime.SocketRuntime;
import weblogic.management.runtime.ThreadPoolRuntimeMBean;
import weblogic.management.runtime.TimeServiceRuntimeMBean;
import weblogic.management.runtime.TimerRuntimeMBean;
import weblogic.management.runtime.WANReplicationRuntimeMBean;
import weblogic.management.runtime.WLDFRuntimeMBean;
import weblogic.management.runtime.WLECConnectionServiceRuntimeMBean;
import weblogic.management.runtime.WTCRuntimeMBean;
import weblogic.management.runtime.WebServerRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.management.runtime.WseeClusterFrontEndRuntimeMBean;
import weblogic.management.runtime.WseeWsrmRuntimeMBean;
import weblogic.protocol.AdminServerIdentity;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.JVMID;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.Channel;
import weblogic.rmi.spi.EndPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.RemoteLifeCycleOperations;
import weblogic.server.ServerLifeCycleRuntime;
import weblogic.server.ServerLifecycleException;
import weblogic.server.ServerLogger;
import weblogic.server.channels.ChannelService;
import weblogic.socket.MuxableSocket;
import weblogic.socket.SocketMuxer;
import weblogic.utils.Classpath;
import weblogic.utils.StackTraceUtils;
import weblogic.work.ContextWrap;
import weblogic.work.WorkManagerFactory;

public final class ServerRuntime extends RuntimeMBeanDelegate implements ServerRuntimeMBean, HealthFeedback {
   private static final long serialVersionUID = 3862450250430200114L;
   private static ServerRuntime singleton;
   private final T3Srvr server = T3Srvr.getT3Srvr();
   private ConnectorServiceRuntimeMBean connectorServiceRuntime;
   private JDBCServiceRuntimeMBean jdbcRuntime;
   private JMSRuntimeMBean jmsRuntime;
   private JTARuntimeMBean jtaRuntime;
   private JVMRuntimeMBean jvmRuntime;
   private SAFRuntimeMBean safRuntime;
   private ClusterRuntimeMBean clusterRuntime;
   private EntityCacheCurrentStateRuntimeMBean ecCurrStateRuntime;
   private EntityCacheCumulativeRuntimeMBean ecCumRuntime;
   private EntityCacheCumulativeRuntimeMBean ecHistRuntime;
   private ExecuteQueueRuntimeMBean executeQueueRuntime;
   private ServerSecurityRuntimeMBean serverSecurityRuntime;
   private SingleSignOnServicesRuntimeMBean ssoRuntime;
   private ThreadPoolRuntimeMBean threadPoolRuntime;
   private final Set executeQueueRuntimes = new HashSet();
   private final Set workManagerRuntimes = new HashSet();
   private final Set minThreadsConstraintRuntimes = new HashSet();
   private final Set maxThreadsConstraintRuntimes = new HashSet();
   private final Set<ApplicationRuntimeMBean> applicationRuntimes = Collections.synchronizedSet(new HashSet());
   private final Set pendingRestartSystemResources = new HashSet();
   private final Map<String, LibraryRuntimeMBean> libraryRuntimes = Collections.synchronizedMap(new HashMap());
   private final Set channelRuntimes = new HashSet();
   private final Set webServerRuntimes = new HashSet();
   private final Map persistentStoreRuntimes = new HashMap();
   private MANReplicationRuntimeMBean manReplicationRuntime;
   private WANReplicationRuntimeMBean wanReplicationRuntime;
   private MANAsyncReplicationRuntimeMBean manAsyncReplicationRuntime;
   private AsyncReplicationRuntimeMBean asyncReplicationRuntime = null;
   private WLDFRuntimeMBean wldfRuntime;
   private WTCRuntimeMBean wtcRuntime;
   private JoltConnectionServiceRuntimeMBean joltRuntime;
   private PathServiceRuntimeMBean pathServiceRuntime;
   private String currentMachine = "";
   private final Set requestClassRuntimes = new HashSet();
   private final Set mailSessionRuntimes = new HashSet();
   private boolean restartRequired;
   private TimerRuntimeMBean timerRuntime;
   private TimeServiceRuntimeMBean timeServiceRuntime;
   private LogRuntimeMBean logRuntime;
   private WLECConnectionServiceRuntimeMBean wlecConnectionService;
   private SNMPAgentRuntimeMBean snmpAgentRuntime;
   private MessagingBridgeRuntimeMBean messagingBridgeRuntime;
   private WseeWsrmRuntimeMBean wseeWsrmRuntime;
   private WseeClusterFrontEndRuntimeMBean wseeClusterFrontEndRuntime;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int stateVal = 9;
   private HealthState healthState = new HealthState(0);
   private Set internalApps;
   private String curDir;

   public static synchronized ServerRuntime init() throws ManagementException {
      if (singleton != null) {
         throw new IllegalStateException("Attempt to double initialize");
      } else {
         singleton = new ServerRuntime();
         return singleton;
      }
   }

   public static synchronized ServerRuntime theOne() {
      return singleton;
   }

   private ServerRuntime() throws ManagementException {
      super(ManagementService.getRuntimeAccess(kernelId).getServerName(), (RuntimeMBean)null, true, ManagementService.getRuntimeAccess(kernelId).getServer());
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      var1.addRegistrationHandler(this.createRegistrationHandler());
      this.server.initializeServerRuntime(this);
   }

   private RegistrationHandler createRegistrationHandler() {
      return new RegistrationHandler() {
         public void registered(RuntimeMBean var1, DescriptorBean var2) {
            if (var1 instanceof ApplicationRuntimeMBean) {
               ServerRuntime.this.applicationRuntimes.add((ApplicationRuntimeMBean)var1);
            } else if (var1 instanceof LibraryRuntimeMBean) {
               ServerRuntime.this.libraryRuntimes.put(var1.getName(), (LibraryRuntimeMBean)var1);
            }

         }

         public void unregistered(RuntimeMBean var1) {
            if (var1 instanceof ApplicationRuntimeMBean) {
               ServerRuntime.this.applicationRuntimes.remove(var1);
            } else if (var1 instanceof LibraryRuntimeMBean) {
               ServerRuntime.this.libraryRuntimes.remove(var1.getName());
            }

         }

         public void registeredCustom(ObjectName var1, Object var2) {
         }

         public void unregisteredCustom(ObjectName var1) {
         }

         public void registered(Service var1) {
         }

         public void unregistered(Service var1) {
         }
      };
   }

   public void suspend() throws ServerLifecycleException {
      this.suspend(0, false);
   }

   public void suspend(int var1, boolean var2) throws ServerLifecycleException {
      GracefulShutdownRequest var3 = new GracefulShutdownRequest(var2, 17);
      this.logAdministratorAddress("Graceful suspend");
      WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var3));
      var3.waitForCompletion(var1 * 1000);
      if (var3.getException() != null) {
         throw new ServerLifecycleException(var3.getException());
      } else {
         if (!var3.isCompleted()) {
            this.forceSuspend();
         }

      }
   }

   public void forceSuspend() throws ServerLifecycleException {
      this.logAdministratorAddress("Force suspend");
      this.server.forceSuspend();
   }

   public void resume() throws ServerLifecycleException {
      this.logAdministratorAddress("Resume");
      this.server.resume();
   }

   public void shutdown(int var1, boolean var2) throws ServerLifecycleException {
      this.logAdministratorAddress("Graceful shutdown");
      GracefulShutdownRequest var3;
      if (var2) {
         var3 = new GracefulShutdownRequest(var2);
      } else {
         var3 = new GracefulShutdownRequest(ManagementService.getRuntimeAccess(kernelId).getServer().isIgnoreSessionsDuringShutdown());
      }

      WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var3));
      if (var1 == 0) {
         var3.waitForCompletion(ManagementService.getRuntimeAccess(kernelId).getServer().getGracefulShutdownTimeout() * 1000);
      } else {
         var3.waitForCompletion(var1 * 1000);
      }

      if (var3.getException() != null) {
         throw new ServerLifecycleException(var3.getException());
      } else {
         if (!var3.isCompleted()) {
            this.forceShutdown();
         }

      }
   }

   public void shutdown() throws ServerLifecycleException {
      this.shutdown(0, false);
   }

   public void forceShutdown() throws ServerLifecycleException {
      try {
         ServerLogger.logForceShuttingDownServer();
         this.logAdministratorAddress("Force shutdown");
         this.server.forceShutdown();
      } catch (RuntimeException var2) {
         ServerLogger.logServerRuntimeError(var2.toString());
         ServerLogger.logServerRuntimeError(StackTraceUtils.throwable2StackTrace(var2));
         throw new ServerLifecycleException(var2);
      } catch (Exception var3) {
         throw new ServerLifecycleException(var3);
      }
   }

   public void abortStartupAfterAdminState() throws ServerLifecycleException {
      this.server.abortStartupAfterAdminState();
   }

   private void logAdministratorAddress(String var1) {
      EndPoint var2 = ServerHelper.getClientEndPointInternal();
      if (var2 != null) {
         Channel var3 = var2.getRemoteChannel();
         if (var3 != null) {
            ServerLogger.logAdminAddress(var1, var3.getInetAddress().getHostAddress());
         }
      }
   }

   public void start() {
      this.server.getLockoutManager().unlockServer();
   }

   public boolean isStartupAbortedInAdminState() {
      return this.stateVal == 17 && this.server.isAbortStartupAfterAdminState();
   }

   public boolean isShuttingDownDueToFailure() {
      return this.server.isShutdownDueToFailure();
   }

   public void updateRunState(int var1) {
      int var2 = this.stateVal;
      int var3;
      if (var1 != 10 && var1 != 11) {
         var3 = this.stateVal = var1;
      } else {
         var3 = this.stateVal = 7;
      }

      this._postSet("State", getStateAsString(var2), getStateAsString(var3));
      this.sendStateToAdminServer(getStateAsString(var3));
   }

   private void sendStateToAdminServer(final String var1) {
      try {
         if (this.stateVal == 1) {
            return;
         }

         String var2 = ManagementService.getRuntimeAccess(kernelId).getAdminServerName();
         if (var2 == null) {
            return;
         }

         final RemoteLifeCycleOperations var3 = ServerLifeCycleRuntime.getLifeCycleOperationsRemote(var2);
         if (var3 == null) {
            return;
         }

         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               try {
                  var3.setState(ManagementService.getRuntimeAccess(ServerRuntime.kernelId).getServerName(), var1);
               } catch (RemoteRuntimeException var2) {
               } catch (RemoteException var3x) {
               }

            }
         });
      } catch (RemoteRuntimeException var4) {
      }

   }

   private static String getStateAsString(int var0) {
      return ServerStates.SERVERSTATES[var0];
   }

   public String getState() {
      return getStateAsString(this.stateVal);
   }

   public boolean isShuttingDown() {
      return this.stateVal == 7 || this.stateVal == 18;
   }

   public int getStateVal() {
      return this.stateVal;
   }

   public long getActivationTime() {
      return this.server.getStartTime();
   }

   public long getServerStartupTime() {
      return this.server.getStartupTime();
   }

   public String getJVMID() {
      return JVMID.localID().objectToString();
   }

   public ServerIdentity getServerIdentity() {
      return LocalServerIdentity.getIdentity();
   }

   public int getSSLListenPort() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getSSL().getListenPort();
   }

   public int getAdministrationPort() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getAdministrationPort();
   }

   public int getRestartsTotalCount() {
      return 0;
   }

   public int getOpenSocketsCurrentCount() {
      return SocketMuxer.getMuxer().getNumSockets();
   }

   public SocketRuntime[] getSockets() {
      MuxableSocket[] var1 = SocketMuxer.getMuxer().getSockets();
      SocketRuntime[] var2 = new SocketRuntime[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         MuxableSocket var4 = var1[var3];
         if (var4 instanceof SocketRuntime) {
            var2[var3] = (SocketRuntime)var4;
         } else {
            var2[var3] = null;
         }
      }

      return var2;
   }

   public ServerChannelRuntimeMBean[] getServerChannelRuntimes() {
      int var1 = this.channelRuntimes.size();
      return (ServerChannelRuntimeMBean[])((ServerChannelRuntimeMBean[])this.channelRuntimes.toArray(new ServerChannelRuntimeMBean[var1]));
   }

   public boolean addServerChannelRuntime(ServerChannelRuntimeMBean var1) {
      return this.channelRuntimes.add(var1);
   }

   public boolean removeServerChannelRuntime(ServerChannelRuntimeMBean var1) {
      return this.channelRuntimes.remove(var1);
   }

   public long getSocketsOpenedTotalCount() {
      return (long)SocketMuxer.getMuxer().getNumSockets();
   }

   public String getMiddlewareHome() {
      String var1 = this.getWeblogicHome();
      if (var1 != null) {
         int var2 = var1.lastIndexOf(File.separatorChar);
         if (var2 != -1) {
            return var1.substring(0, var2);
         }
      }

      return var1;
   }

   /** @deprecated */
   public String getOracleHome() {
      return this.getMiddlewareHome();
   }

   public String getWeblogicHome() {
      String var1 = null;

      try {
         var1 = (new File(Home.getPath())).getCanonicalPath();
      } catch (IOException var3) {
         var1 = Home.getPath();
      }

      int var2 = var1.lastIndexOf(File.separatorChar);
      return var2 != -1 ? var1.substring(0, var2) : var1;
   }

   public String getWeblogicVersion() {
      return version.getVersions();
   }

   public MessagingBridgeRuntimeMBean getMessagingBridgeRuntime() {
      return this.messagingBridgeRuntime;
   }

   public void setMessagingBridgeRuntime(MessagingBridgeRuntimeMBean var1) {
      this.messagingBridgeRuntime = var1;
   }

   public JMSRuntimeMBean getJMSRuntime() {
      return this.jmsRuntime;
   }

   public void setJMSRuntime(JMSRuntimeMBean var1) {
      this.jmsRuntime = var1;
   }

   public SAFRuntimeMBean getSAFRuntime() {
      return this.safRuntime;
   }

   public void setSAFRuntime(SAFRuntimeMBean var1) {
      this.safRuntime = var1;
   }

   public WLECConnectionServiceRuntimeMBean getWLECConnectionServiceRuntime() {
      return this.wlecConnectionService;
   }

   public void setWLECConnectionServiceRuntime(WLECConnectionServiceRuntimeMBean var1) {
      this.wlecConnectionService = var1;
   }

   public JDBCServiceRuntimeMBean getJDBCServiceRuntime() {
      return this.jdbcRuntime;
   }

   public void setJDBCServiceRuntime(JDBCServiceRuntimeMBean var1) {
      this.jdbcRuntime = var1;
   }

   public JTARuntimeMBean getJTARuntime() {
      return this.jtaRuntime;
   }

   public void setJTARuntime(JTARuntimeMBean var1) {
      this.jtaRuntime = var1;
   }

   public WTCRuntimeMBean getWTCRuntime() {
      return this.wtcRuntime;
   }

   public void setWTCRuntime(WTCRuntimeMBean var1) {
      this.wtcRuntime = var1;
   }

   public JoltConnectionServiceRuntimeMBean getJoltRuntime() {
      return this.joltRuntime;
   }

   public void setJoltRuntime(JoltConnectionServiceRuntimeMBean var1) {
      this.joltRuntime = var1;
   }

   public JVMRuntimeMBean getJVMRuntime() {
      return this.jvmRuntime;
   }

   public void setJVMRuntime(JVMRuntimeMBean var1) {
      this.jvmRuntime = var1;
   }

   public ClusterRuntimeMBean getClusterRuntime() {
      return this.clusterRuntime;
   }

   public void setClusterRuntime(ClusterRuntimeMBean var1) {
      this.clusterRuntime = var1;
   }

   public EntityCacheCurrentStateRuntimeMBean getEntityCacheCurrentStateRuntime() {
      return this.ecCurrStateRuntime;
   }

   public void setEntityCacheCurrentStateRuntime(EntityCacheCurrentStateRuntimeMBean var1) {
      this.ecCurrStateRuntime = var1;
   }

   public EntityCacheCumulativeRuntimeMBean getEntityCacheCumulativeRuntime() {
      return this.ecCumRuntime;
   }

   public void setEntityCacheCumulativeRuntime(EntityCacheCumulativeRuntimeMBean var1) {
      this.ecCumRuntime = var1;
   }

   public EntityCacheCumulativeRuntimeMBean getEntityCacheHistoricalRuntime() {
      return this.ecHistRuntime;
   }

   public void setEntityCacheHistoricalRuntime(EntityCacheCumulativeRuntimeMBean var1) {
      this.ecHistRuntime = var1;
   }

   public ThreadPoolRuntimeMBean getThreadPoolRuntime() {
      return this.threadPoolRuntime;
   }

   public void setThreadPoolRuntime(ThreadPoolRuntimeMBean var1) {
      this.threadPoolRuntime = var1;
   }

   public void setTimerRuntime(TimerRuntimeMBean var1) {
      this.timerRuntime = var1;
   }

   public TimerRuntimeMBean getTimerRuntime() {
      return this.timerRuntime;
   }

   public void setTimeServiceRuntime(TimeServiceRuntimeMBean var1) {
      this.timeServiceRuntime = var1;
   }

   public TimeServiceRuntimeMBean getTimeServiceRuntime() {
      return this.timeServiceRuntime;
   }

   public ExecuteQueueRuntimeMBean getDefaultExecuteQueueRuntime() {
      if (this.executeQueueRuntime != null) {
         return this.executeQueueRuntime;
      } else {
         ExecuteQueueRuntimeMBean var1 = this.getExecuteQueueRuntimeInternal("weblogic.kernel.Default");
         return var1 != null ? var1 : this.getExecuteQueueRuntimeInternal("default");
      }
   }

   public void setDefaultExecuteQueueRuntime(ExecuteQueueRuntimeMBean var1) {
      this.executeQueueRuntime = var1;
   }

   private ExecuteQueueRuntimeMBean getExecuteQueueRuntimeInternal(String var1) {
      ExecuteQueueRuntimeMBean[] var2 = this.getExecuteQueueRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public ExecuteQueueRuntimeMBean[] getExecuteQueueRuntimes() {
      int var1 = this.executeQueueRuntimes.size();
      return (ExecuteQueueRuntimeMBean[])((ExecuteQueueRuntimeMBean[])this.executeQueueRuntimes.toArray(new ExecuteQueueRuntimeMBean[var1]));
   }

   public boolean addExecuteQueueRuntime(ExecuteQueueRuntimeMBean var1) {
      return this.executeQueueRuntimes.add(var1);
   }

   public boolean removeExecuteQueueRuntime(ExecuteQueueRuntimeMBean var1) {
      return this.executeQueueRuntimes.remove(var1);
   }

   public WorkManagerRuntimeMBean[] getWorkManagerRuntimes() {
      int var1 = this.workManagerRuntimes.size();
      return (WorkManagerRuntimeMBean[])((WorkManagerRuntimeMBean[])this.workManagerRuntimes.toArray(new WorkManagerRuntimeMBean[var1]));
   }

   public boolean addWorkManagerRuntime(WorkManagerRuntimeMBean var1) {
      return this.workManagerRuntimes.add(var1);
   }

   public boolean removeWorkManagerRuntime(WorkManagerRuntimeMBean var1) {
      return this.workManagerRuntimes.remove(var1);
   }

   public MinThreadsConstraintRuntimeMBean lookupMinThreadsConstraintRuntime(String var1) {
      MinThreadsConstraintRuntimeMBean[] var2 = this.getMinThreadsConstraintRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public RequestClassRuntimeMBean lookupRequestClassRuntime(String var1) {
      RequestClassRuntimeMBean[] var2 = this.getRequestClassRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public MaxThreadsConstraintRuntimeMBean lookupMaxThreadsConstraintRuntime(String var1) {
      MaxThreadsConstraintRuntimeMBean[] var2 = this.getMaxThreadsConstraintRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public boolean addMaxThreadsConstraintRuntime(MaxThreadsConstraintRuntimeMBean var1) {
      return this.maxThreadsConstraintRuntimes.add(var1);
   }

   public MaxThreadsConstraintRuntimeMBean[] getMaxThreadsConstraintRuntimes() {
      int var1 = this.maxThreadsConstraintRuntimes.size();
      return (MaxThreadsConstraintRuntimeMBean[])((MaxThreadsConstraintRuntimeMBean[])this.maxThreadsConstraintRuntimes.toArray(new MaxThreadsConstraintRuntimeMBean[var1]));
   }

   public boolean addMinThreadsConstraintRuntime(MinThreadsConstraintRuntimeMBean var1) {
      return this.minThreadsConstraintRuntimes.add(var1);
   }

   public boolean addRequestClassRuntime(RequestClassRuntimeMBean var1) {
      return this.requestClassRuntimes.add(var1);
   }

   public MinThreadsConstraintRuntimeMBean[] getMinThreadsConstraintRuntimes() {
      int var1 = this.minThreadsConstraintRuntimes.size();
      return (MinThreadsConstraintRuntimeMBean[])((MinThreadsConstraintRuntimeMBean[])this.minThreadsConstraintRuntimes.toArray(new MinThreadsConstraintRuntimeMBean[var1]));
   }

   public RequestClassRuntimeMBean[] getRequestClassRuntimes() {
      int var1 = this.requestClassRuntimes.size();
      return (RequestClassRuntimeMBean[])((RequestClassRuntimeMBean[])this.requestClassRuntimes.toArray(new RequestClassRuntimeMBean[var1]));
   }

   public ServerSecurityRuntimeMBean getServerSecurityRuntime() {
      return this.serverSecurityRuntime;
   }

   public void setServerSecurityRuntime(ServerSecurityRuntimeMBean var1) {
      this.serverSecurityRuntime = var1;
   }

   public SingleSignOnServicesRuntimeMBean getSingleSignOnServicesRuntime() {
      return this.ssoRuntime;
   }

   public void setSingleSignOnServicesRuntime(SingleSignOnServicesRuntimeMBean var1) {
      this.ssoRuntime = var1;
   }

   public String getListenAddress() {
      InetSocketAddress var1 = this.getServerChannel("T3");
      if (var1 == null || var1.isUnresolved()) {
         var1 = this.getServerChannel("T3S");
         if (var1 == null || var1.isUnresolved()) {
            var1 = this.getServerChannel("ADMIN");
            if (var1 == null || var1.isUnresolved()) {
               return null;
            }
         }
      }

      String var2 = var1.getAddress().toString();
      if (var2.startsWith("/")) {
         StringBuffer var3 = new StringBuffer(var1.getHostName());
         var3.append(var2);
         var2 = var3.toString();
      }

      return var2;
   }

   public InetSocketAddress getServerChannel(String var1) {
      return ChannelService.findServerAddress(var1);
   }

   public void restartSSLChannels() {
      ((ChannelService)((ChannelService)ServerChannelManager.getServerChannelManager())).restartSSLChannels();
   }

   public String getDefaultURL() {
      return ChannelHelper.getDefaultURL();
   }

   public String getAdministrationURL() {
      return ChannelHelper.getLocalAdministrationURL();
   }

   public String getURL(String var1) {
      Protocol var2 = ProtocolManager.getProtocolByName(var1);
      return ChannelHelper.getURL(var2);
   }

   public String getIPv4URL(String var1) {
      Protocol var2 = ProtocolManager.getProtocolByName(var1);
      return ChannelHelper.getIPv4URL(var2);
   }

   public String getIPv6URL(String var1) {
      Protocol var2 = ProtocolManager.getProtocolByName(var1);
      return ChannelHelper.getIPv6URL(var2);
   }

   public int getListenPort() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getListenPort();
   }

   public boolean isListenPortEnabled() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().isListenPortEnabled();
   }

   public boolean isAdministrationPortEnabled() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().isAdministrationPortEnabled();
   }

   public boolean isSSLListenPortEnabled() {
      return ManagementService.getRuntimeAccess(kernelId).getServer().getSSL() == null ? false : ManagementService.getRuntimeAccess(kernelId).getServer().getSSL().isListenPortEnabled();
   }

   public boolean isAdminServerListenPortSecure() {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         RuntimeAccess var3 = ManagementService.getRuntimeAccess(kernelId);
         ServerMBean var2 = var3.getServer();
         if (var2 != null) {
            if (var2.isAdministrationPortEnabled()) {
               return true;
            } else {
               return !var2.isListenPortEnabled();
            }
         } else {
            return false;
         }
      } else {
         ManagementService.getPropertyService(kernelId);
         String var1 = PropertyService.getAdminHttpUrl();
         if (null == var1) {
            return false;
         } else {
            return var1.startsWith("https");
         }
      }
   }

   public int getAdminServerListenPort() {
      ServerIdentity var1 = AdminServerIdentity.getIdentity();
      if (var1 == null) {
         throw new IllegalStateException("Admin server identity is unavailable. The managed server may not be connected to the admin server");
      } else {
         String var2 = URLManager.findAdministrationURL(var1);

         try {
            return (new URI(var2)).getPort();
         } catch (URISyntaxException var4) {
            return 0;
         }
      }
   }

   public String getAdminServerHost() {
      String var1 = URLManager.findAdministrationURL(AdminServerIdentity.getIdentity());

      try {
         return (new URI(var1)).getHost();
      } catch (URISyntaxException var3) {
         return null;
      }
   }

   public String getSSLListenAddress() {
      InetSocketAddress var1 = this.getServerChannel("HTTPS");
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.getAddress().toString();
         if (var2.startsWith("/")) {
            StringBuffer var3 = new StringBuffer(var1.getHostName());
            var3.append(var2);
            var2 = var3.toString();
         }

         return var2;
      }
   }

   public HealthState getHealthState() {
      return this.healthState;
   }

   public HealthState getOverallHealthState() {
      HealthState var1 = this.getHealthState();
      HealthState[] var2 = this.getSubsystemHealthStates();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         HealthState var5 = var2[var4];
         if ((!"ApplicationRuntime".equals(var5.getMBeanType()) || !this.isInternalDeployment(var5.getMBeanName())) && !"MessageDrivenEJBRuntime".equals(var5.getMBeanType()) && var1.compareSeverityTo(var5) > 0) {
            var1 = var5;
         }
      }

      return var1;
   }

   private boolean isInternalDeployment(String var1) {
      if (var1 != null && var1.length() != 0) {
         int var2 = var1.indexOf("#");
         if (var2 > 0) {
            var1 = var1.substring(0, var2);
         }

         if (this.internalApps == null) {
            HashSet var3 = new HashSet();
            RuntimeAccess var4 = ManagementService.getRuntimeAccess(kernelId);
            DomainMBean var5 = var4.getDomain();
            AppDeploymentMBean[] var6 = var5.getInternalAppDeployments();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               AppDeploymentMBean var9 = var6[var8];
               var3.add(var9.getApplicationName());
            }

            this.internalApps = var3;
         }

         return this.internalApps.contains(var1);
      } else {
         return false;
      }
   }

   public void setHealthState(int var1, String var2) {
      HealthState var3;
      HealthState var4;
      synchronized(this) {
         var3 = this.healthState;
         var4 = new HealthState(var1, var2);
         this.healthState = var4;
      }

      this._postSet("HealthState", var3, var4);
   }

   public boolean isAdminServer() {
      return ManagementService.getRuntimeAccess(kernelId).isAdminServer() || !ManagementService.getRuntimeAccess(kernelId).isAdminServerAvailable();
   }

   public String getCurrentDirectory() {
      if (this.curDir == null) {
         this.curDir = (new File(".")).getAbsolutePath();
      }

      return this.curDir;
   }

   public ApplicationRuntimeMBean[] getApplicationRuntimes() {
      synchronized(this.applicationRuntimes) {
         int var2 = this.applicationRuntimes.size();
         return (ApplicationRuntimeMBean[])((ApplicationRuntimeMBean[])this.applicationRuntimes.toArray(new ApplicationRuntimeMBean[var2]));
      }
   }

   public ApplicationRuntimeMBean lookupApplicationRuntime(String var1) {
      synchronized(this.applicationRuntimes) {
         Iterator var3 = this.applicationRuntimes.iterator();

         ApplicationRuntimeMBean var4;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (ApplicationRuntimeMBean)var3.next();
         } while(!var4.getApplicationName().equals(var1));

         return var4;
      }
   }

   public String[] getPendingRestartSystemResources() {
      int var1 = this.pendingRestartSystemResources.size();
      return (String[])((String[])this.pendingRestartSystemResources.toArray(new String[var1]));
   }

   public boolean addPendingRestartSystemResource(String var1) {
      return this.pendingRestartSystemResources.add(var1);
   }

   public boolean removePendingRestartSystemResource(String var1) {
      return this.pendingRestartSystemResources.remove(var1);
   }

   public boolean isRestartPendingForSystemResource(String var1) {
      return this.pendingRestartSystemResources.contains(var1);
   }

   public LibraryRuntimeMBean[] getLibraryRuntimes() {
      synchronized(this.libraryRuntimes) {
         return (LibraryRuntimeMBean[])((LibraryRuntimeMBean[])this.libraryRuntimes.values().toArray(new LibraryRuntimeMBean[this.libraryRuntimes.size()]));
      }
   }

   public LibraryRuntimeMBean lookupLibraryRuntime(String var1) {
      return (LibraryRuntimeMBean)this.libraryRuntimes.get(var1);
   }

   public LogBroadcasterRuntimeMBean getLogBroadcasterRuntime() throws ManagementException {
      return LogBroadcaster.getLogBroadcaster();
   }

   public LogRuntimeMBean getLogRuntime() {
      return this.logRuntime;
   }

   public void setLogRuntime(LogRuntimeMBean var1) {
      this.logRuntime = var1;
   }

   public WLDFRuntimeMBean getWLDFRuntime() {
      return this.wldfRuntime;
   }

   public void setWLDFRuntime(WLDFRuntimeMBean var1) {
      this.wldfRuntime = var1;
   }

   public void setMANReplicationRuntime(MANReplicationRuntimeMBean var1) {
      this.manReplicationRuntime = var1;
   }

   public MANReplicationRuntimeMBean getMANReplicationRuntime() {
      return this.manReplicationRuntime;
   }

   public void setWANReplicationRuntime(WANReplicationRuntimeMBean var1) {
      this.wanReplicationRuntime = var1;
   }

   public WANReplicationRuntimeMBean getWANReplicationRuntime() {
      return this.wanReplicationRuntime;
   }

   public AsyncReplicationRuntimeMBean getAsyncReplicationRuntime() {
      return this.asyncReplicationRuntime;
   }

   public void setAsyncReplicationRuntime(AsyncReplicationRuntimeMBean var1) {
      this.asyncReplicationRuntime = var1;
   }

   public String getCurrentMachine() {
      return this.currentMachine;
   }

   public HealthState[] getSubsystemHealthStates() {
      return HealthMonitorService.getHealthStates();
   }

   public HashMap getServerServiceVersions() {
      return ServerServicesManager.getVersionsOnline();
   }

   public void setCurrentMachine(String var1) {
      this.currentMachine = var1;
   }

   public MailSessionRuntimeMBean[] getMailSessionRuntimes() {
      return (MailSessionRuntimeMBean[])((MailSessionRuntimeMBean[])this.mailSessionRuntimes.toArray(new MailSessionRuntimeMBean[this.mailSessionRuntimes.size()]));
   }

   public boolean addMailSessionRuntime(MailSessionRuntimeMBean var1) {
      return this.mailSessionRuntimes.add(var1);
   }

   public boolean removeMailSessionRuntime(MailSessionRuntimeMBean var1) {
      return this.mailSessionRuntimes.remove(var1);
   }

   public PersistentStoreRuntimeMBean[] getPersistentStoreRuntimes() {
      Collection var1 = this.persistentStoreRuntimes.values();
      return (PersistentStoreRuntimeMBean[])((PersistentStoreRuntimeMBean[])var1.toArray(new PersistentStoreRuntimeMBean[this.persistentStoreRuntimes.size()]));
   }

   public PersistentStoreRuntimeMBean lookupPersistentStoreRuntime(String var1) {
      return (PersistentStoreRuntimeMBean)this.persistentStoreRuntimes.get(var1);
   }

   public void addPersistentStoreRuntime(PersistentStoreRuntimeMBean var1) {
      this.persistentStoreRuntimes.put(var1.getName(), var1);
   }

   public void removePersistentStoreRuntime(PersistentStoreRuntimeMBean var1) {
      this.persistentStoreRuntimes.remove(var1.getName());
   }

   public ConnectorServiceRuntimeMBean getConnectorServiceRuntime() {
      return this.connectorServiceRuntime;
   }

   public void setConnectorServiceRuntime(ConnectorServiceRuntimeMBean var1) {
      this.connectorServiceRuntime = var1;
   }

   public WebServerRuntimeMBean[] getWebServerRuntimes() {
      int var1 = this.webServerRuntimes.size();
      return (WebServerRuntimeMBean[])((WebServerRuntimeMBean[])this.webServerRuntimes.toArray(new WebServerRuntimeMBean[var1]));
   }

   public boolean addWebServerRuntime(WebServerRuntimeMBean var1) {
      return this.webServerRuntimes.add(var1);
   }

   public boolean removeWebServerRuntime(WebServerRuntimeMBean var1) {
      return this.webServerRuntimes.remove(var1);
   }

   public boolean isRestartRequired() {
      return this.restartRequired;
   }

   public void setRestartRequired(boolean var1) {
      this.restartRequired = var1;
   }

   public String getServerClasspath() {
      return Classpath.get();
   }

   public PathServiceRuntimeMBean getPathServiceRuntime() {
      return this.pathServiceRuntime;
   }

   public void setPathServiceRuntime(PathServiceRuntimeMBean var1) {
      this.pathServiceRuntime = var1;
   }

   public boolean isClusterMaster() {
      MigratableServerService var1 = MigratableServerService.theOne();
      return var1 != null ? var1.isClusterMaster() : false;
   }

   public SNMPAgentRuntimeMBean getSNMPAgentRuntime() {
      return this.snmpAgentRuntime;
   }

   public void setSNMPAgentRuntime(SNMPAgentRuntimeMBean var1) {
      this.snmpAgentRuntime = var1;
   }

   public boolean isServiceAvailable(String var1) {
      return ServerServices.WLS_DEPENDENCIES.isServiceAvailable(var1);
   }

   public MANAsyncReplicationRuntimeMBean getMANAsyncReplicationRuntime() {
      return this.manAsyncReplicationRuntime;
   }

   public void setMANAsyncReplicationRuntime(MANAsyncReplicationRuntimeMBean var1) {
      this.manAsyncReplicationRuntime = var1;
   }

   public int getStableState() {
      return this.server.getStableState();
   }

   public void setWseeWsrmRuntime(WseeWsrmRuntimeMBean var1) {
      this.wseeWsrmRuntime = var1;
   }

   public WseeWsrmRuntimeMBean getWseeWsrmRuntime() {
      return this.wseeWsrmRuntime;
   }

   public void setWseeClusterFrontEndRuntime(WseeClusterFrontEndRuntimeMBean var1) {
      this.wseeClusterFrontEndRuntime = var1;
   }

   public WseeClusterFrontEndRuntimeMBean getWseeClusterFrontEndRuntime() {
      return this.wseeClusterFrontEndRuntime;
   }
}
