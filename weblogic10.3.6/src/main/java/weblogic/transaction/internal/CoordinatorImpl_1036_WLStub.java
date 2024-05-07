package weblogic.transaction.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.transaction.TransactionHelper;

public final class CoordinatorImpl_1036_WLStub extends Stub implements StubInfoIntf, Coordinator3, Coordinator2, Coordinator, CoordinatorOneway2, CoordinatorOneway, CoordinatorService, SubCoordinator3, SubCoordinator2, SubCoordinator, SubCoordinatorOneway3, SubCoordinatorOneway2, SubCoordinatorOneway, NotificationBroadcaster, NotificationListener, SubCoordinatorRM, SubCoordinatorOneway4, SubCoordinatorOneway5, SubCoordinatorOneway6 {
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md12;
   private static RuntimeMethodDescriptor md11;
   private static RuntimeMethodDescriptor md10;
   private static boolean initialized;
   private static Method[] m;
   private final StubInfo stubinfo;
   // $FF: synthetic field
   private static Class class$weblogic$transaction$internal$Coordinator3;
   private static RuntimeMethodDescriptor md44;
   private static RuntimeMethodDescriptor md43;
   private static RuntimeMethodDescriptor md42;
   private static RuntimeMethodDescriptor md41;
   private static RuntimeMethodDescriptor md40;
   private static RuntimeMethodDescriptor md19;
   private static RuntimeMethodDescriptor md18;
   private static RuntimeMethodDescriptor md17;
   private static RuntimeMethodDescriptor md16;
   private static RuntimeMethodDescriptor md15;
   private static RuntimeMethodDescriptor md14;
   private static RuntimeMethodDescriptor md13;
   private static RuntimeMethodDescriptor md39;
   private static RuntimeMethodDescriptor md38;
   private static RuntimeMethodDescriptor md37;
   private static RuntimeMethodDescriptor md36;
   private static RuntimeMethodDescriptor md35;
   private static RuntimeMethodDescriptor md34;
   private static RuntimeMethodDescriptor md33;
   private static RuntimeMethodDescriptor md32;
   private static RuntimeMethodDescriptor md31;
   private static RuntimeMethodDescriptor md30;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md29;
   private static RuntimeMethodDescriptor md28;
   private static RuntimeMethodDescriptor md27;
   private static RuntimeMethodDescriptor md26;
   private static RuntimeMethodDescriptor md25;
   private static RuntimeMethodDescriptor md24;
   private static RuntimeMethodDescriptor md23;
   private static RuntimeMethodDescriptor md22;
   private static RuntimeMethodDescriptor md21;
   private static RuntimeMethodDescriptor md20;
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
         md0 = new MethodDescriptor(m[0], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, true, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
         md20 = new MethodDescriptor(m[20], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[20]), var0.getRemoteRef().getObjectID());
         md21 = new MethodDescriptor(m[21], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[21]), var0.getRemoteRef().getObjectID());
         md22 = new MethodDescriptor(m[22], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[22]), var0.getRemoteRef().getObjectID());
         md23 = new MethodDescriptor(m[23], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[23]), var0.getRemoteRef().getObjectID());
         md24 = new MethodDescriptor(m[24], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[24]), var0.getRemoteRef().getObjectID());
         md25 = new MethodDescriptor(m[25], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[25]), var0.getRemoteRef().getObjectID());
         md26 = new MethodDescriptor(m[26], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[26]), var0.getRemoteRef().getObjectID());
         md27 = new MethodDescriptor(m[27], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[27]), var0.getRemoteRef().getObjectID());
         md28 = new MethodDescriptor(m[28], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[28]), var0.getRemoteRef().getObjectID());
         md29 = new MethodDescriptor(m[29], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[29]), var0.getRemoteRef().getObjectID());
         md30 = new MethodDescriptor(m[30], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[30]), var0.getRemoteRef().getObjectID());
         md31 = new MethodDescriptor(m[31], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[31]), var0.getRemoteRef().getObjectID());
         md32 = new MethodDescriptor(m[32], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[32]), var0.getRemoteRef().getObjectID());
         md33 = new MethodDescriptor(m[33], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[33]), var0.getRemoteRef().getObjectID());
         md34 = new MethodDescriptor(m[34], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[34]), var0.getRemoteRef().getObjectID());
         md35 = new MethodDescriptor(m[35], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[35]), var0.getRemoteRef().getObjectID());
         md36 = new MethodDescriptor(m[36], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[36]), var0.getRemoteRef().getObjectID());
         md37 = new MethodDescriptor(m[37], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[37]), var0.getRemoteRef().getObjectID());
         md38 = new MethodDescriptor(m[38], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[38]), var0.getRemoteRef().getObjectID());
         md39 = new MethodDescriptor(m[39], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, true, false, false, false, var0.getTimeOut(m[39]), var0.getRemoteRef().getObjectID());
         md40 = new MethodDescriptor(m[40], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[40]), var0.getRemoteRef().getObjectID());
         md41 = new MethodDescriptor(m[41], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[41]), var0.getRemoteRef().getObjectID());
         md42 = new MethodDescriptor(m[42], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[42]), var0.getRemoteRef().getObjectID());
         md43 = new MethodDescriptor(m[43], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[43]), var0.getRemoteRef().getObjectID());
         md44 = new MethodDescriptor(m[44], class$weblogic$transaction$internal$Coordinator3 == null ? (class$weblogic$transaction$internal$Coordinator3 = class$("weblogic.transaction.internal.Coordinator3")) : class$weblogic$transaction$internal$Coordinator3, false, false, false, false, var0.getTimeOut(m[44]), var0.getRemoteRef().getObjectID());
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

   public final void ackCommit(Xid var1, String var2) throws RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md0, var4, m[0]);
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

   public final void ackCommit(Xid var1, String var2, String[] var3) throws RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         this.ror.invoke((Remote)null, md1, var5, m[1]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

   }

   public final void ackPrePrepare(PropagationContext var1) throws RemoteException {
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

   public final void ackPrepare(Xid var1, String var2, int var3) throws RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, var2, new Integer(var3)};
         this.ror.invoke((Remote)null, md3, var5, m[3]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

   }

   public final void ackRollback(Xid var1, String var2) throws RemoteException {
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

   public final void ackRollback(Xid var1, String var2, String[] var3) throws RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         this.ror.invoke((Remote)null, md5, var5, m[5]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

   }

   public final void addNotificationListener(NotificationListener var1, Object var2) throws IllegalArgumentException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md6, var4, m[6]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (IllegalArgumentException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void checkStatus(Xid[] var1, String var2) throws RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md7, var4, m[7]);
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

   public final void commit(PropagationContext var1) throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, SystemException, IllegalStateException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md8, var3, m[8]);
      } catch (Error var16) {
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (RollbackException var18) {
         throw var18;
      } catch (HeuristicMixedException var19) {
         throw var19;
      } catch (HeuristicRollbackException var20) {
         throw var20;
      } catch (SecurityException var21) {
         throw var21;
      } catch (SystemException var22) {
         throw var22;
      } catch (IllegalStateException var23) {
         throw var23;
      } catch (RemoteException var24) {
         throw var24;
      } catch (Throwable var25) {
         throw new RemoteRuntimeException("Unexpected Exception", var25);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void forceGlobalCommit(Xid var1) throws SystemException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md9, var3, m[9]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (SystemException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void forceGlobalRollback(Xid var1) throws SystemException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md10, var3, m[10]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (SystemException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void forceLocalCommit(Xid var1) throws SystemException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md11, var3, m[11]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (SystemException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void forceLocalRollback(Xid var1) throws SystemException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md12, var3, m[12]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (SystemException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final Map getProperties() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Map var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (Map)this.ror.invoke((Remote)null, md13, var2, m[13]);
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

   public final Map getProperties(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Map var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Map)this.ror.invoke((Remote)null, md14, var3, m[14]);
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

   public final Map getSubCoordinatorInfo(String var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Map var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Map)this.ror.invoke((Remote)null, md15, var3, m[15]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final void handleNotification(Notification var1, Object var2) {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md16, var4, m[16]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final Object invokeCoordinatorService(String var1, Object var2) throws RemoteException, SystemException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         return (Object)this.ror.invoke((Remote)null, md17, var3, m[17]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (SystemException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }

   public final void nakCommit(Xid var1, String var2, short var3, String var4) throws RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, new Short(var3), var4};
         this.ror.invoke((Remote)null, md18, var6, m[18]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void nakCommit(Xid var1, String var2, short var3, String var4, String[] var5, String[] var6) throws RemoteException {
      Transaction var7 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var8 = new Object[]{var1, var2, new Short(var3), var4, var5, var6};
         this.ror.invoke((Remote)null, md19, var8, m[19]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (RemoteException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var7);
      }

   }

   public final void nakRollback(Xid var1, String var2, short var3, String var4) throws RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, new Short(var3), var4};
         this.ror.invoke((Remote)null, md20, var6, m[20]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void nakRollback(Xid var1, String var2, short var3, String var4, String[] var5, String[] var6) throws RemoteException {
      Transaction var7 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var8 = new Object[]{var1, var2, new Short(var3), var4, var5, var6};
         this.ror.invoke((Remote)null, md21, var8, m[21]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (RemoteException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var7);
      }

   }

   public final void nonXAResourceCommit(Xid var1, boolean var2, String var3) throws SystemException, RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, new Boolean(var2), var3};
         this.ror.invoke((Remote)null, md22, var5, m[22]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (SystemException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

   }

   public final Xid[] recover(String var1, String var2) throws SystemException, RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Xid[] var19;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var19 = (Xid[])this.ror.invoke((Remote)null, md23, var4, m[23]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (SystemException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var19;
   }

   public final void removeNotificationListener(NotificationListener var1) throws ListenerNotFoundException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md24, var3, m[24]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (ListenerNotFoundException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void rollback(String var1, Xid[] var2) throws SystemException, RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md25, var4, m[25]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (SystemException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void rollback(PropagationContext var1) throws SystemException, IllegalStateException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md26, var3, m[26]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (SystemException var14) {
         throw var14;
      } catch (IllegalStateException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5) throws RemoteException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var7 = new Object[]{var1, var2, var3, new Boolean(var4), new Boolean(var5)};
         this.ror.invoke((Remote)null, md27, var7, m[27]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var6);
      }

   }

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5, AuthenticatedUser var6) throws RemoteException {
      Transaction var7 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var8 = new Object[]{var1, var2, var3, new Boolean(var4), new Boolean(var5), var6};
         this.ror.invoke((Remote)null, md28, var8, m[28]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (RemoteException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var7);
      }

   }

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5, AuthenticatedUser var6, Map var7) throws RemoteException {
      Transaction var8 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var9 = new Object[]{var1, var2, var3, new Boolean(var4), new Boolean(var5), var6, var7};
         this.ror.invoke((Remote)null, md29, var9, m[29]);
      } catch (Error var16) {
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (RemoteException var18) {
         throw var18;
      } catch (Throwable var19) {
         throw new RemoteRuntimeException("Unexpected Exception", var19);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var8);
      }

   }

   public final void startPrePrepareAndChain(PropagationContext var1, int var2) throws RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, new Integer(var2)};
         this.ror.invoke((Remote)null, md30, var4, m[30]);
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

   public final void startPrepare(Xid var1, String var2, String[] var3, int var4) throws RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, var3, new Integer(var4)};
         this.ror.invoke((Remote)null, md31, var6, m[31]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void startPrepare(Xid var1, String var2, String[] var3, int var4, Map var5) throws RemoteException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var7 = new Object[]{var1, var2, var3, new Integer(var4), var5};
         this.ror.invoke((Remote)null, md32, var7, m[32]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var6);
      }

   }

   public final void startRollback(Xid var1, String var2, String[] var3) throws RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         this.ror.invoke((Remote)null, md33, var5, m[33]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (RemoteException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

   }

   public final void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4) throws RemoteException {
      Transaction var5 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var6 = new Object[]{var1, var2, var3, var4};
         this.ror.invoke((Remote)null, md34, var6, m[34]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var5);
      }

   }

   public final void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5) throws RemoteException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var7 = new Object[]{var1, var2, var3, var4, var5};
         this.ror.invoke((Remote)null, md35, var7, m[35]);
      } catch (Error var14) {
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (RemoteException var16) {
         throw var16;
      } catch (Throwable var17) {
         throw new RemoteRuntimeException("Unexpected Exception", var17);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var6);
      }

   }

   public final void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5, Map var6) throws RemoteException {
      Transaction var7 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var8 = new Object[]{var1, var2, var3, var4, var5, var6};
         this.ror.invoke((Remote)null, md36, var8, m[36]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (RemoteException var17) {
         throw var17;
      } catch (Throwable var18) {
         throw new RemoteRuntimeException("Unexpected Exception", var18);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var7);
      }

   }

   public final void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5, Map var6, boolean var7) throws RemoteException {
      Transaction var8 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var9 = new Object[]{var1, var2, var3, var4, var5, var6, new Boolean(var7)};
         this.ror.invoke((Remote)null, md37, var9, m[37]);
      } catch (Error var16) {
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (RemoteException var18) {
         throw var18;
      } catch (Throwable var19) {
         throw new RemoteRuntimeException("Unexpected Exception", var19);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var8);
      }

   }

   public final void startRollback(PropagationContext var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md38, var3, m[38]);
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

   public final void startRollback(Xid[] var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md39, var3, m[39]);
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

   public final void xaCommit(Xid var1) throws RemoteException, XAException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md40, var3, m[40]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (XAException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void xaForget(Xid var1) throws RemoteException, XAException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md41, var3, m[41]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (XAException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void xaPrepare(PropagationContext var1) throws RemoteException, XAException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md42, var3, m[42]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (XAException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final Xid[] xaRecover() throws RemoteException, XAException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Xid[] var17;
      try {
         Object[] var2 = new Object[0];
         var17 = (Xid[])this.ror.invoke((Remote)null, md43, var2, m[43]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (XAException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var17;
   }

   public final void xaRollback(Xid var1) throws RemoteException, XAException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md44, var3, m[44]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (XAException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }
}
