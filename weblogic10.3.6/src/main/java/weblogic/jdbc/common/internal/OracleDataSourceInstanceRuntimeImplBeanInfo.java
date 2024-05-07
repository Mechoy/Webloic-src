package weblogic.jdbc.common.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JDBCOracleDataSourceInstanceRuntimeMBean;

public class OracleDataSourceInstanceRuntimeImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCOracleDataSourceInstanceRuntimeMBean.class;

   public OracleDataSourceInstanceRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public OracleDataSourceInstanceRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = OracleDataSourceInstanceRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.4.0");
      var2.setValue("package", "weblogic.jdbc.common.internal");
      String var3 = (new String("Runtime MBean for monitoring a JDBC GridLink Data Source instance. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JDBCOracleDataSourceInstanceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveConnectionsCurrentCount")) {
         var3 = "getActiveConnectionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsCurrentCount", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveConnectionsCurrentCount", var2);
         var2.setValue("description", "<p> The number of connections currently in use by applications. </p> ");
      }

      if (!var1.containsKey("ConnectionsTotalCount")) {
         var3 = "getConnectionsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsTotalCount", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsTotalCount", var2);
         var2.setValue("description", "<p> The cumulative total number of database connections created in this instance since the data source was deployed. </p> ");
      }

      if (!var1.containsKey("CurrCapacity")) {
         var3 = "getCurrCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("CurrCapacity", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrCapacity", var2);
         var2.setValue("description", "<p> The current count of JDBC connections in the connection pool in the data source for this instance. </p> ");
      }

      if (!var1.containsKey("CurrentWeight")) {
         var3 = "getCurrentWeight";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentWeight", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentWeight", var2);
         var2.setValue("description", "The current weight of the instance. ");
      }

      if (!var1.containsKey("InstanceName")) {
         var3 = "getInstanceName";
         var4 = null;
         var2 = new PropertyDescriptor("InstanceName", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("InstanceName", var2);
         var2.setValue("description", "The name of this instance. ");
      }

      if (!var1.containsKey("NumAvailable")) {
         var3 = "getNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumAvailable", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("NumAvailable", var2);
         var2.setValue("description", "<p> The number of database connections currently available (not in use) in this data source for this instance. </p> ");
      }

      if (!var1.containsKey("NumUnavailable")) {
         var3 = "getNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumUnavailable", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("NumUnavailable", var2);
         var2.setValue("description", "<p> The number of database connections that are currently unavailable (in use or being tested by the system) in this instance. </p> ");
      }

      if (!var1.containsKey("ReserveRequestCount")) {
         var3 = "getReserveRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("ReserveRequestCount", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("ReserveRequestCount", var2);
         var2.setValue("description", "<p> The cumulative, running count of requests for a connection from this instance. </p> ");
      }

      if (!var1.containsKey("Signature")) {
         var3 = "getSignature";
         var4 = null;
         var2 = new PropertyDescriptor("Signature", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("Signature", var2);
         var2.setValue("description", "The signature that uniquely identifies the instance. ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p> The current state of the instance within the data source. </p> <p> Possible states are: </p> <ul> <li>Enabled - the instance is enabled indicating that there are connections established</li> <li>Disabled - the instance is disabled due to unavailability</li> </ul> ");
      }

      if (!var1.containsKey("AffEnabled")) {
         var3 = "isAffEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("AffEnabled", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("AffEnabled", var2);
         var2.setValue("description", "The value from the latest load-balancing advisory <code>aff</code> flag for a GridLink data source instance. ");
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("Enabled", JDBCOracleDataSourceInstanceRuntimeMBean.class, var3, (String)var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "Indicates whether the instance is enabled or disabled: <ul> <li>true if the instance is enabled.</li> <li>false if the instance is disabled.</li> </ul> ");
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
      Method var3 = JDBCOracleDataSourceInstanceRuntimeMBean.class.getMethod("preDeregister");
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
