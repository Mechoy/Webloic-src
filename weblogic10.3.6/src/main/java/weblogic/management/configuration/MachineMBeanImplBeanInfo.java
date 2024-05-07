package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class MachineMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MachineMBean.class;

   public MachineMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MachineMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MachineMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This bean represents a machine on which servers may be booted. A server is bound to a machine by calling to <code>ServerMBean.setMachine()</code>. Although it is typical that one <code>MachineMBean</code> refers to one physical machine and vice versa, it is possible to have a multihomed machine represented by multiple <code>MachineMBeans</code>. The only restriction is that each <code>MachineMBean</code> be configured with non-overlapping addresses. A configuration may contain one or more of <code>MachineMBeans</code> which may be looked up by their logical names.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.MachineMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Addresses")) {
         var3 = "getAddresses";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAddresses";
         }

         var2 = new PropertyDescriptor("Addresses", MachineMBean.class, var3, var4);
         var1.put("Addresses", var2);
         var2.setValue("description", "<p>The addresses by which this machine is known. May be either host names or literal IP addresses.</p> ");
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link ServerMBean#getListenAddress()} ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("NodeManager")) {
         var3 = "getNodeManager";
         var4 = null;
         var2 = new PropertyDescriptor("NodeManager", MachineMBean.class, var3, var4);
         var1.put("NodeManager", var2);
         var2.setValue("description", "<p>Returns the NodeManager Mbean that defines the configuration of the Node Manager instance that runs on the machine.</p> ");
         var2.setValue("relationship", "containment");
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
