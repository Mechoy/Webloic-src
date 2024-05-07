package weblogic.logging.j2ee;

import java.io.File;
import java.io.OutputStream;
import java.util.Properties;
import weblogic.j2ee.descriptor.wl.LoggingBean;
import weblogic.kernel.MBeanStub;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.LogMBean;

public final class LoggingBeanAdapter extends MBeanStub implements LogMBean {
   private LoggingBean delegate = null;
   private OutputStream outputStream;

   public LoggingBeanAdapter(LoggingBean var1) {
      this.delegate = var1;
   }

   public String getFileName() {
      return this.delegate.getLogFilename();
   }

   public void setFileName(String var1) {
      this.delegate.setLogFilename(var1);
   }

   public String getRotationType() {
      return this.delegate.getRotationType();
   }

   public void setRotationType(String var1) {
      this.delegate.setRotationType(var1);
   }

   public boolean isNumberOfFilesLimited() {
      return this.delegate.isNumberOfFilesLimited();
   }

   public void setNumberOfFilesLimited(boolean var1) {
      this.delegate.setNumberOfFilesLimited(var1);
   }

   public int getFileCount() {
      return this.delegate.getFileCount();
   }

   public void setFileCount(int var1) {
      this.delegate.setFileCount(var1);
   }

   public int getFileTimeSpan() {
      return this.delegate.getFileTimeSpan();
   }

   public void setFileTimeSpan(int var1) {
      this.delegate.setFileTimeSpan(var1);
   }

   public int getFileMinSize() {
      return this.delegate.getFileSizeLimit();
   }

   public void setFileMinSize(int var1) {
      this.delegate.setFileSizeLimit(var1);
   }

   public String getRotationTime() {
      return this.delegate.getRotationTime();
   }

   public void setRotationTime(String var1) {
      this.delegate.setRotationTime(var1);
   }

   public boolean getRotateLogOnStartup() {
      return this.delegate.isRotateLogOnStartup();
   }

   public void setRotateLogOnStartup(boolean var1) {
      this.delegate.setRotateLogOnStartup(var1);
   }

   public String getLogFileRotationDir() {
      return this.delegate.getLogFileRotationDir();
   }

   public void setLogFileRotationDir(String var1) {
      this.delegate.setLogFileRotationDir(var1);
   }

   public String getLogFileSeverity() {
      return null;
   }

   public void setLogFileSeverity(String var1) {
   }

   public LogFilterMBean getLogFileFilter() {
      return null;
   }

   public void setLogFileFilter(LogFilterMBean var1) {
   }

   public String getStdoutSeverity() {
      return null;
   }

   public void setStdoutSeverity(String var1) {
   }

   public LogFilterMBean getStdoutFilter() {
      return null;
   }

   public void setStdoutFilter(LogFilterMBean var1) {
   }

   public String getDomainLogBroadcastSeverity() {
      return null;
   }

   public void setDomainLogBroadcastSeverity(String var1) {
   }

   public LogFilterMBean getDomainLogBroadcastFilter() {
      return null;
   }

   public void setDomainLogBroadcastFilter(LogFilterMBean var1) {
   }

   public String getMemoryBufferSeverity() {
      return null;
   }

   public void setMemoryBufferSeverity(String var1) {
   }

   public LogFilterMBean getMemoryBufferFilter() {
      return null;
   }

   public void setMemoryBufferFilter(LogFilterMBean var1) {
   }

   public int getMemoryBufferSize() {
      return 0;
   }

   public void setMemoryBufferSize(int var1) {
   }

   public boolean isLog4jLoggingEnabled() {
      return false;
   }

   public void setLog4jLoggingEnabled(boolean var1) {
   }

   public boolean isRedirectStdoutToServerLogEnabled() {
      return false;
   }

   public void setRedirectStdoutToServerLogEnabled(boolean var1) {
   }

   public boolean isRedirectStderrToServerLogEnabled() {
      return false;
   }

   public void setRedirectStderrToServerLogEnabled(boolean var1) {
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
      return "standard";
   }

   public void setStdoutFormat(String var1) {
   }

   public boolean isStdoutLogStack() {
      return true;
   }

   public void setStdoutLogStack(boolean var1) {
   }

   public int getStacktraceDepth() {
      return -1;
   }

   public void setStacktraceDepth(int var1) {
   }

   public long getFileTimeSpanFactor() {
      return 3600000L;
   }

   public void setFileTimeSpanFactor(long var1) {
   }

   public OutputStream getOutputStream() {
      return this.outputStream;
   }

   public void setOutputStream(OutputStream var1) {
      this.outputStream = var1;
   }

   public int getBufferSizeKB() {
      return 8;
   }

   public void setBufferSizeKB(int var1) {
   }

   public String getDateFormatPattern() {
      return this.delegate.getDateFormatPattern();
   }

   public void setDateFormatPattern(String var1) {
      this.delegate.setDateFormatPattern(var1);
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
