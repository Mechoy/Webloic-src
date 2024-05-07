package weblogic.cluster.singleton;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.jndi.Environment;
import weblogic.protocol.LocalServerIdentity;

class ClusterMaster implements MigratableServiceConstants, SingletonService, ClusterMasterRemote, ClusterLeaderListener {
   private boolean isClusterMaster = false;
   private final MigratableServersMonitorImpl monitor;
   static final String CLUSTER_MASTER = "CLUSTER_MASTER";

   ClusterMaster(int var1) {
      this.monitor = new MigratableServersMonitorImpl(ClusterService.getServices().getDefaultLeaseManager("wlsserver"), var1);
   }

   public boolean isClusterMaster() {
      return this.isClusterMaster;
   }

   public void start() {
      if ("consensus".equals(MigratableServerService.theOne().getLeasingType())) {
         AbstractConsensusService.getInstance().addClusterLeaderListener(this);
      } else {
         SingletonServicesManager.getInstance().add(this.getName(), this);
      }

   }

   public void activate() {
      this.isClusterMaster = true;
      this.bindClusterMaster(this);
      this.monitor.start();
      ClusterLogger.logClusterMasterElected(LocalServerIdentity.getIdentity().getServerName());
   }

   public void deactivate() {
      if (this.isClusterMaster) {
         this.isClusterMaster = false;
         ClusterLogger.logRevokeClusterMasterRole(LocalServerIdentity.getIdentity().getServerName());
         this.monitor.stop();
      }
   }

   public void localServerIsClusterLeader() {
      this.activate();
   }

   public void localServerLostClusterLeadership() {
      this.deactivate();
   }

   public String getName() {
      return "CLUSTER_MASTER";
   }

   public void setServerLocation(String var1, String var2) throws RemoteException {
      this.monitor.setServerLocation(var1, var2);
   }

   public String getServerLocation(String var1) throws RemoteException {
      return this.monitor.getCurrentMachine(var1);
   }

   private void bindClusterMaster(final ClusterMaster var1) {
      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Context var1x = null;

               try {
                  Environment var2 = new Environment();
                  var2.setCreateIntermediateContexts(true);
                  var2.setReplicateBindings(false);
                  var1x = var2.getInitialContext();
                  var1x.rebind("weblogic/cluster/singleton/ClusterMasterRemote", var1);
               } catch (NamingException var11) {
                  throw new AssertionError("Unexpected exception" + var11);
               } finally {
                  if (var1x != null) {
                     try {
                        var1x.close();
                     } catch (NamingException var10) {
                     }
                  }

               }

               return null;
            }
         });
      } catch (Exception var3) {
         throw new AssertionError("Unexpected exception" + var3);
      }
   }
}
