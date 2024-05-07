package weblogic.deploy.api.tools.deployer;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.WebLogicTargetModuleID;
import weblogic.deploy.internal.DeployerTextFormatter;

public class ModuleTargetInfo {
   private String target = null;
   private String module = null;
   protected static DeployerTextFormatter cat = new DeployerTextFormatter();

   protected ModuleTargetInfo() {
   }

   public ModuleTargetInfo(String var1) {
      this.init(var1);
   }

   protected void init(String var1) {
      int var2 = var1.indexOf("@");
      if (var2 != -1) {
         if (var1.length() == var2 + 1) {
            throw new IllegalArgumentException(cat.invalidTargetSyntax(var1));
         }

         this.setModule(var1.substring(0, var2));
         this.setTarget(var1.substring(var2 + 1));
      } else {
         this.setTarget(var1);
      }

   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String var1) {
      this.target = var1;
   }

   public String getModule() {
      return this.module;
   }

   public void setModule(String var1) {
      this.module = var1;
   }

   public TargetModuleID createTmid(String var1, Target var2, WebLogicDeploymentManager var3) {
      Object var4 = WebLogicModuleType.UNKNOWN;
      if (this.getModule() != null) {
         var4 = ModuleType.EAR;
      }

      WebLogicTargetModuleID var5 = var3.createTargetModuleID((String)var1, (ModuleType)var4, (Target)var2);
      if (this.getModule() != null) {
         var5 = var3.createTargetModuleID((TargetModuleID)var5, (String)this.getModule(), (ModuleType)WebLogicModuleType.UNKNOWN);
      }

      return var5;
   }
}
