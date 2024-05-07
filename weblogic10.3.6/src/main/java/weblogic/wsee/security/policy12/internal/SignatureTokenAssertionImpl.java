package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.SignatureToken;
import weblogic.wsee.security.wssp.SignatureTokenAssertion;

public class SignatureTokenAssertionImpl extends ProtectionTokenAssertionImpl implements SignatureTokenAssertion {
   SignatureTokenAssertionImpl(SignatureToken var1) {
      super(var1);
   }
}
