package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class BaseWorkContextFlow extends BaseFlow implements Flow {
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");

   public BaseWorkContextFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   protected void setBindApplicationIdCtx(String var1) {
      AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** " + var1 + " " + (var2 == null ? "" : var2.getApplicationIdentifier()));
      }

      if (var2 != null && var2.getVersionIdentifier() != null) {
         ApplicationVersionUtils.setBindApplicationId(var2.getApplicationIdentifier());
         ApplicationVersionUtils.setCurrentVersionId(var2.getApplicationName(), var2.getVersionIdentifier());
      }

   }

   protected void unsetBindApplicationIdCtx(String var1) {
      AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
      if (DEBUG_APP_VERSION.isEnabled()) {
         Debug.say("*** " + var1 + " " + (var2 == null ? "" : var2.getApplicationIdentifier()));
      }

      if (var2 != null && var2.getVersionIdentifier() != null) {
         ApplicationVersionUtils.unsetBindApplicationId();
         ApplicationVersionUtils.unsetCurrentVersionId(var2.getApplicationName());
      }

   }
}
