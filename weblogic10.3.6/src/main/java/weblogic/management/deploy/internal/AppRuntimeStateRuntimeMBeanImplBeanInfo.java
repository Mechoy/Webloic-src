package weblogic.management.deploy.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;

public class AppRuntimeStateRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = AppRuntimeStateRuntimeMBean.class;

   public AppRuntimeStateRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public AppRuntimeStateRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = AppRuntimeStateRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.deploy.internal");
      String var3 = (new String("Provides access to runtime state for deployed applications. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer"), BeanInfoHelper.encodeEntities("Operator"), BeanInfoHelper.encodeEntities("Monitor")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.AppRuntimeStateRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("ApplicationIds")) {
         String var3 = "getApplicationIds";
         Object var4 = null;
         var2 = new PropertyDescriptor("ApplicationIds", AppRuntimeStateRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationIds", var2);
         var2.setValue("description", " ");
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
      Method var3 = AppRuntimeStateRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("isAdminMode", String.class, String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", "is the application id ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Indicates if application should  only be available through the admin port. This is the desired state of the application ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("isActiveVersion", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", "is the application id ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Indicates if the application is the active version; the one that new sessions will use. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getRetireTimeMillis", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "The time when the app will be retired ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getRetireTimeoutSeconds", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "The amount of time the app is given to retire ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getIntendedState", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "The state the application should be in. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getIntendedState", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "The state the application should be in on a specific target. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getCurrentState", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("target", "logical target where the app is deployed ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Aggregate state for the application. This is defined as the most advanced state of the application's modules on the named target. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getModuleIds", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Names of modules contained in the application. This does not include submodules. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getSubmoduleIds", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("moduleid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Submodules associated with this module ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("weblogic.deploy.api.shared.WebLogicModuleType")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getModuleType", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("moduleid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Indicates type of module: EAR, WAR, etc ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("weblogic.deploy.api.shared.WebLogicModuleType")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getCurrentState", String.class, String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("moduleid", (String)null), createParameterDescriptor("target", "logical target where module is deployed ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Aggregate state for the module. This is defined as the most advanced state of the module on all servers associated with the named target. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getModuleTargets", String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("moduleid", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Logical targets where the module is deployed ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getCurrentState", String.class, String.class, String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("moduleid", (String)null), createParameterDescriptor("subModuleId", (String)null), createParameterDescriptor("target", "logical target where module is deployed ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Aggregate state for a submodule. This is defined as the most advanced state of the submodule on all servers associated with the named target. ");
         var2.setValue("role", "operation");
      }

      var3 = AppRuntimeStateRuntimeMBean.class.getMethod("getModuleTargets", String.class, String.class, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("appid", (String)null), createParameterDescriptor("moduleid", (String)null), createParameterDescriptor("subModuleId", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "Logical targets where the submodule is deployed ");
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
