package weblogic.jdbc.rmi.internal;

import java.sql.SQLException;

public class ResultSetMetaDataImpl implements ResultSetMetaData {
   private java.sql.ResultSetMetaData t2_rsmd;

   public ResultSetMetaDataImpl(java.sql.ResultSetMetaData var1) {
      this.t2_rsmd = null;
      this.t2_rsmd = var1;
   }

   public ResultSetMetaDataImpl() {
      this((java.sql.ResultSetMetaData)null);
   }

   public int getColumnCount() throws SQLException {
      return this.t2_rsmd.getColumnCount();
   }

   public boolean isAutoIncrement(int var1) throws SQLException {
      return this.t2_rsmd.isAutoIncrement(var1);
   }

   public boolean isCaseSensitive(int var1) throws SQLException {
      return this.t2_rsmd.isCaseSensitive(var1);
   }

   public boolean isSearchable(int var1) throws SQLException {
      return this.t2_rsmd.isSearchable(var1);
   }

   public boolean isCurrency(int var1) throws SQLException {
      return this.t2_rsmd.isCurrency(var1);
   }

   public int isNullable(int var1) throws SQLException {
      return this.t2_rsmd.isNullable(var1);
   }

   public boolean isSigned(int var1) throws SQLException {
      return this.t2_rsmd.isSigned(var1);
   }

   public int getColumnDisplaySize(int var1) throws SQLException {
      return this.t2_rsmd.getColumnDisplaySize(var1);
   }

   public String getColumnLabel(int var1) throws SQLException {
      return this.t2_rsmd.getColumnLabel(var1);
   }

   public String getColumnName(int var1) throws SQLException {
      return this.t2_rsmd.getColumnName(var1);
   }

   public String getSchemaName(int var1) throws SQLException {
      return this.t2_rsmd.getSchemaName(var1);
   }

   public int getPrecision(int var1) throws SQLException {
      return this.t2_rsmd.getPrecision(var1);
   }

   public int getScale(int var1) throws SQLException {
      return this.t2_rsmd.getScale(var1);
   }

   public String getTableName(int var1) throws SQLException {
      return this.t2_rsmd.getTableName(var1);
   }

   public String getCatalogName(int var1) throws SQLException {
      return this.t2_rsmd.getCatalogName(var1);
   }

   public int getColumnType(int var1) throws SQLException {
      return this.t2_rsmd.getColumnType(var1);
   }

   public String getColumnTypeName(int var1) throws SQLException {
      return this.t2_rsmd.getColumnTypeName(var1);
   }

   public boolean isReadOnly(int var1) throws SQLException {
      return this.t2_rsmd.isReadOnly(var1);
   }

   public boolean isWritable(int var1) throws SQLException {
      return this.t2_rsmd.isWritable(var1);
   }

   public boolean isDefinitelyWritable(int var1) throws SQLException {
      return this.t2_rsmd.isDefinitelyWritable(var1);
   }

   public String getColumnClassName(int var1) throws SQLException {
      return this.t2_rsmd.getColumnClassName(var1);
   }

   public <T> T unwrap(Class<T> var1) throws SQLException {
      return var1.isInstance(this.t2_rsmd) ? var1.cast(this.t2_rsmd) : this.t2_rsmd.unwrap(var1);
   }

   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return this.t2_rsmd != null ? this.t2_rsmd.isWrapperFor(var1) : false;
   }
}
