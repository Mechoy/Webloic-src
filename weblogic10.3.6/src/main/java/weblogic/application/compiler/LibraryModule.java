package weblogic.application.compiler;

import java.io.File;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public class LibraryModule extends EARModule {
   private final EARModule delegate;

   public LibraryModule(EARModule var1) {
      super(var1.getURI(), var1.getAltDD());
      this.delegate = var1;
   }

   public boolean isLibrary() {
      return true;
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      this.populateMVI(var1, var2);
   }

   public ModuleValidationInfo getModuleValidationInfo() {
      return this.delegate.getModuleValidationInfo();
   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      this.delegate.populateMVI(var1, var2);
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      this.delegate.merge(var1);
   }

   public void writeDescriptors(CompilerCtx var1) throws ToolFailureException {
      this.delegate.writeDescriptors(var1);
   }

   public ClassFinder getClassFinder() {
      return this.delegate.getClassFinder();
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      this.delegate.initModuleClassLoader(var1, var2);
   }

   public GenericClassLoader getModuleClassLoader() {
      return this.delegate.getModuleClassLoader();
   }

   public void setAltDDFile(File var1) {
      this.delegate.setAltDDFile(var1);
   }

   public File getAltDDFile() {
      return this.delegate.getAltDDFile();
   }

   public boolean isSplitDir(CompilerCtx var1) {
      return this.delegate.isSplitDir(var1);
   }

   public String getURI() {
      return this.delegate.getURI();
   }

   public String getAltDD() {
      return this.delegate.getAltDD();
   }

   public boolean isArchive() {
      return this.delegate.isArchive();
   }

   public void setArchive(boolean var1) {
      this.delegate.setArchive(var1);
   }

   public File getOutputDir() {
      return this.delegate.getOutputDir();
   }

   public void setOutputDir(File var1) {
      this.delegate.setOutputDir(var1);
   }

   public String getOutputFileName() {
      return this.delegate.getOutputFileName();
   }

   public void setOutputFileName(String var1) {
      this.delegate.setOutputFileName(var1);
   }

   public VirtualJarFile getVirtualJarFile() {
      return this.delegate.getVirtualJarFile();
   }

   public void setVirtualJarFile(VirtualJarFile var1) {
      this.delegate.setVirtualJarFile(var1);
   }

   public void cleanup() {
      this.delegate.cleanup();
   }

   public boolean needsPackaging() {
      return this.delegate.needsPackaging();
   }

   public void setNeedsPackaging(boolean var1) {
      this.delegate.setNeedsPackaging(var1);
   }

   public String toString() {
      return this.delegate.toString() + " (library)";
   }

   public ModuleType getModuleType() {
      return this.delegate.getModuleType();
   }

   public DescriptorBean getRootBean() {
      return this.delegate.getRootBean();
   }

   public String[] getDescriptorUris() {
      return this.delegate.getDescriptorUris();
   }

   public DescriptorBean getRootBean(String var1) {
      return this.delegate.getRootBean(var1);
   }
}
