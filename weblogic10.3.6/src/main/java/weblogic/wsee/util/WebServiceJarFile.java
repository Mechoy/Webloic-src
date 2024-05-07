package weblogic.wsee.util;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import weblogic.application.ApplicationFileManager;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.JarFileObject;
import weblogic.utils.jars.VirtualJarFile;

public abstract class WebServiceJarFile {
   protected static final boolean debug = System.getProperty(DEBUG_PROPERTY()) != null;
   private File exploded;
   private VirtualJarFile vJarFile;
   protected ApplicationFileManager appFileManager;
   protected File dest;

   protected static String DEBUG_PROPERTY() {
      return "wsjar.debug";
   }

   public WebServiceJarFile(File var1, File var2) throws IOException {
      this.dest = var2;
      if (var2.exists()) {
         if (debug) {
            System.out.println("Extracting previous contents...");
         }

         if (var2.isDirectory()) {
            this.exploded = var2;
         } else {
            this.createExploded(var1, var2);
            JarFileObject var3 = new JarFileObject(var2);
            var3.extract(this.exploded);
         }
      } else {
         this.createExploded(var1, var2);
         if (var2.getParentFile() != null) {
            var2.getParentFile().mkdirs();
         }
      }

      this.appFileManager = ApplicationFileManager.newInstance(this.exploded);
      this.vJarFile = this.appFileManager.getVirtualJarFile();
      if (debug) {
         System.out.println("*** Creating " + this.toString());
      }

   }

   public void remove() throws IOException {
      if (debug) {
         System.out.println("*** Removing " + this.toString());
      }

      this.vJarFile.close();
      if (this.exploded != this.dest && this.exploded != null) {
         FileUtils.remove(this.exploded);
      }

   }

   public void save() throws IOException {
      if (this.dest != this.exploded) {
         JarFileObject var1 = JarFileObject.makeJar(this.dest.getCanonicalPath(), this.exploded);
         var1.save();
      }
   }

   public File getExploded() {
      return this.exploded;
   }

   public VirtualJarFile getVirtualJarFile() {
      return this.vJarFile;
   }

   public File getDestJar() {
      return this.dest;
   }

   public static String stripDriveLetter(String var0) {
      String var1 = var0.trim();
      return var0 != null && var0.length() > 1 && var1.charAt(1) == ':' ? var1.substring(2) : var1;
   }

   private void createExploded(File var1, File var2) throws IOException {
      Random var3 = new Random((long)System.identityHashCode(var2));

      do {
         this.exploded = new File(var1, var2.getName() + var3.nextInt());
      } while(this.exploded.exists());

      this.exploded.mkdirs();
   }

   public String toString() {
      return this.dest + ":" + this.exploded;
   }
}
