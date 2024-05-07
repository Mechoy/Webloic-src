package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SAFAgentMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SAFAgentMBean.class;

   public SAFAgentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SAFAgentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SAFAgentMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a Store-and-Forward (SAF) agent. A SAF sending agent takes care of storing messages to a persistent storage, forwarding messages to the receiving side, and re-transmitting messages when acknowledgements do not come back in time. A SAF receiving agent takes care of detecting and eliminating duplicate messages sent by the receiving agent, and deliver messages to the final endpoint.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SAFAgentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AcknowledgeInterval")) {
         var3 = "getAcknowledgeInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcknowledgeInterval";
         }

         var2 = new PropertyDescriptor("AcknowledgeInterval", SAFAgentMBean.class, var3, var4);
         var1.put("AcknowledgeInterval", var2);
         var2.setValue("description", "<p>The maximum interval between two successive acknowledgements sent by the receiving side.</p>  <ul> <li> <p>Applies only to agents with receiving capability.</p> </li>  <li> <p>A value of <code>-1</code> specifies that there is no time limit between successive acknowledgement.</p> </li>  <li> <p>Updating <code>AcknowlegeInterval</code> causes connections starting after the update to use the new value.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesMaximum")) {
         var3 = "getBytesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesMaximum";
         }

         var2 = new PropertyDescriptor("BytesMaximum", SAFAgentMBean.class, var3, var4);
         var1.put("BytesMaximum", var2);
         var2.setValue("description", "<p>The maximum bytes quota (total amount of bytes) that can be stored in this SAF agent.</p>  <ul> <li> <p>The default value of <code>-1</code> specifies that there is no WebLogic-imposed limit on the number of bytes that can be stored. However, excessive bytes volume can cause memory saturation, so this value should correspond to the total amount of available system memory relative to the rest of your application load.</p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>BytesMaximum</code> affects only new requests.</p> </li> </ul>  <p><b>Range of Values:</b> &gt;= BytesThresholdHigh</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesThresholdHigh")) {
         var3 = "getBytesThresholdHigh";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesThresholdHigh";
         }

         var2 = new PropertyDescriptor("BytesThresholdHigh", SAFAgentMBean.class, var3, var4);
         var1.put("BytesThresholdHigh", var2);
         var2.setValue("description", "<p>The upper threshold value that triggers events based on the number of bytes stored in the SAF agent.</p>  <ul> <li> <p>The default value of <code>-1</code>  disables the events for this SAF agent. </p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>BytesThresholdHigh</code> affects only new requests.</p> </li>  <li> <p>If the number of bytes exceeds this threshold, the triggered events are:</p>  <ul> <li><p><b>Log Messages</b>: A message is logged on the server indicating a high threshold condition.</p> </li>  <li><p><b>Flow Control</b>: The agent becomes armed and instructs producers to begin decreasing their message flow.</p> </li> </ul> </li> </ul>  <p><b>Range of Values:</b> &lt;= BytesMaximum; &gt;= BytesThresholdLow</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesThresholdLow")) {
         var3 = "getBytesThresholdLow";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesThresholdLow";
         }

         var2 = new PropertyDescriptor("BytesThresholdLow", SAFAgentMBean.class, var3, var4);
         var1.put("BytesThresholdLow", var2);
         var2.setValue("description", "<p>The lower threshold that triggers events based on the number of bytes stored in the SAF agent.</p>  <ul> <li> <p>The default value of <code>-1</code>  disables the events for this SAF agent. </p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>BytesThresholdLow</code> affects only new requests.</p> </li>  <li> <p>If the number of bytes falls below this threshold, the triggered events are:</p>  <ul> <li><p><b>Log Messages</b>: A message is logged on the server indicating that the threshold condition has cleared.</p> </li>  <li><p><b>Flow Control</b>: The agent becomes disarmed and instructs producers to begin increasing their message flow.</p> </li> </ul> </li> </ul>  <p><b>Range of Values:</b> &lt;= BytesThresholdHigh</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConversationIdleTimeMaximum")) {
         var3 = "getConversationIdleTimeMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConversationIdleTimeMaximum";
         }

         var2 = new PropertyDescriptor("ConversationIdleTimeMaximum", SAFAgentMBean.class, var3, var4);
         var1.put("ConversationIdleTimeMaximum", var2);
         var2.setValue("description", "<p>The maximum amount of time allowed before a sending side releases the resources used by a conversation.</p>  <ul> <li> <p>If there is no activity for a conversation for the specified amount of time, the sending agent releases all resources for that conversation and notifies the receiving agent to do the same.</p> </li>  <li> <p>A value of <code>0</code> specifies that resources for a conversation are not released unless the application explicitly closes the conversation.</p> </li>  <li> <p>Updating <code>ConversationIdleTimeMaximum</code> causes connections starting after the update to use the new value.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultRetryDelayBase")) {
         var3 = "getDefaultRetryDelayBase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRetryDelayBase";
         }

         var2 = new PropertyDescriptor("DefaultRetryDelayBase", SAFAgentMBean.class, var3, var4);
         var1.put("DefaultRetryDelayBase", var2);
         var2.setValue("description", "<p>The amount of time, in milliseconds, between the original delivery attempt and the first retry.</p>  <ul> <li> <p>If <code>RetryDelayMultiplier</code> is set to <code>1</code>, this defines the interval between any two successive retry attempts.</p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>DefaultRetryDelayBase</code> causes connections starting after the update to use the new value.</p> </li> </ul>  <p><b>Range of Values:</b> &lt;= RetryDelayMaximum if RetryDelayMultiplier is not 1.0. </p> ");
         setPropertyDescriptorDefault(var2, new Long(20000L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultRetryDelayMaximum")) {
         var3 = "getDefaultRetryDelayMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRetryDelayMaximum";
         }

         var2 = new PropertyDescriptor("DefaultRetryDelayMaximum", SAFAgentMBean.class, var3, var4);
         var1.put("DefaultRetryDelayMaximum", var2);
         var2.setValue("description", "<p>The maximum amount of time, in milliseconds, between two successive delivery retry attempts.</p>  <ul> <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>DefaultRetryDelayMaximum</code> causes connections starting after the update to use the new value.</p> </li> </ul>  <p><b>Range of Values:</b> &gt; = RetryDelayBase if RetryDelayMultiplier is not 1.0. </p> ");
         setPropertyDescriptorDefault(var2, new Long(180000L));
         var2.setValue("legalMin", new Long(1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultRetryDelayMultiplier")) {
         var3 = "getDefaultRetryDelayMultiplier";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRetryDelayMultiplier";
         }

         var2 = new PropertyDescriptor("DefaultRetryDelayMultiplier", SAFAgentMBean.class, var3, var4);
         var1.put("DefaultRetryDelayMultiplier", var2);
         var2.setValue("description", "<p>The factor used to multiply the previous delay time to calculate the next delay time to be used.</p>  <ul> <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>DefaultRetryDelayMuliplier</code> causes connections starting after the update to use the new value.</p> </li> </ul>  <p><b>Range of Values:</b> &gt;= 1.</p> ");
         setPropertyDescriptorDefault(var2, new Double(1.0));
         var2.setValue("legalMin", new Double(1.0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultTimeToLive")) {
         var3 = "getDefaultTimeToLive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultTimeToLive";
         }

         var2 = new PropertyDescriptor("DefaultTimeToLive", SAFAgentMBean.class, var3, var4);
         var1.put("DefaultTimeToLive", var2);
         var2.setValue("description", "<p>The default amount of time, in milliseconds, that the agent guarantees to reliably send messages.</p>  <ul> <li> <p>A value of 0 means the agent guarantees to reliably send messages for the duration of the conversation.</p> </li>  <li> <p>Updating <code>DefaultTimeToLive</code> causes conversations starting after the update to use the new value.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JMSSAFMessageLogFile")) {
         var3 = "getJMSSAFMessageLogFile";
         var4 = null;
         var2 = new PropertyDescriptor("JMSSAFMessageLogFile", SAFAgentMBean.class, var3, var4);
         var1.put("JMSSAFMessageLogFile", var2);
         var2.setValue("description", "The jms message log file configuration for this saf agent ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("MaximumMessageSize")) {
         var3 = "getMaximumMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaximumMessageSize";
         }

         var2 = new PropertyDescriptor("MaximumMessageSize", SAFAgentMBean.class, var3, var4);
         var1.put("MaximumMessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in individual messages on this SAF agent.</p>  <ul> <li> <p>The message size includes the message body, any user-defined properties, and the user-defined JMS header fields: <code>JMSCorrelationID</code> and <code>JMSType</code>.</p> </li>  <li> <p>Producers sending messages that exceed the configured maximum message size for the SAF agent will receive a <code>ResourceAllocationException</code>.</p> </li>  <li> <p>The maximum message size is only enforced for the initial production of a message. Messages that are redirected to an error destination or forwarded to a member of a distributed destination are not checked for size. For instance, if a destination and its corresponding error destination are configured with a maximum message size of 128K bytes and 64K bytes, respectively, a message of 96K bytes could be redirected to the error destination (even though it exceeds the 64K byte maximum), but a producer could not directly send the 96K byte message to the error destination.</p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>MaximumMessageSize</code> affects only incoming messages; stored messages are not affected.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("MessageBufferSize")) {
         var3 = "getMessageBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageBufferSize";
         }

         var2 = new PropertyDescriptor("MessageBufferSize", SAFAgentMBean.class, var3, var4);
         var1.put("MessageBufferSize", var2);
         var2.setValue("description", "<p>The amount of memory used to store message bodies in memory before they are paged out to disk.</p> <ul> <li> <p>A value of -1 (the default) specifies that the server will automatically determine a size based on the maximum heap size of the JVM. This default will be set to either one-third the maximum heap size, or 512 megabytes, whichever is smaller.</p> </li>  <li> <p>The larger the buffer, the more memory JMS will consume when many messages are waiting on queues or topics. Once the buffer is surpassed, JMS may write message bodies to the directory specified by PagingDirectory in an effort to reduce memory usage below this buffer.</p> </li>  <li> <p>Surpassing the buffer size does not stop the JMS server from accepting new messages. It is still possible to run out of memory if messages are arriving faster than they can be written to disk. Users with high messaging loads who wish to support the highest possible availability should consider setting a quota or setting a threshold and enabling flow control.</p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Paging is always supported. </p> </li>  <li> <p>Updating the memory size resets the message count so that only requests arriving after the update are charged against the new memory size.</p> </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPagingDirectory")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesMaximum")) {
         var3 = "getMessagesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesMaximum";
         }

         var2 = new PropertyDescriptor("MessagesMaximum", SAFAgentMBean.class, var3, var4);
         var1.put("MessagesMaximum", var2);
         var2.setValue("description", "<p>The maximum message quota (total amount of messages) that can be stored in this SAF agent.</p>  <ul> <li> <p>The default value of <code>-1</code> specifies that the server instance has no limit on the number of messages that can be stored. However, excessive message volume can cause memory saturation, so this value should correspond to the total amount of available system memory relative to the rest of your application load.</p> </li>  <li> <p>Applies only to sending agents.</p> </li>  <li> <p>Updating the maximum message quota resets the message count so that only requests arriving after the update are charged against the new quota.</p> </li> </ul>  <p><b>Range of Values:</b> &gt;= MessagesThresholdHigh</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesThresholdHigh")) {
         var3 = "getMessagesThresholdHigh";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesThresholdHigh";
         }

         var2 = new PropertyDescriptor("MessagesThresholdHigh", SAFAgentMBean.class, var3, var4);
         var1.put("MessagesThresholdHigh", var2);
         var2.setValue("description", "<p>The upper threshold that triggers events based on the number of messages stored in the SAF agent.</p>  <ul> <li> <p>The default value of <code>-1</code> disables the events for this SAF agent. </p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>MessagesThresholdHigh</code> affects only new requests.</p> </li>  <li> <p>If the number of messages exceeds this threshold, the triggered events are:</p>  <ul> <li><p><b>Log Messages</b>: A message is logged on the server indicating a high threshold condition.</p> </li>  <li><p><b>Flow Control</b>: The agent becomes armed and instructs producers to begin decreasing their message flow.</p> </li> </ul> </li> </ul>  <p><b>Range of Values:</b> &lt;= MessagesMaximum; &gt;= MessagesThresholdLow</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesThresholdLow")) {
         var3 = "getMessagesThresholdLow";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesThresholdLow";
         }

         var2 = new PropertyDescriptor("MessagesThresholdLow", SAFAgentMBean.class, var3, var4);
         var1.put("MessagesThresholdLow", var2);
         var2.setValue("description", "<p>The low threshold that triggers events based on the number of messages stored in the SAF agent.</p>  <ul> <li> <p>The default value of <code>-1</code> disables the events for this SAF agent. </p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li>  <li> <p>Updating <code>MessagesThresholdLow</code> affects only new requests.</p> </li>  <li> <p>If the number of messages falls below this threshold, the triggered events are:</p>  <ul> <li><p><b>Log Messages</b>: A message is logged on the server indicating that the threshold condition has cleared.</p> </li>  <li><p><b>Flow Control</b>: The agent becomes disarmed and instructs producers to begin increasing their message flow.</p> </li> </ul> </li> </ul>  <p><b>Range of Values:</b> &lt;= MessagesThresholdHigh.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SAFAgentMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PagingDirectory")) {
         var3 = "getPagingDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingDirectory";
         }

         var2 = new PropertyDescriptor("PagingDirectory", SAFAgentMBean.class, var3, var4);
         var1.put("PagingDirectory", var2);
         var2.setValue("description", "<p> Specifies where message bodies are written when the size of the message bodies in the JMS server exceeds the message buffer size. If unspecified, messages are written to the <code>tmp</code> directory in the host WebLogic Server instance's directory. For example, <code><em>domainName</em>/servers/<em>servername</em>/tmp</code>. </p>  <ul> <li> <p>For best performance, this directory should not be the same as the directory used by the SAF agent's persistent store.</p> </li>  <li> <p>Applies only to agents with sending capability.</p> </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMessageBufferSize")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ServiceType")) {
         var3 = "getServiceType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServiceType";
         }

         var2 = new PropertyDescriptor("ServiceType", SAFAgentMBean.class, var3, var4);
         var1.put("ServiceType", var2);
         var2.setValue("description", "<p>The type of service that this SAF agent provides. JMS requires only a Sending agent on the sending side for messages. Whereas, Web Services Reliable Messaging requires both a Sending and Receiving agent for messages.</p> <ul> <li><b>Sending-only</b> Configures an agent that stores messages in persistent storage, forwards messages to the receiving side, and re-transmits messages when acknowledgements do not come back in time. <li><b>Receiving-only</b> Configures an agent that detects and eliminates duplicate messages sent by a receiving agent, and delivers messages to the final destination. <li><b>Both</b> Configures an agent that has sending and receiving agent functionality. </ul> ");
         setPropertyDescriptorDefault(var2, "Both");
         var2.setValue("legalValues", new Object[]{"Both", "Sending-only", "Receiving-only"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Store")) {
         var3 = "getStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStore";
         }

         var2 = new PropertyDescriptor("Store", SAFAgentMBean.class, var3, var4);
         var1.put("Store", var2);
         var2.setValue("description", "<p>The persistent disk-based file store or JDBC-accessible database for the SAF agent.</p>  <p>If a store is not configured, the server's default store is used.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", SAFAgentMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WindowInterval")) {
         var3 = "getWindowInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWindowInterval";
         }

         var2 = new PropertyDescriptor("WindowInterval", SAFAgentMBean.class, var3, var4);
         var1.put("WindowInterval", var2);
         var2.setValue("description", "<p>The maximum amount of time, in milliseconds, that a JMS sending agent waits before forwarding messages in a single batch. For a distributed queue or topic, <code>WindowInterval</code> setting is ignored.</p>  <p>A sending agent waits to forward the message batch until either: (A) the source destination message count is greater than or equal to the configured <code>Window Size</code>; (B) it has waited a specified <code>Window Interval</code>, whichever comes first.</p> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WindowSize")) {
         var3 = "getWindowSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWindowSize";
         }

         var2 = new PropertyDescriptor("WindowSize", SAFAgentMBean.class, var3, var4);
         var1.put("WindowSize", var2);
         var2.setValue("description", "<p>For JMS messages, the number of messages in a batch. A sending agent waits to forward a message batch until the source destination message count is greater than or equal to this value. For a distributed queue or topic, <code>WindowSize</code> setting is ignored and always internally set to 1 message.</p> <p>For WSRM, the maximum number of unacknowledged messages allowed between a source destination and a target destination during a conversation.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ForwardingPausedAtStartup")) {
         var3 = "isForwardingPausedAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setForwardingPausedAtStartup";
         }

         var2 = new PropertyDescriptor("ForwardingPausedAtStartup", SAFAgentMBean.class, var3, var4);
         var1.put("ForwardingPausedAtStartup", var2);
         var2.setValue("description", "<p>Specifies whether the agent is paused for forwarding messages at the startup time.</p>  <p>When enabled, the sending agent forwards messages. Otherwise, the sending agent does not forward any messages.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("IncomingPausedAtStartup")) {
         var3 = "isIncomingPausedAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIncomingPausedAtStartup";
         }

         var2 = new PropertyDescriptor("IncomingPausedAtStartup", SAFAgentMBean.class, var3, var4);
         var1.put("IncomingPausedAtStartup", var2);
         var2.setValue("description", "<p>Specifies whether the agent is paused for incoming messages at startup time.</p>  <p>When enabled, the sending agent accepts new messages. Otherwise, the sending agent does not accept any new messages.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LoggingEnabled")) {
         var3 = "isLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoggingEnabled";
         }

         var2 = new PropertyDescriptor("LoggingEnabled", SAFAgentMBean.class, var3, var4);
         var1.put("LoggingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a message is logged in the server log file when a message fails to be forwarded.</p>  <ul> <li> <p>If selected, this applies to messages that fail to be forwarded and delivered to the final destination when there is no error handling in a SAF application or the application's error handling fails.</p> </li>  <li> <p>Applies only to agents with receiving capability.</p> </li>  <li> <p>Updating <code>LoggingEnabled</code> causes connections starting after the update to use the new value.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReceivingPausedAtStartup")) {
         var3 = "isReceivingPausedAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReceivingPausedAtStartup";
         }

         var2 = new PropertyDescriptor("ReceivingPausedAtStartup", SAFAgentMBean.class, var3, var4);
         var1.put("ReceivingPausedAtStartup", var2);
         var2.setValue("description", "<p>Specifies whether the agent is paused for receiving messages at the startup time.</p>  <p>When enabled, the sending agent receives messages. Otherwise, the sending agent does not receive any messages.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SAFAgentMBean.class.getMethod("addTarget", TargetMBean.class);
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

      var3 = SAFAgentMBean.class.getMethod("removeTarget", TargetMBean.class);
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
      Method var3 = SAFAgentMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = SAFAgentMBean.class.getMethod("restoreDefaultValue", String.class);
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
