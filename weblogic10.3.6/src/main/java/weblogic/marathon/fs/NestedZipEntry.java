package weblogic.marathon.fs;

import java.io.IOException;
import java.io.InputStream;

class NestedZipEntry extends Entry {
   Entry delegate;

   static void p(String var0) {
      System.err.println("[NestedZipEntry]: " + var0);
   }

   public long getTime() {
      return this.delegate.getTime();
   }

   public NestedZipEntry(Entry var1, String var2) {
      super(var2);
      this.delegate = var1;
   }

   public Entry[] list() {
      Entry[] var1 = this.delegate.list();
      if (var1 != null && var1.length != 0) {
         NestedZipEntry[] var2 = new NestedZipEntry[var1.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = new NestedZipEntry(var1[var3], this.path);
         }

         return var2;
      } else {
         return var1;
      }
   }

   public String getPath() {
      String var1 = this.delegate.getPath();
      String var2 = var1.substring(this.path.length());
      return var2;
   }

   public InputStream getInputStream() throws IOException {
      return this.delegate.getInputStream();
   }
}
