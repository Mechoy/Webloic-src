package weblogic.servlet.internal.session;

import java.util.Collections;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;

public class AsyncJDBCSessionContext extends JDBCSessionContext {
   private AsyncJDBCPersistenceManager persistenceManager;

   public AsyncJDBCSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
      this.persistenceManager = new AsyncJDBCPersistenceManager(this.dataSource, this.updateQuery, this.insertQuery, this.deleteQuery, this.configMgr.getPersistentSessionFlushInterval(), this.configMgr.getPersistentSessionFlushThreshold(), this.configMgr.getPersistentAsyncQueueTimeout());
   }

   public String getPersistentStoreType() {
      return "async-jdbc";
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      JDBCSessionData var4 = AsyncJDBCSessionData.newSession(var1, this, this.dataSource, this.properties);
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

   public JDBCSessionData getSessionDataFromDB(String var1, Properties var2) {
      return AsyncJDBCSessionData.getFromDB(var1, this, this.dataSource, var2);
   }

   protected JDBCSessionData createNewData(String var1, SessionContext var2, DataSource var3, Properties var4, boolean var5) {
      return new AsyncJDBCSessionData(var1, var2, var3, var4, var5);
   }

   public AsyncJDBCPersistenceManager getPersistenceManager() {
      return this.persistenceManager;
   }

   public void destroy(boolean var1) {
      this.persistenceManager.blockingFlush();
      super.destroy(var1);
   }
}
