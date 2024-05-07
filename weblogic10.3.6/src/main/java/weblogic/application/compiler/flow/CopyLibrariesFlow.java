package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.ApplicationConstants;
import weblogic.application.compiler.BuildtimeApplicationContext;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.utils.IOUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class CopyLibrariesFlow extends CompilerFlow {
   private final BuildtimeApplicationContext libCtx;

   public CopyLibrariesFlow(CompilerCtx var1) {
      super(var1);
      this.libCtx = (BuildtimeApplicationContext)var1.getApplicationContext();
   }

   public void compile() throws ToolFailureException {
      this.processLibraries(this.ctx.getModules());
      this.copyAdditionalResources();
   }

   public void cleanup() {
   }

   private void processLibraries(EARModule[] var1) throws ToolFailureException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].isLibrary()) {
            this.copyLibrary(var1[var2]);
         }
      }

   }

   private void copyLibrary(EARModule var1) throws ToolFailureException {
      File var2 = new File(this.ctx.getOutputDir(), var1.getURI());
      File[] var3 = this.getRoots(var1);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         try {
            if (!var3[var4].equals(var2)) {
               FileUtils.copy(var3[var4], var2);
            }
         } catch (IOException var6) {
            throw new ToolFailureException("Failed to copy library " + var3[var4], var6);
         }

         IOUtils.forceClose(var1.getVirtualJarFile());

         try {
            if (var1.getVirtualJarFile() != null) {
               var1.setVirtualJarFile(VirtualJarFactory.createVirtualJar(var2));
            }

            var1.setOutputDir(var2);
         } catch (IOException var7) {
            throw new ToolFailureException(J2EELogger.logAppcErrorAccessingFileLoggable(var2.getAbsolutePath(), var7.toString()).getMessage(), var7);
         }
      }

   }

   private File[] getRoots(EARModule var1) {
      VirtualJarFile var2 = var1.getVirtualJarFile();
      return var2 == null ? new File[]{new File(var1.getOutputFileName())} : var2.getRootFiles();
   }

   private void copyAdditionalResources() throws ToolFailureException {
      ClassFinder var1 = this.ctx.getApplicationContext().getAppClassLoader().getClassFinder();
      String var2 = var1.getClassPath();
      File var3 = new File(this.ctx.getOutputDir(), ApplicationConstants.APP_INF_LIB);
      File var4 = new File(this.ctx.getOutputDir(), ApplicationConstants.APP_INF_CLASSES);
      var3.mkdirs();
      String[] var5 = StringUtils.splitCompletely(var2, File.pathSeparator);

      for(int var6 = 0; var6 < var5.length; ++var6) {
         File var7 = new File(var5[var6]);
         if (var7.exists()) {
            try {
               if (var7.isFile()) {
                  FileUtils.copyNoOverwrite(var7, var3);
               } else if (!var4.getCanonicalPath().startsWith(var7.getCanonicalPath())) {
                  FileUtils.copyNoOverwrite(var7, var4);
               }
            } catch (IOException var9) {
               throw new ToolFailureException("Failed to copy " + var7, var9);
            }
         }
      }

   }
}
