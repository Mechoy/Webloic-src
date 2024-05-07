package weblogic.wsee.wstx.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.store.ObjectHandler;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.XAResource;
import weblogic.transaction.internal.MigratableRM;
import weblogic.transaction.internal.ServerTransactionManagerImpl;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.wsat.WSATHelper;
import weblogic.wsee.wstx.wsat.WSATXAResource;

public class WSATGatewayRM implements XAResource, MigratableRM {
   private static final String RM_NAME_PREFIX = "WSATGatewayRM_";
   private static final String STORE_CONNECTION_NAME = "weblogic.wsee.wstx.gateway";
   private static final int STORE_NO_FLAGS = 0;
   private final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
   private static WSATGatewayRM singleton;
   private String resourceRegistrationName;
   private Map<Xid, BranchRecord> branches;
   private List<Xid> pendingXids;
   private PersistentStore store;
   private PersistentStoreConnection storeConn;
   private final Object currentBQualLock = new Object();
   private byte[] currentBQual;
   private static TransactionManager m_transactionManager;
   private static Transaction m_transaction;

   private WSATGatewayRM() {
   }

   private WSATGatewayRM(String var1, PersistentStore var2) {
      this.resourceRegistrationName = "WSATGatewayRM_" + var1;
      this.branches = Collections.synchronizedMap(new HashMap());
      this.pendingXids = Collections.synchronizedList(new ArrayList());
      this.store = var2;
   }

   public static synchronized WSATGatewayRM getInstance() {
      return singleton;
   }

   public static synchronized WSATGatewayRM create(String var0, PersistentStore var1) throws SystemException, PersistentStoreException {
      if (singleton == null) {
         singleton = new WSATGatewayRM(var0, var1);
         singleton.initStore();
         singleton.recoverPendingBranches();
         singleton.registerResourceWithTM();
      }

      return singleton;
   }

   public synchronized MigratableRM createForMigration(String var1, PersistentStore var2) throws SystemException, PersistentStoreException {
      WSATGatewayRM var3 = new WSATGatewayRM(var1, var2);
      var3.initStore();
      var3.recoverPendingBranches();
      var3.registerResourceWithTM();
      return var3;
   }

   private void initStore() throws PersistentStoreException {
      this.storeConn = this.store.createConnection("weblogic.wsee.wstx.gateway", new BranchObjectHandler());
   }

   private void recoverPendingBranches() throws PersistentStoreException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("recoverPendingBranches()");
      }

      PersistentStoreConnection.Cursor var1 = this.storeConn.createCursor(0);

      PersistentStoreRecord var2;
      while((var2 = var1.next()) != null) {
         BranchRecord var3 = (BranchRecord)var2.getData();
         var3.setStoreHandle(var2.getHandle());
         this.branches.put(var3.getXid(), var3);
         this.pendingXids.addAll(var3.getAllXids());
         if (WSATHelper.isDebugEnabled()) {
            this.debug("recovered: " + var3);
         }
      }

   }

   private void registerResourceWithTM() throws SystemException {
      this.getTM().registerDynamicResource(this.resourceRegistrationName, this);
   }

   public void stop() {
      try {
         this.unregisterResource();
      } catch (SystemException var2) {
         var2.printStackTrace();
      }

   }

   private void unregisterResource() throws SystemException {
      TransactionManager var1 = this.getTM();
      var1.unregisterResource(this.resourceRegistrationName, true);
   }

   public byte[] registerWSATResource(Xid var1, javax.transaction.xa.XAResource var2) throws IllegalStateException, RollbackException, SystemException {
      Transaction var3 = this.getTransaction(var1);
      if (var3 == null) {
         throw new IllegalStateException("Transaction " + var1 + " does not exist, wsatResource=" + var2);
      } else {
         var3.enlistResource(this);
         BranchRecord var4 = this.getOrCreateBranch(var1);
         javax.transaction.xa.XAResource var5 = var4.exists(var2);
         if (var5 != null) {
            return ((WSATXAResource)var5).getXid().getBranchQualifier();
         } else {
            var4.addSubordinate(var2);
            byte[] var6;
            synchronized(this.currentBQualLock) {
               String var8 = var4.getBranchName(var2) + WSATHelper.assignUUID();
               var3.enlistResource(this, var8);
               var6 = this.currentBQual;
               this.currentBQual = null;
            }

            if (WSATHelper.isDebugEnabled()) {
               this.debug("registerWSATResource() xid=" + var1 + " currentBQual=" + this.currentBQual + " bqual=" + var6);
            }

            return var6;
         }
      }
   }

   public void start(Xid var1, int var2) throws XAException {
      this.currentBQual = var1.getBranchQualifier();
      if (WSATHelper.isDebugEnabled()) {
         this.debug("start() xid=" + var1 + ", flags=" + var2 + ", currentBQual=" + this.currentBQual);
      }

      switch (var2) {
         case 0:
            this.getOrCreateBranch(var1);
            break;
         case 2097152:
         case 134217728:
            BranchRecord var3 = this.getBranch(var1);
            if (var3 == null) {
               JTAHelper.throwXAException(-4, "Attempt to resume xid " + var1 + " that is not in SUSPENDED state.");
            }
            break;
         case 536870912:
            JTAHelper.throwXAException(-3, "error while attempting to rollback branch" + this.resourceRegistrationName);
            break;
         default:
            throw new IllegalArgumentException("invalid flag:" + var2);
      }

   }

   public void end(Xid var1, int var2) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("end() xid=" + var1 + ", flags=" + var2);
      }

      BranchRecord var3 = this.getBranch(var1);
      if (var3 == null) {
         JTAHelper.throwXAException(-4, "end: no branch info for " + var1);
      }

   }

   public int prepare(Xid var1) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("prepare() xid=" + var1);
      }

      BranchRecord var2 = this.getBranch(var1);
      if (var2 == null) {
         JTAHelper.throwXAException(-4, "prepare: no branch info for " + var1);
      }

      this.persistBranchIfNecessary(var2);
      return var2.prepare(var1);
   }

   public void commit(Xid var1, boolean var2) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("commit() xid=" + var1);
      }

      BranchRecord var3 = this.getBranch(var1);
      if (var3 == null) {
         JTAHelper.throwXAException(-4, "commit: no branch information for xid:" + var1);
      }

      try {
         var3.commit(var1, var2);
      } finally {
         this.deleteBranchIfNecessary(var3);
      }

   }

   public void rollback(Xid var1) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("rollback() xid=" + var1);
      }

      BranchRecord var2 = this.getBranch(var1);
      if (var2 == null) {
         JTAHelper.throwXAException(-4, "rollback: no branch info for " + var1);
      }

      try {
         var2.rollback(var1);
      } finally {
         this.deleteBranchIfNecessary(var2, true);
      }

   }

   public Xid[] recover(int var1) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("recover() flag=" + var1);
      }

      if ((var1 & 16777216) != 0) {
         if (WSATHelper.isDebugEnabled()) {
            this.debug("WSAT recover(" + var1 + ") returning " + this.pendingXids);
         }

         Xid[] var2 = (Xid[])this.pendingXids.toArray(new Xid[this.pendingXids.size()]);
         return var2;
      } else {
         if (WSATHelper.isDebugEnabled()) {
            this.debug("recover() returning nothing");
         }

         return new Xid[0];
      }
   }

   public void forget(Xid var1) throws XAException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("forget() xid=" + var1);
      }

      BranchRecord var2 = this.getBranch(var1);
      if (var2 == null) {
         JTAHelper.throwXAException(-4, "forget: no branch info for " + var1);
      }

      this.deleteBranchIfNecessary(var2);
   }

   public int getTransactionTimeout() throws XAException {
      return -1;
   }

   public boolean setTransactionTimeout(int var1) throws XAException {
      return false;
   }

   public boolean isSameRM(javax.transaction.xa.XAResource var1) throws XAException {
      if (!(var1 instanceof WSATGatewayRM)) {
         return false;
      } else {
         WSATGatewayRM var2 = (WSATGatewayRM)var1;
         return this.equals(var2);
      }
   }

   public boolean detectedUnavailable() {
      return true;
   }

   public int getDelistFlag() {
      return 67108864;
   }

   private synchronized BranchRecord getOrCreateBranch(Xid var1) {
      BranchRecord var2 = this.getBranch(var1);
      if (var2 == null) {
         var2 = new BranchRecord(var1);
         this.branches.put(var1, var2);
      }

      return var2;
   }

   private synchronized BranchRecord getBranch(Xid var1) {
      BranchRecord var2 = (BranchRecord)this.branches.get(var1);
      if (var2 != null && var1.getBranchQualifier() != null) {
         var2.assignBranchXid(var1);
      }

      return var2;
   }

   private void delete(BranchRecord var1, boolean var2) throws PersistentStoreException {
      if (var2) {
         this.releaseBranchRecord(var1);
      }

      this.branches.remove(var1.getXid());
      this.pendingXids.removeAll(var1.getAllXids());
   }

   private void persistBranchRecord(BranchRecord var1) throws PersistentStoreException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("persist branch record " + var1);
      }

      PersistentStoreTransaction var2 = this.store.begin();
      PersistentHandle var3 = this.storeConn.create(var2, var1, 0);
      var2.commit();
      var1.setStoreHandle(var3);
      var1.setLogged(true);
   }

   private void releaseBranchRecord(BranchRecord var1) throws PersistentStoreException {
      if (WSATHelper.isDebugEnabled()) {
         this.debug("release branch record " + var1);
      }

      PersistentHandle var2 = var1.getStoreHandle();
      if (var2 != null) {
         PersistentStoreTransaction var3 = this.store.begin();
         this.storeConn.delete(var3, var2, 0);
         var3.commit();
         var1.setStoreHandle((PersistentHandle)null);
         var1.setLogged(false);
      }
   }

   private void persistBranchIfNecessary(BranchRecord var1) throws XAException {
      try {
         synchronized(var1) {
            if (!var1.isLogged()) {
               this.persistBranchRecord(var1);
               this.pendingXids.addAll(var1.getAllXids());
            }
         }
      } catch (PersistentStoreException var5) {
         this.debug("error persisting branch " + var1 + ": " + var5.toString());
         WseeWsatLogger.logErrorPersistingBranchRecord(var1.toString(), var5);
         JTAHelper.throwXAException(-3, "Error persisting branch " + var1, var5);
      }

   }

   private boolean deleteBranchIfNecessary(BranchRecord var1) throws XAException {
      return this.deleteBranchIfNecessary(var1, false);
   }

   private boolean deleteBranchIfNecessary(BranchRecord var1, boolean var2) throws XAException {
      boolean var3 = false;

      try {
         synchronized(var1) {
            boolean var5 = var1.isLogged();
            if ((var5 || var2) && var1.allResourcesCompleted()) {
               this.delete(var1, var5);
               var3 = true;
            }
         }
      } catch (PersistentStoreException var8) {
         this.debug("error deleting branch record " + var1 + ": " + var8.toString());
         WseeWsatLogger.logErrorDeletingBranchRecord(var1.toString(), var8);
         JTAHelper.throwXAException(-3, "Error deleting branch record " + var1, var8);
      }

      return var3;
   }

   private TransactionManager getTM() {
      return m_transactionManager != null ? m_transactionManager : (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
   }

   private Transaction getTransaction(Xid var1) {
      if (m_transaction != null) {
         return m_transaction;
      } else {
         TransactionManager var2 = this.getTM();
         return (Transaction)var2.getTransaction(var1);
      }
   }

   static void setTM(TransactionManager var0) {
      m_transactionManager = var0;
   }

   static void setTx(Transaction var0) {
      m_transaction = var0;
   }

   private void debug(String var1) {
      if (WSATHelper.isDebugEnabled()) {
         this.debugWSAT.debug("[WSATGatewayRM] " + this.resourceRegistrationName + " msg:" + var1);
      }

   }

   static {
      ServerTransactionManagerImpl.registerMigratableRM(new WSATGatewayRM());
   }

   private final class BranchObjectHandler implements ObjectHandler {
      private static final int VERSION = 1;

      private BranchObjectHandler() {
      }

      public Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
         int var2 = var1.readInt();
         if (var2 != 1) {
            throw new IOException("Stream corrupted.  Invalid WS-AT gateway branch version: " + var2);
         } else {
            BranchRecord var3 = new BranchRecord();
            var3.readExternal(var1);
            if (WSATHelper.isDebugEnabled()) {
               WSATGatewayRM.this.debug("read WS-AT branch " + var3);
            }

            return var3;
         }
      }

      public void writeObject(ObjectOutput var1, Object var2) throws IOException {
         if (!(var2 instanceof BranchRecord)) {
            throw new IOException("Cannot serialize class of type: " + (var2 == null ? null : var2.getClass().toString()));
         } else {
            var1.writeInt(1);
            BranchRecord var3 = (BranchRecord)var2;
            var3.writeExternal(var1);
            if (WSATHelper.isDebugEnabled()) {
               WSATGatewayRM.this.debug("serialized WS-AT branch " + var3);
            }

         }
      }

      // $FF: synthetic method
      BranchObjectHandler(Object var2) {
         this();
      }
   }
}
