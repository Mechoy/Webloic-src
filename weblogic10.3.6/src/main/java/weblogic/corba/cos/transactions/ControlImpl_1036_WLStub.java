package weblogic.corba.cos.transactions;

import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.transaction.Transaction;
import org.omg.CORBA.Object;
import org.omg.CosTransactions.Control;
import org.omg.CosTransactions.Coordinator;
import org.omg.CosTransactions.Terminator;
import org.omg.CosTransactions.Unavailable;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class ControlImpl_1036_WLStub extends Stub implements StubInfoIntf, Control, Object {
   private static Method[] m;
   // $FF: synthetic field
   private static Class class$org$omg$CosTransactions$Control;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md1;
   private static boolean initialized;

   public ControlImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$org$omg$CosTransactions$Control == null ? (class$org$omg$CosTransactions$Control = class$("org.omg.CosTransactions.Control")) : class$org$omg$CosTransactions$Control, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$org$omg$CosTransactions$Control == null ? (class$org$omg$CosTransactions$Control = class$("org.omg.CosTransactions.Control")) : class$org$omg$CosTransactions$Control, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
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

   public final Coordinator get_coordinator() throws Unavailable {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Coordinator var15;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var15 = (Coordinator)this.ror.invoke((Remote)null, md0, var2, m[0]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Unavailable var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final Terminator get_terminator() throws Unavailable {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Terminator var15;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var15 = (Terminator)this.ror.invoke((Remote)null, md1, var2, m[1]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Unavailable var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }
}
