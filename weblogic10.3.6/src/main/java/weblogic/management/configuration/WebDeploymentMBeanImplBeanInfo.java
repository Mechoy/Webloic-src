package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebDeploymentMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebDeploymentMBean.class;

   public WebDeploymentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebDeploymentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebDeploymentMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>A Web Deployment is any MBean that may be deployed on one or more target or WebServers. Deployments of this type get deployed on web servers. Any target specified through the \"Targets\" attribute of the deployment are deployed on the default web server of that deployment. Targets specified through the \"WebServers\" attribute of the deployment are specified in the targeted Web Server.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebDeploymentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0.", (String)null, this.targetVersion) && !var1.containsKey("VirtualHosts")) {
         var3 = "getVirtualHosts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVirtualHosts";
         }

         var2 = new PropertyDescriptor("VirtualHosts", WebDeploymentMBean.class, var3, var4);
         var1.put("VirtualHosts", var2);
         var2.setValue("description", "<p>Provides a means to target your deployments to specific virtual hosts.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeVirtualHost");
         var2.setValue("adder", "addVirtualHost");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0.");
      }

      if (!var1.containsKey("WebServers")) {
         var3 = "getWebServers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWebServers";
         }

         var2 = new PropertyDescriptor("WebServers", WebDeploymentMBean.class, var3, var4);
         var1.put("WebServers", var2);
         var2.setValue("description", "<p>Returns a list of the targets on which this deployment is deployed.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addWebServer");
         var2.setValue("remover", "removeWebServer");
         var2.setValue("deprecated", "7.0.0.0 This attribute is being replaced by VirtualHosts attribute. To target  an actual web server, the ComponentMBean.Targets attribute should be used. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WebDeploymentMBean.class.getMethod("addWebServer", WebServerMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the WebServer attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "7.0.0.0 This attribute is being replaced by VirtualHosts attribute ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>This adds a target to the list of web servers to which a deployment may be targeted.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "WebServers");
      }

      var3 = WebDeploymentMBean.class.getMethod("removeWebServer", WebServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "7.0.0.0 This attribute is being replaced by VirtualHosts attribute ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>This removes a target from the list of web servers which may be targeted for deployments.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "WebServers");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0.", (String)null, this.targetVersion)) {
         var3 = WebDeploymentMBean.class.getMethod("addVirtualHost", VirtualHostMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the VirtualHost attribute ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0.");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Used to add a virtual host to the list of virtual hosts to which deployments may be targeted.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "VirtualHosts");
            var2.setValue("since", "7.0.0.0.");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0.", (String)null, this.targetVersion)) {
         var3 = WebDeploymentMBean.class.getMethod("removeVirtualHost", VirtualHostMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0.");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Used to remove a virtual host from the list of virtual hosts to which deployments may be targeted.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "VirtualHosts");
            var2.setValue("since", "7.0.0.0.");
         }
      }

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
