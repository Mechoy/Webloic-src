package weblogic.wsee.jws.conversation.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import weblogic.jdbc.vendor.oracle.OracleThinBlob;
import weblogic.wsee.jws.util.Config;

class OracleTableAccess extends GenericTableAccess {
   public String getCreateTableStatement() {
      StringBuffer var1 = new StringBuffer();
      var1.append("CREATE TABLE " + this._tableName + "(");
      var1.append("CG_ID");
      var1.append(" varchar(");
      var1.append(Config.getProperty("weblogic.jws.ConversationMaxKeyLength"));
      var1.append(") not null,");
      var1.append("LAST_ACCESS_TIME number(19,0), ");
      var1.append("CG_DATA BLOB");
      var1.append(", PRIMARY KEY (CG_ID) )");
      var1.append(" INITRANS 6");
      return var1.toString();
   }

   protected String getCreateStatement() {
      if (this._createStmt == null) {
         StringBuffer var1 = new StringBuffer("INSERT INTO ");
         var1.append(this._tableName);
         var1.append(" (CG_ID, LAST_ACCESS_TIME, CG_DATA");
         var1.append(" ) VALUES (?, ?, EMPTY_BLOB()");
         var1.append(")");
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
         var2.append("/*+ FIRST_ROWS(1) */");
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

      LoadedObject var6;
      try {
         var4 = var1.prepareStatement(this.getLoadStatement(var3));
         var4.setObject(1, var2);
         var4.executeQuery();
         var5 = var4.getResultSet();
         if (var5.next()) {
            var6 = new LoadedObject(var5.getLong(1), this.readByteArrayFromResultSet(var5, 2));
            return var6;
         }

         var6 = null;
      } finally {
         DbPersistence.close(var5);
         DbPersistence.close((Statement)var4);
      }

      return var6;
   }

   protected byte[] readByteArrayFromResultSet(ResultSet var1, int var2) throws IOException, SQLException {
      Blob var3 = null;
      boolean var4 = true;
      ByteArrayOutputStream var5 = null;
      Object var6 = null;

      byte[] var12;
      try {
         var3 = var1.getBlob(var2);
         int var11 = (int)var3.length();
         var5 = new ByteArrayOutputStream(var11);
         var5.write(var3.getBytes(1L, var11), 0, var11);
         var12 = var5.toByteArray();
      } finally {
         DbPersistence.close((OutputStream)var5);
      }

      return var12;
   }

   public boolean doStoreByInsert(Connection var1, String var2, byte[] var3, long var4) throws SQLException, IOException {
      Blob var6 = null;
      OutputStream var7 = null;
      CallableStatement var8 = null;
      boolean var9 = false;

      boolean var10;
      try {
         var8 = var1.prepareCall(this.getCreateByCallableStatement());
         var8.setString(1, var2);
         var8.setLong(2, var4);
         var8.registerOutParameter(3, 2004);
         if (var8.executeUpdate() == 1) {
            var6 = var8.getBlob(3);
            if (var6 instanceof OracleThinBlob) {
               var7 = ((OracleThinBlob)var6).getBinaryOutputStream();
            } else {
               var7 = var6.setBinaryStream(1L);
            }

            var7.write(var3);
            var7.flush();
            var7.close();
            var7 = null;
            var9 = true;
         }

         var10 = var9;
      } finally {
         DbPersistence.close(var7);
         DbPersistence.close((Statement)var8);
      }

      return var10;
   }

   public boolean doStoreByUpdate(Connection var1, String var2, byte[] var3, long var4, boolean var6) throws SQLException, IOException {
      Blob var7 = null;
      CallableStatement var8 = null;
      OutputStream var9 = null;
      PreparedStatement var10 = null;
      PreparedStatement var11 = null;
      ResultSet var12 = null;
      boolean var13 = false;

      boolean var14;
      try {
         if (var6) {
            var8 = var1.prepareCall(this.getStoreByCallableStatement());
            var8.setLong(1, var4);
            var8.setString(2, var2);
            var8.registerOutParameter(3, 2004);
            if (var8.executeUpdate() == 1) {
               var7 = var8.getBlob(3);

               try {
                  if (var7 instanceof OracleThinBlob) {
                     var9 = ((OracleThinBlob)var7).getBinaryOutputStream();
                  } else {
                     var9 = var7.setBinaryStream(1L);
                  }

                  var9.write(var3);
                  var9.flush();
                  var9.close();
                  var9 = null;
                  var13 = true;
               } catch (SQLException var19) {
                  if (var19.getErrorCode() != 22275) {
                     throw var19;
                  }

                  var11 = var1.prepareStatement(this.getSelectObjectStatement());
                  var11.setString(1, var2);
                  var11.executeQuery();
                  var12 = var11.getResultSet();
                  if (var12.next()) {
                     throw var19;
                  }
               }
            }
         } else {
            var14 = false;
            var10 = var1.prepareStatement(this.getStoreLiteStatement());
            var10.setLong(1, var4);
            int var21 = this.getStoreLiteStatementIDColumn();
            var10.setObject(var21, var2);
            var13 = var10.executeUpdate() > 0;
         }

         var14 = var13;
      } finally {
         DbPersistence.close(var9);
         DbPersistence.close((Statement)var8);
         DbPersistence.close((Statement)var10);
         DbPersistence.close(var12);
         DbPersistence.close((Statement)var11);
      }

      return var14;
   }
}
