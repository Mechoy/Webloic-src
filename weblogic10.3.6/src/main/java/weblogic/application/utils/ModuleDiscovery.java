package weblogic.application.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.utils.application.WarDetector;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class ModuleDiscovery {
   private static final int MAX_NESTED_DIR_DEPTH = 6;
   static final boolean debug = false;
   private static String[] knownExtensions = null;
   private static final FileFilter EXT_OR_DIR;

   private ModuleDiscovery() {
   }

   public static ApplicationBean discoverModules(VirtualJarFile var0) throws IOException {
      if (var0.isDirectory()) {
         File[] var1 = var0.getRootFiles();
         return var1.length == 1 ? discoverModules(var1[0]) : discoverModules(var1);
      } else {
         return discoverModulesArchived(var0);
      }
   }

   public static ApplicationBean discoverModules(File var0) throws IOException {
      LinkedList var1 = new LinkedList();
      findModules(var0, var1);
      return populateAppBean(var1);
   }

   public static ApplicationBean discoverModules(File[] var0) throws IOException {
      if (var0 != null && var0.length != 0) {
         LinkedList var1 = new LinkedList();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            findModules(var0[var2], var0[var2], 0, var1, true);
         }

         return populateAppBean(var1);
      } else {
         return null;
      }
   }

   static void findModules(File var0, List<DiscoveredModule> var1) throws IOException {
      findModules(var0, var0, 0, var1, false);
   }

   private static void findModules(File var0, File var1, int var2, List<DiscoveredModule> var3, boolean var4) throws IOException {
      assert var1.isDirectory();

      ++var2;
      if (var2 <= 6) {
         URI var5 = var0.toURI();
         File[] var6 = var1.listFiles(EXT_OR_DIR);

         for(int var7 = 0; var7 < var6.length; ++var7) {
            URI var8 = var5.relativize(var6[var7].toURI());
            String var9 = var8.toString();
            if (var9.endsWith("/")) {
               var9 = var9.substring(0, var9.length() - 1);
            }

            if (!"APP-INF".equals(var9) && !"lib".equals(var9) && (!var4 || !sawRelativePath(var9, var3))) {
               DiscoveredModule var10 = DiscoveredModuleFactory.makeDiscoveredModule(var6[var7], var9);
               if (var10 != null) {
                  var3.add(var10);
               } else if (var6[var7].isDirectory()) {
                  findModules(var0, var6[var7], var2, var3, var4);
               }
            }
         }

      }
   }

   private static boolean sawRelativePath(String var0, List<DiscoveredModule> var1) {
      Iterator var2 = var1.iterator();

      DiscoveredModule var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (DiscoveredModule)var2.next();
      } while(!var0.startsWith(var3.getURI()));

      return true;
   }

   private static ApplicationBean populateAppBean(List<DiscoveredModule> var0) {
      ApplicationBean var1 = null;
      if (var0.size() > 0) {
         var1 = (ApplicationBean)(new DescriptorManager()).createDescriptorRoot(ApplicationBean.class).getRootBean();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            DiscoveredModule var3 = (DiscoveredModule)var2.next();
            var3.createModule(var1);
         }
      }

      return var1;
   }

   public static ApplicationBean discoverModulesArchived(VirtualJarFile var0) throws IOException {
      ArrayList var1 = new ArrayList();
      HashSet var2 = new HashSet();

      try {
         assert !var0.isDirectory();

         Iterator var3 = var0.entries();

         while(var3.hasNext()) {
            ZipEntry var4 = (ZipEntry)var3.next();
            String var5 = var4.getName();
            if (var5.endsWith("/")) {
               var5 = var5.substring(0, var5.length() - 1);
            }

            if (!alreadyClaimedPath(var2, var5)) {
               DiscoveredModule var6 = DiscoveredModuleFactory.makeDiscoveredModule(var0, var4, var5);
               if (var6 != null) {
                  var2.add(var5);
                  var1.add(var6);
               }
            }
         }
      } catch (IOException var7) {
      }

      return populateAppBean(var1);
   }

   private static boolean alreadyClaimedPath(Set<String> var0, String var1) {
      if (var0.contains(var1)) {
         return true;
      } else {
         Iterator var2 = var0.iterator();

         String var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (String)var2.next();
         } while(!var1.startsWith(var3));

         return true;
      }
   }

   private static void dumpBean(ApplicationBean var0) throws IOException {
      (new DescriptorManager()).writeDescriptorAsXML(((DescriptorBean)var0).getDescriptor(), System.out);
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.out.println("Usage: java " + ModuleDiscovery.class.getName() + " <ear | dir>");
      } else {
         ApplicationBean var1 = null;
         long var2 = System.currentTimeMillis();
         VirtualJarFile var4 = null;

         try {
            var4 = VirtualJarFactory.createVirtualJar(new File(var0[0]));
            var1 = discoverModules(var4);
            long var5 = System.currentTimeMillis();
            if (var1 != null) {
               dumpBean(var1);
            } else {
               System.out.println("AppBean is null");
            }

            System.out.println("Elapsed: " + (var5 - var2));
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var13) {
               }
            }

         }

      }
   }

   static {
      String[] var0 = WarDetector.instance.getSuffixes();
      knownExtensions = new String[var0.length + 2];
      int var1 = 0;
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         knownExtensions[var1++] = var5;
      }

      knownExtensions[var1++] = ".jar";
      knownExtensions[var1++] = ".rar";
      EXT_OR_DIR = new FileFilter() {
         public boolean accept(File var1) {
            if (var1.isDirectory()) {
               return true;
            } else {
               String var2 = var1.getName();

               for(int var3 = 0; var3 < ModuleDiscovery.knownExtensions.length; ++var3) {
                  if (var2.endsWith(ModuleDiscovery.knownExtensions[var3])) {
                     return true;
                  }
               }

               return false;
            }
         }
      };
   }
}
