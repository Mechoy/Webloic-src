package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.EncryptionToken;
import weblogic.wsee.security.wssp.EncryptionTokenAssertion;

public class EncryptionTokenAssertionImpl extends ProtectionTokenAssertionImpl implements EncryptionTokenAssertion {
   EncryptionTokenAssertionImpl(EncryptionToken var1) {
      super(var1);
   }
}
