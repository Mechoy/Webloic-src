package weblogic.marathon.fs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;

public class FSFinder implements ClassFinder {
   FS[] fs;

   public FSFinder(FS[] var1) {
      this.fs = (FS[])((FS[])var1.clone());
   }

   public FSFinder(FS var1) {
      this.fs = new FS[1];
      this.fs[0] = var1;
   }

   public void addFS(FS var1) {
      FS[] var2 = new FS[this.fs.length + 1];
      System.arraycopy(this.fs, 0, var2, 0, this.fs.length);
      var2[this.fs.length] = var1;
      this.fs = var2;
   }

   public String getClassPath() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.fs.length; ++var2) {
         var1.append(this.fs[var2].getRoot().getAbsolutePath());
         if (var2 != this.fs.length - 1) {
            var1.append(File.pathSeparator);
         }
      }

      return var1.toString();
   }

   public Source getClassSource(String var1) {
      String var2 = var1.replace('.', '/') + ".class";
      return this.getSource(var2);
   }

   public ClassFinder getManifestFinder() {
      throw new Error("NYI");
   }

   public Enumeration entries() {
      return EmptyEnumerator.EMPTY;
   }

   protected String getPrefixPath() {
      return "";
   }

   public Source getSource(String var1) {
      return this.getSourcesInternal(var1, (List)null);
   }

   public Enumeration getSources(String var1) {
      ArrayList var2 = new ArrayList();
      this.getSourcesInternal(var1, var2);
      return new IteratorEnumerator(var2.iterator());
   }

   public Source getSourcesInternal(String var1, List var2) {
      var1 = var1.replace(File.separatorChar, '/');
      var1 = this.getPrefixPath() + var1;

      for(int var3 = 0; var3 < this.fs.length; ++var3) {
         if (this.fs[var3].exists(var1)) {
            try {
               Entry var4 = this.fs[var3].getEntry(var1);
               if (var2 == null) {
                  return new FSSource(this.fs[var3], var4);
               }

               var2.add(new FSSource(this.fs[var3], var4));
            } catch (IOException var5) {
               throw new RuntimeException("nested: " + var5);
            }
         }
      }

      return null;
   }

   public void close() {
   }
}
