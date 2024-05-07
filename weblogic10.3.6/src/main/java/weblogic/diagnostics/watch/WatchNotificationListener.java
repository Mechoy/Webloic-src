package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.Notification;

interface WatchNotificationListener {
   String getNotificationName();

   boolean isEnabled();

   boolean isDisabled();

   void setEnabled();

   void setDisabled();

   void processWatchNotification(Notification var1);
}
