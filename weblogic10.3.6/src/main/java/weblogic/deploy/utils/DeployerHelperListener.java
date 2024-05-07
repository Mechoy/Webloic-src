package weblogic.deploy.utils;

import javax.management.Notification;
import javax.management.NotificationListener;
import weblogic.management.DeploymentNotification;

class DeployerHelperListener implements NotificationListener {
   DeployerHelper helper = null;
   private String taskId;

   DeployerHelperListener(String var1, DeployerHelper var2) {
      this.taskId = var1;
      this.helper = var2;
   }

   public void handleNotification(Notification var1, Object var2) {
      DeploymentNotification var3 = (DeploymentNotification)var1;
      this.helper.showDeploymentNotificationInformation(this.taskId, var3);
   }
}
