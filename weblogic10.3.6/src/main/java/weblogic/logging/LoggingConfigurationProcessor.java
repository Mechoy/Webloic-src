package weblogic.logging;

import java.io.File;
import java.io.IOException;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainLogFilterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;
import weblogic.utils.PlatformConstants;

public class LoggingConfigurationProcessor implements ConfigurationProcessor {
   private static String FILE_NAME_ATTR = "FileName";
   private static final boolean DEBUG = false;

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      DomainLogFilterMBean[] var2 = var1.getDomainLogFilters();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         upgradeDomainLogFilter(var1, var2[var3]);
      }

      ServerMBean[] var5 = var1.getServers();

      for(int var4 = 0; var4 < var5.length; ++var4) {
         this.upgradeServerLogConfiguration(var1, var5[var4]);
      }

      LogMBean var6 = var1.getLog();
      if (isLogFileNameDefaulted(var6)) {
         unsetLogFileName(var6);
      }

   }

   public void upgradeServerLogConfiguration(DomainMBean var1, ServerMBean var2) {
      LogMBean var3 = var2.getLog();
      String var4 = getNormalizedStdoutSeverity(var2.isStdoutEnabled(), var2.getStdoutSeverityLevel(), var2.isStdoutDebugEnabled());
      if (var3.getStdoutSeverity() == null || var3.getStdoutSeverity().equals("Notice") && !var4.equals("Notice")) {
         var3.setStdoutSeverity(var4);
      }

      if (var2.isSet("StdoutFormat")) {
         var3.setStdoutFormat(var2.getStdoutFormat());
      }

      if (var2.isSet("StdoutLogStack")) {
         var3.setStdoutLogStack(var2.isStdoutLogStack());
      }

      upgradeDomainLogFilterEnabled(var2);
      upgradeDomainLogFilter(var1, var2);
      if (isLogFileNameDefaulted(var3)) {
         unsetLogFileName(var3);
      }

   }

   private static void unsetLogFileName(LogMBean var0) {
      var0.unSet(FILE_NAME_ATTR);
   }

   private static boolean isLogFileNameDefaulted(LogMBean var0) {
      String var1 = var0.getFileName();
      String var2 = var0.getName() + ".log";
      if (var1.equals(var2)) {
         return true;
      } else {
         File var3 = new File(var0.getFileName());
         if (var3.isAbsolute()) {
            return false;
         } else {
            File var4 = null;
            boolean var5 = var0.getParent() instanceof ServerMBean;
            if (var5) {
               var4 = new File(DomainDir.getRootDir() + PlatformConstants.FILE_SEP + var0.getName());
            } else {
               var4 = new File(DomainDir.getRootDir());
            }

            File var6 = new File(var4, var0.getName() + ".log");

            try {
               boolean var7 = var6.getCanonicalFile().equals(var3.getCanonicalFile());
               return var7;
            } catch (IOException var8) {
               return var6.equals(var3);
            }
         }
      }
   }

   public static void upgradeDomainLogFilter(DomainMBean var0, ServerMBean var1) {
      DomainLogFilterMBean var2 = var1.getDomainLogFilter();
      applyDomainLogFilterToLogMBean(var0, var1.getLog(), var2);
   }

   public static void upgradeDomainLogFilter(DomainMBean var0, DomainLogFilterMBean var1) {
      if (var1 != null) {
         LogFilterMBean var2 = var0.createLogFilter(var1.getName());
         String[] var3 = var1.getUserIds();
         String[] var4 = var1.getSubsystemNames();
         int var5 = var1.getSeverityLevel();
         var2.setUserIds(var3);
         var2.setSubsystemNames(var4);
         var2.setSeverityLevel(var5);
         String var6 = convertOldAttrsToFilterExpression(var3, var4);

         try {
            var2.setFilterExpression(var6);
            if (var6.length() == 0) {
               var2.unSet("FilterExpression");
            }

         } catch (InvalidAttributeValueException var8) {
            throw new AssertionError("Invalid filter expression");
         }
      }
   }

   public static void applyDomainLogFilterToLogMBean(DomainMBean var0, LogMBean var1, DomainLogFilterMBean var2) {
      if (var2 != null) {
         String var3 = Severities.severityNumToString(var2.getSeverityLevel());
         var1.setDomainLogBroadcastSeverity(var3);
         LogFilterMBean var4 = var0.lookupLogFilter(var2.getName());
         var1.setDomainLogBroadcastFilter(var4);
      } else {
         var1.unSet("DomainLogBroadcastFilter");
      }

   }

   public static String convertOldAttrsToFilterExpression(String[] var0, String[] var1) {
      String var2 = "";
      if (var0 != null && var0.length > 0) {
         var2 = "USERID IN " + convertToQueryParams(var0);
      }

      if (var1 != null && var1.length > 0) {
         String var3 = "SUBSYSTEM IN " + convertToQueryParams(var1);
         if (var2.length() > 0) {
            var2 = var2 + " AND " + var3;
         } else {
            var2 = var3;
         }
      }

      return var2;
   }

   private static String convertToQueryParams(String[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("('");
      var1.append(var0[0]);
      var1.append("'");

      for(int var2 = 1; var2 < var0.length; ++var2) {
         var1.append(",'");
         var1.append(var0[var2]);
         var1.append("'");
      }

      var1.append(")");
      return var1.toString();
   }

   public static void upgradeDomainLogFilterEnabled(ServerMBean var0) {
      LogMBean var1 = var0.getLog();
      if (var0.isEnabledForDomainLog()) {
         if (var1.getDomainLogBroadcastSeverity().equals("Off")) {
            var1.setDomainLogBroadcastSeverity("Warning");
         }
      } else {
         var1.setDomainLogBroadcastSeverity("Off");
      }

   }

   public static String getNormalizedStdoutSeverity(boolean var0, int var1, boolean var2) {
      var1 = Math.max(var1, var2 ? 128 : var1);
      if (!var0) {
         var1 = 0;
      }

      return Severities.severityNumToString(var1);
   }
}
