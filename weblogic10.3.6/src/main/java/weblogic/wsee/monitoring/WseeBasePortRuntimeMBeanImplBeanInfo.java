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
import weblogic.management.runtime.WseeBasePortRuntimeMBean;

public class WseeBasePortRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeBasePortRuntimeMBean.class;

   public WseeBasePortRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeBasePortRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeBasePortRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Describes the state of a particular Web service port.  </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeBasePortRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AggregatedBaseOperations")) {
         var3 = "getAggregatedBaseOperations";
         var4 = null;
         var2 = new PropertyDescriptor("AggregatedBaseOperations", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("AggregatedBaseOperations", var2);
         var2.setValue("description", "Return operation information aggregated across the base operations contained by this port. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("BaseOperations")) {
         var3 = "getBaseOperations";
         var4 = null;
         var2 = new PropertyDescriptor("BaseOperations", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("BaseOperations", var2);
         var2.setValue("description", "Return the base operations contained by this port. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ClusterRouting")) {
         var3 = "getClusterRouting";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterRouting", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterRouting", var2);
         var2.setValue("description", "Get statistics related to routing of messages in a cluster. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Handlers")) {
         var3 = "getHandlers";
         var4 = null;
         var2 = new PropertyDescriptor("Handlers", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("Handlers", var2);
         var2.setValue("description", "<p>List of SOAP message handlers that are associated with this Web service.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Mc")) {
         var3 = "getMc";
         var4 = null;
         var2 = new PropertyDescriptor("Mc", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("Mc", var2);
         var2.setValue("description", "Get information related to MakeConnection anonymous ids. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PolicyFaults")) {
         var3 = "getPolicyFaults";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyFaults", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyFaults", var2);
         var2.setValue("description", "<p>Total number of policy faults.</p> ");
      }

      if (!var1.containsKey("PortName")) {
         var3 = "getPortName";
         var4 = null;
         var2 = new PropertyDescriptor("PortName", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PortName", var2);
         var2.setValue("description", "<p>Name of this port.</p>  <p>This attribute corresponds to the \"name\" attribute of the \"port\" element in the WSDL that describes the Web service.  Programmers specify the name of the port using the portName attribute of the &#64;WLXXXTransport annotation, where XXX refers to the type of transport (HTTP, HTTPS, or JMS).</p>  <p>Programmers can also use the WLXXXTransport child element of the jwsc Ant task to specify this attribute.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("PortPolicy")) {
         var3 = "getPortPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("PortPolicy", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PortPolicy", var2);
         var2.setValue("description", "<p> Get statistics related to Web service security policy. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("StartTime")) {
         var3 = "getStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("StartTime", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("StartTime", var2);
         var2.setValue("description", "<p>Date and time that the Web service endpoint started.</p> ");
      }

      if (!var1.containsKey("TotalFaults")) {
         var3 = "getTotalFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFaults", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFaults", var2);
         var2.setValue("description", "<p>Total number of security faults and violations.</p> ");
      }

      if (!var1.containsKey("TotalSecurityFaults")) {
         var3 = "getTotalSecurityFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSecurityFaults", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSecurityFaults", var2);
         var2.setValue("description", "<p>Total number of security faults and violations.</p> ");
      }

      if (!var1.containsKey("TransportProtocolType")) {
         var3 = "getTransportProtocolType";
         var4 = null;
         var2 = new PropertyDescriptor("TransportProtocolType", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("TransportProtocolType", var2);
         var2.setValue("description", "<p>Transport protocol used to invoke this Web service, such as HTTP, HTTPS, or JMS.</p>  <p>This attribute determines the transport that is published in the endpoint address section of the WSDL of the Web Service. Programmers specify the transport by the type of &#64;WLXXXTransport JWS annotation they specify, where XXX refers to the type of transport (HTTP, HTTPS, or JMS).</p>  <p>Programmers can also use the WLXXXTransport child element of the jwsc Ant task to specify this attribute.</p> ");
      }

      if (!var1.containsKey("Wsrm")) {
         var3 = "getWsrm";
         var4 = null;
         var2 = new PropertyDescriptor("Wsrm", WseeBasePortRuntimeMBean.class, var3, (String)var4);
         var1.put("Wsrm", var2);
         var2.setValue("description", "Get statistics related to reliable messaging. ");
         var2.setValue("relationship", "containment");
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
      Method var3 = WseeBasePortRuntimeMBean.class.getMethod("preDeregister");
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
