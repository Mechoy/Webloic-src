package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSTopicMBeanImplBeanInfo extends JMSDestinationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSTopicMBean.class;

   public JMSTopicMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSTopicMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSTopicMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.TopicBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS topic (Pub/Sub) destination for a JMS server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSTopicMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BytesMaximum")) {
         var3 = "getBytesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesMaximum";
         }

         var2 = new PropertyDescriptor("BytesMaximum", JMSTopicMBean.class, var3, var4);
         var1.put("BytesMaximum", var2);
         var2.setValue("description", "<p>The maximum bytes quota (between <tt>0</tt> and a positive 64-bit integer) that can be stored in this destination. The default value of <i>-1</i> specifies that there is no WebLogic-imposed limit on the number of bytes that can be stored in the destination. However, excessive bytes volume can cause memory saturation, so this value should correspond to the total amount of available system memory relative to the rest of your application load.</p>  <p><b>Range of Values:</b> &gt;= BytesThresholdHigh.</p>  <p>This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p>  <p><i>Note:</i> If a JMS template is used for distributed destination members, then this setting applies only to those specific members and not the distributed destination set as a whole.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesThresholdHigh")) {
         var3 = "getBytesThresholdHigh";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesThresholdHigh";
         }

         var2 = new PropertyDescriptor("BytesThresholdHigh", JMSTopicMBean.class, var3, var4);
         var1.put("BytesThresholdHigh", var2);
         var2.setValue("description", "<p>The upper threshold value that triggers events based on the number of bytes stored in this JMS server. If the number of bytes exceeds this threshold, the following events are triggered :</p>  <ul> <li><b>Log Messages</b>  <p>- A message is logged on the server indicating a high threshold condition.</p> </li>  <li><b>Flow Control</b>  <p>- If flow control is enabled, the destination becomes armed and instructs producers to begin decreasing their message flow.</p> </li> </ul>  <p>A value of -1 specifies that flow control and threshold log messages are disabled for the destination.</p>  <p><b>Range of Values:</b> Between 0 and a positive 64-bit integer; &lt;= BytesMaximum; &gt;BytesThresholdLow.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesThresholdLow")) {
         var3 = "getBytesThresholdLow";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesThresholdLow";
         }

         var2 = new PropertyDescriptor("BytesThresholdLow", JMSTopicMBean.class, var3, var4);
         var1.put("BytesThresholdLow", var2);
         var2.setValue("description", "<p>The lower threshold value (between 0 and a positive 64-bit integer) that triggers events based on the number of bytes stored in this JMS server. If the number of bytes falls below this threshold, the following events are triggered:</p>  <ul> <li><b>Log Messages</b>  <p>- A message is logged on the server indicating that the threshold condition has cleared.</p> </li>  <li><b>Flow Control</b>  <p>- If flow control is enabled, the destination becomes disarmed and instructs producers to begin increasing their message flow.</p> </li> </ul>  <p>A value of -1 specifies that bytes paging, flow control, and threshold log messages are disabled for this JMS server.</p>  <p><b>Range of Values:</b> &lt; BytesThresholdHigh.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DeliveryModeOverride")) {
         var3 = "getDeliveryModeOverride";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeliveryModeOverride";
         }

         var2 = new PropertyDescriptor("DeliveryModeOverride", JMSTopicMBean.class, var3, var4);
         var1.put("DeliveryModeOverride", var2);
         var2.setValue("description", "<p>The delivery mode assigned to all messages that arrive at the destination regardless of the DeliveryMode specified by the message producer.</p>  <p>A value of <tt>No-Delivery</tt> specifies that the DeliveryMode will not be overridden.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, "No-Delivery");
         var2.setValue("legalValues", new Object[]{"Persistent", "Non-Persistent", "No-Delivery"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DestinationKeys")) {
         var3 = "getDestinationKeys";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDestinationKeys";
         }

         var2 = new PropertyDescriptor("DestinationKeys", JMSTopicMBean.class, var3, var4);
         var1.put("DestinationKeys", var2);
         var2.setValue("description", "<p>A read-only array of the destination keys of the JMS template or destination. Destination keys define the sort order for messages that arrive on a specific destination. The keys are ordered from most significant to least significant. If more than one key is specified, a key based on the <tt>JMSMessageID</tt> property can only be the last key in the list.</p>  <p><i>Note:</i> If JMSMessageID is not defined in the key, it is implicitly assumed to be the last key and is set as \"ascending\" (first-in, first-out) for the sort order.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeDestinationKey");
         var2.setValue("adder", "addDestinationKey");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ErrorDestination")) {
         var3 = "getErrorDestination";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setErrorDestination";
         }

         var2 = new PropertyDescriptor("ErrorDestination", JMSTopicMBean.class, var3, var4);
         var1.put("ErrorDestination", var2);
         var2.setValue("description", "<p>The destination for messages that have reached their redelivery limit, or for expired messages on the destination where the expiration policy is <code>Redirect</code>. If this destination has a template, <code>(none)</code> indicates that the error destination comes from the template. If this destination has no template, <code>(none)</code> indicates that there is no error destination configured.</p>  <p><i>Note:</i> If a redelivery limit is specified, but no error destination is set, then messages that have reached their redelivery limit are simply discarded.</p>  <p><i>Note:</i> The error destination must be a destination that is configured on the local JMS server.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ExpirationLoggingPolicy")) {
         var3 = "getExpirationLoggingPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExpirationLoggingPolicy";
         }

         var2 = new PropertyDescriptor("ExpirationLoggingPolicy", JMSTopicMBean.class, var3, var4);
         var1.put("ExpirationLoggingPolicy", var2);
         var2.setValue("description", "<p>The policy that defines what information about the message is logged when the Expiration Policy on this destination is set to <tt>Log</tt>.</p>  <p>The valid logging policy values are:</p>  <ul> <li><b>%header%</b>  <p>- All JMS header fields are logged.</p> </li>  <li><b>%properties%</b>  <p>- All user-defined properties are logged.</p> </li>  <li><b>JMSDeliveryTime</b>  <p>- This WebLogic JMS-specific extended header field is logged.</p> </li>  <li><b>JMSRedeliveryLimit</b>  <p>- This WebLogic JMS-specific extended header field is logged.</p> </li>  <li><b><i>foo</i></b>  <p>- Any valid JMS header field or user-defined property is logged.</p> </li> </ul>  <p>When specifying multiple values, enter them as a comma-separated list. The <tt><tt>%header%</tt></tt> and <tt>%properties%</tt> values are <i>not</i> case sensitive. For example, you could use <tt>\"%header%,%properties%\"</tt> for all the JMS header fields and user properties. However, the enumeration of individual JMS header fields and user-defined properties are case sensitive. To enumerate only individual JMS header fields you could use <tt>\"%header, name, address, city, state, zip\"</tt>.N</p>  <p><i>Note:</i> The <tt>JMSMessageID</tt> field is always logged and cannot be turned off. Therefore, if the Expiration Logging Policy is not defined (i.e., null) or is defined as an empty string, then the output to the log file contains only the <tt>JMSMessageID</tt> of the message.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("ExpirationPolicy")) {
         var3 = "getExpirationPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExpirationPolicy";
         }

         var2 = new PropertyDescriptor("ExpirationPolicy", JMSTopicMBean.class, var3, var4);
         var1.put("ExpirationPolicy", var2);
         var2.setValue("description", "<p>The message Expiration Policy uses when an expired message is encountered on a destination.</p>  <p>The valid expiration policies are:</p>  <p><b>None</b> - Same as the Discard policy; expired messages are simply removed from the destination.</p>  <p><b>Discard</b> - Removes expired messages from the messaging system. The removal is not logged and the message is not redirected to another location. If no value is defined for a given destination (i.e., None), then expired messages are discarded.</p>  <p><b>Log</b> - Removes expired messages from the system and writes an entry to the server log file indicating that the messages have been removed from the system. The actual information that is logged is defined by the Expiration Logging Policy.</p>  <p><b>Redirect</b> - Moves expired messages from their current location to the Error Destination defined for the destination. The message retains its body, and all of its properties. The message also retains all of its header fields, but with the following exceptions:</p>  <ul> <li> <p>The destination for the message becomes the error destination.</p> </li>  <li> <p>All property overrides associated with the error destination are applied to the redirected message.</p> </li>  <li> <p>If there is no Time-To-Live Override value for the error destination, then the message receives a new Expiration Time of zero (indicating that it will not expire again</p> </li> </ul>  <p>It is illegal to use the Redirect policy when there is no valid error destination defined for the destination. Similarly, it is illegal to remove the error destination for a destination that is using the Redirect policy.</p>  <p><i>Note:</i> The Maximum Message quota is only enforced for sending new messages. It is ignored when moving messages because of the Redirect policy.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setExpirationPolicy")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "Discard");
         var2.setValue("legalValues", new Object[]{"Discard", "Log", "Redirect"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIName";
         }

         var2 = new PropertyDescriptor("JNDIName", JMSTopicMBean.class, var3, var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "<p>The JNDI name used to look up the destination within the JNDI namespace. If not specified, the destination name is not advertised through the JNDI namespace and cannot be looked up and used.</p>  <p><i>Note:</i> This attribute is not dynamically configurable.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaximumMessageSize")) {
         var3 = "getMaximumMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaximumMessageSize";
         }

         var2 = new PropertyDescriptor("MaximumMessageSize", JMSTopicMBean.class, var3, var4);
         var1.put("MaximumMessageSize", var2);
         var2.setValue("description", "<p>The maximum size of a message in bytes that will be accepted from producers on this JMS server. The message size includes the message body, any user-defined properties, and the user-defined JMS header fields: <tt>JMSCorrelationID</tt> and <tt>JMSType</tt>. Producers sending messages that exceed the configured maximum message size for the JMS server receive a <tt>ResourceAllocationException</tt>.</p>  <p><b>Range of Values:</b> Between <tt>0</tt> and a positive 32-bit integer.</p>  <p>The maximum message size is only enforced for the initial production of a message. Messages that are redirected to an error destination or forwarded to a member of a distributed destination are not checked for size. For instance, if a destination and its corresponding error destination are configured with a maximum message size of 128K bytes and 64K bytes, respectively, a message of 96K bytes could be redirected to the error destination (even though it exceeds the Range of Values: the 64K byte maximum), but a producer could not directly send the 96K byte message to the error destination.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesMaximum")) {
         var3 = "getMessagesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesMaximum";
         }

         var2 = new PropertyDescriptor("MessagesMaximum", JMSTopicMBean.class, var3, var4);
         var1.put("MessagesMaximum", var2);
         var2.setValue("description", "<p>The maximum message quota (total amount of messages) that can be stored in this JMS server. The default value of <i>-1</i> specifies that there is no WebLogic-imposed limit on the number of messages that can be stored. However, excessive message volume can cause memory saturation, so this value should correspond to the total amount of available system memory relative to the rest of your application load.</p>  <p><b>Range of Values:</b> &gt;= MessagesThresholdHigh.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p>  <p><i>Note:</i> If a JMS template is used for distributed destination members, then this setting applies only to those specific members and not the distributed destination set as a whole.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesThresholdHigh")) {
         var3 = "getMessagesThresholdHigh";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesThresholdHigh";
         }

         var2 = new PropertyDescriptor("MessagesThresholdHigh", JMSTopicMBean.class, var3, var4);
         var1.put("MessagesThresholdHigh", var2);
         var2.setValue("description", "<p>The upper threshold value that triggers events based on the number of messages stored in this JMS server. If the number of messages exceeds this threshold, the following events are triggered:</p>  <ul> <li><b>Log Messages</b>  <p>- A message is logged on the server indicating a high threshold condition.</p> </li>  <li><b>Flow Control</b>  <p>- If flow control is enabled, the destination becomes armed and instructs producers to begin decreasing their message flow.</p> </li> </ul>  <p>A value of -1 specifies that bytes paging, flow control, and threshold log messages are disabled for this JMS server.</p>  <p><b>Range of Values:</b> Between 0 and a positive 64-bit integer; &lt;= MessagesMaximum; &gt;MessagesThresholdLow.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesThresholdLow")) {
         var3 = "getMessagesThresholdLow";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesThresholdLow";
         }

         var2 = new PropertyDescriptor("MessagesThresholdLow", JMSTopicMBean.class, var3, var4);
         var1.put("MessagesThresholdLow", var2);
         var2.setValue("description", "<p>The lower threshold value that triggers events based on the number of messages stored in this JMS server. If the number of messages falls below this threshold, the following events are triggered:</p>  <ul> <li><b>Log Messages</b>  <p>- A message is logged on the server indicating that the threshold condition has cleared.</p> </li>  <li><b>Flow Control</b>  <p>- If flow control is enabled, the destination becomes disarmed and instructs producers to begin increasing their message flow.</p> </li> </ul>  <p>A value of -1 specifies that bytes paging, flow control, and threshold log messages are disabled for this JMS server.</p>  <p><b>Range of Values:</b> Between 0 and a positive 64-bit integer; &lt; MessagesThresholdHigh.</p>  <p>This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MulticastAddress")) {
         var3 = "getMulticastAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastAddress";
         }

         var2 = new PropertyDescriptor("MulticastAddress", JMSTopicMBean.class, var3, var4);
         var1.put("MulticastAddress", var2);
         var2.setValue("description", "<p>The multicast address used by this topic.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MulticastPort")) {
         var3 = "getMulticastPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastPort";
         }

         var2 = new PropertyDescriptor("MulticastPort", JMSTopicMBean.class, var3, var4);
         var1.put("MulticastPort", var2);
         var2.setValue("description", "<p>The multicast port for this topic.</p>  <p>This port is used to transmit messages to multicast consumers.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(6001));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MulticastTTL")) {
         var3 = "getMulticastTTL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastTTL";
         }

         var2 = new PropertyDescriptor("MulticastTTL", JMSTopicMBean.class, var3, var4);
         var1.put("MulticastTTL", var2);
         var2.setValue("description", "<p>The number of network hops that a multicast message is allowed to travel.</p>  <p>This is the Time-To-Live value used for multicasting, which specifies the number of routers that the message can traverse en route to the consumers. A value of <tt>1</tt> indicates that the message will not traverse any routers and is limited to one subnet.</p>  <p><i>Note:</i> This value is independent of the JMSExpirationTime value.</p>  <p><i>Note:</i> This attribute is not dynamically configurable.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(255));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSTopicMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Notes")) {
         var3 = "getNotes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotes";
         }

         var2 = new PropertyDescriptor("Notes", JMSTopicMBean.class, var3, var4);
         var1.put("Notes", var2);
         var2.setValue("description", "<p>Optional information that you can include to describe this configuration.</p>  <p>WebLogic Server saves this note in the domain's configuration file (<code>config.xml</code>) as XML PCDATA. All left angle brackets (&lt;) are converted to the XML entity <code>&amp;lt;</code>. Carriage returns/line feeds are preserved.</p>  <dl> <dt>Note:</dt>  <dd> <p>If you create or edit a note from the Administration Console, the Administration Console does not preserve carriage returns/line feeds.</p> </dd> </dl> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PriorityOverride")) {
         var3 = "getPriorityOverride";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPriorityOverride";
         }

         var2 = new PropertyDescriptor("PriorityOverride", JMSTopicMBean.class, var3, var4);
         var1.put("PriorityOverride", var2);
         var2.setValue("description", "<p>The priority assigned to all messages that arrive at the destination, regardless of the Priority specified by the message producer. The default value (-1) specifies that the destination will not override the Priority set by the message producer.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(9));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RedeliveryDelayOverride")) {
         var3 = "getRedeliveryDelayOverride";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRedeliveryDelayOverride";
         }

         var2 = new PropertyDescriptor("RedeliveryDelayOverride", JMSTopicMBean.class, var3, var4);
         var1.put("RedeliveryDelayOverride", var2);
         var2.setValue("description", "<p>The delay, in milliseconds, before rolled back or recovered messages are redelivered, regardless of the RedeliveryDelay specified by the consumer and/or connection factory. Redelivered queue messages are put back into their originating destination; redelivered topic messages are put back into their originating subscription. The default value (-1) specifies that the destination will not override the RedeliveryDelay setting specified by the consumer and/or connection factory.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p>  <p><i>Note:</i> Changing the RedeliveryDelayOverride only affects future rollbacks and recovers, it does not affect rollbacks and recovers that have already occurred.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RedeliveryLimit")) {
         var3 = "getRedeliveryLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRedeliveryLimit";
         }

         var2 = new PropertyDescriptor("RedeliveryLimit", JMSTopicMBean.class, var3, var4);
         var1.put("RedeliveryLimit", var2);
         var2.setValue("description", "<p>The number of redelivery tries (between <tt>0</tt> and a positive 32-bit integer) a message can have before it is moved to the error destination. This setting overrides any redelivery limit set by the message sender. If the redelivery limit is configured, but no error destination is configured, then persistent and non-persistent messages are simply dropped (deleted) when they reach their redelivery limit.</p>  <p>A value of <tt>-1</tt> means that this value is inherited from the JMS template, if one is configured. If no JMS template is configured, then <tt>-1</tt> means that there is no override.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; previously sent messages continue to use their original redelivery limit.</p>  <p><i>Note:</i> The number of times a message has been redelivered is not persisted. This means that after a restart, the number of delivery attempts on each message is reset to zero.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("StoreEnabled")) {
         var3 = "getStoreEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStoreEnabled";
         }

         var2 = new PropertyDescriptor("StoreEnabled", JMSTopicMBean.class, var3, var4);
         var1.put("StoreEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the destination supports persistent messaging by using the JMS store specified by the JMS server.</p>  <p>Supported values are:</p>  <ul> <li><b>default</b> <p>- The destination uses the JMS store defined for the JMS server, if one is defined, and supports persistent messaging. However, if a JMS store is not defined for the JMS server, then persistent messages are automatically downgraded to non-persistent.</p> </li>  <li><b>false</b> <p>- The destination does not support persistent messaging.</p> </li>  <li><b>true</b> <p>- The destination does support persistent messaging. However, if a JMS store is not defined for the JMS server, then the configuration will fail and the JMS server will not boot.</p> </li> </ul>  <p><i>Note:</i> This attribute is not dynamically configurable.</p> ");
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("legalValues", new Object[]{"default", "false", "true"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Template")) {
         var3 = "getTemplate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTemplate";
         }

         var2 = new PropertyDescriptor("Template", JMSTopicMBean.class, var3, var4);
         var1.put("Template", var2);
         var2.setValue("description", "<p>The JMS template from which the destination is derived. If a JMS template is specified, destination attributes that are set to their default values will inherit their values from the JMS template at run time. However, if this attribute is not defined, then the attributes for the destination must be specified as part of the destination.</p>  <p><i>Note:</i> The Template attribute setting per destination is static. The JMS template's attributes, however, can be modified dynamically.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("TimeToDeliverOverride")) {
         var3 = "getTimeToDeliverOverride";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeToDeliverOverride";
         }

         var2 = new PropertyDescriptor("TimeToDeliverOverride", JMSTopicMBean.class, var3, var4);
         var1.put("TimeToDeliverOverride", var2);
         var2.setValue("description", "<p>The default delay, either in milliseconds or as a schedule, between when a message is produced and when it is made visible on its target destination, regardless of the delivery time specified by the producer and/or connection factory. The default value (<tt>-1</tt>) specifies that the destination will not override the TimeToDeliver setting specified by the producer and/or connection factory. The TimeToDeliverOverride can be specified either as a long or as a schedule.</p>  <p><i>Note:</i> Changing the TimeToDeliverOverride only affects future message delivery, it does not affect message delivery of already produced messages.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.jms.extensions.Schedule")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "-1");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TimeToLiveOverride")) {
         var3 = "getTimeToLiveOverride";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeToLiveOverride";
         }

         var2 = new PropertyDescriptor("TimeToLiveOverride", JMSTopicMBean.class, var3, var4);
         var1.put("TimeToLiveOverride", var2);
         var2.setValue("description", "<p>The time-to-live assigned to all messages that arrive at this topic, regardless of the TimeToLive value specified by the message producer. The default value (<tt>-1</tt>) specifies that this setting will not override the TimeToLive setting specified by the message producer.</p>  <p><b>Range of Values:</b> Between 0 and a positive 64-bit integer.</p>  <p><i>Note:</i> This attribute is dynamically configurable, but only incoming messages are impacted; stored messages are not impacted.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDINameReplicated")) {
         var3 = "isJNDINameReplicated";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDINameReplicated";
         }

         var2 = new PropertyDescriptor("JNDINameReplicated", JMSTopicMBean.class, var3, var4);
         var1.put("JNDINameReplicated", var2);
         var2.setValue("description", "<p>Indicates whether the JNDI name is replicated across the cluster. If JNDINameReplicated is set to true, then the JNDI name for the destination (if present) is replicated across the cluster. If JNDINameReplicated is set to false, then the JNDI name for the destination (if present) is only visible from the server of which this destination is a part.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSTopicMBean.class.getMethod("addDestinationKey", JMSDestinationKeyMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("destinationKey", "a reference to JMSDestinationKeyMBean ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Add a destination key to the JMS template or destination. Destination keys define the sort order for messages that arrive on a specific destination. The keys are ordered from most significant to least significant. If more than one key is specified, a key based on the <tt>JMSMessageID</tt> property can only be the last key in the list.</p>  <p><i>Note:</i> If JMSMessageID is not defined in the key, it is implicitly assumed to be the last key and is set as \"ascending\" (first-in, first-out) for the sort order.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "DestinationKeys");
      }

      var3 = JMSTopicMBean.class.getMethod("removeDestinationKey", JMSDestinationKeyMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("destinationKey", "a reference to  JMSDestinationKeyMBean ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Remove a destination key from the JMS template or destination. Destination keys define the sort order for messages that arrive on a specific destination. The keys are ordered from most significant to least significant. If more than one key is specified, a key based on the <tt>JMSMessageID</tt> property can only be the last key in the list.</p>  <p><i>Note:</i> If JMSMessageID is not defined in the key, it is implicitly assumed to be the last key and is set as \"ascending\" (first-in, first-out) for the sort order.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "DestinationKeys");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSTopicMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSTopicMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
