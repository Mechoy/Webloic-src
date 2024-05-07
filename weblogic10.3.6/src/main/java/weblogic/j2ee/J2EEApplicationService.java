package weblogic.j2ee;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.management.DomainDir;
import weblogic.management.deploy.DeployerRuntimeTextTextFormatter;
import weblogic.management.internal.DeploymentHandlerHome;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.jars.VirtualJarFile;

public final class J2EEApplicationService extends AbstractServerService {
   private static final List haltListeners = new ArrayList(1);
   private static final String TEMP_DIR_NAME = "tmp";
   private static J2EEApplicationService singleton;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static MultiClassFinder finder;
   private static File tempDir = null;
   private final RMCFactoryDeployer mailDeployer = new RMCFactoryDeployer();
   private static final String J2EE_TMP_DIR_NAME = "weblogic.j2ee.application.tmpDir";
   private static final String APPS_TMP_DIR = System.getProperty("weblogic.j2ee.application.tmpDir");
   private static ApplicationCache applicationCache;
   private static List listeners;

   public J2EEApplicationService() {
      singleton = this;
   }

   public static J2EEApplicationService getJ2EEApplicationService() {
      return singleton;
   }

   public void start() throws ServiceFailureException {
      DeploymentHandlerHome.addDeploymentHandler(this.mailDeployer);
      applicationCache = ApplicationCache.getApplicationCache();
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      Iterator var1 = haltListeners.iterator();

      while(var1.hasNext()) {
         ((HaltListener)var1.next()).halt();
      }

      DeploymentHandlerHome.removeDeploymentHandler(this.mailDeployer);
      applicationCache = null;
   }

   public static String getCurrentApplicationName() {
      return ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
   }

   public static ApplicationContext getCurrentApplicationContext() {
      return ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
   }

   public static File getTempDir() {
      if (tempDir != null) {
         return tempDir;
      } else {
         String var0;
         if (APPS_TMP_DIR != null) {
            var0 = APPS_TMP_DIR;
         } else {
            var0 = DomainDir.getRootDir();
         }

         File var1 = new File(var0 + File.separator + "servers" + File.separator + ManagementService.getPropertyService(kernelId).getServerName(), "tmp");
         tempDir = new File(var1.getAbsolutePath());
         String var2 = null;
         if (!tempDir.exists()) {
            if (!tempDir.mkdirs()) {
               var2 = "Create Failed for " + tempDir.toString();
            }

            writeReadmeFile();
         } else if (!tempDir.isDirectory()) {
            var2 = "Cannot create " + tempDir.toString() + ". Non directory file already exists with same name. Please remove it";
         }

         Debug.assertion(var2 == null, var2 + ". Server cannot deploy any webapps");
         return tempDir;
      }
   }

   private static void writeReadmeFile() {
      try {
         File var0 = new File(tempDir, "README.TXT");
         FileWriter var1 = new FileWriter(var0);
         var1.write(DeployerRuntimeTextTextFormatter.getInstance().readmeContent());
         var1.close();
      } catch (IOException var2) {
      }

   }

   public static synchronized void addApplicationLifecycleListener(ApplicationLifecycleListener var0) {
      ArrayList var1 = new ArrayList(listeners.size() + 1);
      var1.addAll(listeners);
      var1.add(var0);
      listeners = var1;
   }

   public static synchronized void removeApplicationLifecycleListener(ApplicationLifecycleListener var0) {
      ArrayList var1 = new ArrayList(listeners.size());
      var1.addAll(listeners);
      var1.remove(var0);
      listeners = var1;
   }

   public static List getApplicationLifecycleListeners() {
      return listeners;
   }

   private static void addManifestToClassLoader(GenericClassLoader var0, ClassFinder var1) {
      if (var1 != null) {
         var0.addClassFinder(var1);
      }
   }

   public static void addClassFinder(GenericClassLoader var0, ClassFinder var1) {
      var0.addClassFinder(var1);
      addManifestToClassLoader(var0, var1.getManifestFinder());
   }

   public static File getAltDDFile(String var0, VirtualJarFile var1) {
      File var2 = null;
      File[] var3 = var1.getRootFiles();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2 = new File(var3[var4], var0);
         if (var2.exists()) {
            return var2;
         }
      }

      return null;
   }

   public static void registerHaltListener(HaltListener var0) {
      haltListeners.add(var0);
   }

   static {
      listeners = Collections.EMPTY_LIST;
   }

   public interface HaltListener {
      void halt();
   }
}
