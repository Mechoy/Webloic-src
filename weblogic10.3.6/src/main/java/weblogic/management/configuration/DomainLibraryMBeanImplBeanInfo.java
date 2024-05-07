package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class DomainLibraryMBeanImplBeanInfo extends LibraryMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DomainLibraryMBean.class;

   public DomainLibraryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DomainLibraryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DomainLibraryMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Configuration bean for Libraries.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.DomainLibraryMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AbsoluteInstallDir")) {
         var3 = "getAbsoluteInstallDir";
         var4 = null;
         var2 = new PropertyDescriptor("AbsoluteInstallDir", DomainLibraryMBean.class, var3, var4);
         var1.put("AbsoluteInstallDir", var2);
         var2.setValue("description", "The fully resolved location of this application's installation root directory on the Administration Server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AbsolutePlanDir")) {
         var3 = "getAbsolutePlanDir";
         var4 = null;
         var2 = new PropertyDescriptor("AbsolutePlanDir", DomainLibraryMBean.class, var3, var4);
         var1.put("AbsolutePlanDir", var2);
         var2.setValue("description", "The fully resolved location of this application's deployment plan directory on the Administration Server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AbsolutePlanPath")) {
         var3 = "getAbsolutePlanPath";
         var4 = null;
         var2 = new PropertyDescriptor("AbsolutePlanPath", DomainLibraryMBean.class, var3, var4);
         var1.put("AbsolutePlanPath", var2);
         var2.setValue("description", "The fully resolved location of this application's deployment plan on the Administration Server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AbsoluteSourcePath")) {
         var3 = "getAbsoluteSourcePath";
         var4 = null;
         var2 = new PropertyDescriptor("AbsoluteSourcePath", DomainLibraryMBean.class, var3, var4);
         var1.put("AbsoluteSourcePath", var2);
         var2.setValue("description", "The fully resolved location of this application's source files on the Administration Server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AppMBean")) {
         var3 = "getAppMBean";
         var4 = null;
         var2 = new PropertyDescriptor("AppMBean", DomainLibraryMBean.class, var3, var4);
         var1.put("AppMBean", var2);
         var2.setValue("description", "This will be removed after all server code stops using application and component mbeans. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ApplicationIdentifier")) {
         var3 = "getApplicationIdentifier";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationIdentifier", DomainLibraryMBean.class, var3, var4);
         var1.put("ApplicationIdentifier", var2);
         var2.setValue("description", "<p>The Application Identifier of the application version uniquely identifies the application version across all versions of all applications. If the application is not versioned, the Application Identifier is the same as the application name.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ApplicationName")) {
         var3 = "getApplicationName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationName", DomainLibraryMBean.class, var3, var4);
         var1.put("ApplicationName", var2);
         var2.setValue("description", "<p>The name of the application.</p> <p>Note that the name of the current MBean is not the name of the application.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("DeploymentPlan")) {
         var3 = "getDeploymentPlan";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentPlan", DomainLibraryMBean.class, var3, var4);
         var1.put("DeploymentPlan", var2);
         var2.setValue("description", "The contents of this application's deployment plan, returned as a byte[] containing the XML. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("sensitive", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
         var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var5);
      }

      if (!var1.containsKey("DeploymentPlanExternalDescriptors")) {
         var3 = "getDeploymentPlanExternalDescriptors";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentPlanExternalDescriptors", DomainLibraryMBean.class, var3, var4);
         var1.put("DeploymentPlanExternalDescriptors", var2);
         var2.setValue("description", "A zip file containing the external descriptors referenced in the deployment plan. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("sensitive", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
         var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var5);
      }

      if (!var1.containsKey("LocalInstallDir")) {
         var3 = "getLocalInstallDir";
         var4 = null;
         var2 = new PropertyDescriptor("LocalInstallDir", DomainLibraryMBean.class, var3, var4);
         var1.put("LocalInstallDir", var2);
         var2.setValue("description", "The location of this application's installation root directory on the current server. This method will throw an unchecked IllegalStateEception if not invoked from within the context of a server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalPlanDir")) {
         var3 = "getLocalPlanDir";
         var4 = null;
         var2 = new PropertyDescriptor("LocalPlanDir", DomainLibraryMBean.class, var3, var4);
         var1.put("LocalPlanDir", var2);
         var2.setValue("description", "The location of this application's deployment plan directory on the current server. This method will throw an unchecked IllegalStateEception if not invoked from within the context of a server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalPlanPath")) {
         var3 = "getLocalPlanPath";
         var4 = null;
         var2 = new PropertyDescriptor("LocalPlanPath", DomainLibraryMBean.class, var3, var4);
         var1.put("LocalPlanPath", var2);
         var2.setValue("description", "The location of this application's deployment plan on the current server. This method will throw an unchecked IllegalStateEception if not invoked from within the context of a server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalSourcePath")) {
         var3 = "getLocalSourcePath";
         var4 = null;
         var2 = new PropertyDescriptor("LocalSourcePath", DomainLibraryMBean.class, var3, var4);
         var1.put("LocalSourcePath", var2);
         var2.setValue("description", "The location of this application's source files on the current server. This method will throw an unchecked IllegalStateEception if not invoked from within the context of a server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", DomainLibraryMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Unique identifier for this bean instance. ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PlanDir")) {
         var3 = "getPlanDir";
         var4 = null;
         var2 = new PropertyDescriptor("PlanDir", DomainLibraryMBean.class, var3, var4);
         var1.put("PlanDir", var2);
         var2.setValue("description", "<p>The location of this application's configuration area. This directory can contain external descriptor files as specified within the deployment plan document.</p>  <p>Rules:</p> If the plan directory is a relative path, it is resolved relative to InstallDir if InstallDir is not null; otherwise, it is resolved relative to domain root.  <p>Use AbsolutePlanDir to get a fully resolved value.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getInstallDir"), BeanInfoHelper.encodeEntities("#getAbsolutePlanDir")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PlanPath")) {
         var3 = "getPlanPath";
         var4 = null;
         var2 = new PropertyDescriptor("PlanPath", DomainLibraryMBean.class, var3, var4);
         var1.put("PlanPath", var2);
         var2.setValue("description", "<p>The path to the deployment plan document on Administration Server.</p> <p>Rules:</p> If the plan path is a relative path, it is resolved relative to PlanDir if PlanDir is not null; otherwise, it is resolved relative to domain root.  <p>Use AbsolutePlanPath to get a fully resolved value.</p> <p>If there is no plan, this returns no plan specified.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPlanDir"), BeanInfoHelper.encodeEntities("#getAbsolutePlanPath")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RootStagingDir")) {
         var3 = "getRootStagingDir";
         var4 = null;
         var2 = new PropertyDescriptor("RootStagingDir", DomainLibraryMBean.class, var3, var4);
         var1.put("RootStagingDir", var2);
         var2.setValue("description", "The root directory under which this application is staged. This method will throw an unchecked IllegalStateEception if not invoked from within the context of a server. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SourcePath")) {
         var3 = "getSourcePath";
         var4 = null;
         var2 = new PropertyDescriptor("SourcePath", DomainLibraryMBean.class, var3, var4);
         var1.put("SourcePath", var2);
         var2.setValue("description", "<p>The path to the source of the deployable unit on the Administration Server.</p> <p>Rules:</p> If the source path is relative, it is resolved relative to InstallDir/app if InstallDir is not null; Otherwise, it is resolved relative to domain root.  <p>Use AbsoluteSourcePath to get a fully resolved value.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getInstallDir"), BeanInfoHelper.encodeEntities("#getAbsoluteSourcePath")};
         var2.setValue("see", var5);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("StagingMode")) {
         var3 = "getStagingMode";
         var4 = null;
         var2 = new PropertyDescriptor("StagingMode", DomainLibraryMBean.class, var3, var4);
         var1.put("StagingMode", var2);
         var2.setValue("description", "<p>The mode that specifies whether a deployment's files are copied from a source on the Administration Server to the Managed Server's staging area during application preparation. </p> <p>Staging mode for an application can only be set the first time the application is deployed. Once staging mode for an application is set, it cannot be changed while the application is configured in the domain. The only way to change staging mode is to undeploy then redeploy the application.</p> <p>This attribute overrides the server's staging mode. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ServerMBean#getStagingMode")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, AppDeploymentMBean.DEFAULT_STAGE);
         var2.setValue("legalValues", new Object[]{AppDeploymentMBean.DEFAULT_STAGE, "nostage", "stage", "external_stage"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("VersionIdentifier")) {
         var3 = "getVersionIdentifier";
         var4 = null;
         var2 = new PropertyDescriptor("VersionIdentifier", DomainLibraryMBean.class, var3, var4);
         var1.put("VersionIdentifier", var2);
         var2.setValue("description", "<p>Uniquely identifies the application version across all versions of the same application.</p> <p>If the application is not versioned, this returns null.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("AutoDeployedApp")) {
         var3 = "isAutoDeployedApp";
         var4 = null;
         var2 = new PropertyDescriptor("AutoDeployedApp", DomainLibraryMBean.class, var3, var4);
         var1.put("AutoDeployedApp", var2);
         var2.setValue("description", "If the application was autodeployed (regardless of whether the app. was autodeployed in this session or not) ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("transient", Boolean.TRUE);
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
      Method var3 = DomainLibraryMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = DomainLibraryMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
      }

      var3 = DomainLibraryMBean.class.getMethod("getStagingMode", String.class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", "The staging mode associated with this application, if not explicit from the StagingMode property. This method will throw an unchecked IllegalStateEception if not invoked from within the context of a server. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("transient", Boolean.TRUE);
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
