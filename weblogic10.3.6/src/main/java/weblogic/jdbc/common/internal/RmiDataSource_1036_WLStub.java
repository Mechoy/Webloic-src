package weblogic.jdbc.common.internal;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import oracle.ucp.ConnectionLabelingCallback;
import oracle.ucp.jdbc.ConnectionInitializationCallback;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;

public final class RmiDataSource_1036_WLStub extends Stub implements StubInfoIntf, RemoteDataSource {
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
   private final StubInfo stubinfo;
   private static RuntimeMethodDescriptor md4;
   private static Method[] m;
   private static RuntimeMethodDescriptor md13;
   // $FF: synthetic field
   private static Class class$weblogic$jdbc$common$internal$RemoteDataSource;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public RmiDataSource_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$jdbc$common$internal$RemoteDataSource == null ? (class$weblogic$jdbc$common$internal$RemoteDataSource = class$("weblogic.jdbc.common.internal.RemoteDataSource")) : class$weblogic$jdbc$common$internal$RemoteDataSource, false, true, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
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

   public final Connection getConnection() throws SQLException {
      try {
         Object[] var1 = new Object[0];
         return (Connection)this.ror.invoke((Remote)null, md0, var1, m[0]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (SQLException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }

   public final Connection getConnection(String var1, String var2) throws SQLException {
      try {
         Object[] var3 = new Object[]{var1, var2};
         return (Connection)this.ror.invoke((Remote)null, md1, var3, m[1]);
      } catch (Error var4) {
         throw var4;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (SQLException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new RemoteRuntimeException("Unexpected Exception", var7);
      }
   }

   public final Connection getConnection(String var1, String var2, Properties var3) throws SQLException {
      try {
         Object[] var4 = new Object[]{var1, var2, var3};
         return (Connection)this.ror.invoke((Remote)null, md2, var4, m[2]);
      } catch (Error var5) {
         throw var5;
      } catch (RuntimeException var6) {
         throw var6;
      } catch (SQLException var7) {
         throw var7;
      } catch (Throwable var8) {
         throw new RemoteRuntimeException("Unexpected Exception", var8);
      }
   }

   public final Connection getConnection(Properties var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Connection)this.ror.invoke((Remote)null, md3, var2, m[3]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final PrintWriter getLogWriter() throws SQLException {
      try {
         Object[] var1 = new Object[0];
         return (PrintWriter)this.ror.invoke((Remote)null, md4, var1, m[4]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (SQLException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }

   public final int getLoginTimeout() throws SQLException {
      try {
         Object[] var1 = new Object[0];
         return (Integer)this.ror.invoke((Remote)null, md5, var1, m[5]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (SQLException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }

   public final boolean isWrapperFor(Class var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Boolean)this.ror.invoke((Remote)null, md6, var2, m[6]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final void registerConnectionInitializationCallback(ConnectionInitializationCallback var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md7, var2, m[7]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final void registerConnectionLabelingCallback(ConnectionLabelingCallback var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md8, var2, m[8]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final void removeConnectionLabelingCallback() throws SQLException {
      try {
         Object[] var1 = new Object[0];
         this.ror.invoke((Remote)null, md9, var1, m[9]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (SQLException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }

   public final void setLogWriter(PrintWriter var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         this.ror.invoke((Remote)null, md10, var2, m[10]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final void setLoginTimeout(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         this.ror.invoke((Remote)null, md11, var2, m[11]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }

   public final void unregisterConnectionInitializationCallback() throws SQLException {
      try {
         Object[] var1 = new Object[0];
         this.ror.invoke((Remote)null, md12, var1, m[12]);
      } catch (Error var2) {
         throw var2;
      } catch (RuntimeException var3) {
         throw var3;
      } catch (SQLException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new RemoteRuntimeException("Unexpected Exception", var5);
      }
   }

   public final Object unwrap(Class var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Object)this.ror.invoke((Remote)null, md13, var2, m[13]);
      } catch (Error var3) {
         throw var3;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RemoteRuntimeException("Unexpected Exception", var6);
      }
   }
}
