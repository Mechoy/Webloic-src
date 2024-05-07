package weblogic.servlet.internal;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import weblogic.application.io.ClasspathInfo;
import weblogic.application.io.DescriptorFinder;
import weblogic.application.io.ExplodedJar;
import weblogic.application.io.JarCopyFilter;
import weblogic.utils.FileUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.classloaders.ZipClassFinder;
import weblogic.utils.classloaders.ZipSource;
import weblogic.utils.collections.FilteringIterator;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.io.StreamUtils;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class ArchivedWar extends ExplodedJar {
   public static final String TMP_CLASSES_JAR = "_wl_cls_gen.jar";
   private static final String WEB_INF_LIB;
   private static final String MANIFEST_MF = "META-INF/MANIFEST.MF";
   private final boolean foundClasses;
   private final File classesJar;
   private final FileFilter jarFileFilter;

   public ArchivedWar(String var1, File var2, File var3, ClasspathInfo var4) throws IOException {
      super(var1, extractWarFile(var2, var3), new File[0], var4, JarCopyFilter.NOCOPY_FILTER);
      this.classesJar = new File(var2, WEB_INF_LIB + File.separator + "_wl_cls_gen.jar");
      this.foundClasses = this.classesJar.exists();
      this.jarFileFilter = new ExplodedJar.JarFileFilter(this.classesJar);
   }

   public ClassFinder getClassFinder() throws IOException {
      ClassFinder var1 = super.getClassFinder();
      if (this.foundClasses) {
         MultiClassFinder var2 = new MultiClassFinder(new ClasspathClassFinder2(this.classesJar.getAbsolutePath()));
         var2.addFinder(var1);
         var2.addFinder(new DescriptorFinder(this.getURI(), new WebInfClassesFinder(this.classesJar)));
         return var2;
      } else {
         return var1;
      }
   }

   protected ClassFinder buildDescriptorFinder() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.dirs.length; ++var2) {
         this.addClasspath(var1, this.dirs[var2]);
      }

      return new DescriptorFinder(this.uri, new CaseAwareClasspathClassFinder(var1.toString()));
   }

   protected FileFilter getJarFileFilter() {
      return this.jarFileFilter;
   }

   private static File extractWarFile(File var0, File var1) throws IOException {
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
         expandWarFileIntoDirectory(var2, var0);
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

   static void expandWarFileIntoDirectory(JarFile var0, File var1) throws IOException {
      Object var2 = null;

      try {
         var2 = VirtualJarFactory.createVirtualJar(var0);
         boolean var3 = extractClasses((VirtualJarFile)var2, var1);
         if (var3) {
            var2 = new NoClassVirtualJarFile((VirtualJarFile)var2);
         }

         JarFileUtils.extract((VirtualJarFile)var2, var1);
      } finally {
         if (var2 != null) {
            try {
               ((VirtualJarFile)var2).close();
            } catch (IOException var10) {
            }
         }

      }

   }

   private static boolean extractClasses(VirtualJarFile var0, File var1) throws IOException {
      boolean var2 = false;
      Iterator var3 = var0.getEntries("WEB-INF/classes/");
      int var4 = "WEB-INF/classes/".length();
      ZipOutputStream var5 = null;
      HashSet var6 = new HashSet();

      try {
         while(var3.hasNext()) {
            ZipEntry var7 = (ZipEntry)var3.next();
            String var8 = var7.getName();
            String var9 = var8.substring(var4);
            if (var9 != null && !var9.trim().equals("")) {
               if (var5 == null) {
                  var5 = initZipOutputStream(var1);
               }

               InputStream var10 = var0.getInputStream(var7);
               var2 = true;
               writeZipEntry(var9, var10, var5, var6);
            }
         }

         if (var5 != null && !var6.contains("META-INF/MANIFEST.MF")) {
            writeZipEntry("META-INF/MANIFEST.MF", createManifestStream(), var5, var6);
         }
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var17) {
            }
         }

      }

      return var2;
   }

   private static ZipOutputStream initZipOutputStream(File var0) throws IOException {
      File var1 = new File(var0, "WEB-INF" + File.separator + "lib");
      if (!var1.exists()) {
         var1.mkdirs();
      }

      File var2 = new File(var1, "_wl_cls_gen.jar");
      FileOutputStream var3 = new FileOutputStream(var2);
      return new ZipOutputStream(new BufferedOutputStream(var3));
   }

   private static void writeZipEntry(String var0, InputStream var1, ZipOutputStream var2, Set var3) throws IOException {
      if (var0.startsWith("/")) {
         var0 = var0.substring(1);
      }

      if (!var3.contains(var0) && !var0.toLowerCase().endsWith(".jsp") && !var0.toLowerCase().endsWith(".jspx")) {
         var2.putNextEntry(new ZipEntry(var0));
         var3.add(var0);
         StreamUtils.writeTo(var1, var2);
         var2.closeEntry();
      }

      var1.close();
      createSubEntries(var0, var2, var3);
   }

   private static void createSubEntries(String var0, ZipOutputStream var1, Set var2) throws IOException {
      if (var0.endsWith("/")) {
         var0 = var0.substring(0, var0.length() - 2);
      }

      while(true) {
         int var3 = var0.lastIndexOf("/");
         if (var3 == -1) {
            return;
         }

         var0 = var0.substring(0, var3 + 1);
         if (!var2.contains(var0)) {
            var1.putNextEntry(new ZipEntry(var0));
            var1.closeEntry();
            var2.add(var0);
         }

         var0 = var0.substring(0, var0.length() - 2);
      }
   }

   private static InputStream createManifestStream() throws IOException {
      Manifest var0 = new Manifest();
      Attributes var1 = var0.getMainAttributes();
      var1.put(Name.MANIFEST_VERSION, "1.0");
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      var0.write(var2);
      var2.close();
      return new ByteArrayInputStream(var2.toByteArray());
   }

   static {
      WEB_INF_LIB = "WEB-INF" + File.separator + "lib";
   }

   private static class WebInfClassesFinder extends ZipClassFinder {
      private static final String PREFIX = "WEB-INF/classes";

      private WebInfClassesFinder(File var1) throws IOException {
         super((ZipFile)(var1.getName().endsWith(".jar") ? new JarFile(var1) : new ZipFile(var1)));
      }

      public Source getSource(String var1) {
         if (var1 == null) {
            return null;
         } else {
            if (var1.startsWith("/")) {
               var1 = var1.substring(1);
            }

            if (!var1.startsWith("WEB-INF/classes")) {
               return null;
            } else {
               var1 = var1.substring("WEB-INF/classes".length());
               if (var1.startsWith("/")) {
                  var1 = var1.substring(1);
               }

               return (Source)(var1.equals("") ? new ZipSource(this.getZipFile(), new ZipEntry("")) : super.getSource(var1));
            }
         }
      }

      public Enumeration getSources(String var1) {
         return (Enumeration)(var1 == null ? new EmptyEnumerator() : super.getSources(var1));
      }

      public Source getClassSource(String var1) {
         if (var1 == null) {
            return null;
         } else {
            if (var1.startsWith("/")) {
               var1 = var1.substring(1);
            }

            if (!var1.startsWith("WEB-INF/classes")) {
               return null;
            } else {
               var1 = var1.substring("WEB-INF/classes".length());
               if (var1.startsWith("/")) {
                  var1 = var1.substring(1);
               }

               return super.getClassSource(var1);
            }
         }
      }

      // $FF: synthetic method
      WebInfClassesFinder(File var1, Object var2) throws IOException {
         this(var1);
      }
   }

   private static class NoClassVirtualJarFile implements VirtualJarFile {
      private static final String PREFIX = "WEB-INF/classes";
      private static final String JSP_SURFIX = ".jsp";
      private static final String JSPX_SURFIX = ".jspx";
      private VirtualJarFile vjf;

      NoClassVirtualJarFile(VirtualJarFile var1) {
         this.vjf = var1;
      }

      public String getName() {
         return this.vjf.getName();
      }

      public void close() throws IOException {
         this.vjf.close();
      }

      public Iterator entries() {
         return new FilteringIterator(this.vjf.entries()) {
            public boolean accept(Object var1) {
               ZipEntry var2 = (ZipEntry)var1;
               String var3 = var2.getName();
               if (var3.endsWith("WEB-INF/classes/") && var2.isDirectory()) {
                  return true;
               } else {
                  return !var3.startsWith("WEB-INF/classes") || var3.toLowerCase().endsWith(".jsp") || var3.toLowerCase().endsWith(".jspx");
               }
            }
         };
      }

      public URL getResource(String var1) {
         return this.vjf.getResource(var1);
      }

      public ZipEntry getEntry(String var1) {
         return this.vjf.getEntry(var1);
      }

      public Iterator getEntries(String var1) throws IOException {
         return this.vjf.getEntries(var1);
      }

      public InputStream getInputStream(ZipEntry var1) throws IOException {
         return this.vjf.getInputStream(var1);
      }

      public Manifest getManifest() throws IOException {
         return this.vjf.getManifest();
      }

      public File[] getRootFiles() {
         return this.vjf.getRootFiles();
      }

      public boolean isDirectory() {
         return this.vjf.isDirectory();
      }

      public JarFile getJarFile() {
         return this.vjf.getJarFile();
      }

      public File getDirectory() {
         return this.vjf.getDirectory();
      }
   }
}
