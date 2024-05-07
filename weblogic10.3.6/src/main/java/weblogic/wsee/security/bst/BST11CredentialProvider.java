package weblogic.wsee.security.bst;

/** @deprecated */
public abstract class BST11CredentialProvider extends BSTCredentialProvider {
   /** @deprecated */
   public static final String[] BUILTIN_BST_VALUETYPES = new String[]{"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v1", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#PKCS7", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier", "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1"};

   /** @deprecated */
   public String[] getValueTypes() {
      return BUILTIN_BST_VALUETYPES;
   }
}
