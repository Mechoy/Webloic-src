package weblogic.scheduler;

final class InformixSQLHelperImpl extends GenericSQLHelperImpl {
   InformixSQLHelperImpl(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public String getAdvanceTimerSQL(String var1) {
      long var2 = System.currentTimeMillis();
      return "UPDATE " + this.TABLE_NAME + " SET START_TIME = (" + var2 + " + " + this.TABLE_NAME + ".INTERVAL), LISTENER = ? " + "WHERE TIMER_ID = '" + var1 + "'" + this.WHERE_CLAUSE;
   }
}
