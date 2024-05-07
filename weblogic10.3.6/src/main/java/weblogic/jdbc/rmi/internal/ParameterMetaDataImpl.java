package weblogic.jdbc.rmi.internal;

import java.sql.SQLException;

public class ParameterMetaDataImpl implements ParameterMetaData {
   public static int parameterModeIn;
   public static int parameterModeInOut;
   public static int parameterModeOut;
   public static int parameterModeUnknown;
   public static int parameterNoNulls;
   public static int parameterNullable;
   public static int parameterNullableUnknown;
   private java.sql.ParameterMetaData t2_pmd = null;

   public ParameterMetaDataImpl(java.sql.ParameterMetaData var1) {
      this.t2_pmd = var1;
      java.sql.ParameterMetaData var10000 = this.t2_pmd;
      parameterModeIn = 1;
      var10000 = this.t2_pmd;
      parameterModeInOut = 2;
      var10000 = this.t2_pmd;
      parameterModeOut = 4;
      var10000 = this.t2_pmd;
      parameterModeUnknown = 0;
      var10000 = this.t2_pmd;
      parameterNoNulls = 0;
      var10000 = this.t2_pmd;
      parameterNullable = 1;
      var10000 = this.t2_pmd;
      parameterNullableUnknown = 2;
   }

   public ParameterMetaDataImpl() {
   }

   public String getParameterClassName(int var1) throws SQLException {
      return this.t2_pmd.getParameterClassName(var1);
   }

   public int getParameterCount() throws SQLException {
      return this.t2_pmd.getParameterCount();
   }

   public int getParameterMode(int var1) throws SQLException {
      return this.t2_pmd.getParameterMode(var1);
   }

   public int getParameterType(int var1) throws SQLException {
      return this.t2_pmd.getParameterType(var1);
   }

   public String getParameterTypeName(int var1) throws SQLException {
      return this.t2_pmd.getParameterTypeName(var1);
   }

   public int getPrecision(int var1) throws SQLException {
      return this.t2_pmd.getPrecision(var1);
   }

   public int getScale(int var1) throws SQLException {
      return this.t2_pmd.getScale(var1);
   }

   public int isNullable(int var1) throws SQLException {
      return this.t2_pmd.isNullable(var1);
   }

   public boolean isSigned(int var1) throws SQLException {
      return this.t2_pmd.isSigned(var1);
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
