package weblogic.diagnostics.instrumentation.gathering;

import com.bea.logging.BaseLogRecord;
import com.bea.logging.LogLevel;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.InvalidEventDefinitionException;
import com.oracle.jrockit.jfr.InvalidValueException;
import com.oracle.jrockit.jfr.Producer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.util.List;
import java.util.logging.LogRecord;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.flightrecorder.FlightRecorderManager;
import weblogic.diagnostics.flightrecorder.event.BaseInstantEvent;
import weblogic.diagnostics.flightrecorder.event.BaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorActivateEndpointEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorBaseEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorDeactivateEndpointEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorEndpointBaseEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorInboundTransactionRollbackEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundConnectionErrorEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundDestroyConnectionEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundRegisterResourceEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundReleaseConnectionEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundReserveConnectionEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundTransactionRollbackEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorOutboundUnregisterResourceEvent;
import weblogic.diagnostics.flightrecorder.event.ConnectorTransactionBaseEvent;
import weblogic.diagnostics.flightrecorder.event.EJBBaseInstantEvent;
import weblogic.diagnostics.flightrecorder.event.EJBBaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodInvokeEvent;
import weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodPostInvokeCleanupEvent;
import weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodPostInvokeEvent;
import weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodPreInvokeEvent;
import weblogic.diagnostics.flightrecorder.event.EJBDatabaseAccessEvent;
import weblogic.diagnostics.flightrecorder.event.EJBHomeCreateEvent;
import weblogic.diagnostics.flightrecorder.event.EJBHomeRemoveEvent;
import weblogic.diagnostics.flightrecorder.event.EJBPoolManagerCreateEvent;
import weblogic.diagnostics.flightrecorder.event.EJBPoolManagerPostInvokeEvent;
import weblogic.diagnostics.flightrecorder.event.EJBPoolManagerPreInvokeEvent;
import weblogic.diagnostics.flightrecorder.event.EJBPoolManagerRemoveEvent;
import weblogic.diagnostics.flightrecorder.event.EJBReplicatedSessionManagerEvent;
import weblogic.diagnostics.flightrecorder.event.EJBTimerManagerEvent;
import weblogic.diagnostics.flightrecorder.event.GlobalInformationEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCBaseInstantEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCBaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionCloseEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionCommitEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionCreateStatementEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionGetVendorConnectionEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionPrepareEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionReleaseEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionReserveEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCConnectionRollbackEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCDataSourceGetConnectionEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCDriverConnectEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCStatementCreationEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCStatementExecuteEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionBaseEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionCommitEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionEndEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionGetXAResourceEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionIsSameRMEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionPrepareEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionRollbackEvent;
import weblogic.diagnostics.flightrecorder.event.JDBCTransactionStartEvent;
import weblogic.diagnostics.flightrecorder.event.JMSBEConsumerLogEvent;
import weblogic.diagnostics.flightrecorder.event.JTABaseInstantEvent;
import weblogic.diagnostics.flightrecorder.event.JTATransactionCommitEvent;
import weblogic.diagnostics.flightrecorder.event.JTATransactionEndEvent;
import weblogic.diagnostics.flightrecorder.event.JTATransactionPrepareEvent;
import weblogic.diagnostics.flightrecorder.event.JTATransactionPreparedEvent;
import weblogic.diagnostics.flightrecorder.event.JTATransactionStartEvent;
import weblogic.diagnostics.flightrecorder.event.LogRecordEvent;
import weblogic.diagnostics.flightrecorder.event.LoggingEvent;
import weblogic.diagnostics.flightrecorder.event.ServletAsyncActionEvent;
import weblogic.diagnostics.flightrecorder.event.ServletBaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.ServletCheckAccessEvent;
import weblogic.diagnostics.flightrecorder.event.ServletContextExecuteEvent;
import weblogic.diagnostics.flightrecorder.event.ServletContextHandleThrowableEvent;
import weblogic.diagnostics.flightrecorder.event.ServletExecuteEvent;
import weblogic.diagnostics.flightrecorder.event.ServletFilterEvent;
import weblogic.diagnostics.flightrecorder.event.ServletInvocationEvent;
import weblogic.diagnostics.flightrecorder.event.ServletRequestActionEvent;
import weblogic.diagnostics.flightrecorder.event.ServletRequestCancelEvent;
import weblogic.diagnostics.flightrecorder.event.ServletRequestDispatchEvent;
import weblogic.diagnostics.flightrecorder.event.ServletRequestOverloadEvent;
import weblogic.diagnostics.flightrecorder.event.ServletRequestRunEvent;
import weblogic.diagnostics.flightrecorder.event.ServletResponseSendEvent;
import weblogic.diagnostics.flightrecorder.event.ServletResponseWriteHeadersEvent;
import weblogic.diagnostics.flightrecorder.event.ServletStaleResourceEvent;
import weblogic.diagnostics.flightrecorder.event.ThrottleInformationEvent;
import weblogic.diagnostics.flightrecorder.event.WLLogRecordEvent;
import weblogic.diagnostics.flightrecorder.event.WebApplicationBaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.WebApplicationLoadEvent;
import weblogic.diagnostics.flightrecorder.event.WebApplicationUnloadEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCBaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCClientRequestActionEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCClientResponseActionEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCDispatchActionEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCRequestActionEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCResponseActionEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSBaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSEndPointEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSRequestActionEvent;
import weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSResourceEvent;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.logging.WLLogRecord;
import weblogic.management.configuration.LogFilterMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DataGatheringManager implements PropertyChangeListener {
   private static DebugLogger debugLog = DebugLogger.getDebugLogger("DebugDiagnosticDataGathering");
   private static boolean initialized = false;
   private static boolean gatheringEnabledDetermined = false;
   private static boolean gatheringEnabled = true;
   public static final int OFF = 0;
   public static final int LOW = 1;
   public static final int MEDIUM = 2;
   public static final int HIGH = 3;
   public static final int[] MAX_CHUNK_SIZE_MULTIPLE = new int[]{3, 3, 4, 5};
   public static boolean[] ENABLE_STACK_TRACES = new boolean[]{false, false, false, true};
   public static boolean jfrActionsDisabled = false;
   private static boolean constantPoolsEnabled = true;
   private static int diagnosticVolume = 0;
   private static int severity = 64;
   private static WLDFServerDiagnosticMBean wldfConfig;
   private static LogFilterMBean serverLogFileFilterConfig;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static RuntimeAccess runtimeAccess;
   private static DataGatheringManager SINGLETON;
   private static Producer baseProducer;
   private static Producer lowProducer;
   private static Producer mediumProducer;
   private static Producer highProducer;
   private Method isGatheringExtended = null;
   private Method updateServerManagedMonitors = null;
   private Object instrumentationManager = null;
   private static final Class[] updateArgs;
   private static WLLog4jLogEventClassHelper wlLog4jLogEventHelper;
   private static List<String> eventClassNamesInUse;

   private static RuntimeAccess getRuntimeAccess() {
      if (runtimeAccess == null) {
         Class var0 = DataGatheringManager.class;
         synchronized(DataGatheringManager.class) {
            if (runtimeAccess == null) {
               runtimeAccess = ManagementService.getRuntimeAccess(KERNEL_ID);
            }
         }
      }

      return runtimeAccess;
   }

   public static void gatherLoggingEvent(Object var0, int var1) {
      if (diagnosticVolume != 0) {
         if (recordBasedOnSeverity(var1)) {
            DataGatheringManager.Helper.recordLoggingEvent(var0);
         }

         if (wlLog4jLogEventHelper.isAvailable(var0)) {
            if (wlLog4jLogEventHelper.isInstance(var0.getClass()) && wlLog4jLogEventHelper.isGatherable(var0)) {
               int var2 = convertVolume(wlLog4jLogEventHelper.getDiagnosticVolume(var0));
               if (diagnosticVolume >= var2) {
                  DataGatheringManager.Helper.recordLoggingEvent(var0);
               }
            }

         }
      }
   }

   public static void gatherLogRecord(LogRecord var0) {
      if (diagnosticVolume != 0) {
         int var1 = LogLevel.getSeverity(var0.getLevel());
         if (recordBasedOnSeverity(LogLevel.getSeverity(var0.getLevel()))) {
            if (var0 instanceof WLLogRecord) {
               DataGatheringManager.Helper.recordWLLogRecord(var0);
            } else {
               DataGatheringManager.Helper.recordLogRecord(var0);
            }

         } else if (var0 instanceof BaseLogRecord) {
            BaseLogRecord var2 = (BaseLogRecord)var0;
            if (var2.isGatherable()) {
               int var3 = convertVolume(var2.getDiagnosticVolume());
               if (diagnosticVolume >= var3) {
                  if (var0 instanceof WLLogRecord) {
                     DataGatheringManager.Helper.recordWLLogRecord(var0);
                  } else {
                     DataGatheringManager.Helper.recordLogRecord(var0);
                  }
               }

            }
         }
      }
   }

   private static boolean recordBasedOnSeverity(int var0) {
      switch (var0) {
         case 1:
         case 2:
         case 4:
            return true;
         case 8:
            if (diagnosticVolume != 1) {
               return true;
            }
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            return false;
      }
   }

   public static synchronized void initialize() {
      if (!initialized) {
         try {
            String var0 = System.getProperty("weblogic.diagnostics.instrumentation.gathering.JFRConstantPoolsEnabled", "true");
            if (!var0.equalsIgnoreCase("true")) {
               constantPoolsEnabled = false;
            }

            int var1 = convertVolume(System.getProperty("weblogic.diagnostics.flightrecorder.StackTracesEnabled", "high"));
            ENABLE_STACK_TRACES[1] = var1 != 0 && var1 == 1;
            ENABLE_STACK_TRACES[2] = var1 != 0 && var1 != 3;
            ENABLE_STACK_TRACES[3] = var1 != 0;
            jfrActionsDisabled = Boolean.getBoolean("weblogic.diagnostics.instrumentation.gathering.JFRActionsDisabled");
         } catch (SecurityException var2) {
         }

         wldfConfig = getRuntimeAccess().getServer().getServerDiagnosticConfig();
         diagnosticVolume = convertVolume(wldfConfig.getWLDFDiagnosticVolume());
         if (FlightRecorderManager.isRecordingPossible()) {
            FlightRecorderManager.setDefaultSettings(MAX_CHUNK_SIZE_MULTIPLE[diagnosticVolume], ENABLE_STACK_TRACES[diagnosticVolume]);
            initializeProducers();
         }

         SINGLETON = new DataGatheringManager();
         wldfConfig.addPropertyChangeListener(SINGLETON);
         initialized = true;
      }
   }

   public static synchronized void setEventClassNamesInUse(List<String> var0) {
      if (eventClassNamesInUse == null) {
         eventClassNamesInUse = var0;
      }

   }

   public static boolean isGatheringEnabled() {
      if (gatheringEnabledDetermined) {
         return gatheringEnabled;
      } else {
         Class var0 = DataGatheringManager.class;
         synchronized(DataGatheringManager.class) {
            if (FlightRecorderManager.isRecordingPossible()) {
               gatheringEnabled = true;
               gatheringEnabledDetermined = true;
            } else {
               if (SINGLETON == null) {
                  initialize();
               }

               gatheringEnabled = SINGLETON.invokeIsGatheringExtended();
               gatheringEnabledDetermined = true;
               if (gatheringEnabled) {
                  jfrActionsDisabled = true;
               }
            }
         }

         return gatheringEnabled;
      }
   }

   public static boolean jfrActionsDisabled() {
      return jfrActionsDisabled;
   }

   public static synchronized void initializeLogging() {
      if (!initialized) {
         initialize();
      }

      if (!isGatheringEnabled()) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Data Gathering is not enabled, no logger handling is registered for it");
         }

      } else {
         wlLog4jLogEventHelper = WLLog4jLogEventClassHelper.getInstance();
         serverLogFileFilterConfig = getRuntimeAccess().getServer().getLog().getLogFileFilter();
         if (serverLogFileFilterConfig != null) {
            severity = serverLogFileFilterConfig.getSeverityLevel();
         }

         registerLogger();
         if (serverLogFileFilterConfig != null) {
            serverLogFileFilterConfig.addPropertyChangeListener(SINGLETON);
         }

      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      Class var2 = DataGatheringManager.class;
      synchronized(DataGatheringManager.class) {
         int var3 = convertVolume(wldfConfig.getWLDFDiagnosticVolume());
         if (var3 != diagnosticVolume) {
            if (var3 == 0) {
               if (lowProducer != null) {
                  lowProducer.disable();
               }

               if (mediumProducer != null) {
                  mediumProducer.disable();
               }

               if (highProducer != null) {
                  highProducer.disable();
               }

               FlightRecorderManager.disableJVMEventsInRecording();
            } else if (var3 == 1) {
               if (lowProducer != null) {
                  lowProducer.enable();
               }

               if (mediumProducer != null) {
                  mediumProducer.disable();
               }

               if (highProducer != null) {
                  highProducer.disable();
               }

               if (FlightRecorderManager.areJVMEventsExpensive()) {
                  FlightRecorderManager.disableJVMEventsInRecording();
               } else {
                  FlightRecorderManager.enableJVMEventsInRecording();
               }
            } else if (var3 == 2) {
               if (lowProducer != null) {
                  lowProducer.enable();
               }

               if (mediumProducer != null) {
                  mediumProducer.enable();
               }

               if (highProducer != null) {
                  highProducer.disable();
               }

               FlightRecorderManager.enableJVMEventsInRecording();
            } else if (var3 == 3) {
               if (lowProducer != null) {
                  lowProducer.enable();
               }

               if (mediumProducer != null) {
                  mediumProducer.enable();
               }

               if (highProducer != null) {
                  highProducer.enable();
               }

               FlightRecorderManager.enableJVMEventsInRecording();
            }

            diagnosticVolume = var3;
            FlightRecorderManager.enableImageRecordingClientEvents("WLDF ", MAX_CHUNK_SIZE_MULTIPLE[diagnosticVolume], ENABLE_STACK_TRACES[diagnosticVolume]);
            this.invokeUpdateServerManagedMonitors();
         }

         if (serverLogFileFilterConfig != null) {
            int var4 = serverLogFileFilterConfig.getSeverityLevel();
            if (severity != var4) {
               severity = var4;
               registerLogger();
            }
         }

      }
   }

   private void invokeUpdateServerManagedMonitors() {
      if (this.updateServerManagedMonitors == null) {
         try {
            Class var1 = Class.forName("weblogic.diagnostics.instrumentation.InstrumentationManager");
            Method var2 = var1.getDeclaredMethod("getInstrumentationManager", (Class[])null);
            this.instrumentationManager = var2.invoke((Object)null, (Object[])null);
            this.updateServerManagedMonitors = var1.getDeclaredMethod("updateServerManagedMonitors", updateArgs);
         } catch (Exception var3) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("Failed to get InstrumentationManager instance", var3);
            }

            return;
         }
      }

      Object[] var5 = new Object[]{new Integer(diagnosticVolume)};

      try {
         if (debugLog.isDebugEnabled() && !isGatheringEnabled() && diagnosticVolume != 0) {
            debugLog.debug("The diagnostic volume is not off but the server managed monitors will be disabled as gathering is not enabled");
         }

         this.updateServerManagedMonitors.invoke(this.instrumentationManager, var5);
      } catch (Exception var4) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to get InstrumentationManager instance", var4);
         }

      }
   }

   private boolean invokeIsGatheringExtended() {
      if (this.isGatheringExtended == null) {
         try {
            Class var1 = Class.forName("weblogic.diagnostics.instrumentation.InstrumentationManager");
            Method var2 = var1.getDeclaredMethod("getInstrumentationManager", (Class[])null);
            this.instrumentationManager = var2.invoke((Object)null, (Object[])null);
            this.isGatheringExtended = var1.getDeclaredMethod("isGatheringExtended", (Class[])null);
         } catch (Exception var3) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("Failed to get InstrumentationManager instance", var3);
            }

            return true;
         }
      }

      try {
         return (Boolean)this.isGatheringExtended.invoke(this.instrumentationManager, (Object[])null);
      } catch (Exception var4) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to determine if gathering is extended, assume it is just in case", var4);
         }

         return true;
      }
   }

   public static int convertVolume(String var0) {
      if (var0 != null && !var0.equalsIgnoreCase("Off")) {
         if (var0.equalsIgnoreCase("Low")) {
            return 1;
         } else if (var0.equalsIgnoreCase("Medium")) {
            return 2;
         } else {
            return var0.equalsIgnoreCase("High") ? 3 : 0;
         }
      } else {
         return 0;
      }
   }

   public static int getDiagnosticVolume() {
      return diagnosticVolume;
   }

   private static void registerLogger() {
      if (debugLog.isDebugEnabled()) {
         debugLog.debug("registerLogger() called, volume = " + diagnosticVolume + " severity = " + severity);
      }

      DataGatheringLogService.deregisterFromServerLogger();
      if (diagnosticVolume != 0) {
         DataGatheringLogService.registerToServerLogger(severity);
      }

   }

   private static void initializeProducers() {
      if (debugLog.isDebugEnabled()) {
         debugLog.debug("initializeProducers() WLDF Diagnostic volume = " + diagnosticVolume);
      }

      try {
         baseProducer = new Producer("WLDF Base Producer", "WLDF Base Producer", "http://www.oracle.com/wls/flightrecorder/base");
      } catch (URISyntaxException var4) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to create base producer", var4);
         }

         return;
      }

      if (constantPoolsEnabled) {
         baseProducer.createConstantPool(String.class, "GlobalPool", 20480, 10485760, true);
      }

      addEvent(baseProducer, "weblogic.diagnostics.flightrecorder.event.GlobalInformationEvent", GlobalInformationEvent.class);
      baseProducer.register();

      try {
         lowProducer = new Producer("WLDF Low Diagnostic Volume Producer", "WLDF Low Diagnostic Volume Producer", "http://www.oracle.com/wls/flightrecorder/low");
      } catch (URISyntaxException var2) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to create low producer", var2);
         }

         return;
      }

      if (constantPoolsEnabled) {
         lowProducer.createConstantPool(String.class, "GlobalPool", 20480, 10485760, true);
         lowProducer.createConstantPool(String.class, "LocalPool", 20480, 1048576, true);
      }

      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ThrottleInformationEvent", ThrottleInformationEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.BaseInstantEvent", BaseInstantEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorBaseEvent", ConnectorBaseEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorEndpointBaseEvent", ConnectorEndpointBaseEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorTransactionBaseEvent", ConnectorTransactionBaseEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.EJBBaseInstantEvent", EJBBaseInstantEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.JDBCBaseInstantEvent", JDBCBaseInstantEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.BaseTimedEvent", BaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.EJBBaseTimedEvent", EJBBaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.JDBCBaseTimedEvent", JDBCBaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ServletBaseTimedEvent", ServletBaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebApplicationBaseTimedEvent", WebApplicationBaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.LoggingEvent", LoggingEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.LogRecordEvent", LogRecordEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WLLogRecordEvent", WLLogRecordEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorActivateEndpointEvent", ConnectorActivateEndpointEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorDeactivateEndpointEvent", ConnectorDeactivateEndpointEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorInboundTransactionRollbackEvent", ConnectorInboundTransactionRollbackEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundConnectionErrorEvent", ConnectorOutboundConnectionErrorEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundDestroyConnectionEvent", ConnectorOutboundDestroyConnectionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundRegisterResourceEvent", ConnectorOutboundRegisterResourceEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundReleaseConnectionEvent", ConnectorOutboundReleaseConnectionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundReserveConnectionEvent", ConnectorOutboundReserveConnectionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundTransactionRollbackEvent", ConnectorOutboundTransactionRollbackEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ConnectorOutboundUnregisterResourceEvent", ConnectorOutboundUnregisterResourceEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodInvokeEvent", EJBBusinessMethodInvokeEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodPostInvokeEvent", EJBBusinessMethodPostInvokeEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodPreInvokeEvent", EJBBusinessMethodPreInvokeEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionRollbackEvent", JDBCConnectionRollbackEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.JDBCStatementExecuteEvent", JDBCStatementExecuteEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionRollbackEvent", JDBCTransactionRollbackEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.ServletInvocationEvent", ServletInvocationEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebApplicationLoadEvent", WebApplicationLoadEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebApplicationUnloadEvent", WebApplicationUnloadEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCBaseTimedEvent", WebservicesJAXRPCBaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCClientRequestActionEvent", WebservicesJAXRPCClientRequestActionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCClientResponseActionEvent", WebservicesJAXRPCClientResponseActionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCDispatchActionEvent", WebservicesJAXRPCDispatchActionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCRequestActionEvent", WebservicesJAXRPCRequestActionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXRPCResponseActionEvent", WebservicesJAXRPCResponseActionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSBaseTimedEvent", WebservicesJAXWSBaseTimedEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSEndPointEvent", WebservicesJAXWSEndPointEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSRequestActionEvent", WebservicesJAXWSRequestActionEvent.class);
      addEvent(lowProducer, "weblogic.diagnostics.flightrecorder.event.WebservicesJAXWSResourceEvent", WebservicesJAXWSResourceEvent.class);
      lowProducer.register();
      if (diagnosticVolume < 1) {
         lowProducer.disable();
      }

      try {
         mediumProducer = new Producer("WLDF Medium Diagnostic Volume Producer", "WLDF Medium Diagnostic Volume Producer", "http://www.oracle.com/wls/flightrecorder/medium");
      } catch (URISyntaxException var3) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to create medium producer", var3);
         }

         return;
      }

      if (constantPoolsEnabled) {
         mediumProducer.createConstantPool(String.class, "GlobalPool", 20480, 10485760, true);
         mediumProducer.createConstantPool(String.class, "LocalPool", 20480, 1048576, true);
      }

      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.EJBHomeCreateEvent", EJBHomeCreateEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.EJBHomeRemoveEvent", EJBHomeRemoveEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.EJBPoolManagerCreateEvent", EJBPoolManagerCreateEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.EJBPoolManagerPostInvokeEvent", EJBPoolManagerPostInvokeEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.EJBPoolManagerPreInvokeEvent", EJBPoolManagerPreInvokeEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionCloseEvent", JDBCConnectionCloseEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionCommitEvent", JDBCConnectionCommitEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionCreateStatementEvent", JDBCConnectionCreateStatementEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionGetVendorConnectionEvent", JDBCConnectionGetVendorConnectionEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionPrepareEvent", JDBCConnectionPrepareEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionReleaseEvent", JDBCConnectionReleaseEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCConnectionReserveEvent", JDBCConnectionReserveEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCDataSourceGetConnectionEvent", JDBCDataSourceGetConnectionEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCDriverConnectEvent", JDBCDriverConnectEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JDBCStatementCreationEvent", JDBCStatementCreationEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletExecuteEvent", ServletExecuteEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletRequestRunEvent", ServletRequestRunEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletRequestDispatchEvent", ServletRequestDispatchEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletRequestActionEvent", ServletRequestActionEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletFilterEvent", ServletFilterEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletAsyncActionEvent", ServletAsyncActionEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletContextExecuteEvent", ServletContextExecuteEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletResponseWriteHeadersEvent", ServletResponseWriteHeadersEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletResponseSendEvent", ServletResponseSendEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletStaleResourceEvent", ServletStaleResourceEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.ServletCheckAccessEvent", ServletCheckAccessEvent.class);
      addEvent(mediumProducer, "weblogic.diagnostics.flightrecorder.event.JMSBEConsumerLogEvent", JMSBEConsumerLogEvent.class);
      mediumProducer.register();
      if (diagnosticVolume < 2) {
         mediumProducer.disable();
      }

      try {
         highProducer = new Producer("WLDF High Diagnostic Volume Producer", "WLDF High Diagnostic Volume Producer", "http://www.oracle.com/wls/flightrecorder/high");
      } catch (URISyntaxException var1) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to create high producer", var1);
         }

         return;
      }

      if (constantPoolsEnabled) {
         highProducer.createConstantPool(String.class, "GlobalPool", 20480, 10485760, true);
         highProducer.createConstantPool(String.class, "LocalPool", 20480, 1048576, true);
      }

      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.EJBDatabaseAccessEvent", EJBDatabaseAccessEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.EJBBusinessMethodPostInvokeCleanupEvent", EJBBusinessMethodPostInvokeCleanupEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.EJBPoolManagerRemoveEvent", EJBPoolManagerRemoveEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.EJBReplicatedSessionManagerEvent", EJBReplicatedSessionManagerEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.EJBTimerManagerEvent", EJBTimerManagerEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionBaseEvent", JDBCTransactionBaseEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionCommitEvent", JDBCTransactionCommitEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionEndEvent", JDBCTransactionEndEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionGetXAResourceEvent", JDBCTransactionGetXAResourceEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionIsSameRMEvent", JDBCTransactionIsSameRMEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionPrepareEvent", JDBCTransactionPrepareEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JDBCTransactionStartEvent", JDBCTransactionStartEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JTABaseInstantEvent", JTABaseInstantEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JTATransactionCommitEvent", JTATransactionCommitEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JTATransactionEndEvent", JTATransactionEndEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JTATransactionPreparedEvent", JTATransactionPreparedEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JTATransactionPrepareEvent", JTATransactionPrepareEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.JTATransactionStartEvent", JTATransactionStartEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.ServletRequestOverloadEvent", ServletRequestOverloadEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.ServletRequestCancelEvent", ServletRequestCancelEvent.class);
      addEvent(highProducer, "weblogic.diagnostics.flightrecorder.event.ServletContextHandleThrowableEvent", ServletContextHandleThrowableEvent.class);
      highProducer.register();
      if (diagnosticVolume != 3) {
         highProducer.disable();
      }

   }

   private static void addEvent(Producer var0, String var1, Class var2) {
      try {
         if (eventClassNamesInUse != null && eventClassNamesInUse.contains(var1)) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("Adding " + var1 + " to " + var0.getName());
            }

            EventToken var3 = var0.addEvent(var2);
            FlightRecorderManager.addEvent(var3, var2);
         } else if (debugLog.isDebugEnabled()) {
            debugLog.debug(var1 + " not used by instrumentation config, not registering it");
         }
      } catch (InvalidEventDefinitionException var4) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to add " + var1 + " to " + var0.getName(), var4);
         }

         return;
      } catch (InvalidValueException var5) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("Failed to add " + var1 + " to " + var0.getName(), var5);
         }

         return;
      }

      FlightRecorderEventHelper.getInstance().registerEventClass(var1, var2);
   }

   static {
      updateArgs = new Class[]{Integer.TYPE};
      eventClassNamesInUse = null;
   }

   static class Helper {
      static final long serialVersionUID = -106921881993537274L;
      public static final String _WLDF$INST_VERSION = "9.0.0";
      // $FF: synthetic field
      static Class _WLDF$INST_FLD_class = Class.forName("weblogic.diagnostics.instrumentation.gathering.DataGatheringManager$Helper");
      public static final DelegatingMonitor _WLDF$INST_FLD_Log_Record_Diagnostic_Volume_Before_Low;
      public static final DelegatingMonitor _WLDF$INST_FLD_Logging_Event_Diagnostic_Volume_Before_Low;
      public static final DelegatingMonitor _WLDF$INST_FLD_WLLog_Record_Diagnostic_Volume_Before_Low;
      public static final JoinPoint _WLDF$INST_JPFLD_0;
      public static final JoinPoint _WLDF$INST_JPFLD_1;
      public static final JoinPoint _WLDF$INST_JPFLD_2;

      public static void recordLoggingEvent(Object var0) {
         if (_WLDF$INST_FLD_Logging_Event_Diagnostic_Volume_Before_Low.isEnabledAndNotDyeFiltered()) {
            Object[] var2 = null;
            if (_WLDF$INST_FLD_Logging_Event_Diagnostic_Volume_Before_Low.isArgumentsCaptureNeeded()) {
               var2 = new Object[]{var0};
            }

            DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var2, (Object)null);
            DelegatingMonitor var10001 = _WLDF$INST_FLD_Logging_Event_Diagnostic_Volume_Before_Low;
            InstrumentationSupport.process(var10000, var10001, var10001.getActions());
         }

      }

      public static void recordLogRecord(LogRecord var0) {
         if (_WLDF$INST_FLD_Log_Record_Diagnostic_Volume_Before_Low.isEnabledAndNotDyeFiltered()) {
            Object[] var2 = null;
            if (_WLDF$INST_FLD_Log_Record_Diagnostic_Volume_Before_Low.isArgumentsCaptureNeeded()) {
               var2 = new Object[]{var0};
            }

            DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var2, (Object)null);
            DelegatingMonitor var10001 = _WLDF$INST_FLD_Log_Record_Diagnostic_Volume_Before_Low;
            InstrumentationSupport.process(var10000, var10001, var10001.getActions());
         }

      }

      public static void recordWLLogRecord(LogRecord var0) {
         if (_WLDF$INST_FLD_WLLog_Record_Diagnostic_Volume_Before_Low.isEnabledAndNotDyeFiltered()) {
            Object[] var2 = null;
            if (_WLDF$INST_FLD_WLLog_Record_Diagnostic_Volume_Before_Low.isArgumentsCaptureNeeded()) {
               var2 = new Object[]{var0};
            }

            DynamicJoinPoint var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var2, (Object)null);
            DelegatingMonitor var10001 = _WLDF$INST_FLD_WLLog_Record_Diagnostic_Volume_Before_Low;
            InstrumentationSupport.process(var10000, var10001, var10001.getActions());
         }

      }

      static {
         _WLDF$INST_FLD_Log_Record_Diagnostic_Volume_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Log_Record_Diagnostic_Volume_Before_Low");
         _WLDF$INST_FLD_Logging_Event_Diagnostic_Volume_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Logging_Event_Diagnostic_Volume_Before_Low");
         _WLDF$INST_FLD_WLLog_Record_Diagnostic_Volume_Before_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "WLLog_Record_Diagnostic_Volume_Before_Low");
         _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DataGatheringManager.java", "weblogic.diagnostics.instrumentation.gathering.DataGatheringManager$Helper", "recordLoggingEvent", "(Ljava/lang/Object;)V", 537, InstrumentationSupport.makeMap(new String[]{"Logging_Event_Diagnostic_Volume_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo((String)null, (String)null, false, false)})}), (boolean)1);
         _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DataGatheringManager.java", "weblogic.diagnostics.instrumentation.gathering.DataGatheringManager$Helper", "recordLogRecord", "(Ljava/util/logging/LogRecord;)V", 538, InstrumentationSupport.makeMap(new String[]{"Log_Record_Diagnostic_Volume_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo((String)null, (String)null, false, false)})}), (boolean)1);
         _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DataGatheringManager.java", "weblogic.diagnostics.instrumentation.gathering.DataGatheringManager$Helper", "recordWLLogRecord", "(Ljava/util/logging/LogRecord;)V", 539, InstrumentationSupport.makeMap(new String[]{"WLLog_Record_Diagnostic_Volume_Before_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo((String)null, (String)null, false, false)})}), (boolean)1);
      }
   }
}
