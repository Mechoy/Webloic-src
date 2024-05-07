package weblogic.wsee.security.saml;

import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.w3c.dom.Element;
import weblogic.xml.crypto.common.keyinfo.EncryptedKeyProvider;

public interface SAMLCredential {
   String getTokenType();

   String getVersion();

   boolean isSaml2();

   void setCredential(Object var1);

   Object getCredential();

   void setPrivateKey(PrivateKey var1);

   PrivateKey getPrivateKey();

   String getAssertionID();

   boolean isHolderOfKey();

   Key getHolderOfKey();

   X509Certificate getX509Cert();

   SAMLAttributeStatementData getAttributes();

   void setAttributes(SAMLAttributeStatementData var1);

   Key getSymmetircKey();

   void setSymmetircKey(Key var1);

   Element getEncryptedKey();

   EncryptedKeyProvider getEncryptedKeyProvider();

   void setEncryptedKeyProvider(EncryptedKeyProvider var1);
}
