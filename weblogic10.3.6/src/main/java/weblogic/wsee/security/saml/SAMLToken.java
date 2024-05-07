package weblogic.wsee.security.saml;

import java.security.cert.X509Certificate;
import javax.security.auth.Subject;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface SAMLToken extends SecurityToken {
   String getAssertionID();

   boolean isHolderOfKey();

   Subject getSubject();

   void setSubject(Subject var1);

   X509Certificate getHolderOfCert();

   boolean isSaml2();
}
