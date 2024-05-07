package weblogic.jdbc.common.internal;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCPropertiesBean;
import weblogic.j2ee.descriptor.wl.JDBCPropertyBean;
import weblogic.jdbc.module.JDBCDeploymentHelper;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.JDBCDataSourceMBean;
import weblogic.management.configuration.JDBCMultiPoolMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JDBCTxDataSourceMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class JDBCMBeanConverter {
   protected static JDBCConnectionPoolMBean myPoolMBean;
   private static DescriptorManager dm = new DescriptorManager();
   private static final AuthenticatedSubject KERNELID = getKernelID();

   private static AuthenticatedSubject getKernelID() {
      return (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   public static JDBCDataSourceBean getJDBCDataSourceDescriptor(JDBCConnectionPoolMBean var0, JDBCDataSourceBean var1) {
      if (var1 == null) {
         var1 = (JDBCDataSourceBean)dm.createDescriptorRoot(JDBCDataSourceBean.class).getRootBean();
      }

      init(var1, var0, 1);
      initDriverParams(var1, var0);
      initPoolParams(var1, var0);
      initXAParams(var1, var0);
      return var1;
   }

   public static JDBCDataSourceBean getJDBCDataSourceDescriptor(JDBCMultiPoolMBean var0, JDBCDataSourceBean var1) {
      if (var1 == null) {
         var1 = (JDBCDataSourceBean)dm.createDescriptorRoot(JDBCDataSourceBean.class).getRootBean();
      }

      init(var1, var0, 2);
      initDataSourceParams(var1, var0);
      if (var0.isSet("HealthCheckFrequencySeconds")) {
         var1.getJDBCConnectionPoolParams().setTestFrequencySeconds(var0.getHealthCheckFrequencySeconds());
      }

      return var1;
   }

   public static JDBCDataSourceBean getJDBCDataSourceDescriptor(JDBCDataSourceMBean var0, JDBCDataSourceBean var1) {
      if (var1 == null) {
         var1 = (JDBCDataSourceBean)dm.createDescriptorRoot(JDBCDataSourceBean.class).getRootBean();
      }

      init(var1, var0, 3);
      initDataSourceParams(var1, var0);
      var1.getJDBCDataSourceParams().setGlobalTransactionsProtocol("None");
      return var1;
   }

   public static JDBCDataSourceBean getJDBCDataSourceDescriptor(JDBCTxDataSourceMBean var0, JDBCDataSourceBean var1, DomainMBean var2) {
      if (var1 == null) {
         var1 = (JDBCDataSourceBean)dm.createDescriptorRoot(JDBCDataSourceBean.class).getRootBean();
      }

      init(var1, var0, 4);
      initDataSourceParams(var1, var0);
      String var3 = getInternalProperty(var1, "LegacyPoolName");
      if (var3 != null) {
         String var4 = JDBCDeploymentHelper.getSystemResourceName(var3, 1);
         JDBCSystemResourceMBean var5 = var2.lookupJDBCSystemResource(var4);
         if (var5 != null) {
            String var6 = var5.getJDBCResource().getJDBCDriverParams().getDriverName();
            if (DataSourceUtil.isXADataSource(var6)) {
               var1.getJDBCDataSourceParams().setGlobalTransactionsProtocol("TwoPhaseCommit");
            } else if (var0.getEnableTwoPhaseCommit()) {
               var1.getJDBCDataSourceParams().setGlobalTransactionsProtocol("EmulateTwoPhaseCommit");
            } else {
               var1.getJDBCDataSourceParams().setGlobalTransactionsProtocol("OnePhaseCommit");
            }
         }
      }

      return var1;
   }

   public static void setDriverProperties(JDBCDataSourceBean var0, Properties var1) {
      JDBCPropertiesBean var2 = var0.getJDBCDriverParams().getProperties();
      JDBCPropertyBean[] var3 = var2.getProperties();
      int var4;
      if (var1 == null) {
         if (var3 != null) {
            for(var4 = 0; var4 < var3.length; ++var4) {
               var2.destroyProperty(var3[var4]);
            }

         }
      } else {
         if (var3 != null) {
            for(var4 = 0; var4 < var3.length; ++var4) {
               var2.destroyProperty(var3[var4]);
            }
         }

         Enumeration var7 = var1.propertyNames();

         while(var7.hasMoreElements()) {
            String var5 = (String)var7.nextElement();
            if ("password".equals(var5)) {
               var0.getJDBCDriverParams().setPassword(var1.getProperty(var5));
            } else {
               var2.createProperty(var5, var1.getProperty(var5));
            }
         }

      }
   }

   public static boolean isInternalPropertySet(JDBCDataSourceBean var0, String var1) {
      if (var0 == null) {
         return false;
      } else {
         JDBCPropertyBean var2 = var0.getInternalProperties().lookupProperty(var1);
         return var2 == null ? false : Boolean.valueOf(var2.getValue());
      }
   }

   public static int getLegacyType(JDBCDataSourceBean var0) {
      int var1 = 0;
      String var2 = getInternalProperty(var0, "LegacyType");
      if (var2 != null) {
         var1 = Integer.parseInt(var2);
      }

      return var1;
   }

   public static String getInternalProperty(JDBCDataSourceBean var0, String var1) {
      JDBCPropertyBean var2 = null;

      try {
         var2 = var0.getInternalProperties().lookupProperty(var1);
      } catch (Exception var4) {
      }

      return var2 == null ? null : var2.getValue();
   }

   public static void setInternalProperty(JDBCDataSourceBean var0, String var1, String var2) {
      JDBCPropertiesBean var3 = var0.getInternalProperties();
      JDBCPropertyBean var4 = var3.lookupProperty(var1);
      if (var4 == null) {
         var3.createProperty(var1, var2);
      } else {
         var4.setValue(var2);
      }

   }

   private static void init(JDBCDataSourceBean var0, DeploymentMBean var1, int var2) {
      var0.setName(var1.getName());
      setInternalProperty(var0, "LegacyType", Integer.toString(var2));
   }

   private static void initDriverParams(JDBCDataSourceBean var0, JDBCConnectionPoolMBean var1) {
      if (var1.isSet("URL")) {
         var0.getJDBCDriverParams().setUrl(var1.getURL());
      }

      if (var1.isSet("DriverName")) {
         var0.getJDBCDriverParams().setDriverName(var1.getDriverName());
      }

      if (var1.isSet("Properties")) {
         Properties var2 = null;
         myPoolMBean = var1;

         try {
            var2 = (Properties)SecurityServiceManager.runAs(KERNELID, KERNELID, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return JDBCMBeanConverter.getProperties();
               }
            });
         } catch (PrivilegedActionException var7) {
         }

         if (var2 != null) {
            setDriverProperties(var0, var2);
         }
      }

      String var8 = var1.getPassword();
      if (var8 != null) {
         var0.getJDBCDriverParams().setPassword(var8);
      }

      byte[] var3 = var1.getPasswordEncrypted();
      if (var3 != null) {
         EncryptionService var4 = SerializedSystemIni.getEncryptionService();
         ClearOrEncryptedService var5 = new ClearOrEncryptedService(var4);
         String var6 = var5.decrypt(new String(var3));
         if (!"".equals(var6)) {
            var0.getJDBCDriverParams().setPassword(var6);
         }
      }

   }

   private static Properties getProperties() {
      return myPoolMBean.getProperties();
   }

   private static void initPoolParams(JDBCDataSourceBean var0, JDBCConnectionPoolMBean var1) {
      int var3;
      if (var1.isSet("SqlStmtProfilingEnabled") && var1.isSqlStmtProfilingEnabled()) {
         var3 = var0.getJDBCConnectionPoolParams().getProfileType();
         var3 |= 32;
         var0.getJDBCConnectionPoolParams().setProfileType(var3);
         setInternalProperty(var0, "SqlStmtProfilingEnabled", Boolean.toString(var1.isSqlStmtProfilingEnabled()));
      }

      if (var1.isSet("ConnLeakProfilingEnabled") && var1.isConnLeakProfilingEnabled()) {
         var3 = var0.getJDBCConnectionPoolParams().getProfileType();
         var3 |= 4;
         var0.getJDBCConnectionPoolParams().setProfileType(var3);
         setInternalProperty(var0, "ConnLeakProfilingEnabled", Boolean.toString(var1.isConnLeakProfilingEnabled()));
      }

      if (var1.isSet("ConnProfilingEnabled") && var1.isConnProfilingEnabled()) {
         var3 = var0.getJDBCConnectionPoolParams().getProfileType();
         var3 |= 64;
         var0.getJDBCConnectionPoolParams().setProfileType(var3);
      }

      if (var1.isSet("InitialCapacity")) {
         var0.getJDBCConnectionPoolParams().setInitialCapacity(var1.getInitialCapacity());
      }

      if (var1.isSet("MaxCapacity")) {
         var0.getJDBCConnectionPoolParams().setMaxCapacity(var1.getMaxCapacity());
      }

      if (var1.isSet("CapacityIncrement")) {
         var0.getJDBCConnectionPoolParams().setCapacityIncrement(var1.getCapacityIncrement());
      }

      boolean var11 = false;
      boolean var4 = false;
      boolean var5 = false;
      int var6 = 0;
      if (var1.isSet("ShrinkPeriodMinutes")) {
         var11 = true;
         var6 = var1.getShrinkPeriodMinutes() * 60;
      }

      if (var1.isSet("ShrinkFrequencySeconds")) {
         var11 = true;
         var6 = var1.getShrinkFrequencySeconds();
      }

      if (var1.isSet("ShrinkingEnabled")) {
         var4 = true;
         var5 = var1.isShrinkingEnabled();
      }

      int var2;
      if (var4) {
         var2 = 900;
         if (var5) {
            if (var11 && var6 > 0) {
               var2 = var6;
            }
         } else {
            var2 = 0;
         }

         var0.getJDBCConnectionPoolParams().setShrinkFrequencySeconds(var2);
      }

      if (var1.isSet("RefreshMinutes")) {
         var0.getJDBCConnectionPoolParams().setTestFrequencySeconds(var1.getRefreshMinutes() * 60);
      }

      if (var1.isSet("TestFrequencySeconds")) {
         var0.getJDBCConnectionPoolParams().setTestFrequencySeconds(var1.getTestFrequencySeconds());
      }

      if (var1.isSet("TestConnectionsOnReserve")) {
         var0.getJDBCConnectionPoolParams().setTestConnectionsOnReserve(var1.getTestConnectionsOnReserve());
      }

      if (var1.isSet("TestConnectionsOnRelease")) {
         setInternalProperty(var0, "TestConnectionsOnRelease", Boolean.toString(var1.getTestConnectionsOnRelease()));
      }

      if (var1.isSet("TestConnectionsOnCreate")) {
         setInternalProperty(var0, "TestConnectionsOnCreate", Boolean.toString(var1.getTestConnectionsOnCreate()));
      }

      if (var1.isSet("JDBCXADebugLevel")) {
         var0.getJDBCConnectionPoolParams().setJDBCXADebugLevel(var1.getJDBCXADebugLevel());
      }

      if (var1.isSet("ConnectionReserveTimeoutSeconds")) {
         var0.getJDBCConnectionPoolParams().setConnectionReserveTimeoutSeconds(var1.getConnectionReserveTimeoutSeconds());
      }

      if (var1.isSet("ConnectionCreationRetryFrequencySeconds")) {
         var0.getJDBCConnectionPoolParams().setConnectionCreationRetryFrequencySeconds(var1.getConnectionCreationRetryFrequencySeconds());
      }

      if (var1.isSet("InactiveConnectionTimeoutSeconds")) {
         var0.getJDBCConnectionPoolParams().setInactiveConnectionTimeoutSeconds(var1.getInactiveConnectionTimeoutSeconds());
      }

      if (var1.isSet("HighestNumWaiters")) {
         var0.getJDBCConnectionPoolParams().setHighestNumWaiters(var1.getHighestNumWaiters());
      }

      if (var1.isSet("HighestNumUnavailable")) {
         setInternalProperty(var0, "HighestNumUnavailable", Integer.toString(var1.getHighestNumUnavailable()));
      }

      if (var1.isSet("TestTableName")) {
         var0.getJDBCConnectionPoolParams().setTestTableName(var1.getTestTableName());
      }

      if (var1.isSet("LoginDelaySeconds")) {
         var0.getJDBCConnectionPoolParams().setLoginDelaySeconds(var1.getLoginDelaySeconds());
      }

      if (var1.isSet("InitSQL")) {
         var0.getJDBCConnectionPoolParams().setInitSql(var1.getInitSQL());
      }

      if (var1.isSet("PreparedStatementCacheSize")) {
         var0.getJDBCConnectionPoolParams().setStatementCacheSize(var1.getPreparedStatementCacheSize());
      }

      if (var1.isSet("XAPreparedStatementCacheSize")) {
         var0.getJDBCConnectionPoolParams().setStatementCacheSize(var1.getXAPreparedStatementCacheSize());
      }

      if (var1.isSet("StatementCacheSize")) {
         var0.getJDBCConnectionPoolParams().setStatementCacheSize(var1.getStatementCacheSize());
      }

      if (var1.isSet("StatementCacheType")) {
         var0.getJDBCConnectionPoolParams().setStatementCacheType(var1.getStatementCacheType());
      }

      if (var1.isSet("RemoveInfectedConnectionsEnabled")) {
         var0.getJDBCConnectionPoolParams().setRemoveInfectedConnections(var1.isRemoveInfectedConnectionsEnabled());
      }

      if (var1.isSet("SecondsToTrustAnIdlePoolConnection")) {
         var0.getJDBCConnectionPoolParams().setSecondsToTrustAnIdlePoolConnection(var1.getSecondsToTrustAnIdlePoolConnection());
      }

      boolean var7 = false;
      boolean var8 = false;
      int var9 = -1;
      int var10 = -1;
      var2 = -1;
      if (var1.isSet("TestStatementTimeout")) {
         var7 = true;
         var9 = var1.getTestStatementTimeout();
      }

      if (var1.isSet("StatementTimeout")) {
         var8 = true;
         var10 = var1.getStatementTimeout();
      }

      if (var7 && var8) {
         var2 = Math.min(var10, var9);
      } else if (var8) {
         var2 = var10;
      } else if (var7) {
         var2 = var9;
      }

      if (var2 > -1) {
         var0.getJDBCConnectionPoolParams().setStatementTimeout(var2);
      }

      if (var1.isSet("IgnoreInUseConnectionsEnabled")) {
         var0.getJDBCConnectionPoolParams().setIgnoreInUseConnectionsEnabled(var1.isIgnoreInUseConnectionsEnabled());
      }

      if (var1.isSet("CredentialMappingEnabled")) {
         var0.getJDBCConnectionPoolParams().setCredentialMappingEnabled(var1.isCredentialMappingEnabled());
      }

      if (var1.isSet("CountOfTestFailuresTillFlush")) {
         setInternalProperty(var0, "CountOfTestFailuresTillFlush", Integer.toString(var1.getCountOfTestFailuresTillFlush()));
      }

      if (var1.isSet("CountOfRefreshFailuresTillDisable")) {
         setInternalProperty(var0, "CountOfRefreshFailuresTillDisable", Integer.toString(var1.getCountOfRefreshFailuresTillDisable()));
      }

   }

   private static void initXAParams(JDBCDataSourceBean var0, JDBCConnectionPoolMBean var1) {
      if (var1.isSet("KeepXAConnTillTxComplete")) {
         var0.getJDBCXAParams().setKeepXaConnTillTxComplete(var1.getKeepXAConnTillTxComplete());
      }

      if (var1.isSet("NeedTxCtxOnClose")) {
         var0.getJDBCXAParams().setNeedTxCtxOnClose(var1.getNeedTxCtxOnClose());
      }

      if (var1.isSet("XAEndOnlyOnce")) {
         var0.getJDBCXAParams().setXaEndOnlyOnce(var1.getXAEndOnlyOnce());
      }

      if (var1.isSet("NewXAConnForCommit")) {
         var0.getJDBCXAParams().setNewXaConnForCommit(var1.getNewXAConnForCommit());
      }

      if (var1.isSet("KeepLogicalConnOpenOnRelease")) {
         var0.getJDBCXAParams().setKeepLogicalConnOpenOnRelease(var1.getKeepLogicalConnOpenOnRelease());
      }

      if (var1.isSet("EnableResourceHealthMonitoring")) {
         var0.getJDBCXAParams().setResourceHealthMonitoring(var1.getEnableResourceHealthMonitoring());
      }

      if (var1.isSet("RecoverOnlyOnce")) {
         var0.getJDBCXAParams().setRecoverOnlyOnce(var1.getRecoverOnlyOnce());
      }

      if (var1.isSet("XASetTransactionTimeout")) {
         var0.getJDBCXAParams().setXaSetTransactionTimeout(var1.getXASetTransactionTimeout());
      }

      if (var1.isSet("XATransactionTimeout")) {
         var0.getJDBCXAParams().setXaTransactionTimeout(var1.getXATransactionTimeout());
      }

      if (var1.isSet("RollbackLocalTxUponConnClose")) {
         var0.getJDBCXAParams().setRollbackLocalTxUponConnClose(var1.getRollbackLocalTxUponConnClose());
      }

      if (var1.isSet("XARetryDurationSeconds")) {
         var0.getJDBCXAParams().setXaRetryDurationSeconds(var1.getXARetryDurationSeconds());
      }

      if (var1.isSet("XARetryIntervalSeconds")) {
         var0.getJDBCXAParams().setXaRetryIntervalSeconds(var1.getXARetryIntervalSeconds());
      }

   }

   private static void initDataSourceParams(JDBCDataSourceBean var0, JDBCMultiPoolMBean var1) {
      String var2;
      if (var1.isSet("AlgorithmType")) {
         var2 = var1.getAlgorithmType();
         if ("High-Availability".equals(var2)) {
            var0.getJDBCDataSourceParams().setAlgorithmType("Failover");
         } else {
            var0.getJDBCDataSourceParams().setAlgorithmType(var2);
         }
      }

      var2 = getDataSources(var1);
      if (var2 != null) {
         var0.getJDBCDataSourceParams().setDataSourceList(var2);
      }

   }

   private static void initDataSourceParams(JDBCDataSourceBean var0, JDBCDataSourceMBean var1) {
      String var2 = ";";
      if (var1.isSet("JNDINameSeparator")) {
         var2 = var1.getJNDINameSeparator();
         setInternalProperty(var0, "JNDINameSeparator", var2);
      }

      if (var1.isSet("JNDIName")) {
         String var3 = var1.getJNDIName();
         StringTokenizer var4 = new StringTokenizer(var3, var2);
         String[] var5 = new String[var4.countTokens()];

         for(int var6 = 0; var4.hasMoreTokens(); var5[var6++] = var4.nextToken()) {
         }

         var0.getJDBCDataSourceParams().setJNDINames(var5);
      }

      if (var1.isSet("PoolName")) {
         setInternalProperty(var0, "LegacyPoolName", var1.getPoolName());
      }

      if (var1.isSet("RowPrefetchEnabled")) {
         var0.getJDBCDataSourceParams().setRowPrefetch(var1.isRowPrefetchEnabled());
      }

      if (var1.isSet("RowPrefetchSize")) {
         var0.getJDBCDataSourceParams().setRowPrefetchSize(var1.getRowPrefetchSize());
      }

      if (var1.isSet("StreamChunkSize")) {
         var0.getJDBCDataSourceParams().setStreamChunkSize(var1.getStreamChunkSize());
      }

   }

   private static void initDataSourceParams(JDBCDataSourceBean var0, JDBCTxDataSourceMBean var1) {
      String var2 = ";";
      if (var1.isSet("JNDINameSeparator")) {
         var2 = var1.getJNDINameSeparator();
         setInternalProperty(var0, "JNDINameSeparator", var2);
      }

      if (var1.isSet("JNDIName")) {
         String var3 = var1.getJNDIName();
         StringTokenizer var4 = new StringTokenizer(var3, var2);
         String[] var5 = new String[var4.countTokens()];

         for(int var6 = 0; var4.hasMoreTokens(); var5[var6++] = var4.nextToken()) {
         }

         var0.getJDBCDataSourceParams().setJNDINames(var5);
      }

      if (var1.isSet("PoolName")) {
         setInternalProperty(var0, "LegacyPoolName", var1.getPoolName());
      }

      if (var1.isSet("RowPrefetchEnabled")) {
         var0.getJDBCDataSourceParams().setRowPrefetch(var1.isRowPrefetchEnabled());
      }

      if (var1.isSet("RowPrefetchSize")) {
         var0.getJDBCDataSourceParams().setRowPrefetchSize(var1.getRowPrefetchSize());
      }

      if (var1.isSet("StreamChunkSize")) {
         var0.getJDBCDataSourceParams().setStreamChunkSize(var1.getStreamChunkSize());
      }

   }

   private static String getDataSources(JDBCMultiPoolMBean var0) {
      JDBCConnectionPoolMBean[] var1 = var0.getPoolList();
      int var2 = var1.length;
      if (var2 == 0) {
         return null;
      } else {
         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var2; ++var4) {
            var3.append(var1[var4].getName());
            var3.append(",");
         }

         var3.deleteCharAt(var3.lastIndexOf(","));
         return var3.toString();
      }
   }

   public static Properties getProperties(JDBCPropertyBean[] var0) {
      return JDBCUtil.getProperties(var0);
   }
}
