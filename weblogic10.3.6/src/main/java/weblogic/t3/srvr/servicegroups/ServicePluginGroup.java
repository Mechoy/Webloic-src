package weblogic.t3.srvr.servicegroups;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.server.ServerLogger;
import weblogic.server.ServiceActivator;
import weblogic.server.ServiceFailureException;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceGroup;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class ServicePluginGroup extends ServiceGroup {
   private static boolean servicePluginsAvailable = true;
   private static final DebugCategory debugSLC = Debug.getCategory("weblogic.slc");
   private static final DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugServerLifeCycle");
   private List<String> availablePlugins = new ArrayList();

   public ServicePluginGroup(boolean var1) throws ServiceFailureException {
      super(var1);
      if (!var1) {
         if (isDebugEnabled()) {
            debugLogger.debug("ServicePluginGroup is not configured. No attempt will be made to look for service plugins.");
         }
      } else {
         File var2 = new File(new File(DomainDir.getRootDir()), "lib");
         if (isDebugEnabled()) {
            debugLogger.debug("Looking in " + var2.toString() + " for service plugins.");
         }

         if (var2.exists()) {
            File[] var3 = var2.listFiles(new FilenameFilter() {
               public boolean accept(File var1, String var2) {
                  return var2.endsWith(".jar");
               }
            });
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  this.processPossibleServicePlugin(var3[var4]);
               }
            }
         }
      }

   }

   public boolean isPluginAvailable(String var1) {
      return this.isAvailable() && this.availablePlugins.contains(var1);
   }

   public boolean isAvailable() {
      return servicePluginsAvailable;
   }

   public void processPossibleServicePlugin(File var1) throws ServiceFailureException {
      JarFile var2 = null;
      Manifest var3 = null;
      String var6 = null;
      String var7 = null;
      String var8 = null;
      String var9 = null;

      try {
         var2 = new JarFile(var1);
      } catch (IOException var19) {
         ServerLogger.logServicePluginException(var1.getPath(), var19);
         return;
      }

      if (isDebugEnabled()) {
         debugLogger.debug("Processing possible service plugin: " + var1.toString());
      }

      try {
         var3 = var2.getManifest();
      } catch (IOException var18) {
         ServerLogger.logServicePluginManifestException(var1.getPath(), var18);
      }

      if (var3 != null) {
         Attributes var10 = var3.getMainAttributes();
         if (var10 != null) {
            var6 = var10.getValue("Bundle-Activator");
            var8 = var10.getValue("Import-Service");
            var7 = var10.getValue("Export-Service");
            var9 = var10.getValue("Bundle-SymbolicName");
         }
      }

      if (var6 != null) {
         ClassLoader var23 = Thread.currentThread().getContextClassLoader();
         if (var23 == null) {
            var23 = ClassLoader.getSystemClassLoader();
         }

         try {
            Class var11 = Class.forName(var6, false, var23);
            if (!ServiceActivator.class.isAssignableFrom(var11)) {
               if (isDebugEnabled()) {
                  debugLogger.debug("The Bundle-Activator for this service plugin does not extend ServiceActivator");
               }

               return;
            }

            if (isDebugEnabled()) {
               debugLogger.debug(var11.getName() + " is a ServiceActivator.");
            }

            Field var12 = var11.getDeclaredField("INSTANCE");
            ServiceActivator var5 = (ServiceActivator)((ServiceActivator)var12.get((Object)null));
            Service var4 = new Service(var5);
            this.addService(var4);
            if (var7.contains("weblogic.application.ServiceRegistrant")) {
               var4.addSuccessor(CoreServiceGroup.getDeploymentService());
            }

            if (var8 != null) {
               String[] var13 = var8.split(", ");

               for(int var14 = 0; var14 < var13.length; ++var14) {
                  Class var15 = Class.forName(var13[var14]);
                  Field var16 = var15.getDeclaredField("INSTANCE");
                  Service var17 = (Service)var16.get((Object)null);
                  var4.addDependency(var17);
               }
            }

            this.availablePlugins.add(var9);
            ServerLogger.logServicePluginAdded(var1.getPath(), var9);
         } catch (NoSuchFieldException var20) {
            throw new ServiceFailureException(ServerLogger.getServicePluginActivatorLoadException(var1.getPath(), var20));
         } catch (IllegalAccessException var21) {
            throw new ServiceFailureException(ServerLogger.getServicePluginActivatorLoadException(var1.getPath(), var21));
         } catch (ClassNotFoundException var22) {
            throw new ServiceFailureException(ServerLogger.getServicePluginActivatorLoadException(var1.getPath(), var22));
         }
      }

   }

   private static boolean isDebugEnabled() {
      return debugSLC.isEnabled() || debugLogger.isDebugEnabled();
   }
}
