package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.Ref;
import java.sql.SQLException;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialRef extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = 5037775703210750679L;
   private Ref rmiRef = null;

   public void init(Ref var1) {
      this.rmiRef = var1;
   }

   public static Ref makeSerialRefFromStub(Ref var0) throws SQLException {
      if (var0 == null) {
         return null;
      } else {
         SerialRef var1 = (SerialRef)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialRef", var0, false);
         var1.init(var0);
         return (Ref)var1;
      }
   }
}
