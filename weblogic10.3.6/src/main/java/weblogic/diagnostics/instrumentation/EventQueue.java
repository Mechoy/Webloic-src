package weblogic.diagnostics.instrumentation;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import weblogic.diagnostics.accessor.DiagnosticAccessRuntime;
import weblogic.diagnostics.accessor.DiagnosticDataAccessRuntime;
import weblogic.diagnostics.archive.DataWriter;
import weblogic.diagnostics.context.DiagnosticContext;
import weblogic.diagnostics.context.DiagnosticContextFactory;
import weblogic.diagnostics.context.DiagnosticContextHelper;
import weblogic.diagnostics.context.InvalidDyeException;
import weblogic.diagnostics.type.UnexpectedExceptionHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public final class EventQueue implements TimerListener, InstrumentationConstants, EventDispatcher {
   private static EventQueue singleton;
   private static final String WORK_MANAGER_NAME = "InstrumentationEvents";
   private static final String INST_TIMER_MANAGER = "InstrumentationTimer";
   private static final int MAX_THREADS = 1;
   private static final boolean noEventArchive = Boolean.getBoolean("weblogic.diagnostics.internal.noEventArchive");
   private static final boolean noQueueEvent = Boolean.getBoolean("weblogic.diagnostics.internal.noQueueEvent");
   private static final boolean noEventPropagation = Boolean.getBoolean("weblogic.diagnostics.internal.noEventPropagation");
   private int eventThreadPool = -1;
   private Boolean mutext = new Boolean(true);
   private List savedEventsList = new ArrayList();
   private TimerManagerFactory timerManagerFactory;
   private TimerManager timerManager;
   private Timer eventsTimer;
   private long timerInterval;
   private long timerPeriod = 5000L;
   private boolean isInProgress;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private EventQueue() {
      TimerManagerFactory var1 = TimerManagerFactory.getTimerManagerFactory();
      this.timerManager = var1.getTimerManager("InstrumentationTimer");
   }

   public static synchronized EventQueue getInstance() {
      if (singleton == null) {
         singleton = new EventQueue();
      }

      return singleton;
   }

   public void dispatch(String var1, EventPayload var2) {
      InstrumentationEvent var3 = new InstrumentationEvent(var1, var2);
      this.enqueue(var3);
   }

   public void enqueue(InstrumentationEvent var1) {
      if (!noQueueEvent) {
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("EventQueue:enqueue " + var1);
         }

         try {
            if (DiagnosticContextHelper.isDyedWith((byte)31)) {
               return;
            }
         } catch (InvalidDyeException var6) {
         }

         boolean var2 = InstrumentationManager.getInstrumentationManager().isSynchronousEventPersistenceEnabled();
         if (var2) {
            this.handleSynchronously(var1);
         } else {
            synchronized(this.mutext) {
               this.savedEventsList.add(var1);
            }
         }

      }
   }

   public synchronized void setTimerInterval(long var1) {
      if (var1 < 1000L) {
         var1 = 1000L;
      }

      if (this.eventsTimer == null) {
         this.eventsTimer = this.timerManager.scheduleAtFixedRate(this, 0L, var1);
      } else {
         this.eventsTimer.cancel();
         this.eventsTimer = this.timerManager.scheduleAtFixedRate(this, 0L, var1);
      }

      this.timerInterval = var1;
   }

   public long getTimerInterval() {
      return this.timerInterval;
   }

   private void handleSynchronously(InstrumentationEvent var1) {
      DiagnosticContext var2 = DiagnosticContextFactory.findOrCreateDiagnosticContext(true);
      boolean var3 = false;

      try {
         var3 = var2.isDyedWith((byte)31);
         var2.setDye((byte)31, true);
      } catch (InvalidDyeException var7) {
      }

      ArrayList var4 = new ArrayList();
      var4.add(var1);
      SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new ArchiveEventsAction(var4));

      try {
         var2.setDye((byte)31, var3);
      } catch (InvalidDyeException var6) {
      }

   }

   public void timerExpired(Timer var1) {
      if (this.isInProgress) {
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Previous archival session is in progress");
         }

      } else {
         this.isInProgress = true;
         List var2 = this.savedEventsList;
         synchronized(this.mutext) {
            if (this.savedEventsList.size() == 0) {
               this.isInProgress = false;
               return;
            }

            this.savedEventsList = new ArrayList();
         }

         if (InstrumentationDebug.DEBUG_EVENTS.isDebugEnabled()) {
            int var3 = var2.size();

            for(int var4 = 0; var4 < var3; ++var4) {
               InstrumentationDebug.DEBUG_EVENTS.debug("EventsWork-EVENT " + var4 + ": " + var2.get(var4));
            }
         }

         DiagnosticContext var9 = DiagnosticContextFactory.findOrCreateDiagnosticContext(true);
         boolean var10 = false;

         try {
            var10 = var9.isDyedWith((byte)31);
            var9.setDye((byte)31, true);
         } catch (InvalidDyeException var7) {
         }

         SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new ArchiveEventsAction(var2));

         try {
            var9.setDye((byte)31, var10);
         } catch (InvalidDyeException var6) {
         }

         this.isInProgress = false;
      }
   }

   private static DataWriter getArchive() {
      DataWriter var0 = null;

      try {
         DiagnosticAccessRuntime var1 = DiagnosticAccessRuntime.getInstance();
         DiagnosticDataAccessRuntime var2 = (DiagnosticDataAccessRuntime)var1.lookupWLDFDataAccessRuntime("EventsDataArchive");
         var0 = (DataWriter)var2.getDiagnosticDataAccessService();
      } catch (Exception var3) {
         if (InstrumentationDebug.DEBUG_ACTIONS.isDebugEnabled()) {
            InstrumentationDebug.DEBUG_ACTIONS.debug("Could not find events archive", var3);
         }
      }

      return var0;
   }

   final class ArchiveEventsAction implements PrivilegedAction {
      private List eventsList;

      ArchiveEventsAction(List var2) {
         this.eventsList = var2;
      }

      public Object run() {
         try {
            int var1 = this.eventsList.size();

            for(int var2 = 0; var2 < var1; ++var2) {
               InstrumentationEvent var3 = (InstrumentationEvent)this.eventsList.get(var2);
               this.eventsList.set(var2, var3.getDataRecord());
            }

            if (!EventQueue.noEventPropagation) {
               InstrumentationManager.getInstrumentationManager().propagateInstrumentationEvents(this.eventsList);
            }

            if (!EventQueue.noEventArchive) {
               DataWriter var5 = EventQueue.getArchive();
               if (var5 != null) {
                  var5.writeData(this.eventsList);
               }
            }
         } catch (Exception var4) {
            UnexpectedExceptionHandler.handle("Error writing data to archive", var4);
         }

         return null;
      }
   }
}
