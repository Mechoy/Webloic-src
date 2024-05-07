package weblogic.management.runtime;

import java.rmi.RemoteException;
import java.util.Properties;

public interface JDBCDataSourceRuntimeMBean extends ComponentRuntimeMBean {
   String testPool();

   int getLeakedConnectionCount();

   boolean isEnabled();

   String getState();

   int getFailuresToReconnectCount();

   int getConnectionDelayTime();

   long getPrepStmtCacheAccessCount();

   long getPrepStmtCacheAddCount();

   long getPrepStmtCacheDeleteCount();

   int getPrepStmtCacheCurrentSize();

   int getPrepStmtCacheHitCount();

   int getPrepStmtCacheMissCount();

   int getActiveConnectionsCurrentCount();

   int getWaitingForConnectionCurrentCount();

   String getVersionJDBCDriver();

   int getActiveConnectionsHighCount();

   int getActiveConnectionsAverageCount();

   long getReserveRequestCount();

   long getFailedReserveRequestCount();

   int getWaitingForConnectionHighCount();

   long getWaitingForConnectionTotal();

   long getWaitingForConnectionSuccessTotal();

   long getWaitingForConnectionFailureTotal();

   int getWaitSecondsHighCount();

   int getConnectionsTotalCount();

   int getCurrCapacity();

   int getCurrCapacityHighCount();

   int getNumAvailable();

   int getHighestNumAvailable();

   int getNumUnavailable();

   int getHighestNumUnavailable();

   void shrink() throws Exception;

   void reset() throws Exception;

   void suspend() throws Exception;

   void forceSuspend() throws Exception;

   void shutdown() throws Exception;

   void forceShutdown() throws Exception;

   void resume() throws Exception;

   void start() throws Exception;

   Properties getProperties() throws Exception;

   boolean poolExists(String var1) throws Exception;

   void clearStatementCache() throws Exception;

   void dumpPool() throws Exception;

   void dumpPoolProfile() throws Exception;

   void setJDBCDriverRuntime(JDBCDriverRuntimeMBean var1);

   JDBCDriverRuntimeMBean getJDBCDriverRuntime();

   JDBCDataSourceTaskRuntimeMBean getLastTask();

   boolean isOperationAllowed(String var1) throws IllegalArgumentException;

   String getDatabaseProductName() throws RemoteException;

   String getDatabaseProductVersion() throws RemoteException;

   String getDriverName() throws RemoteException;

   String getDriverVersion() throws RemoteException;
}
