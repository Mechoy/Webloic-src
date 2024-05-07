package weblogic.marathon.fs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class JarEntry extends Entry implements EntryAddable {
   ZipEntry ze;
   ZipFile zf;
   Entry[] children;

   public String toString() {
      return "[JarEntry path=" + this.getPath() + "]";
   }

   public long getTime() {
      return this.ze.getTime();
   }

   public JarEntry(String var1, ZipFile var2, ZipEntry var3) {
      super(var1);
      this.zf = var2;
      this.ze = var3;
      this.children = new Entry[0];
   }

   public void addEntry(Entry var1) {
      if (!ZU.containsEntry(this, var1)) {
         Entry[] var2 = new Entry[this.children.length + 1];
         System.arraycopy(this.children, 0, var2, 0, this.children.length);
         var2[this.children.length] = var1;
         this.children = var2;
      }
   }

   public String getPath() {
      return ZU.fixPath(super.getPath());
   }

   public Entry[] list() {
      return this.children;
   }

   public InputStream getInputStream() throws IOException {
      return this.zf.getInputStream(this.ze);
   }
}
