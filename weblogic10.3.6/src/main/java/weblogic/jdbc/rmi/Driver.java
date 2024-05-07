package weblogic.jdbc.rmi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jdbc.JDBCTextTextFormatter;
import weblogic.jdbc.common.internal.RmiDataSource;
import weblogic.jndi.Environment;
import weblogic.jndi.WLInitialContextFactory;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.http.HttpParsing;

/** @deprecated */
public final class Driver extends RMIWrapperImpl implements java.sql.Driver {
   private static final String URL_BASE = "jdbc:weblogic:rmi";
   private static final String DATA_SOURCE_NAME_PROP = "weblogic.jdbc.datasource";
   private static final String SERVER_URL_PROP = "weblogic.server.url";
   private static final String USER = "weblogic.user";
   private static final String PASSWORD = "weblogic.credential";
   private static final String REMOTEDATASOURCE = "weblogic.jts.remotedatasource";
   private boolean debug = false;
   private DebugLogger JDBCRMIDriver = null;

   public Connection connect(String var1, Properties var2) throws SQLException {
      Connection var3 = null;
      String var4 = "getConnection";
      Object[] var5 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var4, var5);
         String var6;
         String var7;
         String var9;
         if (var2 != null) {
            var6 = "weblogic.jdbc.verbose";
            var7 = var2.getProperty(var6);
            if (var7 != null) {
               try {
                  this.debug = Boolean.valueOf(var7);
               } catch (Exception var15) {
                  var9 = "The Property " + var6 + " must be a true or false.";
                  throw new SQLException(var9);
               }
            }

            if (this.debug) {
               this.JDBCRMIDriver = DebugLogger.createUnregisteredDebugLogger("JDBCRMIDriver", true);
            }
         }

         if (this.debug) {
            var6 = "time=" + System.currentTimeMillis() + " : connect" + "\n\turl=" + var1 + "\n\tinfo=" + var2;
            this.JDBCRMIDriver.debug(var6);
         }

         if (!this.acceptsURL(var1)) {
            this.postInvocationHandler(var4, var5, (Object)null);
            return null;
         }

         int var19 = var1.indexOf(63);
         if (var19 != -1) {
            HttpParsing.parseQueryString(var1.substring(var19 + 1), var2);
            var1.substring(0, var19);
         }

         if (this.debug) {
            var7 = "time=" + System.currentTimeMillis() + " : getting context";
            this.JDBCRMIDriver.debug(var7);
         }

         var7 = var2.getProperty("weblogic.jdbc.datasource");
         String var8 = var2.getProperty("weblogic.server.url");
         var9 = var2.getProperty("weblogic.user");
         String var10 = var2.getProperty("weblogic.credential");
         if (var7 == null) {
            throw new SQLException("You must define weblogic.jdbc.datasource");
         }

         if (var8 == null) {
            throw new SQLException("You must define weblogic.server.url");
         }

         if (this.debug) {
            this.JDBCRMIDriver.debug("weblogic.jdbc.datasource=" + var7);
            this.JDBCRMIDriver.debug("weblogic.server.url=" + var8);
         }

         InitialContext var11;
         Environment var12;
         String var13;
         try {
            var12 = new Environment();
            var13 = WLInitialContextFactory.class.getName();
            var12.setInitialContextFactory(var13);
            var12.setProviderUrl(var8);
            if (var9 != null) {
               var12.setSecurityPrincipal(var9);
            }

            if (var10 != null) {
               var12.setSecurityCredentials(var10);
            }

            var11 = new InitialContext(var12.getProperties());
         } catch (NamingException var16) {
            throw new SQLException(var16.toString());
         }

         if (this.debug) {
            String var20 = "time=" + System.currentTimeMillis() + " : got context,";
            this.JDBCRMIDriver.debug(var20);
         }

         var12 = null;

         try {
            if (this.debug) {
               var13 = "time=" + System.currentTimeMillis() + " : lookup " + var7;
               this.JDBCRMIDriver.debug(var13);
            }

            DataSource var21 = (DataSource)var11.lookup(var7);
            if (var21 != null && var2.getProperty("weblogic.jts.remotedatasource") != null && var21 instanceof RmiDataSource) {
               try {
                  var12 = null;
                  var21 = (DataSource)ServerHelper.getStubWithPinnedRef((RmiDataSource)var12, var8);
               } catch (Throwable var14) {
               }

               if (var21 == null || var21 instanceof RmiDataSource) {
                  throw new RuntimeException("CR106162: WLS RMI runtime failed to give us a remote DataSource to " + var8);
               }
            }

            if (var21 != null) {
               if (this.debug) {
                  var13 = "time=" + System.currentTimeMillis() + " : got " + var7;
                  this.JDBCRMIDriver.debug(var13);
                  var13 = "time=" + System.currentTimeMillis() + " : getting connection";
                  this.JDBCRMIDriver.debug(var13);
               }

               var3 = var21.getConnection();
               if (this.debug) {
                  var13 = "time=" + System.currentTimeMillis() + " : got connection";
                  this.JDBCRMIDriver.debug(var13);
               }
            }
         } catch (NamingException var17) {
            throw new SQLException(var17.toString());
         }

         this.postInvocationHandler(var4, var5, var3);
      } catch (Exception var18) {
         this.invocationExceptionHandler(var4, var5, var18);
      }

      return var3;
   }

   public boolean acceptsURL(String var1) throws SQLException {
      boolean var2 = true;
      String var3 = "acceptsURL";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         if (var1.startsWith("jdbc:weblogic:rmi")) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.postInvocationHandler(var3, var4, new Boolean(var2));
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public DriverPropertyInfo[] getPropertyInfo(String var1, Properties var2) throws SQLException {
      String var3 = "DriverPropertyInfo";
      Object[] var4 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var3, var4);
         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return null;
   }

   public int getMajorVersion() {
      String var1 = "getMajorVersion";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.postInvocationHandler(var1, var2, new Integer(1));
      } catch (Exception var4) {
      }

      return 1;
   }

   public int getMinorVersion() {
      String var1 = "getMinorVersion";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.postInvocationHandler(var1, var2, new Integer(2));
      } catch (Exception var4) {
      }

      return 2;
   }

   public boolean jdbcCompliant() {
      String var1 = "jdbcCompliant";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.postInvocationHandler(var1, var2, new Boolean(true));
      } catch (Exception var4) {
      }

      return true;
   }

   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException();
   }

   static {
      try {
         DriverManager.registerDriver(new Driver());
      } catch (SQLException var1) {
         DriverManager.println((new JDBCTextTextFormatter()).driverLoadingError(var1.getClass().getName(), var1.getMessage()));
      }

   }
}
