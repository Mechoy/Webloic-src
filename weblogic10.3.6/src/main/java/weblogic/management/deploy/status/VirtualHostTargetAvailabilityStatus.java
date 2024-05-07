package weblogic.management.deploy.status;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.deploy.internal.ComponentTarget;
import weblogic.utils.Debug;

/** @deprecated */
public class VirtualHostTargetAvailabilityStatus extends BaseTargetAvailabilityStatus {
   private static final long serialVersionUID = 8676607003211923506L;
   private HashSet clusterSet = null;

   public VirtualHostTargetAvailabilityStatus(VirtualHostMBean var1, boolean var2) {
      super(var1.getName(), 3, var2);
      this.clusterSet = new HashSet();
      TargetMBean[] var3 = var1.getTargets();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] instanceof ClusterMBean) {
               ClusterTargetAvailabilityStatus var5 = new ClusterTargetAvailabilityStatus((ClusterMBean)var3[var4], var2);
               this.clusterSet.add(var5);
            } else if (var2) {
               this.unavailableServersAvailabilityMap.put(var3[var4].getName(), new Integer(2));
            } else {
               this.unavailableServersAvailabilityMap.put(var3[var4].getName(), new Integer(0));
            }
         }
      }

   }

   public int getDeploymentStatus() {
      int var1 = 0;
      if (this.clusterSet.size() > 0) {
         Iterator var2 = this.clusterSet.iterator();
         int var3 = 0;

         while(var2.hasNext()) {
            ClusterTargetAvailabilityStatus var4 = (ClusterTargetAvailabilityStatus)var2.next();
            if (var3 == 0) {
               var1 = var4.getDeploymentStatus();
               ++var3;
            } else if (var1 != var4.getDeploymentStatus()) {
               var1 = 2;
            }
         }

         if ((this.availableServers.size() > 0 || this.unavailableServersAvailabilityMap.size() > 0) && var1 != super.getDeploymentStatus()) {
            var1 = 2;
         }
      } else {
         var1 = super.getDeploymentStatus();
      }

      return var1;
   }

   public Set getClustersAvailabilityStatus() {
      return this.clusterSet;
   }

   public void updateAvailabilityStatus(ComponentTarget var1) {
      if (var1.isVirtualHostClustered()) {
         String var2 = var1.getClusterTarget();
         Iterator var3 = this.clusterSet.iterator();

         while(var3.hasNext()) {
            ClusterTargetAvailabilityStatus var4 = (ClusterTargetAvailabilityStatus)var3.next();
            if (var4.getTargetName().equals(var2)) {
               var4.updateAvailabilityStatus(var1);
               break;
            }
         }
      } else {
         super.updateAvailabilityStatus(var1);
      }

   }

   public void updateUnavailabilityStatus(List var1) {
      if (this.isStaged) {
         Iterator var2 = this.clusterSet.iterator();

         while(var2.hasNext()) {
            ClusterTargetAvailabilityStatus var3 = (ClusterTargetAvailabilityStatus)var2.next();
            var3.updateUnavailabilityStatus(var1);
         }

         super.updateUnavailabilityStatus(var1);
      } else {
         Debug.assertion(false, "updateUnavailabilityStatus applicable only for staged applications.");
      }

   }
}
