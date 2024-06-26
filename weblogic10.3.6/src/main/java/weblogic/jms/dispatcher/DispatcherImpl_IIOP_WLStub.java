package weblogic.jms.dispatcher;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.JMSException;
import weblogic.corba.rmi.Stub;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;

public final class DispatcherImpl_IIOP_WLStub extends Stub implements StubInfoIntf, DispatcherRemote, DispatcherOneWay {
   // $FF: synthetic field
   private static Class class$weblogic$jms$dispatcher$DispatcherRemote;
   private static RuntimeMethodDescriptor md5;
   private static Method[] m;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md4;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md1;
   private static boolean initialized;

   public DispatcherImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$jms$dispatcher$DispatcherRemote == null ? (class$weblogic$jms$dispatcher$DispatcherRemote = class$("weblogic.jms.dispatcher.DispatcherRemote")) : class$weblogic$jms$dispatcher$DispatcherRemote, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$jms$dispatcher$DispatcherRemote == null ? (class$weblogic$jms$dispatcher$DispatcherRemote = class$("weblogic.jms.dispatcher.DispatcherRemote")) : class$weblogic$jms$dispatcher$DispatcherRemote, false, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$jms$dispatcher$DispatcherRemote == null ? (class$weblogic$jms$dispatcher$DispatcherRemote = class$("weblogic.jms.dispatcher.DispatcherRemote")) : class$weblogic$jms$dispatcher$DispatcherRemote, true, true, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$jms$dispatcher$DispatcherRemote == null ? (class$weblogic$jms$dispatcher$DispatcherRemote = class$("weblogic.jms.dispatcher.DispatcherRemote")) : class$weblogic$jms$dispatcher$DispatcherRemote, false, true, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$jms$dispatcher$DispatcherRemote == null ? (class$weblogic$jms$dispatcher$DispatcherRemote = class$("weblogic.jms.dispatcher.DispatcherRemote")) : class$weblogic$jms$dispatcher$DispatcherRemote, false, true, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$jms$dispatcher$DispatcherRemote == null ? (class$weblogic$jms$dispatcher$DispatcherRemote = class$("weblogic.jms.dispatcher.DispatcherRemote")) : class$weblogic$jms$dispatcher$DispatcherRemote, false, true, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
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

   public final void dispatchAsyncFuture(Request var1, AsyncResult var2) throws RemoteException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md0, var3, m[0]);
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

   public final void dispatchAsyncTranFuture(Request var1, AsyncResult var2) throws RemoteException {
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

   public final void dispatchOneWay(Request var1) throws RemoteException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md2, var2, m[2]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final Response dispatchSyncFuture(Request var1) throws JMSException, RemoteException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Response)this.ror.invoke((Remote)null, md3, var2, m[3]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (JMSException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final Response dispatchSyncNoTranFuture(Request var1) throws JMSException, RemoteException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Response)this.ror.invoke((Remote)null, md4, var2, m[4]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (JMSException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final Response dispatchSyncTranFuture(Request var1) throws RemoteException, JMSException, DispatcherException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Response)this.ror.invoke((Remote)null, md5, var2, m[5]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (JMSException var6) {
         throw var6;
      } catch (DispatcherException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }
}
