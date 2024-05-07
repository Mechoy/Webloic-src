package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Lax extends QNameAssertion {
   public static final String LAX = "Lax";

   public QName getName() {
      return new QName(this.getNamespace(), "Lax", "sp");
   }
}
