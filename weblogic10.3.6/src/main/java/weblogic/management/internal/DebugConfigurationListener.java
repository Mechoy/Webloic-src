package weblogic.management.internal;

import javax.management.Notification;
import weblogic.management.RemoteNotificationListener;
import weblogic.utils.Debug;

public final class DebugConfigurationListener implements RemoteNotificationListener {
   public void handleNotification(Notification var1, Object var2) {
      if (var1 instanceof AttributeChangeNotification) {
         AttributeChangeNotification var3 = (AttributeChangeNotification)var1;
         String var4 = var3.getAttributeName();
         Debug.attributeChangeNotification("weblogic." + var3.getAttributeName(), var3.getOldValue(), var3.getNewValue());
      }

   }
}
