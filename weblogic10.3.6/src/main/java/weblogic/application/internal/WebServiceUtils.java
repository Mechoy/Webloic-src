package weblogic.application.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import weblogic.application.ApplicationFileManager;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.utils.jars.VirtualJarFile;

public final class WebServiceUtils implements J2EEApplicationService.HaltListener {
   private static boolean DEBUG = false;
   private static final String WEB_SERVICES_URI = "WEB-INF/web-services.xml";
   private static final String WEB_URI = "WEB-INF/web-services.xml";
   private static String FILE_NAME = "WebServiceUtils.ser";
   private LinkedList cacheList;
   private static final int MAX_LIST_SIZE = 20;
   private File serFile;

   private WebServiceUtils() {
      this.serFile = new File(J2EEApplicationService.getTempDir(), FILE_NAME);
      this.cacheList = this.readSavedCache();
      J2EEApplicationService.registerHaltListener(this);
   }

   private LinkedList readSavedCache() {
      if (!this.serFile.exists()) {
         return new LinkedList();
      } else {
         ObjectInputStream var1 = null;

         LinkedList var3;
         try {
            FileInputStream var2 = new FileInputStream(this.serFile);
            var1 = new ObjectInputStream(var2);
            var3 = (LinkedList)var1.readObject();
            return var3;
         } catch (Exception var13) {
            if (DEBUG) {
               var13.printStackTrace();
            }

            var3 = new LinkedList();
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Exception var12) {
               }
            }

         }

         return var3;
      }
   }

   public void halt() {
      ObjectOutputStream var1 = null;

      try {
         FileOutputStream var2 = new FileOutputStream(this.serFile);
         var1 = new ObjectOutputStream(var2);
         var1.writeObject(this.cacheList);
      } catch (IOException var11) {
         if (DEBUG) {
            var11.printStackTrace();
         }
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var10) {
            }
         }

      }

   }

   public static WebServiceUtils getWebServiceUtils() {
      return WebServiceUtils.WebServiceUtilsSingleton.SINGLETON;
   }

   private boolean isCacheable(VirtualJarFile var1) {
      return !var1.isDirectory();
   }

   private Set readCache(ApplicationMBean var1, VirtualJarFile var2) {
      if (!this.isCacheable(var2)) {
         return null;
      } else {
         String var3 = var1.getName();
         synchronized(this.cacheList) {
            Iterator var5 = this.cacheList.iterator();

            CacheEntry var6;
            do {
               if (!var5.hasNext()) {
                  return null;
               }

               var6 = (CacheEntry)var5.next();
            } while(!var6.appName.equals(var3));

            File var7 = var2.getRootFiles()[0];
            var5.remove();
            if (var7.lastModified() == var6.lastModified) {
               this.cacheList.addFirst(var6);
               return var6.wsSet;
            } else {
               return null;
            }
         }
      }
   }

   private void updateCache(ApplicationMBean var1, VirtualJarFile var2, Set var3) {
      if (this.isCacheable(var2)) {
         File var4 = var2.getRootFiles()[0];
         CacheEntry var5 = new CacheEntry();
         var5.appName = var1.getName();
         var5.lastModified = var4.lastModified();
         var5.wsSet = var3;
         synchronized(this.cacheList) {
            this.cacheList.addFirst(var5);
            if (this.cacheList.size() > 20) {
               this.cacheList.removeLast();
            }

         }
      }
   }

   Set findWebServices(ApplicationMBean var1, ApplicationFileManager var2, VirtualJarFile var3, ModuleBean[] var4) throws IOException {
      Set var5 = this.readCache(var1, var3);
      if (var5 != null) {
         return var5;
      } else {
         Object var8 = Collections.EMPTY_SET;

         for(int var6 = 0; var6 < var4.length; ++var6) {
            if (var4[var6].getWeb() != null) {
               String var7 = var4[var6].getWeb().getWebUri();
               if (this.isWebService(var2, var3, var7)) {
                  if (((Set)var8).size() == 0) {
                     var8 = new HashSet();
                  }

                  ((Set)var8).add(var7);
               }
            }
         }

         this.updateCache(var1, var3, (Set)var8);
         return (Set)var8;
      }
   }

   private boolean isWebService(ApplicationFileManager var1, VirtualJarFile var2, String var3) throws IOException {
      if (var2.isDirectory()) {
         VirtualJarFile var29 = null;

         boolean var30;
         try {
            var29 = var1.getVirtualJarFile(var3);
            var30 = var29.getEntry("WEB-INF/web-services.xml") != null;
         } finally {
            if (var29 != null) {
               try {
                  var29.close();
               } catch (IOException var26) {
               }
            }

         }

         return var30;
      } else if (var2.getEntry(var3 + "/" + "WEB-INF/web-services.xml") != null) {
         return var2.getEntry(var3 + "/" + "WEB-INF/web-services.xml") != null;
      } else {
         ZipInputStream var4 = null;

         boolean var7;
         try {
            ZipEntry var5 = var2.getEntry(var3);
            if (var5 == null) {
               boolean var31 = false;
               return var31;
            }

            InputStream var6 = var2.getInputStream(var5);
            if (var6 != null) {
               var4 = new ZipInputStream(var6);

               do {
                  if ((var5 = var4.getNextEntry()) == null) {
                     var7 = false;
                     return var7;
                  }
               } while(!var5.getName().equals("WEB-INF/web-services.xml"));

               var7 = true;
               return var7;
            }

            var7 = false;
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var25) {
               }
            }

         }

         return var7;
      }
   }

   // $FF: synthetic method
   WebServiceUtils(Object var1) {
      this();
   }

   private static class CacheEntry implements Serializable {
      String appName;
      long lastModified;
      Set wsSet;

      private CacheEntry() {
      }

      // $FF: synthetic method
      CacheEntry(Object var1) {
         this();
      }
   }

   private static final class WebServiceUtilsSingleton {
      private static final WebServiceUtils SINGLETON = new WebServiceUtils();
   }
}
