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
import weblogic.management.runtime.MigrationDataRuntimeMBean;

public class MigrationDataRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MigrationDataRuntimeMBean.class;

   public MigrationDataRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MigrationDataRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MigrationDataRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.cluster.singleton");
      String var3 = (new String("Runtime information about one past or ongoing migration. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.MigrationDataRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ClusterMasterName")) {
         var3 = "getClusterMasterName";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterMasterName", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterMasterName", var2);
         var2.setValue("description", "Name of the server that acted as the cluster master for this migration. ");
      }

      if (!var1.containsKey("ClusterName")) {
         var3 = "getClusterName";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterName", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterName", var2);
         var2.setValue("description", "Name of the server that acted as the cluster master for this migration. ");
      }

      if (!var1.containsKey("MachineMigratedFrom")) {
         var3 = "getMachineMigratedFrom";
         var4 = null;
         var2 = new PropertyDescriptor("MachineMigratedFrom", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MachineMigratedFrom", var2);
         var2.setValue("description", "Machine from which the server was migrated from ");
      }

      if (!var1.containsKey("MachineMigratedTo")) {
         var3 = "getMachineMigratedTo";
         var4 = null;
         var2 = new PropertyDescriptor("MachineMigratedTo", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MachineMigratedTo", var2);
         var2.setValue("description", "Machine to which the server was migrated to or is in the process of being migrated to. ");
      }

      if (!var1.containsKey("MachinesAttempted")) {
         var3 = "getMachinesAttempted";
         var4 = null;
         var2 = new PropertyDescriptor("MachinesAttempted", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MachinesAttempted", var2);
         var2.setValue("description", "Get all the machines attempted for migration. ");
      }

      if (!var1.containsKey("MigrationEndTime")) {
         var3 = "getMigrationEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("MigrationEndTime", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MigrationEndTime", var2);
         var2.setValue("description", "End time of migration ");
      }

      if (!var1.containsKey("MigrationStartTime")) {
         var3 = "getMigrationStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("MigrationStartTime", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("MigrationStartTime", var2);
         var2.setValue("description", "Start time of migration ");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerName", var2);
         var2.setValue("description", "Name of the server migrated ");
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", MigrationDataRuntimeMBean.class, var3, (String)var4);
         var1.put("Status", var2);
         var2.setValue("description", "Name of the server migrated ");
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
      Method var3 = MigrationDataRuntimeMBean.class.getMethod("preDeregister");
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
