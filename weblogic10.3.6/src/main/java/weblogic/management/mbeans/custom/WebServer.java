package weblogic.management.mbeans.custom;

import java.text.ParseException;
import javax.management.InvalidAttributeValueException;
import weblogic.common.internal.VersionInfo;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.WebServerLogMBean;
import weblogic.management.configuration.WebServerMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.servlet.logging.LogMigrationProcessor;

public class WebServer extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = -1873044084609391595L;
   private static final boolean DEBUG = false;
   private boolean loggingEnabled = true;
   private String logFileFormat = "MM-dd-yyyy-k:mm:ss";
   private boolean logTimeInGMT = false;
   private String logFileName = "logs/access.log";
   private String logFileRotationType = "size";
   private int logRotationPeriodMins = 1440;
   private String logRotationTimeBegin;
   private boolean logFileLimitEnabled = false;
   private int logFileCount = 7;
   private static final VersionInfo diabloVersion = new VersionInfo("9.0.0.0");

   public WebServer(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setLoggingEnabled(boolean var1) {
      this.loggingEnabled = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 == var2.isLoggingEnabled()) {
            return;
         }

         var2.setLoggingEnabled(var1);
      }

   }

   public boolean isLoggingEnabled() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().isLoggingEnabled() : this.loggingEnabled;
   }

   public String getLogFileFormat() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().getLogFileFormat() : this.logFileFormat;
   }

   public void setLogFileFormat(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.logFileFormat = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 != null && var1.equals(var2.getLogFileFormat())) {
            return;
         }

         var2.setLogFileFormat(var1);
      }

   }

   public boolean getLogTimeInGMT() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().isLogTimeInGMT() : this.logTimeInGMT;
   }

   public void setLogTimeInGMT(boolean var1) {
      this.logTimeInGMT = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 == var2.isLogTimeInGMT()) {
            return;
         }

         var2.setLogTimeInGMT(var1);
      }

   }

   public String getLogFileName() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().getFileName() : this.logFileName;
   }

   public void setLogFileName(String var1) throws InvalidAttributeValueException {
      if (var1 != null && var1.length() != 0) {
         this.logFileName = var1;
         if (!this.logFileName.equals("logs/access.log")) {
            if (this.isDelegateModeEnabled()) {
               WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
               var2.setFileName(this.logFileName);
            }

         }
      }
   }

   public String getLogRotationType() {
      if (!this.isDelegateModeEnabled()) {
         return this.logFileRotationType;
      } else {
         WebServerLogMBean var1 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         String var2 = var1.getRotationType();
         if (var2 != null && !var2.equals("bySize")) {
            if (var2.equals("byTime")) {
               var2 = "date";
            }
         } else {
            var2 = "size";
         }

         return var2;
      }
   }

   public void setLogRotationType(String var1) throws InvalidAttributeValueException {
      this.logFileRotationType = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 != null && !var1.equalsIgnoreCase("size")) {
            if (var1.equalsIgnoreCase("date")) {
               var1 = "byTime";
            }
         } else {
            var1 = "bySize";
         }

         if (var1 != null && var1.equals(var2.getRotationType())) {
            return;
         }

         var2.setRotationType(var1);
      }

   }

   public int getLogRotationPeriodMins() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().getFileTimeSpan() * 60 : this.logRotationPeriodMins;
   }

   public void setLogRotationPeriodMins(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      this.logRotationPeriodMins = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 / 60 == var2.getFileTimeSpan()) {
            return;
         }

         var2.setFileTimeSpan(var1 / 60);
      }

   }

   public String getLogRotationTimeBegin() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().getRotationTime() : this.logRotationTimeBegin;
   }

   public void setLogRotationTimeBegin(String var1) throws InvalidAttributeValueException {
      this.logRotationTimeBegin = var1;
      if (this.isDelegateModeEnabled()) {
         if (this.logRotationTimeBegin == null || this.logRotationTimeBegin.length() == 0) {
            return;
         }

         try {
            String var2 = LogMigrationProcessor.convertLogRotationTimeBegin(var1);
            ((WebServerMBean)this.getMbean()).getWebServerLog().setRotationTime(var2);
         } catch (ParseException var4) {
            String var3 = ManagementTextTextFormatter.getInstance().getTimeFormatErrorMessage("MM-dd-yyyy-k:mm:ss", var1);
            throw new IllegalArgumentException(var3);
         }
      }

   }

   public boolean isLogFileLimitEnabled() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().isNumberOfFilesLimited() : this.logFileLimitEnabled;
   }

   public void setLogFileLimitEnabled(boolean var1) throws InvalidAttributeValueException {
      this.logFileLimitEnabled = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 == var2.isNumberOfFilesLimited()) {
            return;
         }

         try {
            var2.setNumberOfFilesLimited(var1);
         } catch (DistributedManagementException var4) {
            throw new IllegalArgumentException(var4.getMessage());
         }
      }

   }

   public int getLogFileCount() {
      return this.isDelegateModeEnabled() ? ((WebServerMBean)this.getMbean()).getWebServerLog().getFileCount() : this.logFileCount;
   }

   public void setLogFileCount(int var1) throws InvalidAttributeValueException {
      this.logFileCount = var1;
      if (this.isDelegateModeEnabled()) {
         WebServerLogMBean var2 = ((WebServerMBean)this.getMbean()).getWebServerLog();
         if (var2 == null) {
            return;
         }

         if (var1 == var2.getFileCount()) {
            return;
         }

         try {
            var2.setFileCount(var1);
         } catch (DistributedManagementException var4) {
            throw new IllegalArgumentException(var4.getMessage());
         }
      }

   }

   protected boolean isDelegateModeEnabled() {
      DomainMBean var1 = (DomainMBean)this.getMbean().getDescriptor().getRootBean();
      String var2 = var1.getConfigurationVersion();
      if (var2 == null) {
         return false;
      } else {
         VersionInfo var3 = new VersionInfo(var2);
         return !var3.earlierThan(diabloVersion);
      }
   }
}
