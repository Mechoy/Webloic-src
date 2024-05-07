package weblogic.servlet.internal.session;

import weblogic.cluster.replication.MANReplicationManager;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.servlet.internal.WebAppServletContext;

public class MANReplicatedSessionContext extends ReplicatedSessionContext {
   private static final ReplicationServices repserv = MANReplicationManager.services();

   public MANReplicatedSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   public String getPersistentStoreType() {
      return "sync-replication-across-cluster";
   }

   protected ReplicationServices getReplicationServices() {
      return repserv;
   }
}
