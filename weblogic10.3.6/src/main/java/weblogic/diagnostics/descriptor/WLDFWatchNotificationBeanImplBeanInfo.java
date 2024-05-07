package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFWatchNotificationBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFWatchNotificationBean.class;

   public WLDFWatchNotificationBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFWatchNotificationBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFWatchNotificationBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Configures and controls the WebLogic Diagnostic Framework (WLDF) Watch Notification component; creates and deletes watch definitions; and defines the rules that apply to specific watches.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFWatchNotificationBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ImageNotifications")) {
         var3 = "getImageNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("ImageNotifications", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("ImageNotifications", var2);
         var2.setValue("description", "<p>The Image notifications defined in this deployment.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyImageNotification");
         var2.setValue("creator", "createImageNotification");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JMSNotifications")) {
         var3 = "getJMSNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("JMSNotifications", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("JMSNotifications", var2);
         var2.setValue("description", "<p>The JMS notifications defined in this deployment.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSNotification");
         var2.setValue("destroyer", "destroyJMSNotification");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JMXNotifications")) {
         var3 = "getJMXNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("JMXNotifications", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("JMXNotifications", var2);
         var2.setValue("description", "<p>The JMX notifications defined in this deployment.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMXNotification");
         var2.setValue("creator", "createJMXNotification");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LogWatchSeverity")) {
         var3 = "getLogWatchSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogWatchSeverity";
         }

         var2 = new PropertyDescriptor("LogWatchSeverity", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("LogWatchSeverity", var2);
         var2.setValue("description", "<p>The threshold severity level of log messages evaluated by log watch rules. Messages with a lower severity than this value will be ignored and not evaluated against the watch rules.</p>  <p>Do not confuse LogWatchSeverity with Severity. LogWatchSeverity filters which log messages will be evaluated; Severity sets the default severity level for a notification.</p> ");
         setPropertyDescriptorDefault(var2, "Warning");
         var2.setValue("legalValues", new Object[]{"Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Notifications")) {
         var3 = "getNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("Notifications", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("Notifications", var2);
         var2.setValue("description", "<p>The notifications defined in this deployment.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addNotification");
         var2.setValue("remover", "removeNotification");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("SMTPNotifications")) {
         var3 = "getSMTPNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("SMTPNotifications", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("SMTPNotifications", var2);
         var2.setValue("description", "<p>The SMTP notifications defined in this deployment.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSMTPNotification");
         var2.setValue("destroyer", "destroySMTPNotification");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPNotifications")) {
         var3 = "getSNMPNotifications";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPNotifications", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("SNMPNotifications", var2);
         var2.setValue("description", "<p>The SNMP notifications defined in this deployment.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSNMPNotification");
         var2.setValue("destroyer", "destroySNMPNotification");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Severity")) {
         var3 = "getSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSeverity";
         }

         var2 = new PropertyDescriptor("Severity", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("Severity", var2);
         var2.setValue("description", "<p>The default notification severity level for all watches. When a watch triggers, the severity level is delivered with the notification.</p>  <p>The severity levels are the same levels used by the logging framework and the {@link weblogic.logging.Severities} class. If no level is specified, the default value is <code>Notice</code>.</p> ");
         setPropertyDescriptorDefault(var2, "Notice");
         var2.setValue("legalValues", new Object[]{"Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Watches")) {
         var3 = "getWatches";
         var4 = null;
         var2 = new PropertyDescriptor("Watches", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("Watches", var2);
         var2.setValue("description", "<p>The watches defined in this deployment.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWatch");
         var2.setValue("creator", "createWatch");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFWatchNotificationBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether the Watch Notification component is enabled.</p>  <p>If <code>true</code> (the default), all configured watches are activated, incoming data or events are evaluated against the rules, and notifications are sent when rule conditions are met. If <code>false</code>, all watches are rendered inactive.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFWatchNotificationBean.class.getMethod("createWatch", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the watch configuration ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a watch configuration with the given name.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Watches");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("destroyWatch", WLDFWatchBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("watch", "the watch configuration defined in this deployment ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the specified watch configuration defined in this deployment.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Watches");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("createImageNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the Image notification being created ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates an Image notification configuration with the specified name.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "ImageNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("destroyImageNotification", WLDFImageNotificationBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("notification", "the Image notification configuration defined in this deployment ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the specified Image notification configuration defined in this deployment.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "ImageNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("createJMSNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the JMS notification being created ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates an JMS notification configuration with the specified name.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("destroyJMSNotification", WLDFJMSNotificationBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("notification", "the JMS notification configuration defined in this deployment ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the specified JMS notification configuration defined in this deployment.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("createJMXNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the JMX notification being created ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates an JMX notification configuration with the specified name.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMXNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("destroyJMXNotification", WLDFJMXNotificationBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("notification", "the JMX notification configuration defined in this deployment ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the specified JMX notification configuration defined in this deployment.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMXNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("createSMTPNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the SMTP notification being created ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates an SMTP notification configuration with the specified name.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SMTPNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("destroySMTPNotification", WLDFSMTPNotificationBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("notification", "the SMTP notification configuration defined in this deployment ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the specified SMTP notification configuration defined in this deployment.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SMTPNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("createSNMPNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the SNMP notification being created ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates an SNMP notification configuration with the specified name.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SNMPNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("destroySNMPNotification", WLDFSNMPNotificationBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("notification", "the SNMP notification configuration defined in this deployment ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the specified SNMP notification configuration defined in this deployment.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SNMPNotifications");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFWatchNotificationBean.class.getMethod("lookupImageNotification", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the Image notification being requested ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Looks up the Image notification configuration with the specified name.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "ImageNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("lookupJMSNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the JMS notification being requested ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Looks up the JMS notification configuration with the given name.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("lookupJMXNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the JMX notification being requested ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Looks up the JMX notification configuration with the specified name.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMXNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("lookupSMTPNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the SMTP notification being requested ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Looks up the SMTP notification configuration with the specified name.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "SMTPNotifications");
      }

      var3 = WLDFWatchNotificationBean.class.getMethod("lookupSNMPNotification", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the SNMP notification being requested ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Looks up the SNMP notification configuration with the given name.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "SNMPNotifications");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFWatchNotificationBean.class.getMethod("lookupNotification", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Looks up a notification with the given name.</p> ");
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
