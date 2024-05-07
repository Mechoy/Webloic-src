package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.sql.RowSetInternal;
import javax.sql.RowSetWriter;
import weblogic.utils.Debug;

public final class CachedRowSetJDBCWriter implements RowSetWriter, Serializable {
   private static final long serialVersionUID = 2948281081383306001L;
   private static final String VERBOSE_JDBC_WRITER = "weblogic.jdbc.rowset.CachedRowSetJDBCWriter.verbose";
   private static final boolean VERBOSE = Boolean.getBoolean("weblogic.jdbc.rowset.CachedRowSetJDBCWriter.verbose");
   private static final String ORACLE_THIN_DRIVER_NAME = "Oracle JDBC driver";
   private static final String WL_OCI_DRIVER_NAME = "Weblogic, Inc. Java-OCI JDBC Driver (weblogicoci39)";

   public boolean writeData(RowSetInternal var1) throws SQLException {
      if (VERBOSE) {
         Debug.say("** BEGIN writeData");
      }

      WLRowSetInternal var2 = (WLRowSetInternal)var1;
      CachedRowSetMetaData var3 = (CachedRowSetMetaData)var2.getMetaData();
      if (var3.getOptimisticPolicy() == 3 && !var3.hasSelectedColumn()) {
         throw new SQLException("There is no column selected to verify.");
      } else {
         List var4 = this.buildTableWriters(var2);
         Connection var5 = var2.getConnection();
         Iterator var6 = var4.iterator();
         if (!var6.hasNext()) {
            label49: {
               Iterator var7 = var2.getCachedRows().iterator();

               CachedRow var8;
               do {
                  if (!var7.hasNext()) {
                     break label49;
                  }

                  var8 = (CachedRow)var7.next();
               } while(!var8.isInsertRow() && !var8.isDeletedRow() && !var8.isUpdatedRow());

               throw new NullUpdateException();
            }
         }

         while(var6.hasNext()) {
            TableWriter var9 = (TableWriter)var6.next();
            var9.issueSQL(var5);
         }

         return true;
      }
   }

   private String getTableName(WLRowSetMetaData var1, int var2, boolean var3, boolean var4, boolean var5, String var6) throws SQLException {
      String var7 = var1.getTableName(var2 + 1);
      String var8;
      if (var7 != null && !"".equals(var7)) {
         if (var3) {
            var8 = var1.getSchemaName(var2 + 1);
            if (var8 != null && !"".equals(var8)) {
               var7 = var8 + "." + var7;
            }
         }

         if (var4) {
            var8 = var1.getCatalogName(var2 + 1);
            if (var5) {
               if (var8 != null && !"".equals(var8)) {
                  var7 = var8 + var6 + var7;
               }
            } else if (var8 != null && !"".equals(var8)) {
               var7 = var7 + var6 + var8;
            }
         }

         return var7;
      } else {
         var8 = var1.getWriteTableName();
         if (var8 == null) {
            throw new SQLException("Unable to determine the table name for column: '" + var1.getColumnName(var2 + 1) + "'.  Please ensure that you've called " + "WLRowSetMetaData.setTableName  to set a table name " + "for this column.");
         } else {
            return var1.isPrimaryKeyColumn(var2 + 1) ? var8 : null;
         }
      }
   }

   private List buildTableWriters(WLRowSetInternal var1) throws SQLException {
      WLRowSetMetaData var2 = (WLRowSetMetaData)var1.getMetaData();
      String var3 = var2.getWriteTableName();
      DatabaseMetaData var4 = var1.getConnection().getMetaData();
      boolean var5 = var4.supportsSchemasInDataManipulation();
      boolean var6 = var4.supportsCatalogsInDataManipulation();
      boolean var7 = var4.isCatalogAtStart();
      String var8 = var4.getCatalogSeparator();
      HashMap var9 = new HashMap();

      for(int var10 = 0; var10 < var2.getColumnCount(); ++var10) {
         String var11 = this.getTableName(var2, var10, var5, var6, var7, var8);
         if (var3 == null || this.qualifiedTableEqual(var2, var10, var4, var3)) {
            BitSet var12 = (BitSet)var9.get(var11);
            if (var12 == null) {
               var12 = new BitSet();
               var9.put(var11, var12);
            }

            var12.set(var10);
         }
      }

      Iterator var13 = var9.keySet().iterator();
      ArrayList var14 = new ArrayList();

      while(var13.hasNext()) {
         String var15 = (String)var13.next();
         var14.add(this.createTableWriter(var4, var1, var15, (BitSet)var9.get(var15)));
      }

      return var14;
   }

   private TableWriter createTableWriter(DatabaseMetaData var1, WLRowSetInternal var2, String var3, BitSet var4) throws SQLException {
      if ("oracle".equalsIgnoreCase(var1.getDatabaseProductName())) {
         if ("Weblogic, Inc. Java-OCI JDBC Driver (weblogicoci39)".equals(var1.getDriverName())) {
            return new OciTableWriter(var2, var3, var4);
         } else {
            return (TableWriter)("Oracle JDBC driver".equals(var1.getDriverName()) ? new ThinOracleTableWriter(var2, var3, var4) : new OracleTableWriter(var2, var3, var4));
         }
      } else {
         return (TableWriter)(var1.getDriverName().startsWith("IBM DB2 JDBC") ? new OracleTableWriter(var2, var3, var4) : new TableWriter(var2, var3, var4));
      }
   }

   private boolean qualifiedTableEqual(WLRowSetMetaData var1, int var2, DatabaseMetaData var3, String var4) throws SQLException {
      String var5 = var1.getTableName(var2 + 1);
      String var6 = var1.getSchemaName(var2 + 1);
      String var7 = var1.getCatalogName(var2 + 1);
      TableNameParser var8 = new TableNameParser(var4, new DatabaseMetaDataHolder(var3));
      String[] var9 = var8.parse();
      return var8.identifierEqual(var5, var9[2]) && var8.identifierEqual(var6, var9[1]) && var8.identifierEqual(var7, var9[0]);
   }
}
