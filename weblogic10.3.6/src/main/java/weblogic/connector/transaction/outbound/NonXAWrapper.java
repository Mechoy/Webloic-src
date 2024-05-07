package weblogic.connector.transaction.outbound;

import java.security.AccessController;
import java.util.Map;
import javax.resource.ResourceException;
import javax.resource.spi.LocalTransaction;
import javax.transaction.xa.Xid;
import weblogic.connector.common.Debug;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.nonxa.NonXAException;
import weblogic.transaction.nonxa.NonXAResource;

public final class NonXAWrapper implements NonXAResource {
   LocalTransaction localTransaction;
   LocalTxConnectionHandler connectionHandler;
   static final long serialVersionUID = -2969897281728638615L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.transaction.outbound.NonXAWrapper");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Tx;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   NonXAWrapper(LocalTransaction var1, LocalTxConnectionHandler var2) {
      this.localTransaction = var1;
      this.connectionHandler = var2;
   }

   public void commit(Xid var1, boolean var2) throws NonXAException {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      Object[] var10;
      DelegatingMonitor var10001;
      DynamicJoinPoint var22;
      if (var10000) {
         var10 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var10 = InstrumentationSupport.toSensitive(3);
         }

         var22 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var22, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium.isEnabledAndNotDyeFiltered()) {
         var10 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium.isArgumentsCaptureNeeded()) {
            var10 = InstrumentationSupport.toSensitive(3);
         }

         var22 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium;
         InstrumentationSupport.process(var22, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var10 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var10 = InstrumentationSupport.toSensitive(3);
         }

         var22 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var22, var10001, var10001.getActions());
      }

      try {
         this.debug(" - commit request for xid: " + var1);
         if (this.connectionHandler.isConnectionErrorOccurred()) {
            this.debug(" - connectionHandler.isConnectionErrorOccurred is true; not issuing commit");
            throw new NonXAException("The underlying ManagedConnection met error and is unusable; cannot commit");
         }

         if (this.localTransaction == null) {
            this.debug(" - localTransaction is null; not issuing commit");
            throw new NonXAException("The localTransaction is null; cannot commit");
         }

         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         try {
            this.debug(" - issuing commit");
            this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().commit(this.localTransaction, var3);
         } catch (ResourceException var20) {
            String var5 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var20, var3);
            this.debug(" ResourceException during commit request: " + var5);
            Throwable var6 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().getCause(var20, var3);
            String var7;
            if (null != var6) {
               var7 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var6, var3);
               this.debug("ResourceException has Linked Exception : " + var7);
            }

            var7 = Debug.getExceptionCommitFailed(var5, this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var20, var3));
            NonXAException var8 = new NonXAException(var7);
            var8.initCause(var20);
            throw var8;
         }
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

         if (var15) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Tx, var16, var17);
         }

      }

   }

   public void rollback(Xid var1) throws NonXAException {
      boolean var14;
      boolean var10000 = var14 = _WLDF$INST_FLD_Connector_Around_Tx.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var15 = null;
      DiagnosticActionState[] var16 = null;
      Object var13 = null;
      Object[] var9;
      DelegatingMonitor var10001;
      DynamicJoinPoint var21;
      if (var10000) {
         var9 = null;
         if (_WLDF$INST_FLD_Connector_Around_Tx.isArgumentsCaptureNeeded()) {
            var9 = InstrumentationSupport.toSensitive(2);
         }

         var21 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Tx;
         DiagnosticAction[] var10002 = var15 = var10001.getActions();
         InstrumentationSupport.preProcess(var21, var10001, var10002, var16 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low.isEnabledAndNotDyeFiltered()) {
         var9 = null;
         if (_WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low.isArgumentsCaptureNeeded()) {
            var9 = InstrumentationSupport.toSensitive(2);
         }

         var21 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low;
         InstrumentationSupport.process(var21, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Tx.isEnabledAndNotDyeFiltered()) {
         var9 = null;
         if (_WLDF$INST_FLD_Connector_Before_Tx.isArgumentsCaptureNeeded()) {
            var9 = InstrumentationSupport.toSensitive(2);
         }

         var21 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Tx;
         InstrumentationSupport.process(var21, var10001, var10001.getActions());
      }

      try {
         this.debug(" - rollback request for xid: " + var1);
         if (!this.connectionHandler.isConnectionErrorOccurred()) {
            if (this.localTransaction == null) {
               this.debug(" - localTransaction is null; not issuing rollback");
               return;
            }

            AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

            try {
               this.debug(" - issuing rollback");
               this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().rollback(this.localTransaction, var2);
               return;
            } catch (ResourceException var19) {
               String var4 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var19, var2);
               this.debug(" - rollback for xid: " + var1 + " ResourceException : " + var4);
               Throwable var5 = this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().getCause(var19, var2);
               if (null != var5) {
                  this.debug(" - rollback for xid: " + var1 + " ResourceException has Linked Exception : " + this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().toString(var5, var2));
               }

               String var6 = Debug.getExceptionRollbackFailed(var4, this.connectionHandler.getPool().getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var19, var2));
               NonXAException var7 = new NonXAException(var6);
               var7.initCause(var19);
               throw var7;
            }
         }

         this.debug(" - isConnectionErrorOccurred is true; not issuing rollback");
      } finally {
         if (_WLDF$INST_FLD_Connector_After_Tx.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Tx;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

         if (var14) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Tx, var15, var16);
         }

      }

   }

   void disable() {
      this.localTransaction = null;
   }

   private void debug(String var1) {
      if (Debug.isLocalOutEnabled()) {
         Debug.localOut(this.connectionHandler.getPool(), "NonXAWrapper: " + this + var1);
      }

   }

   public boolean isSameRM(NonXAResource var1) throws NonXAException {
      if (!(var1 instanceof NonXAWrapper)) {
         return false;
      } else {
         return this.connectionHandler.getManagedConnection() == ((NonXAWrapper)var1).connectionHandler.getManagedConnection();
      }
   }

   static {
      _WLDF$INST_FLD_Connector_Around_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Tx");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Rollback_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Rollback_Low");
      _WLDF$INST_FLD_Connector_Before_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Tx");
      _WLDF$INST_FLD_Connector_After_Tx = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Tx");
      _WLDF$INST_FLD_Connector_Outbound_Transaction_Commmit_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Outbound_Transaction_Commmit_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NonXAWrapper.java", "weblogic.connector.transaction.outbound.NonXAWrapper", "commit", "(Ljavax/transaction/xa/Xid;Z)V", 62, (Map)null, (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "NonXAWrapper.java", "weblogic.connector.transaction.outbound.NonXAWrapper", "rollback", "(Ljavax/transaction/xa/Xid;)V", 124, (Map)null, (boolean)0);
   }
}
