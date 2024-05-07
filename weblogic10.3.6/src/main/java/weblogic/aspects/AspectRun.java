package weblogic.aspects;

import java.lang.reflect.Method;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;

public class AspectRun {
   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.out.println("Usage: java weblogic.aspects.AspectRun [class name] [arguments]");
         System.exit(1);
      }

      ClasspathClassFinder2 var1 = new ClasspathClassFinder2();
      GenericClassLoader var2 = GenericClassLoader.getRootClassLoader(var1);
      Thread.currentThread().setContextClassLoader(var2);
      String var3 = var0[0];
      String[] var4 = new String[var0.length - 1];
      if (var4.length > 0) {
         System.arraycopy(var0, 1, var4, 0, var0.length - 1);
      }

      System.setProperty("weblogic.aspects", "all");
      Class var5 = var2.loadClass(var3);
      Method var6 = var5.getMethod("main", String[].class);
      var6.invoke((Object)null, var4);
   }
}
