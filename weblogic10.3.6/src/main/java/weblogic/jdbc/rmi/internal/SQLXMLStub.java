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

public class SQLXMLStub extends RMIStubWrapperImpl implements Serializable {
   private SQLXML remoteSQLXML = null;
   private RmiDriverSettings rmiDriverSettings = null;

   public SQLXMLStub() {
   }

   public SQLXMLStub(SQLXML var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(SQLXML var1, RmiDriverSettings var2) {
      this.remoteSQLXML = var1;
      this.rmiDriverSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      SQLXMLStub var1 = null;

      try {
         var1 = (SQLXMLStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.SQLXMLStub", this.remoteSQLXML, false);
         var1.init(this.remoteSQLXML, this.rmiDriverSettings);
         return (java.sql.SQLXML)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.remoteSQLXML;
      }
   }

   public void internalClose() {
      this.remoteSQLXML.internalClose();
   }

   public InputStream getBinaryStream() throws SQLException {
      InputStreamHandler var1 = null;
      String var2 = "getBinaryStream";
      Object[] var3 = new Object[0];
      if (this.rmiDriverSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : " + var2;
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      try {
         this.preInvocationHandler(var2, var3);
         BlockGetter var7 = this.remoteSQLXML.getBlockGetter();
         int var5 = this.remoteSQLXML.registerStream(1);
         var1 = new InputStreamHandler();
         var1.setBlockGetter(var7, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }

   public Reader getCharacterStream() throws SQLException {
      ReaderHandler var1 = null;
      String var2 = "getCharacterStream";
      Object[] var3 = new Object[0];
      if (this.rmiDriverSettings.isVerbose()) {
         String var4 = "time=" + System.currentTimeMillis() + " : " + var2;
         JdbcDebug.JDBCRMIInternal.debug(var4);
      }

      try {
         this.preInvocationHandler(var2, var3);
         ReaderBlockGetter var7 = this.remoteSQLXML.getReaderBlockGetter();
         int var5 = this.remoteSQLXML.registerStream(2);
         var1 = new ReaderHandler();
         var1.setReaderBlockGetter(var7, var5);
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var6) {
         this.invocationExceptionHandler(var2, var3, var6);
      }

      return var1;
   }
}
