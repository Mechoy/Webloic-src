package weblogic.kernel;

import java.io.File;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.LogMBean;

final class LogMBeanStub extends MBeanStub implements LogMBean {
   private static final String TRACE = "Trace";
   private static final String INFO = "Info";
   private String fname = null;
   private String rotationType = "none";
   private boolean filesLtd = false;
   private int fileCount = 7;
   private int timeSpan = 24;
   private int fileSize = 500;
   private String logRotationTime;
   private boolean rotateLogOnStartup = false;
   private String rotationDir = null;
   private boolean log4jLoggingEnabled = false;
   private int memoryBufferSize = 500;
   private String stdoutSeverty = "Info";
   private String logFileSeverity = "Trace";
   private String domainLogBroadcastSeverity;
   private String memoryBufferSeverity;
   private LogFilterMBean stdoutFilter;
   private LogFilterMBean logFileFilter;
   private LogFilterMBean domainLogBroadcastFilter;
   private LogFilterMBean memoryBufferFilter;
   private boolean redirectStdoutToServerLogEnabled = false;
   private boolean redirectStderrToServerLogEnabled = false;
   private String stdoutFormat = "standard";
   private boolean stdoutLogStack = true;
   private int stacktraceDepth = 5;
   private String dateFormatPattern;

   LogMBeanStub() {
      this.initializeFromSystemProperties("weblogic.log.");
   }

   public String getFileName() {
      return this.fname;
   }

   public void setFileName(String var1) {
      this.fname = var1;
   }

   public String getRotationType() {
      return this.rotationType;
   }

   public void setRotationType(String var1) {
      this.rotationType = var1;
   }

   public boolean isNumberOfFilesLimited() {
      return this.filesLtd;
   }

   public void setNumberOfFilesLimited(boolean var1) {
      this.filesLtd = var1;
   }

   public int getFileCount() {
      return this.fileCount;
   }

   public void setFileCount(int var1) {
      this.fileCount = var1;
   }

   public int getFileTimeSpan() {
      return this.timeSpan;
   }

   public void setFileTimeSpan(int var1) {
      this.timeSpan = var1;
   }

   public int getFileMinSize() {
      return this.fileSize;
   }

   public void setFileMinSize(int var1) {
      this.fileSize = var1;
   }

   public String getRotationTime() {
      return this.logRotationTime;
   }

   public void setRotationTime(String var1) {
      this.logRotationTime = var1;
   }

   public boolean getRotateLogOnStartup() {
      return this.rotateLogOnStartup;
   }

   public void setRotateLogOnStartup(boolean var1) {
      this.rotateLogOnStartup = var1;
   }

   public String getLogFileRotationDir() {
      return this.rotationDir;
   }

   public void setLogFileRotationDir(String var1) {
      this.rotationDir = var1;
   }

   public LogFilterMBean getLogFileFilter() {
      return this.logFileFilter;
   }

   public void setLogFileFilter(LogFilterMBean var1) {
      this.logFileFilter = var1;
   }

   public LogFilterMBean getStdoutFilter() {
      return this.stdoutFilter;
   }

   public void setStdoutFilter(LogFilterMBean var1) {
      this.stdoutFilter = var1;
   }

   public LogFilterMBean getMemoryBufferFilter() {
      return this.memoryBufferFilter;
   }

   public void setMemoryBufferFilter(LogFilterMBean var1) {
      this.memoryBufferFilter = var1;
   }

   public LogFilterMBean getDomainLogBroadcastFilter() {
      return this.domainLogBroadcastFilter;
   }

   public void setDomainLogBroadcastFilter(LogFilterMBean var1) {
      this.domainLogBroadcastFilter = var1;
   }

   public boolean isLog4jLoggingEnabled() {
      return this.log4jLoggingEnabled;
   }

   public void setLog4jLoggingEnabled(boolean var1) {
      this.log4jLoggingEnabled = var1;
   }

   public int getMemoryBufferSize() {
      return this.memoryBufferSize;
   }

   public void setMemoryBufferSize(int var1) {
      this.memoryBufferSize = var1;
   }

   public String getStdoutSeverity() {
      return this.stdoutSeverty;
   }

   public void setStdoutSeverity(String var1) {
      this.stdoutSeverty = var1;
   }

   public String getLogFileSeverity() {
      return this.logFileSeverity;
   }

   public void setLogFileSeverity(String var1) {
      this.logFileSeverity = var1;
   }

   public String getMemoryBufferSeverity() {
      return this.memoryBufferSeverity;
   }

   public void setMemoryBufferSeverity(String var1) {
      this.memoryBufferSeverity = var1;
   }

   public String getDomainLogBroadcastSeverity() {
      return this.domainLogBroadcastSeverity;
   }

   public void setDomainLogBroadcastSeverity(String var1) {
      this.domainLogBroadcastSeverity = var1;
   }

   public boolean isRedirectStdoutToServerLogEnabled() {
      return this.redirectStdoutToServerLogEnabled;
   }

   public void setRedirectStdoutToServerLogEnabled(boolean var1) {
      this.redirectStdoutToServerLogEnabled = var1;
   }

   public boolean isRedirectStderrToServerLogEnabled() {
      return this.redirectStderrToServerLogEnabled;
   }

   public void setRedirectStderrToServerLogEnabled(boolean var1) {
      this.redirectStderrToServerLogEnabled = var1;
   }

   public int getDomainLogBroadcasterBufferSize() {
      return 0;
   }

   public void setDomainLogBroadcasterBufferSize(int var1) {
   }

   public String computeLogFilePath() {
      return (new File(this.getFileName())).getAbsolutePath();
   }

   public String getStdoutFormat() {
      return this.stdoutFormat;
   }

   public void setStdoutFormat(String var1) {
      this.stdoutFormat = var1;
   }

   public boolean isStdoutLogStack() {
      return this.stdoutLogStack;
   }

   public void setStdoutLogStack(boolean var1) {
      this.stdoutLogStack = var1;
   }

   public int getStacktraceDepth() {
      return this.stacktraceDepth;
   }

   public void setStacktraceDepth(int var1) {
      this.stacktraceDepth = var1;
   }

   public long getFileTimeSpanFactor() {
      return 3600000L;
   }

   public void setFileTimeSpanFactor(long var1) {
   }

   public OutputStream getOutputStream() {
      return null;
   }

   public void setOutputStream(OutputStream var1) {
   }

   public int getBufferSizeKB() {
      return 8;
   }

   public void setBufferSizeKB(int var1) {
   }

   public String getDateFormatPattern() {
      if (this.dateFormatPattern == null) {
         DateFormat var1 = DateFormat.getDateTimeInstance(2, 0);
         if (var1 instanceof SimpleDateFormat) {
            this.dateFormatPattern = ((SimpleDateFormat)var1).toPattern();
         } else {
            this.dateFormatPattern = "MMM d, yyyy h:mm:ss a z";
         }
      }

      return this.dateFormatPattern;
   }

   public void setDateFormatPattern(String var1) {
   }

   public String getLoggerSeverity() {
      return "Trace";
   }

   public void setLoggerSeverity(String var1) {
   }

   public Properties getLoggerSeverityProperties() {
      return null;
   }

   public void setLoggerSeverityProperties(Properties var1) {
   }

   public boolean isServerLoggingBridgeUseParentLoggersEnabled() {
      return false;
   }

   public void setServerLoggingBridgeUseParentLoggersEnabled(boolean var1) {
   }
}
