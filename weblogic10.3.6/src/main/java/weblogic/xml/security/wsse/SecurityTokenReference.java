package weblogic.xml.security.wsse;

import java.security.Key;
import java.security.PrivateKey;
import weblogic.xml.security.keyinfo.X509IssuerSerial;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public interface SecurityTokenReference {
   KeyIdentifier getKeyIdentifier();

   String getReference();

   void setId(String var1);

   String getId();

   void setToken(Token var1);

   Token getToken();

   PrivateKey getPrivateKey();

   Key getSecretKey();

   void toXML(XMLOutputStream var1) throws XMLStreamException;

   boolean references(Token var1);

   void setReference(String var1);

   X509IssuerSerial getX509IssuerSerial();
}
