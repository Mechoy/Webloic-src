package weblogic.deploy.api.tools.deployer;

import java.util.Arrays;
import java.util.Comparator;
import weblogic.deploy.utils.MBeanHomeTool;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;

public class ListTaskOperation extends TaskOperation {
   private DeploymentTaskRuntimeMBean[] tasks = null;

   public ListTaskOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void setAllowedOptions() {
      this.allowedOptions.add("id");
   }

   public void execute() throws Exception {
      if (this.options.id != null) {
         this.task = this.helper.getTaskByID(this.options.id);
         if (this.task == null) {
            throw new DeployerException(cat.errorTaskNotFound(this.options.id));
         }
      } else {
         this.tasks = this.helper.getAllTasks();
         Arrays.sort(this.tasks, new Comparator() {
            public int compare(Object var1, Object var2) {
               DeploymentTaskRuntimeMBean var3 = (DeploymentTaskRuntimeMBean)var1;
               DeploymentTaskRuntimeMBean var4 = (DeploymentTaskRuntimeMBean)var2;
               return var3.getId().compareTo(var4.getId());
            }
         });
      }

   }

   public int report() {
      if (this.tasks != null) {
         for(int var1 = 0; var1 < this.tasks.length; ++var1) {
            this.showTaskInformation(this.tasks[var1]);
         }
      } else if (this.task != null) {
         if (this.options.formatted) {
            this.showTaskInformationHeader();
         }

         this.showTaskInformation(this.task);
      }

      return 0;
   }

   public String getOperation() {
      return this.options.listOp ? "list" : "listtask";
   }
}
