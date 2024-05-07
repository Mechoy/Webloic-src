package weblogic.application;

import weblogic.management.configuration.TargetMBean;

public class ModuleListenerCtxImpl implements ModuleListenerCtx {
   private final String appId;
   private final String moduleUri;
   private final TargetMBean target;
   private final String type;

   public ModuleListenerCtxImpl(String var1, String var2, TargetMBean var3, String var4) {
      this.appId = var1;
      this.moduleUri = var2;
      this.target = var3;
      this.type = var4;
   }

   public String getApplicationId() {
      return this.appId;
   }

   public String getModuleUri() {
      return this.moduleUri;
   }

   public TargetMBean getTarget() {
      return this.target;
   }

   public String getType() {
      return this.type;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("Target=");
      if (this.target != null) {
         var1.append(this.target.getType()).append("/").append(this.target.getName());
      } else {
         var1.append("null");
      }

      return (new StringBuffer("ModuleCtx[appId=")).append(this.appId).append(", modId=").append(this.moduleUri).append(", type=").append(this.type).append(", ").append(var1).append("]").toString();
   }
}
