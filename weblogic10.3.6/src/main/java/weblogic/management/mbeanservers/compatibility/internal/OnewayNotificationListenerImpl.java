package weblogic.management.mbeanservers.compatibility.internal;

import javax.management.NotificationListener;

class OnewayNotificationListenerImpl extends BaseNotificationListenerImpl implements OnewayNotificationListener {
   OnewayNotificationListenerImpl(MBeanProxy var1, NotificationListener var2) {
      super(var1, var2);
   }
}
