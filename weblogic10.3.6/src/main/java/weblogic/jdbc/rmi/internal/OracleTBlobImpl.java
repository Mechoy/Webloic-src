package weblogic.jdbc.rmi.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.sql.SQLException;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.BlockGetterImpl;
import weblogic.jdbc.common.internal.JDBCOutputStreamImpl;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class OracleTBlobImpl extends RMISkelWrapperImpl implements InteropWriteReplaceable {
   private java.sql.Blob t2_blob = null;
   private BlockGetterImpl bg = null;
   private RmiDriverSettings rmiSettings = null;
   public static final int BINARY_STREAM = 3;
   private transient Object interop = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         try {
            if (var3 instanceof OutputStream) {
               OutputStream var4 = (OutputStream)var3;
               JDBCOutputStreamImpl var5 = new JDBCOutputStreamImpl(var4, this.rmiSettings.isVerbose(), this.rmiSettings.getChunkSize());
               var3 = var5;
            }
         } catch (Exception var6) {
            JDBCLogger.logStackTrace(var6);
            throw var6;
         }

         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void init(java.sql.Blob var1, RmiDriverSettings var2) {
      this.t2_blob = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (this.interop == null) {
         Object var2 = StubFactory.getStub((Remote)this);
         this.interop = new OracleTBlobStub((OracleTBlob)var2, this.rmiSettings);
      }

      return this.interop;
   }

   public static java.sql.Blob makeOracleTBlobImpl(java.sql.Blob var0, RmiDriverSettings var1) {
      OracleTBlobImpl var2 = (OracleTBlobImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTBlobImpl", var0, true);
      var2.init(var0, var1);
      return (java.sql.Blob)var2;
   }

   public int registerStream(int var1) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var2 = "time=" + System.currentTimeMillis() + " : registerStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      synchronized(this) {
         if (this.bg == null) {
            this.bg = new BlockGetterImpl();
         }
      }

      if (var1 == 3) {
         InputStream var5 = this.t2_blob.getBinaryStream();
         int var3 = this.bg.register(var5, this.rmiSettings.getChunkSize());
         return var3;
      } else {
         throw new SQLException("Invalid stream type: " + var1);
      }
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

   public void internalClose() {
      try {
         UnicastRemoteObject.unexportObject(this, true);
         this.t2_blob = null;
         this.rmiSettings = null;
      } catch (NoSuchObjectException var2) {
      }

   }
}
