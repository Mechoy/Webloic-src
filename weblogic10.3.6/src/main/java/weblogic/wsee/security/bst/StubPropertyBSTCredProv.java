package weblogic.wsee.security.bst;

import java.security.cert.X509Certificate;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.Purpose;

/** @deprecated */
public class StubPropertyBSTCredProv extends BSTCredentialProvider {
   /** @deprecated */
   public static final String SERVER_VERIFY_CERT = "weblogic.wsee.security.bst.serverVerifyCert";
   /** @deprecated */
   public static final String SERVER_ENCRYPT_CERT = "weblogic.wsee.security.bst.serverEncryptCert";
   private X509Credential serverVerifyCred = null;
   private X509Credential serverEncryptCred = null;

   public StubPropertyBSTCredProv(X509Certificate var1, X509Certificate var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Server encrypt certificate can't be null.");
      } else {
         this.serverEncryptCred = new X509Credential(var1);
         this.serverVerifyCred = new X509Credential(var2);
      }
   }

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (isForEncryption(var4) && BSTUtils.matches(this.serverEncryptCred, var3)) {
         return this.serverEncryptCred;
      } else {
         return this.serverVerifyCred != null && isForVerification(var4) && BSTUtils.matches(this.serverVerifyCred, var3) ? this.serverVerifyCred : null;
      }
   }

   public StubPropertyBSTCredProv cloneAndReplaceServerCert(X509Certificate var1) {
      return this.serverVerifyCred != null ? new StubPropertyBSTCredProv(var1, this.serverVerifyCred.getCertificate()) : new StubPropertyBSTCredProv(var1, (X509Certificate)null);
   }
}
