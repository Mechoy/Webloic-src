package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSServerMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSServerMBean.class;

   public JMSServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSServerMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS server. A JMS server manages connections and message requests on behalf of clients.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BlockingSendPolicy")) {
         var3 = "getBlockingSendPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBlockingSendPolicy";
         }

         var2 = new PropertyDescriptor("BlockingSendPolicy", JMSServerMBean.class, var3, var4);
         var1.put("BlockingSendPolicy", var2);
         var2.setValue("description", "<p>Determines whether the JMS server delivers smaller messages before larger ones when a destination has exceeded its maximum number of messages. <code>FIFO</code> prevents the JMS server from delivering smaller messages when larger ones are already waiting for space. <code>Preemptive</code> allows smaller send requests to preempt previous larger ones when there is sufficient space for smaller messages on the destination.</p>  <p>This policy is defined only for the JMS server; it cannot be set on individual destinations.</p>  <p>Additional information on the <code>FIFO</code> and <code>Preemptive</code> policies is provided below.</p> <p> <code>FIFO</code> (first in, first out) indicates that all send requests for the same destination are queued up one behind the other until space is available. No send request is permitted to successfully complete if there is another send request waiting for space before it. When space is limited, the FIFO policy prevents the starvation of larger requests because smaller requests cannot continuously use the remaining available space. Smaller requests are delayed, though not starved, until the larger request can be completed. When space does become available, requests are considered in the order in which they were made. If there is sufficient space for a given request, then that request is completed and the next request is considered. If there is insufficient space for a given request, then no further requests are considered until sufficient space becomes available for the current request to complete.</p> <p> <code>Preemptive</code> indicates that a send operation can preempt other blocking send operations if space is available. That is, if there is sufficient space for the current request, then that space is used even if there are other requests waiting for space. When space is limited, the Preemptive policy can result in the starvation of larger requests. For example, if there is insufficient available space for a large request, then it is queued up behind other existing requests. When space does become available, all requests are considered in the order in which they were originally made. If there is sufficient space for a given request, then that request is allowed to continue and the next request is considered. If there is insufficient space for a given request, then that request is skipped and the next request is considered.</p> ");
         setPropertyDescriptorDefault(var2, "FIFO");
         var2.setValue("legalValues", new Object[]{"FIFO", "Preemptive"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesMaximum")) {
         var3 = "getBytesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesMaximum";
         }

         var2 = new PropertyDescriptor("BytesMaximum", JMSServerMBean.class, var3, var4);
         var1.put("BytesMaximum", var2);
         var2.setValue("description", "<p>The maximum number of bytes that can be stored in this JMS server. A value of <code>-1</code> removes any WebLogic Server limits.</p>  <p>Because excessive bytes volume can cause memory saturation, Oracle recommends that this maximum corresponds to the total amount of system memory available after accounting for the rest of your application load.</p>  <p><b>Range of Values:</b> &gt;= BytesThresholdHigh</p> ");
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

         var2 = new PropertyDescriptor("BytesThresholdHigh", JMSServerMBean.class, var3, var4);
         var1.put("BytesThresholdHigh", var2);
         var2.setValue("description", "<p>The upper threshold (number of bytes stored in this JMS server) that triggers flow control and logging events. A value of <code>-1</code> disables the events for this JMS server.</p>  <p>The triggered events are:</p> <ul> <li> <code>Log Messages</code> - A message is logged on the server indicating a high threshold condition. </li> <li> <code>Flow Control</code> - If flow control is enabled, the JMS server becomes armed and instructs producers to begin decreasing their message flow. </li> </ul>  <p><b>Range of Values:</b> &lt;= BytesMaximum; &gt;= BytesThresholdLow</p> ");
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

         var2 = new PropertyDescriptor("BytesThresholdLow", JMSServerMBean.class, var3, var4);
         var1.put("BytesThresholdLow", var2);
         var2.setValue("description", "<p>The lower threshold (number of bytes stored in this JMS server) that triggers flow control and logging events. A value of <code>-1</code> disables the events for this JMS server.</p>  <p>If the number of bytes falls below this threshold, the triggered events are:</p> <ul> <li> <code>Log Messages</code> - A message is logged on the server indicating that the threshold condition has cleared. </li> <li> <code>Flow Control</code> - If flow control is enabled, the JMS server becomes disarmed and instructs producers to begin increasing their message flow. </li> </ul>  <p><b>Range of Values:</b> &lt;= BytesThresholdHigh</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("ConsumptionPausedAtStartup")) {
         var3 = "getConsumptionPausedAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsumptionPausedAtStartup";
         }

         var2 = new PropertyDescriptor("ConsumptionPausedAtStartup", JMSServerMBean.class, var3, var4);
         var1.put("ConsumptionPausedAtStartup", var2);
         var2.setValue("description", "<p>Indicates whether consumption is paused at startup on destinations targeted to this JMS server at startup. A destination cannot receive any new messages while it is paused.</p>  <p>When the value is set to <code>true</code>, then immediately after the host server instance is booted, then this JMS server and its targeted destinations are modified such that they are in a \"consumption paused\" state, which prevents any message consuming activity on those destinations.</p>  <p>To allow normal message consumption on the destinations, later you will have to change the state of this JMS server to a \"consumption enabled\" state by setting this value to <code>false</code>, and then either redeploy the JMS server or reboot the hosting server instance.</p>  <p>When the value is set to <code>default</code>, then the Consumption Paused At Startup is determined based on the corresponding setting on the individual destination.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JMSServerRuntimeMBean#resumeConsumption"), BeanInfoHelper.encodeEntities("weblogic.management.runtime.JMSDestinationRuntimeMBean#resumeConsumption")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("Destinations")) {
         var3 = "getDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("Destinations", JMSServerMBean.class, var3, var4);
         var1.put("Destinations", var2);
         var2.setValue("description", "<p>All defined destinations and their associated JNDI names.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addDestination");
         var2.setValue("remover", "removeDestination");
         var2.setValue("deprecated", "9.0.0.0 Replaced with the functionality of JMS modules. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("ExpirationScanInterval")) {
         var3 = "getExpirationScanInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExpirationScanInterval";
         }

         var2 = new PropertyDescriptor("ExpirationScanInterval", JMSServerMBean.class, var3, var4);
         var1.put("ExpirationScanInterval", var2);
         var2.setValue("description", "<p>The number of seconds between this JMS server's cycles of scanning local destinations for expired messages. A value of <code>0</code> disables active scanning. A very large scan interval effectively disables active scanning.</p>  <p>With scanning disabled, users still do not receive expired messages and any expired messages that are discovered by other system activities are removed. However, expired messages sitting in idle destinations (such as an inactive queue or disconnected durable subscriber) are not removed and continue to consume system resources.</p>  <p>The scanning cycle for expired messages occurs as follows:</p> <ul> <li> After the specified waiting period, the JMS server devotes a separate thread to scan all of its local destinations for expired messages. </li> <li> After the scanning is completed, all located expired messages are processed according to the specified Expiration Policy on the destination (Discard, Log, or Redirect). </li> <li> The entire process repeats after another specified waiting period. </li> </ul>  <p><b>Note:</b> Because a new scan does not start until the current one is finished and until the specified waiting period ends, an expired message could still remain in the system for the maximum scan waiting period plus the amount of time it takes to perform the scan.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InsertionPausedAtStartup")) {
         var3 = "getInsertionPausedAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInsertionPausedAtStartup";
         }

         var2 = new PropertyDescriptor("InsertionPausedAtStartup", JMSServerMBean.class, var3, var4);
         var1.put("InsertionPausedAtStartup", var2);
         var2.setValue("description", "<p>Indicates whether insertion is paused at startup on destinations targeted to this JMS server. A destination cannot receive any new messages while it is paused.</p>  <p>When the value is set to <code>true</code>, then immediately after the host server instance is booted, then this JMS server and its targeted destinations are modified such that they are in a \"insertion paused\" state, which results preventing messages that are result of the \"in-flight\" work completion to arrive on those destinations.</p>  <p><b>Note:</b> For a detailed definition of \"in-flight\" work/messages, see weblogic.management.runtime.JMSServerRuntimeMBean#resumeInsertion and weblogic.management.runtime.JMSDestinationRuntime#resumeInsertion</p>  <p>To allow in-flight work messages to appear on the destinations, later you will have to change the state of this JMS server to an \"insertion enabled\" state by setting this value to <code>false</code>, and then either redeploy the JMS server or reboot the hosting server instance.</p>  <p>When the value is set to <code>default</code>, then the Insertion Paused At Startup is determined based on the corresponding setting on the individual destination.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JMSServerRuntimeMBean#resumeInsertion"), BeanInfoHelper.encodeEntities("weblogic.management.runtime.JMSDestinationRuntimeMBean#resumeInsertion")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JMSMessageLogFile")) {
         var3 = "getJMSMessageLogFile";
         var4 = null;
         var2 = new PropertyDescriptor("JMSMessageLogFile", JMSServerMBean.class, var3, var4);
         var1.put("JMSMessageLogFile", var2);
         var2.setValue("description", "The message log file configuration for this JMS Server. ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSQueues")) {
         var3 = "getJMSQueues";
         var4 = null;
         var2 = new PropertyDescriptor("JMSQueues", JMSServerMBean.class, var3, var4);
         var1.put("JMSQueues", var2);
         var2.setValue("description", "<p>Acquire JMSQueues for this JMSServer</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSQueue");
         var2.setValue("creator", "createJMSQueue");
         var2.setValue("creator", "createJMSQueue");
         var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("JMSSessionPools")) {
         var3 = "getJMSSessionPools";
         var4 = null;
         var2 = new PropertyDescriptor("JMSSessionPools", JMSServerMBean.class, var3, var4);
         var1.put("JMSSessionPools", var2);
         var2.setValue("description", "<p>The session pools defined for the JMS server.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSSessionPool");
         var2.setValue("creator", "createJMSSessionPool");
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSTopics")) {
         var3 = "getJMSTopics";
         var4 = null;
         var2 = new PropertyDescriptor("JMSTopics", JMSServerMBean.class, var3, var4);
         var1.put("JMSTopics", var2);
         var2.setValue("description", "<p>Define JMSTopics for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSTopic");
         var2.setValue("destroyer", "destroyJMSTopic");
         var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("MaximumMessageSize")) {
         var3 = "getMaximumMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaximumMessageSize";
         }

         var2 = new PropertyDescriptor("MaximumMessageSize", JMSServerMBean.class, var3, var4);
         var1.put("MaximumMessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in individual messages on this JMS server. The size of a message includes the message body, any user-defined properties, and the user-defined JMS header fields <code>JMSCorrelationID</code> and <code>JMSType</code>.</p>  <p>The maximum message size is only enforced for the initial production of a message. Messages that are redirected to an error destination or forwarded to a member of a distributed destination are not checked for size. For instance, if a destination and its corresponding error destination are configured with a maximum message size of 128K bytes and 64K bytes, respectively, a message of 96K bytes could be redirected to the error destination (even though it exceeds the 64K byte maximum), but a producer could not directly send the 96K byte message to the error destination.</p>  <p><b>Note:</b> Any change to this maximum affects only incoming messages; stored messages are not affected.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessageBufferSize")) {
         var3 = "getMessageBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageBufferSize";
         }

         var2 = new PropertyDescriptor("MessageBufferSize", JMSServerMBean.class, var3, var4);
         var1.put("MessageBufferSize", var2);
         var2.setValue("description", "<p>The amount of memory (in bytes) that this JMS server can use to store message bodies before it writes them to disk. When the JMS server writes the message bodies to disk, it clears them from memory.</p>  <p>A value of -1 (the default) specifies that the server will automatically determine a size based on the maximum heap size of the JVM. This default will be set to either one-third the maximum heap size, or 512 megabytes, whichever is smaller.</p>  <p>The larger the buffer, the more memory JMS will consume when many messages are waiting on queues or topics. Once the buffer is surpassed, JMS may write message bodies to the directory specified by PagingDirectory in an effort to reduce memory usage below this buffer.</p>  <p>Surpassing the buffer size does not stop the JMS server from accepting new messages. It is still possible to run out of memory if messages are arriving faster than they can be written to disk. Users with high messaging loads who wish to support the highest possible availability should consider setting a quota or setting a threshold and enabling flow control.</p>  <p>Paging is always supported.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPagingDirectory")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesMaximum")) {
         var3 = "getMessagesMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesMaximum";
         }

         var2 = new PropertyDescriptor("MessagesMaximum", JMSServerMBean.class, var3, var4);
         var1.put("MessagesMaximum", var2);
         var2.setValue("description", "<p>The maximum number of messages that can be stored in this JMS server. A value of <code>-1</code> removes any WebLogic Server limits.</p>  <p>Because excessive message volume can cause memory saturation, Oracle recommends that this value corresponds to the total amount of system memory available after accounting for the rest of your application load.</p>  <p><b>Range of Values:</b> &gt;= MessagesThresholdHigh.</p> ");
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

         var2 = new PropertyDescriptor("MessagesThresholdHigh", JMSServerMBean.class, var3, var4);
         var1.put("MessagesThresholdHigh", var2);
         var2.setValue("description", "<p>The upper threshold (number of messages stored in this JMS server) that triggers flow control and logging events. A value of <code>-1</code> disables the events for this JMS server.</p>  <p>If the number of messages exceeds this threshold, the triggered events are:</p> <ul> <li> <code>Log Messages</code> - A message is logged on the server indicating a high threshold condition. </li> <li> <code>Flow Control</code> - If flow control is enabled, the JMS server becomes armed and instructs producers to begin decreasing their message flow. </li> </ul>  <p><b>Range of Values:</b> &lt;= MessagesMaximum; &gt;= MessagesThresholdLow.</p> ");
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

         var2 = new PropertyDescriptor("MessagesThresholdLow", JMSServerMBean.class, var3, var4);
         var1.put("MessagesThresholdLow", var2);
         var2.setValue("description", "<p>The lower threshold (number of messages stored in this JMS server) that triggers flow control and logging events. A value of <code>-1</code> disables the events for this JMS server.</p>  <p>If the number of messages falls below this threshold, the triggered events are:</p> <ul> <li> <code>Log Messages</code> - A message is logged on the server indicating that the threshold condition has cleared. </li> <li> <code>Flow Control</code> - If flow control is enabled, the JMS server becomes disarmed and instructs producers to begin increasing their message flow. </li> </ul>  <p><i>Note:</i> This attribute is dynamically configurable.</p>  <p><b>Range of Values:</b> &lt;= MessagesThresholdHigh</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PagingBlockSize")) {
         var3 = "getPagingBlockSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingBlockSize";
         }

         var2 = new PropertyDescriptor("PagingBlockSize", JMSServerMBean.class, var3, var4);
         var1.put("PagingBlockSize", var2);
         var2.setValue("description", "<p>The smallest addressable block, in bytes, of a file. When a native <code>wlfileio</code> driver is available and the paging block size has not been configured by the user, the store selects the minimum OS specific value for unbuffered (direct) I/O, if it is within the range [512, 8192].</p> A paging store's block size does not change once the paging store creates its files. Changes to block size only take effect for new paging stores or after the current files have been deleted. See \"Tuning the Persistent Store\" in <i>Performance and Tuning for Oracle WebLogic Server</i>. ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(8192));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PagingDirectory")) {
         var3 = "getPagingDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingDirectory";
         }

         var2 = new PropertyDescriptor("PagingDirectory", JMSServerMBean.class, var3, var4);
         var1.put("PagingDirectory", var2);
         var2.setValue("description", "<p> Specifies where message bodies are written when the size of the message bodies in the JMS server exceeds the message buffer size.</p> <p> If unspecified, messages are written to the default <code>tmp</code> directory inside the <code><i>server-name</i></code> subdirectory of a domain's root directory. For example, <code><i>domain-name</i>/servers/<i>server-name</i>/tmp</code>, where <code><i>domain-name</i></code> is the root directory of your domain. </p> <p> For best performance, this directory should not be the same as the directory used by the JMS server's persistent store. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMessageBufferSize")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PagingIoBufferSize")) {
         var3 = "getPagingIoBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingIoBufferSize";
         }

         var2 = new PropertyDescriptor("PagingIoBufferSize", JMSServerMBean.class, var3, var4);
         var1.put("PagingIoBufferSize", var2);
         var2.setValue("description", "<p>The I/O buffer size, in bytes, automatically rounded down to the nearest power of 2. <ul> <li>When a native <code>wlfileio</code> driver is available, the setting applies to off-heap (native) memory.</li> <li>When a native <code>wlfileio</code> driver is not available, the setting applies to JAVA heap memory.</li> <li>For the best runtime performance, Oracle recommends setting <code>PagingIOBufferSize</code> so that it is larger than the largest write (multiple concurrent store requests may be combined into a single write).</li> <li>See the JMS server runtime MBean attribute <code>PagingAllocatedIOBufferBytes</code> to find out the actual allocated off-heap (native) memory amount.</li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(67108864));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PagingMaxFileSize")) {
         var3 = "getPagingMaxFileSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingMaxFileSize";
         }

         var2 = new PropertyDescriptor("PagingMaxFileSize", JMSServerMBean.class, var3, var4);
         var1.put("PagingMaxFileSize", var2);
         var2.setValue("description", "<p>The paging maximum file size, in bytes. <ul> <li>The <code>PagingMaxFileSize</code> value affects the number of files needed to accommodate a paging store of a particular size (number of files = paging store size/MaxFileSize rounded up).</li>  <li>A paging store automatically reuses space freed by deleted records and automatically expands individual files up to <code>PagingMaxFileSize</code> if there is not enough space for a new record. If there is no space left in exiting files for a new record, a paging store creates an additional file.</li>  <li> A small number of larger files is normally preferred over a large number of smaller files as each file allocates Window Buffer and file handles. </li>  <li> If <code>PagingMaxFileSize</code> is larger than 2^24 * <code>PagingBlockSize</code>, then <code>MaxFileSize</code> is ignored, and the value becomes 2^24 * <code>PagingBlockSize</code>. The default <code>PagingBlockSize</code> is 512, and 2^24 * 512 is 8 GB. </li> </ul> Oracle recommends not setting the Paging Max File Size above the default value of 1,342,177,280.  </p> ");
         setPropertyDescriptorDefault(var2, new Long(1342177280L));
         var2.setValue("legalMin", new Long(10485760L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PagingMaxWindowBufferSize")) {
         var3 = "getPagingMaxWindowBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingMaxWindowBufferSize";
         }

         var2 = new PropertyDescriptor("PagingMaxWindowBufferSize", JMSServerMBean.class, var3, var4);
         var1.put("PagingMaxWindowBufferSize", var2);
         var2.setValue("description", "<p>The maximum amount of data, in bytes and rounded down to the nearest power of 2, mapped into the JVM's address space per paging store file. Applies only when a native <code>wlfileio</code> library is loaded.</p>  <p>A window buffer does not consume Java heap memory, but does consume off-heap (native) memory. If the paging store is unable to allocate the requested buffer size, it allocates smaller and smaller buffers until it reaches <code>PagingMinWindowBufferSize</code>, and then fails if it cannot honor <code>PagingMinWindowBufferSize</code>.</p>  <p>Oracle recommends setting the max window buffer size to more than double the size of the largest write (multiple concurrently updated records may be combined into a single write), and greater than or equal to the file size, unless there are other constraints. 32-bit JVMs may impose a total limit of between 2 and 4GB for combined Java heap plus off-heap (native) memory usage.</p>  <ul> <li>See the JMS server runtime MBean attribute <code>PagingAllocatedWindowBufferBytes</code> to find out the actual allocated Window Buffer Size.<li> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PagingMinWindowBufferSize")) {
         var3 = "getPagingMinWindowBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingMinWindowBufferSize";
         }

         var2 = new PropertyDescriptor("PagingMinWindowBufferSize", JMSServerMBean.class, var3, var4);
         var1.put("PagingMinWindowBufferSize", var2);
         var2.setValue("description", "<p>The minimum amount of data, in bytes and rounded down to the nearest power of 2, mapped into the JVM's address space per paging store file.  Applies only when a native <code>wlfileio</code> library is loaded. See <a href='#getPagingMaxWindowBufferSize'>Paging Maximum Window Buffer Size</a>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("PagingStore")) {
         var3 = "getPagingStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingStore";
         }

         var2 = new PropertyDescriptor("PagingStore", JMSServerMBean.class, var3, var4);
         var1.put("PagingStore", var2);
         var2.setValue("description", "<p>This parameter has been deprecated. New configurations should use the \"PagingDirectory\" parameter if they wish to control where non-persistent messages will be temporarily stored.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPagingDirectory")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 Replaced with the PagingDirectory attribute. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("PersistentStore")) {
         var3 = "getPersistentStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPersistentStore";
         }

         var2 = new PropertyDescriptor("PersistentStore", JMSServerMBean.class, var3, var4);
         var1.put("PersistentStore", var2);
         var2.setValue("description", "<p>The file or database in which this JMS server stores persistent messages. If unspecified, the JMS server uses the default persistent store that is configured on each targeted WebLogic Server instance.</p>  <p>The disk-based file store or JDBC-accessible database store that you specify must be targeted to the same server instance as this JMS server. Multiple services on the same WebLogic Server instance, including multiple JMS servers, may share the same persistent store. Each service's persistent data will be kept apart.</p>  <p>If you specify a PersistentStore, the deprecated <b>Store</b> field must not be set. If neither the <b>PersistentStore</b> field nor the <b>Store</b> field are set, the JMS server supports persistent messaging using the default persistent store for the targeted WebLogic Server instance.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.PersistentStoreMBean")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ProductionPausedAtStartup")) {
         var3 = "getProductionPausedAtStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProductionPausedAtStartup";
         }

         var2 = new PropertyDescriptor("ProductionPausedAtStartup", JMSServerMBean.class, var3, var4);
         var1.put("ProductionPausedAtStartup", var2);
         var2.setValue("description", "<p>Specifies whether production is paused at server startup on destinations targeted to this JMS server. A destination cannot receive any new messages while it is paused.</p>  <p>When the value is set to <code>true</code>, then immediately after the host server instance is rebooted, then this JMS server and its targeted destinations are modified such that they are in a \"production paused\" state, which results in preventing new message production activities on those destinations.</p>  <p>To resume normal new message production activity, later you will have to change the state of this JMS server to a \"production enabled\" state by setting this value to <code>false</code>, and then either redeploy the JMS server or reboot the hosting server instance. </p>  <p>When the value is set to <code>default</code>, then the Production Paused At Startup is determined based on the corresponding setting on the individual destination.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JMSServerRuntimeMBean#resumeProduction"), BeanInfoHelper.encodeEntities("weblogic.management.runtime.JMSDestinationRuntimeMBean#resumeProduction")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SessionPools")) {
         var3 = "getSessionPools";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionPools";
         }

         var2 = new PropertyDescriptor("SessionPools", JMSServerMBean.class, var3, var4);
         var1.put("SessionPools", var2);
         var2.setValue("description", "<p>The session pools defined for the JMS server. This method is provided for backward compatibility.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeSessionPool");
         var2.setValue("adder", "addSessionPool");
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("Store")) {
         var3 = "getStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStore";
         }

         var2 = new PropertyDescriptor("Store", JMSServerMBean.class, var3, var4);
         var1.put("Store", var2);
         var2.setValue("description", "<p>The persistent disk-based file or JDBC-accessible database for the JMS server.</p>  <p>A persistent store may only be used by one JMS server. If this value is unset, the value set by the \"PersistentStore\" attribute is used. If neither attribute is set, the default persistent store for the targeted managed server is used. It is an error to set both attributes.</p>  <p>This attribute has been deprecated. New configurations should use the \"PersistentStore\" attribute, which allows multiple subsystems, including multiple JMS servers on the same managed server, to share the same persistent store.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPersistentStore")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 Replaced with the PersistentStore attribute. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("StoreEnabled")) {
         var3 = "getStoreEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStoreEnabled";
         }

         var2 = new PropertyDescriptor("StoreEnabled", JMSServerMBean.class, var3, var4);
         var1.put("StoreEnabled", var2);
         var2.setValue("description", "<p> Specifies whether message persistence is supported for this JMS server.</p> <p> When set to true, the default, then if the JMS server has no store configured, the targeted WebLogic Server instance's default store is used to store persistent messages. When set to false, then this JMS server does not support persistent messages, and instead downgrades them to non-persistent.</p> <p> Oracle recommends not setting this parameter to false. It is available to provide compatibility with older releases.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JMSServerMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>The server instances or a migratable targets defined in the current domain that are candidates for hosting the JMSSerer.</p>  <p>In a clustered environment, a recommended best practice is to target a JMSServer to the same migratable target as the Persistent Store that it uses, so that a member server will not be a single point of failure. A JMSServer can also be configured to automatically migrate from an unhealthy server instance to a healthy server instance with the help of the automatic service migration feature.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("TemporaryTemplate")) {
         var3 = "getTemporaryTemplate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTemporaryTemplate";
         }

         var2 = new PropertyDescriptor("TemporaryTemplate", JMSServerMBean.class, var3, var4);
         var1.put("TemporaryTemplate", var2);
         var2.setValue("description", "<p>The name of an existing JMS template to use when creating all temporary queues and topics for this JMS server. Specifying a value for this field allows JMS applications to create temporary destinations. If Store values are provided as part of a temporary template, they are ignored, because temporary destinations do not support persistent messaging.</p>  <p><i>Note:</i> If this attribute is set to none, attempts to create a temporary destination (queue or topic) will fail.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 Replaced with the TemporaryTemplateName and TemporaryTemplateResource attributes. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("TemporaryTemplateName")) {
         var3 = "getTemporaryTemplateName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTemporaryTemplateName";
         }

         var2 = new PropertyDescriptor("TemporaryTemplateName", JMSServerMBean.class, var3, var4);
         var1.put("TemporaryTemplateName", var2);
         var2.setValue("description", "<p>The name of a configured JMS template that this JMS server uses to create temporary destinations.</p>  <p>Entering a template name, requires you to specify the JMS module that contains this template. However, a template name cannot be specified if the <b>Hosting Temporary Destinations</b> field is disabled.</p>  <p><b>Note:</b> If the specified JMS template provides persistent store values, they are ignored because temporary destinations do not support persistent messaging.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.JMSSystemResourceMBean"), BeanInfoHelper.encodeEntities("weblogic.j2ee.descriptor.wl.TemplateBean")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TemporaryTemplateResource")) {
         var3 = "getTemporaryTemplateResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTemporaryTemplateResource";
         }

         var2 = new PropertyDescriptor("TemporaryTemplateResource", JMSServerMBean.class, var3, var4);
         var1.put("TemporaryTemplateResource", var2);
         var2.setValue("description", "<p>The name of a JMS module that contains a template that this JMS server can use to create temporary destinations.</p>  <p>Entering a JMS module name requires you to specify a temporary template name. However, a module name cannot be specified if the <b>Hosting Temporary Destinations</b> field is disabled.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.JMSSystemResourceMBean"), BeanInfoHelper.encodeEntities("weblogic.j2ee.descriptor.wl.TemplateBean")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AllowsPersistentDowngrade")) {
         var3 = "isAllowsPersistentDowngrade";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowsPersistentDowngrade";
         }

         var2 = new PropertyDescriptor("AllowsPersistentDowngrade", JMSServerMBean.class, var3, var4);
         var1.put("AllowsPersistentDowngrade", var2);
         var2.setValue("description", "<p>Specifies whether JMS clients will get an exception when sending persistent messages to a destination targeted to a JMS server that does not have a persistent store configured. This parameter only has effect when the Store Enabled parameter is disabled (false).</p>  <p>When set to false, the default, clients will get an exception when sending persistent messages to a JMS server with no store configured. When set to true, then persistent messages are downgraded to non-persistent; however, the send operations are allowed to continue.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BytesPagingEnabled")) {
         var3 = "isBytesPagingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesPagingEnabled";
         }

         var2 = new PropertyDescriptor("BytesPagingEnabled", JMSServerMBean.class, var3, var4);
         var1.put("BytesPagingEnabled", var2);
         var2.setValue("description", "<p>This parameter has been deprecated. Paging is always enabled. The \"MessageBufferSize\" parameter controls how much memory is used before paging kicks in.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMessageBufferSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Replaced by defaulting the paging to always be enabled. ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("HostingTemporaryDestinations")) {
         var3 = "isHostingTemporaryDestinations";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHostingTemporaryDestinations";
         }

         var2 = new PropertyDescriptor("HostingTemporaryDestinations", JMSServerMBean.class, var3, var4);
         var1.put("HostingTemporaryDestinations", var2);
         var2.setValue("description", "<p>Specifies whether this JMS server can be used to host temporary destinations.</p>  <p>If this field is enabled and no <b>Temporary Template Name</b> is defined, then the temporary destinations created on this JMS server will use all default destination values. If this field is enabled, then the JMS template to be used for creating temporary destinations is specified by the <b>Temporary Template Name</b> field. If this field is disabled, then this JMS server will not host temporary destinations.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagesPagingEnabled")) {
         var3 = "isMessagesPagingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesPagingEnabled";
         }

         var2 = new PropertyDescriptor("MessagesPagingEnabled", JMSServerMBean.class, var3, var4);
         var1.put("MessagesPagingEnabled", var2);
         var2.setValue("description", "<p>This parameter has been deprecated. Paging is always enabled. The \"MessageBufferSize\" parameter controls how much memory is used before paging kicks in.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMessageBufferSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Replaced by defaulting the paging to always be enabled. ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("PagingFileLockingEnabled")) {
         var3 = "isPagingFileLockingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPagingFileLockingEnabled";
         }

         var2 = new PropertyDescriptor("PagingFileLockingEnabled", JMSServerMBean.class, var3, var4);
         var1.put("PagingFileLockingEnabled", var2);
         var2.setValue("description", "<p>Determines whether OS file locking is used. </p> When file locking protection is enabled, a store boot fails if another store instance already has opened the store files. Do not disable this setting unless you have procedures in place to prevent multiple store instances from opening the same file. File locking is not required but helps prevent corruption in the event that two same-named file store instances attempt to operate in the same directories. This setting applies to both primary and cache files. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSServerMBean.class.getMethod("createJMSSessionPool", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the sessionPool to add to the JMS server ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Add a session pool to the JMS server.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSSessionPools");
      }

      var3 = JMSServerMBean.class.getMethod("destroyJMSSessionPool", JMSSessionPoolMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sessionPool", "the sessionPool to remove from the JMS server ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Remove a session pool from the JMS server.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSSessionPools");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("createJMSQueue", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSQueue object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("createJMSQueue", String.class, JMSQueueMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the JMSQueueMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSQueue object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("destroyJMSQueue", JMSQueueMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("queue", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSQueue from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("createJMSTopic", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSTopic object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("destroyJMSTopic", JMSTopicMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("topic", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSTopic from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSServerMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The target where the JMSServer needs to be deployed ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Targets the JMSServer to the specified target instance. The target must be either a server or a migratable target.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = JMSServerMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The target that has to be removed from the JMSServer ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Untargets the JMSServer instance from the current target.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = JMSServerMBean.class.getMethod("addSessionPool", JMSSessionPoolMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sessionPool", "the sessionPool to add to the JMS server ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Add a session pool to the JMS server. This method is provided for backward compatibility.</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("role", "collection");
         var2.setValue("property", "SessionPools");
      }

      var3 = JMSServerMBean.class.getMethod("removeSessionPool", JMSSessionPoolMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sessionPool", "the sessionPool to remove from the JMS server ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Remove a session pool from the JMS server. This method is provided for backward compatibility.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SessionPools");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("addDestination", JMSDestinationMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("destination", "the destination to be added to the JMS server ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if destination is null or exists")};
            var2.setValue("throws", var6);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the functionality of JMS modules. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Add a JMS destination to the JMS server.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "Destinations");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("removeDestination", JMSDestinationMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("destination", "the destination to be removed from the JMS server ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if destination is null or does not exist")};
            var2.setValue("throws", var6);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the functionality of JMS modules. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a destination from the JNDI tree.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "Destinations");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSServerMBean.class.getMethod("lookupJMSSessionPool", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0 Replaced by message-driven beans. The JMSSessionPoolMBean type was deprecated. See JMSSessionPoolMBean for more information. ");
         var1.put(var4, var2);
         var2.setValue("description", "<p>Get a session pool by name from the JMS server.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSSessionPools");
      }

      String var5;
      ParameterDescriptor[] var6;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("lookupJMSQueue", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("lookupJMSTopic", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSServerMBean.class.getMethod("createJMSSessionPool", String.class, JMSSessionPoolMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "the JMSSessionPool that is being cloned. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create JMSTopic object</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = JMSServerMBean.class.getMethod("freezeCurrentValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSServerMBean.class.getMethod("restoreDefaultValue", String.class);
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

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("setDestinations", JMSDestinationMBean[].class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("destinations", "The new destinations value ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the functionality of JMS modules. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Sets the value of the Destinations attribute.</p> ");
            String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#getDestinations")};
            var2.setValue("see", var6);
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSServerMBean.class.getMethod("createJMSTopic", String.class, JMSTopicMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced with the JMS module functionality. ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSTopic object</p> ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
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
