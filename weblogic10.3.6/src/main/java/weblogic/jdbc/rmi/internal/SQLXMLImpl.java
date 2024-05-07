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
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class SQLXMLImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   public static final int BINARY_STREAM = 1;
   public static final int CHARACTER_STREAM = 2;
   private java.sql.SQLXML t2_sqlxml = null;
   private RmiDriverSettings rmiDriverSettings = null;
   private BlockGetterImpl bg = null;
   private ReaderBlockGetterImpl rbg = null;
   private transient Object interop = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof Writer) {
               Writer var4 = (Writer)var3;
               JDBCWriterImpl var5 = new JDBCWriterImpl(var4, this.rmiDriverSettings.isVerbose(), this.rmiDriverSettings.getChunkSize());
               var3 = var5;
            } else if (var3 instanceof OutputStream) {
               OutputStream var8 = (OutputStream)var3;
               JDBCOutputStreamImpl var7 = new JDBCOutputStreamImpl(var8, this.rmiDriverSettings.isVerbose(), this.rmiDriverSettings.getChunkSize());
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

   public void init(java.sql.SQLXML var1, RmiDriverSettings var2) {
      this.t2_sqlxml = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.SQLXML makeSQLXMLImpl(java.sql.SQLXML var0, RmiDriverSettings var1) {
      SQLXMLImpl var2 = (SQLXMLImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.SQLXMLImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.SQLXML)var2;
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new SQLXMLStub((SQLXML)var2, this.rmiDriverSettings);
      }

      return this.interop;
   }

   public int registerStream(int var1) throws SQLException {
      if (this.rmiDriverSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : registerStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
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

      int var5;
      if (var1 == 1) {
         InputStream var3 = this.t2_sqlxml.getBinaryStream();
         var5 = this.bg.register(var3, this.rmiDriverSettings.getChunkSize());
      } else {
         if (var1 != 2) {
            throw new SQLException("Invalid stream type: " + var1);
         }

         Reader var6 = this.t2_sqlxml.getCharacterStream();
         var5 = this.rbg.register(var6, this.rmiDriverSettings.getChunkSize());
      }

      return var5;
   }

   public BlockGetter getBlockGetter() throws SQLException {
      if (this.rmiDriverSettings.isVerbose()) {
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
      if (this.rmiDriverSettings.isVerbose()) {
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

   public void internalClose() {
      try {
         UnicastRemoteObject.unexportObject(this, true);
         this.t2_sqlxml = null;
         this.rmiDriverSettings = null;
      } catch (NoSuchObjectException var2) {
      }

   }
}
