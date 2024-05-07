package weblogic.connector.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import weblogic.connector.common.Debug;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClassFinderUtils;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.utils.jars.VirtualJarFile;

public final class RAClassFinder implements ClassFinder {
   private VirtualJarFile connectorVJar;
   private String[] jarEntries;
   private Hashtable classEntries;
   private Hashtable indexedJars;
   private boolean isClosed;
   private String classpath;
   private RarArchive rar;

   public RAClassFinder(VirtualJarFile var1) {
      this.isClosed = false;
      this.classpath = null;
      this.rar = null;
      this.connectorVJar = var1;
      ArrayList var2 = new ArrayList();
      Iterator var4 = this.connectorVJar.entries();

      while(var4.hasNext()) {
         ZipEntry var3 = (ZipEntry)var4.next();
         if (var3.getName().endsWith(".jar")) {
            var2.add(var3.getName());
         }
      }

      this.jarEntries = (String[])var2.toArray(new String[var2.size()]);
      this.indexedJars = new Hashtable();
      this.classEntries = new Hashtable();
   }

   public RAClassFinder(RarArchive var1) {
      this(var1.getVirtualJarFile());
      this.rar = var1;
   }

   public Source getSource(String var1) {
      if (this.isClosed) {
         return null;
      } else {
         Source var2 = null;

         for(int var4 = 0; var4 < this.jarEntries.length && var2 == null; ++var4) {
            String var3 = this.jarEntries[var4];
            var2 = this.findClassInJar(var1, var3);
         }

         return var2;
      }
   }

   private Source findClassInJar(String var1, String var2) {
      RASource var3 = null;
      if (!this.isClosed) {
         ZipEntry var4 = this.connectorVJar.getEntry(var2);
         this.indexJar(var4);
         if (this.jarContainsClass(var2, var1)) {
            var3 = new RASource(this.connectorVJar, var4, var1);
         }
      }

      return var3;
   }

   private void indexJar(ZipEntry var1) {
      if (!this.jarIndexed(var1.getName())) {
         ZipInputStream var2 = null;

         try {
            var2 = new ZipInputStream(this.connectorVJar.getInputStream(var1));

            ZipEntry var3;
            String var4;
            do {
               var3 = var2.getNextEntry();
               if (var3 != null) {
                  var4 = var1.getName() + "#" + var3.getName();
                  this.classEntries.put(var4, var4);
               }
            } while(var3 != null);

            var4 = var1.getName();
            this.indexedJars.put(var4, var4);
         } catch (IOException var14) {
            debug("Caught IOException while trying to find all classes in " + var1.getName() + ": " + var14.getMessage());
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var13) {
               debug("Caught IOException while trying to close " + var1.getName() + ": " + var13.getMessage());
            }

         }
      }

   }

   private boolean jarIndexed(String var1) {
      return this.indexedJars.get(var1) != null;
   }

   private boolean jarContainsClass(String var1, String var2) {
      return this.classEntries.get(var1 + "#" + var2) != null;
   }

   public Enumeration getSources(String var1) {
      ArrayList var2 = new ArrayList();
      if (!this.isClosed) {
         for(int var5 = 0; var5 < this.jarEntries.length; ++var5) {
            String var4 = this.jarEntries[var5];
            Source var3 = this.findClassInJar(var1, var4);
            if (var3 != null) {
               var2.add(var3);
            }
         }
      }

      return new IteratorEnumerator(var2.iterator());
   }

   public Source getClassSource(String var1) {
      if (!this.isClosed) {
         var1 = var1.replace('.', '/') + ".class";
         return this.getSource(var1);
      } else {
         return null;
      }
   }

   public String getClassPath() {
      if (!this.isClosed) {
         if (this.rar != null) {
            return this.rar.getClassPath();
         } else {
            return this.connectorVJar.isDirectory() ? this.computeClassPathForExplodedRar() : this.connectorVJar.getName();
         }
      } else {
         return "";
      }
   }

   private String computeClassPathForExplodedRar() {
      if (this.classpath == null) {
         this.classpath = ClassPathUtil.computeClasspath(this.connectorVJar.getName(), this.jarEntries);
      }

      return this.classpath;
   }

   public void close() {
      this.connectorVJar = null;
      this.jarEntries = null;
      this.isClosed = true;
   }

   public ClassFinder getManifestFinder() {
      MultiClassFinder var1 = new MultiClassFinder();
      if (!this.isClosed) {
         HashSet var3 = new HashSet();

         try {
            ClassFinder var2;
            if (this.connectorVJar.isDirectory()) {
               File var4 = new File(this.connectorVJar.getDirectory().toString());
               var2 = ClassFinderUtils.getManifestFinder(var4, var3);
            } else {
               var2 = ClassFinderUtils.getManifestFinder(this.connectorVJar.getJarFile(), var3);
            }

            if (var2 != null) {
               var1.addFinder(var2);
            }
         } catch (IOException var5) {
            debug("Caught IOException while trying to get the manifest finders :" + var5.getMessage());
         }
      }

      return var1;
   }

   public Enumeration entries() {
      return EmptyEnumerator.EMPTY;
   }

   private static void debug(String var0) {
      if (Debug.isClassLoadingEnabled()) {
         Debug.classloading(var0);
      }

   }
}
