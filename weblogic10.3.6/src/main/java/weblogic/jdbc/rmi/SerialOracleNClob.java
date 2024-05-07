package weblogic.jdbc.rmi;

import java.sql.NClob;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialOracleNClob extends SerialOracleClob {
   private static final long serialVersionUID = 6168612739413116233L;

   public static NClob makeSerialOracleNClob(NClob var0) {
      if (var0 == null) {
         return null;
      } else {
         SerialOracleNClob var1 = (SerialOracleNClob)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialOracleNClob", var0, false);
         var1.init(var0);
         return (NClob)var1;
      }
   }
}
