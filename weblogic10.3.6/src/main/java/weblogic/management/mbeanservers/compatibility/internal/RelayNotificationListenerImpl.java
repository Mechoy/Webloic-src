package weblogic.management.mbeanservers.compatibility.internal;

import javax.management.NotificationListener;

class RelayNotificationListenerImpl extends BaseNotificationListenerImpl implements RelayNotificationListener {
   RelayNotificationListenerImpl(MBeanProxy var1, NotificationListener var2) {
      super(var1, var2);
   }
}
