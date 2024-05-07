package weblogic.security.SSL.jsseadapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

class DERDecoder {
   static final int UNIVERSAL = 0;
   static final int APPLICATION = 64;
   static final int PRIVATE = 192;
   static final int CONTEXT_SPECIFIC = 128;
   static final int TYPE_CONSTRUCTED = 32;
   static final int BOOLEAN = 1;
   static final int INTEGER = 2;
   static final int BIT_STRING = 3;
   static final int OCTET_STRING = 4;
   static final int SEQUENCE_AND_SEQUENCE_OF = 16;
   static final int SET_AND_SET_OF = 17;
   static final int BMP_STRING = 30;
   static final int IA5_STRING = 22;
   static final int GENERAL_STRING = 27;
   static final int GRAPHIC_STRING = 25;
   static final int NUMERIC_STRING = 18;
   static final int PRINTABLE_STRING = 19;
   static final int TELETEX_STRING = 20;
   static final int UNIVERSAL_STRING = 28;
   static final int UTF8_STRING = 12;
   static final int VIDEO_TEX_STRING = 21;
   static final int VISIBLE_STRING = 26;
   private InputStream is;

   public DERDecoder(InputStream var1) {
      this.is = var1;
   }

   public DERDecoder(byte[] var1) {
      this((InputStream)(new ByteArrayInputStream(var1)));
   }

   public ASN1Object readObject() throws IOException {
      int var1 = this.is.read();
      if (var1 == -1) {
         throw new IOException("Unexpected end of stream while reading tag");
      } else {
         int var2 = this.getLength();
         byte[] var3 = new byte[var2];
         int var4 = this.is.read(var3);
         if (var4 < var2) {
            throw new IOException("The actual number of bytes readObject is less than that of the value.");
         } else {
            ASN1Object var5 = new ASN1Object(var1, var2, var3);
            return var5;
         }
      }
   }

   private int getLength() throws IOException {
      int var1 = this.is.read();
      if (var1 == -1) {
         throw new IOException("Unexpected end of stream.");
      } else if ((var1 & -128) == 0) {
         return var1;
      } else {
         int var2 = var1 & 127;
         if (var1 < 255 && var2 <= 4) {
            byte[] var3 = new byte[var2];
            int var4 = this.is.read(var3);
            if (var4 < var2) {
               throw new IOException("Length shorter than expected: " + var4);
            } else {
               return (new BigInteger(1, var3)).intValue();
            }
         } else {
            throw new IOException("Unexpected too big length: " + var1);
         }
      }
   }
}
