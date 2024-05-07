package weblogic.scheduler;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Transaction;
import oracle.sql.BLOB;
import weblogic.cluster.ClusterLogger;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.store.io.jdbc.JDBCHelper;
import weblogic.timers.TimerListener;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public final class DBTimerBasisImpl implements TimerBasis {
   private static final boolean DEBUG = Debug.getCategory("weblogic.JobScheduler").isEnabled();
   private DataSource ds;
   private final JDBCSystemResourceMBean jdbcResource;
   private final SQLHelper sqlHelper;
   private final String serverName;
   long previousIdTime = -1L;
   int idSuffix = 0;
   private Connection connection;

   DBTimerBasisImpl(JDBCSystemResourceMBean var1, String var2, String var3, String var4, String var5) throws SQLException {
      this.jdbcResource = var1;
      this.serverName = var5;
      DatabaseMetaData var6 = this.getJDBCConnection().getMetaData();
      int var7 = JDBCHelper.getDBMSType(var6, (String[])null);
      if (var7 == 1) {
         this.sqlHelper = new OracleSQLHelperImpl(var2, var3, var4);
      } else if (var7 == 9) {
         this.sqlHelper = new MySQLHelperImpl(var2, var3, var4);
      } else if (var7 == 5) {
         this.sqlHelper = new InformixSQLHelperImpl(var2, var3, var4);
      } else {
         this.sqlHelper = new GenericSQLHelperImpl(var2, var3, var4);
      }

   }

   public String createTimer(String var1, TimerListener var2, long var3, long var5, AuthenticatedSubject var7) throws TimerException {
      Transaction var8 = null;
      TransactionManager var9 = null;
      if (!(var2 instanceof TimerListenerExtension)) {
         var9 = TxHelper.getTransactionManager();
         var8 = var9.forceSuspend();
      }

      String var14;
      try {
         String var10 = this.getUniqueId(var2);
         if (DEBUG) {
            debug("generated id " + var10 + " for listener " + var2);
         }

         long var11 = System.currentTimeMillis() + var3;
         String var13 = this.sqlHelper.getCreateTimerSQL(var10, var1, var11, var5);
         if (DEBUG) {
            debug("executing sql: " + var13);
         }

         this.runCreate(var10, (Serializable)var2, var13);
         ClusterLogger.logCreatedJob(var10, var2.toString());
         var14 = var10;
      } catch (SQLException var19) {
         if (DEBUG) {
            var19.printStackTrace();
         }

         throw new TimerException("Unable to create timer", var19);
      } finally {
         if (var8 != null) {
            var9.forceResume(var8);
         }

      }

      return var14;
   }

   public boolean cancelTimer(String var1) throws TimerException {
      try {
         int var2 = this.runUpdate(this.sqlHelper.getCancelTimerSQL(var1));
         if (var2 > 0) {
            ClusterLogger.logCancelledJob(var1);
            return true;
         } else {
            return false;
         }
      } catch (SQLException var3) {
         if (DEBUG) {
            var3.printStackTrace();
         }

         throw new TimerException("unable to cancel timer", var3);
      }
   }

   public synchronized void advanceIntervalTimer(String var1, TimerListener var2) throws TimerException {
      TransactionManager var3 = TxHelper.getTransactionManager();
      Transaction var4 = var3.forceSuspend();

      try {
         this.writeListener(var1, (Serializable)var2, this.sqlHelper.getAdvanceTimerSQL(var1));
      } catch (SQLException var10) {
         if (DEBUG) {
            var10.printStackTrace();
         }

         throw new TimerException("unable to timeout in database", var10);
      } finally {
         var3.forceResume(var4);
      }

   }

   public synchronized TimerState getTimerState(String var1) throws TimerException {
      Connection var2 = null;
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      TimerState var6;
      try {
         var2 = this.getJDBCConnection();
         String var5 = this.sqlHelper.getTimerStateSQL(var1);
         var3 = var2.prepareStatement(var5);
         var4 = var3.executeQuery();
         if (!var4.next()) {
            throw new TimerException("unable to get timerstate");
         }

         var6 = new TimerState(var4.getString(1), (TimerListener)ObjectPersistenceHelper.getObject(var2, var4, 2), var4.getLong(3), var4.getLong(4), (AuthenticatedSubject)null);
      } catch (SQLException var15) {
         if (DEBUG) {
            ClusterLogger.logInvalidTimerState(var1, var15);
         }

         throw new TimerException("unable to get timer", var15);
      } catch (ApplicationNotFoundException var16) {
         if (DEBUG) {
            ClusterLogger.logInvalidTimerState(var1, var16);
         }

         throw new TimerException("unable to read TimerListener", var16);
      } catch (IOException var17) {
         if (DEBUG) {
            ClusterLogger.logInvalidTimerState(var1, var17);
         }

         throw new TimerException("unable to read TimerListener", var17);
      } catch (RuntimeException var18) {
         ClusterLogger.logInvalidTimerState(var1, var18);
         throw var18;
      } catch (Error var19) {
         ClusterLogger.logInvalidTimerState(var1, var19);
         throw var19;
      } finally {
         this.closeResultSet(var4);
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }

      return var6;
   }

   public synchronized List getReadyTimers(int var1) throws TimerException {
      Connection var2 = null;
      PreparedStatement var3 = null;
      ResultSet var4 = null;
      String var5 = this.sqlHelper.getReadyTimersSQL(var1);

      try {
         var2 = this.getJDBCConnection();
         var3 = var2.prepareStatement(var5);
         var4 = var3.executeQuery();
         ArrayList var6 = new ArrayList();

         while(var4.next()) {
            var6.add(var4.getString(1));
         }

         ArrayList var7 = var6;
         return var7;
      } catch (SQLException var12) {
         if (DEBUG) {
            var12.printStackTrace();
         }

         throw new TimerException("SQLException while getting timers", var12);
      } finally {
         this.closeResultSet(var4);
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }
   }

   private Timer[] getTimersHelper(String var1) throws TimerException {
      Connection var2 = null;
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var2 = this.getJDBCConnection();
         var3 = var2.prepareStatement(var1);
         var4 = var3.executeQuery();
         ArrayList var5 = new ArrayList();

         while(var4.next()) {
            var5.add(new TimerImpl(var4.getString(1)));
         }

         Timer[] var6 = new Timer[var5.size()];
         var5.toArray(var6);
         Timer[] var7 = var6;
         return var7;
      } catch (SQLException var12) {
         if (DEBUG) {
            var12.printStackTrace();
         }

         throw new TimerException("SQLException while getting timers", var12);
      } finally {
         this.closeResultSet(var4);
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }
   }

   public Timer[] getTimers(String var1) throws TimerException {
      return this.getTimersHelper(this.sqlHelper.getTimersSQL(var1));
   }

   public Timer[] getTimers(String var1, String var2) throws TimerException {
      return this.getTimersHelper(this.sqlHelper.getTimersSQL(var1, var2));
   }

   public void cancelTimers(String var1) throws TimerException {
      try {
         this.runUpdate(this.sqlHelper.getCancelTimersSQL(var1));
      } catch (SQLException var3) {
         if (DEBUG) {
            var3.printStackTrace();
         }

         throw new TimerException(var3.toString());
      }
   }

   private synchronized String getUniqueId(TimerListener var1) {
      long var2 = System.currentTimeMillis();
      String var4 = this.serverName + "_" + var2;
      if (var2 == this.previousIdTime) {
         ++this.idSuffix;
         var4 = var4 + "_" + this.idSuffix;
      } else {
         this.previousIdTime = var2;
         this.idSuffix = 0;
      }

      return var1 instanceof TimerCreationCallback ? ((TimerCreationCallback)var1).getTimerId(var4) : var4;
   }

   private int runUpdate(String var1) throws SQLException {
      Connection var2 = this.getJDBCConnection();
      PreparedStatement var3 = null;

      int var4;
      try {
         var3 = var2.prepareStatement(var1);
         var4 = var3.executeUpdate();
      } finally {
         this.closePreparedStatement(var3);
         this.closeSQLConnection(var2);
      }

      return var4;
   }

   private void runCreate(String var1, Serializable var2, String var3) throws SQLException {
      this.writeListener(var1, var2, var3);
   }

   private void writeListener(String var1, Serializable var2, String var3) throws SQLException {
      Connection var4 = this.getJDBCConnection();
      PreparedStatement var5 = null;
      ResultSet var6 = null;
      boolean var7 = var4.getAutoCommit();
      if (var7) {
         var4.setAutoCommit(false);
      }

      try {
         var5 = var4.prepareStatement(var3);
         if (ObjectPersistenceHelper.mustSelectForInsert(var4)) {
            var5.setBlob(1, BLOB.empty_lob());
            var5.executeUpdate();
            var5 = var4.prepareStatement(((OracleSQLHelperImpl)this.sqlHelper).getSelectForInsertSQL(var1));
            var6 = var5.executeQuery();
            if (!var6.next()) {
               throw new SQLException("unable to insert listener");
            }

            Blob var8 = var6.getBlob(1);
            ObjectPersistenceHelper.writeToBlob(var8, var2);
         } else {
            ObjectPersistenceHelper var15 = new ObjectPersistenceHelper(var2);
            var5.setBinaryStream(1, var15.getBinaryStream(), (int)var15.length());
            var5.executeUpdate();
         }

         if (var7) {
            var4.commit();
         }
      } catch (IOException var13) {
         if (DEBUG) {
            var13.printStackTrace();
         }

         if (var7) {
            var4.rollback();
         }

         throw new SQLException("unable to convert Object to Blob. Reason: " + var13.getMessage());
      } finally {
         if (var7) {
            var4.setAutoCommit(var7);
         }

         this.closeResultSet(var6);
         this.closePreparedStatement(var5);
         this.closeSQLConnection(var4);
      }

   }

   private void closePreparedStatement(PreparedStatement var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
            if (DEBUG) {
               var3.printStackTrace();
            }
         }
      }

   }

   private void closeSQLConnection(Connection var1) {
      if (this.connection == null) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (SQLException var3) {
               if (DEBUG) {
                  var3.printStackTrace();
               }
            }
         }

      }
   }

   private void closeResultSet(ResultSet var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
            if (DEBUG) {
               var3.printStackTrace();
            }
         }
      }

   }

   private Connection getJDBCConnection() throws SQLException {
      if (this.connection != null) {
         return this.connection;
      } else if (this.ds != null) {
         return this.ds.getConnection();
      } else {
         JDBCDataSourceParamsBean var1 = this.jdbcResource.getJDBCResource().getJDBCDataSourceParams();
         String[] var2 = var1.getJNDINames();
         InitialContext var3 = null;

         Connection var4;
         try {
            if (var2 == null || var2.length == 0) {
               throw new SQLException("Job Scheduler data source is invalid !");
            }

            var3 = new InitialContext();
            this.ds = (DataSource)var3.lookup(var2[0]);
            var4 = this.ds.getConnection();
         } catch (NamingException var13) {
            throw new SQLException("Got a NamingException while looking up JobScheduler datasource\n" + StackTraceUtils.throwable2StackTrace(var13));
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (NamingException var12) {
               }
            }

         }

         return var4;
      }
   }

   private static void debug(String var0) {
      ClusterLogger.logDebug("[DBTimerBasisImpl] " + var0);
   }

   public static TimerBasis getDBTimerBasis(Connection var0, String var1, String var2, String var3, String var4) throws SQLException {
      return new DBTimerBasisImpl(var0, var1, var2, var3, var4);
   }

   private DBTimerBasisImpl(Connection var1, String var2, String var3, String var4, String var5) throws SQLException {
      this.connection = var1;
      this.jdbcResource = null;
      this.serverName = var5;
      if (JDBCHelper.getDBMSType(var1.getMetaData(), (String[])null) == 1) {
         this.sqlHelper = new OracleSQLHelperImpl(var2, var3, var4);
      } else {
         this.sqlHelper = new GenericSQLHelperImpl(var2, var3, var4);
      }

   }
}
