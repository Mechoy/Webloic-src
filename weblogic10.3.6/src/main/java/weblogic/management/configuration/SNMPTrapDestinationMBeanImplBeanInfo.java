package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SNMPTrapDestinationMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPTrapDestinationMBean.class;

   public SNMPTrapDestinationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPTrapDestinationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPTrapDestinationMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean describes a destination to which an SNMP agent sends SNMP TRAP and INFORM notifications.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPTrapDestinationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Community")) {
         var3 = "getCommunity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCommunity";
         }

         var2 = new PropertyDescriptor("Community", SNMPTrapDestinationMBean.class, var3, var4);
         var1.put("Community", var2);
         var2.setValue("description", "<p>The password (community name) that a WebLogic Server SNMP agent sends to the SNMP manager when the agent generates SNMPv1 or SNMPv2 notifications.</p> <p>The community name that you enter in this trap destination must match the name that the SNMP manager defines.</p> ");
         setPropertyDescriptorDefault(var2, "public");
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Host")) {
         var3 = "getHost";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHost";
         }

         var2 = new PropertyDescriptor("Host", SNMPTrapDestinationMBean.class, var3, var4);
         var1.put("Host", var2);
         var2.setValue("description", "<p>The DNS name or IP address of the computer on which the SNMP manager is running.</p>  <p>The WebLogic SNMP agent sends trap notifications to the host and port that you specify.</p> ");
         setPropertyDescriptorDefault(var2, "localhost");
         var2.setValue("secureValue", "127.0.0.1");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Port")) {
         var3 = "getPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPort";
         }

         var2 = new PropertyDescriptor("Port", SNMPTrapDestinationMBean.class, var3, var4);
         var1.put("Port", var2);
         var2.setValue("description", "<p>The UDP port on which the SNMP manager is listening.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(162));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SecurityLevel")) {
         var3 = "getSecurityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityLevel";
         }

         var2 = new PropertyDescriptor("SecurityLevel", SNMPTrapDestinationMBean.class, var3, var4);
         var1.put("SecurityLevel", var2);
         var2.setValue("description", "<p>Specifies the security protocols that the SNMP agent uses when sending SNMPv3 responses or notifications to the SNMP manager that this trap destination represents. Requires you to specify a security name for this trap destination.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getSecurityName()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "noAuthNoPriv");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"noAuthNoPriv", "authNoPriv", "authPriv"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SecurityName")) {
         var3 = "getSecurityName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityName";
         }

         var2 = new PropertyDescriptor("SecurityName", SNMPTrapDestinationMBean.class, var3, var4);
         var1.put("SecurityName", var2);
         var2.setValue("description", "<p>Specifies the user name that the WebLogic Server SNMP agent encodes into SNMPv3 responses or notifications. Requires you to create a credential map for this user name in the WebLogic Server security realm.</p> <p>The credential map contains an authentication password and an optional privacy password for this user.</p> <p>The user name and passwords must match the credentials required by the SNMP manager that this trap destination represents.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
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
