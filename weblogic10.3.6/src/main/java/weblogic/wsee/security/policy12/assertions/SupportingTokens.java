package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SupportingTokens extends AbstractSupportingTokens {
   public static final String SUPPORTING_TOKENS = "SupportingTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "SupportingTokens", "sp");
   }
}
