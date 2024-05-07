package weblogic.xml.crypto.encrypt;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

public abstract class KeyWrap extends WLEncryptionMethod {
   protected KeyWrap(String var1, Integer var2, AlgorithmParameterSpec var3) {
      super(var1, var2, var3);
   }

   public abstract byte[] decrypt(Key var1, byte[] var2) throws XMLEncryptionException;

   public abstract byte[] encrypt(Key var1, byte[] var2) throws XMLEncryptionException;
}
