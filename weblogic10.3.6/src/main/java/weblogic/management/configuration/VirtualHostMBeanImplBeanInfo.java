package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class VirtualHostMBeanImplBeanInfo extends WebServerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = VirtualHostMBean.class;

   public VirtualHostMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public VirtualHostMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = VirtualHostMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This bean represents the configuration of virtual web server within a WebLogic Server instance. Note that a server may define multiple web servers to support virtual hosts.</p>  <p>This MBean represents a virtual host.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.VirtualHostMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileCount")) {
         var3 = "getLogFileCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileCount";
         }

         var2 = new PropertyDescriptor("LogFileCount", VirtualHostMBean.class, var3, var4);
         var1.put("LogFileCount", var2);
         var2.setValue("description", "<p>The maximum number of log files that this server retains when it rotates the log. (This field is relevant only if you check the Limit Number of Retained Log Files box.)</p>  <p>The maximum number of log files that the server creates when it rotates the log. Only valid if <code>LogFileLimitEnabled</code> is true and <code>LogRotationType</code> is either <code>Size</code> or <code>Time</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(7));
         var2.setValue("legalMax", new Integer(9999));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getFileCount() ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileFormat")) {
         var3 = "getLogFileFormat";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileFormat";
         }

         var2 = new PropertyDescriptor("LogFileFormat", VirtualHostMBean.class, var3, var4);
         var1.put("LogFileFormat", var2);
         var2.setValue("description", "<p>The format of the HTTP log file. Both formats are defined by the W3C. With the <tt>extended</tt> log format, you use server directives in the log file to customize the information that the server records.</p>  <p>Specifies the format of the HTTP log file. Both formats are defined by the W3C. With the extended log format, you use server directives in the log file to customize the information that the server records.</p> ");
         setPropertyDescriptorDefault(var2, "common");
         var2.setValue("legalValues", new Object[]{"common", "extended"});
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getLogFileFormat(). ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileName")) {
         var3 = "getLogFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileName";
         }

         var2 = new PropertyDescriptor("LogFileName", VirtualHostMBean.class, var3, var4);
         var1.put("LogFileName", var2);
         var2.setValue("description", "<p>The name of the file that stores HTTP requests. If the pathname is not absolute, the path is assumed to be relative to the root directory of the machine on which this server is running.</p>  <p>The name of the file that stores the HTTP-request log. If the pathname is not absolute, the path is assumed to be relative to the server's root directory.</p>  <p>This value is relevant only if HTTP logging is enabled.</p>  <p>The current logfile is always the one whose name equals value of the this attribute. If you have enabled log file rotation, when the current file exceeds the size or time limit, it is renamed.</p>  <p>To include a time and date stamp in the file name when the log file is rotated, add <code>java.text.SimpleDateFormat</code> variables to the file name. Surround each variable with percentage (<code>%</code>) characters.</p>  <p>For example, if the file name is defined to be <code>access_%yyyy%_%MM%_%dd%_%hh%_%mm%.log</code>, the log file will be named <code>access_yyyy_mm_dd_hh_mm.log</code>.</p>  <p>When the log file is rotated, the rotated file name contains the date stamp. For example, if the log file is rotated on 2 April, 2003 at 10:05 AM, the log file that contains the old messages will be named <code>access_2003_04_02_10_05.log</code>.</p>  <p>If you do not include a time and date stamp, the rotated log files are numbered in order of creation. For example, <code>access.log00007</code>.</p> ");
         setPropertyDescriptorDefault(var2, "logs/access.log");
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getFileName() ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogRotationPeriodMins")) {
         var3 = "getLogRotationPeriodMins";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogRotationPeriodMins";
         }

         var2 = new PropertyDescriptor("LogRotationPeriodMins", VirtualHostMBean.class, var3, var4);
         var1.put("LogRotationPeriodMins", var2);
         var2.setValue("description", "<p>The number of minutes at which this server saves old HTTP requests to another log file. This field is relevant only if you set Rotation Type to <tt>date</tt>.</p>  <p>The interval (in minutes) at which the server saves old HTTP requests to another log file. This value is relevant only if you use the <code>date</code>-based rotation type.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1440));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getFileTimeSpan() (hours) ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogRotationTimeBegin")) {
         var3 = "getLogRotationTimeBegin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogRotationTimeBegin";
         }

         var2 = new PropertyDescriptor("LogRotationTimeBegin", VirtualHostMBean.class, var3, var4);
         var1.put("LogRotationTimeBegin", var2);
         var2.setValue("description", "<p>The start time for a time-based rotation sequence of the log file, in the format <tt>MM-dd-yyyy-k:mm:ss</tt>. (This field is only relevant if you set Rotation Type to <tt>date</tt>.)</p>  <p>Determines the start time for a time-based rotation sequence. At the time that this value specifies, the server renames the current log file. Thereafter, the server renames the log file at an interval that you specify in <code>LogRotationPeriodMins</code>.</p>  <p>Use the following format: <code>MM-dd-yyyy-k:mm:ss</code> where</p>  <ul> <li><code>MM</code>  <p>is the month as expressed in the Gregorian calendar</p> </li>  <li><code>dd</code>  <p>is the day of the month</p> </li>  <li><code>yyyy</code>  <p>is the year</p> </li>  <li><code>k</code>  <p>is the hour in a 24-hour format.</p> </li>  <li><code>mm</code>  <p>is the minute</p> </li>  <li><code>ss</code>  <p>is the second</p> </li> </ul>  <p>If the time that you specify has already past, then the server starts its file rotation immediately.</p>  <p>By default, rotation starts 24 hours from the time that you restart the server instance.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogRotationType")) {
         var3 = "getLogRotationType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogRotationType";
         }

         var2 = new PropertyDescriptor("LogRotationType", VirtualHostMBean.class, var3, var4);
         var1.put("LogRotationType", var2);
         var2.setValue("description", "<p>The criteria for moving old log messages to a separate file.</p>  <p>Criteria for moving old HTTP requests to a separate log file:</p>  <ul> <li><code>size</code>  <p>. When the log file reaches the size that you specify in <code>MaxLogFileSizeKBytes</code>, the server renames the file as <code><i>LogFileName.n</i></code>.</p> </li>  <li><code>date</code>  <p>. At each time interval that you specify in <code>LogRotationPeriodMin</code>, the server renames the file as <code><i>LogFileName.n</i></code>.</p> </li> </ul>  <p>After the server renames a file, subsequent messages accumulate in a new file with the name that you specified in <code>LogFileName</code>.</p> ");
         setPropertyDescriptorDefault(var2, "size");
         var2.setValue("legalValues", new Object[]{"size", "date"});
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getRotationType() ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogTimeInGMT")) {
         var3 = "getLogTimeInGMT";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogTimeInGMT";
         }

         var2 = new PropertyDescriptor("LogTimeInGMT", VirtualHostMBean.class, var3, var4);
         var1.put("LogTimeInGMT", var2);
         var2.setValue("description", "<p>Specifies whether the time stamps for HTTP log messages are in Greenwich Mean Time (GMT) regardless of the local time zone that the host computer specifies.</p>  <p>Use this method to comply with the W3C specification for Extended Format Log Files. The specification states that all time stamps for Extended Format log entries be in GMT.</p>  <p>This method applies only if you have specified the <code>extended</code> message format.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getLogTimeInGMT(). ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", VirtualHostMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("NetworkAccessPoint")) {
         var3 = "getNetworkAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNetworkAccessPoint";
         }

         var2 = new PropertyDescriptor("NetworkAccessPoint", VirtualHostMBean.class, var3, var4);
         var1.put("NetworkAccessPoint", var2);
         var2.setValue("description", "<p>The dedicated server channel name (NetworkAccessPoint) for which this virtual host will serve http request. If the NetworkAccessPoint for a given http request doesn't match any virtual host's NetworkAccessPoint, incoming HOST header will be matched with the VirtualHostNames in order to resolve the right virtual host. </p> ");
      }

      if (!var1.containsKey("VirtualHostNames")) {
         var3 = "getVirtualHostNames";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVirtualHostNames";
         }

         var2 = new PropertyDescriptor("VirtualHostNames", VirtualHostMBean.class, var3, var4);
         var1.put("VirtualHostNames", var2);
         var2.setValue("description", "<p>The list of host names, separated by line breaks, for which this virtual host will serve requests.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileLimitEnabled")) {
         var3 = "isLogFileLimitEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileLimitEnabled";
         }

         var2 = new PropertyDescriptor("LogFileLimitEnabled", VirtualHostMBean.class, var3, var4);
         var1.put("LogFileLimitEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the number of files that this WebLogic Server retains to store old messages should be limited. After the server reaches this limit, it overwrites the oldest file.</p>  <p>Indicates whether a server will limit the number of log files that it creates when it rotates the log. The limit is based on <code>getLogFileCount</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getNumberOfFilesLimited() ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LoggingEnabled")) {
         var3 = "isLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoggingEnabled";
         }

         var2 = new PropertyDescriptor("LoggingEnabled", VirtualHostMBean.class, var3, var4);
         var1.put("LoggingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether this server logs HTTP requests. (The remaining fields on this page are relevant only if you check this box.)</p>  <p>Gets the loggingEnabled attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().isLoggingEnabled(). ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
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
      Method var3 = VirtualHostMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = VirtualHostMBean.class.getMethod("restoreDefaultValue", String.class);
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
