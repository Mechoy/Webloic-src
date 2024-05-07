package weblogic.application.io;

import java.io.File;
import java.io.IOException;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.MultiClassFinder;

public final class Jar extends Archive {
   private final File jarFile;
   private final String uri;

   public Jar(String var1, File var2) {
      if (DEBUG) {
         Debug.assertion(var2 != null);
         Debug.assertion(var2.exists());
         Debug.assertion(!var2.isDirectory());
      }

      this.uri = var1;
      this.jarFile = var2;
   }

   public ClassFinder getClassFinder() throws IOException {
      MultiClassFinder var1 = new MultiClassFinder();
      var1.addFinder(new DescriptorFinder(this.uri, new ClasspathClassFinder2(this.jarFile.getAbsolutePath())));
      var1.addFinder(new ClasspathClassFinder2(this.jarFile.getAbsolutePath()));
      return var1;
   }

   public void remove() {
   }
}
