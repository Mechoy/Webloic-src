package weblogic.connector.outbound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Map;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ResourceAdapterInternalException;
import weblogic.common.ResourceException;
import weblogic.common.resourcepool.PooledResource;
import weblogic.common.resourcepool.PooledResourceFactory;
import weblogic.common.resourcepool.PooledResourceInfo;
import weblogic.connector.common.Debug;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.jms.bridge.TemporaryResourceException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConnectionFactory implements PooledResourceFactory {
   private ConnectionPool connectionPool;
   private boolean connectionProxyChecked;
   static final long serialVersionUID = 3702342262897055916L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.connector.outbound.ConnectionFactory");
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_After_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Around_Outbound;
   public static final DelegatingMonitor _WLDF$INST_FLD_Connector_Before_Outbound;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public ConnectionFactory(ConnectionPool var1) throws ResourceException {
      this.connectionPool = var1;
      this.connectionProxyChecked = false;
   }

   public PooledResource createResource(PooledResourceInfo var1) throws ResourceException {
      boolean var22;
      boolean var10000 = var22 = _WLDF$INST_FLD_Connector_Around_Outbound.isEnabledAndNotDyeFiltered();
      DiagnosticAction[] var23 = null;
      DiagnosticActionState[] var24 = null;
      Object var21 = null;
      Object[] var17;
      DelegatingMonitor var10001;
      DynamicJoinPoint var48;
      if (var10000) {
         var17 = null;
         if (_WLDF$INST_FLD_Connector_Around_Outbound.isArgumentsCaptureNeeded()) {
            var17 = InstrumentationSupport.toSensitive(2);
         }

         var48 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var17, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Around_Outbound;
         DiagnosticAction[] var10002 = var23 = var10001.getActions();
         InstrumentationSupport.preProcess(var48, var10001, var10002, var24 = InstrumentationSupport.getActionStates(var10002));
      }

      if (_WLDF$INST_FLD_Connector_Before_Outbound.isEnabledAndNotDyeFiltered()) {
         var17 = null;
         if (_WLDF$INST_FLD_Connector_Before_Outbound.isArgumentsCaptureNeeded()) {
            var17 = InstrumentationSupport.toSensitive(2);
         }

         var48 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, var17, (Object)null);
         var10001 = _WLDF$INST_FLD_Connector_Before_Outbound;
         InstrumentationSupport.process(var48, var10001, var10001.getActions());
      }

      boolean var31 = false;

      ConnectionInfo var18;
      ConnectionInfo var49;
      DynamicJoinPoint var50;
      DelegatingMonitor var51;
      try {
         var31 = true;
         Debug.enter(this, "createResource(...)");
         SecurityContext var2 = null;
         String var3 = this.connectionPool.getTransactionSupport();
         ManagedConnection var4 = null;
         ConnectionInfo var5 = null;
         long var6 = 0L;
         AuthenticatedSubject var8 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         String var10;
         String var12;
         String var45;
         try {
            Debug.println("Create the Security Context");
            if (var1 == null) {
               var2 = this.connectionPool.createSecurityContext((ConnectionRequestInfo)null, true, var8);
            } else {
               var2 = ((ConnectionReqInfo)var1).getSecurityContext();
            }

            Debug.println("Create the managed connection");
            long var9 = System.currentTimeMillis();
            var4 = this.connectionPool.getRAInstanceManager().getAdapterLayer().createManagedConnection(this.connectionPool.getManagedConnectionFactory(), var2.getSubject(), var2.getClientInfo(), var8);
            long var47 = System.currentTimeMillis();
            var6 = var47 - var9;
         } catch (TemporaryResourceException var40) {
            var10 = this.connectionPool.getRAInstanceManager().getAdapterLayer().toString(var40, var8);
            if (Debug.isPoolingEnabled()) {
               var45 = Debug.logCreateManagedConnectionException(this.connectionPool.getName(), var10);
               Debug.logStackTrace(var45, var40);
            }

            var45 = Debug.getExceptionCreateMCFailed(var10);
            throw new ResourceException(var45, var40);
         } catch (javax.resource.ResourceException var41) {
            var10 = this.connectionPool.getRAInstanceManager().getAdapterLayer().toString(var41, var8);
            var45 = Debug.logCreateManagedConnectionException(this.connectionPool.getName(), var10);
            if (Debug.isPoolingEnabled()) {
               Debug.logStackTrace(var45, var41);
            }

            var12 = Debug.getExceptionCreateMCFailed(var41.toString());
            throw new ResourceException(var12, var41);
         } catch (Throwable var42) {
            var10 = Debug.getExceptionCreateMCFailed(var42.toString());
            ResourceAdapterInternalException var11 = new ResourceAdapterInternalException(var10, var42);
            var12 = this.connectionPool.getRAInstanceManager().getAdapterLayer().toString(var11, var8);
            String var13 = Debug.logCreateManagedConnectionException(this.connectionPool.getName(), var12);
            if (Debug.isConnectionsEnabled()) {
               Debug.logStackTrace(var13, var11);
            }

            throw new ResourceException(var10, var11);
         }

         if (var4 == null) {
            Debug.logCreateManagedConnectionError(this.connectionPool.getName());
            String var44 = Debug.getExceptionMCFCreateManagedConnectionReturnedNull();
            ResourceAdapterInternalException var46 = new ResourceAdapterInternalException(var44);
            throw new ResourceException(var44, var46);
         }

         try {
            var5 = ConnectionInfo.createConnectionInfo(this.connectionPool, var3, var4, var2);
            var5.setCreationDurationTime(var6);
         } catch (javax.resource.ResourceException var39) {
            this.cleanupManagedConnectionAfterFailure(var4);
            var10 = Debug.getExceptionFailedMCSetup();
            throw new ResourceException(var10, var39);
         }

         if (var4 != null && !this.connectionProxyChecked) {
            try {
               if (this.connectionPool.getUseConnectionProxies() == null) {
                  this.testConnectionProxyViability(var4, var2);
               } else {
                  this.connectionPool.setCanUseProxy(this.connectionPool.getUseConnectionProxies());
                  if (Debug.isPoolingEnabled()) {
                     Debug.pooling("For pool '" + this.connectionPool.getName() + "', the user has specified that use-connection-proxies is '" + this.connectionPool.getUseConnectionProxies() + "'");
                  }
               }
            } finally {
               this.connectionProxyChecked = true;
               Debug.exit(this, "createResource(...)");
            }
         }

         var49 = var5;
         var31 = false;
      } finally {
         if (var31) {
            var18 = null;
            if (var22) {
               InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var18), _WLDF$INST_FLD_Connector_Around_Outbound, var23, var24);
            }

            if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
               var50 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var18);
               var51 = _WLDF$INST_FLD_Connector_After_Outbound;
               InstrumentationSupport.process(var50, var51, var51.getActions());
            }

         }
      }

      var18 = var49;
      if (var22) {
         InstrumentationSupport.postProcess(InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var18), _WLDF$INST_FLD_Connector_Around_Outbound, var23, var24);
      }

      if (_WLDF$INST_FLD_Connector_After_Outbound.isEnabledAndNotDyeFiltered()) {
         var50 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var18);
         var51 = _WLDF$INST_FLD_Connector_After_Outbound;
         InstrumentationSupport.process(var50, var51, var51.getActions());
      }

      return var49;
   }

   public void refreshResource(PooledResource var1) throws ResourceException {
   }

   private void cleanupManagedConnectionAfterFailure(ManagedConnection var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         this.connectionPool.getRAInstanceManager().getAdapterLayer().destroy(var1, var2);
      } catch (javax.resource.ResourceException var6) {
         if (Debug.isConnectionsEnabled()) {
            Debug.connections("WARNING: For pool '" + this.connectionPool.getName() + "':  Failed to destroy managed connection after createConnectionInfo failed:  " + this.connectionPool.getRAInstanceManager().getAdapterLayer().toString(var6, var2));
            Throwable var4 = this.connectionPool.getRAInstanceManager().getAdapterLayer().getCause(var6, var2);
            if (var4 != null) {
               String var5 = this.connectionPool.getRAInstanceManager().getAdapterLayer().toString(var4, var2);
               Debug.connections("LinkedException:  " + var5);
            }
         }
      }

   }

   private synchronized void testConnectionProxyViability(ManagedConnection var1, SecurityContext var2) {
      if (!this.connectionProxyChecked) {
         String var3 = this.connectionPool.getKey();
         Debug.logProxyTestStarted(var3);
         Object var4 = null;
         Object var5 = null;
         ConnectionManagerImpl var6 = this.connectionPool.getConnMgr();
         var6.setTestingProxy(true);
         var6.setTestingProxyThread(Thread.currentThread());
         var6.setMgdConnForTest(var1);
         var6.setSecurityContext(var2);

         try {
            var4 = this.connectionPool.getConnectionFactory();
            Class var7 = var4.getClass();
            Method var23 = var7.getMethod("getConnection", (Class[])null);
            if (var23 != null) {
               AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               Object var10 = this.connectionPool.getRAInstanceManager().getAdapterLayer().invoke(var23, var4, (Object[])null, var9);
               this.closeProxyTestConnection(var10);
            }
         } catch (IllegalAccessException var18) {
            var5 = var18;
         } catch (IllegalArgumentException var19) {
            var5 = var19;
         } catch (InvocationTargetException var20) {
            var5 = var20;
         } catch (Throwable var21) {
            Throwable var8 = var21.getCause();
            var5 = var8 != null ? var8 : var21;
            if (var8 instanceof ClassCastException) {
               this.closeProxyTestConnection(this.connectionPool.getProxyTestConnectionHandle());
            }
         } finally {
            var6.setTestingProxy(false);
            var6.setMgdConnForTest((ManagedConnection)null);
            var6.setTestingProxyThread((Thread)null);
            this.connectionProxyChecked = true;
            this.connectionPool.setCanUseProxy(var5 == null);
            if (var5 == null) {
               Debug.logProxyTestSuccess(var3);
            } else {
               this.logProxyTestFailure(var3, (Throwable)var5);
            }

         }
      }

   }

   private void logProxyTestFailure(String var1, Throwable var2) {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      String var4 = this.connectionPool.getRAInstanceManager().getAdapterLayer().toString(var2, var3);
      if (Debug.isConnectionsEnabled()) {
         Debug.logProxyTestError(var1, var2);
      } else {
         Debug.logProxyTestFailureInfo(var1, var4);
      }

   }

   private void closeProxyTestConnection(Object var1) {
      Class var2 = var1.getClass();
      AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         Method var3 = var2.getMethod("close", (Class[])null);
         this.connectionPool.getRAInstanceManager().getAdapterLayer().invoke(var3, var1, (Object[])null, var4);
      } catch (NoSuchMethodException var11) {
         Debug.logCloseNotFoundOnHandle(this.connectionPool.getName());
      } catch (Exception var12) {
         this.logProxyTestFailure(this.connectionPool.getKey(), var12);
      } finally {
         this.connectionPool.setProxyTestConnectionHandle((Object)null);
      }

   }

   static {
      _WLDF$INST_FLD_Connector_After_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_After_Outbound");
      _WLDF$INST_FLD_Connector_Around_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Around_Outbound");
      _WLDF$INST_FLD_Connector_Before_Outbound = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Connector_Before_Outbound");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "ConnectionFactory.java", "weblogic.connector.outbound.ConnectionFactory", "createResource", "(Lweblogic/common/resourcepool/PooledResourceInfo;)Lweblogic/common/resourcepool/PooledResource;", 60, (Map)null, (boolean)0);
   }
}
