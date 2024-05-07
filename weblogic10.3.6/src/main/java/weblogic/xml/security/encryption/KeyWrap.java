package weblogic.xml.security.encryption;

import java.security.Key;

public abstract class KeyWrap extends EncryptionMethod {
   public abstract byte[] unwrap(Key var1, byte[] var2) throws EncryptionException;

   public abstract byte[] wrap(Key var1, byte[] var2) throws EncryptionException;

   public abstract String getAlgorithm();
}
