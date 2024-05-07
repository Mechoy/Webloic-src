package weblogic.xml.security.encryption;

import java.security.Key;
import weblogic.xml.security.keyinfo.BaseKeyProvider;
import weblogic.xml.security.keyinfo.KeyProvider;
import weblogic.xml.security.keyinfo.KeyPurpose;
import weblogic.xml.security.keyinfo.KeyResult;

public class EncryptedKeyProvider extends BaseKeyProvider {
   private final byte[] keyBytes;
   private KeyResult keyResult = null;
   private static final int TRIPLEDES_KEY_LENGTH = 24;

   public EncryptedKeyProvider(EncryptedKey var1) throws EncryptionException {
      super(var1.getCarriedKeyName(), (byte[])null, var1.getId());
      this.keyBytes = var1.getWrappedKeyBytes();
   }

   public KeyResult getKey(String var1, KeyPurpose var2) {
      try {
         if (this.keyResult != null) {
            return this.keyResult;
         } else {
            Key var3 = null;
            if (weblogic.xml.security.keyinfo.Utils.supports(KeyProvider.AES_ALGORITHMS, var1)) {
               var3 = EncryptedKey.getWrappedKey(EncryptionMethod.get(var1), this.keyBytes);
            } else if (weblogic.xml.security.keyinfo.Utils.supports(KeyProvider.TRIPLEDES_ALGORITHMS, var1) && this.is3DESKey(this.keyBytes)) {
               var3 = EncryptedKey.getWrappedKey(EncryptionMethod.get(var1), this.keyBytes);
            }

            if (var3 != null) {
               this.keyResult = new KeyResult(var3);
            }

            return this.keyResult;
         }
      } catch (EncryptionException var4) {
         return null;
      }
   }

   private boolean is3DESKey(byte[] var1) {
      return var1.length == 24;
   }

   public String toString() {
      return "weblogic.xml.security.encryption.EncryptedKeyProvider{name=" + this.getName() + ", uri=" + this.getUri() + ", keyBytes=" + (this.keyBytes == null ? null : "length:" + this.keyBytes.length) + ", keyResult=" + this.keyResult + "}";
   }
}
