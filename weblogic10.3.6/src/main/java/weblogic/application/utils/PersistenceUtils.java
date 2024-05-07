package weblogic.application.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class PersistenceUtils {
   private static final String persistenceUri = "META-INF/persistence.xml";
   private static final FileFilter JARFILE = new FileFilter() {
      public boolean accept(File var1) {
         return var1.getName().endsWith(".jar") && var1.isFile();
      }
   };

   public static void addRootPersistenceJars(GenericClassLoader var0, String var1, ApplicationBean var2) throws IOException {
      File[] var3 = getApplicationRoots(var0, var1, false);
      HashSet var4 = null;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         File[] var6 = var3[var5].listFiles(JARFILE);
         if (var6 != null) {
            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var4 == null) {
                  var4 = new HashSet();
                  loadJarModuleUris(var2, var4);
               }

               File var8 = var6[var7];
               String var9 = getSimpleName(var8);
               if (!var4.contains(var9)) {
                  VirtualJarFile var10 = null;

                  try {
                     var10 = VirtualJarFactory.createVirtualJar(var8);
                     if (var10.getEntries("META-INF/persistence.xml").hasNext()) {
                        var0.addClassFinder(new JarClassFinder(var8));
                     }
                  } finally {
                     IOUtils.forceClose(var10);
                  }
               }
            }
         }
      }

   }

   private static String getSimpleName(File var0) {
      String var1 = var0.getName();
      int var2 = var1.lastIndexOf(File.pathSeparator);
      return var2 == -1 ? var1 : var1.substring(var2 + 1);
   }

   private static void loadJarModuleUris(ApplicationBean var0, Set var1) {
      ModuleBean[] var2 = var0.getModules();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            ModuleBean var4 = var2[var3];
            if (var4.isEjbSet() && var4.getEjb() != null) {
               var1.add(var4.getEjb());
            } else if (var4.isJavaSet() && var4.getJava() != null) {
               var1.add(var4.getJava());
            }
         }
      }

   }

   public static File[] getApplicationRoots(GenericClassLoader var0, String var1, boolean var2) throws IOException {
      Enumeration var3 = var0.getResources(var1 + "#/");
      ArrayList var4 = new ArrayList();

      while(var3.hasMoreElements()) {
         File var5 = new File(((URL)var3.nextElement()).getFile());
         if (var2) {
            var4.add(var5.getCanonicalFile());
         } else {
            var4.add(var5);
         }
      }

      if (var2) {
         return (File[])((File[])var4.toArray(new File[0]));
      } else {
         return (File[])((File[])var4.toArray(new File[0]));
      }
   }
}
