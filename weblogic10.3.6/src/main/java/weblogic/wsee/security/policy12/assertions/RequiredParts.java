package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequiredParts extends QNameParts {
   public static final String REQUIRED_PARTS = "RequiredParts";

   public QName getName() {
      return new QName(this.getNamespace(), "RequiredParts", "sp");
   }
}
