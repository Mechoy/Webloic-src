package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class ProtectTokens extends QNameAssertion {
   public static final String PROTECT_TOKENS = "ProtectTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "ProtectTokens", "sp");
   }
}
