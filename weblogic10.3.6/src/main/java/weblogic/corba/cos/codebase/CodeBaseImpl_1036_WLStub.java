package weblogic.corba.cos.codebase;

import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.transaction.Transaction;
import org.omg.CORBA.Object;
import org.omg.CORBA.Repository;
import org.omg.CORBA.ValueDefPackage.FullValueDescription;
import org.omg.SendingContext.CodeBase;
import org.omg.SendingContext.RunTime;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class CodeBaseImpl_1036_WLStub extends Stub implements StubInfoIntf, CodeBase, RunTime, Object {
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md4;
   private static Method[] m;
   // $FF: synthetic field
   private static Class class$org$omg$SendingContext$CodeBase;

   public CodeBaseImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$org$omg$SendingContext$CodeBase == null ? (class$org$omg$SendingContext$CodeBase = class$("org.omg.SendingContext.CodeBase")) : class$org$omg$SendingContext$CodeBase, false, false, false, true, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
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

   public final String[] bases(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String[] var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (String[])this.ror.invoke((Remote)null, md0, var3, m[0]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final Repository get_ir() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Repository var13;
      try {
         java.lang.Object[] var2 = new java.lang.Object[0];
         var13 = (Repository)this.ror.invoke((Remote)null, md1, var2, m[1]);
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

   public final String implementation(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (String)this.ror.invoke((Remote)null, md2, var3, m[2]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final String[] implementations(String[] var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String[] var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (String[])this.ror.invoke((Remote)null, md3, var3, m[3]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final String implementationx(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (String)this.ror.invoke((Remote)null, md4, var3, m[4]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final FullValueDescription meta(String var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      FullValueDescription var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (FullValueDescription)this.ror.invoke((Remote)null, md5, var3, m[5]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }

   public final FullValueDescription[] metas(String[] var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      FullValueDescription[] var14;
      try {
         java.lang.Object[] var3 = new java.lang.Object[]{var1};
         var14 = (FullValueDescription[])this.ror.invoke((Remote)null, md6, var3, m[6]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var14;
   }
}
