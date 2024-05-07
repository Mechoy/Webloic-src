package weblogic.wsee.security.serviceref;

import java.security.AccessController;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import weblogic.security.KeyPairCredential;
import weblogic.security.PublicCertCredential;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.security.bst.BSTCredentialProvider;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ServiceRefBSTCredProv extends BSTCredentialProvider {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      Object var5 = null;
      if (!isForVerification(var4) && !isForEncryption(var4)) {
         if (var4 == null || isForIdentity(var4) || isForSigning(var4) || isForResponseEncryption(var4) || isForDecryption(var4)) {
            var5 = ServiceRefUtils.getCredential(kernelID, "weblogic.pki.Keypair", var2, var3);
            if (var5 != null) {
               KeyPairCredential var7 = (KeyPairCredential)var5;
               return new X509Credential((X509Certificate)var7.getCertificate(), (PrivateKey)var7.getKey());
            }
         }
      } else {
         var5 = ServiceRefUtils.getCredential(kernelID, "weblogic.pki.TrustedCertificate", var2, var3);
         if (var5 != null) {
            PublicCertCredential var6 = (PublicCertCredential)var5;
            return new X509Credential((X509Certificate)var6.getCertificate());
         }
      }

      return null;
   }
}
