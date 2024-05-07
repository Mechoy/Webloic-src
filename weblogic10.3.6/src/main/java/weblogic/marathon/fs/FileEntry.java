package weblogic.marathon.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class FileEntry extends Entry {
   File f;

   FileEntry(String var1, File var2) {
      super(var1);
      this.f = var2;
   }

   public long getTime() {
      return this.f.lastModified();
   }

   public Entry[] list() {
      if (!this.f.isDirectory()) {
         return new Entry[0];
      } else {
         File[] var1 = this.f.listFiles();
         if (var1 != null && var1.length != 0) {
            Entry[] var2 = new Entry[var1.length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
               String var4 = null;
               if (!"".equals(this.getPath())) {
                  var4 = this.getPath() + var1[var3].getName();
               } else {
                  var4 = var1[var3].getName();
               }

               if (var1[var3].isDirectory()) {
                  var4 = var4 + "/";
               }

               var2[var3] = new FileEntry(var4, var1[var3]);
            }

            return var2;
         } else {
            return new Entry[0];
         }
      }
   }

   public InputStream getInputStream() throws IOException {
      return new FileInputStream(this.f);
   }
}
