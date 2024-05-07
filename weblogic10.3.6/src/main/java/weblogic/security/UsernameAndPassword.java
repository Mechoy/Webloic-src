package weblogic.security;

import java.io.Serializable;
import java.util.Arrays;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

public class UsernameAndPassword implements Serializable {
   private String username = null;
   private byte[] password = null;
   private ClearOrEncryptedService es = null;

   public UsernameAndPassword() {
   }

   public UsernameAndPassword(String var1, char[] var2) {
      this.setUsername(var1);
      this.setPassword(var2);
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public char[] getPassword() {
      if (this.password == null) {
         return null;
      } else {
         byte[] var1 = this.es == null ? this.password : this.es.decryptBytes(this.password);
         char[] var2 = getUTF16Chars(var1);
         if (var1 != this.password) {
            Arrays.fill(var1, (byte)0);
         }

         return var2;
      }
   }

   public void setPassword(char[] var1) {
      byte[] var2 = null;
      if (var1 != null) {
         var2 = getUTF16Bytes(var1);
         if (this.es != null) {
            byte[] var3 = var2;
            var2 = this.es.encryptBytes(var2);
            if (var3 != var2) {
               Arrays.fill(var3, (byte)0);
            }
         }
      }

      if (this.password != null) {
         Arrays.fill(this.password, (byte)0);
      }

      this.password = var2;
   }

   public boolean isPasswordSet() {
      return this.password != null;
   }

   public boolean isUsernameSet() {
      return this.username != null;
   }

   public void setEncryption(ClearOrEncryptedService var1) {
      if (this.password != null) {
         byte[] var2 = this.es != null ? this.es.decryptBytes(this.password) : this.password;
         byte[] var3 = var1 != null ? var1.encryptBytes(var2) : var2;
         if (var2 != var3) {
            Arrays.fill(var2, (byte)0);
         }

         if (this.password != var3) {
            Arrays.fill(this.password, (byte)0);
         }

         this.password = var3;
      }

      this.es = var1;
   }

   public void dispose() {
      this.setPassword((char[])null);
      this.setUsername((String)null);
   }

   public void finalize() throws Throwable {
      this.dispose();
      super.finalize();
   }

   private static final char[] getUTF16Chars(byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length % 2 == 1) {
         throw new IllegalArgumentException("Odd byte array length: " + var0.length);
      } else {
         char[] var1 = new char[var0.length / 2];
         int var2 = 0;

         for(int var3 = 0; var2 < var1.length; ++var2) {
            var1[var2] = (char)((255 & var0[var3++]) << 8 | 255 & var0[var3++]);
         }

         return var1;
      }
   }

   private static final byte[] getUTF16Bytes(char[] var0) {
      if (var0 == null) {
         return null;
      } else {
         byte[] var1 = new byte[var0.length * 2];
         int var2 = 0;

         for(int var3 = 0; var2 < var0.length; ++var2) {
            var1[var3++] = (byte)(var0[var2] >>> 8 & 255);
            var1[var3++] = (byte)(var0[var2] & 255);
         }

         return var1;
      }
   }
}
