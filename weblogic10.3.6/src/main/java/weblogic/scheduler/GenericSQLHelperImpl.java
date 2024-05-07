package weblogic.scheduler;

class GenericSQLHelperImpl implements SQLHelper {
   protected String TABLE_NAME;
   protected String INSERT_VALUES;
   protected String WHERE_CLAUSE;
   protected String COLUMN_NAMES;

   GenericSQLHelperImpl(String var1, String var2, String var3) {
      this.TABLE_NAME = var1;
      this.INSERT_VALUES = " , '" + var2 + "' , '" + var3 + "'";
      this.WHERE_CLAUSE = " AND DOMAIN_NAME='" + var2 + "' AND CLUSTER_NAME='" + var3 + "'";
      this.COLUMN_NAMES = " ( TIMER_ID, LISTENER, START_TIME, INTERVAL, TIMER_MANAGER_NAME, DOMAIN_NAME, CLUSTER_NAME )";
   }

   public String getCancelTimerSQL(String var1) {
      return "DELETE FROM " + this.TABLE_NAME + " WHERE TIMER_ID = '" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getAdvanceTimerSQL(String var1) {
      long var2 = System.currentTimeMillis();
      return "UPDATE " + this.TABLE_NAME + " SET START_TIME = (" + var2 + " + INTERVAL), LISTENER = ? " + "WHERE TIMER_ID = '" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getCreateTimerSQL(String var1, String var2, long var3, long var5) {
      return "INSERT INTO " + this.TABLE_NAME + this.COLUMN_NAMES + " VALUES ( '" + var1 + "' , ? , " + var3 + " , " + var5 + " , '" + var2 + "'" + this.INSERT_VALUES + " )";
   }

   public String getTimerStateSQL(String var1) {
      return "SELECT * FROM " + this.TABLE_NAME + " WHERE TIMER_ID = '" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getReadyTimersSQL(int var1) {
      long var2 = System.currentTimeMillis() + (long)(var1 * 1000);
      return "SELECT TIMER_ID FROM " + this.TABLE_NAME + " WHERE START_TIME < " + var2 + this.WHERE_CLAUSE;
   }

   public String getTimersSQL(String var1) {
      return "SELECT * FROM " + this.TABLE_NAME + " WHERE TIMER_MANAGER_NAME = '" + var1 + "'" + this.WHERE_CLAUSE;
   }

   public String getTimersSQL(String var1, String var2) {
      return "SELECT * FROM " + this.TABLE_NAME + " WHERE TIMER_MANAGER_NAME = '" + var1 + "' AND TIMER_ID LIKE '" + var2 + "%'" + this.WHERE_CLAUSE;
   }

   public String getCancelTimersSQL(String var1) {
      return "DELETE FROM " + this.TABLE_NAME + " WHERE TIMER_MANAGER_NAME = '" + var1 + "'" + this.WHERE_CLAUSE;
   }
}
