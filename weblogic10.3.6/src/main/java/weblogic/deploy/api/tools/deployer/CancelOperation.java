package weblogic.deploy.api.tools.deployer;

import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;

public class CancelOperation extends TaskOperation {
   private DeploymentTaskRuntimeMBean task;

   public CancelOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void setAllowedOptions() {
      this.allowedOptions.add("id");
   }

   public void validate() throws IllegalArgumentException, DeployerException {
      super.validate();
      if (this.options.id == null) {
         throw new IllegalArgumentException(cat.errorMissingId());
      }
   }

   public void execute() throws Exception {
      this.task = this.helper.getTaskByID(this.options.id);
      if (this.task == null) {
         throw new DeployerException(cat.errorTaskNotFound(this.options.id));
      } else {
         this.task.cancel();
      }
   }

   public int report() {
      byte var1 = 0;
      String var2 = this.task.getId();

      while(this.task.isRunning()) {
      }

      int var3 = this.task.getCancelState();
      if (var3 != 0 && var3 != 2 && var3 != 4) {
         System.out.println(cat.cancelSucceeded(var2));
      } else {
         System.out.println(cat.cancelFailed(var2));
         var1 = 1;
      }

      return var1;
   }

   public String getOperation() {
      return "cancel";
   }
}
