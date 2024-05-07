package weblogic.jdbc.rmi.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.rmi.RMIStubWrapperImpl;
import weblogic.jdbc.rmi.RmiStatement;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.extensions.server.StubDelegateInfo;

public class StatementStub extends RMIStubWrapperImpl implements Serializable, StubDelegateInfo {
   private static final long serialVersionUID = 3782203674892516357L;
   RmiDriverSettings rmiSettings = null;
   java.sql.Statement stmt;

   public StatementStub() {
   }

   public StatementStub(java.sql.Statement var1, RmiDriverSettings var2) {
      this.init(var1, var2);
   }

   public void init(java.sql.Statement var1, RmiDriverSettings var2) {
      this.stmt = var1;
      this.rmiSettings = new RmiDriverSettings(var2);
   }

   public Object readResolve() throws ObjectStreamException {
      StatementStub var1 = null;

      try {
         var1 = (StatementStub)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.StatementStub", this.stmt, false);
         var1.init(this.stmt, this.rmiSettings);
         return (java.sql.Statement)var1;
      } catch (Exception var3) {
         JDBCLogger.logStackTrace(var3);
         return this.stmt;
      }
   }

   public Object getStubDelegate() {
      return this.stmt;
   }

   public int getRmiFetchSize() throws SQLException {
      return this.rmiSettings.getRowCacheSize();
   }

   public void setRmiFetchSize(int var1) throws SQLException {
      ((RmiStatement)this.stmt).setRmiFetchSize(var1);
      this.rmiSettings.setRowCacheSize(var1);
   }

   public void close() throws SQLException {
      if (this.rmiSettings.isVerbose()) {
         String var1 = "time=" + System.currentTimeMillis() + " : close";
         JdbcDebug.JDBCRMIInternal.debug(var1);
      }

      this.stmt.close();
   }
}
