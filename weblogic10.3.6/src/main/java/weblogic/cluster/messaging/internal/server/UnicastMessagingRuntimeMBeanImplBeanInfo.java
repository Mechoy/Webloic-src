package weblogic.cluster.messaging.internal.server;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.UnicastMessagingRuntimeMBean;

public class UnicastMessagingRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UnicastMessagingRuntimeMBean.class;

   public UnicastMessagingRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UnicastMessagingRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UnicastMessagingRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.cluster.messaging.internal.server");
      String var3 = (new String("Monitoring information when unicast messaging is turned on ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.UnicastMessagingRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("DiscoveredGroupLeaders")) {
         var3 = "getDiscoveredGroupLeaders";
         var4 = null;
         var2 = new PropertyDescriptor("DiscoveredGroupLeaders", UnicastMessagingRuntimeMBean.class, var3, (String)var4);
         var1.put("DiscoveredGroupLeaders", var2);
         var2.setValue("description", "Running group leader names ");
      }

      if (!var1.containsKey("Groups")) {
         var3 = "getGroups";
         var4 = null;
         var2 = new PropertyDescriptor("Groups", UnicastMessagingRuntimeMBean.class, var3, (String)var4);
         var1.put("Groups", var2);
         var2.setValue("description", "Formatted group list ");
      }

      if (!var1.containsKey("LocalGroupLeaderName")) {
         var3 = "getLocalGroupLeaderName";
         var4 = null;
         var2 = new PropertyDescriptor("LocalGroupLeaderName", UnicastMessagingRuntimeMBean.class, var3, (String)var4);
         var1.put("LocalGroupLeaderName", var2);
         var2.setValue("description", "Name of the local group leader ");
      }

      if (!var1.containsKey("RemoteGroupsDiscoveredCount")) {
         var3 = "getRemoteGroupsDiscoveredCount";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteGroupsDiscoveredCount", UnicastMessagingRuntimeMBean.class, var3, (String)var4);
         var1.put("RemoteGroupsDiscoveredCount", var2);
         var2.setValue("description", "Returns the total groups discovered by this server ");
      }

      if (!var1.containsKey("TotalGroupsCount")) {
         var3 = "getTotalGroupsCount";
         var4 = null;
         var2 = new PropertyDescriptor("TotalGroupsCount", UnicastMessagingRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalGroupsCount", var2);
         var2.setValue("description", "Total configured groups - running and not running ");
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
      Method var3 = UnicastMessagingRuntimeMBean.class.getMethod("preDeregister");
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
