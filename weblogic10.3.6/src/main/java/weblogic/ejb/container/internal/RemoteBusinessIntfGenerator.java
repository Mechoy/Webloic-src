package weblogic.ejb.container.internal;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import weblogic.ejb.container.deployer.DownloadRemoteBizIntfClassLoader;
import weblogic.ejb.container.deployer.RemoteBizIntfClassLoader;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.GenericClassLoader;

/** @deprecated */
@Deprecated
public final class RemoteBusinessIntfGenerator {
   private String remoteInterfaceName;
   private Class businessInterface;
   private Class generatedRemoteInterface;
   private GenericClassLoader cl;
   private static final Map<LoaderCacheKey, WeakReference> classLoaderCache = new HashMap();
   private static boolean debug = Boolean.getBoolean("weblogic.ejb.enhancement.debug");

   public RemoteBusinessIntfGenerator(String var1, Class var2, GenericClassLoader var3) {
      this.remoteInterfaceName = var1;
      this.businessInterface = var2;
      this.cl = var3;
   }

   public Class getGeneratedRemoteInterface() {
      return this.generatedRemoteInterface;
   }

   public Class generateRemoteInterface() {
      boolean var1 = false;
      Method[] var2 = this.businessInterface.getMethods();
      Method[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         if (!var6.toGenericString().equals(var6.toString()) && this.isGenericInterfaceDeclared(this.businessInterface)) {
            var1 = true;
            break;
         }
      }

      String var9 = this.businessInterface.getName();
      Class var12;
      if (!var1) {
         try {
            RemoteBizIntfClassLoader var11 = this.getEnhanceLoader();
            var12 = var11.loadClass(var9);
            this.debug("loaded by enhanceCL:" + var12.getName());
            if (var12.getName().equals(this.remoteInterfaceName)) {
               this.generatedRemoteInterface = var12;
               this.debug("get generatedRemoteInterface:" + this.generatedRemoteInterface + " by enhancing business interface on RemoteBizIntfClassLoader: \n" + var11);
            } else {
               this.generatedRemoteInterface = var11.loadClass(this.remoteInterfaceName);
               this.debug("get generatedRemoteInterface:" + this.generatedRemoteInterface + " by loading it directly on RemoteBizIntfClassLoader: \n" + var11);
            }

            return this.generatedRemoteInterface;
         } catch (ClassNotFoundException var7) {
            throw new AssertionError(var7);
         }
      } else {
         DownloadRemoteBizIntfClassLoader var10 = null;

         try {
            var10 = new DownloadRemoteBizIntfClassLoader(this.remoteInterfaceName, this.cl);
            var12 = var10.loadClass(var9);
            if (var12.getName().equals(this.remoteInterfaceName)) {
               this.generatedRemoteInterface = var12;
               this.debug("get generatedRemoteInterface:" + this.generatedRemoteInterface + " by enhancing business interface on RemoteBizIntfClassLoader: \n" + var10);
            } else {
               this.generatedRemoteInterface = var10.loadClass(this.remoteInterfaceName);
               this.debug("get generatedRemoteInterface:" + this.generatedRemoteInterface + " by loading it directly on RemoteBizIntfClassLoader: \n" + var10);
            }

            return this.generatedRemoteInterface;
         } catch (ClassNotFoundException var8) {
            throw new AssertionError(var8);
         }
      }
   }

   private RemoteBizIntfClassLoader getEnhanceLoader() {
      if (debug) {
         this.debug("enhance loader cache: " + classLoaderCache);
      }

      this.debug("businessInterface: " + this.businessInterface.getName() + " and generated interface: " + this.remoteInterfaceName);
      LoaderCacheKey var1 = new LoaderCacheKey(this.businessInterface.getName(), this.remoteInterfaceName, this.cl);
      RemoteBizIntfClassLoader var2 = null;
      synchronized(classLoaderCache) {
         WeakReference var4 = (WeakReference)classLoaderCache.get(var1);
         if (var4 != null && var4.get() != null) {
            var2 = (RemoteBizIntfClassLoader)var4.get();
            this.debug("got RemoteBizIntfClassLoader from cache: " + var2);
         } else {
            var2 = new RemoteBizIntfClassLoader(this.businessInterface.getName(), this.remoteInterfaceName, this.cl);
            var4 = new WeakReference(var2);
            classLoaderCache.put(var1, var4);
            if (debug) {
               this.debug("newly created RemoteBizIntfClassLoader: " + var2 + " enhanceLoaderCache: " + classLoaderCache);
            }
         }

         return var2;
      }
   }

   private boolean isGenericInterfaceDeclared(Type var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Class)) {
         return true;
      } else {
         Class var2 = (Class)var1;
         if (!var2.isInterface()) {
            return false;
         } else {
            Type[] var3 = var2.getGenericInterfaces();
            if (var3.length == 0) {
               return var2.getTypeParameters().length != 0;
            } else {
               int var5 = var3.length;
               byte var6 = 0;
               if (var6 < var5) {
                  Type var7 = var3[var6];
                  return this.isGenericInterfaceDeclared(var7);
               } else {
                  return false;
               }
            }
         }
      }
   }

   private static void dumpIfaceInfo(Class var0) {
      System.out.println("iface name: " + var0.getName());
      System.out.println("isInterface: " + var0.isInterface());
      System.out.println("super: " + var0.getSuperclass());
      Class[] var1 = var0.getInterfaces();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         System.out.println("implemented interface: " + var1[var2]);
      }

      Method[] var4 = var0.getMethods();

      for(int var3 = 0; var3 < var4.length; ++var3) {
         System.out.println("Method: " + var4[var3]);
      }

      System.out.println("\n");
   }

   public static void main(String[] var0) {
      if (var0.length != 1) {
         System.out.println("Usage: java RemoteBusinessIntfGenerator <remote-business-interface-name>");
      } else {
         ClassLoader var1 = Thread.currentThread().getContextClassLoader();

         try {
            String var2 = var0[0];
            Class var3 = var1.loadClass(var2);
            GenericClassLoader var4 = AugmentableClassLoaderManager.getAugmentableClassLoader(var1);
            RemoteBusinessIntfGenerator var5 = new RemoteBusinessIntfGenerator(var2 + "Remote", var3, var4);
            Class var6 = var5.generateRemoteInterface();
            dumpIfaceInfo(var6);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   private void debug(String var1) {
      if (debug) {
         System.out.println("[" + this.getClass().getSimpleName() + "]" + "---" + Thread.currentThread() + "---" + "\n\t" + var1);
      }

   }

   private static class LoaderCacheKey {
      private String biName;
      private String remoteBIName;
      private GenericClassLoader appLoader;
      private final int hashCode;

      public LoaderCacheKey(String var1, String var2, GenericClassLoader var3) {
         if (var1 != null && var2 != null && var3 != null) {
            this.biName = var1;
            this.remoteBIName = var2;
            this.appLoader = var3;
            this.hashCode = var1.hashCode() ^ var2.hashCode() ^ var3.hashCode();
         } else {
            throw new AssertionError("Invalid loader cache key. business interface name: " + var1 + ", remote business interface name: " + var2 + ",\nGenericClassLoader: " + var3);
         }
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof LoaderCacheKey)) {
            return false;
         } else {
            LoaderCacheKey var2 = (LoaderCacheKey)var1;
            return this.biName.equals(var2.biName) && this.remoteBIName.equals(var2.remoteBIName) && this.appLoader.equals(var2.appLoader);
         }
      }

      public int hashCode() {
         return this.hashCode;
      }
   }
}
