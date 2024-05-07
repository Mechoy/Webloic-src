package weblogic.diagnostics.watch;

import com.bea.diagnostics.notifications.InvalidNotificationException;
import com.bea.diagnostics.notifications.Notification;
import com.bea.diagnostics.notifications.NotificationServiceFactory;
import com.bea.diagnostics.notifications.SNMPNotificationCustomizer;
import com.bea.diagnostics.notifications.SNMPNotificationService;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPTrapException;
import weblogic.diagnostics.snmp.agent.SNMPTrapSender;
import weblogic.diagnostics.snmp.agent.SNMPTrapUtil;
import weblogic.diagnostics.type.DiagnosticRuntimeException;

final class SNMPNotificationListener extends WatchNotificationListenerCommon implements WatchNotificationListener, SNMPNotificationCustomizer {
   private static final String WATCH_NOTIFICATION_TRAP_NAME = "wlsWatchNotification";
   private SNMPNotificationService snmpService;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");

   SNMPNotificationListener(String var1) throws InvalidNotificationException, NotificationCreateException {
      super(var1);

      try {
         this.snmpService = NotificationServiceFactory.getInstance().createSNMPotificationService(var1, "wlsWatchNotification", (SNMPAgent)null, (Map)null, this);
      } catch (com.bea.diagnostics.notifications.NotificationCreateException var3) {
         throw new NotificationCreateException(var3);
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created snmp notification " + this);
      }

   }

   public void processWatchNotification(Notification var1) {
      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Handle snmp notification for " + this);
            debugLogger.debug("Watch notification: " + var1);
         }

         if (this.snmpService != null) {
            this.snmpService.send(var1);
            this.getWatchRuntime().incrementTotalSNMPNotificationsPerformed();
         }

      } catch (Throwable var3) {
         this.getWatchRuntime().incrementTotalFailedSNMPNotifications();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("SNMP trap send failed with exception ", var3);
         }

         DiagnosticsLogger.logErrorInSNMPNotification(var3);
         throw new DiagnosticRuntimeException(var3);
      }
   }

   public List processNotification(Notification var1) {
      if (var1 instanceof WatchNotification) {
         WatchNotification var2 = (WatchNotification)var1;
         LinkedList var3 = new LinkedList();
         var3.add(new Object[]{"trapTime", var2.getWatchTime()});
         var3.add(new Object[]{"trapDomainName", var2.getWatchDomainName()});
         var3.add(new Object[]{"trapServerName", var2.getWatchServerName()});
         var3.add(new Object[]{"trapWatchSeverity", var2.getWatchSeverityLevel()});
         var3.add(new Object[]{"trapWatchName", var2.getWatchName()});
         var3.add(new Object[]{"trapWatchRuleType", var2.getWatchRuleType()});
         var3.add(new Object[]{"trapWatchRule", var2.getWatchRule()});
         var3.add(new Object[]{"trapWatchData", var2.getWatchDataToString()});
         var3.add(new Object[]{"trapWatchAlarmType", var2.getWatchAlarmType()});
         var3.add(new Object[]{"trapWatchAlarmResetPeriod", var2.getWatchAlarmResetPeriod()});
         var3.add(new Object[]{"trapWatchSNMPNotificationName", this.getNotificationName()});
         SNMPTrapSender var4 = SNMPTrapUtil.getInstance().getSNMPTrapSender();
         if (var4 != null) {
            try {
               var4.sendTrap("wlsWatchNotification", var3);
            } catch (SNMPTrapException var6) {
               DiagnosticsLogger.logErrorInSNMPNotification(var6);
            }
         }
      }

      return null;
   }
}
