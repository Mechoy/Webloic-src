package weblogic.application.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.DescriptorUpdater;
import weblogic.application.internal.library.BasicLibraryData;
import weblogic.application.internal.library.EarLibraryFactory;
import weblogic.application.internal.library.JarLibraryFactory;
import weblogic.application.internal.library.util.DeweyDecimal;
import weblogic.application.library.ApplicationLibrary;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryConstants;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LibraryProvider;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.application.library.LibraryReferencer;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.spi.EJBLibraryFactory;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.LibraryContextRootOverrideBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.servlet.internal.WarLibraryFactory;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class LibraryUtils {
   private static final DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugLibraries");
   private static ApplicationFactoryManager dumbLibFactories = ApplicationFactoryManager.getEmptyApplicationFactoryManager();
   private static boolean initializedDumbFactories = false;
   private static ApplicationFactoryManager standardLibFactories = ApplicationFactoryManager.getEmptyApplicationFactoryManager();
   private static boolean initializedStandardFactories = false;

   private LibraryUtils() {
   }

   public static boolean isDebugOn() {
      return debugLogger.isDebugEnabled();
   }

   public static void debug(String var0) {
      debugLogger.debug(EarUtils.addClassName(var0));
   }

   public static LibraryReferencer initReferencer(String var0, RuntimeMBean var1, String var2) {
      return new LibraryReferencer(var0, var1, var2);
   }

   public static LibraryReferencer initReferencer(ApplicationContextInternal var0, String var1) {
      return initReferencer(ApplicationVersionUtils.getDisplayName(var0.getApplicationId()), var0.getRuntime(), var1);
   }

   public static LibraryReferencer initAppReferencer(ApplicationContextInternal var0) {
      return initReferencer(ApplicationVersionUtils.getDisplayName(var0.getApplicationId()), var0.getRuntime(), getAppLibRefError());
   }

   public static LibraryReferencer initAppReferencer() {
      return initReferencer((String)null, (RuntimeMBean)null, getAppLibRefError());
   }

   public static LibraryReferencer initAppReferencer(String var0) {
      return initReferencer(var0, (RuntimeMBean)null, getAppLibRefError(var0));
   }

   public static LibraryReferencer initOptPackReferencer(ApplicationContextInternal var0) {
      return initReferencer(ApplicationVersionUtils.getDisplayName(var0.getApplicationId()), var0.getRuntime(), "Unresolved Optional Package references (in META-INF/MANIFEST.MF):");
   }

   public static LibraryReferencer initOptPackReferencer() {
      return initReferencer((String)null, (RuntimeMBean)null, "Unresolved Optional Package references (in META-INF/MANIFEST.MF):");
   }

   private static String getAppLibRefError() {
      return getAppLibRefError((String)null);
   }

   private static String getAppLibRefError(String var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("Unresolved application library references");
      if (var0 != null) {
         var1.append(", for application ").append(var0);
      }

      var1.append(", defined in weblogic-application.xml:");
      return var1.toString();
   }

   public static String nullOrString(DeweyDecimal var0) {
      return var0 == null ? null : var0.toString();
   }

   public static String getName(LibraryMBean var0) {
      return var0.getApplicationName();
   }

   public static String getSpecVersion(LibraryMBean var0) {
      return ApplicationVersionUtils.getLibSpecVersion(ApplicationVersionUtils.getVersionId(var0.getName()));
   }

   public static String getImplVersion(LibraryMBean var0) {
      return ApplicationVersionUtils.getLibImplVersion(ApplicationVersionUtils.getVersionId(var0.getName()));
   }

   public static String toString(BasicLibraryData var0) {
      return toString(var0.getName(), nullOrString(var0.getSpecificationVersion()), var0.getImplementationVersion());
   }

   public static String toString(LibraryMBean var0) {
      return toString(getName(var0), getSpecVersion(var0), getImplVersion(var0));
   }

   public static void resetAppDDs(ApplicationDescriptor var0, DescriptorUpdater var1) throws LoggableLibraryProcessingException {
      try {
         var1.setApplicationDescriptor(var0);
      } catch (IOException var3) {
         LibraryLoggingUtils.errorMerging(var3);
      } catch (XMLStreamException var4) {
         LibraryLoggingUtils.errorMerging(var4);
      }

   }

   public static void importAppLibraries(LibraryManager var0, LibraryContext var1, DescriptorUpdater var2) throws LoggableLibraryProcessingException {
      importAppLibraries(var0, var1, var2, false);
   }

   public static void importAppLibraries(LibraryManager var0, LibraryContext var1, DescriptorUpdater var2, boolean var3) throws LoggableLibraryProcessingException {
      ApplicationDescriptor var4 = var1.getApplicationDescriptor();
      populateContextOverrides(var1);

      try {
         var2.setApplicationDescriptor(new ApplicationDescriptor());
         var1.notifyDescriptorUpdate();
         Library[] var5 = var0.getReferencedLibraries();
         LibraryReference[] var6 = var0.getLibraryReferences();
         MultiClassFinder var7 = new MultiClassFinder();

         for(int var8 = var5.length - 1; var8 >= 0; --var8) {
            processLibraryReference((J2EELibraryReference)var6[var8], var5[var8], var1, var3, var7);
         }

         var1.addClassFinder(var7);
         ApplicationBean var11 = var1.getApplicationDD();
         if (var11 != null) {
            overrideContextRoot(var11, var1.getContextRootOverrideMap());
         }

         LibraryLoggingUtils.updateDescriptor(var1.getApplicationDescriptor(), var11);
         var1.notifyDescriptorUpdate();
         if (var4 != null) {
            LibraryLoggingUtils.mergeDescriptors(var4, var1.getApplicationDescriptor());
            var2.setApplicationDescriptor(var4);
            var1.notifyDescriptorUpdate();
         }

         if (isDebugOn()) {
            var1.getApplicationDescriptor().dumpMergedApplicationDescriptor(var1.getRefappName());
         }
      } catch (IOException var9) {
         LibraryLoggingUtils.errorMerging(var9);
      } catch (XMLStreamException var10) {
         LibraryLoggingUtils.errorMerging(var10);
      }

   }

   private static void processLibraryReference(J2EELibraryReference var0, Library var1, LibraryContext var2, boolean var3, MultiClassFinder var4) throws LoggableLibraryProcessingException {
      LibraryLoggingUtils.checkIsAppLibrary(var1);
      ApplicationLibrary var5 = (ApplicationLibrary)var1;
      LibraryLoggingUtils.importLibrary(var5, var0, var2, var3, var4);
   }

   public static String toString(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(LibraryConstants.LIBRARY_NAME).append(": ").append(var0);
      if (var1 != null) {
         var3.append(", ").append(LibraryConstants.SPEC_VERSION_NAME).append(": ").append(var1);
      }

      if (var2 != null) {
         var3.append(", ").append(LibraryConstants.IMPL_VERSION_NAME).append(": ").append(var2);
      }

      return var3.toString();
   }

   public static J2EELibraryReference[] initLibRefs(File var0) throws LibraryProcessingException {
      WeblogicApplicationBean var1 = null;

      try {
         ApplicationDescriptor var2 = new ApplicationDescriptor(VirtualJarFactory.createVirtualJar(var0));
         var1 = var2.getWeblogicApplicationDescriptor();
      } catch (Exception var3) {
         throw new LibraryProcessingException(var3);
      }

      return var1 != null && var1.getLibraryRefs() != null ? LibraryLoggingUtils.initLibRefs(var1.getLibraryRefs()) : new J2EELibraryReference[0];
   }

   public static LibraryReference[] iniOptPackRefs(File var0) throws LibraryProcessingException {
      VirtualJarFile var1 = null;

      LibraryReference[] var3;
      try {
         var1 = VirtualJarFactory.createVirtualJar(var0);
         Manifest var2 = var1.getManifest();
         if (var2 != null) {
            var3 = LibraryReferenceFactory.getOptPackReference(var0.getAbsolutePath(), var2.getMainAttributes());
            return var3;
         }

         var3 = null;
      } catch (IOException var13) {
         throw new LibraryProcessingException(var13);
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var12) {
            }
         }

      }

      return var3;
   }

   public static LibraryReference[] initAllOptPacks(File var0) throws LibraryProcessingException {
      ArrayList var1 = new ArrayList();
      File[] var2 = FileUtils.find(var0, new FileFilter() {
         public boolean accept(File var1) {
            return var1.isFile() && "MANIFEST.MF".equals(var1.getName()) && "META-INF".equals(var1.getParentFile().getName());
         }
      });

      for(int var3 = 0; var3 < var2.length; ++var3) {
         LibraryReference[] var4 = iniOptPackRefs(var2[var3].getParentFile().getParentFile());
         if (var4 != null) {
            var1.addAll(Arrays.asList(var4));
         }
      }

      return (LibraryReference[])((LibraryReference[])var1.toArray(new LibraryReference[var1.size()]));
   }

   public static ApplicationFactoryManager initDumbAppLibraryFactories() {
      synchronized(dumbLibFactories) {
         if (!initializedDumbFactories) {
            initLibraryFactories(dumbLibFactories);
            dumbLibFactories.addLibraryFactory(new WarLibraryFactory.Noop());
            initializedDumbFactories = true;
         }
      }

      return dumbLibFactories;
   }

   public static ApplicationFactoryManager initStandardAppLibraryFactories() {
      synchronized(standardLibFactories) {
         if (!initializedStandardFactories) {
            initLibraryFactories(standardLibFactories);
            standardLibFactories.addLibraryFactory(new WarLibraryFactory());
            initializedStandardFactories = true;
         }
      }

      return standardLibFactories;
   }

   private static void initLibraryFactories(ApplicationFactoryManager var0) {
      var0.addLibraryFactory(new EarLibraryFactory());
      var0.addLibraryFactory(new EJBLibraryFactory());
      var0.addDefaultLibraryFactory(new JarLibraryFactory());
   }

   public static void addLibraryUsage(Getopt2 var0) {
      String var1 = "@";
      String var2 = "name";
      String var3 = "libspecver";
      String var4 = "libimplver";
      var0.addOption("library", "file", "Comma-separated list of libraries. Each library may optionally set its name and versions, if not already set in its manifest, using the following syntax: <file>[" + var1 + var2 + "=<string>" + var1 + var3 + "=<version> " + var1 + var4 + "=<version|string>]");
      var0.addOption("librarydir", "dir", "Registers all files in specified directory as libraries.");
   }

   public static VirtualJarFile[] getLibraryVjarsWithDescriptor(ApplicationContextInternal var0, String var1, String var2) throws IOException {
      ArrayList var3 = new ArrayList();
      LibraryProvider var4 = var0.getLibraryProvider(var1);
      if (var4 != null) {
         Library[] var5 = var4.getReferencedLibraries();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            Library var7 = var5[var6];
            VirtualJarFile var8 = VirtualJarFactory.createVirtualJar(var7.getLocation());
            if (var8.getEntry(var2) != null) {
               var3.add(var8);
            } else {
               var8.close();
            }
         }
      }

      return (VirtualJarFile[])((VirtualJarFile[])var3.toArray(new VirtualJarFile[0]));
   }

   private static void populateContextOverrides(LibraryContext var0) throws LoggableLibraryProcessingException {
      ApplicationDescriptor var1 = var0.getApplicationDescriptor();

      try {
         if (var1 == null) {
            return;
         }

         WeblogicApplicationBean var2 = var1.getWeblogicApplicationDescriptor();
         if (var2 == null) {
            return;
         }

         LibraryContextRootOverrideBean[] var3 = var2.getLibraryContextRootOverrides();
         HashMap var4 = new HashMap();
         if (var3 != null) {
            if (isDebugOn()) {
               debug("Adding LibraryContextRootOverrides ...");
            }

            for(int var5 = 0; var5 < var3.length; ++var5) {
               if (isDebugOn()) {
                  debug("Adding LibraryContextRootOverrideBean with context-root '" + var3[var5].getContextRoot() + "' Override value: '" + var3[var5].getOverrideValue() + "' ");
               }

               if (var3[var5].getContextRoot() != null && var3[var5].getOverrideValue() != null) {
                  var4.put(var3[var5].getContextRoot(), var3[var5].getOverrideValue());
               }
            }
         }

         if (!var4.isEmpty()) {
            var0.setContextRootOverrideMap(var4);
         }
      } catch (XMLStreamException var6) {
         LibraryLoggingUtils.errorMerging(var6);
      } catch (IOException var7) {
         LibraryLoggingUtils.errorMerging(var7);
      }

   }

   private static void overrideContextRoot(ApplicationBean var0, Map var1) {
      if (var1 != null && !var1.isEmpty()) {
         ModuleBean[] var2 = var0.getModules();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getWeb() != null) {
               String var4 = var2[var3].getWeb().getContextRoot();
               if (var4 != null && var1.get(var4) != null) {
                  String var5 = (String)var1.get(var4);
                  if (isDebugOn()) {
                     debug("Overriding context-root '" + var2[var3].getWeb().getContextRoot() + "' with value '" + var5 + "' from descriptor ");
                  }

                  var2[var3].getWeb().setContextRoot(var5);
                  if (isDebugOn()) {
                     debug("Context root from Descriptor '" + var2[var3].getWeb().getContextRoot() + "'");
                  }
               }
            }
         }

      }
   }
}
