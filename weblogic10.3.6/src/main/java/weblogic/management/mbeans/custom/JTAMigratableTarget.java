package weblogic.management.mbeans.custom;

import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public final class JTAMigratableTarget extends MigratableTarget {
   private transient ServerMBean userPreferredServer;
   private transient ServerMBean[] candidateServers;

   public JTAMigratableTarget(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setConstrainedCandidateServers(ServerMBean[] var1) {
      this.candidateServers = var1;
   }

   public ServerMBean[] getConstrainedCandidateServers() {
      if (this.candidateServers != null) {
         return this.candidateServers;
      } else {
         ServerMBean var1 = (ServerMBean)((ServerMBean)this.getMbean().getParent());
         ClusterMBean var2 = null;
         if (var1 != null) {
            var2 = var1.getCluster();
         }

         return var2 != null ? var2.getServers() : new ServerMBean[0];
      }
   }

   public ClusterMBean getCluster() {
      ServerMBean var1 = (ServerMBean)((ServerMBean)this.getMbean().getParent());
      return var1.getCluster();
   }

   public void setUserPreferredServer(ServerMBean var1) {
      this.userPreferredServer = var1;
   }

   public ServerMBean getUserPreferredServer() {
      return this.userPreferredServer != null ? this.userPreferredServer : (ServerMBean)this.getMbean().getParent();
   }
}
