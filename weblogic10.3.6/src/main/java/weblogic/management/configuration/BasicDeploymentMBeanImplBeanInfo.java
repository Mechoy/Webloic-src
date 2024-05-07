package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class BasicDeploymentMBeanImplBeanInfo extends TargetInfoMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = BasicDeploymentMBean.class;

   public BasicDeploymentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public BasicDeploymentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = BasicDeploymentMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean represents a file or archive that is deployed to a set of targets in the domain.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime..</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.BasicDeploymentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DeploymentOrder")) {
         var3 = "getDeploymentOrder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeploymentOrder";
         }

         var2 = new PropertyDescriptor("DeploymentOrder", BasicDeploymentMBean.class, var3, var4);
         var1.put("DeploymentOrder", var2);
         var2.setValue("description", "<p>An integer value that indicates when this unit is deployed, relative to other deployable units on a server, during startup. <p> Units with lower values are deployed before those with higher values. ");
         setPropertyDescriptorDefault(var2, new Integer(100));
      }

      if (!var1.containsKey("DeploymentPrincipalName")) {
         var3 = "getDeploymentPrincipalName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeploymentPrincipalName";
         }

         var2 = new PropertyDescriptor("DeploymentPrincipalName", BasicDeploymentMBean.class, var3, var4);
         var1.put("DeploymentPrincipalName", var2);
         var2.setValue("description", "<p>A string value that indicates what principal should be used when deploying the file or archive during startup and shutdown. This principal will be used to set the current subject when calling out into application code for interfaces such as ApplicationLifecycleListener. If no principal name is specified, then the anonymous principal will be used. ");
      }

      if (!var1.containsKey("SourcePath")) {
         var3 = "getSourcePath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourcePath";
         }

         var2 = new PropertyDescriptor("SourcePath", BasicDeploymentMBean.class, var3, var4);
         var1.put("SourcePath", var2);
         var2.setValue("description", "The path to the source of the deployment unit on admin server. ");
         var2.setValue("setterDeprecated", "9.0.0.0 There is no replacement for this method. ");
      }

      if (!var1.containsKey("SubDeployments")) {
         var3 = "getSubDeployments";
         var4 = null;
         var2 = new PropertyDescriptor("SubDeployments", BasicDeploymentMBean.class, var3, var4);
         var1.put("SubDeployments", var2);
         var2.setValue("description", "Targeting for subcomponents that differs from targeting for the component. ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSubDeployment");
         var2.setValue("destroyer", "destroySubDeployment");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = BasicDeploymentMBean.class.getMethod("createSubDeployment", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Create a new subdeployment ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SubDeployments");
      }

      var3 = BasicDeploymentMBean.class.getMethod("destroySubDeployment", SubDeploymentMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("subDeployment", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Remove subDeployment ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SubDeployments");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = BasicDeploymentMBean.class.getMethod("lookupSubDeployment", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "SubDeployments");
      }

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
