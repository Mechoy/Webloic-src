package weblogic.management.security;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;

public class ProviderMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ProviderMBean.class;

   public ProviderMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ProviderMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ProviderMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security");
      String var3 = (new String("The base MBean for all security providers. <p> It includes attributes common to all security providers. Every security provider must implement an MBean that extends this MBean.</p> <p> If the security provider supports management methods, the management methods cannot be called until the validate method of realm in which the security provider is configured successfully returns. That is, the administrator must completely configure the realm before using the management methods (for example, adding a user). </p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.ProviderMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", ProviderMBean.class, var3, (String)var4);
         var1.put("Description", var2);
         var2.setValue("description", "Returns a description of this security provider. <p> Each security provider's MBean should set the default value of this read-only attribute to a string that describes the provider.  In other words, each security provider's MBean hard-wires its description.  There are no conventions governing the contents of the description.  It should be a human readable string that gives a brief description of the security provider. </p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", ProviderMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "The name of this configuration. WebLogic Server uses an MBean to implement and persist the configuration. ");
         setPropertyDescriptorDefault(var2, "Provider");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("legal", "");
      }

      if (!var1.containsKey("Realm")) {
         var3 = "getRealm";
         var4 = null;
         var2 = new PropertyDescriptor("Realm", ProviderMBean.class, var3, (String)var4);
         var1.put("Realm", var2);
         var2.setValue("description", "Returns the realm that contains this security provider. Returns null if this security provider is not contained by a realm. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Version")) {
         var3 = "getVersion";
         var4 = null;
         var2 = new PropertyDescriptor("Version", ProviderMBean.class, var3, (String)var4);
         var1.put("Version", var2);
         var2.setValue("description", "Returns this security provider's version. <p> Each security provider's MBean should set the default value of this read-only attribute to a string that specifies the version of the provider (e.g. 7.3.04).  In other words, each security provider's MBean hard-wires its version.  There are no conventions governing the contents of the version string. </p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
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
