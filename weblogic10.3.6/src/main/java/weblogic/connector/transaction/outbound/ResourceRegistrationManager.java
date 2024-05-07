package weblogic.connector.transaction.outbound;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.connector.common.Debug;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;

public final class ResourceRegistrationManager {
   private TxConnectionHandler registeredHandler = null;
   private HashSet hashRegistrations = new HashSet();
   static final long serialVersionUID = -4902494837820608521L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.transaction.outbound.ResourceRegistrationManager");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Enlist_Resource_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Unregister_Resource_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Register_Resource_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;

   public synchronized void addResource(TxConnectionHandler var1) throws SystemException {
      if (this.registeredHandler == null) {
         this.registerResource(var1);
         this.registeredHandler = var1;
      }

      this.hashRegistrations.add(var1);
   }

   public synchronized void enlistResource(TxConnectionHandler var1, Transaction var2) throws RollbackException, IllegalStateException, SystemException {
      boolean var10;
      boolean var10000 = var10 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var11 = null;
      DiagnosticActionState[] var12 = null;
      Object var9 = null;
      Object[] var5;
      DynamicJoinPoint var15;
      DelegatingMonitor var10001;
      if (var10000) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1, var2};
         }

         var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         DiagnosticAction[] var10002 = var11 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var12 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1, var2};
         }

         var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var15, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Enlist_Resource_Low.isEnabledAndNotDyeFiltered()) {
         var5 = null;
         if (_WLDF$INST_FLD_Connector_Enlist_Resource_Low.isArgumentsCaptureNeeded()) {
            var5 = new Object[]{this, var1, var2};
         }

         var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var5, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Enlist_Resource_Low;
         InstrumentationSupport.process(var15, var10001, var10001.getActions());
      }

      try {
         if (this.registeredHandler == null) {
            this.registerResource(var1);
            this.registeredHandler = var1;
         }

         if (var1 instanceof XATxConnectionHandler) {
            var2.enlistResource(((XATxConnectionHandler)var1).getXAResource());
         } else {
            if (!(var1 instanceof LocalTxConnectionHandler)) {
               String var3 = Debug.getExceptionEnlistResourceIllegalType();
               throw new SystemException(var3);
            }

            ((weblogic.transaction.Transaction)var2).enlistResource(((LocalTxConnectionHandler)var1).getNonXAResource());
         }
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

         if (var10) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Tx, var11, var12);
         }

      }

   }

   public synchronized void removeResource(TxConnectionHandler var1) {
      this.hashRegistrations.remove(var1);
      if (this.registeredHandler == var1) {
         this.unregisterResource(var1);
         if (this.hashRegistrations.isEmpty()) {
            this.registeredHandler = null;
         } else {
            Iterator var2 = this.hashRegistrations.iterator();
            TxConnectionHandler var3 = (TxConnectionHandler)var2.next();

            try {
               this.registerResource(var3);
               this.registeredHandler = var3;
            } catch (SystemException var5) {
               if (Debug.isXAoutEnabled()) {
                  Debug.xaOut(var3.getPool(), "WARNING:  Failed to switch registration to other available resource:  " + var5);
               }

               this.registeredHandler = null;
            }
         }
      }

   }

   private void registerResource(TxConnectionHandler var1) throws SystemException {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      Object[] var4;
      DynamicJoinPoint var15;
      DelegatingMonitor var10001;
      if (var10000) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var15, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var15, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Register_Resource_Low.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Register_Resource_Low.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         var15 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Register_Resource_Low;
         InstrumentationSupport.process(var15, var10001, var10001.getActions());
      }

      try {
         if (var1 instanceof XATxConnectionHandler) {
            Hashtable var2 = new Hashtable();
            var2.put("weblogic.transaction.registration.type", "standard");
            var2.put("weblogic.transaction.registration.settransactiontimeout", "true");
            ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).registerResource(var1.getPoolName(), ((XATxConnectionHandler)var1).getXAResource(), var2);
         } else {
            if (!(var1 instanceof LocalTxConnectionHandler)) {
               String var14 = Debug.getExceptionRegisterResourceIllegalType(var1.getClass().getName());
               throw new SystemException(var14);
            }

            ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).registerDynamicResource(var1.getPoolName(), ((LocalTxConnectionHandler)var1).getNonXAResource());
         }
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Tx, var10, var11);
         }

      }

   }

   private void unregisterResource(TxConnectionHandler var1) {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      Object[] var4;
      if (var10000) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Unregister_Resource_Low.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Unregister_Resource_Low.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this, var1};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Unregister_Resource_Low;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).unregisterResource(var1.getPoolName());
      } catch (SystemException var14) {
         if (Debug.isXAoutEnabled()) {
            Debug.xaOut(var1.getPool(), "WARNING:  Failed to unregister resource for destroyed connection:  " + var14);
         }
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_2, var10001, var10001.getActions());
         }

         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_Connector_Around_Tx, var10, var11);
         }

      }

   }

   static {
      _WLDF$INST_FLD_Connector_Around_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Tx");
      _WLDF$INST_FLD_Connector_Before_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Tx");
      _WLDF$INST_FLD_Connector_After_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Tx");
      _WLDF$INST_FLD_Connector_Enlist_Resource_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Enlist_Resource_Low");
      _WLDF$INST_FLD_Connector_Unregister_Resource_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Unregister_Resource_Low");
      _WLDF$INST_FLD_Connector_Register_Resource_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Register_Resource_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ResourceRegistrationManager.java", "weblogic.connector.transaction.outbound.ResourceRegistrationManager", "enlistResource", "(Lweblogic/connector/transaction/outbound/TxConnectionHandler;Ljavax/transaction/Transaction;)V", 85, InstrumentationSupport.makeMap(new String[]{"Connector_Around_Tx", "Connector_Before_Tx", "Connector_After_Tx", "Connector_Enlist_Resource_Low"}, new PointcutHandlingInfo[]{null, null, null, InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionHandlerPoolRenderer", false, true), null})}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ResourceRegistrationManager.java", "weblogic.connector.transaction.outbound.ResourceRegistrationManager", "registerResource", "(Lweblogic/connector/transaction/outbound/TxConnectionHandler;)V", 170, InstrumentationSupport.makeMap(new String[]{"Connector_Around_Tx", "Connector_Before_Tx", "Connector_Register_Resource_Low", "Connector_After_Tx"}, new PointcutHandlingInfo[]{null, null, InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionHandlerPoolRenderer", false, true)}), null}), (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ResourceRegistrationManager.java", "weblogic.connector.transaction.outbound.ResourceRegistrationManager", "unregisterResource", "(Lweblogic/connector/transaction/outbound/TxConnectionHandler;)V", 207, InstrumentationSupport.makeMap(new String[]{"Connector_Around_Tx", "Connector_Before_Tx", "Connector_After_Tx", "Connector_Unregister_Resource_Low"}, new PointcutHandlingInfo[]{null, null, null, InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, (ValueHandlingInfo)null, new ValueHandlingInfo[]{InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionHandlerPoolRenderer", false, true)})}), (boolean)0);
   }
}
