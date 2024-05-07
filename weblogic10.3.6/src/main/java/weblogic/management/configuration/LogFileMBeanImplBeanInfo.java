package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class LogFileMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = LogFileMBean.class;

   public LogFileMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public LogFileMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = LogFileMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Configures the location, file-rotation criteria, and number of files that a WebLogic Server uses to store log messages. The methods in this class configure both server and domain log files.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.LogFileMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BufferSizeKB")) {
         var3 = "getBufferSizeKB";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBufferSizeKB";
         }

         var2 = new PropertyDescriptor("BufferSizeKB", LogFileMBean.class, var3, var4);
         var1.put("BufferSizeKB", var2);
         var2.setValue("description", "Gets the underlying log buffer size in kilobytes ");
         setPropertyDescriptorDefault(var2, new Integer(8));
      }

      if (!var1.containsKey("DateFormatPattern")) {
         var3 = "getDateFormatPattern";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDateFormatPattern";
         }

         var2 = new PropertyDescriptor("DateFormatPattern", LogFileMBean.class, var3, var4);
         var1.put("DateFormatPattern", var2);
         var2.setValue("description", "<p>The date format pattern used for rendering dates in the  log. The DateFormatPattern string conforms to the specification of the <code>java.text.SimpleDateFormat</code> class.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("FileCount")) {
         var3 = "getFileCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileCount";
         }

         var2 = new PropertyDescriptor("FileCount", LogFileMBean.class, var3, var4);
         var1.put("FileCount", var2);
         var2.setValue("description", "<p>The maximum number of log files that the server creates when it rotates the log. This number does not include the file that the server uses to store current messages. (Requires that you enable Number of Files Limited.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isNumberOfFilesLimited")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(7));
         var2.setValue("legalMax", new Integer(99999));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FileMinSize")) {
         var3 = "getFileMinSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileMinSize";
         }

         var2 = new PropertyDescriptor("FileMinSize", LogFileMBean.class, var3, var4);
         var1.put("FileMinSize", var2);
         var2.setValue("description", "The size (1 - 65535 kilobytes) that triggers the server to move log messages to a separate file. The default is 500 kilobytes. After the log file reaches the specified minimum size, the next time the server checks the file size, it will rename the current log file as <code><i>SERVER_NAME</i>.log<i>nnnnn</i></code> and create a new one to store subsequent messages. (Requires that you specify a file rotation type of <code>Size</code>.) ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRotationType")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(500));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FileName")) {
         var3 = "getFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileName";
         }

         var2 = new PropertyDescriptor("FileName", LogFileMBean.class, var3, var4);
         var1.put("FileName", var2);
         var2.setValue("description", "<p>The name of the file that stores current log messages. Usually it is a computed value based on the name of the parent of this MBean. For example, for a server log, it is <code><i>SERVER_NAME</i>.log</code>.</p> <p> However, if the name of the parent cannot be obtained, the file name is <code>weblogic.log</code>. If you specify a relative pathname, it is interpreted as relative to the server's root directory.</p>  <p>To include a time and date stamp in the file name when the log file is rotated, add <code>java.text.SimpleDateFormat</code> variables to the file name. Surround each variable with percentage (<code>%</code>) characters.</p>  <p>For example, if the file name is defined to be <code>myserver_%yyyy%_%MM%_%dd%_%hh%_%mm%.log</code>, the log file will be named <code>myserver_yyyy_mm_dd_hh_mm.log</code>.</p>  <p>When the log file is rotated, the rotated file name contains the date stamp. For example, if the log file is rotated for the first time on 2 April, 2003 at 10:05 AM, the log file that contains the old messages will be named <code>myserver_2003_04_02_10_05.log00001</code>.</p>  <p>If you do not include a time and date stamp, the rotated log files are numbered in order of creation. For example, <code>myserver.log00007</code>.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("FileTimeSpan")) {
         var3 = "getFileTimeSpan";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileTimeSpan";
         }

         var2 = new PropertyDescriptor("FileTimeSpan", LogFileMBean.class, var3, var4);
         var1.put("FileTimeSpan", var2);
         var2.setValue("description", "<p>The interval (in hours) at which the server saves old log messages to another file. (Requires that you specify a file rotation type of <code>TIME</code>.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRotationType")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(24));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LogFileRotationDir")) {
         var3 = "getLogFileRotationDir";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileRotationDir";
         }

         var2 = new PropertyDescriptor("LogFileRotationDir", LogFileMBean.class, var3, var4);
         var1.put("LogFileRotationDir", var2);
         var2.setValue("description", "<p>The directory where the rotated log files will be stored. By default the rotated files are stored in the same directory where the log file is stored.</p> ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", LogFileMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("RotateLogOnStartup")) {
         var3 = "getRotateLogOnStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRotateLogOnStartup";
         }

         var2 = new PropertyDescriptor("RotateLogOnStartup", LogFileMBean.class, var3, var4);
         var1.put("RotateLogOnStartup", var2);
         var2.setValue("description", "<p>Specifies whether a server rotates its log file during its startup cycle. The default value in production mode is false.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("RotationTime")) {
         var3 = "getRotationTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRotationTime";
         }

         var2 = new PropertyDescriptor("RotationTime", LogFileMBean.class, var3, var4);
         var1.put("RotationTime", var2);
         var2.setValue("description", "<p>Determines the start time (hour and minute) for a time-based rotation sequence.  <p>At the time that this value specifies, the server renames the current log file. Thereafter, the server renames the log file at an interval that you specify in File Time Span.</p>  <p>Note that WebLogic Server sets a threshold size limit of 500 MB before it forces a hard rotation to prevent excessive log file growth.</p>  <p> Use the following format: <code>H:mm</code>, where <ul><li><code>H</code> is Hour in day (0-23). <li><code>mm</code> is the minute in hour </ul> <p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRotationType"), BeanInfoHelper.encodeEntities("#getFileTimeSpan")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "00:00");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RotationType")) {
         var3 = "getRotationType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRotationType";
         }

         var2 = new PropertyDescriptor("RotationType", LogFileMBean.class, var3, var4);
         var1.put("RotationType", var2);
         var2.setValue("description", "Criteria for moving old log messages to a separate file. <ul> <li><code>NONE</code> Messages accumulate in a single file. You must erase the contents of the file when the size is too large. Note that WebLogic Server sets a threshold size limit of 500 MB before it forces a hard rotation to prevent excessive log file growth.  <li><code>SIZE</code> When the log file reaches the size that you specify in <code>FileMinSize</code>, the server renames the file as <code><i>SERVER_NAME</i>.log<i>nnnnn</i></code>.  <li><code>TIME</code> At each time interval that you specify in <code>TimeSpan</code>, the server renames the file as <code><i>SERVER_NAME</i>.log<i>nnnnn</i></code>. </ul>  <p>After the server renames a file, subsequent messages accumulate in a new file with the name that you specified as the log file name.</p> ");
         setPropertyDescriptorDefault(var2, "bySize");
         var2.setValue("secureValue", "byTime");
         var2.setValue("legalValues", new Object[]{"bySize", "byTime", "none"});
      }

      if (!var1.containsKey("NumberOfFilesLimited")) {
         var3 = "isNumberOfFilesLimited";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNumberOfFilesLimited";
         }

         var2 = new PropertyDescriptor("NumberOfFilesLimited", LogFileMBean.class, var3, var4);
         var1.put("NumberOfFilesLimited", var2);
         var2.setValue("description", "<p>Indicates whether to limit the number of log files that this server instance creates to store old messages. (Requires that you specify a file rotation type of <code>SIZE</code> or <code>TIME</code>.)</p>  <p>After the server reaches this limit, it deletes the oldest log file and creates a new log file with the latest suffix.</p>  <p>If you do not enable this option, the server creates new files indefinitely and you must clean up these files as you require.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRotationType")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
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
      Method var3 = LogFileMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = LogFileMBean.class.getMethod("restoreDefaultValue", String.class);
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

      var3 = LogFileMBean.class.getMethod("computeLogFilePath");
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
