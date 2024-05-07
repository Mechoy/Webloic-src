package weblogic.ejb.container.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;

public class MessageDrivenEJBRuntimeMBeanImplBeanInfo extends EJBRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MessageDrivenEJBRuntimeMBean.class;

   public MessageDrivenEJBRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MessageDrivenEJBRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MessageDrivenEJBRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.monitoring");
      String var3 = (new String("This interface contains accessor methods for all EJB runtime information collected for a Message Driven Bean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.MessageDrivenEJBRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ConnectionStatus")) {
         var3 = "getConnectionStatus";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionStatus", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionStatus", var2);
         var2.setValue("description", "<p>Provides the connection status for the Message Driven Bean. ConnectionStatus can be Connected or Reconnecting.</p> ");
      }

      if (!var1.containsKey("Destination")) {
         var3 = "getDestination";
         var4 = null;
         var2 = new PropertyDescriptor("Destination", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("Destination", var2);
         var2.setValue("description", "<p>Provides a count of the Message Driven Bean destination</p> ");
      }

      if (!var1.containsKey("EJBName")) {
         var3 = "getEJBName";
         var4 = null;
         var2 = new PropertyDescriptor("EJBName", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("EJBName", var2);
         var2.setValue("description", "<p>Provides the ejb-name for this EJB as defined in the ejb-jar.xml deployment descriptor.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of this MDB.</p> ");
      }

      if (!var1.containsKey("JmsClientID")) {
         var3 = "getJmsClientID";
         var4 = null;
         var2 = new PropertyDescriptor("JmsClientID", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("JmsClientID", var2);
         var2.setValue("description", "<p>Provides a count of the Message Driven Bean jmsClientID</p> ");
      }

      if (!var1.containsKey("LastException")) {
         var3 = "getLastException";
         var4 = null;
         var2 = new PropertyDescriptor("LastException", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("LastException", var2);
         var2.setValue("description", "<p>Provides the last exception this MDB encountered</p> ");
      }

      if (!var1.containsKey("LastExceptionAsString")) {
         var3 = "getLastExceptionAsString";
         var4 = null;
         var2 = new PropertyDescriptor("LastExceptionAsString", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("LastExceptionAsString", var2);
         var2.setValue("description", "<p>Provides the last exception as String this MDB encountered</p> ");
      }

      if (!var1.containsKey("MDBStatus")) {
         var3 = "getMDBStatus";
         var4 = null;
         var2 = new PropertyDescriptor("MDBStatus", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("MDBStatus", var2);
         var2.setValue("description", "<p>Provides the Message Driven Bean status.  MDBStatus is used after the MDB is connected to the destination. MDBStatus can be Running or Suspended.</p> ");
      }

      if (!var1.containsKey("PoolRuntime")) {
         var3 = "getPoolRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("PoolRuntime", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolRuntime", var2);
         var2.setValue("description", "<p>Provides runtime information about the free pool for this EJB.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ProcessedMessageCount")) {
         var3 = "getProcessedMessageCount";
         var4 = null;
         var2 = new PropertyDescriptor("ProcessedMessageCount", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("ProcessedMessageCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of messages processed by this Message Driven Bean.</p> ");
      }

      if (!var1.containsKey("Resources")) {
         var3 = "getResources";
         var4 = null;
         var2 = new PropertyDescriptor("Resources", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("Resources", var2);
         var2.setValue("description", "<p>Provides a list of the RuntimeMBeans for the resources used by this EJB. This will always include an ExecuteQueueRuntimeMBean. It will also include a JMSDestinationRuntimeMBean for MessageDriven beans and a JDBCConnectionPoolMBean for CMP Entity beans.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SuspendCount")) {
         var3 = "getSuspendCount";
         var4 = null;
         var2 = new PropertyDescriptor("SuspendCount", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("SuspendCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of times this MDB is suspended by the user or the EJB container.</p> ");
      }

      if (!var1.containsKey("TimerRuntime")) {
         var3 = "getTimerRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("TimerRuntime", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("TimerRuntime", var2);
         var2.setValue("description", "<p>Provides runtime information about any EJB Timers created, for this EJB. If the bean class for this EJB does not implement javax.ejb.TimedObject, null will be returned.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("TransactionRuntime")) {
         var3 = "getTransactionRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRuntime", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRuntime", var2);
         var2.setValue("description", "<p>Provides the EJBTransactionRuntimeMBean, containing runtime transaction counts, for this EJB.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JMSConnectionAlive")) {
         var3 = "isJMSConnectionAlive";
         var4 = null;
         var2 = new PropertyDescriptor("JMSConnectionAlive", MessageDrivenEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("JMSConnectionAlive", var2);
         var2.setValue("description", "<p>Provides information about whether the Message Driven Bean is currently connected to the JMS destination it is mapped to.</p>  <p>Returns whether the Message Driven Bean is currently connected to the JMS destination it is mapped to.</p> ");
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
      Method var3 = MessageDrivenEJBRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = MessageDrivenEJBRuntimeMBean.class.getMethod("suspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Suspends the specific type of MDB by calling stop on the JMS Connection.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MessageDrivenEJBRuntimeMBean.class.getMethod("resume");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes the specific type of MDB by calling start on the JMS Connection.</p> ");
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
