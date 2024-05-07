package weblogic.jms.utils.tracing;

import java.nio.ByteBuffer;

public class SubBuffer {
   private ByteBuffer buffer;
   private int offset;
   private int length;

   public SubBuffer(ByteBuffer var1, int var2, int var3) {
      this.buffer = var1;
      this.offset = var2;
      this.length = var3;
   }

   public void putInt(int var1, int var2) {
      if (var1 > this.length) {
         throw new AssertionError("Yup");
      } else {
         this.buffer.putInt(this.offset + var1, var2);
      }
   }

   public int getInt(int var1) {
      if (var1 > this.length) {
         throw new AssertionError("Yup");
      } else {
         return this.buffer.getInt(this.offset + var1);
      }
   }

   public String toString() {
      if (this.length == 0) {
         return null;
      } else {
         int var1 = this.length / 2;
         char[] var2 = new char[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = this.buffer.getChar(this.offset + var3 * 2);
         }

         return new String(var2, 0, var1 - 1);
      }
   }

   public int limit() {
      return this.length;
   }
}
