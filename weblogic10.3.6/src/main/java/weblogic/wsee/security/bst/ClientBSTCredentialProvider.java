package weblogic.wsee.security.bst;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.security.util.CertUtils;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ClientBSTCredentialProvider extends BST11CredentialProvider implements Serializable {
   private X509Credential clientCredential;
   private X509Credential serverPublicCert;

   public ClientBSTCredentialProvider(String var1, String var2, String var3, String var4) throws Exception {
      this(var1, var2, var3, var4, "JKS", (X509Certificate)null);
   }

   public ClientBSTCredentialProvider(String var1, String var2, String var3, String var4, String var5) throws Exception {
      this(var1, var2, var3, var4, var5, (X509Certificate)null);
   }

   public ClientBSTCredentialProvider(String var1, String var2, String var3, String var4, String var5, X509Certificate var6) throws Exception {
      this((X509Certificate)CertUtils.getCertificate(var1, var2, var3, var5).get(0), CertUtils.getPrivateKey(var3, var4, var1, var5, var2), var6);
   }

   public ClientBSTCredentialProvider(String var1, String var2) throws Exception {
      this((X509Certificate)CertUtils.getCertificate(var1), (PrivateKey)CertUtils.getPKCS8PrivateKey(var2), (X509Certificate)null);
   }

   public ClientBSTCredentialProvider(String var1, String var2, String var3) throws Exception {
      this(CertUtils.getCertificate(var1), CertUtils.getPKCS8PrivateKey(var2), CertUtils.getCertificate(var3));
   }

   public ClientBSTCredentialProvider(X509Certificate var1, PrivateKey var2, X509Certificate var3) {
      this.clientCredential = new X509Credential(var1, var2);
      if (var3 != null) {
         this.serverPublicCert = new X509Credential(var3);
      }

   }

   public ClientBSTCredentialProvider(CertPath var1, PrivateKey var2) {
      this.clientCredential = new X509Credential(var1, var2);
   }

   public ClientBSTCredentialProvider(X509Certificate var1, CertPath var2, PrivateKey var3) {
      this.clientCredential = new X509Credential(var2, var3);
      if (var1 != null) {
         this.serverPublicCert = new X509Credential(var1);
      }

   }

   public void setServerCertificate(X509Certificate var1) {
      if (var1 != null) {
         this.serverPublicCert = new X509Credential(var1);
      } else {
         this.serverPublicCert = null;
      }

   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (this.serverPublicCert != null && (var4 == null || isForEncryption(var4) || isForVerification(var4)) && BSTUtils.matches(this.serverPublicCert, var3)) {
         return this.serverPublicCert;
      } else {
         return this.clientCredential != null && (var4 == null || isForIdentity(var4) || isForSigning(var4) || isForResponseEncryption(var4) || isForDecryption(var4)) && (var2 == null || this.clientCredential.getCertificate().getIssuerX500Principal().getName().equals(var2)) && BSTUtils.matches(this.clientCredential, var3) ? this.clientCredential : null;
      }
   }

   public ClientBSTCredentialProvider cloneAndReplaceServerCert(X509Certificate var1) {
      ClientBSTCredentialProvider var2 = null;

      try {
         var2 = (ClientBSTCredentialProvider)this.clone();
      } catch (CloneNotSupportedException var4) {
         if (this.clientCredential != null) {
            var2 = new ClientBSTCredentialProvider(this.clientCredential.getCertificate(), this.clientCredential.getPrivateKey(), var1);
         } else {
            var2 = new ClientBSTCredentialProvider((X509Certificate)null, (PrivateKey)null, var1);
         }
      }

      var2.setServerCertificate(var1);
      return var2;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[ClientBSTCredentialProvider: clientCred=");
      var1.append(this.clientCredential != null && this.clientCredential.getCertificate() != null ? this.clientCredential.getCertificate().getSubjectDN() : "none");
      var1.append(" serverCert=");
      var1.append(this.serverPublicCert != null && this.serverPublicCert.getCertificate() != null ? this.serverPublicCert.getCertificate().getSubjectDN() : "none");
      var1.append("]");
      return var1.toString();
   }
}
