package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.sql.NClob;
import java.sql.SQLException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderBlockGetterImpl;
import weblogic.jdbc.common.internal.ReaderHandler;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class ResultSetImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private java.sql.ResultSet t2_rs = null;
   private BlockGetter bg = new BlockGetterImpl();
   private ReaderBlockGetter rbg = null;
   private RmiDriverSettings rmiSettings = null;
   private ResultSetMetaDataCache mdCache = null;
   private ResultSetRowCache nextRowCache = null;
   private transient Object interop = null;
   public static final int ASCII_STREAM = 1;
   public static final int UNICODE_STREAM = 2;
   public static final int BINARY_STREAM = 3;
   public static final int CHARACTER_STREAM = 4;
   public static final int NCHARACTER_STREAM = 5;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof java.sql.ResultSet) {
               var3 = makeResultSetImpl((java.sql.ResultSet)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Blob) {
               var3 = OracleTBlobImpl.makeOracleTBlobImpl((java.sql.Blob)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Clob) {
               var3 = OracleTClobImpl.makeOracleTClobImpl((java.sql.Clob)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Struct) {
               var3 = StructImpl.makeStructImpl((java.sql.Struct)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Ref) {
               var3 = RefImpl.makeRefImpl((java.sql.Ref)var3, this.rmiSettings);
            } else if (var3 instanceof java.sql.Array) {
               var3 = ArrayImpl.makeArrayImpl((java.sql.Array)var3, this.rmiSettings);
            }
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
            throw var5;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void init(java.sql.ResultSet var1, RmiDriverSettings var2) {
      this.t2_rs = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.ResultSet makeResultSetImpl(java.sql.ResultSet var0, RmiDriverSettings var1) {
      ResultSetImpl var2 = (ResultSetImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ResultSetImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.ResultSet)var2;
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new ResultSetStub((ResultSet)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public boolean isRowCaching() throws SQLException {
      if (this.rmiSettings.getRowCacheSize() <= 1) {
         if (this.rmiSettings.isVerbose()) {
            JdbcDebug.JDBCRMIInternal.debug("rmiSettings.rowCacheSize is " + this.rmiSettings.getRowCacheSize() + " so returning false");
         }

         return false;
      } else {
         if (this.mdCache == null) {
            this.mdCache = new ResultSetMetaDataCache(this.t2_rs);
         }

         try {
            if (this.isResultSetCacheable()) {
               this.nextRowCache = new ResultSetRowCache(this.rmiSettings.getRowCacheSize(), this.t2_rs, this.mdCache);
            } else {
               this.rmiSettings.setRowCacheSize(0);
               if (this.rmiSettings.isVerbose()) {
                  JdbcDebug.JDBCRMIInternal.debug("Result set is not cacheable");
               }
            }
         } catch (Exception var2) {
            if (this.rmiSettings.isVerbose()) {
               JdbcDebug.JDBCRMIInternal.debug("Exception received: " + var2);
               JDBCLogger.logStackTrace(var2);
            }

            this.rmiSettings.setRowCacheSize(-1);
            if (var2 instanceof SQLException) {
               throw (SQLException)var2;
            }

            throw new SQLException(var2.toString());
         }

         if (this.rmiSettings.isVerbose()) {
            JdbcDebug.JDBCRMIInternal.debug("isRowCaching: rmiSettings.rowCacheSize is " + this.rmiSettings.getRowCacheSize());
         }

         return this.rmiSettings.getRowCacheSize() > 1;
      }
   }

   private boolean isResultSetCacheable() throws SQLException {
      try {
         if (this.t2_rs.getType() != 1003) {
            return false;
         }

         if (this.t2_rs.getConcurrency() != 1007) {
            return false;
         }
      } catch (SQLException var2) {
         if (this.rmiSettings.isVerbose()) {
            JdbcDebug.JDBCRMIInternal.debug("Trapped exception: " + var2);
            JDBCLogger.logStackTrace(var2);
         }
      }

      return ResultSetRowCache.isCacheable(this.mdCache);
   }

   public ResultSetMetaDataCache getMetaDataCache() throws SQLException {
      if (this.mdCache == null) {
         this.mdCache = new ResultSetMetaDataCache(this.t2_rs);
      }

      return this.mdCache;
   }

   public ResultSetRowCache getNextRowCache() throws SQLException {
      ResultSetRowCache var1 = this.nextRowCache;
      if (!var1.isTrueSetFinished()) {
         this.nextRowCache = new ResultSetRowCache(this.rmiSettings.getRowCacheSize(), this.t2_rs, this.mdCache);
         if (this.nextRowCache.getRowCount() < 1) {
            var1.setTrueSetFinished(true);
         }
      }

      return var1;
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
            var9 = this.t2_rs.getAsciiStream(var1);
         } else if (var2 == 2) {
            var9 = this.t2_rs.getUnicodeStream(var1);
         } else {
            if (var2 != 3) {
               throw new SQLException("Invalid stream type: " + var2);
            }

            var9 = this.t2_rs.getBinaryStream(var1);
         }

         if (var9 == null) {
            return -1;
         } else {
            int var4 = this.bg.register(var9, this.rmiSettings.getChunkSize());
            return var4;
         }
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         var3 = null;
         Reader var8;
         if (var2 == 4) {
            var8 = this.t2_rs.getCharacterStream(var1);
         } else {
            var8 = this.t2_rs.getNCharacterStream(var1);
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

      synchronized(this) {
         if (this.bg == null) {
            this.bg = new BlockGetterImpl();
         }
      }

      if (var2 != 4 && var2 != 5) {
         InputStream var9;
         if (var2 == 1) {
            var9 = this.t2_rs.getAsciiStream(var1);
         } else if (var2 == 2) {
            var9 = this.t2_rs.getUnicodeStream(var1);
         } else {
            if (var2 != 3) {
               throw new SQLException("Invalid stream type: " + var2);
            }

            var9 = this.t2_rs.getBinaryStream(var1);
         }

         if (var9 == null) {
            return -1;
         } else {
            int var4 = this.bg.register(var9, this.rmiSettings.getChunkSize());
            return var4;
         }
      } else {
         synchronized(this) {
            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }

         var3 = null;
         Reader var8;
         if (var2 == 4) {
            var8 = this.t2_rs.getCharacterStream(var1);
         } else {
            var8 = this.t2_rs.getNCharacterStream(var1);
         }

         return var8 == null ? -1 : this.rbg.register(var8, this.rmiSettings.getChunkSize());
      }
   }

   public void close() throws SQLException {
      this.t2_rs.close();
      if (this.bg != null) {
         this.bg.close();
      }

      if (this.rbg != null) {
         this.rbg.close();
      }

      try {
         UnicastRemoteObject.unexportObject(this, true);
      } catch (NoSuchObjectException var2) {
      }

   }

   public java.sql.Statement getStatement() throws SQLException {
      java.sql.Statement var1 = null;
      String var2 = "getStatement";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.t2_rs.getStatement();
         if (var1 != null) {
            var1 = StatementImpl.makeStatementImpl(var1, this.rmiSettings);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public java.sql.ResultSetMetaData getMetaData() throws SQLException {
      Object var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.t2_rs.getMetaData();
         if (var1 != null) {
            var1 = new ResultSetMetaDataImpl((java.sql.ResultSetMetaData)var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.ResultSetMetaData)var1;
   }

   public NClob getNClob(int var1) throws SQLException {
      NClob var2 = null;
      String var3 = "getNClob";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.t2_rs.getNClob(var1);
         if (var2 != null) {
            var2 = OracleTNClobImpl.makeOracleTNClobImpl(var2, this.rmiSettings);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public NClob getNClob(String var1) throws SQLException {
      NClob var2 = null;
      String var3 = "getNClob";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.t2_rs.getNClob(var1);
         if (var2 != null) {
            var2 = OracleTNClobImpl.makeOracleTNClobImpl(var2, this.rmiSettings);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public java.sql.SQLXML getSQLXML(int var1) throws SQLException {
      java.sql.SQLXML var2 = null;
      String var3 = "getSQLXML";
      Object[] var4 = new Object[]{new Integer(var1)};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.t2_rs.getSQLXML(var1);
         if (var2 != null) {
            var2 = SQLXMLImpl.makeSQLXMLImpl(var2, this.rmiSettings);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public java.sql.SQLXML getSQLXML(String var1) throws SQLException {
      java.sql.SQLXML var2 = null;
      String var3 = "getSQLXML";
      Object[] var4 = new Object[]{var1};

      try {
         this.preInvocationHandler(var3, var4);
         var2 = this.t2_rs.getSQLXML(var1);
         if (var2 != null) {
            var2 = SQLXMLImpl.makeSQLXMLImpl(var2, this.rmiSettings);
         }

         this.postInvocationHandler(var3, var4, var2);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

      return var2;
   }

   public void updateNClob(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "updateNClob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateNClob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateNClob(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateNClob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateNClob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateClob(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "updateClob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateClob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateClob(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateClob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateClob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateCharacterStream(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "updateCharacterStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateCharacterStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateCharacterStream(int var1, ReaderBlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "updateCharacterStream";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         ReaderHandler var7 = new ReaderHandler();
         var7.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateCharacterStream(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void updateCharacterStream(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateCharacterStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateCharacterStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateNCharacterStream(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "updateNCharacterStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateNCharacterStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateNCharacterStream(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateNCharacterStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateNCharacterStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateAsciiStream(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "updateAsciiStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.t2_rs.updateAsciiStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateAsciiStream(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "updateAsciiStream";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.t2_rs.updateAsciiStream(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void updateAsciiStream(int var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateAsciiStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.t2_rs.updateAsciiStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateBinaryStream(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "updateBinaryStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.t2_rs.updateBinaryStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateBinaryStream(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "updateBinaryStream";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.t2_rs.updateBinaryStream(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void updateBinaryStream(int var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateBinaryStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.t2_rs.updateBinaryStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateBlob(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "updateBlob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.t2_rs.updateBlob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateBlob(int var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "updateBlob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.t2_rs.updateBlob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void updateObject(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "updateObject";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateObject(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateObject(int var1, ReaderBlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "updateObject";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         ReaderHandler var7 = new ReaderHandler();
         var7.setReaderBlockGetter(var2, var3);
         this.t2_rs.updateObject(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void updateObject(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "updateObject";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.t2_rs.updateObject(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void updateObject(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "updateObject";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.t2_rs.updateObject(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }
}
