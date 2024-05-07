package weblogic.xml.crypto.encrypt.api;

import java.io.InputStream;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;

public interface EncryptedType extends XMLStructure {
   String ELEMENT = "http://www.w3.org/2001/04/xmlenc#Element";
   String CONTENT = "http://www.w3.org/2001/04/xmlenc#Content";

   InputStream decrypt(XMLDecryptContext var1) throws XMLEncryptionException;

   void encrypt(XMLEncryptContext var1) throws XMLEncryptionException, MarshalException;

   CipherData getCipherData();

   InputStream getCipherText();

   String getEncoding();

   EncryptionMethod getEncryptionMethod();

   EncryptionProperties getEncryptionProperties();

   String getId();

   KeyInfo getKeyInfo();

   String getMimeType();

   TBE getTBE();

   String getType();
}
