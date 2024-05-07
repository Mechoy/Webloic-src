package weblogic.transaction.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import javax.transaction.xa.Xid;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JTARuntimeMBean;

public class JTARuntimeImplBeanInfo extends JTATransactionStatisticsImplBeanInfo {
   public static Class INTERFACE_CLASS = JTARuntimeMBean.class;

   public JTARuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JTARuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JTARuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.transaction.internal");
      String var3 = (new String("This interface is used for accessing transaction runtime characteristics within a WebLogic server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JTARuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveTransactionsTotalCount")) {
         var3 = "getActiveTransactionsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveTransactionsTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveTransactionsTotalCount", var2);
         var2.setValue("description", "<p>The number of active transactions on the server.</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of the JTA subsystem. See <a href=../../e13941/weblogic/health/HealthState.html>weblogic.health.HealthState</a> for state values.  </p> ");
      }

      if (!var1.containsKey("JTATransactions")) {
         var3 = "getJTATransactions";
         var4 = null;
         var2 = new PropertyDescriptor("JTATransactions", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("JTATransactions", var2);
         var2.setValue("description", "<p>An array of <code>JTATransaction</code> objects. Each object provides detailed information regarding an active transaction.</p> ");
      }

      String[] var5;
      if (!var1.containsKey("NonXAResourceRuntimeMBeans")) {
         var3 = "getNonXAResourceRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("NonXAResourceRuntimeMBeans", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("NonXAResourceRuntimeMBeans", var2);
         var2.setValue("description", "<p>An array of <code>NonXAResourceRuntimeMBeans</code> that each represents the statistics for a non-XA resource.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.NonXAResourceRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("RecoveryRuntimeMBeans")) {
         var3 = "getRecoveryRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("RecoveryRuntimeMBeans", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("RecoveryRuntimeMBeans", var2);
         var2.setValue("description", "<p>Returns the runtime MBeans for the Transaction Recovery Services that were deployed on this server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("RegisteredNonXAResourceNames")) {
         var3 = "getRegisteredNonXAResourceNames";
         var4 = null;
         var2 = new PropertyDescriptor("RegisteredNonXAResourceNames", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("RegisteredNonXAResourceNames", var2);
         var2.setValue("description", "<p>An array of NonXA resource names that are registered with the transaction manager.</p> ");
      }

      if (!var1.containsKey("RegisteredResourceNames")) {
         var3 = "getRegisteredResourceNames";
         var4 = null;
         var2 = new PropertyDescriptor("RegisteredResourceNames", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("RegisteredResourceNames", var2);
         var2.setValue("description", "<p>An array of XA resource names that are registered with the transaction manager.</p> ");
      }

      if (!var1.containsKey("SecondsActiveTotalCount")) {
         var3 = "getSecondsActiveTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SecondsActiveTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("SecondsActiveTotalCount", var2);
         var2.setValue("description", "<p>The total number of seconds that transactions were active for all committed transactions.</p> ");
      }

      if (!var1.containsKey("TransactionAbandonedTotalCount")) {
         var3 = "getTransactionAbandonedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionAbandonedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionAbandonedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions that were abandoned since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionCommittedTotalCount")) {
         var3 = "getTransactionCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionCommittedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionHeuristicsTotalCount")) {
         var3 = "getTransactionHeuristicsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionHeuristicsTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionHeuristicsTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that completed with a heuristic status since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionLLRCommittedTotalCount")) {
         var3 = "getTransactionLLRCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionLLRCommittedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionLLRCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of LLR transactions that were committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionLogStoreRuntimeMBean")) {
         var3 = "getTransactionLogStoreRuntimeMBean";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionLogStoreRuntimeMBean", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionLogStoreRuntimeMBean", var2);
         var2.setValue("description", "<p>Returns the runtime MBean for the primary TLOG persistent store, regardless of it is default store or JDBC store. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("TransactionNameRuntimeMBeans")) {
         var3 = "getTransactionNameRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionNameRuntimeMBeans", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionNameRuntimeMBeans", var2);
         var2.setValue("description", "<p>An array of <code>TransactionNameRuntimeMBeans</code> that represent statistics for all transactions in the server, categorized by transaction name.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.TransactionNameRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("TransactionNoResourcesCommittedTotalCount")) {
         var3 = "getTransactionNoResourcesCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionNoResourcesCommittedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionNoResourcesCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with no enlisted resources that were committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionOneResourceOnePhaseCommittedTotalCount")) {
         var3 = "getTransactionOneResourceOnePhaseCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionOneResourceOnePhaseCommittedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionOneResourceOnePhaseCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with only one enlisted resource that were one-phase committed since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionReadOnlyOnePhaseCommittedTotalCount")) {
         var3 = "getTransactionReadOnlyOnePhaseCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionReadOnlyOnePhaseCommittedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionReadOnlyOnePhaseCommittedTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions with more than one enlisted resource that were one-phase committed due to read-only optimization since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionResourceRuntimeMBeans")) {
         var3 = "getTransactionResourceRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionResourceRuntimeMBeans", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionResourceRuntimeMBeans", var2);
         var2.setValue("description", "<p>An array of <code>TransactionResourceRuntimeMBeans</code> that each represents the statistics for a transaction resource.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.TransactionResourceRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("TransactionRolledBackAppTotalCount")) {
         var3 = "getTransactionRolledBackAppTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackAppTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackAppTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to an application error.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackResourceTotalCount")) {
         var3 = "getTransactionRolledBackResourceTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackResourceTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackResourceTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to a resource error.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackSystemTotalCount")) {
         var3 = "getTransactionRolledBackSystemTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackSystemTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackSystemTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to an internal system error.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackTimeoutTotalCount")) {
         var3 = "getTransactionRolledBackTimeoutTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackTimeoutTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackTimeoutTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back due to a timeout expiration.</p> ");
      }

      if (!var1.containsKey("TransactionRolledBackTotalCount")) {
         var3 = "getTransactionRolledBackTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionRolledBackTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionRolledBackTotalCount", var2);
         var2.setValue("description", "<p>The number of transactions that were rolled back since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionTotalCount")) {
         var3 = "getTransactionTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionTotalCount", JTARuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionTotalCount", var2);
         var2.setValue("description", "<p>The total number of transactions processed. This total includes all committed, rolled back, and heuristic transaction completions since the server was started.</p> ");
      }

      if (!var1.containsKey("TransactionTwoPhaseCommittedTotalCount")) {
         var3 = "getTransactionTwoPhaseCommittedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionTwoPhaseCommittedTotalCount", JTARuntimeMBean.class, var3, (String)var4);
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
      Method var3 = JTARuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JTARuntimeMBean.class.getMethod("getTransactionsOlderThan", Integer.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("seconds", "The transaction duration in seconds qualifier. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>An array of <code>JTATransaction</code> objects. Each object provides detailed information regarding an active transaction that has existed for a period longer than the time specified.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JTARuntimeMBean.class.getMethod("getRecoveryRuntimeMBean", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("serverName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the runtime MBean for the Transaction Recovery Service of the specified server. If the Transaction Recovery Service of the specified server is not deployed on this server, null will be returned.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JTARuntimeMBean.class.getMethod("forceLocalRollback", Xid.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("xid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Forces the transaction represented by xid to be rolled-back at the local SubCoordinator only.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JTARuntimeMBean.class.getMethod("forceGlobalRollback", Xid.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("xid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Forces the transaction represented by xid to be rolled-back at all participating SubCoordinators. If the server on which the method is invoked is not the coordinating server then the coordinating server will be notified to process the rollback.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JTARuntimeMBean.class.getMethod("forceLocalCommit", Xid.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("xid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Forces the transaction represented by xid to be committed at the local SubCoordinator only.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JTARuntimeMBean.class.getMethod("forceGlobalCommit", Xid.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("xid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Forces the transaction represented by xid to be committed at all participating SubCoordinators. If the server on which the method is invoked is not the coordinating server then the coordinating server will be notified to process the commit.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JTARuntimeMBean.class.getMethod("getJTATransaction", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("xid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the JTATransaction information object for the specified Xid. If the transaction represented by xid does not exist on the server, then the method will return null.</p> ");
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
