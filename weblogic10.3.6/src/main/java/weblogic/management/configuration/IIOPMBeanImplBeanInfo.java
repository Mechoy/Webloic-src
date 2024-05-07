package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class IIOPMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = IIOPMBean.class;

   public IIOPMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public IIOPMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = IIOPMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Configuration for IIOP properties.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.IIOPMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CompleteMessageTimeout")) {
         var3 = "getCompleteMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteMessageTimeout", IIOPMBean.class, var3, var4);
         var1.put("CompleteMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete IIOP message to be received. This timeout helps guard against denial of service attacks in which a caller indicates that they will be sending a message of a certain size which they never finish sending.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", " ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultCharCodeset")) {
         var3 = "getDefaultCharCodeset";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultCharCodeset";
         }

         var2 = new PropertyDescriptor("DefaultCharCodeset", IIOPMBean.class, var3, var4);
         var1.put("DefaultCharCodeset", var2);
         var2.setValue("description", "<p>The standard character code set that this server will publish as its native code set. (Older ORBs may have trouble interoperating with anything other than the default.)</p> ");
         setPropertyDescriptorDefault(var2, "US-ASCII");
         var2.setValue("legalValues", new Object[]{"US-ASCII", "UTF-8", "ISO-8859-1"});
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultMinorVersion")) {
         var3 = "getDefaultMinorVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultMinorVersion";
         }

         var2 = new PropertyDescriptor("DefaultMinorVersion", IIOPMBean.class, var3, var4);
         var1.put("DefaultMinorVersion", var2);
         var2.setValue("description", "<p>The default GIOP (General Inter-ORB Protocol) version that this server will negotiate for incoming connections. (You may have to modify the default to work with other vendor's ORBs.)</p>  <p>This attribute is useful for client orbs with broken GIOP 1.2 implementations.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(2));
         var2.setValue("legalMax", new Integer(2));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultWideCharCodeset")) {
         var3 = "getDefaultWideCharCodeset";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultWideCharCodeset";
         }

         var2 = new PropertyDescriptor("DefaultWideCharCodeset", IIOPMBean.class, var3, var4);
         var1.put("DefaultWideCharCodeset", var2);
         var2.setValue("description", "<p>The wide character code set that this server will publish as its native code set. (Older ORBs may have trouble interoperating with anything other than the default.)</p> ");
         setPropertyDescriptorDefault(var2, "UCS-2");
         var2.setValue("legalValues", new Object[]{"UCS-2", "UTF-16", "UTF-8", "UTF-16BE", "UTF-16LE"});
      }

      if (!var1.containsKey("EnableIORServlet")) {
         var3 = "getEnableIORServlet";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnableIORServlet";
         }

         var2 = new PropertyDescriptor("EnableIORServlet", IIOPMBean.class, var3, var4);
         var1.put("EnableIORServlet", var2);
         var2.setValue("description", "Enable getior servlet used to publish COS Naming Service IORs ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("IdleConnectionTimeout")) {
         var3 = "getIdleConnectionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdleConnectionTimeout";
         }

         var2 = new PropertyDescriptor("IdleConnectionTimeout", IIOPMBean.class, var3, var4);
         var1.put("IdleConnectionTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds an IIOP connection is allowed to be idle before it is closed by the server. This timeout helps guard against server deadlock through too many open connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getIdleConnectionTimeout()} ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxMessageSize")) {
         var3 = "getMaxMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxMessageSize";
         }

         var2 = new PropertyDescriptor("MaxMessageSize", IIOPMBean.class, var3, var4);
         var1.put("MaxMessageSize", var2);
         var2.setValue("description", "<p>The maximum IIOP message size allowable in a message header. This attribute attempts to prevent a denial of service attack whereby a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(10000000));
         var2.setValue("deprecated", " ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("SystemSecurity")) {
         var3 = "getSystemSecurity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemSecurity";
         }

         var2 = new PropertyDescriptor("SystemSecurity", IIOPMBean.class, var3, var4);
         var1.put("SystemSecurity", var2);
         var2.setValue("description", "Specify the value System Security. The following variables are affected. clientCertAuthentication, clientAuthentication, identityAssertion confidentiality, integrity. The value set in this MBean would only be picked up if the value set in RTD.xml is \"config\". ");
         setPropertyDescriptorDefault(var2, "supported");
         var2.setValue("legalValues", new Object[]{"none", "supported", "required"});
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("TxMechanism")) {
         var3 = "getTxMechanism";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTxMechanism";
         }

         var2 = new PropertyDescriptor("TxMechanism", IIOPMBean.class, var3, var4);
         var1.put("TxMechanism", var2);
         var2.setValue("description", "<p>The transaction mechanism used by IIOP invocations. The default is the Object Transaction Service (OTS) required by J2EE 1.3.</p> ");
         setPropertyDescriptorDefault(var2, "OTS");
         var2.setValue("legalValues", new Object[]{"OTS", "JTA", "OTSv11", "none"});
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("UseFullRepositoryIdList")) {
         var3 = "getUseFullRepositoryIdList";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseFullRepositoryIdList";
         }

         var2 = new PropertyDescriptor("UseFullRepositoryIdList", IIOPMBean.class, var3, var4);
         var1.put("UseFullRepositoryIdList", var2);
         var2.setValue("description", "<p>Specify whether to use full Repository ID lists when sending value type information for custom-marshaled types. Full Repository ID lists allow C++ ORBS to truncate values to base types. For RMI-IIOP and Java ORBs doing this merely increases transmission overhead. JDK ORBs are known to have problems with these so setting this will prevent JDK ORB access from working.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("UseJavaSerialization")) {
         var3 = "getUseJavaSerialization";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseJavaSerialization";
         }

         var2 = new PropertyDescriptor("UseJavaSerialization", IIOPMBean.class, var3, var4);
         var1.put("UseJavaSerialization", var2);
         var2.setValue("description", "Specity whether to use java serialization for marshalling objects. Setting this property improves performance when marshalling objects with very large object graphs. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseSerialFormatVersion2")) {
         var3 = "getUseSerialFormatVersion2";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseSerialFormatVersion2";
         }

         var2 = new PropertyDescriptor("UseSerialFormatVersion2", IIOPMBean.class, var3, var4);
         var1.put("UseSerialFormatVersion2", var2);
         var2.setValue("description", "<p>Specify whether to advertise RMI objects and EJBs as supporting RMI-IIOP serial format version 2 for custom marshaled objects.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("UseStatefulAuthentication")) {
         var3 = "getUseStatefulAuthentication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseStatefulAuthentication";
         }

         var2 = new PropertyDescriptor("UseStatefulAuthentication", IIOPMBean.class, var3, var4);
         var1.put("UseStatefulAuthentication", var2);
         var2.setValue("description", "<p>Specify whether to advertise RMI objects and EJBs as supporting stateful CSIv2. Stateful CSIv2 is more efficient than stateless, requiring only a single authentication step for each remote principal. Stateless CSIv2 requires per-request authentication. Stateful CSIv2 is not required by J2EE 1.3 and so some ORBs do not support it. Stateful CSIv2 is enabled by default. This property can be changed at the object-level by changing the object's &lt;stateful-authentication&gt; runtime descriptor property.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
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
