package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class DeploymentConfigurationMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DeploymentConfigurationMBean.class;

   public DeploymentConfigurationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DeploymentConfigurationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DeploymentConfigurationMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Specifies the domain-level deployment configuration attributes.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.DeploymentConfigurationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MaxAppVersions")) {
         var3 = "getMaxAppVersions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxAppVersions";
         }

         var2 = new PropertyDescriptor("MaxAppVersions", DeploymentConfigurationMBean.class, var3, var4);
         var1.put("MaxAppVersions", var2);
         var2.setValue("description", "<p>Specifies the maximum number of application versions for each application.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(2));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RemoteDeployerEJBEnabled")) {
         var3 = "isRemoteDeployerEJBEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteDeployerEJBEnabled";
         }

         var2 = new PropertyDescriptor("RemoteDeployerEJBEnabled", DeploymentConfigurationMBean.class, var3, var4);
         var1.put("RemoteDeployerEJBEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Remote Deployer EJB is automatically deployed in the current domain. The Remote Deployer EJB is only used by the weblogic.Deployer tool in the WLS 9.0 and 9.1 releases when the -remote option is specified.</p>  <p>If the Remote Deployer EJB is not deployed, you will not be able to use the -remote option in weblogic.Deployer running in a 9.0 or 9.1 installation. You can still use the -remote option from weblogic.Deployer in 9.2 or later releases.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("RestageOnlyOnRedeploy")) {
         var3 = "isRestageOnlyOnRedeploy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestageOnlyOnRedeploy";
         }

         var2 = new PropertyDescriptor("RestageOnlyOnRedeploy", DeploymentConfigurationMBean.class, var3, var4);
         var1.put("RestageOnlyOnRedeploy", var2);
         var2.setValue("description", "<p>Specifies whether applications with staging mode of STAGE are restaged only during redeploy operation. If set to true, then applications will never restage during server startup and will only be restaged on an explicit redeploy operation.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("since", "10.3.1.0");
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
