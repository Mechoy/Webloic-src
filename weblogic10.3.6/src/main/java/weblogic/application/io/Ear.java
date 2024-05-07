package weblogic.application.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import weblogic.application.ApplicationConstants;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.utils.IOUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class Ear {
   private final ExplodedJar archive;
   private final ClassFinder classfinder;
   private final Map<String, List<File>> uriLinks;
   private final Map<String, VirtualJarFile> virtualJars;
   private final String uri;
   private static final ClasspathInfo earInfo = new ClasspathInfo() {
      private final String[] APP_INF_CLASSES;
      private final String[] LIB_DIRS;

      {
         this.APP_INF_CLASSES = new String[]{ApplicationConstants.APP_INF_CLASSES};
         this.LIB_DIRS = new String[]{ApplicationConstants.APP_INF_LIB};
      }

      public String[] getClasspathURIs() {
         return this.APP_INF_CLASSES;
      }

      public String[] getJarURIs() {
         return this.LIB_DIRS;
      }
   };

   public Ear(String var1, File var2, File var3) throws IOException {
      this.uriLinks = new ConcurrentHashMap();
      this.virtualJars = new HashMap();
      this.archive = new ExplodedJar(var1, var2, var3, earInfo);
      this.classfinder = this.archive.getClassFinder();
      this.uri = var1;
   }

   public Ear(String var1, File var2, File[] var3) throws IOException {
      this(var1, var2, var3, JarCopyFilter.DEFAULT_FILTER);
   }

   public Ear(String var1, File var2, File[] var3, JarCopyFilter var4) throws IOException {
      this.uriLinks = new ConcurrentHashMap();
      this.virtualJars = new HashMap();
      this.archive = new ExplodedJar(var1, var2, var3, earInfo, var4);
      this.classfinder = this.archive.getClassFinder();
      this.uri = var1;
   }

   public Ear(String var1, File var2, SplitDirectoryInfo var3) throws IOException {
      this(var1, var2, var3, JarCopyFilter.DEFAULT_FILTER);
   }

   public Ear(String var1, File var2, SplitDirectoryInfo var3, JarCopyFilter var4) throws IOException {
      this(var1, var2, var3.getRootDirectories(), var4);
      this.uriLinks.putAll(var3.getUriLinks());
      String var5 = var3.getExtraClasspath();
      if (!"".equals(var5)) {
         MultiClassFinder var6 = (MultiClassFinder)this.classfinder;
         ClasspathClassFinder2 var7 = new ClasspathClassFinder2(var5);
         var6.addFinder(var7);
         var6.addFinder(new DescriptorFinder(var1, var7));
      }

   }

   public String getURI() {
      return this.uri;
   }

   public File[] getModuleRoots(String var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      File[] var3 = this.archive.getDirs();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         File var5 = new File(var3[var4], var1);
         if (var5.exists()) {
            var2.add(var5);
         }
      }

      List var6 = (List)this.uriLinks.get(var1);
      if (var6 != null) {
         var2.addAll(var6);
      }

      return (File[])((File[])var2.toArray(new File[var2.size()]));
   }

   public InputStream findScopedModuleResource(String var1, String var2, String var3) throws IOException {
      VirtualJarFile var4 = null;
      synchronized(this.virtualJars) {
         if (this.virtualJars.containsKey(var1)) {
            var4 = (VirtualJarFile)this.virtualJars.get(var1);
         } else {
            var4 = VirtualJarFactory.createVirtualJar(this.getModuleRoots(var1));
            this.virtualJars.put(var1, var4);
         }
      }

      if (var4 == null) {
         return null;
      } else {
         ZipEntry var5 = var4.getEntry(var2 + "/" + var3);
         return var5 == null ? null : var4.getInputStream(var5);
      }
   }

   public synchronized void registerLink(String var1, File var2) {
      Object var3 = (List)this.uriLinks.get(var1);
      if (var3 == null) {
         var3 = new ArrayList();
      }

      ((List)var3).add(var2);
      this.uriLinks.put(var1, var3);
   }

   public ClassFinder getClassFinder() {
      return this.classfinder;
   }

   public void remove() {
      Iterator var1 = this.virtualJars.values().iterator();

      while(var1.hasNext()) {
         IOUtils.forceClose((VirtualJarFile)var1.next());
      }

      this.classfinder.close();
      this.archive.remove();
      this.archive.removeTopLevelTempDir();
   }
}
