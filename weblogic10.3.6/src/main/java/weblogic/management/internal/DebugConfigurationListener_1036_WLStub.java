package weblogic.management.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.management.Notification;
import javax.transaction.Transaction;
import weblogic.management.RemoteNotificationListener;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class DebugConfigurationListener_1036_WLStub extends Stub implements StubInfoIntf, RemoteNotificationListener {
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   // $FF: synthetic field
   private static Class class$weblogic$management$RemoteNotificationListener;
   private static boolean initialized;

   public DebugConfigurationListener_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$management$RemoteNotificationListener == null ? (class$weblogic$management$RemoteNotificationListener = class$("weblogic.management.RemoteNotificationListener")) : class$weblogic$management$RemoteNotificationListener, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
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

   public final void handleNotification(Notification var1, Object var2) {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md0, var4, m[0]);
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
}
