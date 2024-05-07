package weblogic.application.internal.flow;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.DeploymentException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class ApplicationRuntimeMBeanDeactivationFlow extends BaseFlow {
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");

   public ApplicationRuntimeMBeanDeactivationFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** gracefulProductionToAdmin " + this.appCtx.getApplicationId());
      }

      if (ApplicationVersionUtils.getAdminModeAppCtxParam(this.appCtx)) {
         ApplicationVersionUtils.setActiveVersionState(this.appCtx, 1);
      } else {
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** gracefulProductionToAdmin " + this.appCtx.getApplicationId() + " does not change active version state");
         }

      }
   }

   public void forceProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** forceProductionToAdmin " + this.appCtx.getApplicationId());
      }

      if (ApplicationVersionUtils.getAdminModeAppCtxParam(this.appCtx)) {
         ApplicationVersionUtils.setActiveVersionState(this.appCtx, 1);
      } else {
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** forceProductionToAdmin " + this.appCtx.getApplicationId() + " does not change active version state");
         }

      }
   }

   public void deactivate() throws DeploymentException {
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** deactivate " + this.appCtx.getApplicationId());
      }

      ApplicationVersionUtils.setActiveVersionState(this.appCtx, 0);
   }
}
