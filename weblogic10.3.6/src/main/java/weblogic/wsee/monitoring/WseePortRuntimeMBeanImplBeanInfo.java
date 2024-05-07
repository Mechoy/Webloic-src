package weblogic.wsee.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WseePortRuntimeMBean;

public class WseePortRuntimeMBeanImplBeanInfo extends WseeBasePortRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseePortRuntimeMBean.class;

   public WseePortRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseePortRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseePortRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Describes the state of a particular Web Service port.  </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseePortRuntimeMBean");
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
         var2 = new PropertyDescriptor("AggregatedBaseOperations", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("AggregatedBaseOperations", var2);
         var2.setValue("description", "Return operation information aggregated across the base operations contained by this port. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("BaseOperations")) {
         var3 = "getBaseOperations";
         var4 = null;
         var2 = new PropertyDescriptor("BaseOperations", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("BaseOperations", var2);
         var2.setValue("description", "Return the base operations contained by this port. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ClusterRouting")) {
         var3 = "getClusterRouting";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterRouting", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterRouting", var2);
         var2.setValue("description", "Get statistics related to routing of messages in a cluster. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Handlers")) {
         var3 = "getHandlers";
         var4 = null;
         var2 = new PropertyDescriptor("Handlers", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("Handlers", var2);
         var2.setValue("description", "<p>List of SOAP message handlers that are associated with this Web service.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Mc")) {
         var3 = "getMc";
         var4 = null;
         var2 = new PropertyDescriptor("Mc", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("Mc", var2);
         var2.setValue("description", "Get information related to MakeConnection anonymous ids. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Operations")) {
         var3 = "getOperations";
         var4 = null;
         var2 = new PropertyDescriptor("Operations", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("Operations", var2);
         var2.setValue("description", "<p>Specifies the list of public operations exposed by this Web Service.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PolicyAttachmentSupport")) {
         var3 = "getPolicyAttachmentSupport";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyAttachmentSupport", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyAttachmentSupport", var2);
         var2.setValue("description", "Get attachment support for this port MBean. ");
      }

      if (!var1.containsKey("PolicyFaults")) {
         var3 = "getPolicyFaults";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyFaults", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyFaults", var2);
         var2.setValue("description", "<p>Total number of policy faults.</p> ");
      }

      if (!var1.containsKey("PolicySubjectName")) {
         var3 = "getPolicySubjectName";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectName", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectName", var2);
         var2.setValue("description", "Get subject name for this port MBean. ");
      }

      if (!var1.containsKey("PolicySubjectResourcePattern")) {
         var3 = "getPolicySubjectResourcePattern";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectResourcePattern", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectResourcePattern", var2);
         var2.setValue("description", "The policySubject parameter must uniquely identify what application, module, service or reference-name, and port (port or operation for WLS Policy) is targeted. The syntax currently used by JRF (see [JRF ATTACHMENT POINT]) for J2EE Webservice Endpoints will be used: /{domain}/{instance}/{app}/WEBs|EJBs/{module}/WEBSERVICEs/{service}|{reference-name}/PORTs/{port} ");
      }

      if (!var1.containsKey("PolicySubjectType")) {
         var3 = "getPolicySubjectType";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectType", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectType", var2);
         var2.setValue("description", "Get subject type for this port MBean. ");
      }

      if (!var1.containsKey("PortName")) {
         var3 = "getPortName";
         var4 = null;
         var2 = new PropertyDescriptor("PortName", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PortName", var2);
         var2.setValue("description", "<p>Name of this port.</p>  <p>This attribute corresponds to the \"name\" attribute of the \"port\" element in the WSDL that describes the Web service.  Programmers specify the name of the port using the portName attribute of the &#64;WLXXXTransport annotation, where XXX refers to the type of transport (HTTP, HTTPS, or JMS).</p>  <p>Programmers can also use the WLXXXTransport child element of the jwsc Ant task to specify this attribute.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("PortPolicy")) {
         var3 = "getPortPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("PortPolicy", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("PortPolicy", var2);
         var2.setValue("description", "<p> Get statistics related to Web service security policy. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("StartTime")) {
         var3 = "getStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("StartTime", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("StartTime", var2);
         var2.setValue("description", "<p>Date and time that the Web service endpoint started.</p> ");
      }

      if (!var1.containsKey("TotalFaults")) {
         var3 = "getTotalFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFaults", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFaults", var2);
         var2.setValue("description", "<p>Total number of security faults and violations.</p> ");
      }

      if (!var1.containsKey("TotalSecurityFaults")) {
         var3 = "getTotalSecurityFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSecurityFaults", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSecurityFaults", var2);
         var2.setValue("description", "<p>Total number of security faults and violations.</p> ");
      }

      if (!var1.containsKey("TransportProtocolType")) {
         var3 = "getTransportProtocolType";
         var4 = null;
         var2 = new PropertyDescriptor("TransportProtocolType", WseePortRuntimeMBean.class, var3, (String)var4);
         var1.put("TransportProtocolType", var2);
         var2.setValue("description", "<p>Transport protocol used to invoke this Web service, such as HTTP, HTTPS, or JMS.</p>  <p>This attribute determines the transport that is published in the endpoint address section of the WSDL of the Web Service. Programmers specify the transport by the type of &#64;WLXXXTransport JWS annotation they specify, where XXX refers to the type of transport (HTTP, HTTPS, or JMS).</p>  <p>Programmers can also use the WLXXXTransport child element of the jwsc Ant task to specify this attribute.</p> ");
      }

      if (!var1.containsKey("Wsrm")) {
         var3 = "getWsrm";
         var4 = null;
         var2 = new PropertyDescriptor("Wsrm", WseePortRuntimeMBean.class, var3, (String)var4);
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
      Method var3 = WseePortRuntimeMBean.class.getMethod("preDeregister");
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
