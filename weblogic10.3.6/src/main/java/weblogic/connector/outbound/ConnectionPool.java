package weblogic.connector.outbound;

import com.bea.connector.diagnostic.ManagedConnectionType;
import com.bea.connector.diagnostic.OutboundAdapterType;
import com.bea.logging.RotatingFileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Vector;
import javax.resource.NotSupportedException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.SecurityException;
import javax.resource.spi.ValidatingManagedConnectionFactory;
import javax.transaction.SystemException;
import weblogic.common.ConnectDisabledException;
import weblogic.common.ResourceException;
import weblogic.common.resourcepool.PooledResource;
import weblogic.common.resourcepool.PooledResourceFactory;
import weblogic.common.resourcepool.PooledResourceInfo;
import weblogic.common.resourcepool.ResourcePoolImpl;
import weblogic.common.resourcepool.ResourcePoolProfiler;
import weblogic.connector.common.ConnectorDiagnosticImageSource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.monitoring.ConnectorComponentRuntimeMBeanImpl;
import weblogic.connector.monitoring.outbound.ConnectionPoolRuntimeMBeanImpl;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.connector.transaction.outbound.RecoveryOnlyXAWrapper;
import weblogic.connector.transaction.outbound.ResourceRegistrationManager;
import weblogic.connector.transaction.outbound.XATxConnectionHandler;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.jdbc.common.internal.ConnectionLeakProfile;
import weblogic.logging.LogFileConfigUtil;
import weblogic.logging.LoggingOutputStream;
import weblogic.logging.WLLevel;
import weblogic.logging.j2ee.LoggingBeanAdapter;
import weblogic.management.ManagementException;
import weblogic.management.logging.LogRuntime;
import weblogic.management.runtime.ConnectorComponentRuntimeMBean;
import weblogic.management.runtime.LogRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class ConnectionPool extends ResourcePoolImpl {
   public static final String CP_MATCH_CONNECTIONS_SUPORTED = "matchConnectionsSupported";
   private ConnectionPoolRuntimeMBeanImpl rMBean = null;
   private String poolName;
   private ManagedConnectionFactory managedConnectionFactory;
   private ConnectionSharingManager connectionSharingManager;
   private int numReserves = 0;
   private int numReleases = 0;
   private int numLeaks = 0;
   private int numMatchSuccesses;
   private int numRequestsRejected;
   private boolean canUseProxy;
   private ConnectionManagerImpl connMgr;
   private Object connectionFactory;
   private Vector leakProfiles = new Vector();
   private Vector idleProfiles = new Vector();
   private int numRecycled = 0;
   private ResourceRegistrationManager resRegManager = new ResourceRegistrationManager();
   private Object proxyTestConnectionHandle;
   private OutboundInfo initOutboundInfo;
   private OutboundInfo pendingOutboundInfo;
   private RAOutboundManager raOutboundManager;
   private RecoveryOnlyXAWrapper recoveryWrapper;
   private int alternateCount = 0;
   long closeCount = 0L;
   long freePoolSizeHighWaterMark = 0L;
   long freePoolSizeLowWaterMark = 0L;
   long poolSizeHighWaterMark = 0L;
   long poolSizeLowWaterMark = 0L;
   ConnectorComponentRuntimeMBean connRuntimeMbean;
   int connectionsDestroyedByErrorCount = 0;
   boolean useFirstAvailable;
   ResourcePoolProfiler profiler = new ConnectionPoolProfiler(this);
   private volatile boolean shutdown = false;
   private LoggingBeanAdapter loggingBeanAdapter = null;
   private LogRuntime logRuntime = null;
   private String applicationName;
   private String componentName;
   private boolean isShareAllowed;
   static final long serialVersionUID = -8294346449592412623L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.outbound.ConnectionPool");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Release_Connection_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Reserve_Connection_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;
   public static final JoinPoint _WLDF$INST_JPFLD_1;

   public ConnectionPool(ManagedConnectionFactory var1, OutboundInfo var2, String var3, String var4, RAOutboundManager var5) {
      this.applicationName = var3;
      this.componentName = var4;
      this.managedConnectionFactory = var1;
      this.initOutboundInfo = var2;
      this.raOutboundManager = var5;
      this.setPoolName();
      this.connectionSharingManager = new ConnectionSharingManager(this.poolName);
      this.connMgr = new ConnectionManagerImpl(this);
      if (Debug.isPoolingEnabled()) {
         Debug.pooling("Constructed the connection pool : '" + this.poolName + "' with Key '" + this.getKey() + "'");
      }

      this.useFirstAvailable = var2.isUseFirstAvailable();
      this.isShareAllowed = !"NoTransaction".equals(var2.getTransactionSupport());
   }

   public void shutdown() throws ResourceException {
      this.undoSetupForXARecovery();
      super.shutdown();
      this.unregisterConnectionPoolRuntimeMBean();
      this.setLoggingBeanAdapter((LoggingBeanAdapter)null);
      this.shutdown = true;
   }

   public PooledResourceFactory initPooledResourceFactory(Properties var1) throws ResourceException {
      return new ConnectionFactory(this);
   }

   public synchronized PooledResource matchResource(PooledResourceInfo var1) throws ResourceException {
      if (var1 == null) {
         if (Debug.verbose) {
            Debug.enter(this, "matchResource(): called with null. delegate to super's default behavior");
         }

         return super.matchResource(var1);
      } else {
         Object var2 = null;
         HashSet var3 = new HashSet();
         Hashtable var4 = new Hashtable();
         Object[] var5 = null;
         ManagedConnection var6 = null;
         ConnectionInfo var7 = null;
         SecurityContext var8 = null;
         if (Debug.verbose) {
            Debug.enter(this, "matchResource()");
         }

         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("matchResource() called with PoolResourceInfo: " + var1.toString());
            this.dumpPool("at start of matchResource");
         }

         try {
            var5 = super.available.toArray();
            int var10 = super.available.size();
            if (!this.useFirstAvailable) {
               var8 = ((ConnectionReqInfo)var1).getSecurityContext();
               if (Debug.isPoolVerboseEnabled()) {
                  this.debugVerbose("There are " + var10 + " in the available list");
               }

               AuthenticatedSubject var11 = null;
               if (var5 != null && var10 > 0) {
                  if (var11 == null) {
                     var11 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                  }

                  for(int var13 = 0; var13 < var10; ++var13) {
                     PooledResource var9 = (PooledResource)var5[var13];
                     ConnectionInfo var12 = (ConnectionInfo)var5[var13];
                     var3.add(var12.getConnectionHandler().getManagedConnection());
                     this.getRAInstanceManager().getAdapterLayer().htPut(var4, var12.getConnectionHandler().getManagedConnection(), var9, var11);
                  }
               }

               if (var3.size() > 0) {
                  if (var11 == null) {
                     var11 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                  }

                  try {
                     var6 = this.getRAInstanceManager().getAdapterLayer().matchManagedConnection(this.managedConnectionFactory, var3, var8.getSubject(), var8.getClientInfo(), var11);
                  } catch (NotSupportedException var19) {
                     super.matchSupported = false;
                  } catch (javax.resource.ResourceException var20) {
                     throw new ResourceException(var20);
                  }
               }

               if (var6 != null) {
                  if (var11 == null) {
                     var11 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                  }

                  var7 = (ConnectionInfo)this.getRAInstanceManager().getAdapterLayer().htGet(var4, var6, var11);
                  if (var7 != null) {
                     if (Debug.isPoolVerboseEnabled()) {
                        this.debugVerbose("removing " + var7.toString() + " from available");
                     }

                     if (super.available.remove(var7)) {
                        var2 = var7;
                     }

                     this.incrementNumMatchSuccesses();
                  }
               }
            } else if (var10 > 0 && super.available.remove(var5[0])) {
               var2 = (PooledResource)var5[0];
            }
         } finally {
            if (Debug.isPoolVerboseEnabled()) {
               this.dumpPool("on exiting matchResource()");
               this.debugVerbose("exiting ConnectionPool.matchResource() and returning " + (var2 == null ? "null" : var2.toString()));
            }

            if (Debug.verbose) {
               Debug.exit(this, "matchResource()");
            }

         }

         return (PooledResource)var2;
      }
   }

   public PooledResource reserveResource(int var1, PooledResourceInfo var2) throws ResourceException {
      boolean var15;
      boolean var10000 = var15 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var16 = null;
      DiagnosticActionState[] var17 = null;
      Object var14 = null;
      Object[] var10;
      DelegatingMonitor var10001;
      DynamicJoinPoint var30;
      if (var10000) {
         var10 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var10 = new Object[]{this, InstrumentationSupport.convertToObject(var1), var2};
         }

         var30 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var16 = var10001.getActions();
         InstrumentationSupport.preProcess(var30, var10001, var10002, var17 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var10 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var10 = new Object[]{this, InstrumentationSupport.convertToObject(var1), var2};
         }

         var30 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var30, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Reserve_Connection_Low.isEnabledAndNotDyeFiltered()) {
         var10 = null;
         if (_WLDF$INST_FLD_Connector_Reserve_Connection_Low.isArgumentsCaptureNeeded()) {
            var10 = new Object[]{this, InstrumentationSupport.convertToObject(var1), var2};
         }

         var30 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var10, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Reserve_Connection_Low;
         InstrumentationSupport.process(var30, var10001, var10001.getActions());
      }

      boolean var21 = false;

      ConnectionInfo var11;
      ConnectionInfo var31;
      try {
         var21 = true;
         if (Debug.verbose) {
            Debug.enter(this, "reserveResource()");
         }

         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Entering reserveResource( " + var1 + ", " + var2.toString() + " )");
            this.debugVerbose("reserveResource() current capacity = " + this.getCurrCapacity() + ", max capacity = " + this.getMaxCapacity());
            this.dumpPool("on entering reserveResource");
         }

         ConnectionInfo var3 = null;
         long var4 = System.currentTimeMillis();

         try {
            try {
               if (super.state != 101 && super.state != 103) {
                  String var28 = Debug.getExceptionPoolDisabled(this.poolName);
                  throw new ConnectDisabledException(var28);
               }

               boolean var6 = ((ConnectionReqInfo)var2).isShareable();
               if (this.isShareAllowed && var6) {
                  var3 = this.connectionSharingManager.getSharedConnection();
               }

               if (var3 == null) {
                  var3 = (ConnectionInfo)super.reserveResource(var1, var2);
                  var3.setShareable(var6);
                  this.incrementNumReserves();
                  if (this.isShareAllowed && var6) {
                     this.connectionSharingManager.addSharedConnection(var3);
                  }
               }
            } catch (ResourceException var25) {
               this.incrementNumRequestsRejected();
               throw var25;
            }

            if (Debug.isPoolVerboseEnabled()) {
               this.dumpPool("on exiting reserveResource");
               this.debugVerbose("Returning " + var3 + " from reserveResource(  " + var1 + ", " + var2.toString() + " )");
            }

            Debug.assertion(var3 != null, "returnConnectionInfo != null");
            this.updatePoolStats();
         } finally {
            if (Debug.verbose) {
               Debug.exit(this, "reserveResource()");
            }

         }

         long var29 = System.currentTimeMillis();
         var3.setReserveDurationTime(var29 - var4);
         var3.setReserveTime(var29);
         var31 = var3;
         var21 = false;
      } finally {
         if (var21) {
            var11 = null;
            if (var15) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var11), _WLDF$INST_FLD_Connector_Around_Outbound, var16, var17);
            }

            if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var33 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var11);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Connector_After_Outbound;
               InstrumentationSupport.process(var33, var10003, var10003.getActions());
            }

         }
      }

      var11 = var31;
      if (var15) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var11), _WLDF$INST_FLD_Connector_Around_Outbound, var16, var17);
      }

      if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var32 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var11);
         DelegatingMonitor var34 = _WLDF$INST_FLD_Connector_After_Outbound;
         InstrumentationSupport.process(var32, var34, var34.getActions());
      }

      return var31;
   }

   public void releaseResource(PooledResource var1) {
      boolean var14;
      boolean var10000 = var14 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var15 = null;
      DiagnosticActionState[] var16 = null;
      Object var13 = null;
      Object[] var9;
      DelegatingMonitor var10001;
      DynamicJoinPoint var29;
      if (var10000) {
         var9 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var9 = new Object[]{this, var1};
         }

         var29 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var15 = var10001.getActions();
         InstrumentationSupport.preProcess(var29, var10001, var10002, var16 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Release_Connection_Low.isEnabledAndNotDyeFiltered()) {
         var9 = null;
         if (_WLDF$INST_FLD_Connector_Release_Connection_Low.isArgumentsCaptureNeeded()) {
            var9 = new Object[]{this, var1};
         }

         var29 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Release_Connection_Low;
         InstrumentationSupport.process(var29, var10001, var10001.getActions());
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var9 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var9 = new Object[]{this, var1};
         }

         var29 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_1, var9, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var29, var10001, var10001.getActions());
      }

      try {
         if (Debug.verbose) {
            Debug.enter(this, "releaseResource()");
         }

         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Entering releaseResource( " + var1 + ")");
            this.dumpPool("on entering releaseResource");
         }

         try {
            int var2 = this.available.size();
            int var3 = this.reserved.size();

            try {
               synchronized(this) {
                  if (this.reserved.contains(var1)) {
                     if (Debug.isPoolVerboseEnabled()) {
                        this.debugVerbose("calling super.releaseResource()");
                     }

                     super.releaseResource(var1);
                     this.incrementNumReleases();
                  } else {
                     Debug.logReReleasingResource(this.poolName);
                  }
               }
            } catch (ResourceException var26) {
               ((ConnectionInfo)var1).getConnectionHandler().destroyConnection();
               if (Debug.isPoolVerboseEnabled()) {
                  this.debugVerbose("Exception/exiting releaseResource( " + var1 + ")", var26);
               }
            }

            if (Debug.isPoolVerboseEnabled()) {
               this.dumpPool("on exiting releaseResource");
            }

            this.updatePoolStats();
         } finally {
            if (Debug.isPoolVerboseEnabled()) {
               this.debugVerbose("Exiting releaseResource( " + var1 + ")");
            }

            if (Debug.verbose) {
               Debug.exit(this, "releaseResource()");
            }

         }
      } finally {
         if (var14) {
            InstrumentationSupport.postProcess(_WLDF$INST_JPFLD_1, _WLDF$INST_FLD_Connector_Around_Outbound, var15, var16);
         }

         if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
            var10001 = _WLDF$INST_FLD_Connector_After_Outbound;
            InstrumentationSupport.process(_WLDF$INST_JPFLD_1, var10001, var10001.getActions());
         }

      }

   }

   public void releaseOnTransactionCompleted(ConnectionInfo var1) {
      if (Debug.verbose) {
         Debug.enter(this, "releaseOnTransactionCompleted()");
      }

      try {
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Entering releaseOnTransactionCompleted() with ConnectionInfo = " + var1);
         }

         this.releaseResource(var1, true);
      } finally {
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Exiting releaseOnTransactionCompleted() with ConnectionInfo = " + var1);
         }

         if (Debug.verbose) {
            Debug.exit(this, "releaseOnTransactionCompleted()");
         }

      }

   }

   public void releaseOnConnectionClosed(ConnectionInfo var1) {
      if (Debug.verbose) {
         Debug.enter(this, "releaseOnConnectionClosed()");
      }

      try {
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Entering releaseOnConnectionClosed() with ConnectionInfo = " + var1);
         }

         this.releaseResource(var1, false);
      } finally {
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Exiting releaseOnConnectionClosed() with ConnectionInfo = " + var1);
         }

         if (Debug.verbose) {
            Debug.exit(this, "releaseOnConnectionClosed()");
         }

      }

   }

   public void destroyConnection(ConnectionInfo var1) {
      Debug.enter(this, "destroyConnection()");
      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose("Entering ConnectionPool.destroyConnection() with ConnectionInfo  " + var1);
         this.dumpPool("on entering destroyConnection");
      }

      try {
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Removing " + var1 + " from the reserved ");
         }

         synchronized(this) {
            ConnectionInfo var3 = null;
            if (super.reserved.remove(var1)) {
               var3 = var1;
            }

            if (var3 == null && super.available.remove(var1)) {
               ;
            }
         }

         var1.destroy();
      } finally {
         if (Debug.isPoolVerboseEnabled()) {
            this.dumpPool("on exiting destroyConnection");
            this.debugVerbose("Exiting ConnectionPool.destroyConnection() with ConnectionInfo  " + var1.toString());
         }

         if (Debug.verbose) {
            Debug.exit(this, "destroyConnection(");
         }

      }

   }

   public void dumpPool(String var1) {
      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose(" DUMP of ConnectionPool[ " + var1 + " ]");

         try {
            synchronized(this) {
               this.debugVerbose(" currCapacity = " + this.getCurrCapacity() + " maxCapacity = " + this.getMaxCapacity() + " numReserves = " + this.numReserves + " numReleases = " + this.numReleases);
               this.dumpPoolLists();
            }
         } catch (Exception var5) {
            this.debugVerbose("Exception occurred attempting to dump the connection pool", var5);
         }
      }

   }

   public SecurityContext createSecurityContext(ConnectionRequestInfo var1, boolean var2, AuthenticatedSubject var3) throws SecurityException {
      return new SecurityContext(this.initOutboundInfo, this.applicationName, this.componentName, this.poolName, this.managedConnectionFactory, var1, var2, var3);
   }

   public synchronized void incrementNumReserves() {
      ++this.numReserves;
   }

   public synchronized void incrementNumReleases() {
      ++this.numReleases;
   }

   public void suspend() throws ResourceException {
      super.suspend(false);
   }

   public void resume() throws ResourceException {
      super.resume();
   }

   protected PooledResource refreshOldestAvailResource(PooledResourceInfo var1) throws ResourceException {
      PooledResource var2 = null;
      if (super.available.size() > 0) {
         var2 = this.getOldestUnreservedResource();
         if (var2 != null) {
            super.available.remove(var2);
            var2.destroy();
            var2 = null;
            PooledResourceInfo[] var3 = new PooledResourceInfo[1];
            Arrays.fill(var3, var1);
            Vector var4 = new Vector();
            super.makeResources(1, var3, var4, false);
            if (var4.size() > 0) {
               var2 = (PooledResource)var4.firstElement();
               if (var2 != null) {
                  super.available.remove(var2);
               }

               ++this.numRecycled;
            }
         }
      }

      return var2;
   }

   protected void initParameters(Properties var1) {
      super.initParameters(var1);
      String var2;
      if ((var2 = var1.getProperty("matchConnectionsSupported")) != null) {
         super.matchSupported = Boolean.valueOf(var2);
      }

      super.returnNewlyCreatedResource = true;
   }

   protected synchronized void incrementNumMatchSuccesses() {
      ++this.numMatchSuccesses;
   }

   protected synchronized void incrementNumRequestsRejected() {
      ++this.numRequestsRejected;
   }

   protected synchronized void trackLeak(String var1) {
      ++this.numLeaks;
      if (this.getConnectionProfilingEnabled()) {
         this.leakProfiles.add(new ConnectionLeakProfile(this.poolName, var1));
      }

   }

   protected synchronized void trackIdle(String var1) {
      if (this.getConnectionProfilingEnabled()) {
         this.idleProfiles.add(new ConnectionLeakProfile(this.poolName, var1));
      }

   }

   void debugXAout(String var1) {
      if (Debug.isXAoutEnabled()) {
         Debug.xaOut("For pool '" + this.poolName + "' " + var1);
      }

   }

   void debugXAout(String var1, Throwable var2) {
      if (Debug.isXAoutEnabled()) {
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         String var4 = this.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var2, var3);
         Debug.xaOut("For pool '" + this.poolName + "' " + var1 + "\n" + var4);
      }

   }

   void debugVerbose(String var1) {
      if (Debug.isPoolVerboseEnabled()) {
         Debug.poolVerbose("For pool '" + this.poolName + "' " + var1);
      }

   }

   void debugVerbose(String var1, Throwable var2) {
      if (Debug.isPoolVerboseEnabled()) {
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         String var4 = this.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var2, var3);
         Debug.poolVerbose("For pool '" + this.poolName + "' " + var1 + ":\n" + var4);
      }

   }

   private void releaseResource(PooledResource var1, boolean var2) {
      Debug.enter(this, "releaseResource()");
      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose("on entering releaseResource() PooledResource = " + var1.toString() + " & transCompleted = " + var2);
         this.dumpPool("on entering releaseResource");
      }

      ConnectionInfo var3 = (ConnectionInfo)var1;

      try {
         if (super.state != 100) {
            if (this.connectionSharingManager.releaseSharedConnection(var3, var2)) {
               if (Debug.isPoolVerboseEnabled()) {
                  this.debugVerbose("calling releaseResource( " + var1 + " )");
               }

               this.releaseResource(var1);
            } else if (Debug.isPoolVerboseEnabled()) {
               this.debugVerbose("Not calling releaseResource( " + var1 + " )");
            }
         }
      } finally {
         if (Debug.isPoolVerboseEnabled()) {
            this.dumpPool("on exiting releaseResource");
         }

         if (Debug.verbose) {
            Debug.exit(this, "releaseResource()");
         }

      }

   }

   private void unregisterConnectionPoolRuntimeMBean() {
      try {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         SecurityServiceManager.runAs(var1, var1, new PrivilegedAction() {
            public Object run() {
               try {
                  if (ConnectionPool.this.rMBean != null) {
                     if (Debug.verbose) {
                        Debug.println(this, ".unregisterConnectionPoolRuntimeMBean() -- unregistering " + ConnectionPool.this.rMBean.getName());
                     }

                     ConnectionPool.this.rMBean.unregister();
                     ((ConnectorComponentRuntimeMBeanImpl)ConnectionPool.this.connRuntimeMbean).removeConnPoolRuntime(ConnectionPool.this.rMBean);
                  }

                  return null;
               } catch (Exception var2) {
                  Debug.logUnregisterConnRTMBeanError(ConnectionPool.this.poolName, var2.toString());
                  return null;
               }
            }
         });
      } catch (Exception var2) {
         Debug.logUnregisterCPRTMBeanError(this.poolName, var2.toString());
      }

   }

   private void dumpPoolLists() {
      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose(" DUMPING available list, #entries = " + this.available.size());
      }

      int var1 = 0;
      ListIterator var2 = this.available.listIterator(0);

      while(var2.hasNext()) {
         ConnectionInfo var3 = (ConnectionInfo)var2.next();
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose(" available list[" + var1++ + "].connectionInfo = " + var3 + ", sharingCount = " + var3.getSharingCount());
         }
      }

      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose(" DUMPING reserved list, #entries = " + this.reserved.size());
      }

      var1 = 0;
      Iterator var5 = this.reserved.iterator();

      while(var5.hasNext()) {
         ConnectionInfo var4 = (ConnectionInfo)var5.next();
         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose(" reserved list[" + var1++ + "].connectionInfo = " + var4 + ", sharingCount = " + var4.getSharingCount());
         }
      }

      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose(" DUMPING dead list, #entries = " + this.dead.size());
      }

   }

   public void setupForXARecovery() throws ResourceException {
      if (this.getTransactionSupport().equalsIgnoreCase("XATransaction")) {
         try {
            this.recoveryWrapper = RecoveryOnlyXAWrapper.initializeRecoveryOnlyXAWrapper(this);
            this.debugXAout("Registered XAResource for recovery:  " + this.recoveryWrapper);
         } catch (SystemException var3) {
            String var2 = Debug.logRegisterForXARecoveryFailed(var3.toString());
            Debug.logStackTrace(var2, var3);
            throw new ResourceException(var3.toString(), var3);
         }
      }

   }

   private void undoSetupForXARecovery() {
      if (this.getTransactionSupport().equalsIgnoreCase("XATransaction")) {
         try {
            if (this.recoveryWrapper != null) {
               this.recoveryWrapper.cleanupRecoveryOnlyXAWrapper();
            }

            this.debugXAout("Unregistered XAResource for recovery");
         } catch (SystemException var7) {
            String var2 = Debug.logUnregisterForXARecoveryFailed(var7.toString());
            Debug.logStackTrace(var2, var7);
         } finally {
            this.recoveryWrapper = null;
         }
      }

   }

   public void setProxyTestConnectionHandle(Object var1) {
      this.proxyTestConnectionHandle = var1;
   }

   void setCanUseProxy(boolean var1) {
      this.canUseProxy = var1;
   }

   private void setPoolName() {
      this.poolName = this.getKey();
      Debug.println(this, ".setNames() - poolName = " + this.poolName);
   }

   public ManagedConnectionFactory getManagedConnectionFactory() {
      return this.managedConnectionFactory;
   }

   public String getJNDIName() {
      return this.initOutboundInfo.getJndiName();
   }

   public String getResourceLink() {
      return this.initOutboundInfo.getResourceLink();
   }

   public String getKey() {
      String var1 = null;
      String var2 = this.getJNDIName();
      if (var2 != null && var2.trim().length() != 0) {
         var1 = var2;
      } else {
         var1 = this.getResourceLink();
      }

      return var1;
   }

   public String getName() {
      return this.poolName;
   }

   public ConnectionSharingManager getConnectionSharingManager() {
      return this.connectionSharingManager;
   }

   public ConnectionPoolRuntimeMBeanImpl getRuntimeMBean() {
      return this.rMBean;
   }

   public String getConnectionFactoryName() {
      return this.initOutboundInfo.getConnectionFactoryName();
   }

   public RAInstanceManager getRAInstanceManager() {
      return this.raOutboundManager.getRA();
   }

   public String getRALinkRefName() {
      return this.initOutboundInfo.getRaLinkRef();
   }

   public String getTransactionSupport() {
      return this.initOutboundInfo.getTransactionSupport();
   }

   public boolean isLoggingEnabled() {
      return this.getDynamicOutboundInfo().isLoggingEnabled();
   }

   public String getLogFileName() {
      return this.getDynamicOutboundInfo().getLogFilename();
   }

   public int getInactiveConnectionTimeoutSeconds() {
      return this.initOutboundInfo.getInactiveConnectionTimeoutSeconds();
   }

   public boolean getConnectionProfilingEnabled() {
      return this.initOutboundInfo.getConnectionProfilingEnabled();
   }

   public int getDetectedLeakCount() {
      return this.numLeaks;
   }

   public int getInitialCapacity() {
      return this.initialCapacity;
   }

   public int getCapacityIncrement() {
      return this.capacityIncrement;
   }

   public int getShrinkPeriodMinutes() {
      return this.getInactiveResourceTimeoutSeconds() / 60;
   }

   public int getInactiveResourceTimeoutSeconds() {
      return this.inactiveSecs;
   }

   public int getResourceCreationRetrySeconds() {
      return this.retryIntervalSecs;
   }

   public int getResourceReserveTimeoutSeconds() {
      return this.reserveTimeoutSecs;
   }

   public boolean isShrinkingEnabled() {
      return this.allowShrinking;
   }

   public int getShrinkFrequencySeconds() {
      return this.shrinkSecs;
   }

   public int getTestFrequencySeconds() {
      return this.testSecs;
   }

   public boolean getTestOnReserve() {
      return this.testOnReserve;
   }

   public boolean getTestOnRelease() {
      return this.testOnRelease;
   }

   public boolean getTestOnCreate() {
      return this.testOnCreate;
   }

   public PooledResource getOldestUnreservedResource() {
      Object[] var1 = null;
      var1 = super.available.toArray();
      ConnectionInfo var2 = null;
      int var3 = super.available.size();
      long var4 = -1L;
      if (var1 != null && var3 > 0) {
         for(int var7 = 0; var7 < var3; ++var7) {
            ConnectionInfo var6 = (ConnectionInfo)var1[var7];
            if (var4 == -1L || var2.getLastUsedTime() > var6.getLastUsedTime()) {
               var2 = var6;
               var4 = var6.getLastUsedTime();
            }
         }
      }

      return var2;
   }

   public int getConnectionsMatchedTotalCount() {
      return this.numMatchSuccesses;
   }

   public int getConnectionsRejectedTotalCount() {
      return this.numRequestsRejected;
   }

   public int getNumRecycled() {
      return this.numRecycled;
   }

   public int getNumLeaked() {
      return this.numLeaks;
   }

   public int getLeakProfileCount() {
      return this.leakProfiles.size();
   }

   public ConnectionLeakProfile[] getConnectionLeakProfiles() {
      return (ConnectionLeakProfile[])((ConnectionLeakProfile[])this.leakProfiles.toArray(new ConnectionLeakProfile[this.leakProfiles.size()]));
   }

   public ConnectionLeakProfile[] getConnectionLeakProfiles(int var1, int var2) {
      return this.getArray(this.leakProfiles, var1, var2);
   }

   public int getIdleProfileCount() {
      return this.idleProfiles.size();
   }

   public ConnectionLeakProfile[] getConnectionIdleProfiles() {
      return (ConnectionLeakProfile[])((ConnectionLeakProfile[])this.idleProfiles.toArray(new ConnectionLeakProfile[this.idleProfiles.size()]));
   }

   public ConnectionLeakProfile[] getConnectionIdleProfiles(int var1, int var2) {
      return this.getArray(this.idleProfiles, var1, var2);
   }

   public Object getProxyTestConnectionHandle() {
      return this.proxyTestConnectionHandle;
   }

   public OutboundInfo getOutboundInfo() {
      return this.initOutboundInfo;
   }

   public ResourceRegistrationManager getResourceRegistrationManager() {
      return this.resRegManager;
   }

   ConnectionHandler getConnectionHandler(ManagedConnection var1) {
      ConnectionHandler var2 = null;
      synchronized(this) {
         Iterator var4 = this.reserved.iterator();

         while(var4.hasNext()) {
            ConnectionHandler var5 = ((ConnectionInfo)((ConnectionInfo)var4.next())).getConnectionHandler();
            ManagedConnection var6 = var5.getManagedConnection();
            if (var6 == var1) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   boolean getCanUseProxy() {
      return this.canUseProxy;
   }

   ConnectionManagerImpl getConnMgr() {
      return this.connMgr;
   }

   Object getConnectionFactory() throws javax.resource.ResourceException, ResourceAdapterInternalException {
      if (Debug.verbose) {
         Debug.enter(this, "getConnectionFactory()");
      }

      Object var7;
      try {
         if (this.connectionFactory == null) {
            if (Debug.verbose) {
               Debug.println("Creating new connection factory using ConnectionManager = " + this.getConnMgr());
            }

            AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            Debug.showClassLoaders(this, this.managedConnectionFactory);
            this.connectionFactory = this.getRAInstanceManager().getAdapterLayer().createConnectionFactory(this.managedConnectionFactory, this.getConnMgr(), var1);
            Debug.showClassLoaders(this, this.connectionFactory);
            if (this.connectionFactory == null) {
               Debug.logCreateCFReturnedNull(this.poolName);
               String var2 = Debug.getExceptionMCFCreateCFReturnedNull();
               throw new ResourceAdapterInternalException(var2);
            }
         }

         var7 = this.connectionFactory;
      } finally {
         if (Debug.verbose) {
            Debug.exit(this, "getConnectionFactory()");
         }

      }

      return var7;
   }

   private ConnectionLeakProfile[] getArray(Vector var1, int var2, int var3) {
      Vector var4 = new Vector();

      Object var5;
      try {
         for(int var6 = 0; var6 < var3 && var2 + var6 < var1.size() && (var5 = var1.get(var2 + var6)) != null; ++var6) {
            var4.add(var5);
         }
      } catch (ArrayIndexOutOfBoundsException var7) {
      }

      return (ConnectionLeakProfile[])((ConnectionLeakProfile[])var4.toArray());
   }

   public String getDisplayName() {
      return this.initOutboundInfo.getDisplayName();
   }

   public XATxConnectionHandler findXATxConnectionHandler() {
      if (!this.getTransactionSupport().equalsIgnoreCase("XATransaction")) {
         return null;
      } else {
         XATxConnectionHandler var1 = null;
         PooledResource[] var2 = this.getResources();
         if (var2 != null && var2.length > 0) {
            ConnectionInfo var3 = (ConnectionInfo)((ConnectionInfo)var2[0]);
            var1 = (XATxConnectionHandler)var3.getConnectionHandler();
         }

         return var1;
      }
   }

   public XATxConnectionHandler reserveInternal() throws ResourceException {
      ConnectionInfo var1 = (ConnectionInfo)super.reserveResource(-1, (PooledResourceInfo)null);
      if (var1 != null) {
         XATxConnectionHandler var2 = (XATxConnectionHandler)var1.getConnectionHandler();
         return var2;
      } else {
         return null;
      }
   }

   public OutboundAdapterType getXMLBean(ConnectorDiagnosticImageSource var1) {
      OutboundAdapterType var2 = OutboundAdapterType.Factory.newInstance();
      var2.setJndiName(this.getJNDIName());
      var2.setResourceLink(this.getResourceLink());
      var2.setMaxCapacity(this.getMaxCapacity());
      var2.setConnectionsInFreePool(this.getNumAvailable());
      var2.setConnectionsInUse(this.getNumReserved());
      boolean var3 = var1 != null ? var1.timedout() : false;
      if (var3) {
         return var2;
      } else {
         ManagedConnectionType[] var4 = null;
         PooledResource[] var5 = this.getResources();
         if (var5 != null) {
            var4 = new ManagedConnectionType[var5.length];

            for(int var6 = 0; var6 < var5.length; ++var6) {
               ConnectionInfo var7 = (ConnectionInfo)((ConnectionInfo)var5[var6]);
               var4[var6] = var7.getXMLBean(var1);
            }
         }

         var2.setManagedConnectionArray(var4);
         return var2;
      }
   }

   public synchronized void incrementCloseCount() {
      ++this.closeCount;
   }

   public long getCloseCount() {
      return this.closeCount;
   }

   public synchronized void updatePoolStats() {
      this.updateFreePoolStats();
      this.updatePoolSizeStats();
   }

   public synchronized void updateFreePoolStats() {
      long var1 = (long)this.getNumAvailable();
      if (var1 < this.freePoolSizeLowWaterMark) {
         this.freePoolSizeLowWaterMark = var1;
      } else if (var1 > this.freePoolSizeHighWaterMark) {
         this.freePoolSizeHighWaterMark = var1;
      }

   }

   public long getFreePoolSizeHighWaterMark() {
      return this.freePoolSizeHighWaterMark;
   }

   public long getFreePoolSizeLowWaterMark() {
      return this.freePoolSizeLowWaterMark;
   }

   public synchronized void updatePoolSizeStats() {
      long var1 = (long)this.getCurrCapacity();
      if (var1 < this.freePoolSizeLowWaterMark) {
         this.poolSizeLowWaterMark = var1;
      } else if (var1 > this.poolSizeHighWaterMark) {
         this.poolSizeHighWaterMark = var1;
      }

   }

   public long getPoolSizeHighWaterMark() {
      return this.poolSizeHighWaterMark;
   }

   public long getPoolSizeLowWaterMark() {
      return this.poolSizeLowWaterMark;
   }

   public String getManagedConnectionFactoryClassName() {
      return this.initOutboundInfo.getMCFClass();
   }

   public String getConnectionFactoryClassName() {
      return this.initOutboundInfo.getCFImpl();
   }

   public boolean isTestable() {
      return this.getManagedConnectionFactory() instanceof ValidatingManagedConnectionFactory;
   }

   public boolean isProxyOn() {
      return this.canUseProxy;
   }

   public synchronized void incrementConnectionsDestroyedByErrorCount() {
      ++this.connectionsDestroyedByErrorCount;
   }

   public int getConnectionsDestroyedByErrorCount() {
      return this.connectionsDestroyedByErrorCount;
   }

   public boolean testPool() {
      boolean var1 = true;
      synchronized(this) {
         ConnectionInfo var4 = null;
         Object[] var3 = super.available.toArray();
         int var5 = super.available.size();
         if (var3 != null && var5 > 0) {
            for(int var6 = 0; var6 < var5; ++var6) {
               var4 = (ConnectionInfo)var3[var6];

               try {
                  var4.test();
                  if (var4.hasError()) {
                     var1 = false;
                  }
               } catch (Exception var9) {
                  var1 = false;
               }
            }
         }

         return var1;
      }
   }

   public void applyPoolParamChanges(Properties var1) {
      if (var1 != null && var1.size() > 0) {
         Enumeration var2 = var1.propertyNames();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            String var4 = var1.getProperty(var3);

            try {
               this.applyPoolParamChange(var3, var4);
            } catch (ResourceException var7) {
               String var6 = Debug.logFailedToApplyPoolChanges(var7.toString());
               Debug.logStackTrace(var6, var7);
            }
         }
      }

   }

   public void applyPoolParamChange(String var1, String var2) throws ResourceException {
      if (var1.equalsIgnoreCase("initialCapacity")) {
         this.setInitialCapacity(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("maxCapacity")) {
         this.setMaximumCapacity(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("capacityIncrement")) {
         this.setCapacityIncrement(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("shrinkFrequencySeconds")) {
         this.setShrinkFrequencySeconds(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("inactiveResTimeoutSeconds")) {
         this.setInactiveResourceTimeoutSeconds(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("maxWaiters")) {
         this.setHighestNumWaiters(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("maxUnavl")) {
         this.setHighestNumUnavailable(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("resCreationRetrySeconds")) {
         this.setResourceCreationRetrySeconds(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("resvTimeoutSeconds")) {
         this.setResourceReserveTimeoutSeconds(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("testFrequencySeconds")) {
         this.setTestFrequencySeconds(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("harvestFreqSecsonds")) {
         this.setProfileHarvestFrequencySeconds(Integer.valueOf(var2));
      } else if (var1.equalsIgnoreCase("shrinkEnabled")) {
         this.setShrinkEnabled(Boolean.valueOf(var2));
      } else if (var1.equalsIgnoreCase("testOnCreate")) {
         this.setTestOnCreate(Boolean.valueOf(var2));
      } else if (var1.equalsIgnoreCase("testOnRelease")) {
         this.setTestOnRelease(Boolean.valueOf(var2));
      } else if (var1.equalsIgnoreCase("testOnReserve")) {
         this.setTestOnReserve(Boolean.valueOf(var2));
      }

   }

   public void applyLoggingChanges(Properties var1, OutboundInfo var2) {
      if (var1 != null && var1.size() > 0) {
         this.pendingOutboundInfo = var2;
         this.setLogger();
      }

   }

   protected void setLogger() {
      OutboundInfo var1 = this.getDynamicOutboundInfo();

      try {
         if (Debug.isRALifecycleEnabled()) {
            Debug.raLifecycle("Creating logfile '" + var1.getLogFilename() + "' for ManagedConnectionFactory '" + var1.getMCFClass() + "' of RA module '" + var1.getRAInfo().getModuleName() + "'");
         }

         Object var2 = null;
         if (var1.isLoggingEnabled()) {
            this.setLoggingBeanAdapter((LoggingBeanAdapter)null);
            var2 = null;
         }

         if (var1.getLogFilename() != null && var1.getLogFilename().length() != 0) {
            this.setLoggingBeanAdapter(this.createLoggingBeanAdapter(var1));
            var2 = this.loggingBeanAdapter.getOutputStream();
         } else {
            this.setLoggingBeanAdapter((LoggingBeanAdapter)null);
            var2 = new LoggingOutputStream(this.getKey(), WLLevel.TRACE);
         }

         AuthenticatedSubject var8 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         PrintWriter var9;
         if (var2 != null) {
            var9 = new PrintWriter((OutputStream)var2, true);
         } else {
            var9 = null;
         }

         this.getRAInstanceManager().getAdapterLayer().setLogWriter(this.managedConnectionFactory, var9, var8);
      } catch (javax.resource.ResourceException var5) {
         Throwable var7 = var5.getCause();
         String var4 = Debug.logSetLogWriterErrorWithCause(this.initOutboundInfo.getMCFClass(), var5.toString(), var7 != null && var7 != var5 ? var7.toString() : "");
         Debug.logStackTrace(var4, var5);
         if (var7 != null && var7 != var5) {
            Debug.logStackTrace(var4, var7);
         }
      } catch (Throwable var6) {
         String var3 = Debug.logSetLogWriterError(var1.getMCFClass());
         Debug.logStackTrace(var3, var6);
      }

   }

   private void setLoggingBeanAdapter(LoggingBeanAdapter var1) {
      if (this.loggingBeanAdapter != null) {
         OutputStream var2 = this.loggingBeanAdapter.getOutputStream();

         try {
            var2.flush();
            var2.close();
         } catch (IOException var5) {
            Debug.logFailedToCloseLog(this.getKey(), var5.toString());
            Debug.println("WARNING:  Failed to flush and close the logging OutputStream for pool:  " + this.getKey() + ":  " + var5);
         }
      }

      this.loggingBeanAdapter = var1;
      if (this.loggingBeanAdapter != null) {
         try {
            RotatingFileOutputStream var6 = new RotatingFileOutputStream(LogFileConfigUtil.getLogFileConfig(this.loggingBeanAdapter));
            this.loggingBeanAdapter.setOutputStream(var6);
         } catch (IOException var4) {
            Debug.logFailedToCreateLogStream(this.getKey(), var4.toString());
            Debug.println("Failed to create the logging OutputStream for pool:  " + this.getKey() + ":  " + var4);
         }
      }

   }

   public Boolean getUseConnectionProxies() {
      return this.initOutboundInfo.getUseConnectionProxies();
   }

   public synchronized int getAlternateCount() {
      return ++this.alternateCount;
   }

   public ResourcePoolProfiler getProfiler() {
      return this.profiler;
   }

   private LoggingBeanAdapter createLoggingBeanAdapter(OutboundInfo var1) {
      Debug.enter(this, ".createLoggingBeanAdapter()");
      LoggingBeanAdapter var2 = new LoggingBeanAdapter(var1.getLoggingBean());
      var2.setFileCount(var1.getFileCount());
      var2.setFileMinSize(var1.getFileSizeLimit());
      var2.setFileTimeSpan(var1.getFileTimeSpan());
      var2.setLogFileRotationDir(var1.getLogFileRotationDir());
      var2.setFileName(var1.getLogFilename());
      var2.setNumberOfFilesLimited(var1.isNumberOfFilesLimited());
      var2.setRotateLogOnStartup(var1.isRotateLogOnStartup());
      var2.setRotationTime(var1.getRotationTime());
      var2.setRotationType(var1.getRotationType());

      try {
         this.logRuntime = new LogRuntime(var2, this.rMBean);
      } catch (ManagementException var8) {
      } finally {
         Debug.exit(this, ".createLoggingBeanAdapter()");
      }

      return var2;
   }

   public void forceLogRotation() throws ManagementException {
      if (this.loggingBeanAdapter == null) {
         String var1 = Debug.getFailedToForceLogRotation(this.getKey());
         throw new ManagementException(var1);
      } else {
         this.logRuntime.forceLogRotation();
      }
   }

   public void ensureLogOpened() throws ManagementException {
      if (this.loggingBeanAdapter != null) {
         this.logRuntime.ensureLogOpened();
      }

   }

   public LogRuntimeMBean getLogRuntime() {
      return this.logRuntime;
   }

   public void setupRuntime(ConnectorComponentRuntimeMBeanImpl var1, RAOutboundManager var2) {
      try {
         this.connRuntimeMbean = var1;
         this.rMBean = new ConnectionPoolRuntimeMBeanImpl(this.applicationName, this.componentName, this, var1, var2);
         var1.addConnPoolRuntime(this.rMBean);
      } catch (Exception var5) {
         String var4 = Debug.logInitCPRTMBeanError(this.poolName, var5.toString());
         Debug.logStackTrace(var4, var5);
      }

   }

   private OutboundInfo getDynamicOutboundInfo() {
      return this.pendingOutboundInfo != null ? this.pendingOutboundInfo : this.initOutboundInfo;
   }

   public boolean isShutdown() {
      return this.shutdown;
   }

   public String toString() {
      return super.toString() + "-" + this.poolName;
   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Release_Connection_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Release_Connection_Low");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_FLD_Connector_Reserve_Connection_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Reserve_Connection_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ConnectionPool.java", "weblogic.connector.outbound.ConnectionPool", "reserveResource", "(ILweblogic/common/resourcepool/PooledResourceInfo;)Lweblogic/common/resourcepool/PooledResource;", 573, InstrumentationSupport.makeMap(new String[]{"Connector_After_Outbound", "Connector_Reserve_Connection_Low", "Connector_Around_Outbound", "Connector_Before_Outbound"}, new PointcutHandlingInfo[]{null, InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionPoolRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null), null, null}), (boolean)0);
      _WLDF$INST_JPFLD_1 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ConnectionPool.java", "weblogic.connector.outbound.ConnectionPool", "releaseResource", "(Lweblogic/common/resourcepool/PooledResource;)V", 684, InstrumentationSupport.makeMap(new String[]{"Connector_After_Outbound", "Connector_Around_Outbound", "Connector_Release_Connection_Low", "Connector_Before_Outbound"}, new PointcutHandlingInfo[]{null, null, InstrumentationSupport.createPointcutHandlingInfo(InstrumentationSupport.createValueHandlingInfo("pool", "weblogic.diagnostics.instrumentation.gathering.JCAConnectionPoolRenderer", false, true), (ValueHandlingInfo)null, (ValueHandlingInfo[])null), null}), (boolean)0);
   }
}
