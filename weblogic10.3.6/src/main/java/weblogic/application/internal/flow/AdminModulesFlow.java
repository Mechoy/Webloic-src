package weblogic.application.internal.flow;

import weblogic.application.Module;
import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;
import weblogic.utils.ErrorCollectionException;

public final class AdminModulesFlow extends BaseFlow implements Flow {
   private ModuleStateDriver driver;

   public AdminModulesFlow(FlowContext var1) {
      super(var1);
      this.driver = new ModuleStateDriver(this.appCtx);
   }

   public void start(String[] var1) throws DeploymentException {
      if (!this.appCtx.isAdminState()) {
         Module[] var2 = this.appCtx.getStartingModules();

         try {
            this.driver.adminToProduction(var2);
         } catch (Throwable var9) {
            ErrorCollectionException var4 = new ErrorCollectionException(var9);

            try {
               this.driver.deactivate(var2);
            } catch (Throwable var8) {
               var4.add(var8);
            }

            try {
               this.driver.unprepare(var2);
            } catch (Throwable var7) {
               var4.add(var7);
            }

            try {
               this.driver.destroy(var2);
            } catch (Throwable var6) {
               var4.add(var6);
            }

            this.throwAppException(var4);
         }

      }
   }

   public void stop(String[] var1) {
      if (!this.appCtx.isAdminState()) {
         Module[] var2 = this.appCtx.getStoppingModules();

         try {
            this.driver.forceProductionToAdmin(var2);
         } catch (DeploymentException var4) {
            this.log("Ignoring productionToAdmin error ", var4);
         }

      }
   }
}
