package weblogic.wsee.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import weblogic.wsee.tools.WsBuildException;

public class ClassLoaderUtil {
   private static final String FILE_PROTOCOL = "file";
   private static final String JAR_PROTOCOL = "jar";
   private static final String ZIP_PROTOCOL = "zip";

   private ClassLoaderUtil() {
   }

   public static URLClassLoader getClassLoader(File var0) throws WsBuildException {
      if (var0 == null) {
         return null;
      } else {
         URL var1 = null;

         try {
            var1 = var0.toURL();
         } catch (MalformedURLException var3) {
            throw new WsBuildException(var3);
         }

         URLClassLoader var2 = new URLClassLoader(new URL[]{var1});
         return var2;
      }
   }

   public static ClassLoader concat(ClassLoader... var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList(var0.length);
         ClassLoader[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ClassLoader var5 = var2[var4];
            if (var5 != null) {
               var1.add(var5);
            }
         }

         if (var1.size() == 0) {
            return null;
         } else if (var1.size() == 1) {
            return (ClassLoader)var1.get(0);
         } else {
            return new CompositeClassLoader(var1);
         }
      }
   }

   public static List<URI> getSourceURIs(ClassLoader var0, String var1) throws IOException {
      ArrayList var2 = new ArrayList();

      URI var5;
      for(Enumeration var3 = var0.getResources(var1); var3.hasMoreElements(); var2.add(var5)) {
         URL var4 = (URL)var3.nextElement();
         var5 = null;
         if ("file".equals(var4.getProtocol())) {
            var5 = getDirectoryURI(var4.toString(), var1);
         } else if ("jar".equals(var4.getProtocol())) {
            var5 = getJarURI(var4);
         } else {
            if (!"zip".equals(var4.getProtocol())) {
               throw new IllegalArgumentException("Class loader " + var0 + " returned an unhandled url " + var4);
            }

            var5 = getJarURI(var4);
         }
      }

      return var2;
   }

   private static String replaceBlankInDir(String var0) {
      return var0.replaceAll("%20", " ");
   }

   private static URI getDirectoryURI(String var0, String var1) {
      int var2 = var0.indexOf(var1);

      assert var2 > 0 : var1 + " not in URL " + var0;

      return (new File(replaceBlankInDir(var0.substring(0, var2 - 1)))).toURI();
   }

   private static URI getJarURI(URL var0) {
      String var1 = var0.getFile();
      int var2 = 0;
      if (var1.startsWith("file:")) {
         var2 = "file".length() + 1;
      }

      int var3 = var1.indexOf(33);
      if (var3 == -1) {
         var3 = var1.length();
      }

      return (new File(replaceBlankInDir(var1.substring(Math.max(0, var2), var3)))).toURI();
   }

   public static boolean setURL2SystemClassLoader(URL var0, boolean var1) {
      if (var0 == null) {
         return false;
      } else {
         try {
            URLClassLoader var2 = (URLClassLoader)ClassLoader.getSystemClassLoader();
            URL[] var3 = var2.getURLs();
            if (var3 != null) {
               URL[] var4 = var3;
               int var5 = var3.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  URL var7 = var4[var6];
                  if (var7 != null && var7.equals(var0)) {
                     if (var1) {
                        Verbose.log((Object)("URL [" + var0 + "] has existed in system class loader"));
                     }

                     return true;
                  }
               }
            }

            Method var9 = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            var9.setAccessible(true);
            var9.invoke(var2, var0);
            if (var1) {
               Verbose.log((Object)("URL [" + var0 + "] is added into system class loader"));
            }

            return true;
         } catch (Exception var8) {
            if (var1) {
               Verbose.log((Object)("URL [" + var0 + "] failed to add into system class loader"));
               Verbose.logException(var8);
            }

            return false;
         }
      }
   }

   public static Class loadClass(ClassLoader var0, ClassLoader var1, String var2) throws ClassNotFoundException {
      DelegatingLoader var3 = new DelegatingLoader(var1, var0);
      return var3.loadClass(var2);
   }

   public static URL getResource(ClassLoader var0, ClassLoader var1, String var2) throws ClassNotFoundException {
      DelegatingLoader var3 = new DelegatingLoader(var1, var0);
      return var3.getResource(var2);
   }

   public static final class DelegatingLoader extends ClassLoader {
      private final ClassLoader loader;

      public DelegatingLoader(ClassLoader var1, ClassLoader var2) {
         super(var2);
         this.loader = var1;
      }

      protected Class findClass(String var1) throws ClassNotFoundException {
         return this.loader.loadClass(var1);
      }

      protected URL findResource(String var1) {
         return this.loader.getResource(var1);
      }
   }
}
