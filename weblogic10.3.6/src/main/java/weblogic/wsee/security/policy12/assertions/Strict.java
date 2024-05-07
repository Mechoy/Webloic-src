package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Strict extends QNameAssertion {
   public static final String STRICT = "Strict";

   public QName getName() {
      return new QName(this.getNamespace(), "Strict", "sp");
   }
}
