package weblogic.jdbc.rowset;

import java.io.CharArrayReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.utils.AssertionError;
import weblogic.utils.PlatformConstants;

public class TableWriter implements PlatformConstants {
   protected static final ColumnFilter ALL = new ColumnFilter() {
      public boolean include(int var1) {
         return true;
      }
   };
   private final boolean verbose = false;
   protected final String tableName;
   protected final BitSet columnMask;
   private final WLRowSetInternal rowSet;
   protected final WLRowSetMetaData metaData;
   protected final int columnCount;
   private final boolean batchInserts;
   protected boolean batchDeletes;
   private final boolean batchUpdates;
   protected boolean groupDeletes;
   protected final boolean verboseSQL;
   protected DebugLogger JDBCRowsetDebug = null;
   private final Map batchMap;
   private List groupDeleteList;
   private String __dont_touch_me_insert_statement;
   private String __dont_touch_me_update_header;
   private String __dont_touch_me_delete_header;
   private final int groupDeleteSize;
   protected boolean checkBatchUpdateCounts;
   protected List batchVerifyParams;
   protected final int batchVerifySize;

   public TableWriter(WLRowSetInternal var1, String var2, BitSet var3) throws SQLException {
      this.groupDeleteList = Collections.EMPTY_LIST;
      this.__dont_touch_me_insert_statement = null;
      this.__dont_touch_me_update_header = null;
      this.__dont_touch_me_delete_header = null;
      this.checkBatchUpdateCounts = true;
      this.batchVerifyParams = Collections.EMPTY_LIST;
      this.rowSet = var1;
      this.tableName = var2;
      this.columnMask = var3;
      this.metaData = (WLRowSetMetaData)var1.getMetaData();
      this.columnCount = this.metaData.getColumnCount();
      this.batchInserts = this.metaData.getBatchInserts();
      this.batchDeletes = this.metaData.getBatchDeletes();
      this.batchUpdates = this.metaData.getBatchUpdates();
      this.groupDeletes = this.metaData.getGroupDeletes();
      this.verboseSQL = this.metaData.getVerboseSQL();
      this.groupDeleteSize = this.metaData.getGroupDeleteSize();
      this.batchVerifySize = this.metaData.getBatchVerifySize();
      if (this.verboseSQL && this.JDBCRowsetDebug == null) {
         this.JDBCRowsetDebug = DebugLogger.createUnregisteredDebugLogger("JDBCRowset", true);
      }

      if (!this.batchInserts && !this.batchDeletes && !this.batchUpdates) {
         this.batchMap = Collections.EMPTY_MAP;
      } else {
         this.batchMap = new HashMap();
      }

   }

   public void issueSQL(Connection var1) throws SQLException {
      if (this.groupDeletes) {
         this.groupDeleteList = new ArrayList();
      } else {
         this.groupDeleteList = Collections.EMPTY_LIST;
      }

      if (!this.checkBatchUpdateCounts) {
         this.batchVerifyParams = new ArrayList();
      }

      try {
         Iterator var2 = this.rowSet.getCachedRows().iterator();

         while(true) {
            while(true) {
               CachedRow var3;
               do {
                  if (!var2.hasNext()) {
                     this.executeGroupDeletes(var1);
                     this.executeBatchVerifySelects(var1);
                     this.executeBatches();
                     this.updateLOBs(var1);
                     return;
                  }

                  var3 = (CachedRow)var2.next();
               } while(var3.isInsertRow() && var3.isDeletedRow());

               if (var3.isInsertRow() && var3.isUpdatedRow()) {
                  this.updateRow(var1, var3);
               } else if (var3.isInsertRow()) {
                  this.insertRow(var1, var3);
               } else if (var3.isDeletedRow()) {
                  this.deleteRow(var1, var3);
               } else if (var3.isUpdatedRow()) {
                  this.updateRow(var1, var3);
               }
            }
         }
      } finally {
         this.cleanupBatches();
      }
   }

   protected void insertRow(Connection var1, CachedRow var2) throws SQLException {
      Object[] var3 = var2.getColumns();
      if (this.verboseSQL) {
         this.printSQL(this.getInsertStatement(), var3, new AutoIncFilter(this.metaData));
      }

      PreparedStatement var4;
      if (this.batchInserts) {
         var4 = this.getBatchPS(var1, this.getInsertStatement());
         this.setInsertParameters(var1, var4, var3);
         var4.addBatch();
      } else {
         var4 = null;

         try {
            var4 = var1.prepareStatement(this.getInsertStatement());
            this.setInsertParameters(var1, var4, var3);
            if (var4.executeUpdate() == 0) {
               this.throwOCE(this.formatSQL(this.getInsertStatement(), var3, new AutoIncFilter(this.metaData)), var2);
            }
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Exception var11) {
               }
            }

         }
      }

   }

   protected BitSet getModifiedColumns(CachedRow var1) {
      return var1.getModifiedColumns();
   }

   protected Object insertedObject(Connection var1, Object var2) {
      return var2;
   }

   protected Object updatedObject(Object var1) {
      return var1;
   }

   protected void executeBatchVerifySelects(Connection var1) throws SQLException {
   }

   protected void updateLOBs(Connection var1) throws SQLException {
   }

   private void printSQL(String var1) {
      this.JDBCRowsetDebug.debug(var1);
   }

   private void printSQL(String var1, Object[] var2, ColumnFilter var3, Object[] var4, ColumnFilter var5) throws SQLException {
      this.JDBCRowsetDebug.debug(this.formatSQL(var1, var2, var3, var4, var5));
   }

   private String formatSQL(String var1, Object[] var2, ColumnFilter var3, Object[] var4, ColumnFilter var5) throws SQLException {
      StringBuffer var6 = new StringBuffer();
      var6.append(var1);
      var6.append(EOL);
      var6.append("PARAMETERS: ");
      String var7 = "";

      int var8;
      for(var8 = 0; var8 < var2.length; ++var8) {
         if (this.columnMask.get(var8) && var3.include(var8) && !this.metaData.isAutoIncrement(var8 + 1)) {
            var6.append(var7);
            var7 = ", ";
            if (var2[var8] == null) {
               var6.append("<NULL>");
            } else {
               var6.append(var2[var8].toString());
            }
         }
      }

      for(var8 = 0; var8 < var4.length; ++var8) {
         if (this.columnMask.get(var8) && var5.include(var8) && !this.isLOB(var8) && var4[var8] != null) {
            var6.append(var7);
            var7 = ", ";
            var6.append(var4[var8].toString());
         }
      }

      return var6.toString();
   }

   protected void printSQL(String var1, Object[] var2, ColumnFilter var3) throws SQLException {
      this.JDBCRowsetDebug.debug(this.formatSQL(var1, var2, var3));
   }

   private String formatSQL(String var1, Object[] var2, ColumnFilter var3) throws SQLException {
      StringBuffer var4 = new StringBuffer();
      var4.append(var1);
      var4.append(EOL);
      if (var2 != null && var2.length > 0) {
         var4.append("PARAMETERS: ");
         String var5 = "";

         for(int var6 = 0; var6 < var2.length; ++var6) {
            if (this.columnMask.get(var6) && var3.include(var6)) {
               var4.append(var5);
               var5 = ", ";
               if (var2[var6] == null) {
                  var4.append("<NULL>");
               } else {
                  var4.append(var2[var6].toString());
               }
            }
         }
      }

      return var4.toString();
   }

   protected void printSQL(String var1, Object[] var2) throws SQLException {
      StringBuffer var3 = new StringBuffer();
      var3.append(var1);
      var3.append(EOL);
      if (var2 != null && var2.length > 0) {
         var3.append("PARAMETERS: ");
         String var4 = "";

         for(int var5 = 0; var5 < var2.length; ++var5) {
            var3.append(var4);
            var4 = ", ";
            if (var2[var5] == null) {
               var3.append("<NULL>");
            } else {
               var3.append(var2[var5].toString());
            }
         }
      }

      this.JDBCRowsetDebug.debug(var3.toString());
   }

   private void executeGroupDeletes(Connection var1) throws SQLException {
      if (!this.groupDeleteList.isEmpty()) {
         int var2 = 0;

         do {
            this.executeGroupDeletes(var1, var2);
            var2 += this.groupDeleteSize;
         } while(var2 < this.groupDeleteList.size());

      }
   }

   private void executeGroupDeletes(Connection var1, int var2) throws SQLException {
      StringBuffer var3 = new StringBuffer(500);
      var3.append(this.getDeleteHeader()).append("WHERE ");
      String var4 = "";
      int var5 = Math.min(var2 + this.groupDeleteSize, this.groupDeleteList.size());

      for(int var6 = var2; var6 < var5; ++var6) {
         var3.append(var4);
         var4 = " OR ";
         GroupDelete var7 = (GroupDelete)this.groupDeleteList.get(var6);
         var3.append(var7.getWhereClause());
      }

      String var19 = var3.toString();
      if (this.verboseSQL) {
         this.printSQL(var19);
      }

      PreparedStatement var20 = null;

      try {
         var20 = var1.prepareStatement(var19);
         int var8 = 1;
         int var9 = var2;

         while(true) {
            if (var9 >= var5) {
               if (var20.executeUpdate() != var5 - var2) {
                  this.throwOCE(var19, (CachedRow)null);
               }
               break;
            }

            GroupDelete var10 = (GroupDelete)this.groupDeleteList.get(var9);
            var8 = this.setWhereParameters(var20, var10.getCols(), var10.getFilter(), var8);
            ++var9;
         }
      } finally {
         if (var20 != null) {
            try {
               var20.close();
            } catch (Exception var17) {
            }
         }

      }

   }

   protected void throwOCE(String var1, CachedRow var2) throws OptimisticConflictException {
      Connection var3 = null;

      try {
         if (!this.metaData.haveSetPKColumns()) {
            throw new OptimisticConflictException("You must use the WLRowSetMetaData.setPrimaryKeyColumn() method to establish primary key columns before updating rows.");
         }

         StringBuffer var4 = new StringBuffer(500);
         String var5 = "";

         for(int var6 = 0; var6 < this.columnCount; ++var6) {
            if (this.metaData.isPrimaryKeyColumn(var6 + 1) && this.tableName.equals(this.metaData.getQualifiedTableName(var6 + 1))) {
               var4.append(var5).append(this.metaData.getWriteColumnName(var6 + 1));
               var4.append(" = ?");
               var5 = " AND ";
            }
         }

         String var15 = var4.toString();
         if ("".equals(var15)) {
            throw new OptimisticConflictException("You must use the WLRowSetMetaData.setPrimaryKeyColumn() method to establish primary key columns for table " + this.tableName);
         }

         var4 = new StringBuffer(500);
         var5 = "";
         int[] var7 = new int[this.columnCount];
         int var8 = 0;

         for(int var9 = 0; var9 < this.columnCount; ++var9) {
            if (this.tableName.equals(this.metaData.getQualifiedTableName(var9 + 1))) {
               var4.append(var5).append(this.metaData.getWriteColumnName(var9 + 1));
               var7[var8++] = var9;
               var5 = ", ";
            }
         }

         if (var8 < this.columnCount) {
            var7[var8] = -1;
         }

         String var16 = "SELECT " + var4.toString() + " FROM " + this.tableName + " WHERE " + var15;
         if (this.verboseSQL) {
            this.printSQL(var16);
         }

         var3 = this.rowSet.getConnection();
         PreparedStatement var10 = var3.prepareStatement(var16);
         int var12;
         if (var2 == null) {
            Iterator var17 = this.rowSet.getCachedRows().iterator();

            label145:
            while(true) {
               while(true) {
                  do {
                     if (!var17.hasNext()) {
                        break label145;
                     }

                     var2 = (CachedRow)var17.next();
                  } while(!var2.isInsertRow() && !var2.isDeletedRow() && !var2.isUpdatedRow());

                  var12 = 1;

                  for(int var13 = 1; var13 <= this.columnCount; ++var13) {
                     if (this.metaData.isPrimaryKeyColumn(var13) && this.tableName.equals(this.metaData.getQualifiedTableName(var13))) {
                        this.setObject(var10, var12++, var13 - 1, var2.getColumn(var13));
                     }
                  }

                  ResultSet var19 = var10.executeQuery();
                  if (var19 != null && var19.next()) {
                     if (var2.setConflictValue(var19, var7) && (!var2.isInsertRow() || !var2.isDeletedRow())) {
                        if (var2.isDeletedRow()) {
                           var2.setStatus(1);
                        } else if (var2.isUpdatedRow()) {
                           var2.setStatus(0);
                        } else if (var2.isInsertRow()) {
                           var2.setStatus(2);
                        }
                     }

                     var19.close();
                  } else if (!var2.isInsertRow() || !var2.isDeletedRow()) {
                     if (var2.isUpdatedRow()) {
                        var2.setStatus(0);
                     } else if (var2.isInsertRow()) {
                        var2.setStatus(2);
                     }
                  }
               }
            }
         } else {
            int var11 = 1;

            for(var12 = 1; var12 <= this.columnCount; ++var12) {
               if (this.metaData.isPrimaryKeyColumn(var12) && this.tableName.equals(this.metaData.getQualifiedTableName(var12))) {
                  this.setObject(var10, var11++, var12 - 1, var2.getColumn(var12));
               }
            }

            ResultSet var18 = var10.executeQuery();
            if (var18 != null && var18.next()) {
               var2.setConflictValue(var18, var7);
               var18.close();
            }

            if (!var2.isInsertRow() || !var2.isDeletedRow()) {
               if (var2.isDeletedRow()) {
                  var2.setStatus(1);
               } else if (var2.isUpdatedRow()) {
                  var2.setStatus(0);
               } else if (var2.isInsertRow()) {
                  var2.setStatus(2);
               }
            }
         }

         this.rowSet.beforeFirst();
      } catch (Exception var14) {
      }

      throw new OptimisticConflictException("Optimistic conflict when issuing sql: " + var1, (CachedRowSetImpl)this.rowSet);
   }

   private PreparedStatement getBatchPS(Connection var1, String var2) throws SQLException {
      PreparedStatement var3 = (PreparedStatement)this.batchMap.get(var2);
      if (var3 == null) {
         var3 = var1.prepareStatement(var2);
         this.batchMap.put(var2, var3);
      }

      return var3;
   }

   private void executeBatches() throws SQLException {
      Iterator var1 = this.batchMap.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         PreparedStatement var3 = (PreparedStatement)this.batchMap.get(var2);
         int[] var4 = var3.executeBatch();
         if (var4 == null) {
            throw new SQLException("The JDBC Driver returned null from PreparedStatement.executeBatch().  The executed SQL was: " + var2);
         }

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5] == 0) {
               this.throwOCE(var2, (CachedRow)null);
            } else {
               if (this.checkBatchUpdateCounts && var4[var5] == -2) {
                  throw new SQLException("The Batch update with SQL: " + var2 + " returned Statement.SUCCESS_NO_INFO indicating " + "the statement was successful, but we are unable to " + "determine whether an Optimistic conflict occurred.");
               }

               if (var4[var5] == -3) {
                  throw new SQLException("The Batch update with SQL: " + var2 + " returned Statement.EXECUTE_FAILED indicating " + "the statement execution failed without providing" + " any additional information.");
               }
            }
         }
      }

   }

   private void cleanupBatches() {
      Iterator var1 = this.batchMap.values().iterator();

      while(var1.hasNext()) {
         PreparedStatement var2 = (PreparedStatement)var1.next();

         try {
            var2.close();
         } catch (Exception var4) {
         }
      }

      this.batchMap.clear();
   }

   private void setObject(PreparedStatement var1, int var2, int var3, Object var4) throws SQLException {
      if (var4 == null) {
         var1.setNull(var2, this.metaData.getColumnType(var3 + 1));
      } else if (this.metaData.getColumnType(var3 + 1) == 91) {
         if (var4 instanceof Calendar) {
            var1.setDate(var2, new Date(((Calendar)var4).getTimeInMillis()));
         } else if (var4 instanceof java.util.Date) {
            var1.setDate(var2, new Date(((java.util.Date)var4).getTime()));
         } else {
            var1.setObject(var2, var4, this.metaData.getColumnType(var3 + 1));
         }
      } else if (var4 instanceof Integer) {
         var1.setInt(var2, (Integer)var4);
      } else if (var4 instanceof Float) {
         var1.setFloat(var2, (Float)var4);
      } else if (var4 instanceof char[]) {
         var1.setCharacterStream(var2, new CharArrayReader((char[])((char[])var4)), ((char[])((char[])var4)).length);
      } else {
         var1.setObject(var2, var4, this.metaData.getColumnType(var3 + 1));
      }

   }

   private void setInsertParameters(Connection var1, PreparedStatement var2, Object[] var3) throws SQLException {
      int var4 = 1;

      for(int var5 = 0; var5 < this.columnMask.length(); ++var5) {
         if (this.columnMask.get(var5) && !this.metaData.isAutoIncrement(var5 + 1)) {
            this.setObject(var2, var4++, var5, this.insertedObject(var1, var3[var5]));
         }
      }

   }

   private int setWhereParameters(PreparedStatement var1, Object[] var2, ColumnFilter var3) throws SQLException {
      return this.setWhereParameters(var1, var2, var3, 1);
   }

   protected int setWhereParameters(PreparedStatement var1, Object[] var2, ColumnFilter var3, int var4) throws SQLException {
      for(int var5 = 0; var5 < this.columnMask.length(); ++var5) {
         if (this.columnMask.get(var5) && var3.include(var5) && var2[var5] != null && !this.isLOB(var5)) {
            this.setObject(var1, var4++, var5, var2[var5]);
         }
      }

      return var4;
   }

   private void deleteRow(Connection var1, CachedRow var2) throws SQLException {
      OptimisticPolicy var3 = getPolicy(this.metaData.getOptimisticPolicy());
      ColumnFilter var4 = var3.getColumnFilter(this, var2.getModifiedColumns());
      Object[] var5 = null;
      if (var2.isUpdatedRow()) {
         var5 = var2.getOldColumns();
      } else {
         var5 = var2.getColumns();
      }

      if (this.groupDeletes) {
         this.groupDeleteList.add(new GroupDelete(this.getWhereClause(var5, var4), var4, var5));
      } else {
         String var6 = this.getDeleteStatement(this.rowSet, var5, var3, var4);
         if (this.verboseSQL) {
            this.printSQL(var6, var5, var4);
         }

         PreparedStatement var7 = null;

         try {
            if (this.batchDeletes) {
               var7 = this.getBatchPS(var1, var6);
            } else {
               var7 = var1.prepareStatement(var6);
            }

            this.setWhereParameters(var7, var5, var4);
            if (this.batchDeletes) {
               var7.addBatch();
            } else if (var7.executeUpdate() == 0) {
               this.throwOCE(this.formatSQL(var6, var5, var4), var2);
            }
         } finally {
            if (!this.batchDeletes && var7 != null) {
               try {
                  var7.close();
               } catch (Exception var14) {
               }
            }

         }

      }
   }

   private void updateVersionColumns(CachedRow var1) throws SQLException {
      for(int var2 = 0; var2 < this.columnMask.length(); ++var2) {
         if (this.columnMask.get(var2) && this.metaData.isAutoVersionColumn(var2 + 1)) {
            Object var3 = var1.getColumn(var2 + 1);
            if (var3 instanceof Integer) {
               int var4 = (Integer)var3 + 1;
               var1.updateColumn(var2 + 1, new Integer(var4));
            } else if (var3 instanceof Long) {
               long var6 = (Long)var3 + 1L;
               var1.updateColumn(var2 + 1, new Long(var6));
            } else if (var3 instanceof Short) {
               short var7 = (short)((Short)var3 + 1);
               var1.updateColumn(var2 + 1, new Short(var7));
            } else {
               if (!(var3 instanceof BigDecimal)) {
                  throw new SQLException("Column: " + this.metaData.getWriteColumnName(var2 + 1) + " in table: " + this.tableName + " is marked as a version column, but it is not a numeric type.");
               }

               BigDecimal var8 = ((BigDecimal)var3).add(BigDecimal.valueOf(1L));
               var1.updateColumn(var2 + 1, var8);
            }
         }
      }

   }

   private void updateRow(Connection var1, CachedRow var2) throws SQLException {
      PreparedStatement var3 = null;
      Object[] var4 = var2.getColumns();
      Object[] var5 = var2.getOldColumns();
      BitSet var6 = this.getModifiedColumns(var2);
      if (var2.isInsertRow() && var2.isUpdatedRow()) {
         var5 = var4;
      }

      try {
         OptimisticPolicy var7;
         if (var2.isInsertRow() && var2.isUpdatedRow()) {
            var7 = getPolicy(4);
         } else {
            var7 = getPolicy(this.metaData.getOptimisticPolicy());
         }

         ColumnFilter var8 = var7.getColumnFilter(this, var6);
         if (this.metaData.getOptimisticPolicy() == 5) {
            this.updateVersionColumns(var2);
         }

         String var9 = this.buildUpdateSQL(this.rowSet, var2, var7, var8, var6);
         if (var9 == null) {
            return;
         }

         if (this.verboseSQL) {
            this.printSQL(var9, var4, new ModFilter(var6), var5, var8);
         }

         if (this.batchUpdates) {
            var3 = this.getBatchPS(var1, var9);
            if (!this.checkBatchUpdateCounts) {
               this.batchVerifyParams.add(new BatchVerifyParam(this.getWhereClause(var4, var8), var8, var5));
            }
         } else {
            var3 = var1.prepareStatement(var9);
         }

         int var10 = 1;

         for(int var11 = 0; var11 < this.columnMask.length(); ++var11) {
            if (this.columnMask.get(var11) && var6.get(var11) && !this.metaData.isAutoIncrement(var11 + 1)) {
               this.setObject(var3, var10++, var11, this.updatedObject(var4[var11]));
            }
         }

         this.setWhereParameters(var3, var5, var8, var10);
         if (this.batchUpdates) {
            var3.addBatch();
         } else if (var3.executeUpdate() == 0) {
            this.throwOCE(this.formatSQL(var9, var4, new ModFilter(var6), var5, var8), var2);
         }
      } finally {
         if (!this.batchUpdates && var3 != null) {
            try {
               var3.close();
            } catch (Exception var19) {
            }
         }

      }

   }

   private String buildUpdateSQL(WLRowSetInternal var1, CachedRow var2, OptimisticPolicy var3, ColumnFilter var4, BitSet var5) throws SQLException {
      String var6 = this.getSetClause(var2, var5);
      if (var6 == null) {
         return null;
      } else {
         StringBuffer var7 = new StringBuffer(200);
         var7.append(this.getUpdateHeader());
         var7.append(var6);
         var7.append("WHERE ");
         var7.append(this.getWhereClause(var2.getOldColumns(), var4));
         return var7.toString();
      }
   }

   private String getSetClause(CachedRow var1, BitSet var2) throws SQLException {
      StringBuffer var3 = new StringBuffer();
      boolean var4 = true;
      String var5 = "";

      for(int var6 = 0; var6 < this.columnCount; ++var6) {
         if (this.columnMask.get(var6) && var2.get(var6) && !this.metaData.isAutoIncrement(var6 + 1)) {
            var4 = false;
            var3.append(var5);
            var3.append(this.metaData.getWriteColumnName(var6 + 1));
            var3.append(" = ?");
            var5 = ", ";
         }
      }

      if (var4) {
         return null;
      } else {
         var3.append(" ");
         return var3.toString();
      }
   }

   private String getUpdateHeader() {
      if (this.__dont_touch_me_update_header == null) {
         StringBuffer var1 = new StringBuffer(200);
         var1.append("UPDATE ");
         var1.append(this.tableName);
         var1.append(" SET ");
         this.__dont_touch_me_update_header = var1.toString();
      }

      return this.__dont_touch_me_update_header;
   }

   private String getDeleteHeader() {
      if (this.__dont_touch_me_delete_header == null) {
         StringBuffer var1 = new StringBuffer(200);
         var1.append("DELETE FROM ");
         var1.append(this.tableName);
         var1.append(" ");
         this.__dont_touch_me_delete_header = var1.toString();
      }

      return this.__dont_touch_me_delete_header;
   }

   private String getInsertStatement() throws SQLException {
      if (this.__dont_touch_me_insert_statement == null) {
         StringBuffer var1 = new StringBuffer();
         var1.append("INSERT INTO ");
         var1.append(this.tableName);
         var1.append(" (");
         String var2 = "";
         int var3 = 0;

         int var4;
         for(var4 = 0; var4 < this.columnCount; ++var4) {
            if (this.columnMask.get(var4) && !this.metaData.isAutoIncrement(var4 + 1)) {
               ++var3;
               var1.append(var2);
               var1.append(this.metaData.getWriteColumnName(var4 + 1));
               var2 = ", ";
            }
         }

         var1.append(") VALUES (");
         var2 = "";

         for(var4 = 0; var4 < var3; ++var4) {
            var1.append(var2);
            var1.append("?");
            var2 = ", ";
         }

         var1.append(")");
         this.__dont_touch_me_insert_statement = var1.toString();
      }

      return this.__dont_touch_me_insert_statement;
   }

   private String getDeleteStatement(WLRowSetInternal var1, Object[] var2, OptimisticPolicy var3, ColumnFilter var4) throws SQLException {
      StringBuffer var5 = new StringBuffer(500);
      var5.append(this.getDeleteHeader());
      var5.append("WHERE ");
      var5.append(this.getWhereClause(var2, var4));
      return var5.toString();
   }

   protected boolean isLOB(int var1) throws SQLException {
      int var2 = this.metaData.getColumnType(var1 + 1);
      return var2 == 2004 || var2 == 2005 || var2 == 2011;
   }

   protected String getWhereClause(Object[] var1, ColumnFilter var2) throws SQLException {
      if (!this.metaData.haveSetPKColumns()) {
         throw new SQLException("You must use the WLRowSetMetaData.setPrimaryKeyColumn() method to establish primary key columns before updating rows.");
      } else {
         StringBuffer var3 = new StringBuffer(500);
         String var4 = "";

         for(int var5 = 0; var5 < this.columnCount; ++var5) {
            if (this.columnMask.get(var5) && var2.include(var5) && !this.isLOB(var5)) {
               var3.append(var4).append(this.metaData.getWriteColumnName(var5 + 1));
               if (var1[var5] == null) {
                  var3.append(" IS NULL");
               } else {
                  var3.append(" = ?");
               }

               var4 = " AND ";
            }
         }

         return var3.toString();
      }
   }

   private void updateRowParameters(PreparedStatement var1, WLRowSetInternal var2, CachedRow var3, String var4, BitSet var5) throws SQLException {
      Object[] var6 = var3.getColumns();
      Object[] var7 = var3.getOldColumns();
      int var8 = 1;

      for(int var9 = 0; var9 < this.columnMask.length(); ++var9) {
         if (this.columnMask.get(var9) && var5.get(var9)) {
            this.setObject(var1, var8++, var9, var6[var9]);
         }
      }

      ColumnFilter var11 = getPolicy(this.metaData.getOptimisticPolicy()).getColumnFilter(this, var5);

      for(int var10 = 0; var10 < this.columnMask.length(); ++var10) {
         if (this.columnMask.get(var10) && var11.include(var10)) {
            this.setObject(var1, var8++, var10, var7[var10]);
         }
      }

      if (var1.executeUpdate() == 0) {
         this.throwOCE(var4, var3);
      }

   }

   static OptimisticPolicy getPolicy(int var0) {
      switch (var0) {
         case 1:
            return TableWriter.OptimisticPolicy.VERIFY_READ_COLUMNS;
         case 2:
            return TableWriter.OptimisticPolicy.VERIFY_MODIFIED_COLUMNS;
         case 3:
            return TableWriter.OptimisticPolicy.VERIFY_SELECTED_COLUMNS;
         case 4:
            return TableWriter.OptimisticPolicy.VERIFY_NONE;
         case 5:
            return TableWriter.OptimisticPolicy.VERIFY_AUTO_VERSION_COLUMNS;
         case 6:
            return TableWriter.OptimisticPolicy.VERIFY_VERSION_COLUMNS;
         default:
            throw new AssertionError("Unexpected OptimisticPolicy: " + var0);
      }
   }

   protected static final class BatchVerifyParam {
      private final String whereClause;
      private final ColumnFilter filter;
      private final Object[] params;

      BatchVerifyParam(String var1, ColumnFilter var2, Object[] var3) {
         this.whereClause = var1;
         this.filter = var2;
         this.params = var3;
      }

      String getWhereClause() {
         return this.whereClause;
      }

      ColumnFilter getFilter() {
         return this.filter;
      }

      Object[] getCols() {
         return this.params;
      }
   }

   private static final class GroupDelete {
      private final String whereClause;
      private final ColumnFilter filter;
      private final Object[] params;

      GroupDelete(String var1, ColumnFilter var2, Object[] var3) {
         this.whereClause = var1;
         this.filter = var2;
         this.params = var3;
      }

      String getWhereClause() {
         return this.whereClause;
      }

      ColumnFilter getFilter() {
         return this.filter;
      }

      Object[] getCols() {
         return this.params;
      }
   }

   abstract static class OptimisticPolicy {
      static OptimisticPolicy VERIFY_READ_COLUMNS = new OptimisticPolicy() {
         int getCode() {
            return 1;
         }

         String getName() {
            return "VERIFY_READ_COLUMNS";
         }

         ColumnFilter getColumnFilter(TableWriter var1, BitSet var2) {
            return TableWriter.ALL;
         }
      };
      static OptimisticPolicy VERIFY_MODIFIED_COLUMNS = new OptimisticPolicy() {
         int getCode() {
            return 2;
         }

         String getName() {
            return "VERIFY_MODIFIED_COLUMNS";
         }

         ColumnFilter getColumnFilter(TableWriter var1, final BitSet var2) {
            final WLRowSetMetaData var3 = var1.metaData;
            return new ColumnFilter() {
               public boolean include(int var1) throws SQLException {
                  return var2.get(var1) || var3.isPrimaryKeyColumn(var1 + 1);
               }
            };
         }
      };
      static OptimisticPolicy VERIFY_SELECTED_COLUMNS = new OptimisticPolicy() {
         int getCode() {
            return 3;
         }

         String getName() {
            return "VERIFY_SELECTED_COLUMNS";
         }

         ColumnFilter getColumnFilter(TableWriter var1, BitSet var2) throws SQLException {
            final WLRowSetMetaData var3 = var1.metaData;
            return new ColumnFilter() {
               public boolean include(int var1) throws SQLException {
                  return var3.isSelectedColumn(var1 + 1) || var3.isPrimaryKeyColumn(var1 + 1);
               }
            };
         }
      };
      static OptimisticPolicy VERIFY_NONE = new OptimisticPolicy() {
         int getCode() {
            return 4;
         }

         String getName() {
            return "VERIFY_NONE";
         }

         ColumnFilter getColumnFilter(TableWriter var1, BitSet var2) throws SQLException {
            final WLRowSetMetaData var3 = var1.metaData;
            return new ColumnFilter() {
               public boolean include(int var1) throws SQLException {
                  return var3.isPrimaryKeyColumn(var1 + 1);
               }
            };
         }
      };
      static OptimisticPolicy VERIFY_AUTO_VERSION_COLUMNS = new OptimisticPolicy() {
         int getCode() {
            return 5;
         }

         String getName() {
            return "VERIFY_AUTO_VERSION_COLUMNS";
         }

         ColumnFilter getColumnFilter(TableWriter var1, BitSet var2) throws SQLException {
            final WLRowSetMetaData var3 = var1.metaData;
            return new ColumnFilter() {
               public boolean include(int var1) throws SQLException {
                  return var3.isAutoVersionColumn(var1 + 1) || var3.isPrimaryKeyColumn(var1 + 1);
               }
            };
         }
      };
      static OptimisticPolicy VERIFY_VERSION_COLUMNS = new OptimisticPolicy() {
         int getCode() {
            return 6;
         }

         String getName() {
            return "VERIFY_VERSION_COLUMNS";
         }

         ColumnFilter getColumnFilter(TableWriter var1, BitSet var2) throws SQLException {
            final WLRowSetMetaData var3 = var1.metaData;
            return new ColumnFilter() {
               public boolean include(int var1) throws SQLException {
                  return var3.isVersionColumn(var1 + 1) || var3.isPrimaryKeyColumn(var1 + 1);
               }
            };
         }
      };

      abstract int getCode();

      abstract String getName();

      abstract ColumnFilter getColumnFilter(TableWriter var1, BitSet var2) throws SQLException;
   }

   private static final class ModFilter implements ColumnFilter {
      private final BitSet modColMask;

      private ModFilter(BitSet var1) {
         this.modColMask = var1;
      }

      public boolean include(int var1) throws SQLException {
         return this.modColMask.get(var1);
      }

      // $FF: synthetic method
      ModFilter(BitSet var1, Object var2) {
         this(var1);
      }
   }

   private static final class AutoIncFilter implements ColumnFilter {
      private WLRowSetMetaData metaData;

      private AutoIncFilter(WLRowSetMetaData var1) {
         this.metaData = var1;
      }

      public boolean include(int var1) throws SQLException {
         return !this.metaData.isAutoIncrement(var1 + 1);
      }

      // $FF: synthetic method
      AutoIncFilter(WLRowSetMetaData var1, Object var2) {
         this(var1);
      }
   }

   interface ColumnFilter {
      boolean include(int var1) throws SQLException;
   }
}
