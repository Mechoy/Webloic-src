package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import weblogic.ejb.spi.EJBJarUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

class EJBDiscoveredModuleFactory extends DiscoveredModuleFactory {
   public DiscoveredModule claim(File var1, String var2) {
      if (var1.getName().endsWith(".jar")) {
         VirtualJarFile var3 = null;

         EjbDiscoveredModule var4;
         try {
            var3 = VirtualJarFactory.createVirtualJar(var1);
            if (var3.getEntry("META-INF/ejb-jar.xml") == null && (var1.isDirectory() || !EJBJarUtils.ejbAnnotationDetector.isAnnotated((ZipFile)var3.getJarFile())) && (!var1.isDirectory() || !EJBJarUtils.ejbAnnotationDetector.isAnnotated(var3.getDirectory()))) {
               return null;
            }

            var4 = new EjbDiscoveredModule(var2);
         } catch (IOException var9) {
            return null;
         } finally {
            IOUtils.forceClose(var3);
         }

         return var4;
      } else {
         return null;
      }
   }

   public DiscoveredModule claim(VirtualJarFile var1, ZipEntry var2, String var3) throws IOException {
      if (var2.isDirectory()) {
         String var27 = var2.getName();
         if (!var27.endsWith("/")) {
            var27 = var27 + "/";
         }

         var27 = var27 + "META-INF/ejb-jar.xml";
         if (var1.getEntry(var27) != null) {
            return new EjbDiscoveredModule(var3);
         } else {
            Iterator var29 = var1.getEntries(var2.getName());
            return EJBJarUtils.ejbAnnotationDetector.isAnnotated(var1, var29) ? new EjbDiscoveredModule(var3) : null;
         }
      } else {
         ZipInputStream var4 = null;

         try {
            var4 = new ZipInputStream(var1.getInputStream(var2));

            ZipEntry var5;
            try {
               var5 = null;

               while((var5 = var4.getNextEntry()) != null) {
                  if (var5.getName().equals("META-INF/ejb-jar.xml")) {
                     EjbDiscoveredModule var6 = new EjbDiscoveredModule(var3);
                     return var6;
                  }
               }
            } catch (IOException var24) {
            } finally {
               if (var4 != null) {
                  var4.close();
               }

               var4 = null;
            }

            var4 = new ZipInputStream(var1.getInputStream(var2));
            if (EJBJarUtils.ejbAnnotationDetector.isAnnotated(var4)) {
               EjbDiscoveredModule var28 = new EjbDiscoveredModule(var3);
               return var28;
            } else {
               var5 = null;
               return var5;
            }
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
                  var4 = null;
               } catch (IOException var23) {
               }
            }

         }
      }
   }

   static class EjbDiscoveredModule implements DiscoveredModule {
      private final String relPath;

      public EjbDiscoveredModule(String var1) {
         this.relPath = var1;
      }

      public void createModule(ApplicationBean var1) {
         ModuleBean var2 = var1.createModule();
         var2.setEjb(this.relPath);
      }

      public String getURI() {
         return this.relPath;
      }
   }
}
