package weblogic.corba.cos.transactions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.UNKNOWN;
import org.omg.CosTransactions.HeuristicCommit;
import org.omg.CosTransactions.HeuristicHazard;
import org.omg.CosTransactions.HeuristicMixed;
import org.omg.CosTransactions.HeuristicRollback;
import org.omg.CosTransactions.NotPrepared;
import org.omg.CosTransactions.RecoveryCoordinator;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.Vote;
import weblogic.corba.idl.ObjectImpl;
import weblogic.iiop.IIOPLogger;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionLoggable;
import weblogic.transaction.TransactionLogger;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.collections.Pool;
import weblogic.utils.collections.StackPool;
import weblogic.work.WorkManagerFactory;

public final class ResourceImpl extends ObjectImpl implements Resource, TransactionLoggable, Activatable {
   private ResourceActivationID aid;
   private static final int RESOURCE_POOL_SIZE = 1024;
   private static final Pool resourcePool = new StackPool(1024);
   private static final boolean DEBUG = false;
   private boolean isSynced = false;

   static ResourceImpl getResource(Xid var0) {
      Debug.assertion(var0 != null);
      ResourceImpl var1 = (ResourceImpl)resourcePool.remove();
      if (var1 == null) {
         var1 = new ResourceImpl(var0);
      } else {
         var1.aid.xid = var0;
         var1.aid.recoveryCoordinator = null;
      }

      ((ResourceFactory)ResourceFactory.getActivator()).activateResource(var1);
      return var1;
   }

   public static void releaseResource(ResourceImpl var0) {
      synchronized(var0) {
         if (var0.aid.xid != null) {
            ((ServerTransactionManager)TxHelper.getTransactionManager()).getTransactionLogger().release(var0);
            ((ResourceFactory)ResourceFactory.getActivator()).release(var0);
            var0.aid.xid = null;
            var0.aid.recoveryCoordinator = null;
            resourcePool.add(var0);
         }
      }
   }

   public static void releaseLogRecord(ResourceImpl var0) {
      ((ServerTransactionManager)TxHelper.getTransactionManager()).getTransactionLogger().release(var0);
   }

   private ResourceImpl(Xid var1) {
      this.aid = new ResourceActivationID(var1, (RecoveryCoordinator)null);
   }

   public Object getActivationID() {
      return this.aid;
   }

   void registerForRecovery(RecoveryCoordinator var1) {
      this.aid.recoveryCoordinator = var1;
   }

   public synchronized void commit() throws NotPrepared, HeuristicRollback, HeuristicMixed, HeuristicHazard {
      try {
         ServerTransactionManager var1 = (ServerTransactionManager)TxHelper.getTransactionManager();
         Transaction var2 = (Transaction)var1.getTransaction(this.aid.xid);
         if (var2 == null) {
            throw new XAException(-4);
         } else {
            getTMResource().commit(this.aid.xid, false);
            releaseResource(this);
         }
      } catch (XAException var3) {
         switch (var3.errorCode) {
            case -4:
               releaseResource(this);
               throw new INVALID_TRANSACTION(var3.getMessage());
            case 5:
               releaseResource(this);
               throw new HeuristicMixed();
            case 6:
               releaseResource(this);
               throw new HeuristicRollback();
            case 8:
               releaseResource(this);
               throw new HeuristicHazard();
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
               releaseResource(this);
               throw new TRANSACTION_ROLLEDBACK(var3.getMessage());
            default:
               IIOPLogger.logOTSError("local commit() failed unexpectedly", var3);
               throw new UNKNOWN(var3.getMessage(), 1111818304 + var3.errorCode, CompletionStatus.COMPLETED_NO);
         }
      }
   }

   public synchronized void commit_one_phase() throws HeuristicHazard {
      try {
         ServerTransactionManager var1 = (ServerTransactionManager)TxHelper.getTransactionManager();
         Transaction var2 = (Transaction)var1.getTransaction(this.aid.xid);
         if (var2 == null) {
            throw new XAException(-4);
         } else {
            var1.getTransactionLogger().store(this);
            this.waitForSync();
            var2.setLocalProperty("weblogic.transaction.otsLogRecord", this);
            getTMResource().commit(this.aid.xid, true);
            releaseResource(this);
         }
      } catch (XAException var3) {
         switch (var3.errorCode) {
            case -4:
               releaseResource(this);
               throw new INVALID_TRANSACTION(var3.getMessage());
            case 8:
               releaseResource(this);
               throw new HeuristicHazard();
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
               releaseResource(this);
               throw new TRANSACTION_ROLLEDBACK(var3.getMessage());
            default:
               throw new UNKNOWN(var3.getMessage(), 1111818304 + var3.errorCode, CompletionStatus.COMPLETED_NO);
         }
      }
   }

   public void forget() {
      try {
         getTMResource().forget(this.aid.xid);
      } catch (XAException var5) {
         throw new UNKNOWN(var5.getMessage(), 1111818304 + var5.errorCode, CompletionStatus.COMPLETED_MAYBE);
      } finally {
         releaseResource(this);
      }

   }

   public Vote prepare() throws HeuristicMixed, HeuristicHazard {
      try {
         ServerTransactionManager var1 = (ServerTransactionManager)TxHelper.getTransactionManager();
         Transaction var2 = (Transaction)var1.getTransaction(this.aid.xid);
         if (var2 == null) {
            releaseResource(this);
            throw new XAException(-4);
         }

         var1.getTransactionLogger().store(this);
         var2.setLocalProperty("weblogic.transaction.otsReplayCompletionExecuteRequest", new RecoveryRegistrar(this));
         var2.setLocalProperty("weblogic.transaction.otsLogRecord", this);
         this.waitForSync();
         int var3 = getTMResource().prepare(this.aid.xid);
         switch (var3) {
            case 0:
               return Vote.VoteCommit;
            case 3:
               return Vote.VoteReadOnly;
         }
      } catch (XAException var4) {
         switch (var4.errorCode) {
            case 5:
               throw new HeuristicMixed();
            case 8:
               throw new HeuristicHazard();
         }
      }

      return Vote.VoteRollback;
   }

   public synchronized void rollback() throws HeuristicCommit, HeuristicMixed, HeuristicHazard {
      try {
         ServerTransactionManager var1 = (ServerTransactionManager)TxHelper.getTransactionManager();
         Transaction var2 = (Transaction)var1.getTransaction(this.aid.xid);
         if (var2 == null) {
            throw new XAException(-4);
         }

         getTMResource().rollback(this.aid.xid);
      } catch (XAException var6) {
         switch (var6.errorCode) {
            case 5:
               throw new HeuristicMixed();
            case 6:
            default:
               IIOPLogger.logOTSError("local rollback() failed unexpectedly", var6);
               throw new INVALID_TRANSACTION(var6.getMessage());
            case 7:
               throw new HeuristicCommit();
            case 8:
               throw new HeuristicHazard();
         }
      } finally {
         releaseResource(this);
      }

   }

   private static final XAResource getTMResource() {
      return TxHelper.getServerInterposedTransactionManager().getXAResource();
   }

   public ResourceImpl() {
      this.aid = new ResourceActivationID();
   }

   public Activator getActivator() {
      return ResourceFactory.getActivator();
   }

   public void readExternal(DataInput var1) throws IOException {
      this.aid.xid = OTSHelper.readXid(var1);
      this.aid.recoveryCoordinator = (RecoveryCoordinator)OTSHelper.readObject(var1);
   }

   public void writeExternal(DataOutput var1) throws IOException {
      OTSHelper.writeXid(var1, this.aid.xid);
      OTSHelper.writeObject(var1, this.aid.recoveryCoordinator);
   }

   public synchronized void onDisk(TransactionLogger var1) {
      this.isSynced = true;
      this.notify();
   }

   private synchronized void waitForSync() {
      while(!this.isSynced) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

      this.isSynced = false;
   }

   public void onError(TransactionLogger var1) {
   }

   public void onRecovery(TransactionLogger var1) {
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("recovering " + this);
      }

      Debug.assertion(this.aid.recoveryCoordinator != null);
      ((ResourceFactory)ResourceFactory.getActivator()).activateResource(this);
      WorkManagerFactory.getInstance().getSystem().schedule(new RecoveryRegistrar(this));
   }

   protected static void p(String var0) {
      System.err.println("<ResourceImpl> " + var0);
   }

   static class ResourceActivationID implements Externalizable {
      private static final long serialVersionUID = -7221498218539947141L;
      private Xid xid;
      private RecoveryCoordinator recoveryCoordinator;

      public ResourceActivationID() {
      }

      private ResourceActivationID(Xid var1, RecoveryCoordinator var2) {
         this.xid = var1;
         this.recoveryCoordinator = var2;
      }

      final RecoveryCoordinator getRecoveryCoordinator() {
         return this.recoveryCoordinator;
      }

      final Xid getXid() {
         return this.xid;
      }

      public boolean equals(Object var1) {
         try {
            if (var1 == null) {
               return false;
            }

            if (this.xid == null && ((ResourceActivationID)var1).xid == null) {
               return true;
            }

            if (((ResourceActivationID)var1).xid.equals(this.xid)) {
               return true;
            }
         } catch (ClassCastException var3) {
         }

         return false;
      }

      public int hashCode() {
         return this.xid == null ? 1 : this.xid.hashCode();
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this.xid = OTSHelper.readXid(var1);
         this.recoveryCoordinator = (RecoveryCoordinator)var1.readObject();
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         OTSHelper.writeXid(var1, this.xid);
         var1.writeObject(this.recoveryCoordinator);
      }

      // $FF: synthetic method
      ResourceActivationID(Xid var1, RecoveryCoordinator var2, Object var3) {
         this(var1, var2);
      }
   }
}
