package weblogic.deploy.api.tools.deployer;

import java.util.Arrays;
import java.util.Comparator;
import weblogic.deploy.utils.MBeanHomeTool;

public class PurgeTasksOperation extends TaskOperation {
   private String[] retiredTaskIds = null;

   public PurgeTasksOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void setAllowedOptions() {
   }

   public void execute() throws Exception {
      try {
         this.retiredTaskIds = this.helper.getDeployer().purgeRetiredTasks();
      } catch (Throwable var3) {
         String var2 = cat.errorGettingDeployerRuntime();
         throw new RuntimeException(var2, var3);
      }
   }

   public int report() {
      StringBuffer var1 = new StringBuffer();
      if (this.retiredTaskIds != null && this.retiredTaskIds.length != 0) {
         Arrays.sort(this.retiredTaskIds, new Comparator() {
            public int compare(Object var1, Object var2) {
               String var3 = (String)var1;
               String var4 = (String)var2;
               return var3.compareTo(var4);
            }
         });

         for(int var2 = 0; var2 < this.retiredTaskIds.length; ++var2) {
            if (var2 > 0) {
               var1.append(", ");
            }

            var1.append(this.retiredTaskIds[var2]);
         }

         System.out.println(cat.showRetiredTasks(var1.toString()));
      } else {
         System.out.println(cat.showNoRetiredTasks());
      }

      return 0;
   }

   public String getOperation() {
      return "purgetasks";
   }
}
