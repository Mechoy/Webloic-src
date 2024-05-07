package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Created extends QNameAssertion {
   public static final String CREATED = "Created";

   public QName getName() {
      return new QName(this.getNamespace(), "Created", "sp13");
   }
}
