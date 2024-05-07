package weblogic.jdbc.rmi.internal;

import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.BlockGetter;
import weblogic.jdbc.common.internal.InputStreamHandler;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.common.internal.ReaderBlockGetter;
import weblogic.jdbc.common.internal.ReaderHandler;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.server.StubDelegateInfo;

public class OracleTClobStub extends RMIStubWrapperImpl implements Serializable, StubDelegateInfo {
   private static final long serialVersionUID = 2758119947660786307L;
   OracleTClob remoteC;
   RmiDriverSettings rmiSettings = null;

   public OracleTClobStub() {
   }

   public OracleTClobStub(OracleTClob var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(OracleTClob var1, RmiDriverSettings var2) {
      this.remoteC = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public static java.sql.Clob makeOracleTClobStub(java.sql.Clob var0, RmiDriverSettings var1) {
      OracleTClobStub var2 = (OracleTClobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTClobStub", var0, false);
      var2.init((OracleTClob)var0, var1);
      return (java.sql.Clob)var2;
   }

   public void internalClose() {
      this.remoteC.internalClose();
   }

   public Object readResolve() throws ObjectStreamException {
      OracleTClobStub var1 = null;

      try {
         var1 = (OracleTClobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.OracleTClobStub", this.remoteC, false);
         var1.init(this.remoteC, this.rmiSettings);
         return (java.sql.Clob)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteC;
      }
   }

   public InputStream getAsciiStream() throws SQLException {
      InputStreamHandler var1 = null;
      String var2 = "getAsciiStream";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         if (this.rmiSettings.isVerbose()) {
            String var4 = "time=" + System.currentTimeMillis() + " : " + var2;
            JdbcDebug.JDBCRMIInternal.debug(var4);
         }

         BlockGetter var7 = this.remoteC.getBlockGetter();
         int var5 = this.remoteC.registerStream(1);
         var1 = new InputStreamHandler();
         var1.setBlockGetter(var7, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }

   public Reader getCharacterStream() throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var1 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var1);
      }

      ReaderHandler var7 = null;
      String var2 = "getCharacterStream";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         ReaderBlockGetter var4 = this.remoteC.getReaderBlockGetter();
         int var5 = this.remoteC.registerStream(2);
         var7 = new ReaderHandler();
         var7.setReaderBlockGetter(var4, var5);
         this.postInvocationHandler(var2, var3, var7);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var7;
   }

   public Reader getCharacterStream(long var1, long var3) throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var5 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var5);
      }

      ReaderHandler var11 = null;
      String var6 = "getCharacterStream";
      Object[] var7 = new Object[]{var1, var3};

      try {
         this.preInvocationHandler(var6, var7);
         ReaderBlockGetter var8 = this.remoteC.getReaderBlockGetter();
         int var9 = this.remoteC.registerStream(2, new Object[]{var1, var3});
         var11 = new ReaderHandler();
         var11.setReaderBlockGetter(var8, var9);
         this.postInvocationHandler(var6, var7, var11);
      } catch (Exception var10) {
         this.invocationExceptionHandler(var6, var7, var10);
      }

      return var11;
   }

   public Object getStubDelegate() {
      return this.remoteC;
   }
}
