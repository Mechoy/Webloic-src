package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.jar.Attributes;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.Type;
import weblogic.application.internal.library.BasicLibraryData;
import weblogic.application.internal.library.LibraryManagerAggregate;
import weblogic.application.internal.library.LibraryRegistrationException;
import weblogic.application.internal.library.LibraryRegistry;
import weblogic.application.library.ApplicationLibrary;
import weblogic.application.library.IllegalSpecVersionTypeException;
import weblogic.application.library.J2EELibraryReference;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryContext;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryFactory;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LibraryReferenceFactory;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.jars.VirtualJarFile;

public final class LibraryLoggingUtils {
   private static final boolean optPackEnabled = Boolean.getBoolean("weblogic.application.RequireOptionalPackages");
   private static final LibraryRegistry libraryRegistry = LibraryRegistry.getRegistry();

   public static void verifyLibraryReferences(LibraryManager var0) throws LoggableLibraryProcessingException {
      LibraryManagerAggregate var1 = new LibraryManagerAggregate();
      var1.addLibraryManager(var0);
      verifyLibraryReferences(var1, true);
   }

   public static void verifyLibraryReferences(LibraryManagerAggregate var0, boolean var1) throws LoggableLibraryProcessingException {
      verifyLibraryReferences(var0.getOptionalPackagesManager(), var0, var1);
   }

   public static void verifyLibraryReferences(LibraryManagerAggregate var0) throws LoggableLibraryProcessingException {
      verifyLibraryReferences(var0, true);
   }

   private static void verifyLibraryReferences(LibraryManager var0, LibraryManagerAggregate var1, boolean var2) throws LoggableLibraryProcessingException {
      if (var0 != null) {
         handleOptPackErrorLevel(var0);
      }

      if (var1.hasUnresolvedRefs()) {
         if (var2) {
            throw new LoggableLibraryProcessingException(J2EELogger.logUnresolvedLibraryReferencesLoggable(var1.getUnresolvedRefsError()));
         } else {
            throw new LoggableLibraryProcessingException(J2EELogger.logUnresolvedLibraryReferencesWarningLoggable(var1.getUnresolvedRefsError()));
         }
      }
   }

   private static void handleOptPackErrorLevel(LibraryManager var0) {
      if (!optPackEnabled && var0.hasUnresolvedReferences()) {
         J2EELogger.logUnresolvedOptionalPackages(var0.getUnresolvedReferencesAsString());
         var0.resetUnresolvedReferences();
      }

   }

   public static void checkNoContextRootSet(J2EELibraryReference var0, Type var1) {
      if (var1 != Type.WAR && var0.getContextRoot() != null) {
         J2EELogger.logContextPathSetForNonWarLibRef(var0.toString(), var1.toString());
      }

   }

   public static LibraryData initLibraryData(String var0, String var1, String var2, File var3) throws LoggableLibraryProcessingException {
      LibraryData var4 = null;

      try {
         var4 = LibraryData.newInstance(var0, var1, var2, var3);
         return var4;
      } catch (IOException var6) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryInitErrorLoggable(var3.getAbsolutePath(), var6.getMessage()), var6);
      } catch (IllegalSpecVersionTypeException var7) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryWithIllegalSpecVersionLoggable(var3.getAbsolutePath(), var7.getSpecVersion()), var7);
      }
   }

   public static LibraryData initLibraryData(File var0, Attributes var1) throws LoggableLibraryProcessingException {
      LibraryData var2 = null;

      try {
         var2 = LibraryData.initFromManifest(var0, var1);
         return var2;
      } catch (IllegalSpecVersionTypeException var4) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryWithIllegalSpecVersionLoggable(var0.getAbsolutePath(), var4.getSpecVersion()), var4);
      }
   }

   public static LibraryData initLibraryData(File var0) throws LoggableLibraryProcessingException {
      LibraryData var1 = null;

      try {
         var1 = LibraryData.initFromManifest(var0);
         return var1;
      } catch (IOException var3) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryInitErrorLoggable(var0.getAbsolutePath(), var3.getMessage()), var3);
      } catch (IllegalSpecVersionTypeException var4) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryWithIllegalSpecVersionLoggable(var0.getAbsolutePath(), var4.getSpecVersion()), var4);
      }
   }

   public static LibraryData initLibraryData(LibraryMBean var0, File var1) throws LoggableLibraryProcessingException {
      LibraryData var2 = null;

      try {
         var2 = LibraryData.initFromMBean(var0, var1);
         return var2;
      } catch (IOException var4) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryInitErrorLoggable(LibraryUtils.toString(var0), var4.getMessage()), var4);
      } catch (IllegalSpecVersionTypeException var5) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryWithIllegalMBeanSpecVersionLoggable(LibraryUtils.toString(var0), var5.getSpecVersion()), var5);
      }
   }

   public static J2EELibraryReference[] initLibRefs(LibraryRefBean[] var0) throws LoggableLibraryProcessingException {
      J2EELibraryReference[] var1 = null;

      try {
         var1 = LibraryReferenceFactory.getAppLibReference(var0);
         return var1;
      } catch (IllegalSpecVersionTypeException var3) {
         throw new LoggableLibraryProcessingException(J2EELogger.logIllegalAppLibSpecVersionRefLoggable(var3.getSpecVersion()), var3);
      }
   }

   public static void handleAppcLibraryInfoMismatch(BasicLibraryData var0, BasicLibraryData var1, File var2) throws LoggableLibraryProcessingException {
      Collection var3 = var0.verifyDataConsistency(var1);
      if (!var3.isEmpty()) {
         throw new LoggableLibraryProcessingException(J2EELogger.logAppcLibraryInfoMismatchLoggable(var2.getAbsolutePath(), var3.toString()));
      }
   }

   public static void handleLibraryInfoMismatch(BasicLibraryData var0, BasicLibraryData var1) throws LoggableLibraryProcessingException {
      Collection var2 = var0.verifyDataConsistency(var1);
      if (!var2.isEmpty()) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryInfoMismatchLoggable(var0.toString(), var2.toString()));
      }
   }

   public static LibraryDefinition getLibraryDefinition(LibraryData var0, File var1, Iterator var2) throws LoggableLibraryProcessingException {
      Object var3 = null;

      try {
         while(var2.hasNext()) {
            LibraryFactory var4 = (LibraryFactory)var2.next();
            LibraryDefinition var5 = var4.createLibrary(var0, var1);
            if (var5 != null) {
               return var5;
            }
         }
      } catch (LoggableLibraryProcessingException var6) {
         throw var6;
      } catch (LibraryProcessingException var7) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryInitErrorLoggable(var0.getLocation().getAbsolutePath(), StackTraceUtils.throwable2StackTrace(var7)), var7);
      }

      if (var3 == null) {
         throw new LoggableLibraryProcessingException(J2EELogger.logUnknownLibraryTypeLoggable(var0.getLocation().getAbsolutePath()));
      } else {
         return (LibraryDefinition)var3;
      }
   }

   public static void initLibraryDefinition(LibraryDefinition var0) throws LoggableLibraryProcessingException {
      try {
         var0.init();
      } catch (LoggableLibraryProcessingException var2) {
         throw var2;
      } catch (LibraryProcessingException var3) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryInitErrorLoggable(var0.toString(), StackTraceUtils.throwable2StackTrace(var3)), var3);
      }
   }

   public static void registerLibrary(LibraryDefinition var0, boolean var1) throws LoggableLibraryProcessingException {
      try {
         libraryRegistry.register(var0);
         if (var1) {
            J2EELogger.logRegisteredLibrary(var0.toString() + " (" + var0.getType() + ")");
         }

      } catch (LibraryRegistrationException var3) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryRegistrationErrorLoggable(var0.toString(), var3.getMessage()), var3);
      }
   }

   public static void cleanupLibrariesAndRemove() {
      warnCleanupLibraries(true, true);
   }

   private static void warnCleanupLibraries(boolean var0, boolean var1) {
      Collection var2 = libraryRegistry.getAll();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         LibraryDefinition var4 = (LibraryDefinition)var3.next();
         warnCleanupLibrary(var4, var1);
         if (var0) {
            libraryRegistry.remove(var4);
         }
      }

   }

   private static void warnCleanupLibrary(LibraryDefinition var0, boolean var1) {
      try {
         removeLibrary(var0);
      } catch (LoggableLibraryProcessingException var3) {
         if (!var1) {
            var3.getLoggable().log();
         }
      } catch (LibraryProcessingException var4) {
         if (!var1) {
            J2EELogger.logLibraryCleanupWarning(var0.toString(), StackTraceUtils.throwable2StackTrace(var4));
         }
      }

   }

   public static void partialCleanupAndRemove() {
      Collection var0 = libraryRegistry.getAll();
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         LibraryDefinition var2 = (LibraryDefinition)var1.next();
         cleanupOnly(var2, true);
         libraryRegistry.remove(var2);
      }

   }

   private static void cleanupOnly(LibraryDefinition var0, boolean var1) {
      try {
         var0.cleanup();
      } catch (LoggableLibraryProcessingException var3) {
         if (!var1) {
            var3.getLoggable().log();
         }
      } catch (LibraryProcessingException var4) {
         if (!var1) {
            J2EELogger.logLibraryCleanupWarning(var0.toString(), StackTraceUtils.throwable2StackTrace(var4));
         }
      }

   }

   public static void errorRemoveLibrary(LibraryDefinition var0) throws LoggableLibraryProcessingException {
      try {
         removeLibrary(var0);
      } catch (LoggableLibraryProcessingException var2) {
         throw var2;
      } catch (LibraryProcessingException var3) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryCleanupErrorLoggable(var0.toString(), StackTraceUtils.throwable2StackTrace(var3)), var3);
      }
   }

   private static void removeLibrary(LibraryDefinition var0) throws LibraryProcessingException {
      var0.cleanup();
      var0.remove();
   }

   public static void checkLibraryExists(File var0) throws LoggableLibraryProcessingException {
      if (!var0.exists()) {
         throw new LoggableLibraryProcessingException(J2EELogger.logCannotFindLibraryLoggable(var0.getAbsolutePath()));
      }
   }

   public static void checkLibdirIsValid(File var0) throws LoggableLibraryProcessingException {
      if (!var0.exists() || !var0.isDirectory()) {
         throw new LoggableLibraryProcessingException(J2EELogger.logCannotProcessLibdirLoggable(var0.getAbsolutePath()));
      }
   }

   public static void importLibrary(ApplicationLibrary var0, J2EELibraryReference var1, LibraryContext var2, boolean var3, MultiClassFinder var4) throws LoggableLibraryProcessingException {
      try {
         var0.importLibrary(var1, var2, var4);
         if (var3) {
            J2EELogger.logLibraryImport(var0.toString(), var2.getRefappName());
         }

      } catch (LoggableLibraryProcessingException var6) {
         throw var6;
      } catch (LibraryProcessingException var7) {
         throw new LoggableLibraryProcessingException(J2EELogger.logErrorImportingLibraryLoggable(var0.toString(), StackTraceUtils.throwable2StackTrace(var7)), var7);
      }
   }

   public static void mergeDescriptors(ApplicationDescriptor var0, VirtualJarFile var1) throws LoggableLibraryProcessingException {
      try {
         var0.mergeDescriptors(var1);
      } catch (IOException var3) {
         errorMerging(var3);
      } catch (XMLStreamException var4) {
         errorMerging(var4);
      }

   }

   public static void mergeDescriptors(ApplicationDescriptor var0, ApplicationDescriptor var1) throws LoggableLibraryProcessingException {
      try {
         var0.mergeDescriptors(var1);
      } catch (IOException var3) {
         errorMerging(var3);
      } catch (XMLStreamException var4) {
         errorMerging(var4);
      }

   }

   public static void updateDescriptor(ApplicationDescriptor var0, ApplicationBean var1) throws LoggableLibraryProcessingException {
      try {
         var0.updateApplicationDescriptor(var1);
      } catch (IOException var3) {
         errorMerging(var3);
      } catch (XMLStreamException var4) {
         errorMerging(var4);
      }

   }

   public static void errorMerging(Exception var0) throws LoggableLibraryProcessingException {
      throw new LoggableLibraryProcessingException(J2EELogger.logDescriptorMergeErrorLoggable(StackTraceUtils.throwable2StackTrace(var0)), var0);
   }

   public static void checkIsAppLibrary(Library var0) throws LoggableLibraryProcessingException {
      if (!(var0 instanceof ApplicationLibrary)) {
         throw new LoggableLibraryProcessingException(J2EELogger.logLibraryIsNotAppLibraryLoggable(var0.toString()));
      }
   }

   public static void warnMissingExtensionName(String var0, String var1) {
      J2EELogger.logCannotFindExtensionNameWarning(var0, var1);
   }

   public static BasicLibraryData initOptionalPackageRefLibData(String var0, String var1, String var2, String var3) {
      BasicLibraryData var4 = null;

      try {
         var4 = new BasicLibraryData(var0, var1, var2);
         return var4;
      } catch (IllegalSpecVersionTypeException var6) {
         J2EELogger.logIllegalOptPackSpecVersionRefWarning(var0, var1, var3);
         return null;
      }
   }

   public static String registryToString() {
      StringBuffer var0 = new StringBuffer();
      Iterator var1 = libraryRegistry.getAll().iterator();

      while(var1.hasNext()) {
         var0.append(var1.next().toString());
         if (var1.hasNext()) {
            var0.append(", ");
         }
      }

      return var0.toString();
   }
}
