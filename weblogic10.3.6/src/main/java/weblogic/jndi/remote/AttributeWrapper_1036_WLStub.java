package weblogic.jndi.remote;

import java.lang.reflect.Method;
import java.rmi.Remote;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
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

public final class AttributeWrapper_1036_WLStub extends Stub implements StubInfoIntf, RemoteAttribute {
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md12;
   private static RuntimeMethodDescriptor md11;
   private static RuntimeMethodDescriptor md10;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md14;
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md15;
   private static Method[] m;
   private static RuntimeMethodDescriptor md13;
   // $FF: synthetic field
   private static Class class$weblogic$jndi$remote$RemoteAttribute;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public AttributeWrapper_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$jndi$remote$RemoteAttribute == null ? (class$weblogic$jndi$remote$RemoteAttribute = class$("weblogic.jndi.remote.RemoteAttribute")) : class$weblogic$jndi$remote$RemoteAttribute, false, false, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
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

   public final void add(int var1, Object var2) {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var4 = new Object[]{new Integer(var1), var2};
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

   public final boolean add(Object var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md1, var3, m[1]);
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

   public final void clear() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      try {
         Object[] var2 = new Object[0];
         this.ror.invoke((Remote)null, md2, var2, m[2]);
      } catch (Error var8) {
         throw var8;
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Throwable var10) {
         throw new RemoteRuntimeException("Unexpected Exception", var10);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var1);
      }

   }

   public final Object clone() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (Object)this.ror.invoke((Remote)null, md3, var2, m[3]);
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

   public final boolean contains(Object var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md4, var3, m[4]);
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

   public final Object get() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (Object)this.ror.invoke((Remote)null, md5, var2, m[5]);
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

   public final Object get(int var1) throws NamingException {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var16;
      try {
         Object[] var3 = new Object[]{new Integer(var1)};
         var16 = (Object)this.ror.invoke((Remote)null, md6, var3, m[6]);
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

   public final NamingEnumeration getAll() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      NamingEnumeration var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (NamingEnumeration)this.ror.invoke((Remote)null, md7, var2, m[7]);
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

   public final DirContext getAttributeDefinition() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      DirContext var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (DirContext)this.ror.invoke((Remote)null, md8, var2, m[8]);
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

   public final DirContext getAttributeSyntaxDefinition() throws NamingException {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      DirContext var15;
      try {
         Object[] var2 = new Object[0];
         var15 = (DirContext)this.ror.invoke((Remote)null, md9, var2, m[9]);
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

   public final String getID() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      String var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (String)this.ror.invoke((Remote)null, md10, var2, m[10]);
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

   public final boolean isOrdered() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (Boolean)this.ror.invoke((Remote)null, md11, var2, m[11]);
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

   public final Object remove(int var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var14;
      try {
         Object[] var3 = new Object[]{new Integer(var1)};
         var14 = (Object)this.ror.invoke((Remote)null, md12, var3, m[12]);
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

   public final boolean remove(Object var1) {
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      boolean var14;
      try {
         Object[] var3 = new Object[]{var1};
         var14 = (Boolean)this.ror.invoke((Remote)null, md13, var3, m[13]);
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

   public final Object set(int var1, Object var2) {
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      Object var15;
      try {
         Object[] var4 = new Object[]{new Integer(var1), var2};
         var15 = (Object)this.ror.invoke((Remote)null, md14, var4, m[14]);
      } catch (Error var11) {
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Throwable var13) {
         throw new RemoteRuntimeException("Unexpected Exception", var13);
      } finally {
         TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var3);
      }

      return var15;
   }

   public final int size() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

      int var13;
      try {
         Object[] var2 = new Object[0];
         var13 = (Integer)this.ror.invoke((Remote)null, md15, var2, m[15]);
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
}
