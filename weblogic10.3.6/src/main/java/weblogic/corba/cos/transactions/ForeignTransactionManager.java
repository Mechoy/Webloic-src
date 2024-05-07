package weblogic.corba.cos.transactions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.AccessController;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.UNKNOWN;
import org.omg.CosTransactions.HeuristicCommit;
import org.omg.CosTransactions.HeuristicHazard;
import org.omg.CosTransactions.HeuristicMixed;
import org.omg.CosTransactions.HeuristicRollback;
import org.omg.CosTransactions.NotPrepared;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.Vote;
import weblogic.corba.utils.RemoteInfo;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionLoggable;
import weblogic.transaction.TransactionLogger;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.AssertionError;
import weblogic.utils.collections.ConcurrentHashMap;

public final class ForeignTransactionManager implements XAResource, TransactionLoggable, Serializable {
   protected static final boolean DEBUG = false;
   private String name;
   private int id;
   private int txTimeout = 0;
   private transient boolean isSynced = false;
   private transient boolean isFailed = false;
   private static final int FTM_POOL_SIZE = 1024;
   private static final ForeignTransactionManager[] ftmPool = new ForeignTransactionManager[1024];
   private static int ftmCounter = 0;
   private static String FTM_NAME;

   public static ForeignTransactionManager registerResource(Resource var0, Xid var1) throws SystemException {
      ForeignTransactionManager var2 = null;
      Transaction var3 = getTx(var1);
      synchronized(ftmPool) {
         ResourceMap var5 = (ResourceMap)var3.getProperty("weblogic.transaction.ots.resources");
         if (var5 == null) {
            var5 = new ResourceMap();
            var3.setProperty("weblogic.transaction.ots.resources", var5);
         } else if ((var2 = (ForeignTransactionManager)var5.get(var0)) != null) {
            return var2;
         }

         Integer var6 = (Integer)var3.getProperty("weblogic.transaction.ots.ftmCounter");
         int var7 = var6 == null ? 0 : var6;
         var3.setProperty("weblogic.transaction.ots.ftmCounter", new Integer(var7 + 1));
         TransactionManager var8 = TxHelper.getTransactionManager();
         var2 = ftmPool[var7];
         if (var2 == null) {
            if (ftmCounter == 1024) {
               throw new AssertionError("OTS Foreign TM pool exhausted");
            }

            if (var7 >= ftmCounter) {
               ++ftmCounter;
            }

            var2 = ftmPool[var7] = new ForeignTransactionManager(getFtmName() + var7, var7);
            ((ServerTransactionManager)var8).getTransactionLogger().store(var2);
            if (OTSHelper.isDebugEnabled()) {
               IIOPLogger.logDebugOTS("registerResource(" + var2.getName() + "): registering with JTA");
            }

            var8.registerDynamicResource(var2.getName(), var2);
         }

         if (var2.isFailed) {
            var8.unregisterResource(var2.getName());
            var8.registerDynamicResource(var2.getName(), var2);
            var2.isFailed = false;
            if (OTSHelper.isDebugEnabled()) {
               IIOPLogger.logDebugOTS("registerResource(" + var2.getName() + "): re-registering with JTA");
            }
         }

         var5.put(var0, var2);
         var5.put(var2, var0);
      }

      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("registerResource(" + var2.getName() + "): enlisted " + var2 + " for " + var1);
      }

      return var2;
   }

   private static final String getFtmName() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (FTM_NAME == null) {
         Class var1 = ForeignTransactionManager.class;
         synchronized(ForeignTransactionManager.class) {
            FTM_NAME = ManagementService.getRuntimeAccess(var0).getServer().getName() + "_OTS_ForeignTM_";
         }
      }

      return FTM_NAME;
   }

   public static void releaseResource(ForeignTransactionManager var0) {
   }

   private ForeignTransactionManager(String var1, int var2) {
      this.name = var1;
      this.id = var2;
   }

   private void deenlist(Xid var1) {
   }

   public final String getName() {
      return this.name;
   }

   public final String toString() {
      return this.name;
   }

   public void commit(Xid var1, boolean var2) throws XAException {
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("commit(" + var1 + ", " + var2 + ")");
      }

      try {
         Resource var3 = this.findResource(var1);
         if (var2) {
            var3.commit_one_phase();
         } else {
            var3.commit();
         }

         releaseResource(this);
      } catch (HeuristicMixed var15) {
         throw (XAException)(new XAException(5)).initCause(var15);
      } catch (HeuristicRollback var16) {
         throw (XAException)(new XAException(6)).initCause(var16);
      } catch (HeuristicHazard var17) {
         throw (XAException)(new XAException(8)).initCause(var17);
      } catch (NotPrepared var18) {
         throw (XAException)(new XAException(6)).initCause(var18);
      } catch (TRANSACTION_ROLLEDBACK var19) {
         throw (XAException)(new XAException(100)).initCause(var19);
      } catch (UNKNOWN var20) {
         if (var20.minor >= 1111818304 && var20.minor <= 1111818368) {
            throw (XAException)(new XAException(var20.minor - 1111818304)).initCause(var20);
         }

         throw (XAException)(new XAException(-3)).initCause(var20);
      } catch (INVALID_TRANSACTION var21) {
         throw (XAException)(new XAException(-4)).initCause(var21);
      } catch (OBJECT_NOT_EXIST var22) {
         throw new XAException(-4);
      } catch (org.omg.CORBA.SystemException var23) {
         IIOPLogger.logOTSError("commit() failed unexpectedly", var23);
         this.isFailed = true;
         throw (XAException)(new XAException(-3)).initCause(var23);
      } finally {
         this.deenlist(var1);
      }

   }

   public void end(Xid var1, int var2) throws XAException {
      switch (var2) {
         case 33554432:
         case 67108864:
         case 536870912:
            return;
         default:
            throw new XAException(-5);
      }
   }

   public void forget(Xid var1) throws XAException {
      try {
         Resource var2 = this.findResource(var1);
         var2.forget();
         releaseResource(this);
      } catch (org.omg.CORBA.SystemException var6) {
         IIOPLogger.logOTSError("forget() failed unexpectedly", var6);
         this.isFailed = true;
         throw (XAException)(new XAException(-3)).initCause(var6);
      } finally {
         this.deenlist(var1);
      }

   }

   public int getTransactionTimeout() throws XAException {
      return this.txTimeout;
   }

   public boolean isSameRM(XAResource var1) throws XAException {
      if (var1 instanceof ForeignTransactionManager) {
         ForeignTransactionManager var2 = (ForeignTransactionManager)var1;
         if (var2.equals(this)) {
            return true;
         }
      }

      return false;
   }

   public boolean equals(Object var1) {
      try {
         ForeignTransactionManager var2 = (ForeignTransactionManager)var1;
         return var2 != null && this.name.equals(var2.name) && this.id == var2.id;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode() ^ this.id;
   }

   public int prepare(Xid var1) throws XAException {
      try {
         Resource var2 = this.findResource(var1);
         Vote var3 = var2.prepare();
         switch (var3.value()) {
            case 0:
               return 0;
            case 2:
               return 3;
            default:
               throw new XAException(100);
         }
      } catch (HeuristicMixed var4) {
         throw (XAException)(new XAException(5)).initCause(var4);
      } catch (HeuristicHazard var5) {
         throw (XAException)(new XAException(8)).initCause(var5);
      } catch (TRANSACTION_ROLLEDBACK var6) {
         throw (XAException)(new XAException(100)).initCause(var6);
      } catch (INVALID_TRANSACTION var7) {
         throw (XAException)(new XAException(-4)).initCause(var7);
      } catch (org.omg.CORBA.SystemException var8) {
         IIOPLogger.logOTSError("prepare() failed unexpectedly", var8);
         this.isFailed = true;
         throw (XAException)(new XAException(-3)).initCause(var8);
      }
   }

   public Xid[] recover(int var1) throws XAException {
      switch (var1) {
         case 0:
         case 8388608:
         default:
            return null;
         case 16777216:
            return null;
      }
   }

   public void rollback(Xid var1) throws XAException {
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("rollback(" + var1 + ")");
      }

      try {
         Resource var2 = this.findResource(var1);
         var2.rollback();
         releaseResource(this);
      } catch (HeuristicMixed var11) {
         throw (XAException)(new XAException(5)).initCause(var11);
      } catch (HeuristicCommit var12) {
         throw (XAException)(new XAException(7)).initCause(var12);
      } catch (HeuristicHazard var13) {
         throw (XAException)(new XAException(8)).initCause(var13);
      } catch (INVALID_TRANSACTION var14) {
         throw (XAException)(new XAException(-4)).initCause(var14);
      } catch (OBJECT_NOT_EXIST var15) {
         throw new XAException(-4);
      } catch (org.omg.CORBA.SystemException var16) {
         IIOPLogger.logOTSError("rollback() failed unexpectedly", var16);
         this.isFailed = true;
         throw (XAException)(new XAException(-3)).initCause(var16);
      } finally {
         this.deenlist(var1);
      }

   }

   public boolean setTransactionTimeout(int var1) throws XAException {
      this.txTimeout = var1;
      return false;
   }

   public void start(Xid var1, int var2) throws XAException {
      if (this.findResource(var1) == null) {
         throw new XAException(-4);
      }
   }

   private Resource findResource(Xid var1) throws XAException {
      Transaction var2 = getTx(var1);
      if (var2 == null) {
         if (OTSHelper.isDebugEnabled()) {
            IIOPLogger.logDebugOTS("findResource(" + var1 + ") failed, no transaction");
         }

         throw new XAException(-4);
      } else {
         ResourceMap var3 = (ResourceMap)var2.getProperty("weblogic.transaction.ots.resources");
         Resource var4 = null;
         if (var3 != null && (var4 = (Resource)var3.get(this)) != null) {
            return var4;
         } else {
            if (OTSHelper.isDebugEnabled()) {
               IIOPLogger.logDebugOTS("findResource(" + var1 + ") failed because " + var3 == null ? "no resources are registered" : "no mapping exists for this resource");
            }

            throw new XAException(-4);
         }
      }
   }

   Resource findMissingResource(ForeignTransactionManager var1, Transaction var2) {
      LinkedList var3 = (LinkedList)var2.getLocalProperty("weblogic.transaction.ots.failedResources");
      if (var3 != null) {
         ResourceMap var4 = (ResourceMap)var2.getProperty("weblogic.transaction.ots.resources");
         if (var4 == null) {
            var4 = new ResourceMap();
            var2.setProperty("weblogic.transaction.ots.resources", var4);
         }

         Resource var5 = (Resource)var3.removeFirst();
         var4.put(var1, var5);
         var4.put(var5, var1);
         return var5;
      } else {
         return null;
      }
   }

   private static final Transaction getTx(Xid var0) {
      return (Transaction)TxHelper.getTransactionManager().getTransaction(var0);
   }

   public ForeignTransactionManager() {
   }

   public void readExternal(DataInput var1) throws IOException {
      this.name = var1.readUTF();
      this.txTimeout = var1.readInt();
      this.id = var1.readInt();
      synchronized(ftmPool) {
         ftmPool[this.id] = this;
         if (this.id >= ftmCounter) {
            ftmCounter = this.id + 1;
         }

      }
   }

   public void writeExternal(DataOutput var1) throws IOException {
      var1.writeUTF(this.name);
      var1.writeInt(this.txTimeout);
      var1.writeInt(this.id);
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
      try {
         if (OTSHelper.isDebugEnabled()) {
            IIOPLogger.logDebugOTS("recovering " + this);
         }

         TransactionManager var2 = TxHelper.getTransactionManager();
         var2.registerDynamicResource(this.getName(), this);
      } catch (Exception var3) {
         releaseResource(this);
      }

   }

   protected static void p(String var0) {
      System.err.println("<ForeignTransactionManager> " + var0);
   }

   public static class ResourceMap extends ConcurrentHashMap implements Externalizable {
      public synchronized void writeExternal(ObjectOutput var1) throws IOException {
         Set var2 = this.entrySet();
         var1.writeInt(var2.size() / 2);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            Object var5 = var4.getKey();
            Object var6 = var4.getValue();
            if (var5 instanceof ForeignTransactionManager) {
               var1.writeUTF(((ForeignTransactionManager)var5).getName());
               var1.writeObject((IOR)IIOPReplacer.getIIOPReplacer().replaceObject(var6));
            }
         }

      }

      public synchronized void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         int var2 = var1.readInt();

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1.readUTF();
            ForeignTransactionManager var5 = null;
            byte var6 = 0;
            if (var6 < ForeignTransactionManager.ftmCounter && ForeignTransactionManager.ftmPool[var6] != null && ForeignTransactionManager.ftmPool[var6].getName().equals(var4)) {
               var5 = ForeignTransactionManager.ftmPool[var6];
            }

            IOR var8 = (IOR)var1.readObject();
            if (var5 != null) {
               Resource var7 = (Resource)IIOPReplacer.resolveObject(var8, RemoteInfo.findRemoteInfo(Resource.class));
               this.put(var5, var7);
               this.put(var7, var5);
            }
         }

      }
   }
}
