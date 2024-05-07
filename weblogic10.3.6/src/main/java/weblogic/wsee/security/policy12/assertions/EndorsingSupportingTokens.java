package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class EndorsingSupportingTokens extends AbstractSupportingTokens {
   public static final String ENDORSING_SUPPORTING_TOKENS = "EndorsingSupportingTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "EndorsingSupportingTokens", "sp");
   }
}
