package weblogic.wsee.security.wssp;

import weblogic.wsee.security.policy12.assertions.RequestSecurityTokenTemplate;

public interface IssuedTokenAssertion extends TokenAssertion {
   String getIssuerString();

   RequestSecurityTokenTemplate getRequestSecurityTokenTemplate();

   String getIssuedTokenType();

   String getDkTokenType();

   boolean hasRequestSecurityTokenTemplate();
}
