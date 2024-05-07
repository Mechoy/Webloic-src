package weblogic.j2ee;

import java.net.MalformedURLException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Map;
import java.util.WeakHashMap;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.URLClassFinder;

public final class ApplicationManager {
   private static final AppClassLoaderManager APPCLASSLOADER_MANAGER = AppClassLoaderManager.getAppClassLoaderManager();
   private static final DebugCategory classFinderDebugging = Debug.getCategory("weblogic.ClassFinder");
   private static Map networkLoaders = new WeakHashMap();
   private static String INSTALL_HELP_MSG = ":  This error could indicate that a component was deployed on a  cluster member but not other members of that cluster. Make sure that any component deployed on a server that is part of a cluster is also deployed on all other members of that cluster";

   private static ApplicationContextInternal getApplicationContext(String var0) {
      return ApplicationAccess.getApplicationAccess().getApplicationContext(var0);
   }

   public static ClassLoader getApplicationClassLoader(Annotation var0) {
      ApplicationContextInternal var1 = getApplicationContext(var0.getApplicationName());
      return var1 == null ? null : var1.getAppClassLoader();
   }

   public static Class loadClass(String var0, String var1) throws ClassNotFoundException {
      return loadClass(var0, var1, (String)null);
   }

   public static Class loadClass(String var0, String var1, boolean var2) throws ClassNotFoundException {
      return loadClass(var0, var1, (String)null, Thread.currentThread().getContextClassLoader(), var2);
   }

   public static Class loadClassWithNoDependencyClassLoader(String var0, String var1) throws ClassNotFoundException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      return !(var2 instanceof GenericClassLoader) && var1 != null ? APPCLASSLOADER_MANAGER.loadApplicationClass(new Annotation(var1), var0) : loadClass(var0, var1, (String)null, var2);
   }

   public static Class loadClass(String var0, String var1, String var2) throws ClassNotFoundException {
      ClassLoader var3 = Thread.currentThread().getContextClassLoader();
      return loadClass(var0, var1, var2, var3);
   }

   public static Class loadClass(String var0, String var1, String var2, ClassLoader var3) throws ClassNotFoundException {
      return loadClass(var0, var1, var2, var3, false);
   }

   public static Class loadClass(String var0, String var1, String var2, ClassLoader var3, boolean var4) throws ClassNotFoundException {
      String var5 = null;
      if (classFinderDebugging.isEnabled()) {
         log("loadClass: [" + var0 + "] [" + var1 + "] [" + var2 + "] [" + var3 + "]");
      }

      Class var6;
      try {
         var6 = Class.forName(var0, true, var3);
         if (classFinderDebugging.isEnabled()) {
            log("found " + var6 + " in current classloader: " + var3);
         }

         if (var6 != null) {
            return var6;
         }
      } catch (ClassNotFoundException var9) {
         var5 = var9.getMessage();
      }

      if (classFinderDebugging.isEnabled()) {
         log(var0 + " not found in current classloader [" + var3 + "], looking elsewhere");
      }

      if (var1 != null && var1.length() > 0) {
         Annotation var11 = new Annotation(var1);
         Class var7 = null;
         var7 = loadClassFromApplication(var0, var11, var3, var4);
         if (var7 != null) {
            return var7;
         }
      }

      try {
         var6 = DescriptorClassLoader.loadClass(var0);
         if (var6 != null) {
            return var6;
         }
      } catch (ClassNotFoundException var8) {
      }

      try {
         var6 = loadFromNetwork(var3, var0, var1, var2);
         if (var6 != null) {
            return var6;
         }

         if (var1 == null) {
            var6 = Class.forName(var0, true, ApplicationManager.class.getClassLoader());
            if (var6 != null) {
               return var6;
            }
         }
      } catch (ClassNotFoundException var10) {
         if (KernelStatus.isServer()) {
            var5 = var10.getMessage();
         }
      }

      if (classFinderDebugging.isEnabled()) {
         log("Could not find " + var0);
      }

      if (var5 != null) {
         throw new ClassNotFoundException(var5 + INSTALL_HELP_MSG);
      } else {
         throw new ClassNotFoundException(var0 + INSTALL_HELP_MSG);
      }
   }

   private static Class loadClassFromApplication(String var0, Annotation var1, ClassLoader var2, boolean var3) {
      String var4 = var1.getModuleName();
      String var5 = var1.getApplicationName();
      if (classFinderDebugging.isEnabled()) {
         log("loadClass: Looking for " + var0 + " in app containers");
      }

      if (KernelStatus.isServer()) {
         try {
            Class var6 = null;
            if (classFinderDebugging.isEnabled()) {
               log("Looking in new app list");
            }

            var6 = loadClassFromNewStyleApplication(var0, var1, var5, var4, var2, var3);
            if (var6 != null) {
               if (classFinderDebugging.isEnabled()) {
                  log("found " + var6);
               }

               return var6;
            }
         } catch (ClassNotFoundException var7) {
         }
      }

      return null;
   }

   private static Class loadClassFromNewStyleApplication(String var0, Annotation var1, String var2, String var3, ClassLoader var4, boolean var5) throws ClassNotFoundException {
      GenericClassLoader var6;
      if (var5) {
         var6 = APPCLASSLOADER_MANAGER.findOrCreateInterAppLoader(var1, var4);
      } else {
         var6 = APPCLASSLOADER_MANAGER.findOrCreateIntraAppLoader(var1, var4);
      }

      if (var6 != null) {
         return Class.forName(var0, true, var6);
      } else {
         throw new ClassNotFoundException("ClassLoader not found for " + var3);
      }
   }

   private static boolean isNetworkClassLoadingEnabled() {
      if (!KernelStatus.isServer()) {
         return true;
      } else {
         AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         ServerMBean var1 = ManagementService.getRuntimeAccess(var0).getServer();
         return var1 == null ? false : var1.isNetworkClassLoadingEnabled();
      }
   }

   private static Class loadFromNetwork(ClassLoader var0, String var1, String var2, String var3) throws ClassNotFoundException {
      if (var1.indexOf("java.lang.") > -1) {
         return null;
      } else if (!isNetworkClassLoadingEnabled()) {
         return null;
      } else {
         if (classFinderDebugging.isEnabled()) {
            log("loadClass: Going to network with annotation " + var2 + " and codebase " + var3 + " looking for " + var1);
         }

         if (var0 instanceof GenericClassLoader && var3 != null) {
            ClassFinder var10 = getNetworkFinder(var2, var3);
            GenericClassLoader var5 = (GenericClassLoader)var0;
            var5.addClassFinder(var10);
            return Class.forName(var1, true, var5);
         } else {
            GenericClassLoader var4 = getNetworkLoader(var0, var2);
            if (var4 == null && var3 != null) {
               try {
                  var4 = createNetworkLoader(var0, var2, var3);
                  return Class.forName(var1, true, var4);
               } catch (AccessControlException var9) {
                  try {
                     if (classFinderDebugging.isEnabled()) {
                        log("loadClass: Going to rmi classloader with codebase " + var3 + " looking for " + var1);
                     }

                     return RMIClassLoader.loadClass(var3, var1);
                  } catch (MalformedURLException var7) {
                     var7.printStackTrace();
                  } catch (ClassNotFoundException var8) {
                     var8.printStackTrace();
                  }

                  var9.printStackTrace();
                  return null;
               }
            } else {
               return Class.forName(var1, true, var4);
            }
         }
      }
   }

   private static ClassFinder getNetworkFinder(String var0, String var1) {
      String var2;
      if (var0 != null && var0.length() > 0) {
         var2 = var1 + var0 + "/";
      } else {
         var2 = var1;
      }

      return new URLClassFinder(var2);
   }

   private static synchronized GenericClassLoader getNetworkLoader(ClassLoader var0, String var1) {
      Map var2 = (Map)networkLoaders.get(var0);
      if (var2 == null) {
         networkLoaders.put(var0, new WeakHashMap());
         return null;
      } else {
         return (GenericClassLoader)var2.get(var1);
      }
   }

   private static synchronized GenericClassLoader createNetworkLoader(ClassLoader var0, String var1, String var2) {
      GenericClassLoader var3 = getNetworkLoader(var0, var1);
      if (var3 == null && var2 != null) {
         ClassFinder var4 = getNetworkFinder(var1, var2);
         var3 = new GenericClassLoader(var4, var0);
         var3.setAnnotation(new Annotation(var1));
         addNetworkLoader(var0, var1, var3);
      }

      return var3;
   }

   private static void addNetworkLoader(ClassLoader var0, String var1, GenericClassLoader var2) {
      Object var3 = (Map)networkLoaders.get(var0);
      if (var3 == null) {
         var3 = new WeakHashMap();
         networkLoaders.put(var0, var3);
      }

      ((Map)var3).put(var1, var2);
   }

   private static void log(String var0) {
      Debug.say(var0);
      J2EELogger.logDebug(var0);
   }
}
