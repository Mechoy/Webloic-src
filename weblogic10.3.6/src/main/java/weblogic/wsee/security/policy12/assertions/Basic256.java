package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic256 extends QNameAssertion {
   public static final String BASIC_256 = "Basic256";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic256", "sp");
   }
}
