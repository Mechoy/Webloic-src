package weblogic.xml.crypto.utils;

import com.rsa.jsafe.JSAFE_Exception;
import com.rsa.jsafe.JSAFE_SecretKey;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.security.encryption.EncryptionException;
import weblogic.xml.security.encryption.Utils;

public class KeyUtils {
   public static final KeySelector.Purpose[] ALL_PURPOSES;
   public static final KeySelector.Purpose[] PRIVATE_KEY_PURPOSES;
   public static final KeySelector.Purpose[] PUBLIC_KEY_PURPOSES;
   public static final KeySelector.Purpose[] DSA_PRIVATE_KEY_PURPOSES;
   public static final KeySelector.Purpose[] DSA_PUBLIC_KEY_PURPOSES;
   private static final String AES_KEY_FACTORY_ID = "AES";
   private static final String DES_KEY_FACTORY_ID = "DESEDE";
   private static final String DES_JSAFE_KEY_FACTORY_ID = "3DES_EDE";
   private static final String DES_JSAFE_PROVIDER = "Java";
   private static final String DES3_URI = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
   protected static final SecureRandom rand;
   private static final String[] desAlgs;
   private static final String[] aesAlgs;
   private static final Integer KL128;
   private static final Integer KL192;
   private static final Integer KL256;
   private static final Map keyLengths;

   public static final boolean supports(String[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3 = var0[var2];
         if (var3 != null && var3.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static final String[] getAlgorithms(Key var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Provided null, expected key");
      } else {
         String var1 = var0.getAlgorithm();
         if ("DESEDE".equalsIgnoreCase(var1)) {
            return KeyProvider.TRIPLEDES_ALGORITHMS;
         } else if ("AES".equals(var1)) {
            return KeyProvider.AES_ALGORITHMS;
         } else if ("RSA".equals(var1)) {
            return KeyProvider.RSA_ALGORITHMS;
         } else if ("DSA".equals(var1)) {
            return KeyProvider.DSA_ALGORITHMS;
         } else {
            throw new IllegalArgumentException("Unsupported algorithm: " + var1);
         }
      }
   }

   public static final KeySelector.Purpose[] getPurposes(Key var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Provided null, expected key");
      } else if (var0 instanceof PublicKey) {
         return "DSA".equals(var0.getAlgorithm()) ? DSA_PUBLIC_KEY_PURPOSES : PUBLIC_KEY_PURPOSES;
      } else if (var0 instanceof PrivateKey) {
         return "DSA".equals(var0.getAlgorithm()) ? DSA_PRIVATE_KEY_PURPOSES : PRIVATE_KEY_PURPOSES;
      } else {
         return ALL_PURPOSES;
      }
   }

   public static final boolean serves(KeySelector.Purpose[] var0, KeySelector.Purpose var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         KeySelector.Purpose var3 = var0[var2];
         if (var1 == var3) {
            return true;
         }
      }

      return false;
   }

   public static boolean matches(byte[] var0, byte[] var1) {
      if (var0 != null && var1 != null) {
         if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               byte var3 = var0[var2];
               byte var4 = var1[var2];
               if (var3 != var4) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static Key generateKey(String var0) throws EncryptionException {
      Key var1 = null;
      if (isAlg(var0, aesAlgs)) {
         var1 = generateAESKey(getKeyLength(var0));
      }

      if (isAlg(var0, desAlgs)) {
         var1 = generateDES3Key();
      }

      return var1;
   }

   private static boolean isAlg(String var0, String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];
         if (var3 != null && var3.equals(var0)) {
            return true;
         }
      }

      return false;
   }

   private static int getKeyLength(String var0) {
      Integer var1 = (Integer)keyLengths.get(var0);
      if (var1 != null) {
         return var1;
      } else {
         throw new IllegalArgumentException("Unsupported algorithm.");
      }
   }

   private static Key generateAESKey(int var0) throws EncryptionException {
      byte[] var1 = new byte[var0];
      rand.nextBytes(var1);
      return createAESKey(var1);
   }

   private static Key createAESKey(byte[] var0) throws EncryptionException {
      return new SecretKeySpec(var0, "AES");
   }

   private static Key generateDES3Key() throws EncryptionException {
      try {
         JSAFE_SecretKey var0 = JSAFE_SecretKey.getInstance("3DES_EDE", "Java");
         var0.generateInit((int[])null, Utils.getRNG());
         var0.generate();
         return createDES3Key(var0.getSecretKeyData());
      } catch (JSAFE_Exception var1) {
         throw new EncryptionException(var1);
      }
   }

   private static Key createDES3Key(byte[] var0) throws EncryptionException {
      try {
         DESedeKeySpec var1 = new DESedeKeySpec(var0);
         SecretKeyFactory var2 = SecretKeyFactory.getInstance("DESEDE");
         return var2.generateSecret(var1);
      } catch (InvalidKeyException var3) {
         throw new EncryptionException("Invalid key supplied to http://www.w3.org/2001/04/xmlenc#tripledes-cbc", var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new EncryptionException("Cannot locate JCE algorithm (DESEDE) necessary for generating a secret key for: http://www.w3.org/2001/04/xmlenc#tripledes-cbc", var4);
      } catch (InvalidKeySpecException var5) {
         throw new EncryptionException("Unexpected exception when generating key for: http://www.w3.org/2001/04/xmlenc#tripledes-cbc", var5);
      }
   }

   public static byte[] createNonce() {
      byte[] var0 = new byte[16];
      rand.nextBytes(var0);
      return var0;
   }

   public static final SecretKey newSecretKey(String var0, int var1) throws NoSuchAlgorithmException, IllegalArgumentException {
      KeyGenerator var2 = KeyGenerator.getInstance(var0);
      var2.init(var1);
      return var2.generateKey();
   }

   public static Key generateKey(byte[] var0, byte[] var1, String var2, int var3) throws NoSuchAlgorithmException, InvalidKeyException {
      SecretKeySpec var4 = new SecretKeySpec(var0, var2);
      byte[] var5 = P_SHA1(var4, var1, var3 / 8);
      return new SecretKeySpec(var5, var2);
   }

   public static byte[] P_SHA1(Key var0, byte[] var1, int var2) throws NoSuchAlgorithmException, InvalidKeyException {
      Mac var3 = Mac.getInstance("HmacSHA1");
      byte[] var4 = new byte[var2];
      byte[] var5 = var1;
      int var6 = 0;

      do {
         var3.init(var0);
         var5 = var3.doFinal(var5);
         var3.reset();
         var3.init(var0);
         var3.update(var5);
         byte[] var7 = var3.doFinal(var1);
         var3.reset();

         for(int var8 = 0; var6 < var2 && var8 < var7.length; ++var8) {
            var4[var6] = var7[var8];
            ++var6;
         }
      } while(var6 < var2);

      return var4;
   }

   static {
      ALL_PURPOSES = new KeySelector.Purpose[]{KeySelector.Purpose.SIGN, KeySelector.Purpose.VERIFY, KeySelector.Purpose.ENCRYPT, KeySelector.Purpose.DECRYPT};
      PRIVATE_KEY_PURPOSES = new KeySelector.Purpose[]{KeySelector.Purpose.SIGN, KeySelector.Purpose.DECRYPT};
      PUBLIC_KEY_PURPOSES = new KeySelector.Purpose[]{KeySelector.Purpose.VERIFY, KeySelector.Purpose.ENCRYPT};
      DSA_PRIVATE_KEY_PURPOSES = new KeySelector.Purpose[]{KeySelector.Purpose.SIGN};
      DSA_PUBLIC_KEY_PURPOSES = new KeySelector.Purpose[]{KeySelector.Purpose.VERIFY};
      rand = new SecureRandom();
      desAlgs = new String[]{"http://www.w3.org/2001/04/xmlenc#kw-tripledes", "http://www.w3.org/2001/04/xmlenc#tripledes-cbc"};
      aesAlgs = new String[]{"http://www.w3.org/2001/04/xmlenc#aes128-cbc", "http://www.w3.org/2001/04/xmlenc#aes192-cbc", "http://www.w3.org/2001/04/xmlenc#aes256-cbc", "http://www.w3.org/2001/04/xmlenc#kw-aes128", "http://www.w3.org/2001/04/xmlenc#kw-aes192", "http://www.w3.org/2001/04/xmlenc#kw-aes256"};
      KL128 = new Integer(128);
      KL192 = new Integer(192);
      KL256 = new Integer(256);
      keyLengths = new HashMap();
      keyLengths.put("http://www.w3.org/2001/04/xmlenc#aes128-cbc", KL128);
      keyLengths.put("http://www.w3.org/2001/04/xmlenc#aes192-cbc", KL192);
      keyLengths.put("http://www.w3.org/2001/04/xmlenc#aes256-cbc", KL256);
      keyLengths.put("http://www.w3.org/2001/04/xmlenc#kw-aes128", KL128);
      keyLengths.put("http://www.w3.org/2001/04/xmlenc#kw-aes192", KL192);
      keyLengths.put("http://www.w3.org/2001/04/xmlenc#kw-aes256", KL256);
   }
}
