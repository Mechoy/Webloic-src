package weblogic.wsee.mc.mbean;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WseeMcRuntimeMBean;

public class WseeMcRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeMcRuntimeMBean.class;

   public WseeMcRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeMcRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeMcRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.wsee.mc.mbean");
      String var3 = (new String("<p>Describes the state of MakeConnection pending message lists</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeMcRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("AnonymousEndpointIds")) {
         String var3 = "getAnonymousEndpointIds";
         Object var4 = null;
         var2 = new PropertyDescriptor("AnonymousEndpointIds", WseeMcRuntimeMBean.class, var3, (String)var4);
         var1.put("AnonymousEndpointIds", var2);
         var2.setValue("description", "Get the list of MakeConnection anonymous endpoint Ids for which the MC Receiver is awaiting MakeConnection incoming messages ");
         var2.setValue("unharvestable", Boolean.TRUE);
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
      Method var3 = WseeMcRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WseeMcRuntimeMBean.class.getMethod("getAnonymousEndpointInfo", String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("anonymousId", "the id if the anonymous endpoint to get info on ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the id is unknown")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Given an anonyomus endpoint id get info about the endpoint. The info is a CompositeData item containing the following items: <p> AnonymousEndpointId - the MakeConnection unique identifier for this anonymous endpoint </p> <p> PendingmMessageCount - the number of messages stored for this endpoint </p> <p> ReceivedMcMessageCount - the number of MakeConnection messages received for this endpoint </p> <p> EmptyResponseCount - the number of responses to received MakeConnection messages that did not return a stored message </p> <p> NonEmptyResponseCount - the number of responses to received MakeConnection messages that returned a stored message </p <p> OldestPendingMessageTime - the oldest timestamp of messages stored for this endpoint </p> <p> NewestPendingMessageTime - the newest timestamp of messages stored for this endpoint </p ");
         var2.setValue("unharvestable", Boolean.TRUE);
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
