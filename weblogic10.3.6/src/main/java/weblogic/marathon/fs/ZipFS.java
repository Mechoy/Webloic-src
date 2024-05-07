package weblogic.marathon.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import weblogic.utils.StringUtils;
import weblogic.utils.jars.JarFileUtils;

public class ZipFS extends FS {
   ZipFile zf;
   ZipRootEntry zre;
   private int myTmpNum;
   private File jarFile;
   private boolean isInternal;
   boolean inSave = false;
   static int tmpcnt;

   protected void finalize() throws Throwable {
      this.close();
   }

   private ZipFS(File var1, FS var2, String var3, int var4) throws IOException {
      super(var1, var2, var3);
      this.jarFile = var1;
      this.isInternal = true;
      this.myTmpNum = var4;
      this.init();
   }

   public ZipFS(File var1, FS var2, String var3) throws IOException {
      super(var1, var2, var3);
      this.updateTmpNum();
      this.isInternal = false;
      this.copyFile(var1, this.jarFile);
      this.init();
   }

   private void updateTmpNum() {
      this.myTmpNum = nextTmpNum();
      File var1 = new File(marathonTmpDir, this.myTmpNum + ".jar");
      this.jarFile = var1;
      if (this.getParent() != null) {
         this.setRoot(this.jarFile);
      }

   }

   private void init() throws IOException {
      long var1 = System.currentTimeMillis();
      this.zf = new ZipFile(this.jarFile);
      var1 = System.currentTimeMillis();
      ArrayList var3 = new ArrayList();
      Enumeration var4 = this.zf.entries();

      while(var4.hasMoreElements()) {
         ZipEntry var5 = (ZipEntry)var4.nextElement();
         var3.add(var5);
      }

      ZipEntry[] var8 = new ZipEntry[var3.size()];
      var3.toArray(var8);
      var1 = System.currentTimeMillis();
      this.populate(var8);
      long var6 = System.currentTimeMillis() - var1;
   }

   public URL getURL(String var1) throws IOException {
      if (!this.exists(var1)) {
         return null;
      } else {
         String var2 = this.getRoot().getAbsolutePath().replace(File.separatorChar, '/');
         if (!var1.startsWith("/")) {
            var1 = '/' + var1;
         }

         String var3 = var2 + '!' + var1;
         return new URL("zip", "", var3);
      }
   }

   public File getJarFile() {
      return this.jarFile;
   }

   private void populate(ZipEntry[] var1) throws IOException {
      int var2 = var1.length;
      boolean[] var3 = new boolean[var2];
      this.zre = new ZipRootEntry(this.zf);
      HashMap var4 = new HashMap();
      var4.put("", this.zre);

      for(int var5 = 0; var5 < var2; ++var5) {
         ZipEntry var6 = var1[var5];
         String var7 = var6.getName();
         EntryAddable var8 = getParent(var7, var4);
         Object var9 = null;
         boolean var10 = false;
         if (var7.endsWith("/")) {
            var9 = (Entry)getDir(var7, var4);
            var10 = true;
         } else {
            var9 = new JarEntry(var7, this.zf, var6);
         }

         if (!var10) {
            var8.addEntry((Entry)var9);
         }
      }

   }

   private static EntryAddable getDir(String var0, Map var1) {
      Object var2 = (EntryAddable)var1.get(var0);
      if (var2 == null) {
         var2 = new DirEntry(var0);
         var1.put(var0, var2);
         getParent(var0, var1).addEntry((Entry)var2);
      }

      return (EntryAddable)var2;
   }

   private static EntryAddable getParent(String var0, Map var1) {
      int var2 = var0.length();
      EntryAddable var3 = null;
      if (var2 <= 2) {
         var3 = getDir("", var1);
      } else {
         int var4 = var0.lastIndexOf(47, var2 - 2);
         String var5 = var0.substring(0, var4 + 1);
         var3 = getDir(var5, var1);
      }

      return var3;
   }

   public Entry getEntry(String var1) throws IOException {
      var1 = var1.replace(File.separatorChar, '/');
      var1 = trimLeadingSlash(var1);
      Entry var2 = this.findEntry(this.zre, var1, var1);
      if (var2 == null) {
         throw new FileNotFoundException(var1);
      } else {
         return var2;
      }
   }

   private static String trimLeadingSlash(String var0) {
      if (var0.length() > 1 && var0.charAt(0) == '/') {
         var0 = var0.substring(1);
      }

      return var0;
   }

   public boolean exists(String var1) {
      var1 = var1.replace(File.separatorChar, '/');
      var1 = trimLeadingSlash(var1);
      boolean var2 = this.findEntry(this.zre, var1, var1) != null;
      return var2;
   }

   private void p(String var1) {
      System.err.println("[ZipFS(" + this.getPath() + ")(tmp=" + this.myTmpNum + ")]: " + var1);
   }

   private Entry findEntry(Entry var1, String var2, String var3) {
      String var4 = null;
      if (var2.endsWith("/")) {
         var4 = var2.substring(0, var2.length() - 1);
      } else {
         var4 = var2 + "/";
      }

      String var5 = var3;
      String var6 = var3;
      boolean var7 = false;
      if (var3.indexOf(47) != -1) {
         String[] var8 = StringUtils.split(var3, '/');
         var5 = var8[0];
         var6 = var5 + "/";
         var3 = var8[1];
         var7 = true;
      }

      Entry[] var11 = var1.list();
      if (var11 == null) {
         return null;
      } else {
         int var9 = var11.length - 1;

         while(var9 >= 0) {
            if (!var2.equals(var11[var9].getPath()) && !var4.equals(var11[var9].getPath())) {
               String var10 = var11[var9].getName();
               if (!var5.equals(var10) && !var6.equals(var10)) {
                  --var9;
                  continue;
               }

               return var7 ? this.findEntry(var11[var9], var2, var3) : var11[var9];
            }

            return var11[var9];
         }

         return null;
      }
   }

   public FS mountNested(String var1) throws IOException {
      FS var2 = (FS)this.children.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Entry var3 = this.getEntry(var1);
         if (var3 instanceof DirEntry) {
            NestedZipFS var11 = new NestedZipFS(this.getRoot(), this, var1, var3);
            return var11;
         } else {
            int var4 = nextTmpNum();
            File var5 = new File(marathonTmpDir, var4 + ".jar");
            byte[] var6 = new byte[1024];
            InputStream var8 = var3.getInputStream();
            FileOutputStream var9 = new FileOutputStream(var5);

            int var7;
            while((var7 = var8.read(var6)) > 0) {
               var9.write(var6, 0, var7);
            }

            var8.close();
            var9.close();
            ZipFS var10 = new ZipFS(var5, this, var1, var4);
            this.children.put(var1, var10);
            return var10;
         }
      }
   }

   public void unmountNested(String var1) {
      this.children.remove(var1);
   }

   public void close() throws IOException {
      this.zf.close();
      this.jarFile.delete();
   }

   private void remount() throws IOException {
      try {
         this.zf.close();
      } catch (Exception var3) {
      }

      File var1 = this.jarFile;
      this.updateTmpNum();
      File var2 = this.jarFile;
      this.copyFile(var1, var2);
      var1.delete();
      this.init();
   }

   public void save() throws IOException {
      this.inSave = true;

      try {
         File var1 = this.getTmpDir();
         (new DelTree(var1)).run();
         var1.mkdirs();
         this.saveSelf_1();
         this.saveChildren();
         this.saveSelf_2();
      } finally {
         this.close();
         this.inSave = false;
      }

   }

   private File getTmpDir() {
      File var1 = new File(marathonTmpDir, "" + this.myTmpNum);
      var1.mkdirs();
      return var1;
   }

   private void saveChildren() throws IOException {
      if (this.children.size() != 0) {
         Enumeration var1 = this.children.keys();

         while(var1.hasMoreElements()) {
            String var2 = (String)var1.nextElement();
            FS var3 = (FS)this.children.get(var2);
            if (var3 instanceof ZipFS) {
               ZipFS var4 = (ZipFS)var3;
               var4.save();
               this.copyFile(var4.jarFile, new File(this.getTmpDir(), var2.replace('/', File.separatorChar)));
            } else {
               var3.save();
            }
         }

      }
   }

   private void saveSelf_1() throws IOException {
      File var1 = this.getTmpDir();
      var1.mkdirs();
      JarFileUtils.extract(this.jarFile, var1);
      this.close();
      this.saveMems(var1, this.zre);
   }

   private void saveSelf_2() throws IOException {
      JarFileUtils.createJarFileFromDirectory(this.jarFile.getAbsolutePath(), this.getTmpDir());
      (new DelTree(this.getTmpDir())).run();
      this.remount();
      if (!this.isInternal) {
         this.copyFile(this.jarFile, this.getRoot());
      }

   }

   private void saveMems(File var1, Entry var2) throws IOException {
      Entry[] var3 = var2.list();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] instanceof MemoryEntry) {
               MemoryEntry var5 = (MemoryEntry)var3[var4];
               File var6 = new File(var1, var5.getPath().replace('/', File.separatorChar));
               var6.getParentFile().mkdirs();
               FileOutputStream var7 = new FileOutputStream(var6);

               try {
                  var7.write(var5.data);
               } finally {
                  var7.close();
               }
            } else {
               this.saveMems(var1, var3[var4]);
            }
         }

      }
   }

   private static EntryAddable addable(Entry var0) {
      if (!(var0 instanceof EntryAddable)) {
         throw new IllegalArgumentException("not addable: " + var0.getClass().getName());
      } else {
         return (EntryAddable)var0;
      }
   }

   public void put(String var1, byte[] var2) throws IOException {
      Entry var3;
      MemoryEntry var4;
      if (this.exists(var1)) {
         var3 = this.getEntry(var1);
         if (var3 instanceof MemoryEntry) {
            var4 = (MemoryEntry)var3;
            var4.data = var2;
            return;
         }
      }

      var3 = this.getParent(var1);
      var4 = new MemoryEntry(var1, var2);
      addable(var3).addEntry(var4);
   }

   private Entry getParent(String var1) throws IOException {
      int var2 = var1.lastIndexOf(47);
      if (var2 == -1) {
         return this.zre;
      } else {
         String var3 = var1.substring(0, var2);
         Object var4 = null;
         if (this.exists(var3)) {
            var4 = this.getEntry(var3);
         } else {
            Entry var5 = this.getParent(var3);
            addable(var5).addEntry((Entry)(var4 = new DirEntry(var3)));
         }

         return (Entry)var4;
      }
   }

   public Entry getRootEntry() throws IOException {
      return this.zre;
   }

   static synchronized int nextTmpNum() {
      int var0 = ++tmpcnt;
      return var0;
   }

   private void copyFile(File var1, File var2) throws IOException {
      FileOutputStream var3 = new FileOutputStream(var2);
      FileInputStream var4 = new FileInputStream(var1);
      byte[] var5 = new byte[1024];

      int var6;
      while((var6 = var4.read(var5)) > 0) {
         var3.write(var5, 0, var6);
      }

      var4.close();
      var3.close();
   }
}
