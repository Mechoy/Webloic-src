package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.JarClassFinder;

public class CaseAwareClasspathClassFinder extends ClasspathClassFinder2 {
   private static final boolean WIN_32;
   private static final boolean ENFORCE_CASE;

   public CaseAwareClasspathClassFinder(String var1) {
      super(var1, new HashSet());
   }

   protected ClassFinder getClassFinder(File var1, Set var2) throws IOException {
      return new JarClassFinder(var1, var2, ENFORCE_CASE);
   }

   static {
      WIN_32 = System.getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH).indexOf("windows") >= 0;
      ENFORCE_CASE = !SecurityServiceManager.areWebAppFilesCaseInsensitive() && WIN_32;
   }
}
