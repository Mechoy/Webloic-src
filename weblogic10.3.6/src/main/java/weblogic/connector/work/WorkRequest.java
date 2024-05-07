package weblogic.connector.work;

import java.util.HashMap;
import java.util.Map;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkCompletedException;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkRejectedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.security.layer.WorkImpl;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.transaction.InterposedTransactionManager;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.StackTraceUtils;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.spi.WorkContextMapInterceptor;

class WorkRequest implements Runnable {
   private Work work;
   private int status;
   private long startTimeout;
   private long creationTime;
   private ExecutionContext ec;
   private WorkListener listener;
   private boolean notifyOnWorkStart;
   private boolean notifyOnWorkCompletion;
   private WorkException exception;
   private WorkContextMapInterceptor contexts;
   private RAInstanceManager raIM;
   private static HashMap txIds;
   private boolean txOK;
   private Transaction tx;
   private TransactionManager tm;
   private Work originalWork;
   static final long serialVersionUID = 8675661177125583574L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.work.WorkRequest");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Work;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Work;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Work;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   WorkRequest(Work var1, long var2, ExecutionContext var4, WorkListener var5, RAInstanceManager var6) throws WorkException {
      this.work = var1;
      this.startTimeout = var2;
      this.ec = var4;
      this.listener = var5;
      this.raIM = var6;
      this.tx = null;
      this.originalWork = (Work)((Work)((WorkImpl)var1).getSourceObj());
      this.creationTime = System.currentTimeMillis();
      this.contexts = WorkContextHelper.getWorkContextHelper().getInterceptor().copyThreadContexts(2);
      this.txOK = false;
   }

   public void run() {
      long var1 = System.currentTimeMillis();
      if (this.startTimeout != Long.MAX_VALUE && var1 > this.creationTime + this.startTimeout) {
         this.sendWorkRejectedEvent();
      } else {
         WorkContextHelper.getWorkContextHelper().getInterceptor().restoreThreadContexts(this.contexts);
         this.sendWorkStartedEvent();

         try {
            this.setExecutionContext();
            this.work.run();
            this.sendWorkCompletedEvent((Throwable)null);
         } catch (Throwable var4) {
            this.sendWorkCompletedEvent(var4);
         }
      }

   }

   private void setExecutionContext() throws WorkCompletedException {
      try {
         if (Debug.isXAworkEnabled()) {
            Debug.xaWork("WorkRequest.setExecutionContext(), ExecutionContext = " + this.ec);
         }

         this.txOK = this.checkImportedTx();
         if (this.ec != null) {
            Xid var1 = this.ec.getXid();
            if (Debug.isXAworkEnabled()) {
               Debug.xaWork("WorkRequest.setExecutionContext(),  xid = " + TxHelper.xidToString(var1, true));
            }

            if (var1 != null) {
               long var8 = this.ec.getTransactionTimeout() / 1000L;
               if (var8 > 2147483647L) {
                  throw new XAException(-5);
               }

               this.tm = TxHelper.getTransactionManager();
               this.tx = this.tm.getTransaction(var1);
               weblogic.transaction.Transaction var4 = TxHelper.getTransaction();
               if (Debug.isXAworkEnabled()) {
                  Debug.xaWork("WorkRequest.setExecutionContext() tx(" + TxHelper.xidToString(var1, true) + ") = " + this.tx);
                  Debug.xaWork("WorkRequest.setExecutionContext() currentTx = " + var4);
               }

               if (this.tx != null && ((weblogic.transaction.Transaction)this.tx).isCoordinatorLocal()) {
                  if (Debug.isXAworkEnabled()) {
                     Debug.xaWork("Setting tx on Work Thread (txid = " + TxHelper.xidToString(var1, true));
                  }

                  this.tm.resume(this.tx);
                  if (Debug.isXAworkEnabled()) {
                     Debug.xaWork("Succeeded in associating tx with thread. tx = " + TxHelper.xidToString(var1, true));
                  }
               } else {
                  if (this.tx != null && var4 != null && !var4.equals(this.tx)) {
                     if (Debug.isXAworkEnabled()) {
                        Debug.xaWork("Can't associate tx with thread for txid = " + TxHelper.xidToString(var1, true) + ".  There is a different tx already associated with the thread. (tx = " + TxHelper.xidToString(var4.getXID(), true) + ")");
                     }

                     throw new WorkCompletedException("Attempt to associate tx with Thread when a different tx is already associated with the thread");
                  }

                  if (Debug.isXAworkEnabled()) {
                     Debug.xaWork("Starting tx using InterposedTransactionManager");
                  }

                  XAResource var5 = this.getXAResource();
                  int var6 = (new Long(var8)).intValue();
                  var5.setTransactionTimeout(var6);
                  var5.start(var1, 0);
               }
            }
         }

      } catch (Throwable var7) {
         String var2 = Debug.getExceptionSetExecutionContextFailed(var7.toString());
         throw new WorkCompletedException(var2, var7);
      }
   }

   private void sendWorkCompletedEvent(Throwable var1) {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      Object[] var5;
      DelegatingMonitor var10001;
      DynamicJoinPoint var19;
      if (var10000) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var5 = InstrumentationSupport.toSensitive(2);
         }

         var19 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var19, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var5 = InstrumentationSupport.toSensitive(2);
         }

         var19 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var19, var10001, var10001.getActions());
      }

      try {
         if (Debug.isWorkEventsEnabled()) {
            Debug.workEvent("WorkRequest.sendWorkCompletedEvent(); listener = " + this.listener);
         }

         if (this.txOK) {
            try {
               this.releaseImportedTx(this.work, this.ec);
            } catch (XAException var17) {
               if (var1 == null) {
                  var1 = var17;
               } else {
                  var17.initCause((Throwable)var1);
                  var1 = var17;
               }
            }
         }

         if (var1 != null) {
            if (Debug.isWorkEventsEnabled()) {
               Debug.workEvent("WorkRequest.sendWorkCompletedEvent() creating WorkCompletedException\n" + StackTraceUtils.throwable2StackTrace((Throwable)var1));
            }

            if (var1 instanceof WorkCompletedException) {
               this.exception = (WorkCompletedException)var1;
            } else {
               this.exception = new WorkCompletedException();
               this.exception.setErrorCode("0");
               this.exception.initCause((Throwable)var1);
            }
         }

         if (this.listener != null) {
            WorkEvent var2 = new WorkEvent(this.originalWork, 4, this.originalWork, this.exception);
            this.listener.workCompleted(var2);
         }

         this.status = 4;
         synchronized(this) {
            if (this.notifyOnWorkCompletion) {
               this.notifyAll();
            }
         }
      } finally {
         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Work, var11, var12);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   private void sendWorkStartedEvent() {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      Object[] var4;
      if (var10000) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(1);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(1);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         if (Debug.isWorkEventsEnabled()) {
            Debug.workEvent("WorkRequest.sendWorkStartedEvent(); listener = " + this.listener);
         }

         if (this.listener != null) {
            WorkEvent var1 = new WorkEvent(this.originalWork, 3, this.originalWork, (WorkException)null);
            this.listener.workStarted(var1);
         }

         this.status = 3;
         synchronized(this) {
            if (this.notifyOnWorkStart) {
               this.notifyAll();
            }
         }
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Work, var10, var11);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   private void sendWorkRejectedEvent() {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Connector_Around_Work.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      Object[] var4;
      if (var10000) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Around_Work.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(1);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Work;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Work.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Before_Work.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(1);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Work;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         if (Debug.isWorkEventsEnabled()) {
            Debug.workEvent("WorkRequest.sendWorkRejectedEvent(); listener = " + this.listener);
         }

         if (this.listener != null) {
            this.exception = new WorkRejectedException();
            this.exception.setErrorCode("1");
            WorkEvent var1 = new WorkEvent(this.originalWork, 2, this.originalWork, this.exception);
            this.listener.workRejected(var1);
         }

         this.status = 2;
         synchronized(this) {
            this.notifyAll();
         }
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_Connector_Around_Work, var10, var11);
         }

         if (_WLDF$INST_FLD_Connector_After_Work.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Work;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_2, var10001, var10001.getActions());
         }

      }

   }

   void blockTillCompletion() {
      if (this.status != 4) {
         synchronized(this) {
            if (this.status != 4) {
               this.notifyOnWorkCompletion = true;

               try {
                  this.wait();
               } catch (InterruptedException var4) {
               }

            }
         }
      }
   }

   void blockTillStart() {
      if (this.status != 3 && this.status != 4) {
         synchronized(this) {
            if (this.status != 3 && this.status != 4) {
               this.notifyOnWorkStart = true;

               try {
                  this.wait();
               } catch (InterruptedException var4) {
               }

            }
         }
      }
   }

   WorkException getException() {
      return this.exception;
   }

   private synchronized boolean checkImportedTx() throws WorkException {
      if (Debug.isXAworkEnabled()) {
         Debug.xaWork("WorkRequest.checkImportedTx( " + this.work + ", " + this.ec + ", " + this.listener + " )");
      }

      String var1 = null;
      if (this.ec != null) {
         Xid var2 = this.ec.getXid();
         if (var2 == null) {
            if (Debug.isXAworkEnabled()) {
               Debug.xaWork("WorkRequest.checkImportedTx() found null Xid in ExcecutionContext; continuing with non-tx Work processing");
            }

            return false;
         } else {
            Gid var3 = new Gid(var2.getGlobalTransactionId());
            if (Debug.isXAworkEnabled()) {
               var1 = "WorkRequest.checkImportedTx( " + this.work + ", " + this.ec + " ) with gid=" + var3;
               Debug.xaWork(var1);
            }

            if (txIds.containsKey(var3)) {
               if (Debug.isXAworkEnabled()) {
                  Debug.xaWork(var1 + " REJECTED.");
               }

               String var4 = Debug.getExceptionImportedTxAlreadyActive(var3 != null ? var3.toString() : null);
               throw new WorkCompletedException(var4, "2");
            } else {
               if (Debug.isXAworkEnabled()) {
                  Debug.xaWork(var1 + " ACCEPTED.");
               }

               txIds.put(var3, this.work);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private synchronized void releaseImportedTx(Work var1, ExecutionContext var2) throws XAException {
      String var3 = var2 != null ? var2.toString() : null;
      if (Debug.isXAworkEnabled()) {
         Debug.xaWork("WorkRequest.releaseImportedTx( " + var1 + ", " + var3 + " )");
      }

      if (var2 != null) {
         Xid var4 = var2.getXid();
         if (var4 == null) {
            if (Debug.isXAworkEnabled()) {
               Debug.xaWork("WorkRequest.releaseImportedTx() found null Xid; no imported tx to dissassociate from Work");
            }

            return;
         }

         if (this.tx != null) {
            try {
               Transaction var12 = TxHelper.getTransactionManager().suspend();
               if (var12 == null || !var12.equals(this.tx)) {
                  Debug.xaWork("WARNING: WorkRequest.releaseImportedTx() tm.suspend() release tx = " + var12 + " instead of tx = " + this.tx + " as expected");
               }
            } catch (SystemException var10) {
               Debug.xaWork("WARNING: WorkRequest.releaseImportedTx() tm.suspend() threw Exception " + var10);
            } finally {
               this.tx = null;
            }
         } else {
            XAResource var5 = this.getXAResource();
            var5.end(var4, 67108864);
         }

         Gid var13 = new Gid(var4.getGlobalTransactionId());
         if (Debug.isXAworkEnabled()) {
            Debug.xaWork("WorkRequest.releaseImportedTx( " + var1 + ", " + var3 + " ) releasing global tx " + var13);
         }

         Work var6 = (Work)txIds.get(var13);
         String var7;
         if (var6 != var1) {
            if (Debug.isXAworkEnabled()) {
               Debug.xaWork("WorkRequest.releaseImportedTx( " + var1 + ", " + var3 + " ) failed to release global tx " + var13 + " ; Work instance does match Work that started tx: " + var6);
            }

            var7 = Debug.getExceptionInvalidGid(var13.toString());
            throw new IllegalArgumentException(var7);
         }

         if (txIds.remove(var13) == null) {
            if (Debug.isXAworkEnabled()) {
               Debug.xaWork("WorkRequest.releaseImportedTx( " + var1 + ", " + var3 + " ) failed to release global tx " + var13 + " ; WorkManager is not holding Work instance for gid");
            }

            var7 = Debug.getExceptionGidNotRegistered(var13.toString());
            throw new IllegalArgumentException(var7);
         }
      }

   }

   private XAResource getXAResource() {
      TransactionHelper var1 = TransactionHelper.getTransactionHelper();
      InterposedTransactionManager var2 = (InterposedTransactionManager)var1.getTransactionManager();
      return var2.getXAResource();
   }

   Work getWork() {
      return this.work;
   }

   static {
      _WLDF$INST_FLD_Connector_After_Work = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Work");
      _WLDF$INST_FLD_Connector_Around_Work = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Work");
      _WLDF$INST_FLD_Connector_Before_Work = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Work");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkRequest.java", "weblogic.connector.work.WorkRequest", "sendWorkCompletedEvent", "(Ljava/lang/Throwable;)V", 209, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkRequest.java", "weblogic.connector.work.WorkRequest", "sendWorkStartedEvent", "()V", 261, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "WorkRequest.java", "weblogic.connector.work.WorkRequest", "sendWorkRejectedEvent", "()V", 282, (Map)null, (boolean)0);
      txIds = new HashMap(10);
   }
}
