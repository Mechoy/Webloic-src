package weblogic.cluster;

import weblogic.security.HMAC;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.ByteArrayDiffChecker;

final class EncryptionHelper {
   private static final EncryptionService es = SerializedSystemIni.getEncryptionService();
   private static final byte[] SALT = SerializedSystemIni.getSalt();
   private static final byte[] SECRET = SerializedSystemIni.getEncryptedSecretKey();
   private static final ByteArrayDiffChecker DIFF_CHECKER = new ByteArrayDiffChecker();

   static byte[] encrypt(byte[] var0) {
      return es.encryptBytes(var0);
   }

   static byte[] decrypt(byte[] var0, AuthenticatedSubject var1) {
      SecurityServiceManager.checkKernelIdentity(var1);
      return es.decryptBytes(var0);
   }

   static byte[] sign(byte[] var0) {
      return HMAC.digest(var0, SECRET, SALT);
   }

   static boolean verify(byte[] var0, byte[] var1) {
      byte[] var2 = HMAC.digest(var0, SECRET, SALT);
      return DIFF_CHECKER.diffByteArrays(var1, var2) == null;
   }
}
