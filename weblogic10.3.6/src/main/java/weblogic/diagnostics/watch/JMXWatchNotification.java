package weblogic.diagnostics.watch;

import java.io.Serializable;
import javax.management.Notification;

public final class JMXWatchNotification extends Notification implements Serializable {
   static final long serialVersionUID = -7648375988167013033L;
   public static final String GLOBAL_JMX_NOTIFICATION_PRODUCER_NAME = "DiagnosticsJMXNotificationSource";
   private WatchNotification watchNotification = null;

   JMXWatchNotification(String var1, Object var2, long var3, long var5, String var7, WatchNotification var8) {
      super(var1, var2, var3, var5, var7);
      this.watchNotification = var8;
   }

   public WatchNotification getExtendedInfo() {
      return this.watchNotification;
   }
}
