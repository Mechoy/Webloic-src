package weblogic.jdbc.rowset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.Locale;

public class OracleTableWriter extends TableWriter {
   OracleTableWriter(WLRowSetInternal var1, String var2, BitSet var3) throws SQLException {
      super(var1, var2, var3);
   }

   public void issueSQL(Connection var1) throws SQLException {
      this.checkBatchUpdateCounts = false;
      if (this.batchDeletes) {
         this.batchDeletes = false;
         this.groupDeletes = true;
      }

      super.issueSQL(var1);
   }

   protected void executeBatchVerifySelects(Connection var1) throws SQLException {
      if (!this.batchVerifyParams.isEmpty()) {
         int var2 = 0;

         do {
            this.executeBatchVerifySelects(var1, var2);
            var2 += this.batchVerifySize;
         } while(var2 < this.batchVerifyParams.size());

      }
   }

   private void executeBatchVerifySelects(Connection var1, int var2) throws SQLException {
      StringBuffer var3 = new StringBuffer(500);
      var3.append("SELECT 1 from ").append(this.tableName).append(" WHERE ");
      String var4 = "";
      int var5 = Math.min(var2 + this.batchVerifySize, this.batchVerifyParams.size());

      for(int var6 = var2; var6 < var5; ++var6) {
         var3.append(var4);
         var4 = " OR ";
         TableWriter.BatchVerifyParam var7 = (TableWriter.BatchVerifyParam)this.batchVerifyParams.get(var6);
         var3.append(var7.getWhereClause());
      }

      if (var1.getMetaData().getDatabaseProductName().toLowerCase(Locale.ENGLISH).indexOf("oracle") != -1) {
         var3.append(" FOR UPDATE NOWAIT");
      }

      String var23 = var3.toString();
      PreparedStatement var24 = null;
      ResultSet var8 = null;

      try {
         var24 = var1.prepareStatement(var23);
         int var9 = 1;

         int var10;
         for(var10 = var2; var10 < var5; ++var10) {
            TableWriter.BatchVerifyParam var11 = (TableWriter.BatchVerifyParam)this.batchVerifyParams.get(var10);
            var9 = this.setWhereParameters(var24, var11.getCols(), var11.getFilter(), var9);
         }

         var8 = var24.executeQuery();

         for(var10 = 0; var8.next(); ++var10) {
         }

         if (var10 != var5 - var2) {
            this.throwOCE(var23, (CachedRow)null);
         }
      } finally {
         if (var8 != null) {
            try {
               var8.close();
            } catch (Exception var21) {
            }
         }

         if (var24 != null) {
            try {
               var24.close();
            } catch (Exception var20) {
            }
         }

      }

   }
}
