package weblogic.diagnostics.image;

import java.io.IOException;
import java.io.OutputStream;

class ImageSourceOutputStream extends OutputStream {
   private OutputStream out;
   private boolean streamExpired = false;

   ImageSourceOutputStream(OutputStream var1) {
      this.out = var1;
   }

   public void close() {
      this.streamExpired = true;
   }

   public void flush() throws IOException {
      this.checkExpired();
      synchronized(this.out) {
         this.out.flush();
      }
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.checkExpired();
      synchronized(this.out) {
         this.out.write(var1, var2, var3);
      }
   }

   public void write(int var1) throws IOException {
      this.checkExpired();
      synchronized(this.out) {
         this.out.write(var1);
      }
   }

   private void checkExpired() throws IOException {
      if (this.streamExpired) {
         throw new IOException("Stream no longer writable.");
      }
   }
}
