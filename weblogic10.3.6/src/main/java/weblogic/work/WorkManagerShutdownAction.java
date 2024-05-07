package weblogic.work;

import weblogic.j2ee.descriptor.wl.WorkManagerShutdownTriggerBean;
import weblogic.management.configuration.WorkManagerShutdownTriggerMBean;

final class WorkManagerShutdownAction extends AbstractStuckThreadAction {
   private WorkManagerService wmService;

   public WorkManagerShutdownAction(WorkManagerShutdownTriggerBean var1) {
      super((long)var1.getMaxStuckThreadTime(), var1.getStuckThreadCount());
   }

   public WorkManagerShutdownAction(WorkManagerShutdownTriggerMBean var1) {
      super((long)var1.getMaxStuckThreadTime(), var1.getStuckThreadCount());
   }

   public void execute() {
      this.wmService.forceShutdown();
   }

   public void withdraw() {
      this.wmService.start();
   }

   public void setWorkManagerService(WorkManagerService var1) {
      this.wmService = var1;
   }
}
