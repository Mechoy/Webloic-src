package weblogic.xml.security.encryption;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CipherWrapperInputStream extends InputStream {
   private static final int DEFAULT_BUFFER_SIZE = 2048;
   private final CipherWrapper cipher;
   private final InputStream source;
   private final int blockLength;
   private final int bufferLength;
   private final byte[] buffer;
   private int boff;
   private int blen;
   private int threshold;
   private static final Logger LOGGER = Logger.getLogger(CipherWrapperInputStream.class.getName());

   public CipherWrapperInputStream(InputStream var1, CipherWrapper var2) {
      this(var1, var2, 2048);
   }

   public CipherWrapperInputStream(InputStream var1, CipherWrapper var2, int var3) {
      this.source = var1;
      this.cipher = var2;
      this.blockLength = var2.getBlockSize();
      this.bufferLength = Math.max(this.blockLength * 2, var3);
      this.buffer = new byte[var3];
   }

   public int read() throws IOException {
      byte[] var1 = new byte[1];
      this.read(var1, 0, 1);
      return var1[0];
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.blen == -1) {
         return -1;
      } else {
         int var4;
         if (this.boff >= this.threshold) {
            for(var4 = 0; var4 < this.blen; ++var4) {
               this.buffer[var4] = this.buffer[this.boff + var4];
            }

            this.boff = 0;
            var4 = this.bufferLength - this.blen;
            int var5 = this.source.read(this.buffer, this.blen, var4);
            if (var5 == -1) {
               try {
                  this.cipher.doFinal(this.buffer, this.blen);
               } catch (EncryptionException var8) {
                  throwIOException(var8);
               }

               if (this.blen == 0) {
                  return -1;
               }

               byte var6 = this.buffer[this.blen - 1];
               int var7 = this.blen - var6;
               if (var7 <= 0) {
                  return -1;
               }

               var7 = Math.min(var7, var3);
               System.arraycopy(this.buffer, this.boff, var1, var2, var7);
               this.boff += var7;
               if (this.blen - this.boff == var6) {
                  this.blen = -1;
               } else {
                  this.blen -= var7;
               }

               return var7;
            }

            try {
               this.cipher.update(this.buffer, this.blen, var5, this.buffer, this.blen);
            } catch (EncryptionException var9) {
               throwIOException(var9);
            }

            this.blen += var5;
            this.threshold = this.blen - this.blockLength;
            if (this.threshold < 0) {
               this.threshold = 0;
               return 0;
            }
         }

         var4 = Math.min(this.threshold - this.boff, var3);
         System.arraycopy(this.buffer, this.boff, var1, var2, var4);
         this.boff += var4;
         this.blen -= var4;
         return var4;
      }
   }

   private static void throwIOException(EncryptionException var0) throws IOException {
      String var1 = "Found a cipher exception";
      Throwable var2 = var0.getCause();
      if (var2 != null) {
         if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, var2.getMessage());
         }

         throw new IOException(var1);
      } else {
         if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, var0.getMessage());
         }

         throw new IOException(var1);
      }
   }
}
