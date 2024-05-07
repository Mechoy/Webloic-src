package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.Token;
import weblogic.wsee.security.wssp.SecurityContextTokenAssertion;

public abstract class SecurityContextTokenAssertionImpl extends TokenAssertionImpl implements SecurityContextTokenAssertion {
   SecurityContextTokenAssertionImpl(Token var1) {
      super(var1);
   }

   public boolean isExternalUriReferenceRequired() {
      return false;
   }
}
