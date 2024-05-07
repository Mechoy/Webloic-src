package weblogic.xml.crypto.wss;

import java.io.Serializable;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.utils.io.UnsyncByteArrayInputStream;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.wss.api.BinarySecurityToken;
import weblogic.xml.security.utils.Utils;

public class X509CertPath extends X509V3BSTType implements Serializable {
   private static final long serialVersionUID = 2196170009038509190L;
   public static final String PKI_PATH = "PkiPath";
   public static final String PKCS7_PATH = "PKCS7";
   private static final X509CertPath pkiPath = new X509CertPath("PkiPath", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1");
   private static final X509CertPath pkcs7 = new X509CertPath("PKCS7", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7");
   private String encoding;
   private String uri;

   private X509CertPath(String var1, String var2) {
      this.encoding = var1;
      this.uri = var2;
   }

   public static X509CertPath getPKIPath() {
      return pkiPath;
   }

   public static X509CertPath getPKCS7() {
      return pkcs7;
   }

   public String getValueType() {
      return this.uri;
   }

   public byte[] getUnencodedValue(Object var1) throws BSTEncodingException {
      CertPath var2 = this.getCertPath(var1);

      try {
         return var2.getEncoded(this.encoding);
      } catch (CertificateEncodingException var4) {
         throw new BSTEncodingException("Failed to get bytes from certificate path.", var4);
      }
   }

   public Object getCredentials(byte[] var1) {
      CertPath var2 = this.getCertPath(var1);
      return new X509Credential(var2);
   }

   private CertPath getCertPath(Object var1) {
      return ((X509Credential)var1).getCertPath();
   }

   private CertPath getCertPath(byte[] var1) {
      CertPath var2 = null;
      UnsyncByteArrayInputStream var3 = new UnsyncByteArrayInputStream(var1);

      try {
         var2 = Utils.getCertFactory().generateCertPath(var3, this.encoding);
         return var2;
      } catch (CertificateException var5) {
         throw new AssertionError("Unable to decode certificate path: " + var5);
      }
   }

   public boolean validate(BinarySecurityToken var1, MessageContext var2) {
      return CertUtils.validateCertPath(((X509Credential)var1.getCredential()).getCertPath());
   }
}
