package weblogic.transaction.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.NonXAResourceRuntimeMBean;

public class NonXAResourceRuntimeImplBeanInfo extends JTAStatisticsImplBeanInfo {
   public static Class INTERFACE_CLASS = NonXAResourceRuntimeMBean.class;

   public NonXAResourceRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public NonXAResourceRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = NonXAResourceRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.transaction.internal");
      String var3 = (new String("This represents runtime statistical information about a NonXAResource  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.NonXAResourceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("NonXAResourceName")) {
         var3 = "getNonXAResourceName";
         var4 = null;
         var2 = new PropertyDescriptor("NonXAResourceName", NonXAResourceRuntimeMBean.class, var3, (String)var4);
         var1.put("NonXAResourceName", var2);
         var2.setValue("description", "<p>Returns the resource name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("TransactionCommittedTotalCount")) {
         var3 = "getTransactionCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionCommittedTotalCount", NonXAResourceRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionHeuristicsTotalCount")) {
         var3 = "getTransactionHeuristicsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionHeuristicsTotalCount", NonXAResourceRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionHeuristicsTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that completed with a heuristic status since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackTotalCount")) {
         var3 = "getTransactionRolledBackTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackTotalCount", NonXAResourceRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionTotalCount")) {
         var3 = "getTransactionTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionTotalCount", NonXAResourceRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions processed. This total includes all committed, rolled back, and heuristic transaction completions since the server was started.</p> ");
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
      Method var3 = NonXAResourceRuntimeMBean.class.getMethod("preDeregister");
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
