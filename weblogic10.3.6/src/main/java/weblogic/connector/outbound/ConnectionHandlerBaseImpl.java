package weblogic.connector.outbound;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.resource.spi.DissociatableManagedConnection;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ResourceAdapterInternalException;
import weblogic.common.ResourceException;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.monitoring.outbound.ConnectionPoolRuntimeMBeanImpl;
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
import weblogic.transaction.TransactionHelper;
import weblogic.utils.StackTraceUtils;

public abstract class ConnectionHandlerBaseImpl implements ConnectionHandler {
   public volatile boolean connectionErrorOccurred;
   protected ConnectionPool connPool;
   protected SecurityContext securityContext;
   protected ManagedConnection managedConnection;
   protected ConnectionInfo connectionInfo;
   protected volatile boolean isDestroyed;
   private volatile Boolean physicallyDestroyed = false;
   private AtomicBoolean mcDestroyed = new AtomicBoolean(false);
   private Throwable destroyStacktrace;
   private int numHandlesCreated;
   private int activeHighCount;
   private int numActiveHandles;
   private Hashtable connectionStates;
   private String transSupport;
   private Hashtable objectTable = new Hashtable();
   private Hashtable refTable = new Hashtable();
   static final long serialVersionUID = -4294097268005257698L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.outbound.ConnectionHandlerBaseImpl");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Destroy_Connection_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   protected ConnectionHandlerBaseImpl(ManagedConnection var1, ConnectionPool var2, SecurityContext var3, ConnectionInfo var4, String var5) {
      this.transSupport = var5;
      this.managedConnection = var1;
      this.connPool = var2;
      this.securityContext = var3;
      this.numHandlesCreated = 0;
      this.activeHighCount = 0;
      this.connectionInfo = var4;
      this.numActiveHandles = 0;
      this.connectionStates = new Hashtable();
      this.connectionErrorOccurred = false;
      this.isDestroyed = false;
      this.initializeConnectionEventListener();
   }

   public void forcedCleanup() {
      if (Debug.isConnectionsEnabled()) {
         Debug.connections("For pool '" + this.connPool.getName() + "' a connection " + "has timed out.  Closing all handles and releasing back to the " + "available pool");
      }

      Utils.startManagement();

      try {
         this.connPool.trackIdle(this.connectionInfo.getAllocationCallStack());
         Iterator var1 = this.connectionStates.keySet().iterator();

         while(var1.hasNext()) {
            WeakReference var2 = (WeakReference)((WeakReference)var1.next());

            try {
               this.closeConnection(var2);
            } catch (ResourceException var8) {
               if (Debug.isConnectionsEnabled()) {
                  Debug.connections("For pool '" + this.connPool.getName() + "' a ResourceException was thrown while trying to force cleanup " + "on idle connection handle:  " + var8);
               }
            }
         }
      } finally {
         Utils.stopManagement();
      }

   }

   public void closeConnection(Object var1) throws ResourceException {
      WeakReference var2 = (WeakReference)this.refTable.get(var1);
      this.closeConnection(var2);
   }

   private void closeConnection(WeakReference var1) throws ResourceException {
      if (var1 != null) {
         ConnectionState var2 = (ConnectionState)this.connectionStates.get(var1);
         if (var2 != null) {
            var2.setConnectionClosed(true);
            this.untrackObject(var1);
            if (!var2.isConnectionFinalized()) {
               this.decrementNumActiveHandles();
               this.connPool.releaseOnConnectionClosed(this.connectionInfo);
            }
         } else if (!this.connPool.getConnMgr().isProxyBeingTested()) {
            Debug.logConnectionAlreadyClosed(this.connPool.getName());
         }
      }

   }

   public void connectionFinalized(Reference var1) {
      if (Debug.isConnectionsEnabled()) {
         Debug.connections("connectionFinalized() for pool '" + this.connPool.getName() + "'");
      }

      try {
         ConnectionState var2 = (ConnectionState)this.connectionStates.get(var1);
         if (var2 != null) {
            Utils.startManagement();

            try {
               if (this.untrackObject(var1) && !var2.isConnectionClosed() && !this.isConnectionErrorOccurred()) {
                  var2.setConnectionFinalized(true);
                  this.connPool.trackLeak(this.connectionInfo.getAllocationCallStack());
                  this.connPool.releaseOnConnectionClosed(this.connectionInfo);
                  this.decrementNumActiveHandles();
               }
            } finally {
               Utils.stopManagement();
            }
         }
      } finally {
         if (Debug.isConnectionsEnabled()) {
            Debug.connections("connectionFinalized() completed for pool '" + this.connPool.getName() + "'");
         }

      }

   }

   public void destroyConnection() {
      this.isDestroyed = true;
      if (!this.isInTransaction() || this.isConnectionErrorOccurred()) {
         synchronized(this.physicallyDestroyed) {
            if (!this.physicallyDestroyed) {
               this.connPool.destroyConnection(this.connectionInfo);
               this.physicallyDestroyed = true;
            }
         }
      }

   }

   public Object createConnectionHandle(SecurityContext var1) throws javax.resource.ResourceException {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      Object var3 = this.getRAiM().getAdapterLayer().getConnection(this.managedConnection, var1.getSubject(), var1.getClientInfo(), var2);
      if (var3 == null) {
         String var4 = Debug.getExceptionMCGetConnectionReturnedNull(this.managedConnection.getClass().getName());
         throw new ResourceAdapterInternalException(var4);
      } else {
         return this.prepareHandle(var3);
      }
   }

   public void associateConnectionHandle(Object var1) throws javax.resource.ResourceException {
      if (Debug.isConnectionsEnabled()) {
         Debug.enter(this, "associateConnectionHandle()");
      }

      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.getRAiM().getAdapterLayer().associateConnection(this.managedConnection, var1, var2);
      this.prepareHandle(var1);
      if (Debug.isConnectionsEnabled()) {
         Debug.exit(this, "associateConnectionHandle()");
      }

   }

   public abstract void enListResource() throws javax.resource.ResourceException;

   public void cleanup() throws javax.resource.ResourceException {
      this.numHandlesCreated = 0;
      this.activeHighCount = 0;
      this.numActiveHandles = 0;
      this.connectionStates = new Hashtable();
      this.objectTable = new Hashtable();
      this.refTable = new Hashtable();
      this.connectionErrorOccurred = false;

      try {
         if (!this.isDestroyed && !this.mcDestroyed.get()) {
            AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            this.getRAiM().getAdapterLayer().cleanup(this.managedConnection, var1);
         }

      } catch (javax.resource.ResourceException var2) {
         Debug.logCloseConnectionError(this.transSupport, this.connectionInfo, "cleanup", var2);
         throw var2;
      } catch (Throwable var3) {
         Debug.logCloseConnectionError(this.transSupport, this.connectionInfo, "cleanup", var3);
         throw new javax.resource.ResourceException("Failed to cleanup ManagedConnection " + this.managedConnection, var3);
      }
   }

   public void destroy() {
      boolean var9;
      boolean var10000 = var9 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var10 = null;
      DiagnosticActionState[] var11 = null;
      Object var8 = null;
      Object[] var4;
      DynamicJoinPoint var16;
      DelegatingMonitor var10001;
      if (var10000) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var10 = var10001.getActions();
         InstrumentationSupport.preProcess(var16, var10001, var10002, var11 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Destroy_Connection_Low.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Destroy_Connection_Low.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Destroy_Connection_Low;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var4 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var4 = new Object[]{this};
         }

         var16 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var4, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var16, var10001, var10001.getActions());
      }

      try {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         try {
            this.destroyConnectionRuntimeMBean();
            if (this.mcDestroyed.compareAndSet(false, true)) {
               this.getRAiM().getAdapterLayer().destroy(this.managedConnection, var1);
               this.destroyStacktrace = new Exception("ManagedConnection [" + this.managedConnection + "] of " + this.getConnectionInfo() + " is destoyed at " + new Date() + " in thread " + Thread.currentThread());
            } else {
               Exception var2 = new Exception("ManagedConnection [" + this.managedConnection + "] of " + this.getConnectionInfo() + " is destoyed again at " + new Date() + " in thread " + Thread.currentThread());
               ConnectorLogger.logMCDestroyedAlready(this.managedConnection.toString(), this.getConnectionInfo().toString(), this.getPoolName(), this.destroyStacktrace == null ? "previous stacktrace was not recorded" : StackTraceUtils.throwable2StackTrace(this.destroyStacktrace), StackTraceUtils.throwable2StackTrace(var2));
            }
         } catch (Throwable var14) {
            Debug.logCloseConnectionError(this.transSupport, this.connectionInfo, "destroy", var14);
         }
      } finally {
         if (var9) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_0, _WLDF$INST_FLD_Connector_Around_Outbound, var10, var11);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_0, var10001, var10001.getActions());
         }

      }

   }

   public boolean shouldBeDiscard() {
      return this.isDestroyed || this.mcDestroyed.get() || this.connectionErrorOccurred || this.physicallyDestroyed;
   }

   public void dissociateHandles() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         if (Debug.isConnectionsEnabled()) {
            Debug.enter(this, "dissociateHandles()");
         }

         if (!this.isInTransaction() && this.managedConnection instanceof DissociatableManagedConnection && this.numActiveHandles > 0 && this.connectionInfo.isShareable()) {
            this.connectionStates.clear();
            this.refTable.clear();
            this.numActiveHandles = 0;
            this.connPool.releaseOnConnectionClosed(this.connectionInfo);
            this.getRAiM().getAdapterLayer().dissociateConnections((DissociatableManagedConnection)this.managedConnection, var1);
         }
      } catch (javax.resource.ResourceException var8) {
         String var3 = Debug.logDissociateHandlesFailed(this.connPool.getKey(), StackTraceUtils.throwable2StackTrace(var8) + "\n" + (this.mcDestroyed.get() ? "ManagedConnection was destroyed previously at: " + (this.destroyStacktrace == null ? "previous stacktrace was not recorded" : StackTraceUtils.throwable2StackTrace(this.destroyStacktrace)) : "ManagedConnection was not destroyed yet"));
         Debug.logStackTrace(var3, var8);
         Debug.println(this.getRAiM().getAdapterLayer().toString(var8, var1), "dissociateHandles failed");
      } finally {
         if (Debug.isConnectionsEnabled()) {
            Debug.exit(this, "dissociateHandles()");
         }

      }

   }

   protected synchronized void updateActiveHighCount() {
      if (this.numActiveHandles > this.activeHighCount) {
         this.activeHighCount = this.numActiveHandles;
      }

   }

   private synchronized void trackObject(Reference var1) {
      Integer var2 = (Integer)this.objectTable.get(var1);
      if (var2 == null) {
         var2 = new Integer(1);
      } else {
         var2 = new Integer(var2 + 1);
      }

      this.objectTable.put(var1, var2);
   }

   private synchronized boolean untrackObject(Reference var1) {
      Integer var3 = (Integer)this.objectTable.get(var1);
      boolean var2;
      if (var3 != null) {
         var3 = new Integer(var3 - 1);
         if (var3 > 0) {
            var2 = false;
            this.objectTable.put(var1, var3);
         } else {
            var2 = true;
            this.objectTable.remove(var1);
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   protected synchronized void decrementNumActiveHandles() {
      --this.numActiveHandles;
   }

   protected synchronized void incrementNumActiveHandles() {
      ++this.numActiveHandles;
      this.updateActiveHighCount();
   }

   protected synchronized void incrementNumHandlesCreated() {
      ++this.numHandlesCreated;
   }

   protected void addConnectionRuntimeMBean() {
      if (this.connPool == null) {
         Debug.throwAssertionError("ConnectionPool == null");
      }

      ConnectionPoolRuntimeMBeanImpl var1 = this.connPool.getRuntimeMBean();
      if (var1 == null) {
         Debug.throwAssertionError("ConnectionPool has a null runtime mbean");
      }

      if (this.connectionInfo == null) {
         Debug.throwAssertionError("connectionInfo == null");
      }

      var1.addConnectionRuntimeMBean(this.connectionInfo);
   }

   private void destroyConnectionRuntimeMBean() {
      if (this.connPool == null) {
         Debug.throwAssertionError("ConnectionPool == null");
      }

      ConnectionPoolRuntimeMBeanImpl var1 = this.connPool.getRuntimeMBean();
      if (var1 == null) {
         Debug.throwAssertionError("ConnectionPool has a null runtime mbean");
      }

      if (this.connectionInfo == null) {
         Debug.throwAssertionError("connectionInfo == null");
      }

      var1.removeConnectionRuntimeMBean(this.connectionInfo);
   }

   private Object prepareHandle(Object var1) throws javax.resource.ResourceException {
      this.enListResource();
      Object var2 = this.getConnectionOrProxy(var1);
      WeakHandleReference var3 = new WeakHandleReference(var2, this);
      if (this.refTable.containsKey(var1)) {
         String var4 = Debug.getExceptionDuplicateHandle();
         throw new ResourceAdapterInternalException(var4);
      } else {
         this.trackObject(var3);
         this.connectionStates.put(var3, new ConnectionState());
         this.refTable.put(var3, var3);
         this.incrementNumActiveHandles();
         this.incrementNumHandlesCreated();
         return var2;
      }
   }

   public void setConnectionErrorOccurred(boolean var1) {
      this.connectionErrorOccurred = var1;
   }

   public boolean isConnectionErrorOccurred() {
      return this.connectionErrorOccurred;
   }

   public synchronized int getActiveHandlesHighCount() {
      return this.activeHighCount;
   }

   public boolean isInTransaction() {
      return false;
   }

   /** @deprecated */
   public int getHandlesCreatedTotalCount() {
      return this.getNumHandlesCreated();
   }

   public String getPoolName() {
      return this.connPool.getName();
   }

   public SecurityContext getSecurityContext() {
      return this.securityContext;
   }

   public ManagedConnection getManagedConnection() {
      return this.managedConnection;
   }

   public ConnectionInfo getConnectionInfo() {
      return this.connectionInfo;
   }

   public boolean isCallingTransactionLocal() {
      Transaction var1 = (Transaction)TransactionHelper.getTransactionHelper().getTransaction();
      if (var1 != null) {
         return var1.getProperty("LOCAL_ENTITY_TX") != null;
      } else {
         return false;
      }
   }

   public int getNumActiveConns() {
      return this.numActiveHandles;
   }

   public ConnectionPool getPool() {
      return this.connPool;
   }

   protected int getNumHandlesCreated() {
      return this.numHandlesCreated;
   }

   private Object getConnectionOrProxy(Object var1) {
      if (this.connPool.getCanUseProxy()) {
         Object var2 = ConnectionWrapper.createConnectionWrapper(this.connPool, this.connectionInfo, var1);
         return var2;
      } else {
         return var1;
      }
   }

   protected abstract void initializeConnectionEventListener();

   protected RAInstanceManager getRAiM() {
      return this.connPool.getRAInstanceManager();
   }

   public Map<String, Object> dumpState() {
      HashMap var1 = new HashMap();
      var1.put("activeHighCount", this.activeHighCount);
      var1.put("connectionErrorOccurred", this.connectionErrorOccurred);
      var1.put("connectionInfo", this.connectionInfo);
      var1.put("connectionStates", this.connectionStates);
      var1.put("pool", this.connPool);
      var1.put("poolName", this.getPoolName());
      var1.put("destroyStacktrace", this.destroyStacktrace);
      var1.put("isDestroyed", this.isDestroyed);
      var1.put("managedConnection", this.managedConnection);
      var1.put("mcDestroyed", this.mcDestroyed);
      var1.put("numActiveHandles", this.numActiveHandles);
      var1.put("numHandlesCreated", this.numHandlesCreated);
      var1.put("objectTable", this.objectTable);
      var1.put("physicallyDestroyed", this.physicallyDestroyed);
      var1.put("refTable", this.refTable);
      var1.put("securityContext", this.securityContext);
      var1.put("transSupport", this.transSupport);
      var1.put("RAiM", this.getRAiM());
      return var1;
   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Destroy_Connection_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Destroy_Connection_Low");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ConnectionHandlerBaseImpl.java", "weblogic.connector.outbound.ConnectionHandlerBaseImpl", "destroy", "()V", 466, InstrumentationSupport.makeMap(new String[]{"Connector_After_Outbound", "Connector_Destroy_Connection_Low", "Connector_Around_Outbound", "Connector_Before_Outbound"}, new PointcutHandlingInfo[]{null, InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionHandlerPoolRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null), null, null}), (boolean)0);
   }

   private class WeakHandleReference extends WeakReference {
      private ConnectionHandler connHandler;
      private int hash;

      public WeakHandleReference(Object var2, ConnectionHandler var3) {
         super(var2, (ReferenceQueue)null);
         this.connHandler = var3;
         this.hash = var2.hashCode();
         if (Proxy.isProxyClass(var2.getClass()) && Proxy.getInvocationHandler(var2) instanceof ConnectionWrapper) {
            ((ConnectionWrapper)Proxy.getInvocationHandler(var2)).setWeakReference(this);
         }

      }

      public int hashCode() {
         return this.hash;
      }

      public boolean equals(Object var1) {
         boolean var2;
         if (var1 == null) {
            var2 = false;
         } else if (var1 instanceof WeakHandleReference) {
            var2 = var1 == this;
         } else {
            Object var3 = this.get();
            if (var3 != null && Proxy.isProxyClass(var3.getClass()) && Proxy.getInvocationHandler(var3) instanceof ConnectionWrapper) {
               ConnectionWrapper var4 = (ConnectionWrapper)Proxy.getInvocationHandler(var3);
               var3 = var4.getConnectionInstance();
            }

            var2 = var3 == var1;
         }

         return var2;
      }
   }
}
