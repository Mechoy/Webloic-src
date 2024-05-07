package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Trust10 extends NestedSecurityPolicy12Assertion {
   public static final String TRUST_10 = "Trust10";

   public QName getName() {
      return new QName(this.getNamespace(), "Trust10", "sp");
   }

   public MustSupportClientChallenge getMustSupportClientChallenge() {
      return (MustSupportClientChallenge)this.getNestedAssertion(MustSupportClientChallenge.class);
   }

   public MustSupportServerChallenge getMustSupportServerChallenge() {
      return (MustSupportServerChallenge)this.getNestedAssertion(MustSupportServerChallenge.class);
   }

   public RequireClientEntropy getRequireClientEntropy() {
      return (RequireClientEntropy)this.getNestedAssertion(RequireClientEntropy.class);
   }

   public RequireServerEntropy getRequireServerEntropy() {
      return (RequireServerEntropy)this.getNestedAssertion(RequireServerEntropy.class);
   }

   public MustSupportIssuedTokens getMustSupportIssuedTokens() {
      return (MustSupportIssuedTokens)this.getNestedAssertion(MustSupportIssuedTokens.class);
   }
}
