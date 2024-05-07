package weblogic.common.internal;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import weblogic.application.AppClassLoaderManager;
import weblogic.j2ee.ApplicationManager;
import weblogic.kernel.Kernel;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;

public class ProxyClassResolver {
   private static final boolean DEBUG = false;
   private static final boolean isServer = Kernel.isServer();
   private static final boolean isApplet = Kernel.isApplet();
   private static final ClassLoader sysClassLoader;

   public static Class resolveProxyClass(String[] var0) throws IOException, ClassNotFoundException {
      return resolveProxyClass(var0, (String)null, (String)null);
   }

   public static Class resolveProxyClass(String[] var0, String var1, String var2) throws IOException, ClassNotFoundException {
      return resolveProxyClass(var0, var1, var2, false);
   }

   public static Class resolveProxyClass(String[] var0, String var1, String var2, boolean var3) throws IOException, ClassNotFoundException {
      ClassLoader var4 = null;
      ClassLoader var5 = Thread.currentThread().getContextClassLoader();
      ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < var0.length; ++var7) {
         Class var8 = null;

         try {
            var8 = ApplicationManager.loadClass(var0[var7], var1, var2, var5, var3);
         } catch (ClassNotFoundException var11) {
         }

         if (var8 == null && !var3) {
            try {
               var8 = AppClassLoaderManager.getAppClassLoaderManager().loadApplicationClass(new Annotation(var1), var0[var7]);
            } catch (ClassNotFoundException var12) {
               if (isServer) {
                  throw var12;
               }
            }
         }

         if (var8 != null) {
            var6.add(var8);
            if (var4 == null && var8.getClassLoader() instanceof GenericClassLoader) {
               var4 = var8.getClassLoader();
            }
         }
      }

      if (var4 == null) {
         var4 = getProxyLoader();
      }

      Class[] var13 = new Class[var6.size()];
      var13 = (Class[])((Class[])var6.toArray(var13));

      try {
         return Proxy.getProxyClass(var4, var13);
      } catch (IllegalArgumentException var10) {
         throw new ClassNotFoundException((String)null, var10);
      }
   }

   private static ClassLoader getProxyLoader() {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();
      if (var0 == null) {
         var0 = sysClassLoader;
      }

      return var0;
   }

   static {
      if (isApplet) {
         sysClassLoader = null;
      } else {
         sysClassLoader = ClassLoader.getSystemClassLoader();
      }

   }
}
