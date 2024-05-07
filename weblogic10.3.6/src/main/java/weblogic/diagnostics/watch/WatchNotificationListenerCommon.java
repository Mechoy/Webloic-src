package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import weblogic.management.ManagementException;

abstract class WatchNotificationListenerCommon {
   protected String notificationName;
   protected boolean notificationEnabled;
   protected long notificationsPerformed;
   private WatchNotificationRuntimeMBeanImpl watchRuntime = null;

   WatchNotificationListenerCommon(String var1) throws InvalidNotificationException, NotificationCreateException {
      if (var1 == null) {
         throw new InvalidNotificationException("Name can not be null");
      } else {
         this.notificationName = var1;

         try {
            this.watchRuntime = (WatchNotificationRuntimeMBeanImpl)WatchManager.getInstance().getWatchNotificationRuntime();
         } catch (ManagementException var3) {
            throw new NotificationCreateException(var3);
         }
      }
   }

   public String getNotificationName() {
      return this.notificationName;
   }

   public boolean isEnabled() {
      return this.notificationEnabled;
   }

   public boolean isDisabled() {
      return !this.notificationEnabled;
   }

   public void setEnabled() {
      this.notificationEnabled = true;
   }

   public void setDisabled() {
      this.notificationEnabled = false;
   }

   protected WatchNotificationRuntimeMBeanImpl getWatchRuntime() {
      return this.watchRuntime;
   }
}
