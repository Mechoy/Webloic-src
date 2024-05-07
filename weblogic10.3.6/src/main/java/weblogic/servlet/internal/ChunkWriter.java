package weblogic.servlet.internal;

import java.io.IOException;
import java.io.Writer;

public final class ChunkWriter extends Writer {
   private boolean error = false;
   private ChunkOutputWrapper co;

   public ChunkWriter(ChunkOutputWrapper var1) {
      this.co = var1;
   }

   public String getEncoding() {
      String var1 = this.co.getEncoding();
      if (var1 == null) {
         var1 = "ISO-8859-1";
      }

      return var1;
   }

   public void write(int var1) throws IOException {
      if (!this.error) {
         try {
            this.co.write(var1);
         } catch (IOException var3) {
            this.error = true;
            throw var3;
         }
      }
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      if (!this.error) {
         try {
            this.co.write(var1, var2, var3);
         } catch (IOException var5) {
            this.error = true;
            throw var5;
         }
      }
   }

   public void write(String var1) throws IOException {
      if (!this.error) {
         try {
            this.co.print(var1);
         } catch (IOException var3) {
            this.error = true;
            throw var3;
         }
      }
   }

   public void flush() throws IOException {
      if (!this.error) {
         try {
            this.co.flush();
         } catch (IOException var2) {
            this.error = true;
            throw var2;
         }
      }
   }

   public void close() throws IOException {
   }
}
