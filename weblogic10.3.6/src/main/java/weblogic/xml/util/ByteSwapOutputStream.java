package weblogic.xml.util;

import java.io.IOException;
import java.io.OutputStream;

class ByteSwapOutputStream extends OutputStream {
   private OutputStream out;
   private int byte1;

   public ByteSwapOutputStream(OutputStream var1) {
      this.out = var1;
      this.byte1 = -2;
   }

   public void write(int var1) throws IOException {
      if (this.byte1 == -2) {
         this.byte1 = var1;
      } else {
         this.out.write(var1);
         this.out.write(this.byte1);
         this.byte1 = -2;
      }

   }

   public void close() throws IOException {
      if (this.byte1 != -2) {
         this.out.write(0);
         this.out.write(this.byte1);
      }

   }
}
