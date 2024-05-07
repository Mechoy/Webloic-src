package weblogic.connector.external.impl;

import weblogic.connector.external.LoggingInfo;

public class LoggingInfoImpl implements LoggingInfo {
   private String logFilename;
   private boolean loggingEnabled;
   private String rotationType;
   private String rotationTime;
   private boolean numberOfFilesLimited;
   private int fileCount;
   private int fileSizeLimit;
   private int fileTimeSpan;
   private boolean rotateLogOnStartup;
   private String logFileRotationDir;

   public LoggingInfoImpl(String var1, boolean var2, String var3, String var4, boolean var5, int var6, int var7, int var8, boolean var9, String var10) {
      this.logFilename = var1;
      this.loggingEnabled = var2;
      this.rotationType = var3;
      this.rotationTime = var4;
      this.numberOfFilesLimited = var5;
      this.fileCount = var6;
      this.fileSizeLimit = var7;
      this.fileTimeSpan = var8;
      this.rotateLogOnStartup = var9;
      this.logFileRotationDir = var10;
   }

   public String getLogFilename() {
      return this.logFilename;
   }

   public boolean isLoggingEnabled() {
      return this.loggingEnabled;
   }

   public String getRotationType() {
      return this.rotationType;
   }

   public String getRotationTime() {
      return this.rotationTime;
   }

   public boolean isNumberOfFilesLimited() {
      return this.numberOfFilesLimited;
   }

   public int getFileCount() {
      return this.fileCount;
   }

   public int getFileSizeLimit() {
      return this.fileSizeLimit;
   }

   public int getFileTimeSpan() {
      return this.fileTimeSpan;
   }

   public boolean isRotateLogOnStartup() {
      return this.rotateLogOnStartup;
   }

   public String getLogFileRotationDir() {
      return this.logFileRotationDir;
   }
}
