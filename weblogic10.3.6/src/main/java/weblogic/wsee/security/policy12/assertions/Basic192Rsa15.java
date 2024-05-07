package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic192Rsa15 extends QNameAssertion {
   public static final String BASIC_192_RSA_15 = "Basic192Rsa15";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic192Rsa15", "sp");
   }
}
