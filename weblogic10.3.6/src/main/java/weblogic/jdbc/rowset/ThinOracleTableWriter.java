package weblogic.jdbc.rowset;

import java.io.CharArrayReader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ThinOracleTableWriter extends OracleTableWriter {
   private List lobUpdateRows = null;
   protected boolean needs2PhaseInsert;
   private Map canonicalLOBUpdateColumns;

   public ThinOracleTableWriter(WLRowSetInternal var1, String var2, BitSet var3) throws SQLException {
      super(var1, var2, var3);
   }

   protected void insertRow(Connection var1, CachedRow var2) throws SQLException {
      this.needs2PhaseInsert = false;
      super.insertRow(var1, var2);
      if (this.needs2PhaseInsert) {
         this.addUpdateRow(var2);
      }

   }

   private void addUpdateRow(CachedRow var1) {
      if (this.lobUpdateRows == null) {
         this.lobUpdateRows = new ArrayList();
      }

      try {
         for(int var2 = 0; var2 < this.metaData.getColumnCount(); ++var2) {
            if (this.isLOB(var2) && this.tableName.equals(this.metaData.getQualifiedTableName(var2 + 1)) && (var1.isUpdatedRow() && var1.isModified(var2 + 1) || var1.isInsertRow())) {
               this.lobUpdateRows.add(var1);
               return;
            }
         }
      } catch (Exception var3) {
      }

   }

   protected Object insertedObject(Connection var1, Object var2) {
      if (var2 == null) {
         return null;
      } else {
         Class var3 = var2.getClass();
         if (var3 == RowSetNClob.class) {
            return this.emptyNClob(var1);
         } else if (var3 == RowSetClob.class) {
            return this.emptyClob(var1);
         } else {
            return var3 == RowSetBlob.class ? this.emptyBlob(var1) : var2;
         }
      }
   }

   protected Object emptyClob(Connection var1) {
      this.needs2PhaseInsert = true;

      try {
         return var1.createClob();
      } catch (Exception var3) {
         throw new AssertionError(var3);
      }
   }

   protected Object emptyNClob(Connection var1) {
      this.needs2PhaseInsert = true;

      try {
         return var1.createNClob();
      } catch (Exception var3) {
         throw new AssertionError(var3);
      }
   }

   protected Object emptyBlob(Connection var1) {
      this.needs2PhaseInsert = true;

      try {
         return var1.createBlob();
      } catch (Exception var3) {
         throw new AssertionError(var3);
      }
   }

   protected BitSet getModifiedColumns(CachedRow var1) {
      BitSet var2 = var1.getModifiedColumns();
      BitSet var3 = null;
      Object[] var4 = var1.getColumns();

      for(int var5 = var2.nextSetBit(0); var5 >= 0; var5 = var2.nextSetBit(var5 + 1)) {
         if (var4[var5] instanceof RowSetBlob || var4[var5] instanceof RowSetClob) {
            if (var3 == null) {
               var3 = (BitSet)var2.clone();
               this.addUpdateRow(var1);
            }

            var3.clear(var5);
         }
      }

      return var3 != null ? var3 : var2;
   }

   protected void updateLOBs(Connection var1) throws SQLException {
      if (this.lobUpdateRows != null) {
         this.canonicalLOBUpdateColumns = new HashMap();

         try {
            Iterator var2 = this.lobUpdateRows.iterator();

            while(var2.hasNext()) {
               this.updateLOBs(var1, (CachedRow)var2.next());
            }
         } finally {
            Iterator var5 = this.canonicalLOBUpdateColumns.values().iterator();

            while(var5.hasNext()) {
               ((LOBUpdateColumns)var5.next()).closeStatements();
            }

            this.canonicalLOBUpdateColumns = null;
         }

      }
   }

   private void updateLOBs(Connection var1, CachedRow var2) throws SQLException {
      LOBUpdateColumns var3 = this.getLOBUpdateColumns(var1, var2);
      ResultSet var4 = null;

      for(int var5 = 0; var5 < 3; ++var5) {
         try {
            var4 = var3.selectForUpdate(var2);
            if (!var4.next()) {
               this.throwOCE(var3.selectSql, var2);
            }

            if (var3.executeUpdate(var1, var4, var2)) {
               return;
            }
         } finally {
            if (var4 != null) {
               var4.close();
            }

         }
      }

      throw new SQLException("Failed to update row in three passes");
   }

   private LOBUpdateColumns getLOBUpdateColumns(Connection var1, CachedRow var2) throws SQLException {
      LOBUpdateColumns var3 = this.newLOBUpdateColumns(var2);
      LOBUpdateColumns var4 = (LOBUpdateColumns)this.canonicalLOBUpdateColumns.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         var3.cacheAll(var1);
         this.canonicalLOBUpdateColumns.put(var3, var3);
         return var3;
      }
   }

   protected LOBUpdateColumns newLOBUpdateColumns(CachedRow var1) throws SQLException {
      return new LOBUpdateColumns(var1);
   }

   class LOBUpdateColumns extends RowSetLob.UpdateHelper {
      private final BitSet lobCols = new BitSet();
      private BitSet keyCols;
      private String selectSql;
      private String updateSql;
      private PreparedStatement selectPS;
      private PreparedStatement updatePS;
      protected boolean updateAgain;

      LOBUpdateColumns(CachedRow var2) throws SQLException {
         Object[] var3 = var2.getColumns();
         BitSet var4 = var2.getModifiedColumns();

         for(int var5 = var4.nextSetBit(0); var5 >= 0; var5 = var4.nextSetBit(var5 + 1)) {
            if (var3[var5] instanceof RowSetLob && ThinOracleTableWriter.this.tableName.equals(ThinOracleTableWriter.this.metaData.getQualifiedTableName(var5 + 1))) {
               this.lobCols.set(var5);
            }
         }

      }

      public boolean equals(Object var1) {
         return var1 instanceof LOBUpdateColumns && this.lobCols.equals(((LOBUpdateColumns)var1).lobCols);
      }

      public int hashCode() {
         return this.lobCols.hashCode();
      }

      void cacheAll(Connection var1) throws SQLException {
         this.keyCols = new BitSet();
         int var2 = ThinOracleTableWriter.this.metaData.getColumnCount();

         for(int var3 = 1; var3 <= var2; ++var3) {
            if (ThinOracleTableWriter.this.metaData.isPrimaryKeyColumn(var3) && ThinOracleTableWriter.this.tableName.equals(ThinOracleTableWriter.this.metaData.getQualifiedTableName(var3))) {
               this.keyCols.set(var3 - 1);
            }
         }

         this.selectSql = this.getSelectSql();
         this.updateSql = this.getUpdateSql();
         this.selectPS = var1.prepareStatement(this.selectSql);
         this.updatePS = var1.prepareStatement(this.updateSql);
      }

      private String getSelectSql() throws SQLException {
         if (!ThinOracleTableWriter.this.metaData.haveSetPKColumns()) {
            throw new SQLException("You must use the WLRowSetMetaData.setPrimaryKeyColumn() method to establish primary key columns before updating rows.");
         } else {
            StringBuffer var1 = new StringBuffer(300);
            var1.append("SELECT ");
            String var2 = "";

            for(int var3 = this.lobCols.nextSetBit(0); var3 >= 0; var3 = this.lobCols.nextSetBit(var3 + 1)) {
               var1.append(var2);
               var1.append(ThinOracleTableWriter.this.metaData.getWriteColumnName(var3 + 1));
               var2 = ", ";
            }

            var1.append(" FROM ").append(ThinOracleTableWriter.this.tableName).append(" WHERE ");
            TableWriter.ColumnFilter var7 = TableWriter.getPolicy(ThinOracleTableWriter.this.metaData.getOptimisticPolicy()).getColumnFilter(ThinOracleTableWriter.this, this.lobCols);
            boolean var4 = false;
            var2 = "";

            for(int var5 = 0; var5 < ThinOracleTableWriter.this.metaData.getColumnCount(); ++var5) {
               if (ThinOracleTableWriter.this.columnMask.get(var5) && !ThinOracleTableWriter.this.isLOB(var5) && var7.include(var5) && ThinOracleTableWriter.this.tableName.equals(ThinOracleTableWriter.this.metaData.getQualifiedTableName(var5 + 1))) {
                  String var6 = ThinOracleTableWriter.this.metaData.getWriteColumnName(var5 + 1);
                  if (ThinOracleTableWriter.this.metaData.isPrimaryKeyColumn(var5 + 1)) {
                     var1.append(var2).append(var6).append("=? ");
                     var4 = true;
                  } else {
                     var1.append(var2).append("(").append(var6).append("=? OR ");
                     var1.append("(1=? AND ").append(var6).append(" IS NULL))");
                  }

                  var2 = " AND ";
               }
            }

            if (!var4) {
               throw new SQLException("You must use the WLRowSetMetaData.setPrimaryKeyColumn() method to establish primary key columns for table " + ThinOracleTableWriter.this.tableName + " before updating rows.");
            } else {
               return var1.append(" FOR UPDATE NOWAIT").toString();
            }
         }
      }

      private String getUpdateSql() throws SQLException {
         StringBuffer var1 = new StringBuffer(300);
         var1.append("UPDATE ").append(ThinOracleTableWriter.this.tableName).append(" SET ");
         String var2 = "";

         int var3;
         for(var3 = this.lobCols.nextSetBit(0); var3 >= 0; var3 = this.lobCols.nextSetBit(var3 + 1)) {
            var1.append(var2);
            var1.append(ThinOracleTableWriter.this.metaData.getWriteColumnName(var3 + 1)).append("=?");
            var2 = ", ";
         }

         var2 = "";
         var1.append(" WHERE ");

         for(var3 = this.keyCols.nextSetBit(0); var3 >= 0; var3 = this.keyCols.nextSetBit(var3 + 1)) {
            var1.append(var2);
            var1.append(ThinOracleTableWriter.this.metaData.getWriteColumnName(var3 + 1)).append("=?");
            var2 = " AND ";
         }

         return var1.toString();
      }

      void closeStatements() {
         try {
            this.selectPS.close();
         } catch (SQLException var3) {
         }

         try {
            this.updatePS.close();
         } catch (SQLException var2) {
         }

      }

      ResultSet selectForUpdate(CachedRow var1) throws SQLException {
         Object[] var2 = var1.getColumns();
         ArrayList var3 = ThinOracleTableWriter.this.verboseSQL ? new ArrayList() : null;
         int var4 = 1;
         TableWriter.ColumnFilter var5 = TableWriter.getPolicy(ThinOracleTableWriter.this.metaData.getOptimisticPolicy()).getColumnFilter(ThinOracleTableWriter.this, this.lobCols);

         for(int var6 = 0; var6 < ThinOracleTableWriter.this.columnCount; ++var6) {
            if (ThinOracleTableWriter.this.columnMask.get(var6) && !ThinOracleTableWriter.this.isLOB(var6) && var5.include(var6) && ThinOracleTableWriter.this.tableName.equals(ThinOracleTableWriter.this.metaData.getQualifiedTableName(var6 + 1))) {
               if (var2[var6] instanceof Integer) {
                  this.selectPS.setInt(var4++, (Integer)var2[var6]);
               } else if (var2[var6] instanceof Float) {
                  this.selectPS.setFloat(var4++, (Float)var2[var6]);
               } else if (var2[var6] instanceof char[]) {
                  this.selectPS.setCharacterStream(var4++, new CharArrayReader((char[])((char[])var2[var6])), ((char[])((char[])var2[var6])).length);
               } else {
                  this.selectPS.setObject(var4++, var2[var6], ThinOracleTableWriter.this.metaData.getColumnType(var6 + 1));
               }

               if (ThinOracleTableWriter.this.verboseSQL) {
                  var3.add(var2[var6]);
               }

               if (!ThinOracleTableWriter.this.metaData.isPrimaryKeyColumn(var6 + 1)) {
                  int var7 = var2[var6] == null ? 1 : 0;
                  this.selectPS.setInt(var4++, var7);
                  if (ThinOracleTableWriter.this.verboseSQL) {
                     var3.add(new Integer(var7));
                  }
               }
            }
         }

         if (ThinOracleTableWriter.this.verboseSQL) {
            ThinOracleTableWriter.this.printSQL(this.selectSql, var3.toArray(new Object[var3.size()]));
         }

         return this.selectPS.executeQuery();
      }

      boolean executeUpdate(Connection var1, ResultSet var2, CachedRow var3) throws SQLException {
         this.updateAgain = false;
         int var4 = 1;
         Object[] var5 = var3.getColumns();
         ArrayList var6 = ThinOracleTableWriter.this.verboseSQL ? new ArrayList() : null;

         int var7;
         for(var7 = this.lobCols.nextSetBit(0); var7 >= 0; var7 = this.lobCols.nextSetBit(var7 + 1)) {
            Object var8 = ((RowSetLob)var5[var7]).update(var1, var2, var4, this);
            if (var8 instanceof NClob) {
               this.updatePS.setNClob(var4++, (NClob)var8);
            } else {
               this.updatePS.setObject(var4++, var8, ThinOracleTableWriter.this.metaData.getColumnType(var7 + 1));
            }

            if (ThinOracleTableWriter.this.verboseSQL) {
               var6.add(var8);
            }
         }

         for(var7 = this.keyCols.nextSetBit(0); var7 >= 0; var7 = this.keyCols.nextSetBit(var7 + 1)) {
            this.updatePS.setObject(var4++, var5[var7], ThinOracleTableWriter.this.metaData.getColumnType(var7 + 1));
            if (ThinOracleTableWriter.this.verboseSQL) {
               var6.add(var5[var7]);
            }
         }

         if (ThinOracleTableWriter.this.verboseSQL) {
            ThinOracleTableWriter.this.printSQL(this.updateSql, var6.toArray(new Object[var6.size()]));
         }

         this.updatePS.execute();
         return !this.updateAgain;
      }

      private int setBits(BitSet var1) {
         int var2 = 0;

         for(int var3 = var1.nextSetBit(0); var3 >= 0; var3 = var1.nextSetBit(var3 + 1)) {
            ++var2;
         }

         return var2;
      }

      protected Object update(Connection var1, Blob var2, byte[] var3) throws SQLException {
         if (var2 != null && var2.length() <= (long)var3.length) {
            var2.setBytes(1L, var3);
            return var2;
         } else {
            this.updateAgain = true;
            return var1.createBlob();
         }
      }

      protected Object update(Connection var1, Clob var2, char[] var3) throws SQLException {
         if (var2 != null && var2.length() <= (long)var3.length) {
            Writer var4 = var2.setCharacterStream(1L);

            try {
               var4.write(var3);
               var4.flush();
               return var2;
            } catch (Exception var6) {
               throw new AssertionError(var6);
            }
         } else {
            this.updateAgain = true;
            return var1.createClob();
         }
      }

      protected Object update(Connection var1, NClob var2, char[] var3) throws SQLException {
         if (var2 != null && var2.length() <= (long)var3.length) {
            Writer var4 = var2.setCharacterStream(1L);

            try {
               var4.write(var3);
               var4.flush();
               return var2;
            } catch (Exception var6) {
               throw new AssertionError(var6);
            }
         } else {
            this.updateAgain = true;
            return var1.createNClob();
         }
      }
   }
}
