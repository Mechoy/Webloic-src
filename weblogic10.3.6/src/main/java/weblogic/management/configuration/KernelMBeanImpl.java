package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.Kernel;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class KernelMBeanImpl extends ConfigurationMBeanImpl implements KernelMBean, Serializable {
   private boolean _AddWorkManagerThreadsByCpuCount;
   private String _AdministrationProtocol;
   private int _CompleteCOMMessageTimeout;
   private int _CompleteHTTPMessageTimeout;
   private int _CompleteIIOPMessageTimeout;
   private int _CompleteMessageTimeout;
   private int _CompleteT3MessageTimeout;
   private int _ConnectTimeout;
   private int _DGCIdlePeriodsUntilTimeout;
   private int _DefaultGIOPMinorVersion;
   private String _DefaultProtocol;
   private String _DefaultSecureProtocol;
   private boolean _DevPollDisabled;
   private ExecuteQueueMBean[] _ExecuteQueues;
   private boolean _GatheredWritesEnabled;
   private IIOPMBean _IIOP;
   private String _IIOPLocationForwardPolicy;
   private String _IIOPTxMechanism;
   private int _IdleConnectionTimeout;
   private int _IdleIIOPConnectionTimeout;
   private int _IdlePeriodsUntilTimeout;
   private boolean _InstrumentStackTraceEnabled;
   private int _JMSThreadPoolSize;
   private KernelDebugMBean _KernelDebug;
   private boolean _LoadStubUsingContextClassLoader;
   private LogMBean _Log;
   private boolean _LogRemoteExceptionsEnabled;
   private int _MTUSize;
   private int _MaxCOMMessageSize;
   private int _MaxHTTPMessageSize;
   private int _MaxIIOPMessageSize;
   private int _MaxMessageSize;
   private int _MaxOpenSockCount;
   private int _MaxT3MessageSize;
   private int _MessagingBridgeThreadPoolSize;
   private String _MuxerClass;
   private String _Name;
   private boolean _NativeIOEnabled;
   private boolean _OutboundEnabled;
   private boolean _OutboundPrivateKeyEnabled;
   private int _PeriodLength;
   private boolean _RefreshClientRuntimeDescriptor;
   private int _ResponseTimeout;
   private boolean _ReverseDNSAllowed;
   private int _RjvmIdleTimeout;
   private SSLMBean _SSL;
   private boolean _ScatteredReadsEnabled;
   private int _SelfTuningThreadPoolSizeMax;
   private int _SelfTuningThreadPoolSizeMin;
   private boolean _SocketBufferSizeAsChunkSize;
   private int _SocketReaderTimeoutMaxMillis;
   private int _SocketReaderTimeoutMinMillis;
   private int _SocketReaders;
   private boolean _StdoutDebugEnabled;
   private boolean _StdoutEnabled;
   private String _StdoutFormat;
   private boolean _StdoutLogStack;
   private int _StdoutSeverityLevel;
   private int _StuckThreadMaxTime;
   private int _StuckThreadTimerInterval;
   private int _SystemThreadPoolSize;
   private int _T3ClientAbbrevTableSize;
   private int _T3ServerAbbrevTableSize;
   private int _ThreadPoolPercentSocketReaders;
   private int _ThreadPoolSize;
   private long _TimedOutRefIsolationTime;
   private boolean _TracingEnabled;
   private boolean _Use81StyleExecuteQueues;
   private boolean _UseConcurrentQueueForRequestManager;
   private boolean _UseIIOPLocateRequest;
   private Map _ValidProtocols;
   private Kernel _customizer;
   private static SchemaHelper2 _schemaHelper;

   public KernelMBeanImpl() {
      try {
         this._customizer = new Kernel(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public KernelMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new Kernel(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public Map getValidProtocols() {
      return this._ValidProtocols;
   }

   public String getValidProtocolsAsString() {
      return StringHelper.objectToString(this.getValidProtocols());
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isValidProtocolsSet() {
      return this._isSet(7);
   }

   public void setValidProtocolsAsString(String var1) {
      try {
         this.setValidProtocols(StringHelper.stringToMap(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setValidProtocols(Map var1) throws InvalidAttributeValueException {
      Map var2 = this._ValidProtocols;
      this._ValidProtocols = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isReverseDNSAllowed() {
      return this._ReverseDNSAllowed;
   }

   public boolean isReverseDNSAllowedSet() {
      return this._isSet(8);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setReverseDNSAllowed(boolean var1) throws DistributedManagementException {
      boolean var2 = this._ReverseDNSAllowed;
      this._ReverseDNSAllowed = var1;
      this._postSet(8, var2, var1);
   }

   public String getDefaultProtocol() {
      return this._DefaultProtocol;
   }

   public boolean isDefaultProtocolSet() {
      return this._isSet(9);
   }

   public void setDefaultProtocol(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"t3", "t3s", "http", "https", "iiop", "iiops"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultProtocol", var1, var2);
      String var3 = this._DefaultProtocol;
      this._DefaultProtocol = var1;
      this._postSet(9, var3, var1);
   }

   public String getDefaultSecureProtocol() {
      return this._DefaultSecureProtocol;
   }

   public boolean isDefaultSecureProtocolSet() {
      return this._isSet(10);
   }

   public void setDefaultSecureProtocol(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"t3s", "https", "iiops"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("DefaultSecureProtocol", var1, var2);
      String var3 = this._DefaultSecureProtocol;
      this._DefaultSecureProtocol = var1;
      this._postSet(10, var3, var1);
   }

   public String getAdministrationProtocol() {
      if (!this._isSet(11)) {
         try {
            return ((DomainMBean)this.getParent()).getAdministrationProtocol();
         } catch (NullPointerException var2) {
         }
      }

      return this._AdministrationProtocol;
   }

   public boolean isAdministrationProtocolSet() {
      return this._isSet(11);
   }

   public void setAdministrationProtocol(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"t3s", "https", "iiops", "t3", "http", "iiop"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AdministrationProtocol", var1, var2);
      String var3 = this._AdministrationProtocol;
      this._AdministrationProtocol = var1;
      this._postSet(11, var3, var1);
   }

   public int getThreadPoolSize() {
      return this._ThreadPoolSize;
   }

   public boolean isThreadPoolSizeSet() {
      return this._isSet(12);
   }

   public void setThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadPoolSize", (long)var1, 0L, 65534L);
      int var2 = this._ThreadPoolSize;
      this._ThreadPoolSize = var1;
      this._postSet(12, var2, var1);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public int getSystemThreadPoolSize() {
      return this._SystemThreadPoolSize;
   }

   public boolean isSystemThreadPoolSizeSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setSystemThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SystemThreadPoolSize", (long)var1, 5L, 65534L);
      int var2 = this._SystemThreadPoolSize;
      this._SystemThreadPoolSize = var1;
      this._postSet(13, var2, var1);
   }

   public void setSelfTuningThreadPoolSizeMin(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SelfTuningThreadPoolSizeMin", (long)var1, 1L, 65534L);
      int var2 = this._SelfTuningThreadPoolSizeMin;
      this._SelfTuningThreadPoolSizeMin = var1;
      this._postSet(14, var2, var1);
   }

   public int getSelfTuningThreadPoolSizeMin() {
      return this._SelfTuningThreadPoolSizeMin;
   }

   public boolean isSelfTuningThreadPoolSizeMinSet() {
      return this._isSet(14);
   }

   public void setSelfTuningThreadPoolSizeMax(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SelfTuningThreadPoolSizeMax", (long)var1, 1L, 65534L);
      int var2 = this._SelfTuningThreadPoolSizeMax;
      this._SelfTuningThreadPoolSizeMax = var1;
      this._postSet(15, var2, var1);
   }

   public int getSelfTuningThreadPoolSizeMax() {
      return this._SelfTuningThreadPoolSizeMax;
   }

   public boolean isSelfTuningThreadPoolSizeMaxSet() {
      return this._isSet(15);
   }

   public int getJMSThreadPoolSize() {
      return this._JMSThreadPoolSize;
   }

   public boolean isJMSThreadPoolSizeSet() {
      return this._isSet(16);
   }

   public void setJMSThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("JMSThreadPoolSize", (long)var1, 0L, 65534L);
      int var2 = this._JMSThreadPoolSize;
      this._JMSThreadPoolSize = var1;
      this._postSet(16, var2, var1);
   }

   public boolean isNativeIOEnabled() {
      return this._NativeIOEnabled;
   }

   public boolean isNativeIOEnabledSet() {
      return this._isSet(17);
   }

   public void setNativeIOEnabled(boolean var1) {
      boolean var2 = this._NativeIOEnabled;
      this._NativeIOEnabled = var1;
      this._postSet(17, var2, var1);
   }

   public void setDevPollDisabled(boolean var1) {
      boolean var2 = this._DevPollDisabled;
      this._DevPollDisabled = var1;
      this._postSet(18, var2, var1);
   }

   public boolean isDevPollDisabled() {
      return this._DevPollDisabled;
   }

   public boolean isDevPollDisabledSet() {
      return this._isSet(18);
   }

   public String getMuxerClass() {
      if (!this._isSet(19)) {
         try {
            return ((DomainMBean)this.getParent()).isExalogicOptimizationsEnabled() ? "weblogic.socket.NIOSocketMuxer" : null;
         } catch (NullPointerException var2) {
         }
      }

      return this._MuxerClass;
   }

   public boolean isMuxerClassSet() {
      return this._isSet(19);
   }

   public void setMuxerClass(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MuxerClass;
      this._MuxerClass = var1;
      this._postSet(19, var2, var1);
   }

   public int getSocketReaders() {
      return this._SocketReaders;
   }

   public boolean isSocketReadersSet() {
      return this._isSet(20);
   }

   public void setSocketReaders(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SocketReaders", (long)var1, -1L, 65534L);
      int var2 = this._SocketReaders;
      this._SocketReaders = var1;
      this._postSet(20, var2, var1);
   }

   public int getThreadPoolPercentSocketReaders() {
      return this._ThreadPoolPercentSocketReaders;
   }

   public boolean isThreadPoolPercentSocketReadersSet() {
      return this._isSet(21);
   }

   public void setThreadPoolPercentSocketReaders(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ThreadPoolPercentSocketReaders", (long)var1, 1L, 99L);
      int var2 = this._ThreadPoolPercentSocketReaders;
      this._ThreadPoolPercentSocketReaders = var1;
      this._postSet(21, var2, var1);
   }

   public int getSocketReaderTimeoutMinMillis() {
      return this._SocketReaderTimeoutMinMillis;
   }

   public boolean isSocketReaderTimeoutMinMillisSet() {
      return this._isSet(22);
   }

   public void setSocketReaderTimeoutMinMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SocketReaderTimeoutMinMillis", (long)var1, 0L, 65534L);
      int var2 = this._SocketReaderTimeoutMinMillis;
      this._SocketReaderTimeoutMinMillis = var1;
      this._postSet(22, var2, var1);
   }

   public int getSocketReaderTimeoutMaxMillis() {
      return this._SocketReaderTimeoutMaxMillis;
   }

   public boolean isSocketReaderTimeoutMaxMillisSet() {
      return this._isSet(23);
   }

   public void setSocketReaderTimeoutMaxMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("SocketReaderTimeoutMaxMillis", (long)var1, 0L, 65534L);
      int var2 = this._SocketReaderTimeoutMaxMillis;
      this._SocketReaderTimeoutMaxMillis = var1;
      this._postSet(23, var2, var1);
   }

   public boolean isOutboundEnabled() {
      return this._OutboundEnabled;
   }

   public boolean isOutboundEnabledSet() {
      return this._isSet(24);
   }

   public void setOutboundEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._OutboundEnabled;
      this._OutboundEnabled = var1;
      this._postSet(24, var2, var1);
   }

   public boolean isOutboundPrivateKeyEnabled() {
      return this._OutboundPrivateKeyEnabled;
   }

   public boolean isOutboundPrivateKeyEnabledSet() {
      return this._isSet(25);
   }

   public void setOutboundPrivateKeyEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._OutboundPrivateKeyEnabled;
      this._OutboundPrivateKeyEnabled = var1;
      this._postSet(25, var2, var1);
   }

   public int getMaxMessageSize() {
      return this._MaxMessageSize;
   }

   public boolean isMaxMessageSizeSet() {
      return this._isSet(26);
   }

   public void setMaxMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxMessageSize", (long)var1, 4096L, 2000000000L);
      int var2 = this._MaxMessageSize;
      this._MaxMessageSize = var1;
      this._postSet(26, var2, var1);
   }

   public int getMaxT3MessageSize() {
      return this._MaxT3MessageSize;
   }

   public boolean isMaxT3MessageSizeSet() {
      return this._isSet(27);
   }

   public void setMaxT3MessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      KernelValidator.validateMaxT3MessageSize(var1);
      int var2 = this._MaxT3MessageSize;
      this._MaxT3MessageSize = var1;
      this._postSet(27, var2, var1);
   }

   public void setSocketBufferSizeAsChunkSize(boolean var1) throws DistributedManagementException {
      boolean var2 = this._SocketBufferSizeAsChunkSize;
      this._SocketBufferSizeAsChunkSize = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isSocketBufferSizeAsChunkSize() {
      return this._SocketBufferSizeAsChunkSize;
   }

   public boolean isSocketBufferSizeAsChunkSizeSet() {
      return this._isSet(28);
   }

   public int getMaxHTTPMessageSize() {
      return this._MaxHTTPMessageSize;
   }

   public boolean isMaxHTTPMessageSizeSet() {
      return this._isSet(29);
   }

   public void setMaxHTTPMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      KernelValidator.validateMaxHTTPMessageSize(var1);
      int var2 = this._MaxHTTPMessageSize;
      this._MaxHTTPMessageSize = var1;
      this._postSet(29, var2, var1);
   }

   public int getMaxCOMMessageSize() {
      return this._MaxCOMMessageSize;
   }

   public boolean isMaxCOMMessageSizeSet() {
      return this._isSet(30);
   }

   public void setMaxCOMMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      KernelValidator.validateMaxCommMessageSize(var1);
      int var2 = this._MaxCOMMessageSize;
      this._MaxCOMMessageSize = var1;
      this._postSet(30, var2, var1);
   }

   public int getMaxIIOPMessageSize() {
      return this._MaxIIOPMessageSize;
   }

   public boolean isMaxIIOPMessageSizeSet() {
      return this._isSet(31);
   }

   public void setMaxIIOPMessageSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      KernelValidator.validateMaxIIOPMessageSize(var1);
      int var2 = this._MaxIIOPMessageSize;
      this._MaxIIOPMessageSize = var1;
      this._postSet(31, var2, var1);
   }

   public int getDefaultGIOPMinorVersion() {
      return this._DefaultGIOPMinorVersion;
   }

   public boolean isDefaultGIOPMinorVersionSet() {
      return this._isSet(32);
   }

   public void setDefaultGIOPMinorVersion(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("DefaultGIOPMinorVersion", (long)var1, 0L, 2L);
      int var2 = this._DefaultGIOPMinorVersion;
      this._DefaultGIOPMinorVersion = var1;
      this._postSet(32, var2, var1);
   }

   public boolean getUseIIOPLocateRequest() {
      return this._UseIIOPLocateRequest;
   }

   public boolean isUseIIOPLocateRequestSet() {
      return this._isSet(33);
   }

   public void setUseIIOPLocateRequest(boolean var1) throws InvalidAttributeValueException, DistributedManagementException {
      boolean var2 = this._UseIIOPLocateRequest;
      this._UseIIOPLocateRequest = var1;
      this._postSet(33, var2, var1);
   }

   public String getIIOPTxMechanism() {
      return this._IIOPTxMechanism;
   }

   public boolean isIIOPTxMechanismSet() {
      return this._isSet(34);
   }

   public void setIIOPTxMechanism(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"ots", "jta"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("IIOPTxMechanism", var1, var2);
      String var3 = this._IIOPTxMechanism;
      this._IIOPTxMechanism = var1;
      this._postSet(34, var3, var1);
   }

   public String getIIOPLocationForwardPolicy() {
      return this._IIOPLocationForwardPolicy;
   }

   public boolean isIIOPLocationForwardPolicySet() {
      return this._isSet(35);
   }

   public void setIIOPLocationForwardPolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"off", "failover", "round-robin", "random"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("IIOPLocationForwardPolicy", var1, var2);
      String var3 = this._IIOPLocationForwardPolicy;
      this._IIOPLocationForwardPolicy = var1;
      this._postSet(35, var3, var1);
   }

   public int getConnectTimeout() {
      return this._ConnectTimeout;
   }

   public boolean isConnectTimeoutSet() {
      return this._isSet(36);
   }

   public void setConnectTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ConnectTimeout", (long)var1, 0L, 240L);
      int var2 = this._ConnectTimeout;
      this._ConnectTimeout = var1;
      this._postSet(36, var2, var1);
   }

   public int getCompleteMessageTimeout() {
      return this._CompleteMessageTimeout;
   }

   public boolean isCompleteMessageTimeoutSet() {
      return this._isSet(37);
   }

   public void setCompleteMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteMessageTimeout", (long)var1, 0L, 480L);
      int var2 = this._CompleteMessageTimeout;
      this._CompleteMessageTimeout = var1;
      this._postSet(37, var2, var1);
   }

   public int getCompleteT3MessageTimeout() {
      return this._CompleteT3MessageTimeout;
   }

   public boolean isCompleteT3MessageTimeoutSet() {
      return this._isSet(38);
   }

   public void setCompleteT3MessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteT3MessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteT3MessageTimeout;
      this._CompleteT3MessageTimeout = var1;
      this._postSet(38, var2, var1);
   }

   public int getCompleteHTTPMessageTimeout() {
      return this._CompleteHTTPMessageTimeout;
   }

   public boolean isCompleteHTTPMessageTimeoutSet() {
      return this._isSet(39);
   }

   public void setCompleteHTTPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteHTTPMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteHTTPMessageTimeout;
      this._CompleteHTTPMessageTimeout = var1;
      this._postSet(39, var2, var1);
   }

   public int getCompleteCOMMessageTimeout() {
      return this._CompleteCOMMessageTimeout;
   }

   public boolean isCompleteCOMMessageTimeoutSet() {
      return this._isSet(40);
   }

   public void setCompleteCOMMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteCOMMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteCOMMessageTimeout;
      this._CompleteCOMMessageTimeout = var1;
      this._postSet(40, var2, var1);
   }

   public int getIdleConnectionTimeout() {
      return this._IdleConnectionTimeout;
   }

   public boolean isIdleConnectionTimeoutSet() {
      return this._isSet(41);
   }

   public void setIdleConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdleConnectionTimeout", var1, 0);
      int var2 = this._IdleConnectionTimeout;
      this._IdleConnectionTimeout = var1;
      this._postSet(41, var2, var1);
   }

   public int getIdleIIOPConnectionTimeout() {
      return this._IdleIIOPConnectionTimeout;
   }

   public boolean isIdleIIOPConnectionTimeoutSet() {
      return this._isSet(42);
   }

   public void setIdleIIOPConnectionTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("IdleIIOPConnectionTimeout", var1, -1);
      int var2 = this._IdleIIOPConnectionTimeout;
      this._IdleIIOPConnectionTimeout = var1;
      this._postSet(42, var2, var1);
   }

   public int getCompleteIIOPMessageTimeout() {
      return this._CompleteIIOPMessageTimeout;
   }

   public boolean isCompleteIIOPMessageTimeoutSet() {
      return this._isSet(43);
   }

   public void setCompleteIIOPMessageTimeout(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CompleteIIOPMessageTimeout", (long)var1, -1L, 480L);
      int var2 = this._CompleteIIOPMessageTimeout;
      this._CompleteIIOPMessageTimeout = var1;
      this._postSet(43, var2, var1);
   }

   public int getPeriodLength() {
      return this._PeriodLength;
   }

   public boolean isPeriodLengthSet() {
      return this._isSet(44);
   }

   public void setPeriodLength(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkMin("PeriodLength", var1, 0);
      int var2 = this._PeriodLength;
      this._PeriodLength = var1;
      this._postSet(44, var2, var1);
   }

   public int getIdlePeriodsUntilTimeout() {
      return this._IdlePeriodsUntilTimeout;
   }

   public boolean isIdlePeriodsUntilTimeoutSet() {
      return this._isSet(45);
   }

   public void setIdlePeriodsUntilTimeout(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("IdlePeriodsUntilTimeout", (long)var1, 4L, 65534L);
      int var2 = this._IdlePeriodsUntilTimeout;
      this._IdlePeriodsUntilTimeout = var1;
      this._postSet(45, var2, var1);
   }

   public int getRjvmIdleTimeout() {
      return this._RjvmIdleTimeout;
   }

   public boolean isRjvmIdleTimeoutSet() {
      return this._isSet(46);
   }

   public void setRjvmIdleTimeout(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RjvmIdleTimeout", (long)var1, 0L, 900000L);
      int var2 = this._RjvmIdleTimeout;
      this._RjvmIdleTimeout = var1;
      this._postSet(46, var2, var1);
   }

   public int getResponseTimeout() {
      return this._ResponseTimeout;
   }

   public boolean isResponseTimeoutSet() {
      return this._isSet(47);
   }

   public void setResponseTimeout(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ResponseTimeout", (long)var1, 0L, 65534L);
      int var2 = this._ResponseTimeout;
      this._ResponseTimeout = var1;
      this._postSet(47, var2, var1);
   }

   public KernelDebugMBean getKernelDebug() {
      return this._customizer.getKernelDebug();
   }

   public boolean isKernelDebugSet() {
      return this._isSet(48);
   }

   public void setKernelDebug(KernelDebugMBean var1) throws InvalidAttributeValueException {
      this._KernelDebug = var1;
   }

   public int getDGCIdlePeriodsUntilTimeout() {
      return this._DGCIdlePeriodsUntilTimeout;
   }

   public boolean isDGCIdlePeriodsUntilTimeoutSet() {
      return this._isSet(49);
   }

   public void setDGCIdlePeriodsUntilTimeout(int var1) throws ConfigurationException {
      int var2 = this._DGCIdlePeriodsUntilTimeout;
      this._DGCIdlePeriodsUntilTimeout = var1;
      this._postSet(49, var2, var1);
   }

   public SSLMBean getSSL() {
      return this._SSL;
   }

   public boolean isSSLSet() {
      return this._isSet(50) || this._isAnythingSet((AbstractDescriptorBean)this.getSSL());
   }

   public void setSSL(SSLMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 50)) {
         this._postCreate(var2);
      }

      SSLMBean var3 = this._SSL;
      this._SSL = var1;
      this._postSet(50, var3, var1);
   }

   public IIOPMBean getIIOP() {
      return this._IIOP;
   }

   public boolean isIIOPSet() {
      return this._isSet(51) || this._isAnythingSet((AbstractDescriptorBean)this.getIIOP());
   }

   public void setIIOP(IIOPMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 51)) {
         this._postCreate(var2);
      }

      IIOPMBean var3 = this._IIOP;
      this._IIOP = var1;
      this._postSet(51, var3, var1);
   }

   public LogMBean getLog() {
      return this._Log;
   }

   public boolean isLogSet() {
      return this._isSet(52) || this._isAnythingSet((AbstractDescriptorBean)this.getLog());
   }

   public void setLog(LogMBean var1) throws InvalidAttributeValueException {
      AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
      if (this._setParent(var2, this, 52)) {
         this._postCreate(var2);
      }

      LogMBean var3 = this._Log;
      this._Log = var1;
      this._postSet(52, var3, var1);
   }

   public boolean isStdoutEnabled() {
      return this._customizer.isStdoutEnabled();
   }

   public boolean isStdoutEnabledSet() {
      return this._isSet(53);
   }

   public void setStdoutEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this.isStdoutEnabled();
      this._customizer.setStdoutEnabled(var1);
      this._postSet(53, var2, var1);
   }

   public int getStdoutSeverityLevel() {
      return this._customizer.getStdoutSeverityLevel();
   }

   public boolean isStdoutSeverityLevelSet() {
      return this._isSet(54);
   }

   public void setStdoutSeverityLevel(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      int[] var2 = new int[]{256, 128, 64, 16, 8, 32, 4, 2, 1, 0};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StdoutSeverityLevel", var1, var2);
      int var3 = this.getStdoutSeverityLevel();
      this._customizer.setStdoutSeverityLevel(var1);
      this._postSet(54, var3, var1);
   }

   public boolean isStdoutDebugEnabled() {
      return this._customizer.isStdoutDebugEnabled();
   }

   public boolean isStdoutDebugEnabledSet() {
      return this._isSet(55);
   }

   public void setStdoutDebugEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this.isStdoutDebugEnabled();
      this._customizer.setStdoutDebugEnabled(var1);
      this._postSet(55, var2, var1);
   }

   public boolean isLogRemoteExceptionsEnabled() {
      return this._LogRemoteExceptionsEnabled;
   }

   public boolean isLogRemoteExceptionsEnabledSet() {
      return this._isSet(56);
   }

   public void setLogRemoteExceptionsEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this._LogRemoteExceptionsEnabled;
      this._LogRemoteExceptionsEnabled = var1;
      this._postSet(56, var2, var1);
   }

   public boolean isInstrumentStackTraceEnabled() {
      return this._InstrumentStackTraceEnabled;
   }

   public boolean isInstrumentStackTraceEnabledSet() {
      return this._isSet(57);
   }

   public void setInstrumentStackTraceEnabled(boolean var1) throws DistributedManagementException {
      boolean var2 = this._InstrumentStackTraceEnabled;
      this._InstrumentStackTraceEnabled = var1;
      this._postSet(57, var2, var1);
   }

   public void addExecuteQueue(ExecuteQueueMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 58)) {
         ExecuteQueueMBean[] var2;
         if (this._isSet(58)) {
            var2 = (ExecuteQueueMBean[])((ExecuteQueueMBean[])this._getHelper()._extendArray(this.getExecuteQueues(), ExecuteQueueMBean.class, var1));
         } else {
            var2 = new ExecuteQueueMBean[]{var1};
         }

         try {
            this.setExecuteQueues(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ExecuteQueueMBean[] getExecuteQueues() {
      return this._ExecuteQueues;
   }

   public boolean isExecuteQueuesSet() {
      return this._isSet(58);
   }

   public void removeExecuteQueue(ExecuteQueueMBean var1) {
      this.destroyExecuteQueue(var1);
   }

   public void setExecuteQueues(ExecuteQueueMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ExecuteQueueMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 58)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      ExecuteQueueMBean[] var5 = this._ExecuteQueues;
      this._ExecuteQueues = (ExecuteQueueMBean[])var4;
      this._postSet(58, var5, var4);
   }

   public int getMaxOpenSockCount() {
      return this._MaxOpenSockCount;
   }

   public boolean isMaxOpenSockCountSet() {
      return this._isSet(59);
   }

   public void setMaxOpenSockCount(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxOpenSockCount", (long)var1, -1L, 2147483647L);
      int var2 = this._MaxOpenSockCount;
      this._MaxOpenSockCount = var1;
      this._postSet(59, var2, var1);
   }

   public String getStdoutFormat() {
      return this._customizer.getStdoutFormat();
   }

   public boolean isStdoutFormatSet() {
      return this._isSet(60);
   }

   public void setStdoutFormat(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"standard", "noid"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("StdoutFormat", var1, var2);
      String var3 = this.getStdoutFormat();
      this._customizer.setStdoutFormat(var1);
      this._postSet(60, var3, var1);
   }

   public boolean isStdoutLogStack() {
      return this._customizer.isStdoutLogStack();
   }

   public boolean isStdoutLogStackSet() {
      return this._isSet(61);
   }

   public void setStdoutLogStack(boolean var1) {
      boolean var2 = this.isStdoutLogStack();
      this._customizer.setStdoutLogStack(var1);
      this._postSet(61, var2, var1);
   }

   public int getStuckThreadMaxTime() {
      return this._StuckThreadMaxTime;
   }

   public boolean isStuckThreadMaxTimeSet() {
      return this._isSet(62);
   }

   public void setStuckThreadMaxTime(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("StuckThreadMaxTime", (long)var1, 0L, 2147483647L);
      int var2 = this._StuckThreadMaxTime;
      this._StuckThreadMaxTime = var1;
      this._postSet(62, var2, var1);
   }

   public int getStuckThreadTimerInterval() {
      return this._StuckThreadTimerInterval;
   }

   public boolean isStuckThreadTimerIntervalSet() {
      return this._isSet(63);
   }

   public void setStuckThreadTimerInterval(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("StuckThreadTimerInterval", (long)var1, 0L, 2147483647L);
      int var2 = this._StuckThreadTimerInterval;
      this._StuckThreadTimerInterval = var1;
      this._postSet(63, var2, var1);
   }

   public boolean getTracingEnabled() {
      return this._TracingEnabled;
   }

   public boolean isTracingEnabledSet() {
      return this._isSet(64);
   }

   public void setTracingEnabled(boolean var1) {
      boolean var2 = this._TracingEnabled;
      this._TracingEnabled = var1;
      this._postSet(64, var2, var1);
   }

   public int getMessagingBridgeThreadPoolSize() {
      return this._MessagingBridgeThreadPoolSize;
   }

   public boolean isMessagingBridgeThreadPoolSizeSet() {
      return this._isSet(65);
   }

   public void setMessagingBridgeThreadPoolSize(int var1) throws InvalidAttributeValueException, DistributedManagementException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MessagingBridgeThreadPoolSize", (long)var1, -1L, 65534L);
      int var2 = this._MessagingBridgeThreadPoolSize;
      this._MessagingBridgeThreadPoolSize = var1;
      this._postSet(65, var2, var1);
   }

   public int getMTUSize() {
      return this._MTUSize;
   }

   public boolean isMTUSizeSet() {
      return this._isSet(66);
   }

   public void setMTUSize(int var1) {
      int var2 = this._MTUSize;
      this._MTUSize = var1;
      this._postSet(66, var2, var1);
   }

   public void setLoadStubUsingContextClassLoader(boolean var1) {
      boolean var2 = this._LoadStubUsingContextClassLoader;
      this._LoadStubUsingContextClassLoader = var1;
      this._postSet(67, var2, var1);
   }

   public boolean getLoadStubUsingContextClassLoader() {
      return this._LoadStubUsingContextClassLoader;
   }

   public boolean isLoadStubUsingContextClassLoaderSet() {
      return this._isSet(67);
   }

   public void setRefreshClientRuntimeDescriptor(boolean var1) {
      boolean var2 = this._RefreshClientRuntimeDescriptor;
      this._RefreshClientRuntimeDescriptor = var1;
      this._postSet(68, var2, var1);
   }

   public boolean getRefreshClientRuntimeDescriptor() {
      return this._RefreshClientRuntimeDescriptor;
   }

   public boolean isRefreshClientRuntimeDescriptorSet() {
      return this._isSet(68);
   }

   public void setTimedOutRefIsolationTime(long var1) {
      long var3 = this._TimedOutRefIsolationTime;
      this._TimedOutRefIsolationTime = var1;
      this._postSet(69, var3, var1);
   }

   public long getTimedOutRefIsolationTime() {
      return this._TimedOutRefIsolationTime;
   }

   public boolean isTimedOutRefIsolationTimeSet() {
      return this._isSet(69);
   }

   public void setUse81StyleExecuteQueues(boolean var1) {
      boolean var2 = this._Use81StyleExecuteQueues;
      this._Use81StyleExecuteQueues = var1;
      this._postSet(70, var2, var1);
   }

   public boolean getUse81StyleExecuteQueues() {
      return this._Use81StyleExecuteQueues;
   }

   public boolean isUse81StyleExecuteQueuesSet() {
      return this._isSet(70);
   }

   public void setT3ClientAbbrevTableSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("T3ClientAbbrevTableSize", (long)var1, 255L, 1024L);
      int var2 = this._T3ClientAbbrevTableSize;
      this._T3ClientAbbrevTableSize = var1;
      this._postSet(71, var2, var1);
   }

   public int getT3ClientAbbrevTableSize() {
      return this._T3ClientAbbrevTableSize;
   }

   public boolean isT3ClientAbbrevTableSizeSet() {
      return this._isSet(71);
   }

   public void setT3ServerAbbrevTableSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("T3ServerAbbrevTableSize", (long)var1, 255L, 10240L);
      int var2 = this._T3ServerAbbrevTableSize;
      this._T3ServerAbbrevTableSize = var1;
      this._postSet(72, var2, var1);
   }

   public int getT3ServerAbbrevTableSize() {
      return this._T3ServerAbbrevTableSize;
   }

   public boolean isT3ServerAbbrevTableSizeSet() {
      return this._isSet(72);
   }

   public ExecuteQueueMBean createExecuteQueue(String var1) {
      ExecuteQueueMBeanImpl var2 = new ExecuteQueueMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addExecuteQueue(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyExecuteQueue(ExecuteQueueMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 58);
         ExecuteQueueMBean[] var2 = this.getExecuteQueues();
         ExecuteQueueMBean[] var3 = (ExecuteQueueMBean[])((ExecuteQueueMBean[])this._getHelper()._removeElement(var2, ExecuteQueueMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setExecuteQueues(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public ExecuteQueueMBean lookupExecuteQueue(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ExecuteQueues).iterator();

      ExecuteQueueMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ExecuteQueueMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void setGatheredWritesEnabled(boolean var1) {
      boolean var2 = this._GatheredWritesEnabled;
      this._GatheredWritesEnabled = var1;
      this._postSet(73, var2, var1);
   }

   public boolean isGatheredWritesEnabled() {
      if (!this._isSet(73)) {
         try {
            return ((DomainMBean)this.getParent()).isExalogicOptimizationsEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._GatheredWritesEnabled;
   }

   public boolean isGatheredWritesEnabledSet() {
      return this._isSet(73);
   }

   public void setScatteredReadsEnabled(boolean var1) {
      boolean var2 = this._ScatteredReadsEnabled;
      this._ScatteredReadsEnabled = var1;
      this._postSet(74, var2, var1);
   }

   public boolean isScatteredReadsEnabled() {
      if (!this._isSet(74)) {
         try {
            return ((DomainMBean)this.getParent()).isExalogicOptimizationsEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._ScatteredReadsEnabled;
   }

   public boolean isScatteredReadsEnabledSet() {
      return this._isSet(74);
   }

   public void setAddWorkManagerThreadsByCpuCount(boolean var1) {
      boolean var2 = this._AddWorkManagerThreadsByCpuCount;
      this._AddWorkManagerThreadsByCpuCount = var1;
      this._postSet(75, var2, var1);
   }

   public boolean isAddWorkManagerThreadsByCpuCount() {
      if (!this._isSet(75)) {
         try {
            return ((DomainMBean)this.getParent()).isExalogicOptimizationsEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._AddWorkManagerThreadsByCpuCount;
   }

   public boolean isAddWorkManagerThreadsByCpuCountSet() {
      return this._isSet(75);
   }

   public void setUseConcurrentQueueForRequestManager(boolean var1) {
      boolean var2 = this._UseConcurrentQueueForRequestManager;
      this._UseConcurrentQueueForRequestManager = var1;
      this._postSet(76, var2, var1);
   }

   public boolean isUseConcurrentQueueForRequestManager() {
      if (!this._isSet(76)) {
         try {
            return ((DomainMBean)this.getParent()).isExalogicOptimizationsEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._UseConcurrentQueueForRequestManager;
   }

   public boolean isUseConcurrentQueueForRequestManagerSet() {
      return this._isSet(76);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet() || this.isIIOPSet() || this.isLogSet() || this.isSSLSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 11;
      }

      try {
         switch (var1) {
            case 11:
               this._AdministrationProtocol = null;
               if (var2) {
                  break;
               }
            case 40:
               this._CompleteCOMMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 39:
               this._CompleteHTTPMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 43:
               this._CompleteIIOPMessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 37:
               this._CompleteMessageTimeout = 60;
               if (var2) {
                  break;
               }
            case 38:
               this._CompleteT3MessageTimeout = -1;
               if (var2) {
                  break;
               }
            case 36:
               this._ConnectTimeout = 0;
               if (var2) {
                  break;
               }
            case 49:
               this._DGCIdlePeriodsUntilTimeout = 5;
               if (var2) {
                  break;
               }
            case 32:
               this._DefaultGIOPMinorVersion = 2;
               if (var2) {
                  break;
               }
            case 9:
               this._DefaultProtocol = "t3";
               if (var2) {
                  break;
               }
            case 10:
               this._DefaultSecureProtocol = "t3s";
               if (var2) {
                  break;
               }
            case 58:
               this._ExecuteQueues = new ExecuteQueueMBean[0];
               if (var2) {
                  break;
               }
            case 51:
               this._IIOP = new IIOPMBeanImpl(this, 51);
               this._postCreate((AbstractDescriptorBean)this._IIOP);
               if (var2) {
                  break;
               }
            case 35:
               this._IIOPLocationForwardPolicy = "off";
               if (var2) {
                  break;
               }
            case 34:
               this._IIOPTxMechanism = "ots";
               if (var2) {
                  break;
               }
            case 41:
               this._IdleConnectionTimeout = 65;
               if (var2) {
                  break;
               }
            case 42:
               this._IdleIIOPConnectionTimeout = -1;
               if (var2) {
                  break;
               }
            case 45:
               this._IdlePeriodsUntilTimeout = 4;
               if (var2) {
                  break;
               }
            case 16:
               this._JMSThreadPoolSize = 15;
               if (var2) {
                  break;
               }
            case 48:
               this._KernelDebug = null;
               if (var2) {
                  break;
               }
            case 67:
               this._LoadStubUsingContextClassLoader = false;
               if (var2) {
                  break;
               }
            case 52:
               this._Log = new LogMBeanImpl(this, 52);
               this._postCreate((AbstractDescriptorBean)this._Log);
               if (var2) {
                  break;
               }
            case 66:
               this._MTUSize = 1500;
               if (var2) {
                  break;
               }
            case 30:
               this._MaxCOMMessageSize = -1;
               if (var2) {
                  break;
               }
            case 29:
               this._MaxHTTPMessageSize = -1;
               if (var2) {
                  break;
               }
            case 31:
               this._MaxIIOPMessageSize = -1;
               if (var2) {
                  break;
               }
            case 26:
               this._MaxMessageSize = 10000000;
               if (var2) {
                  break;
               }
            case 59:
               this._MaxOpenSockCount = -1;
               if (var2) {
                  break;
               }
            case 27:
               this._MaxT3MessageSize = -1;
               if (var2) {
                  break;
               }
            case 65:
               this._MessagingBridgeThreadPoolSize = 5;
               if (var2) {
                  break;
               }
            case 19:
               this._MuxerClass = null;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 44:
               this._PeriodLength = 60000;
               if (var2) {
                  break;
               }
            case 68:
               this._RefreshClientRuntimeDescriptor = false;
               if (var2) {
                  break;
               }
            case 47:
               this._ResponseTimeout = 0;
               if (var2) {
                  break;
               }
            case 46:
               this._RjvmIdleTimeout = 0;
               if (var2) {
                  break;
               }
            case 50:
               this._SSL = new SSLMBeanImpl(this, 50);
               this._postCreate((AbstractDescriptorBean)this._SSL);
               if (var2) {
                  break;
               }
            case 15:
               this._SelfTuningThreadPoolSizeMax = 400;
               if (var2) {
                  break;
               }
            case 14:
               this._SelfTuningThreadPoolSizeMin = 1;
               if (var2) {
                  break;
               }
            case 23:
               this._SocketReaderTimeoutMaxMillis = 100;
               if (var2) {
                  break;
               }
            case 22:
               this._SocketReaderTimeoutMinMillis = 10;
               if (var2) {
                  break;
               }
            case 20:
               this._SocketReaders = -1;
               if (var2) {
                  break;
               }
            case 60:
               this._customizer.setStdoutFormat("standard");
               if (var2) {
                  break;
               }
            case 54:
               this._customizer.setStdoutSeverityLevel(32);
               if (var2) {
                  break;
               }
            case 62:
               this._StuckThreadMaxTime = 600;
               if (var2) {
                  break;
               }
            case 63:
               this._StuckThreadTimerInterval = 60;
               if (var2) {
                  break;
               }
            case 13:
               this._SystemThreadPoolSize = 5;
               if (var2) {
                  break;
               }
            case 71:
               this._T3ClientAbbrevTableSize = 255;
               if (var2) {
                  break;
               }
            case 72:
               this._T3ServerAbbrevTableSize = 2048;
               if (var2) {
                  break;
               }
            case 21:
               this._ThreadPoolPercentSocketReaders = 33;
               if (var2) {
                  break;
               }
            case 12:
               this._ThreadPoolSize = 15;
               if (var2) {
                  break;
               }
            case 69:
               this._TimedOutRefIsolationTime = 0L;
               if (var2) {
                  break;
               }
            case 64:
               this._TracingEnabled = false;
               if (var2) {
                  break;
               }
            case 70:
               this._Use81StyleExecuteQueues = false;
               if (var2) {
                  break;
               }
            case 33:
               this._UseIIOPLocateRequest = false;
               if (var2) {
                  break;
               }
            case 7:
               this._ValidProtocols = null;
               if (var2) {
                  break;
               }
            case 75:
               this._AddWorkManagerThreadsByCpuCount = false;
               if (var2) {
                  break;
               }
            case 18:
               this._DevPollDisabled = false;
               if (var2) {
                  break;
               }
            case 73:
               this._GatheredWritesEnabled = false;
               if (var2) {
                  break;
               }
            case 57:
               this._InstrumentStackTraceEnabled = true;
               if (var2) {
                  break;
               }
            case 56:
               this._LogRemoteExceptionsEnabled = false;
               if (var2) {
                  break;
               }
            case 17:
               this._NativeIOEnabled = true;
               if (var2) {
                  break;
               }
            case 24:
               this._OutboundEnabled = false;
               if (var2) {
                  break;
               }
            case 25:
               this._OutboundPrivateKeyEnabled = false;
               if (var2) {
                  break;
               }
            case 8:
               this._ReverseDNSAllowed = false;
               if (var2) {
                  break;
               }
            case 74:
               this._ScatteredReadsEnabled = false;
               if (var2) {
                  break;
               }
            case 28:
               this._SocketBufferSizeAsChunkSize = false;
               if (var2) {
                  break;
               }
            case 55:
               this._customizer.setStdoutDebugEnabled(false);
               if (var2) {
                  break;
               }
            case 53:
               this._customizer.setStdoutEnabled(true);
               if (var2) {
                  break;
               }
            case 61:
               this._customizer.setStdoutLogStack(true);
               if (var2) {
                  break;
               }
            case 76:
               this._UseConcurrentQueueForRequestManager = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "Kernel";
   }

   public void putValue(String var1, Object var2) {
      boolean var6;
      if (var1.equals("AddWorkManagerThreadsByCpuCount")) {
         var6 = this._AddWorkManagerThreadsByCpuCount;
         this._AddWorkManagerThreadsByCpuCount = (Boolean)var2;
         this._postSet(75, var6, this._AddWorkManagerThreadsByCpuCount);
      } else {
         String var9;
         if (var1.equals("AdministrationProtocol")) {
            var9 = this._AdministrationProtocol;
            this._AdministrationProtocol = (String)var2;
            this._postSet(11, var9, this._AdministrationProtocol);
         } else {
            int var8;
            if (var1.equals("CompleteCOMMessageTimeout")) {
               var8 = this._CompleteCOMMessageTimeout;
               this._CompleteCOMMessageTimeout = (Integer)var2;
               this._postSet(40, var8, this._CompleteCOMMessageTimeout);
            } else if (var1.equals("CompleteHTTPMessageTimeout")) {
               var8 = this._CompleteHTTPMessageTimeout;
               this._CompleteHTTPMessageTimeout = (Integer)var2;
               this._postSet(39, var8, this._CompleteHTTPMessageTimeout);
            } else if (var1.equals("CompleteIIOPMessageTimeout")) {
               var8 = this._CompleteIIOPMessageTimeout;
               this._CompleteIIOPMessageTimeout = (Integer)var2;
               this._postSet(43, var8, this._CompleteIIOPMessageTimeout);
            } else if (var1.equals("CompleteMessageTimeout")) {
               var8 = this._CompleteMessageTimeout;
               this._CompleteMessageTimeout = (Integer)var2;
               this._postSet(37, var8, this._CompleteMessageTimeout);
            } else if (var1.equals("CompleteT3MessageTimeout")) {
               var8 = this._CompleteT3MessageTimeout;
               this._CompleteT3MessageTimeout = (Integer)var2;
               this._postSet(38, var8, this._CompleteT3MessageTimeout);
            } else if (var1.equals("ConnectTimeout")) {
               var8 = this._ConnectTimeout;
               this._ConnectTimeout = (Integer)var2;
               this._postSet(36, var8, this._ConnectTimeout);
            } else if (var1.equals("DGCIdlePeriodsUntilTimeout")) {
               var8 = this._DGCIdlePeriodsUntilTimeout;
               this._DGCIdlePeriodsUntilTimeout = (Integer)var2;
               this._postSet(49, var8, this._DGCIdlePeriodsUntilTimeout);
            } else if (var1.equals("DefaultGIOPMinorVersion")) {
               var8 = this._DefaultGIOPMinorVersion;
               this._DefaultGIOPMinorVersion = (Integer)var2;
               this._postSet(32, var8, this._DefaultGIOPMinorVersion);
            } else if (var1.equals("DefaultProtocol")) {
               var9 = this._DefaultProtocol;
               this._DefaultProtocol = (String)var2;
               this._postSet(9, var9, this._DefaultProtocol);
            } else if (var1.equals("DefaultSecureProtocol")) {
               var9 = this._DefaultSecureProtocol;
               this._DefaultSecureProtocol = (String)var2;
               this._postSet(10, var9, this._DefaultSecureProtocol);
            } else if (var1.equals("DevPollDisabled")) {
               var6 = this._DevPollDisabled;
               this._DevPollDisabled = (Boolean)var2;
               this._postSet(18, var6, this._DevPollDisabled);
            } else if (var1.equals("ExecuteQueues")) {
               ExecuteQueueMBean[] var14 = this._ExecuteQueues;
               this._ExecuteQueues = (ExecuteQueueMBean[])((ExecuteQueueMBean[])var2);
               this._postSet(58, var14, this._ExecuteQueues);
            } else if (var1.equals("GatheredWritesEnabled")) {
               var6 = this._GatheredWritesEnabled;
               this._GatheredWritesEnabled = (Boolean)var2;
               this._postSet(73, var6, this._GatheredWritesEnabled);
            } else if (var1.equals("IIOP")) {
               IIOPMBean var13 = this._IIOP;
               this._IIOP = (IIOPMBean)var2;
               this._postSet(51, var13, this._IIOP);
            } else if (var1.equals("IIOPLocationForwardPolicy")) {
               var9 = this._IIOPLocationForwardPolicy;
               this._IIOPLocationForwardPolicy = (String)var2;
               this._postSet(35, var9, this._IIOPLocationForwardPolicy);
            } else if (var1.equals("IIOPTxMechanism")) {
               var9 = this._IIOPTxMechanism;
               this._IIOPTxMechanism = (String)var2;
               this._postSet(34, var9, this._IIOPTxMechanism);
            } else if (var1.equals("IdleConnectionTimeout")) {
               var8 = this._IdleConnectionTimeout;
               this._IdleConnectionTimeout = (Integer)var2;
               this._postSet(41, var8, this._IdleConnectionTimeout);
            } else if (var1.equals("IdleIIOPConnectionTimeout")) {
               var8 = this._IdleIIOPConnectionTimeout;
               this._IdleIIOPConnectionTimeout = (Integer)var2;
               this._postSet(42, var8, this._IdleIIOPConnectionTimeout);
            } else if (var1.equals("IdlePeriodsUntilTimeout")) {
               var8 = this._IdlePeriodsUntilTimeout;
               this._IdlePeriodsUntilTimeout = (Integer)var2;
               this._postSet(45, var8, this._IdlePeriodsUntilTimeout);
            } else if (var1.equals("InstrumentStackTraceEnabled")) {
               var6 = this._InstrumentStackTraceEnabled;
               this._InstrumentStackTraceEnabled = (Boolean)var2;
               this._postSet(57, var6, this._InstrumentStackTraceEnabled);
            } else if (var1.equals("JMSThreadPoolSize")) {
               var8 = this._JMSThreadPoolSize;
               this._JMSThreadPoolSize = (Integer)var2;
               this._postSet(16, var8, this._JMSThreadPoolSize);
            } else if (var1.equals("KernelDebug")) {
               KernelDebugMBean var12 = this._KernelDebug;
               this._KernelDebug = (KernelDebugMBean)var2;
               this._postSet(48, var12, this._KernelDebug);
            } else if (var1.equals("LoadStubUsingContextClassLoader")) {
               var6 = this._LoadStubUsingContextClassLoader;
               this._LoadStubUsingContextClassLoader = (Boolean)var2;
               this._postSet(67, var6, this._LoadStubUsingContextClassLoader);
            } else if (var1.equals("Log")) {
               LogMBean var11 = this._Log;
               this._Log = (LogMBean)var2;
               this._postSet(52, var11, this._Log);
            } else if (var1.equals("LogRemoteExceptionsEnabled")) {
               var6 = this._LogRemoteExceptionsEnabled;
               this._LogRemoteExceptionsEnabled = (Boolean)var2;
               this._postSet(56, var6, this._LogRemoteExceptionsEnabled);
            } else if (var1.equals("MTUSize")) {
               var8 = this._MTUSize;
               this._MTUSize = (Integer)var2;
               this._postSet(66, var8, this._MTUSize);
            } else if (var1.equals("MaxCOMMessageSize")) {
               var8 = this._MaxCOMMessageSize;
               this._MaxCOMMessageSize = (Integer)var2;
               this._postSet(30, var8, this._MaxCOMMessageSize);
            } else if (var1.equals("MaxHTTPMessageSize")) {
               var8 = this._MaxHTTPMessageSize;
               this._MaxHTTPMessageSize = (Integer)var2;
               this._postSet(29, var8, this._MaxHTTPMessageSize);
            } else if (var1.equals("MaxIIOPMessageSize")) {
               var8 = this._MaxIIOPMessageSize;
               this._MaxIIOPMessageSize = (Integer)var2;
               this._postSet(31, var8, this._MaxIIOPMessageSize);
            } else if (var1.equals("MaxMessageSize")) {
               var8 = this._MaxMessageSize;
               this._MaxMessageSize = (Integer)var2;
               this._postSet(26, var8, this._MaxMessageSize);
            } else if (var1.equals("MaxOpenSockCount")) {
               var8 = this._MaxOpenSockCount;
               this._MaxOpenSockCount = (Integer)var2;
               this._postSet(59, var8, this._MaxOpenSockCount);
            } else if (var1.equals("MaxT3MessageSize")) {
               var8 = this._MaxT3MessageSize;
               this._MaxT3MessageSize = (Integer)var2;
               this._postSet(27, var8, this._MaxT3MessageSize);
            } else if (var1.equals("MessagingBridgeThreadPoolSize")) {
               var8 = this._MessagingBridgeThreadPoolSize;
               this._MessagingBridgeThreadPoolSize = (Integer)var2;
               this._postSet(65, var8, this._MessagingBridgeThreadPoolSize);
            } else if (var1.equals("MuxerClass")) {
               var9 = this._MuxerClass;
               this._MuxerClass = (String)var2;
               this._postSet(19, var9, this._MuxerClass);
            } else if (var1.equals("Name")) {
               var9 = this._Name;
               this._Name = (String)var2;
               this._postSet(2, var9, this._Name);
            } else if (var1.equals("NativeIOEnabled")) {
               var6 = this._NativeIOEnabled;
               this._NativeIOEnabled = (Boolean)var2;
               this._postSet(17, var6, this._NativeIOEnabled);
            } else if (var1.equals("OutboundEnabled")) {
               var6 = this._OutboundEnabled;
               this._OutboundEnabled = (Boolean)var2;
               this._postSet(24, var6, this._OutboundEnabled);
            } else if (var1.equals("OutboundPrivateKeyEnabled")) {
               var6 = this._OutboundPrivateKeyEnabled;
               this._OutboundPrivateKeyEnabled = (Boolean)var2;
               this._postSet(25, var6, this._OutboundPrivateKeyEnabled);
            } else if (var1.equals("PeriodLength")) {
               var8 = this._PeriodLength;
               this._PeriodLength = (Integer)var2;
               this._postSet(44, var8, this._PeriodLength);
            } else if (var1.equals("RefreshClientRuntimeDescriptor")) {
               var6 = this._RefreshClientRuntimeDescriptor;
               this._RefreshClientRuntimeDescriptor = (Boolean)var2;
               this._postSet(68, var6, this._RefreshClientRuntimeDescriptor);
            } else if (var1.equals("ResponseTimeout")) {
               var8 = this._ResponseTimeout;
               this._ResponseTimeout = (Integer)var2;
               this._postSet(47, var8, this._ResponseTimeout);
            } else if (var1.equals("ReverseDNSAllowed")) {
               var6 = this._ReverseDNSAllowed;
               this._ReverseDNSAllowed = (Boolean)var2;
               this._postSet(8, var6, this._ReverseDNSAllowed);
            } else if (var1.equals("RjvmIdleTimeout")) {
               var8 = this._RjvmIdleTimeout;
               this._RjvmIdleTimeout = (Integer)var2;
               this._postSet(46, var8, this._RjvmIdleTimeout);
            } else if (var1.equals("SSL")) {
               SSLMBean var10 = this._SSL;
               this._SSL = (SSLMBean)var2;
               this._postSet(50, var10, this._SSL);
            } else if (var1.equals("ScatteredReadsEnabled")) {
               var6 = this._ScatteredReadsEnabled;
               this._ScatteredReadsEnabled = (Boolean)var2;
               this._postSet(74, var6, this._ScatteredReadsEnabled);
            } else if (var1.equals("SelfTuningThreadPoolSizeMax")) {
               var8 = this._SelfTuningThreadPoolSizeMax;
               this._SelfTuningThreadPoolSizeMax = (Integer)var2;
               this._postSet(15, var8, this._SelfTuningThreadPoolSizeMax);
            } else if (var1.equals("SelfTuningThreadPoolSizeMin")) {
               var8 = this._SelfTuningThreadPoolSizeMin;
               this._SelfTuningThreadPoolSizeMin = (Integer)var2;
               this._postSet(14, var8, this._SelfTuningThreadPoolSizeMin);
            } else if (var1.equals("SocketBufferSizeAsChunkSize")) {
               var6 = this._SocketBufferSizeAsChunkSize;
               this._SocketBufferSizeAsChunkSize = (Boolean)var2;
               this._postSet(28, var6, this._SocketBufferSizeAsChunkSize);
            } else if (var1.equals("SocketReaderTimeoutMaxMillis")) {
               var8 = this._SocketReaderTimeoutMaxMillis;
               this._SocketReaderTimeoutMaxMillis = (Integer)var2;
               this._postSet(23, var8, this._SocketReaderTimeoutMaxMillis);
            } else if (var1.equals("SocketReaderTimeoutMinMillis")) {
               var8 = this._SocketReaderTimeoutMinMillis;
               this._SocketReaderTimeoutMinMillis = (Integer)var2;
               this._postSet(22, var8, this._SocketReaderTimeoutMinMillis);
            } else if (var1.equals("SocketReaders")) {
               var8 = this._SocketReaders;
               this._SocketReaders = (Integer)var2;
               this._postSet(20, var8, this._SocketReaders);
            } else if (var1.equals("StdoutDebugEnabled")) {
               var6 = this._StdoutDebugEnabled;
               this._StdoutDebugEnabled = (Boolean)var2;
               this._postSet(55, var6, this._StdoutDebugEnabled);
            } else if (var1.equals("StdoutEnabled")) {
               var6 = this._StdoutEnabled;
               this._StdoutEnabled = (Boolean)var2;
               this._postSet(53, var6, this._StdoutEnabled);
            } else if (var1.equals("StdoutFormat")) {
               var9 = this._StdoutFormat;
               this._StdoutFormat = (String)var2;
               this._postSet(60, var9, this._StdoutFormat);
            } else if (var1.equals("StdoutLogStack")) {
               var6 = this._StdoutLogStack;
               this._StdoutLogStack = (Boolean)var2;
               this._postSet(61, var6, this._StdoutLogStack);
            } else if (var1.equals("StdoutSeverityLevel")) {
               var8 = this._StdoutSeverityLevel;
               this._StdoutSeverityLevel = (Integer)var2;
               this._postSet(54, var8, this._StdoutSeverityLevel);
            } else if (var1.equals("StuckThreadMaxTime")) {
               var8 = this._StuckThreadMaxTime;
               this._StuckThreadMaxTime = (Integer)var2;
               this._postSet(62, var8, this._StuckThreadMaxTime);
            } else if (var1.equals("StuckThreadTimerInterval")) {
               var8 = this._StuckThreadTimerInterval;
               this._StuckThreadTimerInterval = (Integer)var2;
               this._postSet(63, var8, this._StuckThreadTimerInterval);
            } else if (var1.equals("SystemThreadPoolSize")) {
               var8 = this._SystemThreadPoolSize;
               this._SystemThreadPoolSize = (Integer)var2;
               this._postSet(13, var8, this._SystemThreadPoolSize);
            } else if (var1.equals("T3ClientAbbrevTableSize")) {
               var8 = this._T3ClientAbbrevTableSize;
               this._T3ClientAbbrevTableSize = (Integer)var2;
               this._postSet(71, var8, this._T3ClientAbbrevTableSize);
            } else if (var1.equals("T3ServerAbbrevTableSize")) {
               var8 = this._T3ServerAbbrevTableSize;
               this._T3ServerAbbrevTableSize = (Integer)var2;
               this._postSet(72, var8, this._T3ServerAbbrevTableSize);
            } else if (var1.equals("ThreadPoolPercentSocketReaders")) {
               var8 = this._ThreadPoolPercentSocketReaders;
               this._ThreadPoolPercentSocketReaders = (Integer)var2;
               this._postSet(21, var8, this._ThreadPoolPercentSocketReaders);
            } else if (var1.equals("ThreadPoolSize")) {
               var8 = this._ThreadPoolSize;
               this._ThreadPoolSize = (Integer)var2;
               this._postSet(12, var8, this._ThreadPoolSize);
            } else if (var1.equals("TimedOutRefIsolationTime")) {
               long var7 = this._TimedOutRefIsolationTime;
               this._TimedOutRefIsolationTime = (Long)var2;
               this._postSet(69, var7, this._TimedOutRefIsolationTime);
            } else if (var1.equals("TracingEnabled")) {
               var6 = this._TracingEnabled;
               this._TracingEnabled = (Boolean)var2;
               this._postSet(64, var6, this._TracingEnabled);
            } else if (var1.equals("Use81StyleExecuteQueues")) {
               var6 = this._Use81StyleExecuteQueues;
               this._Use81StyleExecuteQueues = (Boolean)var2;
               this._postSet(70, var6, this._Use81StyleExecuteQueues);
            } else if (var1.equals("UseConcurrentQueueForRequestManager")) {
               var6 = this._UseConcurrentQueueForRequestManager;
               this._UseConcurrentQueueForRequestManager = (Boolean)var2;
               this._postSet(76, var6, this._UseConcurrentQueueForRequestManager);
            } else if (var1.equals("UseIIOPLocateRequest")) {
               var6 = this._UseIIOPLocateRequest;
               this._UseIIOPLocateRequest = (Boolean)var2;
               this._postSet(33, var6, this._UseIIOPLocateRequest);
            } else if (var1.equals("ValidProtocols")) {
               Map var5 = this._ValidProtocols;
               this._ValidProtocols = (Map)var2;
               this._postSet(7, var5, this._ValidProtocols);
            } else if (var1.equals("customizer")) {
               Kernel var3 = this._customizer;
               this._customizer = (Kernel)var2;
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AddWorkManagerThreadsByCpuCount")) {
         return new Boolean(this._AddWorkManagerThreadsByCpuCount);
      } else if (var1.equals("AdministrationProtocol")) {
         return this._AdministrationProtocol;
      } else if (var1.equals("CompleteCOMMessageTimeout")) {
         return new Integer(this._CompleteCOMMessageTimeout);
      } else if (var1.equals("CompleteHTTPMessageTimeout")) {
         return new Integer(this._CompleteHTTPMessageTimeout);
      } else if (var1.equals("CompleteIIOPMessageTimeout")) {
         return new Integer(this._CompleteIIOPMessageTimeout);
      } else if (var1.equals("CompleteMessageTimeout")) {
         return new Integer(this._CompleteMessageTimeout);
      } else if (var1.equals("CompleteT3MessageTimeout")) {
         return new Integer(this._CompleteT3MessageTimeout);
      } else if (var1.equals("ConnectTimeout")) {
         return new Integer(this._ConnectTimeout);
      } else if (var1.equals("DGCIdlePeriodsUntilTimeout")) {
         return new Integer(this._DGCIdlePeriodsUntilTimeout);
      } else if (var1.equals("DefaultGIOPMinorVersion")) {
         return new Integer(this._DefaultGIOPMinorVersion);
      } else if (var1.equals("DefaultProtocol")) {
         return this._DefaultProtocol;
      } else if (var1.equals("DefaultSecureProtocol")) {
         return this._DefaultSecureProtocol;
      } else if (var1.equals("DevPollDisabled")) {
         return new Boolean(this._DevPollDisabled);
      } else if (var1.equals("ExecuteQueues")) {
         return this._ExecuteQueues;
      } else if (var1.equals("GatheredWritesEnabled")) {
         return new Boolean(this._GatheredWritesEnabled);
      } else if (var1.equals("IIOP")) {
         return this._IIOP;
      } else if (var1.equals("IIOPLocationForwardPolicy")) {
         return this._IIOPLocationForwardPolicy;
      } else if (var1.equals("IIOPTxMechanism")) {
         return this._IIOPTxMechanism;
      } else if (var1.equals("IdleConnectionTimeout")) {
         return new Integer(this._IdleConnectionTimeout);
      } else if (var1.equals("IdleIIOPConnectionTimeout")) {
         return new Integer(this._IdleIIOPConnectionTimeout);
      } else if (var1.equals("IdlePeriodsUntilTimeout")) {
         return new Integer(this._IdlePeriodsUntilTimeout);
      } else if (var1.equals("InstrumentStackTraceEnabled")) {
         return new Boolean(this._InstrumentStackTraceEnabled);
      } else if (var1.equals("JMSThreadPoolSize")) {
         return new Integer(this._JMSThreadPoolSize);
      } else if (var1.equals("KernelDebug")) {
         return this._KernelDebug;
      } else if (var1.equals("LoadStubUsingContextClassLoader")) {
         return new Boolean(this._LoadStubUsingContextClassLoader);
      } else if (var1.equals("Log")) {
         return this._Log;
      } else if (var1.equals("LogRemoteExceptionsEnabled")) {
         return new Boolean(this._LogRemoteExceptionsEnabled);
      } else if (var1.equals("MTUSize")) {
         return new Integer(this._MTUSize);
      } else if (var1.equals("MaxCOMMessageSize")) {
         return new Integer(this._MaxCOMMessageSize);
      } else if (var1.equals("MaxHTTPMessageSize")) {
         return new Integer(this._MaxHTTPMessageSize);
      } else if (var1.equals("MaxIIOPMessageSize")) {
         return new Integer(this._MaxIIOPMessageSize);
      } else if (var1.equals("MaxMessageSize")) {
         return new Integer(this._MaxMessageSize);
      } else if (var1.equals("MaxOpenSockCount")) {
         return new Integer(this._MaxOpenSockCount);
      } else if (var1.equals("MaxT3MessageSize")) {
         return new Integer(this._MaxT3MessageSize);
      } else if (var1.equals("MessagingBridgeThreadPoolSize")) {
         return new Integer(this._MessagingBridgeThreadPoolSize);
      } else if (var1.equals("MuxerClass")) {
         return this._MuxerClass;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("NativeIOEnabled")) {
         return new Boolean(this._NativeIOEnabled);
      } else if (var1.equals("OutboundEnabled")) {
         return new Boolean(this._OutboundEnabled);
      } else if (var1.equals("OutboundPrivateKeyEnabled")) {
         return new Boolean(this._OutboundPrivateKeyEnabled);
      } else if (var1.equals("PeriodLength")) {
         return new Integer(this._PeriodLength);
      } else if (var1.equals("RefreshClientRuntimeDescriptor")) {
         return new Boolean(this._RefreshClientRuntimeDescriptor);
      } else if (var1.equals("ResponseTimeout")) {
         return new Integer(this._ResponseTimeout);
      } else if (var1.equals("ReverseDNSAllowed")) {
         return new Boolean(this._ReverseDNSAllowed);
      } else if (var1.equals("RjvmIdleTimeout")) {
         return new Integer(this._RjvmIdleTimeout);
      } else if (var1.equals("SSL")) {
         return this._SSL;
      } else if (var1.equals("ScatteredReadsEnabled")) {
         return new Boolean(this._ScatteredReadsEnabled);
      } else if (var1.equals("SelfTuningThreadPoolSizeMax")) {
         return new Integer(this._SelfTuningThreadPoolSizeMax);
      } else if (var1.equals("SelfTuningThreadPoolSizeMin")) {
         return new Integer(this._SelfTuningThreadPoolSizeMin);
      } else if (var1.equals("SocketBufferSizeAsChunkSize")) {
         return new Boolean(this._SocketBufferSizeAsChunkSize);
      } else if (var1.equals("SocketReaderTimeoutMaxMillis")) {
         return new Integer(this._SocketReaderTimeoutMaxMillis);
      } else if (var1.equals("SocketReaderTimeoutMinMillis")) {
         return new Integer(this._SocketReaderTimeoutMinMillis);
      } else if (var1.equals("SocketReaders")) {
         return new Integer(this._SocketReaders);
      } else if (var1.equals("StdoutDebugEnabled")) {
         return new Boolean(this._StdoutDebugEnabled);
      } else if (var1.equals("StdoutEnabled")) {
         return new Boolean(this._StdoutEnabled);
      } else if (var1.equals("StdoutFormat")) {
         return this._StdoutFormat;
      } else if (var1.equals("StdoutLogStack")) {
         return new Boolean(this._StdoutLogStack);
      } else if (var1.equals("StdoutSeverityLevel")) {
         return new Integer(this._StdoutSeverityLevel);
      } else if (var1.equals("StuckThreadMaxTime")) {
         return new Integer(this._StuckThreadMaxTime);
      } else if (var1.equals("StuckThreadTimerInterval")) {
         return new Integer(this._StuckThreadTimerInterval);
      } else if (var1.equals("SystemThreadPoolSize")) {
         return new Integer(this._SystemThreadPoolSize);
      } else if (var1.equals("T3ClientAbbrevTableSize")) {
         return new Integer(this._T3ClientAbbrevTableSize);
      } else if (var1.equals("T3ServerAbbrevTableSize")) {
         return new Integer(this._T3ServerAbbrevTableSize);
      } else if (var1.equals("ThreadPoolPercentSocketReaders")) {
         return new Integer(this._ThreadPoolPercentSocketReaders);
      } else if (var1.equals("ThreadPoolSize")) {
         return new Integer(this._ThreadPoolSize);
      } else if (var1.equals("TimedOutRefIsolationTime")) {
         return new Long(this._TimedOutRefIsolationTime);
      } else if (var1.equals("TracingEnabled")) {
         return new Boolean(this._TracingEnabled);
      } else if (var1.equals("Use81StyleExecuteQueues")) {
         return new Boolean(this._Use81StyleExecuteQueues);
      } else if (var1.equals("UseConcurrentQueueForRequestManager")) {
         return new Boolean(this._UseConcurrentQueueForRequestManager);
      } else if (var1.equals("UseIIOPLocateRequest")) {
         return new Boolean(this._UseIIOPLocateRequest);
      } else if (var1.equals("ValidProtocols")) {
         return this._ValidProtocols;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 3:
               if (var1.equals("log")) {
                  return 52;
               }

               if (var1.equals("ssl")) {
                  return 50;
               }
               break;
            case 4:
               if (var1.equals("iiop")) {
                  return 51;
               }

               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 31:
            case 35:
            case 38:
            case 39:
            default:
               break;
            case 8:
               if (var1.equals("mtu-size")) {
                  return 66;
               }
               break;
            case 11:
               if (var1.equals("muxer-class")) {
                  return 19;
               }
               break;
            case 12:
               if (var1.equals("kernel-debug")) {
                  return 48;
               }
               break;
            case 13:
               if (var1.equals("execute-queue")) {
                  return 58;
               }

               if (var1.equals("period-length")) {
                  return 44;
               }

               if (var1.equals("stdout-format")) {
                  return 60;
               }
               break;
            case 14:
               if (var1.equals("socket-readers")) {
                  return 20;
               }

               if (var1.equals("stdout-enabled")) {
                  return 53;
               }
               break;
            case 15:
               if (var1.equals("connect-timeout")) {
                  return 36;
               }

               if (var1.equals("tracing-enabled")) {
                  return 64;
               }

               if (var1.equals("valid-protocols")) {
                  return 7;
               }
               break;
            case 16:
               if (var1.equals("default-protocol")) {
                  return 9;
               }

               if (var1.equals("max-message-size")) {
                  return 26;
               }

               if (var1.equals("response-timeout")) {
                  return 47;
               }

               if (var1.equals("thread-pool-size")) {
                  return 12;
               }

               if (var1.equals("nativeio-enabled")) {
                  return 17;
               }

               if (var1.equals("outbound-enabled")) {
                  return 24;
               }

               if (var1.equals("stdout-log-stack")) {
                  return 61;
               }
               break;
            case 17:
               if (var1.equals("iiop-tx-mechanism")) {
                  return 34;
               }

               if (var1.equals("rjvm-idle-timeout")) {
                  return 46;
               }

               if (var1.equals("dev-poll-disabled")) {
                  return 18;
               }
               break;
            case 18:
               if (var1.equals("maxt3-message-size")) {
                  return 27;
               }

               if (var1.equals("reversedns-allowed")) {
                  return 8;
               }
               break;
            case 19:
               if (var1.equals("maxcom-message-size")) {
                  return 30;
               }

               if (var1.equals("max-open-sock-count")) {
                  return 59;
               }
               break;
            case 20:
               if (var1.equals("jms-thread-pool-size")) {
                  return 16;
               }

               if (var1.equals("maxhttp-message-size")) {
                  return 29;
               }

               if (var1.equals("maxiiop-message-size")) {
                  return 31;
               }

               if (var1.equals("stdout-debug-enabled")) {
                  return 55;
               }
               break;
            case 21:
               if (var1.equals("stdout-severity-level")) {
                  return 54;
               }

               if (var1.equals("stuck-thread-max-time")) {
                  return 62;
               }
               break;
            case 22:
               if (var1.equals("useiiop-locate-request")) {
                  return 33;
               }
               break;
            case 23:
               if (var1.equals("administration-protocol")) {
                  return 11;
               }

               if (var1.equals("default-secure-protocol")) {
                  return 10;
               }

               if (var1.equals("idle-connection-timeout")) {
                  return 41;
               }

               if (var1.equals("system-thread-pool-size")) {
                  return 13;
               }

               if (var1.equals("gathered-writes-enabled")) {
                  return 73;
               }

               if (var1.equals("scattered-reads-enabled")) {
                  return 74;
               }
               break;
            case 24:
               if (var1.equals("complete-message-timeout")) {
                  return 37;
               }
               break;
            case 25:
               if (var1.equals("defaultgiop-minor-version")) {
                  return 32;
               }
               break;
            case 26:
               if (var1.equals("completet3-message-timeout")) {
                  return 38;
               }

               if (var1.equals("idle-periods-until-timeout")) {
                  return 45;
               }

               if (var1.equals("use81-style-execute-queues")) {
                  return 70;
               }
               break;
            case 27:
               if (var1.equals("completecom-message-timeout")) {
                  return 40;
               }

               if (var1.equals("idleiiop-connection-timeout")) {
                  return 42;
               }

               if (var1.equals("stuck-thread-timer-interval")) {
                  return 63;
               }

               if (var1.equals("t3-client-abbrev-table-size")) {
                  return 71;
               }

               if (var1.equals("t3-server-abbrev-table-size")) {
                  return 72;
               }
               break;
            case 28:
               if (var1.equals("completehttp-message-timeout")) {
                  return 39;
               }

               if (var1.equals("completeiiop-message-timeout")) {
                  return 43;
               }

               if (var1.equals("iiop-location-forward-policy")) {
                  return 35;
               }

               if (var1.equals("timed-out-ref-isolation-time")) {
                  return 69;
               }

               if (var1.equals("outbound-private-key-enabled")) {
                  return 25;
               }
               break;
            case 29:
               if (var1.equals("log-remote-exceptions-enabled")) {
                  return 56;
               }
               break;
            case 30:
               if (var1.equals("dgc-idle-periods-until-timeout")) {
                  return 49;
               }

               if (var1.equals("instrument-stack-trace-enabled")) {
                  return 57;
               }
               break;
            case 32:
               if (var1.equals("self-tuning-thread-pool-size-max")) {
                  return 15;
               }

               if (var1.equals("self-tuning-thread-pool-size-min")) {
                  return 14;
               }

               if (var1.equals("socket-reader-timeout-max-millis")) {
                  return 23;
               }

               if (var1.equals("socket-reader-timeout-min-millis")) {
                  return 22;
               }

               if (var1.equals("socket-buffer-size-as-chunk-size")) {
                  return 28;
               }
               break;
            case 33:
               if (var1.equals("messaging-bridge-thread-pool-size")) {
                  return 65;
               }

               if (var1.equals("refresh-client-runtime-descriptor")) {
                  return 68;
               }
               break;
            case 34:
               if (var1.equals("thread-pool-percent-socket-readers")) {
                  return 21;
               }
               break;
            case 36:
               if (var1.equals("load-stub-using-context-class-loader")) {
                  return 67;
               }
               break;
            case 37:
               if (var1.equals("add-work-manager-threads-by-cpu-count")) {
                  return 75;
               }
               break;
            case 40:
               if (var1.equals("use-concurrent-queue-for-request-manager")) {
                  return 76;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 50:
               return new SSLMBeanImpl.SchemaHelper2();
            case 51:
               return new IIOPMBeanImpl.SchemaHelper2();
            case 52:
               return new LogMBeanImpl.SchemaHelper2();
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            default:
               return super.getSchemaHelper(var1);
            case 58:
               return new ExecuteQueueMBeanImpl.SchemaHelper2();
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "valid-protocols";
            case 8:
               return "reversedns-allowed";
            case 9:
               return "default-protocol";
            case 10:
               return "default-secure-protocol";
            case 11:
               return "administration-protocol";
            case 12:
               return "thread-pool-size";
            case 13:
               return "system-thread-pool-size";
            case 14:
               return "self-tuning-thread-pool-size-min";
            case 15:
               return "self-tuning-thread-pool-size-max";
            case 16:
               return "jms-thread-pool-size";
            case 17:
               return "nativeio-enabled";
            case 18:
               return "dev-poll-disabled";
            case 19:
               return "muxer-class";
            case 20:
               return "socket-readers";
            case 21:
               return "thread-pool-percent-socket-readers";
            case 22:
               return "socket-reader-timeout-min-millis";
            case 23:
               return "socket-reader-timeout-max-millis";
            case 24:
               return "outbound-enabled";
            case 25:
               return "outbound-private-key-enabled";
            case 26:
               return "max-message-size";
            case 27:
               return "maxt3-message-size";
            case 28:
               return "socket-buffer-size-as-chunk-size";
            case 29:
               return "maxhttp-message-size";
            case 30:
               return "maxcom-message-size";
            case 31:
               return "maxiiop-message-size";
            case 32:
               return "defaultgiop-minor-version";
            case 33:
               return "useiiop-locate-request";
            case 34:
               return "iiop-tx-mechanism";
            case 35:
               return "iiop-location-forward-policy";
            case 36:
               return "connect-timeout";
            case 37:
               return "complete-message-timeout";
            case 38:
               return "completet3-message-timeout";
            case 39:
               return "completehttp-message-timeout";
            case 40:
               return "completecom-message-timeout";
            case 41:
               return "idle-connection-timeout";
            case 42:
               return "idleiiop-connection-timeout";
            case 43:
               return "completeiiop-message-timeout";
            case 44:
               return "period-length";
            case 45:
               return "idle-periods-until-timeout";
            case 46:
               return "rjvm-idle-timeout";
            case 47:
               return "response-timeout";
            case 48:
               return "kernel-debug";
            case 49:
               return "dgc-idle-periods-until-timeout";
            case 50:
               return "ssl";
            case 51:
               return "iiop";
            case 52:
               return "log";
            case 53:
               return "stdout-enabled";
            case 54:
               return "stdout-severity-level";
            case 55:
               return "stdout-debug-enabled";
            case 56:
               return "log-remote-exceptions-enabled";
            case 57:
               return "instrument-stack-trace-enabled";
            case 58:
               return "execute-queue";
            case 59:
               return "max-open-sock-count";
            case 60:
               return "stdout-format";
            case 61:
               return "stdout-log-stack";
            case 62:
               return "stuck-thread-max-time";
            case 63:
               return "stuck-thread-timer-interval";
            case 64:
               return "tracing-enabled";
            case 65:
               return "messaging-bridge-thread-pool-size";
            case 66:
               return "mtu-size";
            case 67:
               return "load-stub-using-context-class-loader";
            case 68:
               return "refresh-client-runtime-descriptor";
            case 69:
               return "timed-out-ref-isolation-time";
            case 70:
               return "use81-style-execute-queues";
            case 71:
               return "t3-client-abbrev-table-size";
            case 72:
               return "t3-server-abbrev-table-size";
            case 73:
               return "gathered-writes-enabled";
            case 74:
               return "scattered-reads-enabled";
            case 75:
               return "add-work-manager-threads-by-cpu-count";
            case 76:
               return "use-concurrent-queue-for-request-manager";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 58:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 50:
               return true;
            case 51:
               return true;
            case 52:
               return true;
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            default:
               return super.isBean(var1);
            case 58:
               return true;
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 28:
               return true;
            default:
               return super.isConfigurable(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private KernelMBeanImpl bean;

      protected Helper(KernelMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "ValidProtocols";
            case 8:
               return "ReverseDNSAllowed";
            case 9:
               return "DefaultProtocol";
            case 10:
               return "DefaultSecureProtocol";
            case 11:
               return "AdministrationProtocol";
            case 12:
               return "ThreadPoolSize";
            case 13:
               return "SystemThreadPoolSize";
            case 14:
               return "SelfTuningThreadPoolSizeMin";
            case 15:
               return "SelfTuningThreadPoolSizeMax";
            case 16:
               return "JMSThreadPoolSize";
            case 17:
               return "NativeIOEnabled";
            case 18:
               return "DevPollDisabled";
            case 19:
               return "MuxerClass";
            case 20:
               return "SocketReaders";
            case 21:
               return "ThreadPoolPercentSocketReaders";
            case 22:
               return "SocketReaderTimeoutMinMillis";
            case 23:
               return "SocketReaderTimeoutMaxMillis";
            case 24:
               return "OutboundEnabled";
            case 25:
               return "OutboundPrivateKeyEnabled";
            case 26:
               return "MaxMessageSize";
            case 27:
               return "MaxT3MessageSize";
            case 28:
               return "SocketBufferSizeAsChunkSize";
            case 29:
               return "MaxHTTPMessageSize";
            case 30:
               return "MaxCOMMessageSize";
            case 31:
               return "MaxIIOPMessageSize";
            case 32:
               return "DefaultGIOPMinorVersion";
            case 33:
               return "UseIIOPLocateRequest";
            case 34:
               return "IIOPTxMechanism";
            case 35:
               return "IIOPLocationForwardPolicy";
            case 36:
               return "ConnectTimeout";
            case 37:
               return "CompleteMessageTimeout";
            case 38:
               return "CompleteT3MessageTimeout";
            case 39:
               return "CompleteHTTPMessageTimeout";
            case 40:
               return "CompleteCOMMessageTimeout";
            case 41:
               return "IdleConnectionTimeout";
            case 42:
               return "IdleIIOPConnectionTimeout";
            case 43:
               return "CompleteIIOPMessageTimeout";
            case 44:
               return "PeriodLength";
            case 45:
               return "IdlePeriodsUntilTimeout";
            case 46:
               return "RjvmIdleTimeout";
            case 47:
               return "ResponseTimeout";
            case 48:
               return "KernelDebug";
            case 49:
               return "DGCIdlePeriodsUntilTimeout";
            case 50:
               return "SSL";
            case 51:
               return "IIOP";
            case 52:
               return "Log";
            case 53:
               return "StdoutEnabled";
            case 54:
               return "StdoutSeverityLevel";
            case 55:
               return "StdoutDebugEnabled";
            case 56:
               return "LogRemoteExceptionsEnabled";
            case 57:
               return "InstrumentStackTraceEnabled";
            case 58:
               return "ExecuteQueues";
            case 59:
               return "MaxOpenSockCount";
            case 60:
               return "StdoutFormat";
            case 61:
               return "StdoutLogStack";
            case 62:
               return "StuckThreadMaxTime";
            case 63:
               return "StuckThreadTimerInterval";
            case 64:
               return "TracingEnabled";
            case 65:
               return "MessagingBridgeThreadPoolSize";
            case 66:
               return "MTUSize";
            case 67:
               return "LoadStubUsingContextClassLoader";
            case 68:
               return "RefreshClientRuntimeDescriptor";
            case 69:
               return "TimedOutRefIsolationTime";
            case 70:
               return "Use81StyleExecuteQueues";
            case 71:
               return "T3ClientAbbrevTableSize";
            case 72:
               return "T3ServerAbbrevTableSize";
            case 73:
               return "GatheredWritesEnabled";
            case 74:
               return "ScatteredReadsEnabled";
            case 75:
               return "AddWorkManagerThreadsByCpuCount";
            case 76:
               return "UseConcurrentQueueForRequestManager";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AdministrationProtocol")) {
            return 11;
         } else if (var1.equals("CompleteCOMMessageTimeout")) {
            return 40;
         } else if (var1.equals("CompleteHTTPMessageTimeout")) {
            return 39;
         } else if (var1.equals("CompleteIIOPMessageTimeout")) {
            return 43;
         } else if (var1.equals("CompleteMessageTimeout")) {
            return 37;
         } else if (var1.equals("CompleteT3MessageTimeout")) {
            return 38;
         } else if (var1.equals("ConnectTimeout")) {
            return 36;
         } else if (var1.equals("DGCIdlePeriodsUntilTimeout")) {
            return 49;
         } else if (var1.equals("DefaultGIOPMinorVersion")) {
            return 32;
         } else if (var1.equals("DefaultProtocol")) {
            return 9;
         } else if (var1.equals("DefaultSecureProtocol")) {
            return 10;
         } else if (var1.equals("ExecuteQueues")) {
            return 58;
         } else if (var1.equals("IIOP")) {
            return 51;
         } else if (var1.equals("IIOPLocationForwardPolicy")) {
            return 35;
         } else if (var1.equals("IIOPTxMechanism")) {
            return 34;
         } else if (var1.equals("IdleConnectionTimeout")) {
            return 41;
         } else if (var1.equals("IdleIIOPConnectionTimeout")) {
            return 42;
         } else if (var1.equals("IdlePeriodsUntilTimeout")) {
            return 45;
         } else if (var1.equals("JMSThreadPoolSize")) {
            return 16;
         } else if (var1.equals("KernelDebug")) {
            return 48;
         } else if (var1.equals("LoadStubUsingContextClassLoader")) {
            return 67;
         } else if (var1.equals("Log")) {
            return 52;
         } else if (var1.equals("MTUSize")) {
            return 66;
         } else if (var1.equals("MaxCOMMessageSize")) {
            return 30;
         } else if (var1.equals("MaxHTTPMessageSize")) {
            return 29;
         } else if (var1.equals("MaxIIOPMessageSize")) {
            return 31;
         } else if (var1.equals("MaxMessageSize")) {
            return 26;
         } else if (var1.equals("MaxOpenSockCount")) {
            return 59;
         } else if (var1.equals("MaxT3MessageSize")) {
            return 27;
         } else if (var1.equals("MessagingBridgeThreadPoolSize")) {
            return 65;
         } else if (var1.equals("MuxerClass")) {
            return 19;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("PeriodLength")) {
            return 44;
         } else if (var1.equals("RefreshClientRuntimeDescriptor")) {
            return 68;
         } else if (var1.equals("ResponseTimeout")) {
            return 47;
         } else if (var1.equals("RjvmIdleTimeout")) {
            return 46;
         } else if (var1.equals("SSL")) {
            return 50;
         } else if (var1.equals("SelfTuningThreadPoolSizeMax")) {
            return 15;
         } else if (var1.equals("SelfTuningThreadPoolSizeMin")) {
            return 14;
         } else if (var1.equals("SocketReaderTimeoutMaxMillis")) {
            return 23;
         } else if (var1.equals("SocketReaderTimeoutMinMillis")) {
            return 22;
         } else if (var1.equals("SocketReaders")) {
            return 20;
         } else if (var1.equals("StdoutFormat")) {
            return 60;
         } else if (var1.equals("StdoutSeverityLevel")) {
            return 54;
         } else if (var1.equals("StuckThreadMaxTime")) {
            return 62;
         } else if (var1.equals("StuckThreadTimerInterval")) {
            return 63;
         } else if (var1.equals("SystemThreadPoolSize")) {
            return 13;
         } else if (var1.equals("T3ClientAbbrevTableSize")) {
            return 71;
         } else if (var1.equals("T3ServerAbbrevTableSize")) {
            return 72;
         } else if (var1.equals("ThreadPoolPercentSocketReaders")) {
            return 21;
         } else if (var1.equals("ThreadPoolSize")) {
            return 12;
         } else if (var1.equals("TimedOutRefIsolationTime")) {
            return 69;
         } else if (var1.equals("TracingEnabled")) {
            return 64;
         } else if (var1.equals("Use81StyleExecuteQueues")) {
            return 70;
         } else if (var1.equals("UseIIOPLocateRequest")) {
            return 33;
         } else if (var1.equals("ValidProtocols")) {
            return 7;
         } else if (var1.equals("AddWorkManagerThreadsByCpuCount")) {
            return 75;
         } else if (var1.equals("DevPollDisabled")) {
            return 18;
         } else if (var1.equals("GatheredWritesEnabled")) {
            return 73;
         } else if (var1.equals("InstrumentStackTraceEnabled")) {
            return 57;
         } else if (var1.equals("LogRemoteExceptionsEnabled")) {
            return 56;
         } else if (var1.equals("NativeIOEnabled")) {
            return 17;
         } else if (var1.equals("OutboundEnabled")) {
            return 24;
         } else if (var1.equals("OutboundPrivateKeyEnabled")) {
            return 25;
         } else if (var1.equals("ReverseDNSAllowed")) {
            return 8;
         } else if (var1.equals("ScatteredReadsEnabled")) {
            return 74;
         } else if (var1.equals("SocketBufferSizeAsChunkSize")) {
            return 28;
         } else if (var1.equals("StdoutDebugEnabled")) {
            return 55;
         } else if (var1.equals("StdoutEnabled")) {
            return 53;
         } else if (var1.equals("StdoutLogStack")) {
            return 61;
         } else {
            return var1.equals("UseConcurrentQueueForRequestManager") ? 76 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getExecuteQueues()));
         if (this.bean.getIIOP() != null) {
            var1.add(new ArrayIterator(new IIOPMBean[]{this.bean.getIIOP()}));
         }

         if (this.bean.getLog() != null) {
            var1.add(new ArrayIterator(new LogMBean[]{this.bean.getLog()}));
         }

         if (this.bean.getSSL() != null) {
            var1.add(new ArrayIterator(new SSLMBean[]{this.bean.getSSL()}));
         }

         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isAdministrationProtocolSet()) {
               var2.append("AdministrationProtocol");
               var2.append(String.valueOf(this.bean.getAdministrationProtocol()));
            }

            if (this.bean.isCompleteCOMMessageTimeoutSet()) {
               var2.append("CompleteCOMMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteCOMMessageTimeout()));
            }

            if (this.bean.isCompleteHTTPMessageTimeoutSet()) {
               var2.append("CompleteHTTPMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteHTTPMessageTimeout()));
            }

            if (this.bean.isCompleteIIOPMessageTimeoutSet()) {
               var2.append("CompleteIIOPMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteIIOPMessageTimeout()));
            }

            if (this.bean.isCompleteMessageTimeoutSet()) {
               var2.append("CompleteMessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteMessageTimeout()));
            }

            if (this.bean.isCompleteT3MessageTimeoutSet()) {
               var2.append("CompleteT3MessageTimeout");
               var2.append(String.valueOf(this.bean.getCompleteT3MessageTimeout()));
            }

            if (this.bean.isConnectTimeoutSet()) {
               var2.append("ConnectTimeout");
               var2.append(String.valueOf(this.bean.getConnectTimeout()));
            }

            if (this.bean.isDGCIdlePeriodsUntilTimeoutSet()) {
               var2.append("DGCIdlePeriodsUntilTimeout");
               var2.append(String.valueOf(this.bean.getDGCIdlePeriodsUntilTimeout()));
            }

            if (this.bean.isDefaultGIOPMinorVersionSet()) {
               var2.append("DefaultGIOPMinorVersion");
               var2.append(String.valueOf(this.bean.getDefaultGIOPMinorVersion()));
            }

            if (this.bean.isDefaultProtocolSet()) {
               var2.append("DefaultProtocol");
               var2.append(String.valueOf(this.bean.getDefaultProtocol()));
            }

            if (this.bean.isDefaultSecureProtocolSet()) {
               var2.append("DefaultSecureProtocol");
               var2.append(String.valueOf(this.bean.getDefaultSecureProtocol()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getExecuteQueues().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getExecuteQueues()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getIIOP());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isIIOPLocationForwardPolicySet()) {
               var2.append("IIOPLocationForwardPolicy");
               var2.append(String.valueOf(this.bean.getIIOPLocationForwardPolicy()));
            }

            if (this.bean.isIIOPTxMechanismSet()) {
               var2.append("IIOPTxMechanism");
               var2.append(String.valueOf(this.bean.getIIOPTxMechanism()));
            }

            if (this.bean.isIdleConnectionTimeoutSet()) {
               var2.append("IdleConnectionTimeout");
               var2.append(String.valueOf(this.bean.getIdleConnectionTimeout()));
            }

            if (this.bean.isIdleIIOPConnectionTimeoutSet()) {
               var2.append("IdleIIOPConnectionTimeout");
               var2.append(String.valueOf(this.bean.getIdleIIOPConnectionTimeout()));
            }

            if (this.bean.isIdlePeriodsUntilTimeoutSet()) {
               var2.append("IdlePeriodsUntilTimeout");
               var2.append(String.valueOf(this.bean.getIdlePeriodsUntilTimeout()));
            }

            if (this.bean.isJMSThreadPoolSizeSet()) {
               var2.append("JMSThreadPoolSize");
               var2.append(String.valueOf(this.bean.getJMSThreadPoolSize()));
            }

            if (this.bean.isKernelDebugSet()) {
               var2.append("KernelDebug");
               var2.append(String.valueOf(this.bean.getKernelDebug()));
            }

            if (this.bean.isLoadStubUsingContextClassLoaderSet()) {
               var2.append("LoadStubUsingContextClassLoader");
               var2.append(String.valueOf(this.bean.getLoadStubUsingContextClassLoader()));
            }

            var5 = this.computeChildHashValue(this.bean.getLog());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isMTUSizeSet()) {
               var2.append("MTUSize");
               var2.append(String.valueOf(this.bean.getMTUSize()));
            }

            if (this.bean.isMaxCOMMessageSizeSet()) {
               var2.append("MaxCOMMessageSize");
               var2.append(String.valueOf(this.bean.getMaxCOMMessageSize()));
            }

            if (this.bean.isMaxHTTPMessageSizeSet()) {
               var2.append("MaxHTTPMessageSize");
               var2.append(String.valueOf(this.bean.getMaxHTTPMessageSize()));
            }

            if (this.bean.isMaxIIOPMessageSizeSet()) {
               var2.append("MaxIIOPMessageSize");
               var2.append(String.valueOf(this.bean.getMaxIIOPMessageSize()));
            }

            if (this.bean.isMaxMessageSizeSet()) {
               var2.append("MaxMessageSize");
               var2.append(String.valueOf(this.bean.getMaxMessageSize()));
            }

            if (this.bean.isMaxOpenSockCountSet()) {
               var2.append("MaxOpenSockCount");
               var2.append(String.valueOf(this.bean.getMaxOpenSockCount()));
            }

            if (this.bean.isMaxT3MessageSizeSet()) {
               var2.append("MaxT3MessageSize");
               var2.append(String.valueOf(this.bean.getMaxT3MessageSize()));
            }

            if (this.bean.isMessagingBridgeThreadPoolSizeSet()) {
               var2.append("MessagingBridgeThreadPoolSize");
               var2.append(String.valueOf(this.bean.getMessagingBridgeThreadPoolSize()));
            }

            if (this.bean.isMuxerClassSet()) {
               var2.append("MuxerClass");
               var2.append(String.valueOf(this.bean.getMuxerClass()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isPeriodLengthSet()) {
               var2.append("PeriodLength");
               var2.append(String.valueOf(this.bean.getPeriodLength()));
            }

            if (this.bean.isRefreshClientRuntimeDescriptorSet()) {
               var2.append("RefreshClientRuntimeDescriptor");
               var2.append(String.valueOf(this.bean.getRefreshClientRuntimeDescriptor()));
            }

            if (this.bean.isResponseTimeoutSet()) {
               var2.append("ResponseTimeout");
               var2.append(String.valueOf(this.bean.getResponseTimeout()));
            }

            if (this.bean.isRjvmIdleTimeoutSet()) {
               var2.append("RjvmIdleTimeout");
               var2.append(String.valueOf(this.bean.getRjvmIdleTimeout()));
            }

            var5 = this.computeChildHashValue(this.bean.getSSL());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isSelfTuningThreadPoolSizeMaxSet()) {
               var2.append("SelfTuningThreadPoolSizeMax");
               var2.append(String.valueOf(this.bean.getSelfTuningThreadPoolSizeMax()));
            }

            if (this.bean.isSelfTuningThreadPoolSizeMinSet()) {
               var2.append("SelfTuningThreadPoolSizeMin");
               var2.append(String.valueOf(this.bean.getSelfTuningThreadPoolSizeMin()));
            }

            if (this.bean.isSocketReaderTimeoutMaxMillisSet()) {
               var2.append("SocketReaderTimeoutMaxMillis");
               var2.append(String.valueOf(this.bean.getSocketReaderTimeoutMaxMillis()));
            }

            if (this.bean.isSocketReaderTimeoutMinMillisSet()) {
               var2.append("SocketReaderTimeoutMinMillis");
               var2.append(String.valueOf(this.bean.getSocketReaderTimeoutMinMillis()));
            }

            if (this.bean.isSocketReadersSet()) {
               var2.append("SocketReaders");
               var2.append(String.valueOf(this.bean.getSocketReaders()));
            }

            if (this.bean.isStdoutFormatSet()) {
               var2.append("StdoutFormat");
               var2.append(String.valueOf(this.bean.getStdoutFormat()));
            }

            if (this.bean.isStdoutSeverityLevelSet()) {
               var2.append("StdoutSeverityLevel");
               var2.append(String.valueOf(this.bean.getStdoutSeverityLevel()));
            }

            if (this.bean.isStuckThreadMaxTimeSet()) {
               var2.append("StuckThreadMaxTime");
               var2.append(String.valueOf(this.bean.getStuckThreadMaxTime()));
            }

            if (this.bean.isStuckThreadTimerIntervalSet()) {
               var2.append("StuckThreadTimerInterval");
               var2.append(String.valueOf(this.bean.getStuckThreadTimerInterval()));
            }

            if (this.bean.isSystemThreadPoolSizeSet()) {
               var2.append("SystemThreadPoolSize");
               var2.append(String.valueOf(this.bean.getSystemThreadPoolSize()));
            }

            if (this.bean.isT3ClientAbbrevTableSizeSet()) {
               var2.append("T3ClientAbbrevTableSize");
               var2.append(String.valueOf(this.bean.getT3ClientAbbrevTableSize()));
            }

            if (this.bean.isT3ServerAbbrevTableSizeSet()) {
               var2.append("T3ServerAbbrevTableSize");
               var2.append(String.valueOf(this.bean.getT3ServerAbbrevTableSize()));
            }

            if (this.bean.isThreadPoolPercentSocketReadersSet()) {
               var2.append("ThreadPoolPercentSocketReaders");
               var2.append(String.valueOf(this.bean.getThreadPoolPercentSocketReaders()));
            }

            if (this.bean.isThreadPoolSizeSet()) {
               var2.append("ThreadPoolSize");
               var2.append(String.valueOf(this.bean.getThreadPoolSize()));
            }

            if (this.bean.isTimedOutRefIsolationTimeSet()) {
               var2.append("TimedOutRefIsolationTime");
               var2.append(String.valueOf(this.bean.getTimedOutRefIsolationTime()));
            }

            if (this.bean.isTracingEnabledSet()) {
               var2.append("TracingEnabled");
               var2.append(String.valueOf(this.bean.getTracingEnabled()));
            }

            if (this.bean.isUse81StyleExecuteQueuesSet()) {
               var2.append("Use81StyleExecuteQueues");
               var2.append(String.valueOf(this.bean.getUse81StyleExecuteQueues()));
            }

            if (this.bean.isUseIIOPLocateRequestSet()) {
               var2.append("UseIIOPLocateRequest");
               var2.append(String.valueOf(this.bean.getUseIIOPLocateRequest()));
            }

            if (this.bean.isValidProtocolsSet()) {
               var2.append("ValidProtocols");
               var2.append(String.valueOf(this.bean.getValidProtocols()));
            }

            if (this.bean.isAddWorkManagerThreadsByCpuCountSet()) {
               var2.append("AddWorkManagerThreadsByCpuCount");
               var2.append(String.valueOf(this.bean.isAddWorkManagerThreadsByCpuCount()));
            }

            if (this.bean.isDevPollDisabledSet()) {
               var2.append("DevPollDisabled");
               var2.append(String.valueOf(this.bean.isDevPollDisabled()));
            }

            if (this.bean.isGatheredWritesEnabledSet()) {
               var2.append("GatheredWritesEnabled");
               var2.append(String.valueOf(this.bean.isGatheredWritesEnabled()));
            }

            if (this.bean.isInstrumentStackTraceEnabledSet()) {
               var2.append("InstrumentStackTraceEnabled");
               var2.append(String.valueOf(this.bean.isInstrumentStackTraceEnabled()));
            }

            if (this.bean.isLogRemoteExceptionsEnabledSet()) {
               var2.append("LogRemoteExceptionsEnabled");
               var2.append(String.valueOf(this.bean.isLogRemoteExceptionsEnabled()));
            }

            if (this.bean.isNativeIOEnabledSet()) {
               var2.append("NativeIOEnabled");
               var2.append(String.valueOf(this.bean.isNativeIOEnabled()));
            }

            if (this.bean.isOutboundEnabledSet()) {
               var2.append("OutboundEnabled");
               var2.append(String.valueOf(this.bean.isOutboundEnabled()));
            }

            if (this.bean.isOutboundPrivateKeyEnabledSet()) {
               var2.append("OutboundPrivateKeyEnabled");
               var2.append(String.valueOf(this.bean.isOutboundPrivateKeyEnabled()));
            }

            if (this.bean.isReverseDNSAllowedSet()) {
               var2.append("ReverseDNSAllowed");
               var2.append(String.valueOf(this.bean.isReverseDNSAllowed()));
            }

            if (this.bean.isScatteredReadsEnabledSet()) {
               var2.append("ScatteredReadsEnabled");
               var2.append(String.valueOf(this.bean.isScatteredReadsEnabled()));
            }

            if (this.bean.isSocketBufferSizeAsChunkSizeSet()) {
               var2.append("SocketBufferSizeAsChunkSize");
               var2.append(String.valueOf(this.bean.isSocketBufferSizeAsChunkSize()));
            }

            if (this.bean.isStdoutDebugEnabledSet()) {
               var2.append("StdoutDebugEnabled");
               var2.append(String.valueOf(this.bean.isStdoutDebugEnabled()));
            }

            if (this.bean.isStdoutEnabledSet()) {
               var2.append("StdoutEnabled");
               var2.append(String.valueOf(this.bean.isStdoutEnabled()));
            }

            if (this.bean.isStdoutLogStackSet()) {
               var2.append("StdoutLogStack");
               var2.append(String.valueOf(this.bean.isStdoutLogStack()));
            }

            if (this.bean.isUseConcurrentQueueForRequestManagerSet()) {
               var2.append("UseConcurrentQueueForRequestManager");
               var2.append(String.valueOf(this.bean.isUseConcurrentQueueForRequestManager()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            KernelMBeanImpl var2 = (KernelMBeanImpl)var1;
            this.computeDiff("AdministrationProtocol", this.bean.getAdministrationProtocol(), var2.getAdministrationProtocol(), false);
            this.computeDiff("CompleteCOMMessageTimeout", this.bean.getCompleteCOMMessageTimeout(), var2.getCompleteCOMMessageTimeout(), true);
            this.computeDiff("CompleteHTTPMessageTimeout", this.bean.getCompleteHTTPMessageTimeout(), var2.getCompleteHTTPMessageTimeout(), true);
            this.computeDiff("CompleteIIOPMessageTimeout", this.bean.getCompleteIIOPMessageTimeout(), var2.getCompleteIIOPMessageTimeout(), true);
            this.computeDiff("CompleteMessageTimeout", this.bean.getCompleteMessageTimeout(), var2.getCompleteMessageTimeout(), true);
            this.computeDiff("CompleteT3MessageTimeout", this.bean.getCompleteT3MessageTimeout(), var2.getCompleteT3MessageTimeout(), true);
            this.computeDiff("ConnectTimeout", this.bean.getConnectTimeout(), var2.getConnectTimeout(), true);
            this.computeDiff("DGCIdlePeriodsUntilTimeout", this.bean.getDGCIdlePeriodsUntilTimeout(), var2.getDGCIdlePeriodsUntilTimeout(), false);
            this.computeDiff("DefaultGIOPMinorVersion", this.bean.getDefaultGIOPMinorVersion(), var2.getDefaultGIOPMinorVersion(), true);
            this.computeDiff("DefaultProtocol", this.bean.getDefaultProtocol(), var2.getDefaultProtocol(), false);
            this.computeDiff("DefaultSecureProtocol", this.bean.getDefaultSecureProtocol(), var2.getDefaultSecureProtocol(), false);
            this.computeChildDiff("ExecuteQueues", this.bean.getExecuteQueues(), var2.getExecuteQueues(), false);
            this.computeSubDiff("IIOP", this.bean.getIIOP(), var2.getIIOP());
            this.computeDiff("IIOPLocationForwardPolicy", this.bean.getIIOPLocationForwardPolicy(), var2.getIIOPLocationForwardPolicy(), true);
            this.computeDiff("IIOPTxMechanism", this.bean.getIIOPTxMechanism(), var2.getIIOPTxMechanism(), true);
            this.computeDiff("IdleConnectionTimeout", this.bean.getIdleConnectionTimeout(), var2.getIdleConnectionTimeout(), true);
            this.computeDiff("IdleIIOPConnectionTimeout", this.bean.getIdleIIOPConnectionTimeout(), var2.getIdleIIOPConnectionTimeout(), true);
            this.computeDiff("IdlePeriodsUntilTimeout", this.bean.getIdlePeriodsUntilTimeout(), var2.getIdlePeriodsUntilTimeout(), false);
            this.computeDiff("JMSThreadPoolSize", this.bean.getJMSThreadPoolSize(), var2.getJMSThreadPoolSize(), false);
            this.computeDiff("LoadStubUsingContextClassLoader", this.bean.getLoadStubUsingContextClassLoader(), var2.getLoadStubUsingContextClassLoader(), false);
            this.computeSubDiff("Log", this.bean.getLog(), var2.getLog());
            this.computeDiff("MTUSize", this.bean.getMTUSize(), var2.getMTUSize(), false);
            this.computeDiff("MaxCOMMessageSize", this.bean.getMaxCOMMessageSize(), var2.getMaxCOMMessageSize(), true);
            this.computeDiff("MaxHTTPMessageSize", this.bean.getMaxHTTPMessageSize(), var2.getMaxHTTPMessageSize(), true);
            this.computeDiff("MaxIIOPMessageSize", this.bean.getMaxIIOPMessageSize(), var2.getMaxIIOPMessageSize(), true);
            this.computeDiff("MaxMessageSize", this.bean.getMaxMessageSize(), var2.getMaxMessageSize(), true);
            this.computeDiff("MaxOpenSockCount", this.bean.getMaxOpenSockCount(), var2.getMaxOpenSockCount(), true);
            this.computeDiff("MaxT3MessageSize", this.bean.getMaxT3MessageSize(), var2.getMaxT3MessageSize(), true);
            this.computeDiff("MessagingBridgeThreadPoolSize", this.bean.getMessagingBridgeThreadPoolSize(), var2.getMessagingBridgeThreadPoolSize(), false);
            this.computeDiff("MuxerClass", this.bean.getMuxerClass(), var2.getMuxerClass(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("PeriodLength", this.bean.getPeriodLength(), var2.getPeriodLength(), false);
            this.computeDiff("RefreshClientRuntimeDescriptor", this.bean.getRefreshClientRuntimeDescriptor(), var2.getRefreshClientRuntimeDescriptor(), false);
            this.computeDiff("ResponseTimeout", this.bean.getResponseTimeout(), var2.getResponseTimeout(), false);
            this.computeDiff("RjvmIdleTimeout", this.bean.getRjvmIdleTimeout(), var2.getRjvmIdleTimeout(), false);
            this.computeSubDiff("SSL", this.bean.getSSL(), var2.getSSL());
            this.computeDiff("SelfTuningThreadPoolSizeMax", this.bean.getSelfTuningThreadPoolSizeMax(), var2.getSelfTuningThreadPoolSizeMax(), false);
            this.computeDiff("SelfTuningThreadPoolSizeMin", this.bean.getSelfTuningThreadPoolSizeMin(), var2.getSelfTuningThreadPoolSizeMin(), false);
            this.computeDiff("SocketReaderTimeoutMaxMillis", this.bean.getSocketReaderTimeoutMaxMillis(), var2.getSocketReaderTimeoutMaxMillis(), true);
            this.computeDiff("SocketReaderTimeoutMinMillis", this.bean.getSocketReaderTimeoutMinMillis(), var2.getSocketReaderTimeoutMinMillis(), true);
            this.computeDiff("SocketReaders", this.bean.getSocketReaders(), var2.getSocketReaders(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutFormat", this.bean.getStdoutFormat(), var2.getStdoutFormat(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutSeverityLevel", this.bean.getStdoutSeverityLevel(), var2.getStdoutSeverityLevel(), true);
            }

            this.computeDiff("StuckThreadMaxTime", this.bean.getStuckThreadMaxTime(), var2.getStuckThreadMaxTime(), false);
            this.computeDiff("StuckThreadTimerInterval", this.bean.getStuckThreadTimerInterval(), var2.getStuckThreadTimerInterval(), false);
            this.computeDiff("SystemThreadPoolSize", this.bean.getSystemThreadPoolSize(), var2.getSystemThreadPoolSize(), false);
            this.computeDiff("T3ClientAbbrevTableSize", this.bean.getT3ClientAbbrevTableSize(), var2.getT3ClientAbbrevTableSize(), false);
            this.computeDiff("T3ServerAbbrevTableSize", this.bean.getT3ServerAbbrevTableSize(), var2.getT3ServerAbbrevTableSize(), false);
            this.computeDiff("ThreadPoolPercentSocketReaders", this.bean.getThreadPoolPercentSocketReaders(), var2.getThreadPoolPercentSocketReaders(), true);
            this.computeDiff("ThreadPoolSize", this.bean.getThreadPoolSize(), var2.getThreadPoolSize(), false);
            this.computeDiff("TimedOutRefIsolationTime", this.bean.getTimedOutRefIsolationTime(), var2.getTimedOutRefIsolationTime(), false);
            this.computeDiff("TracingEnabled", this.bean.getTracingEnabled(), var2.getTracingEnabled(), false);
            this.computeDiff("Use81StyleExecuteQueues", this.bean.getUse81StyleExecuteQueues(), var2.getUse81StyleExecuteQueues(), false);
            this.computeDiff("UseIIOPLocateRequest", this.bean.getUseIIOPLocateRequest(), var2.getUseIIOPLocateRequest(), true);
            this.computeDiff("ValidProtocols", this.bean.getValidProtocols(), var2.getValidProtocols(), false);
            this.computeDiff("AddWorkManagerThreadsByCpuCount", this.bean.isAddWorkManagerThreadsByCpuCount(), var2.isAddWorkManagerThreadsByCpuCount(), false);
            this.computeDiff("DevPollDisabled", this.bean.isDevPollDisabled(), var2.isDevPollDisabled(), false);
            this.computeDiff("GatheredWritesEnabled", this.bean.isGatheredWritesEnabled(), var2.isGatheredWritesEnabled(), false);
            this.computeDiff("InstrumentStackTraceEnabled", this.bean.isInstrumentStackTraceEnabled(), var2.isInstrumentStackTraceEnabled(), true);
            this.computeDiff("LogRemoteExceptionsEnabled", this.bean.isLogRemoteExceptionsEnabled(), var2.isLogRemoteExceptionsEnabled(), true);
            this.computeDiff("NativeIOEnabled", this.bean.isNativeIOEnabled(), var2.isNativeIOEnabled(), false);
            this.computeDiff("OutboundEnabled", this.bean.isOutboundEnabled(), var2.isOutboundEnabled(), true);
            this.computeDiff("OutboundPrivateKeyEnabled", this.bean.isOutboundPrivateKeyEnabled(), var2.isOutboundPrivateKeyEnabled(), true);
            this.computeDiff("ReverseDNSAllowed", this.bean.isReverseDNSAllowed(), var2.isReverseDNSAllowed(), true);
            this.computeDiff("ScatteredReadsEnabled", this.bean.isScatteredReadsEnabled(), var2.isScatteredReadsEnabled(), false);
            this.computeDiff("SocketBufferSizeAsChunkSize", this.bean.isSocketBufferSizeAsChunkSize(), var2.isSocketBufferSizeAsChunkSize(), false);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutDebugEnabled", this.bean.isStdoutDebugEnabled(), var2.isStdoutDebugEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutEnabled", this.bean.isStdoutEnabled(), var2.isStdoutEnabled(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("StdoutLogStack", this.bean.isStdoutLogStack(), var2.isStdoutLogStack(), true);
            }

            this.computeDiff("UseConcurrentQueueForRequestManager", this.bean.isUseConcurrentQueueForRequestManager(), var2.isUseConcurrentQueueForRequestManager(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            KernelMBeanImpl var3 = (KernelMBeanImpl)var1.getSourceBean();
            KernelMBeanImpl var4 = (KernelMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AdministrationProtocol")) {
                  var3.setAdministrationProtocol(var4.getAdministrationProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("CompleteCOMMessageTimeout")) {
                  var3.setCompleteCOMMessageTimeout(var4.getCompleteCOMMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 40);
               } else if (var5.equals("CompleteHTTPMessageTimeout")) {
                  var3.setCompleteHTTPMessageTimeout(var4.getCompleteHTTPMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 39);
               } else if (var5.equals("CompleteIIOPMessageTimeout")) {
                  var3.setCompleteIIOPMessageTimeout(var4.getCompleteIIOPMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 43);
               } else if (var5.equals("CompleteMessageTimeout")) {
                  var3.setCompleteMessageTimeout(var4.getCompleteMessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 37);
               } else if (var5.equals("CompleteT3MessageTimeout")) {
                  var3.setCompleteT3MessageTimeout(var4.getCompleteT3MessageTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 38);
               } else if (var5.equals("ConnectTimeout")) {
                  var3.setConnectTimeout(var4.getConnectTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 36);
               } else if (var5.equals("DGCIdlePeriodsUntilTimeout")) {
                  var3.setDGCIdlePeriodsUntilTimeout(var4.getDGCIdlePeriodsUntilTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 49);
               } else if (var5.equals("DefaultGIOPMinorVersion")) {
                  var3.setDefaultGIOPMinorVersion(var4.getDefaultGIOPMinorVersion());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("DefaultProtocol")) {
                  var3.setDefaultProtocol(var4.getDefaultProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("DefaultSecureProtocol")) {
                  var3.setDefaultSecureProtocol(var4.getDefaultSecureProtocol());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("ExecuteQueues")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addExecuteQueue((ExecuteQueueMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeExecuteQueue((ExecuteQueueMBean)var2.getRemovedObject());
                  }

                  if (var3.getExecuteQueues() == null || var3.getExecuteQueues().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 58);
                  }
               } else if (var5.equals("IIOP")) {
                  if (var6 == 2) {
                     var3.setIIOP((IIOPMBean)this.createCopy((AbstractDescriptorBean)var4.getIIOP()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("IIOP", var3.getIIOP());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 51);
               } else if (var5.equals("IIOPLocationForwardPolicy")) {
                  var3.setIIOPLocationForwardPolicy(var4.getIIOPLocationForwardPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 35);
               } else if (var5.equals("IIOPTxMechanism")) {
                  var3.setIIOPTxMechanism(var4.getIIOPTxMechanism());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 34);
               } else if (var5.equals("IdleConnectionTimeout")) {
                  var3.setIdleConnectionTimeout(var4.getIdleConnectionTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 41);
               } else if (var5.equals("IdleIIOPConnectionTimeout")) {
                  var3.setIdleIIOPConnectionTimeout(var4.getIdleIIOPConnectionTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 42);
               } else if (var5.equals("IdlePeriodsUntilTimeout")) {
                  var3.setIdlePeriodsUntilTimeout(var4.getIdlePeriodsUntilTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 45);
               } else if (var5.equals("JMSThreadPoolSize")) {
                  var3.setJMSThreadPoolSize(var4.getJMSThreadPoolSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (!var5.equals("KernelDebug")) {
                  if (var5.equals("LoadStubUsingContextClassLoader")) {
                     var3.setLoadStubUsingContextClassLoader(var4.getLoadStubUsingContextClassLoader());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 67);
                  } else if (var5.equals("Log")) {
                     if (var6 == 2) {
                        var3.setLog((LogMBean)this.createCopy((AbstractDescriptorBean)var4.getLog()));
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3._destroySingleton("Log", var3.getLog());
                     }

                     var3._conditionalUnset(var2.isUnsetUpdate(), 52);
                  } else if (var5.equals("MTUSize")) {
                     var3.setMTUSize(var4.getMTUSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 66);
                  } else if (var5.equals("MaxCOMMessageSize")) {
                     var3.setMaxCOMMessageSize(var4.getMaxCOMMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                  } else if (var5.equals("MaxHTTPMessageSize")) {
                     var3.setMaxHTTPMessageSize(var4.getMaxHTTPMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                  } else if (var5.equals("MaxIIOPMessageSize")) {
                     var3.setMaxIIOPMessageSize(var4.getMaxIIOPMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                  } else if (var5.equals("MaxMessageSize")) {
                     var3.setMaxMessageSize(var4.getMaxMessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                  } else if (var5.equals("MaxOpenSockCount")) {
                     var3.setMaxOpenSockCount(var4.getMaxOpenSockCount());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 59);
                  } else if (var5.equals("MaxT3MessageSize")) {
                     var3.setMaxT3MessageSize(var4.getMaxT3MessageSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                  } else if (var5.equals("MessagingBridgeThreadPoolSize")) {
                     var3.setMessagingBridgeThreadPoolSize(var4.getMessagingBridgeThreadPoolSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 65);
                  } else if (var5.equals("MuxerClass")) {
                     var3.setMuxerClass(var4.getMuxerClass());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("PeriodLength")) {
                     var3.setPeriodLength(var4.getPeriodLength());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 44);
                  } else if (var5.equals("RefreshClientRuntimeDescriptor")) {
                     var3.setRefreshClientRuntimeDescriptor(var4.getRefreshClientRuntimeDescriptor());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 68);
                  } else if (var5.equals("ResponseTimeout")) {
                     var3.setResponseTimeout(var4.getResponseTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 47);
                  } else if (var5.equals("RjvmIdleTimeout")) {
                     var3.setRjvmIdleTimeout(var4.getRjvmIdleTimeout());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 46);
                  } else if (var5.equals("SSL")) {
                     if (var6 == 2) {
                        var3.setSSL((SSLMBean)this.createCopy((AbstractDescriptorBean)var4.getSSL()));
                     } else {
                        if (var6 != 3) {
                           throw new AssertionError("Invalid type: " + var6);
                        }

                        var3._destroySingleton("SSL", var3.getSSL());
                     }

                     var3._conditionalUnset(var2.isUnsetUpdate(), 50);
                  } else if (var5.equals("SelfTuningThreadPoolSizeMax")) {
                     var3.setSelfTuningThreadPoolSizeMax(var4.getSelfTuningThreadPoolSizeMax());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("SelfTuningThreadPoolSizeMin")) {
                     var3.setSelfTuningThreadPoolSizeMin(var4.getSelfTuningThreadPoolSizeMin());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("SocketReaderTimeoutMaxMillis")) {
                     var3.setSocketReaderTimeoutMaxMillis(var4.getSocketReaderTimeoutMaxMillis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  } else if (var5.equals("SocketReaderTimeoutMinMillis")) {
                     var3.setSocketReaderTimeoutMinMillis(var4.getSocketReaderTimeoutMinMillis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                  } else if (var5.equals("SocketReaders")) {
                     var3.setSocketReaders(var4.getSocketReaders());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("StdoutFormat")) {
                     var3.setStdoutFormat(var4.getStdoutFormat());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 60);
                  } else if (var5.equals("StdoutSeverityLevel")) {
                     var3.setStdoutSeverityLevel(var4.getStdoutSeverityLevel());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 54);
                  } else if (var5.equals("StuckThreadMaxTime")) {
                     var3.setStuckThreadMaxTime(var4.getStuckThreadMaxTime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 62);
                  } else if (var5.equals("StuckThreadTimerInterval")) {
                     var3.setStuckThreadTimerInterval(var4.getStuckThreadTimerInterval());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 63);
                  } else if (var5.equals("SystemThreadPoolSize")) {
                     var3.setSystemThreadPoolSize(var4.getSystemThreadPoolSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("T3ClientAbbrevTableSize")) {
                     var3.setT3ClientAbbrevTableSize(var4.getT3ClientAbbrevTableSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 71);
                  } else if (var5.equals("T3ServerAbbrevTableSize")) {
                     var3.setT3ServerAbbrevTableSize(var4.getT3ServerAbbrevTableSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 72);
                  } else if (var5.equals("ThreadPoolPercentSocketReaders")) {
                     var3.setThreadPoolPercentSocketReaders(var4.getThreadPoolPercentSocketReaders());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("ThreadPoolSize")) {
                     var3.setThreadPoolSize(var4.getThreadPoolSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("TimedOutRefIsolationTime")) {
                     var3.setTimedOutRefIsolationTime(var4.getTimedOutRefIsolationTime());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 69);
                  } else if (var5.equals("TracingEnabled")) {
                     var3.setTracingEnabled(var4.getTracingEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 64);
                  } else if (var5.equals("Use81StyleExecuteQueues")) {
                     var3.setUse81StyleExecuteQueues(var4.getUse81StyleExecuteQueues());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 70);
                  } else if (var5.equals("UseIIOPLocateRequest")) {
                     var3.setUseIIOPLocateRequest(var4.getUseIIOPLocateRequest());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                  } else if (var5.equals("ValidProtocols")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("AddWorkManagerThreadsByCpuCount")) {
                     var3.setAddWorkManagerThreadsByCpuCount(var4.isAddWorkManagerThreadsByCpuCount());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 75);
                  } else if (var5.equals("DevPollDisabled")) {
                     var3.setDevPollDisabled(var4.isDevPollDisabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("GatheredWritesEnabled")) {
                     var3.setGatheredWritesEnabled(var4.isGatheredWritesEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 73);
                  } else if (var5.equals("InstrumentStackTraceEnabled")) {
                     var3.setInstrumentStackTraceEnabled(var4.isInstrumentStackTraceEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 57);
                  } else if (var5.equals("LogRemoteExceptionsEnabled")) {
                     var3.setLogRemoteExceptionsEnabled(var4.isLogRemoteExceptionsEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 56);
                  } else if (var5.equals("NativeIOEnabled")) {
                     var3.setNativeIOEnabled(var4.isNativeIOEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("OutboundEnabled")) {
                     var3.setOutboundEnabled(var4.isOutboundEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  } else if (var5.equals("OutboundPrivateKeyEnabled")) {
                     var3.setOutboundPrivateKeyEnabled(var4.isOutboundPrivateKeyEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                  } else if (var5.equals("ReverseDNSAllowed")) {
                     var3.setReverseDNSAllowed(var4.isReverseDNSAllowed());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("ScatteredReadsEnabled")) {
                     var3.setScatteredReadsEnabled(var4.isScatteredReadsEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 74);
                  } else if (var5.equals("SocketBufferSizeAsChunkSize")) {
                     var3.setSocketBufferSizeAsChunkSize(var4.isSocketBufferSizeAsChunkSize());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                  } else if (var5.equals("StdoutDebugEnabled")) {
                     var3.setStdoutDebugEnabled(var4.isStdoutDebugEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 55);
                  } else if (var5.equals("StdoutEnabled")) {
                     var3.setStdoutEnabled(var4.isStdoutEnabled());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 53);
                  } else if (var5.equals("StdoutLogStack")) {
                     var3.setStdoutLogStack(var4.isStdoutLogStack());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 61);
                  } else if (var5.equals("UseConcurrentQueueForRequestManager")) {
                     var3.setUseConcurrentQueueForRequestManager(var4.isUseConcurrentQueueForRequestManager());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 76);
                  } else {
                     super.applyPropertyUpdate(var1, var2);
                  }
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            KernelMBeanImpl var5 = (KernelMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AdministrationProtocol")) && this.bean.isAdministrationProtocolSet()) {
               var5.setAdministrationProtocol(this.bean.getAdministrationProtocol());
            }

            if ((var3 == null || !var3.contains("CompleteCOMMessageTimeout")) && this.bean.isCompleteCOMMessageTimeoutSet()) {
               var5.setCompleteCOMMessageTimeout(this.bean.getCompleteCOMMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteHTTPMessageTimeout")) && this.bean.isCompleteHTTPMessageTimeoutSet()) {
               var5.setCompleteHTTPMessageTimeout(this.bean.getCompleteHTTPMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteIIOPMessageTimeout")) && this.bean.isCompleteIIOPMessageTimeoutSet()) {
               var5.setCompleteIIOPMessageTimeout(this.bean.getCompleteIIOPMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteMessageTimeout")) && this.bean.isCompleteMessageTimeoutSet()) {
               var5.setCompleteMessageTimeout(this.bean.getCompleteMessageTimeout());
            }

            if ((var3 == null || !var3.contains("CompleteT3MessageTimeout")) && this.bean.isCompleteT3MessageTimeoutSet()) {
               var5.setCompleteT3MessageTimeout(this.bean.getCompleteT3MessageTimeout());
            }

            if ((var3 == null || !var3.contains("ConnectTimeout")) && this.bean.isConnectTimeoutSet()) {
               var5.setConnectTimeout(this.bean.getConnectTimeout());
            }

            if ((var3 == null || !var3.contains("DGCIdlePeriodsUntilTimeout")) && this.bean.isDGCIdlePeriodsUntilTimeoutSet()) {
               var5.setDGCIdlePeriodsUntilTimeout(this.bean.getDGCIdlePeriodsUntilTimeout());
            }

            if ((var3 == null || !var3.contains("DefaultGIOPMinorVersion")) && this.bean.isDefaultGIOPMinorVersionSet()) {
               var5.setDefaultGIOPMinorVersion(this.bean.getDefaultGIOPMinorVersion());
            }

            if ((var3 == null || !var3.contains("DefaultProtocol")) && this.bean.isDefaultProtocolSet()) {
               var5.setDefaultProtocol(this.bean.getDefaultProtocol());
            }

            if ((var3 == null || !var3.contains("DefaultSecureProtocol")) && this.bean.isDefaultSecureProtocolSet()) {
               var5.setDefaultSecureProtocol(this.bean.getDefaultSecureProtocol());
            }

            if ((var3 == null || !var3.contains("ExecuteQueues")) && this.bean.isExecuteQueuesSet() && !var5._isSet(58)) {
               ExecuteQueueMBean[] var6 = this.bean.getExecuteQueues();
               ExecuteQueueMBean[] var7 = new ExecuteQueueMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (ExecuteQueueMBean)((ExecuteQueueMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setExecuteQueues(var7);
            }

            if ((var3 == null || !var3.contains("IIOP")) && this.bean.isIIOPSet() && !var5._isSet(51)) {
               IIOPMBean var4 = this.bean.getIIOP();
               var5.setIIOP((IIOPMBean)null);
               var5.setIIOP(var4 == null ? null : (IIOPMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if ((var3 == null || !var3.contains("IIOPLocationForwardPolicy")) && this.bean.isIIOPLocationForwardPolicySet()) {
               var5.setIIOPLocationForwardPolicy(this.bean.getIIOPLocationForwardPolicy());
            }

            if ((var3 == null || !var3.contains("IIOPTxMechanism")) && this.bean.isIIOPTxMechanismSet()) {
               var5.setIIOPTxMechanism(this.bean.getIIOPTxMechanism());
            }

            if ((var3 == null || !var3.contains("IdleConnectionTimeout")) && this.bean.isIdleConnectionTimeoutSet()) {
               var5.setIdleConnectionTimeout(this.bean.getIdleConnectionTimeout());
            }

            if ((var3 == null || !var3.contains("IdleIIOPConnectionTimeout")) && this.bean.isIdleIIOPConnectionTimeoutSet()) {
               var5.setIdleIIOPConnectionTimeout(this.bean.getIdleIIOPConnectionTimeout());
            }

            if ((var3 == null || !var3.contains("IdlePeriodsUntilTimeout")) && this.bean.isIdlePeriodsUntilTimeoutSet()) {
               var5.setIdlePeriodsUntilTimeout(this.bean.getIdlePeriodsUntilTimeout());
            }

            if ((var3 == null || !var3.contains("JMSThreadPoolSize")) && this.bean.isJMSThreadPoolSizeSet()) {
               var5.setJMSThreadPoolSize(this.bean.getJMSThreadPoolSize());
            }

            if ((var3 == null || !var3.contains("LoadStubUsingContextClassLoader")) && this.bean.isLoadStubUsingContextClassLoaderSet()) {
               var5.setLoadStubUsingContextClassLoader(this.bean.getLoadStubUsingContextClassLoader());
            }

            if ((var3 == null || !var3.contains("Log")) && this.bean.isLogSet() && !var5._isSet(52)) {
               LogMBean var11 = this.bean.getLog();
               var5.setLog((LogMBean)null);
               var5.setLog(var11 == null ? null : (LogMBean)this.createCopy((AbstractDescriptorBean)var11, var2));
            }

            if ((var3 == null || !var3.contains("MTUSize")) && this.bean.isMTUSizeSet()) {
               var5.setMTUSize(this.bean.getMTUSize());
            }

            if ((var3 == null || !var3.contains("MaxCOMMessageSize")) && this.bean.isMaxCOMMessageSizeSet()) {
               var5.setMaxCOMMessageSize(this.bean.getMaxCOMMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxHTTPMessageSize")) && this.bean.isMaxHTTPMessageSizeSet()) {
               var5.setMaxHTTPMessageSize(this.bean.getMaxHTTPMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxIIOPMessageSize")) && this.bean.isMaxIIOPMessageSizeSet()) {
               var5.setMaxIIOPMessageSize(this.bean.getMaxIIOPMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxMessageSize")) && this.bean.isMaxMessageSizeSet()) {
               var5.setMaxMessageSize(this.bean.getMaxMessageSize());
            }

            if ((var3 == null || !var3.contains("MaxOpenSockCount")) && this.bean.isMaxOpenSockCountSet()) {
               var5.setMaxOpenSockCount(this.bean.getMaxOpenSockCount());
            }

            if ((var3 == null || !var3.contains("MaxT3MessageSize")) && this.bean.isMaxT3MessageSizeSet()) {
               var5.setMaxT3MessageSize(this.bean.getMaxT3MessageSize());
            }

            if ((var3 == null || !var3.contains("MessagingBridgeThreadPoolSize")) && this.bean.isMessagingBridgeThreadPoolSizeSet()) {
               var5.setMessagingBridgeThreadPoolSize(this.bean.getMessagingBridgeThreadPoolSize());
            }

            if ((var3 == null || !var3.contains("MuxerClass")) && this.bean.isMuxerClassSet()) {
               var5.setMuxerClass(this.bean.getMuxerClass());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("PeriodLength")) && this.bean.isPeriodLengthSet()) {
               var5.setPeriodLength(this.bean.getPeriodLength());
            }

            if ((var3 == null || !var3.contains("RefreshClientRuntimeDescriptor")) && this.bean.isRefreshClientRuntimeDescriptorSet()) {
               var5.setRefreshClientRuntimeDescriptor(this.bean.getRefreshClientRuntimeDescriptor());
            }

            if ((var3 == null || !var3.contains("ResponseTimeout")) && this.bean.isResponseTimeoutSet()) {
               var5.setResponseTimeout(this.bean.getResponseTimeout());
            }

            if ((var3 == null || !var3.contains("RjvmIdleTimeout")) && this.bean.isRjvmIdleTimeoutSet()) {
               var5.setRjvmIdleTimeout(this.bean.getRjvmIdleTimeout());
            }

            if ((var3 == null || !var3.contains("SSL")) && this.bean.isSSLSet() && !var5._isSet(50)) {
               SSLMBean var12 = this.bean.getSSL();
               var5.setSSL((SSLMBean)null);
               var5.setSSL(var12 == null ? null : (SSLMBean)this.createCopy((AbstractDescriptorBean)var12, var2));
            }

            if ((var3 == null || !var3.contains("SelfTuningThreadPoolSizeMax")) && this.bean.isSelfTuningThreadPoolSizeMaxSet()) {
               var5.setSelfTuningThreadPoolSizeMax(this.bean.getSelfTuningThreadPoolSizeMax());
            }

            if ((var3 == null || !var3.contains("SelfTuningThreadPoolSizeMin")) && this.bean.isSelfTuningThreadPoolSizeMinSet()) {
               var5.setSelfTuningThreadPoolSizeMin(this.bean.getSelfTuningThreadPoolSizeMin());
            }

            if ((var3 == null || !var3.contains("SocketReaderTimeoutMaxMillis")) && this.bean.isSocketReaderTimeoutMaxMillisSet()) {
               var5.setSocketReaderTimeoutMaxMillis(this.bean.getSocketReaderTimeoutMaxMillis());
            }

            if ((var3 == null || !var3.contains("SocketReaderTimeoutMinMillis")) && this.bean.isSocketReaderTimeoutMinMillisSet()) {
               var5.setSocketReaderTimeoutMinMillis(this.bean.getSocketReaderTimeoutMinMillis());
            }

            if ((var3 == null || !var3.contains("SocketReaders")) && this.bean.isSocketReadersSet()) {
               var5.setSocketReaders(this.bean.getSocketReaders());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutFormat")) && this.bean.isStdoutFormatSet()) {
               var5.setStdoutFormat(this.bean.getStdoutFormat());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutSeverityLevel")) && this.bean.isStdoutSeverityLevelSet()) {
               var5.setStdoutSeverityLevel(this.bean.getStdoutSeverityLevel());
            }

            if ((var3 == null || !var3.contains("StuckThreadMaxTime")) && this.bean.isStuckThreadMaxTimeSet()) {
               var5.setStuckThreadMaxTime(this.bean.getStuckThreadMaxTime());
            }

            if ((var3 == null || !var3.contains("StuckThreadTimerInterval")) && this.bean.isStuckThreadTimerIntervalSet()) {
               var5.setStuckThreadTimerInterval(this.bean.getStuckThreadTimerInterval());
            }

            if ((var3 == null || !var3.contains("SystemThreadPoolSize")) && this.bean.isSystemThreadPoolSizeSet()) {
               var5.setSystemThreadPoolSize(this.bean.getSystemThreadPoolSize());
            }

            if ((var3 == null || !var3.contains("T3ClientAbbrevTableSize")) && this.bean.isT3ClientAbbrevTableSizeSet()) {
               var5.setT3ClientAbbrevTableSize(this.bean.getT3ClientAbbrevTableSize());
            }

            if ((var3 == null || !var3.contains("T3ServerAbbrevTableSize")) && this.bean.isT3ServerAbbrevTableSizeSet()) {
               var5.setT3ServerAbbrevTableSize(this.bean.getT3ServerAbbrevTableSize());
            }

            if ((var3 == null || !var3.contains("ThreadPoolPercentSocketReaders")) && this.bean.isThreadPoolPercentSocketReadersSet()) {
               var5.setThreadPoolPercentSocketReaders(this.bean.getThreadPoolPercentSocketReaders());
            }

            if ((var3 == null || !var3.contains("ThreadPoolSize")) && this.bean.isThreadPoolSizeSet()) {
               var5.setThreadPoolSize(this.bean.getThreadPoolSize());
            }

            if ((var3 == null || !var3.contains("TimedOutRefIsolationTime")) && this.bean.isTimedOutRefIsolationTimeSet()) {
               var5.setTimedOutRefIsolationTime(this.bean.getTimedOutRefIsolationTime());
            }

            if ((var3 == null || !var3.contains("TracingEnabled")) && this.bean.isTracingEnabledSet()) {
               var5.setTracingEnabled(this.bean.getTracingEnabled());
            }

            if ((var3 == null || !var3.contains("Use81StyleExecuteQueues")) && this.bean.isUse81StyleExecuteQueuesSet()) {
               var5.setUse81StyleExecuteQueues(this.bean.getUse81StyleExecuteQueues());
            }

            if ((var3 == null || !var3.contains("UseIIOPLocateRequest")) && this.bean.isUseIIOPLocateRequestSet()) {
               var5.setUseIIOPLocateRequest(this.bean.getUseIIOPLocateRequest());
            }

            if ((var3 == null || !var3.contains("ValidProtocols")) && this.bean.isValidProtocolsSet()) {
            }

            if ((var3 == null || !var3.contains("AddWorkManagerThreadsByCpuCount")) && this.bean.isAddWorkManagerThreadsByCpuCountSet()) {
               var5.setAddWorkManagerThreadsByCpuCount(this.bean.isAddWorkManagerThreadsByCpuCount());
            }

            if ((var3 == null || !var3.contains("DevPollDisabled")) && this.bean.isDevPollDisabledSet()) {
               var5.setDevPollDisabled(this.bean.isDevPollDisabled());
            }

            if ((var3 == null || !var3.contains("GatheredWritesEnabled")) && this.bean.isGatheredWritesEnabledSet()) {
               var5.setGatheredWritesEnabled(this.bean.isGatheredWritesEnabled());
            }

            if ((var3 == null || !var3.contains("InstrumentStackTraceEnabled")) && this.bean.isInstrumentStackTraceEnabledSet()) {
               var5.setInstrumentStackTraceEnabled(this.bean.isInstrumentStackTraceEnabled());
            }

            if ((var3 == null || !var3.contains("LogRemoteExceptionsEnabled")) && this.bean.isLogRemoteExceptionsEnabledSet()) {
               var5.setLogRemoteExceptionsEnabled(this.bean.isLogRemoteExceptionsEnabled());
            }

            if ((var3 == null || !var3.contains("NativeIOEnabled")) && this.bean.isNativeIOEnabledSet()) {
               var5.setNativeIOEnabled(this.bean.isNativeIOEnabled());
            }

            if ((var3 == null || !var3.contains("OutboundEnabled")) && this.bean.isOutboundEnabledSet()) {
               var5.setOutboundEnabled(this.bean.isOutboundEnabled());
            }

            if ((var3 == null || !var3.contains("OutboundPrivateKeyEnabled")) && this.bean.isOutboundPrivateKeyEnabledSet()) {
               var5.setOutboundPrivateKeyEnabled(this.bean.isOutboundPrivateKeyEnabled());
            }

            if ((var3 == null || !var3.contains("ReverseDNSAllowed")) && this.bean.isReverseDNSAllowedSet()) {
               var5.setReverseDNSAllowed(this.bean.isReverseDNSAllowed());
            }

            if ((var3 == null || !var3.contains("ScatteredReadsEnabled")) && this.bean.isScatteredReadsEnabledSet()) {
               var5.setScatteredReadsEnabled(this.bean.isScatteredReadsEnabled());
            }

            if ((var3 == null || !var3.contains("SocketBufferSizeAsChunkSize")) && this.bean.isSocketBufferSizeAsChunkSizeSet()) {
               var5.setSocketBufferSizeAsChunkSize(this.bean.isSocketBufferSizeAsChunkSize());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutDebugEnabled")) && this.bean.isStdoutDebugEnabledSet()) {
               var5.setStdoutDebugEnabled(this.bean.isStdoutDebugEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutEnabled")) && this.bean.isStdoutEnabledSet()) {
               var5.setStdoutEnabled(this.bean.isStdoutEnabled());
            }

            if (var2 && (var3 == null || !var3.contains("StdoutLogStack")) && this.bean.isStdoutLogStackSet()) {
               var5.setStdoutLogStack(this.bean.isStdoutLogStack());
            }

            if ((var3 == null || !var3.contains("UseConcurrentQueueForRequestManager")) && this.bean.isUseConcurrentQueueForRequestManagerSet()) {
               var5.setUseConcurrentQueueForRequestManager(this.bean.isUseConcurrentQueueForRequestManager());
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getExecuteQueues(), var1, var2);
         this.inferSubTree(this.bean.getIIOP(), var1, var2);
         this.inferSubTree(this.bean.getKernelDebug(), var1, var2);
         this.inferSubTree(this.bean.getLog(), var1, var2);
         this.inferSubTree(this.bean.getSSL(), var1, var2);
      }
   }
}
