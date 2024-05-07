package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class CommonLogMBeanImplBeanInfo extends LogFileMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CommonLogMBean.class;

   public CommonLogMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CommonLogMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CommonLogMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Configures the basic configuration for the logging system.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.CommonLogMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LogFileSeverity")) {
         var3 = "getLogFileSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileSeverity";
         }

         var2 = new PropertyDescriptor("LogFileSeverity", CommonLogMBean.class, var3, var4);
         var1.put("LogFileSeverity", var2);
         var2.setValue("description", "<p>The minimum severity of log messages going to the server log file. By default all messages go to the log file. </p> ");
         setPropertyDescriptorDefault(var2, "Trace");
         var2.setValue("legalValues", new Object[]{"Trace", "Debug", "Info", "Notice", "Warning"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.2.0.0", (String)null, this.targetVersion) && !var1.containsKey("LoggerSeverity")) {
         var3 = "getLoggerSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoggerSeverity";
         }

         var2 = new PropertyDescriptor("LoggerSeverity", CommonLogMBean.class, var3, var4);
         var1.put("LoggerSeverity", var2);
         var2.setValue("description", "<p>The minimum severity of log messages going to all log destinations. By default all messages are published. </p> ");
         setPropertyDescriptorDefault(var2, "Info");
         var2.setValue("legalValues", new Object[]{"Trace", "Debug", "Info", "Notice", "Warning"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.2.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("LoggerSeverityProperties")) {
         var3 = "getLoggerSeverityProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoggerSeverityProperties";
         }

         var2 = new PropertyDescriptor("LoggerSeverityProperties", CommonLogMBean.class, var3, var4);
         var1.put("LoggerSeverityProperties", var2);
         var2.setValue("description", "The configuration of the different logger severities keyed by name. The values are one of the predefined Severity strings namely Emergency, Alert, Critical, Error, Warning, Notice, Info, Debug, Trace. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.0.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", CommonLogMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StacktraceDepth")) {
         var3 = "getStacktraceDepth";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStacktraceDepth";
         }

         var2 = new PropertyDescriptor("StacktraceDepth", CommonLogMBean.class, var3, var4);
         var1.put("StacktraceDepth", var2);
         var2.setValue("description", "<p>Determines the no of stacktrace frames to display on standard out. All frames are displayed in the log file. * -1 means all frames are displayed.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StdoutFormat")) {
         var3 = "getStdoutFormat";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutFormat";
         }

         var2 = new PropertyDescriptor("StdoutFormat", CommonLogMBean.class, var3, var4);
         var1.put("StdoutFormat", var2);
         var2.setValue("description", "<p>The output format to use when logging to the console.</p> ");
         setPropertyDescriptorDefault(var2, "standard");
         var2.setValue("legalValues", new Object[]{"standard", "noid"});
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StdoutSeverity")) {
         var3 = "getStdoutSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutSeverity";
         }

         var2 = new PropertyDescriptor("StdoutSeverity", CommonLogMBean.class, var3, var4);
         var1.put("StdoutSeverity", var2);
         var2.setValue("description", "<p>The minimum severity of log messages going to the standard out. Messages with a lower severity than the specified value will not be published to standard out.</p> ");
         setPropertyDescriptorDefault(var2, "Notice");
         var2.setValue("legalValues", new Object[]{"Trace", "Debug", "Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency", "Off"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StdoutLogStack")) {
         var3 = "isStdoutLogStack";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutLogStack";
         }

         var2 = new PropertyDescriptor("StdoutLogStack", CommonLogMBean.class, var3, var4);
         var1.put("StdoutLogStack", var2);
         var2.setValue("description", "<p>Specifies whether to dump stack traces to the console when included in logged message.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
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
      Method var3 = CommonLogMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = CommonLogMBean.class.getMethod("restoreDefaultValue", String.class);
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

      var3 = CommonLogMBean.class.getMethod("computeLogFilePath");
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", "This method computes the log file path based on the defaults and server directory if the FileName attribute is defaulted. ");
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
