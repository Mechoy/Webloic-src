package weblogic.marathon.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.jar.JarFile;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.JarFileUtils;

public abstract class FS {
   static File marathonTmpDir;
   File root;
   FS parent;
   String path;
   protected Hashtable children = new Hashtable();

   private static void cleanTempArea() {
      String[] var0 = marathonTmpDir.list();
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            File var2 = new File(marathonTmpDir, var0[var1]);
            FileUtils.remove(var2);
         }

      }
   }

   private static void p(String var0) {
      System.err.println("[FS]: " + var0);
   }

   protected FS(File var1, FS var2, String var3) {
      this.root = var1;
      this.parent = var2;
      this.path = var3;
   }

   public abstract URL getURL(String var1) throws IOException;

   public abstract Entry getEntry(String var1) throws IOException;

   public abstract boolean exists(String var1);

   public FS getParent() {
      return this.parent;
   }

   public File getRoot() {
      return this.root;
   }

   protected void setRoot(File var1) {
      this.root = var1;
   }

   public String getPath() {
      return this.path;
   }

   public abstract FS mountNested(String var1) throws IOException;

   public void unmountNested(String var1) {
   }

   public abstract void put(String var1, byte[] var2) throws IOException;

   public abstract Entry getRootEntry() throws IOException;

   public abstract void save() throws IOException;

   public abstract void close() throws IOException;

   public static FS mount(File var0, boolean var1) throws IOException {
      return mountInteral(var0, (FS)null, var1);
   }

   public static FS mount(File var0) throws IOException {
      return mountInteral(var0, (FS)null, false);
   }

   private static boolean isSplitDir(File var0) {
      return (new File(var0, ".beabuild.txt")).exists();
   }

   protected static FS mountInteral(File var0, FS var1) throws IOException {
      return mountInteral(var0, var1, false);
   }

   protected static FS mountInteral(File var0, FS var1, boolean var2) throws IOException {
      String var3 = null;
      if (var1 != null) {
         String var4 = var1.getRoot().getAbsolutePath();
         String var5 = var0.getAbsolutePath();
         var3 = var5.substring(var4.length());
      } else {
         var3 = var0.getAbsolutePath();
      }

      if (var0.isDirectory()) {
         return (FS)(isSplitDir(var0) ? new SplitDirectoryFS(var0, var1, var3) : new StdFS(var0, var1, var3));
      } else if (isZip(var0)) {
         if (var0.getPath().endsWith(".ear") && var2) {
            File var6 = explodeEar(var0);
            return new StdFS(var6, (FS)null, var6.getAbsolutePath());
         } else {
            return new ZipFS(var0, var1, var3);
         }
      } else {
         throw new IOException(var0.getAbsolutePath() + " is neither " + "directory nor zip file");
      }
   }

   private static File explodeEar(File var0) throws IOException {
      File var1 = FileUtils.createTempDir("ear", marathonTmpDir);
      JarFile var2 = new JarFile(var0);
      JarFileUtils.extract(var2, var1);
      return var1;
   }

   public static boolean isZip(File var0) throws IOException {
      FileInputStream var1 = new FileInputStream(var0);

      boolean var3;
      try {
         byte[] var2 = new byte[4];
         if (var1.read(var2) == 4) {
            var3 = var2[0] == 80 && var2[1] == 75 && var2[2] == 3 && var2[3] == 4;
            return var3;
         }

         var3 = false;
      } finally {
         var1.close();
      }

      return var3;
   }

   public static void main(String[] var0) throws Exception {
      // $FF: Couldn't be decompiled
   }

   private static void test1() throws Exception {
      File var0 = null;
      var0 = new File("c:/weblogic/dev/sandbox/brown/broken/ear/broken.jar");
      if (!var0.exists()) {
         p(var0.getAbsolutePath() + " doesn't exist!");
      }

      FS var1 = mount(var0);
      var1.getEntry("META-INF/");
      recurse(var1.getRootEntry());
   }

   private static void test2() throws Exception {
      File var0 = null;
      var0 = new File("c:/weblogic/dev/sandbox/brown/broken/broken.jar");
      FS var1 = mount(var0);
      System.err.println("META-INF/ejb-jar.xml:");
      Entry var2 = var1.getEntry("META-INF/ejb-jar.xml");
      InputStream var3 = var2.getInputStream();

      int var4;
      while((var4 = var3.read()) != -1) {
         System.err.write(var4);
      }

      var3.close();
      System.err.flush();
   }

   private static void test3() throws Exception {
      File var0 = new File("c:/weblogic/dev/sandbox/brown/broken/broken.jar");
      FS var1 = mount(var0);
      String var2 = "this is some random data\n";
      var1.put("META-INF/subdir/blah.txt", var2.getBytes());
      Entry var3 = var1.getEntry("META-INF/subdir/blah.txt");
      InputStream var4 = var3.getInputStream();

      int var5;
      while((var5 = var4.read()) != -1) {
         System.err.write(var5);
      }

      var4.close();
      System.err.flush();
      var1.save();
      var3 = var1.getEntry("META-INF/subdir/blah.txt");
      var4 = var3.getInputStream();

      while((var5 = var4.read()) != -1) {
         System.err.write(var5);
      }

      var4.close();
      System.err.flush();
      var1.save();
   }

   private static void test4() throws Exception {
      File var0 = new File("c:/weblogic/dev/sandbox/brown/broken/broken.ear");
      FS var1 = mount(var0);
      FS var2 = var1.mountNested("web.war");
      recurse(var2.getRootEntry());
   }

   private static void test5() throws Exception {
      File var0 = new File("c:/weblogic/dev/sandbox/brown/broken/broken.ear");
      FS var1 = mount(var0);
      FS var2 = var1.mountNested("web.war");
      Entry var3 = var2.getEntry("WEB-INF/weblogic.xml");
      InputStream var4 = var3.getInputStream();

      int var5;
      while((var5 = var4.read()) != -1) {
         System.err.write(var5);
      }

      var4.close();
      System.err.flush();
   }

   private static void test6() throws Exception {
      File var0 = new File("c:/weblogic/dev/sandbox/brown/broken/broken.ear");
      FS var1 = mount(var0);
      FS var2 = var1.mountNested("web.war");
      var2.put("WEB-INF/sub/gumby.txt", "Gumby and Pokey sittin' in a tree...".getBytes());
      Entry var3 = var2.getEntry("WEB-INF/sub/gumby.txt");
      System.err.println("before save, type=" + var3.getClass().getName());
      var1.save();
      var3 = var2.getEntry("WEB-INF/sub/gumby.txt");
      System.err.println("after save, type=" + var3.getClass().getName());
      InputStream var4 = var3.getInputStream();

      int var5;
      while((var5 = var4.read()) != -1) {
         System.err.write(var5);
      }

      var4.close();
      System.err.flush();
   }

   private static void test7() throws Exception {
      File var0 = new File("c:/weblogic/dev/sandbox/brown/broken/broken.ear");
      FS var1 = mount(var0);
      FS var2 = var1.mountNested("web.war");
      FS var3 = var1.mountNested("broken.jar");
      var1.save();
   }

   static void recurse(Entry var0) throws Exception {
      Entry[] var1 = var0.list();
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            System.err.println(var1[var2].getPath());
            recurse(var1[var2]);
         }

      }
   }

   static {
      String var0 = System.getProperty("marathon.tmpdir");
      if (var0 == null) {
         var0 = System.getProperty("java.io.tempdir");
         if (var0 == null) {
            var0 = System.getProperty("java.io.tmpdir");
         }

         String var1;
         if (var0 == null) {
            var1 = System.getProperty("os.name");
            if (var1 != null && var1.toLowerCase().indexOf("windows") != -1) {
               var0 = "C:\\TEMP";
            } else {
               var0 = "/tmp";
            }
         }

         var1 = System.getProperty("user.name");
         if (var1 != null) {
            var0 = (new File(var0, var1)).getPath();
         }

         marathonTmpDir = new File(var0);
         marathonTmpDir = new File(marathonTmpDir, "_wl_mara_tmp");
         marathonTmpDir.mkdirs();
      } else {
         marathonTmpDir = new File(var0);
         marathonTmpDir.mkdirs();
      }

      cleanTempArea();
   }
}
