package weblogic.cluster.singleton;

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
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class SingletonServicesManager_1036_WLStub extends Stub implements StubInfoIntf, RemoteSingletonServicesControl {
   private static Method[] m;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md4;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md1;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$singleton$RemoteSingletonServicesControl;
   private static boolean initialized;

   public SingletonServicesManager_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$cluster$singleton$RemoteSingletonServicesControl == null ? (class$weblogic$cluster$singleton$RemoteSingletonServicesControl = class$("weblogic.cluster.singleton.RemoteSingletonServicesControl")) : class$weblogic$cluster$singleton$RemoteSingletonServicesControl, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$cluster$singleton$RemoteSingletonServicesControl == null ? (class$weblogic$cluster$singleton$RemoteSingletonServicesControl = class$("weblogic.cluster.singleton.RemoteSingletonServicesControl")) : class$weblogic$cluster$singleton$RemoteSingletonServicesControl, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$cluster$singleton$RemoteSingletonServicesControl == null ? (class$weblogic$cluster$singleton$RemoteSingletonServicesControl = class$("weblogic.cluster.singleton.RemoteSingletonServicesControl")) : class$weblogic$cluster$singleton$RemoteSingletonServicesControl, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$cluster$singleton$RemoteSingletonServicesControl == null ? (class$weblogic$cluster$singleton$RemoteSingletonServicesControl = class$("weblogic.cluster.singleton.RemoteSingletonServicesControl")) : class$weblogic$cluster$singleton$RemoteSingletonServicesControl, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$cluster$singleton$RemoteSingletonServicesControl == null ? (class$weblogic$cluster$singleton$RemoteSingletonServicesControl = class$("weblogic.cluster.singleton.RemoteSingletonServicesControl")) : class$weblogic$cluster$singleton$RemoteSingletonServicesControl, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
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

   public final void activateService(String var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md0, var3, m[0]);
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

   public final void deactivateService(String var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md1, var3, m[1]);
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

   public final boolean isServiceActive(String var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Boolean)this.ror.invoke((Remote)null, md2, var3, m[2]);
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

   public final boolean isServiceRegistered(String var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Boolean)this.ror.invoke((Remote)null, md3, var3, m[3]);
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

   public final void restartService(String var1) throws RemoteException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md4, var3, m[4]);
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
