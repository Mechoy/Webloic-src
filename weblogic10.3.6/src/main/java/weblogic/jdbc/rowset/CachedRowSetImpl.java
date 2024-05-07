package weblogic.jdbc.rowset;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.Predicate;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;
import javax.transaction.Transaction;
import weblogic.jdbc.JDBCLogger;
import weblogic.transaction.TransactionHelper;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;

public class CachedRowSetImpl extends BaseRowSet implements WLCachedRowSet, WLRowSetInternal, SyncResolver, Serializable, Cloneable {
   private static final long serialVersionUID = -7682272538461932607L;
   private static final String DEBUGSTR = "weblogic.jdbc.rowset.CachedRowSet.debug";
   private static final String VERBOSESTR = "weblogic.jdbc.rowset.CachedRowSet.debug";
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.jdbc.rowset.CachedRowSet.debug");
   private static final boolean VERBOSE = Boolean.getBoolean("weblogic.jdbc.rowset.CachedRowSet.debug");
   private static final Pattern FROM_PATTERN = Pattern.compile("(?i)\\bfrom\\s+([a-zA-Z_0-9.]+)");
   CachedRow insertRow;
   ArrayList allrows = new ArrayList();
   transient ArrayList rows = new ArrayList();
   transient Predicate filter;
   transient Comparator sorter;
   transient CachedRowSetImpl baseRowSet;
   transient volatile Object lock = null;
   transient int lockNum = 0;
   transient int currentPage = 0;
   transient Connection txConnection = null;
   transient Connection pendingConnection = null;
   transient ResultSet pendingResultSet = null;
   transient boolean populateFromResultSet = true;
   private transient int sc = 0;
   private boolean isPopulated = false;
   RowSetCacheKey key = null;

   void lock() {
      if (this.lock == Thread.currentThread()) {
         ++this.lockNum;
      } else {
         boolean var1 = false;

         do {
            while(this.lock != null) {
            }

            synchronized(this) {
               if (this.lock == null) {
                  this.lock = Thread.currentThread();
                  var1 = true;
               }
            }
         } while(!var1);

         ++this.lockNum;
      }
   }

   void unlock() {
      if (this.lock == Thread.currentThread()) {
         --this.lockNum;
         if (this.lockNum == 0) {
            this.lock = null;
         }

      } else {
         throw new RuntimeException("============================Internal Error: CachedRowSetImpl sync lock is broken. lock: " + this.lock + " current: " + Thread.currentThread());
      }
   }

   public CachedRowSetImpl() {
      try {
         this.metaData = new CachedRowSetMetaData();
      } catch (Throwable var2) {
      }

      this.reset();
   }

   public CachedRowSetImpl(Hashtable var1) {
      try {
         this.metaData = new CachedRowSetMetaData();
      } catch (Throwable var3) {
      }

      this.reset();
   }

   public void populate(ResultSetMetaData var1) throws SQLException {
      this.checkOp(2);
      this.clearData();
      if (var1 instanceof CachedRowSetMetaData) {
         this.metaData = (CachedRowSetMetaData)((CachedRowSetMetaData)var1).clone();
      } else {
         DatabaseMetaData var2 = null;

         try {
            var2 = this.getConnection().getMetaData();
         } catch (Exception var4) {
            JDBCLogger.logStackTrace(var4);
         }

         this.metaData.initialize(var1, var2);
      }

      this.rowSetChanged();
   }

   void populateInternal(ResultSet var1) throws SQLException {
      this.populate(var1);
      this.populateFromResultSet = false;
      this.pendingResultSet = null;
      this.setIsClosed(false);
   }

   public void populate(ResultSet var1) throws SQLException {
      int var2 = this.currentPage * this.maxRows + 1;
      this.populate(var1, var2);
   }

   public void populate(ResultSet var1, int var2) throws SQLException {
      boolean var3 = true;
      if (var2 < 0) {
         throw new SQLException(var2 + " is not a valid start position.");
      } else {
         this.checkOp(2);
         ResultSetMetaData var4 = var1.getMetaData();
         if (var4 instanceof CachedRowSetMetaData) {
            this.metaData = (CachedRowSetMetaData)((CachedRowSetMetaData)var4).clone();
         } else {
            DatabaseMetaData var5 = null;

            try {
               var5 = var1.getStatement().getConnection().getMetaData();
            } catch (Exception var29) {
               JDBCLogger.logStackTrace(var29);
            }

            if (var5 == null) {
               try {
                  var5 = this.getConnection().getMetaData();
               } catch (Exception var28) {
                  JDBCLogger.logStackTrace(var28);
               }
            }

            this.metaData.initialize(var4, var5);
         }

         int var32 = -1;
         boolean var6 = false;

         try {
            var32 = var1.getRow();
         } catch (Exception var27) {
         }

         try {
            if (var32 == -1 && var1.isBeforeFirst()) {
               var32 = 0;
            }
         } catch (Throwable var26) {
            var32 = 0;
         }

         try {
            if (var1.getType() != 1003) {
               var3 = false;
            }
         } catch (Exception var25) {
         }

         int var7;
         try {
            if (var3) {
               label273: {
                  if (var32 >= 0 && var32 <= var2) {
                     var7 = 0;

                     while(true) {
                        if (var7 >= var2 - var32) {
                           break label273;
                        }

                        var6 = var1.next();
                        ++var7;
                     }
                  }

                  throw new SQLException("absolute not supported on ResultSet.TYPE_FORWARD_ONLY; ResultSet position is " + var32 + "; populate position is " + var2);
               }
            } else {
               var6 = var1.absolute(var2);
            }
         } catch (SQLException var31) {
            if (var32 < 0 || var32 > var2) {
               throw var31;
            }

            for(int var8 = 0; var8 < var2 - var32; ++var8) {
               var6 = var1.next();
            }
         }

         try {
            var7 = 0;
            if (var6) {
               this.clearData();
               this.pendingResultSet = var1;
               this.populateFromResultSet = true;

               do {
                  ++var7;
                  if (this.maxRows != 0 && var7 > this.maxRows) {
                     this.setIsComplete(false);
                     break;
                  }

                  this.allrows.add(new CachedRow(var1, this.metaData, this.getTypeMap()));
                  this.isPopulated = true;
               } while(var1.next());
            } else if (var2 == 1) {
               this.clearData();
            }
         } finally {
            if (var32 <= 0) {
               try {
                  if (!var3) {
                     var1.beforeFirst();
                  }
               } catch (Exception var24) {
               }
            } else {
               try {
                  if (!var3) {
                     var1.absolute(var32);
                  }
               } catch (Exception var23) {
               }
            }

         }

         this.filter();
         this.rowSetChanged();
      }
   }

   public void execute() throws SQLException {
      if (this.command != null && !this.command.equals("")) {
         Connection var1 = this.getConnection();
         if (var1 == null) {
            throw new SQLException("Can not get a connection.");
         } else {
            try {
               this.execute(var1);
            } finally {
               try {
                  var1.close();
               } catch (Exception var8) {
               }

               this.cachedConnection = null;
               this.pendingConnection = null;
            }

         }
      } else {
         throw new SQLException("You must call setCommand with a SQL string before calling execute().");
      }
   }

   public void execute(Connection var1) throws SQLException {
      if (var1 == null) {
         throw new SQLException("Can not calling execute with null connection");
      } else {
         Connection var2 = this.cachedConnection;
         this.cachedConnection = var1;

         try {
            this.reader.readData(this);
            this.pendingConnection = var1;
         } finally {
            this.cachedConnection = var2;
         }

      }
   }

   public String executeAndGuessTableName() throws SQLException {
      this.execute();
      Matcher var1 = FROM_PATTERN.matcher(this.command);
      if (!var1.find()) {
         return null;
      } else {
         String var2 = var1.group(1);
         this.metaData.setTableName(var2);
         return var2;
      }
   }

   public boolean executeAndGuessTableNameAndPrimaryKeys() throws SQLException {
      String var1 = this.executeAndGuessTableName();
      if (var1 == null) {
         return false;
      } else {
         Connection var2 = null;
         ResultSet var3 = null;

         boolean var5;
         try {
            var2 = this.getConnection();
            DatabaseMetaData var4 = var2.getMetaData();
            TableNameParser var28 = new TableNameParser(var1, new DatabaseMetaDataHolder(var4));
            String[] var6 = var28.parse();
            String var7 = "".equals(var6[0]) ? null : var6[0];
            String var8 = "".equals(var6[1]) ? null : var6[1];
            String var9 = var6[2];
            var3 = var4.getPrimaryKeys(var7, var8, var9);
            boolean var10;
            if (!var3.next()) {
               if (var7 != null) {
                  var7 = var7.toUpperCase();
               }

               if (var8 != null) {
                  var8 = var8.toUpperCase();
               }

               if (var9 != null) {
                  var9 = var9.toUpperCase();
               }

               var3 = var4.getPrimaryKeys(var7, var8, var9);
               if (!var3.next()) {
                  if (var7 != null) {
                     var7 = var7.toLowerCase();
                  }

                  if (var8 != null) {
                     var8 = var8.toLowerCase();
                  }

                  if (var9 != null) {
                     var9 = var9.toLowerCase();
                  }

                  var3 = var4.getPrimaryKeys(var7, var8, var9);
                  if (!var3.next()) {
                     var10 = false;
                     return var10;
                  }
               }
            }

            do {
               this.metaData.setPrimaryKeyColumn(var3.getString("COLUMN_NAME"), true);
            } while(var3.next());

            var10 = true;
            return var10;
         } catch (SQLException var26) {
            JDBCLogger.logStackTrace(var26);
            var5 = false;
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Exception var25) {
               }
            }

            if (var2 != null) {
               try {
                  var2.close();
               } catch (Exception var24) {
               }
            }

            this.cachedConnection = null;
         }

         return var5;
      }
   }

   void refresh() throws SyncProviderException {
      CachedRow var2;
      for(Iterator var1 = this.rows.iterator(); var1.hasNext(); var2.acceptChanges()) {
         var2 = (CachedRow)var1.next();
         if (var2.isDeletedRow()) {
            this.allrows.remove(var2);
         }
      }

      this.filter();
   }

   SQLException sync(Connection var1) {
      SQLException var2 = null;
      Connection var3 = this.cachedConnection;
      this.cachedConnection = var1;

      try {
         this.writer.writeData(this);
      } catch (SQLException var9) {
         var2 = var9;
      } finally {
         this.cachedConnection = var3;
      }

      return var2;
   }

   public void acceptChanges() throws SyncProviderException {
      if (!this.isReadOnly()) {
         Connection var1 = null;
         SQLException var2 = null;
         boolean var3 = false;

         try {
            Transaction var4 = TransactionHelper.getTransactionHelper().getTransaction();

            try {
               if (var4 != null && var4.getStatus() == 0) {
                  var3 = true;
               }
            } catch (Exception var30) {
            }

            if (var3) {
               if (this.cachedConnection != null) {
                  try {
                     this.cachedConnection.close();
                  } catch (Exception var29) {
                  }

                  this.cachedConnection = null;
               }

               var1 = this.getConnection();
               if (var1 == null) {
                  throw new SQLException("Can not get a connection");
               }

               this.checkOp(9);
               var2 = this.sync(var1);
               if (var2 != null) {
                  try {
                     var4.setRollbackOnly();
                  } catch (Exception var28) {
                  }
               }
            } else {
               var1 = this.getConnection();
               var1.setAutoCommit(false);
               this.checkOp(10);
               this.lock();

               try {
                  var2 = this.sync(var1);
                  if (var2 == null) {
                     var1.commit();
                     this.refresh();
                  } else {
                     var1.rollback();
                  }
               } finally {
                  this.unlock();
               }
            }
         } catch (Exception var31) {
            throw new SyncProviderException(var31.toString());
         } finally {
            try {
               var1.close();
            } catch (Exception var26) {
            }

            this.cachedConnection = null;
            if (var2 != null) {
               if (var2 instanceof SyncProviderException) {
                  throw (SyncProviderException)var2;
               }

               throw new SyncProviderException(var2.toString());
            }

         }

      }
   }

   public void acceptChanges(Connection var1) throws SyncProviderException {
      if (!this.isReadOnly()) {
         if (var1 == null) {
            throw new SyncProviderException("acceptChanges(java.sql.Connection conn) can not be invoked with null conn");
         } else {
            SQLException var2 = null;
            boolean var3 = false;
            Transaction var4 = TransactionHelper.getTransactionHelper().getTransaction();

            try {
               if (var4 != null && var4.getStatus() == 0) {
                  var3 = true;
               }
            } catch (Exception var24) {
               throw new SyncProviderException("failed");
            }

            try {
               if (var3) {
                  this.checkOp(9);
                  var2 = this.sync(var1);
                  if (var2 != null) {
                     try {
                        var4.setRollbackOnly();
                     } catch (Exception var21) {
                        throw new SyncProviderException("failed");
                     }
                  }
               } else {
                  if (var1 == null) {
                     throw new SyncProviderException("Can not calling acceptChanges with null connection");
                  }

                  if (var1.getAutoCommit()) {
                     var1.setAutoCommit(false);
                     this.checkOp(10);
                     this.lock();

                     try {
                        var2 = this.sync(var1);
                        if (var2 == null) {
                           var1.commit();
                           this.refresh();
                        } else {
                           var1.rollback();
                        }
                     } finally {
                        this.unlock();
                        var1.setAutoCommit(true);
                     }
                  } else {
                     this.checkOp(9);
                     var2 = this.sync(var1);
                     this.txConnection = var1;
                  }
               }
            } catch (Exception var22) {
               throw new SyncProviderException(var22.toString());
            } finally {
               if (var2 != null) {
                  if (var2 instanceof SyncProviderException) {
                     throw (SyncProviderException)var2;
                  }

                  throw new SyncProviderException(var2.toString());
               }

            }

         }
      }
   }

   public void insertRow() throws SQLException {
      this.checkOp(8);
      if (this.insertRow == null) {
         throw new SQLException("There is no data to insert");
      } else if (this.filter != null && !this.filter.evaluate(this)) {
         throw new SQLException("Inserted row violates the criteria of the current filter.");
      } else {
         this.rows.add(this.insertRow);
         this.allrows.add(this.insertRow);
         this.insertRow = null;
         this.rowChanged();
      }
   }

   public void undoInsert() throws SQLException {
      this.checkOp(4);
      CachedRow var1 = this.currentRow();
      if (var1.isInsertRow()) {
         try {
            this.rows.remove(this.rows.indexOf(var1));
         } catch (Throwable var4) {
         }

         try {
            this.allrows.remove(this.rows.indexOf(var1));
         } catch (Throwable var3) {
         }
      }

      this.rowChanged();
   }

   public void deleteRow() throws SQLException {
      this.checkOp(4);
      this.currentRow().setDeletedRow(true);
      this.rowChanged();
   }

   public void undoDelete() throws SQLException {
      this.checkOp(4);
      CachedRow var1 = this.currentRow();
      if (var1.isDeletedRow()) {
         var1.setDeletedRow(false);
      }

   }

   public void updateRow() throws SQLException {
      this.checkOp(8);
      if (this.filter != null && !this.filter.evaluate(this)) {
         throw new SQLException("Updated row violates the criteria of the current filter.");
      } else {
         CachedRow var1 = this.currentRow();
         if (var1.isInsertRow()) {
            if (var1.isUpdatedRow() && this.rows.indexOf(var1) != -1) {
               this.rows.add(var1);
               this.allrows.add(var1);
               this.insertRow = null;
            }
         } else {
            var1.setUpdatedRow(true);
         }

         this.rowChanged();
      }
   }

   public void undoUpdate() throws SQLException {
      this.checkOp(4);
      CachedRow var1 = this.currentRow();
      if (var1.isInsertRow() && var1.isUpdatedRow()) {
         try {
            this.rows.remove(this.rows.indexOf(var1));
         } catch (Throwable var4) {
         }

         try {
            this.allrows.remove(this.rows.indexOf(var1));
         } catch (Throwable var3) {
         }
      } else if (var1.isUpdatedRow()) {
         var1.cancelRowUpdates();
      }

   }

   public void cancelRowUpdates() throws SQLException {
      this.checkOp(8);
      CachedRow var1 = this.currentRow();
      if (var1.isInsertRow()) {
         this.insertRow = null;
      } else {
         var1.cancelRowUpdates();
      }

   }

   public void setOriginalRow() throws SQLException {
      this.checkOp(4);
      CachedRow var1 = this.currentRow();
      if (var1.isInsertRow()) {
         try {
            this.rows.remove(this.rows.indexOf(var1));
         } catch (Throwable var4) {
         }

         try {
            this.allrows.remove(this.rows.indexOf(var1));
         } catch (Throwable var3) {
         }
      } else {
         if (var1.isDeletedRow()) {
            var1.setDeletedRow(false);
         }

         if (var1.isUpdatedRow()) {
            var1.cancelRowUpdates();
         }
      }

      this.rowSetChanged();
   }

   public void restoreOriginal() throws SQLException {
      this.checkOp(4);

      for(int var2 = 0; var2 < this.rows.size(); ++var2) {
         CachedRow var1 = (CachedRow)this.rows.get(var2);
         if (var1.isInsertRow()) {
            try {
               this.rows.remove(this.rows.indexOf(var1));
            } catch (Throwable var5) {
            }

            try {
               this.allrows.remove(this.rows.indexOf(var1));
            } catch (Throwable var4) {
            }
         } else {
            if (var1.isDeletedRow()) {
               var1.setDeletedRow(false);
            }

            if (var1.isUpdatedRow()) {
               var1.cancelRowUpdates();
            }
         }
      }

      this.rowIndex = -1;
      this.isComplete = true;
      this.txConnection = null;
      this.cachedConnection = null;
      this.pendingResultSet = null;
      this.pendingConnection = null;
      this.state = LifeCycle.POPULATING;
      this.rowSetChanged();
   }

   public ResultSet getOriginal() throws SQLException {
      CachedRowSetImpl var1 = (CachedRowSetImpl)this.clone();
      var1.resetState();
      var1.rowIndex = -1;
      var1.state = LifeCycle.MANIPULATING;
      var1.restoreOriginal();
      return var1;
   }

   public ResultSet getOriginalRow() throws SQLException {
      CachedRow var1 = this.currentRow();
      CachedRowSetImpl var2 = (CachedRowSetImpl)this.createCopySchema();
      if (!var1.isInsertRow()) {
         var1 = (CachedRow)var1.clone((CachedRowSetMetaData)var2.getMetaData());
         var1.setDeletedRow(false);
         var1.cancelRowUpdates();
         var2.rows.add(var1);
         var2.allrows.add(var1);
      }

      var2.resetState();
      var2.rowIndex = -1;
      var2.state = LifeCycle.MANIPULATING;
      return var2;
   }

   public void refreshRow() throws SQLException {
      throw new SQLException("refreshRow is not supported.");
   }

   public void moveToInsertRow() {
      this.checkOp(5);
   }

   public void moveToCurrentRow() {
      this.checkOp(6);
   }

   public void moveToUpdateRow() {
      this.checkOp(5);

      try {
         this.currentRow().setUpdatedRow(true);
      } catch (Throwable var2) {
         throw new RuntimeException(var2.toString());
      }
   }

   public void setRowSynced() throws SQLException {
      CachedRow var1 = this.currentRow();
      if (!var1.isDeletedRow() || !var1.isInsertRow()) {
         if (var1.isDeletedRow()) {
            try {
               this.rows.remove(this.rows.indexOf(var1));
            } catch (Throwable var4) {
            }

            try {
               this.allrows.remove(this.rows.indexOf(var1));
            } catch (Throwable var3) {
            }
         } else {
            var1.acceptChanges();
         }
      }

   }

   public void setRowSetSynced() throws SQLException {
      for(int var2 = 0; var2 < this.rows.size(); ++var2) {
         CachedRow var1 = (CachedRow)this.rows.get(var2);
         if (!var1.isDeletedRow() || !var1.isInsertRow()) {
            if (var1.isDeletedRow()) {
               try {
                  this.rows.remove(this.rows.indexOf(var1));
               } catch (Throwable var5) {
               }

               try {
                  this.allrows.remove(this.rows.indexOf(var1));
               } catch (Throwable var4) {
               }
            } else {
               var1.acceptChanges();
            }
         }
      }

   }

   public void setFilter(Predicate var1) {
      this.checkOp(2);
      this.filter = var1;
      this.filter();
   }

   public Predicate getFilter() {
      return this.filter;
   }

   void filter() throws RuntimeException {
      int var1 = this.rowIndex;
      ArrayList var2 = this.rows;
      LifeCycle.State var3 = this.state;
      this.rows = this.allrows;

      try {
         this.allrows = new ArrayList(this.rows.size());
         int var4 = 0;

         while(true) {
            if (var4 >= this.rows.size()) {
               var2 = this.rows;
               this.rows = this.allrows;
               this.allrows = var2;
               break;
            }

            this.rowIndex = var4;
            if (this.filter == null || this.filter.evaluate(this)) {
               this.allrows.add(this.rows.get(var4));
            }

            ++var4;
         }
      } catch (Throwable var5) {
         this.allrows = this.rows;
         this.rows = var2;
         this.rowIndex = var1;
         throw new RuntimeException(var5.getMessage());
      }

      this.rowIndex = -1;
      if (this.sorter != null) {
         this.sort();
      }

      this.state = var3;
   }

   public void setSorter(Comparator var1) {
      this.checkOp(2);
      this.sorter = var1;
      this.sort();
   }

   public Comparator getSorter() {
      return this.sorter;
   }

   void sort() throws RuntimeException {
      if (this.sorter != null) {
         Collections.sort(this.rows, this.sorter);
      } else {
         this.filter();
      }

   }

   public int size() {
      return this.rows.size();
   }

   public Map getCurrentRow() throws SQLException {
      return this.currentRow();
   }

   public Map getRow(int var1) throws SQLException {
      try {
         return (Map)this.rows.get(var1);
      } catch (IndexOutOfBoundsException var3) {
         throw new SQLException("getRow(" + var1 + ") is not a valid row index");
      }
   }

   public Map[] getRows(int var1, int var2) throws SQLException {
      if (var1 >= 0 && var1 < this.rows.size()) {
         if (var1 > var2) {
            throw new SQLException("startIndex cannot be > endIndex");
         } else if (var2 >= 0 && var2 <= this.rows.size()) {
            ArrayList var3 = new ArrayList();

            for(int var4 = var1; var4 < var2; ++var4) {
               var3.add(this.rows.get(var4));
            }

            Map[] var5 = new Map[var3.size()];
            return (Map[])((Map[])var3.toArray(var5));
         } else {
            throw new SQLException("endIndex cannot be < 0 or > CachedRowSet.size()");
         }
      } else {
         throw new SQLException("startIndex must be > 0 or < CachedRowSet.size()");
      }
   }

   public Map[] getRows() throws SQLException {
      Map[] var1 = new Map[this.rows.size()];
      return (Map[])((Map[])this.rows.toArray(var1));
   }

   public List getAllCachedRows() {
      return this.allrows;
   }

   public List getCachedRows() {
      return this.rows;
   }

   public void setCachedRows(ArrayList var1) {
      this.allrows = var1;
      this.filter();
      this.rowIndex = 0;
   }

   public Collection toCollection() throws SQLException {
      ArrayList var1 = new ArrayList(this.rows.size());
      var1.addAll(this.rows);
      return var1;
   }

   public Collection toCollection(int var1) throws SQLException {
      ArrayList var2 = new ArrayList(this.rows.size());

      for(int var3 = 0; var3 < this.rows.size(); ++var3) {
         var2.add(((CachedRow)this.rows.get(var3)).getColumn(var1));
      }

      return var2;
   }

   public Collection toCollection(String var1) throws SQLException {
      int var2 = this.findColumn(var1);
      ArrayList var3 = new ArrayList(this.rows.size());

      for(int var4 = 0; var4 < this.rows.size(); ++var4) {
         var3.add(((CachedRow)this.rows.get(var4)).getColumn(var2));
      }

      return var3;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.isComplete = true;
      var1.defaultReadObject();
      this.params = new ArrayList();
      this.rows = new ArrayList();
      this.provider = new WLSyncProvider();
      this.writer = new CachedRowSetJDBCWriter();
      this.reader = new CachedRowSetJDBCReader();
      this.locked = false;
      Iterator var2 = this.allrows.iterator();

      while(var2.hasNext()) {
         CachedRow var3 = (CachedRow)var2.next();
         if (var3.getMetaData() != null) {
            break;
         }

         var3.setMetaData(this.metaData);
      }

      this.filter();
   }

   public void loadXML(XMLInputStream var1) throws IOException, SQLException {
      if (this.metaData != null && this.metaData.getColumnCount() != 0) {
         this.checkOp(2);
         XMLInstanceReader var2 = new XMLInstanceReader(this);
         var2.loadXML(var1);
         this.allrows.clear();
         this.allrows.addAll(this.rows);
         this.state = LifeCycle.POPULATING;
      } else {
         throw new SQLException("You must either use CachedRowSetMetaData.loadSchema toload an XML schema or set the RowSet's metadata before calling loadXML.");
      }
   }

   public void writeXML(XMLOutputStream var1) throws IOException, SQLException {
      this.writeXML(var1, 32);
   }

   public void writeXML(XMLOutputStream var1, int var2) throws IOException, SQLException {
      XMLInstanceWriter var3 = new XMLInstanceWriter(this);
      var3.writeXML(var1, var2);
   }

   public void readXml(XMLInputStream var1) throws IOException, SQLException {
      this.checkOp(2);
      WebRowSetReader var2 = new WebRowSetReader(this);
      var2.loadXML(var1);
      this.allrows.clear();
      this.allrows.addAll(this.rows);
      this.state = LifeCycle.POPULATING;
   }

   public void readXml(Reader var1) throws SQLException {
      try {
         this.readXml(XMLInputStreamFactory.newInstance().newInputStream(var1));
      } catch (NullPointerException var3) {
         throw var3;
      } catch (Throwable var4) {
         throw new SQLException(var4.toString());
      }
   }

   public void readXml(InputStream var1) throws SQLException, IOException {
      this.readXml(XMLInputStreamFactory.newInstance().newInputStream(var1));
   }

   public void writeXml(Writer var1) throws SQLException {
      WebRowSetWriter var2 = new WebRowSetWriter(this);

      try {
         var2.writeXML(XMLOutputStreamFactory.newInstance().newDebugOutputStream(var1), 32);
      } catch (Throwable var4) {
         throw new SQLException(var4.toString());
      }
   }

   public void writeXml(OutputStream var1) throws SQLException, IOException {
      WebRowSetWriter var2 = new WebRowSetWriter(this);
      var2.writeXML(XMLOutputStreamFactory.newInstance().newDebugOutputStream(var1), 32);
   }

   public void writeXml(ResultSet var1, Writer var2) throws SQLException {
      CachedRowSetImpl var3 = new CachedRowSetImpl();
      var3.populate(var1);
      var3.writeXml(var2);
   }

   public void writeXml(ResultSet var1, OutputStream var2) throws SQLException, IOException {
      CachedRowSetImpl var3 = new CachedRowSetImpl();
      var3.populate(var1);
      var3.writeXml(var2);
   }

   protected Object clone() {
      try {
         this.lock();
         CachedRowSetImpl var1 = null;

         try {
            var1 = (CachedRowSetImpl)super.clone();
         } catch (Throwable var8) {
            Object var3 = null;
            return var3;
         }

         var1.metaData = (CachedRowSetMetaData)((CachedRowSetMetaData)this.metaData.clone());
         var1.allrows = new ArrayList(this.allrows.size());

         for(int var2 = 0; var2 < this.allrows.size(); ++var2) {
            var1.allrows.add(((CachedRow)this.allrows.get(var2)).clone(this.metaData));
         }

         if (this.insertRow != null) {
            var1.insertRow = (CachedRow)((CachedRow)this.insertRow.clone(this.metaData));
         }

         var1.rowSetListeners = (List)((ArrayList)this.rowSetListeners).clone();
         var1.cachedConnection = null;
         var1.params = (ArrayList)((ArrayList)this.params.clone());
         var1.filter();
         CachedRowSetImpl var10 = var1;
         return var10;
      } finally {
         this.unlock();
      }
   }

   void detach(SharedRowSetImpl var1) {
      this.lock();

      try {
         --this.sc;
         this.locked = this.sc > 0;
      } finally {
         this.unlock();
      }

   }

   void attach(SharedRowSetImpl var1) {
      this.lock();

      try {
         ++this.sc;
         this.locked = this.sc > 0;
      } finally {
         this.unlock();
      }

   }

   public RowSet createShared() throws SQLException {
      SharedRowSetImpl var1 = null;
      this.lock();

      try {
         var1 = new SharedRowSetImpl(this);
      } catch (Throwable var7) {
         throw new SQLException(this + " can not be shared because of " + var7);
      } finally {
         this.unlock();
      }

      return var1;
   }

   public CachedRowSet createCopy() throws SQLException {
      CachedRowSetImpl var1 = (CachedRowSetImpl)this.clone();
      var1.rowSetListeners.clear();
      return var1;
   }

   public CachedRowSet createCopySchema() throws SQLException {
      CachedRowSetImpl var1 = new CachedRowSetImpl();
      var1.populate(this.getMetaData());
      return var1;
   }

   public CachedRowSet createCopyNoConstraints() throws SQLException {
      CachedRowSetImpl var1 = new CachedRowSetImpl();
      var1.populate((ResultSet)this);
      return var1;
   }

   public void close() {
      this.reset();
      this.setIsClosed(true);
   }

   public void release() throws SQLException {
      this.reset();
   }

   private void reset() {
      this.clearData();
      this.resetState();
   }

   private void clearData() {
      this.rows.clear();
      this.allrows.clear();
      this.rowIndex = -1;
      this.isComplete = true;
      this.txConnection = null;
      this.pendingResultSet = null;
      this.pendingConnection = null;
   }

   private void resetState() {
      this.command = "";
      this.dataSourceName = null;
      this.dataSource = null;
      this.url = "";
      this.userName = null;
      this.password = null;
      this.isolationLevel = -1;
      this.fetchDirection = 1002;
      this.fetchSize = 0;
      this.typeMap = null;
      this.queryTimeout = 0;
      this.maxRows = 0;
      this.maxFieldSize = 0;
      this.escapeProcessing = true;
      this.concurrency = 1008;
      this.resultSetType = 1004;
      this.preferDataSource = true;
      this.cachedConnection = null;
      this.rowSetListeners.clear();
      this.state = LifeCycle.DESIGNING;
      this.params.clear();
      this.metaData.setReadOnly(false);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[CachedRowSet]: [" + System.identityHashCode(this) + "] rowIndex: " + this.rowIndex);
      if (this.metaData == null) {
         var1.append("\nMETADATA: <NULL>");
      } else {
         var1.append("\nMETADATA: " + this.metaData);
      }

      var1.append("\n\nROWS:\n\n");
      Iterator var2 = this.rows.iterator();

      while(var2.hasNext()) {
         var1.append(var2.next().toString());
      }

      return var1.toString();
   }

   public Object getConflictValue(int var1) {
      try {
         return this.currentRow().getConflictValue(var1);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public Object getConflictValue(String var1) {
      try {
         return this.currentRow().getConflictValue(this.findColumn(var1));
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public int getStatus() {
      try {
         return this.currentRow().getStatus();
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public void setResolvedValue(int var1, Object var2) {
      try {
         this.currentRow().setResolvedValue(var1, var2);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public void setResolvedValue(String var1, Object var2) {
      try {
         this.currentRow().setResolvedValue(this.findColumn(var1), var2);
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public boolean nextConflict() throws SQLException {
      boolean var1 = false;

      while(this.next()) {
         if (this.currentRow().getStatus() != 3) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   public boolean previousConflict() throws SQLException {
      boolean var1 = false;

      while(this.previous()) {
         if (this.currentRow().getStatus() != 3) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   CachedRow currentRow() throws SQLException {
      if (this.state == LifeCycle.INSERTING) {
         if (this.insertRow == null) {
            this.insertRow = new CachedRow(this.metaData);
            this.insertRow.setInsertRow(true);
         }

         return this.insertRow;
      } else {
         try {
            return (CachedRow)this.rows.get(this.rowIndex);
         } catch (IndexOutOfBoundsException var2) {
            throw new SQLException("The cursor " + this.rowIndex + " is not positioned over a valid row");
         }
      }
   }

   void updateCurrent(int var1, Object var2) throws SQLException {
      this.checkOp(7);
      this.currentRow().updateColumn(var1, var2);
   }

   public boolean previousPage() throws SQLException {
      if (this.populateFromResultSet && this.pendingResultSet == null) {
         throw new SQLException("execute() or populate() must be called before previousPage() can be invoked.");
      } else {
         boolean var1 = false;
         --this.currentPage;
         if (this.currentPage < 0) {
            this.currentPage = 0;
         } else {
            try {
               if (this.populateFromResultSet) {
                  this.populate(this.pendingResultSet);
               } else if (this.pendingConnection != null) {
                  this.execute(this.pendingConnection);
               } else {
                  this.execute();
               }

               var1 = true;
            } catch (SQLException var3) {
               ++this.currentPage;
            }
         }

         return var1;
      }
   }

   public boolean nextPage() throws SQLException {
      if (this.populateFromResultSet && this.pendingResultSet == null) {
         throw new SQLException("execute() or populate() must be called before nextPage() can be invoked.");
      } else {
         boolean var1 = false;
         ++this.currentPage;

         try {
            this.isPopulated = false;
            if (this.populateFromResultSet) {
               this.populate(this.pendingResultSet);
            } else if (this.pendingConnection != null) {
               this.execute(this.pendingConnection);
            } else {
               this.execute();
            }

            if (this.isPopulated) {
               var1 = true;
            } else {
               --this.currentPage;
            }
         } catch (SQLException var3) {
            --this.currentPage;
         }

         return var1;
      }
   }

   public int getPageSize() {
      return this.getMaxRows();
   }

   public void setPageSize(int var1) throws SQLException {
      if (var1 < 0) {
         throw new SQLException(var1 + " is not a valid PageSize.");
      } else {
         this.setMaxRows(var1);
      }
   }

   public void rowSetPopulated(RowSetEvent var1, int var2) throws SQLException {
      if (var2 > 0 && var2 > this.getFetchSize() && this.size() % var2 == 0) {
         this.rowSetChanged();
      }

   }

   public void rollback(Savepoint var1) throws SQLException {
      this.checkOp();
      boolean var2 = false;
      Transaction var3 = TransactionHelper.getTransactionHelper().getTransaction();

      try {
         if (var3 != null && var3.getStatus() == 0) {
            var2 = true;
         }
      } catch (Exception var10) {
         throw new SyncProviderException("Rollback failed because the status of the current transaction is unknown.");
      }

      if (var2) {
         throw new SQLException("This operation is not supported since there is an active global transaction on the current thread.");
      } else {
         if (this.txConnection != null) {
            try {
               this.txConnection.rollback(var1);
               this.state = LifeCycle.POPULATING;
            } finally {
               this.txConnection = null;
            }
         }

      }
   }

   public void rollback() throws SQLException {
      this.checkOp();
      boolean var1 = false;
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransaction();

      try {
         if (var2 != null && var2.getStatus() == 0) {
            var1 = true;
         }
      } catch (Exception var11) {
         throw new SyncProviderException("Rollback failed because the status of the current transaction is unknown.");
      }

      if (var1) {
         try {
            var2.rollback();
            this.state = LifeCycle.POPULATING;
         } catch (Exception var10) {
            throw new SQLException(var10.toString());
         }
      } else if (this.txConnection != null) {
         try {
            this.txConnection.rollback();
            this.state = LifeCycle.POPULATING;
         } finally {
            this.txConnection = null;
         }
      }

   }

   public void commit() throws SQLException {
      this.checkOp();
      boolean var1 = false;
      Transaction var2 = TransactionHelper.getTransactionHelper().getTransaction();

      try {
         if (var2 != null && var2.getStatus() == 0) {
            var1 = true;
         }
      } catch (Exception var19) {
         throw new SyncProviderException("Rollback failed because the status of the current transaction is unknown.");
      }

      if (var1) {
         this.lock();

         try {
            var2.commit();
            this.refresh();
            this.state = LifeCycle.POPULATING;
         } catch (Exception var17) {
            throw new SQLException(var17.toString());
         } finally {
            this.unlock();
         }
      } else if (this.txConnection != null) {
         this.lock();

         try {
            this.txConnection.commit();
            this.refresh();
            this.state = LifeCycle.POPULATING;
         } finally {
            this.unlock();
            this.txConnection = null;
         }
      }

   }

   public Object getCacheKey() {
      return this.key == null ? (this.key = new RowSetCacheKey(this)) : this.key;
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

   public <T> T getObject(int var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   public <T> T getObject(String var1, Class<T> var2) throws SQLException {
      throw new SQLFeatureNotSupportedException();
   }

   class RowSetCacheKey {
      CachedRowSetImpl crs = null;

      RowSetCacheKey(CachedRowSetImpl var2) {
         this.crs = var2;
      }

      public boolean equals(Object var1) {
         try {
            if (this == var1) {
               return true;
            } else {
               CachedRowSetImpl var2 = ((RowSetCacheKey)var1).crs;
               if (!this.crs.getCommand().equals(var2.getCommand())) {
                  return false;
               } else {
                  ArrayList var3 = this.crs.getParameters();
                  ArrayList var4 = var2.getParameters();
                  if (var3.size() != var4.size()) {
                     return false;
                  } else {
                     for(int var5 = 0; var5 < var3.size(); ++var5) {
                        if (!((WLParameter)var3.get(var5)).getObject().equals(((WLParameter)var3.get(var5)).getObject())) {
                           return false;
                        }
                     }

                     if (this.crs.isPreferDataSource()) {
                        if (!var2.isPreferDataSource()) {
                           return false;
                        }

                        try {
                           if (!this.crs.getDataSourceName().equals(var2.getDataSourceName())) {
                              return false;
                           }
                        } catch (NullPointerException var6) {
                           if (this.crs.getDataSource() != var2.getDataSource()) {
                              return false;
                           }
                        }
                     } else {
                        if (var2.isPreferDataSource()) {
                           return false;
                        }

                        if (!this.crs.getUrl().equals(var2.getUrl())) {
                           return false;
                        }
                     }

                     return true;
                  }
               }
            }
         } catch (Throwable var7) {
            return false;
         }
      }

      public int hashCode() {
         String var1 = this.crs.getCommand();
         return var1 == null ? 0 : var1.hashCode();
      }
   }
}
