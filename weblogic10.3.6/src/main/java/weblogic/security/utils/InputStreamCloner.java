package weblogic.security.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class InputStreamCloner {
   private static final int COPY_SIZE = 1024;
   private boolean copied = false;
   private InputStream original = null;
   private byte[] bytes = null;
   private int hashcode = 0;

   public InputStreamCloner(InputStream var1) {
      this.original = var1;
   }

   private synchronized void copyStream() throws IOException {
      if (!this.copied) {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         try {
            byte[] var2 = new byte[1024];

            int var3;
            while((var3 = this.original.read(var2)) != -1) {
               var1.write(var2, 0, var3);
            }

            this.bytes = var1.toByteArray();
            this.copied = true;
         } finally {
            var1.close();
         }
      }
   }

   public InputStream cloneStream() throws IOException {
      if (!this.copied) {
         this.copyStream();
      }

      return new ByteArrayInputStream(this.bytes);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && var1 instanceof InputStreamCloner) {
         InputStreamCloner var2 = (InputStreamCloner)var1;
         if (this.original == var2.original) {
            return true;
         } else {
            try {
               this.copyStream();
               var2.copyStream();
            } catch (IOException var4) {
               throw new RuntimeException(var4);
            }

            return Arrays.equals(this.bytes, var2.bytes);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashcode == 0) {
         try {
            this.copyStream();
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }

         if (this.bytes == null) {
            return 0;
         }

         int var1 = 1;

         for(int var2 = 0; var2 < this.bytes.length; ++var2) {
            var1 = var1 * 31 + this.bytes[var2];
         }

         this.hashcode = var1;
      }

      return this.hashcode;
   }
}
