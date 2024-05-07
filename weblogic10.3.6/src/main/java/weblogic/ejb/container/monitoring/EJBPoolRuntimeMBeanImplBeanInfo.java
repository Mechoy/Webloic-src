package weblogic.ejb.container.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.EJBPoolRuntimeMBean;

public class EJBPoolRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EJBPoolRuntimeMBean.class;

   public EJBPoolRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EJBPoolRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EJBPoolRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.monitoring");
      String var3 = (new String("This interface contains accessor methods for all free pool runtime information collected for an EJB.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.EJBPoolRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AccessTotalCount")) {
         var3 = "getAccessTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("AccessTotalCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("AccessTotalCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of times an attempt was made to get an instance from the free pool.</p> ");
      }

      if (!var1.containsKey("BeansInUseCount")) {
         var3 = "getBeansInUseCount";
         var4 = null;
         var2 = new PropertyDescriptor("BeansInUseCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("BeansInUseCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of bean instances currently in use from the free pool.</p> ");
         var2.setValue("deprecated", "28-Aug-2002.  Use getBeansInUseCurrentCount() instead. ");
      }

      if (!var1.containsKey("BeansInUseCurrentCount")) {
         var3 = "getBeansInUseCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("BeansInUseCurrentCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("BeansInUseCurrentCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of bean instances currently being used from the free pool.</p> ");
      }

      if (!var1.containsKey("DestroyedTotalCount")) {
         var3 = "getDestroyedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("DestroyedTotalCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("DestroyedTotalCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of times a bean instance from this pool was destroyed due to a non-application Exception being thrown from it.</p> ");
      }

      if (!var1.containsKey("IdleBeansCount")) {
         var3 = "getIdleBeansCount";
         var4 = null;
         var2 = new PropertyDescriptor("IdleBeansCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("IdleBeansCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of available bean instances in the free pool.</p>  <p>Returns the total number of available bean instances in the free pool.</p> ");
         var2.setValue("deprecated", "28-Aug-2002.  Use getPooledBeansCurrentCount() instead. ");
      }

      if (!var1.containsKey("MissTotalCount")) {
         var3 = "getMissTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("MissTotalCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MissTotalCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of times a failed attempt was made to get an instance from the free pool. An Attempt to get a bean from the pool will fail if there are no available instances in the pool.</p> ");
      }

      if (!var1.containsKey("PooledBeansCurrentCount")) {
         var3 = "getPooledBeansCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("PooledBeansCurrentCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("PooledBeansCurrentCount", var2);
         var2.setValue("description", "<p>Provides a count of the current number of available bean instances in the free pool.</p> ");
      }

      if (!var1.containsKey("TimeoutTotalCount")) {
         var3 = "getTimeoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TimeoutTotalCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("TimeoutTotalCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of Threads that have timed out waiting for an available bean instance from the free pool.</p> ");
      }

      if (!var1.containsKey("WaiterCurrentCount")) {
         var3 = "getWaiterCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaiterCurrentCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("WaiterCurrentCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of Threads currently waiting for an available bean instance from the free pool.</p> ");
      }

      if (!var1.containsKey("WaiterTotalCount")) {
         var3 = "getWaiterTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaiterTotalCount", EJBPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("WaiterTotalCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of Threads currently waiting for an available bean instance from the free pool.</p> ");
         var2.setValue("deprecated", "28-Aug-2002.  Use getWaiterCurrentCount() instead. ");
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
      Method var3 = EJBPoolRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = EJBPoolRuntimeMBean.class.getMethod("initializePool");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Instructs the Pool to initialize itself to its configured startup time size.</p>  <p>This is a synchronous and will wait until the pool is initialized before returning.</p> ");
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
