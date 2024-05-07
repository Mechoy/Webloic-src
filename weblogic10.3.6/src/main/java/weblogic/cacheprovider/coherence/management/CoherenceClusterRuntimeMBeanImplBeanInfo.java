package weblogic.cacheprovider.coherence.management;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.CoherenceClusterRuntimeMBean;

public class CoherenceClusterRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CoherenceClusterRuntimeMBean.class;

   public CoherenceClusterRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CoherenceClusterRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CoherenceClusterRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.3.0");
      var2.setValue("package", "weblogic.cacheprovider.coherence.management");
      String var3 = (new String("Coherence cluster run-time information.   <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://edocs.bea.com\" shape=\"rect\">http://edocs.bea.com</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.CoherenceClusterRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ClusterName")) {
         var3 = "getClusterName";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterName", CoherenceClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterName", var2);
         var2.setValue("description", "<p>The name of the Coherence cluster. </p> ");
      }

      if (!var1.containsKey("ClusterSize")) {
         var3 = "getClusterSize";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterSize", CoherenceClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ClusterSize", var2);
         var2.setValue("description", "<p>The size of the Coherence cluster. </p> ");
      }

      if (!var1.containsKey("LicenseMode")) {
         var3 = "getLicenseMode";
         var4 = null;
         var2 = new PropertyDescriptor("LicenseMode", CoherenceClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("LicenseMode", var2);
         var2.setValue("description", "<p>The license mode for the Coherence cluster. Possible values are Evaluation, Development, or Production.</p> ");
      }

      if (!var1.containsKey("Members")) {
         var3 = "getMembers";
         var4 = null;
         var2 = new PropertyDescriptor("Members", CoherenceClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("Members", var2);
         var2.setValue("description", "<p>Identifiers for the available Coherence cluster members. </p> ");
      }

      if (!var1.containsKey("Version")) {
         var3 = "getVersion";
         var4 = null;
         var2 = new PropertyDescriptor("Version", CoherenceClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("Version", var2);
         var2.setValue("description", "<p>The Coherence cluster version. </p> ");
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
      Method var3 = CoherenceClusterRuntimeMBean.class.getMethod("preDeregister");
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
