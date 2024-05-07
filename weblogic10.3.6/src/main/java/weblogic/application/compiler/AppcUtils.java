package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.library.LibraryManager;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.IOUtils;
import weblogic.connector.external.RAComplianceChecker;
import weblogic.connector.external.RAComplianceException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.ejb.spi.EJBC;
import weblogic.ejb.spi.EJBCFactory;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.jdbc.module.JDBCDeploymentHelper;
import weblogic.jms.module.JMSParser;
import weblogic.logging.Loggable;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.servlet.jsp.JspcInvoker;
import weblogic.servlet.utils.WarUtils;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.utils.BadOptionException;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.FilteringClassLoader;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class AppcUtils {
   private static final boolean debug = false;
   public static final String WEBINF_CLASSES;

   private AppcUtils() {
   }

   public static void expandJarFileIntoDirectory(File var0, File var1) throws ToolFailureException {
      VirtualJarFile var2 = null;

      try {
         var2 = VirtualJarFactory.createVirtualJar(var0);
         expandJarFileIntoDirectory(var2, var1);
      } catch (IOException var8) {
         handleIOException(var8, var1);
      } finally {
         IOUtils.forceClose(var2);
      }

   }

   public static void expandJarFileIntoDirectory(VirtualJarFile var0, File var1) throws ToolFailureException {
      try {
         JarFileUtils.extract(var0, var1);
      } catch (IOException var3) {
         handleIOException(var3, var1);
      }

   }

   private static void handleIOException(IOException var0, File var1) throws ToolFailureException {
      throw new ToolFailureException(J2EELogger.logAppcErrorCopyingFilesLoggable(var1.getAbsolutePath(), var0.toString()).getMessage(), var0);
   }

   public static File makeOutputDir(String var0, File var1, boolean var2) throws ToolFailureException {
      File var3 = null;
      if (var1 != null) {
         var3 = new File(var1, var0);
      } else {
         var3 = new File(var0);
      }

      Loggable var4;
      if (!var3.exists()) {
         if (!var3.mkdirs()) {
            var4 = J2EELogger.logAppcCouldNotCreateDirectoryLoggable(var3.getAbsolutePath());
            throw new ToolFailureException(var4.getMessage());
         }
      } else {
         if (!var3.canWrite()) {
            var4 = J2EELogger.logAppcCanNotWriteToDirectoryLoggable(var3.getAbsolutePath());
            throw new ToolFailureException(var4.getMessage());
         }

         if (var2) {
            FileUtils.remove(var3, FileUtils.STAR);
         }
      }

      return var3;
   }

   public static void setDDs(ApplicationDescriptor var0, CompilerCtx var1) throws ToolFailureException {
      try {
         var1.setApplicationDescriptor(var0);
      } catch (IOException var3) {
         EarUtils.handleParsingError(var3, var1.getSourceName());
      } catch (XMLStreamException var4) {
         EarUtils.handleParsingError(var4, var1.getSourceName());
      }

   }

   public static void createOutputArchive(String var0, File var1) throws ToolFailureException {
      if (!var0.endsWith("xml")) {
         File var2 = new File(var0);
         File var3 = null;
         if (var2.exists()) {
            var3 = backupJar(var2);
         }

         try {
            JarFileUtils.createJarFileFromDirectory(var0, var1);
            if (var3 != null) {
               var3.delete();
            }

            FileUtils.remove(var1);
         } catch (Exception var6) {
            if (var2.exists()) {
               var2.delete();
            }

            Loggable var5;
            if (var3 != null && var3.exists()) {
               var3.renameTo(var2);
               var5 = J2EELogger.logAppcUnableToCreateOutputArchiveRestoreLoggable(var2.getAbsolutePath(), var6.toString());
               throw new ToolFailureException(var5.getMessage(), var6);
            } else {
               var5 = J2EELogger.logAppcUnableToCreateOutputArchiveLoggable(var2.getAbsolutePath(), var6.toString());
               throw new ToolFailureException(var5.getMessage(), var6);
            }
         }
      }
   }

   private static File backupJar(File var0) throws ToolFailureException {
      File var1 = new File(var0 + "SAVE");
      Loggable var2;
      if (var1.exists() && !var1.delete()) {
         var2 = J2EELogger.logAppcUnableToDeleteBackupArchiveLoggable(var1.getAbsolutePath());
         throw new ToolFailureException(var2.getMessage());
      } else {
         try {
            FileUtils.copy(var0, var1);
         } catch (IOException var4) {
            Loggable var3 = J2EELogger.logAppcUnableToCreateBackupArchiveLoggable(var1.getAbsolutePath(), var4.toString());
            throw new ToolFailureException(var3.getMessage(), var4);
         }

         if (!var0.delete()) {
            var2 = J2EELogger.logAppcUnableToDeleteArchiveLoggable(var0.getAbsolutePath());
            throw new ToolFailureException(var2.getMessage());
         } else {
            return var1;
         }
      }
   }

   public static GenericClassLoader getClassLoaderForApplication(ClassFinder var0, CompilerCtx var1, String var2) {
      GenericClassLoader var3 = getClassLoader(var0, new HashSet(), var1);
      var3.setAnnotation(new Annotation(var2));
      return var3;
   }

   public static GenericClassLoader getClassLoaderForModule(ClassFinder var0, CompilerCtx var1, String var2, String var3) {
      GenericClassLoader var4 = getClassLoader(var0, new HashSet(), var1);
      var4.setAnnotation(new Annotation(var2, var3));
      return var4;
   }

   private static GenericClassLoader getClassLoader(ClassFinder var0, Set var1, CompilerCtx var2) {
      FilteringClassLoader var3 = null;
      String var4 = var2.getClasspathArg();
      if (var4 != null) {
         var3 = new FilteringClassLoader(new GenericClassLoader(new ClasspathClassFinder2(var4, var1)));
      } else {
         var3 = new FilteringClassLoader(Thread.currentThread().getContextClassLoader());
      }

      GenericClassLoader var5 = new GenericClassLoader(var0, var3);

      try {
         var2.getOpts().setOption("classpath", var5.getClassPath());
         return var5;
      } catch (BadOptionException var7) {
         throw new AssertionError(var7);
      }
   }

   static void compileEJB(GenericClassLoader var0, VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, File var5, ModuleValidationInfo var6, Getopt2 var7) throws ErrorCollectionException {
      Getopt2 var8 = (Getopt2)var7.clone();

      try {
         var8.setOption("d", var5.getPath());
         if (var8.containsOption("k")) {
            var8.removeOption("k");
         }

         if (var8.containsOption("manifest")) {
            var8.removeOption("manifest");
         }

         if (var8.containsOption("ignorePlanValidation")) {
            var8.removeOption("ignorePlanValidation");
         }
      } catch (BadOptionException var10) {
         throw new AssertionError(var10);
      }

      EJBC var9 = EJBCFactory.createEJBC(var8);
      var9.compileEJB(var0, var1, var2, var3, var4, var6);
   }

   static void compileRAR(GenericClassLoader var0, VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, ModuleValidationInfo var5, CompilerCtx var6) throws RAComplianceException {
      RAComplianceChecker.factory.createChecker().validate(var0, var1, var2, var3, var4);
   }

   static void compileWAR(GenericClassLoader var0, VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, File var5, ModuleValidationInfo var6, CompilerCtx var7) throws ToolFailureException {
      Getopt2 var8 = var7.getOpts();
      LibraryManager var9 = WebAppLibraryUtils.getEmptyWebAppLibraryManager(var1.getName());
      var7.getApplicationContext().getLibraryManagerAggregate().addLibraryManager(var1.getName(), var9);

      try {
         if (var7.getPartialOutputTarget() != null) {
            var8.setOption("d", var7.getPartialOutputTarget());
         } else {
            var8.setOption("d", var5.getPath() + WEBINF_CLASSES);
         }
      } catch (BadOptionException var13) {
         throw new AssertionError(var13);
      }

      JspcInvoker var10 = new JspcInvoker(var8);

      try {
         var10.compile(var0, var1, var2, var3, var4, var6, var9);
      } catch (ErrorCollectionException var12) {
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(var1.getName(), var12.toString()).getMessage(), var12);
      }
   }

   public static File getNamedTempDir(String var0, boolean var1) {
      File var2 = new File(System.getProperty("java.io.tmpdir"), var0);
      if (var1) {
         FileUtils.remove(var2);
      }

      var2.mkdirs();
      return var2;
   }

   static void mergeWAR(VirtualJarFile var0, File var1, File var2, DeploymentPlanBean var3, File var4, String var5) throws ToolFailureException {
      WebAppDescriptor var6 = WarUtils.getWebAppDescriptor(var1, var0, var2, var3, var5);
      WeblogicWebAppBean var7 = WarUtils.getWlWebAppBean(var6);
      LibraryManager var8 = WebAppLibraryUtils.getWebAppLibraryManager(var7, var5);
      if (var8.hasReferencedLibraries()) {
         File var9 = getNamedTempDir("warmerge", true);

         try {
            WebAppLibraryUtils.copyWebAppLibraries(var8, var9, var4);
         } catch (IOException var11) {
            throw new ToolFailureException("for webapp with uri " + var5 + ", webapp library merge failed", var11);
         }

         WebAppLibraryUtils.removeLibraryReferences(var7);
      }

      writeWarDescriptors(var6, var4, var5);
   }

   public static void writeDescriptor(File var0, String var1, DescriptorBean var2) throws ToolFailureException {
      if (var2 != null) {
         EditableDescriptorManager var3 = new EditableDescriptorManager();
         File var4 = new File(var0, var1);

         try {
            DescriptorUtils.writeDescriptor(var3, var2, var4);
         } catch (IOException var6) {
            throw new ToolFailureException("Unable to write descriptor " + var1 + " to " + var4.getAbsolutePath(), var6);
         }
      }

   }

   static void writeWarDescriptors(WebAppDescriptor var0, File var1, String var2) throws ToolFailureException {
      try {
         var0.writeDescriptors(var1);
      } catch (IOException var4) {
         throw new ToolFailureException("Error processing web " + var2 + " deployment descriptors", var4);
      } catch (XMLStreamException var5) {
         throw new ToolFailureException("Error processing web " + var2 + " deployment descriptors", var5);
      }
   }

   static void compileJMS(File var0, DeploymentPlanBean var1, File var2, String var3, Getopt2 var4) throws ToolFailureException {
      try {
         var4.setOption("d", var2.getPath());
      } catch (BadOptionException var9) {
         throw new AssertionError(var9);
      }

      try {
         JMSBean var5 = JMSParser.createJMSDescriptor(var3);
      } catch (Exception var10) {
         String var6 = null;
         Throwable var7 = var10.getCause();
         if (var7 != null) {
            var6 = var7.getMessage();
         } else {
            var6 = var10.getMessage();
         }

         Exception var8 = new Exception("ERROR: Failed to compile JMS module " + var3 + ": " + var6);
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(var3, var8.toString()).getMessage(), var8);
      }
   }

   static void compileJDBC(File var0, DeploymentPlanBean var1, File var2, String var3, Getopt2 var4) throws ToolFailureException {
      try {
         var4.setOption("d", var2.getPath());
      } catch (BadOptionException var9) {
         throw new AssertionError(var9);
      }

      try {
         JDBCDeploymentHelper var5 = new JDBCDeploymentHelper();
         var5.createJDBCDataSourceDescriptor(var3);
      } catch (Exception var10) {
         String var6 = null;
         Throwable var7 = var10.getCause();
         if (var7 != null) {
            var6 = var7.getMessage();
         } else {
            var6 = var10.getMessage();
         }

         Exception var8 = new Exception("ERROR: Failed to compile JDBC module " + var3 + ": " + var6);
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(var3, var8.toString()).getMessage(), var8);
      }
   }

   static VirtualJarFile getVirtualJarFile(File var0) throws ToolFailureException {
      VirtualJarFile var1 = null;

      try {
         var1 = VirtualJarFactory.createVirtualJar(var0);
         return var1;
      } catch (IOException var3) {
         throw new ToolFailureException(J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(var0.getAbsolutePath(), var3.toString()).getMessage(), var3);
      }
   }

   static {
      WEBINF_CLASSES = File.separatorChar + "WEB-INF" + File.separatorChar + "classes";
   }
}
