package weblogic.management;

import weblogic.kernel.Kernel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.internal.encryption.EncryptionServiceException;
import weblogic.security.service.SecurityServiceManager;

public final class EncryptionHelper {
   private static EncryptionService encryptor = null;
   private static final byte CLEAR_BYTE = 0;
   private static ClearOrEncryptedService cEncryptor = null;

   public static final String decryptString(byte[] var0, AuthenticatedSubject var1) throws EncryptionServiceException {
      SecurityServiceManager.checkKernelIdentity(var1);
      if (var0 != null && var0.length != 0) {
         ClearOrEncryptedService var2 = getCEncryptionService();
         return var2.decrypt(new String(var0));
      } else {
         return null;
      }
   }

   public static final byte[] decrypt(byte[] var0, AuthenticatedSubject var1) throws EncryptionServiceException {
      SecurityServiceManager.checkKernelIdentity(var1);
      if (var0 != null && var0.length != 0) {
         ClearOrEncryptedService var2 = getCEncryptionService();
         return var2.decryptBytes(var0);
      } else {
         return var0;
      }
   }

   public static final byte[] encryptString(String var0) throws EncryptionServiceException {
      if (!Kernel.isServer()) {
         throw new EncryptionServiceException("Caller not part of the Server JVM, Access to encrypt passwords is denied");
      } else if (var0 != null && var0.length() != 0) {
         ClearOrEncryptedService var1 = getCEncryptionService();
         return var1.encrypt(var0).getBytes();
      } else {
         return null;
      }
   }

   public static final byte[] encrypt(byte[] var0) throws EncryptionServiceException {
      if (var0 != null && var0.length != 0) {
         ClearOrEncryptedService var1 = getCEncryptionService();
         return var1.encryptBytes(var0);
      } else {
         return var0;
      }
   }

   public static byte[] clear(byte[] var0) {
      if (var0 != null && var0.length != 0) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var0[var1] = 0;
         }

         return var0;
      } else {
         return var0;
      }
   }

   public static boolean isEncrypted(String var0) {
      if (var0 == null) {
         return false;
      } else {
         ClearOrEncryptedService var1 = getCEncryptionService();
         return var1.isEncrypted(var0);
      }
   }

   private static ClearOrEncryptedService getCEncryptionService() {
      if (cEncryptor == null) {
         if (encryptor == null) {
            encryptor = SerializedSystemIni.getExistingEncryptionService();
         }

         cEncryptor = new ClearOrEncryptedService(encryptor);
      }

      return cEncryptor;
   }
}
