package weblogic.jdbc.rmi.internal;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.sql.SQLException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.Stub;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.Utilities;

public final class ResultSetMetaDataImpl_1036_WLStub extends Stub implements StubInfoIntf, ResultSetMetaData {
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
   // $FF: synthetic field
   private static Class class$weblogic$jdbc$rmi$internal$ResultSetMetaData;
   private static RuntimeMethodDescriptor md6;
   private static RuntimeMethodDescriptor md5;
   private static RuntimeMethodDescriptor md4;
   private static RuntimeMethodDescriptor md3;
   private static RuntimeMethodDescriptor md2;
   private static RuntimeMethodDescriptor md1;
   private static RuntimeMethodDescriptor md0;
   private static RuntimeMethodDescriptor md22;
   private static RuntimeMethodDescriptor md21;
   private final RemoteReference ror;
   private static RuntimeMethodDescriptor md20;
   private final StubInfo stubinfo;
   private static boolean initialized;
   private static RuntimeMethodDescriptor md9;
   private static RuntimeMethodDescriptor md8;
   private static RuntimeMethodDescriptor md7;

   public ResultSetMetaDataImpl_1036_WLStub(StubInfo var1) {
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
         md0 = new MethodDescriptor(m[0], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[0]), var0.getRemoteRef().getObjectID());
         md1 = new MethodDescriptor(m[1], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[1]), var0.getRemoteRef().getObjectID());
         md2 = new MethodDescriptor(m[2], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[2]), var0.getRemoteRef().getObjectID());
         md3 = new MethodDescriptor(m[3], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[3]), var0.getRemoteRef().getObjectID());
         md4 = new MethodDescriptor(m[4], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[4]), var0.getRemoteRef().getObjectID());
         md5 = new MethodDescriptor(m[5], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[5]), var0.getRemoteRef().getObjectID());
         md6 = new MethodDescriptor(m[6], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[6]), var0.getRemoteRef().getObjectID());
         md7 = new MethodDescriptor(m[7], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[7]), var0.getRemoteRef().getObjectID());
         md8 = new MethodDescriptor(m[8], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[8]), var0.getRemoteRef().getObjectID());
         md9 = new MethodDescriptor(m[9], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[9]), var0.getRemoteRef().getObjectID());
         md10 = new MethodDescriptor(m[10], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[10]), var0.getRemoteRef().getObjectID());
         md11 = new MethodDescriptor(m[11], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[11]), var0.getRemoteRef().getObjectID());
         md12 = new MethodDescriptor(m[12], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[12]), var0.getRemoteRef().getObjectID());
         md13 = new MethodDescriptor(m[13], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[13]), var0.getRemoteRef().getObjectID());
         md14 = new MethodDescriptor(m[14], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[14]), var0.getRemoteRef().getObjectID());
         md15 = new MethodDescriptor(m[15], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[15]), var0.getRemoteRef().getObjectID());
         md16 = new MethodDescriptor(m[16], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[16]), var0.getRemoteRef().getObjectID());
         md17 = new MethodDescriptor(m[17], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[17]), var0.getRemoteRef().getObjectID());
         md18 = new MethodDescriptor(m[18], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[18]), var0.getRemoteRef().getObjectID());
         md19 = new MethodDescriptor(m[19], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[19]), var0.getRemoteRef().getObjectID());
         md20 = new MethodDescriptor(m[20], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[20]), var0.getRemoteRef().getObjectID());
         md21 = new MethodDescriptor(m[21], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[21]), var0.getRemoteRef().getObjectID());
         md22 = new MethodDescriptor(m[22], class$weblogic$jdbc$rmi$internal$ResultSetMetaData == null ? (class$weblogic$jdbc$rmi$internal$ResultSetMetaData = class$("weblogic.jdbc.rmi.internal.ResultSetMetaData")) : class$weblogic$jdbc$rmi$internal$ResultSetMetaData, false, true, false, false, var0.getTimeOut(m[22]), var0.getRemoteRef().getObjectID());
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

   public final String getCatalogName(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md0, var2, m[0]);
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

   public final String getColumnClassName(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md1, var2, m[1]);
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

   public final int getColumnCount() throws SQLException {
      try {
         Object[] var1 = new Object[0];
         return (Integer)this.ror.invoke((Remote)null, md2, var1, m[2]);
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

   public final int getColumnDisplaySize(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Integer)this.ror.invoke((Remote)null, md3, var2, m[3]);
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

   public final String getColumnLabel(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md4, var2, m[4]);
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

   public final String getColumnName(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md5, var2, m[5]);
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

   public final int getColumnType(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Integer)this.ror.invoke((Remote)null, md6, var2, m[6]);
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

   public final String getColumnTypeName(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md7, var2, m[7]);
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

   public final int getPrecision(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Integer)this.ror.invoke((Remote)null, md8, var2, m[8]);
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

   public final int getScale(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Integer)this.ror.invoke((Remote)null, md9, var2, m[9]);
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

   public final String getSchemaName(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md10, var2, m[10]);
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

   public final String getTableName(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (String)this.ror.invoke((Remote)null, md11, var2, m[11]);
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

   public final boolean isAutoIncrement(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md12, var2, m[12]);
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

   public final boolean isCaseSensitive(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md13, var2, m[13]);
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

   public final boolean isCurrency(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md14, var2, m[14]);
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

   public final boolean isDefinitelyWritable(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md15, var2, m[15]);
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

   public final int isNullable(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Integer)this.ror.invoke((Remote)null, md16, var2, m[16]);
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

   public final boolean isReadOnly(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md17, var2, m[17]);
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

   public final boolean isSearchable(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md18, var2, m[18]);
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

   public final boolean isSigned(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md19, var2, m[19]);
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

   public final boolean isWrapperFor(Class var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Boolean)this.ror.invoke((Remote)null, md20, var2, m[20]);
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

   public final boolean isWritable(int var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{new Integer(var1)};
         return (Boolean)this.ror.invoke((Remote)null, md21, var2, m[21]);
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

   public final Object unwrap(Class var1) throws SQLException {
      try {
         Object[] var2 = new Object[]{var1};
         return (Object)this.ror.invoke((Remote)null, md22, var2, m[22]);
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
