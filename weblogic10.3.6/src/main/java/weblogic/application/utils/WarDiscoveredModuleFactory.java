package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.utils.application.WarDetector;
import weblogic.utils.jars.VirtualJarFile;

class WarDiscoveredModuleFactory extends DiscoveredModuleFactory {
   public final DiscoveredModule claim(File var1, String var2) {
      return WarDetector.instance.suffixed(var2) ? new WarDiscoveredModule(var2) : null;
   }

   public final DiscoveredModule claim(VirtualJarFile var1, ZipEntry var2, String var3) throws IOException {
      return WarDetector.instance.suffixed(var3) ? new WarDiscoveredModule(var3) : null;
   }

   static class WarDiscoveredModule implements DiscoveredModule {
      private final String relPath;

      public WarDiscoveredModule(String var1) {
         this.relPath = var1;
      }

      public void createModule(ApplicationBean var1) {
         ModuleBean var2 = var1.createModule();
         WebBean var3 = var2.createWeb();
         var3.setWebUri(this.relPath);
         var3.setContextRoot(this.relPath.substring(0, this.relPath.lastIndexOf(46)));
      }

      public String getURI() {
         return this.relPath;
      }
   }
}
