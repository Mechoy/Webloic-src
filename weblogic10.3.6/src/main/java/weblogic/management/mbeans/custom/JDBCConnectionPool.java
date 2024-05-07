package weblogic.management.mbeans.custom;

import java.security.AccessController;
import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JDBCPropertyBean;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.jdbc.common.internal.JDBCUtil;
import weblogic.management.configuration.JDBCLegalHelper;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class JDBCConnectionPool extends JDBCConfigurationMBeanCustomizer {
   private static final AuthenticatedSubject KERNELID = getKernelID();
   boolean prepStmtCacheProfilingEnabled = false;
   int prepStmtCacheProfilingThreshold = 10;
   boolean sqlStmtProfilingEnabled = false;
   boolean sqlStmtParamLoggingEnabled = false;
   boolean connLeakProfiling = false;
   boolean connProfiling = false;
   int sqlStmtMaxParamLength = 10;
   String aclName = null;
   String url = null;
   String driverName = null;
   Properties properties;
   int loginDelaySeconds = 0;
   int secondsToTrustAnIdlePoolConnection = 10;
   int initialCapacity = 1;
   int maxCapacity = 15;
   int capacityIncrement = 1;
   int shrinkFrequencySeconds = 900;
   int testFrequencySeconds = 120;
   String testTableName = null;
   boolean testConnectionsOnReserve = false;
   boolean testConnectionsOnRelease = false;
   boolean testConnectionsOnCreate = true;
   byte[] passwordEncrypted = null;
   int jdbcXADebugLevel = 10;
   boolean keepXaConnTillTxComplete = false;
   boolean needTxCtxOnClose = false;
   boolean xaEndOnlyOnce = false;
   boolean newXaConnForCommit = false;
   boolean keepLogicalConnOpenOnRelease = false;
   boolean enableResourceHealthMonitoring = true;
   boolean xaSetTransactionTimeout = false;
   int xaTransactionTimeout = 0;
   boolean recoverOnlyOnce = false;
   int connectionReserveTimeoutSeconds = 10;
   int connectionCreationRetryFrequencySeconds = 0;
   int inactiveConnectionTimeoutSeconds = 0;
   int highestNumWaiters = 0;
   int highestNumUnavailable = 0;
   String initSQL = null;
   int statementCacheSize = 10;
   String statementCacheType = "LRU";
   boolean removeInfectedConnectionsEnabled = true;
   boolean rollbackLocalTxUponConnClose = false;
   int statementTimeout = -1;
   boolean ignoreInUseConnectionsEnabled = true;
   boolean credentialMappingEnabled = false;
   int countOfTestFailuresTillFlush = 2;
   int countOfRefreshFailuresTillDisable = 2;
   String lcvStr;
   int lcv;

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   public JDBCConnectionPool(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public boolean isPrepStmtCacheProfilingEnabled() {
      return this.prepStmtCacheProfilingEnabled;
   }

   public void setPrepStmtCacheProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      this.prepStmtCacheProfilingEnabled = var1;
   }

   public void setPrepStmtCacheProfilingThreshold(int var1) throws InvalidAttributeValueException {
      this.prepStmtCacheProfilingThreshold = var1;
   }

   public int getPrepStmtCacheProfilingThreshold() {
      return this.prepStmtCacheProfilingThreshold;
   }

   public void setSqlStmtParamLoggingEnabled(boolean var1) throws InvalidAttributeValueException {
      this.sqlStmtParamLoggingEnabled = var1;
   }

   public boolean isSqlStmtParamLoggingEnabled() {
      return this.sqlStmtParamLoggingEnabled;
   }

   public void setSqlStmtMaxParamLength(int var1) throws InvalidAttributeValueException {
      this.sqlStmtMaxParamLength = var1;
   }

   public int getSqlStmtMaxParamLength() {
      return this.sqlStmtMaxParamLength;
   }

   public void setConnLeakProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         int var2 = this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getProfileType();
         if (var1) {
            var2 |= 4;
         } else {
            var2 &= -5;
         }

         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setProfileType(var2);
      } else {
         this.connLeakProfiling = var1;
      }

   }

   public boolean isConnLeakProfilingEnabled() {
      if (this.delegate != null) {
         return (this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getProfileType() & 4) > 0;
      } else {
         return this.connLeakProfiling;
      }
   }

   public void setConnProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         int var2 = this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getProfileType();
         if (var1) {
            var2 |= 64;
         } else {
            var2 &= -65;
         }

         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setProfileType(var2);
      } else {
         this.connProfiling = var1;
      }

   }

   public boolean isConnProfilingEnabled() {
      if (this.delegate != null) {
         return (this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getProfileType() & 64) > 0;
      } else {
         return this.connProfiling;
      }
   }

   public void setSqlStmtProfilingEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         int var2 = this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getProfileType();
         if (var1) {
            var2 |= 32;
         } else {
            var2 &= -33;
         }

         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setProfileType(var2);
      } else {
         this.sqlStmtProfilingEnabled = var1;
      }

   }

   public boolean isSqlStmtProfilingEnabled() {
      if (this.delegate != null) {
         return (this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getProfileType() & 32) > 0;
      } else {
         return this.sqlStmtProfilingEnabled;
      }
   }

   public void setURL(String var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDriverParams().setUrl(var1);
      } else {
         this.url = var1;
      }

   }

   public String getURL() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDriverParams().getUrl() : this.url;
   }

   public void setDriverName(String var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDriverParams().setDriverName(var1);
      } else {
         this.driverName = var1;
      }

   }

   public String getDriverName() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDriverParams().getDriverName() : this.driverName;
   }

   public void setProperties(Properties var1) {
      if (this.delegate != null) {
         JDBCMBeanConverter.setDriverProperties(this.delegate.getJDBCResource(), var1);
      } else {
         this.properties = var1;
      }

   }

   public Properties getProperties() {
      Properties var1;
      if (this.delegate != null) {
         var1 = JDBCUtil.getProperties(this.delegate.getJDBCResource().getJDBCDriverParams().getProperties().getProperties());
      } else {
         var1 = this.properties;
      }

      if (var1 != null && var1.getProperty("password") != null && SecurityServiceManager.getCurrentSubject(KERNELID) != KERNELID) {
         var1 = (Properties)((Properties)var1.clone());
         var1.remove("password");
      }

      return var1;
   }

   public int getLoginDelaySeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getLoginDelaySeconds() : this.loginDelaySeconds;
   }

   public void setLoginDelaySeconds(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setLoginDelaySeconds(var1);
      } else {
         this.loginDelaySeconds = var1;
      }

   }

   public int getSecondsToTrustAnIdlePoolConnection() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getSecondsToTrustAnIdlePoolConnection() : this.secondsToTrustAnIdlePoolConnection;
   }

   public void setSecondsToTrustAnIdlePoolConnection(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setSecondsToTrustAnIdlePoolConnection(var1);
      } else {
         this.secondsToTrustAnIdlePoolConnection = var1;
      }

   }

   public int getInitialCapacity() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getInitialCapacity() : this.initialCapacity;
   }

   public void setInitialCapacity(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setInitialCapacity(var1);
      } else {
         this.initialCapacity = var1;
      }

   }

   public int getMaxCapacity() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getMaxCapacity() : this.maxCapacity;
   }

   public void setMaxCapacity(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setMaxCapacity(var1);
      } else {
         this.maxCapacity = var1;
      }

   }

   public int getCapacityIncrement() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getCapacityIncrement() : this.capacityIncrement;
   }

   public void setCapacityIncrement(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setCapacityIncrement(var1);
      } else {
         this.capacityIncrement = var1;
      }

   }

   public void setShrinkingEnabled(boolean var1) throws InvalidAttributeValueException {
      if (var1) {
         this.lcv = 900;
      } else {
         this.lcv = 0;
      }

      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setShrinkFrequencySeconds(this.lcv);
      } else {
         this.shrinkFrequencySeconds = this.lcv;
      }

   }

   public boolean isShrinkingEnabled() {
      if (this.delegate != null) {
         this.lcv = this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getShrinkFrequencySeconds();
      } else {
         this.lcv = this.shrinkFrequencySeconds;
      }

      return this.lcv > 0;
   }

   public int getShrinkPeriodMinutes() {
      if (this.delegate != null) {
         this.lcv = this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getShrinkFrequencySeconds() / 60;
      } else {
         this.lcv = this.shrinkFrequencySeconds / 60;
      }

      return this.lcv < 1 ? 1 : this.lcv;
   }

   public void setShrinkPeriodMinutes(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setShrinkFrequencySeconds(var1 * 60);
      } else {
         this.shrinkFrequencySeconds = var1 * 60;
      }

   }

   public int getShrinkFrequencySeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getShrinkFrequencySeconds() : this.shrinkFrequencySeconds;
   }

   public void setShrinkFrequencySeconds(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setShrinkFrequencySeconds(var1);
      } else {
         this.shrinkFrequencySeconds = var1;
      }

   }

   public int getRefreshPeriodMinutes() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getTestFrequencySeconds() / 60 : this.testFrequencySeconds / 60;
   }

   public void setRefreshPeriodMinutes(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setTestFrequencySeconds(var1 * 60);
      } else {
         this.testFrequencySeconds = var1 * 60;
      }

   }

   public int getTestFrequencySeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getTestFrequencySeconds() : this.testFrequencySeconds;
   }

   public void setTestFrequencySeconds(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setTestFrequencySeconds(var1);
      } else {
         this.testFrequencySeconds = var1;
      }

   }

   public String getTestTableName() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getTestTableName() : this.testTableName;
   }

   public void setTestTableName(String var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setTestTableName(var1);
      } else {
         this.testTableName = var1;
      }

   }

   public boolean getTestConnectionsOnReserve() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().isTestConnectionsOnReserve() : this.testConnectionsOnReserve;
   }

   public void setTestConnectionsOnReserve(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setTestConnectionsOnReserve(var1);
      } else {
         this.testConnectionsOnReserve = var1;
      }

   }

   public boolean getTestConnectionsOnRelease() {
      return this.delegate != null ? JDBCMBeanConverter.isInternalPropertySet(this.delegate.getJDBCResource(), "TestConnectionsOnRelease") : this.testConnectionsOnRelease;
   }

   public void setTestConnectionsOnRelease(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "TestConnectionsOnRelease", Boolean.toString(var1));
      } else {
         this.testConnectionsOnRelease = var1;
      }

   }

   public boolean getTestConnectionsOnCreate() {
      return this.delegate != null ? JDBCMBeanConverter.isInternalPropertySet(this.delegate.getJDBCResource(), "TestConnectionsOnCreate") : this.testConnectionsOnCreate;
   }

   public void setTestConnectionsOnCreate(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "TestConnectionsOnCreate", Boolean.toString(var1));
      } else {
         this.testConnectionsOnCreate = var1;
      }

   }

   public byte[] getPasswordEncrypted() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDriverParams().getPasswordEncrypted() : this.passwordEncrypted;
   }

   public void setPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDriverParams().setPasswordEncrypted(var1);
      } else {
         this.passwordEncrypted = var1;
      }

   }

   public int getJDBCXADebugLevel() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getJDBCXADebugLevel() : this.jdbcXADebugLevel;
   }

   public void setJDBCXADebugLevel(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setJDBCXADebugLevel(var1);
      } else {
         this.jdbcXADebugLevel = var1;
      }

   }

   public boolean getSupportsLocalTransaction() {
      return true;
   }

   public void setSupportsLocalTransaction(boolean var1) {
   }

   public boolean getKeepXAConnTillTxComplete() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isKeepXaConnTillTxComplete() : this.keepXaConnTillTxComplete;
   }

   public void setKeepXAConnTillTxComplete(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setKeepXaConnTillTxComplete(var1);
      } else {
         this.keepXaConnTillTxComplete = var1;
      }

   }

   public boolean getNeedTxCtxOnClose() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isNeedTxCtxOnClose() : this.needTxCtxOnClose;
   }

   public void setNeedTxCtxOnClose(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setNeedTxCtxOnClose(var1);
      } else {
         this.needTxCtxOnClose = var1;
      }

   }

   public boolean getXAEndOnlyOnce() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isXaEndOnlyOnce() : this.xaEndOnlyOnce;
   }

   public void setXAEndOnlyOnce(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setXaEndOnlyOnce(var1);
      } else {
         this.xaEndOnlyOnce = var1;
      }

   }

   public boolean getNewXAConnForCommit() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isNewXaConnForCommit() : this.newXaConnForCommit;
   }

   public void setNewXAConnForCommit(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setNewXaConnForCommit(var1);
      } else {
         this.newXaConnForCommit = var1;
      }

   }

   public boolean getKeepLogicalConnOpenOnRelease() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isKeepLogicalConnOpenOnRelease() : this.keepLogicalConnOpenOnRelease;
   }

   public void setKeepLogicalConnOpenOnRelease(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setKeepLogicalConnOpenOnRelease(var1);
      } else {
         this.keepLogicalConnOpenOnRelease = var1;
      }

   }

   public int getXAPreparedStatementCacheSize() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getStatementCacheSize() : this.statementCacheSize;
   }

   public void setXAPreparedStatementCacheSize(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setStatementCacheSize(var1);
      } else {
         this.statementCacheSize = var1;
      }

   }

   public int getPreparedStatementCacheSize() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getStatementCacheSize() : this.statementCacheSize;
   }

   public void setPreparedStatementCacheSize(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setStatementCacheSize(var1);
      } else {
         this.statementCacheSize = var1;
      }

   }

   public boolean getEnableResourceHealthMonitoring() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isResourceHealthMonitoring() : this.enableResourceHealthMonitoring;
   }

   public void setEnableResourceHealthMonitoring(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setResourceHealthMonitoring(var1);
      } else {
         this.enableResourceHealthMonitoring = var1;
      }

   }

   public boolean getRecoverOnlyOnce() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isRecoverOnlyOnce() : this.recoverOnlyOnce;
   }

   public void setRecoverOnlyOnce(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setRecoverOnlyOnce(var1);
      } else {
         this.recoverOnlyOnce = var1;
      }

   }

   public boolean getXASetTransactionTimeout() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isXaSetTransactionTimeout() : this.xaSetTransactionTimeout;
   }

   public void setXASetTransactionTimeout(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setXaSetTransactionTimeout(var1);
      } else {
         this.xaSetTransactionTimeout = var1;
      }

   }

   public int getConnectionReserveTimeoutSeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getConnectionReserveTimeoutSeconds() : this.connectionReserveTimeoutSeconds;
   }

   public void setConnectionReserveTimeoutSeconds(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setConnectionReserveTimeoutSeconds(var1);
      } else {
         this.connectionReserveTimeoutSeconds = var1;
      }

   }

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getConnectionCreationRetryFrequencySeconds() : this.connectionCreationRetryFrequencySeconds;
   }

   public void setConnectionCreationRetryFrequencySeconds(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setConnectionCreationRetryFrequencySeconds(var1);
      } else {
         this.connectionCreationRetryFrequencySeconds = var1;
      }

   }

   public int getInactiveConnectionTimeoutSeconds() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getInactiveConnectionTimeoutSeconds() : this.inactiveConnectionTimeoutSeconds;
   }

   public void setInactiveConnectionTimeoutSeconds(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setInactiveConnectionTimeoutSeconds(var1);
      } else {
         this.inactiveConnectionTimeoutSeconds = var1;
      }

   }

   public int getHighestNumWaiters() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getHighestNumWaiters() : this.highestNumWaiters;
   }

   public void setHighestNumWaiters(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setHighestNumWaiters(var1);
      } else {
         this.highestNumWaiters = var1;
      }

   }

   public int getHighestNumUnavailable() {
      if (this.delegate != null) {
         JDBCPropertyBean var1;
         return (var1 = this.delegate.getJDBCResource().getInternalProperties().lookupProperty("HighestNumUnavailable")) != null ? Integer.parseInt(var1.getValue()) : 0;
      } else {
         return this.highestNumUnavailable;
      }
   }

   public void setHighestNumUnavailable(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "HighestNumUnavailable", Integer.toString(var1));
      } else {
         this.highestNumUnavailable = var1;
      }

   }

   public void setInitSQL(String var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setInitSql(var1);
      } else {
         this.initSQL = var1;
      }

   }

   public String getInitSQL() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getInitSql() : this.initSQL;
   }

   public void setStatementCacheSize(int var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setStatementCacheSize(var1);
      } else {
         this.statementCacheSize = var1;
      }

   }

   public int getStatementCacheSize() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getStatementCacheSize() : this.statementCacheSize;
   }

   public void setStatementCacheType(String var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setStatementCacheType(var1);
      } else {
         this.statementCacheType = var1;
      }

   }

   public String getStatementCacheType() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getStatementCacheType() : this.statementCacheType;
   }

   public void setRemoveInfectedConnectionsEnabled(boolean var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setRemoveInfectedConnections(var1);
      } else {
         this.removeInfectedConnectionsEnabled = var1;
      }

   }

   public boolean isRemoveInfectedConnectionsEnabled() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().isRemoveInfectedConnections() : this.removeInfectedConnectionsEnabled;
   }

   public void setRollbackLocalTxUponConnClose(boolean var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCXAParams().setRollbackLocalTxUponConnClose(var1);
      } else {
         this.rollbackLocalTxUponConnClose = var1;
      }

   }

   public boolean getRollbackLocalTxUponConnClose() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCXAParams().isRollbackLocalTxUponConnClose() : this.rollbackLocalTxUponConnClose;
   }

   public void setTestStatementTimeout(int var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setStatementTimeout(var1);
      } else {
         this.statementTimeout = var1;
      }

   }

   public int getTestStatementTimeout() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getStatementTimeout() : this.statementTimeout;
   }

   public void setStatementTimeout(int var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setStatementTimeout(var1);
      } else {
         this.statementTimeout = var1;
      }

   }

   public int getStatementTimeout() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().getStatementTimeout() : this.statementTimeout;
   }

   public void setIgnoreInUseConnectionsEnabled(boolean var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setIgnoreInUseConnectionsEnabled(var1);
      } else {
         this.ignoreInUseConnectionsEnabled = var1;
      }

   }

   public boolean isIgnoreInUseConnectionsEnabled() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().isIgnoreInUseConnectionsEnabled() : this.ignoreInUseConnectionsEnabled;
   }

   public void setCredentialMappingEnabled(boolean var1) {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCConnectionPoolParams().setCredentialMappingEnabled(var1);
      } else {
         this.credentialMappingEnabled = var1;
      }

   }

   public boolean isCredentialMappingEnabled() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCConnectionPoolParams().isCredentialMappingEnabled() : this.credentialMappingEnabled;
   }

   public int getCountOfTestFailuresTillFlush() {
      if (this.delegate != null) {
         JDBCPropertyBean var1;
         return (var1 = this.delegate.getJDBCResource().getInternalProperties().lookupProperty("CountOfTestFailuresTillFlush")) != null ? Integer.parseInt(var1.getValue()) : JDBCLegalHelper.computeCountTillFlush(this.getTestFrequencySeconds(), this.getMaxCapacity());
      } else {
         return this.countOfTestFailuresTillFlush;
      }
   }

   public void setCountOfTestFailuresTillFlush(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "CountOfTestFailuresTillFlush", Integer.toString(var1));
      } else {
         this.countOfTestFailuresTillFlush = var1;
      }

   }

   public int getCountOfRefreshFailuresTillDisable() {
      if (this.delegate != null) {
         JDBCPropertyBean var1;
         return (var1 = this.delegate.getJDBCResource().getInternalProperties().lookupProperty("CountOfRefreshFailuresTillDisable")) != null ? Integer.parseInt(var1.getValue()) : 2;
      } else {
         return this.countOfRefreshFailuresTillDisable;
      }
   }

   public void setCountOfRefreshFailuresTillDisable(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "CountOfRefreshFailuresTillDisable", Integer.toString(var1));
      } else {
         this.countOfRefreshFailuresTillDisable = var1;
      }

   }
}
