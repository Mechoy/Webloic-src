package weblogic.xml.crypto.wss.api;

import java.security.cert.X509Certificate;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityToken;

public interface BinarySecurityToken extends SecurityToken {
   String getEncodedValue() throws WSSecurityException;

   byte[] getDecodedValue() throws WSSecurityException;

   String getEncodingType();

   boolean isValidated();

   void setValidated(boolean var1);

   X509Certificate getCertificate();
}
