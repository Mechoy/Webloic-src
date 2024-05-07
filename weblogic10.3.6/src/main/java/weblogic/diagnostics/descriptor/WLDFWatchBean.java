package weblogic.diagnostics.descriptor;

public interface WLDFWatchBean extends WLDFBean {
   String HARVESTER_RULE_TYPE = "Harvester";
   String LOG_RULE_TYPE = "Log";
   String EVENT_DATA_RULE_TYPE = "EventData";
   String MANUAL_RESET_ALARM_TYPE = "ManualReset";
   String AUTO_RESET_ALARM_TYPE = "AutomaticReset";
   String NONE_ALARM_TYPE = "None";

   boolean isEnabled();

   void setEnabled(boolean var1);

   String getRuleType();

   void setRuleType(String var1);

   String getRuleExpression();

   void setRuleExpression(String var1);

   String getSeverity();

   void setSeverity(String var1);

   String getAlarmType();

   void setAlarmType(String var1);

   int getAlarmResetPeriod();

   void setAlarmResetPeriod(int var1);

   WLDFNotificationBean[] getNotifications();

   void setNotifications(WLDFNotificationBean[] var1);

   boolean addNotification(WLDFNotificationBean var1);

   boolean removeNotification(WLDFNotificationBean var1);
}
