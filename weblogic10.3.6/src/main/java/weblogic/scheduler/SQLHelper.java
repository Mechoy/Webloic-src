package weblogic.scheduler;

public interface SQLHelper {
   String getCancelTimerSQL(String var1);

   String getAdvanceTimerSQL(String var1);

   String getCreateTimerSQL(String var1, String var2, long var3, long var5);

   String getTimerStateSQL(String var1);

   String getReadyTimersSQL(int var1);

   String getTimersSQL(String var1);

   String getTimersSQL(String var1, String var2);

   String getCancelTimersSQL(String var1);
}
