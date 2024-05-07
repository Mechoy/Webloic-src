package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCPasswordMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCPasswordMBean.class;

   public WTCPasswordMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCPasswordMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCPasswordMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC password configuration attributes. The methods defined herein are applicable for WTC configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCPasswordMBean");
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

         var2 = new PropertyDescriptor("LocalAccessPoint", WTCPasswordMBean.class, var3, var4);
         var1.put("LocalAccessPoint", var2);
         var2.setValue("description", "<p>The name of the local access point to which this password applies.</p> ");
         setPropertyDescriptorDefault(var2, "myLAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalPassword")) {
         var3 = "getLocalPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocalPassword";
         }

         var2 = new PropertyDescriptor("LocalPassword", WTCPasswordMBean.class, var3, var4);
         var1.put("LocalPassword", var2);
         var2.setValue("description", "<p>The local password used to authenticate connections between the local access point and the remote access point.</p>  <p><i>Note:</i> This password is used to authenticate connections between the local Tuxedo access point identified by LocalAccessPoint and the remote Tuxedo access point identified by RemoteAccessPoint.</p> ");
         setPropertyDescriptorDefault(var2, "myLPWD");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalPasswordIV")) {
         var3 = "getLocalPasswordIV";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocalPasswordIV";
         }

         var2 = new PropertyDescriptor("LocalPasswordIV", WTCPasswordMBean.class, var3, var4);
         var1.put("LocalPasswordIV", var2);
         var2.setValue("description", "<p>The initialization vector used to encrypt the local password.</p> ");
         setPropertyDescriptorDefault(var2, "myLPWDIV");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoteAccessPoint")) {
         var3 = "getRemoteAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteAccessPoint";
         }

         var2 = new PropertyDescriptor("RemoteAccessPoint", WTCPasswordMBean.class, var3, var4);
         var1.put("RemoteAccessPoint", var2);
         var2.setValue("description", "<p>The name of the remote access point to which this password applies.</p> ");
         setPropertyDescriptorDefault(var2, "myRAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemotePassword")) {
         var3 = "getRemotePassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemotePassword";
         }

         var2 = new PropertyDescriptor("RemotePassword", WTCPasswordMBean.class, var3, var4);
         var1.put("RemotePassword", var2);
         var2.setValue("description", "<p>The remote password used to authenticate connections between the local access point and remote access point.</p>  <p><i>Note:</i> This password is used to authenticate connections between the local Tuxedo access point identified by LocalAccessPoint and the remote Tuxedo access point identified by RemoteAccessPoint.</p> ");
         setPropertyDescriptorDefault(var2, "myRPWD");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemotePasswordIV")) {
         var3 = "getRemotePasswordIV";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemotePasswordIV";
         }

         var2 = new PropertyDescriptor("RemotePasswordIV", WTCPasswordMBean.class, var3, var4);
         var1.put("RemotePasswordIV", var2);
         var2.setValue("description", "<p>The initialization vector used to encrypt the remote password.</p> ");
         setPropertyDescriptorDefault(var2, "myRPWDIV");
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
