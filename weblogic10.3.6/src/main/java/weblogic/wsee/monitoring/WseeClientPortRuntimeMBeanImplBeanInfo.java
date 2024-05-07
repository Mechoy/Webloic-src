package weblogic.wsee.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WseeClientPortRuntimeMBean;

public class WseeClientPortRuntimeMBeanImplBeanInfo extends WseeBasePortRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeClientPortRuntimeMBean.class;

   public WseeClientPortRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeClientPortRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeClientPortRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Describes the state of a particular Web Service port.  </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeClientPortRuntimeMBean");
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
         var2 = new PropertyDescriptor("AggregatedBaseOperations", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("AggregatedBaseOperations", var2);
         var2.setValue("description", "Return operation information aggregated across the base operations contained by this port. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("BaseOperations")) {
         var3 = "getBaseOperations";
         var4 = null;
         var2 = new PropertyDescriptor("BaseOperations", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("BaseOperations", var2);
         var2.setValue("description", "Return the base operations contained by this port. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ClusterRouting")) {
         var3 = "getClusterRouting";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterRouting", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterRouting", var2);
         var2.setValue("description", "Get statistics related to routing of messages in a cluster. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Handlers")) {
         var3 = "getHandlers";
         var4 = null;
         var2 = new PropertyDescriptor("Handlers", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("Handlers", var2);
         var2.setValue("description", "<p>List of SOAP message handlers that are associated with this Web service.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Mc")) {
         var3 = "getMc";
         var4 = null;
         var2 = new PropertyDescriptor("Mc", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("Mc", var2);
         var2.setValue("description", "Get information related to MakeConnection anonymous ids. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Operations")) {
         var3 = "getOperations";
         var4 = null;
         var2 = new PropertyDescriptor("Operations", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("Operations", var2);
         var2.setValue("description", "<p>Specifies the list of public operations exposed by this Web Service.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PolicyFaults")) {
         var3 = "getPolicyFaults";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyFaults", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyFaults", var2);
         var2.setValue("description", "<p>Total number of policy faults.</p> ");
      }

      if (!var1.containsKey("PoolCapacity")) {
         var3 = "getPoolCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("PoolCapacity", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolCapacity", var2);
         var2.setValue("description", "Capacity of the pool of client instances for this client port runtime, or 0 if no pool has been initialized for it. ");
      }

      if (!var1.containsKey("PoolFreeCount")) {
         var3 = "getPoolFreeCount";
         var4 = null;
         var2 = new PropertyDescriptor("PoolFreeCount", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolFreeCount", var2);
         var2.setValue("description", "Number of free client instances in the pool for this client port runtime, or 0 if no pool has been initialized for it. ");
      }

      if (!var1.containsKey("PoolTakenCount")) {
         var3 = "getPoolTakenCount";
         var4 = null;
         var2 = new PropertyDescriptor("PoolTakenCount", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolTakenCount", var2);
         var2.setValue("description", "Number of taken client instances in the pool for this client port runtime, or 0 if no pool has been initialized for it. ");
      }

      if (!var1.containsKey("PoolTotalConversationalClientTakeCount")) {
         var3 = "getPoolTotalConversationalClientTakeCount";
         var4 = null;
         var2 = new PropertyDescriptor("PoolTotalConversationalClientTakeCount", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolTotalConversationalClientTakeCount", var2);
         var2.setValue("description", "Total number of conversational client instances that have been taken from the pool over the lifetime of the pool for this client port runtime, or 0 if no pool has been initialized for it. Note, conversational client instances are managed separately from simple pooled client instances, and are not subject to the capacity setting for this pool. Thus, using conversational client instances won't increase the values you see for getPoolFreeCount() or getPoolTakenCount(). ");
      }

      if (!var1.containsKey("PoolTotalPooledClientTakeCount")) {
         var3 = "getPoolTotalPooledClientTakeCount";
         var4 = null;
         var2 = new PropertyDescriptor("PoolTotalPooledClientTakeCount", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolTotalPooledClientTakeCount", var2);
         var2.setValue("description", "Total number of client instances that have been taken from the pool over the lifetime of the pool for this client port runtime, or 0 if no pool has been initialized for it. Note, this value can exceed the capacity of the pool because client instances are released back into the pool to be taken again later. ");
      }

      if (!var1.containsKey("PoolTotalSimpleClientCreateCount")) {
         var3 = "getPoolTotalSimpleClientCreateCount";
         var4 = null;
         var2 = new PropertyDescriptor("PoolTotalSimpleClientCreateCount", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolTotalSimpleClientCreateCount", var2);
         var2.setValue("description", "Total number of simple (non-pooled, non-conversational) client instances that have been created over the life of this client port runtime (i.e. using the same client identity and port name). Note, if a non-zero capacity is given for the pool associated with the client identity for this client port runtime, then all client instances retrieved for it will come from the pool, and the simple client create count will be 0. ");
      }

      if (!var1.containsKey("PortName")) {
         var3 = "getPortName";
         var4 = null;
         var2 = new PropertyDescriptor("PortName", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PortName", var2);
         var2.setValue("description", "<p>Name of this port.</p>  <p>This attribute corresponds to the \"name\" attribute of the \"port\" element in the WSDL that describes the Web service.  Programmers specify the name of the port using the portName attribute of the &#64;WLXXXTransport annotation, where XXX refers to the type of transport (HTTP, HTTPS, or JMS).</p>  <p>Programmers can also use the WLXXXTransport child element of the jwsc Ant task to specify this attribute.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("PortPolicy")) {
         var3 = "getPortPolicy";
         var4 = null;
         var2 = new PropertyDescriptor("PortPolicy", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("PortPolicy", var2);
         var2.setValue("description", "<p> Get statistics related to Web service security policy. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("StartTime")) {
         var3 = "getStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("StartTime", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("StartTime", var2);
         var2.setValue("description", "<p>Date and time that the Web service endpoint started.</p> ");
      }

      if (!var1.containsKey("TotalFaults")) {
         var3 = "getTotalFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFaults", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFaults", var2);
         var2.setValue("description", "<p>Total number of security faults and violations.</p> ");
      }

      if (!var1.containsKey("TotalSecurityFaults")) {
         var3 = "getTotalSecurityFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSecurityFaults", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSecurityFaults", var2);
         var2.setValue("description", "<p>Total number of security faults and violations.</p> ");
      }

      if (!var1.containsKey("TransportProtocolType")) {
         var3 = "getTransportProtocolType";
         var4 = null;
         var2 = new PropertyDescriptor("TransportProtocolType", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("TransportProtocolType", var2);
         var2.setValue("description", "<p>Transport protocol used to invoke this Web service, such as HTTP, HTTPS, or JMS.</p>  <p>This attribute determines the transport that is published in the endpoint address section of the WSDL of the Web Service. Programmers specify the transport by the type of &#64;WLXXXTransport JWS annotation they specify, where XXX refers to the type of transport (HTTP, HTTPS, or JMS).</p>  <p>Programmers can also use the WLXXXTransport child element of the jwsc Ant task to specify this attribute.</p> ");
      }

      if (!var1.containsKey("WseePortConfigurationRuntimeMBean")) {
         var3 = "getWseePortConfigurationRuntimeMBean";
         var4 = null;
         var2 = new PropertyDescriptor("WseePortConfigurationRuntimeMBean", WseeClientPortRuntimeMBean.class, var3, (String)var4);
         var1.put("WseePortConfigurationRuntimeMBean", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Wsrm")) {
         var3 = "getWsrm";
         var4 = null;
         var2 = new PropertyDescriptor("Wsrm", WseeClientPortRuntimeMBean.class, var3, (String)var4);
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
      Method var3 = WseeClientPortRuntimeMBean.class.getMethod("preDeregister");
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
