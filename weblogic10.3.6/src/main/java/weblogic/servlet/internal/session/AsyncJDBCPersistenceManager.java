package weblogic.servlet.internal.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import javax.sql.DataSource;
import weblogic.cluster.replication.AsyncFlush;
import weblogic.cluster.replication.AsyncQueueManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.utils.collections.Stack;

public class AsyncJDBCPersistenceManager implements AsyncFlush {
   private AsyncQueueManager queue;
   private String updateQuery;
   private String insertQuery;
   private String deleteQuery;
   private Properties jdbcProps;
   private DataSource dataSource;
   private Stack pendingUpdates;
   private static final int QUERY_TIMEOUT = 30;
   protected static final DebugLogger DEBUG_SESSIONS = DebugLogger.getDebugLogger("DebugHttpSessions");

   public AsyncJDBCPersistenceManager(DataSource var1, String var2, String var3, String var4, int var5, int var6, int var7) {
      this.dataSource = var1;
      this.updateQuery = var2;
      this.insertQuery = var3;
      this.deleteQuery = var4;
      this.queue = new AsyncQueueManager(this, false, var5, var6, var7);
      this.pendingUpdates = new Stack();
   }

   public Connection getConnection() throws SQLException {
      return JDBCSessionData.getConnection(this.dataSource, (Properties)null);
   }

   public void update(AsyncJDBCSessionData var1) {
      this.queue.addToUpdates(var1);
   }

   public void blockingFlush() {
      this.queue.flushOnce();
   }

   public synchronized void flushQueue(BlockingQueue var1) {
      HashSet var2 = new HashSet();
      var1.drainTo(var2);
      if (this.pendingUpdates.size() > 0) {
         var2.addAll(this.pendingUpdates);
         this.pendingUpdates.clear();
      }

      Connection var3 = null;

      try {
         var3 = this.getConnection();
      } catch (SQLException var13) {
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Database unavailable", var13);
         }

         this.pendingUpdates.addAll(var2);
         return;
      }

      Object var4 = null;

      try {
         this.createSessionsInDB(var3, (PreparedStatement)var4, var2);
         this.updateSessionsInDB(var3, (PreparedStatement)var4, var2);
         this.removeSessionsInDB(var3, (PreparedStatement)var4, var2);
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Persisted " + var2.size() + " sessions to the database");
         }
      } finally {
         this.closeStatement((PreparedStatement)var4);
         if (var3 != null) {
            try {
               var3.close();
            } catch (SQLException var12) {
            }
         }

      }

   }

   private void createSessionsInDB(Connection var1, PreparedStatement var2, Set var3) {
      boolean var4 = false;

      try {
         var2 = var1.prepareStatement(this.insertQuery);
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            AsyncJDBCSessionData var6 = (AsyncJDBCSessionData)var5.next();
            if (var6.getState() == 1) {
               var4 = true;
               var6.addStatements(var2);
               var2.addBatch();
            }
         }

         if (var4) {
            var2.executeBatch();
         }
      } catch (SQLException var11) {
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Failed while making bulk  insert. We will automatically attempt to fix this ", var11);
         }
      } finally {
         this.closeStatement(var2);
      }

   }

   private void updateSessionsInDB(Connection var1, PreparedStatement var2, Set var3) {
      boolean var4 = false;

      try {
         var2 = var1.prepareStatement(this.updateQuery);
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            AsyncJDBCSessionData var6 = (AsyncJDBCSessionData)var5.next();
            if (var6.getState() == 2) {
               var4 = true;
               var6.addStatements(var2);
               var2.addBatch();
            }
         }

         if (var4) {
            var2.executeBatch();
         }
      } catch (SQLException var11) {
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Failed while making bulk  update. We will automatically attempt to fix this ", var11);
         }
      } finally {
         this.closeStatement(var2);
      }

   }

   public void removeSessionsInDB(Connection var1, PreparedStatement var2, Set var3) {
      boolean var4 = false;

      try {
         var2 = var1.prepareStatement(this.deleteQuery);
         setQueryTimeout(var2, 30);
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            AsyncJDBCSessionData var6 = (AsyncJDBCSessionData)var5.next();
            if (var6.getState() == 3) {
               var4 = true;
               var6.addStatements(var2);
               var2.addBatch();
            }
         }

         if (var4) {
            var2.executeBatch();
         }
      } catch (SQLException var11) {
         if (DEBUG_SESSIONS.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Failed to invalidate some  sessions. The server will perform auto recovery", var11);
         }
      } finally {
         this.closeStatement(var2);
      }

   }

   private static void setQueryTimeout(PreparedStatement var0, int var1) {
      try {
         var0.setQueryTimeout(var1);
      } catch (SQLException var3) {
      }

   }

   private void closeStatement(PreparedStatement var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (SQLException var3) {
         }
      }

   }
}
