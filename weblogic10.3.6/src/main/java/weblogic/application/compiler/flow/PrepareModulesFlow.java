package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.BuildtimeApplicationContext;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.compiler.LibraryModule;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class PrepareModulesFlow extends CompilerFlow {
   private final BuildtimeApplicationContext libCtx;

   public PrepareModulesFlow(CompilerCtx var1) {
      super(var1);
      this.libCtx = (BuildtimeApplicationContext)var1.getApplicationContext();
   }

   public void compile() throws ToolFailureException {
      this.prepareModules(this.ctx.getModules());
   }

   private void prepareModules(EARModule[] var1) throws ToolFailureException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (this.libCtx.isLibraryURI(var1[var2].getURI())) {
            var1[var2] = new LibraryModule(var1[var2]);
         }

         this.setAltDDFile(var1[var2]);
         this.prepareModule(var1[var2]);
      }

   }

   private void setAltDDFile(EARModule var1) throws ToolFailureException {
      File var2 = null;
      String var3 = var1.getAltDD();
      if (var3 != null) {
         var2 = J2EEApplicationService.getAltDDFile(var3, this.ctx.getVSource());
         if (var2 == null) {
            throw new ToolFailureException(J2EELogger.logAppcMissingModuleAltDDFileLoggable(var1.getAltDD(), var1.getURI()).getMessage());
         }
      }

      var1.setAltDDFile(var2);
   }

   private void prepareModule(EARModule var1) throws ToolFailureException {
      String var2 = var1.getURI();
      File var3 = new File(this.ctx.getOutputDir(), var2);
      if (!var3.exists()) {
         if (var1.isSplitDir(this.ctx)) {
            var3 = this.ctx.getEar().getModuleRoots(var2)[0];
         }

         if (!var3.exists() && this.libCtx.isLibraryURI(var2)) {
            var3 = this.libCtx.getURILink(var2);
         }
      }

      if (!var3.exists()) {
         throw new ToolFailureException(J2EELogger.logAppcCantFindDeclaredModuleLoggable(var2).getMessage());
      } else {
         VirtualJarFile var4 = null;
         if (!var3.getPath().endsWith(".xml")) {
            try {
               File[] var5 = this.ctx.getEar().getModuleRoots(var2);
               if (var5.length > 1 && !var5[0].isDirectory()) {
                  var4 = VirtualJarFactory.createVirtualJar(var5[0]);
               } else {
                  var4 = VirtualJarFactory.createVirtualJar(var5);
               }

               if (var5.length == 1) {
                  var1.setArchive(var5[0].isFile());
               } else {
                  var1.setArchive(false);
               }
            } catch (IOException var6) {
               throw new ToolFailureException(J2EELogger.logAppcErrorAccessingFileLoggable(var2, var6.toString()).getMessage(), var6);
            }

            var1.setVirtualJarFile(var4);
         } else {
            var1.setArchive(false);
         }

         var1.setOutputDir(var3);
         var1.setOutputFileName(var3.getPath());
      }
   }

   public void cleanup() {
   }
}
