package weblogic.xml.crypto.wss;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import javax.xml.rpc.handler.MessageContext;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.crypto.wss.api.BinarySecurityTokenType;
import weblogic.xml.security.utils.Utils;

public class X509V3BSTType implements BinarySecurityTokenType, Serializable {
   private static final long serialVersionUID = 1441392986715203146L;

   public static void init() {
   }

   public String getValueType() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
   }

   public String getKeyIdentifierValueType() {
      return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier";
   }

   public byte[] getUnencodedValue(Object var1) throws BSTEncodingException {
      X509Certificate var2 = this.getCertificate(var1);

      try {
         return var2.getEncoded();
      } catch (CertificateEncodingException var4) {
         throw new BSTEncodingException("Failed to get bytes from certificate.", var4);
      }
   }

   public byte[] getKeyIdRefValue(Object var1) throws BSTEncodingException {
      return var1 instanceof BinarySecurityToken ? Utils.getSubjectKeyIdentifier(((BinarySecurityToken)var1).getCertificate()) : Utils.getSubjectKeyIdentifier(this.getCertificate(var1));
   }

   public PublicKey getPublicKey(Object var1) {
      return this.getCertificate(var1).getPublicKey();
   }

   public PrivateKey getPrivateKey(Object var1) {
      return ((X509Credential)var1).getPrivateKey();
   }

   public SecretKey getSecretKey(Object var1) {
      return null;
   }

   public X509Certificate getCertificate(Object var1) {
      return ((X509Credential)var1).getCertificate();
   }

   public Object getCredentials(byte[] var1) {
      X509Certificate var2 = this.getCertificate(var1);
      return new X509Credential(var2, (PrivateKey)null);
   }

   public boolean validate(BinarySecurityToken var1, MessageContext var2) {
      return CertUtils.validateCertificate(var1.getCertificate());
   }

   public PublicKey getPublicKey(byte[] var1) {
      X509Certificate var2 = this.getCertificate(var1);
      PublicKey var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = var2.getPublicKey();
      }

      return var3;
   }

   public PrivateKey getPrivateKey(byte[] var1) {
      return null;
   }

   public SecretKey getSecretKey(byte[] var1) {
      return null;
   }

   public X509Certificate getCertificate(byte[] var1) {
      X509Certificate var2 = null;
      UnsyncByteArrayInputStream var3 = new UnsyncByteArrayInputStream(var1);

      try {
         var2 = (X509Certificate)Utils.getCertFactory().generateCertificate(var3);
         return var2;
      } catch (CertificateException var5) {
         throw new AssertionError("Unable to decode certificate: " + var5);
      }
   }
}
