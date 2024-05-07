package weblogic.wsee.jws.conversation.database;

import weblogic.wsee.jws.util.Config;

class PointbaseTableAccess extends GenericTableAccess {
   public String getCreateTableStatement() {
      StringBuffer var1 = new StringBuffer();
      var1.append("CREATE TABLE " + this._tableName + "(");
      var1.append("CG_ID varchar(");
      var1.append(Config.getProperty("weblogic.jws.ConversationMaxKeyLength"));
      var1.append(") not null,");
      var1.append("LAST_ACCESS_TIME bigint, ");
      String var2 = Config.getProperty("weblogic.jws.cgdata.size");
      var1.append("CG_DATA BLOB(" + var2 + ")");
      var1.append(", CONSTRAINT PK_" + this._tableName);
      var1.append("_ID PRIMARY KEY (CG_ID) )");
      return var1.toString();
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
}
