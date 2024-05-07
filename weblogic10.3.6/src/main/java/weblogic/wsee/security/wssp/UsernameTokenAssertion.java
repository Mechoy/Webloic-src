package weblogic.wsee.security.wssp;

public interface UsernameTokenAssertion extends TokenAssertion {
   boolean noPasswordRequried();

   boolean isHashPasswordRequired();

   boolean isCreatedRequired();

   boolean isNonceRequired();

   TokenType getUsernameTokenType();

   public static enum TokenType {
      WSS_UT_10,
      WSS_UT_11;
   }
}
