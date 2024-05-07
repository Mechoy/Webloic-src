package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JDBCStoreMBeanImplBeanInfo extends GenericJDBCStoreMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCStoreMBean.class;

   public JDBCStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JDBCStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JDBCStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Defines an instance of the persistent store that stores its persistent records in a JDBC-accessible database. It may be used by JMS and by other subsystems.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JDBCStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DataSource")) {
         var3 = "getDataSource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDataSource";
         }

         var2 = new PropertyDescriptor("DataSource", JDBCStoreMBean.class, var3, var4);
         var1.put("DataSource", var2);
         var2.setValue("description", "<p>The JDBC data source used by this JDBC store to access its backing table.</p>  <p>The specified data source must use a non-XA JDBC driver since connection pools for XA JDBC drivers are not supported.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("DeletesPerBatchMaximum")) {
         var3 = "getDeletesPerBatchMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeletesPerBatchMaximum";
         }

         var2 = new PropertyDescriptor("DeletesPerBatchMaximum", JDBCStoreMBean.class, var3, var4);
         var1.put("DeletesPerBatchMaximum", var2);
         var2.setValue("description", "<p>The maximum number of table rows that are deleted per database call.</p>  <ul> <li> When possible, a JDBC store uses JDBC 3.0 batching to batch concurrent client requests. </li>  <li> Both the maximum batch size for concurrent inserts and for concurrent writes are configurable. </li>  <li> To disable JDBC 3.0 batching, set the maximum batch size to 1. </li>  <li> The maximum batch size has no effect on the maximum number of concurrent client requests. </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getInsertsPerBatchMaximum"), BeanInfoHelper.encodeEntities("#getDeletesPerStatementMaximum")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(20));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DeletesPerStatementMaximum")) {
         var3 = "getDeletesPerStatementMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeletesPerStatementMaximum";
         }

         var2 = new PropertyDescriptor("DeletesPerStatementMaximum", JDBCStoreMBean.class, var3, var4);
         var1.put("DeletesPerStatementMaximum", var2);
         var2.setValue("description", "<p>The maximum number of table rows that are deleted per database call.</p>  <ul> <li> Applies only when a JDBC store does not use JDBC 3.0 batching to batch concurrent client requests. </li>  <li> The maximum deletes per statement has no effect on the maximum number of concurrent client requests. </li>  <li> For some databases, the JDBC store may choose a lower value than the one configured. </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getDeletesPerBatchMaximum"), BeanInfoHelper.encodeEntities("#getInsertsPerBatchMaximum")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(20));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DeploymentOrder")) {
         var3 = "getDeploymentOrder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeploymentOrder";
         }

         var2 = new PropertyDescriptor("DeploymentOrder", JDBCStoreMBean.class, var3, var4);
         var1.put("DeploymentOrder", var2);
         var2.setValue("description", "<p>A priority that the server uses to determine when it deploys an item. The priority is relative to other deployable items of the same type.</p>  <p>For example, the server prioritizes and deploys all EJBs before it prioritizes and deploys startup classes.</p>  <p>Items with the lowest Deployment Order value are deployed first. There is no guarantee on the order of deployments with equal Deployment Order values. There is no guarantee of ordering across clusters.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InsertsPerBatchMaximum")) {
         var3 = "getInsertsPerBatchMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInsertsPerBatchMaximum";
         }

         var2 = new PropertyDescriptor("InsertsPerBatchMaximum", JDBCStoreMBean.class, var3, var4);
         var1.put("InsertsPerBatchMaximum", var2);
         var2.setValue("description", "<p>The maximum number of table rows that are inserted per database call.</p>  <ul> <li> When possible, a JDBC store uses JDBC 3.0 batching to batch concurrent client requests. </li>  <li> Both the maximum batch size for concurrent inserts and for concurrent writes are configurable. </li>  <li> To disable JDBC 3.0 batching, set the maximum batch size to 1. </li>  <li> The maximum batch size has no effect on the maximum number of concurrent client requests. </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getDeletesPerBatchMaximum"), BeanInfoHelper.encodeEntities("#getDeletesPerStatementMaximum")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(20));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LogicalName")) {
         var3 = "getLogicalName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogicalName";
         }

         var2 = new PropertyDescriptor("LogicalName", JDBCStoreMBean.class, var3, var4);
         var1.put("LogicalName", var2);
         var2.setValue("description", "<p>The name used by subsystems to refer to different stores on different servers using the same name.</p>  <p>For example, an EJB that uses the timer service may refer to its store using the logical name, and this name may be valid on multiple servers in the same cluster, even if each server has a store with a different physical name.</p>  <p>Multiple stores in the same domain or the same cluster may share the same logical name. However, a given logical name may not be assigned to more than one store on the same server.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JDBCStoreMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>The server instances or migratable targets defined in the current domain that are candidates for hosting the file store.</p>  <p>In a clustered environment, a recommended best practice is to target a custom file store to the same migratable target as the migratable JMS service, so that a member server will not be a single point of failure. A file store can also be configured to automatically migrate from an unhealthy server instance to a healthy server instance with the help of the server health monitoring services.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ThreeStepThreshold")) {
         var3 = "getThreeStepThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreeStepThreshold";
         }

         var2 = new PropertyDescriptor("ThreeStepThreshold", JDBCStoreMBean.class, var3, var4);
         var1.put("ThreeStepThreshold", var2);
         var2.setValue("description", "<p>Specifies the threshold, in bytes, when the JDBC store uses 3 steps (insert, select, populate) instead of 1 step (insert) to populate an Oracle Blob data type. </p> <p>Applies only to Oracle databases where a Blob data type is used instead of the default Long Raw data type for record data. </p> <p>The default value is 200000.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("<a href=\"../../../web.1111/e13701/store.htm#CNFGD251\">Enabling Oracle BLOB Record Columns</a>")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(200000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(4000));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("WorkerCount")) {
         var3 = "getWorkerCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWorkerCount";
         }

         var2 = new PropertyDescriptor("WorkerCount", JDBCStoreMBean.class, var3, var4);
         var1.put("WorkerCount", var2);
         var2.setValue("description", "<p>The number of JDBC store worker threads to process the workerload.</p> <ul> <li>A value of 1 indicates a single thread is used (the default).</li> <li>A value greater than 1 indicates that multiple threads are used.</li> <li>For Oracle databases, Oracle recommends users rebuild the primary key index into a reverse index for the JDBC Store table when the worker count is greater than 1.</li> <li>For non-Oracle databases, refer to the database provider's documentation for help with indexing.</li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("<a href=\"../../../web.1111/e13701/store.htm#CNFGD311\">Enabling I/O Multithreading for JDBC Stores</a>")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(1000));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("WorkerPreferredBatchSize")) {
         var3 = "getWorkerPreferredBatchSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWorkerPreferredBatchSize";
         }

         var2 = new PropertyDescriptor("WorkerPreferredBatchSize", JDBCStoreMBean.class, var3, var4);
         var1.put("WorkerPreferredBatchSize", var2);
         var2.setValue("description", "<p>Specifies the batch size when the <code>Worker Count</code> attribute is configured to a value greater than 1.</p> <p>Used to configure the workload the JDBC store incrementally puts on each worker thread. The workload consists of IO requests which are grouped and pushed to each JDBC worker thread for processing. If the IO request is very large (for example 1M), then tune this attribute to a smaller value.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("<a href=\"../../../web.1111/e13701/store.htm#CNFGD311\">Enabling I/O Multithreading for JDBC Stores</a>")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XAResourceName")) {
         var3 = "getXAResourceName";
         var4 = null;
         var2 = new PropertyDescriptor("XAResourceName", JDBCStoreMBean.class, var3, var4);
         var1.put("XAResourceName", var2);
         var2.setValue("description", "<p>Overrides the name of the XAResource that this store registers with JTA.</p>  <p>You should not normally set this attribute. Its purpose is to allow the name of the XAResource to be overridden when a store has been upgraded from an older release and the store contained prepared transactions. The generated name should be used in all other cases.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JDBCStoreMBean.class.getMethod("addTarget", TargetMBean.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Targets a server instance to a store.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = JDBCStoreMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Untargets a server instance from a store.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
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
