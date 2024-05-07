package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class StartupClassMBeanImplBeanInfo extends ClassDeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = StartupClassMBean.class;

   public StartupClassMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public StartupClassMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = StartupClassMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Provides methods that configure startup classes. A startup class is a Java program that is automatically loaded and executed when a WebLogic Server instance is started or restarted.  By default, startup classes are loaded and executed after all other server subsystems have initialized and after the server deploys modules. For any startup class, you can override the default and specify that the server loads and executes it and before it deploys JDBC connection pools and before it deploys Web applications and EJBs.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.StartupClassMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("FailureIsFatal")) {
         var3 = "getFailureIsFatal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFailureIsFatal";
         }

         var2 = new PropertyDescriptor("FailureIsFatal", StartupClassMBean.class, var3, var4);
         var1.put("FailureIsFatal", var2);
         var2.setValue("description", "<p>Specifies whether a failure in this startup class prevents the targeted server(s) from starting.</p>  <p>If you specify that failure is <b>not</b> fatal, if the startup class fails, the server continues its startup process.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      String[] var5;
      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("LoadAfterAppsRunning")) {
         var3 = "getLoadAfterAppsRunning";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoadAfterAppsRunning";
         }

         var2 = new PropertyDescriptor("LoadAfterAppsRunning", StartupClassMBean.class, var3, var4);
         var1.put("LoadAfterAppsRunning", var2);
         var2.setValue("description", "<p>Specifies whether the targeted servers load and run this startup class after applications and EJBs are running.</p>  <p>If you enable this feature for a startup class, a server loads and runs the startup class after the activate phase. At this point, JMS and JDBC services are available. (Deployment for applications and EJBs consists of three phases: prepare, admin and activate.)</p>  <p>Enable this feature if the startup class needs to be invoked after applications are running and ready to service client requests.</p>  <p>If you do not enable this feature, LoadBeforeAppDeployments or LoadBeforeAppActivation, a server instance loads startup classes when applications go to the admin state.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.DeploymentMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("since", "10.3.3.0");
      }

      if (!var1.containsKey("LoadBeforeAppActivation")) {
         var3 = "getLoadBeforeAppActivation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoadBeforeAppActivation";
         }

         var2 = new PropertyDescriptor("LoadBeforeAppActivation", StartupClassMBean.class, var3, var4);
         var1.put("LoadBeforeAppActivation", var2);
         var2.setValue("description", "<p>Specifies whether the targeted servers load and run this startup class after activating JMS and JDBC services and before activating applications and EJBs.</p>  <p>If you enable this feature for a startup class, a server loads and runs the startup class before the activate phase. At this point, JMS and JDBC services are available. (Deployment for applications and EJBs consists of three phases: prepare, admin and activate.)</p>  <p>Enable this feature if the startup class needs to be invoked after JDBC connection pools are available but before the applications are activated and ready to service client requests.</p>  <p>If you do not enable this feature, LoadBeforeAppDeployments or LoadAfterAppsRunning, a server instance loads startup classes when applications go to the admin state.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.DeploymentMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("LoadBeforeAppDeployments")) {
         var3 = "getLoadBeforeAppDeployments";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoadBeforeAppDeployments";
         }

         var2 = new PropertyDescriptor("LoadBeforeAppDeployments", StartupClassMBean.class, var3, var4);
         var1.put("LoadBeforeAppDeployments", var2);
         var2.setValue("description", "<p>Specifies whether the targeted servers load and run this startup class before activating JMS and JDBC services and before starting deployment for applications and EJBs.</p>  <p>If you enable this feature for a startup class, a server loads and runs the startup class before the deployment prepare phase. At this point, JMS and JDBC services are not yet available. (Deployment for applications and EJBs consists of three phases: prepare, admin and activate.)</p>  <p>If you do not enable this feature, LoadBeforeAppActivation or LoadAfterAppsRunning, a server instance loads startup classes when applications go to the admin state.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.DeploymentMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
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
