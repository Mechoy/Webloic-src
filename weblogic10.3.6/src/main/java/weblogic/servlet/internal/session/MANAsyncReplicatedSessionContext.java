package weblogic.servlet.internal.session;

import weblogic.cluster.replication.MANAsyncReplicationManager;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.servlet.internal.WebAppServletContext;

public class MANAsyncReplicatedSessionContext extends AsyncReplicatedSessionContext {
   private static final ReplicationServices repserv = MANAsyncReplicationManager.services();

   public MANAsyncReplicatedSessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      super(var1, var2);
   }

   public String getPersistentStoreType() {
      return "async-replication-across-cluster";
   }

   protected ReplicationServices getReplicationServices() {
      return repserv;
   }
}
