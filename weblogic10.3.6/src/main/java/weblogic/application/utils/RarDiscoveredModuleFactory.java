package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.utils.jars.VirtualJarFile;

class RarDiscoveredModuleFactory extends DiscoveredModuleFactory {
   public final DiscoveredModule claim(File var1, String var2) {
      return !var1.getName().endsWith(".rar") ? null : new RarDiscoveredModule(var2);
   }

   public final DiscoveredModule claim(VirtualJarFile var1, ZipEntry var2, String var3) throws IOException {
      return var3.endsWith(".rar") ? new RarDiscoveredModule(var3) : null;
   }

   private static class RarDiscoveredModule implements DiscoveredModule {
      private final String relPath;

      public RarDiscoveredModule(String var1) {
         this.relPath = var1;
      }

      public void createModule(ApplicationBean var1) {
         ModuleBean var2 = var1.createModule();
         var2.setConnector(this.relPath);
      }

      public String getURI() {
         return this.relPath;
      }
   }
}
