package weblogic.diagnostics.snmp.server;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.management.Notification;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPNotificationManager;
import weblogic.i18n.logging.SeverityI18N;
import weblogic.management.logging.WebLogicLogNotification;

public class LogFilterListener extends JMXMonitorListener {
   private String name;
   private int severity;
   private Set allowedSubsystems = new HashSet();
   private Set allowedUserIds = new HashSet();
   private Set allowedMsgIds = new HashSet();
   private String messageSubString = "";

   public LogFilterListener(JMXMonitorLifecycle var1, SNMPAgent var2, String var3, String var4, String[] var5, String[] var6, String[] var7, String var8) {
      super(var1, var2);
      this.name = var3;
      this.severity = SeverityI18N.severityStringToNum(var4);
      if (var5 != null) {
         Collections.addAll(this.allowedSubsystems, var5);
      }

      if (var6 != null) {
         Collections.addAll(this.allowedUserIds, var6);
      }

      if (var7 != null) {
         Collections.addAll(this.allowedMsgIds, var7);
      }

      if (var8 != null) {
         this.messageSubString = var8;
      }

   }

   public boolean isNotificationEnabled(Notification var1) {
      if (var1 instanceof WebLogicLogNotification) {
         WebLogicLogNotification var2 = (WebLogicLogNotification)var1;
         if (var2.getSeverity() > this.severity) {
            return false;
         } else {
            String var3;
            if (!this.allowedSubsystems.isEmpty()) {
               var3 = var2.getSubsystem();
               if (!this.allowedSubsystems.contains(var3)) {
                  return false;
               }
            }

            if (!this.allowedUserIds.isEmpty()) {
               var3 = var2.getUserId();
               if (!this.allowedUserIds.contains(var3)) {
                  return false;
               }
            }

            if (!this.allowedMsgIds.isEmpty()) {
               var3 = var2.getMessageIdString();
               if (!this.allowedMsgIds.contains(var3)) {
                  return false;
               }
            }

            if (this.messageSubString != null && this.messageSubString.length() > 0) {
               var3 = var2.getMessage();
               if (!var3.contains(this.messageSubString)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void handleNotification(Notification var1, Object var2) {
      WebLogicLogNotification var3 = (WebLogicLogNotification)var1;
      SNMPNotificationManager var4 = this.snmpAgent.getSNMPAgentToolkit().getSNMPNotificationManager();
      if (var4 == null) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Null notification manager, agent deactivated.");
         }

      } else {
         LinkedList var5 = new LinkedList();
         String var6 = (new Date()).toString();
         var5.add(new Object[]{"trapTime", var6});
         var5.add(new Object[]{"trapServerName", var3.getServername()});
         var5.add(new Object[]{"trapMachineName", var3.getMachineName()});
         var5.add(new Object[]{"trapLogThreadId", var3.getThreadId()});
         var5.add(new Object[]{"trapLogTransactionId", var3.getTransactionId()});
         var5.add(new Object[]{"trapLogUserId", var3.getUserId()});
         var5.add(new Object[]{"trapLogSubsystem", var3.getSubsystem()});
         var5.add(new Object[]{"trapLogMsgId", var3.getId()});
         String var7 = SeverityI18N.severityNumToString(var3.getSeverity());
         var5.add(new Object[]{"trapLogSeverity", var7});
         var5.add(new Object[]{"trapLogMessage", var3.getMessage()});

         try {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Sending log message trap");
            }

            var4.sendNotification(this.snmpAgent.getNotifyGroup(), "wlsLogNotification", var5);
            this.updateMonitorTrapCount();
         } catch (Exception var9) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Exception sending log message trap", var9);
            }
         }

      }
   }

   void updateMonitorTrapCount() {
      if (this.snmpStats != null) {
         this.snmpStats.incrementLogMessageTrapCount();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Updated log message count to " + this.snmpStats.getLogMessageTrapCount());
         }
      }

   }

   String getName() {
      return this.name;
   }
}
