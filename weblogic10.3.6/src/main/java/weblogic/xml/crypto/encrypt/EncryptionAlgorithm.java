package weblogic.xml.crypto.encrypt;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

public abstract class EncryptionAlgorithm extends WLEncryptionMethod {
   protected static final SecureRandom rand = new SecureRandom();

   protected EncryptionAlgorithm(String var1, Integer var2, AlgorithmParameterSpec var3) {
      super(var1, var2, var3);
   }

   public abstract InputStream decrypt(Key var1, InputStream var2) throws XMLEncryptionException;

   public abstract OutputStream encrypt(Key var1, OutputStream var2) throws XMLEncryptionException;

   public abstract Key createKey(byte[] var1) throws XMLEncryptionException;

   public abstract Key generateKey() throws XMLEncryptionException;

   public abstract Key generateKey(byte[] var1, byte[] var2) throws XMLEncryptionException;
}
