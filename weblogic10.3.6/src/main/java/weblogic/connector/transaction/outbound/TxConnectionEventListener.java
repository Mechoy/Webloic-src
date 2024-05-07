package weblogic.connector.transaction.outbound;

import java.util.Map;
import javax.resource.spi.ConnectionEvent;
import weblogic.connector.common.Debug;
import weblogic.connector.outbound.NoTxConnectionEventListener;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;

public class TxConnectionEventListener extends NoTxConnectionEventListener {
   static final long serialVersionUID = 4244099043013761244L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.transaction.outbound.TxConnectionEventListener");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Connection_Error_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;

   TxConnectionEventListener(TxConnectionHandler var1, String var2) {
      super(var1, var2);
   }

   public void localTransactionStarted(ConnectionEvent var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      Object[] var3;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      DiagnosticAction[] var10002;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var11;
      var10000 = var11 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      var7 = null;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         this.debugConnEvent("LOCAL_TRANSACTION_STARTED event received");
         ((TxConnectionHandler)this.connectionHandler).setLocalTransactionInProgress(true, "LOCAL_TRANSACTION_STARTED");
         Debug.localOut(this.connectionHandler.getPool(), "Local Transaction Started");
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Outbound, var12, var13);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Tx, var9, var10);
         }

      }

   }

   public void connectionErrorOccurred(ConnectionEvent var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      DelegatingMonitor var10001;
      Object[] var3;
      DynamicJoinPoint var13;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Connection_Error_Low.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Connection_Error_Low.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Connection_Error_Low;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         super.connectionErrorOccurred(var1);
         ((TxConnectionHandler)this.connectionHandler).setLocalTransactionInProgress(false);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Outbound, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   public void localTransactionRolledback(ConnectionEvent var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      Object[] var3;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      DiagnosticAction[] var10002;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var11;
      var10000 = var11 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      var7 = null;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         this.debugConnEvent("LOCAL_TRANSACTION_ROLLEDBACK event received");
         ((TxConnectionHandler)this.connectionHandler).setLocalTransactionInProgress(false, "LOCAL_TRANSACTION_ROLLEDBACK");
         ((TxConnectionHandler)this.connectionHandler).notifyConnPoolOfTransCompletion();
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_2, var10001, var10001.getActions());
         }

         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_Connector_Around_Outbound, var12, var13);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_2, var10001, var10001.getActions());
         }

         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_Connector_Around_Tx, var9, var10);
         }

      }

   }

   public void localTransactionCommitted(ConnectionEvent var1) {
      boolean var8;
      boolean var10000 = var8 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      Object[] var3;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      DiagnosticAction[] var10002;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var11;
      var10000 = var11 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      var7 = null;
      if (var10000) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         this.debugConnEvent("LOCAL_TRANSACTION_COMMITTED event received");
         ((TxConnectionHandler)this.connectionHandler).setLocalTransactionInProgress(false, "LOCAL_TRANSACTION_COMMITTED");
         ((TxConnectionHandler)this.connectionHandler).notifyConnPoolOfTransCompletion();
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_3, var10001, var10001.getActions());
         }

         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_Connector_Around_Outbound, var12, var13);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_3, var10001, var10001.getActions());
         }

         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_Connector_Around_Tx, var9, var10);
         }

      }

   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_FLD_Connector_Connection_Error_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Connection_Error_Low");
      _WLDF$INST_FLD_Connector_Around_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Tx");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Rollback_Low");
      _WLDF$INST_FLD_Connector_Before_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Tx");
      _WLDF$INST_FLD_Connector_After_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Tx");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Start_Medium");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Commmit_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "TxConnectionEventListener.java", "weblogic.connector.transaction.outbound.TxConnectionEventListener", "localTransactionStarted", "(Ljavax/resource/spi/ConnectionEvent;)V", 35, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "TxConnectionEventListener.java", "weblogic.connector.transaction.outbound.TxConnectionEventListener", "connectionErrorOccurred", "(Ljavax/resource/spi/ConnectionEvent;)V", 49, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "TxConnectionEventListener.java", "weblogic.connector.transaction.outbound.TxConnectionEventListener", "localTransactionRolledback", "(Ljavax/resource/spi/ConnectionEvent;)V", 62, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "TxConnectionEventListener.java", "weblogic.connector.transaction.outbound.TxConnectionEventListener", "localTransactionCommitted", "(Ljavax/resource/spi/ConnectionEvent;)V", 77, (Map)null, (boolean)0);
   }
}
