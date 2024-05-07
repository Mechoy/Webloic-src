package weblogic.servlet.cluster;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import weblogic.jndi.Environment;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.cluster.wan.BatchedSessionState;
import weblogic.servlet.cluster.wan.Invalidate;
import weblogic.servlet.cluster.wan.PersistenceService;
import weblogic.servlet.cluster.wan.PersistenceServiceControl;
import weblogic.servlet.cluster.wan.PersistenceServiceImpl;
import weblogic.servlet.cluster.wan.PersistenceServiceInternal;
import weblogic.servlet.cluster.wan.ServiceUnavailableException;
import weblogic.servlet.cluster.wan.SessionDiff;
import weblogic.servlet.cluster.wan.Update;
import weblogic.servlet.internal.session.HTTPSessionLogger;
import weblogic.servlet.internal.session.WANSessionData;
import weblogic.servlet.utils.ServletObjectInputStream;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.StopTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.AssertionError;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class WANPersistenceManager implements PersistenceService, PersistenceServiceControl {
   private static final int DEFAULT_MAX_CONCURRENCY = 5;
   private String selectQuery;
   private static String isSessionValidQuery;
   private long timeAtLastInvalidateFlush;
   private final int UPDATE_SIZE;
   private final int INVALIDATE_SIZE;
   private final String backupClusterAddress;
   private final String dataSourceName;
   private final DataSource dataSource;
   private final int sessionFlushInterval;
   private final ArrayList pendingUpdates;
   private final ArrayList pendingInvalidates;
   private final HashSet updateSet;
   private final HashSet invalidateSet;
   private final WorkManager workManager;
   private final PersistenceServiceInternal localService;
   private final Timer updateTimer;
   private final Timer invalidateTimer;
   private long timeAtLastUpdateFlush;
   private int updateIndex;
   private int invalidateIndex;
   private LinkLivenessChecker linkChecker;
   private final TimerManager sessionUpdateFlushTimerManager;
   private final TimerManager sessionInvalidateFlushTimerManager;
   private final boolean isServiceAvailable;
   private final WANReplicationRuntime runtime;
   private final boolean updateOnlyOnShutdown;
   private String srvrState;

   public static final PersistenceService getInstance() {
      return WANPersistenceManager.SingletonMaker.singleton;
   }

   public static final PersistenceServiceControl getControlInstance() {
      return WANPersistenceManager.SingletonMaker.singleton;
   }

   private void initQueryStrings(String var1) {
      this.selectQuery = " SELECT WL_SESSION_ATTRIBUTE_KEY, WL_SESSION_ATTRIBUTE_VALUE FROM " + var1 + " WHERE WL_ID = ? AND WL_CONTEXT_PATH = ? AND WL_INTERNAL_ATTRIBUTE = ?";
      isSessionValidQuery = "SELECT WL_CREATE_TIME, WL_ACCESS_TIME, WL_MAX_INACTIVE_INTERVAL, WL_VERSION FROM " + var1 + " WHERE WL_ID = ? AND WL_CONTEXT_PATH = ? AND" + " WL_VERSION = (SELECT MAX(WL_VERSION) from " + var1 + " WHERE WL_ID = ? AND" + " WL_CONTEXT_PATH = ?)";
   }

   private WANPersistenceManager() {
      this.pendingUpdates = new ArrayList();
      this.pendingInvalidates = new ArrayList();
      this.timeAtLastUpdateFlush = 0L;
      this.updateIndex = 0;
      this.invalidateIndex = 0;
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ClusterMBean var2 = ManagementService.getRuntimeAccess(var1).getServer().getCluster();
      this.initQueryStrings(var2.getWANSessionPersistenceTableName());
      this.UPDATE_SIZE = var2.getSessionFlushThreshold() == -1 ? 0 : var2.getSessionFlushThreshold();
      this.INVALIDATE_SIZE = this.UPDATE_SIZE / 10 == 0 ? 1 : this.UPDATE_SIZE / 10;
      this.sessionFlushInterval = var2.getSessionFlushInterval() == -1 ? 0 : var2.getSessionFlushInterval() * 1000;
      this.updateOnlyOnShutdown = this.UPDATE_SIZE + this.sessionFlushInterval == 0 || var2.getPersistSessionsOnShutdown() && var2.getClusterType() != "wan";
      JDBCSystemResourceMBean var3 = var2.getDataSourceForSessionPersistence();
      if (var3 != null) {
         String[] var4 = var3.getJDBCResource().getJDBCDataSourceParams().getJNDINames();
         if (var4 != null && var4.length > 0) {
            this.dataSourceName = var4[0];
         } else {
            this.dataSourceName = null;
         }
      } else {
         this.dataSourceName = null;
      }

      this.backupClusterAddress = var2.getRemoteClusterAddress();
      this.dataSource = this.lookupDataSource();
      this.workManager = WorkManagerFactory.getInstance().findOrCreate("WAN_ASYNC_SESSION_FLUSH_WM", -1, getMaxThreadsConstraint(var3));
      this.localService = this.dataSource != null ? new PersistenceServiceImpl(this.dataSource) : null;
      this.sessionUpdateFlushTimerManager = this.dataSource != null ? TimerManagerFactory.getTimerManagerFactory().getTimerManager("sessionUpdateFlushTimerManager") : null;
      this.sessionInvalidateFlushTimerManager = this.dataSource != null ? TimerManagerFactory.getTimerManagerFactory().getTimerManager("sessionInvalidateFlushTimerManager") : null;
      this.isServiceAvailable = this.dataSource != null;
      this.startLivenessLinkChecker(var2);
      this.runtime = this.getWANReplicationRuntime(ManagementService.getRuntimeAccess(var1).getServer().getName());
      this.updateSet = createUpdateSet(this.UPDATE_SIZE);
      this.invalidateSet = createInvalidationSet(this.INVALIDATE_SIZE);
      this.updateTimer = this.scheduleSessionUpdateTimer();
      this.invalidateTimer = this.scheduleInvalidationTimer();
      this.srvrState = this.isServiceAvailable ? "RUNNING" : "";
   }

   private static int getMaxThreadsConstraint(JDBCSystemResourceMBean var0) {
      return var0 == null ? 5 : var0.getJDBCResource().getJDBCConnectionPoolParams().getMaxCapacity();
   }

   private Timer scheduleSessionUpdateTimer() {
      if (!this.isServiceAvailable) {
         return null;
      } else {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Session Flush Interval " + this.sessionFlushInterval + "ms" + " and threshold is " + this.UPDATE_SIZE);
         }

         return this.sessionUpdateFlushTimerManager.schedule(new SessionUpdateFlushTrigger(this, this.sessionFlushInterval), (long)this.sessionFlushInterval, (long)this.sessionFlushInterval);
      }
   }

   private Timer scheduleInvalidationTimer() {
      if (!this.isServiceAvailable) {
         return null;
      } else {
         int var1 = this.sessionFlushInterval / 2;
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Session Invalidation Interval " + var1 + "ms " + " and threshold is " + this.INVALIDATE_SIZE);
         }

         return this.sessionInvalidateFlushTimerManager.schedule(new SessionInvalidateFlushTrigger(this), (long)var1, (long)var1);
      }
   }

   private static HashSet createUpdateSet(int var0) {
      return var0 == 0 ? new HashSet(5) : new HashSet(var0);
   }

   private static HashSet createInvalidationSet(int var0) {
      return var0 == 0 ? new HashSet(5) : new HashSet(var0);
   }

   private void startLivenessLinkChecker(ClusterMBean var1) {
      if (this.backupClusterAddress != null) {
         boolean var2 = false;
         if (var1 != null) {
            var2 = ((DomainMBean)var1.getParent()).getSecurityConfiguration().isCrossDomainSecurityEnabled();
         }

         this.linkChecker = new LinkLivenessChecker(this.backupClusterAddress, var1, var2);
         this.linkChecker.resume();
      }
   }

   private WANReplicationRuntime getWANReplicationRuntime(String var1) {
      if (this.dataSource == null) {
         return null;
      } else {
         try {
            return new WANReplicationRuntime(var1);
         } catch (ManagementException var3) {
            throw new AssertionError("Unexpected exception", var3);
         }
      }
   }

   private DataSource lookupDataSource() {
      if (this.dataSourceName == null) {
         return null;
      } else {
         InitialContext var1 = null;

         Object var3;
         try {
            var1 = new InitialContext();
            DataSource var2 = (DataSource)var1.lookup(this.dataSourceName);
            return var2;
         } catch (NamingException var13) {
            HTTPSessionLogger.logWANSessionConfigurationError();
            var3 = null;
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (NamingException var12) {
               }
            }

         }

         return (DataSource)var3;
      }
   }

   private PersistenceServiceInternal getPersistenceServiceInternal() {
      return this.backupClusterAddress == null ? ((PersistenceServiceImpl)this.localService).getLocalService() : this.linkChecker.getRemotePersistenceService();
   }

   public void update(String var1, long var2, String var4, int var5, long var6, SessionDiff var8) {
      if (!this.updateOnlyOnShutdown) {
         boolean var9 = false;
         HashSet var10 = null;
         synchronized(this) {
            this.updateSet.add(new Update(var1, var4, var2, var5, var6, var8));
            ++this.updateIndex;
            var9 = this.updateIndex == this.UPDATE_SIZE;
            if (var9) {
               var10 = (HashSet)this.updateSet.clone();
               this.updateSet.clear();
               this.updateIndex = 0;
               this.timeAtLastUpdateFlush = System.currentTimeMillis();
            }
         }

         if (var9) {
            this.workManager.schedule(new FlushWork(var10, this));
         }

      }
   }

   public void flushUponShutdown(String var1, long var2, String var4, int var5, long var6, SessionDiff var8) {
      this.updateSet.add(new Update(var1, var4, var2, var5, var6, var8));
      PersistenceServiceInternal var9 = this.getPersistenceServiceInternal();
      if (var9 != null) {
         this.flush(var9, this.updateSet);
      }
   }

   public boolean fetchState(String var1, String var2, WANSessionData var3) {
      Connection var4 = null;

      try {
         var4 = this.dataSource.getConnection();
      } catch (SQLException var23) {
         if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Failed to obtain database connection", var23);
         }

         return false;
      }

      PreparedStatement var5 = null;
      ResultSet var6 = null;

      try {
         label231: {
            boolean var8;
            try {
               var6 = isSessionValid(var4, var1, var2);
               if (!var6.next()) {
                  if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                     WANReplicationDetailsDebugLogger.debug("Session is invalid");
                  }

                  boolean var28 = false;
                  return var28;
               }

               long var7 = var6.getLong(1);
               long var9 = var6.getLong(2);
               int var11 = var6.getInt(3);
               long var12 = System.currentTimeMillis();
               if (var12 - var9 <= (long)(var11 * 1000)) {
                  var3.setLastAccessedTime(var9);
                  var3.setMaxInactiveInterval(var11);
                  var3.setCreationTime(var7);
                  SessionDiff var29 = new SessionDiff();
                  int var15 = var6.getInt(4);
                  if (var15 > var29.getVersionCount()) {
                     var29.setVersionCounter(var15);
                  }

                  var5 = var4.prepareStatement(this.selectQuery);
                  var6 = populateInternalAttributes(var5, var1, var2, var6, var29);
                  var5 = var4.prepareStatement(this.selectQuery);
                  var6 = populateAttributes(var5, var1, var2, var6, var29);
                  var3.applySessionDiff(var29);
                  break label231;
               }

               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Session expired");
                  WANReplicationDetailsDebugLogger.debug("CURRENT TIME " + var12);
                  WANReplicationDetailsDebugLogger.debug("Last Access " + var9);
                  WANReplicationDetailsDebugLogger.debug("MAX INACTIVE " + var11);
                  WANReplicationDetailsDebugLogger.debug("RESULT " + (var12 - var9 > (long)(var11 * 1000)));
               }

               boolean var14 = false;
               return var14;
            } catch (SQLException var24) {
               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Failed to updateIndex the database", var24);
               }

               var8 = false;
               return var8;
            } catch (IOException var25) {
               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Failed while deserializing the attribute for user session with id: " + var1, var25);
               }
            } catch (ClassNotFoundException var26) {
               if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
                  WANReplicationDetailsDebugLogger.debug("Failed while deserializing the session attributes for user session  with id: " + var1, var26);
               }

               var8 = false;
               return var8;
            }

            var8 = false;
            return var8;
         }
      } finally {
         close(var5, var6, var4);
      }

      this.runtime.incrementNumberOfSessionsRetrievedFromTheDatabase();
      return true;
   }

   private static ResultSet populateAttributes(PreparedStatement var0, String var1, String var2, ResultSet var3, SessionDiff var4) throws SQLException, IOException, ClassNotFoundException {
      var0.setString(1, var1);
      var0.setString(2, var2);
      var0.setInt(3, 0);
      var3 = var0.executeQuery();
      if (var3.next()) {
         populateAttributes(var3, var4);
      }

      return var3;
   }

   private static ResultSet populateInternalAttributes(PreparedStatement var0, String var1, String var2, ResultSet var3, SessionDiff var4) throws SQLException, IOException, ClassNotFoundException {
      var0.setString(1, var1);
      var0.setString(2, var2);
      var0.setInt(3, 1);
      var3 = var0.executeQuery();
      if (var3.next()) {
         populateInternalAttributes(var3, var4);
      }

      return var3;
   }

   private static ResultSet isSessionValid(Connection var0, String var1, String var2) throws SQLException {
      PreparedStatement var3 = var0.prepareStatement(isSessionValidQuery);
      var3.setString(1, var1);
      var3.setString(2, var2);
      var3.setString(3, var1);
      var3.setString(4, var2);
      return var3.executeQuery();
   }

   private static void close(PreparedStatement var0, ResultSet var1, Connection var2) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (SQLException var6) {
      }

      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (SQLException var5) {
      }

      try {
         if (var2 != null) {
            var2.close();
         }
      } catch (SQLException var4) {
      }

   }

   private static void populateAttributes(ResultSet var0, SessionDiff var1) throws SQLException, IOException, ClassNotFoundException {
      do {
         String var2 = var0.getString(1);
         byte[] var3 = var0.getBytes(2);
         ServletObjectInputStream var4 = ServletObjectInputStream.getInputStream(var3);
         Object var5 = var4.readObject();
         var1.setAttribute(var2, var5, false, false);
         ServletObjectInputStream.releaseInputStream(var4);
      } while(var0.next());

   }

   private static void populateInternalAttributes(ResultSet var0, SessionDiff var1) throws SQLException, IOException, ClassNotFoundException {
      do {
         String var2 = var0.getString(1);
         byte[] var3 = var0.getBytes(2);
         ServletObjectInputStream var4 = ServletObjectInputStream.getInputStream(var3);
         Object var5 = var4.readObject();
         var1.setAttribute(var2, var5, false, true);
         ServletObjectInputStream.releaseInputStream(var4);
      } while(var0.next());

   }

   public void invalidate(String var1, String var2) {
      boolean var3 = false;
      HashSet var4 = null;
      synchronized(this) {
         this.invalidateSet.add(new Invalidate(var1, var2));
         ++this.invalidateIndex;
         var3 = this.invalidateIndex == this.INVALIDATE_SIZE;
         if (var3) {
            var4 = (HashSet)this.invalidateSet.clone();
            this.timeAtLastInvalidateFlush = System.currentTimeMillis();
            this.invalidateIndex = 0;
            this.invalidateSet.clear();
         }
      }

      PersistenceServiceInternal var5 = this.getPersistenceServiceInternal();
      if (var3 && var5 != null) {
         this.flushInvalidateRequests(var5, var4);
      }

   }

   public final boolean isServiceAvailable() {
      return this.isServiceAvailable;
   }

   public void start() {
      if (this.localService != null) {
         try {
            Environment var1 = new Environment();
            var1.setCreateIntermediateContexts(true);
            Context var2 = var1.getInitialContext();
            var2.rebind("weblogic/servlet/wan/persistenceservice", this.localService);
         } catch (NamingException var3) {
            throw new AssertionError("Unexpected exception" + var3.toString());
         }
      }
   }

   public void stop() {
      PersistenceServiceInternal var1 = this.getPersistenceServiceInternal();
      if (var1 != null) {
         HashSet var2 = null;
         synchronized(this) {
            var2 = (HashSet)this.updateSet.clone();
         }

         this.flush(var1, var2);

         try {
            Environment var3 = new Environment();
            Context var4 = var3.getInitialContext();
            var4.unbind("weblogic/servlet/wan/persistenceservice");
         } catch (NamingException var5) {
            throw new AssertionError("Unexpected exception" + var5.toString());
         }

         this.invalidateTimer.cancel();
         this.updateTimer.cancel();
         this.srvrState = "SHUTDOWN";
      }
   }

   public void halt() {
      this.stop();
   }

   private void flushInvalidateRequests(PersistenceServiceInternal var1, Set var2) {
      if (var2.size() != 0) {
         try {
            var1.invalidateSessions(var2);
            if (this.getPendingInvalidatesSize() > 0) {
               this.firePendingInvalidates();
            }

            if (WANReplicationDetailsDebugLogger.isDebugEnabled()) {
               WANReplicationDetailsDebugLogger.debug("Invalidating " + this.invalidateSet.size() + " sessions in the database");
            }
         } catch (RemoteException var4) {
            this.addPendingInvalidates(new PendingInvalidateRequest(var2, this));
         }

      }
   }

   private void flush() {
      PersistenceServiceInternal var1 = this.getPersistenceServiceInternal();
      if (var1 != null) {
         HashSet var2 = null;
         synchronized(this) {
            var2 = (HashSet)this.updateSet.clone();
         }

         this.flush(var1, var2);
      }
   }

   private void flushInvalidation() {
      PersistenceServiceInternal var1 = this.getPersistenceServiceInternal();
      if (var1 != null) {
         HashSet var2 = null;
         synchronized(this) {
            var2 = (HashSet)this.invalidateSet.clone();
         }

         this.flushInvalidateRequests(var1, var2);
      }
   }

   private void flush(PersistenceServiceInternal var1, Set var2) {
      int var3 = var2.size();
      Update[] var4 = new Update[var3];
      var2.toArray(var4);
      BatchedSessionState var5 = new BatchedSessionState(var4);

      try {
         if (var1 != null) {
            var1.persistState(var5);
            if (this.getPendingUpdatesSize() > 0) {
               this.firePendingUpdates();
               this.runtime.setRemoteClusterReachable(true);
            }

            this.incrementUpdateCount();
         } else {
            this.addPendingUpdates(new PendingUpdateRequest(var5, this));
         }

         if (WANReplicationDetailsDebugLogger.isDebugEnabled() && this.backupClusterAddress != null) {
            WANReplicationDetailsDebugLogger.debug("Flushed " + var3 + " sessions to the remote cluster");
         }
      } catch (ServiceUnavailableException var7) {
         this.runtime.setRemoteClusterReachable(false);
         this.addPendingUpdates(new PendingUpdateRequest(var5, this));
      } catch (RemoteException var8) {
         this.runtime.setRemoteClusterReachable(false);
         this.addPendingUpdates(new PendingUpdateRequest(var5, this));
      }

   }

   private long getTimeAtLastUpdateFlush() {
      return this.timeAtLastUpdateFlush;
   }

   private long getTimeAtLastInvalidateFlush() {
      return this.timeAtLastInvalidateFlush;
   }

   private void addPendingUpdates(PendingUpdateRequest var1) {
      synchronized(this) {
         this.pendingUpdates.add(var1);
      }
   }

   private int getPendingUpdatesSize() {
      synchronized(this) {
         return this.pendingUpdates.size();
      }
   }

   private void firePendingUpdates() {
      ArrayList var1 = null;
      synchronized(this) {
         var1 = (ArrayList)this.pendingUpdates.clone();
         this.pendingUpdates.clear();
      }

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         PendingUpdateRequest var3 = (PendingUpdateRequest)var1.get(var2);
         this.workManager.schedule(var3);
      }

   }

   private int getPendingInvalidatesSize() {
      synchronized(this) {
         return this.pendingInvalidates.size();
      }
   }

   private void firePendingInvalidates() {
      ArrayList var1 = null;
      synchronized(this) {
         var1 = (ArrayList)this.pendingInvalidates.clone();
         this.pendingInvalidates.clear();
      }

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         PendingInvalidateRequest var3 = (PendingInvalidateRequest)var1.get(var2);
         this.workManager.schedule(var3);
      }

   }

   private void incrementUpdateCount() {
      this.runtime.incrementNumberOfSessionsFlushedToTheDatabase();
   }

   private LinkLivenessChecker getLinkChecker() {
      return this.linkChecker;
   }

   private void addPendingInvalidates(PendingInvalidateRequest var1) {
      synchronized(this) {
         this.pendingInvalidates.add(var1);
      }
   }

   // $FF: synthetic method
   WANPersistenceManager(Object var1) {
      this();
   }

   private static final class SessionInvalidateFlushTrigger implements NakedTimerListener, StopTimerListener {
      private final WANPersistenceManager manager;

      private SessionInvalidateFlushTrigger(WANPersistenceManager var1) {
         this.manager = var1;
      }

      public void timerExpired(Timer var1) {
         this.manager.flushInvalidation();
      }

      public void timerStopped(Timer var1) {
         this.manager.flushInvalidation();
      }

      // $FF: synthetic method
      SessionInvalidateFlushTrigger(WANPersistenceManager var1, Object var2) {
         this(var1);
      }
   }

   private static final class FlushWork implements Runnable {
      private final WANPersistenceManager manager;
      private final Set set;

      private FlushWork(Set var1, WANPersistenceManager var2) {
         this.set = var1;
         this.manager = var2;
      }

      public void run() {
         PersistenceServiceInternal var1 = this.manager.getPersistenceServiceInternal();
         if (var1 == null && WANReplicationDetailsDebugLogger.isDebugEnabled()) {
            WANReplicationDetailsDebugLogger.debug("Couldn't reach remote cluster  persistence service");
         }

         this.manager.flush(var1, this.set);
      }

      // $FF: synthetic method
      FlushWork(Set var1, WANPersistenceManager var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class SessionUpdateFlushTrigger implements NakedTimerListener {
      private final WANPersistenceManager manager;
      private final int flushPeriod;

      private SessionUpdateFlushTrigger(WANPersistenceManager var1, int var2) {
         this.manager = var1;
         this.flushPeriod = var2;
      }

      public void timerExpired(Timer var1) {
         if (System.currentTimeMillis() - this.manager.getTimeAtLastUpdateFlush() > (long)this.flushPeriod) {
            this.manager.flush();
         }

      }

      // $FF: synthetic method
      SessionUpdateFlushTrigger(WANPersistenceManager var1, int var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class PendingInvalidateRequest implements Runnable {
      private final WANPersistenceManager manager;
      private final Set set;

      private PendingInvalidateRequest(Set var1, WANPersistenceManager var2) {
         this.set = var1;
         this.manager = var2;
      }

      public void run() {
         try {
            PersistenceServiceInternal var1 = this.manager.getPersistenceServiceInternal();
            if (var1 != null) {
               var1.invalidateSessions(this.set);
               if (this.manager.getLinkChecker() != null) {
                  this.manager.getLinkChecker().stop();
               }
            } else {
               this.manager.addPendingInvalidates(this);
            }
         } catch (RemoteException var2) {
            this.manager.addPendingInvalidates(this);
            if (this.manager.getLinkChecker() != null) {
               this.manager.getLinkChecker().resume();
            }
         }

      }

      // $FF: synthetic method
      PendingInvalidateRequest(Set var1, WANPersistenceManager var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class PendingUpdateRequest implements Runnable {
      private final WANPersistenceManager manager;
      private final BatchedSessionState state;

      private PendingUpdateRequest(BatchedSessionState var1, WANPersistenceManager var2) {
         this.state = var1;
         this.manager = var2;
      }

      public void run() {
         try {
            PersistenceServiceInternal var1 = this.manager.getPersistenceServiceInternal();
            if (var1 != null) {
               var1.persistState(this.state);
               this.manager.incrementUpdateCount();
               if (this.manager.getLinkChecker() != null) {
                  this.manager.getLinkChecker().stop();
               }
            } else {
               this.manager.addPendingUpdates(this);
            }
         } catch (ServiceUnavailableException var2) {
            this.manager.addPendingUpdates(this);
            if (this.manager.getLinkChecker() != null) {
               this.manager.getLinkChecker().resume();
            }
         } catch (RemoteException var3) {
            this.manager.addPendingUpdates(this);
            if (this.manager.getLinkChecker() != null) {
               this.manager.getLinkChecker().resume();
            }
         }

      }

      // $FF: synthetic method
      PendingUpdateRequest(BatchedSessionState var1, WANPersistenceManager var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class SingletonMaker {
      static final WANPersistenceManager singleton = new WANPersistenceManager();
   }
}
