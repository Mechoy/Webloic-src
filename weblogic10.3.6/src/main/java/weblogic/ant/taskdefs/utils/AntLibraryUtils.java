package weblogic.ant.taskdefs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import weblogic.application.library.LibraryInitializer;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;

public final class AntLibraryUtils {
   private AntLibraryUtils() {
   }

   public static void validateLibraries(File var0, Collection var1) {
      ArrayList var2 = null;
      if (var0 != null) {
         var2 = new ArrayList(1);
         var2.add(var0);
      }

      validateLibraries((Collection)var2, var1);
   }

   public static void validateLibraries(Collection var0, Collection var1) {
      Iterator var2;
      if (var0 != null) {
         var2 = var0.iterator();

         while(var2.hasNext()) {
            File var3 = (File)var2.next();
            if (!var3.exists()) {
               throw new BuildException("librarydir: " + var3.getAbsolutePath() + " does not exist or cannot be read.");
            }

            if (!var3.isDirectory()) {
               throw new BuildException("librarydir: " + var3.getAbsolutePath() + " is not a directory.");
            }
         }
      }

      var2 = var1.iterator();

      LibraryElement var4;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var4 = (LibraryElement)var2.next();
         if (var4.getFile() == null) {
            throw new BuildException("Library's file attr must be set.");
         }
      } while(var4.getFile().exists());

      throw new BuildException("Library " + var4.getFile().getAbsolutePath() + " does not exist or cannot be read.");
   }

   public static void registerLibraries(LibraryInitializer var0, File[] var1, LibraryElement[] var2, boolean var3) {
      registerLibdirs(var0, var1);
      registerLibraries(var0, var2, var3);
   }

   private static void registerLibdirs(LibraryInitializer var0, File[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            try {
               var0.registerLibdir(var1[var2].getAbsolutePath());
            } catch (LoggableLibraryProcessingException var4) {
               throw new BuildException(var4.getLoggable().getMessage());
            }
         }

      }
   }

   private static void registerLibraries(LibraryInitializer var0, LibraryElement[] var1, boolean var2) {
      boolean var3 = true;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         try {
            var0.registerLibrary(var1[var4].getFile(), var1[var4].getLibraryData());
         } catch (LoggableLibraryProcessingException var6) {
            if (!var2) {
               var6.getLoggable().log();
            }

            var3 = false;
         }
      }

      if (!var3) {
         throw new BuildException(J2EELogger.logAppcLibraryRegistrationFailedLoggable().getMessage());
      }
   }

   public static void logRegistryContent(Project var0, int var1) {
      var0.log(LibraryLoggingUtils.registryToString(), var1);
   }

   public static String getClassPath(File[] var0, ClassFinder var1) {
      String var2 = "";
      MultiClassFinder var3 = new MultiClassFinder();

      try {
         for(int var4 = 0; var4 < var0.length; ++var4) {
            if (!var0[var4].isFile() || var0[var4].getName().toLowerCase().endsWith(".jar")) {
               var3.addFinder(new ClasspathClassFinder2(var0[var4].getAbsolutePath()));
            }
         }

         var3.addFinder(var1);
         var2 = var3.getClassPath();
         return var2;
      } finally {
         var3.close();
      }
   }

   public static List getLibraryFlags(File var0, Collection var1) {
      ArrayList var2 = new ArrayList();
      if (var0 != null) {
         var2.add("-librarydir");
         var2.add(var0.getAbsolutePath());
      }

      if (var1.isEmpty()) {
         return var2;
      } else {
         var2.add("-library");
         StringBuffer var3 = new StringBuffer();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            LibraryElement var5 = (LibraryElement)var4.next();
            var3.append(var5.getFile().getAbsolutePath());
            if (var5.getName() != null) {
               var3.append("@").append("name").append("=").append(var5.getName());
            }

            if (var5.getSpecificationVersion() != null) {
               var3.append("@").append("libspecver").append("=").append(var5.getSpecificationVersion());
            }

            if (var5.getImplementationVersion() != null) {
               var3.append("@").append("libimplver").append("=").append(var5.getImplementationVersion());
            }

            if (var4.hasNext()) {
               var3.append(",");
            }
         }

         var2.add(var3.toString());
         return var2;
      }
   }
}
