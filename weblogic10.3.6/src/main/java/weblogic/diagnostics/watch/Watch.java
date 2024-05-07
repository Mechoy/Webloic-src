package weblogic.diagnostics.watch;

import com.bea.adaptive.harvester.WatchedValues;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import weblogic.diagnostics.accessor.DataRecord;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.harvester.InstanceNameNormalizer;
import weblogic.diagnostics.harvester.InvalidHarvesterInstanceNameException;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.diagnostics.i18n.DiagnosticsTextWatchTextFormatter;
import weblogic.diagnostics.logging.LogVariablesImpl;
import weblogic.diagnostics.notifications.i18n.NotificationsTextTextFormatter;
import weblogic.diagnostics.query.Query;
import weblogic.diagnostics.query.QueryFactory;
import weblogic.diagnostics.query.UnknownVariableException;
import weblogic.diagnostics.query.VariableDeclarator;
import weblogic.diagnostics.query.VariableIndexResolver;
import weblogic.diagnostics.query.VariableResolver;
import weblogic.logging.LogEntry;
import weblogic.management.ManagementException;

class Watch implements VariableIndexResolver {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private static final int STATE_ENABLED = 0;
   private static final int STATE_DISABLED = 1;
   private static final int STATE_ALARM = 2;
   private String watchName;
   private String watchRuleExpression;
   private int watchRuleType;
   private int watchSeverity;
   private String[] watchNotifications;
   private int watchAlarmType;
   private int watchAlarmResetPeriod;
   private boolean alarm = false;
   private WatchNotificationListener[] watchNotificationListeners;
   private Query watchQuery;
   private VariableDeclarator variableDeclarator;
   private VariableIndexResolver variableIndexResolver;
   private VariableResolver variableResolver;
   private int watchState;
   private long watchResetTime;
   private WatchNotificationRuntimeMBeanImpl wnRuntime;
   WatchedValues watchedValues;
   int countOfInstValuesSetLastCycle = 0;
   int countOfInstValuesSetThisCycle = 0;
   boolean containedTypeValuesInThisCycle = false;
   boolean containedTypeValuesInLastCycle = false;
   boolean isDirty = false;
   private boolean lastPerformedEvaluationResult = false;

   Watch(String var1, int var2, String var3, int var4, int var5, int var6, String[] var7, WatchNotificationListener[] var8, WatchConfiguration var9) throws ManagementException, InvalidWatchException {
      this.setWatchName(var1);
      this.watchedValues = var9.getWatchedValues();
      this.watchRuleType = var2;
      switch (var2) {
         case 1:
            LogVariablesImpl var10 = LogVariablesImpl.getInstance();
            this.variableDeclarator = var10;
            this.variableIndexResolver = var10;
            break;
         case 2:
            HarvesterVariablesImpl var11 = new HarvesterVariablesImpl(var9);
            this.variableIndexResolver = this;
            this.variableResolver = var11;
            break;
         case 3:
            EventDataVariablesImpl var12 = new EventDataVariablesImpl();
            this.variableDeclarator = var12;
            this.variableIndexResolver = var12;
            this.variableResolver = var12;
            break;
         default:
            throw new InvalidWatchException("Unknown rule type " + var2);
      }

      this.setRuleExpression(var3);
      this.setSeverity(var4);
      this.setAlarmType(var5);
      this.setAlarmResetPeriod(var6);
      this.watchNotifications = var7;
      this.watchNotificationListeners = var8;
      this.wnRuntime = (WatchNotificationRuntimeMBeanImpl)WatchManager.getInstance().getWatchNotificationRuntime();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created watch: " + this);
      }

   }

   public WatchedValues.Values addVariable(String var1, String var2) throws UnknownVariableException {
      String[] var3 = HarvesterVariablesParser.parse(var1, var2);
      boolean var4 = false;
      boolean var5 = false;
      WatchedValues.Values var6 = null;
      String var7 = var3[0];
      String var8 = var3[1];
      String var9 = var3[2];
      String var10 = var3[3];

      try {
         if (var9 != null) {
            InstanceNameNormalizer var11 = new InstanceNameNormalizer(var9);
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Normalizing instance name " + var9);
            }

            var9 = var11.translateHarvesterSpec();
            var5 = var11.isRegexPattern();
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Normalized instance name: " + var9 + ", isPattern: " + var5);
            }
         }

         var6 = this.watchedValues.addMetric(var7, var8, var9, var10, var4, var5, false, true);
         return var6;
      } catch (InvalidHarvesterInstanceNameException var12) {
         DiagnosticsLogger.logInvalidWatchVariableInstanceNameSpecification(var9, this.getWatchName());
         throw new UnknownVariableException(var12.getMessage(), var12);
      }
   }

   public int getVariableIndex(String var1) throws UnknownVariableException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Getting index for " + var1);
      }

      WatchedValues.Values var2 = this.addVariable(var1, this.getWatchName());
      if (var2 == null) {
         throw new UnknownVariableException(DiagnosticsTextWatchTextFormatter.getInstance().getUnknownWatchVariableExceptionText(this.getWatchName(), var1));
      } else {
         int var3 = var2.getVID();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Returning index for " + var1 + " idx is " + var3);
         }

         return var3;
      }
   }

   String getWatchName() {
      return this.watchName;
   }

   String getRuleExpression() {
      return this.watchRuleExpression;
   }

   int getRuleType() {
      return this.watchRuleType;
   }

   int getSeverity() {
      return this.watchSeverity;
   }

   String[] getNotifications() {
      return this.watchNotifications;
   }

   int getAlarmType() {
      return this.watchAlarmType;
   }

   boolean hasAlarm() {
      return this.getAlarmType() != 0;
   }

   boolean hasAutomaticResetAlarm() {
      return this.getAlarmType() == 2;
   }

   boolean hasManualResetAlarm() {
      return this.getAlarmType() == 1;
   }

   int getAlarmResetPeriod() {
      return this.watchAlarmResetPeriod;
   }

   WatchNotificationListener[] getNotificationListeners() {
      return this.watchNotificationListeners;
   }

   boolean isEnabled() {
      return this.watchState == 0;
   }

   boolean isDisabled() {
      return this.watchState == 1;
   }

   boolean isAlarm() {
      return this.alarm;
   }

   long getResetTime() {
      return this.watchResetTime;
   }

   void setWatchName(String var1) throws InvalidWatchException {
      if (var1 == null) {
         throw new InvalidWatchException("Name can not be null");
      } else if (var1.length() == 0) {
         throw new InvalidWatchException("Name can not be empty");
      } else {
         this.watchName = var1;
      }
   }

   void setRuleExpression(String var1) throws InvalidWatchException {
      if (var1 == null) {
         throw new InvalidWatchException("Rule expression can not be null");
      } else if ((long)var1.length() == 0L) {
         throw new InvalidWatchException("Rule expression can not be empty");
      } else {
         try {
            this.watchQuery = QueryFactory.createQuery(this.variableIndexResolver, var1);
         } catch (Exception var3) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Parsing of watch rule failed: ", var3);
            }

            throw new InvalidWatchException("Invalid watch rule expression", var3);
         }

         this.watchRuleExpression = var1;
      }
   }

   void setSeverity(int var1) throws InvalidWatchException {
      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 4:
         case 8:
         case 16:
         case 32:
         case 64:
         case 128:
         case 256:
            this.watchSeverity = var1;
            return;
         default:
            throw new InvalidWatchException("Invalid severity " + var1);
      }
   }

   void setAlarmType(int var1) throws InvalidWatchException {
      switch (var1) {
         case 0:
         case 1:
         case 2:
            this.watchAlarmType = var1;
            return;
         default:
            throw new InvalidWatchException("Invalid alarm type " + var1);
      }
   }

   void setAlarmResetPeriod(int var1) throws InvalidWatchException {
      if (var1 < 0) {
         throw new InvalidWatchException("Invalid reset period " + var1);
      } else {
         this.watchAlarmResetPeriod = var1;
      }
   }

   void setNotifications(String[] var1) {
      this.watchNotifications = var1;
   }

   void setNotificationListeners(WatchNotificationListener[] var1) {
      this.watchNotificationListeners = var1;
   }

   void setEnabled() {
      this.setState(0);
   }

   void setDisabled() {
      this.setState(1);
   }

   synchronized void setAlarm(boolean var1) {
      this.alarm = var1;
   }

   void setResetTime(long var1) {
      this.watchResetTime = var1;
   }

   boolean evaluateHarvesterRuleWatch(boolean var1) {
      boolean var2 = false;
      if (!var1) {
         try {
            var2 = this.watchQuery.executeQuery(this.variableResolver);
         } catch (Exception var4) {
            DiagnosticsLogger.logWatchEvaluationFailed(this.watchName, var4);
         }

         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Evaluated watch " + this.watchName + " to " + var2);
         }
      } else {
         var2 = var1;
      }

      if (var2) {
         this.performNotifications(this.watchQuery.getLastExecutionTrace().getEvaluatedVariables());
      }

      return var2;
   }

   boolean evaluateLogRuleWatch(LogEntry var1) {
      LogVariablesImpl.LogVariablesResolver var2 = LogVariablesImpl.getInstance().getLogVariablesResolver(var1);
      boolean var3 = false;

      try {
         var3 = this.watchQuery.executeQuery(var2);
      } catch (Exception var5) {
         DiagnosticsLogger.logWatchEvaluationFailed(this.watchName, var5);
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Evaluated watch " + this.watchName + " to " + var3 + " for LogEntry " + var1);
      }

      if (var3) {
         this.performNotifications(var2.getVariableData());
      }

      return var3;
   }

   boolean evaluateEventDataRuleWatch(DataRecord var1) {
      EventDataVariablesImpl var2 = (EventDataVariablesImpl)this.variableResolver;
      var2.setDataRecord(var1);
      boolean var3 = false;

      try {
         var3 = this.watchQuery.executeQuery(this.variableResolver);
      } catch (Exception var5) {
         DiagnosticsLogger.logWatchEvaluationFailed(this.watchName, var5);
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Evaluated watch " + this.watchName + " to " + var3);
      }

      if (var3) {
         this.performNotifications(var2.getWatchData());
      }

      return var3;
   }

   private void performNotifications(Map var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Performing notification for watch: " + this.watchName);
      }

      try {
         WatchNotificationInternal var2 = new WatchNotificationInternal(this, System.currentTimeMillis(), var1);
         DiagnosticsLogger.logWatchEvaluatedToTrue(var2.getWatchName(), var2.getWatchSeverityLevel(), var2.getWatchServerName(), var2.getWatchTime(), this.recordWatchNotificationDetails(var2));
         if (this.watchNotificationListeners == null || this.watchNotificationListeners.length == 0) {
            return;
         }

         for(int var3 = 0; var3 < this.watchNotificationListeners.length; ++var3) {
            try {
               if (!this.watchNotificationListeners[var3].isDisabled()) {
                  this.watchNotificationListeners[var3].processWatchNotification(var2);
               }
            } catch (Exception var5) {
               DiagnosticsLogger.logNotificationError(this.watchName, this.watchNotifications[var3], var5);
            }
         }
      } catch (Exception var6) {
         DiagnosticsLogger.logWatchNotificationError(this.watchName, var6);
      }

   }

   private String recordWatchNotificationDetails(WatchNotification var1) {
      NotificationsTextTextFormatter var2 = NotificationsTextTextFormatter.getInstance();
      StringWriter var3 = new StringWriter();
      BufferedWriter var4 = new BufferedWriter(var3);

      try {
         var4.newLine();
         var4.write(var2.getSMTPDefaultBodyLine("WatchRuleType", var1.getWatchRuleType()));
         var4.write(var2.getSMTPDefaultBodyLine("WatchRule", this.escapeComparisonOperators(var1.getWatchRule())));
         var4.write(var2.getSMTPDefaultBodyLine("WatchData", var1.getWatchDataToString()));
         var4.write(var2.getSMTPDefaultBodyLine("WatchAlarmType", var1.getWatchAlarmType()));
         var4.write(var2.getSMTPDefaultBodyLine("WatchAlarmResetPeriod", var1.getWatchAlarmResetPeriod()));
         var4.close();
      } catch (IOException var6) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Caught IOException building WatchNotification details string", var6);
         }
      }

      return var3.toString();
   }

   private String escapeComparisonOperators(String var1) {
      return var1.replaceAll("> ", ">");
   }

   private void setState(int var1) {
      this.watchState = var1;
   }

   boolean isModified() {
      return this.isDirty;
   }

   void initTypeValue(String var1, boolean var2, Object var3, Object var4) {
      this.containedTypeValuesInThisCycle = true;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Marking watch " + this.getWatchName() + " modified " + "because a type-based metric is set (" + var1 + ").");
      }

      this.isDirty = true;
   }

   void newInstanceValue(String var1, boolean var2, Object var3, Object var4) {
      ++this.countOfInstValuesSetThisCycle;
      if (!this.isDirty) {
         if (var2) {
            --this.countOfInstValuesSetLastCycle;
            if (var3 != null) {
               if (!var3.equals(var4)) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Marking watch " + this.getWatchName() + " modified because the value of variable " + var1 + " has changed: " + var3 + " ---> " + var4);
                  }

                  this.isDirty = true;
               } else if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("The newly set value of variable " + var1 + " in watch " + this.getWatchName() + "(" + var4 + ") is unchanged from " + "the previous value.");
               }
            } else if (var4 != null) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Marking watch " + this.getWatchName() + " modified because the value of variable " + var1 + " has changed: " + var3 + " ---> " + var4);
               }

               this.isDirty = true;
            } else if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("The newly set value of variable " + var1 + " in watch " + this.getWatchName() + "(" + var4 + ") is unchanged from " + "the previous value.");
            }
         } else {
            debugLogger.debug("Marking watch " + this.getWatchName() + " modified because this is the first time " + " variable " + var1 + "has been set since " + "the latest WLDF config was loaded. ");
            this.isDirty = true;
         }
      }

   }

   boolean finalizeCollectionCycle() {
      if (!this.isDirty) {
         this.isDirty = this.countOfInstValuesSetLastCycle != 0 || this.containedTypeValuesInLastCycle || this.containedTypeValuesInThisCycle;
      }

      return this.isDirty;
   }

   void resetCollectionData() {
      this.resetCollectionData(false);
   }

   void resetCollectionData(boolean var1) {
      this.isDirty = var1;
      this.countOfInstValuesSetLastCycle = this.countOfInstValuesSetThisCycle;
      this.countOfInstValuesSetThisCycle = 0;
      this.containedTypeValuesInLastCycle = this.containedTypeValuesInThisCycle;
      this.containedTypeValuesInThisCycle = false;
   }

   boolean getLastPerformedEvaluationResult() {
      return this.lastPerformedEvaluationResult;
   }

   void setLastPerformedEvaluationResult(boolean var1) {
      this.lastPerformedEvaluationResult = var1;
   }

   public String toString() {
      String var1 = "Watch: " + this.watchName + " rule: " + this.watchRuleExpression + " ruleType: " + this.watchRuleType + " severity: " + this.watchSeverity + " alarmType: " + this.watchAlarmType + " alarmReset: " + this.watchAlarmResetPeriod + " state: " + this.watchState + " notifications: ";

      for(int var2 = 0; this.watchNotifications != null && var2 < this.watchNotifications.length; ++var2) {
         var1 = var1 + " " + this.watchNotifications[var2];
      }

      return var1;
   }
}
