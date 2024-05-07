package weblogic;

import java.io.File;
import weblogic.utils.classloaders.BeaHomeHolder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.FileSource;
import weblogic.utils.classloaders.Source;
import weblogic.utils.classloaders.ZipSource;

public final class Home {
   private final String path;

   private Home() {
      String var1 = System.getProperty("weblogic.home");
      if (var1 == null) {
         String var2 = this.getFileSource("weblogic.Home");
         if (var2 != null) {
            if (var2.endsWith(".jar")) {
               int var3 = var2.lastIndexOf("/lib/");
               if (var3 > -1) {
                  var1 = var2.substring(0, var3);
               }
            } else {
               String var6 = var2.toLowerCase();
               String var4 = "/classes/weblogic/home.class";
               int var5 = var6.lastIndexOf(var4);
               if (var5 > -1) {
                  var1 = var2.substring(0, var5);
                  var1 = var1.replace('/', File.separatorChar);
               }
            }
         }
      }

      this.path = var1;
   }

   private String getClassPath() {
      return System.getProperty("java.class.path");
   }

   private String getFileSource(String var1) {
      ClasspathClassFinder2 var2 = null;

      String var4;
      try {
         var2 = new ClasspathClassFinder2(this.getClassPath());
         Source var3 = var2.getClassSource(var1);
         if (var3 instanceof FileSource) {
            var4 = ((FileSource)var3).getFile().toString().replace(File.separatorChar, '/');
            return var4;
         }

         if (!(var3 instanceof ZipSource)) {
            var4 = null;
            return var4;
         }

         var4 = ((ZipSource)var3).getFile().getName().replace(File.separatorChar, '/');
      } finally {
         if (var2 != null) {
            var2.close();
         }

      }

      return var4;
   }

   private static Home getInstance() {
      Home var0 = Home.HomeSingleton.SINGLETON;
      if (var0.path == null) {
         throw new RuntimeException("error in finding weblogic.Home");
      } else {
         return var0;
      }
   }

   public static String getPath() {
      Home var0 = getInstance();
      return var0.path;
   }

   public static File getFile() {
      return new File(getPath());
   }

   public static Home getHome() {
      return getInstance();
   }

   public static String getMiddlewareHomePath() {
      return BeaHomeHolder.getBeaHome();
   }

   public String toString() {
      return getPath();
   }

   public static void main(String[] var0) {
      System.out.println(getHome());
   }

   // $FF: synthetic method
   Home(Object var1) {
      this();
   }

   private static final class HomeSingleton {
      private static final Home SINGLETON = new Home();
   }
}
