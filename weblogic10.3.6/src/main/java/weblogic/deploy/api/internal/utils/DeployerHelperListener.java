package weblogic.deploy.api.internal.utils;

import javax.management.Notification;
import weblogic.management.RemoteNotificationListener;

class DeployerHelperListener implements RemoteNotificationListener {
   JMXDeployerHelper helper = null;

   DeployerHelperListener(JMXDeployerHelper var1) {
      this.helper = var1;
   }

   public void handleNotification(Notification var1, Object var2) {
      this.helper.queueNotification(var1);
   }
}
