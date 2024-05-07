package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class SNMPJMXMonitorMBeanImplBeanInfo extends SNMPTrapSourceMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPJMXMonitorMBean.class;

   public SNMPJMXMonitorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPJMXMonitorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPJMXMonitorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This is a base class for Monitor based trap configuration MBeans : SNMPCounterMonitorMBean, SNMPStringMonitorMBean and SNMPGaugeMonitorMBean.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPJMXMonitorMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MonitoredAttributeName")) {
         var3 = "getMonitoredAttributeName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMonitoredAttributeName";
         }

         var2 = new PropertyDescriptor("MonitoredAttributeName", SNMPJMXMonitorMBean.class, var3, var4);
         var1.put("MonitoredAttributeName", var2);
         var2.setValue("description", "<p>The name of an MBean attribute to monitor. This attribute must be in the WebLogic Server MIB.</p> ");
      }

      if (!var1.containsKey("MonitoredMBeanName")) {
         var3 = "getMonitoredMBeanName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMonitoredMBeanName";
         }

         var2 = new PropertyDescriptor("MonitoredMBeanName", SNMPJMXMonitorMBean.class, var3, var4);
         var1.put("MonitoredMBeanName", var2);
         var2.setValue("description", "<p>The name of the MBean instance that you want to monitor. If you leave the name undefined, WebLogic Server monitors all instances of the MBean type that you specify in Monitored MBean Type.</p>  <p>If you target SNMP agents to individual Managed Servers, make sure that the MBean instance you specify is active on the Managed Servers you have targeted. For example, if you specify <code>MServer1</code> as the name of a ServerRuntimeMBean instance, this monitor will only function if you target an SNMP agent either to the Administration Server or to a Managed Server named <code>MServer1</code>.</p>  <p>Do not enter the full JMX object name of the MBean instance. Instead, enter only the value of the object name's <code>Name=<i>name</i></code> name-value pair. To create unique MBean object names, WebLogic Server encodes several name-value pairs into each object name. One of these pairs is <code>Name=<i>name</i></code>. For example:<br clear=\"none\" /> <code>\"MedRec:<b>Name=MedRecServer</b>, <br clear=\"none\" />Type=ServerRuntime\"</code></p>  <p>In the previous example, specify <code>MedRecServer</code> as the name of the MBean instance.</p> ");
      }

      if (!var1.containsKey("MonitoredMBeanType")) {
         var3 = "getMonitoredMBeanType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMonitoredMBeanType";
         }

         var2 = new PropertyDescriptor("MonitoredMBeanType", SNMPJMXMonitorMBean.class, var3, var4);
         var1.put("MonitoredMBeanType", var2);
         var2.setValue("description", "<p>The MBean type that defines the attribute you want to monitor. Do not include the <code>MBean</code> suffix. For example, <code>ServerRuntime</code>.</p> ");
      }

      if (!var1.containsKey("PollingInterval")) {
         var3 = "getPollingInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPollingInterval";
         }

         var2 = new PropertyDescriptor("PollingInterval", SNMPJMXMonitorMBean.class, var3, var4);
         var1.put("PollingInterval", var2);
         var2.setValue("description", "<p>The frequency (in seconds) that WebLogic Server checks the attribute value.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
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
