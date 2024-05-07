package weblogic.application.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import weblogic.utils.FileUtils;

public final class LibraryMetadataCache {
   private static final String CACHE_FILE = ".cache.ser";
   private static final LibraryMetadataCache INSTANCE = new LibraryMetadataCache();
   private Map cacheObjectMap = new HashMap();
   private boolean useCache = true;

   private LibraryMetadataCache() {
   }

   public static LibraryMetadataCache getInstance() {
      return INSTANCE;
   }

   public void disableCache() {
      this.useCache = false;
   }

   public void initLibraryCache(CachableLibMetadata var1) throws LibraryProcessingException {
      if (this.useCache) {
         CachableLibMetadataEntry[] var2 = var1.findAllCachableEntry();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.lookupCachedObject(var2[var3]);
         }

      }
   }

   public void clearLibraryCache(CachableLibMetadata var1) {
      if (this.useCache) {
         CachableLibMetadataEntry[] var2 = var1.findAllCachableEntry();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            File var4 = getCacheFile(var2[var3], false);
            this.cacheObjectMap.remove(var4);
            remove(var2[var3]);
         }

      }
   }

   public Object lookupCachedObject(CachableLibMetadataEntry var1) throws LibraryProcessingException {
      if (var1 == null) {
         return null;
      } else if (!this.useCache) {
         return var1.getCachableObject();
      } else {
         File var2 = getCacheFile(var1, true);
         Object var3;
         if (!isStale(var2, var1)) {
            var3 = this.cacheObjectMap.get(var2);
            if (var3 != null) {
               return var3;
            }

            try {
               Object var4 = readObject(var2);
               this.cacheObjectMap.put(var2, var4);
               return var4;
            } catch (Exception var6) {
            }
         }

         if (var2.exists()) {
            var2.delete();
         }

         var3 = var1.getCachableObject();

         try {
            writeObject(var2, var3);
         } catch (IOException var5) {
            var2.delete();
         }

         this.cacheObjectMap.put(var2, var3);
         return var3;
      }
   }

   private static void remove(CachableLibMetadataEntry var0) {
      File var1 = getCacheDir(var0);
      if (var1.exists()) {
         FileUtils.remove(var1);
      }
   }

   public static File getCacheDir(CachableLibMetadataEntry var0) {
      return new File(var0.getLocation(), var0.getType().getName());
   }

   private static File getCacheFile(CachableLibMetadataEntry var0, boolean var1) {
      File var2 = getCacheDir(var0);
      if (!var2.exists() && var1) {
         var2.mkdirs();
      }

      return new File(var2, ".cache.ser");
   }

   private static boolean isStale(File var0, CachableLibMetadataEntry var1) {
      return !var0.exists() || var1.isStale(var0.lastModified());
   }

   private static Object readObject(File var0) throws IOException, ClassNotFoundException {
      ObjectInputStream var1 = null;

      Object var2;
      try {
         var1 = new ObjectInputStream(new FileInputStream(var0));
         var2 = var1.readObject();
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var9) {
            }
         }

      }

      return var2;
   }

   private static void writeObject(File var0, Object var1) throws IOException {
      ObjectOutputStream var2 = null;

      try {
         var2 = new ObjectOutputStream(new FileOutputStream(var0));
         var2.writeObject(var1);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var9) {
            }
         }

      }

   }
}
