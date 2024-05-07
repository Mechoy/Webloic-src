package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFWatchBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFWatchBean.class;

   public WLDFWatchBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFWatchBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFWatchBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Defines watches and notifications.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFWatchBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AlarmResetPeriod")) {
         var3 = "getAlarmResetPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAlarmResetPeriod";
         }

         var2 = new PropertyDescriptor("AlarmResetPeriod", WLDFWatchBean.class, var3, var4);
         var1.put("AlarmResetPeriod", var2);
         var2.setValue("description", "<p>For automatic alarms, the time period, in milliseconds, to wait after the watch evaluates to <code>true</code> before the alarm is automatically reset.</p>  <p>The default reset period is 60000 milliseconds, which is equivalent to 60 seconds.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60000));
         var2.setValue("legalMin", new Integer(1000));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AlarmType")) {
         var3 = "getAlarmType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAlarmType";
         }

         var2 = new PropertyDescriptor("AlarmType", WLDFWatchBean.class, var3, var4);
         var1.put("AlarmType", var2);
         var2.setValue("description", "<p>The alarm type for the watch: manual or automatic. The default alarm type is manual.</p>  <p>Once a manually set alarm has triggered, it must be reset through the WebLogic Server Administration Console or programmatically before it can trigger again. An automatic reset alarm will reset after the specified time period has elapsed.</p> ");
         setPropertyDescriptorDefault(var2, "None");
         var2.setValue("legalValues", new Object[]{"None", "ManualReset", "AutomaticReset"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Notifications")) {
         var3 = "getNotifications";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotifications";
         }

         var2 = new PropertyDescriptor("Notifications", WLDFWatchBean.class, var3, var4);
         var1.put("Notifications", var2);
         var2.setValue("description", "<p>The notifications enabled for this watch.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addNotification");
         var2.setValue("remover", "removeNotification");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RuleExpression")) {
         var3 = "getRuleExpression";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRuleExpression";
         }

         var2 = new PropertyDescriptor("RuleExpression", WLDFWatchBean.class, var3, var4);
         var1.put("RuleExpression", var2);
         var2.setValue("description", "<p>The rule expression used to evaluate the watch.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RuleType")) {
         var3 = "getRuleType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRuleType";
         }

         var2 = new PropertyDescriptor("RuleType", WLDFWatchBean.class, var3, var4);
         var1.put("RuleType", var2);
         var2.setValue("description", "<p>The rule-expression type for the watch: <code>HARVESTER_RULE_TYPE</code> refers to harvested data, <code>LOG_RULE_TYPE</code> refers to log entry data, and <code>EVENT_DATA_RULE_TYPE</code> refers to instrumentation event data. The default type is <code>HARVESTER_RULE_TYPE</code>.</p>  <p>For information on rule expressions, see \"Using the Diagnostics Framework for Oracle WebLogic Server\" on <a href=\"http://www.oracle.com/technology/index.html\" shape=\"rect\">http://www.oracle.com/technology/index.html</a>.</p> ");
         setPropertyDescriptorDefault(var2, "Harvester");
         var2.setValue("legalValues", new Object[]{"Harvester", "Log", "EventData"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Severity")) {
         var3 = "getSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSeverity";
         }

         var2 = new PropertyDescriptor("Severity", WLDFWatchBean.class, var3, var4);
         var1.put("Severity", var2);
         var2.setValue("description", "<p>The severity level of the notifications sent when this watch evaluates to <code>true</code>. When set, this level overrides the default value provided in the parent MBean. However, if no severity level is set (null), the value provided in the parent MBean is returned.</p>  <p>The severity levels are the same levels used by the logging framework and the {@link weblogic.logging.Severities} class.</p> ");
         setPropertyDescriptorDefault(var2, "Notice");
         var2.setValue("legalValues", new Object[]{"Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFWatchBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether this watch is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFWatchBean.class.getMethod("addNotification", WLDFNotificationBean.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Adds a notification to this watch.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Notifications");
      }

      var3 = WLDFWatchBean.class.getMethod("removeNotification", WLDFNotificationBean.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Removes a notification from this watch.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Notifications");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
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
