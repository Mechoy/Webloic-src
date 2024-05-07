package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialArray extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = 3806747738272517092L;
   private Array rmiArray = null;
   private boolean closed = false;

   public Object postInvocationHandler(String var1, Object[] var2, Object var3) throws Exception {
      super.postInvocationHandler(var1, var2, var3);
      if (var3 == null) {
         return null;
      } else {
         try {
            return var3 instanceof ResultSet ? SerialResultSet.makeSerialResultSet((ResultSet)var3, (SerialStatement)null) : var3;
         } catch (Exception var5) {
            JDBCLogger.logStackTrace(var5);
            return var3;
         }
      }
   }

   public void init(Array var1) {
      this.rmiArray = var1;
   }

   public static Array makeSerialArrayFromStub(Array var0) throws SQLException {
      if (var0 == null) {
         return null;
      } else {
         SerialArray var1 = (SerialArray)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialArray", var0, false);
         var1.init(var0);
         return (Array)var1;
      }
   }

   public void internalClose() {
      if (!this.closed) {
         try {
            ((weblogic.jdbc.rmi.internal.Array)this.rmiArray).internalClose();
         } catch (Throwable var2) {
         }

         this.closed = true;
      }
   }
}
