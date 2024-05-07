package weblogic.wsee.security.bst;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import weblogic.wsee.security.util.CertUtils;

/** @deprecated */
public class ClientBST11Pkcs7CredentialProvider extends ClientBSTCredentialProvider {
   public static final String[] BUILTIN_BST_VALUETYPES = new String[]{"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7"};

   /** @deprecated */
   public ClientBST11Pkcs7CredentialProvider(String var1, String var2) throws Exception {
      super((X509Certificate)CertUtils.getCertificate(var1), (PrivateKey)CertUtils.getPKCS8PrivateKey(var2), (X509Certificate)null);
   }

   /** @deprecated */
   public String[] getValueTypes() {
      return BUILTIN_BST_VALUETYPES;
   }
}
