package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.sql.SQLException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.JDBCOutputStreamImpl;
import weblogic.jdbc.common.internal.JDBCWriterImpl;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderBlockGetterImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.jdbc.wrapper.JDBCWrapperImpl;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class OracleTClobImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private java.sql.Clob t2_clob = null;
   RmiDriverSettings rmiSettings = null;
   private BlockGetterImpl bg = null;
   private ReaderBlockGetterImpl rbg = null;
   public static final int ASCII_STREAM = 1;
   public static final int CHAR_STREAM = 2;
   transient Object interop = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof Writer) {
               Writer var4 = (Writer)var3;
               JDBCWriterImpl var5 = new JDBCWriterImpl(var4, this.rmiSettings.isVerbose(), this.rmiSettings.getChunkSize());
               var3 = var5;
            } else if (var3 instanceof OutputStream) {
               OutputStream var8 = (OutputStream)var3;
               JDBCOutputStreamImpl var7 = new JDBCOutputStreamImpl(var8, this.rmiSettings.isVerbose(), this.rmiSettings.getChunkSize());
               var3 = var7;
            }
         } catch (Exception var6) {
            JDBCLogger.logStackTrace(var6);
            throw var6;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void init(java.sql.Clob var1, RmiDriverSettings var2) {
      this.t2_clob = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new OracleTClobStub((OracleTClob)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public static java.sql.Clob makeOracleTClobImpl(java.sql.Clob var0, RmiDriverSettings var1) {
      OracleTClobImpl var2 = (OracleTClobImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTClobImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.Clob)var2;
   }

   public int registerStream(int var1) throws SQLException {
      return this.registerStream(var1, (Object[])null);
   }

   public int registerStream(int var1, Object[] var2) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var3 = "time=" + System.currentTimeMillis() + " : registerStream";
         JdbcDebug.JDBCRMIInternal.debug(var3);
      }

      synchronized(this) {
         if (var1 == 1) {
            if (this.bg == null) {
               this.bg = new BlockGetterImpl();
            }
         } else {
            if (var1 != 2) {
               throw new SQLException("Invalid stream type: " + var1);
            }

            if (this.rbg == null) {
               this.rbg = new ReaderBlockGetterImpl();
            }
         }
      }

      InputStream var4;
      int var6;
      if (var1 == 1) {
         var4 = this.t2_clob.getAsciiStream();
         var6 = this.bg.register(var4, this.rmiSettings.getChunkSize());
      } else {
         if (var1 != 2) {
            throw new SQLException("Invalid stream type: " + var1);
         }

         var4 = null;
         Reader var7;
         if (var2 == null) {
            var7 = this.t2_clob.getCharacterStream();
         } else {
            var7 = this.t2_clob.getCharacterStream((Long)var2[0], (Long)var2[1]);
         }

         var6 = this.rbg.register(var7, this.rmiSettings.getChunkSize());
      }

      return var6;
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
         String var1 = "time=" + System.currentTimeMillis() + " : getBlockGetter";
         JdbcDebug.JDBCRMIInternal.debug(var1);
      }

      synchronized(this) {
         if (this.rbg == null) {
            this.rbg = new ReaderBlockGetterImpl();
         }
      }

      return this.rbg;
   }

   public void internalClose() {
      try {
         UnicastRemoteObject.unexportObject(this, true);
         this.t2_clob = null;
         this.rmiSettings = null;
      } catch (NoSuchObjectException var2) {
      }

   }

   public long position(java.sql.Clob var1, long var2) throws SQLException {
      long var4 = -1L;
      String var6 = "position";
      Object[] var7 = new Object[]{var1, var2};

      try {
         this.preInvocationHandler(var6, var7);
         if (var1 instanceof JDBCWrapperImpl) {
            var4 = ((java.sql.Clob)this.getVendorObj()).position((java.sql.Clob)((JDBCWrapperImpl)var1).getVendorObj(), var2);
         } else {
            var4 = ((java.sql.Clob)this.getVendorObj()).position(var1, var2);
         }

         this.postInvocationHandler(var6, var7, var4);
      } catch (Exception var9) {
         this.invocationExceptionHandler(var6, var7, var9);
      }

      return var4;
   }
}
