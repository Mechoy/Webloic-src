package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import weblogic.application.io.ClasspathInfo;
import weblogic.application.io.DescriptorFinder;
import weblogic.application.io.ExplodedJar;
import weblogic.application.io.JarCopyFilter;
import weblogic.utils.classloaders.ClassFinder;

class CaseAwareExplodedJar extends ExplodedJar {
   public CaseAwareExplodedJar(String var1, File var2, File var3, ClasspathInfo var4) throws IOException {
      super(var1, var2, var3, var4);
   }

   public CaseAwareExplodedJar(String var1, File var2, File[] var3, ClasspathInfo var4, JarCopyFilter var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected ClassFinder buildDescriptorFinder() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.dirs.length; ++var2) {
         this.addClasspath(var1, this.dirs[var2]);
      }

      return new DescriptorFinder(this.uri, new CaseAwareClasspathClassFinder(var1.toString()));
   }
}
