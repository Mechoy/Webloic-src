package weblogic.security.internal.encryption;

public final class JSafeEncryptionServiceFactory implements EncryptionServiceFactory {
   public byte[] createEncryptedSecretKey(byte[] var1, String var2) throws EncryptionServiceException {
      return JSafeEncryptionServiceImpl.createEncryptedSecretKey(var2, var1);
   }

   public EncryptionService getEncryptionService(byte[] var1, String var2, byte[] var3) throws EncryptionServiceException {
      return new JSafeEncryptionServiceImpl(var3, var1, var2, (byte[])null);
   }

   public byte[] reEncryptEncryptedSecretKey(byte[] var1, byte[] var2, byte[] var3, String var4, String var5) throws EncryptionServiceException {
      byte[] var6 = JSafeEncryptionServiceImpl.reEncryptSecretKey("3DES_EDE/CBC/PKCS5Padding", var1, var4, var2, var5, var3);
      return var6;
   }

   public byte[] createAESEncryptedSecretKey(byte[] var1, String var2) throws EncryptionServiceException {
      return JSafeEncryptionServiceImpl.createAESEncryptedSecretKey(var2, var1);
   }

   public EncryptionService getEncryptionService(byte[] var1, String var2, byte[] var3, byte[] var4) throws EncryptionServiceException {
      return new JSafeEncryptionServiceImpl(var3, var1, var2, var4);
   }
}
