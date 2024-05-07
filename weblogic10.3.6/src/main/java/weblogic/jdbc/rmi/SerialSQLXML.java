package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLXML;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;

public class SerialSQLXML extends RMIStubWrapperImpl implements Serializable {
   private static final long serialVersionUID = 5044862558599850283L;
   private SQLXML rmiSQLXML = null;
   private boolean closed = false;

   public void init(SQLXML var1) {
      this.rmiSQLXML = var1;
   }

   public static SQLXML makeSerialSQLXMLFromStub(SQLXML var0) throws SQLException {
      if (var0 == null) {
         return null;
      } else {
         SerialSQLXML var1 = (SerialSQLXML)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.SerialSQLXML", var0, false);
         var1.init(var0);
         return (SQLXML)var1;
      }
   }

   public void internalClose() {
      if (!this.closed) {
         try {
            ((weblogic.jdbc.rmi.internal.SQLXML)this.rmiSQLXML).internalClose();
         } catch (Throwable var2) {
         }

         this.closed = true;
      }
   }
}
