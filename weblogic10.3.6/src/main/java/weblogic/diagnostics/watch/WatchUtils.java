package weblogic.diagnostics.watch;

import java.security.AccessController;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import weblogic.diagnostics.descriptor.WLDFNotificationBean;
import weblogic.diagnostics.query.VariableInstance;
import weblogic.logging.SeverityI18N;
import weblogic.management.WebLogicMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

final class WatchUtils {
   static final String IMAGE_NOTIFICATION_TYPE = "WLDFImageNotification";
   static final String JMX_NOTIFICATION_TYPE = "WLDFJMXNotification";
   static final String JMS_NOTIFICATION_TYPE = "WLDFJMSNotification";
   static final String SNMP_NOTIFICATION_TYPE = "WLDFSNMPNotification";
   static final String SMTP_NOTIFICATION_TYPE = "WLDFSMTPNotification";
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static String domainName = null;
   private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(2, 0, Locale.getDefault());

   public static int convertRuleTypeToInt(String var0) {
      byte var1 = 2;
      if (var0 != null) {
         if (var0.equals("Harvester")) {
            var1 = 2;
         } else if (var0.equals("Log")) {
            var1 = 1;
         } else if (var0.equals("EventData")) {
            var1 = 3;
         }
      }

      return var1;
   }

   public static int convertAlarmResetTypeToInt(String var0) {
      byte var1 = 1;
      if (var0 != null) {
         if (var0.equals("ManualReset")) {
            var1 = 1;
         } else if (var0.equals("AutomaticReset")) {
            var1 = 2;
         } else {
            var1 = 0;
         }
      }

      return var1;
   }

   public static String[] getNotificationNames(WLDFNotificationBean[] var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.add(var0[var2].getName());
         }
      }

      String[] var3 = new String[var1.size()];
      var1.toArray(var3);
      return var3;
   }

   public static WLDFNotificationBean[] getNotificationsOfType(WLDFNotificationBean[] var0, String var1) {
      ArrayList var2 = new ArrayList();
      if (var0 != null) {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            if (((WebLogicMBean)var0[var3]).getType().equals(var1)) {
               var2.add(var0[var3]);
            }
         }
      }

      WLDFNotificationBean[] var4 = new WLDFNotificationBean[var2.size()];
      var2.toArray(var4);
      return var4;
   }

   static String getCurrentDomainName() {
      if (domainName != null) {
         return domainName;
      } else {
         RuntimeAccess var0 = ManagementService.getRuntimeAccess(KERNEL_ID);
         if (var0 != null) {
            domainName = var0.getDomainName();
         }

         return domainName;
      }
   }

   static String getToAddresses(String[] var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(var0[var2]);
         if (var2 < var0.length - 1) {
            var1.append(',');
         }
      }

      return var1.toString();
   }

   static WatchNotification setWatchTimeInDateFormat(WatchNotification var0, long var1) {
      var0.setWatchTime(DATE_FORMAT.format(new Date(var1)));
      return var0;
   }

   static WatchNotification populateFromWatch(WatchNotification var0, Watch var1) throws NotificationCreateException {
      String var2 = "WatchName: " + (var1 != null ? var1.getWatchName() : "") + " " + "WatchSeverityLevel" + ": " + SeverityI18N.severityNumToString(var1 != null ? var1.getSeverity() : 0);
      var0.setMessage(var2);
      var0.setWatchName(var1.getWatchName());
      switch (var1.getRuleType()) {
         case 1:
            var0.setWatchRuleType("Log");
            break;
         case 2:
            var0.setWatchRuleType("Harvester");
            break;
         case 3:
            var0.setWatchRuleType("EventData");
            break;
         default:
            throw new NotificationCreateException("Invalid rule type");
      }

      var0.setWatchRule(var1.getRuleExpression());
      var0.setWatchSeverityLevel(SeverityI18N.severityNumToString(var1.getSeverity()));
      var0.setWatchAlarmResetPeriod("" + var1.getAlarmResetPeriod());
      if (!var1.hasAlarm()) {
         var0.setWatchAlarmType("None");
      } else if (var1.hasManualResetAlarm()) {
         var0.setWatchAlarmType("ManualReset");
      } else {
         if (!var1.hasAutomaticResetAlarm()) {
            throw new NotificationCreateException("Invalid alarm type");
         }

         var0.setWatchAlarmType("AutomaticReset");
      }

      var0.setWatchDomainName(WatchUtils.WatchDataStaticInitializer.DOMAIN_NAME);
      var0.setWatchServerName(WatchUtils.WatchDataStaticInitializer.SERVER_NAME);
      return var0;
   }

   static String getWatchDataString(Map var0) {
      StringBuilder var1 = new StringBuilder(128);
      if (var0 != null) {
         Iterator var2 = var0.entrySet().iterator();

         while(true) {
            while(true) {
               Object var4;
               Object var5;
               do {
                  if (!var2.hasNext()) {
                     return var1.toString();
                  }

                  Map.Entry var3 = (Map.Entry)var2.next();
                  var4 = var3.getKey();
                  var5 = var3.getValue();
               } while(var5 == null);

               if (var5.getClass().isArray()) {
                  Object[] var6 = (Object[])((Object[])var5);

                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     appendVariableInstance(var1, var4, var6[var7]);
                  }
               } else {
                  appendVariableInstance(var1, var4, var5);
               }
            }
         }
      } else {
         return var1.toString();
      }
   }

   private static void appendVariableInstance(StringBuilder var0, Object var1, Object var2) {
      if (var2 != null) {
         if (var2 instanceof VariableInstance) {
            VariableInstance var3 = (VariableInstance)var2;
            if (var3.getInstanceValue() == null) {
               return;
            }

            var0.append(var3.getInstanceName());
            var0.append("//");
            var0.append(var3.getAttributeName());
            var0.append(" = ");
            var0.append(var3.getInstanceValue());
         } else {
            var0.append(var1);
            var0.append(" = ");
            var0.append(var2);
         }

         var0.append(" ");
      }
   }

   private static class WatchDataStaticInitializer {
      private static final String DOMAIN_NAME;
      private static final String SERVER_NAME;

      static {
         DOMAIN_NAME = ManagementService.getRuntimeAccess(WatchUtils.KERNEL_ID).getDomainName();
         SERVER_NAME = ManagementService.getRuntimeAccess(WatchUtils.KERNEL_ID).getServerName();
      }
   }
}
