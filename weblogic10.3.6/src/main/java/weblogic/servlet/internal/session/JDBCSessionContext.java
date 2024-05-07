package weblogic.servlet.internal.session;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import weblogic.cache.utils.BubblingCache;
import weblogic.common.ResourceException;
import weblogic.jdbc.common.internal.ConnectionPool;
import weblogic.jdbc.common.internal.ConnectionPoolManager;
import weblogic.servlet.internal.NakedTimerListenerBase;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.timers.Timer;

public class JDBCSessionContext extends SessionContext {
   protected final String selectQuery;
   protected final String selectLATQuery;
   protected final String selectIDSQuery;
   protected final String updateQuery;
   protected final String deleteQuery;
   protected final String insertQuery;
   protected final String countQuery;
   protected final String selectCtxPathQuery;
   protected final String tableName;
   protected final String wlMaxInactiveInternal;
   protected final Properties properties;
   protected final Map cache;
   protected final DataSource dataSource;
   protected static final String dataSourceKey = "dataSourceProp";
   private LastAccessTimeTrigger latTrigger;
   private static boolean tryImplicitConversionFromCharToSmallInt = Boolean.getBoolean("weblogic.servlet.sessions.jdbc.tryImplicitConversionFromCharToSmallInt");

   public JDBCSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
      this.wlMaxInactiveInternal = this.configMgr.getMaxInactiveIntervalColumnName();
      this.tableName = this.configMgr.getPersistentStoreTable();
      this.insertQuery = this.createInsertQuery();
      this.selectIDSQuery = this.createSelectIDSQuery();
      this.updateQuery = this.createUpdateQuery();
      this.selectQuery = this.createSelectQuery();
      this.selectLATQuery = this.createSelectLATQuery();
      this.deleteQuery = this.createDeleteQuery();
      this.countQuery = this.createCountQuery();
      this.selectCtxPathQuery = this.createSelectCtxPathQuery();
      this.cache = (Map)(this.configMgr.getCacheSize() <= 0 ? Collections.EMPTY_MAP : new JDBCSessionCache(this.configMgr.getCacheSize()));
      this.properties = new Properties();
      String var3;
      if ((var3 = this.configMgr.getPersistentDataSourceJNDIName()) != null) {
         this.dataSource = this.lookupDataSource(var3);
      } else {
         this.properties.put("connectionPoolID", this.configMgr.getPersistentStorePool());
         this.initializeConnectionPool();
         this.dataSource = null;
      }

   }

   private DataSource lookupDataSource(String var1) {
      InitialContext var2 = null;

      Object var4;
      try {
         var2 = new InitialContext();
         DataSource var3 = (DataSource)var2.lookup(var1);
         return var3;
      } catch (NamingException var14) {
         HTTPSessionLogger.logUnexpectedError(this.getServletContext().getLogContext(), var14);
         var4 = null;
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (NamingException var13) {
            }
         }

      }

      return (DataSource)var4;
   }

   public void startTimers() {
      super.startTimers();
      this.latTrigger = new LastAccessTimeTrigger(this.getServletContext(), this.dataSource, this.properties, 10, this.isDebugEnabled());
   }

   private void initializeConnectionPool() {
      try {
         ConnectionPool var1 = ConnectionPoolManager.getPool(this.configMgr.getPersistentStorePool());
         if (var1 != null) {
            var1.setResourceReserveTimeoutSeconds(this.configMgr.getJDBCConnectionTimeoutSecs());
            HTTPSessionLogger.logConnectionPoolReserveTimeoutSecondsOverride();
         }
      } catch (ResourceException var2) {
         HTTPSessionLogger.logUnexpectedError(this.getServletContext().getLogContext(), var2);
      }

   }

   String getSelectQuery() {
      return this.selectQuery;
   }

   String getSelectLATQuery() {
      return this.selectLATQuery;
   }

   String getUpdateQuery() {
      return this.updateQuery;
   }

   String getDeleteQuery() {
      return this.deleteQuery;
   }

   String getInsertQuery() {
      return this.insertQuery;
   }

   String getCountQuery() {
      return this.countQuery;
   }

   String getSelectCtxPathQuery() {
      return this.selectCtxPathQuery;
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      JDBCSessionData var4 = JDBCSessionData.newSession(var1, this, this.dataSource, this.properties);
      if (var4 == null) {
         return null;
      } else {
         var4.incrementActiveRequestCount();
         if (this.cache != Collections.EMPTY_MAP) {
            this.cache.put(var4.id, var4);
         }

         this.incrementOpenSessionsCount();
         SessionData.checkSpecial(var2, var4);
         var4.setMonitoringId();
         return var4;
      }
   }

   public String getPersistentStoreType() {
      return "jdbc";
   }

   protected void invalidateOrphanedSessions() {
      Set var1 = this.getServletContext().getServer().getSessionLogin().getAllIds();
      if (!var1.isEmpty()) {
         String[] var2 = this.getIdsInternal();

         int var3;
         for(var3 = 0; var3 < var2.length; ++var3) {
            var1.remove(var2[var3]);
         }

         if (var3 != 0) {
            String var4 = null;
            Iterator var5 = var1.iterator();

            while(var5.hasNext()) {
               var4 = (String)var5.next();
               this.getServletContext().getServer().getSessionLogin().unregister(var4, this.getServletContext().getContextPath());
            }

         }
      }
   }

   public SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      var1 = RSID.getID(var1);
      synchronized(var1.intern()) {
         JDBCSessionData var5 = (JDBCSessionData)this.cache.get(var1);
         if (var5 != null) {
            synchronized(var5) {
               if (!var5.sessionInUse() && var5.isCacheStale()) {
                  this.cache.remove(var1);
                  var5 = null;
               }
            }
         }

         JDBCSessionData var10000;
         if (var5 != null) {
            synchronized(var5) {
               if (var2 != null && var3 != null) {
                  if (!var5.isValidForceCheck()) {
                     this.cache.remove(var1);
                     var10000 = null;
                     return var10000;
                  }

                  var5.incrementActiveRequestCount();
               }

               var10000 = var5;
            }

            return var10000;
         } else {
            var5 = this.getSessionDataFromDB(var1, this.properties);
            if (var5 == null) {
               return null;
            } else {
               synchronized(var5) {
                  var5.transientAttributes = (Hashtable)this.transientData.get(var5.id);
                  if (var2 != null && var3 != null) {
                     if (!var5.isValidForceCheck()) {
                        var10000 = null;
                        return var10000;
                     }

                     var5.incrementActiveRequestCount();
                     var5.updateVersionIfNeeded(this);
                     ((JDBCSessionContext)var5.getContext()).cacheSession(var5);
                     var5.notifyActivated(new HttpSessionEvent(var5));
                     var5.reinitRuntimeMBean();
                  }
               }

               return var5;
            }
         }
      }
   }

   public JDBCSessionData getSessionDataFromDB(String var1, Properties var2) {
      return JDBCSessionData.getFromDB(var1, this, this.dataSource, var2);
   }

   protected JDBCSessionData createNewData(String var1, SessionContext var2, DataSource var3, Properties var4, boolean var5) {
      return new JDBCSessionData(var1, var2, var3, var4, var5);
   }

   public String[] getIdsInternal() {
      return JDBCSessionData.getSessionIds(this.dataSource, this.properties, this.getServletContext(), this.selectIDSQuery);
   }

   public String lookupAppVersionIdForSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      if (var1 == null) {
         return null;
      } else {
         var1 = RSID.getID(var1);
         SessionData var4 = (SessionData)this.cache.get(var1);
         if (var4 != null) {
            return var4.getVersionId();
         } else {
            String var5 = this.getServletContext().getName();
            String[] var6 = JDBCSessionData.getWlCtxPaths(this.dataSource, this.properties, this.getServletContext(), this.getSelectCtxPathQuery(), var1);
            String[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String var10 = var7[var9];
               if (var10.startsWith(var5)) {
                  int var11 = var10.indexOf(35);
                  if (var11 != -1) {
                     return var10.substring(var11 + 1);
                  }
               }
            }

            return null;
         }
      }
   }

   void unregisterExpiredSessions(ArrayList var1) {
   }

   public int getCurrOpenSessionsCount() {
      return JDBCSessionData.getTotalSessionsCount(this.dataSource, this.properties, this.getServletContext(), this.countQuery);
   }

   boolean invalidateSession(SessionData var1, boolean var2, boolean var3) {
      if (var1 == null) {
         return false;
      } else {
         this.cache.remove(var1.id);

         try {
            this.transientData.remove(var1.id);
            var1.remove(var3);
            this.decrementOpenSessionsCount();
            SessionData.invalidateProcessedSession(var1);
            return true;
         } catch (Exception var5) {
            HTTPSessionLogger.logUnableToRemoveSession(var1.id, var5);
            return false;
         }
      }
   }

   public void sync(HttpSession var1) {
      JDBCSessionData var2 = (JDBCSessionData)var1;
      synchronized(var2) {
         var2.decrementActiveRequestCount();
         if (!var2.sessionInUse()) {
            var2.notifyAboutToPassivate(new HttpSessionEvent(var2));
            var2.syncSession();
            if (this.cache != Collections.EMPTY_MAP) {
               this.cache.put(var2.id, var1);
            }

            if (var2.transientAttributes != null) {
               this.transientData.put(var2.id, var2.transientAttributes);
            }

         }
      }
   }

   public void destroy(boolean var1) {
      super.destroy(var1);
      if (this.latTrigger != null) {
         this.latTrigger.stop();
      }

   }

   public int getNonPersistedSessionCount() {
      return 0;
   }

   void updateLAT(JDBCSessionData var1, String var2, String var3) {
      this.latTrigger.addLATUpdateQuery(new UpdateQueryObject(var1, var2, var3, this.tableName));
   }

   private String createInsertQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("insert into ");
      var1.append(this.tableName);
      var1.append(" (wl_id, wl_context_path, wl_is_new, wl_create_time, ");
      var1.append("wl_session_values, wl_is_valid, wl_access_time, ");
      var1.append(this.wlMaxInactiveInternal);
      var1.append(" ) values (?, ?, ?, ?, ?, ?, ?, ?)");
      return var1.toString();
   }

   protected String createSelectIDSQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("select wl_id from ");
      var1.append(this.tableName);
      var1.append(" where ( wl_context_path = ? or wl_context_path = ? )");
      return var1.toString();
   }

   private String createUpdateQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("update ");
      var1.append(this.tableName);
      var1.append(" set wl_session_values = ?, ");
      var1.append("wl_is_new = ?, wl_is_valid = ?, wl_access_time = ?, ");
      var1.append(this.wlMaxInactiveInternal);
      var1.append(" = ?");
      var1.append(" where wl_id = ? ");
      var1.append(" and ( wl_context_path = ? or wl_context_path = ? )");
      var1.append(" and ( wl_access_time = ? or wl_access_time = ? )");
      return var1.toString();
   }

   protected String createSelectQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("select wl_is_new, ");
      var1.append(" wl_create_time,");
      var1.append("wl_session_values, wl_is_valid, wl_access_time, ");
      var1.append(this.wlMaxInactiveInternal);
      var1.append(" from ");
      var1.append(this.tableName);
      var1.append(" where wl_id = ?");
      var1.append(" and ( wl_context_path = ? or wl_context_path = ? )");
      return var1.toString();
   }

   protected String createSelectLATQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("select wl_access_time from ");
      var1.append(this.tableName);
      var1.append(" where wl_id = ?");
      var1.append(" and ( wl_context_path = ? or wl_context_path = ? )");
      return var1.toString();
   }

   private String createDeleteQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("delete from ");
      var1.append(this.tableName);
      var1.append(" where wl_id = ?");
      var1.append("and ( wl_context_path = ? or wl_context_path = ? )");
      return var1.toString();
   }

   protected String createCountQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("select count(*) from ");
      var1.append(this.tableName);
      var1.append(" where ( wl_context_path = ? or wl_context_path = ? )");
      return var1.toString();
   }

   protected String createSelectCtxPathQuery() {
      StringBuilder var1 = new StringBuilder();
      var1.append("select wl_context_path from ").append(this.tableName);
      var1.append(" where wl_id = ? ");
      return var1.toString();
   }

   private void cacheSession(JDBCSessionData var1) {
      if (this.cache != Collections.EMPTY_MAP) {
         this.cache.put(var1.id, var1);
      }

   }

   protected class JDBCSessionCache extends BubblingCache {
      private static final long serialVersionUID = 6276338313903142442L;
      private final Map overflows = new WeakHashMap();

      public JDBCSessionCache(int var2) {
         super(var2);
      }

      public synchronized Object get(Object var1) {
         Object var2 = super.get(var1);
         if (var2 == null) {
            WeakReference var3 = (WeakReference)this.overflows.get(var1);
            if (var3 != null) {
               var2 = var3.get();
               if (var2 == null) {
                  this.overflows.remove(var1);
               }
            }
         }

         return var2;
      }

      public synchronized Object put(Object var1, Object var2) {
         JDBCSessionData var3 = (JDBCSessionData)super.put(var1, var2);
         if (var3 != null && var3 != var2) {
            this.overflows.put(var3.id, new WeakReference(var3));
         }

         return var3;
      }

      public synchronized Object remove(Object var1) {
         this.overflows.remove(var1);
         return super.remove(var1);
      }
   }

   private static class UpdateQueryObject {
      private final JDBCSessionData session;
      private final String contextName;
      private final String ctxNamePlusVid;
      private final String tableName;
      private final int hashCode;

      public UpdateQueryObject(JDBCSessionData var1, String var2, String var3, String var4) {
         this.session = var1;
         this.contextName = var2;
         this.ctxNamePlusVid = var3;
         this.hashCode = var1.getInternalId().hashCode() ^ var2.hashCode();
         this.tableName = var4;
      }

      public JDBCSessionData getSession() {
         return this.session;
      }

      public int hashCode() {
         return this.hashCode;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof UpdateQueryObject)) {
            return false;
         } else {
            UpdateQueryObject var2 = (UpdateQueryObject)var1;
            return this.session.getInternalId().equals(var2.session.getInternalId()) && this.contextName.equals(var2.contextName);
         }
      }

      public String getQuery(long var1) {
         StringBuilder var3 = new StringBuilder();
         var3.append("update ");
         var3.append(this.tableName);
         var3.append(" set  wl_is_valid =");
         if (!JDBCSessionContext.tryImplicitConversionFromCharToSmallInt) {
            var3.append(this.session.isValid() ? "'1'" : "'0'");
         } else {
            var3.append(this.session.isValid() ? "1" : "0");
         }

         var3.append(", wl_access_time =");
         var3.append(var1);
         var3.append(" where wl_id = '");
         var3.append(this.session.getInternalId());
         var3.append("' and ( wl_context_path ='");
         var3.append(this.contextName);
         var3.append("' or wl_context_path = '");
         var3.append(this.ctxNamePlusVid);
         var3.append("' ) and wl_access_time < ");
         var3.append(var1);
         return var3.toString();
      }
   }

   private static class LastAccessTimeTrigger extends NakedTimerListenerBase {
      private Timer timer;
      private final Properties jdbcProps;
      private final HashSet set;
      private final int triggerInterval;
      private final boolean debug;
      private final DataSource dataSource;

      private LastAccessTimeTrigger(WebAppServletContext var1, DataSource var2, Properties var3, int var4, boolean var5) {
         super("JDBCLastAccessTimeTrigger", var1);
         this.jdbcProps = var3;
         this.dataSource = var2;
         this.set = new HashSet();
         this.triggerInterval = var4 * 1000;
         this.debug = var5;
         this.start();
      }

      private void start() {
         this.timer = this.timerManager.schedule(this, 0L, (long)this.triggerInterval);
      }

      private void stop() {
         this.timer.cancel();
         this.timerManager.stop();
      }

      public Connection getConnection() throws SQLException {
         return this.dataSource != null ? this.dataSource.getConnection() : JDBCSessionData.getConnection(this.dataSource, this.jdbcProps);
      }

      public void timerExpired(Timer var1) {
         Connection var2 = null;
         Statement var3 = null;
         HashSet var4 = new HashSet();
         if (this.debug) {
            SessionContext.DEBUG_SESSIONS.debug("LAT trigger started at " + new Date() + " size " + this.set.size());
         }

         try {
            try {
               synchronized(this.set) {
                  if (this.set.size() == 0) {
                     return;
                  }

                  var4.addAll(this.set);
                  this.set.clear();
               }

               var2 = this.getConnection();
               var3 = var2.createStatement();
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  UpdateQueryObject var6 = (UpdateQueryObject)var5.next();
                  JDBCSessionData var7 = var6.getSession();
                  synchronized(var7) {
                     if (!var7.isCacheStale()) {
                        long var9 = var7.getLAT();
                        var3.addBatch(var6.getQuery(var9));
                        var6.getSession().setTriggerLAT(var9);
                     }
                  }
               }

               var3.executeBatch();
               var4.clear();
            } catch (SQLException var31) {
               var31.printStackTrace();
            }

         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (SQLException var28) {
            }

            try {
               if (var3 != null) {
                  var3.close();
               }
            } catch (SQLException var27) {
            }

         }
      }

      private void addLATUpdateQuery(UpdateQueryObject var1) {
         synchronized(this.set) {
            this.set.add(var1);
         }
      }

      // $FF: synthetic method
      LastAccessTimeTrigger(WebAppServletContext var1, DataSource var2, Properties var3, int var4, boolean var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }
}
