package weblogic.jdbc.common.internal;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import weblogic.common.ResourceException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCDataSourceRuntimeMBean;
import weblogic.management.runtime.JDBCDataSourceTaskRuntimeMBean;
import weblogic.management.runtime.JDBCDriverRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class DataSourceRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JDBCDataSourceRuntimeMBean {
   protected ConnectionPool pool;
   private JDBCDriverRuntimeMBean driverRTMBean;
   private ReentrantLock metaDataLock = new ReentrantLock();
   private DatabaseMetaData metaData;
   JDBCDataSourceTaskRuntimeMBeanImpl lastTask = null;

   public DataSourceRuntimeMBeanImpl(ConnectionPool var1, String var2, RuntimeMBean var3, DescriptorBean var4) throws ManagementException {
      super(var2, var3, true, var4);
      this.pool = var1;
   }

   public int getDeploymentState() {
      if (this.pool != null) {
         String var1 = this.getState();
         return var1.equals("Shutdown") ? 1 : 2;
      } else {
         return 2;
      }
   }

   public void setDeploymentState(int var1) {
   }

   public boolean addWorkManagerRuntime(WorkManagerRuntimeMBean var1) {
      return true;
   }

   public WorkManagerRuntimeMBean[] getWorkManagerRuntimes() {
      return null;
   }

   public String getModuleId() {
      return this.getName();
   }

   public String testPool() {
      String var1 = null;
      ConnectionEnv var2 = null;

      try {
         try {
            var2 = this.pool.reserve((AuthenticatedSubject)null, -1);
            if (var2 == null || var2.isConnTested()) {
               return var1;
            }

            String var3 = this.pool.getResourceFactory().getTestQuery();
            String var5;
            if (var3 != null) {
               int var4 = var2.testInternal(var3);
               switch (var4) {
                  case -1:
                     var1 = JDBCUtil.getTextFormatter().testPoolQueryFailed(this.pool.getResourceFactory().getTestQuery());
                     var5 = var1;
                     return var5;
                  case 0:
                  default:
                     break;
                  case 1:
                     var5 = null;
                     return var5;
               }
            }

            if (!var2.supportIsValid()) {
               return var1;
            }

            boolean var20 = var2.conn.jconn.isValid(15);
            if (var20) {
               var5 = null;
               return var5;
            }

            var1 = JDBCUtil.getTextFormatter().testPoolIsValid();
         } catch (Exception var18) {
            var1 = JDBCUtil.getTextFormatter().testPoolException(var18.toString());
         }

         return var1;
      } finally {
         if (var2 != null) {
            try {
               this.pool.release(var2);
            } catch (Exception var17) {
               var1 = JDBCUtil.getTextFormatter().testPoolException(var17.toString());
            }
         }

      }
   }

   public int getLeakedConnectionCount() {
      return this.pool.getNumLeaked();
   }

   public boolean isEnabled() {
      return this.pool.isEnabled();
   }

   public String getState() {
      return this.pool.getDerivedState();
   }

   public int getFailuresToReconnectCount() {
      return this.pool.getNumFailuresToRefresh();
   }

   public int getConnectionDelayTime() {
      return this.pool.getCreationDelayTime();
   }

   public long getPrepStmtCacheAccessCount() {
      return this.pool.getPrepStmtCacheAccessCount();
   }

   public long getPrepStmtCacheAddCount() {
      return this.pool.getPrepStmtCacheAddCount();
   }

   public long getPrepStmtCacheDeleteCount() {
      return this.pool.getPrepStmtCacheDeleteCount();
   }

   public int getPrepStmtCacheCurrentSize() {
      return this.pool.getPrepStmtCacheCurrentSize();
   }

   public int getPrepStmtCacheHitCount() {
      return this.pool.getPrepStmtCacheHitCount();
   }

   public int getPrepStmtCacheMissCount() {
      return this.pool.getPrepStmtCacheMissCount();
   }

   public int getActiveConnectionsCurrentCount() {
      return this.pool.getNumReserved();
   }

   public int getWaitingForConnectionCurrentCount() {
      int var1 = this.pool.getNumWaiters();
      if (var1 < 0) {
         var1 = 0;
      }

      return var1;
   }

   public String getVersionJDBCDriver() {
      return this.pool.getDriverVersion();
   }

   public int getActiveConnectionsHighCount() {
      return this.pool.getHighestNumReserved();
   }

   public int getWaitingForConnectionHighCount() {
      return this.pool.getHighestNumWaiters();
   }

   public long getWaitingForConnectionTotal() {
      return this.pool.getTotalWaitingForConnection();
   }

   public long getWaitingForConnectionSuccessTotal() {
      return this.pool.getTotalWaitingForConnectionSuccess();
   }

   public long getWaitingForConnectionFailureTotal() {
      return this.pool.getTotalWaitingForConnectionFailure();
   }

   public int getWaitSecondsHighCount() {
      return this.pool.getHighestWaitSeconds();
   }

   public int getConnectionsTotalCount() {
      return this.pool.getTotalNumAllocated();
   }

   public int getCurrCapacity() {
      return this.pool.getCurrCapacity();
   }

   public int getCurrCapacityHighCount() {
      return this.pool.getHighestCurrCapacity();
   }

   public int getNumAvailable() {
      return this.pool.getNumAvailable();
   }

   public int getHighestNumAvailable() {
      return this.pool.getHighestNumAvailable();
   }

   public int getNumUnavailable() {
      return this.pool.getNumUnavailable();
   }

   public int getHighestNumUnavailable() {
      return this.pool.getHighestNumUnavailable();
   }

   public int getActiveConnectionsAverageCount() {
      return this.pool.getAverageReserved();
   }

   public long getReserveRequestCount() {
      return this.pool.getNumReserveRequests();
   }

   public long getFailedReserveRequestCount() {
      return this.pool.getNumFailedReserveRequests();
   }

   public void shrink() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("Shrink-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.shrink();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("Shrink JDBCDataSource: " + this.getName());

      try {
         this.pool.shrink();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void reset() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("Reset-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.reset();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("Reset JDBCDataSource: " + this.getName());

      try {
         this.pool.reset();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void suspend() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("Suspend-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.suspendExternal();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("Suspend JDBCDataSource: " + this.getName());

      try {
         this.pool.suspendExternal();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void forceSuspend() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("ForceSuspend-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.forceSuspendExternal();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("ForceSuspend JDBCDataSource: " + this.getName());

      try {
         this.pool.forceSuspendExternal();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void resume() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("Resume-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.resumeExternal();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("Resume JDBCDataSource: " + this.getName());

      try {
         this.pool.resumeExternal();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void shutdown() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("Shutdown-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.shutdownExternal();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("Shutdown JDBCDataSource: " + this.getName());

      try {
         this.pool.shutdownExternal();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void forceShutdown() throws ResourceException {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("ForceShutdown-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.forceShutdownExternal();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("ForceShutdown JDBCDataSource: " + this.getName());

      try {
         this.pool.forceShutdownExternal();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public Properties getProperties() throws ResourceException {
      Properties var1 = (Properties)((Properties)this.pool.getProperties().clone());
      var1.remove("password");
      return var1;
   }

   public boolean poolExists(String var1) throws Exception {
      return this.pool.poolExists(var1);
   }

   public void clearStatementCache() throws Exception {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("ClearStatementCache-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.clearStatementCache();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("ClearStatementCache JDBCDataSource: " + this.getName());

      try {
         this.pool.clearStatementCache();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public void dumpPool() throws Exception {
      this.pool.dumpPool((PrintWriter)null);
   }

   public void dumpPoolProfile() throws Exception {
      this.pool.getProfiler().dumpData();
   }

   public void setJDBCDriverRuntime(JDBCDriverRuntimeMBean var1) {
      this.driverRTMBean = var1;
   }

   public JDBCDriverRuntimeMBean getJDBCDriverRuntime() {
      return this.driverRTMBean;
   }

   public JDBCDataSourceTaskRuntimeMBean getLastTask() {
      return this.lastTask;
   }

   public void start() throws Exception {
      try {
         if (this.lastTask != null) {
            this.lastTask.unregister();
         }

         this.lastTask = new JDBCDataSourceTaskRuntimeMBeanImpl("Start-JDBCDataSource", this, true);
      } catch (Exception var11) {
         this.pool.startExternal();
         return;
      }

      this.lastTask.setBeginTime(System.currentTimeMillis());
      this.lastTask.setDescription("Start JDBCDataSource: " + this.getName());

      try {
         this.pool.startExternal();
         this.lastTask.setStatus("SUCCESS");
      } catch (ResourceException var8) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var8);
         throw var8;
      } catch (RuntimeException var9) {
         this.lastTask.setStatus("FAILURE");
         this.lastTask.setError(var9);
         throw var9;
      } finally {
         this.lastTask.setEndTime(System.currentTimeMillis());
      }

   }

   public boolean isOperationAllowed(String var1) throws IllegalArgumentException {
      int var2 = this.pool.getStateAsInt();
      if ("Start".equalsIgnoreCase(var1)) {
         if (var2 == 100) {
            return true;
         }
      } else if ("Shutdown".equalsIgnoreCase(var1)) {
         if (var2 == 101 || var2 == 102) {
            return true;
         }
      } else if ("Suspend".equalsIgnoreCase(var1)) {
         if (var2 == 101) {
            return true;
         }
      } else if ("Resume".equalsIgnoreCase(var1)) {
         if (var2 == 102) {
            return true;
         }
      } else if ("Reset".equalsIgnoreCase(var1)) {
         if (var2 == 101 || var2 == 102) {
            return true;
         }
      } else if ("Shrink".equalsIgnoreCase(var1)) {
         if (var2 == 101 || var2 == 102) {
            return true;
         }
      } else {
         if (!"Clear".equalsIgnoreCase(var1)) {
            throw new IllegalArgumentException("Invalid argument: " + var1);
         }

         if (var2 == 101 || var2 == 102) {
            return true;
         }
      }

      return false;
   }

   private DatabaseMetaData lookupMetaData() throws ResourceException, SQLException {
      ConnectionEnv var1 = null;

      DatabaseMetaData var2;
      try {
         var1 = this.pool.reserve((AuthenticatedSubject)null, -1);
         if (var1 == null) {
            throw new ResourceException("Unable to obtain a connection for data source " + this.getName());
         }

         var2 = var1.conn.jconn.getMetaData();
      } finally {
         if (var1 != null) {
            this.pool.release(var1);
         }

      }

      return var2;
   }

   private DatabaseMetaData getMetaData() throws ResourceException, SQLException {
      try {
         if (!this.metaDataLock.tryLock(60L, TimeUnit.SECONDS)) {
            throw new ResourceException("Operation timed out waiting to get JDBC meta data");
         }
      } catch (InterruptedException var6) {
         throw new ResourceException(var6.getMessage());
      }

      try {
         if (this.metaData == null) {
            this.metaData = this.lookupMetaData();
         }
      } finally {
         this.metaDataLock.unlock();
      }

      return this.metaData;
   }

   public String getDatabaseProductName() throws RemoteException {
      try {
         DatabaseMetaData var1 = this.getMetaData();
         return var1.getDatabaseProductName();
      } catch (ResourceException var2) {
         throw new RemoteException(var2.getMessage());
      } catch (SQLException var3) {
         throw new RemoteException(var3.getMessage());
      }
   }

   public String getDatabaseProductVersion() throws RemoteException {
      try {
         DatabaseMetaData var1 = this.getMetaData();
         return var1.getDatabaseProductVersion();
      } catch (ResourceException var2) {
         throw new RemoteException(var2.getMessage());
      } catch (SQLException var3) {
         throw new RemoteException(var3.getMessage());
      }
   }

   public String getDriverName() throws RemoteException {
      try {
         DatabaseMetaData var1 = this.getMetaData();
         return var1.getDriverName();
      } catch (ResourceException var2) {
         throw new RemoteException(var2.getMessage());
      } catch (SQLException var3) {
         throw new RemoteException(var3.getMessage());
      }
   }

   public String getDriverVersion() throws RemoteException {
      try {
         DatabaseMetaData var1 = this.getMetaData();
         return var1.getDriverVersion();
      } catch (ResourceException var2) {
         throw new RemoteException(var2.getMessage());
      } catch (SQLException var3) {
         throw new RemoteException(var3.getMessage());
      }
   }
}
