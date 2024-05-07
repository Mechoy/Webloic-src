package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class LogMBeanImplBeanInfo extends CommonLogMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = LogMBean.class;

   public LogMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public LogMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = LogMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Configures the threshold severity level and filter settings for logging output.</p> <p>Specifies whether the server logging is based on a Log4j implementation or the default Java Logging APIs.</p> <p>Redirects the JVM stdout and stderr output to the registered log destinations. </p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.LogMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DomainLogBroadcastFilter")) {
         var3 = "getDomainLogBroadcastFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDomainLogBroadcastFilter";
         }

         var2 = new PropertyDescriptor("DomainLogBroadcastFilter", LogMBean.class, var3, var4);
         var1.put("DomainLogBroadcastFilter", var2);
         var2.setValue("description", "The filter configuration for log events being sent to the domain log. ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DomainLogBroadcastSeverity")) {
         var3 = "getDomainLogBroadcastSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDomainLogBroadcastSeverity";
         }

         var2 = new PropertyDescriptor("DomainLogBroadcastSeverity", LogMBean.class, var3, var4);
         var1.put("DomainLogBroadcastSeverity", var2);
         var2.setValue("description", "The minimum severity of log messages going to the domain log from this server's log broadcaster. Messages with a lower severity than the specified value will not be published to the domain log. ");
         setPropertyDescriptorDefault(var2, "Notice");
         var2.setValue("legalValues", new Object[]{"Debug", "Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency", "Off"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DomainLogBroadcasterBufferSize")) {
         var3 = "getDomainLogBroadcasterBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDomainLogBroadcasterBufferSize";
         }

         var2 = new PropertyDescriptor("DomainLogBroadcasterBufferSize", LogMBean.class, var3, var4);
         var1.put("DomainLogBroadcasterBufferSize", var2);
         var2.setValue("description", "<p>Broadcasts log messages to the domain log in batch mode.</p>  <p>The size of the buffer for log messages that are sent to the domain log. The buffer is maintained on the Managed Server and is broadcasted to the domain log when it is full.</p>  <p>If you notice performance issues due to a high rate of log messages being generated, set this value higher. This will cause the buffer to be broadcasted less frequently from the Managed Server to the domain log. In production environments, it is not recommended to set the buffer size lower than the production default of 10.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LogFileFilter")) {
         var3 = "getLogFileFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileFilter";
         }

         var2 = new PropertyDescriptor("LogFileFilter", LogMBean.class, var3, var4);
         var1.put("LogFileFilter", var2);
         var2.setValue("description", "<p>The filter configuration for the server log file.</p>  <p>A filter configuration defines simple filtering rules to limit the volume of log messages written to the log file.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MemoryBufferFilter")) {
         var3 = "getMemoryBufferFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemoryBufferFilter";
         }

         var2 = new PropertyDescriptor("MemoryBufferFilter", LogMBean.class, var3, var4);
         var1.put("MemoryBufferFilter", var2);
         var2.setValue("description", "<p>The filter configuration for messages that are stored in the log memory buffer. By default, all log messages are cached.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MemoryBufferSeverity")) {
         var3 = "getMemoryBufferSeverity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemoryBufferSeverity";
         }

         var2 = new PropertyDescriptor("MemoryBufferSeverity", LogMBean.class, var3, var4);
         var1.put("MemoryBufferSeverity", var2);
         var2.setValue("description", "<p>The minimum severity of log messages going to the memory buffer of recent log events. Messages with a lower severity than the specified value will not be cached in the buffer.</p> ");
         setPropertyDescriptorDefault(var2, "Trace");
         var2.setValue("legalValues", new Object[]{"Trace", "Debug", "Info", "Warning", "Error", "Notice", "Critical", "Alert", "Emergency", "Off"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MemoryBufferSize")) {
         var3 = "getMemoryBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemoryBufferSize";
         }

         var2 = new PropertyDescriptor("MemoryBufferSize", LogMBean.class, var3, var4);
         var1.put("MemoryBufferSize", var2);
         var2.setValue("description", "<p>The size of the memory buffer that holds the last n log records. This is used to support viewing the most recent log record entries (tail viewing) from the WebLogic Administration Console.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(5000));
         var2.setValue("legalMin", new Integer(10));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", LogMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StdoutFilter")) {
         var3 = "getStdoutFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutFilter";
         }

         var2 = new PropertyDescriptor("StdoutFilter", LogMBean.class, var3, var4);
         var1.put("StdoutFilter", var2);
         var2.setValue("description", "<p>The filter configuration for log events being sent to the standard out.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Log4jLoggingEnabled")) {
         var3 = "isLog4jLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLog4jLoggingEnabled";
         }

         var2 = new PropertyDescriptor("Log4jLoggingEnabled", LogMBean.class, var3, var4);
         var1.put("Log4jLoggingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server logging is based on a Log4j implementation. By default, WebLogic logging uses an implementation based on the Java Logging APIs which are part of the JDK.</p>  <p>Applications that use the WebLogic Message Catalog framework or the NonCatalogLogger will not be affected by the underlying Logging implementation.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RedirectStderrToServerLogEnabled")) {
         var3 = "isRedirectStderrToServerLogEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRedirectStderrToServerLogEnabled";
         }

         var2 = new PropertyDescriptor("RedirectStderrToServerLogEnabled", LogMBean.class, var3, var4);
         var1.put("RedirectStderrToServerLogEnabled", var2);
         var2.setValue("description", "When enabled, this redirects the stderr of the JVM in which a WebLogic Server instance runs, to the WebLogic Logging system. The stderr content is published to all the registered log destinations, like the server terminal console and log file. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RedirectStdoutToServerLogEnabled")) {
         var3 = "isRedirectStdoutToServerLogEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRedirectStdoutToServerLogEnabled";
         }

         var2 = new PropertyDescriptor("RedirectStdoutToServerLogEnabled", LogMBean.class, var3, var4);
         var1.put("RedirectStdoutToServerLogEnabled", var2);
         var2.setValue("description", "When enabled, this redirects the stdout of the JVM in which a WebLogic Server instance runs, to the WebLogic logging system. The stdout content is published to all the registered log destinations, like the server terminal console and log file. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("ServerLoggingBridgeUseParentLoggersEnabled")) {
         var3 = "isServerLoggingBridgeUseParentLoggersEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerLoggingBridgeUseParentLoggersEnabled";
         }

         var2 = new PropertyDescriptor("ServerLoggingBridgeUseParentLoggersEnabled", LogMBean.class, var3, var4);
         var1.put("ServerLoggingBridgeUseParentLoggersEnabled", var2);
         var2.setValue("description", "<p>Specifies whether application log messages are propagated to the parent node in the Logger tree or to the WebLogic Server log by means of the Logging Bridge. By default, this attribute is disabled, which results in application log messages being propagated to the WebLogic Server log.</p>  <p>Note the following behavior:</p> <ul> <li>If WebLogic Server is configured to be based upon the the Java Logging API (the default), the Logging Bridge is made available as a <code>java.util.logging.Handler</code> object.</li> <li>If  WebLogic Server is configured to be based upon a Log4j implementation, the Logging Bridge is made available as a <code>org.apache.log4j.Appender</code> object.</li> <li>If the <code>ServerLoggingBridgeUseParentLoggersEnabled</code> is disabled, applications that use either the Java Logging API or Log4j have their log messages redirected by the Logging Bridge to the WebLogic Server log. <li>If the <code>ServerLoggingBridgeUseParentLoggersEnabled</code> is enabled, applications that use the Java Logging API have their log messages propagated to the parent node in the global Java Logging Logger tree.</li> <li>If the <code>ServerLoggingBridgeUseParentLoggersEnabled</code> is enabled, applications that use Log4j have their log messages propagated to the parent node in the Log4j Logger tree.</li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
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
      Method var3 = LogMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = LogMBean.class.getMethod("restoreDefaultValue", String.class);
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

      var3 = LogMBean.class.getMethod("computeLogFilePath");
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
