package weblogic.jdbc.utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCPropertiesBean;
import weblogic.j2ee.descriptor.wl.JDBCPropertyBean;
import weblogic.jdbc.common.internal.ParentLogger;
import weblogic.management.configuration.JDBCConnectionPoolMBean;

/** @deprecated */
public class BasicDataSource extends ParentLogger implements DataSource {
   private static final String USER_PROPERTY_NAME = "user";
   private static final String PASSWORD_PROPERTY_NAME = "password";
   private Driver dbDriver;
   private PrintWriter debugWriter;
   private JDBCConnectionPoolMBean poolBean;
   private JDBCDriverParamsBean driverParamsBean;
   private String url;
   private Properties properties;
   private String password;
   static final long serialVersionUID = 7022603236273910391L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.jdbc.utils.BasicDataSource");
   public static final DelegatingMonitor _WLDF$INST_FLD_JDBC_After_Connection_Internal;
   public static final DelegatingMonitor _WLDF$INST_FLD_JDBC_Before_Connection_Internal;
   public static final DelegatingMonitor _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public BasicDataSource(JDBCDataSourceBean var1) throws SQLException {
      this((JDBCDataSourceBean)var1, (String)null);
   }

   public BasicDataSource(JDBCDataSourceBean var1, String var2) throws SQLException {
      if (var1 == null) {
         throw new SQLException("No JDBCDataSourceBean was specified");
      } else {
         this.password = var2;
         this.driverParamsBean = var1.getJDBCDriverParams();
         this.loadDriver(this.driverParamsBean.getDriverName());
      }
   }

   public BasicDataSource(JDBCConnectionPoolMBean var1) throws SQLException {
      this((JDBCConnectionPoolMBean)var1, (String)null);
   }

   public BasicDataSource(JDBCConnectionPoolMBean var1, String var2) throws SQLException {
      if (var1 == null) {
         throw new SQLException("No JDBCConnectionPoolMBean was specified");
      } else {
         this.poolBean = var1;
         this.password = var2;
         this.loadDriver(var1.getDriverName());
      }
   }

   public BasicDataSource(String var1, String var2, Properties var3, String var4) throws SQLException {
      this.url = var1;
      this.properties = var3;
      this.password = var4;
      this.loadDriver(var2);
   }

   private void loadDriver(String var1) throws SQLException {
      if (var1 != null && var1.length() != 0) {
         Class var2;
         try {
            var2 = Class.forName(var1);
         } catch (ClassNotFoundException var7) {
            throw makeSQLException("JDBC driver class \"" + var1 + "\" not found", var7);
         }

         try {
            this.dbDriver = (Driver)var2.newInstance();
         } catch (InstantiationException var4) {
            throw makeSQLException("JDBC driver instance of class \"" + var1 + "\" cannot be created: " + var4, var4);
         } catch (IllegalAccessException var5) {
            throw makeSQLException("JDBC driver instance of class \"" + var1 + "\" cannot be created: " + var5, var5);
         } catch (ClassCastException var6) {
            throw makeSQLException("JDBC driver class \"" + var1 + "\" does not implement the java.sql.Driver interface", var6);
         }
      } else {
         throw new SQLException("No JDBC driver class was specified");
      }
   }

   public static Properties parsePropertiesString(String var0) {
      Properties var1 = new Properties();
      if (var0 == null) {
         return var1;
      } else {
         StringTokenizer var2 = new StringTokenizer(var0, ";");

         while(var2.hasMoreElements()) {
            String var3 = var2.nextToken();
            StringTokenizer var4 = new StringTokenizer(var3, "=");
            String var5 = null;
            String var6 = null;
            if (var4.hasMoreElements()) {
               var5 = var4.nextToken();
            }

            if (var4.hasMoreElements()) {
               var6 = var4.nextToken();
            }

            if (var5 != null) {
               var1.put(var5, var6);
            }
         }

         return var1;
      }
   }

   private Properties getPropsFromBean(JDBCDriverParamsBean var1) {
      Properties var2 = new Properties();
      JDBCPropertiesBean var3 = var1.getProperties();
      if (var3 != null) {
         JDBCPropertyBean[] var4 = var3.getProperties();

         for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
            var2.put(var4[var5].getName(), var4[var5].getValue());
         }
      }

      return var2;
   }

   public boolean isDataSource() {
      return this.dbDriver instanceof DataSource;
   }

   public boolean isXADataSource() {
      return this.dbDriver instanceof XADataSource;
   }

   public Connection getConnection() throws SQLException {
      Object[] var3;
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      if (_WLDF$INST_FLD_JDBC_Before_Connection_Internal.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_JDBC_Before_Connection_Internal.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(1);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_JDBC_Before_Connection_Internal;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var8;
      boolean var14 = var8 = _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var14) {
         var3 = null;
         if (_WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(1);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var12 = false;

      Connection var4;
      Connection var15;
      DynamicJoinPoint var16;
      DelegatingMonitor var18;
      label203: {
         label204: {
            try {
               var12 = true;
               String var1;
               if (this.password != null && this.password.length() > 0) {
                  var1 = this.password;
               } else {
                  var1 = null;
               }

               if (this.driverParamsBean == null) {
                  if (this.poolBean != null) {
                     var15 = this.connectInternal(this.poolBean.getURL(), this.poolBean.getProperties(), (String)null, var1 == null ? this.poolBean.getPassword() : var1);
                     var12 = false;
                     break label203;
                  }

                  var15 = this.connectInternal(this.url, this.properties, (String)null, this.password);
                  var12 = false;
                  break label204;
               }

               var15 = this.connectInternal(this.driverParamsBean.getUrl(), this.getPropsFromBean(this.driverParamsBean), (String)null, var1 == null ? this.driverParamsBean.getPassword() : var1);
               var12 = false;
            } finally {
               if (var12) {
                  var4 = null;
                  if (var8) {
                     InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var9, var10);
                  }

                  if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
                     DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
                     DelegatingMonitor var10003 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
                     InstrumentationSupport.process(var17, var10003, var10003.getActions());
                  }

               }
            }

            var4 = var15;
            if (var8) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var9, var10);
            }

            if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
               var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
               var18 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
               InstrumentationSupport.process(var16, var18, var18.getActions());
            }

            return var15;
         }

         var4 = var15;
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var9, var10);
         }

         if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
            var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
            var18 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
            InstrumentationSupport.process(var16, var18, var18.getActions());
         }

         return var15;
      }

      var4 = var15;
      if (var8) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var9, var10);
      }

      if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
         var18 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
         InstrumentationSupport.process(var16, var18, var18.getActions());
      }

      return var15;
   }

   public Connection getConnection(String var1, String var2) throws SQLException {
      Object[] var4;
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      if (_WLDF$INST_FLD_JDBC_Before_Connection_Internal.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_JDBC_Before_Connection_Internal.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(3);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_JDBC_Before_Connection_Internal;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var9;
      boolean var15 = var9 = _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      if (var15) {
         var4 = null;
         if (_WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(3);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var13 = false;

      Connection var5;
      Connection var16;
      DynamicJoinPoint var17;
      DelegatingMonitor var19;
      label164: {
         label165: {
            try {
               var13 = true;
               if (this.driverParamsBean != null) {
                  var16 = this.connectInternal(this.driverParamsBean.getUrl(), this.getPropsFromBean(this.driverParamsBean), var1, var2);
                  var13 = false;
                  break label164;
               }

               if (this.poolBean != null) {
                  var16 = this.connectInternal(this.poolBean.getURL(), this.poolBean.getProperties(), var1, var2);
                  var13 = false;
                  break label165;
               }

               var16 = this.connectInternal(this.url, this.properties, var1, var2);
               var13 = false;
            } finally {
               if (var13) {
                  var5 = null;
                  if (var9) {
                     InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var10, var11);
                  }

                  if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
                     DynamicJoinPoint var18 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, (Object[])null, var5);
                     DelegatingMonitor var10003 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
                     InstrumentationSupport.process(var18, var10003, var10003.getActions());
                  }

               }
            }

            var5 = var16;
            if (var9) {
               InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var10, var11);
            }

            if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
               var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, (Object[])null, var5);
               var19 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
               InstrumentationSupport.process(var17, var19, var19.getActions());
            }

            return var16;
         }

         var5 = var16;
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var10, var11);
         }

         if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
            var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, (Object[])null, var5);
            var19 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
            InstrumentationSupport.process(var17, var19, var19.getActions());
         }

         return var16;
      }

      var5 = var16;
      if (var9) {
         InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium, var10, var11);
      }

      if (_WLDF$INST_FLD_JDBC_After_Connection_Internal.isEnabledAndNotDyeFiltered()) {
         var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, (Object[])null, var5);
         var19 = _WLDF$INST_FLD_JDBC_After_Connection_Internal;
         InstrumentationSupport.process(var17, var19, var19.getActions());
      }

      return var16;
   }

   private Connection connectInternal(String var1, Properties var2, String var3, String var4) throws SQLException {
      if (var2 == null) {
         var2 = new Properties();
      }

      if (var3 != null && var3.length() > 0) {
         var2.put("user", var3);
      }

      if (var4 != null && var4.length() > 0) {
         var2.put("password", var4);
      }

      return this.dbDriver.connect(var1, var2);
   }

   public int getLoginTimeout() throws SQLException {
      return DriverManager.getLoginTimeout();
   }

   public void setLoginTimeout(int var1) throws SQLException {
      DriverManager.setLoginTimeout(var1);
   }

   public PrintWriter getLogWriter() throws SQLException {
      return this.debugWriter;
   }

   public void setLogWriter(PrintWriter var1) throws SQLException {
      this.debugWriter = var1;
   }

   private static SQLException makeSQLException(String var0, Throwable var1) {
      SQLException var2 = new SQLException(var0);
      var2.initCause(var1);
      return var2;
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

   static {
      _WLDF$INST_FLD_JDBC_After_Connection_Internal = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "JDBC_After_Connection_Internal");
      _WLDF$INST_FLD_JDBC_Before_Connection_Internal = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "JDBC_Before_Connection_Internal");
      _WLDF$INST_FLD_JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "JDBC_Diagnostic_Datasource_Get_Connection_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BasicDataSource.java", "weblogic.jdbc.utils.BasicDataSource", "getConnection", "()Ljava/sql/Connection;", 254, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "BasicDataSource.java", "weblogic.jdbc.utils.BasicDataSource", "getConnection", "(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/Connection;", 290, (Map)null, (boolean)0);
   }
}
