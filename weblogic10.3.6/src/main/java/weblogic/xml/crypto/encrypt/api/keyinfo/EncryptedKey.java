package weblogic.xml.crypto.encrypt.api.keyinfo;

import java.security.Key;
import java.util.List;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.encrypt.api.EncryptedType;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;
import weblogic.xml.crypto.encrypt.api.XMLEncryptionException;

public interface EncryptedKey extends EncryptedType, XMLStructure {
   String TYPE = "http://www.w3.org/2001/04/xmlenc#EncryptedKey";

   Key decryptKey(XMLDecryptContext var1) throws XMLEncryptionException;

   Key decryptKey(XMLDecryptContext var1, AlgorithmMethod var2) throws XMLEncryptionException;

   String getCarriedKeyName();

   String getRecipient();

   List getReferenceList();
}
