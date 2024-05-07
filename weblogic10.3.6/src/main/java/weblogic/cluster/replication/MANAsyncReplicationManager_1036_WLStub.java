package weblogic.cluster.replication;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.transaction.Transaction;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class MANAsyncReplicationManager_1036_WLStub extends Stub implements StubInfoIntf, ReplicationServicesInternal {
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md4;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$replication$ReplicationServicesInternal;
   private static Method[] m;
   private static RuntimeMethodDescriptor md7;

   public MANAsyncReplicationManager_1036_WLStub(StubInfo var1) {
      super(var1);
      this.stubinfo = var1;
      this.ror = this.stubinfo.getRemoteRef();
      ensureInitialized(this.stubinfo);
   }

   public StubInfo getStubInfo() {
      return this.stubinfo;
   }

   private static synchronized void ensureInitialized(StubInfo var0) {
      if (!initialized) {
         m = Utilities.getRemoteRMIMethods(var0.getInterfaces());
         md0 = new MethodDescriptor(m[0], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, true, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$cluster$replication$ReplicationServicesInternal == null ? (class$weblogic$cluster$replication$ReplicationServicesInternal = class$("weblogic.cluster.replication.ReplicationServicesInternal")) : class$weblogic$cluster$replication$ReplicationServicesInternal, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         initialized = true;
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public final Object create(HostID var1, int var2, ROID var3, Replicatable var4) throws RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var19;
      try {
         Object[] var6 = new Object[]{var1, new Integer(var2), var3, var4};
         var19 = (Object)this.ror.invoke((Remote)null, md0, var6, m[0]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

      return var19;
   }

   public final ReplicationManager.ROObject fetch(ROID var1) throws RemoteException, NotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ReplicationManager.ROObject var18;
      try {
         Object[] var3 = new Object[]{var1};
         var18 = (ReplicationManager.ROObject)this.ror.invoke((Remote)null, md1, var3, m[1]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (NotFoundException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var18;
   }

   public final void remove(ROID[] var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md2, var3, m[2]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (RemoteException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void remove(ROID[] var1, Object var2) throws RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md3, var4, m[3]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void removeOneWay(ROID[] var1, Object var2) throws RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md4, var4, m[4]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void update(AsyncBatch var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md5, var3, m[5]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (RemoteException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void update(ROID var1, int var2, Serializable var3, Object var4) throws NotFoundException, RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, new Integer(var2), var3, var4};
         this.ror.invoke((Remote)null, md6, var6, m[6]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (NotFoundException var16) {
         throw var16;
      } catch (RemoteException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void updateOneWay(ROID var1, int var2, Serializable var3, Object var4) throws NotFoundException, RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, new Integer(var2), var3, var4};
         this.ror.invoke((Remote)null, md7, var6, m[7]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (NotFoundException var16) {
         throw var16;
      } catch (RemoteException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }
}
