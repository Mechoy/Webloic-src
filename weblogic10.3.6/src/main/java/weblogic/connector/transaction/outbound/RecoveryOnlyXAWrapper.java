package weblogic.connector.transaction.outbound;

import java.security.AccessController;
import java.util.Hashtable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.Utils;
import weblogic.connector.exception.NoEnlistXAResourceException;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;

public final class RecoveryOnlyXAWrapper extends XAWrapper implements ConnectionEventListener {
   private ManagedConnection mc = null;

   public static RecoveryOnlyXAWrapper initializeRecoveryOnlyXAWrapper(ConnectionPool var0) throws SystemException {
      RecoveryOnlyXAWrapper var1 = new RecoveryOnlyXAWrapper(var0);
      Hashtable var2 = new Hashtable();
      var2.put("weblogic.transaction.registration.type", "standard");
      var2.put("weblogic.transaction.registration.settransactiontimeout", "true");
      ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).registerResource(var0.getName(), var1, var2);
      return var1;
   }

   public void cleanupRecoveryOnlyXAWrapper() throws SystemException {
      try {
         ((TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager()).unregisterResource(this.pool.getName(), true);
      } finally {
         this.destroy();
      }

   }

   private RecoveryOnlyXAWrapper(ConnectionPool var1) {
      super((XAResource)null, (XATxConnectionHandler)null, var1);
   }

   public boolean isSameRM(XAResource var1) throws XAException {
      Utils.startManagement();

      boolean var5;
      try {
         this.debug(" - isSameRM request");
         boolean var2 = this == var1;
         boolean var3 = var1 instanceof XAWrapper && ((XAWrapper)var1).getConnectionPool().getName().equals(this.pool.getName());
         boolean var4 = var2 || var3;
         this.debug(" - isSameRM returning " + var4);
         var5 = var4;
      } finally {
         Utils.stopManagement();
      }

      return var5;
   }

   public void connectionClosed(ConnectionEvent var1) {
      this.debug(" - connectionClosed event on RecoveryOnlyXAWrapper");
      Debug.logInvalidRecoveryEvent("connectionClosed");
      if (this.mc != null) {
         try {
            this.mc.cleanup();
         } catch (Throwable var4) {
            this.debug(" - exception while trying to call cleanup on  ManagedConnection for RecoveryOnlyXAWrapper, " + var4);
            String var3 = Debug.logCleanupFailure(var4.toString());
            Debug.logStackTrace(var3, var4);
         }
      }

   }

   public void connectionErrorOccurred(ConnectionEvent var1) {
      this.debug(" - connectionErrorOccurred event on RecoveryOnlyXAWrapper", var1.getException());
      String var2 = var1.getException() != null ? var1.getException().toString() : "";
      String var3 = Debug.logConnectionError(var2);
      if (var1.getException() != null) {
         Debug.logStackTrace(var3, var1.getException());
      }

      this.destroy();
   }

   public void localTransactionStarted(ConnectionEvent var1) {
      this.debug(" - localTransactionStarted");
      Debug.logInvalidRecoveryEvent("localTransactionStarted");
   }

   public void localTransactionRolledback(ConnectionEvent var1) {
      this.debug(" - localTransactionRolledback");
      Debug.logInvalidRecoveryEvent("localTransactionRolledback");
   }

   public void localTransactionCommitted(ConnectionEvent var1) {
      this.debug(" - localTransactionCommitted");
      Debug.logInvalidRecoveryEvent("localTransactionCommitted");
   }

   void destroy() {
      this.debug(" - destroy called on RecoveryOnlyXAWrapper");
      if (this.mc != null) {
         try {
            if (Debug.isXAoutEnabled()) {
               AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               this.debug(" - destroying ManagedConnection for RecoveryOnlyXAWrapper:  " + this.getRAiM().getAdapterLayer().toString(this.mc, var1));
            }

            this.mc.destroy();
         } catch (Throwable var7) {
            this.debug(" - exception while trying to call destroy on  ManagedConnection for RecoveryOnlyXAWrapper, " + var7);
            String var2 = Debug.logDestroyFailed(var7.toString());
            Debug.logStackTrace(var2, var7);
         } finally {
            this.xares = null;
            this.mc = null;
         }
      }

   }

   private XAResource getRecoveryXAResource() throws ResourceException {
      AuthenticatedSubject var2;
      if (this.mc == null) {
         this.mc = this.getRecoveryManagedConnection();
         if (Debug.isXAoutEnabled()) {
            var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            this.debug(" - ManagedConnection for recovery:  " + this.getRAiM().getAdapterLayer().toString(this.mc, var2));
         }
      }

      XAResource var1;
      String var3;
      try {
         var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         var1 = this.getRAiM().getAdapterLayer().getXAResource(this.mc, var2);
         if (var1 == null) {
            Debug.logNullXAResource();
            var3 = Debug.getExceptionMCGetXAResourceReturnedNull();
            throw new ResourceException(var3);
         }
      } catch (NoEnlistXAResourceException var4) {
         this.debug(" - ManagedConnection.getXAResource() threw NoEnlistXAResourceException, recovery resource will not be created or enlisted for this connection pool");
         var1 = null;
         this.enlistableXARes = false;
      } catch (ResourceException var5) {
         throw var5;
      } catch (Throwable var6) {
         var3 = Debug.getExceptionMCGetXAResourceThrewNonResourceException(var6.toString());
         throw new ResourceException(var3, var6);
      }

      this.debug(" - XAResource for recovery:  " + var1);
      return var1;
   }

   private ManagedConnection getRecoveryManagedConnection() throws ResourceException {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      try {
         SecurityContext var2 = this.pool.createSecurityContext((ConnectionRequestInfo)null, true, var1);
         Subject var9 = var2.getSubject();
         ManagedConnectionFactory var4 = this.pool.getManagedConnectionFactory();
         ManagedConnection var5 = var4.createManagedConnection(var9, (ConnectionRequestInfo)null);
         if (var5 == null) {
            String var6 = Debug.getExceptionMCFCreateManagedConnectionReturnedNull();
            throw new ResourceException(var6);
         } else {
            var5.addConnectionEventListener(this);
            return var5;
         }
      } catch (ResourceException var7) {
         throw var7;
      } catch (Throwable var8) {
         String var3 = Debug.getExceptionInitializeForRecoveryFailed(var8.toString());
         throw new ResourceException(var3, var8);
      }
   }

   protected void validate(String var1) throws XAException {
      if (this.xares == null && this.enlistableXARes) {
         try {
            this.xares = this.getRecoveryXAResource();
         } catch (ResourceException var4) {
            this.debug("Failed to get an XAResource for recovery purposes during call " + var1 + " :  " + var4 + ", will attempt to use one from the pool if available");
            this.useXAResourceFromPool();
            if (this.xares == null && this.enlistableXARes) {
               this.debug("Failed to obtain XAResource from the pool during " + var1 + ", throwing XAException with XAER_RMERR");
               XAException var3 = new XAException(-3);
               var3.initCause(var4);
               throw var3;
            }
         }
      }

   }

   protected XAResource getUsableXAResource(String var1) throws XAException {
      this.validate(var1);
      return this.xares;
   }

   private void useXAResourceFromPool() {
      XATxConnectionHandler var1 = this.pool.findXATxConnectionHandler();
      var1.setXARecoveryWrapper(this);
      this.xares = var1.getXAResource();
      this.enlistableXARes = var1.isEnlistableXARes();
   }
}
