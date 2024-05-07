package weblogic.xml.crypto.wss.provider;

import javax.xml.namespace.QName;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.X509IssuerSerial;
import weblogic.xml.crypto.wss.api.KeyIdentifier;
import weblogic.xml.dom.marshal.WLDOMStructure;

/** @deprecated */
public interface SecurityTokenReference extends WLDOMStructure, XMLStructure {
   String getId();

   void setId(String var1);

   KeyIdentifier getKeyIdentifier();

   void setKeyIdentifier(KeyIdentifier var1);

   String getReferenceURI();

   void setReferenceURI(String var1);

   SecurityToken getSecurityToken();

   String getValueType();

   void setValueType(String var1);

   QName getSTRType();

   void setSTRType(QName var1);

   X509IssuerSerial getIssuerSerial();
}
