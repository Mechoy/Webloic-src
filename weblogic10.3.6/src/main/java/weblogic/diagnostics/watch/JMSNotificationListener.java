package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import com.bea.diagnostics.notifications.JMSNotificationCustomizer;
import com.bea.diagnostics.notifications.JMSNotificationService;
import com.bea.diagnostics.notifications.Notification;
import com.bea.diagnostics.notifications.NotificationPropagationException;
import com.bea.diagnostics.notifications.NotificationServiceFactory;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.type.DiagnosticRuntimeException;

final class JMSNotificationListener extends WatchNotificationListenerCommon implements WatchNotificationListener, JMSNotificationCustomizer {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static final String JMS_NOTIFICATION_NAME = "JMSNotificationName";
   private String destinationJNDIName;
   private String connectionFactoryJNDIName;
   private JMSNotificationService jmsService;

   JMSNotificationListener(String var1, String var2) throws NotificationCreateException, InvalidNotificationException {
      this(var1, var2, (String)null);
   }

   JMSNotificationListener(String var1, String var2, String var3) throws NotificationCreateException, InvalidNotificationException {
      super(var1);
      if (var2 == null) {
         throw new NotificationCreateException("JNDI name for JMS Destination must be set and cannot be null");
      } else {
         this.destinationJNDIName = var2;
         if (var3 == null) {
            this.connectionFactoryJNDIName = "weblogic.jms.ConnectionFactory";
         } else {
            this.connectionFactoryJNDIName = var3;
         }

         try {
            this.jmsService = NotificationServiceFactory.getInstance().createJMSNotificationService(this.getNotificationName(), this.destinationJNDIName, this.connectionFactoryJNDIName, this);
         } catch (com.bea.diagnostics.notifications.NotificationCreateException var5) {
            throw new NotificationCreateException(var5);
         }

         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Created JMS notification " + this);
         }

      }
   }

   String getDestinationJNDIName() {
      return this.destinationJNDIName;
   }

   String getConnectionFactoryJNDIName() {
      return this.connectionFactoryJNDIName;
   }

   public synchronized void processWatchNotification(Notification var1) {
      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Handle jms notification for " + this);
            debugLogger.debug("Watch notification: " + var1);
         }

         if (this.jmsService != null) {
            this.jmsService.send(var1);
            this.getWatchRuntime().incrementTotalJMSNotificationsPerformed();
         }

      } catch (Throwable var3) {
         DiagnosticsLogger.logJMSNotificationSendMsgException(var3);
         this.getWatchRuntime().incrementTotalFailedJMSNotifications();
         throw new DiagnosticRuntimeException(var3);
      }
   }

   public String toString() {
      return "JMSNotificationListener " + this.getNotificationName() + " - " + (this.jmsService != null ? this.jmsService.toString() : "");
   }

   public Message createMessage(Session var1, Notification var2) throws NotificationPropagationException {
      try {
         MapMessage var3 = var1.createMapMessage();
         WatchNotification var4 = (WatchNotification)var2;
         var3.setString("WatchTime", var4.getWatchTime());
         var3.setString("WatchDomainName", var4.getWatchDomainName());
         var3.setString("WatchServerName", var4.getWatchServerName());
         var3.setString("WatchSeverityLevel", var4.getWatchSeverityLevel());
         var3.setString("WatchName", var4.getWatchName());
         var3.setString("WatchRuleType", var4.getWatchRuleType());
         var3.setString("WatchRule", var4.getWatchRule());
         var3.setString("WatchAlarmType", var4.getWatchAlarmType());
         var3.setString("WatchAlarmResetPeriod", var4.getWatchAlarmResetPeriod());
         var3.setString("JMSNotificationName", this.getNotificationName());
         var3.setString("WatchData", var4.getWatchDataToString());
         return var3;
      } catch (Exception var5) {
         DiagnosticsLogger.logJMSNotificationCreateMsgException(var5);
         throw new NotificationPropagationException(var5);
      }
   }
}
