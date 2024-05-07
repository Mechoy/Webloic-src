package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSConnectionFactoryMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSConnectionFactoryMBean.class;

   public JMSConnectionFactoryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSConnectionFactoryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSConnectionFactoryMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.JmsConnectionFactoryBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS connection factory. Connection factories are objects that enable JMS clients to create JMS connections.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSConnectionFactoryMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AcknowledgePolicy")) {
         var3 = "getAcknowledgePolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcknowledgePolicy";
         }

         var2 = new PropertyDescriptor("AcknowledgePolicy", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("AcknowledgePolicy", var2);
         var2.setValue("description", "<p>Acknowledge policy for non-transacted sessions that use the <tt>CLIENT_ACKNOWLEDGE</tt> mode. <tt>All</tt> specifies that calling acknowledge on a message acknowledges all unacknowledged messages received on the session. <tt>Previous</tt> specifies that calling acknowledge on a message acknowledges only unacknowledged messages up to, and including, the given message.</p>  <p><i>Note:</i> This value only applies to implementations that use the <tt>CLIENT_ACKNOWLEDGE</tt> acknowledge mode for a non-transacted session.</p>  <p><i>Note:</i> This value works around a change in the JMS specification. Specifically, the specification allowed users to acknowledge all messages before and including the message being acknowledged. The specification was changed so that acknowledging any message acknowledges all messages ever received (even those received after the message being acknowledge), as follows:</p>  <ul> <li> <p>An acknowledge policy of <tt>ACKNOWLEDGE_PREVIOUS</tt> retains the old behavior (acknowledge all message up to and including the message being acknowledged).</p> </li>  <li> <p>An acknowledge policy of <tt>ACKNOWLEDGE_ALL</tt> yields the new behavior, where all messages received by the given session are acknowledged regardless of which message is being used to effect the acknowledge.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "All");
         var2.setValue("secureValue", "All");
         var2.setValue("legalValues", new Object[]{"All", "Previous"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AllowCloseInOnMessage")) {
         var3 = "getAllowCloseInOnMessage";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowCloseInOnMessage";
         }

         var2 = new PropertyDescriptor("AllowCloseInOnMessage", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("AllowCloseInOnMessage", var2);
         var2.setValue("description", "<p>Specifies whether a connection factory creates message consumers that allow a <tt>close()</tt> or <tt>stop()</tt> method to be issued within its <tt>onMessage()</tt> method call.</p>  <ul> <li> <p>If selected (true), a <tt>close()</tt> method call from within an <tt>onMessage()</tt> method call will succeed instead of blocking forever. If the acknowledge mode of the session is set to <tt>AUTO_ACKNOWLEDGE</tt>, the current message will still be acknowledged automatically when the <tt>onMessage()</tt> call completes.</p> </li>  <li> <p>If not selected (false), it will cause the <tt>stop()</tt> and <tt>close()</tt> methods to hang if called from <tt>onMessage()</tt>.</p> </li> </ul>  <p><i>Note:</i> This value is dynamic and can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClientId")) {
         var3 = "getClientId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientId";
         }

         var2 = new PropertyDescriptor("ClientId", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("ClientId", var2);
         var2.setValue("description", "<p>An optional client ID for a durable subscriber that uses this JMS connection factory. Configuring this value on the connection factory prevents more than one JMS client from using a connection from the factory. Generally, JMS durable subscriber applications set their client IDs dynamically using the <tt>javax.jms.Connection.setClientID()</tt> call.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultDeliveryMode")) {
         var3 = "getDefaultDeliveryMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultDeliveryMode";
         }

         var2 = new PropertyDescriptor("DefaultDeliveryMode", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("DefaultDeliveryMode", var2);
         var2.setValue("description", "<p>The delivery mode assigned to all messages sent by a producer using this connection factory.</p>  <p>Message producers can get the delivery mode explicitly by calling the <tt>javax.jms.MessageProducer.getDeliveryMode()</tt> method.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p> ");
         setPropertyDescriptorDefault(var2, "Persistent");
         var2.setValue("legalValues", new Object[]{"Persistent", "Non-Persistent"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultPriority")) {
         var3 = "getDefaultPriority";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultPriority";
         }

         var2 = new PropertyDescriptor("DefaultPriority", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("DefaultPriority", var2);
         var2.setValue("description", "<p>The default priority used for messages when a priority is not explicitly defined.</p>  <p>Message producers can set the priority explicitly by calling the <tt>javax.jms.MessageProducer.setPriority()</tt> method.</p>  <p><b>Range of Values:</b> Between 0 and 9.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(4));
         var2.setValue("legalMax", new Integer(9));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultRedeliveryDelay")) {
         var3 = "getDefaultRedeliveryDelay";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRedeliveryDelay";
         }

         var2 = new PropertyDescriptor("DefaultRedeliveryDelay", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("DefaultRedeliveryDelay", var2);
         var2.setValue("description", "<p>The number of milliseconds before rolled back or recovered messages are redelivered. This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p>  <p>Message consumers can get the redelivery delay explicitly by calling the <tt>weblogic.jms.extensions.WLSession.getRedliveryDelay()</tt> method.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 64-bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultTimeToDeliver")) {
         var3 = "getDefaultTimeToDeliver";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTimeToDeliver";
         }

         var2 = new PropertyDescriptor("DefaultTimeToDeliver", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("DefaultTimeToDeliver", var2);
         var2.setValue("description", "<p>The number of milliseconds between when a message is produced and when it is made visible on its target destination.</p>  <p>Message producers can get the time-to-deliver explicitly by calling the <tt>weblogic.jms.extensions.WLMessageProducer.getTimeToDeliver()</tt> method.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 64-bit integer.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultTimeToLive")) {
         var3 = "getDefaultTimeToLive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTimeToLive";
         }

         var2 = new PropertyDescriptor("DefaultTimeToLive", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("DefaultTimeToLive", var2);
         var2.setValue("description", "<p>The default maximum number of milliseconds that a message will exist. Used for messages for which a Time to Live was not explicitly defined.</p>  <p>The default value of <tt>0</tt> indicates that the message has an infinite amount time to live.</p>  <p>Message producers can get the time-to-live explicitly by calling the <tt>javax.jms.MessageProducer.getTimeToLive()</tt> method.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 64-bit integer.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FlowInterval")) {
         var3 = "getFlowInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFlowInterval";
         }

         var2 = new PropertyDescriptor("FlowInterval", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("FlowInterval", var2);
         var2.setValue("description", "<p>The number of seconds (between 0 and a positive 32-bit integer) when a producer adjusts its flow from the Flow Maximum number of messages to the Flow Minimum amount, or vice versa.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FlowMaximum")) {
         var3 = "getFlowMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFlowMaximum";
         }

         var2 = new PropertyDescriptor("FlowMaximum", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("FlowMaximum", var2);
         var2.setValue("description", "<p>The maximum number of messages-per-second (between 0 and a positive 32-bit integer) allowed for a producer that is experiencing a threshold condition on the JMS server or queue/topic destination. When a producer is flow controlled it will never be allowed to go faster than this number of messages per second.</p>  <p>If a producer is not currently limiting its flow when a threshold condition is reached, the initial flow limit for that producer is set to FlowMaximum. If a producer is already limiting its flow when a threshold condition is reached (the flow limit is less than FlowMaximum), then the producer will continue at its current flow limit until the next time the flow is evaluated.</p>  <p><i>Note:</i> Once a threshold condition has subsided, the producer is not permitted to ignore its flow limit. If its flow limit is less than the FlowMaximum, then the producer must gradually increase its flow to the FlowMaximum each time the flow is evaluated. When the producer finally reaches the FlowMaximum, it can then ignore its flow limit and send without limiting its flow.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(500));
         var2.setValue("secureValue", new Integer(500));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FlowMinimum")) {
         var3 = "getFlowMinimum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFlowMinimum";
         }

         var2 = new PropertyDescriptor("FlowMinimum", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("FlowMinimum", var2);
         var2.setValue("description", "<p>The minimum number of messages-per-second allowed for a producer that is experiencing a threshold condition. That is, WebLogic JMS will not further slow down a producer whose message flow limit is at its Flow Minimum.</p>  <p><b>Range of Values</b>: Between 0 and a positive 32-bit integer.</p>  <p><i>Note:</i> When a producer is flow controlled it will never be required to go slower than FlowMinimum messages per second.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(50));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FlowSteps")) {
         var3 = "getFlowSteps";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFlowSteps";
         }

         var2 = new PropertyDescriptor("FlowSteps", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("FlowSteps", var2);
         var2.setValue("description", "<p>The number of steps (between 1 and a positive 32-bit integer) used when a producer is adjusting its flow from the Flow Maximum amount of messages to the Flow Minimum amount, or vice versa.</p>  <p>Also, the movement (i.e., the rate of adjustment) is calculated by dividing the difference between the Flow Maximum and the Flow Minimum into steps. At each Flow Step, the flow is adjusted upward or downward, as necessary, based on the current conditions, as follows:</p>  <ul> <li> <p>The downward movement (the decay) is geometric over the specified period of time (Flow Interval) and according to the specified number of Flow Steps. (For example, 100, 50, 25, 12.5)</p> </li>  <li> <p>The movement upward is linear. The difference is simply divided by the number of steps.</p> </li> </ul>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIName";
         }

         var2 = new PropertyDescriptor("JNDIName", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "<p>The JNDI name used to look up this JMS connection factory within the JNDI namespace.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MessagesMaximum")) {
         var3 = "getMessagesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesMaximum";
         }

         var2 = new PropertyDescriptor("MessagesMaximum", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("MessagesMaximum", var2);
         var2.setValue("description", "<p>The maximum number of messages that may exist for an asynchronous session and that have not yet been passed to the message listener. A value of <tt>-1</tt> indicates that there is no limit on the number of messages. This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory. (For topic subscribers that use the multicast extension, also see the Overrun Policy field.)</p>  <p>When the number of messages reaches the MessagesMaximum value:</p>  <ul> <li> <p>For multicast sessions, new messages are discarded according the policy specified by the OverrunPolicy value and a DataOverrunException is thrown.</p> </li>  <li> <p>For non-multicast sessions, new messages are flow-controlled, or retained on the server until the application can accommodate the messages.</p> </li> </ul>  <p><b>Range of Values:</b> Between -1 and a positive 32-bit integer.</p>  <p><i>Note:</i> For multicast sessions, when a connection is stopped, messages will continue to be delivered, but only until the MessagesMaximum value is reached. Once this value is reached, messages will be discarded based on the Overrun policy.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSConnectionFactoryMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("Notes", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("Notes", var2);
         var2.setValue("description", "<p>Optional information that you can include to describe this configuration.</p>  <p>WebLogic Server saves this note in the domain's configuration file (<code>config.xml</code>) as XML PCDATA. All left angle brackets (&lt;) are converted to the XML entity <code>&amp;lt;</code>. Carriage returns/line feeds are preserved.</p>  <dl> <dt>Note:</dt>  <dd> <p>If you create or edit a note from the Administration Console, the Administration Console does not preserve carriage returns/line feeds.</p> </dd> </dl> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OverrunPolicy")) {
         var3 = "getOverrunPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOverrunPolicy";
         }

         var2 = new PropertyDescriptor("OverrunPolicy", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("OverrunPolicy", var2);
         var2.setValue("description", "<p>Overrun policy for topic subscribers that use the multicast extension. The policy to use when the number of outstanding multicast messages reaches the value specified in the Messages Maximum field and some messages must be discarded. <tt>Keep New</tt> indicates that the most recent messages are given priority over the oldest messages, and the oldest messages are discarded, as needed. <tt>Keep Old</tt> indicates that the oldest messages are given priority over the most recent messages, and the most recent messages are discarded, as needed. Message age is defined by the order of receipt, not by the JMSTimestamp value.</p>  <p>The policy to use when the number of outstanding multicast messages reaches the value specified in MessagesMaximum and some messages must be discarded.</p>  <ul> <li> <p>If set to <code>Keep New</code>, the most recent messages are given priority over the oldest messages, and the oldest messages are discarded, as needed.</p> </li>  <li> <p>If set to <code>Keep Old</code>, the oldest messages are given priority over the most recent messages, and the most recent messages are discarded, as needed.</p> </li> </ul>  <p>Message age is defined by the order of receipt, not by the JMSTimestamp value.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p> ");
         setPropertyDescriptorDefault(var2, "KeepOld");
         var2.setValue("legalValues", new Object[]{"KeepOld", "KeepNew"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SendTimeout")) {
         var3 = "getSendTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSendTimeout";
         }

         var2 = new PropertyDescriptor("SendTimeout", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("SendTimeout", var2);
         var2.setValue("description", "<p>The maximum number of milliseconds that a sender will wait for sufficient space (quota) on a JMS server and destination to accommodate the message being sent. Also see the Blocking Send Policy field on the JMS Server &gt; Configuration &gt; Thresholds &amp; Quotas tab.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 64-bit integer.</p>  <p>The default time is <code>10</code> milliseconds. A value of <code>0</code> indicates that the sender does not want to wait for space.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections or their producers. It only affects new connections made with this connection factory. Producers inherit the setting from the connection factory used to create their session and connection. The value can then be overridden at run time by setting the value on the producer.</p> ");
         setPropertyDescriptorDefault(var2, new Long(10L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransactionTimeout")) {
         var3 = "getTransactionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransactionTimeout";
         }

         var2 = new PropertyDescriptor("TransactionTimeout", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("TransactionTimeout", var2);
         var2.setValue("description", "<p>The timeout seconds for all transactions on transacted sessions created with this JMS connection factory. This setting has no effect on the transaction-timeout for JTA user transactions.</p>  <p><b>Range of Values:</b> Between 0 and a positive 32-bit integer.</p>  <p><i>Note:</i> This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p>  <p><i>Note:</i> If a transacted session is still active after the timeout has elapsed, the transaction is rolled back. A value of 0 indicates that the default value will be used. If you have long-running transactions, you might want to adjust the value of this value to allow transactions to complete.</p> ");
         setPropertyDescriptorDefault(var2, new Long(3600L));
         var2.setValue("legalMax", new Long(2147483647L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FlowControlEnabled")) {
         var3 = "isFlowControlEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFlowControlEnabled";
         }

         var2 = new PropertyDescriptor("FlowControlEnabled", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("FlowControlEnabled", var2);
         var2.setValue("description", "<p>Indicates whether flow control is enabled for a producer created using this connection factory. If true, the associated message producers will be slowed down if the JMS server reaches Bytes/MessagesThresholdHigh.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LoadBalancingEnabled")) {
         var3 = "isLoadBalancingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoadBalancingEnabled";
         }

         var2 = new PropertyDescriptor("LoadBalancingEnabled", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("LoadBalancingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether non-anonymous producers sending to a distributed destination are load balanced on a per-send basis.</p>  <ul> <li> <p>If true, the associated message producers will be load balanced on every <tt>send()</tt> or <tt>publish()</tt>.</p> </li>  <li> <p>If false, the associated message producers will be load balanced on the first <tt>send()</tt> or <tt>publish()</tt>.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerAffinityEnabled")) {
         var3 = "isServerAffinityEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerAffinityEnabled";
         }

         var2 = new PropertyDescriptor("ServerAffinityEnabled", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("ServerAffinityEnabled", var2);
         var2.setValue("description", "<p>Indicates whether a server that is load balancing consumers or producers across multiple physical destinations in a distributed destination set will first attempt to load balance across any other physical destinations that are also running on the same WebLogic Server instance.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UserTransactionsEnabled")) {
         var3 = "isUserTransactionsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserTransactionsEnabled";
         }

         var2 = new PropertyDescriptor("UserTransactionsEnabled", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("UserTransactionsEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a connection factory creates sessions that are JTA aware. If true, the associated message producers and message consumers look into the running thread for a transaction context. Otherwise, the current JTA transaction will be ignored. This value is dynamic. It can be changed at any time. However, changing the value does not affect existing connections. It only affects new connections made with this connection factory.</p>  <p><i>Note:</i> This value is now deprecated. If the XAServerEnabled value is set, then this value is automatically set as well.</p>  <p><i>Note:</i> Transacted sessions ignore the current threads transaction context in favor of their own internal transaction, regardless of the setting. This setting only affects non-transacted sessions.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link weblogic.management.configuration.JMSConnectionFactoryMBean#XAConnectionFactoryEnabled} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("XAConnectionFactoryEnabled")) {
         var3 = "isXAConnectionFactoryEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXAConnectionFactoryEnabled";
         }

         var2 = new PropertyDescriptor("XAConnectionFactoryEnabled", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("XAConnectionFactoryEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a XA queue or XA topic connection factory is returned, instead of a queue or topic connection factory. An XA factory is required for JMS applications to use JTA user-transactions, but is not required for transacted sessions. All connections created from an XA factory, whether they are XAConnections or plain Connections, become JTA user-transaction-aware.</p>  <p>In addition, this value indicates whether or not a connection factory creates sessions that are JTA aware. If true, the associated message producers and message consumers look into the running thread for a transaction context. Otherwise, the current JTA transaction will be ignored.</p>  <p><i>Note:</i> Transacted sessions ignore the current threads transaction context in favor of their own internal transaction, regardless of the setting. This setting only affects non-transacted sessions.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XAServerEnabled")) {
         var3 = "isXAServerEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXAServerEnabled";
         }

         var2 = new PropertyDescriptor("XAServerEnabled", JMSConnectionFactoryMBean.class, var3, var4);
         var1.put("XAServerEnabled", var2);
         var2.setValue("description", "<p>Indicates whether an XA connection factory will be returned instead of a standard connection factory.</p>  <p><i>Note:</i> This value is deprecated. It is now possible to use a single XA-enabled connection factory for both XA and non-XA purposes.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link weblogic.management.configuration.JMSConnectionFactoryMBean#XAConnectionFactoryEnabled} ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSConnectionFactoryMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = JMSConnectionFactoryMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSConnectionFactoryMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = JMSConnectionFactoryMBean.class.getMethod("restoreDefaultValue", String.class);
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
