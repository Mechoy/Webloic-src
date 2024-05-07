package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.InitiatorEncryptionToken;
import weblogic.wsee.security.wssp.InitiatorEncryptionTokenAssertion;

public class InitiatorEncryptionTokenAssertionImpl extends InitiatorTokenAssertionImpl implements InitiatorEncryptionTokenAssertion {
   InitiatorEncryptionTokenAssertionImpl(InitiatorEncryptionToken var1) {
      super(var1);
   }
}
