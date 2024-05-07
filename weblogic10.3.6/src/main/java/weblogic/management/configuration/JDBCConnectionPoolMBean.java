package weblogic.management.configuration;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;

/** @deprecated */
public interface JDBCConnectionPoolMBean extends DeploymentMBean {
   void setJDBCSystemResource(JDBCSystemResourceMBean var1);

   JDBCSystemResourceMBean getJDBCSystemResource();

   boolean isPrepStmtCacheProfilingEnabled();

   void setPrepStmtCacheProfilingEnabled(boolean var1) throws InvalidAttributeValueException;

   int getPrepStmtCacheProfilingThreshold();

   void setPrepStmtCacheProfilingThreshold(int var1) throws InvalidAttributeValueException;

   void setConnLeakProfilingEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isConnLeakProfilingEnabled();

   void setConnProfilingEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isConnProfilingEnabled();

   boolean isSqlStmtProfilingEnabled();

   void setSqlStmtProfilingEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isSqlStmtParamLoggingEnabled();

   void setSqlStmtParamLoggingEnabled(boolean var1) throws InvalidAttributeValueException;

   int getSqlStmtMaxParamLength();

   void setSqlStmtMaxParamLength(int var1) throws InvalidAttributeValueException;

   String getACLName();

   void setACLName(String var1) throws InvalidAttributeValueException;

   String getURL();

   void setURL(String var1) throws InvalidAttributeValueException;

   String getDriverName();

   void setDriverName(String var1) throws InvalidAttributeValueException;

   Properties getProperties();

   void setProperties(Properties var1) throws InvalidAttributeValueException;

   int getLoginDelaySeconds();

   void setLoginDelaySeconds(int var1) throws InvalidAttributeValueException;

   int getSecondsToTrustAnIdlePoolConnection();

   void setSecondsToTrustAnIdlePoolConnection(int var1) throws InvalidAttributeValueException;

   int getInitialCapacity();

   void setInitialCapacity(int var1) throws InvalidAttributeValueException;

   int getMaxCapacity();

   void setMaxCapacity(int var1) throws InvalidAttributeValueException;

   /** @deprecated */
   int getCapacityIncrement();

   void setCapacityIncrement(int var1) throws InvalidAttributeValueException;

   boolean isShrinkingEnabled();

   void setShrinkingEnabled(boolean var1) throws InvalidAttributeValueException;

   int getShrinkPeriodMinutes();

   void setShrinkPeriodMinutes(int var1) throws InvalidAttributeValueException;

   int getShrinkFrequencySeconds();

   void setShrinkFrequencySeconds(int var1) throws InvalidAttributeValueException;

   int getRefreshMinutes();

   void setRefreshMinutes(int var1) throws InvalidAttributeValueException;

   int getTestFrequencySeconds();

   void setTestFrequencySeconds(int var1) throws InvalidAttributeValueException;

   String getTestTableName();

   void setTestTableName(String var1) throws InvalidAttributeValueException;

   void setTestConnectionsOnReserve(boolean var1) throws InvalidAttributeValueException;

   boolean getTestConnectionsOnReserve();

   void setTestConnectionsOnRelease(boolean var1) throws InvalidAttributeValueException;

   boolean getTestConnectionsOnRelease();

   void setTestConnectionsOnCreate(boolean var1) throws InvalidAttributeValueException;

   boolean getTestConnectionsOnCreate();

   JDBCConnectionPoolRuntimeMBean getJDBCConnectionPoolRuntime();

   void setJDBCConnectionPoolRuntime(JDBCConnectionPoolRuntimeMBean var1) throws InvalidAttributeValueException;

   String getPassword();

   void setPassword(String var1) throws InvalidAttributeValueException;

   byte[] getPasswordEncrypted();

   void setPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getXAPassword();

   void setXAPassword(String var1) throws InvalidAttributeValueException;

   byte[] getXAPasswordEncrypted();

   void setXAPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;

   int getJDBCXADebugLevel();

   void setJDBCXADebugLevel(int var1) throws InvalidAttributeValueException;

   boolean getSupportsLocalTransaction();

   void setSupportsLocalTransaction(boolean var1) throws InvalidAttributeValueException;

   boolean getKeepXAConnTillTxComplete();

   void setKeepXAConnTillTxComplete(boolean var1) throws InvalidAttributeValueException;

   boolean getNeedTxCtxOnClose();

   void setNeedTxCtxOnClose(boolean var1) throws InvalidAttributeValueException;

   boolean getXAEndOnlyOnce();

   void setXAEndOnlyOnce(boolean var1) throws InvalidAttributeValueException;

   boolean getNewXAConnForCommit();

   void setNewXAConnForCommit(boolean var1) throws InvalidAttributeValueException;

   boolean getKeepLogicalConnOpenOnRelease();

   void setKeepLogicalConnOpenOnRelease(boolean var1) throws InvalidAttributeValueException;

   int getXAPreparedStatementCacheSize();

   void setXAPreparedStatementCacheSize(int var1) throws InvalidAttributeValueException;

   boolean getEnableResourceHealthMonitoring();

   void setEnableResourceHealthMonitoring(boolean var1) throws InvalidAttributeValueException;

   boolean getRecoverOnlyOnce();

   void setRecoverOnlyOnce(boolean var1) throws InvalidAttributeValueException;

   boolean getXASetTransactionTimeout();

   void setXASetTransactionTimeout(boolean var1) throws InvalidAttributeValueException;

   int getXATransactionTimeout();

   void setXATransactionTimeout(int var1) throws InvalidAttributeValueException;

   int getXARetryDurationSeconds();

   void setXARetryDurationSeconds(int var1) throws InvalidAttributeValueException;

   int getXARetryIntervalSeconds();

   void setXARetryIntervalSeconds(int var1) throws InvalidAttributeValueException;

   void setPreparedStatementCacheSize(int var1);

   int getPreparedStatementCacheSize();

   void setConnectionReserveTimeoutSeconds(int var1);

   int getConnectionReserveTimeoutSeconds();

   void setConnectionCreationRetryFrequencySeconds(int var1);

   int getConnectionCreationRetryFrequencySeconds();

   void setInactiveConnectionTimeoutSeconds(int var1);

   int getInactiveConnectionTimeoutSeconds();

   void setHighestNumWaiters(int var1);

   int getHighestNumWaiters();

   void setHighestNumUnavailable(int var1);

   int getHighestNumUnavailable();

   void setInitSQL(String var1) throws InvalidAttributeValueException;

   String getInitSQL();

   void setStatementCacheSize(int var1);

   int getStatementCacheSize();

   void setStatementCacheType(String var1);

   String getStatementCacheType();

   void setRemoveInfectedConnectionsEnabled(boolean var1);

   boolean isRemoveInfectedConnectionsEnabled();

   void setRollbackLocalTxUponConnClose(boolean var1);

   boolean getRollbackLocalTxUponConnClose();

   void setTestStatementTimeout(int var1);

   int getTestStatementTimeout();

   void setStatementTimeout(int var1);

   int getStatementTimeout();

   void setIgnoreInUseConnectionsEnabled(boolean var1);

   boolean isIgnoreInUseConnectionsEnabled();

   void setCredentialMappingEnabled(boolean var1);

   boolean isCredentialMappingEnabled();

   void setCountOfTestFailuresTillFlush(int var1);

   int getCountOfTestFailuresTillFlush();

   void setCountOfRefreshFailuresTillDisable(int var1);

   int getCountOfRefreshFailuresTillDisable();
}
