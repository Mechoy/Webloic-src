package weblogic.ejb.container.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.j2ee.ComponentRuntimeMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.EJBComponentRuntimeMBean;

public class EJBComponentRuntimeMBeanImplBeanInfo extends ComponentRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EJBComponentRuntimeMBean.class;

   public EJBComponentRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EJBComponentRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EJBComponentRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.ejb.container.internal");
      String var3 = (new String("This is the top level interface for all runtime information collected for an EJB module.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.EJBComponentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("CoherenceClusterRuntime")) {
         var3 = "getCoherenceClusterRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceClusterRuntime", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("CoherenceClusterRuntime", var2);
         var2.setValue("description", "<p>Returns the Coherence Cluster related runtime mbean for this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.3.0");
      }

      if (!var1.containsKey("DeploymentState")) {
         var3 = "getDeploymentState";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentState", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("DeploymentState", var2);
         var2.setValue("description", "<p>The current deployment state of the module.</p>  <p>A module can be in one and only one of the following states. State can be changed via deployment or administrator console.</p>  <p>- UNPREPARED. State indicating at this  module is neither  prepared or active.</p>  <p>- PREPARED. State indicating at this module of this application is prepared, but not active. The classes have been loaded and the module has been validated.</p>  <p>- ACTIVATED. State indicating at this module  is currently active.</p>  <p>- NEW. State indicating this module has just been created and is being initialized.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#setDeploymentState(int)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("EJBComponent")) {
         var3 = "getEJBComponent";
         var4 = null;
         var2 = new PropertyDescriptor("EJBComponent", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("EJBComponent", var2);
         var2.setValue("description", "<p>Provides the associated EJBComponentMBean for this EJB module.</p> ");
      }

      if (!var1.containsKey("EJBRuntimes")) {
         var3 = "getEJBRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("EJBRuntimes", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("EJBRuntimes", var2);
         var2.setValue("description", "<p>Provides an array of EJBRuntimeMBean objects for this EJB module. The EJBRuntimeMBean instances can be cast to their appropriate subclass (EntityEJBRuntimeMBean, StatelessEJBRuntimeMBean, StatefulEJBRuntimeMBean or MessageDrivenEJBRuntimeMBean) to access additional runtime information for the particular EJB.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("KodoPersistenceUnitRuntimes")) {
         var3 = "getKodoPersistenceUnitRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("KodoPersistenceUnitRuntimes", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("KodoPersistenceUnitRuntimes", var2);
         var2.setValue("description", "<p>Provides an array of KodoPersistenceUnitRuntimeMBean objects for this EJB module. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ModuleId")) {
         var3 = "getModuleId";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleId", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("ModuleId", var2);
         var2.setValue("description", "<p>Returns the identifier for this Component.  The identifier is unique within the application.</p>  <p>Typical modules will use the URI for their id.  Web Modules will return their context-root since the web-uri may not be unique within an EAR. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SpringRuntimeMBean")) {
         var3 = "getSpringRuntimeMBean";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSpringRuntimeMBean";
         }

         var2 = new PropertyDescriptor("SpringRuntimeMBean", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("SpringRuntimeMBean", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all work managers defined in this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("WseeClientConfigurationRuntimes")) {
         var3 = "getWseeClientConfigurationRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeClientConfigurationRuntimes", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("WseeClientConfigurationRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service client reference configuration runtime instances that are contained in this EJB within an Enterprise application.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WseeClientRuntimes")) {
         var3 = "getWseeClientRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeClientRuntimes", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("WseeClientRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service client runtime instances that are contained in this Enterprise JavaBean component. </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WseeV2Runtimes")) {
         var3 = "getWseeV2Runtimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeV2Runtimes", EJBComponentRuntimeMBean.class, var3, var4);
         var1.put("WseeV2Runtimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service runtime instances that are contained in this EJB within an Enterprise application. </p> ");
         var2.setValue("relationship", "reference");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = EJBComponentRuntimeMBean.class.getMethod("lookupWseeClientRuntime", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("rawClientId", "The raw client ID of the client to lookup. This ID does not contain the application/component qualifiers that are prepended to the full client ID for the client. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      java.beans.MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new java.beans.MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service client runtime instances that is contained in this Enterprise JavaBean component. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeClientRuntimes");
      }

      var3 = EJBComponentRuntimeMBean.class.getMethod("lookupWseeV2Runtime", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The web service description name of the web service to look up. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new java.beans.MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service runtime instance that is contained in this EJB within an Enterprise application. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeV2Runtimes");
      }

      var3 = EJBComponentRuntimeMBean.class.getMethod("lookupWseeClientConfigurationRuntime", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The web service client reference name to look up. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new java.beans.MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service client reference configuration runtime instance that is contained in this EJB within an Enterprise application.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeClientConfigurationRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = EJBComponentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      java.beans.MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new java.beans.MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = EJBComponentRuntimeMBean.class.getMethod("getEJBRuntime", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("ejbName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new java.beans.MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides the EJBRuntimeMBean for the EJB with the specified name.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = EJBComponentRuntimeMBean.class.getMethod("getKodoPersistenceUnitRuntime", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("unitName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new java.beans.MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides the KodoPersistenceUnitRuntimeMBean for the EJB with the specified name.</p> ");
         var2.setValue("role", "operation");
      }

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
