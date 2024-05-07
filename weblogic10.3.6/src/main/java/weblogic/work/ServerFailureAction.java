package weblogic.work;

import weblogic.health.HealthMonitorService;
import weblogic.management.configuration.ServerFailureTriggerMBean;

final class ServerFailureAction extends AbstractStuckThreadAction {
   public ServerFailureAction(ServerFailureTriggerMBean var1) {
      super((long)var1.getMaxStuckThreadTime(), var1.getStuckThreadCount());
   }

   public void execute() {
      HealthMonitorService.subsystemFailed("Thread Pool", "Server failed as the number of stuck threads has exceeded the max limit of " + this.maxCount);
   }

   public void withdraw() {
   }
}
