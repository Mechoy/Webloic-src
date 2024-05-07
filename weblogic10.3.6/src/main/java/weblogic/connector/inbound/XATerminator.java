package weblogic.connector.inbound;

import java.util.Map;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.connector.common.Debug;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.transaction.InterposedTransactionManager;
import weblogic.transaction.TxHelper;

public class XATerminator implements javax.resource.spi.XATerminator {
   private static XATerminator oneOfMe;
   private static XAResource myXAResource;
   static final long serialVersionUID = 3854183862937321209L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.inbound.XATerminator");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Inbound_Transaction_Rollback_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Inbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Inbound_Transaction_Commmit_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Inbound_Transaction_Start_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   private XATerminator() {
      InterposedTransactionManager var1 = TxHelper.getServerInterposedTransactionManager();
      myXAResource = var1.getXAResource();
   }

   public synchronized void commit(Xid var1, boolean var2) throws XAException {
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      Object[] var4;
      if (_WLDF$INST_FLD_Connector_Before_Inbound.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Before_Inbound.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(3);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Inbound;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Inbound_Transaction_Commmit_Medium.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Inbound_Transaction_Commmit_Medium.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(3);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Inbound_Transaction_Commmit_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var9;
      boolean var14 = var9 = _WLDF$INST_FLD_Connector_Around_Inbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      if (var14) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Around_Inbound.isArgumentsCaptureNeeded()) {
            var4 = InstrumentationSupport.toSensitive(3);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Inbound;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         Debug.xaIn("XATerminator.commit() called; calling myXAResource.commit()");
         myXAResource.commit(var1, var2);
         Debug.xaIn("XATerminator.commit() returned from myXAResource.commit() & exiting");
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Inbound, var10, var11);
         }

         if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Inbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   public synchronized void forget(Xid var1) throws XAException {
      Debug.xaIn("XATerminator.forget() called; calling myXAResource.commit()");
      myXAResource.forget(var1);
      Debug.xaIn("XATerminator.forget() returned from myXAResource.forget() & exiting");
   }

   public synchronized Xid[] recover(int var1) throws XAException {
      return myXAResource.recover(var1);
   }

   public synchronized void rollback(Xid var1) throws XAException {
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      Object[] var3;
      if (_WLDF$INST_FLD_Connector_Before_Inbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Inbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Inbound;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Inbound_Transaction_Rollback_Low.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Inbound_Transaction_Rollback_Low.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Inbound_Transaction_Rollback_Low;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var8;
      boolean var13 = var8 = _WLDF$INST_FLD_Connector_Around_Inbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var13) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Inbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Inbound;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      try {
         myXAResource.rollback(var1);
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Inbound, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Inbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   public synchronized int prepare(Xid var1) throws XAException {
      Object[] var3;
      DynamicJoinPoint var10000;
      DelegatingMonitor var10001;
      if (_WLDF$INST_FLD_Connector_Inbound_Transaction_Start_Medium.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Inbound_Transaction_Start_Medium.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Inbound_Transaction_Start_Medium;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Inbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Inbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Inbound;
         InstrumentationSupport.process(var10000, var10001, var10001.getActions());
      }

      boolean var8;
      boolean var14 = var8 = _WLDF$INST_FLD_Connector_Around_Inbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var9 = null;
      DiagnosticActionState[] var10 = null;
      Object var7 = null;
      if (var14) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Around_Inbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var10000 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Inbound;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var10000, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      boolean var12 = false;

      Object var4;
      int var15;
      try {
         var12 = true;
         var15 = myXAResource.prepare(var1);
         var12 = false;
      } finally {
         if (var12) {
            var4 = InstrumentationSupport.convertToObject(0);
            if (var8) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, (Object[])null, var4), _WLDF$INST_FLD_Connector_Around_Inbound, var9, var10);
            }

            if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var17 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, (Object[])null, var4);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Connector_After_Inbound;
               InstrumentationSupport.process(var17, var10003, var10003.getActions());
            }

         }
      }

      var4 = InstrumentationSupport.convertToObject(var15);
      if (var8) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, (Object[])null, var4), _WLDF$INST_FLD_Connector_Around_Inbound, var9, var10);
      }

      if (_WLDF$INST_FLD_Connector_After_Inbound.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, (Object[])null, var4);
         DelegatingMonitor var18 = _WLDF$INST_FLD_Connector_After_Inbound;
         InstrumentationSupport.process(var16, var18, var18.getActions());
      }

      return var15;
   }

   public static synchronized XATerminator getXATerminator() {
      if (oneOfMe == null) {
         oneOfMe = new XATerminator();
      }

      return oneOfMe;
   }

   static {
      _WLDF$INST_FLD_Connector_After_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Inbound");
      _WLDF$INST_FLD_Connector_Before_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Inbound");
      _WLDF$INST_FLD_Connector_Inbound_Transaction_Rollback_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Inbound_Transaction_Rollback_Low");
      _WLDF$INST_FLD_Connector_Around_Inbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Inbound");
      _WLDF$INST_FLD_Connector_Inbound_Transaction_Commmit_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Inbound_Transaction_Commmit_Medium");
      _WLDF$INST_FLD_Connector_Inbound_Transaction_Start_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Inbound_Transaction_Start_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "XATerminator.java", "weblogic.connector.inbound.XATerminator", "commit", "(Ljavax/transaction/xa/Xid;Z)V", 48, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "XATerminator.java", "weblogic.connector.inbound.XATerminator", "rollback", "(Ljavax/transaction/xa/Xid;)V", 89, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "XATerminator.java", "weblogic.connector.inbound.XATerminator", "prepare", "(Ljavax/transaction/xa/Xid;)I", 102, (Map)null, (boolean)0);
   }
}
