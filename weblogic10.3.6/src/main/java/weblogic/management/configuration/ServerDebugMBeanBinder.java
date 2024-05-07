package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ServerDebugMBeanBinder extends KernelDebugMBeanBinder implements AttributeBinder {
   private ServerDebugMBeanImpl bean;

   protected ServerDebugMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ServerDebugMBeanImpl)var1;
   }

   public ServerDebugMBeanBinder() {
      super(new ServerDebugMBeanImpl());
      this.bean = (ServerDebugMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ApplicationContainer")) {
                  try {
                     this.bean.setApplicationContainer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var294) {
                     System.out.println("Warning: multiple definitions with same name: " + var294.getMessage());
                  }
               } else if (var1.equals("BugReportServiceWsdlUrl")) {
                  try {
                     this.bean.setBugReportServiceWsdlUrl((String)var2);
                  } catch (BeanAlreadyExistsException var293) {
                     System.out.println("Warning: multiple definitions with same name: " + var293.getMessage());
                  }
               } else if (var1.equals("ClassChangeNotifier")) {
                  try {
                     this.bean.setClassChangeNotifier(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var292) {
                     System.out.println("Warning: multiple definitions with same name: " + var292.getMessage());
                  }
               } else if (var1.equals("ClassFinder")) {
                  try {
                     this.bean.setClassFinder(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var291) {
                     System.out.println("Warning: multiple definitions with same name: " + var291.getMessage());
                  }
               } else if (var1.equals("ClassLoader")) {
                  try {
                     this.bean.setClassLoader(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var290) {
                     System.out.println("Warning: multiple definitions with same name: " + var290.getMessage());
                  }
               } else if (var1.equals("ClassLoaderVerbose")) {
                  try {
                     this.bean.setClassLoaderVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var289) {
                     System.out.println("Warning: multiple definitions with same name: " + var289.getMessage());
                  }
               } else if (var1.equals("ClassloaderWebApp")) {
                  try {
                     this.bean.setClassloaderWebApp(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var288) {
                     System.out.println("Warning: multiple definitions with same name: " + var288.getMessage());
                  }
               } else if (var1.equals("ClasspathServlet")) {
                  try {
                     this.bean.setClasspathServlet(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var287) {
                     System.out.println("Warning: multiple definitions with same name: " + var287.getMessage());
                  }
               } else if (var1.equals("DebugAppContainer")) {
                  try {
                     this.bean.setDebugAppContainer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var286) {
                     System.out.println("Warning: multiple definitions with same name: " + var286.getMessage());
                  }
               } else if (var1.equals("DebugAsyncQueue")) {
                  try {
                     this.bean.setDebugAsyncQueue(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var285) {
                     System.out.println("Warning: multiple definitions with same name: " + var285.getMessage());
                  }
               } else if (var1.equals("DebugBootstrapServlet")) {
                  this.handleDeprecatedProperty("DebugBootstrapServlet", "<unknown>");

                  try {
                     this.bean.setDebugBootstrapServlet(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var284) {
                     System.out.println("Warning: multiple definitions with same name: " + var284.getMessage());
                  }
               } else if (var1.equals("DebugCertRevocCheck")) {
                  try {
                     this.bean.setDebugCertRevocCheck(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var283) {
                     System.out.println("Warning: multiple definitions with same name: " + var283.getMessage());
                  }
               } else if (var1.equals("DebugClassRedef")) {
                  try {
                     this.bean.setDebugClassRedef(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var282) {
                     System.out.println("Warning: multiple definitions with same name: " + var282.getMessage());
                  }
               } else if (var1.equals("DebugClassSize")) {
                  try {
                     this.bean.setDebugClassSize(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var281) {
                     System.out.println("Warning: multiple definitions with same name: " + var281.getMessage());
                  }
               } else if (var1.equals("DebugCluster")) {
                  try {
                     this.bean.setDebugCluster(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var280) {
                     System.out.println("Warning: multiple definitions with same name: " + var280.getMessage());
                  }
               } else if (var1.equals("DebugClusterAnnouncements")) {
                  try {
                     this.bean.setDebugClusterAnnouncements(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var279) {
                     System.out.println("Warning: multiple definitions with same name: " + var279.getMessage());
                  }
               } else if (var1.equals("DebugClusterFragments")) {
                  try {
                     this.bean.setDebugClusterFragments(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var278) {
                     System.out.println("Warning: multiple definitions with same name: " + var278.getMessage());
                  }
               } else if (var1.equals("DebugClusterHeartbeats")) {
                  try {
                     this.bean.setDebugClusterHeartbeats(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var277) {
                     System.out.println("Warning: multiple definitions with same name: " + var277.getMessage());
                  }
               } else if (var1.equals("DebugConfigurationEdit")) {
                  try {
                     this.bean.setDebugConfigurationEdit(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var276) {
                     System.out.println("Warning: multiple definitions with same name: " + var276.getMessage());
                  }
               } else if (var1.equals("DebugConfigurationRuntime")) {
                  try {
                     this.bean.setDebugConfigurationRuntime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var275) {
                     System.out.println("Warning: multiple definitions with same name: " + var275.getMessage());
                  }
               } else if (var1.equals("DebugConnectorService")) {
                  try {
                     this.bean.setDebugConnectorService(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var274) {
                     System.out.println("Warning: multiple definitions with same name: " + var274.getMessage());
                  }
               } else if (var1.equals("DebugConsensusLeasing")) {
                  try {
                     this.bean.setDebugConsensusLeasing(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var273) {
                     System.out.println("Warning: multiple definitions with same name: " + var273.getMessage());
                  }
               } else if (var1.equals("DebugDRSCalls")) {
                  try {
                     this.bean.setDebugDRSCalls(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var272) {
                     System.out.println("Warning: multiple definitions with same name: " + var272.getMessage());
                  }
               } else if (var1.equals("DebugDRSHeartbeats")) {
                  try {
                     this.bean.setDebugDRSHeartbeats(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var271) {
                     System.out.println("Warning: multiple definitions with same name: " + var271.getMessage());
                  }
               } else if (var1.equals("DebugDRSMessages")) {
                  try {
                     this.bean.setDebugDRSMessages(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var270) {
                     System.out.println("Warning: multiple definitions with same name: " + var270.getMessage());
                  }
               } else if (var1.equals("DebugDRSQueues")) {
                  try {
                     this.bean.setDebugDRSQueues(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var269) {
                     System.out.println("Warning: multiple definitions with same name: " + var269.getMessage());
                  }
               } else if (var1.equals("DebugDRSStateTransitions")) {
                  try {
                     this.bean.setDebugDRSStateTransitions(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var268) {
                     System.out.println("Warning: multiple definitions with same name: " + var268.getMessage());
                  }
               } else if (var1.equals("DebugDRSUpdateStatus")) {
                  try {
                     this.bean.setDebugDRSUpdateStatus(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var267) {
                     System.out.println("Warning: multiple definitions with same name: " + var267.getMessage());
                  }
               } else if (var1.equals("DebugDeploy")) {
                  try {
                     this.bean.setDebugDeploy(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var266) {
                     System.out.println("Warning: multiple definitions with same name: " + var266.getMessage());
                  }
               } else if (var1.equals("DebugDeployment")) {
                  try {
                     this.bean.setDebugDeployment(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var265) {
                     System.out.println("Warning: multiple definitions with same name: " + var265.getMessage());
                  }
               } else if (var1.equals("DebugDeploymentService")) {
                  try {
                     this.bean.setDebugDeploymentService(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var264) {
                     System.out.println("Warning: multiple definitions with same name: " + var264.getMessage());
                  }
               } else if (var1.equals("DebugDeploymentServiceInternal")) {
                  try {
                     this.bean.setDebugDeploymentServiceInternal(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var263) {
                     System.out.println("Warning: multiple definitions with same name: " + var263.getMessage());
                  }
               } else if (var1.equals("DebugDeploymentServiceStatusUpdates")) {
                  try {
                     this.bean.setDebugDeploymentServiceStatusUpdates(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var262) {
                     System.out.println("Warning: multiple definitions with same name: " + var262.getMessage());
                  }
               } else if (var1.equals("DebugDeploymentServiceTransport")) {
                  try {
                     this.bean.setDebugDeploymentServiceTransport(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var261) {
                     System.out.println("Warning: multiple definitions with same name: " + var261.getMessage());
                  }
               } else if (var1.equals("DebugDeploymentServiceTransportHttp")) {
                  try {
                     this.bean.setDebugDeploymentServiceTransportHttp(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var260) {
                     System.out.println("Warning: multiple definitions with same name: " + var260.getMessage());
                  }
               } else if (var1.equals("DebugDescriptor")) {
                  try {
                     this.bean.setDebugDescriptor(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var259) {
                     System.out.println("Warning: multiple definitions with same name: " + var259.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticAccessor")) {
                  try {
                     this.bean.setDebugDiagnosticAccessor(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var258) {
                     System.out.println("Warning: multiple definitions with same name: " + var258.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticArchive")) {
                  try {
                     this.bean.setDebugDiagnosticArchive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var257) {
                     System.out.println("Warning: multiple definitions with same name: " + var257.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticArchiveRetirement")) {
                  try {
                     this.bean.setDebugDiagnosticArchiveRetirement(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var256) {
                     System.out.println("Warning: multiple definitions with same name: " + var256.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticCollections")) {
                  try {
                     this.bean.setDebugDiagnosticCollections(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var255) {
                     System.out.println("Warning: multiple definitions with same name: " + var255.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticContext")) {
                  try {
                     this.bean.setDebugDiagnosticContext(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var254) {
                     System.out.println("Warning: multiple definitions with same name: " + var254.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticDataGathering")) {
                  try {
                     this.bean.setDebugDiagnosticDataGathering(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var253) {
                     System.out.println("Warning: multiple definitions with same name: " + var253.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticFileArchive")) {
                  try {
                     this.bean.setDebugDiagnosticFileArchive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var252) {
                     System.out.println("Warning: multiple definitions with same name: " + var252.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticImage")) {
                  try {
                     this.bean.setDebugDiagnosticImage(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var251) {
                     System.out.println("Warning: multiple definitions with same name: " + var251.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticInstrumentation")) {
                  try {
                     this.bean.setDebugDiagnosticInstrumentation(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var250) {
                     System.out.println("Warning: multiple definitions with same name: " + var250.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticInstrumentationActions")) {
                  try {
                     this.bean.setDebugDiagnosticInstrumentationActions(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var249) {
                     System.out.println("Warning: multiple definitions with same name: " + var249.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticInstrumentationConfig")) {
                  try {
                     this.bean.setDebugDiagnosticInstrumentationConfig(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var248) {
                     System.out.println("Warning: multiple definitions with same name: " + var248.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticInstrumentationEvents")) {
                  try {
                     this.bean.setDebugDiagnosticInstrumentationEvents(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var247) {
                     System.out.println("Warning: multiple definitions with same name: " + var247.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticInstrumentationWeaving")) {
                  try {
                     this.bean.setDebugDiagnosticInstrumentationWeaving(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var246) {
                     System.out.println("Warning: multiple definitions with same name: " + var246.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticInstrumentationWeavingMatches")) {
                  try {
                     this.bean.setDebugDiagnosticInstrumentationWeavingMatches(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var245) {
                     System.out.println("Warning: multiple definitions with same name: " + var245.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticJdbcArchive")) {
                  try {
                     this.bean.setDebugDiagnosticJdbcArchive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var244) {
                     System.out.println("Warning: multiple definitions with same name: " + var244.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticLifecycleHandlers")) {
                  try {
                     this.bean.setDebugDiagnosticLifecycleHandlers(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var243) {
                     System.out.println("Warning: multiple definitions with same name: " + var243.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticQuery")) {
                  try {
                     this.bean.setDebugDiagnosticQuery(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var242) {
                     System.out.println("Warning: multiple definitions with same name: " + var242.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticWatch")) {
                  try {
                     this.bean.setDebugDiagnosticWatch(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var241) {
                     System.out.println("Warning: multiple definitions with same name: " + var241.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticWlstoreArchive")) {
                  try {
                     this.bean.setDebugDiagnosticWlstoreArchive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var240) {
                     System.out.println("Warning: multiple definitions with same name: " + var240.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticsHarvester")) {
                  try {
                     this.bean.setDebugDiagnosticsHarvester(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var239) {
                     System.out.println("Warning: multiple definitions with same name: " + var239.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticsHarvesterData")) {
                  try {
                     this.bean.setDebugDiagnosticsHarvesterData(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var238) {
                     System.out.println("Warning: multiple definitions with same name: " + var238.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticsHarvesterMBeanPlugin")) {
                  try {
                     this.bean.setDebugDiagnosticsHarvesterMBeanPlugin(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var237) {
                     System.out.println("Warning: multiple definitions with same name: " + var237.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticsHarvesterTreeBeanPlugin")) {
                  try {
                     this.bean.setDebugDiagnosticsHarvesterTreeBeanPlugin(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var236) {
                     System.out.println("Warning: multiple definitions with same name: " + var236.getMessage());
                  }
               } else if (var1.equals("DebugDiagnosticsModule")) {
                  try {
                     this.bean.setDebugDiagnosticsModule(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var235) {
                     System.out.println("Warning: multiple definitions with same name: " + var235.getMessage());
                  }
               } else if (var1.equals("DebugDomainLogHandler")) {
                  try {
                     this.bean.setDebugDomainLogHandler(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var234) {
                     System.out.println("Warning: multiple definitions with same name: " + var234.getMessage());
                  }
               } else if (var1.equals("DebugEjbCaching")) {
                  try {
                     this.bean.setDebugEjbCaching(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var233) {
                     System.out.println("Warning: multiple definitions with same name: " + var233.getMessage());
                  }
               } else if (var1.equals("DebugEjbCmpDeployment")) {
                  try {
                     this.bean.setDebugEjbCmpDeployment(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var232) {
                     System.out.println("Warning: multiple definitions with same name: " + var232.getMessage());
                  }
               } else if (var1.equals("DebugEjbCmpRuntime")) {
                  try {
                     this.bean.setDebugEjbCmpRuntime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var231) {
                     System.out.println("Warning: multiple definitions with same name: " + var231.getMessage());
                  }
               } else if (var1.equals("DebugEjbCompilation")) {
                  try {
                     this.bean.setDebugEjbCompilation(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var230) {
                     System.out.println("Warning: multiple definitions with same name: " + var230.getMessage());
                  }
               } else if (var1.equals("DebugEjbDeployment")) {
                  try {
                     this.bean.setDebugEjbDeployment(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var229) {
                     System.out.println("Warning: multiple definitions with same name: " + var229.getMessage());
                  }
               } else if (var1.equals("DebugEjbInvoke")) {
                  try {
                     this.bean.setDebugEjbInvoke(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var228) {
                     System.out.println("Warning: multiple definitions with same name: " + var228.getMessage());
                  }
               } else if (var1.equals("DebugEjbLocking")) {
                  try {
                     this.bean.setDebugEjbLocking(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var227) {
                     System.out.println("Warning: multiple definitions with same name: " + var227.getMessage());
                  }
               } else if (var1.equals("DebugEjbMdbConnection")) {
                  try {
                     this.bean.setDebugEjbMdbConnection(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var226) {
                     System.out.println("Warning: multiple definitions with same name: " + var226.getMessage());
                  }
               } else if (var1.equals("DebugEjbPooling")) {
                  try {
                     this.bean.setDebugEjbPooling(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var225) {
                     System.out.println("Warning: multiple definitions with same name: " + var225.getMessage());
                  }
               } else if (var1.equals("DebugEjbSecurity")) {
                  try {
                     this.bean.setDebugEjbSecurity(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var224) {
                     System.out.println("Warning: multiple definitions with same name: " + var224.getMessage());
                  }
               } else if (var1.equals("DebugEjbSwapping")) {
                  try {
                     this.bean.setDebugEjbSwapping(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var223) {
                     System.out.println("Warning: multiple definitions with same name: " + var223.getMessage());
                  }
               } else if (var1.equals("DebugEjbTimers")) {
                  try {
                     this.bean.setDebugEjbTimers(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var222) {
                     System.out.println("Warning: multiple definitions with same name: " + var222.getMessage());
                  }
               } else if (var1.equals("DebugEmbeddedLDAP")) {
                  try {
                     this.bean.setDebugEmbeddedLDAP(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var221) {
                     System.out.println("Warning: multiple definitions with same name: " + var221.getMessage());
                  }
               } else if (var1.equals("DebugEmbeddedLDAPLogLevel")) {
                  try {
                     this.bean.setDebugEmbeddedLDAPLogLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var220) {
                     System.out.println("Warning: multiple definitions with same name: " + var220.getMessage());
                  }
               } else if (var1.equals("DebugEmbeddedLDAPLogToConsole")) {
                  try {
                     this.bean.setDebugEmbeddedLDAPLogToConsole(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var219) {
                     System.out.println("Warning: multiple definitions with same name: " + var219.getMessage());
                  }
               } else if (var1.equals("DebugEmbeddedLDAPWriteOverrideProps")) {
                  try {
                     this.bean.setDebugEmbeddedLDAPWriteOverrideProps(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var218) {
                     System.out.println("Warning: multiple definitions with same name: " + var218.getMessage());
                  }
               } else if (var1.equals("DebugEventManager")) {
                  try {
                     this.bean.setDebugEventManager(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var217) {
                     System.out.println("Warning: multiple definitions with same name: " + var217.getMessage());
                  }
               } else if (var1.equals("DebugFileDistributionServlet")) {
                  try {
                     this.bean.setDebugFileDistributionServlet(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var216) {
                     System.out.println("Warning: multiple definitions with same name: " + var216.getMessage());
                  }
               } else if (var1.equals("DebugHttp")) {
                  try {
                     this.bean.setDebugHttp(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var215) {
                     System.out.println("Warning: multiple definitions with same name: " + var215.getMessage());
                  }
               } else if (var1.equals("DebugHttpLogging")) {
                  try {
                     this.bean.setDebugHttpLogging(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var214) {
                     System.out.println("Warning: multiple definitions with same name: " + var214.getMessage());
                  }
               } else if (var1.equals("DebugHttpSessions")) {
                  try {
                     this.bean.setDebugHttpSessions(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var213) {
                     System.out.println("Warning: multiple definitions with same name: " + var213.getMessage());
                  }
               } else if (var1.equals("DebugIIOPNaming")) {
                  try {
                     this.bean.setDebugIIOPNaming(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var212) {
                     System.out.println("Warning: multiple definitions with same name: " + var212.getMessage());
                  }
               } else if (var1.equals("DebugIIOPTunneling")) {
                  try {
                     this.bean.setDebugIIOPTunneling(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var211) {
                     System.out.println("Warning: multiple definitions with same name: " + var211.getMessage());
                  }
               } else if (var1.equals("DebugJ2EEManagement")) {
                  try {
                     this.bean.setDebugJ2EEManagement(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var210) {
                     System.out.println("Warning: multiple definitions with same name: " + var210.getMessage());
                  }
               } else if (var1.equals("DebugJAXPDebugLevel")) {
                  try {
                     this.bean.setDebugJAXPDebugLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var209) {
                     System.out.println("Warning: multiple definitions with same name: " + var209.getMessage());
                  }
               } else if (var1.equals("DebugJAXPDebugName")) {
                  try {
                     this.bean.setDebugJAXPDebugName((String)var2);
                  } catch (BeanAlreadyExistsException var208) {
                     System.out.println("Warning: multiple definitions with same name: " + var208.getMessage());
                  }
               } else if (var1.equals("DebugJAXPIncludeClass")) {
                  try {
                     this.bean.setDebugJAXPIncludeClass(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var207) {
                     System.out.println("Warning: multiple definitions with same name: " + var207.getMessage());
                  }
               } else if (var1.equals("DebugJAXPIncludeLocation")) {
                  try {
                     this.bean.setDebugJAXPIncludeLocation(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var206) {
                     System.out.println("Warning: multiple definitions with same name: " + var206.getMessage());
                  }
               } else if (var1.equals("DebugJAXPIncludeName")) {
                  try {
                     this.bean.setDebugJAXPIncludeName(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var205) {
                     System.out.println("Warning: multiple definitions with same name: " + var205.getMessage());
                  }
               } else if (var1.equals("DebugJAXPIncludeTime")) {
                  try {
                     this.bean.setDebugJAXPIncludeTime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var204) {
                     System.out.println("Warning: multiple definitions with same name: " + var204.getMessage());
                  }
               } else if (var1.equals("DebugJAXPUseShortClass")) {
                  try {
                     this.bean.setDebugJAXPUseShortClass(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var203) {
                     System.out.println("Warning: multiple definitions with same name: " + var203.getMessage());
                  }
               } else if (var1.equals("DebugJDBCConn")) {
                  try {
                     this.bean.setDebugJDBCConn(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var202) {
                     System.out.println("Warning: multiple definitions with same name: " + var202.getMessage());
                  }
               } else if (var1.equals("DebugJDBCDriverLogging")) {
                  try {
                     this.bean.setDebugJDBCDriverLogging(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var201) {
                     System.out.println("Warning: multiple definitions with same name: " + var201.getMessage());
                  }
               } else if (var1.equals("DebugJDBCInternal")) {
                  try {
                     this.bean.setDebugJDBCInternal(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var200) {
                     System.out.println("Warning: multiple definitions with same name: " + var200.getMessage());
                  }
               } else if (var1.equals("DebugJDBCONS")) {
                  try {
                     this.bean.setDebugJDBCONS(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var199) {
                     System.out.println("Warning: multiple definitions with same name: " + var199.getMessage());
                  }
               } else if (var1.equals("DebugJDBCRAC")) {
                  try {
                     this.bean.setDebugJDBCRAC(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var198) {
                     System.out.println("Warning: multiple definitions with same name: " + var198.getMessage());
                  }
               } else if (var1.equals("DebugJDBCREPLAY")) {
                  try {
                     this.bean.setDebugJDBCREPLAY(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var197) {
                     System.out.println("Warning: multiple definitions with same name: " + var197.getMessage());
                  }
               } else if (var1.equals("DebugJDBCRMI")) {
                  try {
                     this.bean.setDebugJDBCRMI(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var196) {
                     System.out.println("Warning: multiple definitions with same name: " + var196.getMessage());
                  }
               } else if (var1.equals("DebugJDBCSQL")) {
                  try {
                     this.bean.setDebugJDBCSQL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var195) {
                     System.out.println("Warning: multiple definitions with same name: " + var195.getMessage());
                  }
               } else if (var1.equals("DebugJDBCUCP")) {
                  try {
                     this.bean.setDebugJDBCUCP(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var194) {
                     System.out.println("Warning: multiple definitions with same name: " + var194.getMessage());
                  }
               } else if (var1.equals("DebugJMSAME")) {
                  this.handleDeprecatedProperty("DebugJMSAME", "9.0.0.0");

                  try {
                     this.bean.setDebugJMSAME(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var193) {
                     System.out.println("Warning: multiple definitions with same name: " + var193.getMessage());
                  }
               } else if (var1.equals("DebugJMSBackEnd")) {
                  try {
                     this.bean.setDebugJMSBackEnd(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var192) {
                     System.out.println("Warning: multiple definitions with same name: " + var192.getMessage());
                  }
               } else if (var1.equals("DebugJMSBoot")) {
                  try {
                     this.bean.setDebugJMSBoot(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var191) {
                     System.out.println("Warning: multiple definitions with same name: " + var191.getMessage());
                  }
               } else if (var1.equals("DebugJMSCDS")) {
                  try {
                     this.bean.setDebugJMSCDS(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var190) {
                     System.out.println("Warning: multiple definitions with same name: " + var190.getMessage());
                  }
               } else if (var1.equals("DebugJMSCommon")) {
                  try {
                     this.bean.setDebugJMSCommon(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var189) {
                     System.out.println("Warning: multiple definitions with same name: " + var189.getMessage());
                  }
               } else if (var1.equals("DebugJMSConfig")) {
                  try {
                     this.bean.setDebugJMSConfig(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var188) {
                     System.out.println("Warning: multiple definitions with same name: " + var188.getMessage());
                  }
               } else if (var1.equals("DebugJMSDispatcher")) {
                  try {
                     this.bean.setDebugJMSDispatcher(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var187) {
                     System.out.println("Warning: multiple definitions with same name: " + var187.getMessage());
                  }
               } else if (var1.equals("DebugJMSDistTopic")) {
                  try {
                     this.bean.setDebugJMSDistTopic(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var186) {
                     System.out.println("Warning: multiple definitions with same name: " + var186.getMessage());
                  }
               } else if (var1.equals("DebugJMSDurableSubscribers")) {
                  this.handleDeprecatedProperty("DebugJMSDurableSubscribers", "9.0.0.0");

                  try {
                     this.bean.setDebugJMSDurableSubscribers(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var185) {
                     System.out.println("Warning: multiple definitions with same name: " + var185.getMessage());
                  }
               } else if (var1.equals("DebugJMSFrontEnd")) {
                  try {
                     this.bean.setDebugJMSFrontEnd(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var184) {
                     System.out.println("Warning: multiple definitions with same name: " + var184.getMessage());
                  }
               } else if (var1.equals("DebugJMSJDBCScavengeOnFlush")) {
                  this.handleDeprecatedProperty("DebugJMSJDBCScavengeOnFlush", "9.0.0.0");

                  try {
                     this.bean.setDebugJMSJDBCScavengeOnFlush(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var183) {
                     System.out.println("Warning: multiple definitions with same name: " + var183.getMessage());
                  }
               } else if (var1.equals("DebugJMSLocking")) {
                  this.handleDeprecatedProperty("DebugJMSLocking", "9.0.0.0");

                  try {
                     this.bean.setDebugJMSLocking(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var182) {
                     System.out.println("Warning: multiple definitions with same name: " + var182.getMessage());
                  }
               } else if (var1.equals("DebugJMSMessagePath")) {
                  try {
                     this.bean.setDebugJMSMessagePath(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var181) {
                     System.out.println("Warning: multiple definitions with same name: " + var181.getMessage());
                  }
               } else if (var1.equals("DebugJMSModule")) {
                  try {
                     this.bean.setDebugJMSModule(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var180) {
                     System.out.println("Warning: multiple definitions with same name: " + var180.getMessage());
                  }
               } else if (var1.equals("DebugJMSPauseResume")) {
                  try {
                     this.bean.setDebugJMSPauseResume(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var179) {
                     System.out.println("Warning: multiple definitions with same name: " + var179.getMessage());
                  }
               } else if (var1.equals("DebugJMSSAF")) {
                  try {
                     this.bean.setDebugJMSSAF(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var178) {
                     System.out.println("Warning: multiple definitions with same name: " + var178.getMessage());
                  }
               } else if (var1.equals("DebugJMSStore")) {
                  this.handleDeprecatedProperty("DebugJMSStore", "9.0.0.0");

                  try {
                     this.bean.setDebugJMSStore(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var177) {
                     System.out.println("Warning: multiple definitions with same name: " + var177.getMessage());
                  }
               } else if (var1.equals("DebugJMST3Server")) {
                  try {
                     this.bean.setDebugJMST3Server(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var176) {
                     System.out.println("Warning: multiple definitions with same name: " + var176.getMessage());
                  }
               } else if (var1.equals("DebugJMSWrappers")) {
                  try {
                     this.bean.setDebugJMSWrappers(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var175) {
                     System.out.println("Warning: multiple definitions with same name: " + var175.getMessage());
                  }
               } else if (var1.equals("DebugJMSXA")) {
                  this.handleDeprecatedProperty("DebugJMSXA", "9.0.0.0");

                  try {
                     this.bean.setDebugJMSXA(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var174) {
                     System.out.println("Warning: multiple definitions with same name: " + var174.getMessage());
                  }
               } else if (var1.equals("DebugJMX")) {
                  try {
                     this.bean.setDebugJMX(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var173) {
                     System.out.println("Warning: multiple definitions with same name: " + var173.getMessage());
                  }
               } else if (var1.equals("DebugJMXCompatibility")) {
                  try {
                     this.bean.setDebugJMXCompatibility(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var172) {
                     System.out.println("Warning: multiple definitions with same name: " + var172.getMessage());
                  }
               } else if (var1.equals("DebugJMXCore")) {
                  try {
                     this.bean.setDebugJMXCore(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var171) {
                     System.out.println("Warning: multiple definitions with same name: " + var171.getMessage());
                  }
               } else if (var1.equals("DebugJMXDomain")) {
                  try {
                     this.bean.setDebugJMXDomain(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var170) {
                     System.out.println("Warning: multiple definitions with same name: " + var170.getMessage());
                  }
               } else if (var1.equals("DebugJMXEdit")) {
                  try {
                     this.bean.setDebugJMXEdit(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var169) {
                     System.out.println("Warning: multiple definitions with same name: " + var169.getMessage());
                  }
               } else if (var1.equals("DebugJMXRuntime")) {
                  try {
                     this.bean.setDebugJMXRuntime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var168) {
                     System.out.println("Warning: multiple definitions with same name: " + var168.getMessage());
                  }
               } else if (var1.equals("DebugJNDI")) {
                  try {
                     this.bean.setDebugJNDI(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var167) {
                     System.out.println("Warning: multiple definitions with same name: " + var167.getMessage());
                  }
               } else if (var1.equals("DebugJNDIFactories")) {
                  try {
                     this.bean.setDebugJNDIFactories(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var166) {
                     System.out.println("Warning: multiple definitions with same name: " + var166.getMessage());
                  }
               } else if (var1.equals("DebugJNDIResolution")) {
                  try {
                     this.bean.setDebugJNDIResolution(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var165) {
                     System.out.println("Warning: multiple definitions with same name: " + var165.getMessage());
                  }
               } else if (var1.equals("DebugJTA2PC")) {
                  try {
                     this.bean.setDebugJTA2PC(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var164) {
                     System.out.println("Warning: multiple definitions with same name: " + var164.getMessage());
                  }
               } else if (var1.equals("DebugJTA2PCStackTrace")) {
                  try {
                     this.bean.setDebugJTA2PCStackTrace(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var163) {
                     System.out.println("Warning: multiple definitions with same name: " + var163.getMessage());
                  }
               } else if (var1.equals("DebugJTAAPI")) {
                  try {
                     this.bean.setDebugJTAAPI(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var162) {
                     System.out.println("Warning: multiple definitions with same name: " + var162.getMessage());
                  }
               } else if (var1.equals("DebugJTAGateway")) {
                  try {
                     this.bean.setDebugJTAGateway(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var161) {
                     System.out.println("Warning: multiple definitions with same name: " + var161.getMessage());
                  }
               } else if (var1.equals("DebugJTAGatewayStackTrace")) {
                  try {
                     this.bean.setDebugJTAGatewayStackTrace(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var160) {
                     System.out.println("Warning: multiple definitions with same name: " + var160.getMessage());
                  }
               } else if (var1.equals("DebugJTAHealth")) {
                  try {
                     this.bean.setDebugJTAHealth(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var159) {
                     System.out.println("Warning: multiple definitions with same name: " + var159.getMessage());
                  }
               } else if (var1.equals("DebugJTAJDBC")) {
                  try {
                     this.bean.setDebugJTAJDBC(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var158) {
                     System.out.println("Warning: multiple definitions with same name: " + var158.getMessage());
                  }
               } else if (var1.equals("DebugJTALLR")) {
                  try {
                     this.bean.setDebugJTALLR(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var157) {
                     System.out.println("Warning: multiple definitions with same name: " + var157.getMessage());
                  }
               } else if (var1.equals("DebugJTALifecycle")) {
                  try {
                     this.bean.setDebugJTALifecycle(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var156) {
                     System.out.println("Warning: multiple definitions with same name: " + var156.getMessage());
                  }
               } else if (var1.equals("DebugJTAMigration")) {
                  try {
                     this.bean.setDebugJTAMigration(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var155) {
                     System.out.println("Warning: multiple definitions with same name: " + var155.getMessage());
                  }
               } else if (var1.equals("DebugJTANaming")) {
                  try {
                     this.bean.setDebugJTANaming(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var154) {
                     System.out.println("Warning: multiple definitions with same name: " + var154.getMessage());
                  }
               } else if (var1.equals("DebugJTANamingStackTrace")) {
                  try {
                     this.bean.setDebugJTANamingStackTrace(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var153) {
                     System.out.println("Warning: multiple definitions with same name: " + var153.getMessage());
                  }
               } else if (var1.equals("DebugJTANonXA")) {
                  try {
                     this.bean.setDebugJTANonXA(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var152) {
                     System.out.println("Warning: multiple definitions with same name: " + var152.getMessage());
                  }
               } else if (var1.equals("DebugJTAPropagate")) {
                  try {
                     this.bean.setDebugJTAPropagate(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var151) {
                     System.out.println("Warning: multiple definitions with same name: " + var151.getMessage());
                  }
               } else if (var1.equals("DebugJTARMI")) {
                  try {
                     this.bean.setDebugJTARMI(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var150) {
                     System.out.println("Warning: multiple definitions with same name: " + var150.getMessage());
                  }
               } else if (var1.equals("DebugJTARecovery")) {
                  try {
                     this.bean.setDebugJTARecovery(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var149) {
                     System.out.println("Warning: multiple definitions with same name: " + var149.getMessage());
                  }
               } else if (var1.equals("DebugJTARecoveryStackTrace")) {
                  try {
                     this.bean.setDebugJTARecoveryStackTrace(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var148) {
                     System.out.println("Warning: multiple definitions with same name: " + var148.getMessage());
                  }
               } else if (var1.equals("DebugJTAResourceHealth")) {
                  try {
                     this.bean.setDebugJTAResourceHealth(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var147) {
                     System.out.println("Warning: multiple definitions with same name: " + var147.getMessage());
                  }
               } else if (var1.equals("DebugJTAResourceName")) {
                  try {
                     this.bean.setDebugJTAResourceName((String)var2);
                  } catch (BeanAlreadyExistsException var146) {
                     System.out.println("Warning: multiple definitions with same name: " + var146.getMessage());
                  }
               } else if (var1.equals("DebugJTATLOG")) {
                  try {
                     this.bean.setDebugJTATLOG(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var145) {
                     System.out.println("Warning: multiple definitions with same name: " + var145.getMessage());
                  }
               } else if (var1.equals("DebugJTATransactionName")) {
                  try {
                     this.bean.setDebugJTATransactionName((String)var2);
                  } catch (BeanAlreadyExistsException var144) {
                     System.out.println("Warning: multiple definitions with same name: " + var144.getMessage());
                  }
               } else if (var1.equals("DebugJTAXA")) {
                  try {
                     this.bean.setDebugJTAXA(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var143) {
                     System.out.println("Warning: multiple definitions with same name: " + var143.getMessage());
                  }
               } else if (var1.equals("DebugJTAXAStackTrace")) {
                  try {
                     this.bean.setDebugJTAXAStackTrace(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var142) {
                     System.out.println("Warning: multiple definitions with same name: " + var142.getMessage());
                  }
               } else if (var1.equals("DebugJpaDataCache")) {
                  try {
                     this.bean.setDebugJpaDataCache(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var141) {
                     System.out.println("Warning: multiple definitions with same name: " + var141.getMessage());
                  }
               } else if (var1.equals("DebugJpaEnhance")) {
                  try {
                     this.bean.setDebugJpaEnhance(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var140) {
                     System.out.println("Warning: multiple definitions with same name: " + var140.getMessage());
                  }
               } else if (var1.equals("DebugJpaJdbcJdbc")) {
                  try {
                     this.bean.setDebugJpaJdbcJdbc(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var139) {
                     System.out.println("Warning: multiple definitions with same name: " + var139.getMessage());
                  }
               } else if (var1.equals("DebugJpaJdbcSchema")) {
                  try {
                     this.bean.setDebugJpaJdbcSchema(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var138) {
                     System.out.println("Warning: multiple definitions with same name: " + var138.getMessage());
                  }
               } else if (var1.equals("DebugJpaJdbcSql")) {
                  try {
                     this.bean.setDebugJpaJdbcSql(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var137) {
                     System.out.println("Warning: multiple definitions with same name: " + var137.getMessage());
                  }
               } else if (var1.equals("DebugJpaManage")) {
                  try {
                     this.bean.setDebugJpaManage(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var136) {
                     System.out.println("Warning: multiple definitions with same name: " + var136.getMessage());
                  }
               } else if (var1.equals("DebugJpaMetaData")) {
                  try {
                     this.bean.setDebugJpaMetaData(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var135) {
                     System.out.println("Warning: multiple definitions with same name: " + var135.getMessage());
                  }
               } else if (var1.equals("DebugJpaProfile")) {
                  try {
                     this.bean.setDebugJpaProfile(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var134) {
                     System.out.println("Warning: multiple definitions with same name: " + var134.getMessage());
                  }
               } else if (var1.equals("DebugJpaQuery")) {
                  try {
                     this.bean.setDebugJpaQuery(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var133) {
                     System.out.println("Warning: multiple definitions with same name: " + var133.getMessage());
                  }
               } else if (var1.equals("DebugJpaRuntime")) {
                  try {
                     this.bean.setDebugJpaRuntime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var132) {
                     System.out.println("Warning: multiple definitions with same name: " + var132.getMessage());
                  }
               } else if (var1.equals("DebugJpaTool")) {
                  try {
                     this.bean.setDebugJpaTool(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var131) {
                     System.out.println("Warning: multiple definitions with same name: " + var131.getMessage());
                  }
               } else if (var1.equals("DebugLeaderElection")) {
                  try {
                     this.bean.setDebugLeaderElection(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var130) {
                     System.out.println("Warning: multiple definitions with same name: " + var130.getMessage());
                  }
               } else if (var1.equals("DebugLibraries")) {
                  try {
                     this.bean.setDebugLibraries(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var129) {
                     System.out.println("Warning: multiple definitions with same name: " + var129.getMessage());
                  }
               } else if (var1.equals("DebugLoggingConfiguration")) {
                  try {
                     this.bean.setDebugLoggingConfiguration(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var128) {
                     System.out.println("Warning: multiple definitions with same name: " + var128.getMessage());
                  }
               } else if (var1.equals("DebugManagementServicesResource")) {
                  try {
                     this.bean.setDebugManagementServicesResource(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var127) {
                     System.out.println("Warning: multiple definitions with same name: " + var127.getMessage());
                  }
               } else if (var1.equals("DebugMaskCriterias")) {
                  try {
                     this.bean.setDebugMaskCriterias(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var126) {
                     System.out.println("Warning: multiple definitions with same name: " + var126.getMessage());
                  }
               } else if (var1.equals("DebugMessagingBridgeDumpToConsole")) {
                  this.handleDeprecatedProperty("DebugMessagingBridgeDumpToConsole", "<unknown>");

                  try {
                     this.bean.setDebugMessagingBridgeDumpToConsole(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var125) {
                     System.out.println("Warning: multiple definitions with same name: " + var125.getMessage());
                  }
               } else if (var1.equals("DebugMessagingBridgeDumpToLog")) {
                  this.handleDeprecatedProperty("DebugMessagingBridgeDumpToLog", "<unknown>");

                  try {
                     this.bean.setDebugMessagingBridgeDumpToLog(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var124) {
                     System.out.println("Warning: multiple definitions with same name: " + var124.getMessage());
                  }
               } else if (var1.equals("DebugMessagingBridgeRuntime")) {
                  try {
                     this.bean.setDebugMessagingBridgeRuntime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var123) {
                     System.out.println("Warning: multiple definitions with same name: " + var123.getMessage());
                  }
               } else if (var1.equals("DebugMessagingBridgeRuntimeVerbose")) {
                  try {
                     this.bean.setDebugMessagingBridgeRuntimeVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var122) {
                     System.out.println("Warning: multiple definitions with same name: " + var122.getMessage());
                  }
               } else if (var1.equals("DebugMessagingBridgeStartup")) {
                  try {
                     this.bean.setDebugMessagingBridgeStartup(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var121) {
                     System.out.println("Warning: multiple definitions with same name: " + var121.getMessage());
                  }
               } else if (var1.equals("DebugMessagingKernel")) {
                  try {
                     this.bean.setDebugMessagingKernel(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var120) {
                     System.out.println("Warning: multiple definitions with same name: " + var120.getMessage());
                  }
               } else if (var1.equals("DebugMessagingKernelBoot")) {
                  try {
                     this.bean.setDebugMessagingKernelBoot(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var119) {
                     System.out.println("Warning: multiple definitions with same name: " + var119.getMessage());
                  }
               } else if (var1.equals("DebugPathSvc")) {
                  try {
                     this.bean.setDebugPathSvc(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var118) {
                     System.out.println("Warning: multiple definitions with same name: " + var118.getMessage());
                  }
               } else if (var1.equals("DebugPathSvcVerbose")) {
                  try {
                     this.bean.setDebugPathSvcVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var117) {
                     System.out.println("Warning: multiple definitions with same name: " + var117.getMessage());
                  }
               } else if (var1.equals("DebugRA")) {
                  try {
                     this.bean.setDebugRA(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var116) {
                     System.out.println("Warning: multiple definitions with same name: " + var116.getMessage());
                  }
               } else if (var1.equals("DebugRAClassloader")) {
                  try {
                     this.bean.setDebugRAClassloader(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var115) {
                     System.out.println("Warning: multiple definitions with same name: " + var115.getMessage());
                  }
               } else if (var1.equals("DebugRAConnEvents")) {
                  try {
                     this.bean.setDebugRAConnEvents(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var114) {
                     System.out.println("Warning: multiple definitions with same name: " + var114.getMessage());
                  }
               } else if (var1.equals("DebugRAConnections")) {
                  try {
                     this.bean.setDebugRAConnections(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var113) {
                     System.out.println("Warning: multiple definitions with same name: " + var113.getMessage());
                  }
               } else if (var1.equals("DebugRADeployment")) {
                  try {
                     this.bean.setDebugRADeployment(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var112) {
                     System.out.println("Warning: multiple definitions with same name: " + var112.getMessage());
                  }
               } else if (var1.equals("DebugRALifecycle")) {
                  try {
                     this.bean.setDebugRALifecycle(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var111) {
                     System.out.println("Warning: multiple definitions with same name: " + var111.getMessage());
                  }
               } else if (var1.equals("DebugRALocalOut")) {
                  try {
                     this.bean.setDebugRALocalOut(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var110) {
                     System.out.println("Warning: multiple definitions with same name: " + var110.getMessage());
                  }
               } else if (var1.equals("DebugRAParsing")) {
                  try {
                     this.bean.setDebugRAParsing(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var109) {
                     System.out.println("Warning: multiple definitions with same name: " + var109.getMessage());
                  }
               } else if (var1.equals("DebugRAPoolVerbose")) {
                  try {
                     this.bean.setDebugRAPoolVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var108) {
                     System.out.println("Warning: multiple definitions with same name: " + var108.getMessage());
                  }
               } else if (var1.equals("DebugRAPooling")) {
                  try {
                     this.bean.setDebugRAPooling(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var107) {
                     System.out.println("Warning: multiple definitions with same name: " + var107.getMessage());
                  }
               } else if (var1.equals("DebugRASecurityCtx")) {
                  try {
                     this.bean.setDebugRASecurityCtx(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var106) {
                     System.out.println("Warning: multiple definitions with same name: " + var106.getMessage());
                  }
               } else if (var1.equals("DebugRAWork")) {
                  try {
                     this.bean.setDebugRAWork(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var105) {
                     System.out.println("Warning: multiple definitions with same name: " + var105.getMessage());
                  }
               } else if (var1.equals("DebugRAWorkEvents")) {
                  try {
                     this.bean.setDebugRAWorkEvents(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var104) {
                     System.out.println("Warning: multiple definitions with same name: " + var104.getMessage());
                  }
               } else if (var1.equals("DebugRAXAin")) {
                  try {
                     this.bean.setDebugRAXAin(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var103) {
                     System.out.println("Warning: multiple definitions with same name: " + var103.getMessage());
                  }
               } else if (var1.equals("DebugRAXAout")) {
                  try {
                     this.bean.setDebugRAXAout(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var102) {
                     System.out.println("Warning: multiple definitions with same name: " + var102.getMessage());
                  }
               } else if (var1.equals("DebugRAXAwork")) {
                  try {
                     this.bean.setDebugRAXAwork(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var101) {
                     System.out.println("Warning: multiple definitions with same name: " + var101.getMessage());
                  }
               } else if (var1.equals("DebugReplication")) {
                  try {
                     this.bean.setDebugReplication(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var100) {
                     System.out.println("Warning: multiple definitions with same name: " + var100.getMessage());
                  }
               } else if (var1.equals("DebugReplicationDetails")) {
                  try {
                     this.bean.setDebugReplicationDetails(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var99) {
                     System.out.println("Warning: multiple definitions with same name: " + var99.getMessage());
                  }
               } else if (var1.equals("DebugSAFAdmin")) {
                  try {
                     this.bean.setDebugSAFAdmin(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var98) {
                     System.out.println("Warning: multiple definitions with same name: " + var98.getMessage());
                  }
               } else if (var1.equals("DebugSAFLifeCycle")) {
                  this.handleDeprecatedProperty("DebugSAFLifeCycle", "9.0.0.0");

                  try {
                     this.bean.setDebugSAFLifeCycle(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var97) {
                     System.out.println("Warning: multiple definitions with same name: " + var97.getMessage());
                  }
               } else if (var1.equals("DebugSAFManager")) {
                  try {
                     this.bean.setDebugSAFManager(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var96) {
                     System.out.println("Warning: multiple definitions with same name: " + var96.getMessage());
                  }
               } else if (var1.equals("DebugSAFMessagePath")) {
                  this.handleDeprecatedProperty("DebugSAFMessagePath", "9.0.0.0");

                  try {
                     this.bean.setDebugSAFMessagePath(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var95) {
                     System.out.println("Warning: multiple definitions with same name: " + var95.getMessage());
                  }
               } else if (var1.equals("DebugSAFReceivingAgent")) {
                  try {
                     this.bean.setDebugSAFReceivingAgent(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var94) {
                     System.out.println("Warning: multiple definitions with same name: " + var94.getMessage());
                  }
               } else if (var1.equals("DebugSAFSendingAgent")) {
                  try {
                     this.bean.setDebugSAFSendingAgent(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var93) {
                     System.out.println("Warning: multiple definitions with same name: " + var93.getMessage());
                  }
               } else if (var1.equals("DebugSAFStore")) {
                  try {
                     this.bean.setDebugSAFStore(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var92) {
                     System.out.println("Warning: multiple definitions with same name: " + var92.getMessage());
                  }
               } else if (var1.equals("DebugSAFTransport")) {
                  try {
                     this.bean.setDebugSAFTransport(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var91) {
                     System.out.println("Warning: multiple definitions with same name: " + var91.getMessage());
                  }
               } else if (var1.equals("DebugSAFVerbose")) {
                  try {
                     this.bean.setDebugSAFVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var90) {
                     System.out.println("Warning: multiple definitions with same name: " + var90.getMessage());
                  }
               } else if (var1.equals("DebugSNMPAgent")) {
                  try {
                     this.bean.setDebugSNMPAgent(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var89) {
                     System.out.println("Warning: multiple definitions with same name: " + var89.getMessage());
                  }
               } else if (var1.equals("DebugSNMPExtensionProvider")) {
                  try {
                     this.bean.setDebugSNMPExtensionProvider(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var88) {
                     System.out.println("Warning: multiple definitions with same name: " + var88.getMessage());
                  }
               } else if (var1.equals("DebugSNMPProtocolTCP")) {
                  try {
                     this.bean.setDebugSNMPProtocolTCP(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var87) {
                     System.out.println("Warning: multiple definitions with same name: " + var87.getMessage());
                  }
               } else if (var1.equals("DebugSNMPToolkit")) {
                  try {
                     this.bean.setDebugSNMPToolkit(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var86) {
                     System.out.println("Warning: multiple definitions with same name: " + var86.getMessage());
                  }
               } else if (var1.equals("DebugScaContainer")) {
                  try {
                     this.bean.setDebugScaContainer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var85) {
                     System.out.println("Warning: multiple definitions with same name: " + var85.getMessage());
                  }
               } else if (var1.equals("DebugSecurity")) {
                  try {
                     this.bean.setDebugSecurity(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var84) {
                     System.out.println("Warning: multiple definitions with same name: " + var84.getMessage());
                  }
               } else if (var1.equals("DebugSecurityAdjudicator")) {
                  try {
                     this.bean.setDebugSecurityAdjudicator(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var83) {
                     System.out.println("Warning: multiple definitions with same name: " + var83.getMessage());
                  }
               } else if (var1.equals("DebugSecurityAtn")) {
                  try {
                     this.bean.setDebugSecurityAtn(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var82) {
                     System.out.println("Warning: multiple definitions with same name: " + var82.getMessage());
                  }
               } else if (var1.equals("DebugSecurityAtz")) {
                  try {
                     this.bean.setDebugSecurityAtz(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var81) {
                     System.out.println("Warning: multiple definitions with same name: " + var81.getMessage());
                  }
               } else if (var1.equals("DebugSecurityAuditor")) {
                  try {
                     this.bean.setDebugSecurityAuditor(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var80) {
                     System.out.println("Warning: multiple definitions with same name: " + var80.getMessage());
                  }
               } else if (var1.equals("DebugSecurityCertPath")) {
                  try {
                     this.bean.setDebugSecurityCertPath(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var79) {
                     System.out.println("Warning: multiple definitions with same name: " + var79.getMessage());
                  }
               } else if (var1.equals("DebugSecurityCredMap")) {
                  try {
                     this.bean.setDebugSecurityCredMap(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var78) {
                     System.out.println("Warning: multiple definitions with same name: " + var78.getMessage());
                  }
               } else if (var1.equals("DebugSecurityEEngine")) {
                  try {
                     this.bean.setDebugSecurityEEngine(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var77) {
                     System.out.println("Warning: multiple definitions with same name: " + var77.getMessage());
                  }
               } else if (var1.equals("DebugSecurityEncryptionService")) {
                  try {
                     this.bean.setDebugSecurityEncryptionService(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var76) {
                     System.out.println("Warning: multiple definitions with same name: " + var76.getMessage());
                  }
               } else if (var1.equals("DebugSecurityJACC")) {
                  try {
                     this.bean.setDebugSecurityJACC(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var75) {
                     System.out.println("Warning: multiple definitions with same name: " + var75.getMessage());
                  }
               } else if (var1.equals("DebugSecurityJACCNonPolicy")) {
                  try {
                     this.bean.setDebugSecurityJACCNonPolicy(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var74) {
                     System.out.println("Warning: multiple definitions with same name: " + var74.getMessage());
                  }
               } else if (var1.equals("DebugSecurityJACCPolicy")) {
                  try {
                     this.bean.setDebugSecurityJACCPolicy(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var73) {
                     System.out.println("Warning: multiple definitions with same name: " + var73.getMessage());
                  }
               } else if (var1.equals("DebugSecurityKeyStore")) {
                  try {
                     this.bean.setDebugSecurityKeyStore(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var72) {
                     System.out.println("Warning: multiple definitions with same name: " + var72.getMessage());
                  }
               } else if (var1.equals("DebugSecurityPasswordPolicy")) {
                  try {
                     this.bean.setDebugSecurityPasswordPolicy(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var71) {
                     System.out.println("Warning: multiple definitions with same name: " + var71.getMessage());
                  }
               } else if (var1.equals("DebugSecurityPredicate")) {
                  try {
                     this.bean.setDebugSecurityPredicate(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var70) {
                     System.out.println("Warning: multiple definitions with same name: " + var70.getMessage());
                  }
               } else if (var1.equals("DebugSecurityProfiler")) {
                  try {
                     this.bean.setDebugSecurityProfiler(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var69) {
                     System.out.println("Warning: multiple definitions with same name: " + var69.getMessage());
                  }
               } else if (var1.equals("DebugSecurityRealm")) {
                  try {
                     this.bean.setDebugSecurityRealm(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var68) {
                     System.out.println("Warning: multiple definitions with same name: " + var68.getMessage());
                  }
               } else if (var1.equals("DebugSecurityRoleMap")) {
                  try {
                     this.bean.setDebugSecurityRoleMap(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var67) {
                     System.out.println("Warning: multiple definitions with same name: " + var67.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAML2Atn")) {
                  try {
                     this.bean.setDebugSecuritySAML2Atn(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var66) {
                     System.out.println("Warning: multiple definitions with same name: " + var66.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAML2CredMap")) {
                  try {
                     this.bean.setDebugSecuritySAML2CredMap(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var65) {
                     System.out.println("Warning: multiple definitions with same name: " + var65.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAML2Lib")) {
                  try {
                     this.bean.setDebugSecuritySAML2Lib(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var64) {
                     System.out.println("Warning: multiple definitions with same name: " + var64.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAML2Service")) {
                  try {
                     this.bean.setDebugSecuritySAML2Service(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var63) {
                     System.out.println("Warning: multiple definitions with same name: " + var63.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAMLAtn")) {
                  try {
                     this.bean.setDebugSecuritySAMLAtn(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var62) {
                     System.out.println("Warning: multiple definitions with same name: " + var62.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAMLCredMap")) {
                  try {
                     this.bean.setDebugSecuritySAMLCredMap(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var61) {
                     System.out.println("Warning: multiple definitions with same name: " + var61.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAMLLib")) {
                  try {
                     this.bean.setDebugSecuritySAMLLib(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var60) {
                     System.out.println("Warning: multiple definitions with same name: " + var60.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySAMLService")) {
                  try {
                     this.bean.setDebugSecuritySAMLService(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var59) {
                     System.out.println("Warning: multiple definitions with same name: " + var59.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySSL")) {
                  try {
                     this.bean.setDebugSecuritySSL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var58) {
                     System.out.println("Warning: multiple definitions with same name: " + var58.getMessage());
                  }
               } else if (var1.equals("DebugSecuritySSLEaten")) {
                  try {
                     this.bean.setDebugSecuritySSLEaten(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var57) {
                     System.out.println("Warning: multiple definitions with same name: " + var57.getMessage());
                  }
               } else if (var1.equals("DebugSecurityService")) {
                  try {
                     this.bean.setDebugSecurityService(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var56) {
                     System.out.println("Warning: multiple definitions with same name: " + var56.getMessage());
                  }
               } else if (var1.equals("DebugSecurityUserLockout")) {
                  try {
                     this.bean.setDebugSecurityUserLockout(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var55) {
                     System.out.println("Warning: multiple definitions with same name: " + var55.getMessage());
                  }
               } else if (var1.equals("DebugSelfTuning")) {
                  try {
                     this.bean.setDebugSelfTuning(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var54) {
                     System.out.println("Warning: multiple definitions with same name: " + var54.getMessage());
                  }
               } else if (var1.equals("DebugServerLifeCycle")) {
                  try {
                     this.bean.setDebugServerLifeCycle(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var53) {
                     System.out.println("Warning: multiple definitions with same name: " + var53.getMessage());
                  }
               } else if (var1.equals("DebugServerMigration")) {
                  try {
                     this.bean.setDebugServerMigration(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var52) {
                     System.out.println("Warning: multiple definitions with same name: " + var52.getMessage());
                  }
               } else if (var1.equals("DebugServerStartStatistics")) {
                  try {
                     this.bean.setDebugServerStartStatistics(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var51) {
                     System.out.println("Warning: multiple definitions with same name: " + var51.getMessage());
                  }
               } else if (var1.equals("DebugStoreAdmin")) {
                  try {
                     this.bean.setDebugStoreAdmin(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var50) {
                     System.out.println("Warning: multiple definitions with same name: " + var50.getMessage());
                  }
               } else if (var1.equals("DebugStoreIOLogical")) {
                  try {
                     this.bean.setDebugStoreIOLogical(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var49) {
                     System.out.println("Warning: multiple definitions with same name: " + var49.getMessage());
                  }
               } else if (var1.equals("DebugStoreIOLogicalBoot")) {
                  try {
                     this.bean.setDebugStoreIOLogicalBoot(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var48) {
                     System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                  }
               } else if (var1.equals("DebugStoreIOPhysical")) {
                  try {
                     this.bean.setDebugStoreIOPhysical(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var47) {
                     System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                  }
               } else if (var1.equals("DebugStoreIOPhysicalVerbose")) {
                  try {
                     this.bean.setDebugStoreIOPhysicalVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var46) {
                     System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                  }
               } else if (var1.equals("DebugStoreXA")) {
                  this.handleDeprecatedProperty("DebugStoreXA", "9.0.0.0");

                  try {
                     this.bean.setDebugStoreXA(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var45) {
                     System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                  }
               } else if (var1.equals("DebugStoreXAVerbose")) {
                  this.handleDeprecatedProperty("DebugStoreXAVerbose", "9.0.0.0");

                  try {
                     this.bean.setDebugStoreXAVerbose(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var44) {
                     System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                  }
               } else if (var1.equals("DebugTunnelingConnection")) {
                  try {
                     this.bean.setDebugTunnelingConnection(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var43) {
                     System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                  }
               } else if (var1.equals("DebugTunnelingConnectionTimeout")) {
                  try {
                     this.bean.setDebugTunnelingConnectionTimeout(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var42) {
                     System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                  }
               } else if (var1.equals("DebugURLResolution")) {
                  try {
                     this.bean.setDebugURLResolution(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("DebugWANReplicationDetails")) {
                  try {
                     this.bean.setDebugWANReplicationDetails(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("DebugWTCConfig")) {
                  try {
                     this.bean.setDebugWTCConfig(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("DebugWTCCorbaEx")) {
                  try {
                     this.bean.setDebugWTCCorbaEx(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("DebugWTCGwtEx")) {
                  try {
                     this.bean.setDebugWTCGwtEx(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("DebugWTCJatmiEx")) {
                  try {
                     this.bean.setDebugWTCJatmiEx(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("DebugWTCTDomPdu")) {
                  try {
                     this.bean.setDebugWTCTDomPdu(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("DebugWTCUData")) {
                  try {
                     this.bean.setDebugWTCUData(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("DebugWTCtBridgeEx")) {
                  try {
                     this.bean.setDebugWTCtBridgeEx(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("DebugWebAppIdentityAssertion")) {
                  try {
                     this.bean.setDebugWebAppIdentityAssertion(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("DebugWebAppModule")) {
                  try {
                     this.bean.setDebugWebAppModule(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("DebugWebAppSecurity")) {
                  try {
                     this.bean.setDebugWebAppSecurity(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheDebugLevel")) {
                  try {
                     this.bean.setDebugXMLEntityCacheDebugLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheDebugName")) {
                  try {
                     this.bean.setDebugXMLEntityCacheDebugName((String)var2);
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheIncludeClass")) {
                  try {
                     this.bean.setDebugXMLEntityCacheIncludeClass(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheIncludeLocation")) {
                  try {
                     this.bean.setDebugXMLEntityCacheIncludeLocation(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheIncludeName")) {
                  try {
                     this.bean.setDebugXMLEntityCacheIncludeName(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheIncludeTime")) {
                  try {
                     this.bean.setDebugXMLEntityCacheIncludeTime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("DebugXMLEntityCacheUseShortClass")) {
                  try {
                     this.bean.setDebugXMLEntityCacheUseShortClass(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryDebugLevel")) {
                  try {
                     this.bean.setDebugXMLRegistryDebugLevel(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryDebugName")) {
                  try {
                     this.bean.setDebugXMLRegistryDebugName((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryIncludeClass")) {
                  try {
                     this.bean.setDebugXMLRegistryIncludeClass(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryIncludeLocation")) {
                  try {
                     this.bean.setDebugXMLRegistryIncludeLocation(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryIncludeName")) {
                  try {
                     this.bean.setDebugXMLRegistryIncludeName(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryIncludeTime")) {
                  try {
                     this.bean.setDebugXMLRegistryIncludeTime(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("DebugXMLRegistryUseShortClass")) {
                  try {
                     this.bean.setDebugXMLRegistryUseShortClass(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("DefaultStore")) {
                  try {
                     this.bean.setDefaultStore(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("DiagnosticContextDebugMode")) {
                  try {
                     this.bean.setDiagnosticContextDebugMode((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("ListenThreadDebug")) {
                  try {
                     this.bean.setListenThreadDebug(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("MagicThreadDumpBackToSocket")) {
                  try {
                     this.bean.setMagicThreadDumpBackToSocket(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("MagicThreadDumpFile")) {
                  try {
                     this.bean.setMagicThreadDumpFile((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("MagicThreadDumpHost")) {
                  try {
                     this.bean.setMagicThreadDumpHost((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("MasterDeployer")) {
                  try {
                     this.bean.setMasterDeployer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("RedefiningClassLoader")) {
                  try {
                     this.bean.setRedefiningClassLoader(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Server")) {
                  this.bean.setServerAsString((String)var2);
               } else if (var1.equals("SlaveDeployer")) {
                  try {
                     this.bean.setSlaveDeployer(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("WebModule")) {
                  try {
                     this.bean.setWebModule(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("MagicThreadDumpEnabled")) {
                  try {
                     this.bean.setMagicThreadDumpEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var295) {
         System.out.println(var295 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var295;
      } catch (RuntimeException var296) {
         throw var296;
      } catch (Exception var297) {
         if (var297 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var297);
         } else if (var297 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var297.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var297);
         }
      }
   }
}
