package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebServerMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServerMBean.class;

   public WebServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebServerMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean represents the configuration of virtual web server within a weblogic server. Note that a server may define multiple web servers to support virtual hosts.  <p>This MBean represents a virtual host.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Charsets")) {
         var3 = "getCharsets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCharsets";
         }

         var2 = new PropertyDescriptor("Charsets", WebServerMBean.class, var3, var4);
         var1.put("Charsets", var2);
         var2.setValue("description", "<p>Provides user defined mapping between internet and Java charset names.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClientIpHeader")) {
         var3 = "getClientIpHeader";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientIpHeader";
         }

         var2 = new PropertyDescriptor("ClientIpHeader", WebServerMBean.class, var3, var4);
         var1.put("ClientIpHeader", var2);
         var2.setValue("description", "Get the Client IP Header from WebSerevrMBean. ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("DefaultWebApp")) {
         var3 = "getDefaultWebApp";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultWebApp";
         }

         var2 = new PropertyDescriptor("DefaultWebApp", WebServerMBean.class, var3, var4);
         var1.put("DefaultWebApp", var2);
         var2.setValue("description", "<p>Provides the Servlet 2.3 Web Application that maps to the \"default\" servlet context (where ContextPath = \"/\"). This param has been deprecated 9.0.0.0 starting from 8.1 release. Set context-root=\"\" instead in weblogic.xml or application.xml. Alternatively, use getDefaultWebAppDeployment()</p>  <p>Gets the defaultWebApp attribute of the WebServerMBean object</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DefaultWebAppContextRoot")) {
         var3 = "getDefaultWebAppContextRoot";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultWebAppContextRoot";
         }

         var2 = new PropertyDescriptor("DefaultWebAppContextRoot", WebServerMBean.class, var3, var4);
         var1.put("DefaultWebAppContextRoot", var2);
         var2.setValue("description", "<p>Returns the original context-root for the default Web application for this Web server. Alternatively, you can use the context-root attributes in application.xml or weblogic.xml to set a default Web application. The context-root for a default Web application is /. If \"\" (empty string) is specified, the Web server defaults to /.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("FrontendHTTPPort")) {
         var3 = "getFrontendHTTPPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFrontendHTTPPort";
         }

         var2 = new PropertyDescriptor("FrontendHTTPPort", WebServerMBean.class, var3, var4);
         var1.put("FrontendHTTPPort", var2);
         var2.setValue("description", "<p>The name of the HTTP port to which all redirected URLs will be sent. If specified, WebLogic Server will use this value rather than the one in the HOST header.</p>  <p>Sets the frontendHTTPPort Provides a method to ensure that the webapp will always have the correct PORT information, even when the request is coming through a firewall or a proxy. If this parameter is configured, the HOST header will be ignored and the information in this parameter will be used in its place.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FrontendHTTPSPort")) {
         var3 = "getFrontendHTTPSPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFrontendHTTPSPort";
         }

         var2 = new PropertyDescriptor("FrontendHTTPSPort", WebServerMBean.class, var3, var4);
         var1.put("FrontendHTTPSPort", var2);
         var2.setValue("description", "<p>The name of the secure HTTP port to which all redirected URLs will be sent. If specified, WebLogic Server will use this value rather than the one in the HOST header.</p>  <p>Sets the frontendHTTPSPort Provides a method to ensure that the webapp will always have the correct PORT information, even when the request is coming through a firewall or a proxy. If this parameter is configured, the HOST header will be ignored and the information in this parameter will be used in its place.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FrontendHost")) {
         var3 = "getFrontendHost";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFrontendHost";
         }

         var2 = new PropertyDescriptor("FrontendHost", WebServerMBean.class, var3, var4);
         var1.put("FrontendHost", var2);
         var2.setValue("description", "<p>The name of the host to which all redirected URLs will be sent. If specified, WebLogic Server will use this value rather than the one in the HOST header.</p>  <p>Sets the HTTP frontendHost Provides a method to ensure that the webapp will always have the correct HOST information, even when the request is coming through a firewall or a proxy. If this parameter is configured, the HOST header will be ignored and the information in this parameter will be used in its place.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HttpsKeepAliveSecs")) {
         var3 = "getHttpsKeepAliveSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHttpsKeepAliveSecs";
         }

         var2 = new PropertyDescriptor("HttpsKeepAliveSecs", WebServerMBean.class, var3, var4);
         var1.put("HttpsKeepAliveSecs", var2);
         var2.setValue("description", "<p>The amount of time this server waits before closing an inactive HTTPS connection.</p>  <p>Number of seconds to maintain HTTPS keep-alive before timing out the request.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(360));
         var2.setValue("legalMin", new Integer(30));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepAliveSecs")) {
         var3 = "getKeepAliveSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepAliveSecs";
         }

         var2 = new PropertyDescriptor("KeepAliveSecs", WebServerMBean.class, var3, var4);
         var1.put("KeepAliveSecs", var2);
         var2.setValue("description", "<p>The amount of time this server waits before closing an inactive HTTP connection.</p>  <p>Number of seconds to maintain HTTP keep-alive before timing out the request.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("secureValue", new Integer(30));
         var2.setValue("legalMax", new Integer(3600));
         var2.setValue("legalMin", new Integer(5));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileBufferKBytes")) {
         var3 = "getLogFileBufferKBytes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileBufferKBytes";
         }

         var2 = new PropertyDescriptor("LogFileBufferKBytes", WebServerMBean.class, var3, var4);
         var1.put("LogFileBufferKBytes", var2);
         var2.setValue("description", "<p>The maximum size (in kilobytes) of the buffer that stores HTTP requests. When the buffer reaches this size, the server writes the data to the HTTP log file. Use the <code>LogFileFlushSecs</code> property to determine the frequency with which the server checks the size of the buffer.</p>  <p>The maximum size of the buffer that stores HTTP requests.</p>  <p>Gets the logFileBufferKBytes attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Integer(8));
         var2.setValue("legalMax", new Integer(1024));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileCount")) {
         var3 = "getLogFileCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileCount";
         }

         var2 = new PropertyDescriptor("LogFileCount", WebServerMBean.class, var3, var4);
         var1.put("LogFileCount", var2);
         var2.setValue("description", "<p>The maximum number of log files that this server retains when it rotates the log. (This field is relevant only if you check the Limit Number of Retained Log Files box.)</p>  <p>The maximum number of log files that the server creates when it rotates the log. Only valid if <code>LogFileLimitEnabled</code> is true and <code>LogRotationType</code> is either <code>Size</code> or <code>Time</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(7));
         var2.setValue("legalMax", new Integer(9999));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getFileCount() ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileFlushSecs")) {
         var3 = "getLogFileFlushSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileFlushSecs";
         }

         var2 = new PropertyDescriptor("LogFileFlushSecs", WebServerMBean.class, var3, var4);
         var1.put("LogFileFlushSecs", var2);
         var2.setValue("description", "<p>The interval at which this server checks the size of the buffer that stores HTTP requests. When the buffer exceeds the size that is specified in the Log Buffer Size field, the server writes the data to the HTTP request log file.</p>  <p>The interval (in seconds) at which the server checks the size of the buffer that stores HTTP requests. When the buffer exceeds the size that is specified in the <code>LogFileBufferKBytes</code> property, the server writes the data in the buffer to the HTTP request log file.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(360));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileFormat")) {
         var3 = "getLogFileFormat";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileFormat";
         }

         var2 = new PropertyDescriptor("LogFileFormat", WebServerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("LogFileName", WebServerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("LogRotationPeriodMins", WebServerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("LogRotationTimeBegin", WebServerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("LogRotationType", WebServerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("LogTimeInGMT", WebServerMBean.class, var3, var4);
         var1.put("LogTimeInGMT", var2);
         var2.setValue("description", "<p>Specifies whether the time stamps for HTTP log messages are in Greenwich Mean Time (GMT) regardless of the local time zone that the host computer specifies.</p>  <p>Use this method to comply with the W3C specification for Extended Format Log Files. The specification states that all time stamps for Extended Format log entries be in GMT.</p>  <p>This method applies only if you have specified the <code>extended</code> message format.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getLogTimeInGMT(). ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("MaxLogFileSizeKBytes")) {
         var3 = "getMaxLogFileSizeKBytes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxLogFileSizeKBytes";
         }

         var2 = new PropertyDescriptor("MaxLogFileSizeKBytes", WebServerMBean.class, var3, var4);
         var1.put("MaxLogFileSizeKBytes", var2);
         var2.setValue("description", "<p>The maximum size (in kilobytes) of the HTTP log file. After the log file reaches this size, the server renames it as <tt>LogFileName.n</tt>. A value of 0 indicates that the log file can grow indefinitely. (This field is relevant only if you set Rotation Type to <tt>size</tt>.)</p>  <p>The size that triggers the server to move log messages to a separate file. After the log file reaches the specified size, the next time the server checks the file size, it will rename the current log file as <code><i>FileName.n</i></code> and create a new one to store subsequent messages.</p>  <p>This property is relevant only if you choose to rotate files by <code>size</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().getFileMinSize() ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      String[] var5;
      if (!var1.containsKey("MaxPostSize")) {
         var3 = "getMaxPostSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxPostSize";
         }

         var2 = new PropertyDescriptor("MaxPostSize", WebServerMBean.class, var3, var4);
         var1.put("MaxPostSize", var2);
         var2.setValue("description", "<p>The maximum post size this server allows for reading HTTP POST data in a servlet request, excluding chunked HTTP requests (Transfer-Encoding: chunked).</p>  <p>A value less than 0 indicates an unlimited size.</p>  <p>Gets the maxPostSize attribute of the WebServerMBean object</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#getMaxPostSize()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxPostTimeSecs")) {
         var3 = "getMaxPostTimeSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxPostTimeSecs";
         }

         var2 = new PropertyDescriptor("MaxPostTimeSecs", WebServerMBean.class, var3, var4);
         var1.put("MaxPostTimeSecs", var2);
         var2.setValue("description", "<p>Max Post Time (in seconds) for reading HTTP POST data in a servlet request. MaxPostTime &lt; 0 means unlimited</p>  <p>Gets the maxPostTimeSecs attribute of the WebServerMBean object</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#getMaxPostTimeSecs()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WebServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("OverloadResponseCode")) {
         var3 = "getOverloadResponseCode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOverloadResponseCode";
         }

         var2 = new PropertyDescriptor("OverloadResponseCode", WebServerMBean.class, var3, var4);
         var1.put("OverloadResponseCode", var2);
         var2.setValue("description", "Get the response code to be used when an application is overloaded. An application can get overloaded when the number of pending requests has reached the max capacity specified in the WorkManager or when the server is low on memory. The low memory condition is determined using {@link OverloadProtectionMBean#getFreeMemoryPercentLowThreshold()}. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("CapacityMBean"), BeanInfoHelper.encodeEntities("OverloadProtectionMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(503));
         var2.setValue("legalMax", new Integer(599));
         var2.setValue("legalMin", new Integer(100));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PostTimeoutSecs")) {
         var3 = "getPostTimeoutSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostTimeoutSecs";
         }

         var2 = new PropertyDescriptor("PostTimeoutSecs", WebServerMBean.class, var3, var4);
         var1.put("PostTimeoutSecs", var2);
         var2.setValue("description", "<p>Timeout (in seconds) for reading HTTP POST data in a servlet request. If the POST data is chunked, the amount of time the server waits between the end of receiving the last chunk of data and the end of receiving the next chunk of data in an HTTP POST before it times out. (This is used to prevent denial-of-service attacks that attempt to overload the server with POST data.)</p>  <p>Gets the postTimeoutSecs attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("secureValue", new Integer(30));
         var2.setValue("legalMax", new Integer(120));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("URLResource")) {
         var3 = "getURLResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setURLResource";
         }

         var2 = new PropertyDescriptor("URLResource", WebServerMBean.class, var3, var4);
         var1.put("URLResource", var2);
         var2.setValue("description", "<p>Adds a URL connection factory resource into JNDI.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServerLog")) {
         var3 = "getWebServerLog";
         var4 = null;
         var2 = new PropertyDescriptor("WebServerLog", WebServerMBean.class, var3, var4);
         var1.put("WebServerLog", var2);
         var2.setValue("description", "Returns the Log settings for the WebServer/VirtualHost. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("WorkManagerForRemoteSessionFetching")) {
         var3 = "getWorkManagerForRemoteSessionFetching";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWorkManagerForRemoteSessionFetching";
         }

         var2 = new PropertyDescriptor("WorkManagerForRemoteSessionFetching", WebServerMBean.class, var3, var4);
         var1.put("WorkManagerForRemoteSessionFetching", var2);
         var2.setValue("description", "Set the WorkManager name that will be used to execute servlet requests that need their session retrieved from a remote server since the current server is neither the primary nor the secondary for the request. This can happen if request stickness is lost for example due to hardware LB configuration issues. Creating a dedicated WorkManager with a max threads constraint ensures that threads are available to service requests for which the current server is the primary. ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("WriteChunkBytes")) {
         var3 = "getWriteChunkBytes";
         var4 = null;
         var2 = new PropertyDescriptor("WriteChunkBytes", WebServerMBean.class, var3, var4);
         var1.put("WriteChunkBytes", var2);
         var2.setValue("description", "<p>The default size of the blocks to be written to the network layer.</p>  <p>Gets the writeChunkBytes attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Integer(512));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AcceptContextPathInGetRealPath")) {
         var3 = "isAcceptContextPathInGetRealPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcceptContextPathInGetRealPath";
         }

         var2 = new PropertyDescriptor("AcceptContextPathInGetRealPath", WebServerMBean.class, var3, var4);
         var1.put("AcceptContextPathInGetRealPath", var2);
         var2.setValue("description", "<p>Indicates whether this server allows the inclusion of the context path in the virtual path to <tt>context.getRealPath()</tt>. (If checked, you cannot use sub directories with the same name as <tt>contextPath</tt>). This is a compatibility switch that will be deprecated 9.0.0.0 in future releases.</p>  <p>Gets the acceptContextPathInGetRealPath attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("AuthCookieEnabled")) {
         var3 = "isAuthCookieEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthCookieEnabled";
         }

         var2 = new PropertyDescriptor("AuthCookieEnabled", WebServerMBean.class, var3, var4);
         var1.put("AuthCookieEnabled", var2);
         var2.setValue("description", "<p>Whether authcookie feature is enabled or not.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ChunkedTransferDisabled")) {
         var3 = "isChunkedTransferDisabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setChunkedTransferDisabled";
         }

         var2 = new PropertyDescriptor("ChunkedTransferDisabled", WebServerMBean.class, var3, var4);
         var1.put("ChunkedTransferDisabled", var2);
         var2.setValue("description", "<p>Indicates whether the use of Chunk Transfer-Encoding in HTTP/1.1 is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("DebugEnabled")) {
         var3 = "isDebugEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("DebugEnabled", WebServerMBean.class, var3, var4);
         var1.put("DebugEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the debugEnabled attribute is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 use the ServerDebugMBean ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("KeepAliveEnabled")) {
         var3 = "isKeepAliveEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepAliveEnabled";
         }

         var2 = new PropertyDescriptor("KeepAliveEnabled", WebServerMBean.class, var3, var4);
         var1.put("KeepAliveEnabled", var2);
         var2.setValue("description", "<p>Indicates whether there should be a persistent connection to this server. (This may improve the performance of your Web applications.)</p>  <p>Gets the keepAliveEnabled attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("LogFileLimitEnabled")) {
         var3 = "isLogFileLimitEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogFileLimitEnabled";
         }

         var2 = new PropertyDescriptor("LogFileLimitEnabled", WebServerMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("LoggingEnabled", WebServerMBean.class, var3, var4);
         var1.put("LoggingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether this server logs HTTP requests. (The remaining fields on this page are relevant only if you check this box.)</p>  <p>Gets the loggingEnabled attribute of the WebServerMBean object</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 Use getWebServerLog().isLoggingEnabled(). ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("SendServerHeaderEnabled")) {
         var3 = "isSendServerHeaderEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSendServerHeaderEnabled";
         }

         var2 = new PropertyDescriptor("SendServerHeaderEnabled", WebServerMBean.class, var3, var4);
         var1.put("SendServerHeaderEnabled", var2);
         var2.setValue("description", "<p>Indicates whether this server name is sent with the HTTP response. (This is useful for wireless applications where there is limited space for headers.)</p>  <p>Indicates whether this server instance includes its name and WebLogic Server version number in HTTP response headers.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SingleSignonDisabled")) {
         var3 = "isSingleSignonDisabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSingleSignonDisabled";
         }

         var2 = new PropertyDescriptor("SingleSignonDisabled", WebServerMBean.class, var3, var4);
         var1.put("SingleSignonDisabled", var2);
         var2.setValue("description", "<p>Indicates whether the singleSignonDisabled attribute is enabled</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseHeaderEncoding")) {
         var3 = "isUseHeaderEncoding";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseHeaderEncoding";
         }

         var2 = new PropertyDescriptor("UseHeaderEncoding", WebServerMBean.class, var3, var4);
         var1.put("UseHeaderEncoding", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseHighestCompatibleHTTPVersion")) {
         var3 = "isUseHighestCompatibleHTTPVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseHighestCompatibleHTTPVersion";
         }

         var2 = new PropertyDescriptor("UseHighestCompatibleHTTPVersion", WebServerMBean.class, var3, var4);
         var1.put("UseHighestCompatibleHTTPVersion", var2);
         var2.setValue("description", "<p>Enables use of the highest compatible HTTP protocol version-string in the response. E.g. HTTP spec suggests that HTTP/1.1 version-string should be used in response to a request using HTTP/1.0. This does not necessarily affect the response format.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WAPEnabled")) {
         var3 = "isWAPEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWAPEnabled";
         }

         var2 = new PropertyDescriptor("WAPEnabled", WebServerMBean.class, var3, var4);
         var1.put("WAPEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the session ID should include JVM information. (Checking this box may be necessary when using URL rewriting with WAP devices that limit the size of the URL to 128 characters, and may also affect the use of replicated sessions in a cluster.) When this box is selected, the default size of the URL will be set at 52 characters, and it will not contain any special characters.</p>  <p>Gets the WAPEnabled attribute of the WebServerMBean object</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isWAPEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
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
      Method var3 = WebServerMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = WebServerMBean.class.getMethod("restoreDefaultValue", String.class);
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
