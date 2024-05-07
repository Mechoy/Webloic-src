package weblogic.jdbc.rowset;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.BitSet;

public class OciTableWriter extends OracleTableWriter {
   public OciTableWriter(WLRowSetInternal var1, String var2, BitSet var3) throws SQLException {
      super(var1, var2, var3);
   }

   protected Object insertedObject(Connection var1, Object var2) {
      return this.convert(var2);
   }

   protected Object updatedObject(Object var1) {
      return this.convert(var1);
   }

   private Object convert(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Class var2 = var1.getClass();
         if (var2 == RowSetClob.class) {
            return new String(((RowSetClob)var1).getData());
         } else {
            return var2 == RowSetBlob.class ? ((RowSetBlob)var1).getData() : var1;
         }
      }
   }
}
