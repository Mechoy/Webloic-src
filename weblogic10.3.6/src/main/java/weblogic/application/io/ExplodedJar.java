package weblogic.application.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;
import weblogic.utils.FileUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.jars.JarFileUtils;

public class ExplodedJar extends Archive {
   private static final FileFilter JAR_FILTER = new JarFileFilter((File)null);
   private final File extractDir;
   private final ClasspathInfo info;
   private final JarCopyFilter jarCopyFilter;
   protected final File[] dirs;
   protected final String uri;
   protected static final String MARKER_FILE = ".beamarker.dat";

   public ExplodedJar(String var1, File var2, File var3, ClasspathInfo var4) throws IOException {
      this(var1, extractJarFile(var2, var3), new File[0], var4, JarCopyFilter.NOCOPY_FILTER);
   }

   public ExplodedJar(String var1, File var2, File[] var3, ClasspathInfo var4, JarCopyFilter var5) {
      this.extractDir = var2;
      this.info = var4;
      this.uri = var1;
      this.jarCopyFilter = var5;
      this.dirs = new File[var3.length + 1];
      this.dirs[0] = var2;
      System.arraycopy(var3, 0, this.dirs, 1, var3.length);
   }

   protected String getURI() {
      return this.uri;
   }

   public File[] getDirs() {
      return this.dirs;
   }

   public ClassFinder getClassFinder() throws IOException {
      MultiClassFinder var1 = new MultiClassFinder();
      var1.addFinder(this.buildClasspathFinder());
      var1.addFinder(this.buildDescriptorFinder());
      this.addManifestFinders(var1);
      return var1;
   }

   protected ClassFinder buildDescriptorFinder() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.dirs.length; ++var2) {
         this.addClasspath(var1, this.dirs[var2]);
      }

      return new DescriptorFinder(this.uri, new ClasspathClassFinder2(var1.toString()));
   }

   protected FileFilter getJarFileFilter() {
      return JAR_FILTER;
   }

   private void addManifestFinders(MultiClassFinder var1) throws IOException {
      for(int var2 = 0; var2 < this.dirs.length; ++var2) {
         var1.addFinder(new ManifestFinder.ExtensionListFinder(this.dirs[var2]));
      }

   }

   private ClassFinder buildClasspathFinder() throws IOException {
      StringBuffer var1 = new StringBuffer();
      String[] var2 = this.info.getClasspathURIs();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.addClasspathURI(var1, var2[var3]);
      }

      String[] var5 = this.info.getJarURIs();

      for(int var4 = 0; var4 < var5.length; ++var4) {
         this.addJarURI(var1, var5[var4]);
      }

      return new ClasspathClassFinder2(var1.toString());
   }

   private void addClasspathURI(StringBuffer var1, String var2) {
      for(int var3 = 0; var3 < this.dirs.length; ++var3) {
         this.addClasspath(var1, new File(this.dirs[var3], var2));
      }

   }

   private void addJarURI(StringBuffer var1, String var2) throws IOException {
      File var3 = new File(this.extractDir, var2);
      StringBuffer var4 = new StringBuffer();
      Set var5 = Collections.emptySet();
      Object var6 = var5;
      if (this.dirs.length > 1 && var3.exists() && var3.isDirectory()) {
         var6 = new HashSet(Arrays.asList(var3.listFiles(this.getJarFileFilter())));
      }

      for(int var7 = 1; var7 < this.dirs.length; ++var7) {
         this.addJars(var4, this.dirs[var7], var2, var3, (Set)var6);
      }

      Iterator var8 = ((Set)var6).iterator();

      while(var8.hasNext()) {
         FileUtils.remove((File)var8.next());
      }

      this.addJars(var1, this.dirs[0], var2, var3, var5);
      var1.append(File.pathSeparator).append(var4);
   }

   private void addJars(StringBuffer var1, File var2, String var3, File var4, Set<File> var5) throws IOException {
      File var6 = new File(var2, var3);
      if (var6.exists() && var6.isDirectory()) {
         File[] var7 = var6.listFiles(this.getJarFileFilter());
         if (var7 != null && var7.length != 0) {
            for(int var8 = 0; var8 < var7.length; ++var8) {
               this.addAndMaybeCopy(var1, var7[var8], var2, var4, var5);
            }

         }
      }
   }

   private void addAndMaybeCopy(StringBuffer var1, File var2, File var3, File var4, Set<File> var5) throws IOException {
      if (!this.jarCopyFilter.copyJars()) {
         this.addClasspath(var1, var2);
      } else {
         this.handleManifestReferences(var1, var2, var3);
         File var6 = new File(var4, var2.getName());
         if (var5.contains(var6)) {
            var5.remove(var6);
         }

         this.copyFile(var2, var6);
         this.addClasspath(var1, var6);
      }
   }

   private void copyFile(File var1, File var2) throws IOException {
      if (!var2.exists() || var1.lastModified() > var2.lastModified()) {
         FileUtils.copy(var1, var2);
         var2.setLastModified(var1.lastModified());
      }

   }

   private void handleManifestReferences(StringBuffer var1, File var2, File var3) throws IOException {
      File[] var4 = ManifestHelper.getExistingMFClassPathElements(var2);
      URI var5 = var3.toURI();

      for(int var6 = 0; var6 < var4.length; ++var6) {
         URI var7 = var4[var6].toURI();
         URI var8 = var5.relativize(var7);
         if (var8 != var7) {
            if (var4[var6].isFile()) {
               if (!var4[var6].getParentFile().equals(var2.getParentFile())) {
                  File var9 = new File(this.extractDir, var8.toString());
                  this.copyFile(var4[var6], var9);
               }
            } else if (var4[var6].isDirectory()) {
               this.addClasspath(var1, var4[var6]);
            }
         }
      }

   }

   protected void addClasspath(StringBuffer var1, File var2) {
      this.addClasspath(var1, var2.getAbsolutePath());
   }

   private void addClasspath(StringBuffer var1, String var2) {
      if (var1.length() > 0) {
         var1.append(File.pathSeparator);
      }

      var1.append(var2);
   }

   public void remove() {
      if (this.extractDir.exists()) {
         FileUtils.remove(this.extractDir);
      }

   }

   public void removeTopLevelTempDir() {
      File var1 = this.extractDir.getParentFile();
      if (var1.exists()) {
         FileUtils.remove(var1);
      }

   }

   protected static boolean extractionUpToDate(File var0, File var1) {
      File var2 = new File(var0, ".beamarker.dat");
      return var2.exists() && var2.lastModified() == var1.lastModified();
   }

   private static File extractJarFile(File var0, File var1) throws IOException {
      if (var0.exists()) {
         if (extractionUpToDate(var0, var1)) {
            return var0;
         }

         FileUtils.remove(var0);
      }

      var0.mkdirs();
      JarFile var2 = null;

      try {
         var2 = new JarFile(var1);
         JarFileUtils.extract(var2, var0);
      } finally {
         if (var2 != null) {
            var2.close();
         }

      }

      File var3 = new File(var0, ".beamarker.dat");
      FileOutputStream var4 = new FileOutputStream(var3);

      try {
         var4.write(0);
      } finally {
         var4.close();
      }

      var3.setLastModified(var1.lastModified());
      return var0;
   }

   protected static class JarFileFilter implements FileFilter {
      private File excludeFile;

      public JarFileFilter(File var1) {
         this.excludeFile = var1;
      }

      public boolean accept(File var1) {
         if (var1.isFile() && var1.getName().endsWith(".jar")) {
            return this.excludeFile == null || !this.excludeFile.equals(var1);
         } else {
            return false;
         }
      }
   }
}
