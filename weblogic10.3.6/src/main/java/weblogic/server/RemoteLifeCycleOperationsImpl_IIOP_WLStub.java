package weblogic.server;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.transaction.Transaction;
import weblogic.corba.rmi.Stub;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class RemoteLifeCycleOperationsImpl_IIOP_WLStub extends Stub implements StubInfoIntf, RemoteLifeCycleOperations {
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md10;
   private final RemoteReference ror;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md4;
   private static Method[] m;
   // $FF: synthetic field
   private static Class class$weblogic$server$RemoteLifeCycleOperations;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public RemoteLifeCycleOperationsImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, true, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$server$RemoteLifeCycleOperations == null ? (class$weblogic$server$RemoteLifeCycleOperations = class$("weblogic.server.RemoteLifeCycleOperations")) : class$weblogic$server$RemoteLifeCycleOperations, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
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

   public final void forceShutdown() throws ServerLifecycleException, RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md0, var2, m[0]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (ServerLifecycleException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final void forceSuspend() throws ServerLifecycleException, RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md1, var2, m[1]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (ServerLifecycleException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final String getMiddlewareHome() throws RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (String)this.ror.invoke((Remote)null, md2, var2, m[2]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (RemoteException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final String getState() throws RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (String)this.ror.invoke((Remote)null, md3, var2, m[3]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (RemoteException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final String getWeblogicHome() throws RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (String)this.ror.invoke((Remote)null, md4, var2, m[4]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (RemoteException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final void resume() throws ServerLifecycleException, RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md5, var2, m[5]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (ServerLifecycleException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final void setState(String var1, String var2) throws RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md6, var4, m[6]);
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

   public final void shutdown() throws ServerLifecycleException, RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md7, var2, m[7]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (ServerLifecycleException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final void shutdown(int var1, boolean var2) throws ServerLifecycleException, RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{new Integer(var1), new Boolean(var2)};
         this.ror.invoke((Remote)null, md8, var4, m[8]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (ServerLifecycleException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void suspend() throws ServerLifecycleException, RemoteException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md9, var2, m[9]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (ServerLifecycleException var12) {
         throw var12;
      } catch (RemoteException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final void suspend(int var1, boolean var2) throws ServerLifecycleException, RemoteException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{new Integer(var1), new Boolean(var2)};
         this.ror.invoke((Remote)null, md10, var4, m[10]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (ServerLifecycleException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }
}
