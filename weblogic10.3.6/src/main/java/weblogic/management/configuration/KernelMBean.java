package weblogic.management.configuration;

import java.util.Map;
import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface KernelMBean extends ConfigurationMBean {
   String STDOUT_STANDARD = "standard";
   String STDOUT_NOID = "noid";

   Map getValidProtocols();

   boolean isReverseDNSAllowed();

   void setReverseDNSAllowed(boolean var1) throws DistributedManagementException;

   String getDefaultProtocol();

   void setDefaultProtocol(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   String getDefaultSecureProtocol();

   void setDefaultSecureProtocol(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   String getAdministrationProtocol();

   void setAdministrationProtocol(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getThreadPoolSize();

   /** @deprecated */
   void setThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getSystemThreadPoolSize();

   void setSystemThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   void setSelfTuningThreadPoolSizeMin(int var1);

   int getSelfTuningThreadPoolSizeMin();

   void setSelfTuningThreadPoolSizeMax(int var1);

   int getSelfTuningThreadPoolSizeMax();

   int getJMSThreadPoolSize();

   void setJMSThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean isNativeIOEnabled();

   void setNativeIOEnabled(boolean var1);

   void setDevPollDisabled(boolean var1);

   boolean isDevPollDisabled();

   String getMuxerClass();

   void setMuxerClass(String var1);

   int getSocketReaders();

   void setSocketReaders(int var1);

   int getThreadPoolPercentSocketReaders();

   void setThreadPoolPercentSocketReaders(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getSocketReaderTimeoutMinMillis();

   void setSocketReaderTimeoutMinMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getSocketReaderTimeoutMaxMillis();

   void setSocketReaderTimeoutMaxMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean isOutboundEnabled();

   void setOutboundEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isOutboundPrivateKeyEnabled();

   void setOutboundPrivateKeyEnabled(boolean var1) throws InvalidAttributeValueException;

   int getMaxMessageSize();

   void setMaxMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getMaxT3MessageSize();

   void setMaxT3MessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   void setSocketBufferSizeAsChunkSize(boolean var1) throws DistributedManagementException;

   boolean isSocketBufferSizeAsChunkSize();

   /** @deprecated */
   int getMaxHTTPMessageSize();

   void setMaxHTTPMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getMaxCOMMessageSize();

   void setMaxCOMMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getMaxIIOPMessageSize();

   /** @deprecated */
   void setMaxIIOPMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getDefaultGIOPMinorVersion();

   void setDefaultGIOPMinorVersion(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean getUseIIOPLocateRequest();

   void setUseIIOPLocateRequest(boolean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   String getIIOPTxMechanism();

   void setIIOPTxMechanism(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   String getIIOPLocationForwardPolicy();

   void setIIOPLocationForwardPolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getConnectTimeout();

   void setConnectTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getCompleteMessageTimeout();

   void setCompleteMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteT3MessageTimeout();

   void setCompleteT3MessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteHTTPMessageTimeout();

   void setCompleteHTTPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteCOMMessageTimeout();

   void setCompleteCOMMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getIdleConnectionTimeout();

   void setIdleConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getIdleIIOPConnectionTimeout();

   void setIdleIIOPConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   int getCompleteIIOPMessageTimeout();

   void setCompleteIIOPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getPeriodLength();

   void setPeriodLength(int var1) throws InvalidAttributeValueException;

   int getIdlePeriodsUntilTimeout();

   void setIdlePeriodsUntilTimeout(int var1) throws InvalidAttributeValueException;

   int getRjvmIdleTimeout();

   void setRjvmIdleTimeout(int var1) throws InvalidAttributeValueException;

   int getResponseTimeout();

   void setResponseTimeout(int var1) throws InvalidAttributeValueException;

   KernelDebugMBean getKernelDebug();

   int getDGCIdlePeriodsUntilTimeout();

   void setDGCIdlePeriodsUntilTimeout(int var1) throws ConfigurationException;

   SSLMBean getSSL();

   IIOPMBean getIIOP();

   LogMBean getLog();

   /** @deprecated */
   boolean isStdoutEnabled();

   /** @deprecated */
   void setStdoutEnabled(boolean var1) throws DistributedManagementException;

   /** @deprecated */
   int getStdoutSeverityLevel();

   void setStdoutSeverityLevel(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean isStdoutDebugEnabled();

   void setStdoutDebugEnabled(boolean var1) throws DistributedManagementException;

   boolean isLogRemoteExceptionsEnabled();

   void setLogRemoteExceptionsEnabled(boolean var1) throws DistributedManagementException;

   boolean isInstrumentStackTraceEnabled();

   void setInstrumentStackTraceEnabled(boolean var1) throws DistributedManagementException;

   ExecuteQueueMBean[] getExecuteQueues();

   int getMaxOpenSockCount();

   void setMaxOpenSockCount(int var1);

   /** @deprecated */
   String getStdoutFormat();

   /** @deprecated */
   void setStdoutFormat(String var1);

   /** @deprecated */
   boolean isStdoutLogStack();

   /** @deprecated */
   void setStdoutLogStack(boolean var1);

   /** @deprecated */
   int getStuckThreadMaxTime();

   /** @deprecated */
   void setStuckThreadMaxTime(int var1) throws InvalidAttributeValueException;

   int getStuckThreadTimerInterval();

   void setStuckThreadTimerInterval(int var1) throws InvalidAttributeValueException;

   boolean getTracingEnabled();

   void setTracingEnabled(boolean var1);

   /** @deprecated */
   int getMessagingBridgeThreadPoolSize();

   void setMessagingBridgeThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   int getMTUSize();

   void setMTUSize(int var1);

   void setLoadStubUsingContextClassLoader(boolean var1);

   boolean getLoadStubUsingContextClassLoader();

   void setRefreshClientRuntimeDescriptor(boolean var1);

   boolean getRefreshClientRuntimeDescriptor();

   void setTimedOutRefIsolationTime(long var1);

   long getTimedOutRefIsolationTime();

   void setUse81StyleExecuteQueues(boolean var1);

   boolean getUse81StyleExecuteQueues();

   void setT3ClientAbbrevTableSize(int var1);

   int getT3ClientAbbrevTableSize();

   void setT3ServerAbbrevTableSize(int var1);

   int getT3ServerAbbrevTableSize();

   ExecuteQueueMBean createExecuteQueue(String var1);

   void destroyExecuteQueue(ExecuteQueueMBean var1);

   ExecuteQueueMBean lookupExecuteQueue(String var1);

   void setGatheredWritesEnabled(boolean var1);

   boolean isGatheredWritesEnabled();

   void setScatteredReadsEnabled(boolean var1);

   boolean isScatteredReadsEnabled();

   void setAddWorkManagerThreadsByCpuCount(boolean var1);

   boolean isAddWorkManagerThreadsByCpuCount();

   void setUseConcurrentQueueForRequestManager(boolean var1);

   boolean isUseConcurrentQueueForRequestManager();
}
