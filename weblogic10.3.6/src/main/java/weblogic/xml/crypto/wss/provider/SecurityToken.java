package weblogic.xml.crypto.wss.provider;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.dom.marshal.WLDOMStructure;

public interface SecurityToken extends WLDOMStructure, XMLStructure {
   String getValueType();

   String getId();

   /** @deprecated */
   void setId(String var1);

   /** @deprecated */
   PrivateKey getPrivateKey();

   /** @deprecated */
   PublicKey getPublicKey();

   /** @deprecated */
   Key getSecretKey();

   /** @deprecated */
   Object getCredential();
}
