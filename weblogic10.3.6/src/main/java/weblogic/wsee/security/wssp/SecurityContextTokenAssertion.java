package weblogic.wsee.security.wssp;

import weblogic.wsee.policy.framework.NormalizedExpression;

public interface SecurityContextTokenAssertion extends TokenAssertion {
   boolean isExternalUriReferenceRequired();

   boolean isSC200502SecurityContextToken();

   NormalizedExpression getNormalizedBootstrapPolicy();

   String getSctTokenType();

   String getDkTokenType();

   String[] getTokenType();

   boolean isWSSC13SecurityContextToken();
}
