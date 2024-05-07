package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SignedParts extends QNameParts {
   public static final String SIGNED_PARTS = "SignedParts";

   public QName getName() {
      return new QName(this.getNamespace(), "SignedParts", "sp");
   }
}
