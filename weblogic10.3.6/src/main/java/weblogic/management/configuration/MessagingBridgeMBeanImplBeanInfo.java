package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class MessagingBridgeMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MessagingBridgeMBean.class;

   public MessagingBridgeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MessagingBridgeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MessagingBridgeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean represents a WebLogic messaging bridge. A messaging bridge instance interoperates between separate implementations of WebLogic JMS or between WebLogic JMS and another messaging product.</p>  <p>For WebLogic JMS and third-party JMS products, a messaging bridge communicates with a configured source and target destinations using the resource adapters provided with WebLogic Server.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.MessagingBridgeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BatchInterval")) {
         var3 = "getBatchInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBatchInterval";
         }

         var2 = new PropertyDescriptor("BatchInterval", MessagingBridgeMBean.class, var3, var4);
         var1.put("BatchInterval", var2);
         var2.setValue("description", "<p>The maximum amount of time, in milliseconds, that a messaging bridge instance waits before sending a batch of messages in one transaction, regardless of whether the <code>Batch Size</code> has been reached or not.</p>  <ul> <li> <p>Only applies to a messaging bridge instance forwarding messages in synchronous mode and has a QOS (quality of service) that requires two-phase transactions.</p> </li>  <li> <p>The default value of <tt>-1</tt> indicates that the bridge instance waits until the number of messages reaches the <code>Batch Size</code> before it completes a transaction.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BatchSize")) {
         var3 = "getBatchSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBatchSize";
         }

         var2 = new PropertyDescriptor("BatchSize", MessagingBridgeMBean.class, var3, var4);
         var1.put("BatchSize", var2);
         var2.setValue("description", "<p>The number of messages that are processed within one transaction.</p>  <p><code>Batch Size</code> only applies to a messaging bridge instance forwarding messages in synchronous mode and has a QOS (quality of service) that requires two-phase transactions.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdleTimeMaximum")) {
         var3 = "getIdleTimeMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdleTimeMaximum";
         }

         var2 = new PropertyDescriptor("IdleTimeMaximum", MessagingBridgeMBean.class, var3, var4);
         var1.put("IdleTimeMaximum", var2);
         var2.setValue("description", "<p>The maximum amount of time, in seconds, that a messaging bridge instance remains idle.</p>  <ul> <li> <p>In <i>asynchronous</i> mode, this is the longest amount of time a messaging bridge instance stays idle before it checks the sanity of its connection to the source.</p> </li>  <li> <p>In <i>synchronous</i> mode, this is the amount of time the messaging bridge can block on a receive call if no transaction is involved.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PreserveMsgProperty")) {
         var3 = "getPreserveMsgProperty";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPreserveMsgProperty";
         }

         var2 = new PropertyDescriptor("PreserveMsgProperty", MessagingBridgeMBean.class, var3, var4);
         var1.put("PreserveMsgProperty", var2);
         var2.setValue("description", "<p>Specifies if message properties are preserved when messages are forwarded by a bridge instance.</p>  <p>If the target bridge destination is on wls 9.0 or higher, the following message properties are preserved:</p>  <ul> <li> <p><code>message ID</code></p> </li>  <li> <p><code>message timestamp</code></p> </li>  <li> <p><code>user ID</code></p> </li>  <li> <p><code>delivery mode</code></p> </li>  <li> <p><code>priority</code></p> </li>  <li> <p><code>expiration time</code></p> </li>  <li> <p><code>redelivery limit</code></p> </li>  <li> <p><code>unit of order name</code></p> </li> </ul>  <p>If the target bridge destination is on wls between 6.0 and 8.1 or on a foreign JMS server, the following message properties are preserved:</p>  <ul> <li> <p><code>delivery mode</code></p> </li>  <li> <p><code>priority</code></p> </li>  <li> <p><code>expiration time</code></p> </li> </ul>  <p>If the target bridge destination is on wls 5.1 or older, no properties are preserved.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("QualityOfService")) {
         var3 = "getQualityOfService";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setQualityOfService";
         }

         var2 = new PropertyDescriptor("QualityOfService", MessagingBridgeMBean.class, var3, var4);
         var1.put("QualityOfService", var2);
         var2.setValue("description", "<p>The QOS (quality of service) for this messaging bridge instance.</p>  <ul> <li><p><tt>Exactly-once</tt>: Each message in the source destination is transferred to the target exactly once. This is the highest QOS a messaging bridge instance can offer.</p> </li>  <li><p><tt>Atmost-once</tt>: Each message in the source is transferred to the target only once with the possibility of being lost during the forwarding.</p> </li>  <li><p><tt>Duplicate-okay</tt>: Messages in the source destination are transferred to the target (none are lost) but some may appear in the target more than once.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "Exactly-once");
         var2.setValue("secureValue", "Exactly-once");
         var2.setValue("legalValues", new Object[]{"Exactly-once", "Atmost-once", "Duplicate-okay"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ReconnectDelayIncrease")) {
         var3 = "getReconnectDelayIncrease";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReconnectDelayIncrease";
         }

         var2 = new PropertyDescriptor("ReconnectDelayIncrease", MessagingBridgeMBean.class, var3, var4);
         var1.put("ReconnectDelayIncrease", var2);
         var2.setValue("description", "<p>The incremental delay time, in seconds, that a messaging bridge instance increases its waiting time between one failed reconnection attempt and the next retry.</p>  <ul> <li> <p>Use with <tt>ReconnectDelayMinimum</tt> and <tt>ReconnectDelayMaximum</tt>. After the first failure to connect to a destination, the bridge instance waits for the number of seconds defined by <tt>ReconnectDelayMinimum</tt>. Each time a reconnect attempt fails, the bridge instance increases its waiting time by the number of seconds defined by <tt>ReconnectDelayIncrease</tt>. The maximum delay time is defined by <tt>ReconnectDelayMaximum</tt>. Once the waiting time is increased to the maximum value, the bridge instance stops increase its waiting time. Once the bridge instance successfully connects to the destination, the bridge instance resets its waiting time to the initial value defined by <tt>ReconnectDelayMinimum</tt>.</p> </li> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReconnectDelayMaximum")) {
         var3 = "getReconnectDelayMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReconnectDelayMaximum";
         }

         var2 = new PropertyDescriptor("ReconnectDelayMaximum", MessagingBridgeMBean.class, var3, var4);
         var1.put("ReconnectDelayMaximum", var2);
         var2.setValue("description", "<p>The longest time, in seconds, that a messaging bridge instance waits between one failed attempt to connect to the source or target, and the next retry.</p>  <ul> <li> <p>Use with <tt>ReconnectDelayMinimum</tt> and <tt>ReconnectDelayIncrease</tt>. After the first failure to connect to a destination, a bridge instance waits for the number of seconds defined by <tt>ReconnectDelayMinimum</tt>. Each time a reconnect attempt fails, the bridge instance increases its waiting time by the number of seconds defined by <tt>ReconnectDelayIncrease</tt>. The maximum delay time is defined by <tt>ReconnectDelayMaximum</tt>. Once the waiting time is increased to the maximum value, the bridge instance stops increase its waiting time. Once the bridge instance successfully connects to the destination, the bridge instance resets its waiting time to the initial value defined by <tt>ReconnectDelayMinimum</tt>.</p> </li> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReconnectDelayMinimum")) {
         var3 = "getReconnectDelayMinimum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReconnectDelayMinimum";
         }

         var2 = new PropertyDescriptor("ReconnectDelayMinimum", MessagingBridgeMBean.class, var3, var4);
         var1.put("ReconnectDelayMinimum", var2);
         var2.setValue("description", "<p>The minimum amount of time, in seconds, that a messaging bridge instance waits before it tries to reconnect to the source or target destination after a failure.</p>  <ul> <li> <p>Use with <tt>ReconnectDelayMaximum</tt> and <tt>ReconnectDelayIncrease</tt>. After the first failure to connect to a destination, the bridge instance waits for the number of seconds defined by <tt>ReconnectDelayMinimum</tt>. Each time a reconnect attempt fails, the bridge instance increases its waiting time by the number of seconds defined by <tt>ReconnectDelayIncrease</tt>. The maximum delay time is defined by <tt>ReconnectDelayMaximum</tt>. Once the waiting time is increased to the maximum value, the bridge instance stops increase its waiting time. Once the bridge instance successfully connects to the destination, the bridge instance resets its waiting time to the initial value defined by <tt>ReconnectDelayMinimum</tt>.</p> </li> ");
         setPropertyDescriptorDefault(var2, new Integer(15));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Selector")) {
         var3 = "getSelector";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSelector";
         }

         var2 = new PropertyDescriptor("Selector", MessagingBridgeMBean.class, var3, var4);
         var1.put("Selector", var2);
         var2.setValue("description", "<p>The filter for messages that are sent across the messaging bridge instance.</p>  <p>Only messages that match the selection criteria are sent across the messaging bridge:</p>  <ul> <li> <p>For queues, messages that do not match the selection criteria are left behind and accumulate in the queue.</p> </li>  <li> <p>For topics, messages that do not match the connection criteria are dropped.</p> </li> </ul> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SourceDestination")) {
         var3 = "getSourceDestination";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourceDestination";
         }

         var2 = new PropertyDescriptor("SourceDestination", MessagingBridgeMBean.class, var3, var4);
         var1.put("SourceDestination", var2);
         var2.setValue("description", "<p>The source destination from which this messaging bridge instance reads messages.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("TargetDestination")) {
         var3 = "getTargetDestination";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetDestination";
         }

         var2 = new PropertyDescriptor("TargetDestination", MessagingBridgeMBean.class, var3, var4);
         var1.put("TargetDestination", var2);
         var2.setValue("description", "<p>The target destination where a messaging bridge instance sends the messages it receives from the source destination.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("TransactionTimeout")) {
         var3 = "getTransactionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransactionTimeout";
         }

         var2 = new PropertyDescriptor("TransactionTimeout", MessagingBridgeMBean.class, var3, var4);
         var1.put("TransactionTimeout", var2);
         var2.setValue("description", "<p>The amount of time, in seconds, that the transaction manager waits for each transaction before timing it out.</p>  <ul> <li> <p>Transaction timeouts are used when the QOS (quality of service) for a messaging bridge instance requires transactions.</p> </li>  <li> <p>If a bridge is configured with <i>Exactly-once</i> QOS, the receiving and sending is completed in one transaction.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AsyncEnabled")) {
         var3 = "isAsyncEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAsyncEnabled";
         }

         var2 = new PropertyDescriptor("AsyncEnabled", MessagingBridgeMBean.class, var3, var4);
         var1.put("AsyncEnabled", var2);
         var2.setValue("description", "<p>Specifies if a messaging bridge instance forwards in asynchronous messaging mode.</p>  <p>AsyncEnabled only applies to messaging bridge instances whose source destination supports asynchronous receiving. Messaging bridges instances that forward in asynchronous mode are driven by the source destination. A messaging bridge instance listens for messages and forwards them as they arrive. When <code>AsyncEnabled</code> is not selected, a bridge instance is forced to work in synchronous mode, even if the source supports asynchronous receiving.</p>  <p><i><b>Note:</b></i> For a messaging bridge instance with a QOS of <i>Exactly-once</i> to work in asynchronous mode, the source destination has to support the <tt>MDBTransaction</tt> interface. Otherwise, the bridge automatically switches to synchronous mode if it detects that <tt>MDBTransaction</tt> is not supported by the source destination.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DurabilityEnabled")) {
         var3 = "isDurabilityEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDurabilityEnabled";
         }

         var2 = new PropertyDescriptor("DurabilityEnabled", MessagingBridgeMBean.class, var3, var4);
         var1.put("DurabilityEnabled", var2);
         var2.setValue("description", "<p>Specifies whether or not the messaging bridge allows durable messages.</p>  <p>When enabled and the source destination is a JMS topic, a messaging bridge instance uses a durable subscription to ensure that no messages are lost in the event of a failure. <code>DurabilityEnabled</code> ignored if the source destination is a JMS queue.</p>  <ul> <li> <p>When enabled and the source destination uses durable subscriptions, the source JMS implementation saves messages that are sent when a messaging bridge instance is not running. When the bridge instance is restarted, these messages are forwarded to the target destination. The administrator can choose not to be durable.</p> </li>  <li> <p>When not enabled, messages that are sent to the source JMS implementation while the bridge instance is down cannot be forwarded to the target destination.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("QOSDegradationAllowed")) {
         var3 = "isQOSDegradationAllowed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setQOSDegradationAllowed";
         }

         var2 = new PropertyDescriptor("QOSDegradationAllowed", MessagingBridgeMBean.class, var3, var4);
         var1.put("QOSDegradationAllowed", var2);
         var2.setValue("description", "<p>Specifies if this messaging bridge instance allows the degradation of its QOS (quality of service) when the configured QOS is not available.</p>  <ul> <li> <p>When enabled, the messaging bridge instance degrades the QOS when the configured QOS is not available. If the QOS is degraded, a log message is delivered to the WebLogic startup window or log file.</p> </li>  <li> <p>When not enabled, if messaging bridge instance cannot satisfy the quality of service requested, an error results and the messaging bridge instance does not start.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Started")) {
         var3 = "isStarted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStarted";
         }

         var2 = new PropertyDescriptor("Started", MessagingBridgeMBean.class, var3, var4);
         var1.put("Started", var2);
         var2.setValue("description", "<p>Specifies the initial operating state of a targeted messaging bridge instance.</p>  <ul> <li> <p>If enabled, the messaging bridge instance forwards messages (running).</p> </li>  <li> <p>If not enabled, the messaging bridge instance does not forward messages (temporarily stopped).</p> </li> </ul>  <p>After a messaging bridge has started forwarding messages (running), use <code>Started</code> to temporarily suspend an active messaging bridge instance or restart an stopped messaging bridge instance.</p>  <ul> <li> <p>Select the <code>Started</code> checkbox to start a messaging bridge instance that has been temporarily stopped.</p> </li>  <li> <p>Clear the <code>Started</code> checkbox to temporarily stop a messaging bridge instance that was running.</p> </li>  <li> <p>This value does not indicate the run-time state of a messaging bridge instance.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
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
