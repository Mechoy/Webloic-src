package weblogic.management.runtime;

import java.util.Properties;

/** @deprecated */
public interface JDBCConnectionPoolRuntimeMBean extends ComponentRuntimeMBean {
   int getConnectionLeakProfileCount();

   JDBCConnectionLeakProfile[] getConnectionLeakProfiles(int var1, int var2);

   int getStatementProfileCount();

   JDBCStatementProfile[] getStatementProfiles(int var1, int var2);

   String testPool();

   int getLeakedConnectionCount();

   boolean getPoolState();

   boolean isEnabled();

   String getState();

   int getFailuresToReconnectCount();

   int getConnectionDelayTime();

   int getPrepStmtCacheHitCount();

   int getPrepStmtCacheMissCount();

   int getActiveConnectionsCurrentCount();

   int getWaitingForConnectionCurrentCount();

   String getVersionJDBCDriver();

   int getActiveConnectionsHighCount();

   int getActiveConnectionsAverageCount();

   int getWaitingForConnectionHighCount();

   int getWaitSecondsHighCount();

   int getConnectionsTotalCount();

   int getMaxCapacity();

   int getCurrCapacity();

   int getNumAvailable();

   int getHighestNumAvailable();

   int getNumUnavailable();

   int getHighestNumUnavailable();

   void resetConnectionLeakProfile();

   void resetStatementProfile();

   void shrink() throws Exception;

   void reset() throws Exception;

   void suspend() throws Exception;

   void forceSuspend() throws Exception;

   void shutdown() throws Exception;

   void forceShutdown() throws Exception;

   void resume() throws Exception;

   void disableDroppingUsers() throws Exception;

   void disableFreezingUsers() throws Exception;

   void enable() throws Exception;

   Properties getProperties() throws Exception;

   boolean poolExists(String var1) throws Exception;

   void clearStatementCache() throws Exception;

   void dumpPool() throws Exception;
}
