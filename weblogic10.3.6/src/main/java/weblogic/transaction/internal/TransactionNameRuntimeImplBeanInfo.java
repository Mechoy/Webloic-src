package weblogic.transaction.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.TransactionNameRuntimeMBean;

public class TransactionNameRuntimeImplBeanInfo extends JTATransactionStatisticsImplBeanInfo {
   public static Class INTERFACE_CLASS = TransactionNameRuntimeMBean.class;

   public TransactionNameRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public TransactionNameRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = TransactionNameRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.transaction.internal");
      String var3 = (new String("This interface represents runtime statistics for a transaction name category.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.TransactionNameRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("SecondsActiveTotalCount")) {
         var3 = "getSecondsActiveTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SecondsActiveTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondsActiveTotalCount", var2);
         var2.setValue("description", "<p>The total number of seconds that transactions were active for all committed transactions.</p> ");
      }

      if (!var1.containsKey("TransactionAbandonedTotalCount")) {
         var3 = "getTransactionAbandonedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionAbandonedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionAbandonedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions that were abandoned since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionCommittedTotalCount")) {
         var3 = "getTransactionCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionCommittedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionHeuristicsTotalCount")) {
         var3 = "getTransactionHeuristicsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionHeuristicsTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionHeuristicsTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that completed with a heuristic status since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionLLRCommittedTotalCount")) {
         var3 = "getTransactionLLRCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionLLRCommittedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionLLRCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of LLR transactions that were committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionName")) {
         var3 = "getTransactionName";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionName", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionName", var2);
         var2.setValue("description", "<p>The transaction name.</p> ");
      }

      if (!var1.containsKey("TransactionNoResourcesCommittedTotalCount")) {
         var3 = "getTransactionNoResourcesCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionNoResourcesCommittedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionNoResourcesCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with no enlisted resources that were committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionOneResourceOnePhaseCommittedTotalCount")) {
         var3 = "getTransactionOneResourceOnePhaseCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionOneResourceOnePhaseCommittedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionOneResourceOnePhaseCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with only one enlisted resource that were one-phase committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionReadOnlyOnePhaseCommittedTotalCount")) {
         var3 = "getTransactionReadOnlyOnePhaseCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionReadOnlyOnePhaseCommittedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionReadOnlyOnePhaseCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with more than one enlisted resource that were one-phase committed due to read-only optimization since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackAppTotalCount")) {
         var3 = "getTransactionRolledBackAppTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackAppTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackAppTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to an application error.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackResourceTotalCount")) {
         var3 = "getTransactionRolledBackResourceTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackResourceTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackResourceTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to a resource error.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackSystemTotalCount")) {
         var3 = "getTransactionRolledBackSystemTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackSystemTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackSystemTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to an internal system error.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackTimeoutTotalCount")) {
         var3 = "getTransactionRolledBackTimeoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackTimeoutTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackTimeoutTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to a timeout expiration.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackTotalCount")) {
         var3 = "getTransactionRolledBackTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionTotalCount")) {
         var3 = "getTransactionTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions processed. This total includes all committed, rolled back, and heuristic transaction completions since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionTwoPhaseCommittedTotalCount")) {
         var3 = "getTransactionTwoPhaseCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionTwoPhaseCommittedTotalCount", TransactionNameRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionTwoPhaseCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with more than one enlisted resource that were two-phase committed since the server was started.</p> ");
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
      Method var3 = TransactionNameRuntimeMBean.class.getMethod("preDeregister");
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
