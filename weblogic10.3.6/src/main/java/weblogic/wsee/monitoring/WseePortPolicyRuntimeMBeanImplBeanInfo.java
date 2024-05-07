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
import weblogic.management.runtime.WseePortPolicyRuntimeMBean;

public class WseePortPolicyRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseePortPolicyRuntimeMBean.class;

   public WseePortPolicyRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseePortPolicyRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseePortPolicyRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p>Describes the Web service security policy state of a particular Web service port.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseePortPolicyRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AuthenticationSuccesses")) {
         var3 = "getAuthenticationSuccesses";
         var4 = null;
         var2 = new PropertyDescriptor("AuthenticationSuccesses", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("AuthenticationSuccesses", var2);
         var2.setValue("description", "<p> Total number of authentication successes detected for this port. Only incoming message processing can add to the success count. </p> ");
      }

      if (!var1.containsKey("AuthenticationViolations")) {
         var3 = "getAuthenticationViolations";
         var4 = null;
         var2 = new PropertyDescriptor("AuthenticationViolations", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("AuthenticationViolations", var2);
         var2.setValue("description", "<p> Total number of authentication violations generated for this port. Only incoming message processing can add to the violation count. </p> ");
      }

      if (!var1.containsKey("AuthorizationSuccesses")) {
         var3 = "getAuthorizationSuccesses";
         var4 = null;
         var2 = new PropertyDescriptor("AuthorizationSuccesses", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("AuthorizationSuccesses", var2);
         var2.setValue("description", "<p> Total number of authorization successes detected for this port. </p> ");
      }

      if (!var1.containsKey("AuthorizationViolations")) {
         var3 = "getAuthorizationViolations";
         var4 = null;
         var2 = new PropertyDescriptor("AuthorizationViolations", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("AuthorizationViolations", var2);
         var2.setValue("description", "<p> Total number of authorization violations generated for this port. </p> ");
      }

      if (!var1.containsKey("ConfidentialitySuccesses")) {
         var3 = "getConfidentialitySuccesses";
         var4 = null;
         var2 = new PropertyDescriptor("ConfidentialitySuccesses", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("ConfidentialitySuccesses", var2);
         var2.setValue("description", "<p> Total number of confidentiality successes generated for this port. Both outgoing and incoming message processing can add to the success count. </p> ");
      }

      if (!var1.containsKey("ConfidentialityViolations")) {
         var3 = "getConfidentialityViolations";
         var4 = null;
         var2 = new PropertyDescriptor("ConfidentialityViolations", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("ConfidentialityViolations", var2);
         var2.setValue("description", "<p> Total number of confidentiality violations generated for this port. Both outgoing and incoming message processing can add to the violation count. </p> ");
      }

      if (!var1.containsKey("IntegritySuccesses")) {
         var3 = "getIntegritySuccesses";
         var4 = null;
         var2 = new PropertyDescriptor("IntegritySuccesses", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("IntegritySuccesses", var2);
         var2.setValue("description", "<p> Total number of integrity successes generated for this port. Both outgoing and incoming message processing can add to the success count. </p> ");
      }

      if (!var1.containsKey("IntegrityViolations")) {
         var3 = "getIntegrityViolations";
         var4 = null;
         var2 = new PropertyDescriptor("IntegrityViolations", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("IntegrityViolations", var2);
         var2.setValue("description", "<p> Total number of integrity violations generated for this port. Both outgoing and incoming message processing can add to the violation count. </p> ");
      }

      if (!var1.containsKey("PolicyFaults")) {
         var3 = "getPolicyFaults";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyFaults", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyFaults", var2);
         var2.setValue("description", "<p> Total number of policy faults generated for this port. </p> ");
      }

      if (!var1.containsKey("TotalFaults")) {
         var3 = "getTotalFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalFaults", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalFaults", var2);
         var2.setValue("description", "<p> Total number of security faults and violations generated by this port. </p> ");
      }

      if (!var1.containsKey("TotalSecurityFaults")) {
         var3 = "getTotalSecurityFaults";
         var4 = null;
         var2 = new PropertyDescriptor("TotalSecurityFaults", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalSecurityFaults", var2);
         var2.setValue("description", "<p> Total number of security faults and violations generated for this port. </p> ");
      }

      if (!var1.containsKey("TotalViolations")) {
         var3 = "getTotalViolations";
         var4 = null;
         var2 = new PropertyDescriptor("TotalViolations", WseePortPolicyRuntimeMBean.class, var3, (String)var4);
         var1.put("TotalViolations", var2);
         var2.setValue("description", "<p> Total number of authentication, integrity, and confidentialy violations. Both outgoing and incoming message processing can add to the violation count. </p> ");
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
      Method var3 = WseePortPolicyRuntimeMBean.class.getMethod("preDeregister");
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
