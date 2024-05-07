package weblogic.connector.transaction.outbound;

import java.security.AccessController;
import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnection;
import javax.transaction.xa.XAResource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.Utils;
import weblogic.connector.exception.NoEnlistXAResourceException;
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
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;
import weblogic.utils.StackTraceUtils;

public final class XATxConnectionHandler extends TxConnectionHandler {
   boolean enlistableXARes;
   XAResource xaRes;
   RecoveryOnlyXAWrapper recoveryWrapper = null;
   static final long serialVersionUID = 3242721431232342780L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.transaction.outbound.XATxConnectionHandler");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Destroy_Connection_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public XATxConnectionHandler(ManagedConnection var1, ConnectionPool var2, SecurityContext var3, ConnectionInfo var4) throws ResourceException {
      super(var1, var2, var3, var4, "XATransaction");
      this.setGlobalTransactionInProgress(false);
      this.enlistableXARes = true;
      this.initializeXAResource();
      this.addConnectionRuntimeMBean();
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
         if (this.recoveryWrapper != null) {
            this.recoveryWrapper.destroy();
            this.recoveryWrapper = null;
         }

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

   public void enListResource() throws ResourceException {
      if (this.transaction == null) {
         Transaction var1 = TxHelper.getTransaction();
         if (var1 != null && this.enlistableXARes) {
            Exception var2 = null;
            boolean var3 = false;
            Object var4 = null;

            try {
               ConnectionInfo var19 = this.connPool.getConnectionSharingManager().getSharedConnection();
               if (var19 != null && var19 != this.connectionInfo) {
                  int var20 = this.connPool.getAlternateCount();
                  ((Transaction)var1).enlistResource(this.xaRes, this.connPool.getName() + "_branchalias_" + var20);
               } else {
                  var1.enlistResource(this.xaRes);
               }

               var3 = true;
               TxCompletionNotification.register(var1, this);
               this.connPool.getConnectionSharingManager().addSharedConnection(super.getConnectionInfo());
            } catch (Exception var17) {
               Exception var5 = var17;
               var2 = var17;
               if (var4 != null) {
                  ((TxCompletionNotification)var4).deregister();
               }

               if (var3) {
                  try {
                     var1.delistResource(this.xaRes, 536870912);
                  } catch (Exception var16) {
                     this.destroyConnection();
                     if (Debug.isXAoutEnabled()) {
                        AuthenticatedSubject var7 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                        this.debug("Failed to delistResource, the connection will be destroyed:  " + var16 + "\n" + this.getRAiM().getAdapterLayer().throwable2StackTrace(var16, var7));
                     }
                  }
               }

               try {
                  String var6 = Debug.getExceptionEnlistmentFailed(this.connPool.getKey(), var5.toString());
                  ((Transaction)var1).setRollbackOnly(var6, var5);
               } catch (Exception var15) {
                  if (Debug.isXAoutEnabled()) {
                     this.debug("Failed to setRollbackOnly after enlistment failure:  " + var15 + StackTraceUtils.throwable2StackTrace(var15));
                  }
               }
            } finally {
               if (var2 != null) {
                  if (Debug.isXAoutEnabled()) {
                     AuthenticatedSubject var10 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                     this.debug("Failed to setup the connection for transaction enlistment:\n" + this.getRAiM().getAdapterLayer().throwable2StackTrace(var2, var10));
                  }

                  String var21 = Debug.getExceptionEnlistmentFailed(this.connPool.getKey(), var2.toString());
                  throw new ResourceException(var21, var2);
               }

            }

            this.transaction = var1;
            this.setGlobalTransactionInProgress(true);
         }
      }

   }

   public void initializeXAResource() throws ResourceException {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      String var3;
      String var5;
      String var6;
      try {
         this.xaRes = this.getRAiM().getAdapterLayer().getXAResource(this.managedConnection, var1);
      } catch (NoEnlistXAResourceException var7) {
         this.enlistableXARes = false;
         this.debug("Using Unenlistable XA Resource");
      } catch (ResourceException var8) {
         if (Debug.isXAoutEnabled()) {
            this.debug("XATxConnectionHandler:  Resource Adapter with Key = " + this.connPool.getKey() + " threw ResourceException from its implementation of ManagedConnection.getXAResource(), " + this.getRAiM().getAdapterLayer().toString(var8, var1) + "\n" + this.getRAiM().getAdapterLayer().throwable2StackTrace(var8, var1));
         }

         var3 = this.getRAiM().getAdapterLayer().toString(var8, var1);
         Throwable var10 = this.getRAiM().getAdapterLayer().getCause(var8, var1);
         if (var10 != null) {
            var5 = this.getRAiM().getAdapterLayer().toString(var10, var1);
            var6 = Debug.logGetXAResourceError(var5, this.connPool.getKey());
            Debug.logStackTrace(var6, var10);
            if (Debug.isXAoutEnabled()) {
               this.debug("XATxConnectionHandler:  ResourceException has Linked Exception:\n" + var5 + "\n" + this.getRAiM().getAdapterLayer().throwable2StackTrace(var10, var1));
            }
         } else {
            var5 = Debug.logGetXAResourceError(var3, this.connPool.getKey());
            Debug.logStackTrace(var5, var8);
         }

         throw var8;
      } catch (Throwable var9) {
         var3 = this.getRAiM().getAdapterLayer().toString(var9, var1);
         String var4 = this.getRAiM().getAdapterLayer().throwable2StackTrace(var9, var1);
         var5 = Debug.getExceptionMCGetXAResourceThrewNonResourceException(var9.toString());
         var6 = Debug.logGetXAResourceError(var3, this.connPool.getKey());
         Debug.logStackTraceString(var6, var4);
         Utils.throwAsResourceException(var5, var9);
      }

      if (this.enlistableXARes) {
         if (this.xaRes == null) {
            String var2 = Debug.getExceptionMCGetXAResourceReturnedNull();
            Debug.logGetXAResourceError(var2, this.connPool.getKey());
            throw new ResourceException(var2);
         } else {
            this.xaRes = new XAWrapper(this.xaRes, this, this.connPool);
         }
      }
   }

   private void debug(String var1) {
      if (Debug.isXAoutEnabled()) {
         Debug.xaOut(this.connPool, "XATxConnectionHandler: " + var1);
      }

   }

   public XAResource getXAResource() {
      return this.xaRes;
   }

   boolean isEnlistableXARes() {
      return this.enlistableXARes;
   }

   public void setXARecoveryWrapper(RecoveryOnlyXAWrapper var1) {
      this.recoveryWrapper = var1;
   }

   protected void initializeConnectionEventListener() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.getRAiM().getAdapterLayer().addConnectionEventListener(this.managedConnection, new TxConnectionEventListener(this, "XATxConnEventListener"), var1);
   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Destroy_Connection_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Destroy_Connection_Low");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "XATxConnectionHandler.java", "weblogic.connector.transaction.outbound.XATxConnectionHandler", "destroy", "()V", 76, InstrumentationSupport.makeMap(new String[]{"Connector_After_Outbound", "Connector_Destroy_Connection_Low", "Connector_Around_Outbound", "Connector_Before_Outbound"}, new PointcutHandlingInfo[]{null, InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionHandlerPoolRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null), null, null}), (boolean)0);
   }
}
