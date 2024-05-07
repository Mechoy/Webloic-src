package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.RecipientEncryptionToken;
import weblogic.wsee.security.wssp.RecipientEncryptionTokenAssertion;

public class RecipientEncryptionTokenAssertionImpl extends RecipientTokenAssertionImpl implements RecipientEncryptionTokenAssertion {
   RecipientEncryptionTokenAssertionImpl(RecipientEncryptionToken var1) {
      super(var1);
   }
}
