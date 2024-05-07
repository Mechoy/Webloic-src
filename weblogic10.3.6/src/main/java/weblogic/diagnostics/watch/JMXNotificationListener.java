package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import com.bea.diagnostics.notifications.JMXNotificationCustomizer;
import com.bea.diagnostics.notifications.JMXNotificationService;
import com.bea.diagnostics.notifications.Notification;
import com.bea.diagnostics.notifications.NotificationRuntimeException;
import com.bea.diagnostics.notifications.NotificationServiceFactory;
import java.util.Date;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.DiagnosticRuntimeException;
import weblogic.management.ManagementException;
import weblogic.management.jmx.modelmbean.NotificationGenerator;

public final class JMXNotificationListener extends WatchNotificationListenerCommon implements WatchNotificationListener, JMXNotificationCustomizer {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private String notificationType;
   private NotificationGenerator notificationGenerator;
   private JMXNotificationProducer notificationProducer;
   private JMXNotificationService jmxService;

   JMXNotificationListener(String var1, JMXNotificationProducer var2) throws ManagementException, InvalidNotificationException, NotificationCreateException {
      this(var1, (String)null, var2);
   }

   JMXNotificationListener(String var1, String var2, JMXNotificationProducer var3) throws ManagementException, InvalidNotificationException, NotificationCreateException {
      super(var1);
      this.notificationProducer = null;
      if (var3 == null) {
         throw new InvalidNotificationException("Producer must be set and cannot be null");
      } else {
         this.notificationProducer = var3;
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Created JMX notification " + this);
         }

         try {
            this.jmxService = NotificationServiceFactory.getInstance().createJMXNotificationService(var1, var3, var2, this);
         } catch (com.bea.diagnostics.notifications.NotificationCreateException var5) {
            throw new NotificationRuntimeException(var5);
         }

         this.setNotificationType(var2);
      }
   }

   public void processWatchNotification(Notification var1) {
      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Handle JMX notification for " + this);
            debugLogger.debug("Watch notification: " + var1);
         }

         if (this.jmxService != null) {
            this.jmxService.send(var1);
            this.getWatchRuntime().incrementTotalJMXNotificationsPerformed();
         }

      } catch (Throwable var3) {
         this.getWatchRuntime().incrementTotalFailedJMXNotifications();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("JMX send notification failed with exception ", var3);
         }

         DiagnosticsLogger.logErrorInNotification(var3);
         throw new DiagnosticRuntimeException(var3);
      }
   }

   void setNotificationGenerator(NotificationGenerator var1) {
      this.notificationGenerator = var1;
   }

   public String toString() {
      return "JMXNotificationListener - notification runtime mbean name: " + this.notificationName;
   }

   public void setNotificationType(String var1) {
      this.notificationType = var1;
      this.jmxService.setNotificationType(var1);
   }

   public String getNotificationType() {
      return this.notificationType;
   }

   public javax.management.Notification createJMXNotification(Notification var1) {
      JMXWatchNotification var2 = null;
      if (var1 instanceof WatchNotificationInternal) {
         WatchNotificationInternal var3 = (WatchNotificationInternal)var1;

         try {
            WatchNotification var4 = var3.createWatchNotificationExternal();
            var2 = new JMXWatchNotification(this.getNotificationType(), var4.getWatchName(), 0L, (new Date()).getTime(), var4.getMessage(), var4);
         } catch (Exception var5) {
            throw new NotificationRuntimeException(var5);
         }
      }

      return var2;
   }
}
