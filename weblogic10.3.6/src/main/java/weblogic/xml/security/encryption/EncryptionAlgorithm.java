package weblogic.xml.security.encryption;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

public abstract class EncryptionAlgorithm extends EncryptionMethod {
   protected static final SecureRandom rand = new SecureRandom();

   public abstract InputStream decrypt(Key var1, InputStream var2) throws EncryptionException;

   public abstract OutputStream encrypt(Key var1, OutputStream var2) throws EncryptionException;

   public abstract Key createKey(byte[] var1) throws EncryptionException;

   public abstract Key generateKey() throws EncryptionException;

   public abstract Key generateKey(byte[] var1, byte[] var2) throws EncryptionException;
}
