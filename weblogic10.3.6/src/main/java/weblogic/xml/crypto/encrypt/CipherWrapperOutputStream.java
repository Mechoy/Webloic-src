package weblogic.xml.crypto.encrypt;

import java.io.IOException;
import java.io.OutputStream;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

final class CipherWrapperOutputStream extends OutputStream {
   private final OutputStream dest;
   private final int blockSize;
   private final CipherWrapper cipher;
   private boolean flushed = false;
   private byte[] buf;
   private int n = 0;

   CipherWrapperOutputStream(OutputStream var1, CipherWrapper var2) {
      this.dest = var1;
      this.blockSize = var2.getBlockSize();
      this.cipher = var2;
      this.buf = null;
   }

   public void write(int var1) throws IOException {
      this.dest.write(new byte[]{(byte)var1});
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 <= var1.length && var2 + var3 >= 0) {
         if (var3 != 0) {
            int var4 = var3 % this.blockSize;
            if (var4 == 0 && this.buf == null) {
               this.encryptAndUpdate(var1, var2, var3);
            } else if (var4 != 0 && this.buf == null) {
               this.buf = new byte[var4];
               System.arraycopy(var1, var3 - var4, this.buf, 0, var4);
               this.encryptAndUpdate(var1, var2, var3 - var4);
            } else {
               byte[] var5;
               if (this.buf.length + var3 > this.blockSize) {
                  var4 = (this.buf.length + var3) % this.blockSize;
                  var5 = new byte[var4];
                  System.arraycopy(var1, var3 - var4, var5, 0, var4);
                  byte[] var6 = new byte[this.buf.length + var3 - var4];
                  System.arraycopy(this.buf, 0, var6, 0, this.buf.length);
                  System.arraycopy(var1, var2, var6, this.buf.length, var6.length - this.buf.length);
                  this.buf = var5;
                  this.encryptAndUpdate(var6, 0, var6.length);
               } else if (this.buf.length + var3 == this.blockSize) {
                  var5 = new byte[this.blockSize];
                  System.arraycopy(this.buf, 0, var5, 0, this.buf.length);
                  System.arraycopy(var1, var2, var5, this.buf.length, var3);
                  this.buf = null;
                  this.encryptAndUpdate(var5, 0, this.blockSize);
               } else {
                  var5 = new byte[this.buf.length + var3];
                  System.arraycopy(this.buf, 0, var5, 0, this.buf.length);
                  System.arraycopy(var1, var2, var5, this.buf.length, var3);
                  this.buf = var5;
               }
            }

         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void flush() throws IOException {
      if (!this.flushed) {
         if (this.buf != null) {
            this.encryptAndUpdate(this.buf, 0, this.buf.length);
            this.buf = null;
         }

         int var1 = this.blockSize - this.n % this.blockSize;
         byte[] var2 = new byte[var1];
         var2[var1 - 1] = (byte)var1;
         if (var2.length < this.blockSize) {
            byte[] var3 = new byte[this.blockSize];
            System.arraycopy(var2, 0, var3, 0, var2.length);
            var2 = var3;
         }

         this.encryptAndUpdate(var2, 0, var1);

         try {
            this.dest.write(this.cipher.doFinal());
         } catch (XMLEncryptionException var4) {
            throw new IOException(var4.getLocalizedMessage());
         }

         this.dest.flush();
         this.flushed = true;
      }
   }

   public void close() throws IOException {
      if (!this.flushed) {
         this.flush();
      }

   }

   private void encryptAndUpdate(byte[] var1, int var2, int var3) throws IOException {
      boolean var4 = false;

      int var7;
      try {
         var7 = this.cipher.update(var1, var2, var3, var1, var2);
      } catch (XMLEncryptionException var6) {
         throw new IOException(var6.getLocalizedMessage());
      }

      this.dest.write(var1, var2, var7);
      this.n += var3;
   }
}
