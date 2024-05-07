package weblogic.wsee.security.wss.policy;

import java.util.List;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;

public interface IdentityPolicy {
   void addIdentityToken(SecurityTokenType var1);

   void addIdentityToken(SecurityToken var1);

   List getValidIdentityTokens();

   boolean isAuthenticationRequired();
}
