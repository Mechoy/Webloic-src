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
import weblogic.management.runtime.WseeHandlerRuntimeMBean;

public class WseeHandlerRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeHandlerRuntimeMBean.class;

   public WseeHandlerRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeHandlerRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeHandlerRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Provides runtime information about a SOAP message handler that has been associated with a Web service.</p>  <p>SOAP message handlers are used to intercept both the inbound (request) and outbound (response) SOAP messages so that extra processing can be done on the messages. Programmers specify SOAP message handlers for a Web Service using the @SOAPMessageHandlers and @HandlerChain JWS annotations.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeHandlerRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("HandlerClass")) {
         var3 = "getHandlerClass";
         var4 = null;
         var2 = new PropertyDescriptor("HandlerClass", WseeHandlerRuntimeMBean.class, var3, (String)var4);
         var1.put("HandlerClass", var2);
         var2.setValue("description", "<p>Specifies the fully qualified name of the class that implements this SOAP handler.</p>  <p>This class implements for JAX-WS the javax.xml.ws.handler.LogicalHandler class; and for JAX-RPC the javax.xml.rpc.handler.Handler interface or javax.xml.rpc.handler.GenericHandler abstract class. These interfaces or classes contain methods that programmers implement to process the request and response SOAP messages resulting from the invoke of a Web service operation. </p> ");
      }

      if (!var1.containsKey("Headers")) {
         var3 = "getHeaders";
         var4 = null;
         var2 = new PropertyDescriptor("Headers", WseeHandlerRuntimeMBean.class, var3, (String)var4);
         var1.put("Headers", var2);
         var2.setValue("description", "<p>Specifies the SOAP headers that can be processed by this SOAP message handler.</p> ");
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
      Method var3 = WseeHandlerRuntimeMBean.class.getMethod("preDeregister");
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
