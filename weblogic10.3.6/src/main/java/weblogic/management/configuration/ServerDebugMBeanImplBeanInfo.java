package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class ServerDebugMBeanImplBeanInfo extends KernelDebugMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerDebugMBean.class;

   public ServerDebugMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerDebugMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerDebugMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Defines the debug attributes that are specific to WebLogic Server.</p> <p>While all attributes will be supported in adherence with the standard WebLogic Server deprecation policies, the resultant debug content is free to change in both form and content across releases.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ServerDebugMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ApplicationContainer")) {
         var3 = "getApplicationContainer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setApplicationContainer";
         }

         var2 = new PropertyDescriptor("ApplicationContainer", ServerDebugMBean.class, var3, var4);
         var1.put("ApplicationContainer", var2);
         var2.setValue("description", "<p>Debug Application Container deployment processing</p> ");
      }

      if (!var1.containsKey("BugReportServiceWsdlUrl")) {
         var3 = "getBugReportServiceWsdlUrl";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBugReportServiceWsdlUrl";
         }

         var2 = new PropertyDescriptor("BugReportServiceWsdlUrl", ServerDebugMBean.class, var3, var4);
         var1.put("BugReportServiceWsdlUrl", var2);
         var2.setValue("description", " ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ClassChangeNotifier")) {
         var3 = "getClassChangeNotifier";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassChangeNotifier";
         }

         var2 = new PropertyDescriptor("ClassChangeNotifier", ServerDebugMBean.class, var3, var4);
         var1.put("ClassChangeNotifier", var2);
         var2.setValue("description", "<p>Gets the fastswap ClassChangeNotifier debug attribute of ServerDebugMBean</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClassFinder")) {
         var3 = "getClassFinder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassFinder";
         }

         var2 = new PropertyDescriptor("ClassFinder", ServerDebugMBean.class, var3, var4);
         var1.put("ClassFinder", var2);
         var2.setValue("description", "<p>Debug ClassFinder processing</p> ");
      }

      if (!var1.containsKey("ClassLoader")) {
         var3 = "getClassLoader";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassLoader";
         }

         var2 = new PropertyDescriptor("ClassLoader", ServerDebugMBean.class, var3, var4);
         var1.put("ClassLoader", var2);
         var2.setValue("description", "<p>Debug ClassLoader processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClassLoaderVerbose")) {
         var3 = "getClassLoaderVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassLoaderVerbose";
         }

         var2 = new PropertyDescriptor("ClassLoaderVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("ClassLoaderVerbose", var2);
         var2.setValue("description", "<p>Detailed debug of ClassLoader processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClassloaderWebApp")) {
         var3 = "getClassloaderWebApp";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClassloaderWebApp";
         }

         var2 = new PropertyDescriptor("ClassloaderWebApp", ServerDebugMBean.class, var3, var4);
         var1.put("ClassloaderWebApp", var2);
         var2.setValue("description", "<p>Debug WebApp ClassLoader processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClasspathServlet")) {
         var3 = "getClasspathServlet";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClasspathServlet";
         }

         var2 = new PropertyDescriptor("ClasspathServlet", ServerDebugMBean.class, var3, var4);
         var1.put("ClasspathServlet", var2);
         var2.setValue("description", "<p>Debug ClassPathServlet processing</p> ");
      }

      if (!var1.containsKey("DebugAppContainer")) {
         var3 = "getDebugAppContainer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugAppContainer";
         }

         var2 = new PropertyDescriptor("DebugAppContainer", ServerDebugMBean.class, var3, var4);
         var1.put("DebugAppContainer", var2);
         var2.setValue("description", "<p>Debug Application Container processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugAsyncQueue")) {
         var3 = "getDebugAsyncQueue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugAsyncQueue";
         }

         var2 = new PropertyDescriptor("DebugAsyncQueue", ServerDebugMBean.class, var3, var4);
         var1.put("DebugAsyncQueue", var2);
         var2.setValue("description", "<p>Debug async replication/persistence information.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugBootstrapServlet")) {
         var3 = "getDebugBootstrapServlet";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugBootstrapServlet";
         }

         var2 = new PropertyDescriptor("DebugBootstrapServlet", ServerDebugMBean.class, var3, var4);
         var1.put("DebugBootstrapServlet", var2);
         var2.setValue("description", "<p>Debug the bootstrap servlet that runs on the Admin Server and is invoked over HTTP by a booting managed server.</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugCertRevocCheck")) {
         var3 = "getDebugCertRevocCheck";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugCertRevocCheck";
         }

         var2 = new PropertyDescriptor("DebugCertRevocCheck", ServerDebugMBean.class, var3, var4);
         var1.put("DebugCertRevocCheck", var2);
         var2.setValue("description", "<p>Debug Security PKI X.509 certificate revocation checking</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugClassRedef")) {
         var3 = "getDebugClassRedef";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugClassRedef";
         }

         var2 = new PropertyDescriptor("DebugClassRedef", ServerDebugMBean.class, var3, var4);
         var1.put("DebugClassRedef", var2);
         var2.setValue("description", "<p>Gets the DebugClassRedef attribute of ServerDebugMBean</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugClassSize")) {
         var3 = "getDebugClassSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugClassSize";
         }

         var2 = new PropertyDescriptor("DebugClassSize", ServerDebugMBean.class, var3, var4);
         var1.put("DebugClassSize", var2);
         var2.setValue("description", "<p>Gets the fastswap DebugClassSize debug attribute of ServerDebugMBean</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugCluster")) {
         var3 = "getDebugCluster";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugCluster";
         }

         var2 = new PropertyDescriptor("DebugCluster", ServerDebugMBean.class, var3, var4);
         var1.put("DebugCluster", var2);
         var2.setValue("description", "<p>Debug each GroupMessage that is sent or received by multicast.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugClusterAnnouncements")) {
         var3 = "getDebugClusterAnnouncements";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugClusterAnnouncements";
         }

         var2 = new PropertyDescriptor("DebugClusterAnnouncements", ServerDebugMBean.class, var3, var4);
         var1.put("DebugClusterAnnouncements", var2);
         var2.setValue("description", "<p>Debug each Announcement, StateDump, and Attributes message that is sent or received by multicast.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugClusterFragments")) {
         var3 = "getDebugClusterFragments";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugClusterFragments";
         }

         var2 = new PropertyDescriptor("DebugClusterFragments", ServerDebugMBean.class, var3, var4);
         var1.put("DebugClusterFragments", var2);
         var2.setValue("description", "<p>Debug for each fragment that is sent or received by multicast.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugClusterHeartbeats")) {
         var3 = "getDebugClusterHeartbeats";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugClusterHeartbeats";
         }

         var2 = new PropertyDescriptor("DebugClusterHeartbeats", ServerDebugMBean.class, var3, var4);
         var1.put("DebugClusterHeartbeats", var2);
         var2.setValue("description", "<p>Debug each cluster Heartbeat that is sent or received by multicast.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugConfigurationEdit")) {
         var3 = "getDebugConfigurationEdit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugConfigurationEdit";
         }

         var2 = new PropertyDescriptor("DebugConfigurationEdit", ServerDebugMBean.class, var3, var4);
         var1.put("DebugConfigurationEdit", var2);
         var2.setValue("description", "<p>Debug management configuration edit processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugConfigurationRuntime")) {
         var3 = "getDebugConfigurationRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugConfigurationRuntime";
         }

         var2 = new PropertyDescriptor("DebugConfigurationRuntime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugConfigurationRuntime", var2);
         var2.setValue("description", "<p>Debug management configuration runtime processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugConnectorService")) {
         var3 = "getDebugConnectorService";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugConnectorService";
         }

         var2 = new PropertyDescriptor("DebugConnectorService", ServerDebugMBean.class, var3, var4);
         var1.put("DebugConnectorService", var2);
         var2.setValue("description", "<p>Debug connector service action processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugConsensusLeasing")) {
         var3 = "getDebugConsensusLeasing";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugConsensusLeasing";
         }

         var2 = new PropertyDescriptor("DebugConsensusLeasing", ServerDebugMBean.class, var3, var4);
         var1.put("DebugConsensusLeasing", var2);
         var2.setValue("description", "<p>Debug cluster consensus lease processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDRSCalls")) {
         var3 = "getDebugDRSCalls";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDRSCalls";
         }

         var2 = new PropertyDescriptor("DebugDRSCalls", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDRSCalls", var2);
         var2.setValue("description", "<p>Debug Data replication service (DRS) API calls.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDRSHeartbeats")) {
         var3 = "getDebugDRSHeartbeats";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDRSHeartbeats";
         }

         var2 = new PropertyDescriptor("DebugDRSHeartbeats", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDRSHeartbeats", var2);
         var2.setValue("description", "<p>Debug DRS Heartbeats.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDRSMessages")) {
         var3 = "getDebugDRSMessages";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDRSMessages";
         }

         var2 = new PropertyDescriptor("DebugDRSMessages", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDRSMessages", var2);
         var2.setValue("description", "<p>Debug DRS Message traffic.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDRSQueues")) {
         var3 = "getDebugDRSQueues";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDRSQueues";
         }

         var2 = new PropertyDescriptor("DebugDRSQueues", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDRSQueues", var2);
         var2.setValue("description", "<p>Debug DRS Queueing traffic.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDRSStateTransitions")) {
         var3 = "getDebugDRSStateTransitions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDRSStateTransitions";
         }

         var2 = new PropertyDescriptor("DebugDRSStateTransitions", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDRSStateTransitions", var2);
         var2.setValue("description", "<p>Debug DRS State transitions.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDRSUpdateStatus")) {
         var3 = "getDebugDRSUpdateStatus";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDRSUpdateStatus";
         }

         var2 = new PropertyDescriptor("DebugDRSUpdateStatus", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDRSUpdateStatus", var2);
         var2.setValue("description", "<p>Debug DRS Update status processing.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugDeploy")) {
         var3 = "getDebugDeploy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeploy";
         }

         var2 = new PropertyDescriptor("DebugDeploy", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeploy", var2);
         var2.setValue("description", "<p>Debug deploy command processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDeployment")) {
         var3 = "getDebugDeployment";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeployment";
         }

         var2 = new PropertyDescriptor("DebugDeployment", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeployment", var2);
         var2.setValue("description", "<p>Debug deployment processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDeploymentService")) {
         var3 = "getDebugDeploymentService";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeploymentService";
         }

         var2 = new PropertyDescriptor("DebugDeploymentService", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeploymentService", var2);
         var2.setValue("description", "<p>Debug deployment service processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDeploymentServiceInternal")) {
         var3 = "getDebugDeploymentServiceInternal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeploymentServiceInternal";
         }

         var2 = new PropertyDescriptor("DebugDeploymentServiceInternal", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeploymentServiceInternal", var2);
         var2.setValue("description", "<p>Debug internal deployment service processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDeploymentServiceStatusUpdates")) {
         var3 = "getDebugDeploymentServiceStatusUpdates";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeploymentServiceStatusUpdates";
         }

         var2 = new PropertyDescriptor("DebugDeploymentServiceStatusUpdates", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeploymentServiceStatusUpdates", var2);
         var2.setValue("description", "<p>Debug deployment service status update processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDeploymentServiceTransport")) {
         var3 = "getDebugDeploymentServiceTransport";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeploymentServiceTransport";
         }

         var2 = new PropertyDescriptor("DebugDeploymentServiceTransport", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeploymentServiceTransport", var2);
         var2.setValue("description", "<p>Debug deployment service transport processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDeploymentServiceTransportHttp")) {
         var3 = "getDebugDeploymentServiceTransportHttp";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDeploymentServiceTransportHttp";
         }

         var2 = new PropertyDescriptor("DebugDeploymentServiceTransportHttp", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDeploymentServiceTransportHttp", var2);
         var2.setValue("description", "<p>Debug deployment service HTTP transport processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDescriptor")) {
         var3 = "getDebugDescriptor";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDescriptor";
         }

         var2 = new PropertyDescriptor("DebugDescriptor", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDescriptor", var2);
         var2.setValue("description", "<p>Debug descriptor framework processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticAccessor")) {
         var3 = "getDebugDiagnosticAccessor";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticAccessor";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticAccessor", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticAccessor", var2);
         var2.setValue("description", "<p>Debug diagnostic accessor processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticArchive")) {
         var3 = "getDebugDiagnosticArchive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticArchive";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticArchive", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticArchive", var2);
         var2.setValue("description", "<p>Debug diagnostic archive processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticArchiveRetirement")) {
         var3 = "getDebugDiagnosticArchiveRetirement";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticArchiveRetirement";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticArchiveRetirement", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticArchiveRetirement", var2);
         var2.setValue("description", "<p>Debug diagnostic archive retirement processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticCollections")) {
         var3 = "getDebugDiagnosticCollections";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticCollections";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticCollections", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticCollections", var2);
         var2.setValue("description", "<p>Debug diagnostic collection processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticContext")) {
         var3 = "getDebugDiagnosticContext";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticContext";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticContext", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticContext", var2);
         var2.setValue("description", "<p>Debug diagnostic context processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticDataGathering")) {
         var3 = "getDebugDiagnosticDataGathering";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticDataGathering";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticDataGathering", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticDataGathering", var2);
         var2.setValue("description", "<p>Debug data gathering processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticFileArchive")) {
         var3 = "getDebugDiagnosticFileArchive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticFileArchive";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticFileArchive", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticFileArchive", var2);
         var2.setValue("description", "<p>Debug diagnostic file archive processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticImage")) {
         var3 = "getDebugDiagnosticImage";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticImage";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticImage", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticImage", var2);
         var2.setValue("description", "<p>Debug diagnostic image processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticInstrumentation")) {
         var3 = "getDebugDiagnosticInstrumentation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticInstrumentation";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticInstrumentation", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticInstrumentation", var2);
         var2.setValue("description", "<p>Debug diagnostic instrumentation processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticInstrumentationActions")) {
         var3 = "getDebugDiagnosticInstrumentationActions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticInstrumentationActions";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticInstrumentationActions", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticInstrumentationActions", var2);
         var2.setValue("description", "<p>Debug instrumentation actions/monitors</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticInstrumentationConfig")) {
         var3 = "getDebugDiagnosticInstrumentationConfig";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticInstrumentationConfig";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticInstrumentationConfig", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticInstrumentationConfig", var2);
         var2.setValue("description", "<p>Debug instrumentation configuration processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticInstrumentationEvents")) {
         var3 = "getDebugDiagnosticInstrumentationEvents";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticInstrumentationEvents";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticInstrumentationEvents", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticInstrumentationEvents", var2);
         var2.setValue("description", "<p>Debug instrumentation event records</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticInstrumentationWeaving")) {
         var3 = "getDebugDiagnosticInstrumentationWeaving";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticInstrumentationWeaving";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticInstrumentationWeaving", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticInstrumentationWeaving", var2);
         var2.setValue("description", "<p>Debug instrumentation weaving</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticInstrumentationWeavingMatches")) {
         var3 = "getDebugDiagnosticInstrumentationWeavingMatches";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticInstrumentationWeavingMatches";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticInstrumentationWeavingMatches", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticInstrumentationWeavingMatches", var2);
         var2.setValue("description", "<p>Debug instrumentation weaving for matches only</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticJdbcArchive")) {
         var3 = "getDebugDiagnosticJdbcArchive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticJdbcArchive";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticJdbcArchive", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticJdbcArchive", var2);
         var2.setValue("description", "<p>Debug diagnostic jdbc archive processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticLifecycleHandlers")) {
         var3 = "getDebugDiagnosticLifecycleHandlers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticLifecycleHandlers";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticLifecycleHandlers", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticLifecycleHandlers", var2);
         var2.setValue("description", "<p>Debug diagnostic lifecycle handler processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticQuery")) {
         var3 = "getDebugDiagnosticQuery";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticQuery";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticQuery", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticQuery", var2);
         var2.setValue("description", "<p>Debug diagnostic query processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticWatch")) {
         var3 = "getDebugDiagnosticWatch";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticWatch";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticWatch", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticWatch", var2);
         var2.setValue("description", "<p>Debug diagnostic watch processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticWlstoreArchive")) {
         var3 = "getDebugDiagnosticWlstoreArchive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticWlstoreArchive";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticWlstoreArchive", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticWlstoreArchive", var2);
         var2.setValue("description", "<p>Debug diagnostic wlstore archive processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticsHarvester")) {
         var3 = "getDebugDiagnosticsHarvester";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticsHarvester";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticsHarvester", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticsHarvester", var2);
         var2.setValue("description", "<p>Debug diagnostic harvester processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticsHarvesterData")) {
         var3 = "getDebugDiagnosticsHarvesterData";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticsHarvesterData";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticsHarvesterData", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticsHarvesterData", var2);
         var2.setValue("description", "<p>Detailed debug of diagnostic harvester processing</p> <p>This should be used in conjunction with DebugDiagnosticsHarvester.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticsHarvesterMBeanPlugin")) {
         var3 = "getDebugDiagnosticsHarvesterMBeanPlugin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticsHarvesterMBeanPlugin";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticsHarvesterMBeanPlugin", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticsHarvesterMBeanPlugin", var2);
         var2.setValue("description", "<p>Debug MBean harvester processing.</p> <p>This should probably be used in conjunction with DebugDiagnosticsHarvester.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticsHarvesterTreeBeanPlugin")) {
         var3 = "getDebugDiagnosticsHarvesterTreeBeanPlugin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticsHarvesterTreeBeanPlugin";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticsHarvesterTreeBeanPlugin", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticsHarvesterTreeBeanPlugin", var2);
         var2.setValue("description", "<p>Debug tree bean harvester processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDiagnosticsModule")) {
         var3 = "getDebugDiagnosticsModule";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDiagnosticsModule";
         }

         var2 = new PropertyDescriptor("DebugDiagnosticsModule", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDiagnosticsModule", var2);
         var2.setValue("description", "<p>Debug diagnostic module processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugDomainLogHandler")) {
         var3 = "getDebugDomainLogHandler";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugDomainLogHandler";
         }

         var2 = new PropertyDescriptor("DebugDomainLogHandler", ServerDebugMBean.class, var3, var4);
         var1.put("DebugDomainLogHandler", var2);
         var2.setValue("description", "<p>Debug Domain Log Handler processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbCaching")) {
         var3 = "getDebugEjbCaching";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbCaching";
         }

         var2 = new PropertyDescriptor("DebugEjbCaching", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbCaching", var2);
         var2.setValue("description", "<p>Debug EJB Caching</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbCmpDeployment")) {
         var3 = "getDebugEjbCmpDeployment";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbCmpDeployment";
         }

         var2 = new PropertyDescriptor("DebugEjbCmpDeployment", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbCmpDeployment", var2);
         var2.setValue("description", "<p>Debug EJB CMP deployment processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbCmpRuntime")) {
         var3 = "getDebugEjbCmpRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbCmpRuntime";
         }

         var2 = new PropertyDescriptor("DebugEjbCmpRuntime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbCmpRuntime", var2);
         var2.setValue("description", "<p>Debug EJB CMP runtime processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbCompilation")) {
         var3 = "getDebugEjbCompilation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbCompilation";
         }

         var2 = new PropertyDescriptor("DebugEjbCompilation", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbCompilation", var2);
         var2.setValue("description", "<p>Debug EJB compilation</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbDeployment")) {
         var3 = "getDebugEjbDeployment";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbDeployment";
         }

         var2 = new PropertyDescriptor("DebugEjbDeployment", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbDeployment", var2);
         var2.setValue("description", "<p>Debug EJB deployment</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbInvoke")) {
         var3 = "getDebugEjbInvoke";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbInvoke";
         }

         var2 = new PropertyDescriptor("DebugEjbInvoke", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbInvoke", var2);
         var2.setValue("description", "<p>Debug EJB invocation processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbLocking")) {
         var3 = "getDebugEjbLocking";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbLocking";
         }

         var2 = new PropertyDescriptor("DebugEjbLocking", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbLocking", var2);
         var2.setValue("description", "<p>Debug EJB locking</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbMdbConnection")) {
         var3 = "getDebugEjbMdbConnection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbMdbConnection";
         }

         var2 = new PropertyDescriptor("DebugEjbMdbConnection", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbMdbConnection", var2);
         var2.setValue("description", "<p>Debug EJB MDB Connection processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbPooling")) {
         var3 = "getDebugEjbPooling";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbPooling";
         }

         var2 = new PropertyDescriptor("DebugEjbPooling", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbPooling", var2);
         var2.setValue("description", "<p>Debug EJB pooling</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbSecurity")) {
         var3 = "getDebugEjbSecurity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbSecurity";
         }

         var2 = new PropertyDescriptor("DebugEjbSecurity", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbSecurity", var2);
         var2.setValue("description", "<p>Debug EJB Security</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbSwapping")) {
         var3 = "getDebugEjbSwapping";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbSwapping";
         }

         var2 = new PropertyDescriptor("DebugEjbSwapping", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbSwapping", var2);
         var2.setValue("description", "<p>Debug EJB Swapping</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEjbTimers")) {
         var3 = "getDebugEjbTimers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEjbTimers";
         }

         var2 = new PropertyDescriptor("DebugEjbTimers", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEjbTimers", var2);
         var2.setValue("description", "<p>Debug EJB Timer processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugEmbeddedLDAP")) {
         var3 = "getDebugEmbeddedLDAP";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEmbeddedLDAP";
         }

         var2 = new PropertyDescriptor("DebugEmbeddedLDAP", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEmbeddedLDAP", var2);
         var2.setValue("description", "<p>Debug Embedded LDAP processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugEmbeddedLDAPLogLevel")) {
         var3 = "getDebugEmbeddedLDAPLogLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEmbeddedLDAPLogLevel";
         }

         var2 = new PropertyDescriptor("DebugEmbeddedLDAPLogLevel", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEmbeddedLDAPLogLevel", var2);
         var2.setValue("description", "<p>Debug Embedded LDAP processing (log level)</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalMax", new Integer(11));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("DebugEmbeddedLDAPLogToConsole")) {
         var3 = "getDebugEmbeddedLDAPLogToConsole";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEmbeddedLDAPLogToConsole";
         }

         var2 = new PropertyDescriptor("DebugEmbeddedLDAPLogToConsole", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEmbeddedLDAPLogToConsole", var2);
         var2.setValue("description", "<p>Debug Embedded LDAP processing (output to console)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugEmbeddedLDAPWriteOverrideProps")) {
         var3 = "getDebugEmbeddedLDAPWriteOverrideProps";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEmbeddedLDAPWriteOverrideProps";
         }

         var2 = new PropertyDescriptor("DebugEmbeddedLDAPWriteOverrideProps", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEmbeddedLDAPWriteOverrideProps", var2);
         var2.setValue("description", "<p>Embedded LDAP Write All Overrides to Property Files</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugEventManager")) {
         var3 = "getDebugEventManager";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugEventManager";
         }

         var2 = new PropertyDescriptor("DebugEventManager", ServerDebugMBean.class, var3, var4);
         var1.put("DebugEventManager", var2);
         var2.setValue("description", "<p>Debug Event Manager processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugFileDistributionServlet")) {
         var3 = "getDebugFileDistributionServlet";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugFileDistributionServlet";
         }

         var2 = new PropertyDescriptor("DebugFileDistributionServlet", ServerDebugMBean.class, var3, var4);
         var1.put("DebugFileDistributionServlet", var2);
         var2.setValue("description", "<p>Debug the file distribution servlet that runs on the Admin Server and is invoked over HTTP by a booting managed server.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugHttp")) {
         var3 = "getDebugHttp";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugHttp";
         }

         var2 = new PropertyDescriptor("DebugHttp", ServerDebugMBean.class, var3, var4);
         var1.put("DebugHttp", var2);
         var2.setValue("description", "<p>Debug WebApp Container HTTP processing.</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugHttpLogging")) {
         var3 = "getDebugHttpLogging";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugHttpLogging";
         }

         var2 = new PropertyDescriptor("DebugHttpLogging", ServerDebugMBean.class, var3, var4);
         var1.put("DebugHttpLogging", var2);
         var2.setValue("description", "<p>Debug log manager in the webapp container.</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugHttpSessions")) {
         var3 = "getDebugHttpSessions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugHttpSessions";
         }

         var2 = new PropertyDescriptor("DebugHttpSessions", ServerDebugMBean.class, var3, var4);
         var1.put("DebugHttpSessions", var2);
         var2.setValue("description", "<p>Debug Http Session management in the webapp container.</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPNaming")) {
         var3 = "getDebugIIOPNaming";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPNaming";
         }

         var2 = new PropertyDescriptor("DebugIIOPNaming", ServerDebugMBean.class, var3, var4);
         var1.put("DebugIIOPNaming", var2);
         var2.setValue("description", "<p>Debug IIOP CosNaming processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugIIOPTunneling")) {
         var3 = "getDebugIIOPTunneling";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugIIOPTunneling";
         }

         var2 = new PropertyDescriptor("DebugIIOPTunneling", ServerDebugMBean.class, var3, var4);
         var1.put("DebugIIOPTunneling", var2);
         var2.setValue("description", "<p>Debug IIOP tunnelling</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJ2EEManagement")) {
         var3 = "getDebugJ2EEManagement";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJ2EEManagement";
         }

         var2 = new PropertyDescriptor("DebugJ2EEManagement", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJ2EEManagement", var2);
         var2.setValue("description", "<p>Debug J2EE management processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJAXPDebugLevel")) {
         var3 = "getDebugJAXPDebugLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPDebugLevel";
         }

         var2 = new PropertyDescriptor("DebugJAXPDebugLevel", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPDebugLevel", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug level</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalMax", new Integer(3));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("DebugJAXPDebugName")) {
         var3 = "getDebugJAXPDebugName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPDebugName";
         }

         var2 = new PropertyDescriptor("DebugJAXPDebugName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPDebugName", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug name</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJAXPIncludeClass")) {
         var3 = "getDebugJAXPIncludeClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPIncludeClass";
         }

         var2 = new PropertyDescriptor("DebugJAXPIncludeClass", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPIncludeClass", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug IncludeClass</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugJAXPIncludeLocation")) {
         var3 = "getDebugJAXPIncludeLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPIncludeLocation";
         }

         var2 = new PropertyDescriptor("DebugJAXPIncludeLocation", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPIncludeLocation", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug IncludeLocation</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugJAXPIncludeName")) {
         var3 = "getDebugJAXPIncludeName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPIncludeName";
         }

         var2 = new PropertyDescriptor("DebugJAXPIncludeName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPIncludeName", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug IncludeName</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("DebugJAXPIncludeTime")) {
         var3 = "getDebugJAXPIncludeTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPIncludeTime";
         }

         var2 = new PropertyDescriptor("DebugJAXPIncludeTime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPIncludeTime", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug IncludeTime</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugJAXPOutputStream")) {
         var3 = "getDebugJAXPOutputStream";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPOutputStream";
         }

         var2 = new PropertyDescriptor("DebugJAXPOutputStream", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPOutputStream", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug OutputStream</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJAXPUseShortClass")) {
         var3 = "getDebugJAXPUseShortClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJAXPUseShortClass";
         }

         var2 = new PropertyDescriptor("DebugJAXPUseShortClass", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJAXPUseShortClass", var2);
         var2.setValue("description", "<p>JAXP debugging option: Debug UseShortClass</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("DebugJDBCConn")) {
         var3 = "getDebugJDBCConn";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCConn";
         }

         var2 = new PropertyDescriptor("DebugJDBCConn", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCConn", var2);
         var2.setValue("description", "<p>Debug JDBC Connection setup/teardown processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCDriverLogging")) {
         var3 = "getDebugJDBCDriverLogging";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCDriverLogging";
         }

         var2 = new PropertyDescriptor("DebugJDBCDriverLogging", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCDriverLogging", var2);
         var2.setValue("description", "<p>Debug JDBC DriverLogging processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCInternal")) {
         var3 = "getDebugJDBCInternal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCInternal";
         }

         var2 = new PropertyDescriptor("DebugJDBCInternal", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCInternal", var2);
         var2.setValue("description", "<p>Debug JDBC Internal processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCONS")) {
         var3 = "getDebugJDBCONS";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCONS";
         }

         var2 = new PropertyDescriptor("DebugJDBCONS", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCONS", var2);
         var2.setValue("description", "<p>Debug ONS client </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCRAC")) {
         var3 = "getDebugJDBCRAC";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCRAC";
         }

         var2 = new PropertyDescriptor("DebugJDBCRAC", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCRAC", var2);
         var2.setValue("description", "<p>Debug Oracle RAC processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCREPLAY")) {
         var3 = "getDebugJDBCREPLAY";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCREPLAY";
         }

         var2 = new PropertyDescriptor("DebugJDBCREPLAY", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCREPLAY", var2);
         var2.setValue("description", "<p>Debug REPLAY client </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCRMI")) {
         var3 = "getDebugJDBCRMI";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCRMI";
         }

         var2 = new PropertyDescriptor("DebugJDBCRMI", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCRMI", var2);
         var2.setValue("description", "<p>Debug JDBC RMI processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCSQL")) {
         var3 = "getDebugJDBCSQL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCSQL";
         }

         var2 = new PropertyDescriptor("DebugJDBCSQL", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCSQL", var2);
         var2.setValue("description", "<p>Debug JDBC SQL processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJDBCUCP")) {
         var3 = "getDebugJDBCUCP";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJDBCUCP";
         }

         var2 = new PropertyDescriptor("DebugJDBCUCP", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJDBCUCP", var2);
         var2.setValue("description", "<p>Debug Oracle UCP processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSAME")) {
         var3 = "getDebugJMSAME";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSAME";
         }

         var2 = new PropertyDescriptor("DebugJMSAME", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSAME", var2);
         var2.setValue("description", "<p>Debug JMS AME processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSBackEnd")) {
         var3 = "getDebugJMSBackEnd";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSBackEnd";
         }

         var2 = new PropertyDescriptor("DebugJMSBackEnd", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSBackEnd", var2);
         var2.setValue("description", "<p>Debug JMSBackEnd processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSBoot")) {
         var3 = "getDebugJMSBoot";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSBoot";
         }

         var2 = new PropertyDescriptor("DebugJMSBoot", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSBoot", var2);
         var2.setValue("description", "<p>Debug JMS boot operations</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSCDS")) {
         var3 = "getDebugJMSCDS";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSCDS";
         }

         var2 = new PropertyDescriptor("DebugJMSCDS", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSCDS", var2);
         var2.setValue("description", "<p>Debug JMS CDS processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSCommon")) {
         var3 = "getDebugJMSCommon";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSCommon";
         }

         var2 = new PropertyDescriptor("DebugJMSCommon", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSCommon", var2);
         var2.setValue("description", "<p>Debug JMSCommon processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSConfig")) {
         var3 = "getDebugJMSConfig";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSConfig";
         }

         var2 = new PropertyDescriptor("DebugJMSConfig", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSConfig", var2);
         var2.setValue("description", "<p>Debug JMSConfig processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSDispatcher")) {
         var3 = "getDebugJMSDispatcher";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSDispatcher";
         }

         var2 = new PropertyDescriptor("DebugJMSDispatcher", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSDispatcher", var2);
         var2.setValue("description", "<p>Debug JMS Dispatcher processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSDistTopic")) {
         var3 = "getDebugJMSDistTopic";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSDistTopic";
         }

         var2 = new PropertyDescriptor("DebugJMSDistTopic", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSDistTopic", var2);
         var2.setValue("description", "<p>Debug JMS Distributed Topic processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSDurableSubscribers")) {
         var3 = "getDebugJMSDurableSubscribers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSDurableSubscribers";
         }

         var2 = new PropertyDescriptor("DebugJMSDurableSubscribers", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSDurableSubscribers", var2);
         var2.setValue("description", "<p>Debug JMS durable subscriber operations</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSFrontEnd")) {
         var3 = "getDebugJMSFrontEnd";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSFrontEnd";
         }

         var2 = new PropertyDescriptor("DebugJMSFrontEnd", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSFrontEnd", var2);
         var2.setValue("description", "<p>Debug JMSFrontEnd processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSJDBCScavengeOnFlush")) {
         var3 = "getDebugJMSJDBCScavengeOnFlush";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSJDBCScavengeOnFlush";
         }

         var2 = new PropertyDescriptor("DebugJMSJDBCScavengeOnFlush", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSJDBCScavengeOnFlush", var2);
         var2.setValue("description", "<p>Debug JMS JDBC store scavenge mode processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSLocking")) {
         var3 = "getDebugJMSLocking";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSLocking";
         }

         var2 = new PropertyDescriptor("DebugJMSLocking", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSLocking", var2);
         var2.setValue("description", "<p>Debug JMS Lock processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSMessagePath")) {
         var3 = "getDebugJMSMessagePath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSMessagePath";
         }

         var2 = new PropertyDescriptor("DebugJMSMessagePath", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSMessagePath", var2);
         var2.setValue("description", "<p>Debug JMS MessagePath processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSModule")) {
         var3 = "getDebugJMSModule";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSModule";
         }

         var2 = new PropertyDescriptor("DebugJMSModule", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSModule", var2);
         var2.setValue("description", "<p>Debug JMSModule deployment processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSPauseResume")) {
         var3 = "getDebugJMSPauseResume";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSPauseResume";
         }

         var2 = new PropertyDescriptor("DebugJMSPauseResume", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSPauseResume", var2);
         var2.setValue("description", "<p>Debug JMS Pause/Resume processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSSAF")) {
         var3 = "getDebugJMSSAF";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSSAF";
         }

         var2 = new PropertyDescriptor("DebugJMSSAF", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSSAF", var2);
         var2.setValue("description", "<p>Debug JMS SAF processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSStore")) {
         var3 = "getDebugJMSStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSStore";
         }

         var2 = new PropertyDescriptor("DebugJMSStore", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSStore", var2);
         var2.setValue("description", "<p>Debug JMS Store operations</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMST3Server")) {
         var3 = "getDebugJMST3Server";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMST3Server";
         }

         var2 = new PropertyDescriptor("DebugJMST3Server", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMST3Server", var2);
         var2.setValue("description", "<p>Debug DebugJMST3Server processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSWrappers")) {
         var3 = "getDebugJMSWrappers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSWrappers";
         }

         var2 = new PropertyDescriptor("DebugJMSWrappers", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSWrappers", var2);
         var2.setValue("description", "<p>Debug the pooling and wrapping of JMS connections, sessions, and other objects. This feature is enabled when a JMS connection factory, from any vendor, is used inside an EJB or a servlet using the \"resource-reference\" element in the deployment descriptor.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMSXA")) {
         var3 = "getDebugJMSXA";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMSXA";
         }

         var2 = new PropertyDescriptor("DebugJMSXA", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMSXA", var2);
         var2.setValue("description", "<p>Debug JMS XA processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMX")) {
         var3 = "getDebugJMX";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMX";
         }

         var2 = new PropertyDescriptor("DebugJMX", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMX", var2);
         var2.setValue("description", "<p>Debug JMX processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMXCompatibility")) {
         var3 = "getDebugJMXCompatibility";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMXCompatibility";
         }

         var2 = new PropertyDescriptor("DebugJMXCompatibility", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMXCompatibility", var2);
         var2.setValue("description", "<p>Debug JMX CompatibilityMBeanServer service processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMXCore")) {
         var3 = "getDebugJMXCore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMXCore";
         }

         var2 = new PropertyDescriptor("DebugJMXCore", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMXCore", var2);
         var2.setValue("description", "<p>Debug core JMX processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMXDomain")) {
         var3 = "getDebugJMXDomain";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMXDomain";
         }

         var2 = new PropertyDescriptor("DebugJMXDomain", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMXDomain", var2);
         var2.setValue("description", "<p>Debug JMX domain service processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMXEdit")) {
         var3 = "getDebugJMXEdit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMXEdit";
         }

         var2 = new PropertyDescriptor("DebugJMXEdit", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMXEdit", var2);
         var2.setValue("description", "<p>Debug JMX edit service processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJMXRuntime")) {
         var3 = "getDebugJMXRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJMXRuntime";
         }

         var2 = new PropertyDescriptor("DebugJMXRuntime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJMXRuntime", var2);
         var2.setValue("description", "<p>Debug JMX runtime service processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJNDI")) {
         var3 = "getDebugJNDI";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJNDI";
         }

         var2 = new PropertyDescriptor("DebugJNDI", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJNDI", var2);
         var2.setValue("description", "<p>Debug basic naming service machinery.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugJNDIFactories")) {
         var3 = "getDebugJNDIFactories";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJNDIFactories";
         }

         var2 = new PropertyDescriptor("DebugJNDIFactories", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJNDIFactories", var2);
         var2.setValue("description", "<p>Debug JNDI state and object factories.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugJNDIResolution")) {
         var3 = "getDebugJNDIResolution";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJNDIResolution";
         }

         var2 = new PropertyDescriptor("DebugJNDIResolution", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJNDIResolution", var2);
         var2.setValue("description", "<p>Debug naming service name resolution.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugJTA2PC")) {
         var3 = "getDebugJTA2PC";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTA2PC";
         }

         var2 = new PropertyDescriptor("DebugJTA2PC", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTA2PC", var2);
         var2.setValue("description", "<p>Debug JTA 2PC processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTA2PCStackTrace")) {
         var3 = "getDebugJTA2PCStackTrace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTA2PCStackTrace";
         }

         var2 = new PropertyDescriptor("DebugJTA2PCStackTrace", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTA2PCStackTrace", var2);
         var2.setValue("description", "<p>Detailed Debug of JTA 2PC processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAAPI")) {
         var3 = "getDebugJTAAPI";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAAPI";
         }

         var2 = new PropertyDescriptor("DebugJTAAPI", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAAPI", var2);
         var2.setValue("description", "<p>Debug JTA external API</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAGateway")) {
         var3 = "getDebugJTAGateway";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAGateway";
         }

         var2 = new PropertyDescriptor("DebugJTAGateway", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAGateway", var2);
         var2.setValue("description", "<p>Debug JTA imported transactions</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAGatewayStackTrace")) {
         var3 = "getDebugJTAGatewayStackTrace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAGatewayStackTrace";
         }

         var2 = new PropertyDescriptor("DebugJTAGatewayStackTrace", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAGatewayStackTrace", var2);
         var2.setValue("description", "<p>Detailed debug of JTA imported transactions</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAHealth")) {
         var3 = "getDebugJTAHealth";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAHealth";
         }

         var2 = new PropertyDescriptor("DebugJTAHealth", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAHealth", var2);
         var2.setValue("description", "<p>Debug JTA Health Monitoring</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAJDBC")) {
         var3 = "getDebugJTAJDBC";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAJDBC";
         }

         var2 = new PropertyDescriptor("DebugJTAJDBC", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAJDBC", var2);
         var2.setValue("description", "<p>Debug JTA JDBC processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTALLR")) {
         var3 = "getDebugJTALLR";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTALLR";
         }

         var2 = new PropertyDescriptor("DebugJTALLR", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTALLR", var2);
         var2.setValue("description", "<p>Debug JTA Logging Last Resource </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTALifecycle")) {
         var3 = "getDebugJTALifecycle";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTALifecycle";
         }

         var2 = new PropertyDescriptor("DebugJTALifecycle", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTALifecycle", var2);
         var2.setValue("description", "<p>Debug JTA ServerLifecycle</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAMigration")) {
         var3 = "getDebugJTAMigration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAMigration";
         }

         var2 = new PropertyDescriptor("DebugJTAMigration", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAMigration", var2);
         var2.setValue("description", "<p>Debug JTA TLOG Migration</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTANaming")) {
         var3 = "getDebugJTANaming";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTANaming";
         }

         var2 = new PropertyDescriptor("DebugJTANaming", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTANaming", var2);
         var2.setValue("description", "<p>Debug JTA naming</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTANamingStackTrace")) {
         var3 = "getDebugJTANamingStackTrace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTANamingStackTrace";
         }

         var2 = new PropertyDescriptor("DebugJTANamingStackTrace", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTANamingStackTrace", var2);
         var2.setValue("description", "<p>Detailed debug of JTA naming</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTANonXA")) {
         var3 = "getDebugJTANonXA";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTANonXA";
         }

         var2 = new PropertyDescriptor("DebugJTANonXA", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTANonXA", var2);
         var2.setValue("description", "<p>Debug JTA non-XA resources</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAPropagate")) {
         var3 = "getDebugJTAPropagate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAPropagate";
         }

         var2 = new PropertyDescriptor("DebugJTAPropagate", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAPropagate", var2);
         var2.setValue("description", "<p>Debug JTA transaction propagation</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTARMI")) {
         var3 = "getDebugJTARMI";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTARMI";
         }

         var2 = new PropertyDescriptor("DebugJTARMI", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTARMI", var2);
         var2.setValue("description", "<p>Debug JTA RMI processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTARecovery")) {
         var3 = "getDebugJTARecovery";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTARecovery";
         }

         var2 = new PropertyDescriptor("DebugJTARecovery", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTARecovery", var2);
         var2.setValue("description", "<p>Debug JTA Recovery processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTARecoveryStackTrace")) {
         var3 = "getDebugJTARecoveryStackTrace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTARecoveryStackTrace";
         }

         var2 = new PropertyDescriptor("DebugJTARecoveryStackTrace", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTARecoveryStackTrace", var2);
         var2.setValue("description", "<p>Detailed debug of JTA Recovery processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAResourceHealth")) {
         var3 = "getDebugJTAResourceHealth";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAResourceHealth";
         }

         var2 = new PropertyDescriptor("DebugJTAResourceHealth", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAResourceHealth", var2);
         var2.setValue("description", "<p>Debug JTA resource health</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAResourceName")) {
         var3 = "getDebugJTAResourceName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAResourceName";
         }

         var2 = new PropertyDescriptor("DebugJTAResourceName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAResourceName", var2);
         var2.setValue("description", "<p>Debug JTA Resource name filter</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTATLOG")) {
         var3 = "getDebugJTATLOG";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTATLOG";
         }

         var2 = new PropertyDescriptor("DebugJTATLOG", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTATLOG", var2);
         var2.setValue("description", "<p>Debug JTA transaction log processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTATransactionName")) {
         var3 = "getDebugJTATransactionName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTATransactionName";
         }

         var2 = new PropertyDescriptor("DebugJTATransactionName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTATransactionName", var2);
         var2.setValue("description", "<p>Debug JTA Transaction name filter processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAXA")) {
         var3 = "getDebugJTAXA";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAXA";
         }

         var2 = new PropertyDescriptor("DebugJTAXA", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAXA", var2);
         var2.setValue("description", "<p>Debug JTA XA resources</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJTAXAStackTrace")) {
         var3 = "getDebugJTAXAStackTrace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJTAXAStackTrace";
         }

         var2 = new PropertyDescriptor("DebugJTAXAStackTrace", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJTAXAStackTrace", var2);
         var2.setValue("description", "<p>Detailed Debug of JTA XA processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaDataCache")) {
         var3 = "getDebugJpaDataCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaDataCache";
         }

         var2 = new PropertyDescriptor("DebugJpaDataCache", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaDataCache", var2);
         var2.setValue("description", "<p>Debug JPA data cache</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaEnhance")) {
         var3 = "getDebugJpaEnhance";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaEnhance";
         }

         var2 = new PropertyDescriptor("DebugJpaEnhance", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaEnhance", var2);
         var2.setValue("description", "<p>Debug JPA post-compilation</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaJdbcJdbc")) {
         var3 = "getDebugJpaJdbcJdbc";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaJdbcJdbc";
         }

         var2 = new PropertyDescriptor("DebugJpaJdbcJdbc", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaJdbcJdbc", var2);
         var2.setValue("description", "<p>Debug JPA RDBMS JDBC interaction</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaJdbcSchema")) {
         var3 = "getDebugJpaJdbcSchema";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaJdbcSchema";
         }

         var2 = new PropertyDescriptor("DebugJpaJdbcSchema", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaJdbcSchema", var2);
         var2.setValue("description", "<p>Debug JPA RDBMS schema manipulation</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaJdbcSql")) {
         var3 = "getDebugJpaJdbcSql";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaJdbcSql";
         }

         var2 = new PropertyDescriptor("DebugJpaJdbcSql", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaJdbcSql", var2);
         var2.setValue("description", "<p>Debug JPA RDBMS SQL interaction</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaManage")) {
         var3 = "getDebugJpaManage";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaManage";
         }

         var2 = new PropertyDescriptor("DebugJpaManage", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaManage", var2);
         var2.setValue("description", "<p>Debug JPA management and monitoring</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaMetaData")) {
         var3 = "getDebugJpaMetaData";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaMetaData";
         }

         var2 = new PropertyDescriptor("DebugJpaMetaData", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaMetaData", var2);
         var2.setValue("description", "<p>Debug JPA MetaData processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaProfile")) {
         var3 = "getDebugJpaProfile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaProfile";
         }

         var2 = new PropertyDescriptor("DebugJpaProfile", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaProfile", var2);
         var2.setValue("description", "<p>Debug JPA profiling</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaQuery")) {
         var3 = "getDebugJpaQuery";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaQuery";
         }

         var2 = new PropertyDescriptor("DebugJpaQuery", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaQuery", var2);
         var2.setValue("description", "<p>Debug JPA Query processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaRuntime")) {
         var3 = "getDebugJpaRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaRuntime";
         }

         var2 = new PropertyDescriptor("DebugJpaRuntime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaRuntime", var2);
         var2.setValue("description", "<p>Debug JPA runtime diagnostics</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugJpaTool")) {
         var3 = "getDebugJpaTool";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugJpaTool";
         }

         var2 = new PropertyDescriptor("DebugJpaTool", ServerDebugMBean.class, var3, var4);
         var1.put("DebugJpaTool", var2);
         var2.setValue("description", "<p>Debug JPA tools</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugLeaderElection")) {
         var3 = "getDebugLeaderElection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugLeaderElection";
         }

         var2 = new PropertyDescriptor("DebugLeaderElection", ServerDebugMBean.class, var3, var4);
         var1.put("DebugLeaderElection", var2);
         var2.setValue("description", "<p>Debug the cluster leader election messages.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugLibraries")) {
         var3 = "getDebugLibraries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugLibraries";
         }

         var2 = new PropertyDescriptor("DebugLibraries", ServerDebugMBean.class, var3, var4);
         var1.put("DebugLibraries", var2);
         var2.setValue("description", "<p>Debug application library processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugLoggingConfiguration")) {
         var3 = "getDebugLoggingConfiguration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugLoggingConfiguration";
         }

         var2 = new PropertyDescriptor("DebugLoggingConfiguration", ServerDebugMBean.class, var3, var4);
         var1.put("DebugLoggingConfiguration", var2);
         var2.setValue("description", "<p>Debug log configuration processing</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugManagementServicesResource")) {
         var3 = "getDebugManagementServicesResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugManagementServicesResource";
         }

         var2 = new PropertyDescriptor("DebugManagementServicesResource", ServerDebugMBean.class, var3, var4);
         var1.put("DebugManagementServicesResource", var2);
         var2.setValue("description", "<p>Debug Management Services Resources</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMaskCriterias")) {
         var3 = "getDebugMaskCriterias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMaskCriterias";
         }

         var2 = new PropertyDescriptor("DebugMaskCriterias", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMaskCriterias", var2);
         var2.setValue("description", "<p>Indicates the dye mask for criteria used to determine whether the debug will be emitted.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingBridgeDumpToConsole")) {
         var3 = "getDebugMessagingBridgeDumpToConsole";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingBridgeDumpToConsole";
         }

         var2 = new PropertyDescriptor("DebugMessagingBridgeDumpToConsole", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingBridgeDumpToConsole", var2);
         var2.setValue("description", "<p>Not Used</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingBridgeDumpToLog")) {
         var3 = "getDebugMessagingBridgeDumpToLog";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingBridgeDumpToLog";
         }

         var2 = new PropertyDescriptor("DebugMessagingBridgeDumpToLog", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingBridgeDumpToLog", var2);
         var2.setValue("description", "<p>Not Used</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingBridgeRuntime")) {
         var3 = "getDebugMessagingBridgeRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingBridgeRuntime";
         }

         var2 = new PropertyDescriptor("DebugMessagingBridgeRuntime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingBridgeRuntime", var2);
         var2.setValue("description", "<p>Debug Messaging Bridge runtime processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingBridgeRuntimeVerbose")) {
         var3 = "getDebugMessagingBridgeRuntimeVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingBridgeRuntimeVerbose";
         }

         var2 = new PropertyDescriptor("DebugMessagingBridgeRuntimeVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingBridgeRuntimeVerbose", var2);
         var2.setValue("description", "<p>Detailed debug of Messaging Bridge runtime processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingBridgeStartup")) {
         var3 = "getDebugMessagingBridgeStartup";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingBridgeStartup";
         }

         var2 = new PropertyDescriptor("DebugMessagingBridgeStartup", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingBridgeStartup", var2);
         var2.setValue("description", "<p>Debug Messaging Bridge start up processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingKernel")) {
         var3 = "getDebugMessagingKernel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingKernel";
         }

         var2 = new PropertyDescriptor("DebugMessagingKernel", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingKernel", var2);
         var2.setValue("description", "<p>Debugging for the messaging kernel. The messaging kernel is low- level messaging code that is used by the JMS and store and forward subsystems.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugMessagingKernelBoot")) {
         var3 = "getDebugMessagingKernelBoot";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugMessagingKernelBoot";
         }

         var2 = new PropertyDescriptor("DebugMessagingKernelBoot", ServerDebugMBean.class, var3, var4);
         var1.put("DebugMessagingKernelBoot", var2);
         var2.setValue("description", "<p>Debugging for the messaging kernel as the server is being rebooted. This provides detailed information on every persistent message that is recovered.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugPathSvc")) {
         var3 = "getDebugPathSvc";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugPathSvc";
         }

         var2 = new PropertyDescriptor("DebugPathSvc", ServerDebugMBean.class, var3, var4);
         var1.put("DebugPathSvc", var2);
         var2.setValue("description", "<p>Debug the Path Service</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugPathSvcVerbose")) {
         var3 = "getDebugPathSvcVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugPathSvcVerbose";
         }

         var2 = new PropertyDescriptor("DebugPathSvcVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("DebugPathSvcVerbose", var2);
         var2.setValue("description", "<p>Detailed debug for the Path Service</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRA")) {
         var3 = "getDebugRA";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRA";
         }

         var2 = new PropertyDescriptor("DebugRA", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRA", var2);
         var2.setValue("description", "<p>Debug Resource Adapter XA general/top-level processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAClassloader")) {
         var3 = "getDebugRAClassloader";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAClassloader";
         }

         var2 = new PropertyDescriptor("DebugRAClassloader", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAClassloader", var2);
         var2.setValue("description", "<p>Debug Resource Adapter class loading</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAConnEvents")) {
         var3 = "getDebugRAConnEvents";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAConnEvents";
         }

         var2 = new PropertyDescriptor("DebugRAConnEvents", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAConnEvents", var2);
         var2.setValue("description", "<p>Debug Resource Adapter connection event processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAConnections")) {
         var3 = "getDebugRAConnections";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAConnections";
         }

         var2 = new PropertyDescriptor("DebugRAConnections", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAConnections", var2);
         var2.setValue("description", "<p>Debug Resource Adapter outbound connection operations (get, close, associate, disassociate, ping)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRADeployment")) {
         var3 = "getDebugRADeployment";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRADeployment";
         }

         var2 = new PropertyDescriptor("DebugRADeployment", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRADeployment", var2);
         var2.setValue("description", "<p>Debug Resource Adapter (un)deploy, security id settings</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRALifecycle")) {
         var3 = "getDebugRALifecycle";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRALifecycle";
         }

         var2 = new PropertyDescriptor("DebugRALifecycle", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRALifecycle", var2);
         var2.setValue("description", "<p>Debug ResourceAdapter Lifecycle processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRALocalOut")) {
         var3 = "getDebugRALocalOut";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRALocalOut";
         }

         var2 = new PropertyDescriptor("DebugRALocalOut", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRALocalOut", var2);
         var2.setValue("description", "<p>Debug Resource Adapter local tx outgoing message processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAParsing")) {
         var3 = "getDebugRAParsing";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAParsing";
         }

         var2 = new PropertyDescriptor("DebugRAParsing", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAParsing", var2);
         var2.setValue("description", "<p>Debug Resource Adapter descriptor parsing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAPoolVerbose")) {
         var3 = "getDebugRAPoolVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAPoolVerbose";
         }

         var2 = new PropertyDescriptor("DebugRAPoolVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAPoolVerbose", var2);
         var2.setValue("description", "<p>Debug J2EE Resource Adapter pool management (size management) processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAPooling")) {
         var3 = "getDebugRAPooling";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAPooling";
         }

         var2 = new PropertyDescriptor("DebugRAPooling", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAPooling", var2);
         var2.setValue("description", "<p>Debug Resource Adapter operations on a connection pool (proxy testing)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRASecurityCtx")) {
         var3 = "getDebugRASecurityCtx";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRASecurityCtx";
         }

         var2 = new PropertyDescriptor("DebugRASecurityCtx", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRASecurityCtx", var2);
         var2.setValue("description", "<p>Debug setup of resource ref processing (container and application managed security set by calling application components)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAWork")) {
         var3 = "getDebugRAWork";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAWork";
         }

         var2 = new PropertyDescriptor("DebugRAWork", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAWork", var2);
         var2.setValue("description", "<p>Debug Resource Adapter Work submission and cancel processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAWorkEvents")) {
         var3 = "getDebugRAWorkEvents";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAWorkEvents";
         }

         var2 = new PropertyDescriptor("DebugRAWorkEvents", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAWorkEvents", var2);
         var2.setValue("description", "<p>Debug Resource Adapter work event processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAXAin")) {
         var3 = "getDebugRAXAin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAXAin";
         }

         var2 = new PropertyDescriptor("DebugRAXAin", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAXAin", var2);
         var2.setValue("description", "<p>Debug Resource Adapter XA incoming message processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAXAout")) {
         var3 = "getDebugRAXAout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAXAout";
         }

         var2 = new PropertyDescriptor("DebugRAXAout", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAXAout", var2);
         var2.setValue("description", "<p>Debug Resource Adapter XA outgoing message processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugRAXAwork")) {
         var3 = "getDebugRAXAwork";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugRAXAwork";
         }

         var2 = new PropertyDescriptor("DebugRAXAwork", ServerDebugMBean.class, var3, var4);
         var1.put("DebugRAXAwork", var2);
         var2.setValue("description", "<p>Debug Resource Adapter XA Work request processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugReplication")) {
         var3 = "getDebugReplication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugReplication";
         }

         var2 = new PropertyDescriptor("DebugReplication", ServerDebugMBean.class, var3, var4);
         var1.put("DebugReplication", var2);
         var2.setValue("description", "<p>Debug cluster replication information.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugReplicationDetails")) {
         var3 = "getDebugReplicationDetails";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugReplicationDetails";
         }

         var2 = new PropertyDescriptor("DebugReplicationDetails", ServerDebugMBean.class, var3, var4);
         var1.put("DebugReplicationDetails", var2);
         var2.setValue("description", "<p>Debug low-level cluster replication information.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFAdmin")) {
         var3 = "getDebugSAFAdmin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFAdmin";
         }

         var2 = new PropertyDescriptor("DebugSAFAdmin", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFAdmin", var2);
         var2.setValue("description", "<p>Debug Messaging SAF Admin.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFLifeCycle")) {
         var3 = "getDebugSAFLifeCycle";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFLifeCycle";
         }

         var2 = new PropertyDescriptor("DebugSAFLifeCycle", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFLifeCycle", var2);
         var2.setValue("description", "<p>Debug Messaging SAF Lifecycle.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFManager")) {
         var3 = "getDebugSAFManager";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFManager";
         }

         var2 = new PropertyDescriptor("DebugSAFManager", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFManager", var2);
         var2.setValue("description", "<p>Debug Messaging SAF Manager.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFMessagePath")) {
         var3 = "getDebugSAFMessagePath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFMessagePath";
         }

         var2 = new PropertyDescriptor("DebugSAFMessagePath", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFMessagePath", var2);
         var2.setValue("description", "<p>Debug Messaging SAF MessagePath.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFReceivingAgent")) {
         var3 = "getDebugSAFReceivingAgent";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFReceivingAgent";
         }

         var2 = new PropertyDescriptor("DebugSAFReceivingAgent", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFReceivingAgent", var2);
         var2.setValue("description", "<p>Debug Messaging SAF ReceivingAgent.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFSendingAgent")) {
         var3 = "getDebugSAFSendingAgent";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFSendingAgent";
         }

         var2 = new PropertyDescriptor("DebugSAFSendingAgent", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFSendingAgent", var2);
         var2.setValue("description", "<p>Debug Messaging SAF SendingAgent.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFStore")) {
         var3 = "getDebugSAFStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFStore";
         }

         var2 = new PropertyDescriptor("DebugSAFStore", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFStore", var2);
         var2.setValue("description", "<p>Debug Messaging SAF Store.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFTransport")) {
         var3 = "getDebugSAFTransport";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFTransport";
         }

         var2 = new PropertyDescriptor("DebugSAFTransport", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFTransport", var2);
         var2.setValue("description", "<p>Debug Messaging SAF Transport.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSAFVerbose")) {
         var3 = "getDebugSAFVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSAFVerbose";
         }

         var2 = new PropertyDescriptor("DebugSAFVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSAFVerbose", var2);
         var2.setValue("description", "<p>Detailed debug of Messaging SAF.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSNMPAgent")) {
         var3 = "getDebugSNMPAgent";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSNMPAgent";
         }

         var2 = new PropertyDescriptor("DebugSNMPAgent", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSNMPAgent", var2);
         var2.setValue("description", "<p>Debug the SNMP agent framework.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSNMPExtensionProvider")) {
         var3 = "getDebugSNMPExtensionProvider";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSNMPExtensionProvider";
         }

         var2 = new PropertyDescriptor("DebugSNMPExtensionProvider", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSNMPExtensionProvider", var2);
         var2.setValue("description", "<p> The debug attribute to enable or disable the SNMP Agent Extension Provider discovery and initialization. </p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSNMPProtocolTCP")) {
         var3 = "getDebugSNMPProtocolTCP";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSNMPProtocolTCP";
         }

         var2 = new PropertyDescriptor("DebugSNMPProtocolTCP", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSNMPProtocolTCP", var2);
         var2.setValue("description", "<p>Debug the SNMP TCP protocol handler.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSNMPToolkit")) {
         var3 = "getDebugSNMPToolkit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSNMPToolkit";
         }

         var2 = new PropertyDescriptor("DebugSNMPToolkit", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSNMPToolkit", var2);
         var2.setValue("description", "<p>Debug the SNMP vendor toolkit implementation.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugScaContainer")) {
         var3 = "getDebugScaContainer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugScaContainer";
         }

         var2 = new PropertyDescriptor("DebugScaContainer", ServerDebugMBean.class, var3, var4);
         var1.put("DebugScaContainer", var2);
         var2.setValue("description", "<p>Debug Weblogic SCA Container </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurity")) {
         var3 = "getDebugSecurity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurity";
         }

         var2 = new PropertyDescriptor("DebugSecurity", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurity", var2);
         var2.setValue("description", "<p>Debug Security service manager</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityAdjudicator")) {
         var3 = "getDebugSecurityAdjudicator";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityAdjudicator";
         }

         var2 = new PropertyDescriptor("DebugSecurityAdjudicator", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityAdjudicator", var2);
         var2.setValue("description", "<p>Debug Security Framework Adjudication processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityAtn")) {
         var3 = "getDebugSecurityAtn";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityAtn";
         }

         var2 = new PropertyDescriptor("DebugSecurityAtn", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityAtn", var2);
         var2.setValue("description", "<p>Debug Security Framework Atn processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityAtz")) {
         var3 = "getDebugSecurityAtz";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityAtz";
         }

         var2 = new PropertyDescriptor("DebugSecurityAtz", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityAtz", var2);
         var2.setValue("description", "<p>Debug Security Framework Atz processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityAuditor")) {
         var3 = "getDebugSecurityAuditor";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityAuditor";
         }

         var2 = new PropertyDescriptor("DebugSecurityAuditor", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityAuditor", var2);
         var2.setValue("description", "<p>Debug Security Framework Auditor processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityCertPath")) {
         var3 = "getDebugSecurityCertPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityCertPath";
         }

         var2 = new PropertyDescriptor("DebugSecurityCertPath", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityCertPath", var2);
         var2.setValue("description", "<p>Debug Security Framework CertPath processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityCredMap")) {
         var3 = "getDebugSecurityCredMap";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityCredMap";
         }

         var2 = new PropertyDescriptor("DebugSecurityCredMap", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityCredMap", var2);
         var2.setValue("description", "<p>Debug Security Framework Credential Mapper processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityEEngine")) {
         var3 = "getDebugSecurityEEngine";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityEEngine";
         }

         var2 = new PropertyDescriptor("DebugSecurityEEngine", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityEEngine", var2);
         var2.setValue("description", "<p>Debug Security Framework Entitlements Engine processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityEncryptionService")) {
         var3 = "getDebugSecurityEncryptionService";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityEncryptionService";
         }

         var2 = new PropertyDescriptor("DebugSecurityEncryptionService", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityEncryptionService", var2);
         var2.setValue("description", "<p>Debug Security Framework Encryption Service processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityJACC")) {
         var3 = "getDebugSecurityJACC";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityJACC";
         }

         var2 = new PropertyDescriptor("DebugSecurityJACC", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityJACC", var2);
         var2.setValue("description", "<p>Debug Security Framework JACC processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityJACCNonPolicy")) {
         var3 = "getDebugSecurityJACCNonPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityJACCNonPolicy";
         }

         var2 = new PropertyDescriptor("DebugSecurityJACCNonPolicy", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityJACCNonPolicy", var2);
         var2.setValue("description", "<p>Debug Security Framework JACC nonPolicy processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityJACCPolicy")) {
         var3 = "getDebugSecurityJACCPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityJACCPolicy";
         }

         var2 = new PropertyDescriptor("DebugSecurityJACCPolicy", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityJACCPolicy", var2);
         var2.setValue("description", "<p>Debug Security Framework JACC Policy processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityKeyStore")) {
         var3 = "getDebugSecurityKeyStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityKeyStore";
         }

         var2 = new PropertyDescriptor("DebugSecurityKeyStore", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityKeyStore", var2);
         var2.setValue("description", "<p>Debug Security Framework KeyStore processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityPasswordPolicy")) {
         var3 = "getDebugSecurityPasswordPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityPasswordPolicy";
         }

         var2 = new PropertyDescriptor("DebugSecurityPasswordPolicy", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityPasswordPolicy", var2);
         var2.setValue("description", "<p>Debug Security Password Guessing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityPredicate")) {
         var3 = "getDebugSecurityPredicate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityPredicate";
         }

         var2 = new PropertyDescriptor("DebugSecurityPredicate", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityPredicate", var2);
         var2.setValue("description", "<p>Debug Security Framework predicate processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityRealm")) {
         var3 = "getDebugSecurityRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityRealm";
         }

         var2 = new PropertyDescriptor("DebugSecurityRealm", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityRealm", var2);
         var2.setValue("description", "<p>Debug Security Realm processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityRoleMap")) {
         var3 = "getDebugSecurityRoleMap";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityRoleMap";
         }

         var2 = new PropertyDescriptor("DebugSecurityRoleMap", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityRoleMap", var2);
         var2.setValue("description", "<p>Debug Security Framework Role Mapping</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAML2Atn")) {
         var3 = "getDebugSecuritySAML2Atn";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAML2Atn";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAML2Atn", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAML2Atn", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML2 Provider atn processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAML2CredMap")) {
         var3 = "getDebugSecuritySAML2CredMap";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAML2CredMap";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAML2CredMap", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAML2CredMap", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML2 Provider credential mapper processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAML2Lib")) {
         var3 = "getDebugSecuritySAML2Lib";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAML2Lib";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAML2Lib", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAML2Lib", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML2 library processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAML2Service")) {
         var3 = "getDebugSecuritySAML2Service";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAML2Service";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAML2Service", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAML2Service", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML2 SSO profile services</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAMLAtn")) {
         var3 = "getDebugSecuritySAMLAtn";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAMLAtn";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAMLAtn", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAMLAtn", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML Provider atn processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAMLCredMap")) {
         var3 = "getDebugSecuritySAMLCredMap";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAMLCredMap";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAMLCredMap", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAMLCredMap", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML Provider credential mapper processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAMLLib")) {
         var3 = "getDebugSecuritySAMLLib";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAMLLib";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAMLLib", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAMLLib", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML library processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySAMLService")) {
         var3 = "getDebugSecuritySAMLService";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySAMLService";
         }

         var2 = new PropertyDescriptor("DebugSecuritySAMLService", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySAMLService", var2);
         var2.setValue("description", "<p>Debug Security Framework SAML SSO profile services</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySSL")) {
         var3 = "getDebugSecuritySSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySSL";
         }

         var2 = new PropertyDescriptor("DebugSecuritySSL", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySSL", var2);
         var2.setValue("description", "<p>Debug Security SSL and TLS processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecuritySSLEaten")) {
         var3 = "getDebugSecuritySSLEaten";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecuritySSLEaten";
         }

         var2 = new PropertyDescriptor("DebugSecuritySSLEaten", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecuritySSLEaten", var2);
         var2.setValue("description", "<p>Debug Security SSL and TLS exception processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityService")) {
         var3 = "getDebugSecurityService";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityService";
         }

         var2 = new PropertyDescriptor("DebugSecurityService", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityService", var2);
         var2.setValue("description", "<p>Debug Security Service</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSecurityUserLockout")) {
         var3 = "getDebugSecurityUserLockout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSecurityUserLockout";
         }

         var2 = new PropertyDescriptor("DebugSecurityUserLockout", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSecurityUserLockout", var2);
         var2.setValue("description", "<p>Debug Security User Lockout processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugSelfTuning")) {
         var3 = "getDebugSelfTuning";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugSelfTuning";
         }

         var2 = new PropertyDescriptor("DebugSelfTuning", ServerDebugMBean.class, var3, var4);
         var1.put("DebugSelfTuning", var2);
         var2.setValue("description", "<p>Debug WorkManager self-tuning processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugServerLifeCycle")) {
         var3 = "getDebugServerLifeCycle";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugServerLifeCycle";
         }

         var2 = new PropertyDescriptor("DebugServerLifeCycle", ServerDebugMBean.class, var3, var4);
         var1.put("DebugServerLifeCycle", var2);
         var2.setValue("description", "<p>Debug Server ServerLifeCycle processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugServerMigration")) {
         var3 = "getDebugServerMigration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugServerMigration";
         }

         var2 = new PropertyDescriptor("DebugServerMigration", ServerDebugMBean.class, var3, var4);
         var1.put("DebugServerMigration", var2);
         var2.setValue("description", "<p>Debug low-level Server Migration processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugServerStartStatistics")) {
         var3 = "getDebugServerStartStatistics";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugServerStartStatistics";
         }

         var2 = new PropertyDescriptor("DebugServerStartStatistics", ServerDebugMBean.class, var3, var4);
         var1.put("DebugServerStartStatistics", var2);
         var2.setValue("description", "<p> If statistics about server start will be logged after the running message.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreAdmin")) {
         var3 = "getDebugStoreAdmin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreAdmin";
         }

         var2 = new PropertyDescriptor("DebugStoreAdmin", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreAdmin", var2);
         var2.setValue("description", "<p>Debug the persistent store's administration code. This will produce debug events when instances of the store are created and deleted, and when the server is booted.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreIOLogical")) {
         var3 = "getDebugStoreIOLogical";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreIOLogical";
         }

         var2 = new PropertyDescriptor("DebugStoreIOLogical", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreIOLogical", var2);
         var2.setValue("description", "<p>Debug persistent store high-level logical operations, such as read, write, delete, and update. Multiple logical operations may occur in a single physical operation.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreIOLogicalBoot")) {
         var3 = "getDebugStoreIOLogicalBoot";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreIOLogicalBoot";
         }

         var2 = new PropertyDescriptor("DebugStoreIOLogicalBoot", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreIOLogicalBoot", var2);
         var2.setValue("description", "<p>Debug persistent store logical boot operations (lists all recovered records).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreIOPhysical")) {
         var3 = "getDebugStoreIOPhysical";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreIOPhysical";
         }

         var2 = new PropertyDescriptor("DebugStoreIOPhysical", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreIOPhysical", var2);
         var2.setValue("description", "<p>Debugging for persistent store low-level physical operations which typically directly correspond to file or JDBC operations. Multiple logical operations may occur in a single physical operation.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreIOPhysicalVerbose")) {
         var3 = "getDebugStoreIOPhysicalVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreIOPhysicalVerbose";
         }

         var2 = new PropertyDescriptor("DebugStoreIOPhysicalVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreIOPhysicalVerbose", var2);
         var2.setValue("description", "<p>Detailed debug for persistent store low-level physical operations which typically directly correspond to file or JDBC operations.</p> <p>This will produce a great deal of debugging for every disk I/O performed by the store. Multiple logical operations may occur in a single physical operation.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreXA")) {
         var3 = "getDebugStoreXA";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreXA";
         }

         var2 = new PropertyDescriptor("DebugStoreXA", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreXA", var2);
         var2.setValue("description", "<p>Debug persistent store resource manager transaction activity, includes tracing for the related operations of layered subsystems (such as JMS).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugStoreXAVerbose")) {
         var3 = "getDebugStoreXAVerbose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugStoreXAVerbose";
         }

         var2 = new PropertyDescriptor("DebugStoreXAVerbose", ServerDebugMBean.class, var3, var4);
         var1.put("DebugStoreXAVerbose", var2);
         var2.setValue("description", "<p>Detailed debug of persistent store resource manager transaction activity, includes tracing for the related operations of layered subsystems (such as JMS).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugTunnelingConnection")) {
         var3 = "getDebugTunnelingConnection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugTunnelingConnection";
         }

         var2 = new PropertyDescriptor("DebugTunnelingConnection", ServerDebugMBean.class, var3, var4);
         var1.put("DebugTunnelingConnection", var2);
         var2.setValue("description", "<p>Debug HTTP tunneling connection open/close processing.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugTunnelingConnectionTimeout")) {
         var3 = "getDebugTunnelingConnectionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugTunnelingConnectionTimeout";
         }

         var2 = new PropertyDescriptor("DebugTunnelingConnectionTimeout", ServerDebugMBean.class, var3, var4);
         var1.put("DebugTunnelingConnectionTimeout", var2);
         var2.setValue("description", "<p>Debug HTTP tunneling connection timed out processing.</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugURLResolution")) {
         var3 = "getDebugURLResolution";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugURLResolution";
         }

         var2 = new PropertyDescriptor("DebugURLResolution", ServerDebugMBean.class, var3, var4);
         var1.put("DebugURLResolution", var2);
         var2.setValue("description", "<p>Debug URL resolution for incoming http requests</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWANReplicationDetails")) {
         var3 = "getDebugWANReplicationDetails";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWANReplicationDetails";
         }

         var2 = new PropertyDescriptor("DebugWANReplicationDetails", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWANReplicationDetails", var2);
         var2.setValue("description", "<p>Debug low-level wan replication processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCConfig")) {
         var3 = "getDebugWTCConfig";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCConfig";
         }

         var2 = new PropertyDescriptor("DebugWTCConfig", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCConfig", var2);
         var2.setValue("description", "<p>Debug WTC configuration processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCCorbaEx")) {
         var3 = "getDebugWTCCorbaEx";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCCorbaEx";
         }

         var2 = new PropertyDescriptor("DebugWTCCorbaEx", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCCorbaEx", var2);
         var2.setValue("description", "<p>Debug WTC corba execution</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCGwtEx")) {
         var3 = "getDebugWTCGwtEx";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCGwtEx";
         }

         var2 = new PropertyDescriptor("DebugWTCGwtEx", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCGwtEx", var2);
         var2.setValue("description", "<p>Debug WTC gwt execution</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCJatmiEx")) {
         var3 = "getDebugWTCJatmiEx";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCJatmiEx";
         }

         var2 = new PropertyDescriptor("DebugWTCJatmiEx", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCJatmiEx", var2);
         var2.setValue("description", "<p>Debug WTC jatmi execution</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCTDomPdu")) {
         var3 = "getDebugWTCTDomPdu";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCTDomPdu";
         }

         var2 = new PropertyDescriptor("DebugWTCTDomPdu", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCTDomPdu", var2);
         var2.setValue("description", "<p>Debug WTC XATMI Message processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCUData")) {
         var3 = "getDebugWTCUData";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCUData";
         }

         var2 = new PropertyDescriptor("DebugWTCUData", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCUData", var2);
         var2.setValue("description", "<p>Debug WTC user data processing</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWTCtBridgeEx")) {
         var3 = "getDebugWTCtBridgeEx";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWTCtBridgeEx";
         }

         var2 = new PropertyDescriptor("DebugWTCtBridgeEx", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWTCtBridgeEx", var2);
         var2.setValue("description", "<p>Debug WTC tBridge execution</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWebAppIdentityAssertion")) {
         var3 = "getDebugWebAppIdentityAssertion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWebAppIdentityAssertion";
         }

         var2 = new PropertyDescriptor("DebugWebAppIdentityAssertion", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWebAppIdentityAssertion", var2);
         var2.setValue("description", "<p>Debug identity assertion flow when identity assertion occurs in the webapp container.</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWebAppModule")) {
         var3 = "getDebugWebAppModule";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWebAppModule";
         }

         var2 = new PropertyDescriptor("DebugWebAppModule", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWebAppModule", var2);
         var2.setValue("description", "<p>Debug WebApp Module deployment callbacks</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugWebAppSecurity")) {
         var3 = "getDebugWebAppSecurity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugWebAppSecurity";
         }

         var2 = new PropertyDescriptor("DebugWebAppSecurity", ServerDebugMBean.class, var3, var4);
         var1.put("DebugWebAppSecurity", var2);
         var2.setValue("description", "<p>Debug webapp security</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugXMLEntityCacheDebugLevel")) {
         var3 = "getDebugXMLEntityCacheDebugLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheDebugLevel";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheDebugLevel", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheDebugLevel", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug level</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalMax", new Integer(3));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("DebugXMLEntityCacheDebugName")) {
         var3 = "getDebugXMLEntityCacheDebugName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheDebugName";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheDebugName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheDebugName", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug name</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugXMLEntityCacheIncludeClass")) {
         var3 = "getDebugXMLEntityCacheIncludeClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheIncludeClass";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheIncludeClass", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheIncludeClass", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug IncludeClass</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugXMLEntityCacheIncludeLocation")) {
         var3 = "getDebugXMLEntityCacheIncludeLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheIncludeLocation";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheIncludeLocation", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheIncludeLocation", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug IncludeLocation</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugXMLEntityCacheIncludeName")) {
         var3 = "getDebugXMLEntityCacheIncludeName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheIncludeName";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheIncludeName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheIncludeName", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug IncludeName</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("DebugXMLEntityCacheIncludeTime")) {
         var3 = "getDebugXMLEntityCacheIncludeTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheIncludeTime";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheIncludeTime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheIncludeTime", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug IncludeTime</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugXMLEntityCacheOutputStream")) {
         var3 = "getDebugXMLEntityCacheOutputStream";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheOutputStream";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheOutputStream", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheOutputStream", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug OutputStream</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugXMLEntityCacheUseShortClass")) {
         var3 = "getDebugXMLEntityCacheUseShortClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLEntityCacheUseShortClass";
         }

         var2 = new PropertyDescriptor("DebugXMLEntityCacheUseShortClass", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLEntityCacheUseShortClass", var2);
         var2.setValue("description", "<p>XMLEntityCache debugging option: Debug UseShortClass</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("DebugXMLRegistryDebugLevel")) {
         var3 = "getDebugXMLRegistryDebugLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryDebugLevel";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryDebugLevel", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryDebugLevel", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug levels</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalMax", new Integer(3));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("DebugXMLRegistryDebugName")) {
         var3 = "getDebugXMLRegistryDebugName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryDebugName";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryDebugName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryDebugName", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug name</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("legalNull", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugXMLRegistryIncludeClass")) {
         var3 = "getDebugXMLRegistryIncludeClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryIncludeClass";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryIncludeClass", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryIncludeClass", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug IncludeClass</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugXMLRegistryIncludeLocation")) {
         var3 = "getDebugXMLRegistryIncludeLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryIncludeLocation";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryIncludeLocation", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryIncludeLocation", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug IncludeLocation</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugXMLRegistryIncludeName")) {
         var3 = "getDebugXMLRegistryIncludeName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryIncludeName";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryIncludeName", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryIncludeName", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug IncludeName</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("DebugXMLRegistryIncludeTime")) {
         var3 = "getDebugXMLRegistryIncludeTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryIncludeTime";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryIncludeTime", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryIncludeTime", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug IncludeTime</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("DebugXMLRegistryOutputStream")) {
         var3 = "getDebugXMLRegistryOutputStream";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryOutputStream";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryOutputStream", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryOutputStream", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug OutputStream</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugXMLRegistryUseShortClass")) {
         var3 = "getDebugXMLRegistryUseShortClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugXMLRegistryUseShortClass";
         }

         var2 = new PropertyDescriptor("DebugXMLRegistryUseShortClass", ServerDebugMBean.class, var3, var4);
         var1.put("DebugXMLRegistryUseShortClass", var2);
         var2.setValue("description", "<p>XML Registry debugging option: Debug UseShortClass</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("DefaultStore")) {
         var3 = "getDefaultStore";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultStore";
         }

         var2 = new PropertyDescriptor("DefaultStore", ServerDebugMBean.class, var3, var4);
         var1.put("DefaultStore", var2);
         var2.setValue("description", "<p>Gets the fastswap DefaultStore debug attribute of ServerDebugMBean</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DiagnosticContextDebugMode")) {
         var3 = "getDiagnosticContextDebugMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDiagnosticContextDebugMode";
         }

         var2 = new PropertyDescriptor("DiagnosticContextDebugMode", ServerDebugMBean.class, var3, var4);
         var1.put("DiagnosticContextDebugMode", var2);
         var2.setValue("description", "<p>Indicates whether or not context based debugging is enabled.</p> ");
         setPropertyDescriptorDefault(var2, "Off");
         var2.setValue("legalValues", new Object[]{"Off", "And", "Or"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenThreadDebug")) {
         var3 = "getListenThreadDebug";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenThreadDebug";
         }

         var2 = new PropertyDescriptor("ListenThreadDebug", ServerDebugMBean.class, var3, var4);
         var1.put("ListenThreadDebug", var2);
         var2.setValue("description", "<p>Debug listenThread processing</p> ");
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("MagicThreadDumpBackToSocket")) {
         var3 = "getMagicThreadDumpBackToSocket";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMagicThreadDumpBackToSocket";
         }

         var2 = new PropertyDescriptor("MagicThreadDumpBackToSocket", ServerDebugMBean.class, var3, var4);
         var1.put("MagicThreadDumpBackToSocket", var2);
         var2.setValue("description", " ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MagicThreadDumpFile")) {
         var3 = "getMagicThreadDumpFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMagicThreadDumpFile";
         }

         var2 = new PropertyDescriptor("MagicThreadDumpFile", ServerDebugMBean.class, var3, var4);
         var1.put("MagicThreadDumpFile", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, "debugMagicThreadDumpFile");
         var2.setValue("secureValue", "debugMagicThreadDumpFile");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MagicThreadDumpHost")) {
         var3 = "getMagicThreadDumpHost";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMagicThreadDumpHost";
         }

         var2 = new PropertyDescriptor("MagicThreadDumpHost", ServerDebugMBean.class, var3, var4);
         var1.put("MagicThreadDumpHost", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, "localhost");
         var2.setValue("secureValue", "127.0.0.1");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MasterDeployer")) {
         var3 = "getMasterDeployer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMasterDeployer";
         }

         var2 = new PropertyDescriptor("MasterDeployer", ServerDebugMBean.class, var3, var4);
         var1.put("MasterDeployer", var2);
         var2.setValue("description", "<p>Debug Master Deployer processing</p> ");
      }

      if (!var1.containsKey("RedefiningClassLoader")) {
         var3 = "getRedefiningClassLoader";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRedefiningClassLoader";
         }

         var2 = new PropertyDescriptor("RedefiningClassLoader", ServerDebugMBean.class, var3, var4);
         var1.put("RedefiningClassLoader", var2);
         var2.setValue("description", "<p>Gets the fastswap RedefiningClassLoader debug attribute of ServerDebugMBean</p> ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Server")) {
         var3 = "getServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServer";
         }

         var2 = new PropertyDescriptor("Server", ServerDebugMBean.class, var3, var4);
         var1.put("Server", var2);
         var2.setValue("description", "<p>Returns the server for this bean</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("SlaveDeployer")) {
         var3 = "getSlaveDeployer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSlaveDeployer";
         }

         var2 = new PropertyDescriptor("SlaveDeployer", ServerDebugMBean.class, var3, var4);
         var1.put("SlaveDeployer", var2);
         var2.setValue("description", "<p>Debug Slave Deployer processing</p> ");
      }

      if (!var1.containsKey("WebModule")) {
         var3 = "getWebModule";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWebModule";
         }

         var2 = new PropertyDescriptor("WebModule", ServerDebugMBean.class, var3, var4);
         var1.put("WebModule", var2);
         var2.setValue("description", "<p>Debug WebModule processing</p> ");
      }

      if (!var1.containsKey("MagicThreadDumpEnabled")) {
         var3 = "isMagicThreadDumpEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMagicThreadDumpEnabled";
         }

         var2 = new PropertyDescriptor("MagicThreadDumpEnabled", ServerDebugMBean.class, var3, var4);
         var1.put("MagicThreadDumpEnabled", var2);
         var2.setValue("description", " ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("exclude", Boolean.TRUE);
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
