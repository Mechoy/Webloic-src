package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ParentModule;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.compiler.flow.ApplicationViewerFlow;
import weblogic.application.io.Ear;
import weblogic.application.io.ExplodedJar;
import weblogic.application.io.JarCopyFilter;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReference;
import weblogic.application.utils.CompositeWebAppFinder;
import weblogic.application.utils.CustomModuleManager;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.application.utils.PathUtils;
import weblogic.deployment.PersistenceUnitViewer;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.FaceConfigCacheHelper;
import weblogic.servlet.internal.TldCacheHelper;
import weblogic.servlet.internal.War;
import weblogic.servlet.internal.WebAnnotationProcessor;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.servlet.internal.WebAppHelper;
import weblogic.servlet.jsp.JspcInvoker;
import weblogic.servlet.utils.WarUtils;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.utils.BadOptionException;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.wsee.deploy.WSEEDescriptor;
import weblogic.wsee.policy.deployment.WsPolicyDescriptor;

public class WARModule extends EARModule implements ParentModule {
   GenericClassLoader wcl;
   private CustomModuleManager customModuleManager = null;
   protected WebAppBean webBean;
   private WeblogicWebAppBean wlBean;
   private ExplodedJar moduleWar = null;
   private File moduleExtractDir = null;
   private War libraryWar = null;
   private CompositeWebAppFinder moduleClassFinder = new CompositeWebAppFinder();
   private CompositeWebAppFinder resourceFinder = new CompositeWebAppFinder();
   private String contextRoot;
   private PersistenceUnitViewer perViewer;
   private WSEEModuleHelper wseeHelper;
   public static final String WL_EXT_URI = "WEB-INF/weblogic-extension.xml";
   public static final String WSEE_WEB_URI_81 = "WEB-INF/web-services.xml";

   public WARModule(String var1, String var2, String var3) {
      super(var1, var2);
      this.contextRoot = var3;
   }

   public String getDescriptorURI() {
      return "WEB-INF";
   }

   public ClassFinder getClassFinder() {
      return this.moduleClassFinder;
   }

   public ClassFinder getResourceFinder() {
      return this.resourceFinder;
   }

   public CustomModuleManager getCustomModuleManager() {
      return this.customModuleManager;
   }

   private File getExtractDir(CompilerCtx var1) throws ToolFailureException {
      Ear var3 = var1.getEar();
      String var2;
      if (var3 == null) {
         var2 = this.getURI();
      } else {
         var2 = var3.getURI();
      }

      String var4 = this.getURI();
      File var5 = var1.getTempDir();
      File var6 = new File(var5, var2);
      if (var6.exists() && !var6.isDirectory()) {
         var6.delete();
      }

      File var7 = new File(var5, PathUtils.generateTempPath((String)null, var2, var4));
      boolean var8 = false;
      if (var7.exists()) {
         var8 = var7.canWrite();
      } else {
         var8 = var7.mkdirs();
      }

      if (!var8) {
         throw new ToolFailureException("Unable to generate temporary directory for module: " + var7.getAbsolutePath());
      } else {
         return var7;
      }
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      try {
         this.moduleExtractDir = this.getExtractDir(var1);
         if (this.isArchive()) {
            this.moduleWar = new ExplodedJar(this.getURI(), this.moduleExtractDir, this.getVirtualJarFile().getRootFiles()[0], War.WAR_CLASSPATH_INFO);
         } else {
            this.moduleWar = new ExplodedJar(this.getURI(), this.moduleExtractDir, this.getVirtualJarFile().getRootFiles(), War.WAR_CLASSPATH_INFO, JarCopyFilter.NOCOPY_FILTER);
         }

         this.moduleClassFinder.addFinder(this.moduleWar.getClassFinder());
         SplitDirectoryInfo var3 = var1.getApplicationContext().getSplitDirectoryInfo();
         if (var3 != null) {
            String[] var4 = var3.getWebAppClasses(this.getURI());
            if (var4 != null && var4.length > 0) {
               this.moduleClassFinder.addFinder(new ClasspathClassFinder2(StringUtils.join(var4, File.pathSeparator)));
            }
         }

         this.resourceFinder.addFinder(new ApplicationViewerFlow.ApplicationResourceFinder(this.getURI(), this.moduleWar.getClassFinder()));
         super.initModuleClassLoader(var1, var2);
      } catch (IOException var5) {
         throw new ToolFailureException("Unable to load web module at uri " + this.getURI(), var5);
      }
   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      JspcInvoker var3 = new JspcInvoker(var2.getOpts());

      try {
         var3.checkCompliance(var1, this.getVirtualJarFile(), this.getAltDDFile(), var2.getConfigDir(), var2.getPlanBean(), this.getModuleValidationInfo(), var2.isVerbose());
      } catch (ErrorCollectionException var5) {
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.getVirtualJarFile().getName(), var5.toString()).getMessage(), var5);
      }
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      this.wcl = var1;
      this.wcl.addClassFinder(this.moduleClassFinder);

      try {
         var2.getOpts().setOption("classpath", this.wcl.getClassPath());
      } catch (BadOptionException var4) {
         throw new AssertionError(var4);
      }

      if (!this.getVirtualJarFile().isDirectory()) {
         IOUtils.forceClose(this.getVirtualJarFile());
         this.setVirtualJarFile(AppcUtils.getVirtualJarFile(this.getOutputDir()));
      }

      WebAppDescriptor var3 = WarUtils.getWebAppDescriptor(this.getAltDDFile(), this.getVirtualJarFile(), var2.getConfigDir(), var2.getPlanBean(), this.getURI());
      this.processLibraries(var2, var3);
      if (WarUtils.configureFCL(this.wlBean, var2.getApplicationContext().getAppClassLoader(), var2.getEar() == null)) {
         J2EELogger.logFilteringConfigurationIgnored(var2.getApplicationContext().getApplicationId(), this.getURI());
      }

      AppcUtils.compileWAR(this.wcl, this.getVirtualJarFile(), this.getAltDDFile(), var2.getConfigDir(), var2.getPlanBean(), this.getOutputDir(), this.getModuleValidationInfo(), var2);
      if (var2.isWriteInferredDescriptors() && (this.webBean == null || !this.webBean.isMetadataComplete())) {
         this.wseeHelper = new WSEEModuleHelper(var2, this.getVirtualJarFile(), this.getURI(), true);
         this.backupDescriptors();
         this.processAnnotations(var2);
         this.writeDescriptors(var2);
      }

   }

   public void cleanup() {
      super.cleanup();
      if (this.wcl != null) {
         this.wcl.close();
      }

      this.moduleClassFinder.close();
      this.resourceFinder.close();
      if (this.libraryWar != null) {
         this.libraryWar.closeAllFinders();
      }

      if (this.customModuleManager != null) {
         this.customModuleManager.cleanup();
      }

      if (this.moduleWar != null) {
         this.moduleWar.remove();
      }

   }

   public void writeDescriptors(CompilerCtx var1) throws ToolFailureException {
      if (var1.isWriteInferredDescriptors()) {
         this.webBean.setMetadataComplete(true);
      }

      AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/web.xml", (DescriptorBean)this.webBean);
      AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/weblogic.xml", (DescriptorBean)this.wlBean);
      AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/webservices.xml", (DescriptorBean)this.wseeHelper.getWsBean());
      AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/weblogic-webservices.xml", (DescriptorBean)this.wseeHelper.getWlWsBean());
      AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/weblogic-webservices-policy.xml", this.getRootBean("WEB-INF/weblogic-webservices-policy.xml"));
   }

   private void processDDs(CompilerCtx var1) {
      if (this.webBean != null) {
         this.addRootBean("WEB-INF/web.xml", (DescriptorBean)this.webBean);
      }

      if (this.wlBean != null) {
         this.addRootBean("WEB-INF/weblogic.xml", (DescriptorBean)this.wlBean);
      }

      if (this.wseeHelper.getWsBean() != null) {
         this.addRootBean("WEB-INF/webservices.xml", (DescriptorBean)this.wseeHelper.getWsBean());
      }

      if (this.wseeHelper.getWlWsBean() != null) {
         this.addRootBean("WEB-INF/weblogic-webservices.xml", (DescriptorBean)this.wseeHelper.getWlWsBean());
      }

      try {
         if (WarUtils.isWebServices(this.moduleExtractDir)) {
            WSEEDescriptor var2 = new WSEEDescriptor(new File(this.moduleExtractDir, "WEB-INF/web-services.xml"), (File)null, (DeploymentPlanBean)null, (String)null);
            this.addRootBean("WEB-INF/web-services.xml", (DescriptorBean)var2.getWebservicesBean());
         }
      } catch (Exception var7) {
      }

      WsPolicyDescriptor var8 = new WsPolicyDescriptor(this.getVirtualJarFile(), var1.getConfigDir(), var1.getPlanBean(), this.getURI(), true);

      try {
         if (var8.getWebservicesPolicyBean() != null) {
            this.addRootBean("WEB-INF/weblogic-webservices-policy.xml", (DescriptorBean)var8.getWebservicesPolicyBean());
         }
      } catch (Exception var6) {
      }

      this.addDiagnosticDDRootBean(var1);
      Iterator var3 = this.perViewer.getDescriptorURIs();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Descriptor var5 = this.perViewer.getDescriptor(var4);
         this.addRootBean(var4, var5.getRootBean());
      }

   }

   private DescriptorBean mergeDDFromLibraries(War var1, WebAppDescriptor var2, String var3) throws IOException, XMLStreamException {
      Enumeration var4 = var1.getResourceFinder("/").getSources("/" + var3);
      ArrayList var5 = Collections.list(var4);
      Source[] var6 = (Source[])((Source[])var5.toArray(new Source[0]));
      return var2.mergeLibaryDescriptors(var6, var3);
   }

   private void initWebAppLibraryManager(CompilerCtx var1, LibraryManager var2, WeblogicWebAppBean var3, String var4) throws ToolFailureException {
      if (var3 != null) {
         if (var3.getLibraryRefs() != null) {
            LibraryReference[] var5 = WebAppLibraryUtils.getWebLibRefs(var3, var4);
            var2.lookup(var5);
            if (var2.hasUnresolvedReferences()) {
               String var6 = var2.getUnresolvedReferencesError();
               if (var1.verifyLibraryReferences()) {
                  throw new ToolFailureException(var6);
               }

               J2EELogger.logUnresolvedLibraryReferencesWarningLoggable(var6).log();
            }

         }
      }
   }

   private void processLibraries(CompilerCtx var1, WebAppDescriptor var2) throws ToolFailureException {
      this.wlBean = WarUtils.getWlWebAppBean(var2);
      LibraryManager var3 = new LibraryManager(WebAppLibraryUtils.getLibraryReferencer(this.getURI()));
      this.initWebAppLibraryManager(var1, var3, this.wlBean, this.getURI());
      var1.getApplicationContext().addLibraryManager(this.getURI(), var3);

      try {
         if (var3.hasReferencedLibraries()) {
            this.libraryWar = new War(this.getURI());
            WebAppLibraryUtils.extractWebAppLibraries(var3, this.libraryWar, this.moduleExtractDir);
            this.moduleClassFinder.addLibraryFinder(this.libraryWar.getClassFinder());
            this.resourceFinder.addLibraryFinder(this.libraryWar.getResourceFinder("/"));
            this.webBean = (WebAppBean)this.mergeDDFromLibraries(this.libraryWar, var2, "WEB-INF/web.xml");
            this.wlBean = (WeblogicWebAppBean)this.mergeDDFromLibraries(this.libraryWar, var2, "WEB-INF/weblogic.xml");
         }

         this.webBean = WarUtils.getWebAppBean(var2);
      } catch (IOException var5) {
         throw new ToolFailureException("Unable to parse or merge standard web module descriptors", var5);
      } catch (XMLStreamException var6) {
         throw new ToolFailureException("Unable to parse or merge standard web module descriptors", var6);
      }
   }

   protected void processAnnotations() throws ClassNotFoundException, ErrorCollectionException {
      if (WarUtils.isAnnotationEnabled(this.webBean)) {
         String var1 = "weblogic.servlet.internal.WebAnnotationProcessorImpl";

         try {
            WebAnnotationProcessor var2 = (WebAnnotationProcessor)Class.forName(var1).newInstance();
            WarModuleWebAppHelper var3 = new WarModuleWebAppHelper();
            var2.processAnnotations(this.getModuleClassLoader(), this.webBean, var3);
         } catch (InstantiationException var5) {
            var5.printStackTrace();
         } catch (IllegalAccessException var6) {
            var6.printStackTrace();
         }

         ServletBean[] var7 = this.webBean.getServlets();
         String[][] var8 = new String[var7.length][2];

         for(int var4 = 0; var4 < var7.length; ++var4) {
            var8[var4][0] = var7[var4].getServletName();
            var8[var4][1] = var7[var4].getServletClass();
         }

         this.wseeHelper.processAnnotations(this.getModuleClassLoader(), var8);
      }
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      WebAppDescriptor var2 = WarUtils.getWebAppDescriptor(this.getAltDDFile(), this.getVirtualJarFile(), var1.getConfigDir(), var1.getPlanBean(), this.getURI());
      this.processLibraries(var1, var2);
      if (WarUtils.configureFCL(this.wlBean, var1.getApplicationContext().getAppClassLoader(), var1.getEar() == null)) {
         J2EELogger.logFilteringConfigurationIgnored(var1.getApplicationContext().getApplicationId(), this.getURI());
      }

      EarUtils.handleUnsetContextRoot(this.getURI(), (String)null, var1.getApplicationDD(), this.wlBean);
      if (!var1.isBasicView()) {
         this.customModuleManager = new CustomModuleManager(var1, this, "WEB-INF/weblogic-extension.xml");
         this.customModuleManager.merge(var1);
      }

      this.perViewer = new PersistenceUnitViewer.ResourceViewer(this.getModuleClassLoader(), this.getURI(), var1.getConfigDir(), var1.getPlanBean());
      this.perViewer.loadDescriptors();
      this.wseeHelper = new WSEEModuleHelper(var1, this.getVirtualJarFile(), this.getURI(), true);
      if (var1.isWriteInferredDescriptors() && (this.webBean == null || !this.webBean.isMetadataComplete())) {
         this.backupDescriptors();
      }

      this.wseeHelper.mergeDescriptors(this.resourceFinder);
      this.processAnnotations(var1);
      this.processDDs(var1);
   }

   public void write(CompilerCtx var1) throws ToolFailureException {
      try {
         if (this.libraryWar != null) {
            WebAppLibraryUtils.writeWar(this.libraryWar, this.getOutputDir());
         }

         if (this.customModuleManager != null) {
            this.customModuleManager.write(var1);
         }

         this.writeDescriptors(var1);
      } catch (IOException var3) {
         throw new ToolFailureException("Unable to write out web module", var3);
      }
   }

   public DescriptorBean getRootBean() {
      return this.getRootBean("WEB-INF/web.xml");
   }

   public ModuleType getModuleType() {
      return ModuleType.WAR;
   }

   private void backupDescriptors() throws ToolFailureException {
      if (this.getVirtualJarFile().getEntry("WEB-INF/weblogic.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/weblogic.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.wlBean);
      }

      if (this.getVirtualJarFile().getEntry("WEB-INF/web.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/web.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.webBean);
      }

      if (this.getVirtualJarFile().getEntry("WEB-INF/webservices.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/webservices.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.wseeHelper.getWsBean());
      }

      if (this.getVirtualJarFile().getEntry("WEB-INF/weblogic-webservices.xml") != null) {
         AppcUtils.writeDescriptor(this.getOutputDir(), "WEB-INF/weblogic-webservices.xml" + ORIGINAL_DESCRIPTOR_SUFFIX, (DescriptorBean)this.wseeHelper.getWlWsBean());
      }

   }

   public String getContextRoot() {
      return this.contextRoot;
   }

   public void setContextRoot(String var1) {
      this.contextRoot = var1;
   }

   private class WarModuleWebAppHelper implements WebAppHelper {
      private Map tldInfo = null;
      private Set managedBeans = null;
      private List annotatedClasses = null;

      public WarModuleWebAppHelper() {
      }

      public Set getTagListeners(boolean var1) {
         return this.getTagClasses(var1, "listener-class");
      }

      public Set getTagHandlers(boolean var1) {
         return this.getTagClasses(var1, "tag-class");
      }

      private Set getTagClasses(boolean var1, String var2) {
         Set var3 = null;
         ClassFinder var4 = WARModule.this.moduleClassFinder.getWebappFinder();
         Map var5 = this.getWebTldInfo();
         Collection var6 = War.getWebTagClasses(var4, var5, var1, var2);
         var3 = War.addAllIfNotEmpty(var3, var6);
         if (WARModule.this.libraryWar != null) {
            Set var7 = WARModule.this.libraryWar.getLibTagClasses(var1, var2);
            var3 = War.addAllIfNotEmpty((Set)var3, var7);
         }

         return var3 == null ? Collections.EMPTY_SET : var3;
      }

      public Set getManagedBeanClasses() {
         if (!WarUtils.isJsfApplication(WARModule.this.webBean, WARModule.this.wlBean)) {
            return Collections.EMPTY_SET;
         } else if (this.managedBeans != null) {
            return this.managedBeans;
         } else {
            String var1 = WarUtils.getFacesConfigFiles(WARModule.this.webBean);
            List var2 = War.findFacesConfigs(var1, WARModule.this.resourceFinder.getWebappFinder(), WARModule.this.moduleClassFinder.getWebappFinder());
            Set var3 = FaceConfigCacheHelper.parseFacesConfigs(var2, (File)WARModule.this.moduleExtractDir, WARModule.this.getURI());
            this.managedBeans = War.addAllIfNotEmpty((Set)this.managedBeans, var3);
            if (WARModule.this.libraryWar != null) {
               Set var4 = WARModule.this.libraryWar.getLibManagedBeans();
               this.managedBeans = War.addAllIfNotEmpty((Set)this.managedBeans, var4);
            }

            if (this.managedBeans == null) {
               this.managedBeans = Collections.EMPTY_SET;
            }

            return this.managedBeans;
         }
      }

      public Set getManagedBeanClasses(Set<String> var1) {
         this.managedBeans = this.getManagedBeanClasses();
         if (this.managedBeans == Collections.EMPTY_SET) {
            this.managedBeans = new HashSet();
         }

         this.managedBeans.addAll(var1);
         return this.managedBeans;
      }

      public List getAnnotatedClasses(WebAnnotationProcessor var1) {
         if (this.annotatedClasses != null) {
            return this.annotatedClasses;
         } else {
            List var2 = var1.getAnnotatedClasses(WARModule.this.moduleClassFinder.getWebappFinder());
            this.annotatedClasses = War.addAllIfNotEmpty((List)this.annotatedClasses, var2);
            if (WARModule.this.libraryWar != null) {
               List var3 = WARModule.this.libraryWar.getAnnotatedClasses(var1);
               this.annotatedClasses = War.addAllIfNotEmpty((List)this.annotatedClasses, var3);
            }

            if (this.annotatedClasses == null) {
               this.annotatedClasses = Collections.EMPTY_LIST;
            }

            return this.annotatedClasses;
         }
      }

      private Map getWebTldInfo() {
         if (this.tldInfo == null) {
            ArrayList var1 = new ArrayList();
            War.findTlds(WARModule.this.resourceFinder.getWebappFinder(), var1, WARModule.this.moduleClassFinder.getWebappFinder());
            this.tldInfo = TldCacheHelper.parseTagLibraries(var1, (File)WARModule.this.moduleExtractDir, WARModule.this.getURI());
         }

         return this.tldInfo;
      }
   }
}
