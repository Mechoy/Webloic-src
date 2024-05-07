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
import weblogic.management.runtime.ServerMigrationRuntimeMBean;

public class ServerMigrationRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerMigrationRuntimeMBean.class;

   public ServerMigrationRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerMigrationRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerMigrationRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.cluster.singleton");
      String var3 = (new String("ServerMigrationRuntimeMBean provides runtime monitoring information about the past migrations performed by this server as the cluster master. If this server was never the cluster master then no information would be available. This RuntimeMBean would be hosted on all cluster members and can be queried for the location of the cluster master which is just another peer in the cluster. JMX clients can make another call to the server hosting the cluster master functionality to get the migration history. <p> Please note that the migration history is not persisted and is lost when a server is shutdown. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServerMigrationRuntimeMBean");
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
         var2 = new PropertyDescriptor("ClusterMasterName", ServerMigrationRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterMasterName", var2);
         var2.setValue("description", "Returns the server name who is the cluster master. Migration history is only available from the cluster master. ");
      }

      if (!var1.containsKey("MigrationData")) {
         var3 = "getMigrationData";
         var4 = null;
         var2 = new PropertyDescriptor("MigrationData", ServerMigrationRuntimeMBean.class, var3, (String)var4);
         var1.put("MigrationData", var2);
         var2.setValue("description", "Returns the migrations performed by this server as the cluster master. Returns <code>null</code> if there is no history available. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ClusterMaster")) {
         var3 = "isClusterMaster";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterMaster", ServerMigrationRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterMaster", var2);
         var2.setValue("description", "Is the current server the cluster master? ");
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
      Method var3 = ServerMigrationRuntimeMBean.class.getMethod("preDeregister");
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
