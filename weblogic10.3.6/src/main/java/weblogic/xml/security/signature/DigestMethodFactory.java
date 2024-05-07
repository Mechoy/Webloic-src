package weblogic.xml.security.signature;

import weblogic.xml.security.utils.XMLSecurityException;

public interface DigestMethodFactory {
   String getURI();

   DigestMethod newDigestMethod() throws XMLSecurityException;
}
