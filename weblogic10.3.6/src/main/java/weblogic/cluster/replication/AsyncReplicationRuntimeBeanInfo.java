package weblogic.cluster.replication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.AsyncReplicationRuntimeMBean;
import weblogic.management.runtime.ReplicationRuntimeBeanInfo;

public class AsyncReplicationRuntimeBeanInfo extends ReplicationRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = AsyncReplicationRuntimeMBean.class;

   public AsyncReplicationRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public AsyncReplicationRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = AsyncReplicationRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.0.0");
      var2.setValue("package", "weblogic.cluster.replication");
      String var3 = (new String("This MBean provides runtime statistics for async replication in a WebLogic Cluster ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.AsyncReplicationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("DetailedSecondariesDistribution")) {
         var3 = "getDetailedSecondariesDistribution";
         var4 = null;
         var2 = new PropertyDescriptor("DetailedSecondariesDistribution", AsyncReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("DetailedSecondariesDistribution", var2);
         var2.setValue("description", "<p>Provides the names of the remote servers (such as myserver) for which the local server is hosting secondary objects. The name is appended with a number to indicate the number of secondaries hosted on behalf of that server.</p> ");
      }

      if (!var1.containsKey("LastSessionsFlushTime")) {
         var3 = "getLastSessionsFlushTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastSessionsFlushTime", AsyncReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastSessionsFlushTime", var2);
         var2.setValue("description", "<p>The last time the sessions were flushed, in milliseconds since midnight, January 1, 1970 UTC</p> ");
      }

      if (!var1.containsKey("PrimaryCount")) {
         var3 = "getPrimaryCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrimaryCount", AsyncReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("PrimaryCount", var2);
         var2.setValue("description", "<p>Provides the number of object that the local server hosts as primaries.</p>  <p>Answer the number of object that the local server hosts as primaries.</p> ");
      }

      if (!var1.containsKey("SecondaryCount")) {
         var3 = "getSecondaryCount";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryCount", AsyncReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryCount", var2);
         var2.setValue("description", "<p>Answer the number of object that the local server hosts as secondaries.</p> ");
      }

      if (!var1.containsKey("SecondaryServerDetails")) {
         var3 = "getSecondaryServerDetails";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryServerDetails", AsyncReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryServerDetails", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SessionsWaitingForFlushCount")) {
         var3 = "getSessionsWaitingForFlushCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionsWaitingForFlushCount", AsyncReplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("SessionsWaitingForFlushCount", var2);
         var2.setValue("description", "<p>Number of sessions which are waiting to be flushed.</p> ");
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
      Method var3 = AsyncReplicationRuntimeMBean.class.getMethod("preDeregister");
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
