package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.Blob;
import weblogic.jdbc.rmi.internal.OracleTBlob;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialOracleBlob extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = -8657894292226207928L;
   private Blob rmi_b = null;
   private boolean closed = false;

   public void init(Blob var1) {
      this.rmi_b = var1;
   }

   public void internalClose() {
      if (!this.closed) {
         try {
            ((OracleTBlob)this.rmi_b).internalClose();
         } catch (Throwable var2) {
         }

         this.closed = true;
      }
   }

   public static Blob makeSerialOracleBlob(Blob var0) {
      if (var0 == null) {
         return null;
      } else {
         SerialOracleBlob var1 = (SerialOracleBlob)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialOracleBlob", var0, false);
         var1.init(var0);
         return (Blob)var1;
      }
   }
}
