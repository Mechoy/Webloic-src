package weblogic.scheduler;

final class OracleSQLHelperImpl extends GenericSQLHelperImpl {
   OracleSQLHelperImpl(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public String getSelectForInsertSQL(String var1) {
      return "SELECT listener FROM " + this.TABLE_NAME + " WHERE timer_id = '" + var1 + "'" + this.WHERE_CLAUSE + " FOR UPDATE";
   }
}
