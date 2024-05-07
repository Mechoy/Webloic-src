package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebAppContainerMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebAppContainerMBean.class;

   public WebAppContainerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebAppContainerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebAppContainerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean is used to specify domain-wide defaults for the WebApp container. In general, these properties can be overridden at the cluster level (in ClusterMBean, if the same property is present there), the server level (in ServerMBean, if the same property is present there) or for a specific Web application (in weblogic.xml).</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebAppContainerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      if (!var1.containsKey("MaxPostSize")) {
         var3 = "getMaxPostSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxPostSize";
         }

         var2 = new PropertyDescriptor("MaxPostSize", WebAppContainerMBean.class, var3, var4);
         var1.put("MaxPostSize", var2);
         var2.setValue("description", "<p>The maximum post size this server allows for reading HTTP POST data in a servlet request. A value less than 0 indicates an unlimited size.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setMaxPostSize(int)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#getMaxPostSize()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxPostTimeSecs")) {
         var3 = "getMaxPostTimeSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxPostTimeSecs";
         }

         var2 = new PropertyDescriptor("MaxPostTimeSecs", WebAppContainerMBean.class, var3, var4);
         var1.put("MaxPostTimeSecs", var2);
         var2.setValue("description", "<p>Max Post Time (in seconds) for reading HTTP POST data in a servlet request. MaxPostTime &lt; 0 means unlimited</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setMaxPostTimeSecs(int)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#getMaxPostTimeSecs()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MimeMappingFile")) {
         var3 = "getMimeMappingFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMimeMappingFile";
         }

         var2 = new PropertyDescriptor("MimeMappingFile", WebAppContainerMBean.class, var3, var4);
         var1.put("MimeMappingFile", var2);
         var2.setValue("description", "<p>Returns the name of the file containing mime-mappings for the domain.</p> Format of the file should be: extension=mime-type <br> <br> Example: <br> htm=text/html <br> gif=image/gif <br> jpg=image/jpeg <br>  <p>If this file does not exist, WebLogic Server uses an implicit mime-mapping set of mappings defined in weblogic.utils.http.HttpConstants (DEFAULT_MIME_MAPPINGS). To remove a mapping defined in implicit map just set it to blank. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setMimeMappingFile(String)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "./config/mimemappings.properties");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("P3PHeaderValue")) {
         var3 = "getP3PHeaderValue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setP3PHeaderValue";
         }

         var2 = new PropertyDescriptor("P3PHeaderValue", WebAppContainerMBean.class, var3, var4);
         var1.put("P3PHeaderValue", var2);
         var2.setValue("description", "<p> Returns the P3P Header value that will be sent with all responses for http requests (if non-null). The value of this header points to the location of the policy reference file for the Web site.</p>  <p>Alternatively, a servlet filter can be used to set the P3P header.<p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setP3PHeaderValue(String)")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("PostTimeoutSecs")) {
         var3 = "getPostTimeoutSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostTimeoutSecs";
         }

         var2 = new PropertyDescriptor("PostTimeoutSecs", WebAppContainerMBean.class, var3, var4);
         var1.put("PostTimeoutSecs", var2);
         var2.setValue("description", "<p>The amount of time this server waits between receiving chunks of data in an HTTP POST data before it times out. (This is used to prevent denial-of-service attacks that attempt to overload the server with POST data.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setPostTimeoutSecs(int)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#getPostTimeoutSecs()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("secureValue", new Integer(30));
         var2.setValue("legalMax", new Integer(120));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServletReloadCheckSecs")) {
         var3 = "getServletReloadCheckSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServletReloadCheckSecs";
         }

         var2 = new PropertyDescriptor("ServletReloadCheckSecs", WebAppContainerMBean.class, var3, var4);
         var1.put("ServletReloadCheckSecs", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("XPoweredByHeaderLevel")) {
         var3 = "getXPoweredByHeaderLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXPoweredByHeaderLevel";
         }

         var2 = new PropertyDescriptor("XPoweredByHeaderLevel", WebAppContainerMBean.class, var3, var4);
         var1.put("XPoweredByHeaderLevel", var2);
         var2.setValue("description", "<p> WebLogic Server uses the X-Powered-By HTTP header, as recommended by the Servlet 2.4 specification, to publish its implementation information.</p>  <p> Following are the options: </p> <ul> <li>\"NONE\":            X-Powered-By header will not be sent </li> <li>\"SHORT\" (default): \"Servlet/2.4 JSP/2.0\"  </li> <li>\"MEDIUM\":          \"Servlet/2.4 JSP/2.0 (WebLogic/9.1)\" </li> <li>\"FULL\":            \"Servlet/2.4 JSP/1.2 (WebLogic/9.1 JDK/1.4.1_05)\" </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setXPoweredByHeaderLevel(String)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "SHORT");
         var2.setValue("legalValues", new Object[]{"NONE", "SHORT", "MEDIUM", "FULL"});
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AllowAllRoles")) {
         var3 = "isAllowAllRoles";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowAllRoles";
         }

         var2 = new PropertyDescriptor("AllowAllRoles", WebAppContainerMBean.class, var3, var4);
         var1.put("AllowAllRoles", var2);
         var2.setValue("description", "<p> In the security-constraints elements defined in a Web application's web.xml deployment descriptor, the auth-constraint element indicates the user roles that should be permitted access to this resource collection. Here role-name = \"*\" is a compact syntax for indicating all roles in the Web application. In previous releases, role-name = \"*\" was treated as all users/roles defined in the realm.  This parameter is a backward-compatibility switch to restore old behavior. Default behavior is one required by the spec, meaning all roles defined in the web application.  If set, the value defined in weblogic.xml (container-descriptor -> allow-all-roles) takes precedence (if set) over this value. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setAllowAllRoles(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthCookieEnabled")) {
         var3 = "isAuthCookieEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthCookieEnabled";
         }

         var2 = new PropertyDescriptor("AuthCookieEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("AuthCookieEnabled", var2);
         var2.setValue("description", "<p>Whether authcookie feature is enabled or not.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setAuthCookieEnabled(boolean)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#isAuthCookieEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ChangeSessionIDOnAuthentication")) {
         var3 = "isChangeSessionIDOnAuthentication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setChangeSessionIDOnAuthentication";
         }

         var2 = new PropertyDescriptor("ChangeSessionIDOnAuthentication", WebAppContainerMBean.class, var3, var4);
         var1.put("ChangeSessionIDOnAuthentication", var2);
         var2.setValue("description", "<p>Global property to determine if we need to generate a new SessionID after authentication. When this property set to \"false\", the previous sessionID will be retained even after authorization. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("ClientCertProxyEnabled")) {
         var3 = "isClientCertProxyEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertProxyEnabled";
         }

         var2 = new PropertyDescriptor("ClientCertProxyEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("ClientCertProxyEnabled", var2);
         var2.setValue("description", "<p>Specifies whether or not to honor the WL-Proxy-Client-Cert header coming with the request. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setClientCertProxyEnabled(boolean)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#isClientCertProxyEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isClientCertProxyEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("FilterDispatchedRequestsEnabled")) {
         var3 = "isFilterDispatchedRequestsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFilterDispatchedRequestsEnabled";
         }

         var2 = new PropertyDescriptor("FilterDispatchedRequestsEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("FilterDispatchedRequestsEnabled", var2);
         var2.setValue("description", "<p> Indicates whether or not to apply filters to dispatched requests. This is a backward compatibility flag. Until version 8.1, WebLogic Server applied ServletFilters (if configured for the Web application) on request dispatches (and includes/forwards). Servlet 2.4 has introduced the \"Dispatcher\" element to make this behavior explicit. The default value is Dispatcher=REQUEST. In order to be complaint with the J2EE specification, the default value for FilterDispatchedRequestsEnabled is false beginning with WebLogic Server 9.0. Note that if you are using old descriptors (meaning web.xml does not have version=2.4), then WebLogic Server automatically uses FilterDispatchedRequestsEnabled = true for the Web applications, unless filter-dispatched-requests-enabled is explicitly set to false in weblogic.xml. This means that old applications will work fine without any modification. Additionally, during migration of old domains to the 9.0 domain, the migration plugin automatically sets this flag to true. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setFilterDispatchedRequestsEnabled(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("HttpTraceSupportEnabled")) {
         var3 = "isHttpTraceSupportEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHttpTraceSupportEnabled";
         }

         var2 = new PropertyDescriptor("HttpTraceSupportEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("HttpTraceSupportEnabled", var2);
         var2.setValue("description", "<p> Returns the value of HttpTraceSupportEnabled. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setHttpTraceSupportEnabled(boolean)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#setHttpTraceSupportEnabled(boolean)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#setHttpTraceSupportEnabled(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JSPCompilerBackwardsCompatible")) {
         var3 = "isJSPCompilerBackwardsCompatible";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJSPCompilerBackwardsCompatible";
         }

         var2 = new PropertyDescriptor("JSPCompilerBackwardsCompatible", WebAppContainerMBean.class, var3, var4);
         var1.put("JSPCompilerBackwardsCompatible", var2);
         var2.setValue("description", "<p>Global property to determine the behavior of the JSP compiler. When this property set to \"true\", the JSP compiler throws a translation error for JSPs that do not conform to the JSP2.0 specification. This property exists for backward compatibility.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("OptimisticSerialization")) {
         var3 = "isOptimisticSerialization";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOptimisticSerialization";
         }

         var2 = new PropertyDescriptor("OptimisticSerialization", WebAppContainerMBean.class, var3, var4);
         var1.put("OptimisticSerialization", var2);
         var2.setValue("description", "<p> When OptimisticSerialization is turned on, WebLogic server does not serialize-deserialize context and request attributes upon getAttribute(name) when a request gets dispatched across servlet contexts. This means you will need to make sure that the attributes common to Web applications are scoped to a common parent classloader (they are application-scoped) or placed in the system classpath if the two Web applications do not belong to the same application. When OptimisticSerialization is turned off (which is the default) WebLogic Server does serialize-deserialize context and request attributes upon getAttribute(name) to avoid the possibility of ClassCastExceptions. The value of OptimisticSerialization can also be overridden for specific Web applications by setting the optimistic-serialization value in weblogic.xml. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isOptimisticSerialization()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("OverloadProtectionEnabled")) {
         var3 = "isOverloadProtectionEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOverloadProtectionEnabled";
         }

         var2 = new PropertyDescriptor("OverloadProtectionEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("OverloadProtectionEnabled", var2);
         var2.setValue("description", "<p>This parameter is used to enable overload protection in the webapp container against low memory conditions. When a low memory situation occurs, new session creation attempts will result in weblogic.servlet.SessionCreationException. The application code needs to catch this exception and take proper action. Alternatively appropriate error-pages can be configured in web.xml against weblogic.servlet.SessionCreationException. This check is performed only on memory and replicated sessions. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.servlet.SessionCreationException"), BeanInfoHelper.encodeEntities("#setOverloadProtectionEnabled(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReloginEnabled")) {
         var3 = "isReloginEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReloginEnabled";
         }

         var2 = new PropertyDescriptor("ReloginEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("ReloginEnabled", var2);
         var2.setValue("description", "<p>Beginning with the 9.0 release the FORM/BASIC authentication behavior has been modified to conform strictly to the J2EE Specification. If a user has logged-in but does not have privileges to access a resource, the 403 (FORBIDDEN) page will be returned. Turn this flag on to enable the old behavior, which was to return the user to the login form.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setReloginEnabled(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("RetainOriginalURL")) {
         var3 = "isRetainOriginalURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetainOriginalURL";
         }

         var2 = new PropertyDescriptor("RetainOriginalURL", WebAppContainerMBean.class, var3, var4);
         var1.put("RetainOriginalURL", var2);
         var2.setValue("description", "<p> retain-original-url is used in FORM based authentication scenarios. When this property is set to true, after a successful authentication, Weblogic Server will redirect back to the web resource (page/servlet) retaining the protocol (http/https) used to access the protected resource in the original request. If set to false (which is the default value), Weblogic Server will redirect back to the protected resource using the current protocol. retain-original-url value can also be specified at per webapp level in weblogic.xml. The value in weblogic.xml, if specified, overrides the domain level value.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isRetainOriginalURL()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("RtexprvalueJspParamName")) {
         var3 = "isRtexprvalueJspParamName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRtexprvalueJspParamName";
         }

         var2 = new PropertyDescriptor("RtexprvalueJspParamName", WebAppContainerMBean.class, var3, var4);
         var1.put("RtexprvalueJspParamName", var2);
         var2.setValue("description", "<p>Global property which determines the behavior of the JSP compiler when a jsp:param attribute \"name\" has a request time value. Without this property set to \"true\", the JSP compiler throws an error for a JSP using a request time value for the \"name\" attribute as mandated by the JSP 2.0 spec. This property exists for backward compatibility.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setRtexprvalueJspParamName(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ServletAuthenticationFormURL")) {
         var3 = "isServletAuthenticationFormURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServletAuthenticationFormURL";
         }

         var2 = new PropertyDescriptor("ServletAuthenticationFormURL", WebAppContainerMBean.class, var3, var4);
         var1.put("ServletAuthenticationFormURL", var2);
         var2.setValue("description", "<p> ServletAuthenticationFormURL is used for backward compatibility with previous releases of Weblogic Server.If ServletAuthenticationFormURL is set to true (default), then ServletAuthentication.getTargetURLForFormAuthentication() and HttpSession.getAttribute(AuthFilter.TARGET_URL) will return the URL of the protected target resource. If set to false, the above API's will return the URI of the protected target resource. By default the value is set to true.(new method added in 9.0.0.1) </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.servlet.security.ServletAuthethentication.getTargetURLForFormAuthentication"), BeanInfoHelper.encodeEntities("weblogic.servlet.security.ServletAuthethentication.getTargetURIForFormAuthentication"), BeanInfoHelper.encodeEntities("#isServletAuthenticationFormURL()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ShowArchivedRealPathEnabled")) {
         var3 = "isShowArchivedRealPathEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setShowArchivedRealPathEnabled";
         }

         var2 = new PropertyDescriptor("ShowArchivedRealPathEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("ShowArchivedRealPathEnabled", var2);
         var2.setValue("description", "<p>Global property to determine the behavior of getRealPath() for archived web applications. When this property set to \"true\", getRealPath() will return the canonical path of the resource files. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("WAPEnabled")) {
         var3 = "isWAPEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWAPEnabled";
         }

         var2 = new PropertyDescriptor("WAPEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("WAPEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the session ID should include JVM information. (Checking this box may be necessary when using URL rewriting with WAP devices that limit the size of the URL to 128 characters, and may also affect the use of replicated sessions in a cluster.) When this box is selected, the default size of the URL will be set at 52 characters, and it will not contain any special characters.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setWAPEnabled(boolean)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#isWAPEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WeblogicPluginEnabled")) {
         var3 = "isWeblogicPluginEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWeblogicPluginEnabled";
         }

         var2 = new PropertyDescriptor("WeblogicPluginEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("WeblogicPluginEnabled", var2);
         var2.setValue("description", "<p>Specifies whether or not the proprietary <tt>WL-Proxy-Client-IP</tt> header should be honored. (This is needed only when WebLogic plugins are configured.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setWeblogicPluginEnabled(boolean)"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#isWeblogicPluginEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isWeblogicPluginEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WorkContextPropagationEnabled")) {
         var3 = "isWorkContextPropagationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWorkContextPropagationEnabled";
         }

         var2 = new PropertyDescriptor("WorkContextPropagationEnabled", WebAppContainerMBean.class, var3, var4);
         var1.put("WorkContextPropagationEnabled", var2);
         var2.setValue("description", "<p>Indicates whether or not WorkContextPropagation is enabled. By default it is turned on. There is a little overhead involved in propagating WorkContexts. Therefore, if you don't care about WorkContext propagation, turn this value off in production environments. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setWorkContextPropagationEnabled(boolean)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
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
