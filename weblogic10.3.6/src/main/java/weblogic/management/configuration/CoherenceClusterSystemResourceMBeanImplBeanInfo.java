package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class CoherenceClusterSystemResourceMBeanImplBeanInfo extends SystemResourceMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CoherenceClusterSystemResourceMBean.class;

   public CoherenceClusterSystemResourceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CoherenceClusterSystemResourceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CoherenceClusterSystemResourceMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean defines a system-level Coherence cluster resource. It links to a separate descriptor that specifies the definition.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CoherenceClusterSystemResourceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CoherenceClusterResource")) {
         var3 = "getCoherenceClusterResource";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceClusterResource", CoherenceClusterSystemResourceMBean.class, var3, var4);
         var1.put("CoherenceClusterResource", var2);
         var2.setValue("description", "<p> The Coherence cluster resource descriptor. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomClusterConfigurationFileName")) {
         var3 = "getCustomClusterConfigurationFileName";
         var4 = null;
         var2 = new PropertyDescriptor("CustomClusterConfigurationFileName", CoherenceClusterSystemResourceMBean.class, var3, var4);
         var1.put("CustomClusterConfigurationFileName", var2);
         var2.setValue("description", "<p> The external custom Coherence cluster configuration file. </p> ");
      }

      if (!var1.containsKey("DescriptorFileName")) {
         var3 = "getDescriptorFileName";
         var4 = null;
         var2 = new PropertyDescriptor("DescriptorFileName", CoherenceClusterSystemResourceMBean.class, var3, var4);
         var1.put("DescriptorFileName", var2);
         var2.setValue("description", "<p> The name of the file that contains the module configuration. By default the file resides in the DOMAIN_DIR/config/coherence/<bean name> directory. </p> <p> The module file derives its name from the bean name using the following pattern: </p> <p> <beanName>.xml <p> Note that this is a read-only property that can only be set when the bean is created. <p> ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", CoherenceClusterSystemResourceMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Unique identifier for this bean instance. ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Resource")) {
         var3 = "getResource";
         var4 = null;
         var2 = new PropertyDescriptor("Resource", CoherenceClusterSystemResourceMBean.class, var3, var4);
         var1.put("Resource", var2);
         var2.setValue("description", "<p>Return the Descriptor for the system resource. This should be overridden by the derived system resources. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("UsingCustomClusterConfigurationFile")) {
         var3 = "isUsingCustomClusterConfigurationFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUsingCustomClusterConfigurationFile";
         }

         var2 = new PropertyDescriptor("UsingCustomClusterConfigurationFile", CoherenceClusterSystemResourceMBean.class, var3, var4);
         var1.put("UsingCustomClusterConfigurationFile", var2);
         var2.setValue("description", "<p> Specifies whether you are using a custom external Coherence cluster configuration file. </p> ");
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
      Method var3 = CoherenceClusterSystemResourceMBean.class.getMethod("importCustomClusterConfigurationFile", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("file", "Absolute path to the custom Coherence cluster configuration file </p> ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p> Allows you to specify a custom Coherence cluster configuration file. The file must be present locally on the Administration Server. ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceClusterSystemResourceMBean.class.getMethod("freezeCurrentValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceClusterSystemResourceMBean.class.getMethod("restoreDefaultValue", String.class);
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
