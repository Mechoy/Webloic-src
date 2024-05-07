package weblogic.ejb.spi;

import java.io.File;
import java.io.IOException;
import weblogic.application.io.Archive;
import weblogic.application.io.DescriptorFinder;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;

public final class ExplodedEJB extends Archive {
   private final MultiClassFinder classFinder = new MultiClassFinder();

   public ExplodedEJB(String var1, File[] var2) {
      String var3 = this.flatten(var2);
      this.classFinder.addFinder(new DescriptorFinder(var1, new ClasspathClassFinder2(var3)));
      this.classFinder.addFinder(new ClasspathClassFinder2(var3));
   }

   private String flatten(File[] var1) {
      StringBuffer var2 = new StringBuffer();
      String var3 = "";

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2.append(var3);
         var3 = File.pathSeparator;
         var2.append(var1[var4].getAbsolutePath());
      }

      return var2.toString();
   }

   public ClassFinder getClassFinder() throws IOException {
      return this.classFinder;
   }

   public void remove() {
   }
}
