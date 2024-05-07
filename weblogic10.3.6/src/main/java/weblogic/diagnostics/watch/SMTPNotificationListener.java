package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import com.bea.diagnostics.notifications.Notification;
import com.bea.diagnostics.notifications.NotificationServiceFactory;
import com.bea.diagnostics.notifications.SMTPNotificationCustomizer;
import com.bea.diagnostics.notifications.SMTPNotificationService;
import javax.mail.Session;
import javax.naming.InitialContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.diagnostics.notifications.i18n.NotificationsTextTextFormatter;

final class SMTPNotificationListener extends WatchNotificationListenerCommon implements WatchNotificationListener, SMTPNotificationCustomizer {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static final String SMTP_NOTIFICATION_NAME = "SMTPNotificationName";
   private String[] smtpRecipients;
   private String smtpSubject;
   private String smtpBody;
   private String mailSessionJNDIName;
   private Session mailSession;
   private SMTPNotificationService smtpService;

   SMTPNotificationListener(String var1, String var2, String[] var3) throws NotificationCreateException, InvalidNotificationException {
      this(var1, var2, var3, (String)null, (String)null);
   }

   SMTPNotificationListener(String var1, String var2, String[] var3, String var4, String var5) throws NotificationCreateException {
      super(var1);
      if (var3 == null) {
         throw new InvalidNotificationException("Email destination must be set and cannot be null");
      } else if (var2 == null) {
         throw new InvalidNotificationException("Mail Session JNDI Name must be set and cannot be null");
      } else {
         this.smtpRecipients = var3;
         this.smtpSubject = var4;
         this.smtpBody = var5;
         this.mailSessionJNDIName = var2;
         this.mailSession = null;
         this.initializeSMTP();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Created smtp notification " + this);
         }

      }
   }

   String[] getSMTPRecipients() {
      return this.smtpRecipients;
   }

   String getSMTPSubject() {
      return this.smtpSubject;
   }

   String getSMTPBody() {
      return this.smtpBody;
   }

   String getMailSessionJNDIName() {
      return this.mailSessionJNDIName;
   }

   void setSMTPRecipients(String[] var1) {
      this.smtpRecipients = var1;
      if (this.smtpService != null) {
         String var2 = WatchUtils.getToAddresses(this.smtpRecipients);
         this.smtpService.setSMTPRecipients(var2);
      }

   }

   void setSMTPSubject(String var1) {
      this.smtpSubject = var1;

      try {
         this.initializeSMTP();
      } catch (NotificationCreateException var3) {
      }

   }

   void setSMTPBody(String var1) {
      this.smtpBody = var1;

      try {
         this.initializeSMTP();
      } catch (NotificationCreateException var3) {
      }

   }

   void setMailSessionJNDIName(String var1) {
      this.mailSessionJNDIName = var1;

      try {
         this.initializeSMTP();
      } catch (NotificationCreateException var3) {
      }

   }

   public synchronized void processWatchNotification(Notification var1) {
      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Handle smtp notification for " + this);
            debugLogger.debug("Watch notification: " + var1);
         }

         this.getSMTPService().send(var1);
         this.getWatchRuntime().incrementTotalSMTPNotificationsPerformed();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("SMTP send of message suceeded.");
         }
      } catch (Exception var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("SMTP create of message or send failed with exception ", var3);
         }

         this.getWatchRuntime().incrementTotalFailedSMTPNotifications();
         DiagnosticsLogger.logMessagingExceptionInNotification(var3);
      }

   }

   public String toString() {
      return "SMTPNotificationListener - email recipients: " + this.smtpRecipients + " subject: " + this.smtpSubject + " body: " + this.smtpBody + " mailSession: " + this.mailSession + " ndi name: " + this.mailSessionJNDIName;
   }

   private String getDefaultSubject(WatchNotification var1) {
      DiagnosticsTextTextFormatter var2 = DiagnosticsTextTextFormatter.getInstance();
      String var3 = var2.getSMTPDefaultSubject(var1.getWatchName(), var1.getWatchSeverityLevel(), var1.getWatchServerName(), var1.getWatchTime());
      return var3;
   }

   private String getDefaultBody(WatchNotification var1) {
      NotificationsTextTextFormatter var2 = NotificationsTextTextFormatter.getInstance();
      StringBuffer var3 = new StringBuffer();
      synchronized(var3) {
         var3.append(var2.getSMTPDefaultBodyLine("WatchTime", var1.getWatchTime()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchDomainName", var1.getWatchDomainName()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchServerName", var1.getWatchServerName()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchSeverityLevel", var1.getWatchSeverityLevel()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchName", var1.getWatchName()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchRuleType", var1.getWatchRuleType()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchRule", var1.getWatchRule()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchData", var1.getWatchDataToString()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchAlarmType", var1.getWatchAlarmType()));
         var3.append(var2.getSMTPDefaultBodyLine("WatchAlarmResetPeriod", var1.getWatchAlarmResetPeriod()));
         var3.append(var2.getSMTPDefaultBodyLine("SMTPNotificationName", this.getNotificationName()));
      }

      return var3.toString();
   }

   private synchronized void initializeSMTP() throws NotificationCreateException, InvalidNotificationException {
      try {
         InitialContext var1 = new InitialContext();
         this.mailSession = (Session)var1.lookup(this.mailSessionJNDIName);
      } catch (Exception var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Mail session lookup failed with exception ", var2);
         }

         DiagnosticsLogger.logErrorInMailNotification(var2);
         throw new NotificationCreateException(var2);
      }

      if (this.smtpRecipients != null) {
         String var3 = WatchUtils.getToAddresses(this.smtpRecipients);
         this.getSMTPService().setSMTPRecipients(var3);
      }
   }

   private SMTPNotificationService getSMTPService() throws NotificationCreateException {
      if (this.smtpService == null) {
         try {
            this.smtpService = NotificationServiceFactory.getInstance().createSMTPNotificationService(this.getNotificationName(), this.mailSession, (String)null, this.smtpSubject, this.smtpBody, this);
         } catch (com.bea.diagnostics.notifications.NotificationCreateException var2) {
            throw new NotificationCreateException(var2);
         }
      }

      return this.smtpService;
   }

   public String getSubject(Notification var1) {
      return this.getDefaultSubject((WatchNotification)var1);
   }

   public String getBody(Notification var1) {
      return this.getDefaultBody((WatchNotification)var1);
   }
}
