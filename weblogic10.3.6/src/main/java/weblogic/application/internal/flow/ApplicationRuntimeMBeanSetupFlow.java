package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deployment.AbstractPersistenceUnitRegistry;
import weblogic.deployment.EarPersistenceUnitRegistry;
import weblogic.deployment.EnvironmentException;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.DeploymentException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class ApplicationRuntimeMBeanSetupFlow extends BaseFlow {
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");

   public ApplicationRuntimeMBeanSetupFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void activate() throws DeploymentException {
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** activate " + this.appCtx.getApplicationId());
      }

      this.initPersistenceMBean();
      if (ApplicationVersionUtils.getAdminModeAppCtxParam(this.appCtx)) {
         ApplicationVersionUtils.setActiveVersionState(this.appCtx, 1);
      } else {
         if (DEBUG_APP_VERSION.isEnabled()) {
            Debug.say("*** activate " + this.appCtx.getApplicationId() + " does not " + "change active version state");
         }

      }
   }

   private void initPersistenceMBean() throws DeploymentException {
      AbstractPersistenceUnitRegistry var1 = (AbstractPersistenceUnitRegistry)this.appCtx.getUserObject(EarPersistenceUnitRegistry.class);
      if (var1 != null) {
         J2EEApplicationRuntimeMBeanImpl var2 = this.appCtx.getRuntime();
         if (var2 != null) {
            try {
               var1.setParentRuntimeMBean(var2);
            } catch (EnvironmentException var4) {
               throw new DeploymentException(var4);
            }
         }
      }

   }

   public void adminToProduction() throws DeploymentException {
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** adminToProduction " + this.appCtx.getApplicationId());
      }

      ApplicationVersionUtils.setActiveVersionState(this.appCtx, 2);
   }
}
