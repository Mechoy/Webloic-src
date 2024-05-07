package weblogic.connector.deploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import weblogic.connector.common.Debug;
import weblogic.utils.classloaders.Source;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import weblogic.utils.jars.VirtualJarFile;

public final class RASource implements Source {
   private ZipEntry ze = null;
   private String className = null;
   private VirtualJarFile vjar = null;

   public RASource(VirtualJarFile var1, ZipEntry var2, String var3) {
      this.vjar = var1;
      this.ze = var2;
      this.className = var3;
   }

   public byte[] getBytes() throws IOException {
      return getClassBytes(this.vjar, this.ze, this.className);
   }

   public InputStream getInputStream() throws IOException {
      return this.vjar.getInputStream(this.ze);
   }

   public URL getURL() {
      URL var3 = null;

      try {
         String var1;
         String var2;
         if (this.vjar.isDirectory()) {
            var2 = this.vjar.getName().replace(File.separatorChar, '/');
            if (!var2.endsWith("/")) {
               var2 = var2 + '/';
            }

            var1 = var2 + this.ze.getName() + '!' + '/' + this.className;
            var3 = new URL("zip", "", var1);
         } else {
            var2 = this.vjar.getName().replace(File.separatorChar, '/');
            var1 = "rar://" + var2 + '!' + this.ze.getName() + '!' + '/' + this.className;
            var3 = new URL((URL)null, var1, new RAStreamHandler(this.vjar, this.ze, this.className));
         }
      } catch (MalformedURLException var5) {
         debug("Caught MalformedURLException while trying to create a URL in getURL :" + var5.getMessage());
      } catch (Exception var6) {
         debug("Caught general Exception while trying to create a URL in getURL :" + var6.getMessage());
      }

      return var3;
   }

   public URL getCodeSourceURL() {
      URL var1 = null;

      try {
         File var2 = new File(this.vjar.getName());
         var1 = var2.toURL();
      } catch (MalformedURLException var3) {
         debug("Caught MalformedURLException while trying to create a URL in getCoseSourceURL : " + var3.getMessage());
      }

      return var1;
   }

   public long lastModified() {
      return this.ze.getTime();
   }

   public long length() {
      return this.ze.getSize();
   }

   private static void debug(String var0) {
      if (Debug.isClassLoadingEnabled()) {
         Debug.classloading(var0);
      }

   }

   protected static byte[] getClassBytes(VirtualJarFile var0, ZipEntry var1, String var2) throws IOException {
      byte[] var3 = new byte[512];
      UnsyncByteArrayOutputStream var4 = null;
      ZipInputStream var5 = null;
      boolean var6 = false;
      boolean var7 = false;

      try {
         var5 = new ZipInputStream(var0.getInputStream(var1));

         while(!var6 && !var7) {
            ZipEntry var8 = var5.getNextEntry();
            if (var8 == null) {
               var7 = true;
            } else if (var8.getName().equals(var2)) {
               var4 = new UnsyncByteArrayOutputStream();

               int var9;
               while((var9 = var5.read(var3, 0, var3.length)) != -1) {
                  var4.write(var3, 0, var9);
               }

               var3 = var4.toByteArray();
               var6 = true;
            }
         }
      } catch (IOException var22) {
         if (Debug.isClassLoadingEnabled()) {
            Debug.classloading("Caught IOException while trying to read the bytes of resource : " + var2 + " in zip entry: " + var1.getName() + " in vjar: " + var0.getName() + " exception: " + var22.getMessage());
         }

         throw var22;
      } catch (Exception var23) {
         if (Debug.isClassLoadingEnabled()) {
            Debug.classloading("Caught unkown Exception while trying to read the bytes of resource : " + var2 + " in zip entry: " + var1.getName() + " in vjar: " + var0.getName() + " exception: " + var23.getMessage());
         }

         throw (IOException)((IOException)(new IOException()).initCause(var23));
      } finally {
         try {
            var4.close();
         } catch (Exception var21) {
            if (Debug.isClassLoadingEnabled()) {
               Debug.classloading("Caught Exception while closing outputstream : " + var2 + " in zip entry: " + var1.getName() + " in vjar: " + var0.getName() + " exception: " + var21.getMessage());
            }
         }

         try {
            var5.close();
         } catch (Exception var20) {
            if (Debug.isClassLoadingEnabled()) {
               Debug.classloading("Caught Exception while closing zip input stream : " + var2 + " in zip entry: " + var1.getName() + " in vjar: " + var0.getName() + " exception: " + var20.getMessage());
            }
         }

      }

      return var3;
   }

   static final class RAStreamHandler extends URLStreamHandler {
      private final ZipEntry zipEntry;
      private final VirtualJarFile vjar;
      private final String className;

      public RAStreamHandler(VirtualJarFile var1, ZipEntry var2, String var3) {
         this.zipEntry = var2;
         this.vjar = var1;
         this.className = var3;
      }

      protected URLConnection openConnection(URL var1) {
         RarURLConnection var2 = new RarURLConnection(var1, this.vjar, this.zipEntry, this.className);
         return var2;
      }

      final class RarURLConnection extends URLConnection {
         private final ZipEntry zipEntry;
         private final VirtualJarFile vjar;
         private final String className;

         RarURLConnection(URL var2, VirtualJarFile var3, ZipEntry var4, String var5) {
            super(var2);
            this.zipEntry = var4;
            this.vjar = var3;
            this.className = var5;
         }

         public void connect() throws IOException {
         }

         public InputStream getInputStream() throws IOException {
            byte[] var1 = RASource.getClassBytes(this.vjar, this.zipEntry, this.className);
            return new UnsyncByteArrayInputStream(var1);
         }
      }
   }
}
