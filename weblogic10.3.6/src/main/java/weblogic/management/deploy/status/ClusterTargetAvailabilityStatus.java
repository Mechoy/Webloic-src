package weblogic.management.deploy.status;

import java.io.Serializable;
import weblogic.management.TargetAvailabilityStatus;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;

/** @deprecated */
public class ClusterTargetAvailabilityStatus extends BaseTargetAvailabilityStatus implements Serializable, TargetAvailabilityStatus {
   private static final long serialVersionUID = -2825946542858790432L;

   public ClusterTargetAvailabilityStatus(ClusterMBean var1, boolean var2) {
      super(var1.getName(), 2, var2);
      ServerMBean[] var3 = var1.getServers();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var2) {
               this.unavailableServersAvailabilityMap.put(var3[var4].getName(), new Integer(2));
            } else {
               this.unavailableServersAvailabilityMap.put(var3[var4].getName(), new Integer(0));
            }
         }
      }

   }
}
