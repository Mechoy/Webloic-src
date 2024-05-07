package weblogic.jms.saf;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SAFAgentRuntimeMBean;
import weblogic.messaging.saf.internal.SAFStatisticsCommonMBeanImplBeanInfo;

public class SAFAgentRuntimeMBeanAggregatorBeanInfo extends SAFStatisticsCommonMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SAFAgentRuntimeMBean.class;

   public SAFAgentRuntimeMBeanAggregatorBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SAFAgentRuntimeMBeanAggregatorBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SAFAgentRuntimeMBeanAggregator.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.jms.saf");
      String var3 = (new String("This class is used for monitoring a WebLogic SAF agent.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SAFAgentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("BytesCurrentCount")) {
         var3 = "getBytesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesCurrentCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of bytes. This number does not include the pending bytes.</p> ");
      }

      if (!var1.containsKey("BytesHighCount")) {
         var3 = "getBytesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesHighCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesHighCount", var2);
         var2.setValue("description", "<p>Returns the peak number of bytes since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>Returns the number of pending bytes. Pending bytes are over and above the current number of bytes.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The number of bytes received since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesThresholdTime")) {
         var3 = "getBytesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("BytesThresholdTime", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesThresholdTime", var2);
         var2.setValue("description", "<p>Returns the amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("Conversations")) {
         var3 = "getConversations";
         var4 = null;
         var2 = new PropertyDescriptor("Conversations", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("Conversations", var2);
         var2.setValue("description", "<p>A list of SAFConversationRuntimeMBean instances</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ConversationsCurrentCount")) {
         var3 = "getConversationsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConversationsCurrentCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConversationsCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of conversations</p> ");
      }

      if (!var1.containsKey("ConversationsHighCount")) {
         var3 = "getConversationsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConversationsHighCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConversationsHighCount", var2);
         var2.setValue("description", "<p>The peak number of conversations since the last reset.</p> ");
      }

      if (!var1.containsKey("ConversationsTotalCount")) {
         var3 = "getConversationsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConversationsTotalCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConversationsTotalCount", var2);
         var2.setValue("description", "<p>The total number of conversations since the last reset.</p> ");
      }

      if (!var1.containsKey("FailedMessagesTotal")) {
         var3 = "getFailedMessagesTotal";
         var4 = null;
         var2 = new PropertyDescriptor("FailedMessagesTotal", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("FailedMessagesTotal", var2);
         var2.setValue("description", "<p>Returns the total number of messages that have failed to be forwarded since the last reset.</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of this JMS server.</p> ");
      }

      if (!var1.containsKey("MessagesCurrentCount")) {
         var3 = "getMessagesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesCurrentCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of messages. This number includes the pending messages.</p> ");
      }

      if (!var1.containsKey("MessagesHighCount")) {
         var3 = "getMessagesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesHighCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesHighCount", var2);
         var2.setValue("description", "<p>Returns the peak number of messages since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>Returns the number of pending messages. Pending messages are over and above the current number of messages. A pending message is one that has either been sent in a transaction and not committed, or been forwarded but has not been acknowledged.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesThresholdTime")) {
         var3 = "getMessagesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesThresholdTime", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesThresholdTime", var2);
         var2.setValue("description", "<p>Returns the amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("RemoteEndpoints")) {
         var3 = "getRemoteEndpoints";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteEndpoints", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("RemoteEndpoints", var2);
         var2.setValue("description", "<p>The remote endpoints to which this SAF agent has been storing and forwarding messages.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("RemoteEndpointsCurrentCount")) {
         var3 = "getRemoteEndpointsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteEndpointsCurrentCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("RemoteEndpointsCurrentCount", var2);
         var2.setValue("description", "<p>The current number of remote endpoints to which this SAF agent has been storing and forwarding messages.</p> ");
      }

      if (!var1.containsKey("RemoteEndpointsHighCount")) {
         var3 = "getRemoteEndpointsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteEndpointsHighCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("RemoteEndpointsHighCount", var2);
         var2.setValue("description", "<p>The peak number of remote endpoints to which this SAF agent has been storing and forwarding messages since last reset.</p> ");
      }

      if (!var1.containsKey("RemoteEndpointsTotalCount")) {
         var3 = "getRemoteEndpointsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteEndpointsTotalCount", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("RemoteEndpointsTotalCount", var2);
         var2.setValue("description", "<p>The number of remote endpoints to which this SAF agent has been storing and forwarding messages since last reset.</p> ");
      }

      if (!var1.containsKey("PausedForForwarding")) {
         var3 = "isPausedForForwarding";
         var4 = null;
         var2 = new PropertyDescriptor("PausedForForwarding", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("PausedForForwarding", var2);
         var2.setValue("description", "<p>Indicates whether or not the sending agent is paused for forwarding at the current time.</p> ");
      }

      if (!var1.containsKey("PausedForIncoming")) {
         var3 = "isPausedForIncoming";
         var4 = null;
         var2 = new PropertyDescriptor("PausedForIncoming", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("PausedForIncoming", var2);
         var2.setValue("description", "<p>Indicates whether or not the sending agent is paused for incoming messages at the current time.</p> ");
      }

      if (!var1.containsKey("PausedForReceiving")) {
         var3 = "isPausedForReceiving";
         var4 = null;
         var2 = new PropertyDescriptor("PausedForReceiving", SAFAgentRuntimeMBean.class, var3, (String)var4);
         var1.put("PausedForReceiving", var2);
         var2.setValue("description", "<p>Indicates whether or not the receiving agent is paused for receiving at the current time.</p> ");
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
      Method var3 = SAFAgentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = SAFAgentRuntimeMBean.class.getMethod("pauseIncoming");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      String[] var5;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Pauses the sending agent on accepting new messages.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = SAFAgentRuntimeMBean.class.getMethod("resumeIncoming");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes the sending agent for accepting new messages.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = SAFAgentRuntimeMBean.class.getMethod("pauseForwarding");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Pauses the sending agent on forwarding messages so that the agent will not forward messages but will accept new messages.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = SAFAgentRuntimeMBean.class.getMethod("resumeForwarding");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes the sending agent for forwarding messages.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = SAFAgentRuntimeMBean.class.getMethod("pauseReceiving");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Pauses the receiving agent on receiving messages.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = SAFAgentRuntimeMBean.class.getMethod("resumeReceiving");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes the receiving agent for receiving messages.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
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
