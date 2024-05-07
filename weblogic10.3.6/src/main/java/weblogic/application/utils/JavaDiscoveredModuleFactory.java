package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.zip.ZipEntry;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

class JavaDiscoveredModuleFactory extends DiscoveredModuleFactory {
   public final DiscoveredModule claim(File var1, String var2) {
      if (!var1.getName().endsWith(".jar")) {
         return null;
      } else {
         VirtualJarFile var3 = null;

         JavaDiscoveredModule var4;
         try {
            var3 = VirtualJarFactory.createVirtualJar(var1);
            if ((var3.getManifest() == null || var3.getManifest().getMainAttributes().get(Name.MAIN_CLASS) == null) && var3.getEntry("META-INF/application-client.xml") == null) {
               return null;
            }

            var4 = new JavaDiscoveredModule(var2);
         } catch (IOException var9) {
            return null;
         } finally {
            IOUtils.forceClose(var3);
         }

         return var4;
      }
   }

   public final DiscoveredModule claim(VirtualJarFile var1, ZipEntry var2, String var3) throws IOException {
      ZipEntry var5;
      if (var2.isDirectory()) {
         var3 = var3.endsWith("/") ? var3 : var3 + "/";
         String var34 = var3 + "META-INF/MANIFEST.MF";
         var5 = var1.getEntry(var34);
         if (var5 != null) {
            InputStream var36 = null;

            try {
               var36 = var1.getInputStream(var5);
               Manifest var7 = new Manifest(var36);
               if (var7.getMainAttributes().get(Name.MAIN_CLASS) != null) {
                  JavaDiscoveredModule var8 = new JavaDiscoveredModule(var3);
                  return var8;
               }
            } finally {
               if (var36 != null) {
                  try {
                     var36.close();
                  } catch (IOException var30) {
                  }
               }

            }
         }

         if (var1.getEntry(var3 + "META-INF/application-client.xml") != null) {
            return new JavaDiscoveredModule(var3);
         } else {
            return null;
         }
      } else {
         JarInputStream var4 = null;

         try {
            var4 = new JarInputStream(var1.getInputStream(var2));
            if (var4.getManifest() != null && var4.getManifest().getMainAttributes().get(Name.MAIN_CLASS) != null) {
               JavaDiscoveredModule var35 = new JavaDiscoveredModule(var3);
               return var35;
            }

            var5 = null;

            while((var5 = var4.getNextEntry()) != null) {
               if (var5.getName().equals("META-INF/application-client.xml")) {
                  JavaDiscoveredModule var6 = new JavaDiscoveredModule(var3);
                  return var6;
               }
            }
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var31) {
               }
            }

         }

         return null;
      }
   }

   static class JavaDiscoveredModule implements DiscoveredModule {
      private final String relPath;

      public JavaDiscoveredModule(String var1) {
         this.relPath = var1;
      }

      public void createModule(ApplicationBean var1) {
         ModuleBean var2 = var1.createModule();
         var2.setJava(this.relPath);
      }

      public String getURI() {
         return this.relPath;
      }
   }
}
