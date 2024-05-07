package weblogic.connector.work;

import java.io.Serializable;
import java.security.AccessController;
import java.util.Map;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkRejectedException;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RACollectionManager;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.extensions.LongRunning;
import weblogic.connector.security.layer.ExecutionContextImpl;
import weblogic.connector.security.layer.WorkImpl;
import weblogic.connector.security.layer.WorkListenerImpl;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.kernel.KernelStatus;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public class WorkManager implements javax.resource.spi.work.WorkManager, Serializable {
   private static final long serialVersionUID = 3997838374571468077L;
   private String appId;
   private String name;
   private boolean acceptingDoWorkCalls;
   private boolean suspended;
   private transient weblogic.work.WorkManager workManager;
   private transient LongRunningWorkManager lrWorkManager;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.work.WorkManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Work;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Work;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Work;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;
   public static final JoinPoint _WLDF$INST_JPFLD_5;

   private WorkManager(String var1, String var2, weblogic.work.WorkManager var3) {
      this.appId = var1;
      this.name = var2;
      this.acceptingDoWorkCalls = true;
      this.suspended = false;
      this.workManager = var3;
      this.lrWorkManager = new LongRunningWorkManager(var2);
   }

   public static WorkManager create(String var0, String var1, weblogic.work.WorkManager var2) {
      return new WorkManager(var0, var1, var2);
   }

   /** @deprecated */
   public static WorkManager create(String var0, String var1, int var2) {
      return new WorkManager(var0, var1, WorkManagerFactory.getInstance().getDefault());
   }

   public void doWork(Runnable var1) throws WorkException {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      DelegatingMonitor var10001;
      Object[] var3;
      DynamicJoinPoint var13;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.doWork((Work)(new ProxyWork(var1)), Long.MAX_VALUE, (ExecutionContext)null, (WorkListener)null);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   public void doWork(Work var1) throws WorkException {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      DelegatingMonitor var10001;
      Object[] var3;
      DynamicJoinPoint var13;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.doWork((Work)var1, Long.MAX_VALUE, (ExecutionContext)null, (WorkListener)null);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   public void doWork(Runnable var1, long var2, ExecutionContext var4, WorkListener var5) throws WorkException {
      this.doWork((Work)(new ProxyWork(var1)), var2, var4, var5);
   }

   public void doWork(Work var1, long var2, ExecutionContext var4, WorkListener var5) throws WorkException {
      AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RAInstanceManager var7 = RACollectionManager.getRAInstanceManagerByAppIdAndModuleName(this.appId, this.name);
      Work var8 = var1;
      String var9;
      if (var1 != null) {
         WorkImpl var11 = new WorkImpl(var1, var7, var6);
         if (var4 != null) {
            var4 = new ExecutionContextImpl((ExecutionContext)var4, var7, var6);
         }

         if (var5 != null) {
            var5 = new WorkListenerImpl((WorkListener)var5, var7, var6);
         }

         if (Debug.isWorkEnabled()) {
            Debug.work("WorkManager.doWork( " + var11 + ", " + var2 + ", " + var4 + ", " + var5 + " )");
         }

         if (!this.acceptingDoWorkCalls && KernelStatus.isServer()) {
            var9 = Debug.getExceptionDoWorkNotAccepted();
            rejectWork(var11, (WorkListener)var5, new WorkRejectedException(var9), 2);
         }

         if (this.suspended) {
            var9 = Debug.getExceptionWorkManagerSuspended();
            rejectWork(var11, (WorkListener)var5, new WorkRejectedException(var9), 2);
         }

         this.sendWorkAcceptedEvent(var11, (WorkListener)var5);
         WorkRequest var12 = this.createRequestAndSchedule(var11, var2, (ExecutionContext)var4, (WorkListener)var5, var7, var8);
         var12.blockTillCompletion();
         WorkException var10 = var12.getException();
         this.reportException(".doWork()", var10);
      } else {
         var9 = Debug.getExceptionWorkIsNull();
         throw new IllegalArgumentException(var9);
      }
   }

   public void scheduleWork(Runnable var1) throws WorkException {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      DelegatingMonitor var10001;
      Object[] var3;
      DynamicJoinPoint var13;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.scheduleWork((Work)(new ProxyWork(var1)), Long.MAX_VALUE, (ExecutionContext)null, (WorkListener)null);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_2, var10001, var10001.getActions());
         }

      }

   }

   public void scheduleWork(Work var1) throws WorkException {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      DelegatingMonitor var10001;
      Object[] var3;
      DynamicJoinPoint var13;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.scheduleWork((Work)var1, Long.MAX_VALUE, (ExecutionContext)null, (WorkListener)null);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_3, var10001, var10001.getActions());
         }

      }

   }

   public void scheduleWork(Runnable var1, long var2, ExecutionContext var4, WorkListener var5) throws WorkException {
      this.scheduleWork((Work)(new ProxyWork(var1)), var2, var4, var5);
   }

   public void scheduleWork(Work var1, long var2, ExecutionContext var4, WorkListener var5) throws WorkException {
      AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RAInstanceManager var7 = RACollectionManager.getRAInstanceManagerByAppIdAndModuleName(this.appId, this.name);
      Work var8 = var1;
      String var9;
      if (var1 != null) {
         WorkImpl var10 = new WorkImpl(var1, var7, var6);
         if (var4 != null) {
            var4 = new ExecutionContextImpl((ExecutionContext)var4, var7, var6);
         }

         if (var5 != null) {
            var5 = new WorkListenerImpl((WorkListener)var5, var7, var6);
         }

         if (Debug.isWorkEnabled()) {
            Debug.work("WorkManager.scheduleWork( " + var10 + ", " + var2 + ", " + var4 + ", " + var5 + " )");
         }

         if (this.suspended) {
            var9 = Debug.getExceptionWorkManagerSuspended();
            rejectWork(var10, (WorkListener)var5, new WorkRejectedException(var9), 2);
         }

         this.sendWorkAcceptedEvent(var10, (WorkListener)var5);
         this.createRequestAndSchedule(var10, var2, (ExecutionContext)var4, (WorkListener)var5, var7, var8);
      } else {
         var9 = Debug.getExceptionWorkIsNull();
         throw new IllegalArgumentException(var9);
      }
   }

   public long startWork(Runnable var1) throws WorkException {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      Object[] var3;
      DynamicJoinPoint var14;
      DelegatingMonitor var10001;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var14, var10001, var10001.getActions());
      }

      boolean var12 = false;

      Object var4;
      long var15;
      try {
         var12 = true;
         var15 = this.startWork((Work)(new ProxyWork(var1)), Long.MAX_VALUE, (ExecutionContext)null, (WorkListener)null);
         var12 = false;
      } finally {
         if (var12) {
            var4 = InstrumentationSupport.convertToObject(0L);
            if (var8) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, (Object[])null, var4), _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
            }

            if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, (Object[])null, var4);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Connector_After_Work;
               InstrumentationSupport.process(var17, var10003, var10003.getActions());
            }

         }
      }

      var4 = InstrumentationSupport.convertToObject(var15);
      if (var8) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, (Object[])null, var4), _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
      }

      if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, (Object[])null, var4);
         DelegatingMonitor var18 = _WLDF$INST_FLD_Connector_After_Work;
         InstrumentationSupport.process(var16, var18, var18.getActions());
      }

      return var15;
   }

   public long startWork(Work var1) throws WorkException {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      Object[] var3;
      DynamicJoinPoint var14;
      DelegatingMonitor var10001;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var14, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var14 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var14, var10001, var10001.getActions());
      }

      boolean var12 = false;

      Object var4;
      long var15;
      try {
         var12 = true;
         var15 = this.startWork((Work)var1, Long.MAX_VALUE, (ExecutionContext)null, (WorkListener)null);
         var12 = false;
      } finally {
         if (var12) {
            var4 = InstrumentationSupport.convertToObject(0L);
            if (var8) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, (Object[])null, var4), _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
            }

            if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, (Object[])null, var4);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Connector_After_Work;
               InstrumentationSupport.process(var17, var10003, var10003.getActions());
            }

         }
      }

      var4 = InstrumentationSupport.convertToObject(var15);
      if (var8) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, (Object[])null, var4), _WLDF$INST_FLD_Connector_Around_Work, var9, var10);
      }

      if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_5, (Object[])null, var4);
         DelegatingMonitor var18 = _WLDF$INST_FLD_Connector_After_Work;
         InstrumentationSupport.process(var16, var18, var18.getActions());
      }

      return var15;
   }

   public long startWork(Runnable var1, long var2, ExecutionContext var4, WorkListener var5) throws WorkException {
      return this.startWork((Work)(new ProxyWork(var1)), var2, var4, var5);
   }

   public long startWork(Work var1, long var2, ExecutionContext var4, WorkListener var5) throws WorkException {
      long var6 = System.currentTimeMillis();
      AuthenticatedSubject var8 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      RAInstanceManager var9 = RACollectionManager.getRAInstanceManagerByAppIdAndModuleName(this.appId, this.name);
      Work var10 = var1;
      String var11;
      if (var1 != null) {
         WorkImpl var14 = new WorkImpl(var1, var9, var8);
         if (var4 != null) {
            var4 = new ExecutionContextImpl((ExecutionContext)var4, var9, var8);
         }

         if (var5 != null) {
            var5 = new WorkListenerImpl((WorkListener)var5, var9, var8);
         }

         if (Debug.isWorkEnabled()) {
            Debug.work("WorkManager.startWork( " + var14 + ", " + var2 + ", " + var4 + ", " + var5 + " )");
         }

         if (this.suspended) {
            var11 = Debug.getExceptionWorkManagerSuspended();
            rejectWork(var14, (WorkListener)var5, new WorkRejectedException(var11), 2);
         }

         this.sendWorkAcceptedEvent(var14, (WorkListener)var5);
         WorkRequest var13 = this.createRequestAndSchedule(var14, var2, (ExecutionContext)var4, (WorkListener)var5, var9, var10);
         var13.blockTillStart();
         WorkException var12 = var13.getException();
         this.reportException("WorkManager.startWork()", var12);
         return System.currentTimeMillis() - var6;
      } else {
         var11 = Debug.getExceptionWorkIsNull();
         throw new IllegalArgumentException(var11);
      }
   }

   private void sendWorkAcceptedEvent(Work var1, WorkListener var2) {
      if (Debug.isWorkEventsEnabled()) {
         Debug.workEvent("WorkManager.sendWorkAcceptedEvent( " + var1 + ", " + var2 + " )");
      }

      if (var2 != null) {
         WorkEvent var3 = new WorkEvent(((WorkImpl)var1).getSourceObj(), 1, (Work)((Work)((WorkImpl)var1).getSourceObj()), (WorkException)null);
         var2.workAccepted(var3);
      }

   }

   private void reportException(String var1, WorkException var2) throws WorkException {
      if (var2 != null) {
         if (Debug.isWorkEnabled()) {
            Debug.work(var1 + " threw exception: " + StackTraceUtils.throwable2StackTrace(var2));
         }

         throw var2;
      }
   }

   public void acceptDoWorkCalls() {
      this.acceptingDoWorkCalls = true;
   }

   public void suspend() {
      this.suspended = true;
      if (Debug.isWorkEnabled()) {
         Debug.work("WorkManager.suspend()");
      }

   }

   public void resume() {
      this.suspended = false;
      if (Debug.isWorkEnabled()) {
         Debug.work("WorkManager.resume()");
      }

   }

   static void rejectWork(Work var0, WorkListener var1, WorkException var2, int var3) throws WorkException {
      if (Debug.isWorkEventsEnabled()) {
         Debug.workEvent("WorkManager.rejectWork( " + var1 + ", " + var2 + ", " + var3 + " )");
      }

      if (var1 != null) {
         WorkEvent var4 = new WorkEvent(((WorkImpl)var0).getSourceObj(), var3, (Work)((Work)((WorkImpl)var0).getSourceObj()), var2);
         var1.workRejected(var4);
      }

      throw var2;
   }

   private WorkRequest createRequestAndSchedule(Work var1, long var2, ExecutionContext var4, WorkListener var5, RAInstanceManager var6, Work var7) throws WorkException {
      Object var8;
      if (isLongRunningWork(var7)) {
         var8 = new LongRunningWorkRequest(var1, var2, var4, var5, var6, this.lrWorkManager);
         this.lrWorkManager.schedule((LongRunningWorkRequest)var8);
      } else {
         var8 = new WorkRequest(var1, var2, var4, var5, var6);
         this.workManager.schedule((Runnable)var8);
      }

      return (WorkRequest)var8;
   }

   public static boolean isLongRunningWork(Work var0) {
      boolean var1 = var0.getClass().isAnnotationPresent(LongRunning.class);
      if (var1 && Debug.isWorkEnabled()) {
         Debug.work("WorkManager.isLongRunningWork(): true : work:" + var0);
      }

      return var1;
   }

   public LongRunningWorkManager getLongRunningWorkManager() {
      return this.lrWorkManager;
   }

   static {
      _WLDF$INST_FLD_Connector_After_Work = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Work");
      _WLDF$INST_FLD_Connector_Around_Work = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Work");
      _WLDF$INST_FLD_Connector_Before_Work = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Work");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkManager.java", "weblogic.connector.work.WorkManager", "doWork", "(Ljava/lang/Runnable;)V", 123, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkManager.java", "weblogic.connector.work.WorkManager", "doWork", "(Ljavax/resource/spi/work/Work;)V", 136, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkManager.java", "weblogic.connector.work.WorkManager", "scheduleWork", "(Ljava/lang/Runnable;)V", 251, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkManager.java", "weblogic.connector.work.WorkManager", "scheduleWork", "(Ljavax/resource/spi/work/Work;)V", 263, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkManager.java", "weblogic.connector.work.WorkManager", "startWork", "(Ljava/lang/Runnable;)J", 365, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_5 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkManager.java", "weblogic.connector.work.WorkManager", "startWork", "(Ljavax/resource/spi/work/Work;)J", 379, (Map)null, (boolean)0);
   }
}
