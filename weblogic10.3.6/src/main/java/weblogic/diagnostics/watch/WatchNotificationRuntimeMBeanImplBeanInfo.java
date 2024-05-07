package weblogic.diagnostics.watch;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFWatchNotificationRuntimeMBean;

public class WatchNotificationRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFWatchNotificationRuntimeMBean.class;

   public WatchNotificationRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WatchNotificationRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WatchNotificationRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.watch");
      String var3 = (new String("<p>Provides access to Watch and Notification statistical data for the current instance of this server.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFWatchNotificationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveAlarmWatches")) {
         var3 = "getActiveAlarmWatches";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveAlarmWatches", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveAlarmWatches", var2);
         var2.setValue("description", "<p>The names of active alarm watches.</p> ");
      }

      if (!var1.containsKey("AverageEventDataWatchEvaluationTime")) {
         var3 = "getAverageEventDataWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageEventDataWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageEventDataWatchEvaluationTime", var2);
         var2.setValue("description", "<p>The average Instrumentation event data evaluation cycle time, in milliseconds.</p> ");
      }

      if (!var1.containsKey("AverageHarvesterWatchEvaluationTime")) {
         var3 = "getAverageHarvesterWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageHarvesterWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageHarvesterWatchEvaluationTime", var2);
         var2.setValue("description", "<p>The average Harvester evaluation cycle time, in milliseconds.</p> ");
      }

      if (!var1.containsKey("AverageLogWatchEvaluationTime")) {
         var3 = "getAverageLogWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("AverageLogWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageLogWatchEvaluationTime", var2);
         var2.setValue("description", "<p>The average Log evaluation cycle time, in milliseconds.</p> ");
      }

      if (!var1.containsKey("CurrentActiveAlarmsCount")) {
         var3 = "getCurrentActiveAlarmsCount";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentActiveAlarmsCount", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentActiveAlarmsCount", var2);
         var2.setValue("description", "<p>The number of active alarms of any type.</p> ");
      }

      if (!var1.containsKey("MaximumActiveAlarmsCount")) {
         var3 = "getMaximumActiveAlarmsCount";
         var4 = null;
         var2 = new PropertyDescriptor("MaximumActiveAlarmsCount", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MaximumActiveAlarmsCount", var2);
         var2.setValue("description", "<p>The maximum number of active alarms at any one time.</p> ");
      }

      if (!var1.containsKey("MaximumEventDataWatchEvaluationTime")) {
         var3 = "getMaximumEventDataWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaximumEventDataWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MaximumEventDataWatchEvaluationTime", var2);
         var2.setValue("description", "The maximum time spent evaluating EventData watches. ");
      }

      if (!var1.containsKey("MaximumHarvesterWatchEvaluationTime")) {
         var3 = "getMaximumHarvesterWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaximumHarvesterWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MaximumHarvesterWatchEvaluationTime", var2);
         var2.setValue("description", "The maximum time spent evaluating Harvester watches. ");
      }

      if (!var1.containsKey("MaximumLogWatchEvaluationTime")) {
         var3 = "getMaximumLogWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaximumLogWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MaximumLogWatchEvaluationTime", var2);
         var2.setValue("description", "The maximum time spent evaluating Log watches. ");
      }

      if (!var1.containsKey("MinimumEventDataWatchEvaluationTime")) {
         var3 = "getMinimumEventDataWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("MinimumEventDataWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MinimumEventDataWatchEvaluationTime", var2);
         var2.setValue("description", "The minimum time spent evaluating Log watches. ");
      }

      if (!var1.containsKey("MinimumHarvesterWatchEvaluationTime")) {
         var3 = "getMinimumHarvesterWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("MinimumHarvesterWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MinimumHarvesterWatchEvaluationTime", var2);
         var2.setValue("description", "The minimum time spent evaluating Harvester watches. ");
      }

      if (!var1.containsKey("MinimumLogWatchEvaluationTime")) {
         var3 = "getMinimumLogWatchEvaluationTime";
         var4 = null;
         var2 = new PropertyDescriptor("MinimumLogWatchEvaluationTime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("MinimumLogWatchEvaluationTime", var2);
         var2.setValue("description", "The minimum time spent evaluating Log watches. ");
      }

      if (!var1.containsKey("TotalActiveAutomaticResetAlarms")) {
         var3 = "getTotalActiveAutomaticResetAlarms";
         var4 = null;
         var2 = new PropertyDescriptor("TotalActiveAutomaticResetAlarms", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalActiveAutomaticResetAlarms", var2);
         var2.setValue("description", "<p>The total number of active automatically reset alarms.</p> ");
      }

      if (!var1.containsKey("TotalActiveManualResetAlarms")) {
         var3 = "getTotalActiveManualResetAlarms";
         var4 = null;
         var2 = new PropertyDescriptor("TotalActiveManualResetAlarms", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalActiveManualResetAlarms", var2);
         var2.setValue("description", "<p>The total number of active manually reset alarms.</p> ");
      }

      if (!var1.containsKey("TotalDIMGNotificationsPerformed")) {
         var3 = "getTotalDIMGNotificationsPerformed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalDIMGNotificationsPerformed", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalDIMGNotificationsPerformed", var2);
         var2.setValue("description", "The total number of Diagnostic Image notifications fired.  Diagnostic Image files are not true notifications, but this records the number of image captures requested by the watch component. ");
      }

      if (!var1.containsKey("TotalEventDataEvaluationCycles")) {
         var3 = "getTotalEventDataEvaluationCycles";
         var4 = null;
         var2 = new PropertyDescriptor("TotalEventDataEvaluationCycles", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalEventDataEvaluationCycles", var2);
         var2.setValue("description", "<p>The total number of times Instrumentation event data watch rules have been evaluated.</p> ");
      }

      if (!var1.containsKey("TotalEventDataWatchEvaluations")) {
         var3 = "getTotalEventDataWatchEvaluations";
         var4 = null;
         var2 = new PropertyDescriptor("TotalEventDataWatchEvaluations", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalEventDataWatchEvaluations", var2);
         var2.setValue("description", "<p>The total number of Instrumentation event data watch rules that have been evaluated. For each cycle, the Watch and Notification component evaluates all of the enabled Instrumentation event data watches.</p> ");
      }

      if (!var1.containsKey("TotalEventDataWatchesTriggered")) {
         var3 = "getTotalEventDataWatchesTriggered";
         var4 = null;
         var2 = new PropertyDescriptor("TotalEventDataWatchesTriggered", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalEventDataWatchesTriggered", var2);
         var2.setValue("description", "<p>The total number of Instrumentation event data watch rules that have evaluated to <code>true</code> and triggered notifications.</p> ");
      }

      if (!var1.containsKey("TotalFailedDIMGNotifications")) {
         var3 = "getTotalFailedDIMGNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFailedDIMGNotifications", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFailedDIMGNotifications", var2);
         var2.setValue("description", "The total number of failed Diagnostic Image notification requests. ");
      }

      if (!var1.containsKey("TotalFailedJMSNotifications")) {
         var3 = "getTotalFailedJMSNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFailedJMSNotifications", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFailedJMSNotifications", var2);
         var2.setValue("description", "The total number of failed JMS notification attempts. ");
      }

      if (!var1.containsKey("TotalFailedJMXNotifications")) {
         var3 = "getTotalFailedJMXNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFailedJMXNotifications", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFailedJMXNotifications", var2);
         var2.setValue("description", "The total number of failed JMX notification attempts. ");
      }

      if (!var1.containsKey("TotalFailedNotifications")) {
         var3 = "getTotalFailedNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFailedNotifications", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFailedNotifications", var2);
         var2.setValue("description", "The total number of failed notification requests. ");
      }

      if (!var1.containsKey("TotalFailedSMTPNotifications")) {
         var3 = "getTotalFailedSMTPNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFailedSMTPNotifications", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFailedSMTPNotifications", var2);
         var2.setValue("description", "The total number of failed SMTP notification attempts. ");
      }

      if (!var1.containsKey("TotalFailedSNMPNotifications")) {
         var3 = "getTotalFailedSNMPNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFailedSNMPNotifications", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFailedSNMPNotifications", var2);
         var2.setValue("description", "The total number of failed SNMP notification attempts. ");
      }

      if (!var1.containsKey("TotalHarvesterEvaluationCycles")) {
         var3 = "getTotalHarvesterEvaluationCycles";
         var4 = null;
         var2 = new PropertyDescriptor("TotalHarvesterEvaluationCycles", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalHarvesterEvaluationCycles", var2);
         var2.setValue("description", "<p>The total number of times the Harvester has invoked the Watch and Notification component to evaluate Harvester watch rules. (This number corresponds to the number of sampling cycles.)</p> ");
      }

      if (!var1.containsKey("TotalHarvesterWatchEvaluations")) {
         var3 = "getTotalHarvesterWatchEvaluations";
         var4 = null;
         var2 = new PropertyDescriptor("TotalHarvesterWatchEvaluations", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalHarvesterWatchEvaluations", var2);
         var2.setValue("description", "<p>The total number of Harvester watch rules that have been evaluated. For each cycle, the Watch and Notification component evaluates all of the enabled Harvester watches.</p> ");
      }

      if (!var1.containsKey("TotalHarvesterWatchesTriggered")) {
         var3 = "getTotalHarvesterWatchesTriggered";
         var4 = null;
         var2 = new PropertyDescriptor("TotalHarvesterWatchesTriggered", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalHarvesterWatchesTriggered", var2);
         var2.setValue("description", "<p>The total number of Harvester watch rules that have evaluated to <code>true</code> and triggered notifications. </p> ");
      }

      if (!var1.containsKey("TotalJMSNotificationsPerformed")) {
         var3 = "getTotalJMSNotificationsPerformed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalJMSNotificationsPerformed", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalJMSNotificationsPerformed", var2);
         var2.setValue("description", "The total number of JMS notifications successfully fired. ");
      }

      if (!var1.containsKey("TotalJMXNotificationsPerformed")) {
         var3 = "getTotalJMXNotificationsPerformed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalJMXNotificationsPerformed", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalJMXNotificationsPerformed", var2);
         var2.setValue("description", "The total number of JMX notifications successfully fired. ");
      }

      if (!var1.containsKey("TotalLogEvaluationCycles")) {
         var3 = "getTotalLogEvaluationCycles";
         var4 = null;
         var2 = new PropertyDescriptor("TotalLogEvaluationCycles", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalLogEvaluationCycles", var2);
         var2.setValue("description", "<p>The total number of times Log watch rules have been evaluated.</p> ");
      }

      if (!var1.containsKey("TotalLogWatchEvaluations")) {
         var3 = "getTotalLogWatchEvaluations";
         var4 = null;
         var2 = new PropertyDescriptor("TotalLogWatchEvaluations", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalLogWatchEvaluations", var2);
         var2.setValue("description", "<p>The total number of Log watch rules that have been evaluated. For each cycle, the Watch and Notification component evaluates all of the enabled Log watches.</p> ");
      }

      if (!var1.containsKey("TotalLogWatchesTriggered")) {
         var3 = "getTotalLogWatchesTriggered";
         var4 = null;
         var2 = new PropertyDescriptor("TotalLogWatchesTriggered", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalLogWatchesTriggered", var2);
         var2.setValue("description", "<p>The total number of Log watch rules that have evaluated to <code>true</code> and triggered notifications. </p> ");
      }

      if (!var1.containsKey("TotalNotificationsPerformed")) {
         var3 = "getTotalNotificationsPerformed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNotificationsPerformed", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNotificationsPerformed", var2);
         var2.setValue("description", "<p>The total number of notifications performed.</p> ");
      }

      if (!var1.containsKey("TotalSMTPNotificationsPerformed")) {
         var3 = "getTotalSMTPNotificationsPerformed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSMTPNotificationsPerformed", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSMTPNotificationsPerformed", var2);
         var2.setValue("description", "The total number of SMTP notifications successfully fired. ");
      }

      if (!var1.containsKey("TotalSNMPNotificationsPerformed")) {
         var3 = "getTotalSNMPNotificationsPerformed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSNMPNotificationsPerformed", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSNMPNotificationsPerformed", var2);
         var2.setValue("description", "The total number of SNMP notifications successfully fired. ");
      }

      if (!var1.containsKey("WLDFWatchJMXNotificationRuntime")) {
         var3 = "getWLDFWatchJMXNotificationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFWatchJMXNotificationRuntime", WLDFWatchNotificationRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFWatchJMXNotificationRuntime", var2);
         var2.setValue("description", "<p>The WLDFWatchJMXNotificationRuntimeMBean instance that sends JMX notifications when a configured watch rule evaluates to true.</p> ");
         var2.setValue("relationship", "containment");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFWatchNotificationRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFWatchNotificationRuntimeMBean.class.getMethod("resetWatchAlarm", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("watchName", "the name of the watch to reset ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Resets a watch alarm.</p> ");
         var2.setValue("role", "operation");
      }

   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
