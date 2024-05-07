package weblogic.application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.utils.Debug;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class ApplicationFileManagerImpl extends ApplicationFileManager implements SplitDirectoryConstants {
   private static final boolean debug = Debug.getCategory("weblogic.application.AFM").isEnabled();
   private static final boolean verbose = Debug.getCategory("weblogic.application.barf").isEnabled();
   private final File appFile;
   private final VirtualJarFactory factory;
   private boolean isSplitDirectory;
   private File splitSourceDir;
   private HashMap links;

   protected ApplicationFileManagerImpl(String var1) throws IOException {
      this(new File(var1));
   }

   protected ApplicationFileManagerImpl(File var1) throws IOException {
      this.factory = new VirtualJarFactory();
      this.links = new HashMap();
      if (debug) {
         Debug.say("created with : " + var1.getPath());
         if (verbose) {
            Debug.stackdump("\n");
         }
      }

      this.appFile = var1;
      this.handleSplitDirectory();
   }

   protected ApplicationFileManagerImpl(SplitDirectoryInfo var1) throws IOException {
      this.factory = new VirtualJarFactory();
      this.links = new HashMap();
      this.appFile = var1.getDestDir();
      this.splitSourceDir = var1.getSrcDir();
      this.isSplitDirectory = this.splitSourceDir != null;
      this.registerLinks(var1);
   }

   private void handleSplitDirectory() throws IOException {
      if (!this.appFile.isDirectory()) {
         if (debug) {
            Debug.say("is archive not split dir");
         }

      } else {
         File var1 = new File(this.appFile, ".beabuild.txt");
         if (!var1.exists()) {
            if (debug) {
               Debug.say("No split directory file so it must be a \"normal\" exploded archive");
            }

            this.isSplitDirectory = false;
            this.splitSourceDir = null;
         } else {
            this.isSplitDirectory = true;
            SplitDirectoryInfo var2 = new SplitDirectoryInfo(this.appFile, var1);
            this.splitSourceDir = var2.getSrcDir();
            if (debug) {
               Debug.say("Its a split directory app and source dir is : " + this.splitSourceDir);
            }

            this.registerLinks(var2);
         }
      }
   }

   public boolean isSplitDirectory() {
      return this.isSplitDirectory;
   }

   private void registerLinks(SplitDirectoryInfo var1) throws IOException {
      Map var2 = var1.getUriLinks();
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         List var5 = (List)var2.get(var4);
         this.registerLink(var4, (File[])((File[])var5.toArray(new File[var5.size()])));
      }

   }

   public void registerLink(String var1, String var2) throws IOException {
      File var3 = new File(var2);
      if (!var3.exists()) {
         throw new IOException("Cannot register link for uri '" + var1 + "'.  " + var2 + " does not exist.");
      } else {
         if (debug) {
            Debug.say("About to register " + var1 + " for location\n" + var2);
         }

         this.registerLink(var1, new File[]{var3});
      }
   }

   private void registerLink(String var1, File[] var2) throws IOException {
      synchronized(this) {
         this.links.put(var1, var2);
      }
   }

   public VirtualJarFile getVirtualJarFile() throws IOException {
      VirtualJarFactory var10000;
      if (this.isSplitDirectory) {
         if (debug) {
            Debug.say("getVirtualJarFile isSplitDirectory src=" + this.splitSourceDir.getPath() + " appFile=" + this.appFile.getPath());
         }

         var10000 = this.factory;
         return VirtualJarFactory.createVirtualJar(this.splitSourceDir, this.appFile);
      } else {
         if (debug) {
            Debug.say("getVirtualJarFile isNOTSplitDirectory src=" + this.appFile.getPath());
         }

         var10000 = this.factory;
         return VirtualJarFactory.createVirtualJar(this.appFile);
      }
   }

   public VirtualJarFile getVirtualJarFile(String var1) throws IOException {
      if (debug) {
         Debug.say("getVirtualJarFile isSplitDirectory=" + this.isSplitDirectory);
         if (verbose) {
            Debug.stackdump("\n");
         }
      }

      File[] var2 = (File[])((File[])this.links.get(var1));
      VirtualJarFactory var10000;
      if (var2 != null) {
         var10000 = this.factory;
         return VirtualJarFactory.createVirtualJar(var2);
      } else if (this.isSplitDirectory) {
         if (debug) {
            Debug.say("getVirtualJarFile creating SplitExplodedJarFile \nsrcdir is : " + this.splitSourceDir + "/" + var1 + "\noutdir is : " + this.appFile.getPath() + "/" + var1);
         }

         File var3 = new File(this.splitSourceDir, var1);
         File var4 = new File(this.appFile, var1);
         if (var3.exists() && !var3.isDirectory()) {
            var10000 = this.factory;
            return VirtualJarFactory.createVirtualJar(var3);
         } else if (var4.exists() && !var4.isDirectory()) {
            var10000 = this.factory;
            return VirtualJarFactory.createVirtualJar(var4);
         } else {
            var10000 = this.factory;
            return VirtualJarFactory.createVirtualJar(var3, var4);
         }
      } else if (this.appFile.isDirectory()) {
         if (debug) {
            Debug.say("getVirtualJarFile creating with exploded dir \nsrcdir is : " + this.appFile.getPath() + "/" + var1);
         }

         var10000 = this.factory;
         return VirtualJarFactory.createVirtualJar(new File(this.appFile, var1));
      } else {
         throw new AssertionError("You cannot get a sub-vjf on an archive");
      }
   }

   private String getFileCP(File var1, String var2, String var3) {
      return (new File(var1, var2 + File.separatorChar + var3)).getAbsolutePath();
   }

   public String getClasspath(String var1) {
      return this.getClasspath(var1, "");
   }

   public String getClasspath(String var1, String var2) {
      File[] var3 = (File[])((File[])this.links.get(var1));
      if (var3 == null) {
         return this.isSplitDirectory ? this.getFileCP(this.splitSourceDir, var1, var2) + File.pathSeparator + this.getFileCP(this.appFile, var1, var2) : this.getFileCP(this.appFile, var1, var2);
      } else {
         String var4 = "";
         StringBuffer var5 = new StringBuffer();

         for(int var6 = 0; var6 < var3.length; ++var6) {
            var5.append(var4);
            var5.append(var3[var6].getAbsolutePath());
            var4 = File.pathSeparator;
         }

         return var5.toString();
      }
   }

   public File getSourcePath() {
      return this.isSplitDirectory ? this.splitSourceDir : this.appFile;
   }

   public File getSourcePath(String var1) {
      if (debug) {
         Debug.say("getSourcePath uri " + var1 + " isSplitDirectory : " + this.isSplitDirectory);
      }

      File var2 = null;
      File[] var3 = (File[])((File[])this.links.get(var1));
      if (var3 != null) {
         return var3[0];
      } else {
         return this.isSplitDirectory && (var2 = new File(this.splitSourceDir, var1)).exists() ? var2 : new File(this.appFile, var1);
      }
   }

   public File getOutputPath() {
      return this.appFile;
   }

   public File getOutputPath(String var1) {
      File[] var2 = (File[])((File[])this.links.get(var1));
      return var2 != null ? var2[0] : new File(this.appFile, var1);
   }

   public String toString() {
      return super.toString() + " isSplitDirectory : " + (new Boolean(this.isSplitDirectory)).toString() + (this.appFile != null ? this.appFile.getPath() : "null");
   }
}
