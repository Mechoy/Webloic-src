package weblogic.diagnostics.watch;

import com.bea.adaptive.harvester.HarvestCallback;
import com.bea.adaptive.harvester.WatchedValues;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.HarvesterInternalAccess;
import weblogic.diagnostics.harvester.WLDFToHarvester;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.instrumentation.InstrumentationEventListener;
import weblogic.diagnostics.instrumentation.InstrumentationManager;
import weblogic.diagnostics.module.SubModuleRegistry;
import weblogic.diagnostics.module.WLDFModuleException;
import weblogic.diagnostics.module.WLDFSubModule;
import weblogic.logging.LogEntry;
import weblogic.management.ManagementException;
import weblogic.management.runtime.WLDFWatchNotificationRuntimeMBean;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.collections.CircularQueue;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class WatchManager implements TimerListener, HarvestCallback, Runnable, InstrumentationEventListener {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static WatchManager watchManager = null;
   private WatchConfiguration watchConfig = null;
   private Map<String, Watch> alarmWatches = new Hashtable();
   private WatchNotificationRuntimeMBeanImpl wnRuntime = WatchNotificationRuntimeMBeanImpl.getInstance();
   private TimerManagerFactory timerManagerFactory;
   private TimerManager timerManager;
   private Timer alarmResetTimer;
   private static final String WORK_MANAGER_NAME = "WatchManagerEvents";
   private static final int MAX_THREADS = 1;
   private Thread asyncLogThread;
   private CircularQueue eventQueue = new CircularQueue();
   private WorkManager workManager;
   private WLDFSubModule watchSubModule = null;
   private WLDFToHarvester wldf2Hv;
   private int wvid;
   private int numActiveImageNotifications;

   private WatchManager() throws ManagementException {
      ImageManager var1 = ImageManager.getInstance();
      var1.registerImageSource("WatchSource", new WatchSource());
      this.workManager = WorkManagerFactory.getInstance().findOrCreate("WatchManagerEvents", 1, 1);
      this.wldf2Hv = HarvesterInternalAccess.getInstance();
   }

   public static synchronized WatchManager getInstance() throws ManagementException {
      if (watchManager == null) {
         watchManager = new WatchManager();
      }

      return watchManager;
   }

   public synchronized void moduleActivated(WatchConfiguration var1) throws WLDFModuleException {
      this.watchConfig = var1;
      this.identifyActiveImageNotifications();
      this.initializeEventListener();
      this.initializeLogEventHandler();
      this.activateConfiguration();
   }

   private void identifyActiveImageNotifications() {
      Watch[] var1 = this.watchConfig.getWatches();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Watch var4 = var1[var3];
         WatchNotificationListener[] var5 = var4.getNotificationListeners();
         if (var5 != null) {
            WatchNotificationListener[] var6 = var5;
            int var7 = var5.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               WatchNotificationListener var9 = var6[var8];
               if (var9 instanceof ImageNotificationListener) {
                  ++this.numActiveImageNotifications;
               }
            }
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Number of active Diagnostic Image listeners: " + this.numActiveImageNotifications);
      }

   }

   public int getNumActiveImageNotifications() {
      return this.numActiveImageNotifications;
   }

   public synchronized void moduleDeactivated() {
      this.watchConfig = null;
      this.deinitializeEventListener();
      this.deinitializeLogEventHandler();
   }

   public void activateConfiguration() {
      if (this.watchConfig != null && this.watchConfig.isWatchNotificationEnabled()) {
         if (this.wldf2Hv.isActivated()) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Activating WatchedNotification Configuration.");
            }

            WatchedValues var1 = this.watchConfig.getWatchedValues();
            if (var1 == null) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("No watched metrics configured.");
               }
            } else if (var1.getId() > -1) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Watched Values already appears initialized, value: " + var1.getId());
               }
            } else if (var1.getAllMetricValues().size() > 0) {
               this.wvid = this.wldf2Hv.addWatchedValues(var1);
            } else if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("No watched metrics configured.");
            }
         } else {
            debugLogger.debug("Watch activation deferred, harvester is not yet activated");
         }
      } else if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Watch not enabled in configuration, no watched values registered with harvester.");
      }

   }

   synchronized WatchConfiguration getWatchConfiguration() {
      return this.watchConfig;
   }

   public WLDFWatchNotificationRuntimeMBean getWatchNotificationRuntime() {
      return this.wnRuntime;
   }

   public WLDFSubModule getWatchSubModule() {
      if (this.watchSubModule == null) {
         WLDFSubModule[] var1 = SubModuleRegistry.getWLDFSubModules();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (WatchSubModule.class.isAssignableFrom(SubModuleRegistry.getWLDFSubModuleType(var1[var2]))) {
               this.watchSubModule = var1[var2];
               break;
            }
         }
      }

      return this.watchSubModule;
   }

   public void newDataHarvested() {
   }

   public void evaluateHarvesterRules() {
      this.wnRuntime.incrementTotalHarvesterEvaluationCycles();
      WatchConfiguration var1 = this.getWatchConfiguration();
      if (var1 != null && var1.isWatchNotificationEnabled()) {
         ArrayList var2 = var1.getEnabledHarvesterWatches();
         if (var2.isEmpty()) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("No harvester rules configured");
            }

            this.updateWatchNotificationRuntime();
         } else {
            boolean var3 = false;
            long var4 = System.nanoTime();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Evaluating harvester watch rules ");
               WatchedValues var6 = this.watchConfig.getWatchedValues();
               debugLogger.debug(var6.dump("", true, false, true));
            }

            int var14 = 0;

            for(int var7 = 0; var7 < var2.size(); ++var7) {
               Watch var8 = (Watch)var2.get(var7);
               if (var8.isAlarm()) {
                  var8.resetCollectionData(true);
               } else {
                  ++var14;
                  boolean var10 = this.getWatchConfiguration().getWatchedValues().getMostRecentValuesCount() > 0L;
                  boolean var9;
                  if (!var10) {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Values are unchanged for watch " + var8.getWatchName() + ". " + "Using previous evaluation result: " + var8.getLastPerformedEvaluationResult());
                     }

                     var9 = var8.getLastPerformedEvaluationResult();
                     if (var9) {
                        var8.evaluateHarvesterRuleWatch(true);
                     }
                  } else {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Evaluating watch " + var8);
                     }

                     if (var8.evaluateHarvesterRuleWatch(false)) {
                        var9 = true;
                        if (debugLogger.isDebugEnabled()) {
                           debugLogger.debug("Evaluated watch to true " + var8.getWatchName());
                        }
                     } else {
                        var9 = false;
                     }

                     var8.setLastPerformedEvaluationResult(var9);
                  }

                  var8.resetCollectionData();
                  if (var9) {
                     this.wnRuntime.incrementTotalHarvesterWatchesTriggered();
                     if (var8.hasAlarm()) {
                        synchronized(this) {
                           var3 = this.setAlarm(var8);
                        }
                     }
                  }
               }
            }

            if (var14 > 0) {
               this.wnRuntime.incrementTotalHarvesterWatchEvaluations(var14);
            }

            if (var3) {
               this.resetAlarmTimer();
            }

            long var15 = System.nanoTime() - var4;
            this.wnRuntime.incrementTotalHarvesterWatchEvaluationTime(var15 * 1000000L);
            this.updateWatchNotificationRuntime();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Finished evaluating harvester watch rules in " + var15 + " nanos");
            }

         }
      }
   }

   public void evaluateLogEventRules(LogEntry var1) {
      WatchConfiguration var2 = this.getWatchConfiguration();
      if (var2 != null && var2.isWatchNotificationEnabled()) {
         if (var1.getSeverity() != 128) {
            this.wnRuntime.incrementTotalLogEvaluationCycles();
            ArrayList var3 = var2.getEnabledLogWatches();
            if (var3.isEmpty()) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("No log rules configured");
               }

            } else {
               synchronized(this.eventQueue) {
                  if (this.asyncLogThread != null && Thread.currentThread().equals(this.asyncLogThread)) {
                     return;
                  }

                  this.eventQueue.add(var1);
                  if (this.eventQueue.size() > 1) {
                     return;
                  }
               }

               this.workManager.schedule(this);
            }
         }
      }
   }

   public void handleInstrumentationEvent(DataRecord var1) {
      this.wnRuntime.incrementTotalEventDataEvaluationCycles();
      WatchConfiguration var2 = this.getWatchConfiguration();
      if (var2 != null && var2.isWatchNotificationEnabled()) {
         ArrayList var3 = var2.getEnabledEventDataWatches();
         if (var3.isEmpty()) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("No event data rules configured");
            }

         } else {
            synchronized(this.eventQueue) {
               this.eventQueue.add(var1);
               if (this.eventQueue.size() > 1) {
                  return;
               }
            }

            this.workManager.schedule(this);
         }
      }
   }

   public void run() {
      while(true) {
         Object var1;
         synchronized(this.eventQueue) {
            this.asyncLogThread = null;
            if (this.eventQueue.isEmpty()) {
               return;
            }

            var1 = this.eventQueue.remove();
            if (var1 instanceof LogEntry) {
               this.asyncLogThread = Thread.currentThread();
            }
         }

         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Executing event " + var1);
         }

         if (var1 instanceof LogEntry) {
            this.evaluateLogEventRulesAsync((LogEntry)var1);
         } else {
            if (!(var1 instanceof DataRecord)) {
               throw new AssertionError("Unknown event work" + var1);
            }

            this.evaluateEventDataRulesAsync((DataRecord)var1);
         }
      }
   }

   public Watch[] getActiveAlarmWatches() {
      Watch[] var1 = new Watch[this.alarmWatches.size()];
      this.alarmWatches.values().toArray(var1);
      return var1;
   }

   public void timerExpired(Timer var1) {
      try {
         long var2 = System.currentTimeMillis();
         synchronized(this.alarmWatches) {
            Iterator var5 = this.alarmWatches.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry var6 = (Map.Entry)var5.next();
               Watch var7 = (Watch)var6.getValue();
               if (var7.getAlarmType() == 2 && var7.getResetTime() <= var2) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Reset watch " + var7.getWatchName());
                  }

                  var5.remove();
                  var7.setAlarm(false);
               }
            }

            this.alarmResetTimer = null;
         }
      } catch (Exception var14) {
         DiagnosticsLogger.logUnexpectedException("" + var1, var14);
      } finally {
         this.resetAlarmTimer();
      }

   }

   public void resetWatchAlarm(String var1) throws WatchNotFoundException, WatchNotActiveAlarmException {
      WatchConfiguration var2 = this.getWatchConfiguration();
      if (var2 != null) {
         Watch var3 = var2.getWatch(var1);
         if (!var3.isAlarm()) {
            throw new WatchNotActiveAlarmException("Watch " + var1 + " is not an active alarm");
         } else {
            synchronized(this.alarmWatches) {
               this.alarmWatches.remove(var1);
            }

            var3.setAlarm(false);
         }
      }
   }

   private boolean setAlarm(Watch var1) {
      boolean var2 = false;
      if (var1.getAlarmType() == 0) {
         return false;
      } else {
         var1.setAlarm(true);
         if (var1.getAlarmType() == 2) {
            var1.setResetTime(System.currentTimeMillis() + (long)var1.getAlarmResetPeriod());
            var2 = true;
            this.wnRuntime.incrementTotalActiveAutomaticResetAlarms();
         } else {
            this.wnRuntime.incrementTotalActiveManualResetAlarms();
         }

         synchronized(this.alarmWatches) {
            this.alarmWatches.put(var1.getWatchName(), var1);
            return var2;
         }
      }
   }

   private void resetAlarmTimer() {
      if (this.timerManager == null) {
         this.timerManagerFactory = TimerManagerFactory.getTimerManagerFactory();
         this.timerManager = this.timerManagerFactory.getDefaultTimerManager();
      }

      synchronized(this.alarmWatches) {
         long var2 = 0L;
         Iterator var4 = this.alarmWatches.values().iterator();

         while(true) {
            long var6;
            do {
               Watch var5;
               do {
                  if (!var4.hasNext()) {
                     if (var2 > 0L) {
                        if (this.alarmResetTimer != null) {
                           boolean var10 = this.alarmResetTimer.cancel();
                           if (debugLogger.isDebugEnabled()) {
                              debugLogger.debug("Canceled timer with result " + var10);
                           }
                        }

                        long var11 = var2 - System.currentTimeMillis();
                        if (var11 < 0L) {
                           var11 = 0L;
                        }

                        this.alarmResetTimer = this.timerManager.schedule(this, var11);
                        if (debugLogger.isDebugEnabled()) {
                           debugLogger.debug("Scheduled timer for delay of " + var11);
                        }
                     } else {
                        this.alarmResetTimer = null;
                        if (debugLogger.isDebugEnabled()) {
                           debugLogger.debug("No timer scheduled");
                        }
                     }

                     return;
                  }

                  var5 = (Watch)var4.next();
               } while(var5.getAlarmType() != 2);

               var6 = var5.getResetTime();
            } while(var6 >= var2 && var2 != 0L);

            var2 = var6;
         }
      }
   }

   private void initializeEventListener() {
      InstrumentationManager var1 = InstrumentationManager.getInstrumentationManager();
      var1.removeInstrumentationEventListener(this);
      var1.addInstrumentationEventListener(this);
   }

   private void initializeLogEventHandler() {
      WatchLogService.deregisterFromServerLogger();
      WatchLogService.registerToServerLogger(this, this.watchConfig.getEventHandlerSeverity());
   }

   private void deinitializeEventListener() {
      InstrumentationManager var1 = InstrumentationManager.getInstrumentationManager();
      var1.removeInstrumentationEventListener(this);
   }

   private void deinitializeLogEventHandler() {
      WatchLogService.deregisterFromServerLogger();
   }

   private void evaluateLogEventRulesAsync(LogEntry var1) {
      boolean var2 = false;
      long var3 = System.currentTimeMillis();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Evaluating log watch rules in separate thread ");
      }

      WatchConfiguration var5 = this.getWatchConfiguration();
      if (var5 != null) {
         ArrayList var6 = var5.getEnabledLogWatches();
         int var7 = 0;
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Evaluating " + var6.size() + " watches");
         }

         for(int var8 = 0; var8 < var6.size(); ++var8) {
            Watch var9 = (Watch)var6.get(var8);
            if (!var9.isAlarm()) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Evaluating watch " + var9);
               }

               ++var7;
               if (var9.evaluateLogRuleWatch(var1)) {
                  this.wnRuntime.incrementTotalLogWatchesTriggered();
                  if (var9.hasAlarm()) {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Handling alarm for watch " + var9.getWatchName());
                     }

                     synchronized(this) {
                        var2 = this.setAlarm(var9);
                     }
                  }
               }
            }
         }

         this.wnRuntime.incrementTotalLogWatchEvaluations(var7);
         if (var2) {
            this.resetAlarmTimer();
         }

         long var13 = System.currentTimeMillis() - var3;
         this.wnRuntime.incrementTotalLogWatchEvaluationTime(var13);
      }
   }

   private void evaluateEventDataRulesAsync(DataRecord var1) {
      long var2 = System.currentTimeMillis();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Evaluating event data watch rules in separate thread ");
      }

      WatchConfiguration var4 = this.getWatchConfiguration();
      if (var4 != null) {
         ArrayList var5 = var4.getEnabledEventDataWatches();
         int var6 = 0;
         boolean var7 = false;

         for(int var8 = 0; var8 < var5.size(); ++var8) {
            Watch var9 = (Watch)var5.get(var8);
            if (!var9.isAlarm()) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Evaluating watch " + var9);
               }

               ++var6;
               if (var9.evaluateEventDataRuleWatch(var1)) {
                  this.wnRuntime.incrementTotalEventDataWatchesTriggered();
                  if (var9.hasAlarm()) {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Handling alarm for watch " + var9.getWatchName());
                     }

                     synchronized(this) {
                        var7 = this.setAlarm(var9);
                     }
                  }
               }
            }
         }

         this.wnRuntime.incrementTotalEventDataWatchEvaluations(var6);
         if (var7) {
            this.resetAlarmTimer();
         }

         long var13 = System.currentTimeMillis() - var2;
         this.wnRuntime.incrementTotalEventDataWatchEvaluationTime(var13);
      }
   }

   private void updateWatchNotificationRuntime() {
      int var1 = this.alarmWatches.size();
      this.wnRuntime.setCurrentActiveAlarmsCount(var1);
   }
}
