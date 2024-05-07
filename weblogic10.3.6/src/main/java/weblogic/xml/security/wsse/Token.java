package weblogic.xml.security.wsse;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;

/** @deprecated */
public interface Token {
   String getId();

   SecurityTokenReference getSecurityTokenReference();

   KeyIdentifier getKeyIdentifier();

   PublicKey getPublicKey();

   PrivateKey getPrivateKey();

   X509Certificate getCertificate();

   X509Certificate[] getCertificateChain();

   CertPath getCertPath();

   Key getSecretKey();
}
