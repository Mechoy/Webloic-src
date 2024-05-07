package weblogic.connector.transaction.outbound;

import java.security.AccessController;
import javax.resource.ResourceException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.transaction.SystemException;
import weblogic.connector.common.Debug;
import weblogic.connector.common.Utils;
import weblogic.connector.outbound.ConnectionInfo;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;
import weblogic.transaction.nonxa.NonXAResource;
import weblogic.utils.StackTraceUtils;

public class LocalTxConnectionHandler extends TxConnectionHandler {
   private NonXAWrapper localTransactionWrapper;

   public LocalTxConnectionHandler(ManagedConnection var1, ConnectionPool var2, SecurityContext var3, ConnectionInfo var4) throws ResourceException {
      super(var1, var2, var3, var4, "LocalTransaction");
      this.initializeNonXAResource();
      this.addConnectionRuntimeMBean();
   }

   public void enListResource() throws ResourceException {
      if (this.transaction == null) {
         Transaction var1 = TxHelper.getTransaction();
         if (var1 != null) {
            Throwable var2 = null;
            boolean var3 = false;
            Object var4 = null;
            boolean var5 = false;
            AuthenticatedSubject var6 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

            try {
               this.connPool.getResourceRegistrationManager().enlistResource(this, var1);
               var3 = true;
               TxCompletionNotification.register(var1, this);
               this.connPool.getRAInstanceManager().getAdapterLayer().begin(this.getLocalTransactionWrapper().localTransaction, var6);
               var5 = true;
               this.connPool.getConnectionSharingManager().addSharedConnection(super.getConnectionInfo());
            } catch (Throwable var16) {
               Throwable var7 = var16;
               var2 = var16;
               if (var4 != null) {
                  ((TxCompletionNotification)var4).deregister();
               }

               if (var5) {
                  this.getLocalTransactionWrapper().localTransaction.rollback();
               }

               if (var3) {
                  this.localTransactionWrapper.disable();
                  this.destroy();
               }

               try {
                  String var8 = Debug.getExceptionEnlistmentFailed(this.connPool.getKey(), var7.toString());
                  ((Transaction)var1).setRollbackOnly(var8, var7);
               } catch (Throwable var15) {
                  Debug.localOut(this.connPool, "Failed to setRollbackOnly after enlistment failure:  " + var15 + StackTraceUtils.throwable2StackTrace(var15));
               }
            } finally {
               if (var2 != null) {
                  if (Debug.isLocalOutEnabled()) {
                     Debug.localOut(this.connPool, "Failed to setup the connection for transaction enlistment:\n" + this.connPool.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var2, var6));
                  }

                  String var11 = Debug.getExceptionEnlistmentFailed(this.connPool.getKey(), var2.toString());
                  throw new ResourceException(var11, var2);
               }

            }

            this.transaction = var1;
            this.setGlobalTransactionInProgress(true);
         }
      }

   }

   private void initializeNonXAResource() throws ResourceException {
      LocalTransaction var1 = null;
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      String var4;
      String var5;
      String var6;
      String var7;
      try {
         var1 = this.connPool.getRAInstanceManager().getAdapterLayer().getLocalTransaction(this.managedConnection, var2);
      } catch (ResourceException var9) {
         if (Debug.isLocalOutEnabled()) {
            Debug.localOut(this.connPool, "LocalTxConnectionHandler:  Resource Adapter with key = " + this.connPool.getKey() + " threw ResourceException from its implementation of ManagedConnection.getLocalTransaction(), " + this.connPool.getRAInstanceManager().getAdapterLayer().toString(var9, var2) + "\n" + this.connPool.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var9, var2));
         }

         var4 = this.connPool.getRAInstanceManager().getAdapterLayer().toString(var9, var2);
         Debug.logGetLocalTransactionError(var4, this.connPool.getKey());
         Throwable var11 = this.connPool.getRAInstanceManager().getAdapterLayer().getCause(var9, var2);
         if (var11 != null && Debug.isLocalOutEnabled()) {
            Debug.localOut(this.connPool, "LocalTxConnectionHandler:  ResourceException has LinkedException:\n" + var11 + "\n" + this.connPool.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var11, var2));
         }

         throw var9;
      } catch (Throwable var10) {
         var4 = this.connPool.getRAInstanceManager().getAdapterLayer().toString(var10, var2);
         var5 = this.connPool.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var10, var2);
         var6 = Debug.getExceptionMCGetLocalTransactionThrewNonResourceException(this.connPool.getKey(), var4);
         var7 = Debug.logGetLocalTransactionError(var4, this.connPool.getKey());
         Debug.logStackTraceString(var7, var5);
         Utils.throwAsResourceException(var6, var10);
      }

      if (var1 == null) {
         String var3 = Debug.getExceptionMCGetLocalTransactionReturnedNull(this.connPool.getKey());
         Debug.logGetLocalTransactionError(var3, this.connPool.getKey());
         throw new ResourceException(var3);
      } else {
         try {
            this.localTransactionWrapper = new NonXAWrapper(var1, this);
            this.connPool.getResourceRegistrationManager().addResource(this);
         } catch (SystemException var8) {
            var4 = this.connPool.getRAInstanceManager().getAdapterLayer().toString(var8, var2);
            var5 = this.connPool.getRAInstanceManager().getAdapterLayer().throwable2StackTrace(var8, var2);
            var6 = Debug.logRegisterNonXAResourceError(this.getPoolName(), var4);
            Debug.logStackTraceString(var6, var5);
            var7 = Debug.getExceptionRegisterNonXAFailed(var8.toString());
            Utils.throwAsResourceException(var7, var8);
         }

      }
   }

   void setLocalTransactionWrapper(NonXAWrapper var1) {
      this.localTransactionWrapper = var1;
   }

   NonXAResource getNonXAResource() {
      return this.localTransactionWrapper;
   }

   NonXAWrapper getLocalTransactionWrapper() {
      return this.localTransactionWrapper;
   }

   public LocalTransaction getLocalTransaction() {
      return this.localTransactionWrapper.localTransaction;
   }

   protected void initializeConnectionEventListener() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.connPool.getRAInstanceManager().getAdapterLayer().addConnectionEventListener(this.managedConnection, new TxConnectionEventListener(this, "LocalTransConnEventListener"), var1);
   }
}
