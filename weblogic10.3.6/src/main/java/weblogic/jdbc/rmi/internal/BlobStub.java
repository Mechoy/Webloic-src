package weblogic.jdbc.rmi.internal;

import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.server.StubDelegateInfo;

public class BlobStub extends RMIStubWrapperImpl implements Serializable, StubDelegateInfo {
   private static final long serialVersionUID = 6113511803561203420L;
   Blob remoteB;
   private RmiDriverSettings rmiSettings = null;

   public void init(Blob var1, RmiDriverSettings var2) {
      this.remoteB = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public void internalClose() {
      this.remoteB.internalClose();
   }

   public Object readResolve() throws ObjectStreamException {
      BlobStub var1 = null;

      try {
         var1 = (BlobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.BlobStub", this.remoteB, false);
         var1.init(this.remoteB, this.rmiSettings);
         return (java.sql.Blob)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteB;
      }
   }

   public InputStream getBinaryStream() throws SQLException {
      InputStreamHandler var1 = null;
      String var2;
      if (this.rmiSettings.isVerbose()) {
         var2 = "time=" + System.currentTimeMillis() + " : getBinaryStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      var2 = "getBinaryStream";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         BlockGetter var4 = this.getBlockGetter();
         int var5 = this.registerStream(3);
         var1 = new InputStreamHandler();
         var1.setBlockGetter(var4, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }

   public int registerStream(int var1) throws SQLException {
      return this.remoteB.registerStream(var1);
   }

   public BlockGetter getBlockGetter() throws SQLException {
      return this.remoteB.getBlockGetter();
   }

   public Object getStubDelegate() {
      return this.remoteB;
   }
}
