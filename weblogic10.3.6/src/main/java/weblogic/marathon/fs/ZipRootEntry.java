package weblogic.marathon.fs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipFile;

class ZipRootEntry extends Entry implements EntryAddable {
   ZipFile zf;
   List l;
   Entry[] entries;

   public ZipRootEntry(ZipFile var1) {
      super("");
      this.zf = var1;
      this.entries = new Entry[0];
   }

   public long getTime() {
      return System.currentTimeMillis();
   }

   public Entry[] list() {
      return this.entries;
   }

   public void addEntry(Entry var1) {
      if (!ZU.containsEntry(this, var1)) {
         Entry[] var2 = new Entry[this.entries.length + 1];
         System.arraycopy(this.entries, 0, var2, 0, this.entries.length);
         var2[this.entries.length] = var1;
         this.entries = var2;
      }
   }

   public InputStream getInputStream() throws IOException {
      throw new IllegalArgumentException("no input for this entry");
   }
}
