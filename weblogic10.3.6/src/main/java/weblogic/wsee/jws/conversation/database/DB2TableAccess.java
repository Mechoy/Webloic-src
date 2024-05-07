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
import weblogic.utils.io.StreamUtils;
import weblogic.wsee.jws.util.Config;

class DB2TableAccess extends GenericTableAccess {
   public String getCreateTableStatement() {
      StringBuffer var1 = new StringBuffer();
      var1.append("CREATE TABLE " + this._tableName + "(");
      var1.append("CG_ID");
      var1.append(" varchar(");
      var1.append(Config.getProperty("weblogic.jws.ConversationMaxKeyLength"));
      var1.append(") not null,");
      var1.append("LAST_ACCESS_TIME DECIMAL (19, 0),");
      String var2 = Config.getProperty("weblogic.jws.cgdata.size");
      var1.append("CG_DATA BLOB(" + var2 + "),");
      var1.append("PRIMARY KEY (CG_ID) )");
      return var1.toString();
   }

   protected String getCreateStatement() {
      if (this._createStmt == null) {
         StringBuffer var1 = new StringBuffer("INSERT INTO ");
         var1.append(this._tableName);
         var1.append(" (CG_ID, LAST_ACCESS_TIME, CG_DATA");
         var1.append(" ) VALUES (?, ?, ?)");
         this._createStmt = var1.toString();
      }

      return this._createStmt;
   }

   protected String getSelectObjectStatement() {
      if (this._selectObjectStmt == null) {
         StringBuffer var1 = new StringBuffer("SELECT CG_DATA FROM ");
         var1.append(this._tableName);
         var1.append(" WHERE CG_ID = ? ");
         var1.append("FOR UPDATE");
         this._selectObjectStmt = var1.toString();
      }

      return this._selectObjectStmt;
   }

   protected String getLoadStatement(boolean var1) {
      if (var1 && this._loadStmt != null) {
         return this._loadStmt;
      } else if (!var1 && this._loadStmtNoUpdateLock != null) {
         return this._loadStmtNoUpdateLock;
      } else {
         StringBuffer var2 = new StringBuffer("SELECT ");
         var2.append("LAST_ACCESS_TIME, CG_DATA ");
         var2.append(" FROM ");
         var2.append(this._tableName);
         var2.append(" WHERE CG_ID = ?");
         if (var1) {
            var2.append(" FOR UPDATE");
            this._loadStmt = var2.toString();
            return this._loadStmt;
         } else {
            this._loadStmtNoUpdateLock = var2.toString();
            return this._loadStmtNoUpdateLock;
         }
      }
   }

   public LoadedObject doLoad(Connection var1, String var2, boolean var3) throws SQLException, IOException {
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      LoadedObject var14;
      try {
         var4 = var1.prepareStatement(this.getLoadStatement(var3));
         var4.setObject(1, var2);
         var4.executeQuery();
         var5 = var4.getResultSet();
         LoadedObject var6 = null;
         if (var5.next()) {
            byte[] var7 = this.readByteArrayFromResultSet(var5, 2);
            long var8 = var5.getLong(1);
            var6 = new LoadedObject(var8, var7);
         }

         var14 = var6;
      } finally {
         DbPersistence.close(var5);
         DbPersistence.close((Statement)var4);
      }

      return var14;
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
         var5 = var1.getBinaryStream(2);
         StreamUtils.writeTo(var5, var4);
         var4.flush();
         var3.flush();
         var11 = var3.toByteArray();
      } finally {
         DbPersistence.close((OutputStream)var3);
      }

      return var11;
   }

   public boolean doStoreByInsert(Connection var1, String var2, byte[] var3, long var4) throws SQLException, IOException {
      ByteArrayInputStream var6 = null;
      PreparedStatement var7 = null;

      boolean var9;
      try {
         var7 = var1.prepareStatement(this.getCreateStatement());
         var7.setObject(1, var2);
         var7.setLong(2, var4);
         var6 = null;
         var6 = new ByteArrayInputStream(var3);
         var7.setBinaryStream(3, var6, var3.length);
         boolean var8 = var7.executeUpdate() == 1;
         var9 = var8;
      } finally {
         DbPersistence.close((Statement)var7);
         DbPersistence.close((InputStream)var6);
      }

      return var9;
   }

   public boolean doStoreByUpdate(Connection var1, String var2, byte[] var3, long var4, boolean var6) throws SQLException, IOException {
      PreparedStatement var7 = null;
      ByteArrayInputStream var8 = null;
      boolean var9 = false;

      boolean var10;
      try {
         if (var6) {
            var7 = var1.prepareStatement(this.getStoreStatement());
            var7.setLong(1, var4);
            var8 = new ByteArrayInputStream(var3);
            var7.setBinaryStream(2, var8, var3.length);
            var7.setObject(3, var2);
            var9 = var7.executeUpdate() > 0;
         } else {
            var10 = false;
            var7 = var1.prepareStatement(this.getStoreLiteStatement());
            var7.setLong(1, var4);
            int var15 = this.getStoreLiteStatementIDColumn();
            var7.setObject(var15, var2);
            var9 = var7.executeUpdate() > 0;
         }

         var10 = var9;
      } finally {
         DbPersistence.close((Statement)var7);
         DbPersistence.close((InputStream)var8);
      }

      return var10;
   }
}
