package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SingletonServiceBaseMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SingletonServiceBaseMBean.class;

   public SingletonServiceBaseMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SingletonServiceBaseMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SingletonServiceBaseMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.2.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("TargetMBean"), BeanInfoHelper.encodeEntities("SingletonServiceMBean"), BeanInfoHelper.encodeEntities("SingletonServiceAppScopedMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("A service that will be automatically maintained as a Singleton in a cluster. There will always be exactly one instance of it active at any given time.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SingletonServiceBaseMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AdditionalMigrationAttempts")) {
         var3 = "getAdditionalMigrationAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdditionalMigrationAttempts";
         }

         var2 = new PropertyDescriptor("AdditionalMigrationAttempts", SingletonServiceBaseMBean.class, var3, var4);
         var1.put("AdditionalMigrationAttempts", var2);
         var2.setValue("description", "A migratable service could fail to come up on every possible configured server. This attribute controls how many further attempts, after the service has failed on every server at least once, should be tried. Note that each attempt specified here indicates another full circuit of migrations amongst all the configured servers. So for a 3-server cluster, and a value of 2, a total of 4 additional migrations will be attempted. (the original server is never a valid destination) ");
         setPropertyDescriptorDefault(var2, new Integer(2));
      }

      if (!var1.containsKey("HostingServer")) {
         var3 = "getHostingServer";
         var4 = null;
         var2 = new PropertyDescriptor("HostingServer", SingletonServiceBaseMBean.class, var3, var4);
         var1.put("HostingServer", var2);
         var2.setValue("description", "<p>Returns the name of the server that currently hosts the singleton service.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MillisToSleepBetweenAttempts")) {
         var3 = "getMillisToSleepBetweenAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMillisToSleepBetweenAttempts";
         }

         var2 = new PropertyDescriptor("MillisToSleepBetweenAttempts", SingletonServiceBaseMBean.class, var3, var4);
         var1.put("MillisToSleepBetweenAttempts", var2);
         var2.setValue("description", "Controls how long of a pause there should be between the migration attempts described in getAdditionalMigrationAttempts(). Note that this delay only happens when the service has failed to come up on every server. It does not cause any sort of delay between attempts to migrate otherwise. ");
         setPropertyDescriptorDefault(var2, new Integer(300000));
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SingletonServiceBaseMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("UserPreferredServer")) {
         var3 = "getUserPreferredServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPreferredServer";
         }

         var2 = new PropertyDescriptor("UserPreferredServer", SingletonServiceBaseMBean.class, var3, var4);
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
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SingletonServiceBaseMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = SingletonServiceBaseMBean.class.getMethod("restoreDefaultValue", String.class);
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
