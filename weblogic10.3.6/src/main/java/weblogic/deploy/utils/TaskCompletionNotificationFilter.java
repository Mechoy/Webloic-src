package weblogic.deploy.utils;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;

public class TaskCompletionNotificationFilter implements NotificationFilter {
   private static final Object FAILED = new Integer(3);
   private static final Object COMPLETED = new Integer(2);
   private static final Object DEFERRED = new Integer(4);

   public boolean isNotificationEnabled(Notification var1) {
      if (!(var1 instanceof AttributeChangeNotification)) {
         return false;
      } else {
         AttributeChangeNotification var2 = (AttributeChangeNotification)var1;
         if (!"State".equals(var2.getAttributeName())) {
            return false;
         } else {
            Object var3 = var2.getNewValue();
            return var3.equals(FAILED) || var3.equals(COMPLETED) || var3.equals(DEFERRED);
         }
      }
   }
}
