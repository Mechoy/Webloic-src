package weblogic.cluster.singleton;

import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.transaction.Transaction;
import weblogic.corba.rmi.Stub;
import weblogic.management.runtime.MigrationDataRuntimeMBean;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class DomainMigrationHistoryImpl_IIOP_WLStub extends Stub implements StubInfoIntf, DomainMigrationHistory {
   private static Method[] m;
   private static RuntimeMethodDescriptor md2;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$singleton$DomainMigrationHistory;
   private static RuntimeMethodDescriptor md1;
   private static boolean initialized;

   public DomainMigrationHistoryImpl_IIOP_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$cluster$singleton$DomainMigrationHistory == null ? (class$weblogic$cluster$singleton$DomainMigrationHistory = class$("weblogic.cluster.singleton.DomainMigrationHistory")) : class$weblogic$cluster$singleton$DomainMigrationHistory, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$cluster$singleton$DomainMigrationHistory == null ? (class$weblogic$cluster$singleton$DomainMigrationHistory = class$("weblogic.cluster.singleton.DomainMigrationHistory")) : class$weblogic$cluster$singleton$DomainMigrationHistory, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$cluster$singleton$DomainMigrationHistory == null ? (class$weblogic$cluster$singleton$DomainMigrationHistory = class$("weblogic.cluster.singleton.DomainMigrationHistory")) : class$weblogic$cluster$singleton$DomainMigrationHistory, true, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
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

   public final MigrationDataRuntimeMBean[] getMigrationDataRuntimes() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      MigrationDataRuntimeMBean[] var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (MigrationDataRuntimeMBean[])this.ror.invoke((Remote)null, md0, var2, m[0]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final ServiceMigrationDataRuntimeMBean[] getServiceMigrationDataRuntimes() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      ServiceMigrationDataRuntimeMBean[] var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (ServiceMigrationDataRuntimeMBean[])this.ror.invoke((Remote)null, md1, var2, m[1]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var13;
   }

   public final void update(MigrationData var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md2, var3, m[2]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Throwable var11) {
         throw new RemoteRuntimeException("Unexpected Exception", var11);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }
}
