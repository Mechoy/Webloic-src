package weblogic.application;

import weblogic.management.configuration.TargetMBean;

public class SubModuleListenerCtxImpl extends ModuleListenerCtxImpl implements SubModuleListenerCtx {
   private final String subModuleName;
   private final TargetMBean[] subModuleTargets;

   public SubModuleListenerCtxImpl(String var1, String var2, String var3, TargetMBean var4, String var5, TargetMBean[] var6) {
      super(var1, var2, var4, var3);
      this.subModuleName = var5;
      this.subModuleTargets = var6;
   }

   public String getSubModuleName() {
      return this.subModuleName;
   }

   public TargetMBean[] getSubModuleTargets() {
      return this.subModuleTargets;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("Targets=");
      if (this.subModuleTargets != null) {
         for(int var2 = 0; var2 < this.subModuleTargets.length; ++var2) {
            TargetMBean var3 = this.subModuleTargets[var2];
            var1.append(var3.getType()).append("/").append(var3.getName() + " ");
         }
      } else {
         var1.append("null");
      }

      return (new StringBuffer("SubModuleCtx[appId=")).append(this.getApplicationId()).append(", modId=").append(this.subModuleName + "[" + this.getModuleUri() + "]").append(", type=").append(this.getType()).append(", ").append(var1).append("]").toString();
   }
}
