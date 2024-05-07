package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic192 extends QNameAssertion {
   public static final String BASIC_192 = "Basic192";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic192", "sp");
   }
}
