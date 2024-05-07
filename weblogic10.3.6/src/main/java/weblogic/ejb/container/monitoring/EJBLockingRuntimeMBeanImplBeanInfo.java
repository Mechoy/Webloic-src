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
import weblogic.management.runtime.EJBLockingRuntimeMBean;

public class EJBLockingRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EJBLockingRuntimeMBean.class;

   public EJBLockingRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EJBLockingRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EJBLockingRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.monitoring");
      String var3 = (new String("This interface contains accessor methods for all lock manager runtime information collected for an EJB.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.EJBLockingRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("LockEntriesCurrentCount")) {
         var3 = "getLockEntriesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("LockEntriesCurrentCount", EJBLockingRuntimeMBean.class, var3, (String)var4);
         var1.put("LockEntriesCurrentCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of beans currently locked.</p> ");
      }

      if (!var1.containsKey("LockManagerAccessCount")) {
         var3 = "getLockManagerAccessCount";
         var4 = null;
         var2 = new PropertyDescriptor("LockManagerAccessCount", EJBLockingRuntimeMBean.class, var3, (String)var4);
         var1.put("LockManagerAccessCount", var2);
         var2.setValue("description", "<p>Provides the total number of attempts to obtain a lock on a bean. This includes attempts to obtain a lock on a bean that is already locked on behalf of the client.</p> ");
      }

      if (!var1.containsKey("TimeoutTotalCount")) {
         var3 = "getTimeoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TimeoutTotalCount", EJBLockingRuntimeMBean.class, var3, (String)var4);
         var1.put("TimeoutTotalCount", var2);
         var2.setValue("description", "<p>Provides the current number of Threads that have timed out waiting for a lock on a bean.</p> ");
      }

      if (!var1.containsKey("WaiterCurrentCount")) {
         var3 = "getWaiterCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaiterCurrentCount", EJBLockingRuntimeMBean.class, var3, (String)var4);
         var1.put("WaiterCurrentCount", var2);
         var2.setValue("description", "<p>Provides the current number of Threads that have waited for a lock on a bean.</p> ");
      }

      if (!var1.containsKey("WaiterTotalCount")) {
         var3 = "getWaiterTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaiterTotalCount", EJBLockingRuntimeMBean.class, var3, (String)var4);
         var1.put("WaiterTotalCount", var2);
         var2.setValue("description", "<p>Provides the total number of Threads that have waited for a lock on a bean.</p>  <p>Returns the total number of Threads that have waited for a lock on a bean.</p> ");
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
      Method var3 = EJBLockingRuntimeMBean.class.getMethod("preDeregister");
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
