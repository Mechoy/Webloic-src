package weblogic.xml.crypto.encrypt;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.security.signature.DigestMethod;
import weblogic.xml.security.utils.XMLSecurityException;

final class KeyWrapDES3 extends KeyWrap implements WLEncryptionMethodFactory {
   private static final String ALGORITHM_ID = "3DES_EDE/CBC/NoPad";
   private static final String KEY_FACTORY_ID = "DESEDE";
   private static final byte[] IV = new byte[]{74, -35, -94, 44, 121, -24, 33, 5};

   private KeyWrapDES3() {
      super("http://www.w3.org/2001/04/xmlenc#kw-tripledes", (Integer)null, (AlgorithmParameterSpec)null);
   }

   private KeyWrapDES3(Integer var1, AlgorithmParameterSpec var2) {
      super("http://www.w3.org/2001/04/xmlenc#kw-tripledes", var1, var2);
   }

   public static void init() {
      WLEncryptionMethod.register(new KeyWrapDES3());
   }

   public byte[] decrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 2, var1, IV);
      byte[] var4 = var3.decrypt(var2);
      this.reverse(var4);
      byte[] var5 = new byte[8];
      System.arraycopy(var4, 0, var5, 0, 8);
      var3 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 2, var1, var5);
      byte[] var6 = var3.decrypt(var4, 8, var4.length - 8);
      byte[] var7 = new byte[var6.length - 8];
      System.arraycopy(var6, 0, var7, 0, var7.length);
      DigestMethod var8 = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
      MessageDigest var9 = var8.getMessageDigest();
      var9.update(var7);
      byte[] var10 = var9.digest();

      for(int var11 = 0; var11 < 8; ++var11) {
         if (var6[var6.length - 8 + var11] != var10[var11]) {
            throw new XMLEncryptionException("Cannot decrypt key: bad CMS checksum");
         }
      }

      return var7;
   }

   public byte[] encrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 1, var1);
      byte[] var4 = var3.getIV();
      byte[] var5 = new byte[var2.length + 8];
      System.arraycopy(var2, 0, var5, 0, var2.length);
      DigestMethod var6 = getDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
      MessageDigest var7 = var6.getMessageDigest();
      var7.update(var2);
      byte[] var8 = var7.digest();
      System.arraycopy(var8, 0, var5, var2.length, 8);
      byte[] var9 = var3.encrypt(var5);
      byte[] var10 = new byte[var9.length + var4.length];
      System.arraycopy(var4, 0, var10, 0, var4.length);
      System.arraycopy(var9, 0, var10, var4.length, var9.length);
      this.reverse(var10);
      var3 = CipherWrapper.getInstance("3DES_EDE/CBC/NoPad", 1, var1, IV);
      return var3.encrypt(var10);
   }

   private final void reverse(byte[] var1) {
      int var2 = var1.length;
      int var3 = var2 / 2;

      for(int var4 = 0; var4 < var3; ++var4) {
         var1[var4] ^= var1[var2 - 1 - var4];
         var1[var2 - 1 - var4] ^= var1[var4];
         var1[var4] ^= var1[var2 - 1 - var4];
      }

   }

   private static DigestMethod getDigestMethod(String var0) throws XMLEncryptionException {
      try {
         return DigestMethod.get(var0);
      } catch (XMLSecurityException var2) {
         throw new XMLEncryptionException("DigestMethod: " + var0 + " not available");
      }
   }

   public WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2) {
      return this.getKeyWrap(var1, var2);
   }

   public KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2) {
      return var1 == null && var2 == null ? this : new KeyWrapDES3(var2, var1);
   }

   public EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2) {
      throw new UnsupportedOperationException("Algorithm " + this.getAlgorithm() + " cannot be used for bulk encryption");
   }

   public AlgorithmParameterSpec readParameters(XMLStreamReader var1) {
      return null;
   }
}
