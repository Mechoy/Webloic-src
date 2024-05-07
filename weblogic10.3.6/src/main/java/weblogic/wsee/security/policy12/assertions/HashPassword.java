package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class HashPassword extends QNameAssertion {
   public static final String HASH_PASSWORD = "HashPassword";

   public QName getName() {
      return new QName(this.getNamespace(), "HashPassword", "sp");
   }
}
