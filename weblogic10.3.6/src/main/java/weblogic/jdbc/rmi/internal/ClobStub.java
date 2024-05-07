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

public class ClobStub extends RMIStubWrapperImpl implements Serializable, StubDelegateInfo {
   private static final long serialVersionUID = 5514150181551731451L;
   Clob remoteC;
   private RmiDriverSettings rmiSettings = null;

   public void init(Clob var1, RmiDriverSettings var2) {
      this.remoteC = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public void internalClose() {
      this.remoteC.internalClose();
   }

   public Object readResolve() throws ObjectStreamException {
      ClobStub var1 = null;

      try {
         var1 = (ClobStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ClobStub", this.remoteC, false);
         var1.init(this.remoteC, this.rmiSettings);
         return (java.sql.Clob)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteC;
      }
   }

   public InputStream getAsciiStream() throws SQLException {
      InputStreamHandler var1 = null;
      String var2;
      if (this.rmiSettings.isVerbose()) {
         var2 = "time=" + System.currentTimeMillis() + " : getAsciiStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      var2 = "getAsciiStream";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         BlockGetter var4 = this.getBlockGetter();
         int var5 = this.registerStream(1);
         var1 = new InputStreamHandler();
         var1.setBlockGetter(var4, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }

   public Reader getCharacterStream() throws SQLException {
      ReaderHandler var1 = null;
      String var2;
      if (this.rmiSettings.isVerbose()) {
         var2 = "time=" + System.currentTimeMillis() + " : getCharacterStream";
         JdbcDebug.JDBCRMIInternal.debug(var2);
      }

      var2 = "getCharacterStream";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         ReaderBlockGetter var4 = this.getReaderBlockGetter();
         int var5 = this.registerStream(2);
         var1 = new ReaderHandler();
         var1.setReaderBlockGetter(var4, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }

   public int registerStream(int var1) throws SQLException {
      return this.remoteC.registerStream(var1);
   }

   public BlockGetter getBlockGetter() throws SQLException {
      return this.remoteC.getBlockGetter();
   }

   public ReaderBlockGetter getReaderBlockGetter() throws SQLException {
      return this.remoteC.getReaderBlockGetter();
   }

   public Object getStubDelegate() {
      return this.remoteC;
   }
}
