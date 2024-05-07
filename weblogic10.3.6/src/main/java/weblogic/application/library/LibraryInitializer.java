package weblogic.application.library;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.internal.library.LibraryRegistry;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.application.utils.PathUtils;
import weblogic.utils.FileUtils;

public final class LibraryInitializer {
   private static final LibraryRegistry libraryRegistry = LibraryRegistry.getRegistry();
   private final ApplicationFactoryManager libFactories;
   private final File baseExtractDir;
   private final Collection libdirs;
   private boolean verbose;
   private boolean silent;

   public LibraryInitializer() {
      this(new File(System.getProperty("java.io.tmpdir"), "libraries"), LibraryUtils.initStandardAppLibraryFactories());
   }

   public LibraryInitializer(File var1) {
      this(var1, LibraryUtils.initStandardAppLibraryFactories());
   }

   public LibraryInitializer(File var1, ApplicationFactoryManager var2) {
      this.libdirs = new HashSet();
      this.verbose = false;
      this.silent = false;
      this.baseExtractDir = var1;
      this.libFactories = var2;
   }

   public void setVerbose() {
      this.verbose = true;
   }

   public void setSilent() {
      this.silent = true;
   }

   public Library[] getAllLibraries() {
      Collection var1 = libraryRegistry.getAll();
      return (Library[])((Library[])var1.toArray(new Library[var1.size()]));
   }

   public void cleanup() {
      LibraryLoggingUtils.cleanupLibrariesAndRemove();
      FileUtils.remove(this.baseExtractDir);
   }

   public void registerLibrary(File var1, LibraryData var2) throws LoggableLibraryProcessingException {
      LibraryLoggingUtils.checkLibraryExists(var1);
      LibraryData var3 = LibraryLoggingUtils.initLibraryData(var1);
      var3 = mergeData(var3, var2, var1);
      LibraryDefinition var4 = this.getLibraryDefinition(var3);
      LibraryLoggingUtils.registerLibrary(var4, this.verbose);
   }

   public void initRegisteredLibraries() throws LoggableLibraryProcessingException {
      Collection var1 = libraryRegistry.getAll();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         LibraryDefinition var3 = (LibraryDefinition)var2.next();
         LibraryLoggingUtils.initLibraryDefinition(var3);
      }

   }

   public void registerLibdir(String var1) throws LoggableLibraryProcessingException {
      File var2 = new File(var1);
      LibraryLoggingUtils.checkLibdirIsValid(var2);

      try {
         if (!this.libdirs.add(var2.getCanonicalFile())) {
            return;
         }
      } catch (IOException var7) {
         throw new AssertionError(var7);
      }

      File[] var3 = var2.listFiles();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         try {
            this.registerLibrary(var3[var4], LibraryData.newEmptyInstance(var3[var4]));
         } catch (LoggableLibraryProcessingException var6) {
            if (!this.silent) {
               var6.getLoggable().log();
            }
         }
      }

   }

   private LibraryDefinition getLibraryDefinition(LibraryData var1) throws LoggableLibraryProcessingException {
      File var2 = new File(this.baseExtractDir, PathUtils.generateTempPath((String)null, var1.getName(), var1.getSpecificationVersion() + var1.getImplementationVersion()));
      LibraryDefinition var3 = LibraryLoggingUtils.getLibraryDefinition(var1, var2, this.libFactories.getLibraryFactories());
      return var3;
   }

   private static LibraryData mergeData(LibraryData var0, LibraryData var1, File var2) throws LoggableLibraryProcessingException {
      if (var1.getName() == null && var0.getName() == null) {
         var1 = LibraryData.cloneWithNewName(getName(var2), var1);
      }

      var0 = var0.importData(var1);
      var1 = var1.importData(var0);
      LibraryLoggingUtils.handleAppcLibraryInfoMismatch(var0, var1, var2);
      return var0;
   }

   private static String getName(File var0) {
      if (var0.isDirectory()) {
         return var0.getName();
      } else {
         return var0.getName().indexOf(".") < 0 ? var0.getName() : var0.getName().substring(0, var0.getName().lastIndexOf("."));
      }
   }
}
