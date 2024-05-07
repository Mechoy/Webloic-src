package weblogic.management.descriptors;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

public class BaseXMLElementMBeanImpl extends XMLElementMBeanDelegate {
   public MBeanNotificationInfo[] getNotificationInfo() {
      return null;
   }

   public void addNotificationListener(NotificationListener var1, NotificationFilter var2, Object var3) throws IllegalArgumentException {
   }

   public void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
   }
}
