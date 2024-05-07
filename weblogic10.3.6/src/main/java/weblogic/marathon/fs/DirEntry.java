package weblogic.marathon.fs;

import java.io.IOException;
import java.io.InputStream;

class DirEntry extends Entry implements EntryAddable {
   Entry[] children = new Entry[0];

   public String toString() {
      return "[DirEntry path=" + this.getPath() + "]";
   }

   public DirEntry(String var1) {
      super(var1);
   }

   public Entry[] list() {
      return this.children;
   }

   public void addEntry(Entry var1) {
      if (!ZU.containsEntry(this, var1)) {
         Entry[] var2 = new Entry[this.children.length + 1];
         System.arraycopy(this.children, 0, var2, 0, this.children.length);
         var2[this.children.length] = var1;
         this.children = var2;
      }
   }

   public InputStream getInputStream() throws IOException {
      throw new IllegalArgumentException("no inputstream from directory");
   }

   public long getTime() {
      return System.currentTimeMillis();
   }
}
