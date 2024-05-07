package weblogic.cluster.singleton;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Locale;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.health.HealthMonitorService;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.NodeManagerMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.DomainAccessSettable;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.server.channels.AddressUtils;
import weblogic.store.io.jdbc.JDBCHelper;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class MigratableServerService extends AbstractServerService implements MigratableServiceConstants, LeaseLostListener, PropertyChangeListener, LeaseObtainedListener, NakedTimerListener {
   private String serverName;
   private MachineMBean machine;
   private ServerMBean server;
   private boolean failedAndShouldMigrate = false;
   private boolean isMigratableServer;
   private boolean isMigratableCluster;
   private ClusterMaster clusterMaster;
   private SingletonMaster singletonMaster;
   private ServerMigrationCoordinator coordinator;
   private int triggerIntervalMillis;
   private int idlePeriodsUntilTimeout;
   private static final boolean DEBUG = MigrationDebugLogger.isDebugEnabled();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static MigratableServerService theMigratableServerService;
   private LeaseManager serverLeaseManager;
   private LeaseManager servicesLeaseManager;
   private String leasingType;
   private boolean clusterMasterUpdated = false;
   private Timer timer;
   private static final String UNRESOLVEABLE_JNDI_NAME = "__UNKNOWN";
   boolean BEADriver = false;

   public void initialize() throws ServiceFailureException {
      theMigratableServerService = this;
      RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
      if (var1.isAdminServer()) {
         this.coordinator = new ServerMigrationCoordinatorImpl();
         this.bindServerMigrationCoordinator(this.coordinator);
      }

      this.server = var1.getServer();
      this.isMigratableServer = this.server.isAutoMigrationEnabled();
      ClusterMBean var2 = this.server.getCluster();
      if (var2 != null) {
         this.leasingType = var2.getMigrationBasis();
      }

      this.serverName = this.server.getName();
      this.triggerIntervalMillis = var2 == null ? 0 : var2.getHealthCheckIntervalMillis();
      this.idlePeriodsUntilTimeout = var2 == null ? 0 : var2.getHealthCheckPeriodsUntilFencing();
      if (this.isMigratableServer && var2 == null) {
         throw new ServiceFailureException("Migratable server is not part of a cluster");
      } else {
         if (this.isMigratableServer) {
            verifyMachineConfiguration(this.server);
            var1.getServerRuntime().addPropertyChangeListener(this);
            this.machine = this.getCurrentMachine();
            if (this.machine == null) {
               throw new AssertionError("Failed to find the current machine");
            }

            var1.getServerRuntime().setCurrentMachine(this.machine.getName());
         } else {
            this.machine = this.server.getMachine();
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("MigratableServerService.initialize() : Final current machine = " + this.machine);
            }

            if (this.machine == null) {
               var1.getServerRuntime().setCurrentMachine("");
            } else {
               var1.getServerRuntime().setCurrentMachine(this.machine.getName());
            }
         }

         this.isMigratableCluster = this.isMigratableServer || isMigratableCluster(var2);
         if (DEBUG) {
            p("Initializing migratable service: " + this.isMigratableCluster);
         }

         if (var2 != null && ClusterService.getServices().getDefaultLeasingBasis() != null) {
            this.servicesLeaseManager = ClusterService.getServices().getDefaultLeaseManager("service");
            this.singletonMaster = new SingletonMaster(this.servicesLeaseManager, this.triggerIntervalMillis);
            this.servicesLeaseManager.start();
            SingletonServicesManager.getInstance().start();
         }

         if (this.isMigratableCluster) {
            try {
               this.serverLeaseManager = ClusterService.getServices().getDefaultLeaseManager("wlsserver");
               this.serverLeaseManager.start();
               this.clusterMaster = new ClusterMaster(this.triggerIntervalMillis);
            } catch (Throwable var4) {
               throw new ServiceFailureException("Failed to start singleton service because of: " + var4, var4);
            }
         }

      }
   }

   public String getLeasingType() {
      return this.leasingType;
   }

   public static MigratableServerService theOne() {
      return theMigratableServerService;
   }

   public boolean isClusterMaster() {
      return this.clusterMaster == null ? false : this.clusterMaster.isClusterMaster();
   }

   String findClusterMaster() throws LeasingException {
      if (this.isClusterMaster()) {
         return this.serverName;
      } else {
         return this.servicesLeaseManager == null ? null : this.unwrapServerID(this.servicesLeaseManager.findOwner("CLUSTER_MASTER"));
      }
   }

   ClusterMasterRemote getClusterMasterRemote() throws LeasingException {
      if (this.isClusterMaster()) {
         return this.clusterMaster;
      } else {
         String var1 = this.findClusterMaster();
         return this.getClusterMaster(var1);
      }
   }

   public boolean isSingletonMaster() {
      return this.singletonMaster == null ? false : this.singletonMaster.isSingletonMaster();
   }

   public String findSingletonMaster() throws LeasingException {
      if (this.isSingletonMaster()) {
         return this.serverName;
      } else {
         return this.servicesLeaseManager == null ? null : this.unwrapServerID(this.servicesLeaseManager.findOwner("SINGLETON_MASTER"));
      }
   }

   public SingletonMonitorRemote getSingletonMasterRemote() throws LeasingException {
      return this.getSingletonMasterRemote(1);
   }

   public SingletonMonitorRemote getSingletonMasterRemote(int var1) throws LeasingException {
      if (this.isSingletonMaster()) {
         return this.singletonMaster.getSingletonMonitor();
      } else {
         String var2 = null;
         LeasingException var3 = new LeasingException("No singleton master found");

         for(int var4 = 0; var4 < var1; ++var4) {
            if (var4 > 0) {
               try {
                  Thread.sleep(5000L);
               } catch (InterruptedException var6) {
               }
            }

            try {
               var2 = this.findSingletonMaster();
               SingletonMonitorRemote var5 = this.getSingletonMaster(var2);
               if (var5 != null) {
                  return var5;
               }

               var3 = new LeasingException("Could not contact singleton master: " + var2);
            } catch (LeasingException var7) {
               var3 = var7;
               if (DEBUG) {
                  p("getSingletonMasterRemote Failed - " + var7.getCause());
               }
            }
         }

         throw var3;
      }
   }

   public static String findURLOfUnconnectedServer(final String var0) {
      PrivilegedExceptionAction var1 = new PrivilegedExceptionAction() {
         public Object run() {
            try {
               String var1 = MigratableServerService.getURLSuggestion(var0);
               Environment var2 = new Environment();
               var2.setProviderUrl(var1);
               Context var3 = var2.getInitialContext();
               var3.lookup("__UNKNOWN");
               return null;
            } catch (NameNotFoundException var4) {
               return null;
            } catch (NamingException var5) {
               return var5;
            }
         }
      };

      try {
         Object var2 = SecurityManager.runAs(kernelId, SubjectUtils.getAnonymousSubject(), var1);
         if (var2 != null) {
            return null;
         }
      } catch (PrivilegedActionException var4) {
         throw new AssertionError(var4);
      }

      try {
         return URLManager.findAdministrationURL(var0);
      } catch (UnknownHostException var3) {
         return null;
      }
   }

   private static String getURLSuggestion(String var0) {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var0);
      String var2 = var1.getDefaultProtocol();
      String var3 = var1.getListenAddress();
      if (var3 == null || var3.equals("")) {
         var3 = "localhost";
      }

      if (var1.isListenPortEnabled()) {
         int var8 = var1.getListenPort();
         if (DEBUG) {
            p("Chosen url: " + var2 + "://" + var3 + ":" + var8);
         }

         return var2 + "://" + var3 + ":" + var8;
      } else {
         int var5;
         if (var1.getSSL() != null && var1.getSSL().isEnabled()) {
            SSLMBean var7 = var1.getSSL();
            if (!var2.endsWith("s")) {
               var2 = var2 + "s";
            }

            var5 = var7.getListenPort();
            if (DEBUG) {
               p("chosen SSL url: " + var2 + "://" + var3 + ":" + var5);
            }

            return var2 + "://" + var3 + ":" + var5;
         } else {
            NetworkAccessPointMBean[] var4 = var1.getNetworkAccessPoints();

            for(var5 = 0; var5 < var4.length; ++var5) {
               if (var4[var5].isEnabled()) {
                  String var6 = var4[var5].getListenAddress();
                  if (var6 != null && !var6.equals("")) {
                     var3 = var6;
                  }

                  if (DEBUG) {
                     p("chosen NAP url: " + var4[var5].getProtocol() + "://" + var3 + ":" + var4[var5].getListenPort());
                  }

                  return var4[var5].getProtocol() + "://" + var3 + ":" + var4[var5].getListenPort();
               }
            }

            return null;
         }
      }
   }

   private SingletonMonitorRemote getSingletonMaster(String var1) throws LeasingException {
      if (var1 == null) {
         return null;
      } else {
         Environment var2 = new Environment();
         Context var3 = null;

         SingletonMonitorRemote var5;
         try {
            String var4 = null;

            try {
               var4 = URLManager.findAdministrationURL(var1);
            } catch (UnknownHostException var16) {
               var4 = findURLOfUnconnectedServer(var1);
            }

            if (var4 != null) {
               if (DEBUG) {
                  p("Looking up SingletonMonitorRemote on " + var1 + " with url " + var4);
               }

               var2.setProviderUrl(var4);
               var3 = var2.getInitialContext();
               var5 = (SingletonMonitorRemote)var3.lookup("weblogic/cluster/singleton/SingletonMonitorRemote");
               return var5;
            }

            var5 = null;
         } catch (NamingException var17) {
            throw new LeasingException("Error connecting to Singleton Monitor at " + var1 + " : " + var17, var17);
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (NamingException var15) {
               }
            }

         }

         return var5;
      }
   }

   private ClusterMasterRemote getClusterMaster(String var1) {
      if (var1 == null) {
         return null;
      } else {
         Environment var2 = new Environment();
         Context var3 = null;

         ClusterMasterRemote var5;
         try {
            String var4 = findURLOfUnconnectedServer(var1);
            if (DEBUG) {
               p("Looking up " + var1 + " at " + var4);
            }

            if (var4 != null) {
               var2.setProviderUrl(var4);
               var3 = var2.getInitialContext();
               var5 = (ClusterMasterRemote)var3.lookup("weblogic/cluster/singleton/ClusterMasterRemote");
               return var5;
            }

            var5 = null;
            return var5;
         } catch (NamingException var16) {
            if (DEBUG) {
               var16.printStackTrace();
            }

            var5 = null;
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (NamingException var15) {
               }
            }

         }

         return var5;
      }
   }

   private String unwrapServerID(String var1) {
      if (var1 == null) {
         return null;
      } else {
         return var1.indexOf("/") == -1 ? var1 : var1.substring(var1.indexOf("/") + 1, var1.length());
      }
   }

   private static void verifyMachineConfiguration(ServerMBean var0) throws ServiceFailureException {
      ClusterMBean var1 = var0.getCluster();
      MachineMBean[] var2 = var1.getCandidateMachinesForMigratableServers();
      MachineMBean[] var3 = var0.getCandidateMachines();
      HashSet var4 = new HashSet();
      if (var3 == null && var2 == null) {
         ClusterLogger.logMisconfiguredMigratableCluster();
         throw new ServiceFailureException("Invalid migratable cluster configuration");
      } else {
         int var5;
         if (var3 != null) {
            for(var5 = 0; var5 < var3.length; ++var5) {
               var4.add(var3[var5]);
            }
         }

         if (var2 != null) {
            for(var5 = 0; var5 < var2.length; ++var5) {
               var4.add(var2[var5]);
            }
         }

         if (var4.size() < 2) {
            ClusterLogger.logMigratableServerNotTargetToAMachine(var0.getName());
         }

      }
   }

   public void start() throws ServiceFailureException {
      if (DEBUG) {
         p("Starting migratable service");
      }

      this.initialize();
      this.initializeMigrationMonitoring();
      if (this.singletonMaster != null) {
         this.singletonMaster.start();
      }

      if (this.isMigratableCluster) {
         this.clusterMaster.start();
         this.serverLeaseManager.addLeaseLostListener(this);
      }

      if (this.isMigratableServer) {
         if (this.isConsensusLeasing()) {
            WorkManager var1 = WorkManagerFactory.getInstance().findOrCreate("WorkManagerForConsensusLeasing", 2, -1);
            this.timer = TimerManagerFactory.getTimerManagerFactory().getTimerManager("ConsensusLeasingTimerManager", var1).schedule(this, 0L, (long)this.triggerIntervalMillis);
            if (this.timer.isCancelled()) {
               this.timer = null;
            }
         } else {
            boolean var4 = false;

            try {
               var4 = this.serverLeaseManager.tryAcquire(this.serverName);
               this.updateClusterMaster();
            } catch (LeasingException var3) {
               ClusterLogger.logDatabaseUnreachable("startup", "database");
               if (MigrationDebugLogger.isDebugEnabled()) {
                  MigrationDebugLogger.debug("Failed to obtain lease: " + var3, var3);
               }

               throw new ServiceFailureException("There was a problem contacting the database: " + var3, var3);
            }

            if (!var4) {
               throw new ServiceFailureException("There is either a problem contacting the database or there is another instance of " + this.serverName + " running");
            }
         }

      }
   }

   public void timerExpired(Timer var1) {
      try {
         String var2 = null;
         boolean var3 = false;

         try {
            var2 = this.serverLeaseManager.findOwner(this.serverName);
            if (var2 == null) {
               if (MigrationDebugLogger.isDebugEnabled()) {
                  MigrationDebugLogger.debug("Found no owner for " + this.serverName + ", will acquire server lease");
               }

               if (this.acquireServerLease()) {
                  this.updateClusterMaster();
                  this.stopTimer(var1);
                  return;
               }

               if (MigrationDebugLogger.isDebugEnabled()) {
                  MigrationDebugLogger.debug("Could not acquire available server lease for " + this.serverName + " at this time, will retry");
               }

               return;
            }

            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Another owner was found for lease of: " + this.serverName + ", other owner: " + var2);
            }

            var3 = true;
         } catch (LeasingException var6) {
            Throwable var5 = var6.getCause();
            if (var5 == null || !(var5 instanceof ClusterReformationInProgressException)) {
               throw var6;
            }

            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Using PrimordialLeasingBasis to find owner for " + this.serverName);
            }

            var2 = this.serverLeaseManager.findOwner(this.serverName);
            if (var2 != null) {
               var3 = true;
            }
         }

         if (var3) {
            String var4 = "Another server " + var2 + " has the consensus lease for: " + this.serverName;
            this.failShutDown(var4);
         }
      } catch (LeasingException var7) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Got exception trying to obtain consensus lease: " + this.serverName, var7);
         }
      }

   }

   private void failShutDown(String var1) {
      if (MigrationDebugLogger.isDebugEnabled()) {
         MigrationDebugLogger.debug(var1);
      }

      try {
         this.stop();
      } catch (ServiceFailureException var3) {
      }

      HealthMonitorService.subsystemFailedForceShutdown("MigratableServerService", var1);
   }

   private synchronized void stopTimer(Timer var1) {
      if (var1 != null) {
         var1.cancel();
      }

      if (this.timer != null) {
         this.timer = null;
      }

   }

   private boolean acquireServerLease() throws LeasingException {
      boolean var1 = false;
      if (this.serverLeaseManager.tryAcquire(this.serverName)) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("MigratableServerService has obtained the consensus lease for: " + this.serverName + "\nWill cancel the timer and add the LeaseListener");
         }

         var1 = true;
      }

      return var1;
   }

   private void initializeMigrationMonitoring() throws ServiceFailureException {
      try {
         ServerMigrationRuntimeMBeanImpl.initialize();
         ServiceMigrationRuntimeMBeanImpl.initialize();
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         if (var1.isAdminServer()) {
            ((DomainAccessSettable)ManagementService.getDomainAccess(kernelId)).setDomainMigrationHistory(DomainMigrationHistoryImpl.THE_ONE);
            ServerHelper.exportObject(DomainMigrationHistoryImpl.THE_ONE);
         }

      } catch (ManagementException var2) {
         throw new ServiceFailureException(var2);
      } catch (RemoteException var3) {
         throw new ServiceFailureException(var3);
      }
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      this.stopTimer(this.timer);
      if (this.singletonMaster != null) {
         this.singletonMaster.stop();
      }

      SingletonServicesManager.getInstance().stop();
      if (this.isMigratableCluster) {
         this.servicesLeaseManager.voidLeases();
         this.serverLeaseManager.stop();
      }

      if (this.isMigratableServer) {
         if (!this.failedAndShouldMigrate) {
            try {
               this.serverLeaseManager.release(this.serverName);
            } catch (LeasingException var2) {
               ClusterLogger.logDatabaseUnreachable("shutdown", this.getLeaseBasisLocation());
               if (MigrationDebugLogger.isDebugEnabled()) {
                  MigrationDebugLogger.debug("Failed to obtain lease: " + var2, var2);
               }
            }

         }
      }
   }

   private boolean isConsensusLeasing() {
      return "consensus".equalsIgnoreCase(this.server.getCluster().getMigrationBasis());
   }

   private String getLeaseBasisLocation() {
      if (this.isConsensusLeasing()) {
         String var1 = AbstractConsensusService.getInstance().getLeasingBasisLocation();
         return var1 != null ? "leasing basis hosted by " + var1 : "consensus leasing basis";
      } else {
         return "database";
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if ("State".equals(var1.getPropertyName())) {
         String var2 = (String)var1.getNewValue();
         if ("FAILED".equals(var2)) {
            this.failedAndShouldMigrate = true;
         }
      }

   }

   private void bindServerMigrationCoordinator(final ServerMigrationCoordinator var1) {
      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Context var1x = null;

               try {
                  Environment var2 = new Environment();
                  var2.setCreateIntermediateContexts(true);
                  var1x = var2.getInitialContext();
                  var1x.bind("weblogic/cluster/singleton/ServerMigrationCoordinator", var1);
               } catch (NamingException var11) {
                  throw new AssertionError("Unexpected exception" + var11);
               } finally {
                  if (var1x != null) {
                     try {
                        var1x.close();
                     } catch (NamingException var10) {
                     }
                  }

               }

               return null;
            }
         });
      } catch (Exception var3) {
         throw new AssertionError("Unexpected exception" + var3);
      }
   }

   private MachineMBean getConfiguredMachine() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(this.serverName).getMachine();
   }

   private boolean isLocalMachine(MachineMBean var1) throws java.net.UnknownHostException {
      NodeManagerMBean var2 = var1.getNodeManager();
      if (var2 == null) {
         return true;
      } else {
         InetAddress var3 = InetAddress.getByName(var2.getListenAddress());
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("MigratableServerService.isLocalMachine() : Inet address = " + var3);
         }

         for(int var4 = 0; var4 < AddressUtils.getIPAny().length; ++var4) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("MigratableServerService.isLocalMachine() : AddressUtils.getIPAny[" + var4 + "]=" + AddressUtils.getIPAny()[var4]);
            }

            if (var3.equals(AddressUtils.getIPAny()[var4])) {
               MigrationDebugLogger.debug("MigratableServerService.isLocalMachine() : selected " + var1);
               return true;
            }
         }

         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("MigratableServerService.isLocalMachine() : returned false");
         }

         return false;
      }
   }

   public MachineMBean getCurrentMachine() {
      try {
         MachineMBean var1 = this.getConfiguredMachine();
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("MigratableServerService.getCurrentMachine() : configured machine = " + var1);
         }

         if (var1 == null) {
            return null;
         } else if (this.isLocalMachine(var1)) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("MigratableServerService.getCurrentMachine() : configured machine = " + var1 + " matches machine from local list ");
            }

            return var1;
         } else {
            MachineMBean[] var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getMachines();
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("MigratableServerService.getCurrentMachine() : now going to domain mbean to get list of machines = " + var2);
            }

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (this.isLocalMachine(var2[var3])) {
                  return var2[var3];
               }
            }

            return null;
         }
      } catch (java.net.UnknownHostException var4) {
         throw new AssertionError("Unexpected exception" + var4);
      }
   }

   private static boolean isMigratableCluster(ClusterMBean var0) {
      if (var0 == null) {
         return false;
      } else {
         ServerMBean[] var1 = var0.getServers();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].isAutoMigrationEnabled()) {
               return true;
            }
         }

         return false;
      }
   }

   public void onRelease() {
      ClusterLogger.logServerFailedtoRenewLease(this.serverName, this.getLeaseBasisLocation());
      ClusterLogger.logDatabaseUnreachableForLeaseRenewal(this.idlePeriodsUntilTimeout * this.triggerIntervalMillis / 1000, this.getLeaseBasisLocation());
      HealthMonitorService.subsystemFailedForceShutdown("ServerMigration", "Server" + this.serverName + " failed to renew lease in the " + this.getLeaseBasisLocation());
   }

   boolean isBEADriver() {
      return this.BEADriver;
   }

   QueryHelper identifyVendorSpecificQuery(String var1, Connection var2) {
      String var3 = this.server.getCluster().getSingletonSQLQueryHelper();
      String var4 = this.server.getParent().getName();
      String var5 = this.server.getCluster().getName();

      try {
         String var6 = var2.getMetaData().getDriverName().toLowerCase(Locale.ENGLISH);
         if (!var6.startsWith("oracle jdbc")) {
            this.BEADriver = true;
         }
      } catch (SQLException var11) {
         throw new AssertionError("Could not contact database to get vendor and version: " + var11);
      }

      if (var3 != null && var3.length() > 0) {
         try {
            Class var12 = Class.forName(var3);
            Constructor var13 = var12.getConstructor((Class[])null);
            QueryHelper var8 = (QueryHelper)var13.newInstance((Object[])null);
            var8.init(var1, var4, var5, JDBCHelper.getDBMSType(var2.getMetaData(), (String[])null));
            return var8;
         } catch (Throwable var9) {
            ClusterLogger.logUnableToLoadCustomQueryHelper(var3, var9);
            AssertionError var7 = new AssertionError("Failed to load " + var3 + " because of " + var9);
            var7.initCause(var9);
            throw var7;
         }
      } else {
         try {
            return new QueryHelperImpl(var1, var4, var5, JDBCHelper.getDBMSType(var2.getMetaData(), (String[])null));
         } catch (SQLException var10) {
            throw new AssertionError("Could not contact database to get vendor and version: " + var10);
         }
      }
   }

   private static final void p(String var0) {
      System.out.println("<MigratableServerService>: " + var0);
   }

   public void onAcquire(String var1) {
      this.updateClusterMaster();
   }

   public void onException(Exception var1, String var2) {
   }

   private void updateClusterMaster() {
      if (!this.clusterMasterUpdated) {
         try {
            ClusterMasterRemote var1 = this.getClusterMasterRemote();
            if (var1 != null) {
               var1.setServerLocation(this.serverName, this.machine.getName());
            }

            this.clusterMasterUpdated = true;
         } catch (LeasingException var2) {
            if (DEBUG) {
               var2.printStackTrace();
            }
         } catch (RemoteException var3) {
            if (DEBUG) {
               var3.printStackTrace();
            }
         }

      }
   }

   public void notifySingletonMasterShutdown() {
      try {
         SingletonMonitorRemote var1 = this.getSingletonMasterRemote();
         if (DEBUG) {
            p("MigratabaleServerService.notifyClusterMasterShutdown server: " + this.serverName + ", ClusterMaster: " + this.clusterMaster);
         }

         if (var1 != null) {
            var1.notifyShutdown(this.serverName);
         }
      } catch (LeasingException var2) {
         if (DEBUG) {
            p(var2.toString());
         }
      }

   }
}
