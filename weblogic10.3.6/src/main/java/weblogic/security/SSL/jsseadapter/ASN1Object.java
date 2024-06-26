package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.math.BigInteger;

class ASN1Object {
   protected int type;
   protected int length;
   protected byte[] value;
   protected int tag;

   public ASN1Object(int var1, int var2, byte[] var3) {
      this.tag = var1;
      this.length = var2;
      this.value = var3;
      this.type = var1 % 32;
   }

   public int getLength() {
      return this.length;
   }

   public byte[] getValue() {
      return this.value;
   }

   public int getType() {
      return this.type;
   }

   public boolean isConstructed() {
      return (this.tag & 32) == 32;
   }

   public DERDecoder getDecoder() {
      DERDecoder var1 = null;
      if (this.isConstructed()) {
         var1 = new DERDecoder(this.value);
      }

      return var1;
   }

   public BigInteger getBigInteger() {
      BigInteger var1 = null;
      if (this.type == 2) {
         var1 = new BigInteger(this.value);
      }

      return var1;
   }

   public String getString() throws IOException {
      String var1;
      switch (this.type) {
         case 12:
         case 18:
         case 19:
         case 21:
         case 22:
         case 25:
         case 26:
         case 27:
            var1 = "UTF-8";
            break;
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 20:
         case 23:
         case 24:
         case 29:
         default:
            throw new IOException("Object not recognized as string.");
         case 28:
            throw new IOException("Universal string not supported.");
         case 30:
            var1 = "UTF-16BE";
      }

      return new String(this.value, var1);
   }
}
