package weblogic.connector.work;

import weblogic.connector.common.Debug;

public class Gid {
   byte[] bytes;
   int hc;
   int len;
   private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public Gid(byte[] var1) {
      boolean var2 = Debug.isXAworkEnabled();
      this.bytes = var1;
      this.len = this.bytes.length;
      if (var2) {
         Debug.xaWork("Gid( arr ) len = " + this.len);
      }

      for(int var3 = 0; var3 < this.len; ++var3) {
         this.hc += this.bytes[var3];
      }

      if (var2) {
         Debug.xaWork("Gid() hashcode for " + var1 + " is = " + this.hc);
      }

   }

   public boolean equals(Object var1) {
      if (var1 instanceof Gid && var1 != null) {
         byte[] var2 = ((Gid)var1).bytes;
         if (var2.length == this.len) {
            for(int var3 = 0; var3 < this.len; ++var3) {
               if (var2[var3] != this.bytes[var3]) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return this.hc;
   }

   public String toString() {
      return byteArrayToString(this.bytes);
   }

   private static String byteArrayToString(byte[] var0) {
      if (var0 == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(DIGITS[(var0[var2] & 240) >>> 4]);
            var1.append(DIGITS[var0[var2] & 15]);
         }

         return var1.toString();
      }
   }
}
