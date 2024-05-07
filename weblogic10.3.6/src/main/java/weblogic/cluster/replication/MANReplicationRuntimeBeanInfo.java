package weblogic.cluster.replication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.MANReplicationRuntimeMBean;
import weblogic.management.runtime.ReplicationRuntimeBeanInfo;

public class MANReplicationRuntimeBeanInfo extends ReplicationRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = MANReplicationRuntimeMBean.class;

   public MANReplicationRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MANReplicationRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MANReplicationRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.cluster.replication");
      String var3 = (new String("<h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.MANReplicationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveServersInRemoteCluster")) {
         var3 = "getActiveServersInRemoteCluster";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveServersInRemoteCluster", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveServersInRemoteCluster", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("DetailedSecondariesDistribution")) {
         var3 = "getDetailedSecondariesDistribution";
         var4 = null;
         var2 = new PropertyDescriptor("DetailedSecondariesDistribution", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("DetailedSecondariesDistribution", var2);
         var2.setValue("description", "<p>Provides the names of the remote servers (such as myserver) for which the local server is hosting secondary objects. The name is appended with a number to indicate the number of secondaries hosted on behalf of that server.</p> ");
      }

      if (!var1.containsKey("PrimaryCount")) {
         var3 = "getPrimaryCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrimaryCount", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("PrimaryCount", var2);
         var2.setValue("description", "<p>Provides the number of object that the local server hosts as primaries.</p>  <p>Answer the number of object that the local server hosts as primaries.</p> ");
      }

      if (!var1.containsKey("RemoteClusterReachable")) {
         var3 = "getRemoteClusterReachable";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteClusterReachable", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("RemoteClusterReachable", var2);
         var2.setValue("description", "Answer if the remote cluster is reachable or not. ");
      }

      if (!var1.containsKey("SecondaryCount")) {
         var3 = "getSecondaryCount";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryCount", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryCount", var2);
         var2.setValue("description", "<p>Answer the number of object that the local server hosts as secondaries.</p> ");
      }

      if (!var1.containsKey("SecondaryServerDetails")) {
         var3 = "getSecondaryServerDetails";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryServerDetails", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryServerDetails", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SecondaryServerName")) {
         var3 = "getSecondaryServerName";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryServerName", MANReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryServerName", var2);
         var2.setValue("description", "<p> Answer the name of the secondary server </p> ");
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
      Method var3 = MANReplicationRuntimeMBean.class.getMethod("preDeregister");
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
