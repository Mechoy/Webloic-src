package weblogic.marathon.fs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class MemoryEntry extends Entry {
   byte[] data;

   public MemoryEntry(String var1, byte[] var2) {
      super(var1);
      this.data = var2;
   }

   public long getTime() {
      return System.currentTimeMillis();
   }

   public Entry[] list() {
      return new Entry[0];
   }

   public InputStream getInputStream() {
      return new ByteArrayInputStream(this.data);
   }
}
