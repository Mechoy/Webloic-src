package weblogic.management.deploy.status;

import weblogic.management.TargetAvailabilityStatus;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.utils.Debug;

/** @deprecated */
public class StatusFactory {
   private static StatusFactory instance = new StatusFactory();

   public static StatusFactory getInstance() {
      return instance;
   }

   private StatusFactory() {
   }

   public TargetAvailabilityStatus createStatus(ApplicationMBean var1, ConfigurationMBean var2) {
      Object var3 = null;
      boolean var4 = var1.getStagingMode().equals("stage");
      if (var2 instanceof ServerMBean) {
         var3 = new ServerTargetAvailabilityStatus((ServerMBean)var2, var4);
      } else if (var2 instanceof ClusterMBean) {
         var3 = new ClusterTargetAvailabilityStatus((ClusterMBean)var2, var4);
      } else if (var2 instanceof VirtualHostMBean) {
         var3 = new VirtualHostTargetAvailabilityStatus((VirtualHostMBean)var2, var4);
      } else {
         Debug.assertion(false, "Invalid ConfigurationMBean");
      }

      return (TargetAvailabilityStatus)var3;
   }
}
