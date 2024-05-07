package weblogic.jms.frontend;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSConsumerRuntimeMBean;

public class FEConsumerBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSConsumerRuntimeMBean.class;

   public FEConsumerBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public FEConsumerBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = FEConsumer.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jms.frontend");
      String var3 = (new String("This class is used for monitoring a WebLogic JMS consumer.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSConsumerRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>The number of bytes pending (uncommitted and unacknowledged) by this consumer.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The number of bytes received by this consumer since the last reset.</p> ");
      }

      if (!var1.containsKey("ClientID")) {
         var3 = "getClientID";
         var4 = null;
         var2 = new PropertyDescriptor("ClientID", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("ClientID", var2);
         var2.setValue("description", "<p>The client ID for this connection.</p> ");
      }

      if (!var1.containsKey("ClientIDPolicy")) {
         var3 = "getClientIDPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("ClientIDPolicy", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("ClientIDPolicy", var2);
         var2.setValue("description", "<p>The ClientIDPolicy on this connection or durable subscriber.</p> Valid values are: <ul><li> <code>weblogic.management.configuration.JMSConstants.CLIENT_ID_POLICY_RESTRICTED</code>: Only one connection that uses this policy exists in a cluster at any given time for a particular <code>ClientID</code>.</li> <li><code>weblogic.management.configuration.JMSConstants.CLIENT_ID_POLICY_UNRESTRICTED</code>:  Connections created using this policy can specify any <code>ClientID</code>, even when other restricted or unrestricted connections already use the same <code>ClientID</code>.</li> </ul> ");
      }

      if (!var1.containsKey("DestinationName")) {
         var3 = "getDestinationName";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationName", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationName", var2);
         var2.setValue("description", "<p>The name of the destination for this consumer. In case of a distributed destination, it is the name of the distributed destination, instead of the member destination.</p> ");
      }

      if (!var1.containsKey("MemberDestinationName")) {
         var3 = "getMemberDestinationName";
         var4 = null;
         var2 = new PropertyDescriptor("MemberDestinationName", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("MemberDestinationName", var2);
         var2.setValue("description", "<p>The name of the destination for this consumer. In case of a distributed destination, it is the name of the member destination.</p> ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>The number of messages pending (uncommitted and unacknowledged) by this consumer.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received by this consumer since the last reset.</p> ");
      }

      if (!var1.containsKey("Selector")) {
         var3 = "getSelector";
         var4 = null;
         var2 = new PropertyDescriptor("Selector", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("Selector", var2);
         var2.setValue("description", "<p>The selector associated with this consumer, if any.</p> ");
      }

      if (!var1.containsKey("SubscriptionSharingPolicy")) {
         var3 = "getSubscriptionSharingPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("SubscriptionSharingPolicy", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscriptionSharingPolicy", var2);
         var2.setValue("description", "<p>The Subscription Sharing Policy on this subscriber.</p> ");
      }

      if (!var1.containsKey("Active")) {
         var3 = "isActive";
         var4 = null;
         var2 = new PropertyDescriptor("Active", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("Active", var2);
         var2.setValue("description", "<p>Indicates whether the consumer active. A consumer is active if it has a message listener set up or a synchronous receive in progress.</p> ");
      }

      if (!var1.containsKey("Durable")) {
         var3 = "isDurable";
         var4 = null;
         var2 = new PropertyDescriptor("Durable", JMSConsumerRuntimeMBean.class, var3, (String)var4);
         var1.put("Durable", var2);
         var2.setValue("description", "<p>Indicates whether the consumer is durable.</p> ");
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
      Method var3 = JMSConsumerRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
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
