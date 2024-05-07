package weblogic.management.mbeanservers.internal.utils.typing;

import java.io.Serializable;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;

class MyNotificationFilter implements NotificationFilter, Serializable {
   static final long serialVersionUID = 1L;

   public boolean isNotificationEnabled(Notification var1) {
      MBeanServerNotification var2 = (MBeanServerNotification)var1;
      String var3 = var2.getType();
      return var3.equals("JMX.mbean.registered") || var3.equals("JMX.mbean.unregistered");
   }
}
