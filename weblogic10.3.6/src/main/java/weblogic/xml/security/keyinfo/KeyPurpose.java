package weblogic.xml.security.keyinfo;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public final class KeyPurpose {
   private final String description;
   public static final KeyPurpose SIGN = new KeyPurpose("signing");
   public static final KeyPurpose VERIFY = new KeyPurpose("verifying");
   public static final KeyPurpose ENCRYPT = new KeyPurpose("encryption");
   public static final KeyPurpose DECRYPT = new KeyPurpose("decryption");
   public static final KeyPurpose[] ALL_PURPOSES;
   public static final KeyPurpose[] PRIVATE_KEY_PURPOSES;
   public static final KeyPurpose[] PUBLIC_KEY_PURPOSES;
   public static final KeyPurpose[] DSA_PRIVATE_KEY_PURPOSES;
   public static final KeyPurpose[] DSA_PUBLIC_KEY_PURPOSES;

   private KeyPurpose(String var1) {
      this.description = var1;
   }

   public String toString() {
      return this.description;
   }

   public boolean served(KeyPurpose[] var1) {
      if (var1 == ALL_PURPOSES) {
         return true;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            KeyPurpose var3 = var1[var2];
            if (this == var3) {
               return true;
            }
         }

         return false;
      }
   }

   protected static final boolean serves(KeyPurpose[] var0, KeyPurpose var1) {
      return var1.served(var0);
   }

   public static final KeyPurpose[] getPurposes(Key var0) {
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

   static {
      ALL_PURPOSES = new KeyPurpose[]{SIGN, VERIFY, ENCRYPT, DECRYPT};
      PRIVATE_KEY_PURPOSES = new KeyPurpose[]{SIGN, DECRYPT};
      PUBLIC_KEY_PURPOSES = new KeyPurpose[]{VERIFY, ENCRYPT};
      DSA_PRIVATE_KEY_PURPOSES = new KeyPurpose[]{SIGN};
      DSA_PUBLIC_KEY_PURPOSES = new KeyPurpose[]{VERIFY};
   }
}
