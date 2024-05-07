package weblogic.servlet.internal;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import weblogic.application.library.CachableLibMetadataEntry;
import weblogic.application.library.LibraryMetadataCache;
import weblogic.application.library.LibraryProcessingException;

public abstract class DescriptorCacheEntry implements CachableLibMetadataEntry {
   protected WarLibraryDefinition library;

   public DescriptorCacheEntry(WarLibraryDefinition var1) {
      this.library = var1;
   }

   public File getLocation() {
      return this.library.getLibTempDir();
   }

   public Object getCachableObject() throws LibraryProcessingException {
      File var1 = LibraryMetadataCache.getCacheDir(this);
      List var2 = this.getResourceLocations();
      return new DescriptorCachableObject(var1, var2);
   }

   protected abstract List getResourceLocations() throws LibraryProcessingException;

   public boolean isStale(long var1) {
      return !this.library.isArchived();
   }

   static class DescriptorCachableObject implements Serializable {
      private List resLocations;
      private File cacheBaseDir;

      public DescriptorCachableObject(File var1, List var2) {
         this.cacheBaseDir = var1;
         this.resLocations = var2;
      }

      public File getCacheBaseDir() {
         return this.cacheBaseDir;
      }

      public List getResourceLocation() {
         return this.resLocations;
      }
   }
}
