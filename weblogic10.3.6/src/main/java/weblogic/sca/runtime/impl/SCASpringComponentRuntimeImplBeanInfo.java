package weblogic.sca.runtime.impl;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.j2ee.ComponentRuntimeMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SCASpringComponentRuntimeMBean;

public class SCASpringComponentRuntimeImplBeanInfo extends ComponentRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SCASpringComponentRuntimeMBean.class;

   public SCASpringComponentRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SCASpringComponentRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SCASpringComponentRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.sca.runtime.impl");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SCASpringComponentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("DeploymentState")) {
         var3 = "getDeploymentState";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentState", SCASpringComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("DeploymentState", var2);
         var2.setValue("description", "<p>The current deployment state of the module.</p>  <p>A module can be in one and only one of the following states. State can be changed via deployment or administrator console.</p>  <p>- UNPREPARED. State indicating at this  module is neither  prepared or active.</p>  <p>- PREPARED. State indicating at this module of this application is prepared, but not active. The classes have been loaded and the module has been validated.</p>  <p>- ACTIVATED. State indicating at this module  is currently active.</p>  <p>- NEW. State indicating this module has just been created and is being initialized.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#setDeploymentState(int)")};
         var2.setValue("see", var5);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ModuleId")) {
         var3 = "getModuleId";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleId", SCASpringComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ModuleId", var2);
         var2.setValue("description", "<p>Returns the identifier for this Component.  The identifier is unique within the application.</p>  <p>Typical modules will use the URI for their id.  Web Modules will return their context-root since the web-uri may not be unique within an EAR. ");
         var2.setValue("unharvestable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", SCASpringComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all work managers defined in this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
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
      Method var3 = SCASpringComponentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
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
