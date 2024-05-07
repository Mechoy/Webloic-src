package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCImportMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCImportMBean.class;

   public WTCImportMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCImportMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCImportMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC import configuration attributes.  The methods defined herein are applicable for WTC configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCImportMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("LocalAccessPoint")) {
         var3 = "getLocalAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocalAccessPoint";
         }

         var2 = new PropertyDescriptor("LocalAccessPoint", WTCImportMBean.class, var3, var4);
         var1.put("LocalAccessPoint", var2);
         var2.setValue("description", "<p>The name of the local access point that offers this service.</p> ");
         setPropertyDescriptorDefault(var2, "myLAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoteAccessPointList")) {
         var3 = "getRemoteAccessPointList";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteAccessPointList";
         }

         var2 = new PropertyDescriptor("RemoteAccessPointList", WTCImportMBean.class, var3, var4);
         var1.put("RemoteAccessPointList", var2);
         var2.setValue("description", "<p>The comma-separated failover list that identifies the remote domain access points through which resources are imported.</p>  <p>For example: <tt>TDOM1,TDOM2,TDOM3</tt></p> ");
         setPropertyDescriptorDefault(var2, "myRAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoteName")) {
         var3 = "getRemoteName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteName";
         }

         var2 = new PropertyDescriptor("RemoteName", WTCImportMBean.class, var3, var4);
         var1.put("RemoteName", var2);
         var2.setValue("description", "<p>The remote name of this service.</p>  <p><i>Note:</i> If not specified, the ResourceName value is used.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ResourceName")) {
         var3 = "getResourceName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResourceName";
         }

         var2 = new PropertyDescriptor("ResourceName", WTCImportMBean.class, var3, var4);
         var1.put("ResourceName", var2);
         var2.setValue("description", "<p>The name used to identify this imported service.</p>  <p><i>Note:</i> This name must be unique within defined Imports. This allows you to define unique configurations having the same Remote Name.</p> ");
         setPropertyDescriptorDefault(var2, "myImport");
         var2.setValue("legalNull", Boolean.TRUE);
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
