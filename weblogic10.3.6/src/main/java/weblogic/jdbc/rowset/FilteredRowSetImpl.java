package weblogic.jdbc.rowset;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;
import javax.sql.rowset.FilteredRowSet;

public class FilteredRowSetImpl extends CachedRowSetImpl implements FilteredRowSet {
   static final long serialVersionUID = -8547038078811651350L;

   public FilteredRowSetImpl() {
   }

   public FilteredRowSetImpl(Hashtable var1) {
      super(var1);
   }

   public <T> T getObject(int var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public <T> T getObject(String var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }
}
