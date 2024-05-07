package weblogic.jdbc.rmi.internal;

import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderBlockGetterImpl;
import weblogic.jdbc.common.internal.ReaderHandler;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class CallableStatementStub extends PreparedStatementStub {
   private static final long serialVersionUID = 8441497012896429985L;
   transient BlockGetter bg = null;
   private RmiDriverSettings rmiSettings = null;
   CallableStatement cstmt;

   public CallableStatementStub() {
   }

   public CallableStatementStub(CallableStatement var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(CallableStatement var1, RmiDriverSettings var2) {
      super.init(var1, var2);
      this.cstmt = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      CallableStatementStub var1 = null;

      try {
         var1 = (CallableStatementStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.CallableStatementStub", this.cstmt, false);
         var1.init(this.cstmt, this.rmiSettings);
         return (java.sql.CallableStatement)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.cstmt;
      }
   }

   public CallableStatement getStubDelegateAsCS() {
      return (CallableStatement)this.getStubDelegate();
   }

   public InputStream getAsciiStream(int var1) throws SQLException {
      InputStreamHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getAsciiStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         BlockGetter var5 = this.getStubDelegateAsCS().getBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 1);
         var2 = new InputStreamHandler();
         var2.setBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }

   public InputStream getBinaryStream(int var1) throws SQLException {
      InputStreamHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getBinaryStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         BlockGetter var5 = this.getStubDelegateAsCS().getBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 3);
         var2 = new InputStreamHandler();
         var2.setBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }

   public InputStream getUnicodeStream(int var1) throws SQLException {
      InputStreamHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getUnicodeStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getUnicodeStream";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         BlockGetter var5 = this.getStubDelegateAsCS().getBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 2);
         var2 = new InputStreamHandler();
         var2.setBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }

   public void setClob(String var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setClob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setClob(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setClob(var1, this.rbg, var6);
      }

   }

   public void setClob(String var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setClob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setClob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setClob(var1, this.rbg, var8, var3);
      }

   }

   public void setCharacterStream(String var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setCharacterStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setCharacterStream(var1, this.rbg, var6);
      }

   }

   public void setCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var7 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setCharacterStream(var1, this.rbg, var7, var3);
      }

   }

   public void setCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setCharacterStream(var1, this.rbg, var8, var3);
      }

   }

   public void setNClob(String var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setNClob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setNClob(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setNClob(var1, this.rbg, var6);
      }

   }

   public void setNClob(String var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setNClob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setNClob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setNClob(var1, this.rbg, var8, var3);
      }

   }

   public void setNCharacterStream(String var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setNCharacterStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setNCharacterStream(var1, this.rbg, var6);
      }

   }

   public void setNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setNCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setNCharacterStream(var1, this.rbg, var8, var3);
      }

   }

   public void setAsciiStream(String var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setAsciiStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setAsciiStream(var1, this.bg, var6);
      }

   }

   public void setAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setAsciiStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var7 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setAsciiStream(var1, this.bg, var7, var3);
      }

   }

   public void setAsciiStream(String var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setAsciiStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setAsciiStream(var1, this.bg, var8, var3);
      }

   }

   public void setBinaryStream(String var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setBinaryStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setBinaryStream(var1, this.bg, var6);
      }

   }

   public void setBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setBinaryStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var7 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setBinaryStream(var1, this.bg, var7, var3);
      }

   }

   public void setBinaryStream(String var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setBinaryStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setBinaryStream(var1, this.bg, var8, var3);
      }

   }

   public void setBlob(String var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setBlob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setBlob(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setBlob(var1, this.bg, var6);
      }

   }

   public void setBlob(String var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setBlob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsCS().setBlob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsCS().setBlob(var1, this.bg, var8, var3);
      }

   }

   public void setObject(String var1, Object var2) throws SQLException {
      if (var2 == null) {
         this.getStubDelegateAsCS().setObject(var1, var2);
      } else {
         String var4;
         int var10;
         if (var2 instanceof Reader) {
            Reader var3 = (Reader)var2;
            if (this.rmiSettings.isVerbose()) {
               var4 = "time=" + System.currentTimeMillis() + " : setObject";
               JdbcDebug.JDBCRMIInternal.debug(var4);
            }

            synchronized(this) {
               if (this.rbg == null) {
                  this.rbg = new ReaderBlockGetterImpl();
               }
            }

            var10 = this.rbg.register(var3, this.rmiSettings.getChunkSize());
            this.getStubDelegateAsCS().setObject(var1, this.rbg, var10);
         } else if (var2 instanceof InputStream) {
            InputStream var9 = (InputStream)var2;
            if (this.rmiSettings.isVerbose()) {
               var4 = "time=" + System.currentTimeMillis() + " : setObject";
               JdbcDebug.JDBCRMIInternal.debug(var4);
            }

            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            var10 = this.bg.register(var9, this.rmiSettings.getChunkSize());
            this.getStubDelegateAsCS().setObject(var1, this.bg, var10);
         } else {
            this.getStubDelegateAsCS().setObject(var1, var2);
         }
      }

   }

   public void setObject(String var1, Object var2, int var3) throws SQLException {
      if (var2 == null) {
         this.getStubDelegateAsCS().setObject(var1, (Object)var2, var3);
      } else {
         String var5;
         int var11;
         if (var2 instanceof Reader) {
            Reader var4 = (Reader)var2;
            if (this.rmiSettings.isVerbose()) {
               var5 = "time=" + System.currentTimeMillis() + " : setObject";
               JdbcDebug.JDBCRMIInternal.debug(var5);
            }

            synchronized(this) {
               if (this.rbg == null) {
                  this.rbg = new ReaderBlockGetterImpl();
               }
            }

            var11 = this.rbg.register(var4, this.rmiSettings.getChunkSize());
            this.getStubDelegateAsCS().setObject(var1, this.rbg, var11, var3);
         } else if (var2 instanceof InputStream) {
            InputStream var10 = (InputStream)var2;
            if (this.rmiSettings.isVerbose()) {
               var5 = "time=" + System.currentTimeMillis() + " : setObject";
               JdbcDebug.JDBCRMIInternal.debug(var5);
            }

            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            var11 = this.bg.register(var10, this.rmiSettings.getChunkSize());
            this.getStubDelegateAsCS().setObject(var1, this.bg, var11, var3);
         } else {
            this.getStubDelegateAsCS().setObject(var1, (Object)var2, var3);
         }
      }

   }

   public void setObject(String var1, Object var2, int var3, int var4) throws SQLException {
      if (var2 == null) {
         this.getStubDelegateAsCS().setObject(var1, (Object)var2, var3, var4);
      } else {
         String var6;
         int var12;
         if (var2 instanceof Reader) {
            Reader var5 = (Reader)var2;
            if (this.rmiSettings.isVerbose()) {
               var6 = "time=" + System.currentTimeMillis() + " : setObject";
               JdbcDebug.JDBCRMIInternal.debug(var6);
            }

            synchronized(this) {
               if (this.rbg == null) {
                  this.rbg = new ReaderBlockGetterImpl();
               }
            }

            var12 = this.rbg.register(var5, this.rmiSettings.getChunkSize());
            this.getStubDelegateAsCS().setObject(var1, this.rbg, var12, var3, var4);
         } else if (var2 instanceof InputStream) {
            InputStream var11 = (InputStream)var2;
            if (this.rmiSettings.isVerbose()) {
               var6 = "time=" + System.currentTimeMillis() + " : setObject";
               JdbcDebug.JDBCRMIInternal.debug(var6);
            }

            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            var12 = this.bg.register(var11, this.rmiSettings.getChunkSize());
            this.getStubDelegateAsCS().setObject(var1, this.bg, var12, var3, var4);
         } else {
            this.getStubDelegateAsCS().setObject(var1, (Object)var2, var3, var4);
         }
      }

   }

   public Reader getCharacterStream(int var1) throws SQLException {
      ReaderHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getCharacterStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         ReaderBlockGetter var5 = this.getStubDelegateAsCS().getReaderBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 4);
         var2 = new ReaderHandler();
         var2.setReaderBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }

   public Reader getCharacterStream(String var1) throws SQLException {
      ReaderHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getCharacterStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         ReaderBlockGetter var5 = this.getStubDelegateAsCS().getReaderBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 4);
         var2 = new ReaderHandler();
         var2.setReaderBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }

   public Reader getNCharacterStream(int var1) throws SQLException {
      ReaderHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getNCharacterStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         ReaderBlockGetter var5 = this.getStubDelegateAsCS().getReaderBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 5);
         var2 = new ReaderHandler();
         var2.setReaderBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }

   public Reader getNCharacterStream(String var1) throws SQLException {
      ReaderHandler var2 = null;
      String var3;
      if (this.rmiSettings.isVerbose()) {
         var3 = "time=" + System.currentTimeMillis() + " : getNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      var3 = "getNCharacterStream";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         ReaderBlockGetter var5 = this.getStubDelegateAsCS().getReaderBlockGetter();
         int var6 = this.getStubDelegateAsCS().registerStream(var1, 5);
         var2 = new ReaderHandler();
         var2.setReaderBlockGetter(var5, var6);
         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var3, var4, var7);
      }

      return var2;
   }
}
