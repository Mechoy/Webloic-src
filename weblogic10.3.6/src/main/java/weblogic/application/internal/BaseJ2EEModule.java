package weblogic.application.internal;

import java.io.File;
import java.io.IOException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.application.utils.EarUtils;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.utils.jars.VirtualJarFile;

public abstract class BaseJ2EEModule {
   private static final DebugLogger debugLogger;

   protected final ComponentMBean findComponentMBeanInternal(ApplicationContextInternal var1, String var2, Class var3) throws ModuleException {
      ComponentMBean var4 = ComponentMBeanHelper.findComponentMBeanByURI(var1.getApplicationMBean(), var2, var3);
      if (var4 != null) {
         return var4;
      } else {
         LibraryRuntimeMBean[] var5 = var1.getRuntime().getLibraryRuntimes();
         if (var5 != null) {
            for(int var6 = 0; var6 < var5.length; ++var6) {
               var4 = ComponentMBeanHelper.findComponentMBeanByURI(var5[var6].getComponents(), var2, var3);
               if (var4 != null) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   protected final ComponentMBean findComponentMBean(ApplicationContextInternal var1, String var2, Class var3) throws ModuleException {
      ComponentMBean var4 = this.findComponentMBeanInternal(var1, var2, var3);
      if (var4 != null) {
         return var4;
      } else {
         throw new ModuleException("No ComponentMBean was found in Application " + var1.getApplicationName() + " with the URI " + var2);
      }
   }

   protected File resolveAltDD(ApplicationContextInternal var1, String var2) throws ModuleException {
      ApplicationBean var3 = var1.getApplicationDD();
      if (var3 == null) {
         return null;
      } else {
         ModuleBean[] var4 = var3.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (EarUtils.reallyGetModuleURI(var4[var5]).equals(var2)) {
               String var6 = var4[var5].getAltDd();
               if (var6 == null) {
                  return null;
               }

               VirtualJarFile var7 = null;

               try {
                  var7 = var1.getApplicationFileManager().getVirtualJarFile();
                  File[] var8 = var7.getRootFiles();

                  for(int var9 = 0; var9 < var8.length; ++var9) {
                     File var10 = new File(var8[var9], var6);
                     if (var10.exists()) {
                        File var11 = var10;
                        return var11;
                     }
                  }
               } catch (IOException var21) {
                  throw new ModuleException(var21);
               } finally {
                  try {
                     if (var7 != null) {
                        var7.close();
                     }
                  } catch (IOException var20) {
                  }

               }

               throw new ModuleException("Unable to find the alt-dd for module " + var2 + " with the alt-dd " + var6);
            }
         }

         return null;
      }
   }

   public DescriptorBean[] getDescriptors() {
      return new DescriptorBean[0];
   }

   protected boolean acceptModuleUri(ApplicationContextInternal var1, String var2, String var3) {
      String var4 = var2 + "/";
      debug("module uri prefix: " + var4);
      debug("module uri: " + var3);
      return !var1.isEar() || var3.startsWith(var4);
   }

   protected String mangle(ApplicationContextInternal var1, String var2, String var3) {
      return var1.isEar() ? var2 + "/" + var3 : var3;
   }

   protected String unmangle(ApplicationContextInternal var1, String var2, String var3) {
      return var1.isEar() && var3.startsWith(var2) ? var3.substring(var2.length()) : var3;
   }

   private static void debug(String var0) {
      debugLogger.debug("[BaseJ2EEModule] " + var0);
   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }
}
