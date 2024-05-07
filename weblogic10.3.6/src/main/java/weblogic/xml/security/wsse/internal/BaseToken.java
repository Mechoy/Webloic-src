package weblogic.xml.security.wsse.internal;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import weblogic.xml.security.wsse.KeyIdentifier;
import weblogic.xml.security.wsse.SecurityTokenReference;
import weblogic.xml.security.wsse.Token;
import weblogic.xml.security.wsse.v200207.SecurityTokenReferenceImpl;

public abstract class BaseToken implements Token {
   public SecurityTokenReference getSecurityTokenReference() {
      return new SecurityTokenReferenceImpl(this);
   }

   public KeyIdentifier getKeyIdentifier() {
      return null;
   }

   public PublicKey getPublicKey() {
      return null;
   }

   public PrivateKey getPrivateKey() {
      return null;
   }

   public X509Certificate getCertificate() {
      return null;
   }

   public X509Certificate[] getCertificateChain() {
      return null;
   }

   public CertPath getCertPath() {
      return null;
   }

   public Key getSecretKey() {
      return null;
   }
}
