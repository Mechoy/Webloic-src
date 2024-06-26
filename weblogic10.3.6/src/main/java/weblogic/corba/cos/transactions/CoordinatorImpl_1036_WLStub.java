package weblogic.corba.cos.transactions;

import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.transaction.Transaction;
import org.omg.CORBA.Object;
import org.omg.CosTransactions.Control;
import org.omg.CosTransactions.Coordinator;
import org.omg.CosTransactions.Inactive;
import org.omg.CosTransactions.NotSubtransaction;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.RecoveryCoordinator;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.Status;
import org.omg.CosTransactions.SubtransactionAwareResource;
import org.omg.CosTransactions.SubtransactionsUnavailable;
import org.omg.CosTransactions.Synchronization;
import org.omg.CosTransactions.SynchronizationUnavailable;
import org.omg.CosTransactions.Unavailable;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class CoordinatorImpl_1036_WLStub extends Stub implements StubInfoIntf, Coordinator, Object {
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md12;
   private static RuntimeMethodDescriptor md11;
   private static RuntimeMethodDescriptor md10;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md14;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md16;
   private static RuntimeMethodDescriptor md15;
   private static Method[] m;
   private static RuntimeMethodDescriptor md13;
   // $FF: synthetic field
   private static Class class$org$omg$CosTransactions$Coordinator;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public CoordinatorImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$org$omg$CosTransactions$Coordinator == null ? (class$org$omg$CosTransactions$Coordinator = class$("org.omg.CosTransactions.Coordinator")) : class$org$omg$CosTransactions$Coordinator, false, false, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
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

   public final Control create_subtransaction() throws SubtransactionsUnavailable, Inactive {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Control var17;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var17 = (Control)this.ror.invoke((Remote)null, md0, var2, m[0]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (SubtransactionsUnavailable var13) {
         throw var13;
      } catch (Inactive var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var17;
   }

   public final Status get_parent_status() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Status var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Status)this.ror.invoke((Remote)null, md1, var2, m[1]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final Status get_status() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Status var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Status)this.ror.invoke((Remote)null, md2, var2, m[2]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final Status get_top_level_status() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Status var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Status)this.ror.invoke((Remote)null, md3, var2, m[3]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final String get_transaction_name() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (String)this.ror.invoke((Remote)null, md4, var2, m[4]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final PropagationContext get_txcontext() throws Unavailable {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      PropagationContext var15;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var15 = (PropagationContext)this.ror.invoke((Remote)null, md5, var2, m[5]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Unavailable var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final int hash_top_level_tran() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      int var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Integer)this.ror.invoke((Remote)null, md6, var2, m[6]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final int hash_transaction() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      int var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Integer)this.ror.invoke((Remote)null, md7, var2, m[7]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final boolean is_ancestor_transaction(Coordinator var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md8, var3, m[8]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final boolean is_descendant_transaction(Coordinator var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md9, var3, m[9]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final boolean is_related_transaction(Coordinator var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md10, var3, m[10]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final boolean is_same_transaction(Coordinator var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md11, var3, m[11]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final boolean is_top_level_transaction() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Boolean)this.ror.invoke((Remote)null, md12, var2, m[12]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final RecoveryCoordinator register_resource(Resource var1) throws Inactive {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      RecoveryCoordinator var16;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var16 = (RecoveryCoordinator)this.ror.invoke((Remote)null, md13, var3, m[13]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Inactive var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final void register_subtran_aware(SubtransactionAwareResource var1) throws Inactive, NotSubtransaction {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         this.ror.invoke((Remote)null, md14, var3, m[14]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Inactive var13) {
         throw var13;
      } catch (NotSubtransaction var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void register_synchronization(Synchronization var1) throws Inactive, SynchronizationUnavailable {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         this.ror.invoke((Remote)null, md15, var3, m[15]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Inactive var13) {
         throw var13;
      } catch (SynchronizationUnavailable var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void rollback_only() throws Inactive {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         this.ror.invoke((Remote)null, md16, var2, m[16]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Inactive var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }
}
