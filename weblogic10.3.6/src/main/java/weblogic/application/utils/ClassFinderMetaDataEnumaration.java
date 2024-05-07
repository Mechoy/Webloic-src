package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.apache.openjpa.lib.meta.ClassAnnotationMetaDataFilter;
import org.apache.openjpa.lib.meta.MetaDataFilter;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.FileSource;
import weblogic.utils.classloaders.Source;
import weblogic.utils.classloaders.URLSource;
import weblogic.utils.classloaders.ZipSource;

public class ClassFinderMetaDataEnumaration implements Enumeration {
   private final ClassAnnotationMetaDataFilter annotationFilter;
   private final Enumeration delegate;
   private MetaDataFilter.Resource entry;

   public ClassFinderMetaDataEnumaration(ClassFinder var1, ClassAnnotationMetaDataFilter var2) {
      this.annotationFilter = var2;
      this.delegate = var1.entries();
   }

   public boolean hasMoreElements() {
      while(this.entry == null && this.delegate.hasMoreElements()) {
         final Source var1 = (Source)this.delegate.nextElement();
         if (var1.getURL().getFile().endsWith(".class")) {
            this.entry = new MetaDataFilter.Resource() {
               public String getName() {
                  if (var1 instanceof FileSource) {
                     FileSource var1x = (FileSource)var1;

                     try {
                        int var2 = (new File(var1x.getParentDir())).getCanonicalPath().length();
                        return var1x.getFile().getCanonicalPath().substring(var2 + 1);
                     } catch (IOException var3) {
                        var3.printStackTrace();
                     }
                  } else if (var1 instanceof ZipSource) {
                     return ((ZipSource)var1).getEntry().getName();
                  }

                  return var1.getURL().getFile();
               }

               public byte[] getContent() throws IOException {
                  return (new URLSource(var1.getURL())).getBytes();
               }
            };

            try {
               if (!this.annotationFilter.matches(this.entry)) {
                  this.entry = null;
               }
            } catch (IOException var3) {
               this.entry = null;
               return false;
            }
         }
      }

      return this.entry != null;
   }

   public Object nextElement() {
      if (!this.hasMoreElements()) {
         throw new NoSuchElementException();
      } else {
         String var1 = this.entry.getName();
         this.entry = null;
         var1 = var1.replace('\\', '.');
         var1 = var1.replace('/', '.');
         var1 = var1.substring(0, var1.length() - 6);
         return var1;
      }
   }
}
