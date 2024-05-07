package weblogic.application.internal.flow;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;

public final class StateFlow extends BaseFlow implements Flow {
   public StateFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void activate() {
      this.appCtx.setAdminState(true);
   }

   public void deactivate() {
      this.appCtx.setAdminState(false);
   }

   public void adminToProduction() {
      this.appCtx.setAdminState(false);
   }

   public void forceProductionToAdmin(AdminModeCompletionBarrier var1) {
      this.appCtx.setAdminState(true);
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
      this.appCtx.setAdminState(true);
   }
}
