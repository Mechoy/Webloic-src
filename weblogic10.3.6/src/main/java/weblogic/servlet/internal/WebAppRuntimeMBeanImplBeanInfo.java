package weblogic.servlet.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.j2ee.ComponentRuntimeMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;

public class WebAppRuntimeMBeanImplBeanInfo extends ComponentRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebAppComponentRuntimeMBean.class;

   public WebAppRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebAppRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebAppRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.servlet.internal");
      String var3 = (new String("Describes a servlet component (servlet context).  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WebAppComponentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AllServletSessions")) {
         var3 = "getAllServletSessions";
         var4 = null;
         var2 = new PropertyDescriptor("AllServletSessions", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("AllServletSessions", var2);
         var2.setValue("description", "<p>Returns a set of all current valid sessions as a list. If session monitoring is turned off, this method will return an empty list.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ApplicationIdentifier")) {
         var3 = "getApplicationIdentifier";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationIdentifier", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ApplicationIdentifier", var2);
         var2.setValue("description", "<p>Provides the identifier of the application that contains the web module</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("CoherenceClusterRuntime")) {
         var3 = "getCoherenceClusterRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceClusterRuntime", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("CoherenceClusterRuntime", var2);
         var2.setValue("description", "<p>Returns the Coherence Cluster related runtime mbean for this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.3.0");
      }

      if (!var1.containsKey("ComponentName")) {
         var3 = "getComponentName";
         var4 = null;
         var2 = new PropertyDescriptor("ComponentName", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ComponentName", var2);
         var2.setValue("description", "<p>Provides the name of this component.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ContextRoot")) {
         var3 = "getContextRoot";
         var4 = null;
         var2 = new PropertyDescriptor("ContextRoot", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ContextRoot", var2);
         var2.setValue("description", "<p>Returns the context root (context path) for the webapp</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("DeploymentState")) {
         var3 = "getDeploymentState";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentState", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("DeploymentState", var2);
         var2.setValue("description", "<p>The current deployment state of the module.</p>  <p>A module can be in one and only one of the following states. State can be changed via deployment or administrator console.</p>  <p>- UNPREPARED. State indicating at this  module is neither  prepared or active.</p>  <p>- PREPARED. State indicating at this module of this application is prepared, but not active. The classes have been loaded and the module has been validated.</p>  <p>- ACTIVATED. State indicating at this module  is currently active.</p>  <p>- NEW. State indicating this module has just been created and is being initialized.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setDeploymentState(int)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("JSPCompileCommand")) {
         var3 = "getJSPCompileCommand";
         var4 = null;
         var2 = new PropertyDescriptor("JSPCompileCommand", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("JSPCompileCommand", var2);
         var2.setValue("description", "<p>Provides the jsp's compileCommand as it is configured in weblogic.xml.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JSPPageCheckSecs")) {
         var3 = "getJSPPageCheckSecs";
         var4 = null;
         var2 = new PropertyDescriptor("JSPPageCheckSecs", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("JSPPageCheckSecs", var2);
         var2.setValue("description", "<p>Provides the jsp's PageCheckSecs as it is configured in weblogic.xml.</p> ");
      }

      if (!var1.containsKey("KodoPersistenceUnitRuntimes")) {
         var3 = "getKodoPersistenceUnitRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("KodoPersistenceUnitRuntimes", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("KodoPersistenceUnitRuntimes", var2);
         var2.setValue("description", "<p>Provides an array of KodoPersistenceUnitRuntimeMBean objects for this EJB module. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("LibraryRuntimes")) {
         var3 = "getLibraryRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("LibraryRuntimes", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("LibraryRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of library runtime instances for each J2EE library that is contained in this Enterprise application. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("LogFilename")) {
         var3 = "getLogFilename";
         var4 = null;
         var2 = new PropertyDescriptor("LogFilename", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("LogFilename", var2);
         var2.setValue("description", "Returns the log filename as configured in the \"logging/log-filename\" element in weblogic.xml ");
      }

      if (!var1.containsKey("LogRuntime")) {
         var3 = "getLogRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("LogRuntime", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("LogRuntime", var2);
         var2.setValue("description", "<p> Returns the log runtime associated with the j2ee webapp log ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ModuleId")) {
         var3 = "getModuleId";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleId", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ModuleId", var2);
         var2.setValue("description", "<p>Returns the identifier for this Component.  The identifier is unique within the application.</p>  <p>Typical modules will use the URI for their id.  Web Modules will return their context-root since the web-uri may not be unique within an EAR. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ModuleURI")) {
         var3 = "getModuleURI";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleURI", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ModuleURI", var2);
         var2.setValue("description", "<p>Returns the web-uri as configured in application.xml for the webapp. For a standalone war it will return the docroot (if exploded) or name of the war file (if archived).</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>Provides the name of this mbean.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("OpenSessionsCurrentCount")) {
         var3 = "getOpenSessionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("OpenSessionsCurrentCount", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("OpenSessionsCurrentCount", var2);
         var2.setValue("description", "<p>Provides a count of the current total number of open sessions in this module.</p>  <p>Returns the current total number of open sessions in this component.</p> ");
      }

      if (!var1.containsKey("OpenSessionsHighCount")) {
         var3 = "getOpenSessionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("OpenSessionsHighCount", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("OpenSessionsHighCount", var2);
         var2.setValue("description", "<p>Provides the high water mark of the total number of open sessions in this server. The count starts at zero each time the server is activated. Note that this is an optimization method for a highly useful statistic that could be implemented less efficiently using change notification.</p> ");
      }

      if (!var1.containsKey("PageFlows")) {
         var3 = "getPageFlows";
         var4 = null;
         var2 = new PropertyDescriptor("PageFlows", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("PageFlows", var2);
         var2.setValue("description", "<p>Provides an hook for getting Beehive runtime metrics for the current module</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ServletReloadCheckSecs")) {
         var3 = "getServletReloadCheckSecs";
         var4 = null;
         var2 = new PropertyDescriptor("ServletReloadCheckSecs", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ServletReloadCheckSecs", var2);
         var2.setValue("description", "<p>Provides the servlet reload check seconds as it is configured in weblogic.xml.</p> ");
      }

      if (!var1.containsKey("ServletSessions")) {
         var3 = "getServletSessions";
         var4 = null;
         var2 = new PropertyDescriptor("ServletSessions", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ServletSessions", var2);
         var2.setValue("description", "<p>Provides an array of ServletSessionRuntimeMBeans associated with this component. This operation should only be done by explicit poll request (no real-time monitoring). This method will return a non-empty array only when session-monitoring has been turned on in weblogic.xml</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getServletSessionsMonitoringIds()")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("deprecated", "as of WebLogic 9.0, use getServletSessionsMonitoringTags() ");
      }

      if (!var1.containsKey("ServletSessionsMonitoringIds")) {
         var3 = "getServletSessionsMonitoringIds";
         var4 = null;
         var2 = new PropertyDescriptor("ServletSessionsMonitoringIds", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("ServletSessionsMonitoringIds", var2);
         var2.setValue("description", "<p> This method returns an array of monitoring Ids for http sessions. By default the monitoring id for a given http session is a random string (not the same as session id for security reasons). If the value of the element monitoring-attribute-name in session-descriptor of weblogic.xml is set, the monitoring id will be the toString() of the attribute value in the session, using monitoring-attribute-name as the key. </p> ");
      }

      if (!var1.containsKey("Servlets")) {
         var3 = "getServlets";
         var4 = null;
         var2 = new PropertyDescriptor("Servlets", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("Servlets", var2);
         var2.setValue("description", "<p>Provides an array of ServletRuntimeMBeans associated with this module</p>  <p>Return an array of ServletRuntimeMBeans associated with this component</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SessionCookieComment")) {
         var3 = "getSessionCookieComment";
         var4 = null;
         var2 = new PropertyDescriptor("SessionCookieComment", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionCookieComment", var2);
         var2.setValue("description", "<p>By default, all applications on WebLogic Server specify \"WebLogic Session Tracking Cookie\" as the cookie comment. To provide a more specific comment, edit your application's <code>weblogic.xml</code> deployment descriptor.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionCookieDomain")) {
         var3 = "getSessionCookieDomain";
         var4 = null;
         var2 = new PropertyDescriptor("SessionCookieDomain", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionCookieDomain", var2);
         var2.setValue("description", "<p>By default, clients can return cookies only to the server that issued the cookie. You can change this default behavior by editing your application's <code>weblogic.xml</code> deployment descriptor.</p>  <p>For more information, see the Servlet specification from Sun Microsystems.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionCookieMaxAgeSecs")) {
         var3 = "getSessionCookieMaxAgeSecs";
         var4 = null;
         var2 = new PropertyDescriptor("SessionCookieMaxAgeSecs", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionCookieMaxAgeSecs", var2);
         var2.setValue("description", "<p>Provides the life span of the session cookie, in seconds, after which it expires on the client. If the value is 0, the cookie expires immediately.<br clear=\"none\" /> If set to -1, the cookie expires when the user exits the browser.</p> ");
      }

      if (!var1.containsKey("SessionCookieName")) {
         var3 = "getSessionCookieName";
         var4 = null;
         var2 = new PropertyDescriptor("SessionCookieName", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionCookieName", var2);
         var2.setValue("description", "<p>By default, all applications on WebLogic Server specify \"JSESSIONID\" as the cookie name. To provide a more specific name, edit your application's <code>weblogic.xml</code> deployment descriptor.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionCookiePath")) {
         var3 = "getSessionCookiePath";
         var4 = null;
         var2 = new PropertyDescriptor("SessionCookiePath", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionCookiePath", var2);
         var2.setValue("description", "<p>Provides the path name to which clients send cookies.</p> ");
      }

      if (!var1.containsKey("SessionIDLength")) {
         var3 = "getSessionIDLength";
         var4 = null;
         var2 = new PropertyDescriptor("SessionIDLength", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionIDLength", var2);
         var2.setValue("description", "<p>Provides the session ID length configured for http sessions.</p> ");
      }

      if (!var1.containsKey("SessionInvalidationIntervalSecs")) {
         var3 = "getSessionInvalidationIntervalSecs";
         var4 = null;
         var2 = new PropertyDescriptor("SessionInvalidationIntervalSecs", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionInvalidationIntervalSecs", var2);
         var2.setValue("description", "<p>Provides the invalidation check timer interval configured for http sessions.</p> ");
      }

      if (!var1.containsKey("SessionTimeoutSecs")) {
         var3 = "getSessionTimeoutSecs";
         var4 = null;
         var2 = new PropertyDescriptor("SessionTimeoutSecs", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionTimeoutSecs", var2);
         var2.setValue("description", "<p>Provides the timeout configured for http sessions.</p> ");
      }

      if (!var1.containsKey("SessionsOpenedTotalCount")) {
         var3 = "getSessionsOpenedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SessionsOpenedTotalCount", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionsOpenedTotalCount", var2);
         var2.setValue("description", "<p>Provides a count of the total number of sessions opened.</p> ");
      }

      if (!var1.containsKey("SingleThreadedServletPoolSize")) {
         var3 = "getSingleThreadedServletPoolSize";
         var4 = null;
         var2 = new PropertyDescriptor("SingleThreadedServletPoolSize", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SingleThreadedServletPoolSize", var2);
         var2.setValue("description", "<p>Provides the single threaded servlet pool size as it is configured in weblogic.xml.</p> ");
      }

      if (!var1.containsKey("SourceInfo")) {
         var3 = "getSourceInfo";
         var4 = null;
         var2 = new PropertyDescriptor("SourceInfo", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SourceInfo", var2);
         var2.setValue("description", "<p>Provides an informative string about the module's source.</p>  <p>Return an informative string about the component's source.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SpringRuntimeMBean")) {
         var3 = "getSpringRuntimeMBean";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSpringRuntimeMBean";
         }

         var2 = new PropertyDescriptor("SpringRuntimeMBean", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SpringRuntimeMBean", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("Status", var2);
         var2.setValue("description", "<p>Provides the status of the component.</p> ");
      }

      if (!var1.containsKey("WebPubSubRuntime")) {
         var3 = "getWebPubSubRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWebPubSubRuntime";
         }

         var2 = new PropertyDescriptor("WebPubSubRuntime", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("WebPubSubRuntime", var2);
         var2.setValue("description", "Get Http Pub/Sub Server Runtime of this webapp ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all work managers defined in this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("WseeClientConfigurationRuntimes")) {
         var3 = "getWseeClientConfigurationRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeClientConfigurationRuntimes", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("WseeClientConfigurationRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service client reference configuration runtime instances that are contained in this web app within an Enterprise application.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WseeClientRuntimes")) {
         var3 = "getWseeClientRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeClientRuntimes", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("WseeClientRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service client runtime instances that are contained in this Enterprise JavaBean component. </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WseeV2Runtimes")) {
         var3 = "getWseeV2Runtimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeV2Runtimes", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("WseeV2Runtimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service runtime instances that are contained in this web app within an Enterprise application. </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("FilterDispatchedRequestsEnabled")) {
         var3 = "isFilterDispatchedRequestsEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("FilterDispatchedRequestsEnabled", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("FilterDispatchedRequestsEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the dispatched requests are filtered as configured in weblogic.xml.</p> ");
      }

      if (!var1.containsKey("IndexDirectoryEnabled")) {
         var3 = "isIndexDirectoryEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("IndexDirectoryEnabled", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("IndexDirectoryEnabled", var2);
         var2.setValue("description", "<p>Provides the directory indexing indicator as it is configured in weblogic.xml.</p> ");
      }

      if (!var1.containsKey("JSPDebug")) {
         var3 = "isJSPDebug";
         var4 = null;
         var2 = new PropertyDescriptor("JSPDebug", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("JSPDebug", var2);
         var2.setValue("description", "<p>Provides the jsp's debug/linenumbers parameter value as it is configured in weblogic.xml.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JSPKeepGenerated")) {
         var3 = "isJSPKeepGenerated";
         var4 = null;
         var2 = new PropertyDescriptor("JSPKeepGenerated", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("JSPKeepGenerated", var2);
         var2.setValue("description", "<p>Provides the jsp's KeepGenerated parameter value as it is configured in weblogic.xml.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("JSPVerbose")) {
         var3 = "isJSPVerbose";
         var4 = null;
         var2 = new PropertyDescriptor("JSPVerbose", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("JSPVerbose", var2);
         var2.setValue("description", "<p>Provides the jsp's verbose parameter value as it is configured in weblogic.xml.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionMonitoringEnabled")) {
         var3 = "isSessionMonitoringEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("SessionMonitoringEnabled", WebAppComponentRuntimeMBean.class, var3, var4);
         var1.put("SessionMonitoringEnabled", var2);
         var2.setValue("description", "<p>Provides the session monitoring indicator as it is configured in weblogic.xml.</p> ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WebAppComponentRuntimeMBean.class.getMethod("lookupWseeClientRuntime", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("rawClientId", "The raw client ID of the client to lookup. This ID does not contain the application/component qualifiers that are prepended to the full client ID for the client. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service client runtime instances that is contained in this Enterprise JavaBean component. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeClientRuntimes");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("lookupWseeV2Runtime", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The web service description name of the web service to look up. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service runtime instance that is contained in this web app within an Enterprise application. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeV2Runtimes");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("lookupWseeClientConfigurationRuntime", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The web service client reference name to look up. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service client reference configuration runtime instance that is contained in this web app within an Enterprise application.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeClientConfigurationRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WebAppComponentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("getServletSession", String.class);
      ParameterDescriptor[] var8 = new ParameterDescriptor[]{createParameterDescriptor("sessionID", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var8);
         var2.setValue("deprecated", "as of WebLogic 9.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides the servlet session by its session ID.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("invalidateServletSession", String.class);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("monitoringId", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      String[] var7;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var8);
         var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalStateException if sessions has been invalidated already")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Invalidates this session for a given monitoring id.</p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("#getServletSessionsMonitoringIds()")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("getSessionLastAccessedTime", String.class);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("monitoringId", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var8);
         var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalStateException if sessions has been invalidated already")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides a record of the last time this session was accessed. You need to pass the string returned by getServletSessionsMonitoringIds() </p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("#getServletSessionsMonitoringIds()")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("getSessionMaxInactiveInterval", String.class);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("monitoringId", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var8);
         var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalStateException if sessions has been invalidated already")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p> Returns the timeout (seconds) for the session corresponding to the given monitoringId.</p> ");
         var7 = new String[]{BeanInfoHelper.encodeEntities("#getServletSessionsMonitoringIds()")};
         var2.setValue("see", var7);
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("getMonitoringId", String.class);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("sessionId", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var8);
         var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalStateException if sessions has been invalidated already")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns the monitoring id for a session for a given sessionId</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("deleteInvalidSessions");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Invalidates expired sessions. This is useful to do the cleanup if the session invalidation trigger is too large.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("registerServlet", String.class, String.class, String[].class, Map.class, Integer.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<servlet> and <servlet-mapping> if load-on-startup has been specified and this method is invoked after the server has started the container will preload the servlet immediately. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentRuntimeMBean.class.getMethod("getKodoPersistenceUnitRuntime", String.class);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("unitName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var8);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Provides the KodoPersistenceUnitRuntimeMBean for the EJB with the specified name.</p> ");
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
