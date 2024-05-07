package weblogic.wsee.security.policy12.assertions;

public abstract class AbstractSupportingTokens extends NestedSecurityPolicy12Assertion {
   private static final long serialVersionUID = -3584708108247966952L;

   public UsernameToken getUsernameToken() {
      return (UsernameToken)this.getNestedAssertion(UsernameToken.class);
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

   public SecureConversationToken getSecureConversationToken() {
      return (SecureConversationToken)this.getNestedAssertion(SecureConversationToken.class);
   }

   public IssuedToken getIssuedToken() {
      return (IssuedToken)this.getNestedAssertion(IssuedToken.class);
   }

   public SignedParts getSignedParts() {
      return (SignedParts)this.getNestedAssertion(SignedParts.class);
   }

   public SignedElements getSignedElements() {
      return (SignedElements)this.getNestedAssertion(SignedElements.class);
   }

   public EncryptedParts getEncryptedParts() {
      return (EncryptedParts)this.getNestedAssertion(EncryptedParts.class);
   }

   public EncryptedElements getEncryptedElements() {
      return (EncryptedElements)this.getNestedAssertion(EncryptedElements.class);
   }
}
