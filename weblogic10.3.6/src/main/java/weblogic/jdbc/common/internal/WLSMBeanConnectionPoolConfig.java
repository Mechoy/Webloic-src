package weblogic.jdbc.common.internal;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import weblogic.common.ResourceException;
import weblogic.j2ee.descriptor.wl.ApplicationPoolParamsBean;
import weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBean;
import weblogic.j2ee.descriptor.wl.ConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ConnectionPropertiesBean;
import weblogic.j2ee.descriptor.wl.DriverParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.PreparedStatementBean;
import weblogic.j2ee.descriptor.wl.SizeParamsBean;
import weblogic.j2ee.descriptor.wl.XAParamsBean;
import weblogic.management.configuration.JDBCDataSourceFactoryMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

/** @deprecated */
public class WLSMBeanConnectionPoolConfig implements ConnectionPoolConfig {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private JDBCConnectionPoolBean cpBean;
   private JDBCDataSourceFactoryMBean factoryMBean;
   private JDBCDataSourceBean dsBean;
   private final String name;
   private String driver = null;
   private String url = null;
   private Properties driverProperties;
   private boolean removeInfectedConn = true;
   boolean credentialMappingEnabled = false;
   private boolean oldAppScopedPool;
   private boolean pinnedToThread = false;
   private boolean createConnectionInline = false;
   private ConnectionInfo defaultConnectionInfo = null;
   private boolean onePinnedConnectionOnly = false;
   boolean identityBasedConnectionPoolingEnabled = false;
   private boolean nativeXA = false;
   private String DD_XA_TX_GROUP_NAME = "XATransactionGroup";
   private int profileType;

   public WLSMBeanConnectionPoolConfig(JDBCConnectionPoolBean var1, JDBCDataSourceFactoryMBean var2) {
      this.cpBean = var1;
      this.factoryMBean = var2;
      this.oldAppScopedPool = true;
      this.name = null;
   }

   public Properties getPoolProperties() throws ResourceException {
      if (JDBCUtil.JDBCInternal.isDebugEnabled()) {
         JDBCUtil.JDBCInternal.debug(" > CP(" + this.name + "):doStart (10) oldAppScopedPool = " + Boolean.toString(this.oldAppScopedPool));
      }

      try {
         Properties var1 = (Properties)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return WLSMBeanConnectionPoolConfig.this.initJDBCAndGetPoolParameters();
            }
         });
         return var1;
      } catch (PrivilegedActionException var3) {
         if (JDBCUtil.JDBCInternal.isDebugEnabled()) {
            JDBCUtil.JDBCInternal.debug(" <* CP(" + this.name + "):doStart(30)");
         }

         throw new ResourceException(var3.toString());
      }
   }

   private Properties initJDBCAndGetPoolParameters() {
      if (JDBCUtil.JDBCInternal.isDebugEnabled()) {
         JDBCUtil.JDBCInternal.debug(" > CP(" + this.name + "):initJDBCAndGetPoolParams (10)");
      }

      Properties var1 = new Properties();
      boolean var2 = false;
      String var3 = null;
      if (this.driverProperties == null) {
         this.driverProperties = new Properties();
      }

      Object var12;
      if (this.cpBean != null) {
         if ((var3 = this.cpBean.getDataSourceJNDIName()) != null) {
            var1.setProperty("name", var3);
         }

         ApplicationPoolParamsBean var4 = this.cpBean.getPoolParams();
         if (var4 != null) {
            var1.setProperty("createDelay", Integer.toString(var4.getLoginDelaySeconds()));
            var1.setProperty("DebugLevel", Integer.toString(var4.getJDBCXADebugLevel()));
            this.removeInfectedConn = var4.isRemoveInfectedConnectionsEnabled();
            SizeParamsBean var5 = var4.getSizeParams();
            int var8;
            if (var5 != null) {
               var1.setProperty("maxCapacity", Integer.toString(var5.getMaxCapacity()));
               var1.setProperty("capacityIncrement", Integer.toString(var5.getCapacityIncrement()));
               var1.setProperty("initialCapacity", Integer.toString(var5.getInitialCapacity()));
               var1.setProperty("maxWaiters", Integer.toString(var5.getHighestNumWaiters()));
               var1.setProperty("maxUnavl", Integer.toString(var5.getHighestNumUnavailable()));
               var1.setProperty("shrinkEnabled", Boolean.toString(var5.isShrinkingEnabled()));
               var1.setProperty("shrinkFrequencySeconds", Integer.toString(var5.getShrinkPeriodMinutes() * 60));
               if ((var8 = var5.getShrinkFrequencySeconds()) > 0) {
                  var1.setProperty("shrinkFrequencySeconds", Integer.toString(var8));
               }
            }

            ConnectionCheckParamsBean var6 = var4.getConnectionCheckParams();
            if (var6 != null) {
               if ((var3 = var6.getTableName()) != null) {
                  var1.setProperty("testName", var3);
               }

               if ((var3 = var6.getInitSql()) != null) {
                  var1.setProperty("initName", var3);
               }

               var1.setProperty("testOnReserve", Boolean.toString(var6.isCheckOnReserveEnabled()));
               var1.setProperty("testOnRelease", Boolean.toString(var6.isCheckOnReleaseEnabled()));
               var1.setProperty("testOnCreate", Boolean.toString(var6.isCheckOnCreateEnabled()));
               var1.setProperty("resvTimeoutSeconds", Integer.toString(var6.getConnectionReserveTimeoutSeconds()));
               var1.setProperty("resCreationRetrySeconds", Integer.toString(var6.getConnectionCreationRetryFrequencySeconds()));
               var1.setProperty("inactiveResTimeoutSeconds", Integer.toString(var6.getInactiveConnectionTimeoutSeconds()));
               var1.setProperty("testFrequencySeconds", Integer.toString(var6.getRefreshMinutes() * 60));
               if ((var8 = var6.getTestFrequencySeconds()) > 0) {
                  var1.setProperty("testFrequencySeconds", Integer.toString(var8));
               }
            }

            XAParamsBean var7 = var4.getXAParams();
            if (var7 != null) {
               var1.setProperty("PSCacheSize", Integer.toString(var7.getPreparedStatementCacheSize()));
            }
         }

         var12 = this.cpBean.getDriverParams();
         if (var12 != null) {
            PreparedStatementBean var13 = ((DriverParamsBean)var12).getPreparedStatement();
            if (var13 != null) {
               var1.setProperty("PSCacheSize", Integer.toString(var13.getCacheSize()));
            }
         }

         ConnectionFactoryBean var14 = this.cpBean.getConnectionFactory();
         if (var14 != null) {
            ConnectionPropertiesBean var16 = var14.getConnectionProperties();
            if (var16 != null) {
               if ((var3 = var16.getUrl()) != null) {
                  var1.setProperty("Url", var3);
               }

               this.driver = var16.getDriverClassName();
               if ((var3 = var16.getUserName()) != null) {
                  this.driverProperties.put("user", var3);
               }

               if ((var3 = var16.getPassword()) != null) {
                  this.driverProperties.put("password", var3);
               }
            }
         }
      }

      if (this.factoryMBean != null) {
         if (var1.getProperty("Url") == null && (var3 = this.factoryMBean.getURL()) != null) {
            var1.setProperty("Url", var3);
         }

         if (this.driver == null) {
            this.driver = this.factoryMBean.getDriverClassName();
         }

         if (this.driverProperties.get("user") == null && (var3 = this.factoryMBean.getUserName()) != null) {
            this.driverProperties.put("user", var3);
         }

         if (this.driverProperties.get("password") == null && (var3 = this.factoryMBean.getPassword()) != null) {
            this.driverProperties.put("password", var3);
         }

         Map var9 = this.factoryMBean.getProperties();
         if (var9 != null) {
            this.mergeDefaultProps(var9);
         }
      }

      String var15;
      if ((var3 = this.driverProperties.getProperty("password")) != null) {
         EncryptionService var10 = SerializedSystemIni.getEncryptionService();
         var12 = new ClearOrEncryptedService(var10);
         var15 = ((ClearOrEncryptedService)var12).decrypt(var3);
         this.driverProperties.setProperty("password", var15);
      }

      if (DataSourceUtil.isXADataSource(this.driver)) {
         var1.setProperty("UseXAInterface", "true");
      }

      if (!this.driver.equals("oracle.jdbc.xa.client.OracleXADataSource") || !"true".equalsIgnoreCase(this.driverProperties.getProperty("nativeXA")) && !"true".equalsIgnoreCase(this.driverProperties.getProperty("NativeXA"))) {
         if ("true".equalsIgnoreCase((String)this.driverProperties.remove("PinnedToThread")) || "true".equalsIgnoreCase((String)this.driverProperties.remove("pinnedToThread"))) {
            this.pinnedToThread = true;
            var1.setProperty("maxCapacity", Integer.toString(Integer.MAX_VALUE));
         }

         if ("true".equalsIgnoreCase((String)this.driverProperties.remove("CreateConnectionInline")) || "true".equalsIgnoreCase((String)this.driverProperties.remove("createConnectionInline"))) {
            this.createConnectionInline = true;
            var1.setProperty("capacityIncrement", "1");
            var1.setProperty("initialCapacity", "0");
         }
      } else {
         this.pinnedToThread = true;
         var1.setProperty("maxCapacity", Integer.toString(Integer.MAX_VALUE));
         this.createConnectionInline = true;
         var1.setProperty("capacityIncrement", "1");
         var1.setProperty("initialCapacity", "0");
         this.nativeXA = true;
      }

      String var11 = this.driverProperties.getProperty("drivername");
      if (var11 == null || (var12 = VendorId.get(var11)) == true) {
         var12 = VendorId.get(this.driver);
      }

      if (var12 == true) {
         var15 = null;
         if (this.cpBean != null) {
            var15 = this.cpBean.getDataSourceJNDIName();
         } else if (this.factoryMBean != null) {
            var15 = this.factoryMBean.getName();
         }

         String var17 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomainName() + ":" + ManagementService.getRuntimeAccess(KERNEL_ID).getServerName() + ":" + var15;
         this.driverProperties.put(this.DD_XA_TX_GROUP_NAME, var17);
      }

      if (JDBCUtil.JDBCInternal.isDebugEnabled()) {
         JDBCUtil.JDBCInternal.debug(" < CP(" + this.name + "):initJDBCAndGetPoolParams (100)");
      }

      return var1;
   }

   public Properties getDriverProperties() {
      return this.driverProperties;
   }

   private void mergeDefaultProps(Map var1) {
      if (this.driverProperties == null) {
         this.driverProperties = new Properties();
      }

      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (!this.driverProperties.containsKey(var3)) {
            this.driverProperties.put(var3, var1.get(var3));
         }
      }

   }

   public void setCredentialMappingEnabled(boolean var1) {
      this.credentialMappingEnabled = var1;
   }

   public boolean isCredentialMappingEnabled() {
      return this.credentialMappingEnabled;
   }

   public boolean isPinnedToThread() {
      return this.pinnedToThread;
   }

   public boolean isCreateConnectionInline() {
      return this.createConnectionInline;
   }

   public boolean isRemoveInfectedConnectionEnabled() {
      return this.removeInfectedConn;
   }

   public ConnectionInfo getDefaultConnectionInfo() {
      return this.defaultConnectionInfo;
   }

   public String getDriver() {
      return this.driver;
   }

   public boolean isNativeXA() {
      return this.nativeXA;
   }

   public String getURL() {
      return this.url;
   }

   public boolean isOnePinnedConnectionOnly() {
      return this.onePinnedConnectionOnly;
   }

   public int getProfileType() {
      return this.profileType;
   }

   public boolean isIdentityBasedConnectionPoolingEnabled() {
      return this.identityBasedConnectionPoolingEnabled;
   }

   public int getXaRetryDurationSeconds() {
      return this.cpBean.getPoolParams().getXAParams().getXaRetryDurationSeconds();
   }

   public int getStatementTimeout() {
      return 0;
   }

   public boolean isOracleEnableJavaNetFastPath() {
      return false;
   }

   public boolean isOracleOptimizeUtf8Conversion() {
      return false;
   }

   public boolean isWrapTypes() {
      return false;
   }

   public void setJDBCDataSourceBean(JDBCDataSourceBean var1) {
      this.dsBean = var1;
   }
}
