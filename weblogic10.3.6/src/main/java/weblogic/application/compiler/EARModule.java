package weblogic.application.compiler;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.utils.IOUtils;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.module.WLDFModule;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public abstract class EARModule {
   protected static final boolean debug = false;
   private boolean isArchive;
   private boolean needsPackaging;
   private File outputDir;
   private String outputFileName;
   private final String moduleURI;
   private VirtualJarFile vjf;
   private final String altDD;
   private File altDDFile;
   private final ModuleValidationInfo mvi;
   private final Map<String, DescriptorBean> descriptors = new Hashtable();
   protected GenericClassLoader moduleClassLoader = null;
   public static String ORIGINAL_DESCRIPTOR_SUFFIX = ".orig";
   public static final String WL_DIAG_URI = "META-INF/weblogic-diagnostics.xml";

   public EARModule(String var1, String var2) {
      this.moduleURI = var1;
      this.altDD = var2;
      this.mvi = new ModuleValidationInfo(var1);
   }

   public abstract void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException;

   public abstract ClassFinder getClassFinder();

   public ClassFinder getResourceFinder() {
      return null;
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      if (var2 != null) {
         this.moduleClassLoader = new GenericClassLoader(this.getClassFinder(), var2);
      } else {
         this.moduleClassLoader = new GenericClassLoader(this.getClassFinder());
      }

      this.moduleClassLoader.setAnnotation(new Annotation(var1.getApplicationContext().getApplicationId(), this.getURI()));
   }

   public GenericClassLoader getModuleClassLoader() {
      return this.moduleClassLoader;
   }

   public abstract void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException;

   public ModuleValidationInfo getModuleValidationInfo() {
      return this.mvi;
   }

   public boolean isLibrary() {
      return false;
   }

   public void setAltDDFile(File var1) {
      this.altDDFile = var1;
   }

   public File getAltDDFile() {
      return this.altDDFile;
   }

   public boolean isSplitDir(CompilerCtx var1) {
      return var1 != null && var1.getApplicationContext() != null && var1.getApplicationContext().getApplicationFileManager() != null ? var1.getApplicationContext().getApplicationFileManager().isSplitDirectory() : false;
   }

   public String getURI() {
      return this.moduleURI;
   }

   public String getAltDD() {
      return this.altDD;
   }

   public boolean isArchive() {
      return this.isArchive;
   }

   public void setArchive(boolean var1) {
      this.isArchive = var1;
   }

   public boolean needsPackaging() {
      return this.needsPackaging;
   }

   public void setNeedsPackaging(boolean var1) {
      this.needsPackaging = var1;
   }

   public File getOutputDir() {
      return this.outputDir;
   }

   public void setOutputDir(File var1) {
      this.outputDir = var1;
   }

   public String getOutputFileName() {
      return this.outputFileName;
   }

   public void setOutputFileName(String var1) {
      this.outputFileName = var1;
   }

   public VirtualJarFile getVirtualJarFile() {
      return this.vjf;
   }

   public void setVirtualJarFile(VirtualJarFile var1) {
      this.vjf = var1;
   }

   public void cleanup() {
      IOUtils.forceClose(this.vjf);
      this.vjf = null;
      if (this.moduleClassLoader != null) {
         this.moduleClassLoader.close();
      }

   }

   public void writeDescriptors(CompilerCtx var1) throws ToolFailureException {
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
   }

   public void write(CompilerCtx var1) throws ToolFailureException {
      this.writeDescriptors(var1);
   }

   public String toString() {
      return this.getURI();
   }

   protected void addRootBean(String var1, DescriptorBean var2) {
      this.descriptors.put(var1, var2);
   }

   public String[] getDescriptorUris() {
      return (String[])((String[])this.descriptors.keySet().toArray(new String[0]));
   }

   public DescriptorBean getRootBean(String var1) {
      return (DescriptorBean)this.descriptors.get(var1);
   }

   public DescriptorBean getRootBean() {
      return null;
   }

   public abstract ModuleType getModuleType();

   protected void processAnnotations() throws ClassNotFoundException, ErrorCollectionException, NoSuchMethodException, NoClassDefFoundError {
   }

   protected final void processAnnotations(CompilerCtx var1) throws ToolFailureException {
      if (!var1.isBasicView() && (var1.isReadOnlyInvocation() || var1.isWriteInferredDescriptors())) {
         try {
            this.processAnnotations();
         } catch (ClassNotFoundException var3) {
            this.throwAnnotationProcessingException(var1, var3);
         } catch (NoSuchMethodException var4) {
            this.throwAnnotationProcessingException(var1, var4);
         } catch (NoClassDefFoundError var5) {
            this.throwAnnotationProcessingException(var1, var5);
         } catch (ErrorCollectionException var6) {
            this.throwAnnotationProcessingException(var1, var6);
         }
      }

   }

   private void throwAnnotationProcessingException(CompilerCtx var1, Throwable var2) throws ToolFailureException {
      if (var1.verifyLibraryReferences()) {
         var2.printStackTrace();
         throw new ToolFailureException("Unable to process annotations for module " + this.getURI(), var2);
      } else {
         var2.printStackTrace();
      }
   }

   protected void addDiagnosticDDRootBean(CompilerCtx var1) {
      File var2 = this.getExternalDiagnosticDescriptorFile(var1, this);

      try {
         DescriptorBean var3 = var2 != null ? WLDFModule.getDescriptorBean(var2, var1.getConfigDir(), var1.getPlanBean(), this.getURI(), "META-INF/weblogic-diagnostics.xml") : WLDFModule.getDescriptorBean(this.getVirtualJarFile(), var1.getConfigDir(), var1.getPlanBean(), this.getURI(), "META-INF/weblogic-diagnostics.xml");
         if (var3 != null) {
            this.addRootBean("META-INF/weblogic-diagnostics.xml", var3);
         } else if (var1.getEar() == null) {
            this.addRootBean("META-INF/weblogic-diagnostics.xml", (new DescriptorManager()).createDescriptorRoot(WLDFResourceBean.class).getRootBean());
         }
      } catch (Exception var4) {
      }

   }

   private File getExternalDiagnosticDescriptorFile(CompilerCtx var1, EARModule var2) {
      DeploymentPlanBean var3 = var1.getPlanBean();
      File var4 = var1.getConfigDir();
      if (var3 != null) {
         ModuleOverrideBean[] var5 = var3.getModuleOverrides();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getModuleName().equals(var2.getURI()) && var5[var6].getModuleType().equals(var2.getModuleType().toString())) {
               ModuleDescriptorBean[] var7 = var5[var6].getModuleDescriptors();

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  if (var7[var8].isExternal() && var7[var8].getRootElement().equals("wldf-resource")) {
                     File var9 = new File(var4, var7[var8].getUri());
                     if (var9.isFile() && var9.exists()) {
                        return var9;
                     }
                  }
               }
            }
         }
      }

      return null;
   }
}
