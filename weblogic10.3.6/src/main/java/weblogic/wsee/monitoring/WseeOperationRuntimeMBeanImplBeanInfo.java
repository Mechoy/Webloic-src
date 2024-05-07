package weblogic.wsee.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WseeOperationRuntimeMBean;

public class WseeOperationRuntimeMBeanImplBeanInfo extends WseeBaseOperationRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseeOperationRuntimeMBean.class;

   public WseeOperationRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseeOperationRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseeOperationRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Describes the state of a particular Web Service operation, such as deployment state and runtime statistics about the execution of the operation.</p> <p>This MBean can describe the operation on a web service client or service. Request statistics relate to outgoing requests on a client or incoming requests on a service. Response statistics relate to an incoming response on a client or an outgoing response on a service.</p> <p>Time values are reported in milliseconds</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseeOperationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("DispatchTimeAverage")) {
         var3 = "getDispatchTimeAverage";
         var4 = null;
         var2 = new PropertyDescriptor("DispatchTimeAverage", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("DispatchTimeAverage", var2);
         var2.setValue("description", "<p>Average operation dispatch time for the current measurement period.</p>  <p>Dispatch time refers to the time for WebLogic Server to process the invocation.</p>  <p>The measurement period typically starts when WebLogic Server is first started.</p> ");
      }

      if (!var1.containsKey("DispatchTimeHigh")) {
         var3 = "getDispatchTimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("DispatchTimeHigh", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("DispatchTimeHigh", var2);
         var2.setValue("description", "<p>Longest operation dispatch time for the current measurement period.</p>  <p>Dispatch time refers to the time for WebLogic Server to process the invocation.</p>  <p>The measurement period typically starts when WebLogic Server is first started.</p> ");
      }

      if (!var1.containsKey("DispatchTimeLow")) {
         var3 = "getDispatchTimeLow";
         var4 = null;
         var2 = new PropertyDescriptor("DispatchTimeLow", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("DispatchTimeLow", var2);
         var2.setValue("description", "<p>Shortest operation dispatch time for the current measurement period.</p>  <p>Dispatch time refers to the time for WebLogic Server to process the invocation.</p>  <p>The measurement period typically starts when WebLogic Server is first started.</p> ");
      }

      if (!var1.containsKey("DispatchTimeTotal")) {
         var3 = "getDispatchTimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("DispatchTimeTotal", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("DispatchTimeTotal", var2);
         var2.setValue("description", "<p>Total time for all operation dispatches in the current measurement period.</p>  <p>Dispatch time refers to the time for WebLogic Server to process the invocation.</p>  <p>The measurement period typically starts when WebLogic Server is first started.</p> ");
      }

      if (!var1.containsKey("ErrorCount")) {
         var3 = "getErrorCount";
         var4 = null;
         var2 = new PropertyDescriptor("ErrorCount", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ErrorCount", var2);
         var2.setValue("description", "Number of errors sending or receiving a request. ");
      }

      if (!var1.containsKey("ExecutionTimeAverage")) {
         var3 = "getExecutionTimeAverage";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeAverage", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeAverage", var2);
         var2.setValue("description", "<p>Average operation execution time.</p> ");
      }

      if (!var1.containsKey("ExecutionTimeHigh")) {
         var3 = "getExecutionTimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeHigh", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeHigh", var2);
         var2.setValue("description", "<p>Longest operation execution time.</p> ");
      }

      if (!var1.containsKey("ExecutionTimeLow")) {
         var3 = "getExecutionTimeLow";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeLow", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeLow", var2);
         var2.setValue("description", "<p>Shortest operation execution time.</p> ");
      }

      if (!var1.containsKey("ExecutionTimeTotal")) {
         var3 = "getExecutionTimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutionTimeTotal", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutionTimeTotal", var2);
         var2.setValue("description", "<p>Total time for all operation executions.</p> ");
      }

      if (!var1.containsKey("InvocationCount")) {
         var3 = "getInvocationCount";
         var4 = null;
         var2 = new PropertyDescriptor("InvocationCount", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("InvocationCount", var2);
         var2.setValue("description", "<p>Total number of operation invocations in the current measurement period.</p>  <p>The measurement period typically starts when WebLogic Server is first started.</p> ");
      }

      if (!var1.containsKey("LastError")) {
         var3 = "getLastError";
         var4 = null;
         var2 = new PropertyDescriptor("LastError", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastError", var2);
         var2.setValue("description", "Last error that occurred processing a request. ");
      }

      if (!var1.containsKey("LastErrorTime")) {
         var3 = "getLastErrorTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastErrorTime", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastErrorTime", var2);
         var2.setValue("description", "Time on WebLogic Server of the last error for a request (sending or receiving) was detected expressed as the number of milliseconds since midnight, January 1, 1970 UTC. ");
      }

      if (!var1.containsKey("LastInvocationTime")) {
         var3 = "getLastInvocationTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastInvocationTime", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastInvocationTime", var2);
         var2.setValue("description", "Time of the last operation request to be sent or received (or 0 if no requests have been sent or received).</p> ");
      }

      if (!var1.containsKey("LastResponseError")) {
         var3 = "getLastResponseError";
         var4 = null;
         var2 = new PropertyDescriptor("LastResponseError", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastResponseError", var2);
         var2.setValue("description", "Last response error to arrive for this client/service (or null if no errors have occurred). ");
      }

      if (!var1.containsKey("LastResponseErrorTime")) {
         var3 = "getLastResponseErrorTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastResponseErrorTime", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastResponseErrorTime", var2);
         var2.setValue("description", "Time on WebLogic Server of the last error sending or receiving a response (or 0 if no failures have occurred) expressed as the number of milliseconds since midnight, January 1, 1970 UTC. ");
      }

      if (!var1.containsKey("LastResponseTime")) {
         var3 = "getLastResponseTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastResponseTime", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("LastResponseTime", var2);
         var2.setValue("description", "Time on WebLogic Server of the last response to arrive for this client/service (or 0 if no responses have been received) expressed as the number of milliseconds since midnight, January 1, 1970 UTC. ");
      }

      if (!var1.containsKey("OperationName")) {
         var3 = "getOperationName";
         var4 = null;
         var2 = new PropertyDescriptor("OperationName", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("OperationName", var2);
         var2.setValue("description", "<p>Name of the operation for which runtime information is being provided.</p> ");
      }

      if (!var1.containsKey("PolicyAttachmentSupport")) {
         var3 = "getPolicyAttachmentSupport";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyAttachmentSupport", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyAttachmentSupport", var2);
         var2.setValue("description", "Get attachment support for this port MBean. ");
      }

      if (!var1.containsKey("PolicySubjectName")) {
         var3 = "getPolicySubjectName";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectName", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectName", var2);
         var2.setValue("description", "Get subject name for this port MBean. ");
      }

      if (!var1.containsKey("PolicySubjectResourcePattern")) {
         var3 = "getPolicySubjectResourcePattern";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectResourcePattern", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectResourcePattern", var2);
         var2.setValue("description", "The policySubject parameter must uniquely identify what application, module, service, and port (port or operation for WLS Policy) is targeted. The syntax currently used by JRF (see [JRF ATTACHMENT POINT]) for J2EE Webservice Endpoints will be used: /{domain}/{instance}/{app}/WEBs|EJBs/{module }/WEBSERVICEs/{service}/PORTs/{port}/OPERATIONs/{operation} ");
      }

      if (!var1.containsKey("PolicySubjectType")) {
         var3 = "getPolicySubjectType";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectType", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectType", var2);
         var2.setValue("description", "Get subject type for this port MBean. ");
      }

      if (!var1.containsKey("ResponseCount")) {
         var3 = "getResponseCount";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseCount", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseCount", var2);
         var2.setValue("description", "<p>Total number of oresponses generated from operation invocations.</p> ");
      }

      if (!var1.containsKey("ResponseErrorCount")) {
         var3 = "getResponseErrorCount";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseErrorCount", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseErrorCount", var2);
         var2.setValue("description", "<p>Total number of errors from responses generated from operation invocations.</p> ");
      }

      if (!var1.containsKey("ResponseTimeAverage")) {
         var3 = "getResponseTimeAverage";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseTimeAverage", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseTimeAverage", var2);
         var2.setValue("description", "<p>Average response time from the responses generated from operation invocations. </p> ");
      }

      if (!var1.containsKey("ResponseTimeHigh")) {
         var3 = "getResponseTimeHigh";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseTimeHigh", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseTimeHigh", var2);
         var2.setValue("description", "<p>Longest response time from the responses generated from operation invocations.</p> ");
      }

      if (!var1.containsKey("ResponseTimeLow")) {
         var3 = "getResponseTimeLow";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseTimeLow", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseTimeLow", var2);
         var2.setValue("description", "<p>Lowest response time from the responses generated from operation invocations.</p> ");
      }

      if (!var1.containsKey("ResponseTimeTotal")) {
         var3 = "getResponseTimeTotal";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseTimeTotal", WseeOperationRuntimeMBean.class, var3, (String)var4);
         var1.put("ResponseTimeTotal", var2);
         var2.setValue("description", "<p>Total time for all responses generated from operation invocations.</p> ");
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
      Method var3 = WseeOperationRuntimeMBean.class.getMethod("preDeregister");
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
