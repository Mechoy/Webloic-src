package weblogic.application.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import javax.naming.Context;
import javax.security.jacc.PolicyConfiguration;
import javax.xml.stream.XMLStreamException;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.DescriptorUpdater;
import weblogic.application.Module;
import weblogic.application.ModuleManager;
import weblogic.application.SecurityRole;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.UpdateListener;
import weblogic.application.internal.library.LibraryManagerAggregate;
import weblogic.application.io.Ear;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryProvider;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.application.utils.XMLWriter;
import weblogic.deploy.event.ApplicationVersionLifecycleListenerAdapter;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.FilteringClassLoader;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.work.WorkManagerCollection;

public class ApplicationContextImpl implements ApplicationContextInternal, DescriptorUpdater, LibraryContext, FlowContext, UpdateListener.Registration {
   private AppDeploymentMBean duMBean;
   private final SystemResourceMBean srMBean;
   private final String appId;
   private final File applicationRootFile;
   private Context rootContext;
   private Context envContext;
   private GenericClassLoader appClassLoader;
   private ApplicationLifecycleListener[] listeners;
   private ApplicationVersionLifecycleListenerAdapter versionListenerAdapter;
   private ApplicationBean appDD;
   private WeblogicApplicationBean wlappDD;
   private WeblogicExtensionBean extDD;
   private final ModuleManager moduleManager;
   private Module[] startingModules;
   private Module[] stoppingModules;
   private String[] stoppedModuleIds;
   private File[] paths;
   private File descriptorCacheDir;
   private String securityRealmName;
   private J2EEApplicationRuntimeMBeanImpl appRuntimeMBean;
   private ApplicationFileManager afm;
   private Map ejbCacheMap;
   private Map ejbQueryCacheMap;
   private Map applicationParameters;
   private Map factoryMap;
   private WorkManagerCollection workManagerCollection;
   private Ear ear;
   private final LibraryManagerAggregate libAggr;
   private final List updateListeners;
   private ApplicationDescriptor appDesc;
   private final Collection policyConfigurations;
   private SplitDirectoryInfo splitInfo;
   private final boolean isInternalApp;
   private DomainMBean proposedDomain;
   private AuthenticatedSubject deploymentInitiator;
   private Map appRoleMappings;
   private Map appListenerIdentityMappings;
   private boolean isStaticDeployment;
   private boolean isAdminState;
   private boolean requiresRestart;
   private boolean isSplitDir;
   private int deploymentOperation;
   private AppDDHolder appDDHolder;
   private Map clToSchemaTypeLoader;
   private Map contextRootOverrideMap;
   private Map userObjects;
   private String[] partialRedeployURIs;
   private final Map<String, AppDeploymentExtension> appExtensions;
   private final Set<AppDeploymentExtension> appPreProcessorExtensions;
   private final Set<AppDeploymentExtension> appPostProcessorExtensions;
   private PersistenceUnitRegistry proposedPersistenceUnitRegistry;
   private Map<String, String> moduleURItoIdMap;

   ApplicationContextImpl(String var1) {
      this((AppDeploymentMBean)null, (SystemResourceMBean)null, var1, (File)null, false);
   }

   ApplicationContextImpl(AppDeploymentMBean var1, File var2) {
      this(var1, (SystemResourceMBean)null, var1.getApplicationIdentifier(), var2, var1.isInternalApp());
   }

   ApplicationContextImpl(SystemResourceMBean var1, File var2) {
      this((AppDeploymentMBean)null, var1, var1.getName(), var2, false);
   }

   public ApplicationContextImpl(String var1, File var2, ClassLoader var3) {
      this((AppDeploymentMBean)null, (SystemResourceMBean)null, var1, var2, false, false, var3);
   }

   private ApplicationContextImpl(AppDeploymentMBean var1, SystemResourceMBean var2, String var3, File var4, boolean var5) {
      this(var1, var2, var3, var4, var5, true, AugmentableClassLoaderManager.getAugmentableSystemClassLoader());
   }

   private ApplicationContextImpl(AppDeploymentMBean var1, SystemResourceMBean var2, String var3, File var4, boolean var5, boolean var6, ClassLoader var7) {
      this.listeners = new ApplicationLifecycleListener[0];
      this.moduleManager = new ModuleManager();
      this.startingModules = new Module[0];
      this.stoppingModules = new Module[0];
      this.stoppedModuleIds = null;
      this.paths = new File[0];
      this.ejbCacheMap = Collections.EMPTY_MAP;
      this.ejbQueryCacheMap = Collections.EMPTY_MAP;
      this.applicationParameters = Collections.EMPTY_MAP;
      this.factoryMap = Collections.EMPTY_MAP;
      this.libAggr = new LibraryManagerAggregate();
      this.updateListeners = new ArrayList();
      this.appDesc = null;
      this.policyConfigurations = new ArrayList();
      this.appRoleMappings = null;
      this.appListenerIdentityMappings = null;
      this.isStaticDeployment = false;
      this.isAdminState = false;
      this.requiresRestart = false;
      this.isSplitDir = false;
      this.appDDHolder = null;
      this.clToSchemaTypeLoader = new ConcurrentHashMap();
      this.userObjects = Collections.EMPTY_MAP;
      this.duMBean = var1;
      this.srMBean = var2;
      this.appId = var3;
      this.applicationRootFile = var4;
      this.isInternalApp = var5;
      if (var6) {
         this.workManagerCollection = new WorkManagerCollection(var3, var5);
      }

      FilteringClassLoader var8 = new FilteringClassLoader(var7);
      this.appClassLoader = new GenericClassLoader(new MultiClassFinder(), var8);
      this.appClassLoader.setAnnotation(new Annotation(var3));
      this.proposedPersistenceUnitRegistry = null;
      this.appExtensions = new HashMap();
      this.appPreProcessorExtensions = new HashSet();
      this.appPostProcessorExtensions = new HashSet();
   }

   public ApplicationMBean getApplicationMBean() {
      return this.duMBean == null ? null : this.duMBean.getAppMBean();
   }

   public BasicDeploymentMBean getBasicDeploymentMBean() {
      return (BasicDeploymentMBean)(this.duMBean != null ? this.duMBean : this.srMBean);
   }

   public AppDeploymentMBean getAppDeploymentMBean() {
      return this.duMBean;
   }

   public void setUpdatedAppDeploymentMBean(AppDeploymentMBean var1) {
      if (this.duMBean != null && var1 != null) {
         this.duMBean = var1;
      }

   }

   public SystemResourceMBean getSystemResourceMBean() {
      return this.srMBean;
   }

   public DomainMBean getProposedDomain() {
      return this.proposedDomain;
   }

   public void setProposedDomain(DomainMBean var1) {
      this.proposedDomain = var1;
   }

   public AuthenticatedSubject getDeploymentInitiator() {
      return this.deploymentInitiator;
   }

   public void setDeploymentInitiator(AuthenticatedSubject var1) {
      this.deploymentInitiator = var1;
   }

   public boolean requiresRestart() {
      return this.requiresRestart;
   }

   public void setRequiresRestart(boolean var1) {
      this.requiresRestart = var1;
   }

   public Context getEnvContext() {
      return this.envContext;
   }

   public void setEnvContext(Context var1) {
      this.envContext = var1;
   }

   public Context getRootContext() {
      return this.rootContext;
   }

   public void setRootContext(Context var1) {
      this.rootContext = var1;
   }

   public GenericClassLoader getAppClassLoader() {
      return this.appClassLoader;
   }

   public void resetAppClassLoader(GenericClassLoader var1) {
      this.appClassLoader = var1;
      Thread.currentThread().setContextClassLoader(this.appClassLoader);
   }

   public ApplicationLifecycleListener[] getApplicationListeners() {
      return this.listeners;
   }

   public void setApplicationListeners(ApplicationLifecycleListener[] var1) {
      if (var1 != null && var1.length != 0) {
         if (this.listeners.length == 0) {
            this.listeners = var1;
         } else {
            this.addListeners(var1);
         }

      }
   }

   public void addApplicationListener(ApplicationLifecycleListener var1) {
      this.setApplicationListeners(new ApplicationLifecycleListener[]{var1});
   }

   private void addListeners(ApplicationLifecycleListener[] var1) {
      ArrayList var2 = new ArrayList(this.listeners.length + var1.length);
      var2.addAll(Arrays.asList(this.listeners));
      var2.addAll(Arrays.asList(var1));
      this.listeners = (ApplicationLifecycleListener[])((ApplicationLifecycleListener[])var2.toArray(new ApplicationLifecycleListener[var2.size()]));
   }

   public void setApplicationVersionListenerAdapter(ApplicationVersionLifecycleListenerAdapter var1) {
      if (var1 == null && this.versionListenerAdapter != null) {
         this.versionListenerAdapter.cleanup();
      }

      this.versionListenerAdapter = var1;
   }

   public void setApplicationDescriptor(ApplicationDescriptor var1) throws IOException, XMLStreamException {
      this.appDesc = var1;
      this.appDD = var1.getApplicationDescriptor();
      this.wlappDD = var1.getWeblogicApplicationDescriptor();
      this.extDD = var1.getWeblogicExtensionDescriptor();
   }

   public ApplicationDescriptor getApplicationDescriptor() {
      return this.appDesc;
   }

   public WorkManagerCollection getWorkManagerCollection() {
      return this.workManagerCollection;
   }

   public ApplicationBean getApplicationDD() {
      return this.appDD;
   }

   public WeblogicApplicationBean getWLApplicationDD() {
      return this.wlappDD;
   }

   public WeblogicExtensionBean getWLExtensionDD() {
      return this.extDD;
   }

   public void setEar(Ear var1) {
      this.ear = var1;
   }

   public Ear getEar() {
      return this.ear;
   }

   public boolean isEar() {
      return this.ear != null;
   }

   public File[] getApplicationPaths() {
      return this.paths;
   }

   public void setApplicationPaths(File[] var1) {
      this.paths = var1;
   }

   public String getApplicationFileName() {
      return this.applicationRootFile == null ? null : this.applicationRootFile.getName();
   }

   public String getStagingPath() {
      return this.applicationRootFile.getPath();
   }

   public String getOutputPath() {
      if (this.afm == null) {
         throw new AssertionError("getOutputPath called too early!");
      } else {
         return this.afm.getOutputPath().getPath();
      }
   }

   public String getApplicationSecurityRealmName() {
      return this.securityRealmName;
   }

   public void setApplicationSecurityRealmName(String var1) {
      this.securityRealmName = var1;
   }

   public String getApplicationName() {
      return ApplicationVersionUtils.getApplicationName(this.appId);
   }

   public String getApplicationId() {
      return this.appId;
   }

   public J2EEApplicationRuntimeMBeanImpl getRuntime() {
      return this.appRuntimeMBean;
   }

   public void setRuntime(J2EEApplicationRuntimeMBeanImpl var1) {
      this.appRuntimeMBean = var1;
   }

   public ApplicationFileManager getApplicationFileManager() {
      return this.afm;
   }

   public void setApplicationFileManager(ApplicationFileManager var1) {
      this.afm = var1;
   }

   public SplitDirectoryInfo getSplitDirectoryInfo() {
      return this.splitInfo;
   }

   public void setSplitDirectoryInfo(SplitDirectoryInfo var1) {
      this.splitInfo = var1;
   }

   public Map getEJBCacheMap() {
      return this.ejbCacheMap;
   }

   public void setEJBCacheMap(Map var1) {
      if (var1 == null) {
         this.ejbCacheMap = Collections.EMPTY_MAP;
      } else {
         this.ejbCacheMap = var1;
      }

   }

   public Map getEJBQueryCacheMap() {
      return this.ejbQueryCacheMap;
   }

   public void setEJBQueryCacheMap(Map var1) {
      if (var1 == null) {
         this.ejbQueryCacheMap = Collections.EMPTY_MAP;
      } else {
         this.ejbQueryCacheMap = var1;
      }

   }

   public Map getApplicationParameters() {
      return this.applicationParameters;
   }

   public void setApplicationParameters(Map var1) {
      if (var1 == null) {
         this.applicationParameters = Collections.EMPTY_MAP;
      } else {
         this.applicationParameters = var1;
      }

   }

   public String getApplicationParameter(String var1) {
      return (String)this.applicationParameters.get(var1);
   }

   public InputStream getElement(String var1) throws IOException {
      VirtualJarFile var2 = this.afm.getVirtualJarFile();
      ZipEntry var3 = var2.getEntry(var1);
      return var3 == null ? null : var2.getInputStream(var3);
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public Module[] getApplicationModules() {
      return this.moduleManager.getModules();
   }

   public void setApplicationModules(Module[] var1) {
      this.moduleManager.setModules(var1);
   }

   public Module[] getStartingModules() {
      return this.startingModules;
   }

   public void setStartingModules(Module[] var1) {
      this.startingModules = var1;
   }

   public Module[] getStoppingModules() {
      return this.stoppingModules;
   }

   public void setStoppingModules(Module[] var1) {
      this.stoppingModules = var1;
   }

   public Map getCustomModuleFactories() {
      return this.factoryMap;
   }

   public void setCustomModuleFactories(Map var1) {
      this.factoryMap = var1;
   }

   public void addUpdateListener(UpdateListener var1) {
      this.updateListeners.add(var1);
   }

   public void removeUpdateListener(UpdateListener var1) {
      this.updateListeners.remove(var1);
   }

   public List getUpdateListeners() {
      return this.updateListeners;
   }

   public void addLibraryManager(String var1, LibraryManager var2) {
      this.libAggr.addLibraryManager(var1, var2);
   }

   public void removeLibraryManager(String var1, LibraryManager var2) {
      this.libAggr.removeLibraryManager(var1, var2);
   }

   public LibraryManagerAggregate getLibraryManagerAggregate() {
      return this.libAggr;
   }

   public LibraryProvider getLibraryProvider(String var1) {
      return this.libAggr.getLibraryProvider(var1);
   }

   public void addJACCPolicyConfiguration(PolicyConfiguration var1) {
      this.policyConfigurations.add(var1);
   }

   public PolicyConfiguration[] getJACCPolicyConfigurations() {
      return (PolicyConfiguration[])((PolicyConfiguration[])this.policyConfigurations.toArray(new PolicyConfiguration[this.policyConfigurations.size()]));
   }

   public boolean useJACC() {
      return SecurityServiceManager.isJACCEnabled() && !this.isInternalApp;
   }

   public void addClassFinder(ClassFinder var1) {
      this.getAppClassLoader().addClassFinder(var1);
   }

   public void registerLink(File var1) throws IOException {
      this.registerLink(var1.getName(), var1);
   }

   public void registerLink(String var1, File var2) throws IOException {
      EarUtils.linkURI(this.getEar(), this.getApplicationFileManager(), var1, var2);
   }

   public void notifyDescriptorUpdate() throws LoggableLibraryProcessingException {
      LibraryUtils.resetAppDDs(this.appDesc, this);
   }

   public String getRefappName() {
      return this.getApplicationName();
   }

   public DeploymentPlanBean findDeploymentPlan() {
      if (this.srMBean != null) {
         return null;
      } else {
         AppDeploymentMBean var1;
         if (this.proposedDomain == null) {
            var1 = this.duMBean;
         } else {
            var1 = this.proposedDomain.lookupAppDeployment(this.getApplicationId());
         }

         return var1 != null ? var1.getDeploymentPlanDescriptor() : null;
      }
   }

   public void writeDiagnosticImage(XMLWriter var1) {
      var1.addElement("name", this.getApplicationId());
      var1.addElement("internal", String.valueOf(this.isInternalApp));
      var1.addElement("paths");
      File[] var2 = this.getApplicationPaths();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.addElement("path", var2[var3].getAbsolutePath());
      }

      var1.closeElement();
      var1.addElement("classpath", this.getAppClassLoader().getClassPath());
      var1.addElement("modules");
      Module[] var5 = this.getModuleManager().getModules();

      for(int var4 = 0; var4 < var5.length; ++var4) {
         var1.addElement("module");
         var1.addElement("name", var5[var4].getId());
         var1.addElement("type", var5[var4].getType());
         var1.closeElement();
      }

      var1.closeElement();
      var1.addElement("libraries");
      this.libAggr.writeDiagnosticImage(var1);
      var1.closeElement();
   }

   public void setAppLevelRoleMappings(Map var1) {
      if (this.appRoleMappings != null) {
         throw new AssertionError("Application Role mappings cannot be reset");
      } else {
         this.appRoleMappings = var1;
      }
   }

   public void setAppListenerIdentityMappings(Map var1) {
      if (this.appListenerIdentityMappings != null) {
         throw new AssertionError("ApplicationLifecycleListener to run-as principal mappings cannot be reset");
      } else {
         this.appListenerIdentityMappings = var1;
      }
   }

   public AuthenticatedSubject getAppListenerIdentity(ApplicationLifecycleListener var1) {
      return this.appListenerIdentityMappings == null ? null : (AuthenticatedSubject)this.appListenerIdentityMappings.get(var1);
   }

   public SecurityRole getSecurityRole(String var1) {
      return this.appRoleMappings == null ? null : (SecurityRole)this.appRoleMappings.get(var1);
   }

   public File getDescriptorCacheDir() {
      return this.descriptorCacheDir;
   }

   public void setDescriptorCacheDir(File var1) {
      this.descriptorCacheDir = var1;
   }

   public boolean isStaticDeploymentOperation() {
      return this.isStaticDeployment;
   }

   public void setStaticDeploymentOperation(boolean var1) {
      this.isStaticDeployment = var1;
   }

   public void setAdminState(boolean var1) {
      this.isAdminState = var1;
   }

   public boolean isAdminState() {
      return this.isAdminState;
   }

   public boolean isInternalApp() {
      return this.isInternalApp;
   }

   public void setSplitDir() {
      this.isSplitDir = true;
   }

   public boolean isSplitDir() {
      return this.isSplitDir;
   }

   public boolean isRedeployOperation() {
      return this.deploymentOperation == 9;
   }

   public boolean isStopOperation() {
      return this.deploymentOperation == 8;
   }

   public int getDeploymentOperation() {
      return this.deploymentOperation;
   }

   public void setDeploymentOperation(int var1) {
      this.deploymentOperation = var1;
   }

   public void setAdditionalModuleUris(Map var1) {
      this.moduleManager.setAdditionalModuleUris(var1);
   }

   public void setPartialRedeployURIs(String[] var1) {
      this.partialRedeployURIs = var1;
   }

   public String[] getPartialRedeployURIs() {
      return this.partialRedeployURIs;
   }

   public AppDDHolder getProposedPartialRedeployDDs() {
      return this.appDDHolder;
   }

   public void setProposedPartialRedeployDDs(AppDDHolder var1) {
      this.appDDHolder = var1;
   }

   public Object getSchemaTypeLoader(ClassLoader var1) {
      return this.clToSchemaTypeLoader.get(var1);
   }

   public void setSchemaTypeLoader(ClassLoader var1, Object var2) {
      this.clToSchemaTypeLoader.put(var1, var2);
   }

   public void clear() {
      this.clToSchemaTypeLoader.clear();
   }

   public String getRefappUri() {
      return this.getEar().getURI();
   }

   public Map getContextRootOverrideMap() {
      return this.contextRootOverrideMap;
   }

   public void setContextRootOverrideMap(Map var1) {
      this.contextRootOverrideMap = var1;
   }

   public SubDeploymentMBean[] getLibrarySubDeployments() {
      return (new LibrarySubDeploymentFetcher()).getSubDeploymentMBeans();
   }

   public Object putUserObject(Object var1, Object var2) {
      if (this.userObjects == Collections.EMPTY_MAP) {
         this.userObjects = new HashMap();
      }

      return this.userObjects.put(var1, var2);
   }

   public Object getUserObject(Object var1) {
      return this.userObjects.get(var1);
   }

   public Object removeUserObject(Object var1) {
      return this.userObjects.remove(var1);
   }

   public void setProposedPersistenceUnitRegistry(PersistenceUnitRegistry var1) {
      this.proposedPersistenceUnitRegistry = var1;
   }

   public PersistenceUnitRegistry getProposedPersistenceUnitRegistry() {
      return this.proposedPersistenceUnitRegistry;
   }

   public void addAppDeploymentExtension(AppDeploymentExtension var1, FlowContext.ExtensionType var2) {
      this.appExtensions.put(var1.getName(), var1);
      if (var2 == FlowContext.ExtensionType.PRE) {
         this.appPreProcessorExtensions.add(var1);
      } else {
         this.appPostProcessorExtensions.add(var1);
      }

   }

   public Set<AppDeploymentExtension> getAppDeploymentExtensions(FlowContext.ExtensionType var1) {
      return var1 == FlowContext.ExtensionType.PRE ? this.appPreProcessorExtensions : this.appPostProcessorExtensions;
   }

   public String toString() {
      return this.appId;
   }

   public AppDeploymentExtension getAppDeploymentExtension(String var1) {
      return (AppDeploymentExtension)this.appExtensions.get(var1);
   }

   public void clearAppDeploymentExtensions() {
      this.appExtensions.clear();
      this.appPreProcessorExtensions.clear();
      this.appPostProcessorExtensions.clear();
   }

   public Map<String, String> getModuleURItoIdMap() {
      return this.moduleURItoIdMap;
   }

   public void setModuleURItoIdMap(Map<String, String> var1) {
      this.moduleURItoIdMap = var1;
   }

   public String[] getStoppedModules() {
      return this.stoppedModuleIds;
   }

   public void setStoppedModules(String[] var1) {
      this.stoppedModuleIds = var1;
   }

   private class LibrarySubDeploymentFetcher {
      private LibrarySubDeploymentFetcher() {
      }

      SubDeploymentMBean[] getSubDeploymentMBeans() {
         LibraryRuntimeMBean[] var1 = ApplicationContextImpl.this.getRuntime().getLibraryRuntimes();
         if (var1 != null && var1.length != 0) {
            WebLogicMBean var2 = ApplicationContextImpl.this.getBasicDeploymentMBean().getParent();
            DescriptorBean var3 = (DescriptorBean)var2;
            DomainMBean var4 = (DomainMBean)var3.getDescriptor().getRootBean();
            ArrayList var5 = new ArrayList();

            for(int var6 = 0; var6 < var1.length; ++var6) {
               String var7 = var1[var6].getLibraryIdentifier();
               LibraryMBean var8 = var4.lookupLibrary(var7);
               if (var8 != null) {
                  SubDeploymentMBean[] var9 = var8.getSubDeployments();
                  if (var9 != null && var9.length > 0) {
                     var5.addAll(Arrays.asList(var9));
                  }
               }
            }

            return (SubDeploymentMBean[])((SubDeploymentMBean[])var5.toArray(new SubDeploymentMBean[var5.size()]));
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      LibrarySubDeploymentFetcher(Object var2) {
         this();
      }
   }
}
