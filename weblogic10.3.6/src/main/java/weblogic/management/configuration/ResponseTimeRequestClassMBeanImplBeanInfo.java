package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ResponseTimeRequestClassMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ResponseTimeRequestClassMBean.class;

   public ResponseTimeRequestClassMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ResponseTimeRequestClassMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ResponseTimeRequestClassMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean defines the response time goal for this request class.  <p> Response time goals can be used to differentiate request classes. We do not try to meet a response time goal for an individual request. Rather we compute a tolerable waiting time for a request class by subtracting the observed average thread use time. Then we schedule requests so that the average wait for each request class is in proportion to their tolerable waiting time. For example, consider we only have two request classes, A and B, with response time goals 2000ms and 5000ms, respectively, where the time an individual request uses a thread is much smaller. During a period in which both request classes are sufficiently requested, say, zero think time and more clients than threads, we schedule to keep the average response time in the ratio 2:5 so that it is a common fraction or multiple of the stated goal. </p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ResponseTimeRequestClassMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("GoalMs")) {
         String var3 = "getGoalMs";
         String var4 = null;
         if (!this.readOnly) {
            var4 = "setGoalMs";
         }

         var2 = new PropertyDescriptor("GoalMs", ResponseTimeRequestClassMBean.class, var3, var4);
         var1.put("GoalMs", var2);
         var2.setValue("description", "<p> A response time goal in milliseconds. </p> <p> You can either define a response time goal or keep the -1 default value, which acts as a placeholder for the response time goal. By doing so, you have not defined the response time goal at run time but can define one later. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
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
