package weblogic.corba.utils;

import java.io.Serializable;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

public class MarshaledString implements Serializable {
   static final int MAX_STRING_SIZE = 524288;
   public static final MarshaledString EMPTY = new MarshaledString("");
   protected byte[] encodedString;
   private String string;
   protected int hash;

   public MarshaledString(InputStream var1, int var2) {
      this.read(var1, var2);
   }

   MarshaledString(MarshaledString var1) {
      this.encodedString = var1.encodedString;
      this.string = var1.string;
      this.hash = var1.hash;
   }

   public MarshaledString(InputStream var1) {
      this.read(var1, var1.read_ulong());
   }

   MarshaledString() {
   }

   public MarshaledString(String var1) {
      this.string = var1;
      int var2 = this.string.length();
      this.encodedString = new byte[var2];
      this.string.getBytes(0, var2, this.encodedString, 0);
   }

   public final int length() {
      return this.encodedString.length;
   }

   public final byte[] getEncoded() {
      return this.encodedString;
   }

   public int hashCode() {
      if (this.hash == 0) {
         synchronized(this) {
            if (this.hash == 0) {
               int var2 = 0;
               int var3 = this.encodedString.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  var2 = 31 * var2 + this.encodedString[var4];
               }

               this.hash = var2;
            }
         }
      }

      return this.hash;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MarshaledString)) {
         return false;
      } else {
         MarshaledString var2 = (MarshaledString)var1;
         return this.compareStrings(var2);
      }
   }

   public boolean compareStrings(MarshaledString var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && this.hashCode() == var1.hashCode()) {
         if (var1.encodedString.length == this.encodedString.length) {
            int var2 = this.encodedString.length;

            do {
               if (var2-- <= 0) {
                  return true;
               }
            } while(this.encodedString[var2] == var1.encodedString[var2]);

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final String toString() {
      if (this.string == null) {
         this.string = new String(this.encodedString, 0);
      }

      return this.string;
   }

   public final void write(OutputStream var1) {
      var1.write_ulong(this.encodedString.length + 1);
      var1.write_octet_array(this.encodedString, 0, this.encodedString.length);
      var1.write_octet((byte)0);
   }

   public final void read(InputStream var1, int var2) {
      if (var2 > 524288) {
         throw new MARSHAL("Stream corrupted: tried to read string of length " + Integer.toHexString(var2));
      } else {
         this.encodedString = new byte[var2 - 1];
         var1.read_octet_array(this.encodedString, 0, this.encodedString.length);
         var1.read_octet();
      }
   }
}
