package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Nonce extends QNameAssertion {
   public static final String NONCE = "Nonce";

   public QName getName() {
      return new QName(this.getNamespace(), "Nonce", "sp13");
   }
}
