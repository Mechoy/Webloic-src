package weblogic.cluster.singleton;

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

public final class ServerMigrationCoordinatorImpl_IIOP_WLStub extends Stub implements StubInfoIntf, ServerMigrationCoordinator {
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$singleton$ServerMigrationCoordinator;
   private final RemoteReference ror;
   private static boolean initialized;

   public ServerMigrationCoordinatorImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$cluster$singleton$ServerMigrationCoordinator == null ? (class$weblogic$cluster$singleton$ServerMigrationCoordinator = class$("weblogic.cluster.singleton.ServerMigrationCoordinator")) : class$weblogic$cluster$singleton$ServerMigrationCoordinator, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
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

   public final void migrate(String var1, String var2, String var3, boolean var4, boolean var5) throws ServerMigrationException, RemoteException {
      Transaction var6 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var7 = new Object[]{var1, var2, var3, new Boolean(var4), new Boolean(var5)};
         this.ror.invoke((Remote)null, md0, var7, m[0]);
      } catch (Error var15) {
         throw var15;
      } catch (RuntimeException var16) {
         throw var16;
      } catch (ServerMigrationException var17) {
         throw var17;
      } catch (RemoteException var18) {
         throw var18;
      } catch (Throwable var19) {
         throw new RemoteRuntimeException("Unexpected Exception", var19);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var6);
      }

   }
}
