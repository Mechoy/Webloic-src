package weblogic.jdbc.rowset;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class RowSetNClob extends RowSetClob implements NClob {
   private static final long serialVersionUID = -1366136586508870902L;

   public RowSetNClob() {
   }

   public RowSetNClob(String var1) {
      super(var1);
   }

   public RowSetNClob(char[] var1) {
      super(var1);
   }

   public RowSetNClob(NClob var1) throws SQLException {
      super((Clob)var1);
   }

   protected Object update(Connection var1, ResultSet var2, int var3, RowSetLob.UpdateHelper var4) throws SQLException {
      return var4.update(var1, var2.getNClob(var3), this.data);
   }
}
