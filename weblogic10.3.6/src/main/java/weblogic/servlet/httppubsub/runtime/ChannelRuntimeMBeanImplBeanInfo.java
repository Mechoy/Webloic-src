package weblogic.servlet.httppubsub.runtime;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ChannelRuntimeMBean;

public class ChannelRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ChannelRuntimeMBean.class;

   public ChannelRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ChannelRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ChannelRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.servlet.httppubsub.runtime");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ChannelRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", ChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "Get the qualified name for this channel ");
      }

      if (!var1.containsKey("PublishedMessageCount")) {
         var3 = "getPublishedMessageCount";
         var4 = null;
         var2 = new PropertyDescriptor("PublishedMessageCount", ChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("PublishedMessageCount", var2);
         var2.setValue("description", "Get the number of published messages to this Channel ");
      }

      if (!var1.containsKey("SubChannels")) {
         var3 = "getSubChannels";
         var4 = null;
         var2 = new PropertyDescriptor("SubChannels", ChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("SubChannels", var2);
         var2.setValue("description", "Get the sub channel runtimes if any ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SubscriberCount")) {
         var3 = "getSubscriberCount";
         var4 = null;
         var2 = new PropertyDescriptor("SubscriberCount", ChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("SubscriberCount", var2);
         var2.setValue("description", "Get the number of subscribers for this channel currently ");
      }

      if (!var1.containsKey("Subscribers")) {
         var3 = "getSubscribers";
         var4 = null;
         var2 = new PropertyDescriptor("Subscribers", ChannelRuntimeMBean.class, var3, (String)var4);
         var1.put("Subscribers", var2);
         var2.setValue("description", "Get the subscribers for this channel ");
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
      Method var3 = ChannelRuntimeMBean.class.getMethod("preDeregister");
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
