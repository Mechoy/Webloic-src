package weblogic.security.acl.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.corba.rmi.Stub;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.security.acl.SecurityService;
import weblogic.security.acl.UserInfo;

public final class SecurityServiceImpl_IIOP_WLStub extends Stub implements StubInfoIntf, SecurityService {
   // $FF: synthetic field
   private static Class class$weblogic$security$acl$SecurityService;
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   private static boolean initialized;

   public SecurityServiceImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$security$acl$SecurityService == null ? (class$weblogic$security$acl$SecurityService = class$("weblogic.security.acl.SecurityService")) : class$weblogic$security$acl$SecurityService, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
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

   public final AuthenticatedUser authenticate(UserInfo var1) throws RemoteException {
      try {
         Object[] var2 = new Object[]{var1};
         return (AuthenticatedUser)this.ror.invoke((Remote)null, md0, var2, m[0]);
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
}
