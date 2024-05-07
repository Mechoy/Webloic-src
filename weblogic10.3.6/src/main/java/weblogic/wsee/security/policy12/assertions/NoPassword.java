package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class NoPassword extends QNameAssertion {
   public static final String NO_PASSWORD = "NoPassword";

   public QName getName() {
      return new QName(this.getNamespace(), "NoPassword", "sp");
   }
}
