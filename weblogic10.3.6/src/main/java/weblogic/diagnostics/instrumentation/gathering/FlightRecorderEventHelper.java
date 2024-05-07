package weblogic.diagnostics.instrumentation.gathering;

import com.bea.logging.BaseLogRecord;
import com.oracle.jrockit.jfr.InstantEvent;
import com.oracle.jrockit.jfr.TimedEvent;
import java.security.AccessController;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.LogRecord;
import javax.security.auth.Subject;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.diagnostics.context.DiagnosticContextManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.flightrecorder.event.BaseInstantEvent;
import weblogic.diagnostics.flightrecorder.event.BaseTimedEvent;
import weblogic.diagnostics.flightrecorder.event.GlobalInformationEvent;
import weblogic.diagnostics.flightrecorder.event.GlobalInformationEventInfoHelper;
import weblogic.diagnostics.flightrecorder.event.LogRecordEvent;
import weblogic.diagnostics.flightrecorder.event.LoggingEvent;
import weblogic.diagnostics.flightrecorder.event.ThrottleInformationEvent;
import weblogic.diagnostics.flightrecorder.event.ThrottleInformationEventInfoHelper;
import weblogic.diagnostics.flightrecorder.event.WLLogRecordEvent;
import weblogic.diagnostics.instrumentation.DiagnosticMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.GatheredArgument;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.kernel.Kernel;
import weblogic.security.Security;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.spi.WLSUser;
import weblogic.transaction.TxHelper;

public class FlightRecorderEventHelper {
   private static DebugLogger debugLog = DebugLogger.getDebugLogger("DebugDiagnosticDataGathering");
   private static final DebugLogger diagnosticContextDebugLogger = DebugLogger.getDebugLogger("DebugDiagnosticContext");
   private static FlightRecorderEventHelper SINGLETON = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Map<String, Class> eventClassMap = new HashMap();
   private WLLog4jLogEventClassHelper wlLog4jLogEventClassHelper;

   private FlightRecorderEventHelper() {
      this.initialize();
   }

   private void initialize() {
   }

   public static FlightRecorderEventHelper getInstance() {
      if (SINGLETON == null) {
         Class var0 = FlightRecorderEventHelper.class;
         synchronized(FlightRecorderEventHelper.class) {
            if (SINGLETON == null) {
               SINGLETON = new FlightRecorderEventHelper();
            }
         }
      }

      return SINGLETON;
   }

   public void recordStatelessEvent(DiagnosticMonitor var1, JoinPoint var2) {
      int var3 = DataGatheringManager.getDiagnosticVolume();
      InstantEvent var4 = this.getInstantEventInstance(var3, var1);
      if (var4 != null) {
         boolean var5 = false;
         if (var4 instanceof BaseInstantEvent) {
            var5 = true;
            this.populateBaseInstantEvent(var1, var2, (BaseInstantEvent)var4);
            if (!((BaseInstantEvent)var4).getThrottled()) {
               DiagnosticContextManager.incrementJFREventCounter();
               var4.commit();
            }
         } else {
            DynamicJoinPoint var6;
            Object var7;
            if (var4 instanceof ThrottleInformationEvent) {
               if (var2 instanceof DynamicJoinPoint) {
                  var6 = (DynamicJoinPoint)var2;
                  var7 = var6.getReturnValue();
                  if (var7 != null) {
                     ThrottleInformationEventInfoHelper.populateExtensions(var7, (ThrottleInformationEvent)var4);
                     DiagnosticContextManager.incrementJFREventCounter();
                     var4.commit();
                  }
               }

            } else if (var4 instanceof WLLogRecordEvent) {
               WLLogRecordEvent var10 = (WLLogRecordEvent)var4;
               var10.initialize((BaseLogRecord)this.getStaticFirstArg(var2));
               DiagnosticContextManager.incrementJFREventCounter();
               var4.commit();
            } else if (var4 instanceof LogRecordEvent) {
               LogRecordEvent var9 = (LogRecordEvent)var4;
               var9.initialize((LogRecord)this.getStaticFirstArg(var2));
               DiagnosticContextManager.incrementJFREventCounter();
               var4.commit();
            } else if (var4 instanceof LoggingEvent) {
               if (this.wlLog4jLogEventClassHelper == null) {
                  this.wlLog4jLogEventClassHelper = WLLog4jLogEventClassHelper.getInstance();
               }

               Object var8 = this.getStaticFirstArg(var2);
               if (var8 != null && this.wlLog4jLogEventClassHelper.isAvailable(var8) && this.wlLog4jLogEventClassHelper.isInstance(var8.getClass())) {
                  WLLogRecordEvent var12 = this.wlLog4jLogEventClassHelper.populateWLLogRecordEvent(var8);
                  DiagnosticContextManager.incrementJFREventCounter();
                  var12.commit();
               } else {
                  LoggingEvent var11 = (LoggingEvent)var4;
                  var11.initialize(this.getStaticFirstArg(var2));
                  DiagnosticContextManager.incrementJFREventCounter();
                  var4.commit();
               }
            } else if (var4 instanceof GlobalInformationEvent) {
               if (var2 instanceof DynamicJoinPoint) {
                  var6 = (DynamicJoinPoint)var2;
                  var7 = var6.getReturnValue();
                  if (var7 != null) {
                     GlobalInformationEventInfoHelper.populateExtensions(var7, (GlobalInformationEvent)var4);
                     DiagnosticContextManager.incrementJFREventCounter();
                     var4.commit();
                  }
               }

            }
         }
      }
   }

   private InstantEvent getInstantEventInstance(int var1, DiagnosticMonitor var2) {
      InstantEvent var3 = (InstantEvent)this.getEventClassInstance(var2, InstantEvent.class);
      if (var3 == null) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("No event class found for monitor: " + var2.getName());
         }

         return var3;
      } else {
         if (!var3.shouldWrite()) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("Event should not be written: " + var3);
            }

            var3 = null;
         }

         return var3;
      }
   }

   private void populateBaseInstantEvent(DiagnosticMonitor var1, JoinPoint var2, BaseInstantEvent var3) {
      var3.className = var2.getClassName();
      var3.methodName = var2.getMethodName();
      if (var3.isECIDEnabled()) {
         DiagnosticContext var4 = DiagnosticContextFactory.findOrCreateDiagnosticContext(true);
         if (var4 != null) {
            var3.ECID = var4.getContextId();
         }
      }

      if (diagnosticContextDebugLogger.isDebugEnabled() && !DiagnosticContextManager.isJFRThrottled()) {
         diagnosticContextDebugLogger.debug("Event generated for a throttled request", new Exception());
      }

      if (Kernel.isInitialized()) {
         var3.transactionID = TxHelper.getTransactionId();
         if (DataGatheringManager.getDiagnosticVolume() >= 2) {
            Subject var7 = Security.getCurrentSubject();
            var3.userID = this.extractCurrentWLSSubject(var7);
            if (var3.userID == null) {
               var3.userID = SubjectUtils.getUsername(var7);
            }
         }
      }

      DynamicJoinPoint var8 = null;
      if (var2 instanceof DynamicJoinPoint) {
         var8 = (DynamicJoinPoint)var2;
      }

      Object var5 = null;
      if (var8 != null && var8.isReturnGathered()) {
         var5 = var8.getReturnValue();
         var3.returnValue = var5 == null ? null : var5.toString();
      }

      GatheredArgument[] var6 = var8 == null ? null : var8.getGatheredArguments();
      if (var5 != null || var6 != null) {
         var3.populateExtensions(var5, var6 == null ? null : var8.getArguments(), var8, true);
      }

   }

   public TimedEvent getTimedEvent(DiagnosticMonitor var1, JoinPoint var2) {
      int var3 = DataGatheringManager.getDiagnosticVolume();
      if (var3 == 0) {
         return null;
      } else {
         TimedEvent var4 = this.getTimedEventInstance(var3, var1);
         if (var4 == null) {
            return null;
         } else {
            if (var4 instanceof BaseTimedEvent) {
               this.populateBaseTimedEventBefore(var1, var2, (BaseTimedEvent)var4);
            }

            return var4;
         }
      }
   }

   private TimedEvent getTimedEventInstance(int var1, DiagnosticMonitor var2) {
      TimedEvent var3 = (TimedEvent)this.getEventClassInstance(var2, TimedEvent.class);
      if (var3 == null) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("No event class found for monitor: " + var2.getName());
         }

         return var3;
      } else {
         if (var3 != null && !var3.getEventInfo().isEnabled()) {
            var3 = null;
         }

         return var3;
      }
   }

   public void recordTimedEvent(DiagnosticMonitor var1, JoinPoint var2, TimedEvent var3) {
      BaseTimedEvent var4 = (BaseTimedEvent)var3;
      if (var2 instanceof DynamicJoinPoint) {
         DynamicJoinPoint var5 = (DynamicJoinPoint)var2;
         if (var4.getThrottled()) {
            return;
         }

         if (var5.isReturnGathered()) {
            Object var6 = var5.getReturnValue();
            var4.returnValue = var6 == null ? null : var6.toString();
            if (var6 == null) {
               var4.returnValue = null;
            } else {
               var4.returnValue = var6.toString();
               var4.populateExtensions(var6, (Object[])null, var5, true);
            }
         } else if (var4.requiresProcessingArgsAfter()) {
            var4.populateExtensions((Object)null, (Object[])null, var5, true);
         }
      }

      if (!var4.getThrottled()) {
         DiagnosticContextManager.incrementJFREventCounter();
         var3.commit();
      }
   }

   private void populateBaseTimedEventBefore(DiagnosticMonitor var1, JoinPoint var2, BaseTimedEvent var3) {
      var3.className = var2.getClassName();
      var3.methodName = var2.getMethodName();
      if (var3.isECIDEnabled()) {
         DiagnosticContext var4 = DiagnosticContextFactory.findOrCreateDiagnosticContext(true);
         if (var4 != null) {
            var3.ECID = var4.getContextId();
         }
      }

      if (diagnosticContextDebugLogger.isDebugEnabled() && !DiagnosticContextManager.isJFRThrottled()) {
         diagnosticContextDebugLogger.debug("Event generated for a throttled request", new Exception());
      }

      if (Kernel.isInitialized()) {
         var3.transactionID = TxHelper.getTransactionId();
         if (DataGatheringManager.getDiagnosticVolume() >= 2) {
            Subject var6 = Security.getCurrentSubject();
            var3.userID = this.extractCurrentWLSSubject(var6);
            if (var3.userID == null) {
               var3.userID = SubjectUtils.getUsername(var6);
            }
         }
      }

      if (var2 instanceof DynamicJoinPoint) {
         DynamicJoinPoint var7 = (DynamicJoinPoint)var2;
         GatheredArgument[] var5 = var7.getGatheredArguments();
         if (var5 != null) {
            var3.populateExtensions((Object)null, var7.getArguments(), var7, false);
         }
      }
   }

   private Object getEventClassInstance(DiagnosticMonitor var1, Class var2) {
      Object var3 = null;
      Class var4 = var1.getEventClass();
      if (var4 != null && var2.isAssignableFrom(var4)) {
         try {
            var3 = var2.cast(var4.newInstance());
         } catch (Exception var6) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("getEventClassInstance failed to get instance of " + var4, var6);
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public void registerEventClass(String var1, Class var2) {
      if (var1 != null && var2 != null) {
         if (!this.eventClassMap.containsKey(var1)) {
            synchronized(this.eventClassMap) {
               if (!this.eventClassMap.containsKey(var1)) {
                  this.eventClassMap.put(var1, var2);
               }

            }
         }
      }
   }

   private Object getStaticFirstArg(JoinPoint var1) {
      if (!(var1 instanceof DynamicJoinPoint)) {
         return null;
      } else {
         DynamicJoinPoint var2 = (DynamicJoinPoint)var1;
         Object[] var3 = var2.getArguments();
         return var3 != null && var3.length != 0 ? var3[0] : null;
      }
   }

   private String extractCurrentWLSSubject(Subject var1) {
      String var2 = null;
      int var3 = var1.getPrincipals().size();
      if (var3 > 0) {
         Iterator var4 = var1.getPrincipals().iterator();

         while(var4.hasNext()) {
            Principal var5 = (Principal)var4.next();
            if (var5 instanceof WLSUser) {
               var2 = var5.getName();
               break;
            }
         }
      } else {
         var2 = WLSPrincipals.getAnonymousUsername();
      }

      return var2;
   }
}
