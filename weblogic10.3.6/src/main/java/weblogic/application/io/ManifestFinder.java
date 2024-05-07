package weblogic.application.io;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipFile;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.AbstractClassFinder;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClassFinderUtils;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.classloaders.ClassFinderUtils.Attr;

public class ManifestFinder extends AbstractClassFinder implements ClassFinder {
   private final ClassFinder manifestFinder;

   public ManifestFinder(File var1) {
      this(var1, (ClassFinderUtils.Attr)null);
   }

   private ManifestFinder(File var1, ClassFinderUtils.Attr var2) {
      ClassFinder var3 = null;
      if (var1.exists()) {
         if (var1.isFile()) {
            ZipFile var4 = null;

            try {
               var4 = new ZipFile(var1);
               var3 = ClassFinderUtils.getManifestFinder(var4, new HashSet(), var2);
            } catch (IOException var17) {
            } finally {
               try {
                  if (var4 != null) {
                     var4.close();
                  }
               } catch (IOException var15) {
               }

            }
         } else {
            try {
               var3 = ClassFinderUtils.getManifestFinder(var1, new HashSet(), var2);
            } catch (IOException var16) {
            }
         }
      }

      if (var3 == null) {
         this.manifestFinder = NullClassFinder.NULL_FINDER;
      } else {
         this.manifestFinder = var3;
      }

   }

   public String getClassPath() {
      return this.manifestFinder.getClassPath();
   }

   public Source getSource(String var1) {
      return this.manifestFinder.getSource(var1);
   }

   public Enumeration getSources(String var1) {
      return this.manifestFinder.getSources(var1);
   }

   public ClassFinder getManifestFinder() {
      return this.manifestFinder;
   }

   public Enumeration entries() {
      return this.manifestFinder.entries();
   }

   public void close() {
      this.manifestFinder.close();
   }

   public String[] getPathElements() {
      return StringUtils.splitCompletely(this.getClassPath(), File.pathSeparator);
   }

   // $FF: synthetic method
   ManifestFinder(File var1, ClassFinderUtils.Attr var2, Object var3) {
      this(var1, var2);
   }

   public static final class ExtensionListFinder extends ManifestFinder implements ClassFinder {
      public ExtensionListFinder(File var1) {
         super(var1, Attr.EXTENSION_LIST, null);
      }
   }

   public static final class ClassPathFinder extends ManifestFinder implements ClassFinder {
      public ClassPathFinder(File var1) {
         super(var1, Attr.CLASS_PATH, null);
      }
   }
}
