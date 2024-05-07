package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;

public final class ApplicationRuntimeMBeanFlow extends BaseFlow {
   public ApplicationRuntimeMBeanFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   private J2EEApplicationRuntimeMBeanImpl createRuntime() throws DeploymentException {
      String var1 = ApplicationVersionUtils.replaceDelimiter(this.appCtx.getApplicationId(), '_');

      try {
         AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
         if (var2 != null) {
            return new J2EEApplicationRuntimeMBeanImpl(var1, var2);
         } else {
            SystemResourceMBean var3 = this.appCtx.getSystemResourceMBean();
            if (var3 == null) {
               throw new AssertionError("neither app or System resource?");
            } else {
               return new J2EEApplicationRuntimeMBeanImpl(var1, var3);
            }
         }
      } catch (ManagementException var4) {
         throw new DeploymentException(var4);
      }
   }

   public void prepare() throws DeploymentException {
      this.appCtx.setRuntime(this.createRuntime());
   }

   public void unprepare() throws DeploymentException {
      J2EEApplicationRuntimeMBeanImpl var1 = this.appCtx.getRuntime();

      try {
         var1.unregister();
      } catch (Exception var3) {
         throw new DeploymentException(var3);
      }
   }
}
