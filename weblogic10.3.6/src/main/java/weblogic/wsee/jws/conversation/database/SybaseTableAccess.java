package weblogic.wsee.jws.conversation.database;

import weblogic.wsee.jws.util.Config;

class SybaseTableAccess extends GenericTableAccess {
   protected String getCreateTableStatement() {
      StringBuffer var1 = new StringBuffer();
      var1.append("CREATE TABLE " + this._tableName + "(");
      var1.append("CG_ID varchar(" + Config.getProperty("weblogic.jws.ConversationMaxKeyLength") + ") NOT NULL PRIMARY KEY,");
      var1.append("LAST_ACCESS_TIME DECIMAL(19,0), ");
      var1.append("CG_DATA IMAGE");
      var1.append(") LOCK DATAROWS");
      return var1.toString();
   }

   protected String getLoadStatement(boolean var1) {
      if (var1 && this._loadStmt != null) {
         return this._loadStmt;
      } else if (!var1 && this._loadStmtNoUpdateLock != null) {
         return this._loadStmtNoUpdateLock;
      } else {
         StringBuffer var2 = new StringBuffer("SELECT LAST_ACCESS_TIME, CG_DATA ");
         var2.append(" FROM " + this._tableName);
         if (var1) {
            var2.append(" HOLDLOCK");
         }

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
