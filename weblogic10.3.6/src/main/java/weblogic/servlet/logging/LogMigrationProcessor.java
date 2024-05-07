package weblogic.servlet.logging;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebServerLogMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public final class LogMigrationProcessor implements ConfigurationProcessor {
   private static final SimpleDateFormat OLD_FORMAT = new SimpleDateFormat("MM-dd-yyyy-k:mm:ss");
   private static final SimpleDateFormat NEW_FORMAT = new SimpleDateFormat("H:mm");

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      ServerMBean[] var2 = var1.getServers();
      if (var2 != null && var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            try {
               this.upgradeConfig(var2[var3].getWebServer());
            } catch (InvalidAttributeValueException var8) {
               throw new UpdateException(var8);
            } catch (DistributedManagementException var9) {
               throw new UpdateException(var9);
            }
         }
      }

      VirtualHostMBean[] var10 = var1.getVirtualHosts();
      if (var10 != null && var10.length > 0) {
         for(int var4 = 0; var4 < var10.length; ++var4) {
            try {
               this.upgradeConfig(var10[var4]);
            } catch (InvalidAttributeValueException var6) {
               throw new UpdateException(var6);
            } catch (DistributedManagementException var7) {
               throw new UpdateException(var7);
            }
         }
      }

   }

   private void upgradeConfig(WebServerMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      WebServerLogMBean var2 = var1.getWebServerLog();
      if (var1.isSet("LoggingEnabled")) {
         var2.setLoggingEnabled(var1.isLoggingEnabled());
      }

      if (var1.isSet("LogFileFormat")) {
         var2.setLogFileFormat(var1.getLogFileFormat());
      }

      if (var1.isSet("LogTimeInGMT")) {
         var2.setLogTimeInGMT(var1.getLogTimeInGMT());
      }

      if (var1.isSet("LogFileName")) {
         var2.setFileName(var1.getLogFileName());
      }

      String var3;
      if (var1.isSet("LogRotationType")) {
         var3 = var1.getLogRotationType();
         if (var3 != null && !var3.equalsIgnoreCase("size")) {
            if (var3.equalsIgnoreCase("date")) {
               var3 = "byTime";
            }
         } else {
            var3 = "bySize";
         }

         var2.setRotationType(var3);
      }

      if (var1.isSet("LogRotationTimeBegin")) {
         try {
            var3 = var1.getLogRotationTimeBegin();
            if (var3 != null || var3.length() > 0) {
               String var4 = convertLogRotationTimeBegin(var3);
               var2.setRotationTime(var4);
            }
         } catch (ParseException var6) {
         }
      }

      if (var1.isSet("LogRotationPeriodMins")) {
         var2.setFileTimeSpan(var1.getLogRotationPeriodMins() / 60);
      }

      if (var1.isSet("LogFileLimitEnabled")) {
         var2.setNumberOfFilesLimited(var1.isLogFileLimitEnabled());
      }

      if (var1.isSet("LogFileCount")) {
         var2.setFileCount(var1.getLogFileCount());
      }

      if (var1.isSet("MaxLogFileSizeKBytes")) {
         if (var1.getMaxLogFileSizeKBytes() == 0) {
            var2.setRotationType("none");
         } else {
            var2.setFileMinSize(var1.getMaxLogFileSizeKBytes());
         }
      }

      if (var2.getFileMinSize() < 1) {
         var2.setRotationType("none");
      }

      try {
         File var8 = new File(DomainDir.getRootDir() + File.separatorChar + var1.getLogFileName());
         if (!var1.isSet("LogFileName")) {
            var8 = new File(DomainDir.getRootDir() + File.separatorChar + var1.getName() + File.separatorChar + "access.log");
            if (!var8.exists()) {
               var8 = new File(DomainDir.getRootDir() + File.separatorChar + "access.log");
            }
         }

         if ("extended".equals(var1.getLogFileFormat()) && var8.exists()) {
            String[] var7 = ELFLogger.readELFFields(var8.getPath());
            if (var7 != null && var7[1] != null) {
               var2.setELFFields(var7[1]);
            }
         }
      } catch (Exception var5) {
      }

   }

   public static final String convertLogRotationTimeBegin(String var0) throws ParseException {
      Date var1 = OLD_FORMAT.parse(var0);
      return NEW_FORMAT.format(var1);
   }
}
