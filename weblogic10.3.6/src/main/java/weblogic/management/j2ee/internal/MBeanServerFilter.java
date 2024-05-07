package weblogic.management.j2ee.internal;

import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.ObjectName;

class MBeanServerFilter implements NotificationFilter {
   public boolean isNotificationEnabled(Notification var1) {
      if (var1 instanceof MBeanServerNotification) {
         MBeanServerNotification var2 = (MBeanServerNotification)var1;
         ObjectName var3 = var2.getMBeanName();
         boolean var4 = Types.isValidWLSType(var3.getKeyProperty("Type"));
         return var4;
      } else {
         return false;
      }
   }
}
