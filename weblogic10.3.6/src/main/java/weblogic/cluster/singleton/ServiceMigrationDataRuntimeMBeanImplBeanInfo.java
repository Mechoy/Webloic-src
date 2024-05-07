package weblogic.cluster.singleton;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ServiceMigrationDataRuntimeMBean;

public class ServiceMigrationDataRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServiceMigrationDataRuntimeMBean.class;

   public ServiceMigrationDataRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServiceMigrationDataRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServiceMigrationDataRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.cluster.singleton");
      String var3 = (new String("Runtime information about one past or ongoing migration. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServiceMigrationDataRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ClusterName")) {
         var3 = "getClusterName";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterName", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterName", var2);
         var2.setValue("description", "Name of the cluster ");
      }

      if (!var1.containsKey("CoordinatorName")) {
         var3 = "getCoordinatorName";
         var4 = null;
         var2 = new PropertyDescriptor("CoordinatorName", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("CoordinatorName", var2);
         var2.setValue("description", "Name of the server that acted as the coordinator for this migration. ");
      }

      if (!var1.containsKey("DestinationsAttempted")) {
         var3 = "getDestinationsAttempted";
         var4 = null;
         var2 = new PropertyDescriptor("DestinationsAttempted", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("DestinationsAttempted", var2);
         var2.setValue("description", "Get all the destinations attempted for migration. ");
      }

      if (!var1.containsKey("MigratedFrom")) {
         var3 = "getMigratedFrom";
         var4 = null;
         var2 = new PropertyDescriptor("MigratedFrom", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MigratedFrom", var2);
         var2.setValue("description", "Where this object was migrated from ");
      }

      if (!var1.containsKey("MigratedTo")) {
         var3 = "getMigratedTo";
         var4 = null;
         var2 = new PropertyDescriptor("MigratedTo", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MigratedTo", var2);
         var2.setValue("description", "Where this object was migrated to ");
      }

      if (!var1.containsKey("MigrationEndTime")) {
         var3 = "getMigrationEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("MigrationEndTime", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MigrationEndTime", var2);
         var2.setValue("description", "End time of migration ");
      }

      if (!var1.containsKey("MigrationStartTime")) {
         var3 = "getMigrationStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("MigrationStartTime", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MigrationStartTime", var2);
         var2.setValue("description", "Start time of migration ");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerName", var2);
         var2.setValue("description", "Name of the object migrated ");
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", ServiceMigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("Status", var2);
         var2.setValue("description", "Status of the migration (SUCCESSFUL, IN_PROGRESS, or FAILED) ");
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
      Method var3 = ServiceMigrationDataRuntimeMBean.class.getMethod("preDeregister");
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
