package weblogic.kernel;

import java.util.Map;
import weblogic.logging.Severities;
import weblogic.management.configuration.ExecuteQueueMBean;
import weblogic.management.configuration.IIOPMBean;
import weblogic.management.configuration.KernelDebugMBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.SSLMBean;

final class KernelMBeanStub extends MBeanStub implements KernelMBean {
   private final LogMBean log = new LogMBeanStub();
   private final SSLMBean ssl = new SSLMBeanStub();
   private final IIOPMBeanStub iiop = new IIOPMBeanStub();
   private final KernelDebugMBeanStub debug = new KernelDebugMBeanStub();
   private final ExecuteQueueMBeanStub[] queues = new ExecuteQueueMBeanStub[]{new ExecuteQueueMBeanStub()};
   private boolean reverseDNSAllowed = false;
   private String defaultProtocol = "t3";
   private String defaultSecureProtocol = "t3s";
   private String defaultAdminProtocol = "t3s";
   private int systemThreadPoolSize = 0;
   private int selfTuningThreadPoolSizeMin = 1;
   private int selfTuningThreadPoolSizeMax = 600;
   private int jmsThreadPoolSize = 0;
   private int messagingBridgeThreadPoolSize = 0;
   private boolean nativeIOEnabled = false;
   private boolean outboundEnabled = false;
   private boolean outboundPrivateKeyEnabled = false;
   private String muxerClass = null;
   private boolean devPollDisabled = false;
   private int socketReaders = -1;
   private int percentSocketReaders = 33;
   private long timePeriod = 0L;
   private int socketReaderTimeoutMinMillis = 500;
   private int socketReaderTimeoutMaxMillis = 1000;
   private int maxMessageSize = 10000000;
   private int connectTimeout = 0;
   private int completeMessageTimeout = 60;
   private int completeT3MessageTimeout = 60;
   private boolean socketBufferSizeAsChunkSize = false;
   private int periodLength = 60000;
   private int idlePeriodsUntilTimeout = 4;
   private int idleTimeout = 0;
   private int rjvmIdleTimeout = 0;
   private int responseTimeout = 0;
   private boolean stdoutEnabled = true;
   private int stdoutSeverityLevel = 64;
   private boolean stdoutDebugEnabled = false;
   private int stacktraceDepth = 5;
   private boolean logRemoteExceptionsEnabled = false;
   private boolean instrumentStackTraceEnabled = true;
   private int maxOpenSockCount = -1;
   private String fmt = "standard";
   private boolean stack = true;
   private int stuckThreadTimerInterval = 600;
   private int stuckThreadMaxTime = 600;
   private boolean tracingEnabled = false;
   private int mtuSize = 1500;
   private boolean enableStubLoadingUsingCCL;
   private boolean refreshClientRuntimeDescriptor = false;
   private boolean use81StyleExecuteQueues = true;
   private int t3clientAbbrevTableSize = 255;
   private int t3serverAbbrevTableSize = 2048;
   private boolean gatheredWritesEnabled = false;
   private boolean scatteredReadsEnabled = false;
   private boolean addWorkManagerThreadsByCpuCount = false;
   private boolean useConcurrentQueueForRequestManager = false;

   KernelMBeanStub() {
      this.initializeFromSystemProperties("weblogic.");

      try {
         String var1 = System.getProperty("weblogic.StdoutSeverityLevel");
         if (var1 != null) {
            this.getLog().setStdoutSeverity(Severities.severityNumToString(Integer.parseInt(var1)));
         }

         boolean var2 = Boolean.getBoolean("weblogic.StdoutDebugEnabled");
         if (var2) {
            this.getLog().setStdoutSeverity("Debug");
         }
      } catch (SecurityException var3) {
      }

   }

   public Map getValidProtocols() {
      return null;
   }

   public boolean isReverseDNSAllowed() {
      return this.reverseDNSAllowed;
   }

   public void setReverseDNSAllowed(boolean var1) {
      this.reverseDNSAllowed = var1;
   }

   public String getDefaultProtocol() {
      return this.defaultProtocol;
   }

   public void setDefaultProtocol(String var1) {
      this.defaultProtocol = var1;
   }

   public String getDefaultSecureProtocol() {
      return this.defaultSecureProtocol;
   }

   public void setDefaultSecureProtocol(String var1) {
      this.defaultSecureProtocol = var1;
   }

   public String getAdministrationProtocol() {
      return this.defaultAdminProtocol;
   }

   public void setAdministrationProtocol(String var1) {
      this.defaultAdminProtocol = var1;
   }

   public int getThreadPoolSize() {
      return this.queues[0].getThreadCount();
   }

   public void setThreadPoolSize(int var1) {
      this.queues[0].setThreadCount(var1);
   }

   public int getSystemThreadPoolSize() {
      return this.systemThreadPoolSize;
   }

   public void setSystemThreadPoolSize(int var1) {
      this.systemThreadPoolSize = var1;
   }

   public void setSelfTuningThreadPoolSizeMin(int var1) {
      this.selfTuningThreadPoolSizeMin = var1;
   }

   public int getSelfTuningThreadPoolSizeMin() {
      return this.selfTuningThreadPoolSizeMin;
   }

   public void setSelfTuningThreadPoolSizeMax(int var1) {
      this.selfTuningThreadPoolSizeMax = var1;
   }

   public int getSelfTuningThreadPoolSizeMax() {
      return this.selfTuningThreadPoolSizeMax;
   }

   public int getJMSThreadPoolSize() {
      return this.jmsThreadPoolSize;
   }

   public void setJMSThreadPoolSize(int var1) {
      this.jmsThreadPoolSize = var1;
   }

   public int getMessagingBridgeThreadPoolSize() {
      return this.messagingBridgeThreadPoolSize;
   }

   public void setMessagingBridgeThreadPoolSize(int var1) {
      this.messagingBridgeThreadPoolSize = var1;
   }

   public boolean isNativeIOEnabled() {
      return this.nativeIOEnabled;
   }

   public void setNativeIOEnabled(boolean var1) {
      this.nativeIOEnabled = var1;
   }

   public boolean isOutboundEnabled() {
      return this.outboundEnabled;
   }

   public void setOutboundEnabled(boolean var1) {
      this.outboundEnabled = var1;
   }

   public boolean isOutboundPrivateKeyEnabled() {
      return this.outboundPrivateKeyEnabled;
   }

   public void setOutboundPrivateKeyEnabled(boolean var1) {
      this.outboundPrivateKeyEnabled = var1;
   }

   public String getMuxerClass() {
      return this.muxerClass;
   }

   public void setMuxerClass(String var1) {
      this.muxerClass = var1;
   }

   public boolean isDevPollDisabled() {
      return this.devPollDisabled;
   }

   public void setDevPollDisabled(boolean var1) {
      this.devPollDisabled = var1;
   }

   public int getSocketReaders() {
      return this.socketReaders;
   }

   public void setSocketReaders(int var1) {
      this.socketReaders = var1;
   }

   public int getThreadPoolPercentSocketReaders() {
      return this.percentSocketReaders;
   }

   public void setThreadPoolPercentSocketReaders(int var1) {
      this.percentSocketReaders = var1;
   }

   public void setTimedOutRefIsolationTime(long var1) {
      this.timePeriod = var1;
   }

   public long getTimedOutRefIsolationTime() {
      return this.timePeriod;
   }

   public int getSocketReaderTimeoutMinMillis() {
      return this.socketReaderTimeoutMinMillis;
   }

   public void setSocketReaderTimeoutMinMillis(int var1) {
      this.socketReaderTimeoutMinMillis = var1;
   }

   public int getSocketReaderTimeoutMaxMillis() {
      return this.socketReaderTimeoutMaxMillis;
   }

   public void setSocketReaderTimeoutMaxMillis(int var1) {
      this.socketReaderTimeoutMaxMillis = var1;
   }

   public int getMaxMessageSize() {
      return this.maxMessageSize;
   }

   public void setMaxMessageSize(int var1) {
      this.maxMessageSize = var1;
   }

   public int getConnectTimeout() {
      return this.connectTimeout;
   }

   public void setConnectTimeout(int var1) {
      this.connectTimeout = var1;
   }

   public int getCompleteMessageTimeout() {
      return this.completeMessageTimeout;
   }

   public void setCompleteMessageTimeout(int var1) {
      this.completeMessageTimeout = var1;
   }

   public int getMaxT3MessageSize() {
      return 10000000;
   }

   public void setMaxT3MessageSize(int var1) {
   }

   public int getMaxHTTPMessageSize() {
      return 10000000;
   }

   public void setMaxHTTPMessageSize(int var1) {
   }

   public int getMaxIIOPMessageSize() {
      return this.iiop.getMaxMessageSize();
   }

   public void setMaxIIOPMessageSize(int var1) {
   }

   public int getMaxCOMMessageSize() {
      return 10000000;
   }

   public void setMaxCOMMessageSize(int var1) {
   }

   public int getCompleteT3MessageTimeout() {
      return this.completeT3MessageTimeout;
   }

   public void setCompleteT3MessageTimeout(int var1) {
      this.completeT3MessageTimeout = var1;
   }

   public void setSocketBufferSizeAsChunkSize(boolean var1) {
      this.socketBufferSizeAsChunkSize = var1;
   }

   public boolean isSocketBufferSizeAsChunkSize() {
      return this.socketBufferSizeAsChunkSize;
   }

   public int getCompleteHTTPMessageTimeout() {
      return 60;
   }

   public void setCompleteHTTPMessageTimeout(int var1) {
   }

   public int getCompleteIIOPMessageTimeout() {
      return this.iiop.getCompleteMessageTimeout();
   }

   public void setCompleteIIOPMessageTimeout(int var1) {
   }

   public int getCompleteCOMMessageTimeout() {
      return 60;
   }

   public void setCompleteCOMMessageTimeout(int var1) {
   }

   public int getPeriodLength() {
      return this.periodLength;
   }

   public void setPeriodLength(int var1) {
      this.periodLength = var1;
   }

   public int getIdlePeriodsUntilTimeout() {
      return this.idlePeriodsUntilTimeout;
   }

   public void setIdlePeriodsUntilTimeout(int var1) {
      this.idlePeriodsUntilTimeout = var1;
   }

   public int getIdleConnectionTimeout() {
      return this.idleTimeout;
   }

   public void setIdleConnectionTimeout(int var1) {
      this.idleTimeout = var1;
   }

   public int getDefaultGIOPMinorVersion() {
      return this.iiop.getDefaultMinorVersion();
   }

   public void setDefaultGIOPMinorVersion(int var1) {
      this.iiop.setDefaultMinorVersion(var1);
   }

   public String getIIOPLocationForwardPolicy() {
      return this.iiop.getLocationForwardPolicy();
   }

   public void setIIOPLocationForwardPolicy(String var1) {
      this.iiop.setLocationForwardPolicy(var1);
   }

   public boolean getUseIIOPLocateRequest() {
      return this.iiop.getUseLocateRequest();
   }

   public void setUseIIOPLocateRequest(boolean var1) {
      this.iiop.setUseLocateRequest(var1);
   }

   public String getIIOPTxMechanism() {
      return this.iiop.getTxMechanism();
   }

   public void setIIOPTxMechanism(String var1) {
      this.iiop.setTxMechanism(var1);
   }

   public int getIdleIIOPConnectionTimeout() {
      return this.iiop.getIdleConnectionTimeout();
   }

   public void setIdleIIOPConnectionTimeout(int var1) {
      this.iiop.setIdleConnectionTimeout(var1);
   }

   public IIOPMBean getIIOP() {
      return this.iiop;
   }

   public void setIIOPMBean() {
   }

   public int getRjvmIdleTimeout() {
      return this.rjvmIdleTimeout;
   }

   public void setRjvmIdleTimeout(int var1) {
      this.rjvmIdleTimeout = var1;
   }

   public int getResponseTimeout() {
      return this.responseTimeout;
   }

   public void setResponseTimeout(int var1) {
      this.responseTimeout = var1;
   }

   public boolean isUnsafeClassLoadingEnabled() {
      return false;
   }

   public void setUnsafeClassLoadingEnabled(boolean var1) {
   }

   public KernelDebugMBean getKernelDebug() {
      return this.debug;
   }

   public void setKernelDebug(KernelDebugMBean var1) {
   }

   public int getDGCIdlePeriodsUntilTimeout() {
      return this.getIdlePeriodsUntilTimeout();
   }

   public void setDGCIdlePeriodsUntilTimeout(int var1) {
   }

   public LogMBean getLog() {
      return this.log;
   }

   public boolean isStdoutEnabled() {
      return this.stdoutEnabled;
   }

   public void setStdoutEnabled(boolean var1) {
      this.stdoutEnabled = var1;
   }

   public int getStdoutSeverityLevel() {
      return this.stdoutSeverityLevel;
   }

   public void setStdoutSeverityLevel(int var1) {
      this.stdoutSeverityLevel = var1;
   }

   public boolean isStdoutDebugEnabled() {
      return this.stdoutDebugEnabled;
   }

   public void setStdoutDebugEnabled(boolean var1) {
      this.stdoutDebugEnabled = var1;
   }

   public int getStacktraceDepth() {
      return this.stacktraceDepth;
   }

   public void setStacktraceDepth(int var1) {
      this.stacktraceDepth = var1;
   }

   public boolean isLogRemoteExceptionsEnabled() {
      return false;
   }

   public void setLogRemoteExceptionsEnabled(boolean var1) {
      this.logRemoteExceptionsEnabled = var1;
   }

   public boolean isInstrumentStackTraceEnabled() {
      return this.instrumentStackTraceEnabled;
   }

   public void setInstrumentStackTraceEnabled(boolean var1) {
      this.instrumentStackTraceEnabled = var1;
   }

   public SSLMBean getSSL() {
      return this.ssl;
   }

   public void setSSLMBean() {
   }

   public ExecuteQueueMBean[] getExecuteQueues() {
      return this.queues;
   }

   public void setExecuteQueues(ExecuteQueueMBean[] var1) {
   }

   public int getMaxOpenSockCount() {
      return this.maxOpenSockCount;
   }

   public void setMaxOpenSockCount(int var1) {
      this.maxOpenSockCount = var1;
   }

   public String getStdoutFormat() {
      return this.fmt;
   }

   public void setStdoutFormat(String var1) {
      if (var1.equals("standard") || var1.equals("standard")) {
         this.fmt = var1;
      }

   }

   public boolean isStdoutLogStack() {
      return this.stack;
   }

   public void setStdoutLogStack(boolean var1) {
      this.stack = var1;
   }

   public int getStuckThreadTimerInterval() {
      return this.stuckThreadTimerInterval;
   }

   public void setStuckThreadTimerInterval(int var1) {
      this.stuckThreadTimerInterval = var1;
   }

   public int getStuckThreadMaxTime() {
      return this.stuckThreadMaxTime;
   }

   public void setStuckThreadMaxTime(int var1) {
      this.stuckThreadMaxTime = var1;
   }

   public boolean getTracingEnabled() {
      return this.tracingEnabled;
   }

   public void setTracingEnabled(boolean var1) {
      this.tracingEnabled = var1;
   }

   public int getMTUSize() {
      return this.mtuSize;
   }

   public void setMTUSize(int var1) {
      this.mtuSize = var1;
   }

   public void setLoadStubUsingContextClassLoader(boolean var1) {
      this.enableStubLoadingUsingCCL = var1;
   }

   public boolean getLoadStubUsingContextClassLoader() {
      return this.enableStubLoadingUsingCCL;
   }

   public void setRefreshClientRuntimeDescriptor(boolean var1) {
      this.refreshClientRuntimeDescriptor = var1;
   }

   public boolean getRefreshClientRuntimeDescriptor() {
      return this.refreshClientRuntimeDescriptor;
   }

   public void setUse81StyleExecuteQueues(boolean var1) {
      this.use81StyleExecuteQueues = var1;
   }

   public boolean getUse81StyleExecuteQueues() {
      return this.use81StyleExecuteQueues;
   }

   public void setT3ClientAbbrevTableSize(int var1) {
      this.t3clientAbbrevTableSize = var1;
   }

   public int getT3ClientAbbrevTableSize() {
      return this.t3clientAbbrevTableSize;
   }

   public void setT3ServerAbbrevTableSize(int var1) {
      this.t3serverAbbrevTableSize = var1;
   }

   public int getT3ServerAbbrevTableSize() {
      return this.t3serverAbbrevTableSize;
   }

   public ExecuteQueueMBean createExecuteQueue(String var1) {
      return null;
   }

   public ExecuteQueueMBean lookupExecuteQueue(String var1) {
      return null;
   }

   public void destroyExecuteQueue(ExecuteQueueMBean var1) {
   }

   public boolean removeExecuteQueue(ExecuteQueueMBean var1) {
      return true;
   }

   public boolean addExecuteQueue(ExecuteQueueMBean var1) {
      return true;
   }

   public boolean isGatheredWritesEnabled() {
      return this.gatheredWritesEnabled;
   }

   public void setGatheredWritesEnabled(boolean var1) {
      this.gatheredWritesEnabled = var1;
   }

   public boolean isScatteredReadsEnabled() {
      return this.scatteredReadsEnabled;
   }

   public void setScatteredReadsEnabled(boolean var1) {
      this.scatteredReadsEnabled = var1;
   }

   public boolean isAddWorkManagerThreadsByCpuCount() {
      return this.addWorkManagerThreadsByCpuCount;
   }

   public void setAddWorkManagerThreadsByCpuCount(boolean var1) {
      this.addWorkManagerThreadsByCpuCount = var1;
   }

   public boolean isUseConcurrentQueueForRequestManager() {
      return this.useConcurrentQueueForRequestManager;
   }

   public void setUseConcurrentQueueForRequestManager(boolean var1) {
      this.useConcurrentQueueForRequestManager = var1;
   }
}
