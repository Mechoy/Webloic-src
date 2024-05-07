package weblogic.deploy.internal.targetserver.state;

import weblogic.application.ModuleListener;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public class ListenerFactory {
   public static ModuleListener createListener(BasicDeploymentMBean var0, String var1, DeploymentState var2) {
      if (var0 instanceof AppDeploymentMBean) {
         return new ModuleTransitionTracker((AppDeploymentMBean)var0, var1, var2);
      } else if (var0 instanceof SystemResourceMBean) {
         return new ModuleStateTracker(var2, (SystemResourceMBean)var0);
      } else {
         throw new AssertionError("Unknown type " + var0.getType());
      }
   }
}
