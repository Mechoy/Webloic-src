package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic128 extends QNameAssertion {
   public static final String BASIC_128 = "Basic128";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic128", "sp");
   }
}
