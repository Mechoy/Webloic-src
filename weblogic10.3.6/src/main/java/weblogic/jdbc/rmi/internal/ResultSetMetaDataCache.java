package weblogic.jdbc.rmi.internal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;

public class ResultSetMetaDataCache implements Serializable {
   private int[] cachedTypes;
   private HashMap nameColLookup;
   private static final int INITIAL_HASH_SIZE = 89;

   ResultSetMetaDataCache() {
   }

   ResultSetMetaDataCache(java.sql.ResultSet var1) throws SQLException {
      if (var1 != null) {
         java.sql.ResultSetMetaData var2 = var1.getMetaData();
         int var3 = var2.getColumnCount();
         this.cachedTypes = new int[var3];
         this.nameColLookup = new HashMap(89);

         for(int var4 = 0; var4 < var3; ++var4) {
            this.cachedTypes[var4] = var2.getColumnType(var4 + 1);
            String var5 = var2.getColumnName(var4 + 1);
            this.nameColLookup.put(var5.toLowerCase(), new Integer(var4 + 1));
         }

      }
   }

   int getColumnCount() {
      return this.cachedTypes.length;
   }

   int getColumnType(int var1) {
      return this.cachedTypes[var1 - 1];
   }

   int getColumnTypeZeroBased(int var1) {
      return this.cachedTypes[var1];
   }

   int getColumnType(String var1) throws SQLException {
      return this.getColumnType(this.findColumn(var1));
   }

   public int findColumn(String var1) throws SQLException {
      String var2 = var1.toLowerCase();
      Integer var3 = (Integer)this.nameColLookup.get(var2);
      if (var3 == null) {
         throw new SQLException("no such column in this ResultSet:" + var1);
      } else {
         return var3;
      }
   }
}
