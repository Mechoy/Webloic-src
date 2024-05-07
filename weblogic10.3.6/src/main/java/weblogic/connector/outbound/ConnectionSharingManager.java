package weblogic.connector.outbound;

import java.util.Hashtable;
import java.util.Iterator;
import javax.transaction.Transaction;
import weblogic.common.ResourceException;
import weblogic.connector.common.Debug;
import weblogic.transaction.TransactionHelper;

public class ConnectionSharingManager {
   private Hashtable sharedConnections = new Hashtable();
   private String poolName;

   public ConnectionSharingManager(String var1) {
      this.poolName = var1;
   }

   public synchronized void addSharedConnection(ConnectionInfo var1) throws ResourceException {
      if (Debug.verbose) {
         Debug.enter(this, "addSharedConnection()");
      }

      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose("on entering addSharedConnection() ConnectionInfo = " + var1.toString());
         this.dumpSharedConnectionsTable("On entry to addSharedConnection()");
      }

      try {
         Transaction var2 = TransactionHelper.getTransactionHelper().getTransaction();
         Transaction var3 = null;
         ConnectionInfo var4 = null;
         if (var2 != null) {
            if (Debug.isPoolVerboseEnabled()) {
               this.debugVerbose("tx is non-null, tx.hashcode() = " + var2.hashCode() + ", txid = " + ((weblogic.transaction.Transaction)var2).getXID() + " -- adding to sharedConnections");
            }

            var4 = (ConnectionInfo)this.sharedConnections.get(var2);
            if (var4 == null) {
               var3 = this.getTransaction(var1);
               if (var3 != null) {
                  if (Debug.isPoolVerboseEnabled()) {
                     this.debugVerbose("Removing residual tx, tx.hashcode() = " + var2.hashCode() + ", txid = " + ((weblogic.transaction.Transaction)var3).getXID());
                  }

                  this.sharedConnections.remove(var3);
               }

               this.sharedConnections.put(var2, var1);
            } else if (var4.equals(var1)) {
               this.debugVerbose("*** attempt to add same connection/tx again -- doing nothing***");
            } else {
               this.debugVerbose("*** new connection created with tx which already had a conneciton assoc. w/it.  Shouldn't happen!!!");
            }
         } else if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("tx is null - not doing anything.");
         }

         if (Debug.isPoolVerboseEnabled()) {
            this.dumpSharedConnectionsTable("On exit from addSharedConnection()");
         }
      } finally {
         if (Debug.verbose) {
            Debug.exit(this, "addSharedConnection()");
         }

      }

   }

   public synchronized boolean releaseSharedConnection(ConnectionInfo var1, boolean var2) {
      if (Debug.verbose) {
         Debug.enter(this, "releaseSharedConnection()");
      }

      if (Debug.isPoolVerboseEnabled()) {
         this.debugVerbose("on entering releaseSharedConnection() ConnectionInfo = " + var1.toString() + " & transactionCompleted = " + var2);
      }

      boolean var3 = false;
      boolean var4 = false;
      Iterator var5 = this.sharedConnections.keySet().iterator();
      Transaction var6 = null;
      ConnectionInfo var7 = null;

      try {
         if (Debug.isPoolVerboseEnabled()) {
            this.dumpSharedConnectionsTable("Before Release");
         }

         while(var5.hasNext() && !var4) {
            var6 = (Transaction)var5.next();
            var7 = (ConnectionInfo)this.sharedConnections.get(var6);
            if (var7.equals(var1)) {
               var4 = true;
               if (!var2) {
                  if (Debug.isPoolVerboseEnabled()) {
                     this.debugVerbose("decrementing sharing count from " + var1.getSharingCount());
                  }

                  var1.decrementSharingCount();
               }

               if (var1.getSharingCount() == 0 && !var1.isInTransaction()) {
                  if (Debug.isPoolVerboseEnabled()) {
                     this.debugVerbose("removing transaction from sharedConnections and releasing back to pool");
                  }

                  this.sharedConnections.remove(var6);
                  var3 = true;
                  break;
               }
            }
         }

         if (!var4 && (var1.isShareable() || var1.getConnectionHandler().getNumActiveConns() <= 0)) {
            if (Debug.isPoolVerboseEnabled()) {
               this.debugVerbose("*** couldn't find connection info in sharedConnection table ****");
               this.debugVerbose("releasing to pool");
            }

            var3 = !var1.isInTransaction();
            var1.resetSharingCount();
         }

         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("returning releaseToPool=" + var3 + " from releaseSharedConnection()");
            this.dumpSharedConnectionsTable("After Release");
         }
      } finally {
         if (Debug.verbose) {
            Debug.exit(this, "releaseSharedConnection(): releaseToPool:" + var3);
         }

      }

      return var3;
   }

   private void dumpSharedConnectionsTable(String var1) {
      if (Debug.isPoolVerboseEnabled()) {
         Iterator var2 = this.sharedConnections.keySet().iterator();
         Transaction var3 = null;
         ConnectionInfo var4 = null;
         this.debugVerbose("*** Dump of sharedConnections " + var1 + " ***");

         while(var2.hasNext()) {
            var3 = (Transaction)var2.next();
            var4 = (ConnectionInfo)this.sharedConnections.get(var3);
            this.debugVerbose("[ tx = " + ((weblogic.transaction.Transaction)var3).getXID() + ", info = " + var4.toString() + ", sharingCount = " + var4.getSharingCount() + " ]");
         }
      }

   }

   public synchronized ConnectionInfo getSharedConnection() {
      if (Debug.verbose) {
         Debug.enter(this, ".getSharedConnection");
      }

      ConnectionInfo var1 = null;

      try {
         if (Debug.isPoolVerboseEnabled()) {
            this.dumpSharedConnectionsTable("On entry to getSharedConnection()");
         }

         Transaction var2 = TransactionHelper.getTransactionHelper().getTransaction();
         if (var2 != null) {
            var1 = (ConnectionInfo)this.sharedConnections.get(var2);
         }

         if (Debug.isPoolVerboseEnabled()) {
            this.debugVerbose("Returning " + var1 + "on exit from getSharedConnection() ");
         }
      } finally {
         if (Debug.verbose) {
            Debug.exit(this, "getSharedConnection()");
         }

      }

      return var1;
   }

   private Transaction getTransaction(ConnectionInfo var1) {
      Transaction var2 = null;
      Transaction var3 = null;
      ConnectionInfo var4 = null;
      Iterator var5 = this.sharedConnections.keySet().iterator();
      boolean var6 = false;

      while(var5.hasNext() && !var6) {
         var3 = (Transaction)var5.next();
         var4 = (ConnectionInfo)this.sharedConnections.get(var3);
         if (var4.equals(var1)) {
            var6 = true;
            var2 = var3;
         }
      }

      return var2;
   }

   void debugVerbose(String var1) {
      if (Debug.isPoolVerboseEnabled()) {
         Debug.poolVerbose("For pool '" + this.poolName + "' " + var1);
      }

   }
}
