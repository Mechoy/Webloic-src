package weblogic.jdbc.rmi.internal;

import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderBlockGetterImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.server.StubDelegateInfo;

public class PreparedStatementStub extends StatementStub implements Serializable, StubDelegateInfo {
   private static final long serialVersionUID = -2902370428006989865L;
   transient BlockGetter bg = null;
   transient ReaderBlockGetter rbg = null;
   protected RmiDriverSettings rmiSettings = null;
   protected PreparedStatement pstmt;

   public PreparedStatementStub() {
   }

   public PreparedStatementStub(PreparedStatement var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(PreparedStatement var1, RmiDriverSettings var2) {
      super.init(var1, var2);
      this.pstmt = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      PreparedStatementStub var1 = null;

      try {
         var1 = (PreparedStatementStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.PreparedStatementStub", this.pstmt, false);
         var1.init(this.pstmt, this.rmiSettings);
         return (java.sql.PreparedStatement)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.pstmt;
      }
   }

   public PreparedStatement getStubDelegateAsPS() {
      return (PreparedStatement)this.getStubDelegate();
   }

   public Object getStubDelegate() {
      return this.pstmt;
   }

   public int getRmiFetchSize() throws SQLException {
      return this.rmiSettings.getRowCacheSize();
   }

   public void setRmiFetchSize(int var1) throws SQLException {
      this.pstmt.setRmiFetchSize(var1);
      this.rmiSettings.setRowCacheSize(var1);
   }

   public void setAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      String var4 = "setAsciiStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         if (this.rmiSettings.isVerbose()) {
            String var6 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
            JdbcDebug.JDBCRMIInternal.debug(var6);
         }

         if (var2 != null && var3 > 0) {
            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            int var10 = this.bg.register(var2, this.rmiSettings.getChunkSize());
            this.pstmt.setAsciiStream(var1, this.bg, var10, var3);
         } else {
            this.pstmt.setAsciiStream(var1, var2, var3);
         }

         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var4, var5, var9);
      }

   }

   public void setUnicodeStream(int var1, InputStream var2, int var3) throws SQLException {
      String var4;
      if (this.rmiSettings.isVerbose()) {
         var4 = "time=" + System.currentTimeMillis() + " : setUnicodeStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      var4 = "setUnicodeStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         if (var2 != null && var3 > 0) {
            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
            this.pstmt.setUnicodeStream(var1, this.bg, var6, var3);
         } else {
            this.pstmt.setUnicodeStream(var1, var2, var3);
         }

         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var4, var5, var9);
      }

   }

   public void setBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      String var4;
      if (this.rmiSettings.isVerbose()) {
         var4 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      var4 = "setBinaryStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         if (var2 != null && var3 > 0) {
            synchronized(this) {
               if (this.bg == null) {
                  this.bg = new BlockGetterImpl();
               }
            }

            int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
            this.pstmt.setBinaryStream(var1, this.bg, var6, var3);
         } else {
            this.pstmt.setBinaryStream(var1, var2, var3);
         }

         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var4, var5, var9);
      }

   }

   public void setCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      String var4;
      if (this.rmiSettings.isVerbose()) {
         var4 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      var4 = "setCharacterStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         if (var2 != null && var3 > 0) {
            synchronized(this) {
               if (this.rbg == null) {
                  this.rbg = new ReaderBlockGetterImpl();
               }
            }

            int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
            this.pstmt.setCharacterStream(var1, this.rbg, var6, var3);
         } else {
            this.pstmt.setCharacterStream(var1, var2, var3);
         }

         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var4, var5, var9);
      }

   }

   public java.sql.ResultSetMetaData getMetaData() throws SQLException {
      Object var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.pstmt.getMetaData();
         if (var1 != null) {
            var1 = new ResultSetMetaDataImpl((java.sql.ResultSetMetaData)var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.ResultSetMetaData)var1;
   }

   public java.sql.ParameterMetaData getParameterMetaData() throws SQLException {
      Object var1 = null;
      String var2 = "getParameterMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.pstmt.getParameterMetaData();
         if (var1 != null) {
            var1 = new ParameterMetaDataImpl((java.sql.ParameterMetaData)var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.ParameterMetaData)var1;
   }

   public void close() throws SQLException {
      super.close();
      synchronized(this) {
         if (this.bg != null) {
            this.bg.close();
            this.bg = null;
         }

         if (this.rbg != null) {
            this.rbg.close();
            this.rbg = null;
         }

      }
   }

   public void setClob(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setClob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setClob(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setClob(var1, this.rbg, var6);
      }

   }

   public void setClob(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setClob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setClob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setClob(var1, this.rbg, var8, var3);
      }

   }

   public void setCharacterStream(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setCharacterStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setCharacterStream(var1, this.rbg, var6);
      }

   }

   public void setCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setCharacterStream(var1, this.rbg, var8, var3);
      }

   }

   public void setNClob(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setNClob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setNClob(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setNClob(var1, this.rbg, var6);
      }

   }

   public void setNClob(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setNClob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setNClob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setNClob(var1, this.rbg, var8, var3);
      }

   }

   public void setNCharacterStream(int var1, Reader var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setNCharacterStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var6 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setNCharacterStream(var1, this.rbg, var6);
      }

   }

   public void setNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setNCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setNCharacterStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         int var8 = this.rbg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setNCharacterStream(var1, this.rbg, var8, var3);
      }

   }

   public void setAsciiStream(int var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setAsciiStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setAsciiStream(var1, this.bg, var6);
      }

   }

   public void setAsciiStream(int var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setAsciiStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setAsciiStream(var1, this.bg, var8, var3);
      }

   }

   public void setBinaryStream(int var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setBinaryStream(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setBinaryStream(var1, this.bg, var6);
      }

   }

   public void setBinaryStream(int var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setBinaryStream(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setBinaryStream(var1, this.bg, var8, var3);
      }

   }

   public void setBlob(int var1, InputStream var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : setBlob";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setBlob(var1, var2);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var6 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setBlob(var1, this.bg, var6);
      }

   }

   public void setBlob(int var1, InputStream var2, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setBlob";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      if (var2 == null) {
         this.getStubDelegateAsPS().setBlob(var1, var2, var3);
      } else {
         synchronized(this) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         }

         int var8 = this.bg.register(var2, this.rmiSettings.getChunkSize());
         this.getStubDelegateAsPS().setBlob(var1, this.bg, var8, var3);
      }

   }

   public void setObject(int var1, Object var2) throws SQLException {
      if (var2 == null) {
         this.getStubDelegateAsPS().setObject(var1, var2);
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
            this.getStubDelegateAsPS().setObject(var1, this.rbg, var10);
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
            this.getStubDelegateAsPS().setObject(var1, this.bg, var10);
         } else {
            this.getStubDelegateAsPS().setObject(var1, var2);
         }
      }

   }

   public void setObject(int var1, Object var2, int var3) throws SQLException {
      if (var2 == null) {
         this.getStubDelegateAsPS().setObject(var1, (Object)var2, var3);
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
            this.getStubDelegateAsPS().setObject(var1, this.rbg, var11, var3);
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
            this.getStubDelegateAsPS().setObject(var1, this.bg, var11, var3);
         } else {
            this.getStubDelegateAsPS().setObject(var1, (Object)var2, var3);
         }
      }

   }

   public void setObject(int var1, Object var2, int var3, int var4) throws SQLException {
      if (var2 == null) {
         this.getStubDelegateAsPS().setObject(var1, (Object)var2, var3, var4);
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
            this.getStubDelegateAsPS().setObject(var1, this.rbg, var12, var3, var4);
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
            this.getStubDelegateAsPS().setObject(var1, this.bg, var12, var3, var4);
         } else {
            this.getStubDelegateAsPS().setObject(var1, (Object)var2, var3, var4);
         }
      }

   }
}
