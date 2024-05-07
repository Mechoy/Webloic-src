package weblogic.application.ddconvert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import weblogic.application.ApplicationFileManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

final class ConvertCtx {
   private final ApplicationFileManager afm;
   private final File outputDir;
   private final boolean isVerbose;
   private final boolean isQuiet;
   private final EditableDescriptorManager edm = new EditableDescriptorManager();

   ConvertCtx(ApplicationFileManager var1, File var2, boolean var3, boolean var4) {
      this.afm = var1;
      this.outputDir = var2;
      this.isVerbose = var3;
      this.isQuiet = var4;
   }

   File getOutputDir() {
      return this.outputDir;
   }

   VirtualJarFile getAppVJF() throws IOException {
      return this.afm.getVirtualJarFile();
   }

   VirtualJarFile getModuleVJF(String var1) throws IOException {
      return this.afm.getVirtualJarFile(var1);
   }

   boolean isVerbose() {
      return this.isVerbose;
   }

   boolean isQuiet() {
      return this.isQuiet;
   }

   EditableDescriptorManager getDescriptorManager() {
      return this.edm;
   }

   GenericClassLoader newClassLoader(VirtualJarFile var1) {
      return new VJarResourceLoader(var1);
   }

   static void debug(String var0) {
      System.err.println("[DDConverter] " + var0);
   }

   private static class VJarResourceLoader extends GenericClassLoader {
      private final VirtualJarFile vjar;

      VJarResourceLoader(VirtualJarFile var1) {
         super((ClassLoader)null);
         this.vjar = var1;
      }

      public URL getResource(String var1) {
         return this.vjar.getResource(var1);
      }

      public Class loadClass(String var1) {
         throw new AssertionError("VJarResourceLoader only loads resources");
      }
   }
}
