package weblogic.servlet.jsp;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import weblogic.servlet.internal.ChunkOutputWrapper;

class BodyOutputStream extends ServletOutputStream {
   private ChunkOutputWrapper cow;

   BodyOutputStream(ChunkOutputWrapper var1) {
      this.cow = var1;
   }

   public void write(int var1) throws IOException {
      this.cow.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.cow.write(var1, var2, var3);
   }

   public void close() {
   }
}
