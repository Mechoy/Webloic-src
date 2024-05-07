package weblogic.management.deploy.status;

import java.util.Set;
import weblogic.management.configuration.ServerMBean;
import weblogic.utils.Debug;

/** @deprecated */
public class ServerTargetAvailabilityStatus extends BaseTargetAvailabilityStatus {
   private static final long serialVersionUID = -2705317822110632531L;

   public ServerTargetAvailabilityStatus(ServerMBean var1, boolean var2) {
      super(var1.getName(), 1, var2);
      if (var2) {
         this.unavailableServersAvailabilityMap.put(var1.getName(), new Integer(2));
      } else {
         this.unavailableServersAvailabilityMap.put(var1.getName(), new Integer(0));
      }

   }

   public ServerTargetAvailabilityStatus(String var1, boolean var2, boolean var3, int var4) {
      super(var1, 1, var2);
      if (var3) {
         this.availableServers.add(var1);
      } else {
         this.unavailableServersAvailabilityMap.put(var1, new Integer(var4));
      }

   }

   public int getAvailabilityStatus() {
      boolean var1 = false;
      int var2 = this.getDeploymentStatus();
      int var4;
      if (var2 == 1) {
         var4 = 1;
      } else {
         Integer var3 = (Integer)this.unavailableServersAvailabilityMap.get(this.getTargetName());
         var4 = var3;
      }

      return var4;
   }

   public Set getServersAvailabilityStatus() {
      Debug.assertion(false, "This is not a valid call");
      return null;
   }

   public Set getClustersAvailabilityStatus() {
      Debug.assertion(false, "This is not a valid call");
      return null;
   }
}
