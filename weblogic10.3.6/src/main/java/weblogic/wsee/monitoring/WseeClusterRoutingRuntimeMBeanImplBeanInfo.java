package weblogic.wsee.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WseeClusterRoutingRuntimeMBean;

public class WseeClusterRoutingRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeClusterRoutingRuntimeMBean.class;

   public WseeClusterRoutingRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeClusterRoutingRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeClusterRoutingRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Encapsulates runtime information about a particular Web Service cluster routing instance (whether it be a front-end router or an in-place router).</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeClusterRoutingRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("LastRoutingFailure")) {
         var3 = "getLastRoutingFailure";
         var4 = null;
         var2 = new PropertyDescriptor("LastRoutingFailure", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("LastRoutingFailure", var2);
         var2.setValue("description", "The exception that caused the last routing failure, or null if no failures have occurred. ");
      }

      if (!var1.containsKey("LastRoutingFailureTime")) {
         var3 = "getLastRoutingFailureTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastRoutingFailureTime", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("LastRoutingFailureTime", var2);
         var2.setValue("description", "The time (in milliseconds since epoch) of the last routing failure (or 0 if no failures have occurred). ");
      }

      if (!var1.containsKey("RequestCount")) {
         var3 = "getRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("RequestCount", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("RequestCount", var2);
         var2.setValue("description", "The number of requests (messages with no RelatesTo header) that have come through this front-end since the server started. ");
      }

      if (!var1.containsKey("ResponseCount")) {
         var3 = "getResponseCount";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseCount", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseCount", var2);
         var2.setValue("description", "The number of responses (messages with a RelatesTo header) that have come through this front-end since the server started. ");
      }

      if (!var1.containsKey("RoutedRequestCount")) {
         var3 = "getRoutedRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("RoutedRequestCount", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("RoutedRequestCount", var2);
         var2.setValue("description", "The number of requests that were routed to a specific server instance via routing information in the request (not just load balanced) since the server started. ");
      }

      if (!var1.containsKey("RoutedResponseCount")) {
         var3 = "getRoutedResponseCount";
         var4 = null;
         var2 = new PropertyDescriptor("RoutedResponseCount", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("RoutedResponseCount", var2);
         var2.setValue("description", "The number of responses that were routed to a specific server instance via routing information in the response (not just load balanced) since the server started. ");
      }

      if (!var1.containsKey("RoutingFailureCount")) {
         var3 = "getRoutingFailureCount";
         var4 = null;
         var2 = new PropertyDescriptor("RoutingFailureCount", WseeClusterRoutingRuntimeMBean.class, var3, (String)var4);
         var1.put("RoutingFailureCount", var2);
         var2.setValue("description", "The number of times a message failed to be routed, since the server started. ");
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
      Method var3 = WseeClusterRoutingRuntimeMBean.class.getMethod("preDeregister");
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
