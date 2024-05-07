package weblogic.jdbc.rowset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.ElementFactory;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

public final class CachedRowSetMetaData implements WLRowSetMetaData, Serializable, XMLSchemaConstants, Cloneable {
   private static final long serialVersionUID = -343025277802741983L;
   private static final boolean DEBUG = true;
   private static final boolean VERBOSE = false;
   private int columnCount = 0;
   private boolean haveSetPKColumns = false;
   private int version = 2;
   private DatabaseMetaDataHolder databaseMetaData = null;
   private boolean isValidMetaData = false;
   private String writeTableName;
   private String rowName;
   private String rowSetName;
   private boolean isReadOnly = false;
   private String defaultNamespace = "http://www.openuri.org";
   private ColumnAttributes[] columnAttributes = null;
   private transient Map rowAttributes = new HashMap();
   private int optimisticPolicy = 1;
   private boolean batchInserts;
   private boolean batchDeletes;
   private boolean batchUpdates;
   private boolean groupDeletes;
   private int groupDeleteSize = 50;
   private int batchVerifySize = 50;
   private boolean verboseSQL;
   private String schemaLocation;
   ArrayList matchColumns = new ArrayList();

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CachedRowSetMetaData)) {
         return false;
      } else {
         CachedRowSetMetaData var2 = (CachedRowSetMetaData)var1;
         if (var2.columnCount != this.columnCount) {
            return false;
         } else if (var2.haveSetPKColumns != this.haveSetPKColumns) {
            return false;
         } else if (var2.isValidMetaData != this.isValidMetaData) {
            return false;
         } else if (var2.databaseMetaData == this.databaseMetaData || var2.databaseMetaData != null && var2.databaseMetaData.equals(this.databaseMetaData)) {
            if (var2.writeTableName != this.writeTableName && !var2.writeTableName.equals(this.writeTableName)) {
               return false;
            } else if (var2.rowName != this.rowName && !var2.rowName.equals(this.rowName)) {
               return false;
            } else if (var2.rowSetName != this.rowSetName && !var2.rowSetName.equals(this.rowSetName)) {
               return false;
            } else if (var2.isReadOnly != this.isReadOnly) {
               return false;
            } else if (var2.defaultNamespace != this.defaultNamespace && !var2.defaultNamespace.equals(this.defaultNamespace)) {
               return false;
            } else {
               if (!var2.columnAttributes.equals(this.columnAttributes)) {
                  if (var2.columnAttributes.length != this.columnAttributes.length) {
                     return false;
                  }

                  for(int var3 = 0; var3 < this.columnAttributes.length; ++var3) {
                     if (!this.columnAttributes[var3].equals(var2.columnAttributes[var3])) {
                        return false;
                     }
                  }
               }

               if (!var2.rowAttributes.equals(this.rowAttributes)) {
                  return false;
               } else if (var2.optimisticPolicy != this.optimisticPolicy) {
                  return false;
               } else if (var2.batchInserts != this.batchInserts) {
                  return false;
               } else if (var2.batchDeletes != this.batchDeletes) {
                  return false;
               } else if (var2.batchUpdates != this.batchUpdates) {
                  return false;
               } else if (var2.groupDeletes != this.groupDeletes) {
                  return false;
               } else if (var2.groupDeleteSize != this.groupDeleteSize) {
                  return false;
               } else if (var2.batchVerifySize != this.batchVerifySize) {
                  return false;
               } else if (var2.verboseSQL != this.verboseSQL) {
                  return false;
               } else {
                  return var2.schemaLocation == this.schemaLocation || var2.schemaLocation.equals(this.schemaLocation);
               }
            }
         } else {
            return false;
         }
      }
   }

   public CachedRowSetMetaData() throws SQLException {
   }

   protected Object clone() {
      CachedRowSetMetaData var1 = null;

      try {
         var1 = (CachedRowSetMetaData)super.clone();
      } catch (Throwable var3) {
         return null;
      }

      if (this.columnAttributes == null) {
         var1.columnAttributes = null;
      } else {
         var1.columnAttributes = new ColumnAttributes[this.columnAttributes.length];

         for(int var2 = 0; var2 < this.columnAttributes.length; ++var2) {
            if (this.columnAttributes[var2] != null) {
               var1.columnAttributes[var2] = (ColumnAttributes)((ColumnAttributes)this.columnAttributes[var2].clone());
            }
         }
      }

      if (this.rowAttributes == null) {
         var1.rowAttributes = null;
      } else {
         var1.rowAttributes = (Map)((HashMap)this.rowAttributes).clone();
      }

      return var1;
   }

   public void initialize(ResultSetMetaData var1, DatabaseMetaData var2) throws SQLException {
      this.columnCount = var1.getColumnCount();
      this.columnAttributes = new ColumnAttributes[this.columnCount];

      for(int var3 = 0; var3 < this.columnCount; ++var3) {
         this.columnAttributes[var3] = new ColumnAttributes();
         this.columnAttributes[var3].colName = var1.getColumnName(var3 + 1);

         try {
            this.columnAttributes[var3].tableName = var1.getTableName(var3 + 1);
         } catch (Throwable var11) {
            this.columnAttributes[var3].tableName = "";
         }

         try {
            this.columnAttributes[var3].catalogName = var1.getCatalogName(var3 + 1);
         } catch (Throwable var10) {
            this.columnAttributes[var3].catalogName = "";
         }

         this.columnAttributes[var3].columnLabel = var1.getColumnLabel(var3 + 1);

         try {
            this.columnAttributes[var3].schemaName = var1.getSchemaName(var3 + 1);
         } catch (Throwable var9) {
            this.columnAttributes[var3].schemaName = "";
         }

         this.columnAttributes[var3].isAutoIncrement = var1.isAutoIncrement(var3 + 1);
         this.columnAttributes[var3].isCaseSensitive = var1.isCaseSensitive(var3 + 1);
         this.columnAttributes[var3].isCurrency = var1.isCurrency(var3 + 1);
         this.columnAttributes[var3].isNullable = var1.isNullable(var3 + 1);

         try {
            this.columnAttributes[var3].isReadOnly = var1.isReadOnly(var3 + 1);
         } catch (Exception var8) {
            this.columnAttributes[var3].isReadOnly = false;
         }

         this.columnAttributes[var3].isSearchable = var1.isSearchable(var3 + 1);
         this.columnAttributes[var3].isSigned = var1.isSigned(var3 + 1);

         try {
            this.columnAttributes[var3].isWritable = var1.isWritable(var3 + 1);
         } catch (Exception var7) {
            this.columnAttributes[var3].isWritable = true;
         }

         this.columnAttributes[var3].columnDisplaySize = var1.getColumnDisplaySize(var3 + 1);

         try {
            this.columnAttributes[var3].isDefinitelyWritable = var1.isDefinitelyWritable(var3 + 1);
         } catch (Exception var6) {
            this.columnAttributes[var3].isDefinitelyWritable = false;
         }

         try {
            this.columnAttributes[var3].columnClassName = var1.getColumnClassName(var3 + 1);
         } catch (SQLException var5) {
            this.columnAttributes[var3].columnClassName = "";
         }

         this.columnAttributes[var3].columnTypeName = var1.getColumnTypeName(var3 + 1);
         this.columnAttributes[var3].columnType = var1.getColumnType(var3 + 1);
         if (this.columnAttributes[var3].columnType == -1 && this.columnAttributes[var3].columnTypeName.equals("xml")) {
            this.columnAttributes[var3].columnType = 2009;
         }

         if (this.columnAttributes[var3].columnType == 93 && "DATE".equals(this.columnAttributes[var3].columnTypeName)) {
            this.columnAttributes[var3].columnType = 91;
         }

         if (this.isNumericType(this.columnAttributes[var3].columnType)) {
            this.columnAttributes[var3].precision = var1.getPrecision(var3 + 1);
            this.columnAttributes[var3].scale = var1.getScale(var3 + 1);
         }
      }

      if (var2 == null) {
         this.databaseMetaData = null;
         this.isValidMetaData = false;
      } else {
         this.databaseMetaData = new DatabaseMetaDataHolder(var2);
         this.isValidMetaData = true;
      }

   }

   public void setVersion(int var1) {
      this.version = var1;
   }

   public int getVersion() {
      return this.version;
   }

   public void setMetaDataHolder(DatabaseMetaDataHolder var1) {
      this.databaseMetaData = var1;
      this.isValidMetaData = var1 != null;
   }

   public DatabaseMetaDataHolder getMetaDataHolder() {
      return this.databaseMetaData;
   }

   public boolean isValidMetaData() {
      return this.isValidMetaData;
   }

   public void addColumns(ResultSetMetaData var1) throws SQLException {
      int[] var2 = new int[var1.getColumnCount()];

      for(int var3 = 0; var3 < var1.getColumnCount(); ++var3) {
         var2[var3] = var3 + 1;
      }

      this.addColumns(var1, var2);
   }

   public void addColumns(ResultSetMetaData var1, int[] var2) throws SQLException {
      int var3 = var2.length + this.columnCount;
      ColumnAttributes[] var4 = new ColumnAttributes[var3];
      System.arraycopy(this.columnAttributes, 0, var4, 0, this.columnAttributes.length);
      this.columnAttributes = var4;
      int var5 = 0;

      for(int var6 = this.columnCount; var6 < var3; ++var6) {
         this.columnAttributes[var6] = new ColumnAttributes();
         this.columnAttributes[var6].colName = var1.getColumnName(var2[var5]);

         try {
            this.columnAttributes[var6].tableName = var1.getTableName(var2[var5]);
         } catch (Throwable var11) {
            this.columnAttributes[var6].tableName = "";
         }

         try {
            this.columnAttributes[var6].catalogName = var1.getCatalogName(var2[var5]);
         } catch (Throwable var10) {
            this.columnAttributes[var6].catalogName = "";
         }

         this.columnAttributes[var6].columnLabel = var1.getColumnLabel(var2[var5]);

         try {
            this.columnAttributes[var6].schemaName = var1.getSchemaName(var2[var5]);
         } catch (Throwable var9) {
            this.columnAttributes[var6].schemaName = "";
         }

         this.columnAttributes[var6].isAutoIncrement = var1.isAutoIncrement(var2[var5]);
         this.columnAttributes[var6].isCaseSensitive = var1.isCaseSensitive(var2[var5]);
         this.columnAttributes[var6].isCurrency = var1.isCurrency(var2[var5]);
         this.columnAttributes[var6].isNullable = var1.isNullable(var2[var5]);
         this.columnAttributes[var6].isReadOnly = var1.isReadOnly(var2[var5]);
         this.columnAttributes[var6].isSearchable = var1.isSearchable(var2[var5]);
         this.columnAttributes[var6].isSigned = var1.isSigned(var2[var5]);
         this.columnAttributes[var6].isWritable = var1.isWritable(var2[var5]);
         this.columnAttributes[var6].columnDisplaySize = var1.getColumnDisplaySize(var2[var5]);
         this.columnAttributes[var6].isDefinitelyWritable = var1.isDefinitelyWritable(var2[var5]);

         try {
            this.columnAttributes[var6].columnClassName = var1.getColumnClassName(var2[var5]);
         } catch (SQLException var8) {
            this.columnAttributes[var6].columnClassName = "";
         }

         this.columnAttributes[var6].columnTypeName = var1.getColumnTypeName(var2[var5]);
         this.columnAttributes[var6].columnType = var1.getColumnType(var2[var5]);
         if (this.columnAttributes[var6].columnType == 93 && "DATE".equals(this.columnAttributes[var6].columnTypeName)) {
            this.columnAttributes[var6].columnType = 91;
         }

         if (this.isNumericType(this.columnAttributes[var6].columnType)) {
            this.columnAttributes[var6].precision = var1.getPrecision(var2[var5]);
            this.columnAttributes[var6].scale = var1.getScale(var2[var5]);
         }

         ++var5;
      }

      this.columnCount = var3;
   }

   private boolean isNumericType(int var1) {
      return var1 == -7 || var1 == 5 || var1 == 4 || var1 == -5 || var1 == 6 || var1 == 7 || var1 == 2 || var1 == 3;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[CachedRowSetMetaData] [" + System.identityHashCode(this) + "] ");
      var1.append("columnCount: " + this.columnCount + "\n");

      for(int var2 = 0; var2 < this.columnCount; ++var2) {
         var1.append(this.columnAttributes[var2].toString());
         var1.append('\n');
      }

      return var1.toString();
   }

   public int getOptimisticPolicy() {
      return this.optimisticPolicy;
   }

   public void setBatchInserts(boolean var1) {
      this.batchInserts = var1;
   }

   public boolean getBatchInserts() {
      return this.batchInserts;
   }

   public void setBatchDeletes(boolean var1) {
      this.batchDeletes = var1;
   }

   public boolean getBatchDeletes() {
      return this.batchDeletes;
   }

   public void setBatchUpdates(boolean var1) {
      this.batchUpdates = var1;
   }

   public boolean getBatchUpdates() {
      return this.batchUpdates;
   }

   public void setGroupDeletes(boolean var1) {
      this.groupDeletes = var1;
   }

   public boolean getGroupDeletes() {
      return this.groupDeletes;
   }

   public void setGroupDeleteSize(int var1) throws SQLException {
      if (var1 <= 0) {
         throw new SQLException("setGroupDeleteSize must be called  with a size > 0");
      } else {
         this.groupDeleteSize = var1;
      }
   }

   public int getGroupDeleteSize() {
      return this.groupDeleteSize;
   }

   public void setBatchVerifySize(int var1) throws SQLException {
      if (var1 <= 0) {
         throw new SQLException("setBatchVerifySize must be called  with a size > 0");
      } else {
         this.batchVerifySize = var1;
      }
   }

   public int getBatchVerifySize() {
      return this.batchVerifySize;
   }

   public void setOptimisticPolicyAsString(String var1) throws SQLException {
      if (!"VERIFY_READ_COLUMNS".equalsIgnoreCase(var1) && !"".equals(var1)) {
         if ("VERIFY_MODIFIED_COLUMNS".equalsIgnoreCase(var1)) {
            this.setOptimisticPolicy(2);
         } else if ("VERIFY_SELECTED_COLUMNS".equalsIgnoreCase(var1)) {
            this.setOptimisticPolicy(3);
         } else if ("VERIFY_NONE".equalsIgnoreCase(var1)) {
            this.setOptimisticPolicy(4);
         } else if ("VERIFY_AUTO_VERSION_COLUMNS".equalsIgnoreCase(var1)) {
            this.setOptimisticPolicy(5);
         } else {
            if (!"VERIFY_VERSION_COLUMNS".equalsIgnoreCase(var1)) {
               throw new SQLException("Unexpected parameter to setOptimisticPolicyAsString: " + var1 + ".  The parameter " + "must be VERIFY_MODIFIED_COLUMNS, VERIFY_READ_COLUMNS," + " VERIFY_SELECTED_COLUMNS, VERIFY_NONE, VERIFY_AUTO_VERSION_COLUMNS," + " VERIFY_VERSION_COLUMNS");
            }

            this.setOptimisticPolicy(6);
         }
      } else {
         this.setOptimisticPolicy(1);
      }

   }

   public void setOptimisticPolicy(int var1) throws SQLException {
      switch (var1) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            this.optimisticPolicy = var1;
            return;
         default:
            throw new SQLException("Unexpected parameter to setOptimisticPolicy: " + var1 + ".  The parameter " + "must be VERIFY_MODIFIED_COLUMNS, VERIFY_READ_COLUMNS," + " VERIFY_SELECTED_COLUMNS, VERIFY_NONE, VERIFY_AUTO_VERSION_COLUMNS," + " VERIFY_VERSION_COLUMNS");
      }
   }

   public String getOptimisticPolicyAsString() {
      switch (this.getOptimisticPolicy()) {
         case 1:
            return "VERIFY_READ_COLUMNS";
         case 2:
            return "VERIFY_MODIFIED_COLUMNS";
         case 3:
            return "VERIFY_SELECTED_COLUMNS";
         case 4:
            return "VERIFY_NONE";
         case 5:
            return "VERIFY_AUTO_VERSION_COLUMNS";
         case 6:
            return "VERIFY_VERSION_COLUMNS";
         default:
            throw new AssertionError("Unexpected getOptimisticPolicy:" + this.getOptimisticPolicy());
      }
   }

   public void setVerboseSQL(boolean var1) {
      this.verboseSQL = var1;
   }

   public boolean getVerboseSQL() {
      return this.verboseSQL;
   }

   boolean claimSchema(String var1) {
      return this.rowSetName == null ? true : this.rowSetName.equals(var1);
   }

   void setXMLAttributes(int var1, List var2) {
      this.columnAttributes[var1].setXMLAttributes(var2);
   }

   void readXMLAttributes(int var1, StartElement var2) throws IOException {
      this.columnAttributes[var1].readXMLAttributes(var2);
   }

   public int findColumn(String var1) throws SQLException {
      if (var1 != null) {
         for(int var2 = 0; var2 < this.columnCount; ++var2) {
            if (var1.equalsIgnoreCase(this.columnAttributes[var2].colName)) {
               return var2 + 1;
            }
         }
      }

      throw new SQLException("There is no column named: " + var1 + " in this RowSet.");
   }

   private void checkColumn(int var1) throws SQLException {
      if (this.columnCount == 0) {
         throw new SQLException("You should populate the RowSet with data or call setColumnCount before calling any other method onthe RowSetMetaData");
      } else if (var1 == 0) {
         throw new SQLException("You have specified a column index of 0.  JDBC indexes begin with 1.");
      } else if (var1 < 1 || var1 > this.columnCount) {
         throw new SQLException("There is no column: " + var1 + " in this RowSet.");
      }
   }

   public int getColumnCount() {
      return this.columnCount;
   }

   void setColumnCountInternal(int var1) throws SQLException {
      if (var1 < 1) {
         throw new SQLException("Invalid column count: " + var1);
      } else {
         this.columnCount = var1;
         this.columnAttributes = new ColumnAttributes[this.columnCount];

         for(int var2 = 0; var2 < this.columnCount; ++var2) {
            this.columnAttributes[var2] = new ColumnAttributes();
         }

      }
   }

   public void setColumnCount(int var1) throws SQLException {
      if (this.columnCount != 0) {
         throw new SQLException("You cannot reset the columnCount of the RowSetMetaData to a new value.");
      } else {
         this.setColumnCountInternal(var1);
      }
   }

   public void setColumnName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].colName = var2;
   }

   public String getColumnName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].colName;
   }

   String getQualifiedColumnName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].getQualifiedTableName() + "." + this.columnAttributes[var1 - 1].colName;
   }

   public void setTableName(String var1) throws SQLException {
      this.checkColumn(1);
      String var2 = "";
      String var3 = "";
      String var4 = "";
      if (var1 != null) {
         if (this.isValidMetaData) {
            TableNameParser var5 = new TableNameParser(var1, this.databaseMetaData);
            String[] var6 = var5.parse();
            var2 = var6[0];
            var3 = var6[1];
            var4 = var6[2];
         } else {
            for(StringTokenizer var7 = new StringTokenizer(var1, "."); var7.hasMoreTokens(); var4 = var7.nextToken()) {
               var2 = var3;
               var3 = var4;
            }
         }
      }

      for(int var8 = 0; var8 < this.columnAttributes.length; ++var8) {
         this.columnAttributes[var8].setTableName(var2, var3, var4);
      }

   }

   public void setTableName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].setTableName(var2);
   }

   public void setTableName(String var1, String var2) throws SQLException {
      this.setTableName(this.findColumn(var1), var2);
   }

   public String getTableName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].tableName;
   }

   public String getQualifiedTableName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].getQualifiedTableName();
   }

   public String getQualifiedTableName(String var1) throws SQLException {
      return this.getQualifiedTableName(this.findColumn(var1));
   }

   public void setMatchColumns(int[] var1) throws SQLException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.setMatchColumn(var1[var2], true);
      }

   }

   public int[] getMatchColumns() throws SQLException {
      int[] var1 = new int[this.matchColumns.size()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = (Integer)this.matchColumns.get(var2);
      }

      return var1;
   }

   public void setMatchColumn(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      int var3 = this.matchColumns.indexOf(new Integer(var1));
      if (var2) {
         if (var3 == -1) {
            this.matchColumns.add(new Integer(var1));
         }
      } else if (var3 != -1) {
         this.matchColumns.remove(var3);
      }

   }

   public void setKeyColumns(int[] var1) throws SQLException {
      int var2;
      for(var2 = 0; var2 < this.columnCount; ++var2) {
         this.columnAttributes[var2].isPrimaryKeyColumn = false;
      }

      for(var2 = 0; var2 < var1.length; ++var2) {
         this.setPrimaryKeyColumn(var1[var2], true);
      }

   }

   public int[] getKeyColumns() throws SQLException {
      int[] var1 = new int[this.columnCount];
      int var2 = 0;

      for(int var3 = 0; var3 < this.columnCount; ++var3) {
         if (this.columnAttributes[var3].isPrimaryKeyColumn) {
            var1[var2++] = var3 + 1;
         }
      }

      int[] var5 = new int[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var5[var4] = var1[var4];
      }

      return var5;
   }

   public void setPrimaryKeyColumn(String var1, boolean var2) throws SQLException {
      this.setPrimaryKeyColumn(this.findColumn(var1), var2);
   }

   public void setPrimaryKeyColumn(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      if (var2) {
         this.haveSetPKColumns = true;
         this.columnAttributes[var1 - 1].isReadOnly = true;
      }

      this.columnAttributes[var1 - 1].isPrimaryKeyColumn = var2;
   }

   public boolean isPrimaryKeyColumn(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isPrimaryKeyColumn;
   }

   public boolean isPrimaryKeyColumn(String var1) throws SQLException {
      return this.isPrimaryKeyColumn(this.findColumn(var1));
   }

   public boolean haveSetPKColumns() {
      if (this.haveSetPKColumns) {
         return true;
      } else {
         for(int var1 = 0; var1 < this.columnCount; ++var1) {
            if (this.columnAttributes[var1].isPrimaryKeyColumn) {
               this.haveSetPKColumns = true;
               return true;
            }
         }

         return false;
      }
   }

   public void setAutoIncrement(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isAutoIncrement = var2;
      this.setReadOnly(var1, var2);
   }

   public boolean isAutoIncrement(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isAutoIncrement;
   }

   public void setCaseSensitive(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isCaseSensitive = var2;
   }

   public boolean isCaseSensitive(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isCaseSensitive;
   }

   public void setSearchable(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isSearchable = var2;
   }

   public boolean isSearchable(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isSearchable;
   }

   public void setCurrency(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isCurrency = var2;
   }

   public boolean isCurrency(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isCurrency;
   }

   public void setNullable(int var1, int var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isNullable = var2;
   }

   public int isNullable(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isNullable;
   }

   public void setSigned(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isSigned = var2;
   }

   public boolean isSigned(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isSigned;
   }

   public String getCatalogName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].catalogName;
   }

   public void setCatalogName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      if (var2 != null) {
         this.columnAttributes[var1 - 1].catalogName = var2;
      } else {
         this.columnAttributes[var1 - 1].catalogName = "";
      }

   }

   public int getColumnDisplaySize(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].columnDisplaySize;
   }

   public void setColumnDisplaySize(int var1, int var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].columnDisplaySize = var2;
   }

   public String getColumnLabel(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].columnLabel;
   }

   public void setColumnLabel(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].columnLabel = var2;
   }

   public String getSchemaName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].schemaName;
   }

   public void setSchemaName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].schemaName = var2;
   }

   public int getColumnType(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].columnType;
   }

   public void setColumnType(int var1, int var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].columnType = var2;
   }

   public String getColumnTypeName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].columnTypeName;
   }

   public void setColumnTypeName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].columnTypeName = var2;
   }

   public int getPrecision(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].precision;
   }

   public void setPrecision(int var1, int var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].precision = var2;
   }

   public int getScale(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].scale;
   }

   public void setScale(int var1, int var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].scale = var2;
   }

   public boolean isReadOnly() {
      return this.isReadOnly;
   }

   public void setReadOnly(boolean var1) {
      this.isReadOnly = var1;

      for(int var2 = 0; var2 < this.columnCount; ++var2) {
         this.columnAttributes[var2].isReadOnly = var1;
      }

   }

   public boolean isReadOnly(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.isReadOnly ? true : this.columnAttributes[var1 - 1].isReadOnly;
   }

   public boolean isReadOnly(String var1) throws SQLException {
      return this.isReadOnly(this.findColumn(var1));
   }

   public void setReadOnly(String var1, boolean var2) throws SQLException {
      this.setReadOnly(this.findColumn(var1), var2);
   }

   public void setReadOnly(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isReadOnly = var2;
   }

   public void setVerifySelectedColumn(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isSelectedColumn = var2;
   }

   boolean hasSelectedColumn() throws SQLException {
      for(int var1 = 0; var1 < this.columnAttributes.length; ++var1) {
         if (this.columnAttributes[var1].isSelectedColumn) {
            return true;
         }
      }

      return false;
   }

   public boolean isSelectedColumn(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isSelectedColumn;
   }

   public void setVerifySelectedColumn(String var1, boolean var2) throws SQLException {
      this.setVerifySelectedColumn(this.findColumn(var1), var2);
   }

   public boolean isSelectedColumn(String var1) throws SQLException {
      return this.isSelectedColumn(this.findColumn(var1));
   }

   public void setAutoVersionColumn(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isAutoVersionColumn = var2;
   }

   public boolean isAutoVersionColumn(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isAutoVersionColumn;
   }

   public void setAutoVersionColumn(String var1, boolean var2) throws SQLException {
      this.setAutoVersionColumn(this.findColumn(var1), var2);
   }

   public boolean isAutoVersionColumn(String var1) throws SQLException {
      return this.isAutoVersionColumn(this.findColumn(var1));
   }

   public void setVersionColumn(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isVersionColumn = var2;
   }

   public boolean isVersionColumn(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isVersionColumn;
   }

   public void setVersionColumn(String var1, boolean var2) throws SQLException {
      this.setVersionColumn(this.findColumn(var1), var2);
   }

   public boolean isVersionColumn(String var1) throws SQLException {
      return this.isVersionColumn(this.findColumn(var1));
   }

   public boolean isWritable(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isWritable;
   }

   public boolean isDefinitelyWritable(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].isDefinitelyWritable;
   }

   public void setDefinitelyWritable(int var1, boolean var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].isDefinitelyWritable = var2;
   }

   public String getColumnClassName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].columnClassName;
   }

   public void setColumnClassName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].columnClassName = var2;
   }

   public String getWriteColumnName(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].writeColumnName != null ? this.columnAttributes[var1 - 1].writeColumnName : this.columnAttributes[var1 - 1].colName;
   }

   public String getWriteColumnName(String var1) throws SQLException {
      return this.getWriteColumnName(this.findColumn(var1));
   }

   public void setWriteColumnName(int var1, String var2) throws SQLException {
      this.checkColumn(var1);
      this.columnAttributes[var1 - 1].writeColumnName = var2;
   }

   public void setWriteColumnName(String var1, String var2) throws SQLException {
      this.setWriteColumnName(this.findColumn(var1), var2);
   }

   public String getWriteTableName() {
      return this.writeTableName;
   }

   private boolean seq(String var1, String var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var1.equals(var2);
      }
   }

   public void setWriteTableName(String var1) throws SQLException {
      this.writeTableName = var1;
      if (var1 != null) {
         if (this.isValidMetaData) {
            TableNameParser var2 = new TableNameParser(this.writeTableName, this.databaseMetaData);
            String[] var3 = var2.parse();

            for(int var4 = 0; var4 < this.columnCount; ++var4) {
               ColumnAttributes var5 = this.columnAttributes[var4];
               if (!var2.identifierEqual(var5.tableName, var3[2]) || !var2.identifierEqual(var5.schemaName, var3[1]) || !var2.identifierEqual(var5.catalogName, var3[0])) {
                  var5.isReadOnly = true;
               }
            }
         } else {
            String var8 = "";
            String var9 = "";
            String var10 = "";

            for(StringTokenizer var11 = new StringTokenizer(var1, "."); var11.hasMoreTokens(); var10 = var11.nextToken()) {
               var8 = var9;
               var9 = var10;
            }

            for(int var6 = 0; var6 < this.columnCount; ++var6) {
               ColumnAttributes var7 = this.columnAttributes[var6];
               if (!this.seq(var7.tableName, var10) || !"".equals(var9) && !this.seq(var7.schemaName, var9) || !"".equals(var8) && !this.seq(var7.catalogName, var8)) {
                  var7.isReadOnly = true;
               }
            }
         }

      }
   }

   public void markUpdateProperties(String var1, String var2, String var3) throws SQLException {
      this.setTableName(var1);
      this.setWriteTableName(var1);
      this.setPrimaryKeyColumn(this.findColumn(var2), true);
      this.setOptimisticPolicy(6);
      this.setVersionColumn(this.findColumn(var3), true);
   }

   public String getDefaultNamespace() {
      return this.defaultNamespace;
   }

   public void setDefaultNamespace(String var1) {
      this.defaultNamespace = var1;
   }

   public String getRowName() {
      if (this.rowName == null) {
         return this.writeTableName != null ? this.writeTableName + "Row" : "TableRow";
      } else {
         return this.rowName;
      }
   }

   public void setRowName(String var1) {
      this.rowName = var1;
   }

   public String getRowSetName() {
      return this.rowSetName == null ? this.getRowName() + "Set" : this.rowSetName;
   }

   public void setRowSetName(String var1) {
      this.rowSetName = var1;
   }

   public void writeXMLSchema(XMLOutputStream var1) throws IOException, SQLException {
      XMLSchemaWriter var2 = new XMLSchemaWriter(this);
      var2.writeSchema(var1);
   }

   public void loadXMLSchema(XMLInputStream var1) throws IOException, SQLException {
      XMLSchemaReader var2 = new XMLSchemaReader(this);
      var2.loadSchema(var1);
   }

   public String getXMLSchemaLocation() {
      return this.schemaLocation == null ? this.getDefaultNamespace() + "/" + this.getRowSetName() + ".xsd" : this.schemaLocation;
   }

   public void setXMLSchemaLocation(String var1) {
      this.schemaLocation = var1;
   }

   private XMLName getName(String var1, String var2) {
      return ElementFactory.createXMLName(var1, "", var2);
   }

   public Properties getRowAttributes(String var1, String var2) {
      return (Properties)this.rowAttributes.get(this.getName(var1, var2));
   }

   public Map getAllRowAttributes() {
      return this.rowAttributes;
   }

   void readRowAttributes(StartElement var1) throws IOException, SQLException {
      this.rowAttributes = XMLUtils.readPropertyMapFromAttributes(var1);
   }

   public Properties setRowAttributes(String var1, String var2, Properties var3) throws SQLException {
      if (!"http://www.w3.org/2001/XMLSchema".equals(var1) && !"http://www.bea.com/2002/10/weblogicdata".equals(var1)) {
         return (Properties)this.rowAttributes.put(this.getName(var1, var2), var3);
      } else {
         throw new SQLException("namespace parameter " + var1 + " cannot be reserved namespaces: " + "http://www.bea.com/2002/10/weblogicdata" + " or " + "http://www.w3.org/2001/XMLSchema");
      }
   }

   public Properties getColAttributes(String var1, String var2, String var3) throws SQLException {
      return this.getColAttributes(var1, var2, this.findColumn(var3));
   }

   public Properties getColAttributes(String var1, String var2, int var3) throws SQLException {
      this.checkColumn(var3);
      Map var4 = this.columnAttributes[var3 - 1].attributes;
      return var4 == null ? null : (Properties)var4.get(this.getName(var1, var2));
   }

   public Map getAllColAttributes(String var1) throws SQLException {
      return this.getAllColAttributes(this.findColumn(var1));
   }

   public Map getAllColAttributes(int var1) throws SQLException {
      this.checkColumn(var1);
      return this.columnAttributes[var1 - 1].attributes == null ? Collections.EMPTY_MAP : this.columnAttributes[var1 - 1].attributes;
   }

   public Properties setColAttributes(String var1, String var2, int var3, Properties var4) throws SQLException {
      if (!"http://www.w3.org/2001/XMLSchema".equals(var1) && !"http://www.bea.com/2002/10/weblogicdata".equals(var1)) {
         this.checkColumn(var3);
         Object var5 = this.columnAttributes[var3 - 1].attributes;
         if (var5 == null) {
            var5 = new HashMap();
            this.columnAttributes[var3 - 1].attributes = (Map)var5;
         }

         return (Properties)((Map)var5).put(this.getName(var1, var2), var4);
      } else {
         throw new SQLException("namespace parameter " + var1 + " cannot be reserved namespaces: " + "http://www.bea.com/2002/10/weblogicdata" + " or " + "http://www.w3.org/2001/XMLSchema");
      }
   }

   public Properties setColAttributes(String var1, String var2, String var3, Properties var4) throws SQLException {
      return this.setColAttributes(var1, var2, this.findColumn(var3), var4);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.defaultNamespace = "http://www.openuri.org";
      this.groupDeletes = true;
      this.groupDeleteSize = 50;
      this.batchVerifySize = 50;
      this.rowAttributes = new HashMap();
      var1.defaultReadObject();
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

   final class ColumnAttributes implements Serializable, Cloneable {
      private static final long serialVersionUID = -3162667379829885159L;
      String colName;
      String writeColumnName;
      String tableName;
      String catalogName;
      String columnClassName;
      String columnLabel;
      String schemaName;
      String columnTypeName;
      transient Map attributes;
      boolean isAutoIncrement = false;
      boolean isCaseSensitive = false;
      boolean isCurrency = false;
      boolean isDefinitelyWritable = false;
      boolean isPrimaryKeyColumn = false;
      boolean isReadOnly = false;
      boolean isWritable = true;
      boolean isSearchable = true;
      boolean isSigned = false;
      boolean isAutoVersionColumn = false;
      boolean isVersionColumn = false;
      boolean isSelectedColumn = false;
      int isNullable;
      int columnDisplaySize;
      int columnType;
      int precision;
      int scale;

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            ColumnAttributes var2 = (ColumnAttributes)var1;
            if (var2.colName != this.colName) {
               return false;
            } else if (var2.writeColumnName != this.writeColumnName) {
               return false;
            } else if (var2.tableName != this.tableName) {
               return false;
            } else if (var2.catalogName != this.catalogName) {
               return false;
            } else if (var2.columnClassName != this.columnClassName) {
               return false;
            } else if (var2.columnLabel != this.columnLabel) {
               return false;
            } else if (var2.schemaName != this.schemaName) {
               return false;
            } else if (var2.columnTypeName != this.columnTypeName) {
               return false;
            } else if (var2.attributes != this.attributes) {
               return false;
            } else if (var2.isAutoIncrement != this.isAutoIncrement) {
               return false;
            } else if (var2.isCaseSensitive != this.isCaseSensitive) {
               return false;
            } else if (var2.isCurrency != this.isCurrency) {
               return false;
            } else if (var2.isDefinitelyWritable != this.isDefinitelyWritable) {
               return false;
            } else if (var2.isPrimaryKeyColumn != this.isPrimaryKeyColumn) {
               return false;
            } else if (var2.isReadOnly != this.isReadOnly) {
               return false;
            } else if (var2.isWritable != this.isWritable) {
               return false;
            } else if (var2.isSearchable != this.isSearchable) {
               return false;
            } else if (var2.isSigned != this.isSigned) {
               return false;
            } else if (var2.isAutoVersionColumn != this.isAutoVersionColumn) {
               return false;
            } else if (var2.isVersionColumn != this.isVersionColumn) {
               return false;
            } else if (var2.isSelectedColumn != this.isSelectedColumn) {
               return false;
            } else if (var2.isNullable != this.isNullable) {
               return false;
            } else if (var2.columnDisplaySize != this.columnDisplaySize) {
               return false;
            } else if (var2.columnType != this.columnType) {
               return false;
            } else if (var2.precision != this.precision) {
               return false;
            } else {
               return var2.scale == this.scale;
            }
         }
      }

      private Attribute getAttr(XMLName var1, String var2) {
         return ElementFactory.createAttribute(var1, var2);
      }

      protected Object clone() {
         ColumnAttributes var1 = null;

         try {
            var1 = (ColumnAttributes)super.clone();
         } catch (Throwable var3) {
            return null;
         }

         if (this.attributes != null) {
            var1.attributes = (Map)((HashMap)this.attributes).clone();
         }

         return var1;
      }

      public void setXMLAttributes(List var1) {
         if (this.isAutoIncrement) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_AUTO, "true"));
         }

         if (this.isPrimaryKeyColumn) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_PK, "true"));
         }

         if (this.isReadOnly) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_READONLY, "true"));
         }

         if (this.isAutoVersionColumn) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_AUTO_VERSION, "true"));
         }

         if (this.isDefinitelyWritable) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_DEFINITELY_WRITABLE, "true"));
         }

         if (this.isVersionColumn) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_VERSION, "true"));
         }

         if (this.isSelectedColumn) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_SELECTED, "true"));
         }

         if (this.writeColumnName != null) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_WRITECOL, this.writeColumnName));
         }

         if (this.tableName != null && !"".equals(this.tableName)) {
            var1.add(this.getAttr(XMLSchemaConstants.WLDD_TABLE_NAME, this.getQualifiedTableName()));
         }

         if (this.isNullable != 0) {
            var1.add(XMLSchemaConstants.NIL_ATTR);
         }

         if (this.attributes != null) {
            XMLUtils.addAttributesFromPropertyMap(var1, this.attributes);
         }

      }

      public void readXMLAttributes(StartElement var1) throws IOException {
         this.colName = XMLUtils.getRequiredAttribute(var1, "name").getValue();
         this.isAutoIncrement = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_AUTO);
         this.isDefinitelyWritable = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_DEFINITELY_WRITABLE);
         this.isPrimaryKeyColumn = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_PK);
         this.isReadOnly = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_READONLY);
         this.isAutoVersionColumn = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_AUTO_VERSION);
         this.isVersionColumn = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_VERSION);
         this.isSelectedColumn = XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.WLDD_SELECTED);
         this.writeColumnName = XMLUtils.getOptionalStringAttribute(var1, XMLSchemaConstants.WLDD_WRITECOL);
         String var2 = XMLUtils.getOptionalStringAttribute(var1, XMLSchemaConstants.WLDD_TABLE_NAME);
         this.setTableName(var2);
         String var3 = XMLUtils.getRequiredAttribute(var1, "type").getValue();
         String var4 = XMLUtils.getOptionalStringAttribute(var1, XMLSchemaConstants.WLDD_JDBC_TYPE);
         if (var4 == null) {
            this.columnType = TypeMapper.getDbType(var3);
            this.columnTypeName = TypeMapper.getJDBCTypeAsString(this.columnType);
         } else {
            this.columnTypeName = var4;
            this.columnType = TypeMapper.getJDBCTypeFromString(this.columnTypeName);
         }

         if (XMLUtils.getOptionalBooleanAttribute(var1, XMLSchemaConstants.NILLABLE_NAME)) {
            this.isNullable = 1;
         } else {
            this.isNullable = 0;
         }

         this.attributes = XMLUtils.readPropertyMapFromAttributes(var1);
      }

      String getQualifiedTableName() {
         StringBuffer var1 = new StringBuffer();
         if (CachedRowSetMetaData.this.isValidMetaData) {
            var1.append(this.tableName);
            if (CachedRowSetMetaData.this.databaseMetaData.supportsSchemasInDataManipulation() && this.schemaName != null && !"".equals(this.schemaName)) {
               var1.insert(0, ".").insert(0, this.schemaName);
            }

            if (CachedRowSetMetaData.this.databaseMetaData.supportsCatalogsInDataManipulation() && this.catalogName != null && !"".equals(this.catalogName)) {
               String var2 = CachedRowSetMetaData.this.databaseMetaData.getCatalogSeparator();
               if (var2 != null && !"".equals(var2)) {
                  if (CachedRowSetMetaData.this.databaseMetaData.isCatalogAtStart()) {
                     var1.insert(0, var2).insert(0, this.catalogName);
                  } else {
                     var1.append(var2).append(this.catalogName);
                  }
               }
            }
         } else {
            if (this.catalogName != null && !"".equals(this.catalogName)) {
               var1.append(this.catalogName).append(".");
            }

            if (this.schemaName != null && !"".equals(this.schemaName)) {
               var1.append(this.schemaName).append(".");
            }

            var1.append(this.tableName);
         }

         return var1.toString();
      }

      void setTableName(String var1) {
         String var2 = "";
         String var3 = "";
         String var4 = "";
         if (var1 != null) {
            if (CachedRowSetMetaData.this.isValidMetaData) {
               try {
                  TableNameParser var5 = new TableNameParser(var1, CachedRowSetMetaData.this.databaseMetaData);
                  String[] var6 = var5.parse();
                  var2 = var6[0];
                  var3 = var6[1];
                  var4 = var6[2];
               } catch (ParseException var7) {
               }
            } else {
               for(StringTokenizer var8 = new StringTokenizer(var1, "."); var8.hasMoreTokens(); var4 = var8.nextToken()) {
                  var2 = var3;
                  var3 = var4;
               }
            }
         }

         this.setTableName(var2, var3, var4);
      }

      void setTableName(String var1, String var2, String var3) {
         if (var1 != null) {
            this.catalogName = var1;
         } else {
            this.catalogName = "";
         }

         if (var2 != null) {
            this.schemaName = var2;
         } else {
            this.schemaName = "";
         }

         this.tableName = var3;
      }

      public String toString() {
         return "colName=" + this.colName + ", tableName=" + this.tableName + ", catalogName=" + this.catalogName + ", columnClassName=" + this.columnClassName + ", columnLabel=" + this.columnLabel + ", schemaName=" + this.schemaName + ", columnTypeName=" + this.columnTypeName + ", isAutoIncrement=" + this.isAutoIncrement + ", isCaseSensitive=" + this.isCaseSensitive + ", isCurrency=" + this.isCurrency + ", isDefinitelyWritable=" + this.isDefinitelyWritable + ", isPrimaryKeyColumn=" + this.isPrimaryKeyColumn + ", isReadOnly=" + this.isReadOnly + ", isWritable=" + this.isWritable + ", isSearchable=" + this.isSearchable + ", isSigned=" + this.isSigned + ", isNullable=" + this.isNullable + ", columnDisplaySize=" + this.columnDisplaySize + ", columnType=" + this.columnType + ", precision=" + this.precision + ", scale=" + this.scale;
      }
   }
}
