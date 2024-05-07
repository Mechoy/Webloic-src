package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class HTTPProxyMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = HTTPProxyMBean.class;

   public HTTPProxyMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public HTTPProxyMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = HTTPProxyMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.HTTPProxyMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("HealthCheckInterval")) {
         var3 = "getHealthCheckInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckInterval";
         }

         var2 = new PropertyDescriptor("HealthCheckInterval", HTTPProxyMBean.class, var3, var4);
         var1.put("HealthCheckInterval", var2);
         var2.setValue("description", "<p>The health check interval in milliseconds between pings.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMax", new Integer(300));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InitialConnections")) {
         var3 = "getInitialConnections";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitialConnections";
         }

         var2 = new PropertyDescriptor("InitialConnections", HTTPProxyMBean.class, var3, var4);
         var1.put("InitialConnections", var2);
         var2.setValue("description", "The number of initial connections that should be opened to each server in the back end servers.</p> ");
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaxConnections")) {
         var3 = "getMaxConnections";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxConnections";
         }

         var2 = new PropertyDescriptor("MaxConnections", HTTPProxyMBean.class, var3, var4);
         var1.put("MaxConnections", var2);
         var2.setValue("description", "<p>The maximum number of connections that each server can open to the back end servers.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(100));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaxHealthCheckInterval")) {
         var3 = "getMaxHealthCheckInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxHealthCheckInterval";
         }

         var2 = new PropertyDescriptor("MaxHealthCheckInterval", HTTPProxyMBean.class, var3, var4);
         var1.put("MaxHealthCheckInterval", var2);
         var2.setValue("description", "<p>The maximum interval between health checks.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
      }

      if (!var1.containsKey("MaxRetries")) {
         var3 = "getMaxRetries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxRetries";
         }

         var2 = new PropertyDescriptor("MaxRetries", HTTPProxyMBean.class, var3, var4);
         var1.put("MaxRetries", var2);
         var2.setValue("description", "<p>The max retries after which the server will be marked dead.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMax", new Integer(200));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerList")) {
         var3 = "getServerList";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerList";
         }

         var2 = new PropertyDescriptor("ServerList", HTTPProxyMBean.class, var3, var4);
         var1.put("ServerList", var2);
         var2.setValue("description", "<p>The list of servers in the back end that the HCS should proxy to.</p> ");
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
