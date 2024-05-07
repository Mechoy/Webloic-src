package weblogic.security.internal.encryption;

import java.io.IOException;
import weblogic.security.SecurityLogger;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public final class ClearOrEncryptedService {
   private String encryptedPrefix = null;
   private byte[] encryptedStartBytes = null;
   private EncryptionServiceV2 encryptionService = null;

   public ClearOrEncryptedService(EncryptionService var1) {
      if (!(var1 instanceof EncryptionServiceV2)) {
         throw new EncryptionServiceException("IllegalStateException: Invalid Service");
      } else {
         this.encryptionService = (EncryptionServiceV2)var1;
         this.encryptedPrefix = this.encryptionService.getDefaultKeyContext();
         this.encryptedStartBytes = "{".getBytes();
      }
   }

   public boolean isEncrypted(String var1) {
      String var2 = this.findPrefix(var1);
      return var2 == null ? false : this.encryptionService.isKeyContextAvailable(var2);
   }

   public boolean isEncryptedBytes(byte[] var1) {
      return !this.startsWithBytes(this.encryptedStartBytes, var1) ? false : this.isEncrypted(new String(var1));
   }

   public String encrypt(String var1) {
      if (this.isEncrypted(var1)) {
         return var1;
      } else {
         byte[] var2 = this.encryptionService.encryptString(this.encryptedPrefix, var1);
         String var3 = (new BASE64Encoder()).encodeBuffer(var2);
         return this.encryptedPrefix + var3;
      }
   }

   public byte[] encryptBytes(byte[] var1) {
      if (this.isEncryptedBytes(var1)) {
         return var1;
      } else {
         byte[] var2 = this.encryptionService.encryptBytes(this.encryptedPrefix, var1);
         String var3 = (new BASE64Encoder()).encodeBuffer(var2);
         return (this.encryptedPrefix + var3).getBytes();
      }
   }

   public String decrypt(String var1) {
      String var2 = this.findPrefix(var1);
      if (var2 == null) {
         return var1;
      } else if (!this.encryptionService.isKeyContextAvailable(var2)) {
         return var1;
      } else {
         String var3 = var1.substring(var2.length());

         try {
            byte[] var4 = (new BASE64Decoder()).decodeBuffer(var3);
            return this.encryptionService.decryptString(var2, var4);
         } catch (IOException var5) {
            throw new EncryptionServiceException(SecurityLogger.getDecodingError("" + var5));
         }
      }
   }

   public byte[] decryptBytes(byte[] var1) {
      if (!this.startsWithBytes(this.encryptedStartBytes, var1)) {
         return var1;
      } else {
         String var2 = new String(var1);
         String var3 = this.findPrefix(var2);
         if (var3 == null) {
            return var1;
         } else if (!this.encryptionService.isKeyContextAvailable(var3)) {
            return var1;
         } else {
            String var4 = var2.substring(var3.length());

            try {
               byte[] var5 = (new BASE64Decoder()).decodeBuffer(var4);
               return this.encryptionService.decryptBytes(var3, var5);
            } catch (IOException var6) {
               throw new EncryptionServiceException(SecurityLogger.getDecodingError("" + var6));
            }
         }
      }
   }

   private boolean startsWithBytes(byte[] var1, byte[] var2) {
      if (var2.length < var1.length) {
         return false;
      } else {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var2[var3] != var1[var3]) {
               return false;
            }
         }

         return true;
      }
   }

   private byte[] bytesSubstring(byte[] var1, int var2) {
      int var3 = var1.length - var2;
      byte[] var4 = new byte[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         var4[var5] = var1[var5 + var2];
      }

      return var4;
   }

   private String findPrefix(String var1) {
      if (var1 != null && var1.length() != 0) {
         if (var1.charAt(0) != '{') {
            return null;
         } else {
            int var2 = var1.indexOf(125);
            return var2 == -1 ? null : var1.substring(0, var2 + 1);
         }
      } else {
         return null;
      }
   }
}
