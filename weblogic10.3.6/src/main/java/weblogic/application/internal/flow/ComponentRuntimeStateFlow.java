package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.internal.Flow;
import weblogic.management.runtime.ComponentRuntimeMBean;

public final class ComponentRuntimeStateFlow extends BaseFlow implements Flow {
   public ComponentRuntimeStateFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   private void setDeploymentState(int var1) {
      this.setDeploymentState(var1, this.appCtx.getApplicationModules());
   }

   private void setDeploymentState(int var1, Module[] var2) {
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            ComponentRuntimeMBean[] var4 = var2[var3].getComponentRuntimeMBeans();
            if (var4 != null) {
               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (var4[var5] != null) {
                     var4[var5].setDeploymentState(var1);
                  }
               }
            }
         }

      }
   }

   public void prepare() {
      this.setDeploymentState(1);
   }

   public void activate() {
      this.setDeploymentState(2);
   }

   public void deactivate() {
      this.setDeploymentState(1);
   }

   public void unprepare() {
      this.setDeploymentState(0);
   }

   public void prepareUpdate(String[] var1) {
      this.setDeploymentState(4);
   }

   public void activateUpdate(String[] var1) {
      this.setDeploymentState(2);
   }

   public void rollbackUpdate(String[] var1) {
      this.setDeploymentState(2);
   }
}
