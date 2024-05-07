package weblogic.deploy.api.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import weblogic.application.ApplicationFileManager;
import weblogic.deploy.api.internal.Closable;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ClassLoaderControl;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.deploy.api.internal.utils.LibrarySpec;
import weblogic.deploy.api.model.internal.DDBeanRootImpl;
import weblogic.deploy.api.model.internal.WebLogicDeployableObjectFactoryImpl;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.api.spi.config.DescriptorParser;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.FileUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.jars.VirtualJarFile;

public class WebLogicDeployableObject implements DeployableObject, Closable {
   private static final boolean debug = Debug.isDebug("model");
   protected ModuleType moduleType;
   protected File moduleArchive;
   protected WebLogicDeployableObject parent = null;
   protected boolean standalone = true;
   protected List subModules = new ArrayList();
   protected String uri = null;
   protected Map ddMap = new HashMap();
   protected InstallDir installDir = null;
   protected boolean haveAppRoot = false;
   private VirtualJarFile vjf = null;
   protected GenericClassLoader gcl = null;
   protected DescriptorBean beanTree = null;
   protected DDRootFields ddRoot;
   protected boolean lazy = false;
   protected ClassLoaderControl clf = null;
   protected File plan;
   protected File plandir;
   protected DeploymentPlanBean planBean;
   protected LibrarySpec[] libraries = null;
   protected boolean deleteOnClose = false;
   protected ClassFinder resourceFinder;
   protected String contextRoot;

   protected WebLogicDeployableObject(File var1, ModuleType var2, WebLogicDeployableObject var3, String var4, String var5, File var6, File var7, File var8) throws IOException {
      ConfigHelper.checkParam("File", var1);
      this.setPlan(var7);
      this.setPlanDir(var8);
      this.setParent(var3);
      this.setModuleArchive(var1);
      this.setModuleType(var2);
      this.setUri(var4);
      this.setInstallDir(var6);
   }

   public WebLogicDeployableObject(File var1, ModuleType var2, WebLogicDeployableObject var3, String var4, String var5, File var6, File var7, File var8, LibrarySpec[] var9, boolean var10) throws InvalidModuleException, IOException {
      ConfigHelper.checkParam("File", var1);
      ConfigHelper.checkParam("ModuleType", var2);
      this.lazy = var10;
      this.setPlan(var7);
      this.setPlanDir(var8);
      this.setLibraries(var9);
      if (var3 == null) {
         DescriptorSupportManager.flush();
      }

      this.setParent(var3);
      this.setModuleArchive(var1);
      this.setModuleType(var2);
      this.setUri(var4);
      this.setInstallDir(var6);
      this.setDDBeanRoot(new DDBeanRootImpl(var5, this, var2));
      if (!var10) {
         this.getDDBeanRootInternal();
      }

   }

   protected void setLibraries(LibrarySpec[] var1) {
      this.libraries = var1;
   }

   public File getPlandir() {
      return this.plandir;
   }

   public File getPlan() {
      return this.plan;
   }

   public void setVirtualJarFile(VirtualJarFile var1) {
      this.closeVJF();
      this.vjf = var1;
   }

   public DeploymentPlanBean getPlanBean() throws IOException {
      if (this.planBean == null && this.plan != null) {
         FileInputStream var1 = new FileInputStream(this.plan);

         try {
            this.planBean = DescriptorParser.parseDeploymentPlan(var1);
         } finally {
            var1.close();
         }
      }

      return this.planBean;
   }

   protected void setPlanDir(File var1) {
      this.plandir = var1;
      if (var1 == null && this.planBean != null && this.planBean.getConfigRoot() != null) {
         this.plandir = new File(this.planBean.getConfigRoot());
      }

   }

   protected void setPlan(File var1) {
      this.plan = var1;
   }

   protected DDBeanRootImpl getDDBeanRootInternal() throws IOException, InvalidModuleException {
      if (this.ddRoot == null) {
         this.setDDBeanRoot(new DDBeanRootImpl((String)null, this, this.moduleType, (DescriptorBean)null, true));
      }

      DDBeanRootImpl var1 = this.ddRoot.getRoot();
      if (var1 != null && var1.isInitialized()) {
         return var1;
      } else {
         if (debug) {
            Debug.say("DDBeanRootImpl root for " + var1.getFilename() + " is null or not initialized");
         }

         return null;
      }
   }

   protected InputStream getDDStream(String var1, ModuleType var2) throws FileNotFoundException {
      Object var3;
      if (var1 != null) {
         if (this.parent == null) {
            throw new AssertionError("Attempt to construct standalone module with altDD");
         }

         var3 = this.parent.getEntry(var1);
         if (var3 == null) {
            throw new FileNotFoundException(SPIDeployerLogger.noFile(var1));
         }

         if (debug) {
            Debug.say("using altdd at " + var1);
         }
      } else if (this.getArchive().getName().endsWith(".xml")) {
         var3 = new FileInputStream(this.getArchive());
      } else {
         var3 = this.getEntry(WebLogicModuleType.getDDUri(var2.getValue()));
      }

      return (InputStream)var3;
   }

   public ModuleType getType() {
      return this.moduleType;
   }

   public DDBean[] getChildBean(String var1) {
      ConfigHelper.checkParam("xpath", var1);
      return this.getDDBeanRoot().getChildBean(var1);
   }

   public Class getClassFromScope(String var1) {
      ConfigHelper.checkParam("className", var1);

      try {
         this.gcl = this.getOrCreateGCL();
         return Class.forName(var1, false, this.gcl);
      } catch (ClassNotFoundException var3) {
         return null;
      } catch (IOException var4) {
         return null;
      }
   }

   public DDBeanRoot getDDBeanRoot() {
      try {
         return this.getDDBeanRootInternal();
      } catch (Exception var2) {
         if (debug) {
            var2.printStackTrace();
         }

         throw new RuntimeException(var2);
      }
   }

   protected void setDDBeanRoot(DDBeanRoot var1) {
      this.ddRoot = new DDRootFields((DDBeanRootImpl)var1);
   }

   public String[] getText(String var1) {
      ConfigHelper.checkParam("xpath", var1);
      return this.getDDBeanRoot().getText(var1);
   }

   public boolean hasDDBean(String var1) throws FileNotFoundException {
      if (debug) {
         Debug.say("getting ddbean root for : " + var1);
      }

      if (var1 == null) {
         throw new FileNotFoundException(var1);
      } else {
         if (var1.equals(".")) {
            var1 = this.getArchive().getPath();
         }

         DDBeanRootImpl var2 = (DDBeanRootImpl)this.ddMap.get(var1);
         return var2 != null ? var2.hasDBean() : false;
      }
   }

   public DDBeanRoot getDDBeanRoot(String var1) throws FileNotFoundException, DDBeanCreateException {
      if (debug) {
         Debug.say("getting ddbean root for : " + var1);
      }

      if (var1 == null) {
         throw new FileNotFoundException(var1);
      } else {
         if (var1.equals(".")) {
            var1 = this.getArchive().getPath();
         }

         DDBeanRootImpl var2 = (DDBeanRootImpl)this.ddMap.get(var1);
         if (var2 != null && var2.isInitialized()) {
            return var2;
         } else {
            if (debug) {
               Debug.say("DDBeanRootImpl root " + (var2 == null ? "" : var2.getFilename()) + " is null or not initialized");
            }

            return null;
         }
      }
   }

   public InputStream getInputStream(String var1) throws FileNotFoundException {
      Object var2 = this.getEntry(var1);
      if (var2 == null && this.haveAppRoot) {
         File var3;
         if (this.parent == null) {
            var3 = this.getInstallDir().getAppDDFile(var1);
         } else {
            var3 = this.getInstallDir().getDDFile(this.uri, var1);
         }

         if (debug) {
            Debug.say("Looking for dd in config area: " + var3.getPath());
         }

         var2 = new FileInputStream(var3);
      }

      return (InputStream)var2;
   }

   public Enumeration entries() {
      if (this.getArchive().getName().endsWith(".xml")) {
         ArrayList var3 = new ArrayList();
         var3.add(this.moduleArchive.getPath());
         return Collections.enumeration(var3);
      } else {
         Iterator var1 = this.vjf.entries();
         ArrayList var2 = new ArrayList();

         while(var1.hasNext()) {
            var2.add(((ZipEntry)var1.next()).getName());
         }

         return Collections.enumeration(var2);
      }
   }

   public Enumeration getDDResourceEntries(String var1) {
      HashSet var2 = new HashSet();
      String var3 = null;
      Iterator var4 = this.ddMap.keySet().iterator();

      while(var4.hasNext()) {
         var3 = (String)var4.next();
         if (var3.endsWith(var1)) {
            var2.add(var3);
         }
      }

      return Collections.enumeration(var2);
   }

   public InputStream getEntry(String var1) {
      ConfigHelper.checkParam("name", var1);

      try {
         if (this.getArchive().getName().endsWith(".xml")) {
            if (var1.equals(".")) {
               return new FileInputStream(this.getArchive());
            }

            if (debug) {
               Debug.say("No entry in document for " + var1);
            }

            return null;
         }

         if (debug) {
            Debug.say("in DO : " + this.moduleArchive.getName() + " with uri " + this.uri);
         }

         if (debug) {
            Debug.say("Getting stream for entry " + var1);
         }

         if (this.vjf != null) {
            ZipEntry var2 = this.vjf.getEntry(var1);
            if (var2 != null) {
               return this.vjf.getInputStream(var2);
            }
         }

         if (this.parent != null) {
            return this.getStreamFromParent(this.uri, var1);
         }

         if (debug) {
            Debug.say("No entry in archive for " + var1);
         }
      } catch (IOException var3) {
         if (debug) {
            Debug.say("No entry in archive for " + var1);
         }
      }

      return null;
   }

   protected InputStream getStreamFromParent(String var1, String var2) throws IOException {
      VirtualJarFile var3 = this.parent.getVirtualJarFile();
      if (var3 != null) {
         ZipEntry var4 = var3.getEntry(var1 + "/" + var2);
         if (var4 != null) {
            return var3.getInputStream(var4);
         }
      }

      return null;
   }

   /** @deprecated */
   public String getModuleDTDVersion() {
      return !((DDBeanRootImpl)this.getDDBeanRoot()).isSchemaBased() ? this.getDDBeanRoot().getDDBeanRootVersion() : null;
   }

   public void setDescriptorBean(DescriptorBean var1) {
      this.beanTree = var1;
   }

   public boolean isDBSet() {
      return this.beanTree != null;
   }

   public DescriptorBean getDescriptorBean() throws IOException {
      if (this.beanTree != null) {
         return this.beanTree;
      } else {
         if (debug) {
            Debug.say("beanTree of WebLogicDeployableObject (" + this.toString() + ") is null.");
         }

         return null;
      }
   }

   /** @deprecated */
   public static WebLogicDeployableObject createDeployableObject(File var0) throws InvalidModuleException, IOException {
      WebLogicDeployableObjectFactoryImpl var1 = new WebLogicDeployableObjectFactoryImpl();
      return var1.createDeployableObject(var0);
   }

   /** @deprecated */
   public static WebLogicDeployableObject createDeployableObject(File var0, File var1) throws InvalidModuleException, IOException {
      WebLogicDeployableObjectFactoryImpl var2 = new WebLogicDeployableObjectFactoryImpl();
      return var2.createDeployableObject(var0, var1);
   }

   /** @deprecated */
   public static WebLogicDeployableObject createDeployableObject(File var0, File var1, File var2, File var3, LibrarySpec[] var4) throws IOException, InvalidModuleException, ConfigurationException {
      WebLogicDeployableObjectFactoryImpl var5 = new WebLogicDeployableObjectFactoryImpl();
      return var5.createDeployableObject(var0, var1, var2, var3, var4);
   }

   /** @deprecated */
   public static WebLogicDeployableObject createLazyDeployableObject(File var0, File var1, File var2, File var3, LibrarySpec[] var4) throws IOException, InvalidModuleException, ConfigurationException {
      WebLogicDeployableObjectFactoryImpl var5 = new WebLogicDeployableObjectFactoryImpl();
      return var5.createLazyDeployableObject(var0, var1, var2, var3, var4);
   }

   public void setDeleteOnClose() {
      this.deleteOnClose = true;
   }

   public LibrarySpec[] getLibraries() {
      return this.libraries;
   }

   /** @deprecated */
   public static WebLogicDeployableObject createLazyDeployableObject(File var0, File var1) throws InvalidModuleException, IOException {
      WebLogicDeployableObjectFactoryImpl var2 = new WebLogicDeployableObjectFactoryImpl();
      return var2.createLazyDeployableObject(var0, var1, (File)null, (File)null, (LibrarySpec[])null);
   }

   public File getArchive() {
      return this.moduleArchive;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.moduleType.toString());
      var1.append(" Archive: ");
      var1.append(this.moduleArchive.toString());
      if (this.contextRoot != null) {
         var1.append(" ContextRoot: ");
         var1.append(this.contextRoot);
      }

      return var1.toString();
   }

   public String dump() {
      StringBuffer var1 = new StringBuffer();
      if (this.getDDBeanRoot() != null) {
         var1.append(this.toString());
         var1.append("\n");
         var1.append(this.getDDBeanRoot().toString());
         var1.append("\n");
      }

      return var1.toString();
   }

   public String getFileName(DDBeanRootImpl var1) {
      Iterator var4 = this.ddMap.keySet().iterator();

      DDBeanRootImpl var2;
      String var3;
      do {
         if (!var4.hasNext()) {
            return WebLogicModuleType.getDDUri(this.moduleType.getValue());
         }

         var3 = (String)var4.next();
         var2 = (DDBeanRootImpl)this.ddMap.get(var3);
      } while(var2 != var1);

      return var3;
   }

   public DDBeanRoot[] getDDBeanRoots() {
      return (DDBeanRoot[])((DDBeanRoot[])this.ddMap.values().toArray(new DDBeanRoot[0]));
   }

   public File getInstallDirPath() {
      return this.parent != null ? this.parent.getInstallDirPath() : this.getInstallDir().getInstallDir();
   }

   public InstallDir getInstallDir() {
      return this.parent != null ? this.parent.getInstallDir() : this.installDir;
   }

   protected void setInstallDir(File var1) throws IOException {
      this.haveAppRoot = var1 != null;
      if (this.parent == null) {
         this.installDir = new InstallDir(this.moduleArchive.getName(), var1);
      }
   }

   protected void setModuleArchive(File var1) throws IOException {
      if (debug) {
         Debug.say("setting module archive: " + var1.toString());
      }

      this.moduleArchive = var1;
      if (!var1.getName().endsWith(".xml")) {
         if (this.vjf == null && var1 != null) {
            this.vjf = ApplicationFileManager.newInstance(this.moduleArchive).getVirtualJarFile();
         }

      }
   }

   protected void setModuleType(ModuleType var1) {
      if (debug) {
         Debug.say("setting module type: " + var1);
      }

      this.moduleType = var1;
   }

   public void setParent(WebLogicDeployableObject var1) {
      this.parent = var1;
   }

   public WebLogicDeployableObject getParent() {
      return this.parent;
   }

   public VirtualJarFile getVirtualJarFile() {
      return this.vjf;
   }

   public String getUri() {
      return this.uri;
   }

   protected void setUri(String var1) {
      if (debug) {
         Debug.say("setting module uri: " + var1);
      }

      this.uri = var1;
   }

   public GenericClassLoader getOrCreateGCL() throws IOException {
      if (this.gcl == null) {
         ClassLoader var1 = Thread.currentThread().getContextClassLoader();
         if (this.moduleArchive.getName().endsWith(".xml")) {
            this.gcl = new GenericClassLoader(NullClassFinder.NULL_FINDER, var1);
         } else {
            JarClassFinder var2 = new JarClassFinder(this.moduleArchive);
            this.gcl = new GenericClassLoader(new MultiClassFinder(), var1);
            this.gcl.addClassFinder(var2);
         }
      }

      return this.gcl;
   }

   public void close() {
      Iterator var1 = this.subModules.iterator();

      while(var1.hasNext()) {
         WebLogicDeployableObject var2 = (WebLogicDeployableObject)var1.next();
         if (var2 != null) {
            var2.close();
         }
      }

      this.closeGCL();
      this.closeVJF();
      this.closeResourceFinder();
      if (this.deleteOnClose && !debug) {
         FileUtils.remove(this.moduleArchive.getParentFile());
      }

      this.ddMap.clear();
   }

   protected void closeGCL() {
      if (this.gcl != null) {
         this.gcl.close();
      }

      this.gcl = null;
   }

   protected void closeResourceFinder() {
      if (this.resourceFinder != null) {
         this.resourceFinder.close();
      }

      this.resourceFinder = null;
   }

   protected void closeVJF() {
      if (this.vjf != null) {
         try {
            if (debug) {
               Debug.say("Closing " + this.vjf.getName());
            }

            this.vjf.close();
         } catch (IOException var2) {
         }
      }

      this.vjf = null;
   }

   public void setResourceFinder(ClassFinder var1) {
      this.resourceFinder = var1;
   }

   public ClassFinder getResourceFinder() {
      return this.resourceFinder;
   }

   public String getContextRoot() {
      return this.contextRoot;
   }

   public void setContextRoot(String var1) {
      this.contextRoot = var1;
   }

   protected class DDRootFields {
      private DDBeanRootImpl root;

      DDRootFields(DDBeanRootImpl var2) {
         this.setRoot(var2);
      }

      public DDBeanRootImpl getRoot() {
         return this.root;
      }

      public void setRoot(DDBeanRootImpl var1) {
         this.root = var1;
      }
   }
}
