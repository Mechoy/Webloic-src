package weblogic.wsee.security.policy12.assertions;

public abstract class SecurityBinding extends NestedSecurityPolicy12Assertion {
   public AlgorithmSuite getAlgorithmSuite() {
      return (AlgorithmSuite)this.getNestedAssertion(AlgorithmSuite.class);
   }

   public Layout getLayout() {
      return (Layout)this.getNestedAssertion(Layout.class);
   }

   public IncludeTimestamp getIncludeTimestamp() {
      return (IncludeTimestamp)this.getNestedAssertion(IncludeTimestamp.class);
   }

   public EncryptBeforeSigning getEncryptBeforeSigning() {
      return (EncryptBeforeSigning)this.getNestedAssertion(EncryptBeforeSigning.class);
   }

   public EncryptSignature getEncryptSignature() {
      return (EncryptSignature)this.getNestedAssertion(EncryptSignature.class);
   }

   public ProtectTokens getProtectTokens() {
      return (ProtectTokens)this.getNestedAssertion(ProtectTokens.class);
   }

   public OnlySignEntireHeadersAndBody getOnlySignEntireHeadersAndBody() {
      return (OnlySignEntireHeadersAndBody)this.getNestedAssertion(OnlySignEntireHeadersAndBody.class);
   }
}
