package weblogic.jdbc.rmi;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public final class SerialResultSetMetaData implements ResultSetMetaData, Serializable {
   private static final long serialVersionUID = -6370560484041523204L;
   private ResultSetMetaData rmi_rsmd;

   public SerialResultSetMetaData() {
      this((ResultSetMetaData)null);
   }

   public SerialResultSetMetaData(ResultSetMetaData var1) {
      this.rmi_rsmd = null;
      this.rmi_rsmd = var1;
   }

   public int getColumnCount() throws SQLException {
      try {
         return this.rmi_rsmd.getColumnCount();
      } catch (Exception var2) {
         if (var2 instanceof SQLException) {
            throw (SQLException)var2;
         } else {
            throw new SQLException(var2.toString());
         }
      }
   }

   public boolean isAutoIncrement(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isAutoIncrement(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isCaseSensitive(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isCaseSensitive(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isSearchable(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isSearchable(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isCurrency(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isCurrency(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public int isNullable(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isNullable(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isSigned(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isSigned(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public int getColumnDisplaySize(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getColumnDisplaySize(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getColumnLabel(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getColumnLabel(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getColumnName(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getColumnName(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getSchemaName(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getSchemaName(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public int getPrecision(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getPrecision(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public int getScale(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getScale(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getTableName(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getTableName(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getCatalogName(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getCatalogName(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public int getColumnType(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getColumnType(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getColumnTypeName(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getColumnTypeName(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isReadOnly(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isReadOnly(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isWritable(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isWritable(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public boolean isDefinitelyWritable(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.isDefinitelyWritable(var1);
      } catch (Exception var3) {
         if (var3 instanceof SQLException) {
            throw (SQLException)var3;
         } else {
            throw new SQLException(var3.toString());
         }
      }
   }

   public String getColumnClassName(int var1) throws SQLException {
      try {
         return this.rmi_rsmd.getColumnClassName(var1);
      } catch (Exception var3) {
         throw new SQLException(var3.toString());
      }
   }

   public <T> T unwrap(Class<T> var1) throws SQLException {
      if (var1.isInstance(this)) {
         return var1.cast(this);
      } else {
         throw new SQLException(this + " is not an instance of " + var1);
      }
   }

   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return var1.isInstance(this);
   }
}
