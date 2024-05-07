package weblogic.connector.outbound;

import java.security.AccessController;
import java.util.Map;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import weblogic.common.ResourceException;
import weblogic.connector.common.Debug;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class NoTxConnectionEventListener implements ConnectionEventListener {
   protected ConnectionHandler connectionHandler;
   private String listenerName;
   static final long serialVersionUID = -1839136863056260782L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.outbound.NoTxConnectionEventListener");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Connection_Error_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Connection_Closed_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;
   public static final JoinPoint _WLDF$INST_JPFLD_2;
   public static final JoinPoint _WLDF$INST_JPFLD_3;
   public static final JoinPoint _WLDF$INST_JPFLD_4;

   public NoTxConnectionEventListener(ConnectionHandler var1) {
      this.connectionHandler = var1;
      this.listenerName = "NoTransConnEventListener";
   }

   public NoTxConnectionEventListener(ConnectionHandler var1, String var2) {
      this.connectionHandler = var1;
      this.listenerName = var2;
   }

   public void localTransactionStarted(ConnectionEvent var1) {
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

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.debugConnEvent("LOCAL_TRANSACTION_STARTED event received. Ignore");
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Outbound, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   public void localTransactionRolledback(ConnectionEvent var1) {
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

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.debugConnEvent("LOCAL_TRANSACTION_ROLLEDBACK event received. Ignore");
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

   public void localTransactionCommitted(ConnectionEvent var1) {
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

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var9 = var10001.getActions();
         InstrumentationSupport.preProcess(var13, var10001, var10002, var10 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var3 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var3 = InstrumentationSupport.toSensitive(2);
         }

         var13 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_2, var3, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var13, var10001, var10001.getActions());
      }

      try {
         this.debugConnEvent("LOCAL_TRANSACTION_COMMITTED event received. Ignore");
      } finally {
         if (var8) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_2, _WLDF$INST_FLD_Connector_Around_Outbound, var9, var10);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_2, var10001, var10001.getActions());
         }

      }

   }

   public void connectionClosed(ConnectionEvent var1) {
      boolean var11;
      boolean var10000 = var11 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var12 = null;
      DiagnosticActionState[] var13 = null;
      Object var10 = null;
      Object[] var6;
      DelegatingMonitor var10001;
      DynamicJoinPoint var18;
      if (var10000) {
         var6 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var6 = InstrumentationSupport.toSensitive(2);
         }

         var18 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var6, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var12 = var10001.getActions();
         InstrumentationSupport.preProcess(var18, var10001, var10002, var13 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var6 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var6 = InstrumentationSupport.toSensitive(2);
         }

         var18 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var6, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var18, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Connection_Closed_Low.isEnabledAndNotDyeFiltered()) {
         var6 = null;
         if (_WLDF$INST_FLD_Connector_Connection_Closed_Low.isArgumentsCaptureNeeded()) {
            var6 = InstrumentationSupport.toSensitive(2);
         }

         var18 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_3, var6, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Connection_Closed_Low;
         InstrumentationSupport.process(var18, var10001, var10001.getActions());
      }

      try {
         AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         try {
            this.debugConnEvent("CONNECTION_CLOSED event received");
            this.connectionHandler.getPool().incrementCloseCount();
            Object var3 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().getConnectionHandle(var1, var2);
            if (var3 == null) {
               String var4 = Debug.getExceptionHandleNotSet();
               throw new IllegalStateException(var4);
            }

            this.connectionHandler.closeConnection(var3);
         } catch (ResourceException var16) {
            Debug.logCloseConnectionError(this.listenerName, this.connectionHandler.getConnectionInfo(), "close", var16);
         }
      } finally {
         if (var11) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_3, _WLDF$INST_FLD_Connector_Around_Outbound, var12, var13);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_3, var10001, var10001.getActions());
         }

      }

   }

   public void connectionErrorOccurred(ConnectionEvent var1) {
      boolean var12;
      boolean var10000 = var12 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var13 = null;
      DiagnosticActionState[] var14 = null;
      Object var11 = null;
      Object[] var7;
      DelegatingMonitor var10001;
      DynamicJoinPoint var19;
      if (var10000) {
         var7 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var7 = InstrumentationSupport.toSensitive(2);
         }

         var19 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var7, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var13 = var10001.getActions();
         InstrumentationSupport.preProcess(var19, var10001, var10002, var14 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var7 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var7 = InstrumentationSupport.toSensitive(2);
         }

         var19 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var7, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var19, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Connection_Error_Low.isEnabledAndNotDyeFiltered()) {
         var7 = null;
         if (_WLDF$INST_FLD_Connector_Connection_Error_Low.isArgumentsCaptureNeeded()) {
            var7 = InstrumentationSupport.toSensitive(2);
         }

         var19 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_4, var7, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Connection_Error_Low;
         InstrumentationSupport.process(var19, var10001, var10001.getActions());
      }

      try {
         AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         try {
            this.debugConnEvent("CONNECTION_ERROR_OCCURRED event received");
            this.connectionHandler.setConnectionErrorOccurred(true);
            this.connectionHandler.destroyConnection();
            this.connectionHandler.getPool().incrementConnectionsDestroyedByErrorCount();
            Exception var3 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().getException(var1, var2);
            String var4 = "connectionErrorOccurred:  EventId=" + this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().getId(var1, var2);
            if (var3 != null) {
               String var5 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var3, var2);
               var4 = var4 + ", Exception information:  " + var5;
            }

            this.debugConnEvent(var4);
         } catch (Exception var17) {
            this.debugConnEvent("Exception during connectionErrorOccurred", var17);
         }
      } finally {
         if (var12) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_4, _WLDF$INST_FLD_Connector_Around_Outbound, var13, var14);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_4, var10001, var10001.getActions());
         }

      }

   }

   public void cleanup() {
   }

   protected void debugConnEvent(String var1) {
      if (Debug.isConnEventsEnabled()) {
         Debug.connEvent("For the " + this.listenerName + " of pool '" + this.connectionHandler.getPoolName() + "' " + var1);
      }

   }

   protected void debugConnEvent(String var1, Exception var2) {
      if (Debug.isConnEventsEnabled()) {
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         String var4 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var2, var3);
         Debug.connEvent("For the " + this.listenerName + " of pool '" + this.connectionHandler.getPoolName() + "' " + var1 + "\n" + var4);
      }

   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_FLD_Connector_Connection_Error_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Connection_Error_Low");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Rollback_Low");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Start_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Start_Medium");
      _WLDF$INST_FLD_Connector_Connection_Closed_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Connection_Closed_Low");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Commmit_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NoTxConnectionEventListener.java", "weblogic.connector.outbound.NoTxConnectionEventListener", "localTransactionStarted", "(Ljavax/resource/spi/ConnectionEvent;)V", 66, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NoTxConnectionEventListener.java", "weblogic.connector.outbound.NoTxConnectionEventListener", "localTransactionRolledback", "(Ljavax/resource/spi/ConnectionEvent;)V", 81, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_2 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NoTxConnectionEventListener.java", "weblogic.connector.outbound.NoTxConnectionEventListener", "localTransactionCommitted", "(Ljavax/resource/spi/ConnectionEvent;)V", 96, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_3 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NoTxConnectionEventListener.java", "weblogic.connector.outbound.NoTxConnectionEventListener", "connectionClosed", "(Ljavax/resource/spi/ConnectionEvent;)V", 111, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_4 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NoTxConnectionEventListener.java", "weblogic.connector.outbound.NoTxConnectionEventListener", "connectionErrorOccurred", "(Ljavax/resource/spi/ConnectionEvent;)V", 150, (Map)null, (boolean)0);
   }
}
