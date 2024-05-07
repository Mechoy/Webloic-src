package weblogic.connector.outbound;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.security.AccessController;
import javax.resource.ResourceException;
import javax.resource.spi.ApplicationServerInternalException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RACollectionManager;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.connector.transaction.outbound.TxConnectionHandler;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ConnectionManagerImpl implements ConnectionManagerRemote {
   private static final long serialVersionUID = 7744058501606837252L;
   private transient boolean testingProxy;
   private transient Thread testingProxyThread;
   private String key;
   private String raJndi;
   private transient ManagedConnection mgdConnForTest;
   private transient SecurityContext secCtx;
   private transient ConnectionPool connPool;

   public ConnectionManagerImpl(ConnectionPool var1) {
      Debug.println("Constructing the ConnectionManagerImpl : " + var1);
      this.connPool = var1;
      this.testingProxy = false;
      this.key = var1.getKey();
      this.raJndi = var1.getRAInstanceManager().getJndiName();
      this.mgdConnForTest = null;
   }

   public Object allocateConnection(ManagedConnectionFactory var1, ConnectionRequestInfo var2) throws ResourceException {
      this.checkIfPoolIsValid("allocateConnection(...)");
      if (this.testingProxy && Thread.currentThread().equals(this.testingProxyThread)) {
         return this.testAllocateConnection(var2);
      } else {
         if (Debug.getVerbose(this)) {
            AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            Debug.println(this, ".allocateConnection() called with cxReqInfo = " + this.connPool.getRAInstanceManager().getAdapterLayer().toString(var2, var3));
         }

         return this.getConnection(var2);
      }
   }

   public void lazyEnlist(ManagedConnection var1) throws ResourceException {
      Debug.enter(this, "lazyEnlist(...)");
      this.checkIfPoolIsValid("lazyEnlist(...)");

      try {
         if (var1 == null) {
            Debug.logLazyEnlistNullMC();
            Debug.println(this, ".lazyEnlist() was passed a null Managed Connection");
            String var7 = Debug.getLazyEnlistNullMC(this.connPool.getKey());
            throw new ResourceException(var7);
         }

         ConnectionHandler var2 = this.connPool.getConnectionHandler(var1);
         if (var2 instanceof TxConnectionHandler) {
            ((TxConnectionHandler)var2).enListResource();
         }
      } finally {
         Debug.exit(this, "lazyEnlist(...)");
      }

   }

   public void associateConnection(Object var1, ManagedConnectionFactory var2, ConnectionRequestInfo var3) throws ResourceException {
      try {
         Debug.enter(this, "associateConnection(...)");
         this.checkIfPoolIsValid("associateConnection(...)");
         AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         SecurityContext var5 = this.connPool.createSecurityContext(var3, false, var4);
         ConnectionInfo var6 = this.getConnectionInfo(var3, var5);
         var6.associateConnectionHandle(var1);
      } finally {
         Debug.exit(this, "associateConnection(...)");
      }

   }

   public Object testAllocateConnection(ConnectionRequestInfo var1) throws ResourceException {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (Debug.getVerbose(this)) {
         Debug.println(this, ".testAllocateConnection() called for pool '" + this.connPool.getName() + "' with cxReqInfo=" + this.connPool.getRAInstanceManager().getAdapterLayer().toString(var1, var2));
      }

      Object var3 = this.mgdConnForTest.getConnection(this.secCtx.getSubject(), this.secCtx.getClientInfo());
      this.connPool.setProxyTestConnectionHandle(var3);
      return ConnectionWrapper.createProxyTestConnectionWrapper(this.connPool, var3);
   }

   void setTestingProxy(boolean var1) {
      this.testingProxy = var1;
   }

   void setMgdConnForTest(ManagedConnection var1) {
      this.mgdConnForTest = var1;
   }

   void setSecurityContext(SecurityContext var1) {
      this.secCtx = var1;
   }

   void setTestingProxyThread(Thread var1) {
      this.testingProxyThread = var1;
   }

   boolean isProxyBeingTested() {
      return this.testingProxy;
   }

   private Object getConnection(ConnectionRequestInfo var1) throws ResourceException {
      Debug.enter(this, "getConnection(...)");
      ConnectionInfo var2 = null;
      Object var3 = null;
      AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         SecurityContext var5 = this.connPool.createSecurityContext(var1, false, var4);
         var2 = this.getConnectionInfo(var1, var5);
         var3 = var2.createConnectionHandle(var5);
      } catch (Throwable var10) {
         if (Debug.isPoolingEnabled()) {
            Debug.pooling().debug("Connection pool " + this.key + ":" + this + ": getConnection() failed with exception: " + var10, var10);
         }

         if (var10 instanceof ResourceException) {
            throw (ResourceException)var10;
         }

         if (var10 instanceof RuntimeException) {
            throw (RuntimeException)var10;
         }

         if (var10 instanceof Error) {
            throw (Error)var10;
         }

         throw new ResourceException("Connection pool " + this + ": getConnection() failed with exception: " + var10, var10);
      } finally {
         if (var3 == null && var2 != null) {
            this.connPool.releaseResource(var2);
         }

         Debug.exit(this, "getConnection(...)");
      }

      return var3;
   }

   private ConnectionInfo getConnectionInfo(ConnectionRequestInfo var1, SecurityContext var2) throws ResourceException {
      Debug.enter(this, "getConnectionInfo(...)");
      ConnectionInfo var3 = null;

      try {
         ConnectionReqInfo var4 = new ConnectionReqInfo(var1, var2);

         try {
            Debug.println(this, ".getConnectionInfo() Check if access is allowed");
            if (!var2.isAccessAllowed()) {
               String var5 = Debug.getExceptionRAAccessDenied(this.connPool.getKey());
               throw new ApplicationServerInternalException(var5);
            }

            var4.setShareable(var2.isShareable());
            Debug.println(this, ".getConnectionInfo() Reserve connection from the pool");
            var3 = (ConnectionInfo)this.connPool.reserveResource(var4);
         } catch (SecurityException var13) {
            throw var13;
         } catch (weblogic.common.ResourceException var14) {
            Throwable var6 = var14.getNested();
            if (var6 != null && var6 instanceof weblogic.common.ResourceException) {
               var6 = ((weblogic.common.ResourceException)var6).getNested();
            }

            if (var6 != null && var6 instanceof ResourceException) {
               throw (ResourceException)var6;
            }

            String var7 = Debug.getExceptionGetConnectionFailed(this.connPool.getKey(), var14.toString());
            throw new ApplicationServerInternalException(var7, var14);
         }

         if (var2.isShareable()) {
            var3.pushConnection();
         }
      } finally {
         Debug.exit(this, "getConnectionInfo(...)");
      }

      return var3;
   }

   private void checkIfPoolIsValid(String var1) throws IllegalStateException {
      if (this.connPool.isShutdown()) {
         throw new IllegalStateException(ConnectorLogger.getExceptionAllocateConnectionOnStaleConnectionFactory(this.connPool.getKey(), var1));
      }
   }

   private Object readResolve() throws ObjectStreamException {
      ConnectionPool var1 = null;
      RAInstanceManager var2 = RACollectionManager.getRAInstanceManager(this.raJndi);
      if (var2 != null) {
         var1 = var2.getRAOutboundManager().getConnectionPool(this.key);
      }

      if (var1 == null) {
         throw new InvalidObjectException(ConnectorLogger.getExceptionDeserializeConnectionManager());
      } else {
         return var1.getConnMgr();
      }
   }

   public String toString() {
      return super.toString() + "-" + this.key;
   }
}
