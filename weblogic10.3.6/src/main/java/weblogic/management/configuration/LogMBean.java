package weblogic.management.configuration;

public interface LogMBean extends CommonLogMBean {
   int PROD_MODE_MEMORY_BUFFER_SIZE = 500;
   int DEV_MODE_MEMORY_BUFFER_SIZE = 10;

   LogFilterMBean getLogFileFilter();

   void setLogFileFilter(LogFilterMBean var1);

   LogFilterMBean getStdoutFilter();

   void setStdoutFilter(LogFilterMBean var1);

   String getDomainLogBroadcastSeverity();

   void setDomainLogBroadcastSeverity(String var1);

   LogFilterMBean getDomainLogBroadcastFilter();

   void setDomainLogBroadcastFilter(LogFilterMBean var1);

   String getMemoryBufferSeverity();

   void setMemoryBufferSeverity(String var1);

   LogFilterMBean getMemoryBufferFilter();

   void setMemoryBufferFilter(LogFilterMBean var1);

   int getMemoryBufferSize();

   void setMemoryBufferSize(int var1);

   boolean isLog4jLoggingEnabled();

   void setLog4jLoggingEnabled(boolean var1);

   boolean isRedirectStdoutToServerLogEnabled();

   void setRedirectStdoutToServerLogEnabled(boolean var1);

   boolean isRedirectStderrToServerLogEnabled();

   void setRedirectStderrToServerLogEnabled(boolean var1);

   int getDomainLogBroadcasterBufferSize();

   void setDomainLogBroadcasterBufferSize(int var1);

   boolean isServerLoggingBridgeUseParentLoggersEnabled();

   void setServerLoggingBridgeUseParentLoggersEnabled(boolean var1);
}
