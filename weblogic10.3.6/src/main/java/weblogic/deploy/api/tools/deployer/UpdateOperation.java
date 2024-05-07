package weblogic.deploy.api.tools.deployer;

import weblogic.deploy.utils.MBeanHomeTool;

public class UpdateOperation extends Jsr88Operation {
   public UpdateOperation(MBeanHomeTool var1, Options var2) {
      super(var1, var2);
   }

   public void setAllowedOptions() throws IllegalArgumentException {
      super.setAllowedOptions();
      this.allowedOptions.add("upload");
      this.allowedOptions.add("plan");
      this.allowedOptions.add("usenonexclusivelock");
   }

   protected void validateName() throws IllegalArgumentException {
      if (this.options.name == null) {
         throw new IllegalArgumentException(cat.errorMissingName());
      }
   }

   protected boolean isSourceRequired() {
      return false;
   }

   protected boolean isPlanRequired() {
      return true;
   }

   public void execute() throws Exception {
      this.po = this.dm.update(this.getTmids(), this.plan, this.dOpts);
      this.postExecute();
   }

   public String getOperation() {
      return "update";
   }
}
