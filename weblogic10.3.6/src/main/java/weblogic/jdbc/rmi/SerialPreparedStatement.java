package weblogic.jdbc.rmi;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialPreparedStatement extends SerialStatement {
   private static final long serialVersionUID = -8138909829902801692L;
   private PreparedStatement rmi_pstmt = null;

   public void init(PreparedStatement var1, SerialConnection var2) {
      super.init(var1, var2);
      this.rmi_pstmt = var1;
   }

   public static PreparedStatement makeSerialPreparedStatement(PreparedStatement var0, SerialConnection var1) {
      if (var0 == null) {
         return null;
      } else {
         SerialPreparedStatement var2 = (SerialPreparedStatement)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialPreparedStatement", var0, false);
         var2.init(var0, var1);
         return (PreparedStatement)var2;
      }
   }

   public ResultSetMetaData getMetaData() throws SQLException {
      SerialResultSetMetaData var1 = null;
      String var2 = "getMetaData";
      Object[] var3 = new Object[0];

      try {
         this.preInvocationHandler(var2, var3);
         var1 = new SerialResultSetMetaData(this.rmi_pstmt.getMetaData());
         this.postInvocationHandler(var2, var3, var1);
      } catch (Exception var5) {
         this.invocationExceptionHandler(var2, var3, var5);
      }

      return var1;
   }
}
