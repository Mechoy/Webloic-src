package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import weblogic.application.library.CachableLibMetadataEntry;
import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.LibraryProcessingException;
import weblogic.utils.classloaders.JarClassFinder;
import weblogic.utils.classloaders.Source;

class AnnotatedClassesCacheEntry implements CachableLibMetadataEntry {
   protected WarLibraryDefinition library;

   public AnnotatedClassesCacheEntry(WarLibraryDefinition var1) {
      this.library = var1;
   }

   public CachableLibMetadataType getType() {
      return CachableLibMetadataType.ANNOTATED_CLASSES;
   }

   public File getLocation() {
      return this.library.getLibTempDir();
   }

   public Object getCachableObject() throws LibraryProcessingException {
      return this.library.getAnnotatedClasses();
   }

   public boolean isStale(long var1) {
      if (this.library.isArchived()) {
         return false;
      } else {
         JarClassFinder var3 = null;

         try {
            var3 = new JarClassFinder(this.library.getLocation());
            Enumeration var4 = var3.entries();

            while(var4.hasMoreElements()) {
               Source var5 = (Source)var4.nextElement();
               if (var5.getURL().getPath().endsWith(".class") && this.isStale(var5, var1)) {
                  boolean var6 = true;
                  return var6;
               }
            }

            boolean var13 = false;
            return var13;
         } catch (IOException var11) {
         } finally {
            if (var3 != null) {
               var3.close();
            }

         }

         return true;
      }
   }

   private boolean isStale(Source var1, long var2) {
      return var1.lastModified() > var2;
   }
}
