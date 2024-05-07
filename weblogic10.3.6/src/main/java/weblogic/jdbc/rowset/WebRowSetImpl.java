package weblogic.jdbc.rowset;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;
import javax.sql.rowset.WebRowSet;

public class WebRowSetImpl extends CachedRowSetImpl implements WebRowSet {
   static final long serialVersionUID = -4142451464489136731L;

   public WebRowSetImpl() {
   }

   public WebRowSetImpl(Hashtable var1) {
      super(var1);
   }

   public <T> T getObject(int var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public <T> T getObject(String var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }
}
