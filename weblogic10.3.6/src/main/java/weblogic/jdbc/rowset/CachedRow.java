package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.serial.SerialArray;
import javax.sql.rowset.serial.SerialDatalink;
import javax.sql.rowset.serial.SerialRef;
import javax.sql.rowset.spi.SyncProviderException;

public final class CachedRow extends AbstractMap implements Serializable, Map, Cloneable {
   private static final long serialVersionUID = -8966306632273347421L;
   private static final boolean VERBOSE = false;
   private static final boolean DEBUG = true;
   private CachedRowSetMetaData metaData;
   private Object[] oldColumns;
   private Object[] columns;
   private transient Object[] conflicts;
   private BitSet modifiedCols;
   private boolean isUpdatedRow;
   private boolean isInsertRow;
   private boolean isDeletedRow;
   private transient CachedRow baseRow;
   private transient Map typeMap;
   private int columnCount;
   private int status;

   public CachedRow(RowSetMetaData var1) throws SQLException {
      this.status = 3;
      this.metaData = (CachedRowSetMetaData)var1;
      this.columnCount = var1.getColumnCount();
      this.columns = new Object[this.columnCount];
      this.modifiedCols = new BitSet(this.columnCount);
      this.isUpdatedRow = false;
      this.isInsertRow = false;
      this.isDeletedRow = false;
   }

   protected Object clone(CachedRowSetMetaData var1) {
      CachedRow var2 = null;

      try {
         var2 = (CachedRow)super.clone();
      } catch (Throwable var4) {
         return null;
      }

      var2.metaData = var1;
      var2.columns = new Object[this.columns.length];
      System.arraycopy(this.columns, 0, var2.columns, 0, this.columns.length);
      var2.modifiedCols = (BitSet)((BitSet)this.modifiedCols.clone());
      if (this.oldColumns != null) {
         var2.oldColumns = new Object[this.oldColumns.length];
         System.arraycopy(this.oldColumns, 0, var2.oldColumns, 0, this.oldColumns.length);
      }

      return var2;
   }

   protected CachedRow createShared(CachedRowSetMetaData var1) throws SyncProviderException {
      CachedRow var2;
      if (this.baseRow != null) {
         var2 = (CachedRow)this.baseRow.clone(var1);
         var2.baseRow = this.baseRow;
      } else {
         var2 = (CachedRow)this.clone(var1);
         var2.baseRow = this;
      }

      if (var2.oldColumns != null) {
         var2.columns = var2.oldColumns;
      }

      var2.acceptChanges();
      return var2;
   }

   CachedRow getBaseRow() {
      return this.baseRow;
   }

   void copyFrom(CachedRow var1) {
      if (var1 != null) {
         System.arraycopy(var1.columns, 0, this.columns, 0, var1.columns.length);
      }
   }

   void copyFrom(int var1, CachedRow var2) {
      if (var2 != null) {
         int[] var3 = new int[var2.getMetaData().getColumnCount()];

         for(int var4 = 0; var4 < var2.getMetaData().getColumnCount(); ++var4) {
            var3[var4] = var4 + 1;
         }

         this.copyFrom(var1, var2, var3);
      }
   }

   void copyFrom(int var1, CachedRow var2, int[] var3) {
      if (var2 != null) {
         int var4 = 0;

         for(int var5 = var1; var5 < var1 + var3.length; ++var5) {
            this.columns[var5] = var2.columns[var3[var4++] - 1];
         }

      }
   }

   public CachedRow(ResultSet var1, RowSetMetaData var2, Map var3) throws SQLException {
      this(var2);
      this.typeMap = var3;

      for(int var4 = 0; var4 < this.columnCount; ++var4) {
         this.columns[var4] = this.retrieveData(var1, var4);
      }

   }

   private Object retrieveData(ResultSet var1, int var2) throws SQLException {
      Object var3 = null;
      if (this.typeMap == null) {
         var3 = var1.getObject(var2 + 1);
      } else {
         var3 = var1.getObject(var2 + 1, this.typeMap);
      }

      if (this.metaData.getColumnType(var2 + 1) == 93) {
         var3 = var1.getTimestamp(var2 + 1);
      }

      if (var3 instanceof NClob) {
         var3 = new RowSetNClob((NClob)var3);
      } else if (var3 instanceof Clob) {
         var3 = new RowSetClob((Clob)var3);
      } else if (var3 instanceof Blob) {
         var3 = new RowSetBlob((Blob)var3);
      } else if (var3 instanceof Array && !(var3 instanceof SerialArray)) {
         var3 = new SerialArray((Array)var3);
      } else if (var3 instanceof Ref) {
         var3 = new SerialRef((Ref)var3);
      } else if (var3 instanceof URL) {
         var3 = new SerialDatalink((URL)var3);
      }

      return var3;
   }

   public CachedRowSetMetaData getMetaData() {
      return this.metaData;
   }

   public void setMetaData(CachedRowSetMetaData var1) {
      this.metaData = var1;
   }

   public Object[] getOldColumns() {
      return this.oldColumns;
   }

   public void clearModified() {
      this.modifiedCols.clear();
   }

   public BitSet getModifiedColumns() {
      return this.modifiedCols;
   }

   public boolean isModified(int var1) throws SQLException {
      if (var1 > 0 && var1 <= this.columnCount) {
         return this.modifiedCols.get(var1 - 1);
      } else {
         throw new SQLException("There is no column: " + var1 + " in this RowSet");
      }
   }

   public void setModified(int var1, boolean var2) throws SQLException {
      if (var1 > 0 && var1 <= this.columnCount) {
         if (var2) {
            this.modifiedCols.set(var1 - 1);
         } else {
            this.modifiedCols.clear(var1 - 1);
         }

      } else {
         throw new SQLException("There is no column: " + var1 + " in this RowSet");
      }
   }

   public void mergeOriginalValues(CachedRow var1, BitSet var2) throws SQLException {
      this.modifiedCols = var2;
      Object[] var3 = var1.getColumns();
      this.oldColumns = new Object[this.columnCount];

      for(int var4 = 0; var4 < this.columnCount; ++var4) {
         if (var2.get(var4)) {
            this.oldColumns[var4] = var3[var4];
         } else {
            this.oldColumns[var4] = this.columns[var4];
         }
      }

   }

   public void mergeNewValues(CachedRow var1, BitSet var2) throws SQLException {
      this.modifiedCols = var2;
      Object[] var3 = var1.getColumns();
      this.oldColumns = new Object[this.columnCount];

      for(int var4 = 0; var4 < this.columnCount; ++var4) {
         if (var2.get(var4)) {
            this.oldColumns[var4] = var3[var4];
         } else {
            this.oldColumns[var4] = this.columns[var4];
         }
      }

      System.arraycopy(var3, 0, this.columns, 0, this.columnCount);
   }

   public void cancelRowUpdates() {
      if (this.isUpdatedRow) {
         this.columns = this.oldColumns;
         this.oldColumns = null;
         this.isUpdatedRow = false;
      }
   }

   public void acceptChanges() throws SyncProviderException {
      this.oldColumns = null;
      this.isUpdatedRow = false;
      this.isInsertRow = false;
      this.isDeletedRow = false;

      for(int var1 = 0; var1 < this.modifiedCols.length(); ++var1) {
         this.modifiedCols.clear(var1);
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append("[CachedRow]: [" + System.identityHashCode(this) + "] columnCount: " + this.columnCount + " isUpdatedRow: " + this.isUpdatedRow + " modifiedCols: " + this.modifiedCols + " isInsertRow: " + this.isInsertRow + " isDeletedRow: " + this.isDeletedRow);
      var1.append("\nColumns:\n");

      for(int var2 = 0; var2 < this.columnCount; ++var2) {
         try {
            var1.append("[" + this.metaData.getColumnName(var2 + 1) + " = " + this.columns[var2]);
            if (this.oldColumns != null && this.isModified(var2 + 1)) {
               var1.append(", old value = " + this.oldColumns[var2]);
            }
         } catch (SQLException var4) {
            throw new AssertionError(var4);
         }

         var1.append("] ");
      }

      var1.append("\n\n");
      return var1.toString();
   }

   public boolean isDeletedRow() {
      return this.isDeletedRow;
   }

   public void setDeletedRow(boolean var1) throws SQLException {
      if (this.metaData.isReadOnly()) {
         throw new SQLException("This RowSet is Read-Only.  You must  setReadOnly(false) before attempting to delete a row.");
      } else {
         this.isDeletedRow = var1;
      }
   }

   public boolean isInsertRow() {
      return this.isInsertRow;
   }

   public void setInsertRow(boolean var1) {
      this.isInsertRow = var1;
   }

   public boolean isUpdatedRow() {
      return this.isUpdatedRow;
   }

   public void setUpdatedRow(boolean var1) {
      this.isUpdatedRow = var1;
   }

   public int getColumnCount() {
      return this.columnCount;
   }

   public Object getColumn(int var1) throws SQLException {
      try {
         return this.columns[var1 - 1];
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new SQLException("There is no column: " + var1 + " in this RowSet");
      }
   }

   public Object getOldColumn(int var1) throws SQLException {
      try {
         return this.oldColumns[var1 - 1];
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new SQLException("There is no column: " + var1 + " in this RowSet");
      }
   }

   private void copyColumns() {
      this.oldColumns = this.columns;
      this.columns = new Object[this.columnCount];
      System.arraycopy(this.oldColumns, 0, this.columns, 0, this.columnCount);
   }

   void setOriginal(int var1, Object var2) {
      this.columns[var1 - 1] = var2;
      if (this.oldColumns != null) {
         this.oldColumns[var1 - 1] = var2;
      }

   }

   public void setColumn(int var1, Object var2) {
      this.columns[var1 - 1] = var2;
   }

   private boolean isVersionColumn(int var1) throws SQLException {
      return this.metaData.getOptimisticPolicy() == 6 && this.metaData.isVersionColumn(var1);
   }

   public Object updateColumn(int var1, Object var2) throws SQLException {
      try {
         if (this.metaData.isReadOnly()) {
            throw new SQLException("This RowSet is Read-Only and cannot be updated.");
         } else if (!this.isInsertRow && this.metaData.isReadOnly(var1)) {
            throw new SQLException("Column: " + this.metaData.getColumnName(var1) + " is marked as read-only and cannot be updated.");
         } else if (this.isInsertRow) {
            this.columns[var1 - 1] = this.typeConvert(var1, var2);
            this.modifiedCols.set(var1 - 1);
            return null;
         } else {
            Object var3;
            if (this.isVersionColumn(var1)) {
               var3 = this.columns[var1 - 1];
               this.columns[var1 - 1] = this.typeConvert(var1, var2);
               if (this.isUpdatedRow) {
                  this.oldColumns[var1 - 1] = this.columns[var1 - 1];
               }

               return var3;
            } else {
               if (!this.isUpdatedRow) {
                  this.copyColumns();
                  this.isUpdatedRow = true;
               }

               var3 = this.columns[var1 - 1];
               this.columns[var1 - 1] = this.typeConvert(var1, var2);
               this.modifiedCols.set(var1 - 1);
               return var3;
            }
         }
      } catch (ArrayIndexOutOfBoundsException var4) {
         throw new SQLException("There is no column: " + var1 + " in this RowSet");
      }
   }

   private Object typeConvert(int var1, Object var2) throws SQLException {
      if (this.metaData != null) {
         switch (this.metaData.getColumnType(var1)) {
            case 2004:
               if (var2 instanceof byte[]) {
                  return new RowSetBlob((byte[])((byte[])var2));
               }

               if (var2 instanceof Blob && !(var2 instanceof RowSetBlob)) {
                  return new RowSetBlob((Blob)var2);
               }
               break;
            case 2005:
               if (var2 instanceof String) {
                  return new RowSetClob((String)var2);
               }

               if (var2 instanceof char[]) {
                  return new RowSetClob((char[])((char[])var2));
               }

               if (var2 instanceof NClob && !(var2 instanceof RowSetNClob)) {
                  return new RowSetNClob((NClob)var2);
               }

               if (var2 instanceof Clob && !(var2 instanceof RowSetClob)) {
                  return new RowSetClob((Clob)var2);
               }
               break;
            case 2011:
               if (var2 instanceof String) {
                  return new RowSetNClob((String)var2);
               }

               if (var2 instanceof char[]) {
                  return new RowSetNClob((char[])((char[])var2));
               }

               if (var2 instanceof NClob && !(var2 instanceof RowSetNClob)) {
                  return new RowSetNClob((NClob)var2);
               }
         }
      }

      return var2;
   }

   Object getConflictValue(int var1) {
      if (var1 >= 1 && var1 <= this.columnCount) {
         if (this.conflicts == null) {
            if (this.status != 1 && this.status != 0) {
               throw new RuntimeException("No conflict has been detected.");
            } else {
               throw new RowNotFoundException("No conflict value available since the corresponding row has already been deleted in the datasource.");
            }
         } else {
            return this.conflicts[var1 - 1];
         }
      } else {
         throw new RuntimeException("Invalid column index.");
      }
   }

   void setConflictValue(int var1, Object var2) {
      if (var1 >= 1 && var1 <= this.columnCount) {
         if (this.conflicts == null) {
            this.conflicts = new Object[this.columns.length];
         }

         this.conflicts[var1 - 1] = var2;
      } else {
         throw new RuntimeException("Invalid column index.");
      }
   }

   boolean setConflictValue(ResultSet var1, int[] var2) throws SQLException {
      boolean var3 = false;
      if (this.conflicts == null) {
         this.conflicts = new Object[this.columns.length];
      }

      boolean var4 = true;

      int var9;
      for(int var5 = 0; var5 < var2.length && (var9 = var2[var5]) != -1; ++var5) {
         Object var6 = this.retrieveData(var1, var5);
         this.conflicts[var9] = var6;

         try {
            if (var6 == null) {
               if (this.columns[var9] != null) {
                  var3 = true;
               }
            } else if (!var6.equals(this.columns[var9])) {
               var3 = true;
            }
         } catch (Exception var8) {
            var3 = true;
         }
      }

      if (!var3) {
         this.conflicts = null;
      }

      return var3;
   }

   int getStatus() {
      return this.status;
   }

   void setStatus(int var1) {
      this.status = var1;
   }

   void setResolvedValue(int var1, Object var2) {
      if (var1 >= 1 && var1 <= this.columnCount) {
         this.columns[var1 - 1] = var2;
         if (this.oldColumns != null) {
            this.oldColumns[var1 - 1] = this.conflicts[var1 - 1];
         }

      } else {
         throw new RuntimeException("Invalid column index.");
      }
   }

   public Object[] getColumns() {
      return this.columns;
   }

   public int size() {
      return this.columnCount;
   }

   public boolean isEmpty() {
      return this.columnCount == 0;
   }

   public boolean containsKey(Object var1) {
      return this.get(var1) != null;
   }

   public boolean containsValue(Object var1) {
      for(int var2 = 0; var2 < this.columnCount; ++var2) {
         if (this.columns[var2] == null) {
            if (var1 == null) {
               return true;
            }
         } else if (this.columns[var2].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public Object get(Object var1) {
      try {
         return this.getColumn(this.metaData.findColumn((String)var1));
      } catch (SQLException var3) {
         return null;
      } catch (ClassCastException var4) {
         throw new SQLRuntimeException("Key class: " + var1.getClass().getName() + " was not java.lang.String", var4);
      }
   }

   public Object put(Object var1, Object var2) {
      try {
         return this.updateColumn(this.metaData.findColumn((String)var1), var2);
      } catch (SQLException var4) {
         throw new SQLRuntimeException(var4);
      } catch (ClassCastException var5) {
         throw new SQLRuntimeException("Key class: " + var1.getClass().getName() + " was not java.lang.String", var5);
      }
   }

   public Object remove(Object var1) {
      return this.put(var1, (Object)null);
   }

   public void clear() {
      for(int var1 = 0; var1 < this.columnCount; ++var1) {
         try {
            this.put(this.metaData.getColumnName(var1 + 1), (Object)null);
         } catch (SQLException var3) {
            throw new AssertionError(var3);
         }
      }

   }

   public Set entrySet() {
      HashSet var1 = new HashSet(this.columnCount);

      for(int var2 = 0; var2 < this.columnCount; ++var2) {
         try {
            var1.add(new Entry(this.metaData.getColumnName(var2 + 1), this.columns[var2]));
         } catch (SQLException var4) {
            throw new AssertionError(var4);
         }
      }

      return var1;
   }

   private static class SQLRuntimeException extends RuntimeException {
      private static final long serialVersionUID = 1444694475457733067L;

      SQLRuntimeException(Throwable var1) {
         super(var1);
      }

      SQLRuntimeException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   private static final class Entry implements Map.Entry {
      private final Object key;
      private Object value;

      Entry(Object var1, Object var2) {
         this.key = var1;
         this.value = var2;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            if (this.key.equals(var2.key)) {
               if (this.value == null) {
                  return var2.value == null;
               } else {
                  return this.value.equals(var2.value);
               }
            } else {
               return false;
            }
         }
      }

      public int hashCode() {
         return this.value == null ? this.key.hashCode() : this.key.hashCode() ^ this.value.hashCode();
      }
   }
}
