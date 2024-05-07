package weblogic.management.internal;

import java.io.File;
import java.io.IOException;
import weblogic.management.DomainDir;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;

public final class EncryptedData {
   public static void initialize() {
      ClearOrEncryptedService var0 = EncryptedData.SINGLETON.encryptor;
   }

   public static String decrypt(String var0) {
      return EncryptedData.SINGLETON.encryptor.decrypt(var0);
   }

   static String encrypt(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         return EncryptedData.SINGLETON.encryptor.encrypt(var0);
      } else {
         throw new IllegalArgumentException("Can't encrypt null or empty string");
      }
   }

   private static ClearOrEncryptedService createEncryptionService() {
      try {
         File var0 = new File(DomainDir.getRootDir());
         EncryptionService var3 = SerializedSystemIni.getEncryptionService(var0.getCanonicalPath());
         return new ClearOrEncryptedService(var3);
      } catch (IOException var2) {
         AssertionError var1 = new AssertionError("Failed to find SerializedSystemIni.dat");
         var1.initCause(var2);
         throw var1;
      }
   }

   private static class SINGLETON {
      static final ClearOrEncryptedService encryptor = EncryptedData.createEncryptionService();
   }
}
