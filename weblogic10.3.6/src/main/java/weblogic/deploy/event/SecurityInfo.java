package weblogic.deploy.event;

import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.configuration.AppDeploymentMBean;

public final class SecurityInfo {
   private final BaseDeploymentEvent evt;
   private AppDeploymentMBean srcMBean;

   SecurityInfo(BaseDeploymentEvent var1) {
      this.evt = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName()).append("{").append("SourceMBean=").append(this.getSource()).append("}");
      return var1.toString();
   }

   public AppDeploymentMBean getSource() {
      if (this.srcMBean == null) {
         AppDeploymentMBean var1 = this.evt.getAppDeployment();
         if (var1 == null) {
            return null;
         }

         this.srcMBean = ApplicationVersionUtils.getActiveAppDeployment(var1.getApplicationName());
      }

      return this.srcMBean;
   }

   public String getRealm() {
      return "weblogicDEFAULT";
   }
}
