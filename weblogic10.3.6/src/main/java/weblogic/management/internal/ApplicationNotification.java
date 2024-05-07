package weblogic.management.internal;

import java.util.Date;
import javax.management.Notification;
import javax.management.ObjectName;

/** @deprecated */
public final class ApplicationNotification extends Notification {
   public static final String LOADED = "weblogic.management.application.loaded";
   public static final String RELOADED = "weblogic.management.application.reloaded";
   public static final String DEPLOYED = "weblogic.management.application.deployed";
   public static final String UNDEPLOYED = "weblogic.management.application.undeployed";
   private static final long serialVersionUID = 154577046124260765L;
   private static long sequenceNumber = 0L;
   private static String appName = null;

   public ApplicationNotification(ObjectName var1, String var2, String var3) {
      super(var3, var1, generateSequenceNumber(), (new Date()).getTime(), "Application  " + var2 + " Reloaded ");
   }

   public static long getChangeNotificationCount() {
      return sequenceNumber;
   }

   public static String getAppName() {
      return appName;
   }

   private static synchronized long generateSequenceNumber() {
      return (long)(sequenceNumber++);
   }
}
