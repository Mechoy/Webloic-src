package weblogic.servlet.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.IllegalSpecVersionTypeException;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.application.library.LibraryReferencer;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.War;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class WebAppLibraryUtils {
   private WebAppLibraryUtils() {
   }

   public static LibraryManager getEmptyWebAppLibraryManager() {
      return getEmptyWebAppLibraryManager((String)null);
   }

   public static LibraryManager getEmptyWebAppLibraryManager(String var0) {
      return new LibraryManager(getLibraryReferencer(var0));
   }

   public static LibraryManager getWebAppLibraryManager(WeblogicWebAppBean var0, String var1) throws ToolFailureException {
      LibraryManager var2 = new LibraryManager(getLibraryReferencer(var1));
      initWebAppLibraryManager(var2, var0, var1);
      return var2;
   }

   public static void initWebAppLibraryManager(LibraryManager var0, WeblogicWebAppBean var1, String var2) throws ToolFailureException {
      if (var1 != null) {
         if (var1.getLibraryRefs() != null) {
            LibraryReference[] var3 = getWebLibRefs(var1, var2);
            var0.lookup(var3);
            if (var0.hasUnresolvedReferences()) {
               throw new ToolFailureException("Error: " + var0.getUnresolvedReferencesError());
            }
         }
      }
   }

   public static LibraryReference[] getWebLibRefs(WeblogicWebAppBean var0, String var1) throws ToolFailureException {
      J2EELibraryReference[] var2 = null;

      try {
         var2 = LibraryReferenceFactory.getWebLibReference(var0.getLibraryRefs());
      } catch (IllegalSpecVersionTypeException var4) {
         throw new ToolFailureException(HTTPLogger.logIllegalWebLibSpecVersionRefLoggable(var1 == null ? "" : var1, var4.getSpecVersion()).getMessage());
      }

      return (LibraryReference[])(var2 == null ? new LibraryReference[0] : var2);
   }

   private static War extractWebAppLibraries(LibraryManager var0, File var1, String var2) throws IOException {
      War var3 = new War(var2);
      extractWebAppLibraries(var0, var3, var1);
      return var3;
   }

   public static void extractWebAppLibraries(LibraryManager var0, War var1, File var2) throws IOException {
      Library[] var3 = var0.getReferencedLibraries();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var1.addLibrary(var3[var4], var2);
      }

   }

   public static void copyWebAppLibraries(LibraryManager var0, File var1, File var2) throws IOException {
      War var3 = extractWebAppLibraries(var0, var1, "webappmerge");
      writeWar(var3, var2);
   }

   public static void writeWar(War var0, File var1) throws IOException {
      ClassFinder var2 = var0.getResourceFinder("/");
      String var3 = var2.getClassPath();
      var2.close();
      String[] var4 = StringUtils.splitCompletely(var3, File.pathSeparator);

      for(int var5 = 0; var5 < var4.length; ++var5) {
         File var6 = new File(var4[var5]);
         if (var6.exists()) {
            Set var7 = getExcludedFiles(var6);
            FileUtils.copyNoOverwrite(var6, var1, var7);
         }
      }

   }

   public static void removeLibraryReferences(WeblogicWebAppBean var0) {
      if (var0 != null) {
         LibraryRefBean[] var1 = var0.getLibraryRefs();
         if (var1 != null) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               var0.destroyLibraryRef(var1[var2]);
            }

         }
      }
   }

   private static Set getExcludedFiles(File var0) {
      HashSet var1 = new HashSet(5);
      var1.add(new File(var0, "WEB-INF/web.xml"));
      var1.add(new File(var0, "WEB-INF/weblogic.xml"));
      var1.add(new File(var0, CachableLibMetadataType.ANNOTATED_CLASSES.getName()));
      var1.add(new File(var0, CachableLibMetadataType.TLD.getName()));
      var1.add(new File(var0, CachableLibMetadataType.FACE_BEANS.getName()));
      return var1;
   }

   public static LibraryReferencer getLibraryReferencer(String var0) {
      String var1 = "Unresolved WebApp library references defined in weblogic.xml";
      if (var0 != null) {
         var1 = var1 + ", of module '" + var0 + "'";
      }

      return new LibraryReferencer((String)null, (RuntimeMBean)null, var1);
   }

   public static LibraryReference[] initAllWebLibRefs(File var0) throws ToolFailureException {
      ArrayList var1 = new ArrayList();
      File[] var2 = FileUtils.find(var0, new FileFilter() {
         public boolean accept(File var1) {
            return var1.isFile() && var1.getName().equals("weblogic.xml") && var1.getParentFile().getName().equals("WEB-INF");
         }
      });

      for(int var3 = 0; var3 < var2.length; ++var3) {
         LibraryReference[] var4 = initWebLibRefs(var2[var3].getParentFile().getParentFile());
         if (var4 != null) {
            var1.addAll(Arrays.asList(var4));
         }
      }

      return (LibraryReference[])((LibraryReference[])var1.toArray(new LibraryReference[var1.size()]));
   }

   private static LibraryReference[] initWebLibRefs(File var0) throws ToolFailureException {
      VirtualJarFile var1 = null;
      LibraryReference[] var2 = null;

      try {
         var1 = VirtualJarFactory.createVirtualJar(var0);
         WebAppDescriptor var3 = new WebAppDescriptor(var1);
         WeblogicWebAppBean var4 = WarUtils.getWlWebAppBean(var3);
         var2 = getWebLibRefs(var4, var0.getName());
      } catch (IOException var13) {
         throw new ToolFailureException("Error parsing weblogic.xml");
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var12) {
            }
         }

      }

      return var2;
   }
}
