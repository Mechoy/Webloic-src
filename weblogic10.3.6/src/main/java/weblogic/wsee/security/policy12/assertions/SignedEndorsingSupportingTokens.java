package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SignedEndorsingSupportingTokens extends AbstractSupportingTokens {
   public static final String SIGNED_ENDORISNG_SUPPORTING_TOKENS = "SignedEndorsingSupportingTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "SignedEndorsingSupportingTokens", "sp");
   }
}
