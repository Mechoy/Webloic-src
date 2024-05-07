package weblogic.diagnostics.context;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.flightrecorder.event.ThrottleInformationEventInfo;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.kernel.AuditableThreadLocal;
import weblogic.kernel.AuditableThreadLocalFactory;
import weblogic.kernel.ThreadLocalInitialValue;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;

public final class DiagnosticContextManager implements TimerListener, PropertyChangeListener {
   private static final DebugLogger DEBUG_LOGGER;
   private static final DiagnosticContextManager singleton;
   private static final AuthenticatedSubject kernelId;
   private boolean enabled;
   private boolean initialized;
   private static WLSDiagnosticContextFactoryImpl contextFactory;
   private static final String THROTTLE_RATE_REFRESH_PERIOD_PROPERTY = "weblogic.diagnostics.context.throttlerate_refresh_period";
   private static final String THROTTLING_MAX_EVENT_PER_SECOND_GOAL_PROPERTY = "weblogic.diagnostics.context.throttle_max_event_per_second_goal";
   private static final String THROTTLING_MAX_SELECTED_REQUESTS_PER_SECOND_GOAL_PROPERTY = "weblogic.diagnostics.context.throttle_max_selected_requests_per_second_goal";
   private static final int THROTTLE_RATE_REFRESH_PERIOD;
   private static final int THROTTLE_RATE_REFRESH_SECS;
   private static final int THROTTLING_MAX_EVENT_PER_SECOND_GOAL;
   private static final int THROTTLING_MAX_SELECTED_REQUESTS_PER_SECOND_GOAL;
   private static AtomicInteger requestSeqId;
   private static int ctxThrottleRate;
   private static AtomicInteger ctxJFREventsInTimerWindow;
   private static long lastRefreshTime;
   private TimerManager timerManager;
   private static AtomicLong runningEventCount;
   private static long previousEventCount;
   private static volatile int requestTotalInPeriod;
   private static volatile int requestSelectedInPeriod;
   private static int previousRequestTotal;
   private static int previousSelectedTotal;
   private static int periodsSinceThrottleChanged;
   private static Map<Long, WeakReference<DiagnosticContextWrapper>> diagnosticContextWrappers;
   private ThrottleInfoImpl throttleInfo = new ThrottleInfoImpl();
   private static AuditableThreadLocal localDiagnosticContext;
   static final long serialVersionUID = 3954498904932759198L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.diagnostics.context.DiagnosticContextManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_Throttleinfo_Diagnostic_Volume_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public static DiagnosticContextManager getDiagnosticContextManager() {
      return singleton;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      DiagnosticContextFactory.setEnabled(var1);
   }

   public int getPropagationMode() {
      return DiagnosticContextFactory.getPropagationMode();
   }

   public void setPropagationMode(int var1) {
      DiagnosticContextFactory.setPropagationMode(var1);
   }

   public void initialize() {
      if (!this.initialized) {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         ServerMBean var2 = var1.getServer();
         WLDFServerDiagnosticMBean var3 = var2 != null ? var2.getServerDiagnosticConfig() : null;
         if (var3 != null) {
            this.setEnabled(var3.isDiagnosticContextEnabled());
            var3.addPropertyChangeListener(this);
            contextFactory = new WLSDiagnosticContextFactoryImpl();
            DiagnosticContextFactory.setFactory(contextFactory);
            TimerManagerFactory var4 = TimerManagerFactory.getTimerManagerFactory();
            this.timerManager = var4.getDefaultTimerManager();
            this.timerManager.scheduleAtFixedRate(this, 0L, (long)THROTTLE_RATE_REFRESH_PERIOD);
            this.initialized = true;
         }
      }
   }

   public void propertyChange(PropertyChangeEvent var1) {
      this.attributesChanged(var1.getSource());
   }

   private void attributesChanged(Object var1) {
      if (var1 instanceof WLDFServerDiagnosticMBean) {
         WLDFServerDiagnosticMBean var2 = (WLDFServerDiagnosticMBean)var1;
         this.setEnabled(var2.isDiagnosticContextEnabled());
      }

   }

   public static boolean isJFRThrottled() {
      int var0 = ctxThrottleRate;
      if (localDiagnosticContext != null && var0 > 1) {
         DiagnosticContextWrapper var1 = (DiagnosticContextWrapper)localDiagnosticContext.get();
         return var1.isSelected();
      } else {
         return true;
      }
   }

   public static String getDiagnosticContextId(long var0) {
      WeakReference var2 = (WeakReference)diagnosticContextWrappers.get(var0);
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Found DiagnosticContextWrapper ref = " + var2 + " for thread id = " + var0);
      }

      if (var2 != null) {
         DiagnosticContextWrapper var3 = (DiagnosticContextWrapper)var2.get();
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Found DiagnosticContextWrapper wrapper = " + var3 + " for thread id = " + var0);
         }

         if (var3 != null) {
            DiagnosticContext var4 = var3.getDiagnosticContext();
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("DiagnosticContextWrapper context = " + var4);
            }

            if (var4 != null) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("DiagnosticContext id = " + var4.getContextId() + " for thread id = " + var0);
               }

               return var4.getContextId();
            }
         }
      }

      return "";
   }

   public void timerExpired(Timer var1) {
      long var2 = System.currentTimeMillis();
      long var4 = var2 - lastRefreshTime;
      int var6 = requestTotalInPeriod;
      int var7 = requestSelectedInPeriod;
      int var8 = ctxJFREventsInTimerWindow.get();
      if (DEBUG_LOGGER.isDebugEnabled()) {
         long var9 = runningEventCount.get();
         DEBUG_LOGGER.debug("Event counts period (running, period) = " + (var9 - previousEventCount) + ", " + var8);
         previousEventCount = var9;
      }

      int var17 = 1;
      float var10 = 0.0F;
      float var11 = 0.0F;
      float var12 = 0.0F;
      float var13 = 0.0F;
      var10 = (float)(var6 / THROTTLE_RATE_REFRESH_SECS);
      if (var7 > 0 && var8 > 0) {
         var11 = (float)var8 / (float)var7;
         var12 = (float)THROTTLING_MAX_EVENT_PER_SECOND_GOAL / var11;
         var17 = Math.round(var10 / var12);
      }

      if (var17 < 1) {
         var17 = 1;
      }

      var13 = var10 / (float)var17;
      if (var13 > (float)THROTTLING_MAX_SELECTED_REQUESTS_PER_SECOND_GOAL) {
         var17 = Math.round(var10 / (float)THROTTLING_MAX_SELECTED_REQUESTS_PER_SECOND_GOAL);
      }

      if (var17 < 1) {
         var17 = 1;
      }

      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug(" Last period Elapsed time: " + var4 + "   requests/sec: " + var10 + "   selected reqs: " + var7 + "   total reqs: " + var6 + "   events: " + var8 + "   events per request: " + var11 + "   projected selected requests: " + var13 + "   old throttle rate: " + ctxThrottleRate + "   new throttle rate: " + var17 + "   timer ECID: " + (new DiagnosticContextImpl()).getContextId());
      }

      synchronized(this.throttleInfo) {
         this.throttleInfo.setLastPeriodDuration(var4);
         this.throttleInfo.setRequestsSelectedLastPeriod(var7);
         this.throttleInfo.setRequestsSeenLastPeriod(var6);
         this.throttleInfo.setEventsGeneratedLastPeriod(var8);
         this.throttleInfo.setAverageEventsPerRequestLastPeriod(var11);
         this.throttleInfo.setProjectedSelectedRequestsPerSecBeforeCapCheck(Math.round(var13));
         this.throttleInfo.setPreviousThrottleRate(ctxThrottleRate);
         this.throttleInfo.setCurrentThrottleRate(var17);
         this.throttleInfo.setPeriodsSinceLastThrottleChange(periodsSinceThrottleChanged);
      }

      if (ctxThrottleRate != var17) {
         this.triggerThrottleInformationEvent();
         periodsSinceThrottleChanged = 1;
      } else {
         ++periodsSinceThrottleChanged;
      }

      ctxThrottleRate = var17;
      previousRequestTotal = var6;
      previousSelectedTotal = var7;
      requestSelectedInPeriod = 0;
      requestTotalInPeriod = 0;
      lastRefreshTime = var2;
      ctxJFREventsInTimerWindow.set(0);
   }

   public static void incrementJFREventCounter() {
      ctxJFREventsInTimerWindow.incrementAndGet();
      runningEventCount.incrementAndGet();
   }

   public ThrottleInformationEventInfo triggerThrottleInformationEvent() {
      boolean var11 = false;

      ThrottleInfoImpl var10000;
      ThrottleInfoImpl var5;
      try {
         var11 = true;
         synchronized(this.throttleInfo) {
            var10000 = this.throttleInfo;
            var11 = false;
         }
      } finally {
         if (var11) {
            var5 = null;
            if (_WLDF$INST_FLD_Throttleinfo_Diagnostic_Volume_After_Low.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var10002 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var5);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Throttleinfo_Diagnostic_Volume_After_Low;
               InstrumentationSupport.process(var10002, var10003, var10003.getActions());
            }

         }
      }

      var5 = var10000;
      if (_WLDF$INST_FLD_Throttleinfo_Diagnostic_Volume_After_Low.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var10001 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var5);
         DelegatingMonitor var14 = _WLDF$INST_FLD_Throttleinfo_Diagnostic_Volume_After_Low;
         InstrumentationSupport.process(var10001, var14, var14.getActions());
      }

      return var10000;
   }

   static {
      _WLDF$INST_FLD_Throttleinfo_Diagnostic_Volume_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Throttleinfo_Diagnostic_Volume_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "DiagnosticContextManager.java", "weblogic.diagnostics.context.DiagnosticContextManager", "triggerThrottleInformationEvent", "()Lweblogic/diagnostics/flightrecorder/event/ThrottleInformationEventInfo;", 577, InstrumentationSupport.makeMap(new String[]{"Throttleinfo_Diagnostic_Volume_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, InstrumentationSupport.createValueHandlingInfo("return", (String)null, false, true), (ValueHandlingInfo[])null)}), (boolean)0);
      DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugDiagnosticContext");
      singleton = new DiagnosticContextManager();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      THROTTLE_RATE_REFRESH_PERIOD = Integer.getInteger("weblogic.diagnostics.context.throttlerate_refresh_period", 2000);
      THROTTLE_RATE_REFRESH_SECS = THROTTLE_RATE_REFRESH_PERIOD / 1000;
      THROTTLING_MAX_EVENT_PER_SECOND_GOAL = Integer.getInteger("weblogic.diagnostics.context.throttle_max_event_per_second_goal", 800);
      THROTTLING_MAX_SELECTED_REQUESTS_PER_SECOND_GOAL = Integer.getInteger("weblogic.diagnostics.context.throttle_max_selected_requests_per_second_goal", 128);
      requestSeqId = new AtomicInteger();
      ctxThrottleRate = 1;
      ctxJFREventsInTimerWindow = new AtomicInteger(0);
      lastRefreshTime = System.currentTimeMillis();
      runningEventCount = new AtomicLong(0L);
      previousEventCount = 0L;
      requestTotalInPeriod = 0;
      requestSelectedInPeriod = 0;
      previousRequestTotal = 0;
      previousSelectedTotal = 0;
      periodsSinceThrottleChanged = 1;
      diagnosticContextWrappers = new ConcurrentHashMap();
      localDiagnosticContext = AuditableThreadLocalFactory.createThreadLocal(new ThreadLocalInitialValue() {
         protected Object initialValue() {
            if (DiagnosticContextManager.DEBUG_LOGGER.isDebugEnabled()) {
               DiagnosticContextManager.DEBUG_LOGGER.debug("Invoked DCM.initialValue() for thread id=" + Thread.currentThread().getId() + ", name=" + Thread.currentThread().getName(), new Exception());
            }

            return this.initialValue((DiagnosticContextWrapper)null);
         }

         private Object initialValue(DiagnosticContextWrapper var1) {
            boolean var2 = false;
            if (var1 == null) {
               var1 = new DiagnosticContextWrapper();
               var2 = true;
            }

            long var3 = Thread.currentThread().getId();
            if (!DiagnosticContextManager.diagnosticContextWrappers.containsKey(var3)) {
               if (DiagnosticContextManager.DEBUG_LOGGER.isDebugEnabled()) {
                  DiagnosticContextManager.DEBUG_LOGGER.debug("Populating DC Wrapper map for thread id=" + Thread.currentThread().getId() + ", name=" + Thread.currentThread().getName() + ", wrapper=" + var1);
               }

               DiagnosticContextManager.diagnosticContextWrappers.put(var3, new WeakReference(var1));
            } else if (DiagnosticContextManager.DEBUG_LOGGER.isDebugEnabled() & var2) {
               DiagnosticContextManager.DEBUG_LOGGER.debug("DC Wrapper already exists for thread id=" + Thread.currentThread().getId() + ", name=" + Thread.currentThread().getName() + ", Wrapper for child thread=" + var1, new Exception());
            }

            var1.reset();
            return var1;
         }

         protected Object resetValue(Object var1) {
            return this.initialValue((DiagnosticContextWrapper)var1);
         }
      });
   }

   private static class ThrottleInfoImpl implements ThrottleInformationEventInfo {
      private float averageEventsPerRequestLastPeriod;
      private int currentThrottleRate;
      private int eventsGeneratedLastPeriod;
      private long lastPeriodDuration;
      private int previousThrottleRate;
      private int projectedSelectedRequestsPerSecBeforeCapCheck;
      private int requestsSeenLastPeriod;
      private int requestsSelectedLastPeriod;
      private int periodsSinceLastThrottleChange;

      private ThrottleInfoImpl() {
      }

      public float getAverageEventsPerRequestLastPeriod() {
         return this.averageEventsPerRequestLastPeriod;
      }

      public int getCurrentThrottleRate() {
         return this.currentThrottleRate;
      }

      public int getEventsGeneratedLastPeriod() {
         return this.eventsGeneratedLastPeriod;
      }

      public long getLastPeriodDuration() {
         return this.lastPeriodDuration;
      }

      public int getPeriodsSinceLastThrottleChange() {
         return this.periodsSinceLastThrottleChange;
      }

      public int getPreviousThrottleRate() {
         return this.previousThrottleRate;
      }

      public int getProjectedSelectedRequestsPerSecBeforeCapCheck() {
         return this.projectedSelectedRequestsPerSecBeforeCapCheck;
      }

      public int getRequestsSeenLastPeriod() {
         return this.requestsSeenLastPeriod;
      }

      public int getRequestsSelectedLastPeriod() {
         return this.requestsSelectedLastPeriod;
      }

      public void setAverageEventsPerRequestLastPeriod(float var1) {
         this.averageEventsPerRequestLastPeriod = var1;
      }

      public void setCurrentThrottleRate(int var1) {
         this.currentThrottleRate = var1;
      }

      public void setEventsGeneratedLastPeriod(int var1) {
         this.eventsGeneratedLastPeriod = var1;
      }

      public void setLastPeriodDuration(long var1) {
         this.lastPeriodDuration = var1;
      }

      public void setPeriodsSinceLastThrottleChange(int var1) {
         this.periodsSinceLastThrottleChange = var1;
      }

      public void setPreviousThrottleRate(int var1) {
         this.previousThrottleRate = var1;
      }

      public void setProjectedSelectedRequestsPerSecBeforeCapCheck(int var1) {
         this.projectedSelectedRequestsPerSecBeforeCapCheck = var1;
      }

      public void setRequestsSeenLastPeriod(int var1) {
         this.requestsSeenLastPeriod = var1;
      }

      public void setRequestsSelectedLastPeriod(int var1) {
         this.requestsSelectedLastPeriod = var1;
      }

      // $FF: synthetic method
      ThrottleInfoImpl(Object var1) {
         this();
      }
   }

   private static final class WLSDiagnosticContextFactoryImpl implements DiagnosticContextFactory.Factory {
      private WLSDiagnosticContextFactoryImpl() {
      }

      public DiagnosticContext findOrCreateDiagnosticContext(final boolean var1) {
         DiagnosticContext var2 = null;
         final DiagnosticContextWrapper var3 = (DiagnosticContextWrapper)DiagnosticContextManager.localDiagnosticContext.get();
         var2 = var3.getDiagnosticContext();
         if (var2 != null) {
            return var2;
         } else {
            try {
               var2 = (DiagnosticContext)SecurityServiceManager.runAs(DiagnosticContextManager.kernelId, DiagnosticContextManager.kernelId, new PrivilegedExceptionAction() {
                  public Object run() throws Exception {
                     WorkContextMap var1x = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
                     Object var2 = (DiagnosticContext)var1x.get("weblogic.diagnostics.DiagnosticContext");
                     if (var2 == null && var1) {
                        var2 = new DiagnosticContextImpl();
                        int var3x = DiagnosticContextFactory.getPropagationMode();
                        var1x.put("weblogic.diagnostics.DiagnosticContext", (WorkContext)var2, var3x);
                     }

                     var3.setDiagnosticContext((DiagnosticContext)var2);
                     if (var2 != null && var3.isSelected()) {
                        long var5 = ((DiagnosticContext)var2).getDyeVector();
                        ((DiagnosticContext)var2).setDyeVector(var5 | 8589934592L);
                     }

                     return var2;
                  }
               });
            } catch (Throwable var5) {
               if (DiagnosticContextManager.DEBUG_LOGGER.isDebugEnabled()) {
                  var5.printStackTrace();
               }
            }

            return var2;
         }
      }

      public void invalidateCache() {
         DiagnosticContextWrapper var1 = (DiagnosticContextWrapper)DiagnosticContextManager.localDiagnosticContext.get();
         var1.setDiagnosticContext((DiagnosticContext)null);
      }

      public void setJFRThrottled(DiagnosticContext var1) {
         if (var1 != null) {
            long var2 = var1.getDyeVector();
            if ((var2 & 8589934592L) != 0L) {
               DiagnosticContextWrapper var4 = (DiagnosticContextWrapper)DiagnosticContextManager.localDiagnosticContext.get();
               var4.setSelected(true);
            }
         }

      }

      public void setDiagnosticContext(final DiagnosticContext var1) {
         final DiagnosticContextWrapper var2 = (DiagnosticContextWrapper)DiagnosticContextManager.localDiagnosticContext.get();

         try {
            SecurityServiceManager.runAs(DiagnosticContextManager.kernelId, DiagnosticContextManager.kernelId, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  WorkContextMap var1x = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
                  int var2x = DiagnosticContextFactory.getPropagationMode();
                  if (var1 == null) {
                     if (var1x.get("weblogic.diagnostics.DiagnosticContext") != null) {
                        var1x.remove("weblogic.diagnostics.DiagnosticContext");
                        WLSDiagnosticContextFactoryImpl.this.invalidateCache();
                     }
                  } else {
                     var1x.put("weblogic.diagnostics.DiagnosticContext", var1, var2x);
                  }

                  var2.setDiagnosticContext(var1);
                  return null;
               }
            });
         } catch (Throwable var4) {
            if (DiagnosticContextManager.DEBUG_LOGGER.isDebugEnabled()) {
               var4.printStackTrace();
            }
         }

      }

      // $FF: synthetic method
      WLSDiagnosticContextFactoryImpl(Object var1) {
         this();
      }
   }

   private static final class DiagnosticContextWrapper {
      private boolean selected;
      private DiagnosticContext ctx;

      private DiagnosticContextWrapper() {
      }

      DiagnosticContext getDiagnosticContext() {
         return this.ctx;
      }

      void setDiagnosticContext(DiagnosticContext var1) {
         this.ctx = var1;
      }

      boolean isSelected() {
         return this.selected;
      }

      void setSelected(boolean var1) {
         if (var1) {
            DiagnosticContextManager.requestSeqId.set(0);
            DiagnosticContextManager.requestSelectedInPeriod++;
         }

         this.selected = var1;
      }

      void reset() {
         this.ctx = null;
         int var1 = DiagnosticContextManager.ctxThrottleRate;
         if (var1 <= 1) {
            this.selected = true;
            DiagnosticContextManager.requestTotalInPeriod++;
            DiagnosticContextManager.requestSelectedInPeriod++;
         } else {
            this.selected = false;
            int var2 = DiagnosticContextManager.requestSeqId.incrementAndGet();
            DiagnosticContextManager.requestTotalInPeriod++;
            if (var2 >= var1) {
               if (var2 == var1) {
                  this.setSelected(true);
               } else {
                  DiagnosticContextManager.requestSeqId.set(0);
               }
            }

         }
      }

      // $FF: synthetic method
      DiagnosticContextWrapper(Object var1) {
         this();
      }
   }
}
