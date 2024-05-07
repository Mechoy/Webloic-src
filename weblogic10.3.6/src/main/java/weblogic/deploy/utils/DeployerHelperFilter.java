package weblogic.deploy.utils;

import javax.management.Notification;
import javax.management.NotificationFilter;
import weblogic.management.DeploymentNotification;

class DeployerHelperFilter implements NotificationFilter {
   public boolean isNotificationEnabled(Notification var1) {
      return var1 instanceof DeploymentNotification;
   }
}
