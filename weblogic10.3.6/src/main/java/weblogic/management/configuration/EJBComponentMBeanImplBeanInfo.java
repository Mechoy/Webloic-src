package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class EJBComponentMBeanImplBeanInfo extends ComponentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EJBComponentMBean.class;

   public EJBComponentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EJBComponentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EJBComponentMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 in favor of {@link AppDeploymentMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("The top level interface for all configuration information that WebLogic Server maintains for an EJB module.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.EJBComponentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ActivatedTargets")) {
         var3 = "getActivatedTargets";
         var4 = null;
         var2 = new PropertyDescriptor("ActivatedTargets", EJBComponentMBean.class, var3, var4);
         var1.put("ActivatedTargets", var2);
         var2.setValue("description", "<p>List of servers and clusters where this module is currently active. This attribute is valid only for modules deployed via the two phase protocol. Modules deployed with the WLS 6.x deployment protocol do not maintain this attribute. To determine active targets for a module regardless of deployment protocol, use {@link weblogic.management.runtime.DeployerRuntimeMBean#lookupActiveTargetsForComponent}.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ApplicationMBean#isTwoPhase")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("Application")) {
         var3 = "getApplication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setApplication";
         }

         var2 = new PropertyDescriptor("Application", EJBComponentMBean.class, var3, var4);
         var1.put("Application", var2);
         var2.setValue("description", "<p>The application this component is a part of. This is guaranteed to never be null.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("EJBComponentRuntime")) {
         var3 = "getEJBComponentRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EJBComponentRuntime", EJBComponentMBean.class, var3, var4);
         var1.put("EJBComponentRuntime", var2);
         var2.setValue("description", "<p>Returns the EJBComponentRuntimeMBean representing this EJB module.</p> ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("ExtraEjbcOptions")) {
         var3 = "getExtraEjbcOptions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExtraEjbcOptions";
         }

         var2 = new PropertyDescriptor("ExtraEjbcOptions", EJBComponentMBean.class, var3, var4);
         var1.put("ExtraEjbcOptions", var2);
         var2.setValue("description", "<p>Returns the extra options passed to ejbc during the dynamic ejbc of a jar file. For example: -J-mx128m By default this value is null. If no ExtraEJBCOptions are specified on the EJBComponent, the default will be pulled from the Server.ExtraEJBCOptions.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("ExtraRmicOptions")) {
         var3 = "getExtraRmicOptions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExtraRmicOptions";
         }

         var2 = new PropertyDescriptor("ExtraRmicOptions", EJBComponentMBean.class, var3, var4);
         var1.put("ExtraRmicOptions", var2);
         var2.setValue("description", "<p>The extra options passed to rmic during server-side generation are noted here. The default for this attribute must be null. If no ExtraRmicOptions are specified on the EJBComponent, the default will be pulled from Server.ExtraRmicOptions.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("ForceGeneration")) {
         var3 = "getForceGeneration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setForceGeneration";
         }

         var2 = new PropertyDescriptor("ForceGeneration", EJBComponentMBean.class, var3, var4);
         var1.put("ForceGeneration", var2);
         var2.setValue("description", "<p>Indicates whether the ForceGeneration is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("JavaCompiler")) {
         var3 = "getJavaCompiler";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaCompiler";
         }

         var2 = new PropertyDescriptor("JavaCompiler", EJBComponentMBean.class, var3, var4);
         var1.put("JavaCompiler", var2);
         var2.setValue("description", "<p>The path to the Java compiler to use to compile EJBs (e.g. \"sj\" or \"javac\"). Note: the default must be null. If no JavaCompiler is specified on this specific EJBComponent, the default will be pulled in the following order from - EJBContainerMBean - Server.JavaCompiler.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaCompilerPostClassPath")) {
         var3 = "getJavaCompilerPostClassPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaCompilerPostClassPath";
         }

         var2 = new PropertyDescriptor("JavaCompilerPostClassPath", EJBComponentMBean.class, var3, var4);
         var1.put("JavaCompilerPostClassPath", var2);
         var2.setValue("description", "<p>Provides a list of the options to append to the Java compiler classpath when you compile Java code.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("JavaCompilerPreClassPath")) {
         var3 = "getJavaCompilerPreClassPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJavaCompilerPreClassPath";
         }

         var2 = new PropertyDescriptor("JavaCompilerPreClassPath", EJBComponentMBean.class, var3, var4);
         var1.put("JavaCompilerPreClassPath", var2);
         var2.setValue("description", "<p>Provides a list of the options to prepend to the Java compiler classpath when you compile Java code.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepGenerated")) {
         var3 = "getKeepGenerated";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepGenerated";
         }

         var2 = new PropertyDescriptor("KeepGenerated", EJBComponentMBean.class, var3, var4);
         var1.put("KeepGenerated", var2);
         var2.setValue("description", "<p>indicates whether KeepGenerated is enabled and the ejbc source files will be kept.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", EJBComponentMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", EJBComponentMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TmpPath")) {
         var3 = "getTmpPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTmpPath";
         }

         var2 = new PropertyDescriptor("TmpPath", EJBComponentMBean.class, var3, var4);
         var1.put("TmpPath", var2);
         var2.setValue("description", "<p>Return the temporary directory where generated files are stored by ejbc. Deprecated: All EJB compiler output is now stored in the EJBCompilerCache subdirectory of the server staging directory. This directory should not be described as \"temporary\" since removing it would cause the EJB compiler to be rerun as necessary the next time the server is restarted.</p> ");
         setPropertyDescriptorDefault(var2, "tmp_ejb");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("VerboseEJBDeploymentEnabled")) {
         var3 = "getVerboseEJBDeploymentEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVerboseEJBDeploymentEnabled";
         }

         var2 = new PropertyDescriptor("VerboseEJBDeploymentEnabled", EJBComponentMBean.class, var3, var4);
         var1.put("VerboseEJBDeploymentEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the VerboseEJBDeployment is enabled.</p> ");
         setPropertyDescriptorDefault(var2, "false");
         var2.setValue("deprecated", "Deprecated as of 10.3.3.0 in favor of {@link ServerDebugMBean#getDebugEjbDeployment()} ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = EJBComponentMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = EJBComponentMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = EJBComponentMBean.class.getMethod("activated", TargetMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Indicates whether component has been activated on a server</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "7.0.0.0");
         }
      }

      var3 = EJBComponentMBean.class.getMethod("refreshDDsIfNeeded", String[].class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = EJBComponentMBean.class.getMethod("freezeCurrentValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = EJBComponentMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
