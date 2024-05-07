package weblogic.security;

import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;

public final class MBeanSecurityValidator {
   public static void ensureEncrypted(byte[] var0) throws IllegalArgumentException {
      EncryptionService var1 = SerializedSystemIni.getEncryptionService();
      ClearOrEncryptedService var2 = new ClearOrEncryptedService(var1);
      if (!var2.isEncryptedBytes(var0)) {
         throw new IllegalArgumentException(SecurityLogger.getArgumentNotEncrypted());
      }
   }
}
