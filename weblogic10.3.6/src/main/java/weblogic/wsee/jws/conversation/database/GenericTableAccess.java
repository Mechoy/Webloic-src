package weblogic.wsee.jws.conversation.database;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import weblogic.utils.io.StreamUtils;
import weblogic.wsee.jws.util.Config;

abstract class GenericTableAccess implements TableAccess {
   protected boolean _createOnStore = false;
   protected boolean _insertOnCreate = Config.getInsertOnCreate();
   static final String CONV_ID_COLUMN_NAME = "CG_ID";
   protected String _tableName;
   protected String _createStmt;
   protected String _createByCallableStmt;
   protected String _findStmt;
   protected String _loadStmt;
   protected String _loadStmtNoUpdateLock;
   protected String _storeStmt;
   protected String _storeByCallableStmt;
   protected String _storeLiteStmt;
   protected String _removeStmt;
   protected String _selectStmt;
   protected String _resetObjectStmt;
   protected String _selectObjectStmt;
   protected int _storeStmtIDColumn = -1;
   protected int _storeLiteStmtIDColumn = -1;

   public String getTableName() {
      return this._tableName;
   }

   public void setTableName(String var1) {
      this._tableName = var1;
   }

   protected abstract String getCreateTableStatement();

   int getStoreStatementIDColumn() {
      if (this._storeStmtIDColumn == -1) {
         this._storeStmtIDColumn = countQuestionMarks(this.getStoreStatement());
      }

      return this._storeStmtIDColumn;
   }

   int getStoreLiteStatementIDColumn() {
      if (this._storeLiteStmtIDColumn == -1) {
         this._storeLiteStmtIDColumn = countQuestionMarks(this.getStoreLiteStatement());
      }

      return this._storeLiteStmtIDColumn;
   }

   private static int countQuestionMarks(String var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if (var0.charAt(var2) == '?') {
            ++var1;
         }
      }

      return var1;
   }

   public HashMap ensureTableCreated(Connection var1) throws SQLException {
      String var2 = this.getTableName();
      String var3 = this.getCreateTableStatement();
      HashMap var4 = new HashMap();
      PreparedStatement var5 = null;

      try {
         var5 = var1.prepareStatement("SELECT CG_ID FROM " + var2);
         var5.executeQuery();
         return var4;
      } catch (SQLException var23) {
         if (this instanceof DB2TableAccess && var23.getErrorCode() != -204) {
            HashMap var25 = var4;
            return var25;
         } else {
            Statement var7 = null;

            try {
               var7 = var1.createStatement();
               var7.executeUpdate(var3);
               var5 = var1.prepareStatement("SELECT CG_ID FROM " + var2);
               var5.executeQuery();
               return var4;
            } catch (SQLException var21) {
               throw var21;
            } finally {
               DbPersistence.close(var7);
            }
         }
      } finally {
         DbPersistence.cleanup((Connection)null, var5);
      }
   }

   protected String getCreateStatement() {
      if (this._createStmt == null) {
         StringBuffer var1 = new StringBuffer("INSERT INTO ");
         var1.append(this._tableName);
         var1.append(" (CG_ID, LAST_ACCESS_TIME, CG_DATA");
         var1.append(" ) VALUES (?, ?, ?");
         var1.append(")");
         this._createStmt = var1.toString();
      }

      return this._createStmt;
   }

   protected String getCreateByCallableStatement() {
      if (this._createByCallableStmt == null) {
         StringBuffer var1 = new StringBuffer("BEGIN ");
         var1.append("INSERT INTO ");
         var1.append(this._tableName);
         var1.append(" (CG_ID, LAST_ACCESS_TIME, CG_DATA");
         var1.append(" ) VALUES (?, ?, EMPTY_BLOB()) RETURNING CG_DATA INTO ?; END;");
         this._createByCallableStmt = var1.toString();
      }

      return this._createByCallableStmt;
   }

   private String getFindStatement() {
      if (this._findStmt == null) {
         StringBuffer var1 = new StringBuffer("SELECT CG_ID");
         var1.append(" FROM ");
         var1.append(this._tableName);
         var1.append(" WHERE CG_ID = ?");
         this._findStmt = var1.toString();
      }

      return this._findStmt;
   }

   protected String getLoadStatement(boolean var1) {
      if (this._loadStmt == null) {
         StringBuffer var2 = new StringBuffer("SELECT LAST_ACCESS_TIME, CG_DATA ");
         var2.append(" FROM ");
         var2.append(this._tableName);
         var2.append(" WHERE CG_ID = ?");
         this._loadStmt = var2.toString();
      }

      return this._loadStmt;
   }

   protected String getStoreStatement() {
      if (this._storeStmt == null) {
         StringBuffer var1 = new StringBuffer("UPDATE ");
         var1.append(this._tableName);
         var1.append(" SET ");
         var1.append(" LAST_ACCESS_TIME = ?, ");
         var1.append(" CG_DATA = ? ");
         var1.append(" WHERE CG_ID = ?");
         this._storeStmt = var1.toString();
      }

      return this._storeStmt;
   }

   protected String getStoreByCallableStatement() {
      if (this._storeByCallableStmt == null) {
         StringBuffer var1 = new StringBuffer("BEGIN ");
         var1.append("UPDATE ");
         var1.append(this._tableName);
         var1.append(" SET LAST_ACCESS_TIME = ?,");
         var1.append(" CG_DATA = EMPTY_BLOB() ");
         var1.append(" WHERE CG_ID = ? ");
         var1.append(" RETURNING CG_DATA INTO ?");
         var1.append("; END;");
         this._storeByCallableStmt = var1.toString();
      }

      return this._storeByCallableStmt;
   }

   protected String getStoreLiteStatement() {
      if (this._storeLiteStmt == null) {
         StringBuffer var1 = new StringBuffer("UPDATE ");
         var1.append(this._tableName);
         var1.append(" SET ");
         var1.append(" LAST_ACCESS_TIME = ? ");
         var1.append(" WHERE CG_ID = ?");
         this._storeLiteStmt = var1.toString();
      }

      return this._storeLiteStmt;
   }

   private String getRemoveStatement() {
      if (this._removeStmt == null) {
         StringBuffer var1 = new StringBuffer("DELETE FROM ");
         var1.append(this._tableName);
         var1.append(" WHERE CG_ID = ?");
         this._removeStmt = var1.toString();
      }

      return this._removeStmt;
   }

   protected String getSelectObjectStatement() {
      return this._selectObjectStmt;
   }

   public LoadedObject doLoad(Connection var1, String var2, boolean var3) throws SQLException, IOException {
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      LoadedObject var7;
      try {
         var4 = var1.prepareStatement(this.getLoadStatement(var3));
         var4.setObject(1, var2);
         var4.executeQuery();
         var5 = var4.getResultSet();
         LoadedObject var6;
         if (!var5.next()) {
            var6 = null;
            return var6;
         }

         var6 = new LoadedObject(var5.getLong(1), this.readByteArrayFromResultSet(var5, 2));
         var7 = var6;
      } finally {
         DbPersistence.close(var5);
         DbPersistence.close((Statement)var4);
      }

      return var7;
   }

   protected byte[] readByteArrayFromResultSet(ResultSet var1, int var2) throws IOException, SQLException {
      ByteArrayOutputStream var3 = null;
      BufferedOutputStream var4 = null;
      InputStream var5 = null;
      Object var6 = null;

      byte[] var11;
      try {
         var3 = new ByteArrayOutputStream();
         var4 = new BufferedOutputStream(var3);
         var5 = var1.getBinaryStream(var2);
         StreamUtils.writeTo(var5, var4);
         var4.flush();
         var3.flush();
         var11 = var3.toByteArray();
      } finally {
         DbPersistence.close((OutputStream)var3);
         DbPersistence.close((OutputStream)var4);
         DbPersistence.close(var5);
      }

      return var11;
   }

   public boolean doStoreByInsert(Connection var1, String var2, byte[] var3, long var4) throws SQLException, IOException {
      PreparedStatement var6 = null;

      boolean var7;
      try {
         var6 = var1.prepareStatement(this.getCreateStatement());
         var6.setObject(1, var2);
         var6.setLong(2, var4);
         this.writeByteArrayToStatement(var6, 3, var3);
         var7 = var6.executeUpdate() == 1;
      } finally {
         DbPersistence.close((Statement)var6);
      }

      return var7;
   }

   public boolean doStoreByUpdate(Connection var1, String var2, byte[] var3, long var4, boolean var6) throws SQLException, IOException {
      PreparedStatement var7 = null;
      String var8 = null;
      if (var6) {
         var8 = this.getStoreStatement();
      } else {
         var8 = this.getStoreLiteStatement();
      }

      boolean var10;
      try {
         var7 = var1.prepareStatement(var8);
         var7.setLong(1, var4);
         boolean var9 = false;
         int var15;
         if (var6) {
            this.writeByteArrayToStatement(var7, 2, var3);
            var15 = this.getStoreStatementIDColumn();
         } else {
            var15 = this.getStoreLiteStatementIDColumn();
         }

         var7.setObject(var15, var2);
         var10 = var7.executeUpdate() > 0;
      } finally {
         DbPersistence.close((Statement)var7);
      }

      return var10;
   }

   public boolean doRemove(Connection var1, String var2) throws SQLException {
      PreparedStatement var3 = null;

      boolean var4;
      try {
         var3 = var1.prepareStatement(this.getRemoveStatement());
         var3.setObject(1, var2);
         var4 = var3.executeUpdate() > 0;
      } finally {
         DbPersistence.close((Statement)var3);
      }

      return var4;
   }

   private void writeByteArrayToStatement(PreparedStatement var1, int var2, byte[] var3) throws SQLException {
      ByteArrayInputStream var4 = null;

      try {
         var4 = new ByteArrayInputStream(var3);
         var1.setBinaryStream(var2, var4, var3.length);
      } finally {
         DbPersistence.close((InputStream)var4);
      }

   }

   public boolean keyExists(Connection var1, String var2) throws SQLException {
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      boolean var5;
      try {
         var3 = var1.prepareStatement(this.getFindStatement());
         var3.setObject(1, var2);
         var3.executeQuery();
         var4 = var3.getResultSet();
         var5 = var4.next();
      } finally {
         DbPersistence.close(var4);
         DbPersistence.close((Statement)var3);
      }

      return var5;
   }
}
