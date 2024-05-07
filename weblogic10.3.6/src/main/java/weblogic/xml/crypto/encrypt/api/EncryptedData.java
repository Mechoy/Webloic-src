package weblogic.xml.crypto.encrypt.api;

import java.io.InputStream;

public interface EncryptedData extends EncryptedType {
   InputStream decrypt(XMLDecryptContext var1) throws XMLEncryptionException;

   void decryptAndReplace(XMLDecryptContext var1) throws XMLEncryptionException;
}
