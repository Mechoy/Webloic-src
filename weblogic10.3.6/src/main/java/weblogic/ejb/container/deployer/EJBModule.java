package weblogic.ejb.container.deployer;

import com.bea.wls.redef.RedefiningClassLoader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import kodo.jdbc.conf.descriptor.PersistenceUnitConfigurationBean;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ConcurrentModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.Type;
import weblogic.application.UpdateListener;
import weblogic.application.internal.BaseJ2EEModule;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryConstants;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReferencer;
import weblogic.cacheprovider.coherence.CoherenceClusterManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deployment.AbstractPersistenceUnitRegistry;
import weblogic.deployment.EnvironmentException;
import weblogic.deployment.ModulePersistenceUnitRegistry;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.deployment.PersistenceUnitRegistryInitializer;
import weblogic.deployment.PersistenceUnitRegistryProvider;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.spi.EJBDeploymentException;
import weblogic.ejb.spi.EJBJar;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.ejb.spi.InvalidationMessage;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.ActivationConfigPropertyBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.PersistenceUnitBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBean;
import weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBean;
import weblogic.j2ee.descriptor.wl.CoherenceClusterRefBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.EjbBean;
import weblogic.j2ee.descriptor.wl.EntityCacheRefBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.StatelessSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.compiler.jdt.JDTJavaCompilerFactory;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.work.WorkManagerCollection;
import weblogic.work.WorkManagerRuntimeMBeanImpl;
import weblogic.work.WorkManagerService;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public class EJBModule extends BaseJ2EEModule implements Module, UpdateListener, PersistenceUnitRegistryProvider, ConcurrentModule {
   private Set<String> ejbInterfaces = new HashSet();
   private static final DebugLogger debugLogger;
   private static final Map ejbModules;
   private EJBComponentRuntimeMBeanImpl compRTMBean;
   private VirtualJarFile jf;
   private EJBDeployer ejbDeployer;
   private String applicationName;
   private String name;
   private boolean isEnableBeanClassRedeploy = false;
   private final Map implClassToNameMap = new HashMap();
   private final List updateImplClasses = new ArrayList();
   protected GenericClassLoader classLoader;
   private ClassFinder classFinder;
   private String altDD = null;
   protected ApplicationContextInternal appCtx;
   private final String uri;
   private EjbDescriptorBean ejbDescriptor;
   private EjbDescriptorBean proposedDescriptor;
   private EJBJar ejbJar;
   private AbstractPersistenceUnitRegistry persistenceUnitRegistry;
   private AbstractPersistenceUnitRegistry proposedPersistenceUnitRegistry;
   private LibraryManager manager = null;

   public EJBModule(String var1) {
      this.uri = var1;
   }

   public String getId() {
      return this.uri;
   }

   public String getURI() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_EJB;
   }

   public GenericClassLoader getClassLoader() {
      return this.classLoader;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[]{this.compRTMBean};
   }

   public DescriptorBean[] getDescriptors() {
      return this.ejbDescriptor.getWeblogicEjbJarBean() == null ? new DescriptorBean[]{(DescriptorBean)this.ejbDescriptor.getEjbJarBean()} : new DescriptorBean[]{(DescriptorBean)this.ejbDescriptor.getEjbJarBean(), (DescriptorBean)this.ejbDescriptor.getWeblogicEjbJarBean()};
   }

   private ClassFinder buildClassFinder(ApplicationContextInternal var1) throws IOException {
      this.ejbJar = new EJBJar(this.uri, var1);
      ApplicationFileManager var2 = var1.getApplicationFileManager();
      this.jf = var2.getVirtualJarFile(this.uri);
      Debug.assertion(this.jf != null);
      return this.ejbJar.getClassFinder();
   }

   protected void setupPersistenceUnitRegistry() throws DeploymentException {
      try {
         if (this.persistenceUnitRegistry == null) {
            this.persistenceUnitRegistry = new ModulePersistenceUnitRegistry(this.classLoader, this.appCtx, this, true);
         }

      } catch (EnvironmentException var2) {
         throw new DeploymentException(var2);
      } catch (MalformedURLException var3) {
         throw new DeploymentException(var3);
      }
   }

   private void initMBeans() throws ModuleException {
      EJBComponentMBean var1 = this.getEJBComponent();
      if (var1 != null) {
         this.name = var1.getName();
      } else {
         this.name = this.getURI();
      }

      this.applicationName = this.appCtx.getApplicationName();

      try {
         this.compRTMBean = new EJBComponentRuntimeMBeanImpl(this.getName(), this.getId(), this.appCtx.getRuntime(), this.applicationName);
         if (this.persistenceUnitRegistry != null) {
            this.persistenceUnitRegistry.setParentRuntimeMBean(this.compRTMBean);
         }

      } catch (ManagementException var3) {
         throw new ModuleException("Error creating RuntimeMBean for EJBModule '" + this + "': " + var3.getMessage(), var3);
      } catch (EnvironmentException var4) {
         throw new ModuleException("Error creating RuntimeMBean for EJBModule '" + this + "': " + var4.getMessage(), var4);
      }
   }

   private void initJNDIContext() {
      Context var1 = this.appCtx.getEnvContext();

      try {
         var1.lookup("/ejb");
      } catch (NameNotFoundException var5) {
         try {
            var1.createSubcontext("ejb");
         } catch (NamingException var4) {
            throw new AssertionError(var4);
         }
      } catch (NamingException var6) {
         throw new AssertionError(var6);
      }

   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.init(var1, var2, var3);
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.appCtx = (ApplicationContextInternal)var1;
      this.classLoader = var2;
      this.initJNDIContext();
      var3.addUpdateListener(this);

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("init() on module : " + this + ".");
         }

         this.classFinder = this.buildClassFinder(this.appCtx);
         this.classLoader.addClassFinder(this.classFinder);
         PersistenceUnitRegistryInitializer var4 = PersistenceUnitRegistryInitializer.getInstance(this.appCtx);
         var4.addPersistenceUnitRegistryPrepareTask(new PersistenceUnitRegistryInitializer.PersistenceUnitRegistryPrepareTask() {
            public void execute() throws ModuleException {
               try {
                  EJBModule.this.setupPersistenceUnitRegistry();
               } catch (DeploymentException var2) {
                  throw new ModuleException(var2);
               }
            }
         });
         return this.classLoader;
      } catch (IOException var5) {
         throw new ModuleException(var5);
      }
   }

   public void remove() throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("remove() on module : " + this + ".");
      }

      JDTJavaCompilerFactory.getInstance().resetCache(this.getClassLoader());
      if (this.ejbJar != null) {
         this.ejbJar.remove();
      }

      if (this.ejbDeployer != null) {
         this.ejbDeployer.remove();
         this.ejbDeployer.removeEJBTimers();
      }

      this.ejbDeployer = null;
   }

   public void adminToProduction() {
      if (debugLogger.isDebugEnabled()) {
         debug("adminToProduction() on module : " + this + ".");
      }

   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("gracefulProductionToAdmin() on module : " + this + ".");
      }

   }

   public void forceProductionToAdmin() {
      if (debugLogger.isDebugEnabled()) {
         debug("forceProductionToAdmin() on module : " + this + ".");
      }

   }

   public ComponentRuntimeMBean getRuntimeMBean() {
      return this.compRTMBean;
   }

   public final void prepare() throws ModuleException {
      try {
         PersistenceUnitRegistryInitializer var1 = PersistenceUnitRegistryInitializer.getInstance(this.appCtx);
         var1.setupPersistenceUnitRegistries();
         this.initMBeans();
         this.ejbDescriptor = this.loadEJBDescriptor();
         if (this.classLoader instanceof RedefiningClassLoader) {
            this.fillInterfaceInfoForEJBModule(this.ejbDescriptor);
            this.setupRedefiningClassLoader(this.classLoader);
         }

         if (debugLogger.isDebugEnabled()) {
            debug("prepare() on module : " + this + ".");
         }

         WeblogicEjbJarBean var2 = this.ejbDescriptor.getWeblogicEjbJarBean();
         this.isEnableBeanClassRedeploy = var2.isEnableBeanClassRedeploy();
         this.setupEJBToImplClassDependencies(this.classLoader);
         if (debugLogger.isDebugEnabled()) {
            this.dumpDependencyMaps();
         }

         if (this.isEnableBeanClassRedeploy) {
            Iterator var3 = this.implClassToNameMap.keySet().iterator();

            while(var3.hasNext()) {
               this.classLoader.excludeClass((String)var3.next());
            }
         }

         WorkManagerCollection var6 = this.appCtx.getWorkManagerCollection();
         var6.populate(this.getName(), this.ejbDescriptor.getWeblogicEjbJarBean());
         this.addWorkManagerRuntimes(var6.getWorkManagers(this.getName()));
         this.setupCoherenceCaches();
         this.ejbDeployer = new EJBDeployer(this.appCtx, this, this.compRTMBean);
         this.ejbDeployer.prepare(this.jf, this.classLoader, this.ejbDescriptor, this.appCtx.getEnvContext(), this.appCtx.getEJBCacheMap(), this.appCtx.getEJBQueryCacheMap());
      } catch (DeploymentException var5) {
         try {
            this.unprepare();
         } catch (ModuleException var4) {
            EJBLogger.logExcepionUninitializing(this.getURI(), var4);
         }

         throw new ModuleException("Exception preparing module: " + this + "\n" + var5.getMessage(), var5);
      }
   }

   public final void activate() throws IllegalStateException, ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("activate() on module : " + this + " : activating module");
      }

      JDTJavaCompilerFactory.getInstance().resetCache(this.getClassLoader());
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(this.classLoader);

      try {
         this.ejbDeployer.activate(this.ejbDescriptor, this.classLoader, this.appCtx.getEnvContext());
      } catch (DeploymentException var7) {
         if (debugLogger.isDebugEnabled()) {
            debug("Error activating module : " + this + " :\n" + var7.getMessage());
         }

         this.doDeactivate();
         throw new ModuleException("Exception activating module: " + this + "\n" + var7.getMessage(), var7);
      } finally {
         Thread.currentThread().setContextClassLoader(var1);
      }

      ejbModules.put(new ModuleKey(this.appCtx.getApplicationId(), this.getName()), this);
      this.registerBeanUpdateListeners();
      this.reconfigPersistenceUnits();
   }

   private void reconfigPersistenceUnits() throws ModuleException {
      PersistenceUnitRegistry var1 = this.getPersistenceUnitRegistry();
      if (var1 != null) {
         Collection var2 = var1.getPersistenceUnitNames();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            PersistenceUnitInfoImpl var5 = var1.getPersistenceUnit(var4);

            try {
               var5.activate(this.appCtx.getEnvContext());
            } catch (EnvironmentException var7) {
               throw new ModuleException("Error activating JPA deployment:", var7);
            }
         }

      }
   }

   private void addWorkManagerRuntimes(List var1) throws DeploymentException {
      try {
         Iterator var2 = var1.iterator();
         J2EEApplicationRuntimeMBeanImpl var3 = this.appCtx.getRuntime();

         while(var2.hasNext()) {
            WorkManagerRuntimeMBean var4 = WorkManagerRuntimeMBeanImpl.getWorkManagerRuntime(((WorkManagerService)var2.next()).getDelegate(), var3, this.compRTMBean);
            if (var4 != null) {
               this.compRTMBean.addWorkManagerRuntime(var4);
            }
         }

      } catch (ManagementException var5) {
         throw new DeploymentException("unable to create WorkManagerRuntimeMBean", var5);
      }
   }

   private boolean startMdbsWithApplication() {
      WeblogicApplicationBean var1 = this.appCtx.getWLApplicationDD();
      if (var1 == null) {
         return false;
      } else {
         EjbBean var2 = var1.getEjb();
         return var2 == null ? false : var2.isStartMdbsWithApplication();
      }
   }

   public final void start() throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("start() on module : " + this + " : starting module");
      }

      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(this.classLoader);

      try {
         this.ejbDeployer.start(!this.startMdbsWithApplication());
      } catch (EJBDeploymentException var7) {
         throw new ModuleException("Exception starting module: " + this + "\n" + var7.getMessage(), var7);
      } finally {
         Thread.currentThread().setContextClassLoader(var1);
      }

   }

   public final void deactivate() throws IllegalStateException, ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("deactivate() on module : " + this + ".");
      }

      ejbModules.remove(new ModuleKey(this.appCtx.getApplicationId(), this.getName()));
      this.unregisterBeanUpdateListeners();
      this.doDeactivate();
   }

   public boolean acceptURI(String var1) {
      if (!this.acceptModuleUri(this.appCtx, this.uri, var1)) {
         debug("acceptURI: does not acceptModuleURI: " + var1);
         return false;
      } else if (this.getEjbDDName(var1) != null) {
         debug("getEjbDDName(u) != null: " + var1);
         debug("acceptURI: does acceptModuleURI: " + var1);
         return true;
      } else if (this.getPersistenceDDName(var1) != null) {
         debug("getPersistenceDDName(u) != null: " + var1);
         debug("acceptURI: does acceptModuleURI: " + var1);
         return true;
      } else if (this.findImplClassName(var1) != null) {
         debug("findImplClassName(u) != null: " + var1);
         debug("acceptURI: does acceptModuleURI: " + var1);
         return true;
      } else {
         debug("acceptURI: none of the cases applies, therefore no acceptance of uri: " + var1);
         return false;
      }
   }

   private String findImplClassName(String var1) {
      if (!var1.endsWith(".class")) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer();
         if (this.appCtx.isEar()) {
            var2.append(var1.substring(this.uri.length() + 1));
         } else {
            var2.append(var1);
         }

         var2.setLength(var2.length() - 6);
         if (var2.length() == 0) {
            return null;
         } else {
            for(int var3 = 0; var3 < var2.length(); ++var3) {
               char var4 = var2.charAt(var3);
               if (var4 == '\\' || var4 == '/') {
                  var2.setCharAt(var3, '.');
               }
            }

            String var5 = var2.toString();
            if (this.implClassToNameMap.containsKey(var5)) {
               return var5;
            } else {
               return null;
            }
         }
      }
   }

   private String getEjbDDName(String var1) {
      if (!var1.endsWith("xml")) {
         return null;
      } else {
         String var2 = var1.replace('\\', '/');
         if (this.appCtx.isEar()) {
            var2 = var2.substring(this.uri.length() + 1);
         }

         if (var2.length() == 0) {
            return null;
         } else if (var2.equals("META-INF/ejb-jar.xml")) {
            return var2;
         } else if (var2.equals("META-INF/weblogic-ejb-jar.xml")) {
            return var2;
         } else {
            WeblogicEjbJarBean var3 = this.ejbDescriptor.getWeblogicEjbJarBean();
            if (var3 != null) {
               WeblogicEnterpriseBeanBean[] var4 = var3.getWeblogicEnterpriseBeans();

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  EntityDescriptorBean var6 = var4[var5].getEntityDescriptor();
                  if (var6 != null) {
                     PersistenceBean var7 = var6.getPersistence();
                     if (var7 != null) {
                        PersistenceUseBean var8 = var7.getPersistenceUse();
                        if (var8 != null) {
                           String var9 = var8.getTypeStorage();
                           var9 = var9.replace('\\', '/');
                           if (var2.equals(var9)) {
                              EjbJarBean var10 = this.ejbDescriptor.getEjbJarBean();
                              EnterpriseBeansBean var11 = var10.getEnterpriseBeans();
                              EntityBeanBean[] var12 = var11.getEntities();

                              for(int var13 = 0; var13 < var12.length; ++var13) {
                                 if (var4[var5].getEjbName().equals(var12[var13].getEjbName())) {
                                    String var14 = var12[var13].getCmpVersion();
                                    if (var14.equals("2.x")) {
                                       return var2;
                                    }

                                    return null;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   private String getPersistenceDDName(String var1) {
      if (!var1.endsWith("xml")) {
         return null;
      } else {
         String var2 = var1.replace('\\', '/');
         if (this.appCtx.isEar()) {
            var2 = var2.substring(this.uri.length() + 1);
            debug("getPersistenceDDName: candidate is " + var2);
         }

         if (var2.length() == 0) {
            return null;
         } else if (var2.equals("META-INF/persistence.xml")) {
            return var2;
         } else {
            return var2.equals("META-INF/persistence-configuration.xml") ? var2 : null;
         }
      }
   }

   public void prepareUpdate(String var1) throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("prepareUpdate called for app: " + this.appCtx.getApplicationName() + " uri: " + this.uri);
      }

      if (var1.endsWith(".xml")) {
         this.prepareDescriptorUpdate(var1);
      } else {
         this.updateImplClasses.clear();
         String var2 = this.findImplClassName(var1);
         if (var2 != null) {
            this.updateImplClasses.add(var2);
         }

         if (!this.updateImplClasses.isEmpty()) {
            if (!this.isEnableBeanClassRedeploy) {
               EJBLogger.logEJBModuleRolledBackSinceImplCLDisabled(this.getDisplayName(), (String)this.updateImplClasses.get(0));
               throw new ModuleException("Unable to update bean implementation class for EJB " + var1 + " since enable-bean-class-redeploy was not" + " enabled in the weblogic-ejb-jar.xml");
            } else {
               GenericClassLoader var3 = new GenericClassLoader(this.classFinder);

               try {
                  String var4 = this.ejbDeployer.needsRecompile(this.updateImplClasses, var3);
                  if (var4 != null) {
                     EJBLogger.logEJBModuleRolledBackSinceChangeIncompatible(this.getDisplayName(), var4);
                     if (debugLogger.isDebugEnabled()) {
                        debug("needsRecompile returned: " + var4);
                     }

                     throw new ModuleException("Attempt to update EJB implementation class in EJB " + var1 + " failed because " + var4 + "requires weblogic.appc to be run.  You must redeploy " + "the application.");
                  } else {
                     if (debugLogger.isDebugEnabled()) {
                        debug("needsRecompile returned false");
                     }

                  }
               } catch (ClassNotFoundException var5) {
                  if (debugLogger.isDebugEnabled()) {
                     debug("ClassNotFoundException during needsRecompile: " + var5);
                     var5.printStackTrace();
                  }

                  throw new ModuleException(var5);
               }
            }
         }
      }
   }

   public void activateUpdate(String var1) throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("activateUpdate() on module : " + this + ".");
      }

      if (var1.endsWith(".xml")) {
         this.activateDescriptorUpdate(var1);
      } else if (!this.updateImplClasses.isEmpty()) {
         if (debugLogger.isDebugEnabled()) {
            debug("Updating beanImpl ClassLoader for EJBs:");
         }

         Iterator var2 = this.updateImplClasses.iterator();
         HashSet var3 = new HashSet();

         while(var2.hasNext()) {
            String var4 = (String)var2.next();
            var3.addAll((List)this.implClassToNameMap.get(var4));
         }

         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            String var5 = (String)var8.next();
            if (debugLogger.isDebugEnabled()) {
               debug(var5);
            }

            try {
               this.ejbDeployer.updateImplClassLoader(var5);
            } catch (WLDeploymentException var7) {
               throw new ModuleException("Module :" + var1 + " cannot be redeployed: " + var7);
            }
         }

      }
   }

   public void rollbackUpdate(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("rollbackUpdate() on module : " + this + ".");
      }

      if (var1.endsWith(".xml")) {
         this.rollbackDescriptorUpdate(var1);
      }
   }

   private Descriptor getDescriptor(EjbDescriptorBean var1, String var2) {
      if (var2.equals("META-INF/ejb-jar.xml")) {
         return ((DescriptorBean)var1.getEjbJarBean()).getDescriptor();
      } else {
         return var2.equals("META-INF/weblogic-ejb-jar.xml") ? ((DescriptorBean)var1.getWeblogicEjbJarBean()).getDescriptor() : ((DescriptorBean)var1.getWeblogicRdbmsJarBean(var2)).getDescriptor();
      }
   }

   private void prepareDescriptorUpdate(String var1) throws ModuleException {
      String var2 = this.getEjbDDName(var1);
      debug("prepareDescriptorUpdate: EJB descriptorName = " + var2);
      if (var2 != null) {
         if (this.proposedDescriptor == null) {
            this.proposedDescriptor = this.loadEJBDescriptor();
         }

         if (!var2.equals("META-INF/ejb-jar.xml") && !var2.equals("META-INF/weblogic-ejb-jar.xml")) {
            String var3 = "6.0";

            try {
               ProcessorFactory var4 = new ProcessorFactory();
               EJBDescriptorMBeanUtils.loadRDBMSDescriptor(this.proposedDescriptor, this.jf, var2, var2, var3, var4, true);
            } catch (Exception var9) {
               throw new ModuleException(var9);
            }
         }

         try {
            Descriptor var10 = this.getDescriptor(this.ejbDescriptor, var2);
            var10.prepareUpdate(this.getDescriptor(this.proposedDescriptor, var2));
         } catch (DescriptorUpdateRejectedException var8) {
            throw new ModuleException(var8);
         }
      } else {
         var2 = this.getPersistenceDDName(var1);
         debug("prepareDescriptorUpdate: persistence descriptorName = " + var2);

         assert var2 != null;

         if (this.proposedPersistenceUnitRegistry == null) {
            try {
               this.proposedPersistenceUnitRegistry = new ModulePersistenceUnitRegistry(this.classLoader, this.appCtx, this, false);
            } catch (EnvironmentException var6) {
               throw new ModuleException(var6);
            } catch (MalformedURLException var7) {
               throw new ModuleException(var7);
            }
         }

         try {
            ModulePersistenceUnitRegistry var11 = (ModulePersistenceUnitRegistry)this.getPersistenceUnitRegistry();
            Descriptor var12 = var11.getDescriptor(var2);
            var11 = (ModulePersistenceUnitRegistry)this.getProposedPersistenceUnitRegistry();
            var12.prepareUpdate(var11.getDescriptor(var2));
         } catch (DescriptorUpdateRejectedException var5) {
            throw new ModuleException(var5);
         }
      }

   }

   private void activateDescriptorUpdate(String var1) throws ModuleException {
      String var2 = this.getEjbDDName(var1);
      if (var2 != null) {
         this.proposedDescriptor = null;
         Descriptor var3 = this.getDescriptor(this.ejbDescriptor, var2);

         try {
            var3.activateUpdate();
         } catch (DescriptorUpdateFailedException var7) {
            throw new ModuleException(var7);
         }
      } else {
         var2 = this.getPersistenceDDName(var1);

         assert var2 != null;

         this.proposedPersistenceUnitRegistry = null;
         ModulePersistenceUnitRegistry var8 = (ModulePersistenceUnitRegistry)this.getPersistenceUnitRegistry();
         Descriptor var4 = var8.getDescriptor(var2);

         try {
            var4.activateUpdate();
         } catch (DescriptorUpdateFailedException var6) {
            throw new ModuleException(var6);
         }
      }

   }

   private void rollbackDescriptorUpdate(String var1) {
      String var2 = this.getEjbDDName(var1);
      if (var2 != null) {
         this.proposedDescriptor = null;
         Descriptor var3 = this.getDescriptor(this.ejbDescriptor, var2);
         var3.rollbackUpdate();
      } else {
         var2 = this.getPersistenceDDName(var1);

         assert var2 != null;

         this.proposedPersistenceUnitRegistry = null;
         ModulePersistenceUnitRegistry var5 = (ModulePersistenceUnitRegistry)this.getPersistenceUnitRegistry();
         Descriptor var4 = var5.getDescriptor(var2);
         var4.rollbackUpdate();
      }

   }

   public void invalidate(InvalidationMessage var1) {
      this.ejbDeployer.invalidate(var1);
   }

   public String getObjectSchemaName() {
      return this.ejbDeployer.getObjectSchemaName();
   }

   public Map getDataBeansMap() {
      return this.ejbDeployer.getDataBeansMap();
   }

   public String toString() {
      String var1 = this.name == null ? this.getURI() : this.name;
      return "EJBModule(" + var1 + ")";
   }

   protected EjbDescriptorBean loadEJBDescriptor() throws ModuleException {
      File var1 = this.resolveAltDD(this.appCtx, this.uri);
      DeploymentPlanBean var2 = this.appCtx.findDeploymentPlan();
      File var3 = null;
      if (var2 != null) {
         AppDeploymentMBean var4 = this.appCtx.getAppDeploymentMBean();
         if (var4.getPlanDir() != null) {
            var3 = new File(var4.getLocalPlanDir());
         }
      }

      Debug.assertion(this.jf != null);

      Loggable var5;
      try {
         if (this.manager == null) {
            this.manager = new LibraryManager(new LibraryReferencer(this.uri, this.compRTMBean, "Unresolved library references for module " + this.uri));
         }

         this.manager.lookupAndAddAutoReferences(Type.EJB, LibraryConstants.AutoReferrer.EJBApp);
         Library[] var14 = this.manager.getAutoReferencedLibraries();
         VirtualJarFile[] var15 = null;
         if (var14 != null && var14.length > 0) {
            var15 = new VirtualJarFile[var14.length];

            for(int var6 = 0; var6 < var14.length; ++var6) {
               File var7 = var14[var6].getLocation();
               var15[var6] = VirtualJarFactory.createVirtualJar(var7);
               JarClassFinder var8 = new JarClassFinder(var7);
               this.classLoader.addClassFinder(var8);
            }
         }

         return EjbDescriptorFactory.createReadOnlyDescriptorFromJarFile(this.jf, var1, var3, var2, this.appCtx.getApplicationId(), this.uri, this.classLoader, var15);
      } catch (IOException var9) {
         var5 = EJBLogger.logErrorReadingDDLoggable(var9.getMessage());
         throw new ModuleException(var5.getMessage(), var9);
      } catch (XMLParsingException var10) {
         var5 = EJBLogger.logXmlParsingErrorLoggable(var10.getMessage());
         throw new ModuleException(var5.getMessage(), var10);
      } catch (XMLProcessingException var11) {
         var5 = EJBLogger.logXmlProcessingErrorLoggable(var11.getMessage());
         throw new ModuleException(var5.getMessage(), var11);
      } catch (XMLStreamException var12) {
         var5 = EJBLogger.logXmlProcessingErrorLoggable(var12.getMessage());
         throw new ModuleException(var5.getMessage(), var12);
      } catch (Throwable var13) {
         var13.printStackTrace();
         var5 = EJBLogger.logErrorReadingDDLoggable(var13.getMessage());
         throw new ModuleException(var5.getMessage(), var13);
      }
   }

   private void closeJarFile() {
      if (this.jf != null) {
         try {
            this.jf.close();
            this.jf = null;
         } catch (Exception var2) {
            EJBLogger.logStackTrace(var2);
         }

      }
   }

   EJBComponentMBean getEJBComponent() throws ModuleException {
      return (EJBComponentMBean)this.findComponentMBeanInternal(this.appCtx, this.uri, EJBComponentMBean.class);
   }

   public void unprepare() throws ModuleException {
      EJBComponentMBean var1 = this.getEJBComponent();

      try {
         if (debugLogger.isDebugEnabled()) {
            debug("unprepare() on module : " + this + ".");
         }

         if (var1 != null) {
            this.appCtx.getWorkManagerCollection().removeModuleEntries(this.getName());
         }

         if (this.ejbDeployer != null) {
            this.ejbDeployer.rollback(this.appCtx.getEnvContext());
         }

         if (this.persistenceUnitRegistry != null) {
            this.persistenceUnitRegistry.close();
            this.persistenceUnitRegistry = null;
         }

         this.releaseCoherenceCaches();
      } catch (NamingException var28) {
         throw new ModuleException(var28);
      } finally {
         if (this.manager != null) {
            this.manager.removeReferences();
            this.manager = null;
         }

         if (var1 != null) {
            var1.setEJBComponentRuntime((EJBComponentRuntimeMBean)null);
         }

         if (this.compRTMBean != null) {
            try {
               this.compRTMBean.unregister();
            } catch (ManagementException var26) {
               throw new ModuleException("Error unregistering RuntimeMBean: " + var26.getMessage(), var26);
            } finally {
               this.compRTMBean = null;
            }
         }

      }

   }

   private void doDeactivate() {
      this.ejbDeployer.deactivate();
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debug("destroy() on module : " + this + ".");
      }

      var1.removeUpdateListener(this);
      if (this.classFinder != null) {
         this.classFinder.close();
      }

      this.closeJarFile();
      this.classLoader = null;
      this.classFinder = null;
   }

   public String getName() {
      return this.name == null ? this.getURI() : this.name;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   private String getDisplayName() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getName());
      var1.append("(Application: ");
      var1.append(this.getApplicationName());
      var1.append(")");
      return var1.toString();
   }

   public void setAltDD(String var1) {
      this.altDD = var1;
   }

   public String getAltDD() {
      return this.altDD;
   }

   private void registerBeanUpdateListeners() {
      HashMap var1 = new HashMap();
      WeblogicEjbJarBean var2 = this.ejbDescriptor.getWeblogicEjbJarBean();
      WeblogicEnterpriseBeanBean[] var3 = var2.getWeblogicEnterpriseBeans();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var1.put(var3[var4].getEjbName(), var3[var4]);
      }

      Collection var18 = this.ejbDeployer.getBeanInfos();
      Iterator var5 = var18.iterator();

      BeanInfo var6;
      while(var5.hasNext()) {
         var6 = (BeanInfo)var5.next();

         assert var1.containsKey(var6.getEJBName());

         WeblogicEnterpriseBeanBean var7 = (WeblogicEnterpriseBeanBean)var1.get(var6.getEJBName());
         TransactionDescriptorBean var8 = var7.getTransactionDescriptor();
         DescriptorBean var9 = (DescriptorBean)var8;
         var9.addBeanUpdateListener(var6);
         if (var6 instanceof EntityBeanInfo) {
            EntityBeanInfo var22 = (EntityBeanInfo)var6;
            EntityDescriptorBean var26 = var7.getEntityDescriptor();
            if (var26.getEntityCacheRef() != null) {
               EntityCacheRefBean var27 = var26.getEntityCacheRef();
               var9 = (DescriptorBean)var27;
               var9.addBeanUpdateListener(var6);
               WeblogicApplicationBean var32 = this.appCtx.getWLApplicationDD();
               EjbBean var35 = var32.getEjb();
               ApplicationEntityCacheBean[] var37 = var35.getEntityCaches();

               for(int var40 = 0; var40 < var37.length; ++var40) {
                  if (var27.getEntityCacheName().equals(var37[var40].getEntityCacheName())) {
                     var9 = (DescriptorBean)var37[var40];
                     var9.addBeanUpdateListener(var6);
                  }
               }
            } else {
               var9 = (DescriptorBean)var26.getEntityCache();
               var9.addBeanUpdateListener(var6);
            }

            var9 = (DescriptorBean)var26.getPool();
            var9.addBeanUpdateListener(var6);
            if (!var22.getIsBeanManagedPersistence() && var22.getCMPInfo().uses20CMP()) {
               WeblogicRdbmsJarBean[] var30 = this.ejbDescriptor.getWeblogicRdbmsJarBeans();

               for(int var33 = 0; var33 < var30.length; ++var33) {
                  WeblogicRdbmsBeanBean[] var36 = var30[var33].getWeblogicRdbmsBeans();

                  for(int var39 = 0; var39 < var36.length; ++var39) {
                     if (var22.getEJBName().equals(var36[var39].getEjbName())) {
                        AutomaticKeyGenerationBean var42 = var36[var39].getAutomaticKeyGeneration();
                        if (var42 != null) {
                           var9 = (DescriptorBean)var42;
                           var9.addBeanUpdateListener(var6);
                        }
                     }
                  }
               }
            }
         } else if (var6 instanceof SessionBeanInfo) {
            SessionBeanInfo var21 = (SessionBeanInfo)var6;
            if (var21.isStateful()) {
               StatefulSessionDescriptorBean var24 = var7.getStatefulSessionDescriptor();
               var9 = (DescriptorBean)var24.getStatefulSessionCache();
               var9.addBeanUpdateListener(var6);
            } else {
               StatelessSessionDescriptorBean var25 = var7.getStatelessSessionDescriptor();
               var9 = (DescriptorBean)var25.getPool();
               var9.addBeanUpdateListener(var6);
            }
         } else {
            if (!(var6 instanceof MessageDrivenBeanInfo)) {
               throw new AssertionError("Unknown BeanInfo type!");
            }

            MessageDrivenDescriptorBean var10 = var7.getMessageDrivenDescriptor();
            var9 = (DescriptorBean)var10;
            var9.addBeanUpdateListener(var6);
            var9 = (DescriptorBean)var10.getPool();
            var9.addBeanUpdateListener(var6);
            EjbJarBean var11 = this.ejbDescriptor.getEjbJarBean();
            EnterpriseBeansBean var12 = var11.getEnterpriseBeans();
            MessageDrivenBeanBean[] var13 = var12.getMessageDrivens();

            for(int var14 = 0; var14 < var13.length; ++var14) {
               if (var6.getEJBName().equals(var13[var14].getEjbName())) {
                  ActivationConfigBean var15 = var13[var14].getActivationConfig();
                  ActivationConfigPropertyBean[] var16 = var15.getActivationConfigProperties();

                  for(int var17 = 0; var17 < var16.length; ++var17) {
                     if (DDConstants.updateElementSet.contains(var16[var17].getActivationConfigPropertyName().toUpperCase())) {
                        var9 = (DescriptorBean)var16[var17];
                        var9.addBeanUpdateListener(var6);
                     }
                  }
               }
            }
         }
      }

      var5 = var18.iterator();

      while(true) {
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (BeanInfo)var5.next();
         } while(!(var6 instanceof EntityBeanInfo) && !(var6 instanceof SessionBeanInfo));

         ClientDrivenBeanInfo var19 = (ClientDrivenBeanInfo)var6;
         var19.setPersistenceUnitRegistry(this.getPersistenceUnitRegistry());
         PersistenceUnitRegistry var20 = this.getPersistenceUnitRegistry();
         Collection var29 = var20.getPersistenceUnitNames();
         Iterator var23 = var29.iterator();

         while(var23.hasNext()) {
            String var28 = (String)var23.next();
            PersistenceUnitInfoImpl var31 = var20.getPersistenceUnit(var28);
            PersistenceUnitBean var34 = var31.getDD();
            DescriptorBean var38 = (DescriptorBean)var34;
            var38.addBeanUpdateListener(var19);
            PersistenceUnitConfigurationBean var41 = var31.getConfigDD();
            if (var41 != null) {
               var38 = (DescriptorBean)var41;
               var38.addBeanUpdateListener(var19);
            }
         }
      }
   }

   private void unregisterBeanUpdateListeners() {
      HashMap var1 = new HashMap();
      WeblogicEjbJarBean var2 = this.ejbDescriptor.getWeblogicEjbJarBean();
      WeblogicEnterpriseBeanBean[] var3 = var2.getWeblogicEnterpriseBeans();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var1.put(var3[var4].getEjbName(), var3[var4]);
      }

      Collection var18 = this.ejbDeployer.getBeanInfos();
      Iterator var5 = var18.iterator();

      BeanInfo var6;
      while(var5.hasNext()) {
         var6 = (BeanInfo)var5.next();

         assert var1.containsKey(var6.getEJBName());

         WeblogicEnterpriseBeanBean var7 = (WeblogicEnterpriseBeanBean)var1.get(var6.getEJBName());
         TransactionDescriptorBean var8 = var7.getTransactionDescriptor();
         DescriptorBean var9 = (DescriptorBean)var8;
         var9.removeBeanUpdateListener(var6);
         if (var6 instanceof EntityBeanInfo) {
            EntityBeanInfo var22 = (EntityBeanInfo)var6;
            EntityDescriptorBean var26 = var7.getEntityDescriptor();
            if (var26.getEntityCacheRef() != null) {
               EntityCacheRefBean var27 = var26.getEntityCacheRef();
               var9 = (DescriptorBean)var27;
               var9.removeBeanUpdateListener(var6);
               WeblogicApplicationBean var32 = this.appCtx.getWLApplicationDD();
               EjbBean var35 = var32.getEjb();
               ApplicationEntityCacheBean[] var37 = var35.getEntityCaches();

               for(int var40 = 0; var40 < var37.length; ++var40) {
                  if (var27.getEntityCacheName().equals(var37[var40].getEntityCacheName())) {
                     var9 = (DescriptorBean)var37[var40];
                     var9.removeBeanUpdateListener(var6);
                  }
               }
            } else {
               var9 = (DescriptorBean)var26.getEntityCache();
               var9.removeBeanUpdateListener(var6);
            }

            var9 = (DescriptorBean)var26.getPool();
            var9.removeBeanUpdateListener(var6);
            if (!var22.getIsBeanManagedPersistence() && var22.getCMPInfo().uses20CMP()) {
               WeblogicRdbmsJarBean[] var30 = this.ejbDescriptor.getWeblogicRdbmsJarBeans();

               for(int var33 = 0; var33 < var30.length; ++var33) {
                  WeblogicRdbmsBeanBean[] var36 = var30[var33].getWeblogicRdbmsBeans();

                  for(int var39 = 0; var39 < var36.length; ++var39) {
                     if (var22.getEJBName().equals(var36[var39].getEjbName())) {
                        AutomaticKeyGenerationBean var41 = var36[var39].getAutomaticKeyGeneration();
                        if (var41 != null) {
                           var9 = (DescriptorBean)var41;
                           var9.removeBeanUpdateListener(var6);
                        }
                     }
                  }
               }
            }
         } else if (var6 instanceof SessionBeanInfo) {
            SessionBeanInfo var21 = (SessionBeanInfo)var6;
            if (var21.isStateful()) {
               StatefulSessionDescriptorBean var24 = var7.getStatefulSessionDescriptor();
               var9 = (DescriptorBean)var24.getStatefulSessionCache();
               var9.removeBeanUpdateListener(var6);
            } else {
               StatelessSessionDescriptorBean var25 = var7.getStatelessSessionDescriptor();
               var9 = (DescriptorBean)var25.getPool();
               var9.removeBeanUpdateListener(var6);
            }
         } else {
            if (!(var6 instanceof MessageDrivenBeanInfo)) {
               throw new AssertionError("Unknown BeanInfo type!");
            }

            MessageDrivenDescriptorBean var10 = var7.getMessageDrivenDescriptor();
            var9 = (DescriptorBean)var10;
            var9.removeBeanUpdateListener(var6);
            var9 = (DescriptorBean)var10.getPool();
            var9.removeBeanUpdateListener(var6);
            EjbJarBean var11 = this.ejbDescriptor.getEjbJarBean();
            EnterpriseBeansBean var12 = var11.getEnterpriseBeans();
            MessageDrivenBeanBean[] var13 = var12.getMessageDrivens();

            for(int var14 = 0; var14 < var13.length; ++var14) {
               if (var6.getEJBName().equals(var13[var14].getEjbName())) {
                  ActivationConfigBean var15 = var13[var14].getActivationConfig();
                  ActivationConfigPropertyBean[] var16 = var15.getActivationConfigProperties();

                  for(int var17 = 0; var17 < var16.length; ++var17) {
                     if (DDConstants.updateElementSet.contains(var16[var17].getActivationConfigPropertyName().toUpperCase())) {
                        var9 = (DescriptorBean)var16[var17];
                        var9.removeBeanUpdateListener(var6);
                     }
                  }
               }
            }
         }
      }

      var5 = var18.iterator();

      while(true) {
         do {
            if (!var5.hasNext()) {
               return;
            }

            var6 = (BeanInfo)var5.next();
         } while(!(var6 instanceof EntityBeanInfo) && !(var6 instanceof SessionBeanInfo));

         PersistenceUnitRegistry var19 = this.getPersistenceUnitRegistry();
         Collection var20 = var19.getPersistenceUnitNames();
         Iterator var29 = var20.iterator();

         while(var29.hasNext()) {
            String var23 = (String)var29.next();
            PersistenceUnitInfoImpl var28 = var19.getPersistenceUnit(var23);
            PersistenceUnitBean var31 = var28.getDD();
            DescriptorBean var34 = (DescriptorBean)var31;
            var34.removeBeanUpdateListener(var6);
            PersistenceUnitConfigurationBean var38 = var28.getConfigDD();
            if (var38 != null) {
               var34 = (DescriptorBean)var38;
               var34.removeBeanUpdateListener(var6);
            }
         }
      }
   }

   private void setupEJBToImplClassDependencies(ClassLoader var1) {
      this.implClassToNameMap.clear();
      EnterpriseBeansBean var2 = this.ejbDescriptor.getEjbJarBean().getEnterpriseBeans();
      EntityBeanBean[] var3 = var2.getEntities();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         this.addEJBToImplClassDependency(var3[var4].getEjbName(), var3[var4].getEjbClass());
      }

      SessionBeanBean[] var18 = var2.getSessions();

      for(int var5 = 0; var5 < var18.length; ++var5) {
         this.addEJBToImplClassDependency(var18[var5].getEjbName(), var18[var5].getEjbClass());
      }

      MessageDrivenBeanBean[] var19 = var2.getMessageDrivens();

      for(int var6 = 0; var6 < var19.length; ++var6) {
         this.addEJBToImplClassDependency(var19[var6].getEjbName(), var19[var6].getEjbClass());
      }

      ClassFinder var20 = ((GenericClassLoader)var1).getClassFinder();
      GenericClassLoader var7 = new GenericClassLoader(var20, var1.getParent());
      Set var8 = this.implClassToNameMap.keySet();
      String[] var9 = new String[var8.size()];
      var9 = (String[])((String[])var8.toArray(var9));

      for(int var10 = 0; var10 < var9.length; ++var10) {
         String var11 = var9[var10];

         try {
            Class var12 = var7.loadClass(var11);

            while(true) {
               Class var13 = var12.getSuperclass();
               if (var13.getClassLoader() != var7) {
                  break;
               }

               List var14 = (List)this.implClassToNameMap.get(var11);
               var11 = var13.getName();
               Iterator var15 = var14.iterator();

               while(var15.hasNext()) {
                  String var16 = (String)var15.next();
                  this.addEJBToImplClassDependency(var16, var11);
               }

               var12 = var13;
            }
         } catch (ClassNotFoundException var17) {
         }
      }

   }

   private void addEJBToImplClassDependency(String var1, String var2) {
      List var3 = (List)this.implClassToNameMap.get(var2);
      if (var3 == null) {
         ArrayList var4 = new ArrayList();
         var4.add(var1);
         this.implClassToNameMap.put(var2, var4);
      } else if (!var3.contains(var1)) {
         var3.add(var1);
      }

   }

   private void dumpDependencyMaps() {
      System.out.println("Dumping beanImpl -> ejbName map");
      Set var1 = this.implClassToNameMap.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         List var4 = (List)this.implClassToNameMap.get(var3);
         Iterator var5 = var4.iterator();
         boolean var6 = true;

         String var7;
         for(var7 = ""; var5.hasNext(); var7 = var7 + (String)var5.next()) {
            if (!var6) {
               var7 = var7 + ", ";
            } else {
               var6 = false;
            }
         }

         System.out.println(var3 + ": " + var7);
      }

   }

   public static EJBModule findModule(String var0, String var1) {
      return (EJBModule)ejbModules.get(new ModuleKey(var0, var1));
   }

   private static void debug(String var0) {
      debugLogger.debug("[EJBModule] " + var0);
   }

   private static void debug(String var0, Throwable var1) {
      debugLogger.debug("[EJBModule] " + var0, var1);
   }

   public PersistenceUnitRegistry getPersistenceUnitRegistry() {
      return this.persistenceUnitRegistry;
   }

   public PersistenceUnitRegistry getProposedPersistenceUnitRegistry() {
      return this.proposedPersistenceUnitRegistry;
   }

   private void fillInterfaceInfoForEJBModule(EjbDescriptorBean var1) {
      EjbJarBean var2 = var1.getEjbJarBean();
      EnterpriseBeansBean var3 = var2.getEnterpriseBeans();
      SessionBeanBean[] var4 = var3.getSessions();
      SessionBeanBean[] var5 = var4;
      int var6 = var4.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         SessionBeanBean var8 = var5[var7];
         this.fillInterfaceInfoForEJBModule((EnterpriseBeanBean)var8);
         String[] var9 = var8.getBusinessRemotes();
         String[] var10;
         int var12;
         if (var9 != null) {
            var10 = var9;
            int var11 = var9.length;

            for(var12 = 0; var12 < var11; ++var12) {
               String var13 = var10[var12];
               this.ejbInterfaces.add(var13);
            }
         }

         if (var8.getHome() != null) {
            this.ejbInterfaces.add(var8.getHome());
            this.ejbInterfaces.add(var8.getRemote());
         }

         var10 = var8.getBusinessLocals();
         if (var10 != null) {
            String[] var23 = var10;
            var12 = var10.length;

            for(int var24 = 0; var24 < var12; ++var24) {
               String var14 = var23[var24];
               this.ejbInterfaces.add(var14);
            }
         }

         if (var8.getLocalHome() != null) {
            this.ejbInterfaces.add(var8.getLocalHome());
            this.ejbInterfaces.add(var8.getLocal());
         }
      }

      EntityBeanBean[] var15 = var3.getEntities();
      EntityBeanBean[] var16 = var15;
      var7 = var15.length;

      int var19;
      for(var19 = 0; var19 < var7; ++var19) {
         EntityBeanBean var20 = var16[var19];
         this.fillInterfaceInfoForEJBModule((EnterpriseBeanBean)var20);
         if (var20.getHome() != null) {
            this.ejbInterfaces.add(var20.getHome());
         }

         if (var20.getLocal() != null) {
            this.ejbInterfaces.add(var20.getLocal());
         }

         if (var20.getLocalHome() != null) {
            this.ejbInterfaces.add(var20.getLocalHome());
         }

         if (var20.getRemote() != null) {
            this.ejbInterfaces.add(var20.getRemote());
         }

         if (var20.getPrimKeyClass() != null) {
            this.ejbInterfaces.add(var20.getPrimKeyClass());
         }
      }

      MessageDrivenBeanBean[] var17 = var3.getMessageDrivens();
      MessageDrivenBeanBean[] var18 = var17;
      var19 = var17.length;

      for(int var21 = 0; var21 < var19; ++var21) {
         MessageDrivenBeanBean var22 = var18[var21];
         this.fillInterfaceInfoForEJBModule((EnterpriseBeanBean)var22);
      }

   }

   private void fillInterfaceInfoForEJBModule(EnterpriseBeanBean var1) {
      if (var1.getEjbClass() != null) {
         this.ejbInterfaces.add(var1.getEjbClass());
      }

      EjbRefBean[] var2 = var1.getEjbRefs();
      if (var2 != null) {
         EjbRefBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            EjbRefBean var6 = var3[var5];
            if (var6.getHome() != null) {
               this.ejbInterfaces.add(var6.getHome());
            }

            if (var6.getRemote() != null) {
               this.ejbInterfaces.add(var6.getRemote());
            }
         }
      }

   }

   private Set getEJBInterfaces() {
      return this.ejbInterfaces;
   }

   private void setupRedefiningClassLoader(GenericClassLoader var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug(" ClassLoader " + var1 + " EJB Interfaces " + this.ejbInterfaces);
      }

      if (var1 instanceof RedefiningClassLoader && this.ejbInterfaces.size() > 0) {
         ((RedefiningClassLoader)var1).setExcludedClasses(this.ejbInterfaces);
      }

   }

   private void setupCoherenceCaches() throws DeploymentException {
      if (this.shouldProcessCoherence()) {
         try {
            GenericClassLoader var1 = this.getClassLoader();
            CoherenceClusterManager var2 = CoherenceClusterManager.getInstance();
            boolean var3 = var2.isCoherenceAvailable(var1);
            CoherenceClusterRefBean var4 = this.getCoherenceClusterRefBean();
            String var5 = null;
            if (var4 != null) {
               var5 = var4.getCoherenceClusterName();
               if (!var3) {
                  throw new ModuleException("Missing Coherence jar or WebLogic Coherence Integration jar");
               }
            }

            if (var3) {
               var2.registerEJBComponentRuntimeMBean(var1, (EJBComponentRuntimeMBean)this.getRuntimeMBean());
               var2.configureClusterService(var1, var5);
               var2.addCacheConfiguration(var1);
            }
         } catch (ModuleException var6) {
            throw var6;
         } catch (Exception var7) {
            throw new ModuleException(var7.getMessage(), var7);
         }
      }

   }

   private boolean shouldProcessCoherence() {
      GenericClassLoader var1 = this.getClassLoader();
      return this.appCtx != null && (!this.appCtx.isEar() || this.appCtx.getAppClassLoader() != var1);
   }

   private CoherenceClusterRefBean getCoherenceClusterRefBean() {
      WeblogicEjbJarBean var1 = this.ejbDescriptor.getWeblogicEjbJarBean();
      return var1 != null ? var1.getCoherenceClusterRef() : null;
   }

   private void releaseCoherenceCaches() {
      if (this.shouldProcessCoherence()) {
         try {
            GenericClassLoader var1 = this.getClassLoader();
            CoherenceClusterManager var2 = CoherenceClusterManager.getInstance();
            if (var2.isCoherenceAvailable(var1)) {
               var2.releaseCacheConfiguration(var1);
               var2.shutdownClusterService(var1);
               var2.unRegisterEJBComponentRuntimeMBean(var1, (EJBComponentRuntimeMBean)this.getRuntimeMBean());
            }
         } catch (Exception var3) {
            EJBLogger.logErrorUndeploying(this.getURI(), var3);
         }
      }

   }

   public boolean isParallelEnabled() {
      return true;
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
      ejbModules = new ConcurrentHashMap();
   }

   private static final class ModuleKey {
      private final String appName;
      private final String compName;
      private final int hash;

      ModuleKey(String var1, String var2) {
         this.appName = var1;
         this.compName = var2;
         this.hash = var1.hashCode() ^ var2.hashCode();
      }

      public int hashCode() {
         return this.hash;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ModuleKey)) {
            return false;
         } else {
            ModuleKey var2 = (ModuleKey)var1;
            return var2.hashCode() == this.hash && var2.appName.equals(this.appName) && var2.compName.equals(this.compName);
         }
      }
   }
}
