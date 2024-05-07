package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.rmi.Remote;
import java.sql.SQLException;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderBlockGetterImpl;
import weblogic.jdbc.common.internal.ReaderHandler;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;

public class CallableStatementImpl extends PreparedStatementImpl {
   private java.sql.CallableStatement t2_cstmt = null;
   private RmiDriverSettings rmiSettings = null;
   private BlockGetter bg = null;
   private ReaderBlockGetter rbg = null;
   private transient Object interop = null;
   public static final int ASCII_STREAM = 1;
   public static final int UNICODE_STREAM = 2;
   public static final int BINARY_STREAM = 3;
   public static final int CHARACTER_STREAM = 4;
   public static final int NCHARACTER_STREAM = 5;

   public CallableStatementImpl() {
   }

   public CallableStatementImpl(java.sql.CallableStatement var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(java.sql.CallableStatement var1, RmiDriverSettings var2) {
      super.init(var1, var2);
      this.t2_cstmt = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.CallableStatement makeCallableStatementImpl(java.sql.CallableStatement var0, RmiDriverSettings var1) {
      if (var0 == null) {
         return null;
      } else {
         CallableStatementImpl var2 = (CallableStatementImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.CallableStatementImpl", var0, true);
         var2.init(var0, var1);
         return (java.sql.CallableStatement)var2;
      }
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new CallableStatementStub((CallableStatement)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public java.sql.CallableStatement getImplDelegateAsCS() {
      return (java.sql.CallableStatement)this.getImplDelegate();
   }

   public BlockGetter getBlockGetter() throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var1 = "time=" + System.currentTimeMillis() + " : getBlockGetter";
         JdbcDebug.JDBCRMIInternal.debug(var1);
      }

      synchronized(this) {
         if (this.bg == null) {
            this.bg = new BlockGetterImpl();
         }
      }

      return this.bg;
   }

   public int registerStream(int var1, int var2) throws SQLException {
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : registerStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 != 4 && var2 != 5) {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         InputStream var9;
         if (var2 == 1) {
            var9 = this.getAsciiStream(var1);
         } else if (var2 == 2) {
            var9 = this.getUnicodeStream(var1);
         } else {
            if (var2 != 3) {
               throw new SQLException("Invalid stream type: " + var2);
            }

            var9 = this.getBinaryStream(var1);
         }

         int var4 = this.bg.register(var9, this.rmiSettings.getChunkSize());
         return var4;
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         var3 = null;
         Reader var8;
         if (var2 == 4) {
            var8 = this.getImplDelegateAsCS().getCharacterStream(var1);
         } else {
            var8 = this.getImplDelegateAsCS().getNCharacterStream(var1);
         }

         return var8 == null ? -1 : this.rbg.register(var8, this.rmiSettings.getChunkSize());
      }
   }

   public int registerStream(String var1, int var2) throws SQLException {
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : registerStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 != 4 && var2 != 5) {
         throw new SQLException("Invalid stream type: " + var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         var3 = null;
         Reader var6;
         if (var2 == 4) {
            var6 = this.getImplDelegateAsCS().getCharacterStream(var1);
         } else {
            var6 = this.getImplDelegateAsCS().getNCharacterStream(var1);
         }

         return var6 == null ? -1 : this.rbg.register(var6, this.rmiSettings.getChunkSize());
      }
   }

   public void close() throws SQLException {
      super.close();
      if (this.bg != null) {
         this.bg.close();
      }

      if (this.rbg != null) {
         this.rbg.close();
      }

   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      throw new SQLException("This vendor feature is not supported");
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      throw new SQLException("This vendor feature is not supported");
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      throw new SQLException("This vendor feature is not supported");
   }

   public ReaderBlockGetter getReaderBlockGetter() throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var1 = "time=" + System.currentTimeMillis() + " : getReaderBlockGetter";
         JdbcDebug.JDBCRMIInternal.debug(var1);
      }

      synchronized(this) {
         if (this.rbg == null) {
            this.rbg = new ReaderBlockGetterImpl();
         }
      }

      return this.rbg;
   }

   public void setClob(String var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setClob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setClob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setClob(String var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setClob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setClob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setCharacterStream(String var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setCharacterStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setCharacterStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setCharacterStream(String var1, ReaderBlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setCharacterStream";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         ReaderHandler var7 = new ReaderHandler();
         var7.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setCharacterStream(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setCharacterStream(String var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setCharacterStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setCharacterStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setNClob(String var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setNClob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setNClob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setNClob(String var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setNClob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setNClob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setNCharacterStream(String var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setNCharacterStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setNCharacterStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setNCharacterStream(String var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setNCharacterStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setNCharacterStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setAsciiStream(String var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setAsciiStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setAsciiStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setAsciiStream(String var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setAsciiStream";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setAsciiStream(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setAsciiStream(String var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setAsciiStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setAsciiStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setBinaryStream(String var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setBinaryStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setBinaryStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setBinaryStream(String var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setBinaryStream";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setBinaryStream(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setBinaryStream(String var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setBinaryStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setBinaryStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setBlob(String var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setBlob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setBlob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setBlob(String var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setBlob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setBlob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setObject(String var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setObject";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setObject(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setObject(String var1, ReaderBlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setObject";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         ReaderHandler var7 = new ReaderHandler();
         var7.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setObject(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setObject(String var1, ReaderBlockGetter var2, int var3, int var4, int var5) throws SQLException {
      String var6 = "setObject";
      Object[] var7 = new Object[]{var1, var2, var3, var4, var5};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setObject(var1, var8, var4, var5);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setObject(String var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setObject";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setObject(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setObject(String var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setObject";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setObject(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setObject(String var1, BlockGetter var2, int var3, int var4, int var5) throws SQLException {
      String var6 = "setObject";
      Object[] var7 = new Object[]{var1, var2, var3, var4, var5};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsCS().setObject(var1, var8, var4, var5);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }
}
