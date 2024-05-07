package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import weblogic.servlet.proxy.WriteClientIOException;
import weblogic.utils.Hex;

public final class ChunkInput {
   private static void enforceEOL(InputStream var0) throws IOException {
      if (var0.read() != 13) {
         throw new IOException("chunk data not ended with CR");
      } else if (var0.read() != 10) {
         throw new IOException("chunk data not ended with LF");
      }
   }

   public static int readCTE(byte[] var0, InputStream var1) throws IOException {
      boolean var2 = false;
      int var3 = 0;
      int var4 = 0;

      int var6;
      while((var6 = readChunkSize(var1)) > 0) {
         int var5;
         do {
            var5 = var1.read(var0, var3, var6);
            var3 += var5;
            if (var3 > var0.length) {
               throw new IOException("Max buffer exceeded");
            }

            var6 -= var5;
         } while(var6 > 0);

         var4 += var5;
         enforceEOL(var1);
      }

      return var4;
   }

   public static int readCTE(OutputStream var0, InputStream var1) throws IOException {
      int var3 = 0;

      int var2;
      for(boolean var4 = true; (var2 = readChunkSize(var1)) > 0; var3 += var2 + 2) {
         if (readAndWriteChunk(var2, var0, var1, var4) == -1) {
            var4 = false;
         }
      }

      return var3;
   }

   public static int readChunkSize(InputStream var0) throws IOException {
      StringBuilder var1 = new StringBuilder();
      boolean var2 = false;

      int var3;
      while((var3 = var0.read()) != -1 && (var3 != 13 || (var3 = var0.read()) != 10)) {
         char var4 = (char)var3;
         if (Hex.isHexChar(var4)) {
            var1.append(var4);
         }
      }

      int var5 = Integer.parseInt(var1.toString(), 16);
      if (var5 == 0) {
         enforceEOL(var0);
      }

      return var5;
   }

   /** @deprecated */
   public static int readAndWriteChunk(int var0, OutputStream var1, InputStream var2) throws IOException {
      return readAndWriteChunk(var0, var1, var2, true);
   }

   public static int readAndWriteChunk(int var0, OutputStream var1, InputStream var2, boolean var3) throws IOException {
      int var4 = var0;
      boolean var5 = false;

      int var9;
      for(byte[] var6 = new byte[Math.min(var0, 8192)]; var4 > 0; var4 -= var9) {
         var9 = var2.read(var6, 0, Math.min(var6.length, var4));
         if (var9 == -1) {
            throw new IOException("unexpected EOF, expected to read: " + var0 + " actually read: " + var9);
         }

         if (var3) {
            try {
               var1.write(var6, 0, var9);
            } catch (IOException var8) {
               var3 = false;
               throw new WriteClientIOException("Error in writing to client");
            }
         }
      }

      enforceEOL(var2);
      if (!var3) {
         return -1;
      } else {
         return var0;
      }
   }
}
