package weblogic.deploy.api.tools.deployer;

import weblogic.deploy.utils.MBeanHomeTool;

public class UnprepareOperation extends StopOperation {
   public UnprepareOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public String getOperation() {
      return "unprepare";
   }

   protected final void doExecute() throws Exception {
      this.po = this.dm.unprepare(this.getTmids(), this.dOpts);
   }
}
