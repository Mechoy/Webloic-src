package weblogic.j2ee;

import java.io.Serializable;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import weblogic.management.logging.WebLogicLogNotification;

public final class DeploymentNotificationFilter extends NotificationFilterSupport implements Serializable {
   private static final long serialVersionUID = 9189305265523348803L;

   public boolean isNotificationEnabled(Notification var1) {
      if (var1 instanceof WebLogicLogNotification) {
         WebLogicLogNotification var2 = (WebLogicLogNotification)var1;
         String var3 = var2.getSubsystem();
         return "J2EE".equals(var3);
      } else {
         return false;
      }
   }
}
