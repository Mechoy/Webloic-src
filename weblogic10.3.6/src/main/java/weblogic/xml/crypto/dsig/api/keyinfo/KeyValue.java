package weblogic.xml.crypto.dsig.api.keyinfo;

import java.security.KeyException;
import java.security.PublicKey;
import weblogic.xml.crypto.api.XMLStructure;

public interface KeyValue extends XMLStructure {
   String DSA_TYPE = "http://www.w3.org/2000/09/xmldsig#DSAKeyValue";
   String RSA_TYPE = "http://www.w3.org/2000/09/xmldsig#RSAKeyValue";

   PublicKey getPublicKey() throws KeyException;
}
