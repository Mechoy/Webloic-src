package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import weblogic.application.ModuleException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.DirectoryClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.EmptyEnumerator;

public final class AppFileOverrideUtils {
   private static final String OVERRIDE_SUBDIRECTORY = "AppFileOverrides";
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDeployment");

   private AppFileOverrideUtils() {
   }

   public static void addFinderIfRequired(AppDeploymentMBean var0, GenericClassLoader var1) throws ModuleException {
      if (var0 != null && var1 != null && var0.getLocalPlanDir() != null) {
         File var2 = new File(var0.getLocalPlanDir(), "AppFileOverrides");
         if (!var2.isDirectory()) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Application File Override is not enabled for " + var0.getApplicationName());
            }

         } else {
            try {
               String var3 = null;
               if (DEBUG.isDebugEnabled()) {
                  var3 = var0.getLocalPlanDir() + File.separator + "AppFileOverrides";
                  DEBUG.debug("Application File Overrides enabled for " + var0.getApplicationName() + ", overrides located at: " + var3);
               }

               var1.addClassFinderFirst(new AppFileOverrideFinder(new DirectoryClassFinder(var2), var3));
            } catch (IOException var4) {
               throw new ModuleException(var4);
            }
         }
      }
   }

   public static ClassFinder getFinderIfRequired(AppDeploymentMBean var0, String var1) throws ModuleException {
      if (var0 != null && var0.getLocalPlanDir() != null) {
         File var2;
         if (var1 == null) {
            var2 = new File(var0.getLocalPlanDir(), "AppFileOverrides");
         } else {
            var2 = new File(var0.getLocalPlanDir() + File.separator + "AppFileOverrides", var1);
         }

         if (!var2.isDirectory()) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Application File Override is not enabled for " + var0.getApplicationName() + (var1 == null ? "" : ", moduleURI: " + var1));
            }

            return null;
         } else {
            try {
               String var3 = null;
               if (DEBUG.isDebugEnabled()) {
                  var3 = var0.getLocalPlanDir() + File.separator + "AppFileOverrides" + (var1 == null ? "" : File.separator + var1);
                  DEBUG.debug("Application File Override finder returned for " + var0.getApplicationName() + ", overrides located at: " + var3);
               }

               return new AppFileOverrideFinder(new DirectoryClassFinder(var2), var3);
            } catch (IOException var4) {
               throw new ModuleException(var4);
            }
         }
      } else {
         return null;
      }
   }

   private static final class AppFileOverrideFinder implements ClassFinder {
      private ClassFinder delegate;
      private String debugDir;

      public AppFileOverrideFinder(ClassFinder var1, String var2) {
         this.delegate = var1;
         this.debugDir = var2;
      }

      public Source getSource(String var1) {
         if (var1.endsWith(".class")) {
            return null;
         } else {
            Source var2 = this.delegate.getSource(var1);
            if (AppFileOverrideUtils.DEBUG.isDebugEnabled()) {
               AppFileOverrideUtils.DEBUG.debug("getSource(" + var1 + ") found " + var2);
               if (this.debugDir != null) {
                  File var3 = new File(this.debugDir + File.separator + var1);
                  AppFileOverrideUtils.DEBUG.debug(" resource file: " + this.debugDir + File.separator + var1 + ", exists = " + var3.exists());
               }
            }

            return var2;
         }
      }

      public Enumeration getSources(String var1) {
         return var1.endsWith(".class") ? EmptyEnumerator.EMPTY : this.delegate.getSources(var1);
      }

      public Source getClassSource(String var1) {
         return null;
      }

      public String getClassPath() {
         return "";
      }

      public ClassFinder getManifestFinder() {
         return NullClassFinder.NULL_FINDER;
      }

      public Enumeration entries() {
         return EmptyEnumerator.EMPTY;
      }

      public void close() {
         this.delegate.close();
      }
   }
}
