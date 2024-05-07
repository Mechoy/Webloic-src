package weblogic.deploy.api.tools.deployer;

import java.io.Serializable;
import weblogic.deploy.utils.MBeanHomeTool;

public class ActivateOperation extends DeployOperation implements Serializable {
   private static final long serialVersionUID = 1L;

   public ActivateOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void execute() throws Exception {
      this.po = this.dm.activate(this.getTmids(), this.src, this.plan, this.dOpts);
      this.postExecute();
   }

   public String getOperation() {
      return "activate";
   }
}
