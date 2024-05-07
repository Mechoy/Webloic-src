package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.AsymmetricToken;
import weblogic.wsee.security.policy12.assertions.SamlToken;
import weblogic.wsee.security.policy12.assertions.X509Token;
import weblogic.wsee.security.wssp.InitiatorTokenAssertion;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;

public class InitiatorTokenAssertionImpl implements InitiatorTokenAssertion {
   X509TokenAssertion x509Asst;
   SamlTokenAssertionImpl samlTokenAsst;

   InitiatorTokenAssertionImpl(AsymmetricToken var1) {
      X509Token var2 = var1.getX509Token();
      if (var2 != null) {
         this.x509Asst = new X509TokenAssertionImpl(var2);
      }

      SamlToken var3 = var1.getSamlToken();
      if (var3 != null) {
         this.samlTokenAsst = new SamlTokenAssertionImpl(var3);
         this.samlTokenAsst.setConfirmationMethodHolderOfKey();
      }

   }

   public X509TokenAssertion getX509TokenAssertion() {
      return this.x509Asst;
   }

   public SamlTokenAssertion getSamlTokenAssertion() {
      return this.samlTokenAsst;
   }
}
