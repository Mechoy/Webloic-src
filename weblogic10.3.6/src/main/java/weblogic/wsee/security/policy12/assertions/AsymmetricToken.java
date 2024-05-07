package weblogic.wsee.security.policy12.assertions;

public abstract class AsymmetricToken extends NestedSecurityPolicy12Assertion {
   public X509Token getX509Token() {
      return (X509Token)this.getNestedAssertion(X509Token.class);
   }

   public SamlToken getSamlToken() {
      return (SamlToken)this.getNestedAssertion(SamlToken.class);
   }
}
