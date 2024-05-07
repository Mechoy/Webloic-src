package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.internal.Flow;
import weblogic.management.DeploymentException;
import weblogic.utils.ErrorCollectionException;

public final class StartModulesFlow extends BaseFlow implements Flow {
   private final ModuleStateDriver driver;

   public StartModulesFlow(ApplicationContextInternal var1) {
      super(var1);
      this.driver = new ModuleStateDriver(this.appCtx);
   }

   public void activate() throws DeploymentException {
      this.driver.start(this.appCtx.getApplicationModules());
   }

   public void start(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getStartingModules();

      try {
         this.driver.start(var2);
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
