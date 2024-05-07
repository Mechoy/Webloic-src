package weblogic.transaction.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
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

public final class SubCoordinatorImpl_1036_WLStub extends Stub implements StubInfoIntf, SubCoordinator3, SubCoordinator2, SubCoordinator, SubCoordinatorOneway3, SubCoordinatorOneway2, SubCoordinatorOneway, NotificationBroadcaster, NotificationListener, SubCoordinatorRM, SubCoordinatorOneway4, SubCoordinatorOneway5, SubCoordinatorOneway6 {
   private static RuntimeMethodDescriptor md19;
   private static RuntimeMethodDescriptor md18;
   private static RuntimeMethodDescriptor md17;
   private static RuntimeMethodDescriptor md16;
   private static RuntimeMethodDescriptor md15;
   private static RuntimeMethodDescriptor md14;
   private static RuntimeMethodDescriptor md13;
   private static RuntimeMethodDescriptor md12;
   private static RuntimeMethodDescriptor md11;
   private static RuntimeMethodDescriptor md10;
   private static Method[] m;
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   // $FF: synthetic field
   private static Class class$weblogic$transaction$internal$SubCoordinator3;
   private static RuntimeMethodDescriptor md21;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md20;
   private final StubInfo stubinfo;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public SubCoordinatorImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
         md20 = new MethodDescriptor(m[20], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[20]), var0.getRemoteRef().getObjectID());
         md21 = new MethodDescriptor(m[21], class$weblogic$transaction$internal$SubCoordinator3 == null ? (class$weblogic$transaction$internal$SubCoordinator3 = class$("weblogic.transaction.internal.SubCoordinator3")) : class$weblogic$transaction$internal$SubCoordinator3, true, false, false, false, var0.getTimeOut(m[21]), var0.getRemoteRef().getObjectID());
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

   public final void addNotificationListener(NotificationListener var1, Object var2) throws IllegalArgumentException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md0, var4, m[0]);
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

   public final void forceLocalCommit(Xid var1) throws SystemException, RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md1, var3, m[1]);
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
         this.ror.invoke((Remote)null, md2, var3, m[2]);
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

   public final Map getProperties(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Map var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Map)this.ror.invoke((Remote)null, md3, var3, m[3]);
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
         var16 = (Map)this.ror.invoke((Remote)null, md4, var3, m[4]);
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
         this.ror.invoke((Remote)null, md5, var4, m[5]);
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

   public final void nonXAResourceCommit(Xid var1, boolean var2, String var3) throws SystemException, RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var5 = new Object[]{var1, new Boolean(var2), var3};
         this.ror.invoke((Remote)null, md6, var5, m[6]);
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
         var19 = (Xid[])this.ror.invoke((Remote)null, md7, var4, m[7]);
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
         this.ror.invoke((Remote)null, md8, var3, m[8]);
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
         this.ror.invoke((Remote)null, md9, var4, m[9]);
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

   public final void startCommit(Xid var1, String var2, String[] var3, boolean var4, boolean var5) throws RemoteException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var7 = new Object[]{var1, var2, var3, new Boolean(var4), new Boolean(var5)};
         this.ror.invoke((Remote)null, md10, var7, m[10]);
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
         this.ror.invoke((Remote)null, md11, var8, m[11]);
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
         this.ror.invoke((Remote)null, md12, var9, m[12]);
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
         this.ror.invoke((Remote)null, md13, var4, m[13]);
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
         this.ror.invoke((Remote)null, md14, var6, m[14]);
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
         this.ror.invoke((Remote)null, md15, var7, m[15]);
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
         this.ror.invoke((Remote)null, md16, var5, m[16]);
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
         this.ror.invoke((Remote)null, md17, var6, m[17]);
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
         this.ror.invoke((Remote)null, md18, var7, m[18]);
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

   public final void startRollback(Xid var1, String var2, String[] var3, AuthenticatedUser var4, String[] var5, Map var6, boolean var7) throws RemoteException {
      Transaction var8 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var9 = new Object[]{var1, var2, var3, var4, var5, var6, new Boolean(var7)};
         this.ror.invoke((Remote)null, md20, var9, m[20]);
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

   public final void startRollback(Xid[] var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md21, var3, m[21]);
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
}
