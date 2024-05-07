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
import weblogic.management.runtime.JMSSessionRuntimeMBean;

public class FESessionBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSSessionRuntimeMBean.class;

   public FESessionBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public FESessionBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = FESession.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jms.frontend");
      String var3 = (new String("This class is used for monitoring a WebLogic JMS session.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSSessionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AcknowledgeMode")) {
         var3 = "getAcknowledgeMode";
         var4 = null;
         var2 = new PropertyDescriptor("AcknowledgeMode", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("AcknowledgeMode", var2);
         var2.setValue("description", "<p>The acknowledge mode as one of the following:</p>  <ul> <li><code>AUTO_ACKNOWLEDGE</code></li>  <li><code>CLIENT_ACKNOWLEDGE</code></li>  <li><code>DUPS_OK_ACKNOWLEDGE</code></li>  <li><code>NO_ACKNOWLEDGE</code></li> </ul> ");
      }

      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>The number of bytes pending (uncommitted and unacknowledged) for this session.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The number of bytes received by this session since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesSentCount")) {
         var3 = "getBytesSentCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesSentCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("BytesSentCount", var2);
         var2.setValue("description", "<p>The number of bytes sent by this session since the last reset.</p> ");
      }

      if (!var1.containsKey("Consumers")) {
         var3 = "getConsumers";
         var4 = null;
         var2 = new PropertyDescriptor("Consumers", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("Consumers", var2);
         var2.setValue("description", "<p>An array of consumers for this session.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ConsumersCurrentCount")) {
         var3 = "getConsumersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConsumersCurrentCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("ConsumersCurrentCount", var2);
         var2.setValue("description", "<p>The current number of consumers for this session.</p> ");
      }

      if (!var1.containsKey("ConsumersHighCount")) {
         var3 = "getConsumersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConsumersHighCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("ConsumersHighCount", var2);
         var2.setValue("description", "<p>The peak number of consumers for this session since the last reset.</p> ");
      }

      if (!var1.containsKey("ConsumersTotalCount")) {
         var3 = "getConsumersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConsumersTotalCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("ConsumersTotalCount", var2);
         var2.setValue("description", "<p>The number of consumers instantiated by this session since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>The number of messages pending (uncommitted and unacknowledged) for this session.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received by this session since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesSentCount")) {
         var3 = "getMessagesSentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesSentCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("MessagesSentCount", var2);
         var2.setValue("description", "<p>The number of bytes sent by this session since the last reset.</p> ");
      }

      if (!var1.containsKey("Producers")) {
         var3 = "getProducers";
         var4 = null;
         var2 = new PropertyDescriptor("Producers", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("Producers", var2);
         var2.setValue("description", "<p>An array of producers for this session.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ProducersCurrentCount")) {
         var3 = "getProducersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ProducersCurrentCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("ProducersCurrentCount", var2);
         var2.setValue("description", "<p>The current number of producers for this session.</p> ");
      }

      if (!var1.containsKey("ProducersHighCount")) {
         var3 = "getProducersHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ProducersHighCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("ProducersHighCount", var2);
         var2.setValue("description", "<p>The peak number of producers for this session since the last reset.</p> ");
      }

      if (!var1.containsKey("ProducersTotalCount")) {
         var3 = "getProducersTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ProducersTotalCount", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("ProducersTotalCount", var2);
         var2.setValue("description", "<p>The number of producers for this session since the last reset.</p> ");
      }

      if (!var1.containsKey("Transacted")) {
         var3 = "isTransacted";
         var4 = null;
         var2 = new PropertyDescriptor("Transacted", JMSSessionRuntimeMBean.class, var3, (String)var4);
         var1.put("Transacted", var2);
         var2.setValue("description", "<p>Indicates whether the session is transacted.</p> ");
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
      Method var3 = JMSSessionRuntimeMBean.class.getMethod("preDeregister");
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
