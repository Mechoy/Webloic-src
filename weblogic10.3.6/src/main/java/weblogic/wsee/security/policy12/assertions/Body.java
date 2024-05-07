package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Body extends QNameAssertion {
   public static final String BODY = "Body";

   public QName getName() {
      return new QName(this.getNamespace(), "Body", "sp");
   }
}
