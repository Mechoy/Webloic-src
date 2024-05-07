package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SingletonServiceAppScopedMBeanImplBeanInfo extends SingletonServiceBaseMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SingletonServiceAppScopedMBean.class;

   public SingletonServiceAppScopedMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SingletonServiceAppScopedMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SingletonServiceAppScopedMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.2.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("SingletonServiceBaseMBean"), BeanInfoHelper.encodeEntities("SubDeploymentMBean"), BeanInfoHelper.encodeEntities("SingletonServiceMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("A service that will be automatically maintained as a Singleton in a cluster. There will always be exactly one instance of it active at any given time.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SingletonServiceAppScopedMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ClassName")) {
         var3 = "getClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassName";
         }

         var2 = new PropertyDescriptor("ClassName", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("ClassName", var2);
         var2.setValue("description", "<p>The fully qualified name of a class to load and run. The class must be on the server's classpath.</p>  <p>For example, <code>mycompany.mypackage.myclass</code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("CompatibilityName")) {
         var3 = "getCompatibilityName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompatibilityName";
         }

         var2 = new PropertyDescriptor("CompatibilityName", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("CompatibilityName", var2);
         var2.setValue("description", "<p> This is only set for beans created as a result of conversion from an 8.1 application configured using ApplicationMBean and ComponentMBean. </p> Standalone modules in 8.1 have both an ApplicationMBean name and ComponentMBean name. This attribute stores the name of the latter, to be used when the server creates the transient ComponentMBean for backward compatibility. ");
      }

      if (!var1.containsKey("HostingServer")) {
         var3 = "getHostingServer";
         var4 = null;
         var2 = new PropertyDescriptor("HostingServer", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("HostingServer", var2);
         var2.setValue("description", "<p>Returns the name of the server that currently hosts the singleton service.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ModuleType")) {
         var3 = "getModuleType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setModuleType";
         }

         var2 = new PropertyDescriptor("ModuleType", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("ModuleType", var2);
         var2.setValue("description", "The values match those defined by jsr88. This attribute may move to another MBean. ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Unique identifier for this bean instance. It is used by the application container to match the module in the application package to the targeting information in the configuration.  <p>For modules within an EAR, the name should be the URI of the module as defined in the META-INF/application.xml deployment descriptor. There is an exception to this for web applications. See below.</p> <p>For web modules in an EAR, the name should always equal the context root of that webapp, because the URI is not always unique</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("SubDeployments")) {
         var3 = "getSubDeployments";
         var4 = null;
         var2 = new PropertyDescriptor("SubDeployments", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("SubDeployments", var2);
         var2.setValue("description", "<p>The subdeployment groups within this JMS module. Subdeployments enable you to deploy some resources in a JMS module to a JMS server and other JMS resources to a server instance or cluster.</p>  <p>Standalone queues or topics can only be targeted to a single JMS server. Whereas, connection factories, uniform distributed destinations (UDDs), and foreign servers can be targeted to one or more JMS servers, one or more server instances, or to a cluster. Therefore, standalone queues or topics cannot be associated with a subdeployment if other members of the subdeployment are targeted to multiple JMS servers. However, UDDs can be associated with such subdeployments since the purpose of UDDs is to distribute its members to multiple JMS servers in a domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UserPreferredServer")) {
         var3 = "getUserPreferredServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPreferredServer";
         }

         var2 = new PropertyDescriptor("UserPreferredServer", SingletonServiceAppScopedMBean.class, var3, var4);
         var1.put("UserPreferredServer", var2);
         var2.setValue("description", "<p>Returns the server that the user prefers the singleton service to be active on.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SingletonServiceAppScopedMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = SingletonServiceAppScopedMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The target to remove ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the Target attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SingletonServiceAppScopedMBean.class.getMethod("lookupSubDeployment", String.class);
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
      Method var3 = SingletonServiceAppScopedMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SingletonServiceAppScopedMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
