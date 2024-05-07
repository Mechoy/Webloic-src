package weblogic.servlet.internal.session;

import java.util.Iterator;
import javax.servlet.http.HttpSession;
import weblogic.servlet.cluster.WANPersistenceManager;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;

public final class WANSessionContext extends ReplicatedSessionContext {
   private final String contextPath = this.getServletContext().getContextPath();

   public WANSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   public String getPersistentStoreType() {
      return "async-replication-across-cluster";
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      this.checkSessionCount();
      WANSessionData var4 = new WANSessionData(var1, this);
      var4.setMonitoringId();
      return var4;
   }

   public SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      SessionData var4 = super.getSessionInternal(var1, var2, var3);
      String var5 = RSID.getID(var1);
      if (var4 == null && var2 != null && var3 != null) {
         WANSessionData var6 = new WANSessionData(var5, this, false);
         return WANPersistenceManager.getInstance().fetchState(var5, this.contextPath, var6) ? var6 : null;
      } else {
         return var4;
      }
   }

   public void destroy(boolean var1) {
      Iterator var2 = this.getOpenSessions().values().iterator();

      while(var2.hasNext()) {
         WANSessionData var3 = (WANSessionData)var2.next();
         WANPersistenceManager.getInstance().flushUponShutdown(RSID.getID(var3.getIdWithServerInfo()), var3.getCreationTime(), var3.getContextPath(), var3.getMaxInactiveInterval(), var3.getLAT(), var3.getSessionDiff());
      }

      super.destroy(var1);
   }

   public int getNonPersistedSessionCount() {
      return 0;
   }
}
