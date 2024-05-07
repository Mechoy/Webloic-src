package weblogic.messaging.saf.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SAFStatisticsCommonMBean;

public class SAFStatisticsCommonMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SAFStatisticsCommonMBean.class;

   public SAFStatisticsCommonMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SAFStatisticsCommonMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SAFStatisticsCommonMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.messaging.saf.internal");
      String var3 = (new String("This class is has all the common statistics stuff for a SAFAgentRuntimeMBean or a SAFRemoteEndpoitsRuntimeMBean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SAFStatisticsCommonMBean");
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
         var2 = new PropertyDescriptor("BytesCurrentCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("BytesCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of bytes. This number does not include the pending bytes.</p> ");
      }

      if (!var1.containsKey("BytesHighCount")) {
         var3 = "getBytesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesHighCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("BytesHighCount", var2);
         var2.setValue("description", "<p>Returns the peak number of bytes since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesPendingCount")) {
         var3 = "getBytesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesPendingCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("BytesPendingCount", var2);
         var2.setValue("description", "<p>Returns the number of pending bytes. Pending bytes are over and above the current number of bytes.</p> ");
      }

      if (!var1.containsKey("BytesReceivedCount")) {
         var3 = "getBytesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("BytesReceivedCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("BytesReceivedCount", var2);
         var2.setValue("description", "<p>The number of bytes received since the last reset.</p> ");
      }

      if (!var1.containsKey("BytesThresholdTime")) {
         var3 = "getBytesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("BytesThresholdTime", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("BytesThresholdTime", var2);
         var2.setValue("description", "<p>Returns the amount of time in the threshold condition since the last reset.</p> ");
      }

      if (!var1.containsKey("FailedMessagesTotal")) {
         var3 = "getFailedMessagesTotal";
         var4 = null;
         var2 = new PropertyDescriptor("FailedMessagesTotal", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("FailedMessagesTotal", var2);
         var2.setValue("description", "<p>Returns the total number of messages that have failed to be forwarded since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesCurrentCount")) {
         var3 = "getMessagesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesCurrentCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("MessagesCurrentCount", var2);
         var2.setValue("description", "<p>Returns the current number of messages. This number includes the pending messages.</p> ");
      }

      if (!var1.containsKey("MessagesHighCount")) {
         var3 = "getMessagesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesHighCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("MessagesHighCount", var2);
         var2.setValue("description", "<p>Returns the peak number of messages since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesPendingCount")) {
         var3 = "getMessagesPendingCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesPendingCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("MessagesPendingCount", var2);
         var2.setValue("description", "<p>Returns the number of pending messages. Pending messages are over and above the current number of messages. A pending message is one that has either been sent in a transaction and not committed, or been forwarded but has not been acknowledged.</p> ");
      }

      if (!var1.containsKey("MessagesReceivedCount")) {
         var3 = "getMessagesReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesReceivedCount", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("MessagesReceivedCount", var2);
         var2.setValue("description", "<p>The number of messages received since the last reset.</p> ");
      }

      if (!var1.containsKey("MessagesThresholdTime")) {
         var3 = "getMessagesThresholdTime";
         var4 = null;
         var2 = new PropertyDescriptor("MessagesThresholdTime", SAFStatisticsCommonMBean.class, var3, (String)var4);
         var1.put("MessagesThresholdTime", var2);
         var2.setValue("description", "<p>Returns the amount of time in the threshold condition since the last reset.</p> ");
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
      Method var3 = SAFStatisticsCommonMBean.class.getMethod("preDeregister");
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
