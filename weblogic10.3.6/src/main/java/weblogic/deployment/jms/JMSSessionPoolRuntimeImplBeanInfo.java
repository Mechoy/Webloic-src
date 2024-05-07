package weblogic.deployment.jms;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JMSPooledConnectionRuntimeMBean;

public class JMSSessionPoolRuntimeImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSPooledConnectionRuntimeMBean.class;

   public JMSSessionPoolRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSSessionPoolRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSSessionPoolRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.deployment.jms");
      String var3 = (new String("This class is used for monitoring pooled JMS connections. A pooled JMS connection is a session pool used by EJBs and servlets that use a resource-reference element in their EJB or Servlet deployment descriptor to define their JMS connection factories.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JMSPooledConnectionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AverageReserved")) {
         var3 = "getAverageReserved";
         var4 = null;
         var2 = new PropertyDescriptor("AverageReserved", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageReserved", var2);
         var2.setValue("description", "<p>The average number of JMS sessions that have been in use in this instance of the session pool since it was instantiated. This generally happens when an EJB or servlet is deployed that requires the session pool.</p> ");
      }

      if (!var1.containsKey("CreationDelayTime")) {
         var3 = "getCreationDelayTime";
         var4 = null;
         var2 = new PropertyDescriptor("CreationDelayTime", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("CreationDelayTime", var2);
         var2.setValue("description", "<p>The average amount of time that it takes to create each JMS session in the session pool.</p> ");
      }

      if (!var1.containsKey("CurrCapacity")) {
         var3 = "getCurrCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("CurrCapacity", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrCapacity", var2);
         var2.setValue("description", "<p>The current capacity of the session pool, which is always less than or equal to the maximum capacity of JMS sessions.</p> ");
      }

      if (!var1.containsKey("HighestNumAvailable")) {
         var3 = "getHighestNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumAvailable", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumAvailable", var2);
         var2.setValue("description", "<p>The highest number of available JMS sessions in this instance of the session pool since it was instantiated.</p> ");
      }

      if (!var1.containsKey("HighestNumReserved")) {
         var3 = "getHighestNumReserved";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumReserved", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumReserved", var2);
         var2.setValue("description", "<p>The highest number of concurrent JMS sessions reserved for this instance of the session pool since it was instantiated.</p> ");
      }

      if (!var1.containsKey("HighestNumUnavailable")) {
         var3 = "getHighestNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumUnavailable", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumUnavailable", var2);
         var2.setValue("description", "<p>The highest number of unusable JMS sessions in this instance of the session pool since it was instantiated.</p> ");
      }

      if (!var1.containsKey("HighestNumWaiters")) {
         var3 = "getHighestNumWaiters";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumWaiters", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumWaiters", var2);
         var2.setValue("description", "<p>The highest number of threads waiting to retrieve a JMS session in this instance of the session pool since it was instantiated.</p> ");
      }

      if (!var1.containsKey("HighestWaitSeconds")) {
         var3 = "getHighestWaitSeconds";
         var4 = null;
         var2 = new PropertyDescriptor("HighestWaitSeconds", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestWaitSeconds", var2);
         var2.setValue("description", "<p>The highest number of seconds that an application waited to retrieve a JMS session in this instance of the session pool since it was instantiated.</p> ");
      }

      if (!var1.containsKey("MaxCapacity")) {
         var3 = "getMaxCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("MaxCapacity", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxCapacity", var2);
         var2.setValue("description", "<p>The maximum number of JMS sessions that can be allocated using the session pool.</p> ");
      }

      if (!var1.containsKey("NumAvailable")) {
         var3 = "getNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumAvailable", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumAvailable", var2);
         var2.setValue("description", "<p>The number of available JMS sessions in the session pool that are not currently in use.</p> ");
      }

      if (!var1.containsKey("NumConnectionObjects")) {
         var3 = "getNumConnectionObjects";
         var4 = null;
         var2 = new PropertyDescriptor("NumConnectionObjects", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumConnectionObjects", var2);
         var2.setValue("description", "<p>The number of JMS connections that back this session pool. This value may be greater than one if different sessions were created using different combinations of a username and password to contact the JMS server.</p> ");
      }

      if (!var1.containsKey("NumFailuresToRefresh")) {
         var3 = "getNumFailuresToRefresh";
         var4 = null;
         var2 = new PropertyDescriptor("NumFailuresToRefresh", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumFailuresToRefresh", var2);
         var2.setValue("description", "<p>The number of failed attempts to create a JMS session in the session pool.</p> ");
      }

      if (!var1.containsKey("NumLeaked")) {
         var3 = "getNumLeaked";
         var4 = null;
         var2 = new PropertyDescriptor("NumLeaked", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumLeaked", var2);
         var2.setValue("description", "<p>The number of JMS sessions that were removed from the session pool, but were not returned.</p> ");
      }

      if (!var1.containsKey("NumReserved")) {
         var3 = "getNumReserved";
         var4 = null;
         var2 = new PropertyDescriptor("NumReserved", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumReserved", var2);
         var2.setValue("description", "<p>The number of JMS sessions that are currently in use.</p> ");
      }

      if (!var1.containsKey("NumUnavailable")) {
         var3 = "getNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumUnavailable", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumUnavailable", var2);
         var2.setValue("description", "<p>The number of JMS sessions in the session pool that are not currently usable for some reason.</p> ");
      }

      if (!var1.containsKey("NumWaiters")) {
         var3 = "getNumWaiters";
         var4 = null;
         var2 = new PropertyDescriptor("NumWaiters", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("NumWaiters", var2);
         var2.setValue("description", "<p>The number of threads waiting to retrieve a JMS session from the session pool.</p> ");
      }

      if (!var1.containsKey("TotalNumAllocated")) {
         var3 = "getTotalNumAllocated";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumAllocated", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNumAllocated", var2);
         var2.setValue("description", "<p>The total number of JMS sessions allocated by this session pool in this instance of the session pool since it was instantiated.</p> ");
      }

      if (!var1.containsKey("TotalNumDestroyed")) {
         var3 = "getTotalNumDestroyed";
         var4 = null;
         var2 = new PropertyDescriptor("TotalNumDestroyed", JMSPooledConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalNumDestroyed", var2);
         var2.setValue("description", "<p>The total number of JMS sessions that were created and then destroyed in this instance of the session pool since it was instantiated.</p> ");
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
      Method var3 = JMSPooledConnectionRuntimeMBean.class.getMethod("preDeregister");
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
