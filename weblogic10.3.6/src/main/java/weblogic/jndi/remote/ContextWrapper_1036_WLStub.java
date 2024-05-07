package weblogic.jndi.remote;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.transaction.Transaction;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;
import weblogic.transaction.TransactionHelper;

public final class ContextWrapper_1036_WLStub extends Stub implements StubInfoIntf, RemoteContext {
   private static RuntimeMethodDescriptor md19;
   private static RuntimeMethodDescriptor md18;
   private static RuntimeMethodDescriptor md17;
   private static RuntimeMethodDescriptor md16;
   private static RuntimeMethodDescriptor md15;
   private static RuntimeMethodDescriptor md14;
   private static RuntimeMethodDescriptor md13;
   private static RuntimeMethodDescriptor md12;
   private static RuntimeMethodDescriptor md11;
   private static RuntimeMethodDescriptor md10;
   // $FF: synthetic field
   private static Class class$weblogic$jndi$remote$RemoteContext;
   private static Method[] m;
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md28;
   private static RuntimeMethodDescriptor md27;
   private static RuntimeMethodDescriptor md26;
   private static RuntimeMethodDescriptor md25;
   private static RuntimeMethodDescriptor md24;
   private static RuntimeMethodDescriptor md23;
   private static RuntimeMethodDescriptor md22;
   private static RuntimeMethodDescriptor md21;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md20;
   private final StubInfo stubinfo;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public ContextWrapper_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
         md20 = new MethodDescriptor(m[20], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[20]), var0.getRemoteRef().getObjectID());
         md21 = new MethodDescriptor(m[21], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[21]), var0.getRemoteRef().getObjectID());
         md22 = new MethodDescriptor(m[22], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[22]), var0.getRemoteRef().getObjectID());
         md23 = new MethodDescriptor(m[23], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[23]), var0.getRemoteRef().getObjectID());
         md24 = new MethodDescriptor(m[24], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[24]), var0.getRemoteRef().getObjectID());
         md25 = new MethodDescriptor(m[25], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[25]), var0.getRemoteRef().getObjectID());
         md26 = new MethodDescriptor(m[26], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[26]), var0.getRemoteRef().getObjectID());
         md27 = new MethodDescriptor(m[27], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[27]), var0.getRemoteRef().getObjectID());
         md28 = new MethodDescriptor(m[28], class$weblogic$jndi$remote$RemoteContext == null ? (class$weblogic$jndi$remote$RemoteContext = class$("weblogic.jndi.remote.RemoteContext")) : class$weblogic$jndi$remote$RemoteContext, false, false, false, false, var0.getTimeOut(m[28]), var0.getRemoteRef().getObjectID());
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

   public final Object addToEnvironment(String var1, Object var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (Object)this.ror.invoke((Remote)null, md0, var4, m[0]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (NamingException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var17;
   }

   public final void bind(String var1, Object var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md1, var4, m[1]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void bind(Name var1, Object var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md2, var4, m[2]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void close() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md3, var2, m[3]);
      } catch (Error var9) {
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (NamingException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new RemoteRuntimeException("Unexpected Exception", var12);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final String composeName(String var1, String var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (String)this.ror.invoke((Remote)null, md4, var4, m[4]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (NamingException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var17;
   }

   public final Name composeName(Name var1, Name var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Name var17;
      try {
         Object[] var4 = new Object[]{var1, var2};
         var17 = (Name)this.ror.invoke((Remote)null, md5, var4, m[5]);
      } catch (Error var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (NamingException var14) {
         throw var14;
      } catch (Throwable var15) {
         throw new RemoteRuntimeException("Unexpected Exception", var15);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var17;
   }

   public final Context createSubcontext(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Context var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Context)this.ror.invoke((Remote)null, md6, var3, m[6]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final Context createSubcontext(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Context var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Context)this.ror.invoke((Remote)null, md7, var3, m[7]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final void destroySubcontext(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md8, var3, m[8]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (NamingException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void destroySubcontext(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md9, var3, m[9]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (NamingException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final Hashtable getEnvironment() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Hashtable var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (Hashtable)this.ror.invoke((Remote)null, md10, var2, m[10]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (NamingException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final String getNameInNamespace() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (String)this.ror.invoke((Remote)null, md11, var2, m[11]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (NamingException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

      return var15;
   }

   public final NameParser getNameParser(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NameParser var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (NameParser)this.ror.invoke((Remote)null, md12, var3, m[12]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final NameParser getNameParser(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NameParser var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (NameParser)this.ror.invoke((Remote)null, md13, var3, m[13]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final NamingEnumeration list(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NamingEnumeration var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (NamingEnumeration)this.ror.invoke((Remote)null, md14, var3, m[14]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final NamingEnumeration list(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NamingEnumeration var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (NamingEnumeration)this.ror.invoke((Remote)null, md15, var3, m[15]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final NamingEnumeration listBindings(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NamingEnumeration var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (NamingEnumeration)this.ror.invoke((Remote)null, md16, var3, m[16]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final NamingEnumeration listBindings(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NamingEnumeration var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (NamingEnumeration)this.ror.invoke((Remote)null, md17, var3, m[17]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final Object lookup(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Object)this.ror.invoke((Remote)null, md18, var3, m[18]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final Object lookup(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Object)this.ror.invoke((Remote)null, md19, var3, m[19]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final Object lookupLink(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Object)this.ror.invoke((Remote)null, md20, var3, m[20]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final Object lookupLink(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Object)this.ror.invoke((Remote)null, md21, var3, m[21]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final void rebind(String var1, Object var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md22, var4, m[22]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void rebind(Name var1, Object var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md23, var4, m[23]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final Object removeFromEnvironment(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{var1};
         var16 = (Object)this.ror.invoke((Remote)null, md24, var3, m[24]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

      return var16;
   }

   public final void rename(String var1, String var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md25, var4, m[25]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void rename(Name var1, Name var2) throws NamingException {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md26, var4, m[26]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (NamingException var13) {
         throw var13;
      } catch (Throwable var14) {
         throw new RemoteRuntimeException("Unexpected Exception", var14);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

   }

   public final void unbind(String var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md27, var3, m[27]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (NamingException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }

   public final void unbind(Name var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var3 = new Object[]{var1};
         this.ror.invoke((Remote)null, md28, var3, m[28]);
      } catch (Error var10) {
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (NamingException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var2);
      }

   }
}
