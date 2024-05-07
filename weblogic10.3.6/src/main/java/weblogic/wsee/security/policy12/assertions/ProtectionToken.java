package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class ProtectionToken extends NestedSecurityPolicy12Assertion {
   public static final String PROTECTION_TOKEN = "ProtectionToken";

   public QName getName() {
      return new QName(this.getNamespace(), "ProtectionToken", "sp");
   }

   public SecureConversationToken getSecureConversationToken() {
      return (SecureConversationToken)this.getNestedAssertion(SecureConversationToken.class);
   }

   public X509Token getX509Token() {
      return (X509Token)this.getNestedAssertion(X509Token.class);
   }

   public KerberosToken getKerberosToken() {
      return (KerberosToken)this.getNestedAssertion(KerberosToken.class);
   }

   public SamlToken getSamlToken() {
      return (SamlToken)this.getNestedAssertion(SamlToken.class);
   }
}
