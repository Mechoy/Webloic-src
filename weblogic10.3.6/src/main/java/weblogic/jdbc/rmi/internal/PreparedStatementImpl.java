package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.rmi.Remote;
import java.sql.NClob;
import java.sql.SQLException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderHandler;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.jdbc.wrapper.JDBCWrapperImpl;
import weblogic.rmi.extensions.StubFactory;

public class PreparedStatementImpl extends StatementImpl implements InteropWriteReplaceable {
   protected java.sql.PreparedStatement t2_pstmt = null;
   protected RmiDriverSettings rmiSettings = null;
   protected transient Object interop = null;

   public PreparedStatementImpl() {
   }

   public PreparedStatementImpl(java.sql.PreparedStatement var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(java.sql.PreparedStatement var1, RmiDriverSettings var2) {
      super.init(var1, var2);
      this.t2_pstmt = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.PreparedStatement makePreparedStatementImpl(java.sql.PreparedStatement var0, RmiDriverSettings var1) {
      if (var0 == null) {
         return null;
      } else {
         PreparedStatementImpl var2 = (PreparedStatementImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.PreparedStatementImpl", var0, true);
         var2.init(var0, var1);
         return (java.sql.PreparedStatement)var2;
      }
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new PreparedStatementStub((PreparedStatement)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public java.sql.PreparedStatement getImplDelegateAsPS() {
      return (java.sql.PreparedStatement)this.getImplDelegate();
   }

   public void setAsciiStream(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      InputStreamHandler var6 = new InputStreamHandler();
      var6.setBlockGetter(var2, var3);
      this.setAsciiStream(var1, (InputStream)var6, var4);
   }

   public void setUnicodeStream(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setUnicodeStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      InputStreamHandler var6 = new InputStreamHandler();
      var6.setBlockGetter(var2, var3);
      this.setUnicodeStream(var1, var6, var4);
   }

   public void setBinaryStream(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      InputStreamHandler var6 = new InputStreamHandler();
      var6.setBlockGetter(var2, var3);
      this.setBinaryStream(var1, (InputStream)var6, var4);
   }

   public void setCharacterStream(int var1, ReaderBlockGetter var2, int var3, int var4) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : setCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      ReaderHandler var6 = new ReaderHandler();
      var6.setReaderBlockGetter(var2, var3);
      this.setCharacterStream(var1, (Reader)var6, var4);
   }

   public void setAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      String var4 = "setAsciiStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         this.t2_pstmt.setAsciiStream(var1, var2, var3);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setUnicodeStream(int var1, InputStream var2, int var3) throws SQLException {
      String var4 = "setUnicodeStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         this.t2_pstmt.setUnicodeStream(var1, var2, var3);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      String var4 = "setBinaryStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         this.t2_pstmt.setBinaryStream(var1, var2, var3);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      String var4 = "setCharacterStream";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         this.t2_pstmt.setCharacterStream(var1, var2, var3);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setObject(int var1, Object var2, int var3, int var4) throws SQLException {
      String var5 = "setObject";
      Object[] var6 = new Object[]{new Integer(var1), var2, new Integer(var3), new Integer(var4)};

      try {
         this.preInvocationHandler(var5, var6);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setObject(var1, ((JDBCWrapperImpl)var2).getVendorObj(), var3, var4);
         } else {
            this.t2_pstmt.setObject(var1, var2, var3, var4);
         }

         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setObject(int var1, Object var2, int var3) throws SQLException {
      String var4 = "setObject";
      Object[] var5 = new Object[]{new Integer(var1), var2, new Integer(var3)};

      try {
         this.preInvocationHandler(var4, var5);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setObject(var1, ((JDBCWrapperImpl)var2).getVendorObj(), var3);
         } else {
            this.t2_pstmt.setObject(var1, var2, var3);
         }

         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setObject(int var1, Object var2) throws SQLException {
      String var3 = "setObject";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setObject(var1, ((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.t2_pstmt.setObject(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public void setRef(int var1, java.sql.Ref var2) throws SQLException {
      String var3 = "setRef";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setRef(var1, (java.sql.Ref)((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.t2_pstmt.setRef(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public void setBlob(int var1, java.sql.Blob var2) throws SQLException {
      String var3 = "setBlob";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setBlob(var1, (java.sql.Blob)((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.t2_pstmt.setBlob(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public void setClob(int var1, java.sql.Clob var2) throws SQLException {
      String var3 = "setClob";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setClob(var1, (java.sql.Clob)((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.t2_pstmt.setClob(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public void setArray(int var1, java.sql.Array var2) throws SQLException {
      String var3 = "setArray";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.t2_pstmt.setArray(var1, (java.sql.Array)((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.t2_pstmt.setArray(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public java.sql.ResultSetMetaData getMetaData() throws SQLException {
      Object var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.t2_pstmt.getMetaData();
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
         var1 = this.t2_pstmt.getParameterMetaData();
         if (var1 != null) {
            var1 = new ParameterMetaDataImpl((java.sql.ParameterMetaData)var1);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return (java.sql.ParameterMetaData)var1;
   }

   public java.sql.ResultSet executeQuery() throws SQLException {
      java.sql.ResultSet var1 = null;
      String var2 = "executeQuery";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = this.getImplDelegateAsPS().executeQuery();
         if (var1 != null) {
            var1 = ResultSetImpl.makeResultSetImpl(var1, this.rmiSettings);
         }

         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }

   public void setNClob(int var1, NClob var2) throws SQLException {
      String var3 = "setNClob";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.getImplDelegateAsPS().setNClob(var1, (NClob)((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.getImplDelegateAsPS().setNClob(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public void setSQLXML(int var1, java.sql.SQLXML var2) throws SQLException {
      String var3 = "setSQLXML";
      Object[] var4 = new Object[]{new Integer(var1), var2};

      try {
         this.preInvocationHandler(var3, var4);
         if (var2 instanceof JDBCWrapperImpl) {
            this.getImplDelegateAsPS().setSQLXML(var1, (java.sql.SQLXML)((JDBCWrapperImpl)var2).getVendorObj());
         } else {
            this.getImplDelegateAsPS().setSQLXML(var1, var2);
         }

         this.postInvocationHandler(var3, var4, (Object)null);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var3, var4, var6);
      }

   }

   public void setClob(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setClob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setClob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setClob(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setClob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setClob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setCharacterStream(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setCharacterStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setCharacterStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setCharacterStream(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setCharacterStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setCharacterStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setNClob(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setNClob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setNClob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setNClob(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setNClob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setNClob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setNCharacterStream(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setNCharacterStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setNCharacterStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setNCharacterStream(int var1, ReaderBlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setNCharacterStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setNCharacterStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setAsciiStream(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setAsciiStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setAsciiStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setAsciiStream(int var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setAsciiStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setAsciiStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setBinaryStream(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setBinaryStream";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setBinaryStream(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setBinaryStream(int var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setBinaryStream";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setBinaryStream(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setBlob(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setBlob";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setBlob(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setBlob(int var1, BlockGetter var2, int var3, long var4) throws SQLException {
      String var6 = "setBlob";
      Object[] var7 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setBlob(var1, var8, var4);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setObject(int var1, ReaderBlockGetter var2, int var3) throws SQLException {
      String var4 = "setObject";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         ReaderHandler var6 = new ReaderHandler();
         var6.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setObject(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setObject(int var1, ReaderBlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setObject";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         ReaderHandler var7 = new ReaderHandler();
         var7.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setObject(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setObject(int var1, ReaderBlockGetter var2, int var3, int var4, int var5) throws SQLException {
      String var6 = "setObject";
      Object[] var7 = new Object[]{var1, var2, var3, var4, var5};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderHandler var8 = new ReaderHandler();
         var8.setReaderBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setObject(var1, var8, var4, var5);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }

   public void setObject(int var1, BlockGetter var2, int var3) throws SQLException {
      String var4 = "setObject";
      Object[] var5 = new Object[]{var1, var2, var3};

      try {
         this.preInvocationHandler(var4, var5);
         InputStreamHandler var6 = new InputStreamHandler();
         var6.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setObject(var1, var6);
         this.postInvocationHandler(var4, var5, (Object)null);
      } catch (Exception var7) {
         this.invocationExceptionHandler(var4, var5, var7);
      }

   }

   public void setObject(int var1, BlockGetter var2, int var3, int var4) throws SQLException {
      String var5 = "setObject";
      Object[] var6 = new Object[]{var1, var2, var3, var4};

      try {
         this.preInvocationHandler(var5, var6);
         InputStreamHandler var7 = new InputStreamHandler();
         var7.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setObject(var1, var7, var4);
         this.postInvocationHandler(var5, var6, (Object)null);
      } catch (Exception var8) {
         this.invocationExceptionHandler(var5, var6, var8);
      }

   }

   public void setObject(int var1, BlockGetter var2, int var3, int var4, int var5) throws SQLException {
      String var6 = "setObject";
      Object[] var7 = new Object[]{var1, var2, var3, var4, var5};

      try {
         this.preInvocationHandler(var6, var7);
         InputStreamHandler var8 = new InputStreamHandler();
         var8.setBlockGetter(var2, var3);
         this.getImplDelegateAsPS().setObject(var1, var8, var4, var5);
         this.postInvocationHandler(var6, var7, (Object)null);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

   }
}
