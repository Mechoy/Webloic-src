package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.AsymmetricToken;
import weblogic.wsee.security.wssp.RecipientTokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;

public class RecipientTokenAssertionImpl implements RecipientTokenAssertion {
   X509TokenAssertionImpl x509Asst;

   RecipientTokenAssertionImpl(AsymmetricToken var1) {
      this.x509Asst = new X509TokenAssertionImpl(var1.getX509Token());
   }

   public X509TokenAssertion getX509TokenAssertion() {
      return this.x509Asst;
   }
}
