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

public class OracleTBlobStub extends RMIStubWrapperImpl implements Serializable, StubDelegateInfo {
   private static final long serialVersionUID = 9079787653929248555L;
   OracleTBlob remoteB;
   private RmiDriverSettings rmiSettings = null;

   public OracleTBlobStub() {
   }

   public OracleTBlobStub(OracleTBlob var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(OracleTBlob var1, RmiDriverSettings var2) {
      this.remoteB = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.Blob makeOracleTBlobStub(java.sql.Blob var0, RmiDriverSettings var1) {
      OracleTBlobStub var2 = (OracleTBlobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTBlobStub", var0, false);
      var2.init((OracleTBlob)var0, var1);
      return (java.sql.Blob)var2;
   }

   public void internalClose() {
      this.remoteB.internalClose();
   }

   public Object readResolve() throws ObjectStreamException {
      OracleTBlobStub var1 = null;

      try {
         var1 = (OracleTBlobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTBlobStub", this.remoteB, false);
         var1.init(this.remoteB, this.rmiSettings);
         return (java.sql.Blob)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteB;
      }
   }

   public InputStream getBinaryStream() throws SQLException {
      InputStreamHandler var1 = null;
      String var2 = "getBinaryStream";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         if (this.rmiSettings.isVerbose()) {
            String var4 = "time=" + System.currentTimeMillis() + " : getBinaryStream";
            JdbcDebug.JDBCRMIInternal.debug(var4);
         }

         BlockGetter var7 = this.remoteB.getBlockGetter();
         int var5 = this.remoteB.registerStream(3);
         var1 = new InputStreamHandler();
         var1.setBlockGetter(var7, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }

   public Object getStubDelegate() {
      return this.remoteB;
   }
}
