package weblogic.servlet.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.transaction.Transaction;
import weblogic.cluster.replication.ROID;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class ROIDLookupImpl_1036_WLStub extends Stub implements StubInfoIntf, ROIDLookup {
   private static Method[] m;
   // $FF: synthetic field
   private static Class class$weblogic$servlet$internal$ROIDLookup;
   private static RuntimeMethodDescriptor md2;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md1;
   private static boolean initialized;

   public ROIDLookupImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$servlet$internal$ROIDLookup == null ? (class$weblogic$servlet$internal$ROIDLookup = class$("weblogic.servlet.internal.ROIDLookup")) : class$weblogic$servlet$internal$ROIDLookup, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$servlet$internal$ROIDLookup == null ? (class$weblogic$servlet$internal$ROIDLookup = class$("weblogic.servlet.internal.ROIDLookup")) : class$weblogic$servlet$internal$ROIDLookup, true, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$servlet$internal$ROIDLookup == null ? (class$weblogic$servlet$internal$ROIDLookup = class$("weblogic.servlet.internal.ROIDLookup")) : class$weblogic$servlet$internal$ROIDLookup, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
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

   public final ROID lookupROID(String var1, String var2, String var3) throws RemoteException {
      Transaction var4 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ROID var18;
      try {
         Object[] var5 = new Object[]{var1, var2, var3};
         var18 = (ROID)this.ror.invoke((Remote)null, md0, var5, m[0]);
      } catch (Error var13) {
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (RemoteException var15) {
         throw var15;
      } catch (Throwable var16) {
         throw new RemoteRuntimeException("Unexpected Exception", var16);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var4);
      }

      return var18;
   }

   public final void unregister(ROID var1, Object[] var2) throws RemoteException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md1, var3, m[1]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void updateLastAccessTimes(ROID[] var1, long[] var2, long var3, String var5) throws RemoteException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var7 = new Object[]{var1, var2, new Long(var3), var5};
         this.ror.invoke((Remote)null, md2, var7, m[2]);
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
}
