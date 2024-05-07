package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import weblogic.utils.jars.VirtualJarFile;

public abstract class DiscoveredModuleFactory {
   private static final boolean debug = false;
   private static final DiscoveredModuleFactory[] factories = new DiscoveredModuleFactory[]{new WarDiscoveredModuleFactory(), new RarDiscoveredModuleFactory(), new JavaDiscoveredModuleFactory(), new EJBDiscoveredModuleFactory()};

   public abstract DiscoveredModule claim(File var1, String var2);

   public abstract DiscoveredModule claim(VirtualJarFile var1, ZipEntry var2, String var3) throws IOException;

   static DiscoveredModule makeDiscoveredModule(File var0, String var1) {
      for(int var2 = 0; var2 < factories.length; ++var2) {
         DiscoveredModuleFactory var3 = factories[var2];
         DiscoveredModule var4 = var3.claim(var0, var1);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   static DiscoveredModule makeDiscoveredModule(VirtualJarFile var0, ZipEntry var1, String var2) throws IOException {
      for(int var3 = 0; var3 < factories.length; ++var3) {
         DiscoveredModuleFactory var4 = factories[var3];
         DiscoveredModule var5 = var4.claim(var0, var1, var2);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
   }
}
