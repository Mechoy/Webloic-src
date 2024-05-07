package weblogic.xml.util;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import weblogic.utils.io.ChunkedInputStream;
import weblogic.utils.io.ChunkedOutputStream;

public class CachedInputStream extends FilterInputStream {
   boolean isUserStream = true;
   boolean streamclose = false;
   ChunkedOutputStream output = new ChunkedOutputStream();

   public CachedInputStream(InputStream var1) throws IOException {
      super(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.isUserStream) {
         int var4 = super.read(var1, var2, var3);
         if (var4 != -1) {
            this.output.write(var1, var2, var4);
         }

         return var4;
      } else {
         return super.read(var1, var2, var3);
      }
   }

   public int read() throws IOException {
      if (this.isUserStream) {
         int var1 = super.read();
         if (var1 != -1) {
            this.output.write(var1);
         }

         return var1;
      } else {
         return super.read();
      }
   }

   public void reset() throws IOException {
      if (this.isUserStream) {
         this.readFullstream();
         this.output.flush();
         this.output.close();
         this.in = new ChunkedInputStream(this.output.getChunks(), 0);
         this.in.mark(0);
         this.output = null;
         this.isUserStream = false;
      } else {
         this.in.reset();
         this.in.mark(0);
      }

      this.streamclose = false;
   }

   private void readFullstream() throws IOException {
      try {
         if (this.streamclose) {
            return;
         }

         byte[] var1 = new byte[512];

         while(true) {
            if (this.read(var1) != -1) {
               continue;
            }
         }
      } catch (EOFException var2) {
      }

   }

   public void close() throws IOException {
      if (this.isUserStream) {
         this.readFullstream();
         this.in.close();
      }

      this.streamclose = true;
   }

   public void closeAll() throws IOException {
      if (this.isUserStream) {
         this.close();
      }

      this.reset();
      this.in.close();
   }
}
