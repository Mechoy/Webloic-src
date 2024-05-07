package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCExportMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCExportMBean.class;

   public WTCExportMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCExportMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCExportMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC export configuration attributes.  The methods defined herein are applicable for WTC configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCExportMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("EJBName")) {
         var3 = "getEJBName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEJBName";
         }

         var2 = new PropertyDescriptor("EJBName", WTCExportMBean.class, var3, var4);
         var1.put("EJBName", var2);
         var2.setValue("description", "<p>The complete name of the EJB home interface to use when invoking a service.</p>  <p>If not specified, the default interface used is <code>tuxedo.services.<i>servicename</i>Home</code>. For example: If the service being invoked is TOUPPER and EJBName attribute is not specified, the home interface looked up in JNDI would be <code>tuxedo.services.TOUPPERHome</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalAccessPoint")) {
         var3 = "getLocalAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocalAccessPoint";
         }

         var2 = new PropertyDescriptor("LocalAccessPoint", WTCExportMBean.class, var3, var4);
         var1.put("LocalAccessPoint", var2);
         var2.setValue("description", "<p>The name of the local access point that exports this service.</p> ");
         setPropertyDescriptorDefault(var2, "myLAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoteName")) {
         var3 = "getRemoteName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteName";
         }

         var2 = new PropertyDescriptor("RemoteName", WTCExportMBean.class, var3, var4);
         var1.put("RemoteName", var2);
         var2.setValue("description", "<p>The remote name of this service. </p>  <p>If this value is not specified, the ResourceName value is used.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ResourceName")) {
         var3 = "getResourceName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResourceName";
         }

         var2 = new PropertyDescriptor("ResourceName", WTCExportMBean.class, var3, var4);
         var1.put("ResourceName", var2);
         var2.setValue("description", "<p>The name used to identify an exported service.</p>  <p>The combination of ResourceName and LocalAccessPoint must be unique within defined Exports. This allows you to define unique configurations having the same ResourceName.</p> ");
         setPropertyDescriptorDefault(var2, "myExport");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TargetClass")) {
         var3 = "getTargetClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetClass";
         }

         var2 = new PropertyDescriptor("TargetClass", WTCExportMBean.class, var3, var4);
         var1.put("TargetClass", var2);
         var2.setValue("description", " ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("TargetJar")) {
         var3 = "getTargetJar";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetJar";
         }

         var2 = new PropertyDescriptor("TargetJar", WTCExportMBean.class, var3, var4);
         var1.put("TargetJar", var2);
         var2.setValue("description", " ");
         var2.setValue("dynamic", Boolean.TRUE);
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
