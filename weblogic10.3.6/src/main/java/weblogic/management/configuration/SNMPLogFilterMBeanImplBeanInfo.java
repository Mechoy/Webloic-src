package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SNMPLogFilterMBeanImplBeanInfo extends SNMPTrapSourceMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPLogFilterMBean.class;

   public SNMPLogFilterMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPLogFilterMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPLogFilterMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents a filter to qualify log messages which are logged to the server logfile. A message must qualify criteria specified as a filter in order to generate a notification. Multiple instances of this MBean can be defined, if needed. If there are multiple instances, a message must qualify atleast one filter to qualify for the server logfile.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPLogFilterMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MessageIds")) {
         var3 = "getMessageIds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageIds";
         }

         var2 = new PropertyDescriptor("MessageIds", SNMPLogFilterMBean.class, var3, var4);
         var1.put("MessageIds", var2);
         var2.setValue("description", "<p>A list of message IDs or ID ranges that cause a WebLogic Server SNMP agent to generate a notification.</p>  <p>If no IDs are specified, this filter selects all message IDs.</p>  <p>Example list: 20,50-100,300</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessageSubstring")) {
         var3 = "getMessageSubstring";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageSubstring";
         }

         var2 = new PropertyDescriptor("MessageSubstring", SNMPLogFilterMBean.class, var3, var4);
         var1.put("MessageSubstring", var2);
         var2.setValue("description", "<p>A string that is searched for in the message text. Only messages that contain the string are selected. If a string is not specified, all messages are selected.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SeverityLevel")) {
         var3 = "getSeverityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSeverityLevel";
         }

         var2 = new PropertyDescriptor("SeverityLevel", SNMPLogFilterMBean.class, var3, var4);
         var1.put("SeverityLevel", var2);
         var2.setValue("description", "<p>The minimum severity of a message that causes a WebLogic Server SNMP agent to generate a notification.</p> ");
         setPropertyDescriptorDefault(var2, "Notice");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SubsystemNames")) {
         var3 = "getSubsystemNames";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSubsystemNames";
         }

         var2 = new PropertyDescriptor("SubsystemNames", SNMPLogFilterMBean.class, var3, var4);
         var1.put("SubsystemNames", var2);
         var2.setValue("description", "<p>A list of subsystems whose messages are selected by this log filter. If none are specified, messages from all subsystems are selected.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UserIds")) {
         var3 = "getUserIds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserIds";
         }

         var2 = new PropertyDescriptor("UserIds", SNMPLogFilterMBean.class, var3, var4);
         var1.put("UserIds", var2);
         var2.setValue("description", "<p>A list of user IDs that causes a WebLogic Server SNMP agent to generate a notification.</p>  <p>Every message includes the user ID from the security context in which the message was generated.</p>  <p>If the user ID field for a message matches one of the user IDs you specify in the filter, WebLogic Server generates a notification.</p>  <p>If this log filter doesn't specify user IDs, WebLogic Server can generate a notification for messages from all user IDs.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SNMPLogFilterMBean.class.getMethod("addSubsystemName", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("subsystem", "The feature to be added to the SubsystemName attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the SubsystemName attribute of the SNMPLogFilterMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SubsystemNames");
      }

      var3 = SNMPLogFilterMBean.class.getMethod("removeSubsystemName", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("subsystem", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SubsystemNames");
      }

      var3 = SNMPLogFilterMBean.class.getMethod("addUserId", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("userid", "The feature to be added to the UserId attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the UserId attribute of the SNMPLogFilterMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "UserIds");
      }

      var3 = SNMPLogFilterMBean.class.getMethod("removeUserId", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("userid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "UserIds");
      }

      var3 = SNMPLogFilterMBean.class.getMethod("addMessageId", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("messageid", "The feature to be added to the MessageId attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the MessageId attribute of the SNMPLogFilterMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "MessageIds");
      }

      var3 = SNMPLogFilterMBean.class.getMethod("removeMessageId", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("messageid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "MessageIds");
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
