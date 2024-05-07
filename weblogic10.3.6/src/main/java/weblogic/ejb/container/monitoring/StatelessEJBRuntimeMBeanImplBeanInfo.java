package weblogic.ejb.container.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.StatelessEJBRuntimeMBean;

public class StatelessEJBRuntimeMBeanImplBeanInfo extends EJBRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = StatelessEJBRuntimeMBean.class;

   public StatelessEJBRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public StatelessEJBRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = StatelessEJBRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.monitoring");
      String var3 = (new String("This interface contains accessor methods for all EJB runtime information collected for a Stateless Session Bean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.StatelessEJBRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("EJBName")) {
         var3 = "getEJBName";
         var4 = null;
         var2 = new PropertyDescriptor("EJBName", StatelessEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("EJBName", var2);
         var2.setValue("description", "<p>Provides the ejb-name for this EJB as defined in the ejb-jar.xml deployment descriptor.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("PoolRuntime")) {
         var3 = "getPoolRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("PoolRuntime", StatelessEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolRuntime", var2);
         var2.setValue("description", "<p>Provides the runtime information about the free pool for this EJB.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Resources")) {
         var3 = "getResources";
         var4 = null;
         var2 = new PropertyDescriptor("Resources", StatelessEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("Resources", var2);
         var2.setValue("description", "<p>Provides a list of the RuntimeMBeans for the resources used by this EJB. This will always include an ExecuteQueueRuntimeMBean. It will also include a JMSDestinationRuntimeMBean for MessageDriven beans and a JDBCConnectionPoolMBean for CMP Entity beans.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("TimerRuntime")) {
         var3 = "getTimerRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("TimerRuntime", StatelessEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("TimerRuntime", var2);
         var2.setValue("description", "<p>Provides runtime information about any EJB Timers created for this EJB. If the bean class for this EJB does not implement javax.ejb.TimedObject, null will be returned.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("TransactionRuntime")) {
         var3 = "getTransactionRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRuntime", StatelessEJBRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRuntime", var2);
         var2.setValue("description", "<p>Provides the EJBTransactionRuntimeMBean, containing runtime transaction counts, for this EJB.</p> ");
         var2.setValue("relationship", "containment");
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
      Method var3 = StatelessEJBRuntimeMBean.class.getMethod("preDeregister");
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
