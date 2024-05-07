package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import com.bea.diagnostics.notifications.JMXNotificationProducerMBean;
import javax.management.Notification;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.DiagnosticRuntimeException;
import weblogic.management.ManagementException;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WLDFWatchJMXNotificationRuntimeMBean;

public final class JMXNotificationProducer extends RuntimeMBeanDelegate implements WLDFWatchJMXNotificationRuntimeMBean, JMXNotificationProducerMBean {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static long sequenceNumber = 0L;
   private NotificationGenerator notificationGenerator;
   private static JMXNotificationProducer singleton = null;

   static JMXNotificationProducer getInstance() {
      if (singleton == null) {
         try {
            singleton = new JMXNotificationProducer("DiagnosticsJMXNotificationSource");
            WatchNotificationRuntimeMBeanImpl.getInstance().setWatchJMXNotificationRuntime(singleton);
         } catch (ManagementException var1) {
            throw new RuntimeException(var1);
         }
      }

      return singleton;
   }

   private JMXNotificationProducer(String var1) throws ManagementException, InvalidNotificationException {
      super(var1, WatchNotificationRuntimeMBeanImpl.getInstance());
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created JMX notification producer " + this);
      }

   }

   private void postJMXNotification(Notification var1) {
      if (this.notificationGenerator != null && this.notificationGenerator.isSubscribed()) {
         try {
            this.notificationGenerator.sendNotification(var1);
         } catch (Exception var3) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("JMX send notification failed with exception ", var3);
            }

            DiagnosticsLogger.logErrorInNotification(var3);
            throw new DiagnosticRuntimeException(var3);
         }
      }

   }

   void setNotificationGenerator(NotificationGenerator var1) {
      this.notificationGenerator = var1;
   }

   public String toString() {
      return "JMXNotificationProducer - notification runtime mbean name: " + this.getName();
   }

   public void sendNotification(Notification var1) {
      this.postJMXNotification(var1);
   }

   public long generateSequenceNumber() {
      return (long)(sequenceNumber++);
   }
}
