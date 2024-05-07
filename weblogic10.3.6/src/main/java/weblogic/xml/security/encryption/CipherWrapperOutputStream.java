package weblogic.xml.security.encryption;

import java.io.IOException;
import java.io.OutputStream;

final class CipherWrapperOutputStream extends OutputStream {
   private final OutputStream dest;
   private final int blockSize;
   private final CipherWrapper cipher;
   private int cnt = 0;
   private int n = 0;

   CipherWrapperOutputStream(OutputStream var1, CipherWrapper var2) {
      this.dest = var1;
      this.blockSize = var2.getBlockSize();
      this.cipher = var2;
   }

   public void write(int var1) throws IOException {
      this.dest.write(new byte[]{(byte)var1});
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var1.length < this.blockSize) {
         byte[] var4 = new byte[this.blockSize];
         System.arraycopy(var1, 0, var4, 0, var1.length);
         var1 = var4;
      }

      int var5 = this.cipher.update(var1, var2, var3, var1, var2);
      this.dest.write(var1, var2, var5);
      this.cnt += var5;
      this.n += var3;
   }

   public void flush() throws IOException {
      int var1 = this.blockSize - this.n % this.blockSize;
      byte[] var2 = new byte[var1];
      var2[var1 - 1] = (byte)var1;
      this.write(var2, 0, var2.length);
      this.dest.write(this.cipher.doFinal());
      this.dest.flush();
   }

   public void close() throws IOException {
      this.flush();
   }
}
