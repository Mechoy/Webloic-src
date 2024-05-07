package weblogic.jdbc.rowset;

import javax.sql.DataSource;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.WebRowSet;

public final class RowSetFactoryImpl extends RowSetFactory {
   String dataSourceName = null;
   String username = null;
   String password = null;
   String url = null;
   DataSource dataSource = null;

   public WLCachedRowSet newCachedRowSet() {
      CachedRowSetImpl var1 = new CachedRowSetImpl();
      if (this.dataSourceName != null) {
         var1.setDataSourceName(this.dataSourceName);
      }

      if (this.username != null) {
         var1.setUsername(this.username);
      }

      if (this.password != null) {
         var1.setPassword(this.password);
      }

      if (this.url != null) {
         var1.setUrl(this.url);
      }

      if (this.dataSource != null) {
         var1.setDataSource(this.dataSource);
      }

      return var1;
   }

   public JoinRowSet newJoinRowSet() {
      return new JoinRowSetImpl();
   }

   public JdbcRowSet newJdbcRowSet() {
      return new JdbcRowSetImpl();
   }

   public FilteredRowSet newFilteredRowSet() {
      return new FilteredRowSetImpl();
   }

   public WebRowSet newWebRowSet() {
      return new WebRowSetImpl();
   }

   public String getDataSourceName() {
      return this.dataSourceName;
   }

   public void setDataSourceName(String var1) {
      this.dataSourceName = var1;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String var1) {
      this.url = var1;
   }

   public DataSource getDataSource() {
      return this.dataSource;
   }

   public void setDataSource(DataSource var1) {
      this.dataSource = var1;
   }
}
