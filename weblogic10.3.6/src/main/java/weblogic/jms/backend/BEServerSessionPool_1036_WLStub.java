package weblogic.jms.backend;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;

public final class BEServerSessionPool_1036_WLStub extends Stub implements StubInfoIntf, BEServerSessionPoolRemote {
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   // $FF: synthetic field
   private static Class class$weblogic$jms$backend$BEServerSessionPoolRemote;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md1;
   private static boolean initialized;

   public BEServerSessionPool_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$jms$backend$BEServerSessionPoolRemote == null ? (class$weblogic$jms$backend$BEServerSessionPoolRemote = class$("weblogic.jms.backend.BEServerSessionPoolRemote")) : class$weblogic$jms$backend$BEServerSessionPoolRemote, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$jms$backend$BEServerSessionPoolRemote == null ? (class$weblogic$jms$backend$BEServerSessionPoolRemote = class$("weblogic.jms.backend.BEServerSessionPoolRemote")) : class$weblogic$jms$backend$BEServerSessionPoolRemote, false, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
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

   public final void close() throws JMSException, RemoteException {
      try {
         Object[] var1 = new Object[0];
         this.ror.invoke((Remote)null, md0, var1, m[0]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (JMSException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final ServerSession getServerSession(DispatcherId var1) throws JMSException, RemoteException {
      try {
         Object[] var2 = new Object[]{var1};
         return (ServerSession)this.ror.invoke((Remote)null, md1, var2, m[1]);
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
}
