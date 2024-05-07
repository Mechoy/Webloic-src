package weblogic.connector.transaction.outbound;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnection;
import javax.transaction.Transaction;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.outbound.ConnectionHandlerBaseImpl;
import weblogic.connector.outbound.ConnectionInfo;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;

public abstract class TxConnectionHandler extends ConnectionHandlerBaseImpl {
   protected Transaction transaction;
   private boolean globalTransactionInProgress = false;
   private boolean localTransactionInProgress = false;
   static final long serialVersionUID = 5380606416383877290L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.transaction.outbound.TxConnectionHandler");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Destroy_Connection_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   TxConnectionHandler(ManagedConnection var1, ConnectionPool var2, SecurityContext var3, ConnectionInfo var4, String var5) {
      super(var1, var2, var3, var4, var5);
   }

   public void destroy() {
      boolean var7;
      boolean var10000 = var7 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var8 = null;
      DiagnosticActionState[] var9 = null;
      Object var6 = null;
      DelegatingMonitor var10001;
      Object[] var2;
      DynamicJoinPoint var12;
      if (var10000) {
         var2 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var2 = new Object[]{this};
         }

         var12 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var2, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var8 = var10001.getActions();
         InstrumentationSupport.preProcess(var12, var10001, var10002, var9 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Destroy_Connection_Low.isEnabledAndNotDyeFiltered()) {
         var2 = null;
         if (_WLDF$INST_FLD_Connector_Destroy_Connection_Low.isArgumentsCaptureNeeded()) {
            var2 = new Object[]{this};
         }

         var12 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var2, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Destroy_Connection_Low;
         InstrumentationSupport.process(var12, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var2 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var2 = new Object[]{this};
         }

         var12 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var2, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var12, var10001, var10001.getActions());
      }

      try {
         this.connPool.getResourceRegistrationManager().removeResource(this);
         super.destroy();
      } finally {
         if (var7) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Outbound, var8, var9);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   void notifyConnPoolOfTransCompletion() {
      this.setGlobalTransactionInProgress(false);
      this.transaction = null;
      if (!this.isDestroyed) {
         this.connPool.releaseOnTransactionCompleted(this.getConnectionInfo());
         this.dissociateHandles();
      } else {
         this.destroyConnection();
      }

   }

   public void setLocalTransactionInProgress(boolean var1, String var2) {
      if (this.isGlobalTransactionInProgress()) {
         String var3 = ConnectorLogger.logAdapterShouldnotSendLocalTxEvent(var2);
         throw new IllegalStateException(var3);
      } else {
         this.setLocalTransactionInProgress(var1);
      }
   }

   public void setLocalTransactionInProgress(boolean var1) {
      if (!this.localTransactionInProgress && var1) {
         XANotificationListener.getInstance().registerNotification(this);
      } else if (this.localTransactionInProgress && !var1) {
         XANotificationListener.getInstance().deregisterNotification(this);
      }

      this.localTransactionInProgress = var1;
   }

   public synchronized void setGlobalTransactionInProgress(boolean var1) {
      this.globalTransactionInProgress = var1;
   }

   public synchronized boolean isGlobalTransactionInProgress() {
      return this.globalTransactionInProgress;
   }

   public boolean isLocalTransactionInProgress() {
      return this.localTransactionInProgress;
   }

   public Transaction getTransaction() {
      return this.transaction;
   }

   public void cleanup() throws ResourceException {
      this.globalTransactionInProgress = false;
      this.localTransactionInProgress = false;
      super.cleanup();
   }

   public synchronized boolean isInTransaction() {
      return this.isLocalTransactionInProgress() || this.isGlobalTransactionInProgress();
   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Destroy_Connection_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Destroy_Connection_Low");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "TxConnectionHandler.java", "weblogic.connector.transaction.outbound.TxConnectionHandler", "destroy", "()V", 62, InstrumentationSupport.makeMap(new String[]{"Connector_After_Outbound", "Connector_Destroy_Connection_Low", "Connector_Around_Outbound", "Connector_Before_Outbound"}, new PointcutHandlingInfo[]{null, InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionHandlerPoolRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null), null, null}), (boolean)0);
   }
}
