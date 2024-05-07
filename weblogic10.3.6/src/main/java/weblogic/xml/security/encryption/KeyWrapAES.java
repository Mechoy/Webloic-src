package weblogic.xml.security.encryption;

import java.security.Key;
import java.util.Arrays;

public class KeyWrapAES extends KeyWrap implements KeyWrapFactory {
   static final String URI_KW_128 = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
   static final String URI_KW_192 = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
   static final String URI_KW_256 = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
   private static final String ALGORITHM_ID = "AES/ECB/NoPadding";
   private static final String KEY_FACTORY_ID = "AES";
   private static final int BLOCK_LEN = 8;
   private static final byte[] IV = new byte[]{-90, -90, -90, -90, -90, -90, -90, -90};
   private final String uri;

   private KeyWrapAES(String var1) {
      this.uri = var1;
   }

   public String getURI() {
      return this.uri;
   }

   public String getAlgorithm() {
      return "AES";
   }

   public static void init() {
      EncryptionMethod.register(new KeyWrapAES("http://www.w3.org/2001/04/xmlenc#kw-aes128"));
      EncryptionMethod.register(new KeyWrapAES("http://www.w3.org/2001/04/xmlenc#kw-aes192"));
      EncryptionMethod.register(new KeyWrapAES("http://www.w3.org/2001/04/xmlenc#kw-aes256"));
   }

   public EncryptionMethod newEncryptionMethod() {
      return this;
   }

   public KeyWrap newKeyWrap() {
      return this;
   }

   public byte[] unwrap(Key var1, byte[] var2) throws EncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("AES/ECB/NoPadding", 2, var1);
      int var4 = var2.length;
      if (var4 % 8 != 0) {
         throw new EncryptionException("Invalid key passed to " + this.getURI() + " size=" + var4 * 8);
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
               throw new EncryptionException("Failed to unwrap key");
            } else {
               return var7;
            }
         } else {
            throw new AssertionError("Invalid key size: " + var5 * 8);
         }
      }
   }

   public byte[] wrap(Key var1, byte[] var2) throws EncryptionException {
      CipherWrapper var3 = CipherWrapper.getInstance("AES/ECB/NoPadding", 1, var1);
      int var4 = var2.length;
      if (var4 % 8 != 0) {
         throw new EncryptionException("Invalid key passed to " + this.getURI() + " size=" + var4 * 8);
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
}
