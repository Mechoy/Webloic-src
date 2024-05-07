package weblogic.xml.crypto.common.keyinfo;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.KeySelectorResult;
import weblogic.xml.crypto.encrypt.WLCipherData;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionFactory;
import weblogic.xml.crypto.encrypt.api.keyinfo.EncryptedKey;
import weblogic.xml.crypto.encrypt.api.spec.EncryptionMethodParameterSpec;
import weblogic.xml.crypto.utils.KeyUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.provider.SecurityToken;
import weblogic.xml.security.keyinfo.Utils;

public class EncryptedKeyProvider extends BaseKeyProvider {
   private static final String HASH_FUNCTION_SHA_256 = "SHA-256";
   private static final String HASH_FUNCTION_SHA_1 = "SHA-1";
   private EncryptedKey encryptedKey;
   private XMLDecryptContext cryptoCtx;
   private Key key;
   private KeySelectorResultImpl ksr;
   private static Map keySizes = new HashMap();
   public static final String DEFAULT_GENKEY_ALG = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";

   public EncryptedKeyProvider(EncryptedKey var1, Key var2, SecurityToken var3) throws XMLEncryptionException {
      super(var1.getCarriedKeyName(), getKeyIdentifier(var1), getUri(var1.getId()), var3);
      this.key = var2;
      this.encryptedKey = var1;
   }

   public EncryptedKeyProvider(EncryptedKey var1, Key var2) throws XMLEncryptionException {
      this(var1, (XMLDecryptContext)null);
      this.key = var2;
   }

   public EncryptedKeyProvider(EncryptedKey var1, SecurityToken var2, XMLDecryptContext var3) throws XMLEncryptionException {
      super(var1.getCarriedKeyName(), getKeyIdentifier(var1), getUri(var1.getId()), var2);
      this.encryptedKey = var1;
      this.cryptoCtx = var3;
   }

   public EncryptedKeyProvider(EncryptedKey var1, XMLDecryptContext var2) throws XMLEncryptionException {
      super(var1.getCarriedKeyName(), getKeyIdentifier(var1), getUri(var1.getId()));
      this.encryptedKey = var1;
      this.cryptoCtx = var2;
   }

   public static List<byte[]> getKeyIdentifiers(EncryptedKey var0) throws XMLEncryptionException {
      ArrayList var1 = new ArrayList();
      var1.add(getKeyIdentifier(var0, "SHA-256"));
      var1.add(getKeyIdentifier(var0, "SHA-1"));
      return var1;
   }

   public static byte[] getKeyIdentifier(EncryptedKey var0) throws XMLEncryptionException {
      return getKeyIdentifier(var0, "SHA-1");
   }

   private static byte[] getKeyIdentifier(EncryptedKey var0, String var1) throws XMLEncryptionException {
      byte[] var2 = ((WLCipherData)var0.getCipherData()).getCipherBytes();
      return var2 != null ? getDigest(var2, var1) : null;
   }

   private static byte[] getDigest(byte[] var0, String var1) {
      MessageDigest var2 = null;
      Object var3 = null;

      try {
         var2 = MessageDigest.getInstance(var1);
      } catch (NoSuchAlgorithmException var7) {
         try {
            var2 = MessageDigest.getInstance("SHA-1");
         } catch (NoSuchAlgorithmException var6) {
            throw new RuntimeException(var6);
         }
      }

      var2.update(var0);
      byte[] var8 = var2.digest();
      return var8;
   }

   public KeySelectorResult getKeyByIdentifier(byte[] var1, String var2, KeySelector.Purpose var3) {
      KeySelectorResult var4 = super.getKeyByIdentifier(var1, var2, var3);
      if (var4 == null) {
         List var5 = null;

         try {
            var5 = getKeyIdentifiers(this.encryptedKey);
         } catch (XMLEncryptionException var8) {
            throw new RuntimeException(var8);
         }

         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            byte[] var7 = (byte[])var6.next();
            if (KeyUtils.matches(var1, var7)) {
               var4 = this.getKey(var2, var3);
               break;
            }
         }
      }

      return var4;
   }

   public KeySelectorResult getKey(String var1, KeySelector.Purpose var2) {
      String var3 = "Could not derive key from encrypted key for requested algorithm";
      KeySelectorResultImpl var4 = null;
      if (this.key == null) {
         try {
            XMLEncryptionFactory var5 = XMLEncryptionFactory.getInstance();
            Object var6 = null;
            EncryptionMethod var7 = null;
            if (KeySelector.Purpose.VERIFY.equals(var2)) {
               var7 = XMLEncryptionFactory.getInstance().newEncryptionMethod("http://www.w3.org/2001/04/xmlenc#aes256-cbc", (Integer)null, (EncryptionMethodParameterSpec)null);
            } else {
               var7 = var5.newEncryptionMethod(var1, (Integer)null, (EncryptionMethodParameterSpec)var6);
            }

            this.key = this.encryptedKey.decryptKey(this.cryptoCtx, var7);
            var4 = (KeySelectorResultImpl)this.cryptoCtx.getProperty("weblogic.xml.crypto.ksr");
         } catch (XMLEncryptionException var8) {
            var3 = var3 + ". Cause: " + var8.getMessage();
         } catch (InvalidAlgorithmParameterException var9) {
            var3 = var3 + ". Cause: " + var9.getMessage();
         }
      }

      if (this.key != null && Utils.supports(Utils.getAlgorithms(this.key), var1) && KeyUtils.serves(KeyUtils.getPurposes(this.key), var2)) {
         this.ksr = new KeySelectorResultImpl(this.key);
         if (!var2.equals(KeySelector.Purpose.SIGN) && !var2.equals(KeySelector.Purpose.VERIFY)) {
            if (var4 != null) {
               this.ksr.setSecurityToken(var4.getSecurityToken());
            } else {
               this.ksr.setSecurityToken(this.getSecurityToken());
            }
         } else {
            this.ksr.setSecurityToken(this.getSecurityToken());
         }

         return this.ksr;
      } else {
         var3 = var3 + " because encrypted key doesn't support required " + "algorithm or purpose.";
         LogUtils.logKeyInfo(var3);
         return null;
      }
   }

   private static String getUri(String var0) {
      return var0 != null ? "#" + var0 : null;
   }

   static {
      keySizes.put("http://www.w3.org/2001/04/xmlenc#kw-aes128", new Integer(128));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#kw-aes192", new Integer(192));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#kw-aes256", new Integer(256));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#kw-tripledes", new Integer(192));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#aes128-cbc", new Integer(128));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#aes192-cbc", new Integer(192));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#aes256-cbc", new Integer(256));
      keySizes.put("http://www.w3.org/2001/04/xmlenc#tripledes-cbc", new Integer(192));
   }
}
