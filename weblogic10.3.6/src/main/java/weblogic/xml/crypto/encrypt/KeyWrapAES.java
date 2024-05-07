package weblogic.xml.crypto.encrypt;

import java.io.InputStream;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.xml.stream.XMLStreamReader;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

final class KeyWrapAES extends KeyWrap implements WLEncryptionMethodFactory {
   private static final String ALGORITHM_ID = "AES/ECB/NoPadding";
   private static final String KEY_FACTORY_ID = "AES";
   private static final int BLOCK_LEN = 8;
   private static final byte[] IV = new byte[]{-90, -90, -90, -90, -90, -90, -90, -90};

   private KeyWrapAES(String var1) {
      super(var1, (Integer)null, (AlgorithmParameterSpec)null);
   }

   private KeyWrapAES(String var1, AlgorithmParameterSpec var2, Integer var3) {
      super(var1, var3, var2);
   }

   public static void init() {
      WLEncryptionMethod.register(new KeyWrapAES("http://www.w3.org/2001/04/xmlenc#kw-aes128"));
      WLEncryptionMethod.register(new KeyWrapAES("http://www.w3.org/2001/04/xmlenc#kw-aes192"));
      WLEncryptionMethod.register(new KeyWrapAES("http://www.w3.org/2001/04/xmlenc#kw-aes256"));
   }

   public InputStream decrypt(Key var1, InputStream var2) {
      throw new AssertionError("NYI");
   }

   public byte[] decrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("AES/ECB/NoPadding", 2, var1);
      int var4 = var2.length;
      if (var4 % 8 != 0) {
         throw new XMLEncryptionException("Invalid key passed to " + this.getAlgorithm() + " size=" + var4 * 8);
      } else {
         int var5 = (var4 >> 3) - 1;
         if (var5 >= 2 && var5 <= 4) {
            byte[] var6 = new byte[IV.length];
            byte[] var7 = new byte[var4 - IV.length];
            byte[] var8 = new byte[16];
            System.arraycopy(var2, 0, var6, 0, var6.length);
            System.arraycopy(var2, var6.length, var7, 0, var7.length);

            for(int var9 = 5; var9 >= 0; --var9) {
               for(int var10 = var5; var10 >= 1; --var10) {
                  int var11 = var10 + var9 * var5;
                  var6[7] = (byte)(var6[7] ^ var11);
                  System.arraycopy(var6, 0, var8, 0, 8);
                  System.arraycopy(var7, (var10 - 1) * 8, var8, 8, 8);
                  var3.update(var8, 0, var8.length, var8, 0);
                  System.arraycopy(var8, 0, var6, 0, 8);
                  System.arraycopy(var8, 8, var7, (var10 - 1) * 8, 8);
               }
            }

            if (!Arrays.equals(var6, IV)) {
               throw new XMLEncryptionException("Failed to decrypt key");
            } else {
               return var7;
            }
         } else {
            throw new AssertionError("Invalid key size: " + var5 * 8);
         }
      }
   }

   public byte[] encrypt(Key var1, byte[] var2) throws XMLEncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("AES/ECB/NoPadding", 1, var1);
      int var4 = var2.length;
      if (var4 % 8 != 0) {
         throw new XMLEncryptionException("Invalid key passed to " + this.getAlgorithm() + " size=" + var4 * 8);
      } else {
         int var5 = var4 >> 3;
         if (var5 >= 2 && var5 <= 4) {
            byte[] var6 = new byte[IV.length];
            System.arraycopy(IV, 0, var6, 0, IV.length);
            byte[] var7 = new byte[var4];
            System.arraycopy(var2, 0, var7, 0, var4);
            byte[] var8 = new byte[16];

            for(int var9 = 0; var9 <= 5; ++var9) {
               for(int var10 = 1; var10 <= var5; ++var10) {
                  int var11 = var10 + var9 * var5;
                  System.arraycopy(var6, 0, var8, 0, var6.length);
                  System.arraycopy(var7, (var10 - 1) * 8, var8, var6.length, 8);
                  var3.update(var8, 0, var8.length, var8, 0);
                  System.arraycopy(var8, 0, var6, 0, 8);
                  System.arraycopy(var8, 8, var7, (var10 - 1) * 8, 8);
                  var6[7] = (byte)(var6[7] ^ var11);
               }
            }

            byte[] var12 = new byte[var6.length + var7.length];
            System.arraycopy(var6, 0, var12, 0, var6.length);
            System.arraycopy(var7, 0, var12, var6.length, var7.length);
            return var12;
         } else {
            throw new AssertionError("Invalid key size: " + var5 * 8);
         }
      }
   }

   public WLEncryptionMethod getEncryptionMethod(AlgorithmParameterSpec var1, Integer var2) {
      return this.getKeyWrap(var1, var2);
   }

   public KeyWrap getKeyWrap(AlgorithmParameterSpec var1, Integer var2) {
      return var1 == null && var2 == null ? this : new KeyWrapAES(this.getAlgorithm(), var1, var2);
   }

   public EncryptionAlgorithm getEncryptionAlgorithm(AlgorithmParameterSpec var1, Integer var2) {
      throw new UnsupportedOperationException("Algorithm " + this.getAlgorithm() + " cannot be used for bulk encryption");
   }

   public AlgorithmParameterSpec readParameters(XMLStreamReader var1) {
      return null;
   }
}
