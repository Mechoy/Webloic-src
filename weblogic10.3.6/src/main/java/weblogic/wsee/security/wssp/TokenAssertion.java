package weblogic.wsee.security.wssp;

public interface TokenAssertion {
   String getIssuer();

   String getIssuerName();

   boolean isDerivedKeyOptional();

   void setDerivedKeyOptional(boolean var1);

   boolean isRequireInternalReference();

   boolean isRequireExternalReference();

   TokenInclusion getTokenInclusion();

   boolean requireDerivedKey();

   boolean requireExplicitDerivedKey();

   boolean requireImplicitDerivedKey();

   boolean isOptional();

   void setOptional(boolean var1);

   public static enum TokenInclusion {
      NEVER,
      ONCE,
      TO_RECIPIENT_ONLY,
      TO_INITIATOR_ONLY,
      ALWAYS;
   }
}
