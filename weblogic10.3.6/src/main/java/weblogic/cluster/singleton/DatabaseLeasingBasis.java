package weblogic.cluster.singleton;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import weblogic.cluster.ClusterLogger;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean;
import weblogic.jdbc.common.internal.JDBCUtil;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;

public class DatabaseLeasingBasis implements LeasingBasis {
   private static final boolean DEBUG = MigrationDebugLogger.isDebugEnabled();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected final JDBCSystemResourceMBean jdbcResource;
   protected final String TABLE_NAME;
   protected final int queryTimeoutSeconds;
   private boolean supportsTimeouts = false;
   private DataSource ds;
   private QueryHelper queryHelper;

   public DatabaseLeasingBasis(JDBCSystemResourceMBean var1, int var2, String var3) {
      this.TABLE_NAME = var3;
      this.jdbcResource = var1;
      this.queryTimeoutSeconds = var2;
   }

   private QueryHelper getQueryHelper(Connection var1) {
      if (this.queryHelper == null) {
         this.queryHelper = MigratableServerService.theOne().identifyVendorSpecificQuery(this.TABLE_NAME, var1);
         if (this.queryHelper.getDBType() != 4 && this.queryHelper.getDBType() != 5) {
            this.supportsTimeouts = true;
         } else {
            this.supportsTimeouts = false;
         }
      }

      return this.queryHelper;
   }

   public boolean acquire(String var1, String var2, int var3) throws LeasingException {
      if (DEBUG) {
         p("tryAcquire(" + var1 + ", " + var3 + ")");
      }

      this.acquireLease(var1, var2);
      return this.assumeLease(var1, var2, var3) == 1;
   }

   public void release(String var1, String var2) throws IOException {
      if (DEBUG) {
         p("release(" + var1 + ")");
      }

      int var3 = this.abdicateLease(var1, var2);
      if (var3 < 0 || var3 > 1) {
         throw new IOException("Could not release: \"" + var1 + "\"");
      }
   }

   public int renewAllLeases(int var1, String var2) throws MissedHeartbeatException {
      if (DEBUG) {
         p("renewAllLeases(" + var1 + ")");
      }

      int var3 = this.renewAllLeases(var2, var1);
      if (var3 < 0) {
         throw new MissedHeartbeatException("Could not heartbeat");
      } else {
         return var3;
      }
   }

   public int renewLeases(String var1, Set var2, int var3) throws IOException {
      Connection var4 = null;

      try {
         var4 = this.getJDBCConnection();
      } catch (SQLException var14) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Unexpected exception", var14);
         }

         return -1;
      }

      PreparedStatement var5 = null;
      String var6 = null;

      byte var8;
      try {
         try {
            var6 = this.getQueryHelper(var4).getRenewLeasesQuery(var1, var2, var3 / 1000);
            if (DEBUG) {
               MigrationDebugLogger.debug("Query: " + var6);
            }

            var5 = var4.prepareStatement(var6);
            if (this.supportsTimeouts) {
               var5.setQueryTimeout(this.queryTimeoutSeconds);
            }

            int var7 = var5.executeUpdate();
            return var7;
         } catch (SQLException var15) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Attempted Query: " + var6);
               MigrationDebugLogger.debug("Unexpected exception", var15);
            }
         }

         var8 = -1;
      } finally {
         this.closePreparedStatement(var5);
         this.closeSQLConnection(var4);
      }

      return var8;
   }

   protected int renewAllLeases(String var1, int var2) {
      Connection var3 = null;

      try {
         var3 = this.getJDBCConnection();
      } catch (SQLException var13) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Unexpected exception", var13);
         }

         return -1;
      }

      PreparedStatement var4 = null;
      String var5 = null;

      byte var7;
      try {
         try {
            var5 = this.getQueryHelper(var3).getRenewAllLeasesQuery(var1, var2 / 1000);
            if (DEBUG) {
               MigrationDebugLogger.debug("Query: " + var5);
            }

            var4 = var3.prepareStatement(var5);
            if (this.supportsTimeouts) {
               var4.setQueryTimeout(this.queryTimeoutSeconds);
            }

            int var6 = var4.executeUpdate();
            return var6;
         } catch (SQLException var14) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Attempted Query: " + var5);
               MigrationDebugLogger.debug("Unexpected exception", var14);
            }
         }

         var7 = -1;
      } finally {
         this.closePreparedStatement(var4);
         this.closeSQLConnection(var3);
      }

      return var7;
   }

   protected int abdicateLease(String var1, String var2) {
      Connection var3 = null;

      try {
         var3 = this.getJDBCConnection();
      } catch (SQLException var13) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Unexpected exception", var13);
         }

         return -1;
      }

      PreparedStatement var4 = null;
      String var5 = null;

      byte var7;
      try {
         try {
            var5 = this.getQueryHelper(var3).getAbdicateLeaseQuery(var1, var2);
            if (DEBUG) {
               MigrationDebugLogger.debug("Query: " + var5);
            }

            var4 = var3.prepareStatement(var5);
            if (this.supportsTimeouts) {
               var4.setQueryTimeout(this.queryTimeoutSeconds);
            }

            int var6 = var4.executeUpdate();
            return var6;
         } catch (SQLException var14) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Attempted Query: " + var5);
               MigrationDebugLogger.debug("Unexpected exception", var14);
            }
         }

         var7 = -1;
      } finally {
         this.closePreparedStatement(var4);
         this.closeSQLConnection(var3);
      }

      return var7;
   }

   protected int acquireLease(String var1, String var2) {
      Connection var3 = null;

      try {
         var3 = this.getJDBCConnection();
      } catch (SQLException var13) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Unexpected exception", var13);
         }

         return -1;
      }

      PreparedStatement var4 = null;
      String var5 = null;

      byte var7;
      try {
         try {
            var5 = this.getQueryHelper(var3).getAcquireLeaseQuery(var1, var2);
            if (DEBUG) {
               MigrationDebugLogger.debug("Query: " + var5);
            }

            var4 = var3.prepareStatement(var5);
            if (this.supportsTimeouts) {
               var4.setQueryTimeout(this.queryTimeoutSeconds);
            }

            int var6 = var4.executeUpdate();
            return var6;
         } catch (SQLException var14) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Attempted Query: " + var5);
               MigrationDebugLogger.debug("Unexpected exception", var14);
            }
         }

         var7 = -1;
      } finally {
         this.closePreparedStatement(var4);
         this.closeSQLConnection(var3);
      }

      return var7;
   }

   protected int assumeLease(String var1, String var2, int var3) {
      Connection var4 = null;

      try {
         var4 = this.getJDBCConnection();
      } catch (SQLException var14) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Unexpected exception", var14);
         }

         return -1;
      }

      PreparedStatement var5 = null;
      String var6 = null;

      byte var8;
      try {
         try {
            var6 = this.getQueryHelper(var4).getAssumeLeaseQuery(var1, var2, var3 / 1000);
            if (DEBUG) {
               MigrationDebugLogger.debug("Query: " + var6);
            }

            var5 = var4.prepareStatement(var6);
            if (this.supportsTimeouts) {
               var5.setQueryTimeout(this.queryTimeoutSeconds);
            }

            int var7 = var5.executeUpdate();
            return var7;
         } catch (SQLException var15) {
            if (MigrationDebugLogger.isDebugEnabled()) {
               MigrationDebugLogger.debug("Attempted Query: " + var6);
               MigrationDebugLogger.debug("Unexpected exception", var15);
            }
         }

         var8 = -1;
      } finally {
         this.closePreparedStatement(var5);
         this.closeSQLConnection(var4);
      }

      return var8;
   }

   public String findPreviousOwner(String var1) throws IOException {
      Connection var2 = null;

      try {
         var2 = this.getJDBCConnection();
      } catch (SQLException var12) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Failed to get sql connection", var12);
         }

         return null;
      }

      PreparedStatement var3 = null;
      ResultSet var4 = null;
      String var5 = null;

      try {
         var5 = this.getQueryHelper(var2).getPreviousLeaseOwnerQuery(var1);
         if (DEBUG) {
            MigrationDebugLogger.debug("Query: " + var5);
         }

         var3 = var2.prepareStatement(var5);
         if (this.supportsTimeouts) {
            var3.setQueryTimeout(this.queryTimeoutSeconds);
         }

         var4 = var3.executeQuery();
         if (var4.next()) {
            String var6 = var4.getString(1);
            return var6;
         }
      } catch (SQLException var13) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Attempted Query: " + var5);
            MigrationDebugLogger.debug("Unexpected sql exception", var13);
         }
      } finally {
         this.closeResultSet(var4);
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }

      return null;
   }

   public String findOwner(String var1) throws IOException {
      Connection var2 = null;

      try {
         var2 = this.getJDBCConnection();
      } catch (SQLException var12) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Failed to get sql connection", var12);
         }

         throw new IOException(var12.getMessage());
      }

      PreparedStatement var3 = null;
      ResultSet var4 = null;
      String var5 = null;

      try {
         var5 = this.getQueryHelper(var2).getLeaseOwnerQuery(var1);
         if (DEBUG) {
            MigrationDebugLogger.debug("Query: " + var5);
         }

         var3 = var2.prepareStatement(var5);
         if (this.supportsTimeouts) {
            var3.setQueryTimeout(this.queryTimeoutSeconds);
         }

         var4 = var3.executeQuery();
         if (var4.next()) {
            String var6 = var4.getString(1);
            return var6;
         }
      } catch (SQLException var13) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Attempted Query: " + var5);
            MigrationDebugLogger.debug("Unexpected sql exception", var13);
         }

         throw new IOException(var13.getMessage());
      } finally {
         this.closeResultSet(var4);
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }

      return null;
   }

   public String[] findExpiredLeases(int var1) {
      Connection var2 = null;

      try {
         var2 = this.getJDBCConnection();
      } catch (SQLException var13) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Failed to get sql connection", var13);
         }

         return new String[0];
      }

      PreparedStatement var3 = null;
      ArrayList var4 = new ArrayList();
      ResultSet var5 = null;
      String var6 = null;

      try {
         var6 = this.getQueryHelper(var2).getUnresponsiveMigratableServersQuery(var1 / 1000);
         if (DEBUG) {
            MigrationDebugLogger.debug("Query: " + var6);
         }

         var3 = var2.prepareStatement(var6);
         if (this.supportsTimeouts) {
            var3.setQueryTimeout(this.queryTimeoutSeconds);
         }

         var5 = var3.executeQuery();

         while(var5.next()) {
            var4.add(var5.getString(1));
         }
      } catch (SQLException var14) {
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug("Attempted Query: " + var6);
            MigrationDebugLogger.debug("Unexpected sql exception", var14);
         }
      } finally {
         this.closeResultSet(var5);
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }

      String[] var7 = new String[var4.size()];
      var4.toArray(var7);
      return var7;
   }

   protected void closePreparedStatement(PreparedStatement var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
            var3.printStackTrace();
         }
      }

   }

   protected void closeSQLConnection(Connection var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
            var3.printStackTrace();
         }
      }

   }

   protected void closeResultSet(ResultSet var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
            var3.printStackTrace();
         }
      }

   }

   private void pingDB(Connection var1) throws SQLException {
      PreparedStatement var2 = null;
      String var3 = this.getQueryHelper(var1).getLeaseOwnerQuery("nonexistantlease");
      var2 = var1.prepareStatement(var3);
      if (this.supportsTimeouts) {
         var2.setQueryTimeout(this.queryTimeoutSeconds);
      }

      var2.executeQuery();
      this.closePreparedStatement(var2);
   }

   private Connection getJDBCConnection(JDBCSystemResourceMBean var1) throws SQLException {
      JDBCDataSourceBean var2 = var1.getJDBCResource();
      JDBCDataSourceParamsBean var3 = var1.getJDBCResource().getJDBCDataSourceParams();
      String[] var4;
      if (this.ds != null) {
         var4 = null;

         Connection var23;
         try {
            var23 = this.ds.getConnection();
            this.pingDB(var23);
            return var23;
         } catch (SQLException var20) {
            this.closeSQLConnection(var4);
            var23 = this.createDirectConnection(var2.getJDBCDriverParams());
            this.pingDB(var23);
            return var23;
         }
      } else {
         var4 = var3.getJNDINames();
         InitialContext var5 = null;
         Connection var6 = null;

         Connection var8;
         try {
            Connection var7;
            if (var4 != null && var4.length > 0) {
               var5 = new InitialContext();
               this.ds = (DataSource)var5.lookup(var4[0]);
               var6 = this.ds.getConnection();
               this.pingDB(var6);
               var7 = var6;
               return var7;
            }

            var6 = this.createDirectConnection(var2.getJDBCDriverParams());
            this.pingDB(var6);
            var7 = var6;
            return var7;
         } catch (NamingException var21) {
            this.closeSQLConnection(var6);
            var6 = this.createDirectConnection(var2.getJDBCDriverParams());
            this.pingDB(var6);
            var8 = var6;
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (NamingException var19) {
               }
            }

         }

         return var8;
      }
   }

   protected Connection getJDBCConnection() throws SQLException {
      if (!isMultipool(this.jdbcResource)) {
         return this.getJDBCConnection(this.jdbcResource);
      } else {
         SQLException var1 = null;
         String var2 = this.jdbcResource.getJDBCResource().getJDBCDataSourceParams().getDataSourceList();
         StringTokenizer var3 = new StringTokenizer(var2, ",");
         JDBCSystemResourceMBean[] var4 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJDBCSystemResources();

         while(var3.hasMoreTokens()) {
            try {
               String var5 = var3.nextToken().trim();

               for(int var6 = 0; var6 < var4.length; ++var6) {
                  JDBCDataSourceBean var7 = var4[var6].getJDBCResource();
                  if (var7 != null && var5.equals(var7.getName())) {
                     return this.getJDBCConnection(var4[var6]);
                  }
               }
            } catch (SQLException var8) {
               var1 = var8;
            }
         }

         if (var1 != null) {
            throw var1;
         } else {
            throw new SQLException("No living database found!");
         }
      }
   }

   private Connection createDirectConnection(JDBCDriverParamsBean var1) throws SQLException {
      String var2 = var1.getDriverName();
      String var3 = var1.getPassword();
      Properties var4 = JDBCUtil.getProperties(var1.getProperties().getProperties());
      if (var4 != null && var3 != null) {
         var4.setProperty("password", var3);
      }

      try {
         Class var5 = Class.forName(var2);
         Constructor var6 = var5.getConstructor((Class[])null);
         Driver var7 = (Driver)var6.newInstance((Object[])null);
         Connection var8 = var7.connect(var1.getUrl(), var4);
         if (var8 == null) {
            throw new SQLException("Failed to get connection to the database due to bad configuration.");
         } else {
            return var8;
         }
      } catch (Throwable var9) {
         if (DEBUG) {
            var9.printStackTrace();
         }

         throw new SQLException("Failed to get connection to the database " + var9.getMessage());
      }
   }

   public static LeasingBasis createBasis(ServerMBean var0, JDBCSystemResourceMBean var1, int var2, String var3) throws ServiceFailureException {
      checkSystemResource(var0, var1);
      if (isMultipool(var1)) {
         JDBCDataSourceBean var4 = var1.getJDBCResource();
         String var5 = var4.getJDBCDataSourceParams().getDataSourceList();
         String var6 = null;
         StringTokenizer var7 = new StringTokenizer(var5, ",");
         JDBCSystemResourceMBean[] var8 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJDBCSystemResources();
         JDBCSystemResourceMBean var9 = null;

         while(var7.hasMoreTokens()) {
            String var10 = var7.nextToken().trim();

            for(int var11 = 0; var11 < var8.length; ++var11) {
               JDBCDataSourceBean var12 = var8[var11].getJDBCResource();
               if (var12 != null && var10.equals(var12.getName())) {
                  var9 = var8[var11];
                  break;
               }
            }

            String var13;
            if (var9 == null) {
               ClusterLogger.logMissingJDBCConfigurationForAutoMigration(var0.getName());
               var13 = "Invalid migratable server configuration. The pool named  " + var10 + " which is supposed to be a part of the multipool was not found. ";
               throw new ServiceFailureException(var13);
            }

            checkSystemResource(var0, var9);
            if (var6 == null) {
               var6 = var4.getJDBCDriverParams().getDriverName();
            } else if (!var6.equals(var4.getJDBCDriverParams().getDriverName())) {
               ClusterLogger.logMissingJDBCConfigurationForAutoMigration(var0.getName());
               var13 = "Invalid migratable server configuration. All pools in   a multipool for Singleton Services must have the same driver.";
               throw new ServiceFailureException(var13);
            }
         }
      }

      return new DatabaseLeasingBasis(var1, var2, var3);
   }

   private static String getDriverName(JDBCSystemResourceMBean var0) {
      if (!isMultipool(var0)) {
         if (DEBUG) {
            p("Driver for " + var0 + " is" + var0.getJDBCResource().getJDBCDriverParams().getDriverName());
         }

         return var0.getJDBCResource().getJDBCDriverParams().getDriverName();
      } else {
         String var1 = var0.getJDBCResource().getJDBCDataSourceParams().getDataSourceList();
         StringTokenizer var2 = new StringTokenizer(var1, ",");
         JDBCSystemResourceMBean[] var3 = ManagementService.getRuntimeAccess(kernelId).getDomain().getJDBCSystemResources();
         JDBCSystemResourceMBean var4 = null;

         while(var2.hasMoreTokens()) {
            String var5 = var2.nextToken().trim();

            for(int var6 = 0; var6 < var3.length; ++var6) {
               JDBCDataSourceBean var7 = var3[var6].getJDBCResource();
               if (var7 != null && var5.equals(var7.getName())) {
                  var4 = var3[var6];
                  return getDriverName(var4);
               }
            }
         }

         throw new AssertionError("No driver found for jdbc resource: " + var0);
      }
   }

   private static boolean isMultipool(JDBCSystemResourceMBean var0) {
      JDBCDataSourceBean var1 = var0.getJDBCResource();
      return var1.getJDBCDriverParams().getDriverName() == null && var1.getJDBCDataSourceParams() != null && var1.getJDBCDataSourceParams().getDataSourceList() != null;
   }

   private static void checkSystemResource(ServerMBean var0, JDBCSystemResourceMBean var1) throws ServiceFailureException {
      if (var1 == null) {
         ClusterLogger.logMissingJDBCConfigurationForAutoMigration(var0.getName());
         String var4 = "Invalid migratable server configuration. The  DataSourceForAutomaticMigration was not set. Please refer to cluster documents for more information";
         throw new ServiceFailureException(var4);
      } else {
         JDBCDataSourceBean var2 = var1.getJDBCResource();
         String[] var3 = var2.getJDBCDataSourceParams().getJNDINames();
         if (var3 != null && var3.length != 0) {
            if (var2.getJDBCDriverParams().getDriverName() == null && !isMultipool(var1)) {
               ClusterLogger.logMissingJDBCConfigurationForAutoMigration(var0.getName());
               throw new ServiceFailureException("Invalid migratable server configuration, please use a fully-populated JDBC resource");
            }
         } else {
            ClusterLogger.logMissingJDBCConfigurationForAutoMigration(var0.getName());
            throw new ServiceFailureException("Invalid migratable server configuration");
         }
      }
   }

   private static final void p(String var0) {
      MigrationDebugLogger.debug("<DatabaseLeasingBasis>: " + var0);
   }
}
