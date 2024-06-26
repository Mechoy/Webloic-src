package weblogic.corba.cos.transactions;

import java.lang.reflect.Method;
import java.rmi.Remote;
import org.omg.CORBA.Object;
import org.omg.CosTransactions.Control;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.TransactionFactory;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;

public final class TransactionFactoryImpl_1036_WLStub extends Stub implements StubInfoIntf, TransactionFactory, Object {
   // $FF: synthetic field
   private static Class class$org$omg$CosTransactions$TransactionFactory;
   private static Method[] m;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md1;
   private static boolean initialized;

   public TransactionFactoryImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$org$omg$CosTransactions$TransactionFactory == null ? (class$org$omg$CosTransactions$TransactionFactory = class$("org.omg.CosTransactions.TransactionFactory")) : class$org$omg$CosTransactions$TransactionFactory, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$org$omg$CosTransactions$TransactionFactory == null ? (class$org$omg$CosTransactions$TransactionFactory = class$("org.omg.CosTransactions.TransactionFactory")) : class$org$omg$CosTransactions$TransactionFactory, false, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
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

   public final Control create(int var1) {
      try {
         java.lang.Object[] var2 = new java.lang.Object[]{new Integer(var1)};
         return (Control)this.ror.invoke((Remote)null, md0, var2, m[0]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }

   public final Control recreate(PropagationContext var1) {
      try {
         java.lang.Object[] var2 = new java.lang.Object[]{var1};
         return (Control)this.ror.invoke((Remote)null, md1, var2, m[1]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }
}
