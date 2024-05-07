package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;

public class SerialDatabaseMetaData extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = -7342158409258074989L;
   private DatabaseMetaData rmi_dbmd = null;
   private transient Connection parent_conn = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      super.postInvocationHandler(var1, var2, var3);
      if (var3 == null) {
         return null;
      } else {
         try {
            if (var3 instanceof ResultSet) {
               return SerialResultSet.makeSerialResultSet((ResultSet)var3, (SerialStatement)null);
            }
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
         }

         return var3;
      }
   }

   public void init(DatabaseMetaData var1, Connection var2) {
      this.rmi_dbmd = var1;
      this.parent_conn = var2;
   }

   public Connection getConnection() throws SQLException {
      String var1 = "getConnection";
      Object[] var2 = new Object[0];

      try {
         this.preInvocationHandler(var1, var2);
         this.postInvocationHandler(var1, var2, this.parent_conn);
      } catch (Exception var4) {
         this.invocationExceptionHandler(var1, var2, var4);
      }

      return this.parent_conn;
   }
}
