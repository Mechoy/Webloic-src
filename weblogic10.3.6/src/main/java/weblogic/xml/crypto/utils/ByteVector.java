package weblogic.xml.crypto.utils;

import java.io.IOException;
import java.io.InputStream;

public class ByteVector {
   byte[] array;
   int count;

   public ByteVector() {
      this(8);
   }

   public ByteVector(int var1) {
      this.array = new byte[var1];
      this.count = 0;
   }

   public int count() {
      return this.count;
   }

   public int size() {
      return this.count;
   }

   public void setCount(int var1) {
      this.count = var1;
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public void addElements(byte[] var1) {
      if (var1 != null) {
         int var2 = var1.length;
         if (this.array == null || this.count + var2 >= this.array.length) {
            this.ensureCapacity(this.count + var2);
         }

         System.arraycopy(var1, 0, this.array, this.count, var2);
         this.count += var2;
      }
   }

   public void addElements(InputStream var1) throws IOException {
      while(true) {
         int var2 = var1.available();
         if (this.array.length < this.count + var2) {
            this.ensureCapacity(this.count + var2);
         }

         if (var2 == 0) {
            var2 = 1;
         }

         int var3 = var1.read(this.array, this.count, var2);
         if (var3 < 0) {
            return;
         }

         this.count += var3;
      }
   }

   public byte[] elementArray() {
      return this.array;
   }

   public byte[] minSizedElementArray() {
      byte[] var1 = new byte[this.count];
      System.arraycopy(this.array, 0, var1, 0, this.count);
      return var1;
   }

   public void ensureCapacity(int var1) {
      if (this.array == null) {
         this.array = new byte[8];
      }

      if (var1 >= this.array.length) {
         int var2;
         if (this.array.length < 8) {
            var2 = 8;
         } else {
            var2 = this.array.length;
         }

         while(var2 < var1) {
            var2 = 2 * var2;
         }

         byte[] var3 = new byte[var2];
         System.arraycopy(this.array, 0, var3, 0, this.count);
         this.array = var3;
      }
   }

   public int capacity() {
      return this.array == null ? 0 : this.array.length;
   }

   public void removeAllElements() {
      this.count = 0;
   }
}
