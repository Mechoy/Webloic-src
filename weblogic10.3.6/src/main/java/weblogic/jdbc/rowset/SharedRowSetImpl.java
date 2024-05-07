package weblogic.jdbc.rowset;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.spi.SyncProviderException;
import javax.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.xml.stream.XMLInputStream;

class SharedRowSetImpl extends CachedRowSetImpl {
   SharedRowSetImpl(CachedRowSetImpl var1) throws SQLException {
      this.baseRowSet = var1;
      this.metaData = (CachedRowSetMetaData)((CachedRowSetMetaData)this.baseRowSet.metaData.clone());
      this.command = this.baseRowSet.command;
      this.dataSourceName = this.baseRowSet.dataSourceName;
      this.dataSource = this.baseRowSet.dataSource;
      this.url = this.baseRowSet.url;
      this.userName = this.baseRowSet.userName;
      this.password = this.baseRowSet.password;
      this.isolationLevel = this.baseRowSet.isolationLevel;
      this.fetchDirection = this.baseRowSet.fetchDirection;
      this.fetchSize = this.baseRowSet.fetchSize;
      this.typeMap = this.baseRowSet.typeMap;
      this.queryTimeout = this.baseRowSet.queryTimeout;
      this.maxRows = this.baseRowSet.maxRows;
      this.maxFieldSize = this.baseRowSet.maxFieldSize;
      this.escapeProcessing = this.baseRowSet.escapeProcessing;
      this.concurrency = this.baseRowSet.concurrency;
      this.resultSetType = this.baseRowSet.resultSetType;
      this.preferDataSource = this.baseRowSet.preferDataSource;
      this.insertRow = null;
      this.rowSetListeners = new ArrayList();
      this.cachedConnection = null;
      this.pendingConnection = null;
      this.params = new ArrayList();
      this.state = LifeCycle.POPULATING;
      this.repopulate();
   }

   void repopulate() throws SQLException {
      this.checkOp(2);
      this.allrows.clear();
      this.rows.clear();
      this.baseRowSet.lock();
      int var1 = this.currentPage * this.maxRows;

      try {
         int var2 = 0;

         for(int var3 = var1; var3 < this.baseRowSet.allrows.size() && (this.maxRows == 0 || var2 <= this.maxRows); ++var3) {
            CachedRow var4 = (CachedRow)((CachedRow)this.baseRowSet.allrows.get(var3));
            if (!var4.isInsertRow()) {
               this.allrows.add(var4.createShared(this.metaData));
               ++var2;
            }
         }
      } finally {
         this.baseRowSet.unlock();
      }

      this.filter();
      this.pendingConnection = null;
      this.baseRowSet.attach(this);
   }

   public void populate(ResultSetMetaData var1) throws SQLException {
      throw new SQLException("populate is not supported by SharedRowSet because SharedRowSet can only populate its data from CachedRowSet object.");
   }

   public void populate(ResultSet var1) throws SQLException {
      throw new SQLException("populate is not supported by SharedRowSet because SharedRowSet can only populate its data from CachedRowSet object.");
   }

   public void execute() throws SQLException {
      this.repopulate();
      this.rowSetChanged();
   }

   public void execute(Connection var1) throws SQLException {
      this.execute();
   }

   public String executeAndGuessTableName() throws SQLException {
      this.execute();
      return this.metaData.getQualifiedTableName(1);
   }

   public boolean executeAndGuessTableNameAndPrimaryKeys() throws SQLException {
      this.execute();
      return true;
   }

   void updateMemory() throws SQLException {
      CachedRow var2;
      for(Iterator var1 = this.getCachedRows().iterator(); var1.hasNext(); var2.acceptChanges()) {
         var2 = (CachedRow)var1.next();
         if (var2.isDeletedRow()) {
            this.allrows.remove(var2);
            this.baseRowSet.allrows.remove(var2.getBaseRow());
         } else if (var2.isInsertRow()) {
            CachedRow var3 = (CachedRow)var2.clone((CachedRowSetMetaData)this.baseRowSet.getMetaData());
            this.baseRowSet.allrows.add(var3);
         } else if (var2.isUpdatedRow()) {
            var2.getBaseRow().copyFrom(var2);
         }
      }

      this.filter();
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
               throw new SyncProviderException("failed");
            }

            if (var3) {
               if (this.cachedConnection != null) {
                  try {
                     this.cachedConnection.close();
                  } catch (Exception var29) {
                     throw new SyncProviderException("failed");
                  }

                  this.cachedConnection = null;
               }

               var1 = this.getConnection();
               if (var1 == null) {
                  throw new SyncProviderException("failed");
               }

               this.state = this.state.checkOp(9);
               var2 = this.sync(var1);
               if (var2 != null) {
                  try {
                     var4.setRollbackOnly();
                  } catch (Exception var28) {
                     throw new SyncProviderException("failed");
                  }
               }
            } else {
               var1 = this.getConnection();
               var1.setAutoCommit(false);
               this.state = this.state.checkOp(10);
               this.baseRowSet.lock();

               try {
                  var2 = this.sync(var1);
                  if (var2 == null) {
                     var1.commit();
                     this.updateMemory();
                  } else {
                     var1.rollback();
                  }
               } finally {
                  this.baseRowSet.unlock();
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
                  this.state = this.state.checkOp(9);
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
                     throw new SyncProviderException("failed");
                  }

                  if (var1.getAutoCommit()) {
                     var1.setAutoCommit(false);
                     this.state = this.state.checkOp(10);
                     this.baseRowSet.lock();

                     try {
                        var2 = this.sync(var1);
                        if (var2 == null) {
                           var1.commit();
                           this.updateMemory();
                        } else {
                           var1.rollback();
                        }
                     } finally {
                        this.baseRowSet.unlock();
                        var1.setAutoCommit(true);
                     }
                  } else {
                     this.state = this.state.checkOp(9);
                     var2 = this.sync(var1);
                     this.pendingConnection = var1;
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

   public RowSet createShared() throws SQLException {
      return this.baseRowSet.createShared();
   }

   public CachedRowSet createCopy() throws SQLException {
      return (CachedRowSet)this.createShared();
   }

   public CachedRowSet createCopySchema() throws SQLException {
      return (CachedRowSet)this.createShared();
   }

   public CachedRowSet createCopyNoConstraints() throws SQLException {
      return (CachedRowSet)this.createShared();
   }

   protected Object clone() {
      try {
         return this.createShared();
      } catch (Exception var2) {
         throw new RuntimeException(this + " can not be cloned because of " + var2);
      }
   }

   public void close() {
      super.close();
      this.baseRowSet.detach(this);
   }

   protected void finalize() {
      this.close();
   }

   public void loadXML(XMLInputStream var1) throws IOException, SQLException {
      throw new SQLException("loadXML is not supported by SharedRowSet because SharedRowSet can only populate its data from CachedRowSet object.");
   }

   public void readXml(XMLInputStream var1) throws IOException, SQLException {
      throw new SQLException("readXml is not supported by SharedRowSet because SharedRowSet can only populate its data from CachedRowSet object.");
   }

   public void readXml(Reader var1) throws SQLException {
      throw new SQLException("readXml is not supported by SharedRowSet because SharedRowSet can only populate its data from CachedRowSet object.");
   }

   public void readXml(InputStream var1) throws SQLException, IOException {
      throw new SQLException("readXml is not supported by SharedRowSet because SharedRowSet can only populate its data from CachedRowSet object.");
   }

   void toDesign() {
      throw new RuntimeException("Design operations are not supported by SharedRowSet because SharedRowSet populate its data from CachedRowSet object rather than DataSource.");
   }

   void toConfigQuery() {
      throw new RuntimeException("ConfigQuery operations are not supported by SharedRowSet because SharedRowSet populate its data from CachedRowSet object rather than DataSource.");
   }

   public boolean previousPage() {
      throw new RuntimeException("Method not implemented");
   }
}
