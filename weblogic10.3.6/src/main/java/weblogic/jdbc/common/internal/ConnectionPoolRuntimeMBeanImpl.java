package weblogic.jdbc.common.internal;

import java.io.PrintWriter;
import java.util.Properties;
import weblogic.common.ResourceException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCConnectionLeakProfile;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;
import weblogic.management.runtime.JDBCStatementProfile;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.utils.StackTraceUtils;

/** @deprecated */
public final class ConnectionPoolRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JDBCConnectionPoolRuntimeMBean {
   private static final long serialVersionUID = 6620033273282151271L;
   private ConnectionPool pool;

   public ConnectionPoolRuntimeMBeanImpl(ConnectionPool var1, String var2, RuntimeMBean var3) throws ManagementException {
      super(var2, var3);
      this.pool = var1;
   }

   public int getConnectionLeakProfileCount() {
      return ProfileStorage.ConnectionLeakProfileCount(this.pool.getResourceName());
   }

   public int getDeploymentState() {
      String var1 = this.getState();
      return var1.equals("Shutdown") ? 1 : 2;
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

   public JDBCConnectionLeakProfile[] getConnectionLeakProfiles(int var1, int var2) {
      return ProfileStorage.getConnectionLeakProfiles(var1, var2, this.pool.getResourceName());
   }

   public void resetConnectionLeakProfile() {
      ProfileStorage.resetConnectionLeakProfile();
   }

   public int getStatementProfileCount() {
      return ProfileStorage.StatementProfileCount(this.pool.getResourceName());
   }

   public JDBCStatementProfile[] getStatementProfiles(int var1, int var2) {
      return ProfileStorage.getStatementProfiles(var1, var2, this.pool.getResourceName());
   }

   public void resetStatementProfile() {
      ProfileStorage.resetStatementProfile();
   }

   public void resetPreparedStatementCacheProfile() {
   }

   public String testPool() {
      String var1 = null;
      ConnectionEnv var2 = null;

      try {
         var2 = this.pool.reserve((AuthenticatedSubject)null, -1);
         if (var2 != null && !var2.isConnTested()) {
            var1 = "Warning! Connectivity to backend database not verified. This is either because required connection pool attribute \"TestConnectionsOnReserve\" has not been enabled, or an invalid value has been specified for attribute \"TestTableName\". Please check the server log for more details..";
         }
      } catch (Exception var13) {
         var1 = StackTraceUtils.throwable2StackTrace(var13);
      } finally {
         if (var2 != null) {
            try {
               this.pool.release(var2);
            } catch (Exception var12) {
               var1 = StackTraceUtils.throwable2StackTrace(var12);
            }
         }

      }

      return var1;
   }

   public int getLeakedConnectionCount() {
      return this.pool.getNumLeaked();
   }

   public boolean getPoolState() {
      return this.isEnabled();
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

   public int getWaitSecondsHighCount() {
      return this.pool.getHighestWaitSeconds();
   }

   public int getConnectionsTotalCount() {
      return this.pool.getTotalNumAllocated();
   }

   public int getMaxCapacity() {
      return this.pool.getMaxCapacity();
   }

   public int getCurrCapacity() {
      return this.pool.getCurrCapacity();
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

   public void shrink() throws ResourceException {
      this.pool.shrink();
   }

   public void reset() throws ResourceException {
      this.pool.reset();
   }

   public void suspend() throws ResourceException {
      this.pool.suspendExternal();
   }

   public void forceSuspend() throws ResourceException {
      this.pool.forceSuspendExternal();
   }

   public void resume() throws ResourceException {
      this.pool.resumeExternal();
   }

   public void shutdown() throws ResourceException {
      this.pool.shutdownExternal();
   }

   public void forceShutdown() throws ResourceException {
      this.pool.forceShutdownExternal();
   }

   public void disableDroppingUsers() throws ResourceException, Exception {
      this.pool.disableDroppingUsers();
   }

   public void disableFreezingUsers() throws ResourceException, Exception {
      this.pool.disableFreezingUsers();
   }

   public void enable() throws ResourceException {
      this.pool.enable();
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
      this.pool.clearStatementCache();
   }

   public void dumpPool() throws Exception {
      this.pool.dumpPool((PrintWriter)null);
   }
}
