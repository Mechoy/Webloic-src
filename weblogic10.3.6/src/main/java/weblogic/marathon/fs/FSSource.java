package weblogic.marathon.fs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import weblogic.utils.classloaders.Source;
import weblogic.utils.zip.Handler;

public class FSSource implements Source {
   Entry e;
   FS fs;

   public FSSource(FS var1, Entry var2) {
      this.fs = var1;
      this.e = var2;
   }

   public byte[] getBytes() {
      try {
         InputStream var1 = this.e.getInputStream();
         byte[] var2 = new byte[2048];
         boolean var3 = false;
         int var4 = 0;

         byte[] var5;
         int var7;
         while((var7 = var1.read(var2, var4, var2.length - var4)) > 0) {
            var4 += var7;
            if (var4 == var2.length) {
               var5 = new byte[var4 * 2];
               System.arraycopy(var2, 0, var5, 0, var4);
               var2 = var5;
            }
         }

         var5 = new byte[var4];
         System.arraycopy(var2, 0, var5, 0, var4);
         return var5;
      } catch (IOException var6) {
         throw new RuntimeException("nested IO: " + var6.toString());
      }
   }

   public URL getCodeSourceURL() {
      return null;
   }

   public InputStream getInputStream() throws IOException {
      return this.e.getInputStream();
   }

   public URL getURL() {
      try {
         return this.fs.getURL(this.e.getPath());
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public long lastModified() {
      return -1L;
   }

   public long length() {
      return -1L;
   }

   static {
      Handler.init();
   }
}
