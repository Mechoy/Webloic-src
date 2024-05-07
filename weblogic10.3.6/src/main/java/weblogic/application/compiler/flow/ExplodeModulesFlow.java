package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.Debug;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFile;

public final class ExplodeModulesFlow extends CompilerFlow {
   private final boolean explodeLibraries;

   public ExplodeModulesFlow(CompilerCtx var1) {
      this(var1, false);
   }

   public ExplodeModulesFlow(CompilerCtx var1, boolean var2) {
      super(var1);
      this.explodeLibraries = var2;
   }

   public void compile() throws ToolFailureException {
      this.maybeExplodeModules(this.ctx.getModules());
   }

   public void cleanup() throws ToolFailureException {
      EARModule[] var1 = this.ctx.getModules();
      if (!this.ctx.getOpts().hasOption("nopackage")) {
         this.packageModules(var1);
      }

   }

   private void packageModules(EARModule[] var1) throws ToolFailureException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].needsPackaging()) {
            if (debug) {
               Debug.say("Creating output archive: " + var1[var2].getOutputFileName());
            }

            AppcUtils.createOutputArchive(var1[var2].getOutputFileName(), var1[var2].getOutputDir());
         }
      }

   }

   private void maybeExplodeModules(EARModule[] var1) throws ToolFailureException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].isArchive() && (!var1[var2].isLibrary() || this.explodeLibraries)) {
            this.explodeModule(var1[var2]);
         }
      }

   }

   private void explodeModule(EARModule var1) throws ToolFailureException {
      String var2 = this.ctx.getTempDir().getName() + "_" + var1.getURI().replace(File.separatorChar, '_');
      File var3 = AppcUtils.makeOutputDir(var2, this.ctx.getOutputDir(), true);
      if (debug) {
         Debug.say("Expanding " + var1.getVirtualJarFile().getName() + " into " + var3);
      }

      this.expandJarFileIntoDirectory(var1.getVirtualJarFile(), var3);
      var1.setOutputDir(var3);
      var1.setNeedsPackaging(true);
   }

   private void expandJarFileIntoDirectory(VirtualJarFile var1, File var2) throws ToolFailureException {
      try {
         JarFileUtils.extract(var1, var2);
      } catch (IOException var4) {
         throw new ToolFailureException(J2EELogger.logAppcErrorCopyingFilesLoggable(var2.getAbsolutePath(), var4.toString()).getMessage(), var4);
      }
   }
}
