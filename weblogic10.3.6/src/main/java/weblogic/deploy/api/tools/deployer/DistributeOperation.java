package weblogic.deploy.api.tools.deployer;

import weblogic.deploy.utils.MBeanHomeTool;

public class DistributeOperation extends Jsr88Operation {
   public DistributeOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void setAllowedOptions() throws IllegalArgumentException {
      super.setAllowedOptions();
      this.allowedOptions.add("upload");
      this.allowedOptions.add("enableSecurityValidation");
      this.allowedOptions.add("source");
      this.allowedOptions.add("securityModel");
      this.allowedOptions.add("altappdd");
      this.allowedOptions.add("altwlsappdd");
      this.allowedOptions.add("plan");
      this.allowedOptions.add("stage");
      this.allowedOptions.add("nostage");
      this.allowedOptions.add("external_stage");
      this.allowedOptions.add("defaultsubmoduletargets");
      this.allowedOptions.add("nodefaultsubmoduletargets");
      this.allowedOptions.add("noversion");
      this.allowedOptions.add("usenonexclusivelock");
   }

   protected boolean isSourceRequired() {
      return true;
   }

   public void execute() throws Exception {
      this.po = this.dm.distribute(this.getTmids(), this.src, this.plan, this.dOpts);
      this.postExecute();
   }

   public String getOperation() {
      return "distribute";
   }
}
