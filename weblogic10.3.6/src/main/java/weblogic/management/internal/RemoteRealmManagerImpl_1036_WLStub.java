package weblogic.management.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import weblogic.management.configuration.ListResults;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;

public final class RemoteRealmManagerImpl_1036_WLStub extends Stub implements StubInfoIntf, RemoteRealmManager {
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
   private static Method[] m;
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private final RemoteReference ror;
   // $FF: synthetic field
   private static Class class$weblogic$management$internal$RemoteRealmManager;
   private final StubInfo stubinfo;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public RemoteRealmManagerImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$management$internal$RemoteRealmManager == null ? (class$weblogic$management$internal$RemoteRealmManager = class$("weblogic.management.internal.RemoteRealmManager")) : class$weblogic$management$internal$RemoteRealmManager, false, true, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
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

   public final boolean aclExists(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Boolean)this.ror.invoke((Remote)null, md0, var2, m[0]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void addMember(String var1, String var2) throws RemoteException, RemoteRealmException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md1, var3, m[1]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (RemoteRealmException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }

   public final boolean changeCredential(String var1, Object var2, Object var3) throws RemoteException, RemoteRealmException {
      try {
         Object[] var4 = new Object[]{var1, var2, var3};
         return (Boolean)this.ror.invoke((Remote)null, md2, var4, m[2]);
      } catch (Error var5) {
         throw var5;
      } catch (RuntimeException var6) {
         throw var6;
      } catch (RemoteException var7) {
         throw var7;
      } catch (RemoteRealmException var8) {
         throw var8;
      } catch (Throwable var9) {
         throw new RemoteRuntimeException("Unexpected Exception", var9);
      }
   }

   public final void createAcl(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md3, var2, m[3]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void createGroup(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md4, var2, m[4]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void createUser(String var1, Object var2) throws RemoteException, RemoteRealmException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md5, var3, m[5]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (RemoteRealmException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }

   public final ListResults getGrantees(String var1, String var2) throws RemoteException, RemoteRealmException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         return (ListResults)this.ror.invoke((Remote)null, md6, var3, m[6]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (RemoteRealmException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }

   public final ListResults getMembers(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         return (ListResults)this.ror.invoke((Remote)null, md7, var2, m[7]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final String[] getPermissions(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         return (String[])this.ror.invoke((Remote)null, md8, var2, m[8]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void grantPermission(String var1, String var2, String var3) throws RemoteException, RemoteRealmException {
      try {
         Object[] var4 = new Object[]{var1, var2, var3};
         this.ror.invoke((Remote)null, md9, var4, m[9]);
      } catch (Error var5) {
         throw var5;
      } catch (RuntimeException var6) {
         throw var6;
      } catch (RemoteException var7) {
         throw var7;
      } catch (RemoteRealmException var8) {
         throw var8;
      } catch (Throwable var9) {
         throw new RemoteRuntimeException("Unexpected Exception", var9);
      }
   }

   public final boolean groupExists(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Boolean)this.ror.invoke((Remote)null, md10, var2, m[10]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final ListResults listAcls() throws RemoteException, RemoteRealmException {
      try {
         Object[] var1 = new Object[0];
         return (ListResults)this.ror.invoke((Remote)null, md11, var1, m[11]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (RemoteException var4) {
         throw var4;
      } catch (RemoteRealmException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final ListResults listGroups() throws RemoteException, RemoteRealmException {
      try {
         Object[] var1 = new Object[0];
         return (ListResults)this.ror.invoke((Remote)null, md12, var1, m[12]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (RemoteException var4) {
         throw var4;
      } catch (RemoteRealmException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final ListResults listUsers() throws RemoteException, RemoteRealmException {
      try {
         Object[] var1 = new Object[0];
         return (ListResults)this.ror.invoke((Remote)null, md13, var1, m[13]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (RemoteException var4) {
         throw var4;
      } catch (RemoteRealmException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final void removeAcl(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md14, var2, m[14]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void removeGroup(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md15, var2, m[15]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void removeMember(String var1, String var2) throws RemoteException, RemoteRealmException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         this.ror.invoke((Remote)null, md16, var3, m[16]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (RemoteException var6) {
         throw var6;
      } catch (RemoteRealmException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }

   public final void removeUser(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md17, var2, m[17]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final void revokePermission(String var1, String var2, String var3) throws RemoteException, RemoteRealmException {
      try {
         Object[] var4 = new Object[]{var1, var2, var3};
         this.ror.invoke((Remote)null, md18, var4, m[18]);
      } catch (Error var5) {
         throw var5;
      } catch (RuntimeException var6) {
         throw var6;
      } catch (RemoteException var7) {
         throw var7;
      } catch (RemoteRealmException var8) {
         throw var8;
      } catch (Throwable var9) {
         throw new RemoteRuntimeException("Unexpected Exception", var9);
      }
   }

   public final boolean userExists(String var1) throws RemoteException, RemoteRealmException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Boolean)this.ror.invoke((Remote)null, md19, var2, m[19]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (RemoteException var5) {
         throw var5;
      } catch (RemoteRealmException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }
}
