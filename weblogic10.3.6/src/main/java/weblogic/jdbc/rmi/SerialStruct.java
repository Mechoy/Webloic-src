package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Struct;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialStruct extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = -1333590052490436895L;
   private Struct rmiStruct = null;

   public void init(Struct var1) {
      this.rmiStruct = var1;
   }

   public static Struct makeSerialStructFromStub(Struct var0) throws SQLException {
      if (var0 == null) {
         return null;
      } else {
         SerialStruct var1 = (SerialStruct)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialStruct", var0, false);
         var1.init(var0);
         return (Struct)var1;
      }
   }
}
