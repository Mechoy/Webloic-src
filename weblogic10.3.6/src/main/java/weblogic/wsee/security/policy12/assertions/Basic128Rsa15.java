package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic128Rsa15 extends QNameAssertion {
   public static final String BASIC_128_RSA_15 = "Basic128Rsa15";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic128Rsa15", "sp");
   }
}
