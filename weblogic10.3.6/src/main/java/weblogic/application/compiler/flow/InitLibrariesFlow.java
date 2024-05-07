package weblogic.application.compiler.flow;

import java.io.File;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryInitializer;
import weblogic.application.library.LibraryMetadataCache;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.logging.Loggable;
import weblogic.utils.Getopt2;
import weblogic.utils.StringUtils;
import weblogic.utils.compiler.ToolFailureException;

public class InitLibrariesFlow extends CompilerFlow {
   private static final boolean debug = false;
   private LibraryInitializer libraryInitializer = null;

   public InitLibrariesFlow(CompilerCtx var1, boolean var2) {
      super(var1);
      if (!var2) {
         LibraryMetadataCache.getInstance().disableCache();
      }

   }

   public void compile() throws ToolFailureException {
      Getopt2 var1 = this.ctx.getOpts();
      if (this.initLibraries(var1)) {
         File var2 = new File(System.getProperty("java.io.tmpdir"), "appc_libraries");
         this.libraryInitializer = new LibraryInitializer(var2);
         if (this.ctx.isVerbose()) {
            this.libraryInitializer.setVerbose();
         }

         if (var1.hasOption("librarydir")) {
            try {
               this.libraryInitializer.registerLibdir(var1.getOption("librarydir"));
            } catch (LoggableLibraryProcessingException var5) {
               throw new ToolFailureException(var5.getLoggable().getMessage(), var5);
            }

            var1.removeOption("librarydir");
         }

         if (var1.hasOption("library")) {
            this.registerLibraries(var1.getOption("library"));
            var1.removeOption("library");
         }

         try {
            this.libraryInitializer.initRegisteredLibraries();
         } catch (LoggableLibraryProcessingException var4) {
            throw new ToolFailureException(var4.getLoggable().getMessage(), var4);
         }
      }

   }

   private boolean initLibraries(Getopt2 var1) {
      return var1.hasOption("library") || var1.hasOption("librarydir");
   }

   private void registerLibraries(String var1) throws ToolFailureException {
      String[] var2 = StringUtils.splitCompletely(var1, ",");
      boolean var3 = true;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4];
         String var6 = var2[var4];
         int var7 = var2[var4].indexOf("@");
         if (var7 > -1) {
            var5 = var2[var4].substring(0, var7);
            var6 = var2[var4].substring(var7);
         }

         File var8 = new File(var5);

         try {
            LibraryData var9 = this.parseLibraryArg(var8, var6);
            this.libraryInitializer.registerLibrary(var8, var9);
         } catch (LoggableLibraryProcessingException var10) {
            var10.getLoggable().log();
            var3 = false;
         }
      }

      if (!var3) {
         Loggable var11 = J2EELogger.logAppcLibraryRegistrationFailedLoggable();
         throw new ToolFailureException(var11.getMessage());
      }
   }

   private LibraryData parseLibraryArg(File var1, String var2) throws LoggableLibraryProcessingException {
      if (var2.indexOf("@") == -1) {
         return LibraryData.newEmptyInstance(var1);
      } else {
         String[] var3 = StringUtils.splitCompletely(var2, "@");
         String var4 = null;
         String var5 = null;
         String var6 = null;

         for(int var7 = 0; var7 < var3.length; ++var7) {
            if (var3[var7].indexOf("=") != -1) {
               String[] var8 = StringUtils.splitCompletely(var3[var7], "=");
               if (var8[0].equalsIgnoreCase("name")) {
                  var4 = var8[1];
               }

               if (var8[0].equalsIgnoreCase("libspecver")) {
                  var5 = var8[1];
               }

               if (var8[0].equalsIgnoreCase("libimplver")) {
                  var6 = var8[1];
               }
            }
         }

         return LibraryLoggingUtils.initLibraryData(var4, var5, var6, var1);
      }
   }

   public void cleanup() {
      if (this.ctx.unregisterLibrariesOnExit() && !Boolean.getBoolean("weblogic.application.compiler.flow.InitLibrariesFlow.KeepLibrariesOnExit") && this.libraryInitializer != null) {
         this.libraryInitializer.cleanup();
      }

   }
}
