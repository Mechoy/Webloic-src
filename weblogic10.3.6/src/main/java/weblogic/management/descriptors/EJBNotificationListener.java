package weblogic.management.descriptors;

import javax.management.Notification;
import weblogic.management.RemoteNotificationListener;
import weblogic.utils.Debug;

class EJBNotificationListener implements RemoteNotificationListener {
   public void handleNotification(Notification var1, Object var2) {
      Debug.say("@@@ RECEIVED NOTIFICATON " + var1);
      Debug.say("@@@ msg:" + var1.getMessage());
      Debug.say("@@@ source:" + var1.getSource());
      Debug.say("@@@ type:" + var1.getType());
      Debug.say("@@@ userdata:" + var2);
   }
}
