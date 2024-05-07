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
import weblogic.management.runtime.WseePortConfigurationRuntimeMBean;

public class WseePortConfigurationRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WseePortConfigurationRuntimeMBean.class;

   public WseePortConfigurationRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WseePortConfigurationRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WseePortConfigurationRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.wsee.monitoring");
      String var3 = (new String("<p> Encapsulates runtime policy subject information about a particular Port. The subject name attribute of this MBean will be the value of the local part of port QName. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WseePortConfigurationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Operations")) {
         var3 = "getOperations";
         var4 = null;
         var2 = new PropertyDescriptor("Operations", WseePortConfigurationRuntimeMBean.class, var3, (String)var4);
         var1.put("Operations", var2);
         var2.setValue("description", "Specifies the array of operation configurations that are associated with this port. ");
         var2.setValue("relationship", "containment");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PolicyAttachmentSupport")) {
         var3 = "getPolicyAttachmentSupport";
         var4 = null;
         var2 = new PropertyDescriptor("PolicyAttachmentSupport", WseePortConfigurationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicyAttachmentSupport", var2);
         var2.setValue("description", "Get attachment support for this operation MBean. ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PolicySubjectName")) {
         var3 = "getPolicySubjectName";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectName", WseePortConfigurationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectName", var2);
         var2.setValue("description", "Get subject name for this operation MBean. ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PolicySubjectResourcePattern")) {
         var3 = "getPolicySubjectResourcePattern";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectResourcePattern", WseePortConfigurationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectResourcePattern", var2);
         var2.setValue("description", "The policySubject parameter must uniquely identify what application, module, service, and port (port or operation for WLS Policy) is targeted. The syntax currently used by JRF for J2EE WebService Endpoints will be used: /{domain}/{instance}/{app}/WEBs|EJBs/{module }/WEBSERVICECLIENTs/{service-ref-name}/PORTs/{port} ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PolicySubjectType")) {
         var3 = "getPolicySubjectType";
         var4 = null;
         var2 = new PropertyDescriptor("PolicySubjectType", WseePortConfigurationRuntimeMBean.class, var3, (String)var4);
         var1.put("PolicySubjectType", var2);
         var2.setValue("description", "Get subject type for this operation MBean. ");
         var2.setValue("exclude", Boolean.TRUE);
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
      Method var3 = WseePortConfigurationRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
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
