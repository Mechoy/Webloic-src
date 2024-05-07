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
import weblogic.management.runtime.DeploymentManagerMBean;

public class DeploymentManagerImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DeploymentManagerMBean.class;

   public DeploymentManagerImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DeploymentManagerImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DeploymentManagerImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("notificationTranslator", "weblogic.management.deploy.internal.DeploymentManagerNotificationTranslator");
      var2.setValue("package", "weblogic.management.deploy.internal");
      String var3 = (new String("<p>This MBean provides deployment operations.  A DeploymentManager object is a stateless interface into the Weblogic Server deployment framework. It currently provides access to the App Deployment Runtime MBeans that allow the user to start and stop deployments. In the future, this MBean may be enhanced with operations to support deployment applications to the domain as well as extended WLS deployment features such as production redeployment and partial deployment of modules in an enterprise application.  This MBean emits notifications when an application is created or removed and when the application state changes.  The notification types are appdeployment.created, appdeployment.deleted, appdeployment.state.new, appdeployment.state.prepared, appdeployment.state.admin, appdeployment.state.active, appdeployment.state.retired, appdeployment.state.failed, appdeployment.state.update.pending, and appdeployment.state.unknown.  The userdata is the object name of the application. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.DeploymentManagerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AppDeploymentRuntimes")) {
         var3 = "getAppDeploymentRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("AppDeploymentRuntimes", DeploymentManagerMBean.class, var3, var4);
         var1.put("AppDeploymentRuntimes", var2);
         var2.setValue("description", "<p>Provides access to the applications that are deployed in the domain.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("DeploymentProgressObjects")) {
         var3 = "getDeploymentProgressObjects";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentProgressObjects", DeploymentManagerMBean.class, var3, var4);
         var1.put("DeploymentProgressObjects", var2);
         var2.setValue("description", "<p>Provides access to the deployment operations that have been performed on this domain.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeDeploymentProgressObject");
      }

      if (!var1.containsKey("MaximumDeploymentProgressObjectsCount")) {
         var3 = "getMaximumDeploymentProgressObjectsCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaximumDeploymentProgressObjectsCount";
         }

         var2 = new PropertyDescriptor("MaximumDeploymentProgressObjectsCount", DeploymentManagerMBean.class, var3, var4);
         var1.put("MaximumDeploymentProgressObjectsCount", var2);
         var2.setValue("description", "<p>The maximum number of progress objects allowed.</p> ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DeploymentManagerMBean.class.getMethod("removeDeploymentProgressObject", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("appName", "The name of the application that the progress object is for ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Remove a progress object.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "DeploymentProgressObjects");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DeploymentManagerMBean.class.getMethod("lookupAppDeploymentRuntime", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("appName", "The name of the application ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Finds the application deployment runtime MBean for an application.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "AppDeploymentRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DeploymentManagerMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = DeploymentManagerMBean.class.getMethod("purgeCompletedDeploymentProgressObjects");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Removes progress objects for completed operations.</p> ");
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
