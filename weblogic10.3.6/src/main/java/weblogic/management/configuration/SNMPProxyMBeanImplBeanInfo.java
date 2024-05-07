package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SNMPProxyMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPProxyMBean.class;

   public SNMPProxyMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPProxyMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPProxyMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents an SNMP agent that is proxied by a WebLogic Server SNMP agent.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPProxyMBean");
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

         var2 = new PropertyDescriptor("Community", SNMPProxyMBean.class, var3, var4);
         var1.put("Community", var2);
         var2.setValue("description", "<p>The community name to be passed on for all SNMPv1 requests to this proxied SNMP agent.</p>  <p>If you specify a <i>security name</i> for this proxied agent, the WebLogic SNMP agent ignores this community name. Instead, the agent encodes the security name in an SNMPv3 request and forwards the SNMPv3 request to this proxied agent.</p> ");
         setPropertyDescriptorDefault(var2, "public");
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OidRoot")) {
         var3 = "getOidRoot";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOidRoot";
         }

         var2 = new PropertyDescriptor("OidRoot", SNMPProxyMBean.class, var3, var4);
         var1.put("OidRoot", var2);
         var2.setValue("description", "<p>The root of the object identifier (OID) tree that this proxied SNMP agent controls.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Port")) {
         var3 = "getPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPort";
         }

         var2 = new PropertyDescriptor("Port", SNMPProxyMBean.class, var3, var4);
         var1.put("Port", var2);
         var2.setValue("description", "<p>The port number on which this proxied SNMP agent is listening.</p> ");
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SecurityLevel")) {
         var3 = "getSecurityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityLevel";
         }

         var2 = new PropertyDescriptor("SecurityLevel", SNMPProxyMBean.class, var3, var4);
         var1.put("SecurityLevel", var2);
         var2.setValue("description", "The security level that the proxied SNMP agent expects for the specified security name. ");
         setPropertyDescriptorDefault(var2, "noAuthNoPriv");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"noAuthNoPriv", "authNoPriv", "authPriv"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SecurityName")) {
         var3 = "getSecurityName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityName";
         }

         var2 = new PropertyDescriptor("SecurityName", SNMPProxyMBean.class, var3, var4);
         var1.put("SecurityName", var2);
         var2.setValue("description", "<p>The user name on whose behalf the WebLogic SNMP agent forwards v3 requests.  If not specified, the request is forwarded as a v1 request.</p>  <p>If you specify a security name, you must also specify a security level that is equal to or lower than the security level that is configured for communication between the WebLogic SNMP agent and SNMP managers. For example, if the WebLogic SNMP agent requires incoming SNMPv3 requests to use the authentication protocol but no privacy protocol, the security level for this proxy must be either Authentication Only or None.  Note that if you want to use the authorization  or privacy protocols, you must configure credential mapping in the WebLogic Server security realm.</p>  <p>The WebLogic SNMP agent cannot forward or pass through the credentials that are contained in SNMPv3 requests from SNMP managers. Instead, the agent authenticates and performs other security operations on incoming requests, and then constructs a new request to forward to a proxied agent.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getSecurityLevel()"), BeanInfoHelper.encodeEntities("#getCommunity()")};
         var2.setValue("see", var5);
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Timeout")) {
         var3 = "getTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeout";
         }

         var2 = new PropertyDescriptor("Timeout", SNMPProxyMBean.class, var3, var4);
         var1.put("Timeout", var2);
         var2.setValue("description", "<p>The number of milliseconds that the WebLogic Server SNMP agent waits for a response to requests that it forwards to this proxy agent.</p>  <p>If the interval elapses without a response, the WebLogic SNMP agent sends an error to the requesting manager.</p> ");
         setPropertyDescriptorDefault(var2, new Long(5000L));
         var2.setValue("legalMin", new Long(0L));
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
