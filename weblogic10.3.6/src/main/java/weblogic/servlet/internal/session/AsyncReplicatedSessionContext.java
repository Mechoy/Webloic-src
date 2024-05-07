package weblogic.servlet.internal.session;

import javax.servlet.http.HttpSession;
import weblogic.cluster.replication.AsyncReplicationManager;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;

public class AsyncReplicatedSessionContext extends ReplicatedSessionContext {
   private static final ReplicationServices repserv = AsyncReplicationManager.services();

   public AsyncReplicatedSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   public String getPersistentStoreType() {
      return "async-replication";
   }

   protected ReplicationServices getReplicationServices() {
      return repserv;
   }

   public HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      this.checkSessionCount();
      AsyncReplicatedSessionData var4 = new AsyncReplicatedSessionData(var1, this);
      var4.setMonitoringId();
      return var4;
   }

   public void destroy(boolean var1) {
      ((AsyncReplicationManager)repserv).blockingFlush();
      super.destroy(var1);
   }
}
