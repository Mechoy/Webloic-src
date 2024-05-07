package weblogic.jdbc.rmi.internal;

import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.sql.Savepoint;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.rmi.server.UnicastRemoteObject;

public class SavepointImpl extends RMISkelWrapperImpl {
   private Savepoint t2_sp = null;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      if (var3 == null) {
         super.postInvocationHandler(var1, var2, (Object)null);
         return null;
      } else {
         super.postInvocationHandler(var1, var2, var3);
         return var3;
      }
   }

   public void init(Savepoint var1) {
      this.t2_sp = var1;
   }

   public static Savepoint makeSavepointImpl(Savepoint var0) {
      if (var0 == null) {
         return null;
      } else {
         SavepointImpl var1 = (SavepointImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.SavepointImpl", var0, true);
         var1.init(var0);
         return (Savepoint)var1;
      }
   }

   void close() throws SQLException {
      try {
         UnicastRemoteObject.unexportObject(this, true);
      } catch (NoSuchObjectException var2) {
      }

   }
}
