package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.Clob;
import weblogic.jdbc.rmi.internal.OracleTClob;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialOracleClob extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = -3424919475796496658L;
   private Clob rmi_c = null;
   private boolean closed = false;

   public void init(Clob var1) {
      this.rmi_c = var1;
   }

   public void internalClose() {
      if (!this.closed) {
         try {
            ((OracleTClob)this.rmi_c).internalClose();
         } catch (Throwable var2) {
         }

         this.closed = true;
      }
   }

   public static Clob makeSerialOracleClob(Clob var0) {
      if (var0 == null) {
         return null;
      } else {
         SerialOracleClob var1 = (SerialOracleClob)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialOracleClob", var0, false);
         var1.init(var0);
         return (Clob)var1;
      }
   }
}
