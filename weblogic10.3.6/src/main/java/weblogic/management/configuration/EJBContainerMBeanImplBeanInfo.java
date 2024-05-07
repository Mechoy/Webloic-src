package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class EJBContainerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = EJBContainerMBean.class;

   public EJBContainerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EJBContainerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EJBContainerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean is used to specify EJB container-wide settings.  These can be overridden by a specific EJBComponentMBean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.EJBContainerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ExtraEjbcOptions")) {
         var3 = "getExtraEjbcOptions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExtraEjbcOptions";
         }

         var2 = new PropertyDescriptor("ExtraEjbcOptions", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("ExtraRmicOptions", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("ForceGeneration", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("JavaCompiler", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("JavaCompilerPostClassPath", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("JavaCompilerPreClassPath", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("KeepGenerated", EJBContainerMBean.class, var3, var4);
         var1.put("KeepGenerated", var2);
         var2.setValue("description", "<p>indicates whether KeepGenerated is enabled and the ejbc source files will be kept.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("TmpPath")) {
         var3 = "getTmpPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTmpPath";
         }

         var2 = new PropertyDescriptor("TmpPath", EJBContainerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("VerboseEJBDeploymentEnabled", EJBContainerMBean.class, var3, var4);
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
