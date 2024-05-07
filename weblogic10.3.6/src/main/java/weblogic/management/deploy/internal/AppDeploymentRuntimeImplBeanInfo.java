package weblogic.management.deploy.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.AppDeploymentRuntimeMBean;

public class AppDeploymentRuntimeImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = AppDeploymentRuntimeMBean.class;

   public AppDeploymentRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public AppDeploymentRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = AppDeploymentRuntimeImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("notificationTranslator", "weblogic.management.deploy.internal.AppDeploymentRuntimeNotificationTranslator");
      var2.setValue("package", "weblogic.management.deploy.internal");
      String var3 = (new String("<p>This MBean provides deployment operations for an application. Currently only start and stop are supported. In the future, this MBean may be enhanced with operations to support deployment applications to the domain as well as extended WLS deployment features such as production redeployment and partial deployment of modules in an enterprise application.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer"), BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.AppDeploymentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ApplicationName")) {
         var3 = "getApplicationName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationName", AppDeploymentRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationName", var2);
         var2.setValue("description", "<p>The application's name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ApplicationVersion")) {
         var3 = "getApplicationVersion";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationVersion", AppDeploymentRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationVersion", var2);
         var2.setValue("description", "<p>The application's version identifier.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Modules")) {
         var3 = "getModules";
         var4 = null;
         var2 = new PropertyDescriptor("Modules", AppDeploymentRuntimeMBean.class, var3, (String)var4);
         var1.put("Modules", var2);
         var2.setValue("description", "<p>The list of modules for the application. These modules can be used in module level targeting.</p> ");
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
      Method var3 = AppDeploymentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = AppDeploymentRuntimeMBean.class.getMethod("start");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Start the application using the default options and configured targets. This is a synchronous operation that returns when the start operation has completed. The default options are clusterDeploymentTimeout: 3600000 milliseconds, gracefulIgnoreSessions: false, gracefulProductionToAdmin: false, retireGracefully: true, retireTimeout: no timeout), testMode: false, timeout: no timeout</p> ");
         var2.setValue("role", "operation");
      }

      var3 = AppDeploymentRuntimeMBean.class.getMethod("start", String[].class, Properties.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("targets", "The targets on which to start the application. This would be server names, cluster names, or module names in a similar format to weblogic.Deployer (i.e. module1@server1). If null, the application will be started on all configured targets. "), createParameterDescriptor("deploymentOptions", "Allows for overriding the deployment options. If null, default options will be used. The values should all be of type The keys,units and default values for options are clusterDeploymentTimeout milliseconds 3600000, gracefulIgnoreSessions boolean false, gracefulProductionToAdmin boolean false, retireGracefully boolean true, retireTimeout seconds -1 (no timeout), testMode boolean false, timeout milliseconds 0 (no timeout) ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Start the application in the background for the targets specified with the options specified.  This is an asynchronous operation that returns immediately.  The returned {@link DeploymentProgressObjectMBean} can be used to determine when the operation is completed.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = AppDeploymentRuntimeMBean.class.getMethod("stop");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Stop the application using the default options and configured targets. This is a synchronous operation that returns when the stop operation has completed. The default options are clusterDeploymentTimeout: 3600000 milliseconds, gracefulIgnoreSessions: false, gracefulProductionToAdmin: false, retireGracefully: true, retireTimeout: no timeout), testMode: false, timeout: no timeout</p> ");
         var2.setValue("role", "operation");
      }

      var3 = AppDeploymentRuntimeMBean.class.getMethod("stop", String[].class, Properties.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("targets", "The targets on which to stop the application. This would be server names, cluster names, or module names in a similar format to weblogic.Deployer (i.e. module1@server1). If null, the application will be stoped on all configured targets. "), createParameterDescriptor("deploymentOptions", "Allows for overriding the deployment options. If null, default options will be used. The values should all be of type The keys,units and default values for options are clusterDeploymentTimeout milliseconds 3600000, gracefulIgnoreSessions boolean false, gracefulProductionToAdmin boolean false, retireGracefully boolean true, retireTimeout seconds -1 (no timeout), testMode boolean false, timeout milliseconds 0 (no timeout) ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Stop the application in the background for the targets specified with the options specified.  This is an asynchronous operation that returns immediately.  The returned {@link DeploymentProgressObjectMBean} can be used to determine when the operation is completed.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = AppDeploymentRuntimeMBean.class.getMethod("getState", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("target", "the target for the application state ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>The state of the application for a target. Notifications will be generated for this attribute on state changes. Valid states are those supported by the {@link AppRuntimeStateRuntimeMBean.} The notification types are appdeployment.created, appdeployment.deleted, appdeployment.state.new, appdeployment.state.prepared, appdeployment.state.admin, appdeployment.state.active, appdeployment.state.retired, appdeployment.state.failed, appdeployment.state.update.pending, and appdeployment.state.unknown.  The userdata is a Map where the keys are target names and the values are the application state for that target.</p> ");
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
