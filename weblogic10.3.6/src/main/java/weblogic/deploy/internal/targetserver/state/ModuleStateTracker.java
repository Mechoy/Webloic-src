package weblogic.deploy.internal.targetserver.state;

import weblogic.application.ModuleListener;
import weblogic.application.ModuleListenerCtx;
import weblogic.management.configuration.SystemResourceMBean;

public class ModuleStateTracker implements ModuleListener {
   private final DeploymentState state;
   private final SystemResourceMBean mbean;

   public ModuleStateTracker(DeploymentState var1, SystemResourceMBean var2) {
      this.state = var1;
      this.mbean = var2;
   }

   public void endTransition(ModuleListenerCtx var1, ModuleListener.State var2, ModuleListener.State var3) {
      if (var1.getApplicationId().equals(this.mbean.getName())) {
         TargetModuleState var4 = this.state.getOrCreateTargetModuleState(var1);
         if (var4 != null) {
            var4.setCurrentState(var3.toString());
         }

      }
   }

   public void beginTransition(ModuleListenerCtx var1, ModuleListener.State var2, ModuleListener.State var3) {
   }

   public void failedTransition(ModuleListenerCtx var1, ModuleListener.State var2, ModuleListener.State var3) {
   }
}
