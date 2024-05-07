package weblogic.connector.transaction.outbound;

import java.security.AccessController;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.common.ResourceException;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class XAWrapper implements XAResource {
   boolean enlistableXARes = true;
   private Xid xidCached = null;
   private static String ORACLE_THIN_DRIVER_XARESOURCE_CLASSNAME = "oracle.jdbc.xa.client.OracleXAResource";
   private static String ORACLE_THIN_DRIVER_90_XARESOURCE_CLASSNAME = "oracle.jdbc.driver.T4CXAResource";
   protected boolean ended = true;
   protected XAResource xares;
   private XATxConnectionHandler connectionHandler;
   private XATxConnectionHandler connectionHandlerForRetry;
   protected ConnectionPool pool;
   private RAInstanceManager raIM;

   XAWrapper(XAResource var1, XATxConnectionHandler var2, ConnectionPool var3) {
      this.xares = var1;
      this.connectionHandler = var2;
      this.pool = var3;
   }

   public void commit(Xid var1, boolean var2) throws XAException {
      Utils.startManagement();

      try {
         this.debug(" - commit request for xid: " + var1);
         XAResource var3 = this.getUsableXAResource("commit");
         this.debug(" - issuing commit");
         AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.getRAiM().getAdapterLayer().commit(var3, this.getXid(var1), var2, var4);
         this.clearXid(var1);
      } finally {
         this.releaseResourceForRetryIfNecessary();
         Utils.stopManagement();
      }

   }

   public void end(Xid var1, int var2) throws XAException {
      Utils.startManagement();

      try {
         this.debug(" - end request for xid: " + var1 + ", flags = " + var2);
         this.validate("end");
         if (this.isUsingOracleThinDriver() && this.ended) {
            this.debug(" - not issuing repeated call to end because of issue with Oracle Thin Driver (CR100269)");
         } else {
            this.debug(" - issuing end");
            this.ended = true;
            AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            this.getRAiM().getAdapterLayer().end(this.xares, this.getXid(var1), var2, var3);
         }
      } finally {
         Utils.stopManagement();
      }

   }

   public void forget(Xid var1) throws XAException {
      Utils.startManagement();

      try {
         this.debug(" - forget request for xid: " + var1);
         XAResource var2 = this.getUsableXAResource("forget");
         this.debug(" - issuing forget");
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.getRAiM().getAdapterLayer().forget(var2, this.getXid(var1), var3);
         this.clearXid(var1);
      } finally {
         this.releaseResourceForRetryIfNecessary();
         Utils.stopManagement();
      }

   }

   public int getTransactionTimeout() throws XAException {
      Utils.startManagement();

      int var2;
      try {
         this.debug(" - getTransactionTimeout request");
         this.validate("getTransactionTimeout");
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         var2 = this.getRAiM().getAdapterLayer().getTransactionTimeout(this.xares, var1);
      } finally {
         Utils.stopManagement();
      }

      return var2;
   }

   public boolean isSameRM(XAResource var1) throws XAException {
      Utils.startManagement();

      boolean var5;
      try {
         this.debug(" - isSameRM request, xares2 = " + var1);
         boolean var2 = this == var1;
         boolean var3 = var1 instanceof XAWrapper && ((XAWrapper)var1).getConnectionPool().getName().equals(this.getConnectionPool().getName());
         boolean var4 = var2 || var3;
         this.debug(" - isSameRM request, returning:  " + var4);
         var5 = var4;
      } finally {
         Utils.stopManagement();
      }

      return var5;
   }

   public int prepare(Xid var1) throws XAException {
      Utils.startManagement();

      int var4;
      try {
         this.debug(" - prepare request for xid: " + var1);
         XAResource var2 = this.getUsableXAResource("prepare");
         this.debug(" - issuing prepare");
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         var4 = this.getRAiM().getAdapterLayer().prepare(var2, this.getXid(var1), var3);
      } finally {
         this.releaseResourceForRetryIfNecessary();
         Utils.stopManagement();
      }

      return var4;
   }

   public Xid[] recover(int var1) throws XAException {
      Utils.startManagement();

      Xid[] var10;
      try {
         this.debug(" - recover request, flag: " + var1);
         XAResource var2 = this.getUsableXAResource("recover");
         this.debug(" - issuing recover");
         Xid[] var3 = null;
         AuthenticatedSubject var4 = null;
         if (!this.enlistableXARes) {
            var3 = new Xid[0];
         } else {
            var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            if (this.callRecoverOnlyOnce() && (var1 & 16777216) == 16777216) {
               var3 = this.getRAiM().getAdapterLayer().recover(var2, 25165824, var4);
            } else if (!this.callRecoverOnlyOnce()) {
               var3 = this.getRAiM().getAdapterLayer().recover(var2, var1, var4);
            }
         }

         if (Debug.isXAoutEnabled()) {
            if (var3 != null && var3.length > 0) {
               this.debug(" - recover request, returning Xids:  count = " + var3.length);

               for(int var5 = 0; var5 < var3.length; ++var5) {
                  this.debug("   #" + var5 + ":  xid = " + this.getRAiM().getAdapterLayer().toString(var3[var5], var4));
               }
            } else {
               this.debug(" - recover request, returning no Xids");
            }
         }

         var10 = var3;
      } finally {
         this.releaseResourceForRetryIfNecessary();
         Utils.stopManagement();
      }

      return var10;
   }

   public void rollback(Xid var1) throws XAException {
      Utils.startManagement();

      try {
         this.debug(" - rollback request for xid: " + var1);
         XAResource var2 = this.getUsableXAResource("rollback");
         this.debug(" - issuing rollback");
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.getRAiM().getAdapterLayer().rollback(var2, this.getXid(var1), var3);
         this.clearXid(var1);
      } finally {
         this.releaseResourceForRetryIfNecessary();
         Utils.stopManagement();
      }

   }

   public boolean setTransactionTimeout(int var1) throws XAException {
      Utils.startManagement();

      boolean var3;
      try {
         this.debug(" - setTransactionTimeout request:  seconds = " + var1);
         this.validate("setTransactionTimeout");
         AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         var3 = this.getRAiM().getAdapterLayer().setTransactionTimeout(this.xares, var1, var2);
      } finally {
         Utils.stopManagement();
      }

      return var3;
   }

   public void start(Xid var1, int var2) throws XAException {
      Utils.startManagement();

      try {
         this.debug(" - start request for xid: " + var1 + ", flags: " + var2);
         this.validate("start");
         this.debug(" - issuing start");
         this.xidCached = this.getXid(var1);
         AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.getRAiM().getAdapterLayer().start(this.xares, this.xidCached, var2, var3);
         this.ended = false;
      } finally {
         Utils.stopManagement();
      }

   }

   protected void debug(String var1) {
      if (Debug.isXAoutEnabled()) {
         Debug.xaOut(this.getConnectionPool(), this.getClass().getName() + ":  " + this + ", " + var1);
      }

   }

   protected void debug(String var1, Throwable var2) {
      if (Debug.isXAoutEnabled()) {
         this.debug(var1 + ", Exception:  " + var2);
      }

   }

   protected ConnectionPool getConnectionPool() {
      return this.pool;
   }

   protected boolean isUsingOracleThinDriver() {
      return this.xares != null && this.xares.getClass().getName().equalsIgnoreCase(ORACLE_THIN_DRIVER_XARESOURCE_CLASSNAME);
   }

   protected boolean callRecoverOnlyOnce() {
      return this.xares != null && (this.xares.getClass().getName().equalsIgnoreCase(ORACLE_THIN_DRIVER_90_XARESOURCE_CLASSNAME) || this.xares.getClass().getName().equalsIgnoreCase(ORACLE_THIN_DRIVER_XARESOURCE_CLASSNAME));
   }

   protected void validate(String var1) throws XAException {
      if (this.connectionHandler.isConnectionErrorOccurred()) {
         this.debug(" - connectionHandler.isConnectionErrorOccurred is true; not issuing " + var1);
         throw new XAException(-3);
      } else if (this.xares == null) {
         this.debug(" - XAResource is null; not issuing " + var1);
         throw new XAException(-3);
      }
   }

   protected XAResource getUsableXAResource(String var1) throws XAException {
      XAException var2;
      if (this.connectionHandler.isConnectionErrorOccurred()) {
         this.debug(" - connectionHandler.isConnectionErrorOccurred is true; try reserve another connection from pool. operation:" + var1);

         try {
            this.connectionHandlerForRetry = this.pool.reserveInternal();
         } catch (ResourceException var4) {
            XAException var3 = new XAException("Connection error occured on original connection, and failed to get new connection from pool.");
            var3.errorCode = -3;
            var3.initCause(var4);
            throw var3;
         }

         if (this.connectionHandlerForRetry == null) {
            var2 = new XAException("Connection error occured on original connection, and cannot get new connection from pool.");
            var2.errorCode = -3;
            throw var2;
         } else {
            XAResource var5 = this.connectionHandlerForRetry.getXAResource();
            XAResource var6 = ((XAWrapper)var5).xares;
            this.debug(" get new connection from pool: " + this.connectionHandlerForRetry + "; XAWrapper: " + var5 + "; phisical XAResoruce" + var6);
            return var6;
         }
      } else if (this.xares == null) {
         this.debug(" - XAResource is null; not issuing " + var1);
         var2 = new XAException("XAResource is null.");
         var2.errorCode = -3;
         throw var2;
      } else {
         return this.xares;
      }
   }

   protected void releaseResourceForRetryIfNecessary() {
      if (this.connectionHandlerForRetry != null) {
         try {
            this.debug("release the retry resource " + this.connectionHandlerForRetry + "; XAWrapper: " + this.connectionHandlerForRetry.getXAResource());
            this.pool.releaseResource(this.connectionHandlerForRetry.getConnectionInfo());
         } catch (Throwable var2) {
            if (Debug.isXAoutEnabled()) {
               this.debug("Error occured during release the retry connection/resource. ignore it", var2);
            }
         }

         this.connectionHandlerForRetry = null;
      }

   }

   protected RAInstanceManager getRAiM() {
      if (this.raIM == null) {
         this.raIM = this.getConnectionPool().getRAInstanceManager();
      }

      return this.raIM;
   }

   private synchronized Xid getXid(Xid var1) {
      Object var2;
      if (this.xidCached != null && this.xidCached.equals(var1)) {
         var2 = this.xidCached;
      } else {
         var2 = new XidImpl(var1);
      }

      return (Xid)var2;
   }

   private void clearXid(Xid var1) {
      if (this.xidCached != null && this.xidCached.equals(var1)) {
         this.xidCached = null;
      }

   }
}
