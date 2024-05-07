package weblogic.deploy.api.tools.deployer;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.internal.DeployerTextFormatter;

public class SubModuleTargetInfo extends ModuleTargetInfo {
   private String submodule = null;
   protected static DeployerTextFormatter cat = new DeployerTextFormatter();

   public SubModuleTargetInfo(String var1) {
      int var2 = var1.indexOf("@");
      if (var2 == -1) {
         throw new IllegalArgumentException(cat.invalidTargetSyntax(var1));
      } else {
         this.setSubmodule(var1.substring(0, var2));
         this.init(var1.substring(var2 + 1));
      }
   }

   public String getSubmodule() {
      return this.submodule;
   }

   public void setSubmodule(String var1) {
      this.submodule = var1;
   }

   public TargetModuleID createTmid(String var1, Target var2, WebLogicDeploymentManager var3) {
      Object var5 = null;
      Object var4 = super.createTmid(var1, var2, var3);
      TargetModuleID[] var6 = ((TargetModuleID)var4).getChildTargetModuleID();
      if (var6 != null) {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            TargetModuleID var8 = var6[var7];
            if (var8.getModuleID().equals(this.getModule())) {
               var5 = var8;
            }
         }

         if (var5 == null) {
            throw new AssertionError("Failed to build tmids");
         }
      } else {
         var5 = var4;
      }

      if (this.getSubmodule() != null) {
         var4 = var3.createTargetModuleID((TargetModuleID)var5, (String)this.getSubmodule(), (ModuleType)WebLogicModuleType.SUBMODULE);
      }

      return (TargetModuleID)var4;
   }
}
