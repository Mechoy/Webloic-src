package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class WebServerLogMBeanImplBeanInfo extends LogFileMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServerLogMBean.class;

   public WebServerLogMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServerLogMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServerLogMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Aggregates the logging attributes for the WebServerMBean.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServerLogMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ELFFields")) {
         var3 = "getELFFields";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setELFFields";
         }

         var2 = new PropertyDescriptor("ELFFields", WebServerLogMBean.class, var3, var4);
         var1.put("ELFFields", var2);
         var2.setValue("description", "<p>Returns the list of fields specified for the <code>extended</code> logging format for access.log. </p> ");
         setPropertyDescriptorDefault(var2, "date time cs-method cs-uri sc-status");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FileName")) {
         var3 = "getFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileName";
         }

         var2 = new PropertyDescriptor("FileName", WebServerLogMBean.class, var3, var4);
         var1.put("FileName", var2);
         var2.setValue("description", "<p>The name of the log file.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("LogFileFormat")) {
         var3 = "getLogFileFormat";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileFormat";
         }

         var2 = new PropertyDescriptor("LogFileFormat", WebServerLogMBean.class, var3, var4);
         var1.put("LogFileFormat", var2);
         var2.setValue("description", "<p>The format of the HTTP log file. Both formats are defined by the W3C. With the extended log format, you use server directives in the log file to customize the information that the server records.</p> ");
         setPropertyDescriptorDefault(var2, "common");
         var2.setValue("legalValues", new Object[]{"common", "extended"});
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("LogTimeInGMT")) {
         var3 = "isLogTimeInGMT";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogTimeInGMT";
         }

         var2 = new PropertyDescriptor("LogTimeInGMT", WebServerLogMBean.class, var3, var4);
         var1.put("LogTimeInGMT", var2);
         var2.setValue("description", "<p>Specifies whether the time stamps for HTTP log messages are in Greenwich Mean Time (GMT) regardless of the local time zone that the host computer specifies.</p>  <p>Use this method to comply with the W3C specification for Extended Format log files. The specification states that all time stamps for Extended Format log entries be in GMT.</p>  <p>This method applies only if you have specified the <code>extended</code> message format.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("LoggingEnabled")) {
         var3 = "isLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoggingEnabled";
         }

         var2 = new PropertyDescriptor("LoggingEnabled", WebServerLogMBean.class, var3, var4);
         var1.put("LoggingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether this server logs HTTP requests. (The remaining fields on this page are relevant only if you select this check box.)</p>  <p>Gets the loggingEnabled attribute of the WebServerMBean object.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
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
