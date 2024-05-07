package weblogic.rmi.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.corba.rmi.Stub;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.HeartbeatHelper;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.utils.Utilities;

public final class HeartbeatHelperImpl_IIOP_WLStub extends Stub implements StubInfoIntf, HeartbeatHelper {
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   // $FF: synthetic field
   private static Class class$weblogic$rmi$extensions$server$HeartbeatHelper;
   private final RemoteReference ror;
   private static boolean initialized;

   public HeartbeatHelperImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$rmi$extensions$server$HeartbeatHelper == null ? (class$weblogic$rmi$extensions$server$HeartbeatHelper = class$("weblogic.rmi.extensions.server.HeartbeatHelper")) : class$weblogic$rmi$extensions$server$HeartbeatHelper, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
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

   public final void ping() throws RemoteException {
      try {
         Object[] var1 = new Object[0];
         this.ror.invoke((Remote)null, md0, var1, m[0]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (RemoteException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }
}
