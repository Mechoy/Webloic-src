package weblogic.application.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.compiler.ToolFailureException;

public abstract class WLSModule extends EARModule {
   MultiClassFinder moduleClassFinder = new MultiClassFinder();

   public WLSModule(String var1, String var2) {
      super(var1, var2);
   }

   public ClassFinder getClassFinder() {
      return this.moduleClassFinder;
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      this.moduleClassFinder.addFinder(new ClasspathClassFinder2(this.getOutputDir().getPath()));
      if (this.isSplitDir(var1)) {
         File[] var3 = var1.getEar().getModuleRoots(this.getURI());

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.moduleClassFinder.addFinder(new ClasspathClassFinder2(var3[var4].getAbsolutePath()));
         }
      }

      super.initModuleClassLoader(var1, var2);
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
      this.populateMVI(var1, var2);
   }

   public String getModuleName() {
      return this.getModuleValidationInfo().getModuleName();
   }

   public void setModuleName(String var1) {
      this.getModuleValidationInfo().setModuleName(var1);
   }

   public DescriptorBean getRootBean() {
      return this.getRootBean(this.getURI());
   }

   protected GenericClassLoader getCL(String var1) throws IOException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      if (var2 == null) {
         var2 = ClassLoader.getSystemClassLoader();
      }

      ClassFinder var3 = NullClassFinder.NULL_FINDER;
      if (var2 instanceof GenericClassLoader) {
         var3 = ((GenericClassLoader)var2).getClassFinder();
      }

      return this.createClassLoader(var3, var2, var1);
   }

   private GenericClassLoader createClassLoader(ClassFinder var1, ClassLoader var2, final String var3) throws IOException {
      return new GenericClassLoader(var1, var2) {
         public InputStream getResourceAsStream(String var1) {
            if (var1.equals(var3)) {
               try {
                  return new FileInputStream(new File(WLSModule.this.getOutputFileName()));
               } catch (FileNotFoundException var3x) {
               }
            }

            return super.getResourceAsStream(var1);
         }
      };
   }
}
