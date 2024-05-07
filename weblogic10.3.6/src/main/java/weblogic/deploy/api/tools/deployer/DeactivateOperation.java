package weblogic.deploy.api.tools.deployer;

import weblogic.deploy.utils.MBeanHomeTool;

public class DeactivateOperation extends StopOperation {
   public DeactivateOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void execute() throws Exception {
      this.po = this.dm.deactivate(this.getTmids(), this.dOpts);
      this.postExecute();
   }

   public String getOperation() {
      return "deactivate";
   }
}
