package weblogic.wsee.security.wssp;

public interface WsTrustOptions {
   boolean isWst10();

   boolean isMustSupportClientChallenge();

   boolean isMustSupportServerChallenge();

   boolean isClientEntropyRequired();

   boolean isServerEntropyRequired();

   boolean isMustSupportIssuedTokens();

   boolean isWst13();

   boolean isScopePolicy15();

   boolean isMustSupportInteractiveChallenge();
}
