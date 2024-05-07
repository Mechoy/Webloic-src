package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SignedSupportingTokens extends AbstractSupportingTokens {
   public static final String SIGNED_SUPPORTING_TOKENS = "SignedSupportingTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "SignedSupportingTokens", "sp");
   }
}
