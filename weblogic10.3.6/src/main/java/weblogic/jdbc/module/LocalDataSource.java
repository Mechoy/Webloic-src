package weblogic.jdbc.module;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import weblogic.j2ee.descriptor.wl.ApplicationPoolParamsBean;
import weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBean;
import weblogic.j2ee.descriptor.wl.ConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ConnectionPropertiesBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.XAParamsBean;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.ConnectionPool;
import weblogic.jdbc.common.internal.DataSourceMetaData;
import weblogic.jdbc.common.internal.DataSourceUtil;
import weblogic.jdbc.common.internal.GenericConnectionPool;
import weblogic.jdbc.common.internal.ParentLogger;
import weblogic.jdbc.common.internal.WLSMBeanConnectionPoolConfig;
import weblogic.management.configuration.JDBCDataSourceFactoryMBean;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.XAResource;

/** @deprecated */
public final class LocalDataSource extends ParentLogger implements DataSource, DataSourceMetaData {
   private final Driver driverInstance;
   private Properties driverProps = null;
   private final String driverURL;
   private final String driverClassName;
   private static final String JTSURL = "jdbc:weblogic:jts";
   private final DataSource dsInstance;
   private final ConnectionPool poolRef;
   private final String poolName;
   private final String appName;
   private final String moduleName;
   private final String jtaRegistrationName;

   public LocalDataSource(JDBCConnectionPoolBean var1, String var2, String var3, JDBCDataSourceFactoryMBean var4) throws SQLException {
      if (var1 == null && var4 == null) {
         throw new SQLException("Both application descriptor and config mbean  are null");
      } else {
         this.appName = var2;
         this.moduleName = var3;
         this.poolName = var1.getDataSourceJNDIName();
         ConnectionFactoryBean var5 = var1.getConnectionFactory();
         ConnectionPropertiesBean var6 = var5.getConnectionProperties();
         String var7 = null;
         String var8 = null;
         if (var6 != null) {
            var7 = var6.getDriverClassName();
            var8 = var6.getUrl();
         }

         if (var4 != null) {
            if (var7 == null) {
               var7 = var4.getDriverClassName();
            }

            if (var8 == null) {
               var8 = var4.getURL();
            }
         }

         if (var7 == null) {
            throw new SQLException("Driver class must be included in your pool or DataSource definition");
         } else {
            this.driverClassName = var7;
            if (var8 == null) {
               throw new SQLException("URL must be included in your pool or DataSource definition");
            } else {
               this.driverURL = var8;
               this.driverProps = this.defineDriverProps(var7, var1);
               if (var2 != null) {
                  this.jtaRegistrationName = var2 + "@" + var3 + "@" + this.poolName;
               } else {
                  this.jtaRegistrationName = this.poolName;
               }

               if (DataSourceUtil.isXADataSource(var7)) {
                  try {
                     this.driverInstance = null;
                     this.dsInstance = new weblogic.jdbc.jta.DataSource();
                     ((weblogic.jdbc.jta.DataSource)this.dsInstance).setProperties(this.driverProps);

                     try {
                        Hashtable var9 = new Hashtable();
                        var9.put("weblogic.transaction.registration.type", "dynamic");
                        String var10 = (String)this.driverProps.get("callXASetTransactionTimeout");
                        if (var10 != null && "true".equals(var10)) {
                           var9.put("weblogic.transaction.registration.settransactiontimeout", "true");
                        }

                        try {
                           if (((String)this.driverProps.get("callXAEndAtTxTimout")).equals("true")) {
                              var9.put("weblogic.transaction.registration.asynctimeoutdelist", "true");
                           }
                        } catch (Throwable var16) {
                        }

                        ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).registerResource(this.jtaRegistrationName, (XAResource)this.dsInstance, var9);
                     } catch (Exception var17) {
                        throw new SQLException("Cannot register XAResource '" + this.poolName + "': " + var17.getMessage());
                     }
                  } catch (Exception var18) {
                     throw new SQLException(var18.getMessage());
                  }
               } else {
                  try {
                     this.dsInstance = null;
                     this.driverInstance = (Driver)Class.forName("weblogic.jdbc.jts.Driver").newInstance();
                  } catch (ClassNotFoundException var13) {
                     throw new SQLException(var13.getMessage());
                  } catch (InstantiationException var14) {
                     throw new SQLException(var14.getMessage());
                  } catch (IllegalAccessException var15) {
                     throw new SQLException(var15.getMessage());
                  }
               }

               try {
                  WLSMBeanConnectionPoolConfig var19 = new WLSMBeanConnectionPoolConfig(var1, var4);
                  this.poolRef = new GenericConnectionPool(var2, var3, var19);
                  JDBCLogger.logStart(this.poolName, var2, var3);
               } catch (Exception var12) {
                  throw new SQLException(var12.getMessage());
               }
            }
         }
      }
   }

   public ConnectionPool getPoolRef() {
      return this.poolRef;
   }

   private Properties defineDriverProps(String var1, JDBCConnectionPoolBean var2) {
      Properties var3 = new Properties();
      ConnectionFactoryBean var4 = var2.getConnectionFactory();
      ConnectionPropertiesBean var5 = var4.getConnectionProperties();
      Object var6 = null;
      var3.put("connectionPoolScope", "application");
      var3.put("applicationName", this.appName);
      if (this.moduleName != null) {
         var3.put("moduleName", this.moduleName);
      }

      if (DataSourceUtil.isXADataSource(var1)) {
         var3.put("connectionPoolID", this.poolName);
         ApplicationPoolParamsBean var7 = var2.getPoolParams();
         if (var7 != null) {
            var3.put("weblogic.t3.waitSecondsForConnectionSecs", String.valueOf(var7.getLoginDelaySeconds()));
            ConnectionCheckParamsBean var8 = null;
            XAParamsBean var9 = null;
            var8 = var7.getConnectionCheckParams();
            if (var8 != null) {
               var3.put("testTableName", var8.getTableName());
            }

            var9 = var7.getXAParams();
            if (var9 != null) {
               var3.put("jdbcxaDebugLevel", String.valueOf(var9.getDebugLevel()));
               var3.put("preparedStatementCacheSize", String.valueOf(var9.getPreparedStatementCacheSize()));
               var3.put("enableResourceHealthMonitoring", String.valueOf(var9.isResourceHealthMonitoringEnabled()));
               var3.put("keepLogicalConnOpenOnRelease", String.valueOf(var9.isKeepLogicalConnOpenOnRelease()));
               var3.put("callRecoverOnlyOnce", String.valueOf(var9.isRecoverOnlyOnceEnabled()));
               var3.put("keepXAConnTillTxComplete", String.valueOf(var9.isKeepConnUntilTxCompleteEnabled()));
               var3.put("xaEndOnlyOnce", String.valueOf(var9.isEndOnlyOnceEnabled()));
               var3.put("needTxCtxOnClose", String.valueOf(var9.isTxContextOnCloseNeeded()));
               var3.put("newXAConnForCommit", String.valueOf(var9.isNewConnForCommitEnabled()));
            }
         }
      } else {
         var3.put("weblogic.jts.connectionPoolId", this.poolName);
         var3.put("weblogic.jts.driverURL", this.driverURL);
         var3.put("weblogic.jts.driverClassName", var1);
      }

      return var3;
   }

   public void setLogWriter(PrintWriter var1) throws SQLException {
   }

   public PrintWriter getLogWriter() throws SQLException {
      return null;
   }

   public void setLoginTimeout(int var1) throws SQLException {
   }

   public int getLoginTimeout() throws SQLException {
      return 0;
   }

   public Connection getConnection(String var1, String var2) throws SQLException {
      Connection var3 = null;
      if (this.dsInstance != null) {
         var3 = this.dsInstance.getConnection(var1, var2);
      } else {
         var3 = this.localConnect();
      }

      return var3;
   }

   public Connection getConnection() throws SQLException {
      return this.localConnect();
   }

   private Connection localConnect() throws SQLException {
      Connection var1 = null;
      if (this.driverInstance != null) {
         var1 = this.driverInstance.connect("jdbc:weblogic:jts", this.driverProps);
      } else {
         var1 = ((weblogic.jdbc.jta.DataSource)this.dsInstance).getConnection();
      }

      return var1;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getPoolName() {
      return this.poolName;
   }

   public boolean isTxDataSource() {
      return true;
   }

   void unregister() {
      if (DataSourceUtil.isXADataSource(this.driverClassName)) {
         try {
            ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).unregisterResource(this.jtaRegistrationName, true);
         } catch (SystemException var2) {
         }
      }

   }

   public <T> T unwrap(Class<T> var1) throws SQLException {
      if (var1.isInstance(this)) {
         return var1.cast(this);
      } else {
         throw new SQLException(this + " is not an instance of " + var1);
      }
   }

   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return var1.isInstance(this);
   }
}
