package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class SNMPAttributeChangeMBeanImplBeanInfo extends SNMPTrapSourceMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPAttributeChangeMBean.class;

   public SNMPAttributeChangeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPAttributeChangeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPAttributeChangeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This class describes the settings to receive MBean-attribute change notification.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPAttributeChangeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AttributeMBeanName")) {
         var3 = "getAttributeMBeanName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAttributeMBeanName";
         }

         var2 = new PropertyDescriptor("AttributeMBeanName", SNMPAttributeChangeMBean.class, var3, var4);
         var1.put("AttributeMBeanName", var2);
         var2.setValue("description", "<p>The name of the MBean instance that you want to monitor. If you leave the name undefined, WebLogic Server monitors all instances of the MBean type that you specify in Monitored MBean Type.</p>  <p>Do not enter the full JMX object name of the MBean instance. Instead, enter only the value that you provided when you created the instance. To create unique MBean object names, WebLogic Server encodes several name-value pairs into each object name. One of these pairs is <code>Name=<i>name</i></code>, and this is the value that you enter for MBean Name. For example:<br clear=\"none\" /> <code>\"MedRec:<b>Name=MedRecServer</b>, <br clear=\"none\" />Type=ServerRuntime\"</code></p>  <p>In the previous example, specify <code>MedRecServer</code> as the name of the MBean instance.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AttributeMBeanType")) {
         var3 = "getAttributeMBeanType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAttributeMBeanType";
         }

         var2 = new PropertyDescriptor("AttributeMBeanType", SNMPAttributeChangeMBean.class, var3, var4);
         var1.put("AttributeMBeanType", var2);
         var2.setValue("description", "<p>The MBean type that defines the attribute you want to monitor. Do not include the <code>MBean</code> suffix. For example, specify <code>Server</code> to monitor a ServerMBean.</p>  <p>WebLogic Server does not support using Attribute Change notifications to monitor run-time attributes. Runtime MBeans always include the word <code>Runtime</code> in their names. For example, the <code>ServerRuntime</code> MBean provides access to runtime attributes while the <code>Server</code> MBean provides access to configuration attributes. To monitor changes in an MBean that includes <code>Runtime</code> in its name, use a String Monitor, Gauge Monitor, or Counter Monitor.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AttributeName")) {
         var3 = "getAttributeName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAttributeName";
         }

         var2 = new PropertyDescriptor("AttributeName", SNMPAttributeChangeMBean.class, var3, var4);
         var1.put("AttributeName", var2);
         var2.setValue("description", "<p>The name of the attribute that you want to monitor. This attribute must be in the WebLogic Server MIB.</p> ");
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
